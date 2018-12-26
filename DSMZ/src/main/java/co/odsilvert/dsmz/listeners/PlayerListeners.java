package co.odsilvert.dsmz.listeners;

import co.odsilvert.dsmz.listeners.modules.*;
import co.odsilvert.dsmz.main.DSMZ;

import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.*;

import com.google.inject.Inject;

import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionType;
import org.bukkit.scheduler.BukkitRunnable;

public class PlayerListeners implements Listener {
	
	// Modifiable variables to remove "Magic Numbers"
	final private int bleedRange = 2;
	final private int infectionRange = 2;
	final private int maxWaterLevel = 20;
	

	// Constructor parameter list was getting ridiculous
	@Inject private PotionsRemove potionsRemove;
	@Inject private SugarSpeed sugarSpeed;
	@Inject private KillMessages killMessages;
	@Inject private GrenadeListener grenadeListener;
	@Inject private FlashGrenadeListener flashGrenadeListener;
	@Inject private PlayerWaterHandler playerWaterHandler;
	@Inject private PlayerStatusHandler playerStatusHandler;
	@Inject private BandageItem bandageItem;
	@Inject private DSMZ plugin;
    
	@EventHandler
    public void onPlayerInteract(PlayerInteractEvent event){
    	// All actions for that event would be listed here
		Player player = event.getPlayer();
		ItemStack item = player.getEquipment().getItemInMainHand();

		Action action = event.getAction();
		if ((action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) && event.getHand() == EquipmentSlot.HAND) {
			switch(item.getType()) {
				case SLIME_BALL:
					flashGrenadeListener.action(event);
					break;
				case SUGAR:
					sugarSpeed.action(event);
					break;
				case PAPER:
					bandageItem.action(event);
				default:
					break;
			}
		}
    }

    @EventHandler
    public void onConsume(PlayerItemConsumeEvent event) {
		Player player = event.getPlayer();
		ItemStack item = player.getEquipment().getItemInMainHand();

		switch (item.getType()) {
			case POTION:
				potionsRemove.action(event);
				PotionMeta potion = (PotionMeta)item.getItemMeta();

				if (potion.getBasePotionData().getType().equals(PotionType.WATER)) {
					playerWaterHandler.setWaterLevel(player, maxWaterLevel);
					playerWaterHandler.setDehydrating(player, false);

					player.sendMessage(ChatColor.BLUE + "Ahh, much better");
				}
				break;
            case MILK_BUCKET:
				potionsRemove.action(event);
				playerStatusHandler.setInfected(player, false);
				player.sendMessage(ChatColor.GREEN + "You feel cured");
			case GOLDEN_APPLE:
				// Modify absorption hearts
				break;
				
			
			// Default case to stop eclipse screaming at me
			default:
				break;
		}
    }

    @EventHandler
    public void onPlayerConnect(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        if (player.getLevel() == 0) {
            // Start damage timer after an arbitrary amount of time
        	// Is this really necessary?
            playerWaterHandler.setDehydrating(player, true, 40L);
        }
    }

    @EventHandler
    public void onPlayerDisconnect(PlayerQuitEvent event) {
	    Player player = event.getPlayer();
	    playerStatusHandler.setBleeding(player, false);
        playerWaterHandler.setDehydrating(player, false);
    }
    
    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event) {
    	Entity victim = event.getEntity();
    	Entity damager = event.getDamager();
    	
    	if (damager instanceof Player) {
    		if (victim instanceof Player) {
    			switch (((Player) damager).getEquipment().getItemInMainHand().getType()) {
    				case PAPER:
    					bandageItem.bandageHit((Player) damager, (Player) victim);
    					event.setCancelled(true);
    					return;
    				case SHEARS:
    					bandageItem.shearHit((Player) damager, (Player) victim);
    					event.setCancelled(true);
    					return;
    				default:
    					break;
    			}
    		}
    	} 
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
    	    		System.out.println(random);
    	    		if (random == 1) {
    	    			playerStatusHandler.setInfected(playerHurt, true);
    	    		}
        		}
    		}
    		
    		
    	}
    }

    @EventHandler
	public void onPlayerDeath(PlayerDeathEvent event){
        killMessages.action(event);

        event.setKeepLevel(true);
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
	    Player player = event.getPlayer();
	    BukkitRunnable respawnTask = new BukkitRunnable() {
			public void run() {
				playerWaterHandler.setWaterLevel(player, maxWaterLevel);
			    playerStatusHandler.setInfected(player, false);
			    playerStatusHandler.setBleeding(player, false);
			}
    	};
    	respawnTask.runTaskLater(plugin, 5L);
    }

	@EventHandler
	public void onProjectileHit(ProjectileHitEvent event){
		grenadeListener.explodeAction(event);
	}

	@EventHandler
	public void onTeleport(PlayerTeleportEvent event){
		grenadeListener.teleportAction(event);
	}
		
}
