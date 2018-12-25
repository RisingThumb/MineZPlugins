package co.odsilvert.dsmz.listeners.modules;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import com.google.inject.Inject;

import co.odsilvert.dsmz.main.DSMZ;

public class SugarSpeed {
	
	private DSMZ plugin;
	
	private HashMap<UUID, Boolean> sugarUsed = new HashMap<>();
	
	@Inject
    public SugarSpeed(DSMZ plugin) {
        this.plugin = plugin;
	}

	public void speedEffectTriggered(Player player) {
    	
		player.getInventory().setItemInMainHand(null);
		sugarUsed.put(player.getUniqueId(), true);
    	
    	// This is a task for resetting the UUID
    	BukkitRunnable resetTask = new BukkitRunnable() {
			public void run() {
				sugarUsed.put(player.getUniqueId(), false);
			}
    	};
    	
    	// This is a task for giving the player slowness
    	BukkitRunnable slownessTask = new BukkitRunnable() {
    		public void run() {
    	    	PotionEffect slowEffect = new PotionEffect(PotionEffectType.SLOW, 200, 1);
    	    	player.addPotionEffect(slowEffect);
    	    }
    	};
    	
    	
    	PotionEffect speedEffect = new PotionEffect(PotionEffectType.SPEED, 600, 1);
		player.addPotionEffect(speedEffect);
		slownessTask.runTaskLater(this.plugin, 600L);
		resetTask.runTaskLater(this.plugin, 1000L);
    }
    
    public void action(PlayerInteractEvent event) {
    	Player player = event.getPlayer();
    	Action action = event.getAction();
    	UUID UUID = player.getUniqueId();

		if (player.getEquipment().getItemInMainHand().getType() == Material.SUGAR) {
			// If it doesn't exist in the map, add it to the map and set it to true
			if (!(sugarUsed.containsKey(UUID))) {
				speedEffectTriggered(player);
			}
			// Check if the UUID in the map has the object
			else if (!sugarUsed.get(UUID)) {
				speedEffectTriggered(player);
			}
			// If the UUID exists and if it is false, tell the user to wait a while
			else {
				player.sendMessage("You've used a sugar recently, wait a while before using one again.");
			}
		}
    }
}
