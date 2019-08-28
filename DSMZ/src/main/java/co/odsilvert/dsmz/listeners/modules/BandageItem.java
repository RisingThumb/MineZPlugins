package co.odsilvert.dsmz.listeners.modules;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import co.odsilvert.dsmz.main.DSMZ;
import co.odsilvert.dsmz.util.PlayerDataClass;

@Singleton
public class BandageItem {

	// TODO: Add to main plugin configuration file to be editable from outside the code
	private final int bleedRange = 2;
	private final int infectionRange = 2;

	private DSMZ plugin;
	
	@Inject PlayerStatusHandler playerStatusHandler;
	
	@Inject
    public BandageItem(DSMZ plugin) {
        this.plugin = plugin;
	}
	
	private HashMap<Player, PlayerDataClass> healing = new HashMap<Player, PlayerDataClass>();
	private ArrayList<Player> cooldown = new ArrayList<Player>();
	
	public void action(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		playerStatusHandler.setBleeding(player, false);
		player.setHealth(player.getHealth() + Math.min(1, 20 - player.getHealth()));
		player.getInventory().setItemInMainHand(null);
		player.sendMessage(ChatColor.GREEN + "You bandaged yourself.");
	}

//	public void action(EntityDamageByEntityEvent event) {
//    	Entity victim = event.getEntity();
//    	Entity damager = event.getDamager();
//
//
//    	// All code that should be checked for items the damager is using on a player
//    	// Could be extended to legendaries, but that'd probably be best in a completely different class for modularity
//
//    	if (damager instanceof Player) {
//			ItemStack item = ((Player)damager).getEquipment().getItemInMainHand();
//			event.setCancelled(true);
//
//			if (victim instanceof Player) {
//				switch (item.getType()) {
//					case PAPER:
//						bandageHit((Player) damager, (Player) victim);
//						return;
//					case SHEARS:
//						shearHit((Player) damager, (Player) victim);
//						return;
//					case INK_SACK:
//						// Yay! Magic numbers!
//						// 1 = Red dye, 10 = Lime dye
//						if (item.getData().getData() == 1 || item.getData().getData() == 10) {
//							ointmentHit((Player)damager, (Player)victim, item.getData().getData());
//						}
//						return;
//					default:
//						event.setCancelled(false);
//    					break;
//				}
//    		}
//    	}
//
//    	// All bleeding and infection code
//		// TODO: Move bleeding code from PlayerInteractEvent to EntityDamageEvent to allow bleeding from sources other than players
//
//    	if (victim instanceof Player) {
//    		Player playerHurt = (Player) victim;
//    		// Bleeding check
//    		{
//	    		int random = (int )(Math.random() * bleedRange + 1);
//	    		if (random == 1) {
//	    			playerStatusHandler.setBleeding(playerHurt, true);
//	    		}
//    		}
//
//    		if (damager instanceof Zombie) {
//        		// Bleeding check
//        		{
//    	    		int random = (int )(Math.random() * infectionRange + 1);
//    	    		System.out.println(random);
//    	    		if (random == 1) {
//    	    			playerStatusHandler.setInfected(playerHurt, true);
//    	    		}
//        		}
//    		}
//
//
//    	}
//	}
	
	
	
	
	public void bandageHit(final Player healer, Player healTarget) {
		if (!(cooldown.contains(healTarget))) {
			PlayerDataClass healInfo = new PlayerDataClass(healTarget, false, false);
			healing.put(healer, healInfo);
			healer.sendMessage(ChatColor.GREEN+"You begin healing "+healTarget.getName());
			healTarget.sendMessage(ChatColor.GREEN+healTarget.getName()+" begins healing you");
			
			BukkitRunnable healTimeoutTask = new BukkitRunnable() {
				public void run() {
					healing.remove(healer);
				}
	    	};
	    	healTimeoutTask.runTaskLater(plugin, 600L);
		} else {
			healer.sendMessage(ChatColor.RED+"That person has been healed recently");
		}
	}
	
	public void ointmentHit(Player healer, Player healTarget, int ointment) {
		if (!(cooldown.contains(healTarget))) {
			if (healing.containsKey(healer)) {
				PlayerDataClass healInfo = healing.get(healer);
				switch(ointment) {
					case 0:
						healInfo.setGreenOintment(true);
						healer.sendMessage(ChatColor.GREEN+"You applied green ointment");
						break;
					case 1:
						healInfo.setRedOintment(true);
						healer.sendMessage(ChatColor.GREEN+"You applied red ointment");
						break;
						
				}
				healing.put(healer, healInfo);
			}
			else {
				healer.sendMessage(ChatColor.RED+"You need to start by applying the bandage");
			}
		} else {
			healer.sendMessage(ChatColor.RED+"That person has been healed recently");
		}
	}
	
	public void shearHit(Player healer, final Player healTarget) {
		if (healing.containsKey(healer)) {
			
			PlayerDataClass healInfo = healing.get(healer);
			PotionEffect healEffect;
			
			if (healInfo.getRedOintment()) {
				healEffect = new PotionEffect(PotionEffectType.REGENERATION, 200, 1);
			}
			else {
				healEffect = new PotionEffect(PotionEffectType.REGENERATION, 120, 1);
			}
			if (healInfo.getGreenOintment()) {
				playerStatusHandler.setInfected(healTarget, false);
			}
			
			healer.sendMessage(ChatColor.GREEN+"You healed "+healTarget.getName());
			healTarget.sendMessage(ChatColor.GREEN+"You've been healed by "+healer.getName());
			
			healTarget.addPotionEffect(healEffect);
			playerStatusHandler.setBleeding(healTarget, false);
			
			
			healing.remove(healer);
			cooldown.add(healTarget);
			BukkitRunnable cooldownTask = new BukkitRunnable() {
				public void run() {
					cooldown.remove(healTarget);
				}
	    	};
	    	cooldownTask.runTaskLater(plugin, 6000L);
		} else {
			healer.sendMessage(ChatColor.BLUE+ healTarget.getName()+" is at "+Integer.toString((int) healTarget.getHealth())+" health." );
		}
	}
	
	public void bleedingInfection(Entity victim, Entity damager) {
		if (victim instanceof Player) {
    		Player playerHurt = (Player) victim;
    		// Bleeding check
    		{
	    		int random = (int )(Math.random() * bleedRange + 1);
	    		if (random == 1) {
	    			playerStatusHandler.setBleeding(playerHurt, true);
	    		}
    		}
    		
    		if (damager instanceof Zombie) {
        		// Bleeding check
        		{
    	    		int random = (int )(Math.random() * infectionRange + 1);
    	    		if (random == 1) {
    	    			playerStatusHandler.setInfected(playerHurt, true);
    	    		}
        		}
    		}

    	}
	}
}
