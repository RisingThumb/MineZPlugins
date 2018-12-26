package co.odsilvert.dsmz.listeners.modules;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import com.google.inject.Inject;

import co.odsilvert.dsmz.main.DSMZ;

public class BandageItem {
	private DSMZ plugin;
	
	@Inject PlayerStatusHandler playerStatusHandler;
	
	@Inject
    public BandageItem(DSMZ plugin) {
        this.plugin = plugin;
	}
	
	@SuppressWarnings("rawtypes")
	private HashMap<Player, ArrayList> healing = new HashMap<>();
	private ArrayList<Player> cooldown = new ArrayList<>();
	
	public void action(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		playerStatusHandler.setBleeding(player, false);
		player.setHealth(player.getHealth()+1);
		player.getInventory().setItemInMainHand(null);
		player.sendMessage(ChatColor.GREEN+"You bandaged yourself.");
	}
	
	public void bandageHit(Player healer, Player healTarget) {
		if (!(cooldown.contains(healTarget))) {
			ArrayList<Object> healInfo = new ArrayList<>();
			healInfo.add(healTarget);
			healing.put(healer, healInfo);
			healer.sendMessage(ChatColor.GREEN+"You begin healing "+healTarget.getName());
			
			BukkitRunnable healTimeoutTask = new BukkitRunnable() {
				public void run() {
					healing.remove(healer);
				}
	    	};
	    	healTimeoutTask.runTaskLater(plugin, 6000L);
		} else {
			healer.sendMessage(ChatColor.RED+"That person has been healed recently");
		}
	}
	
	public void shearHit(Player healer, Player healTarget) {
		if (healing.containsKey(healer)) {
			PotionEffect healEffect;
			healEffect = new PotionEffect(PotionEffectType.REGENERATION, 40, 0);
			
			healer.sendMessage(ChatColor.GREEN+"You healed "+healTarget.getName());
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

}
