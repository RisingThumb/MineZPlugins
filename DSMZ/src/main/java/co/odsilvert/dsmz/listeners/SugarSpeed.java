package co.odsilvert.dsmz.listeners;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import co.odsilvert.dsmz.main.DSMZ;

public class SugarSpeed {
	
	private DSMZ plugin = DSMZ.getPlugin();
	private HashMap<UUID, Boolean> sugarUsed = new HashMap<>();
	
    @SuppressWarnings("deprecation")
	public void speedEffectTriggered(Player player) {
    	
    	player.setItemInHand(null);
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
		slownessTask.runTaskLater(plugin, 600L);
		resetTask.runTaskLater(plugin, 1000L);
    }
    
    public void action(PlayerInteractEvent event) {
    	Player player = event.getPlayer();
    	Action action = event.getAction();
    	UUID UUID = player.getUniqueId();
    	
    	if ((action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) && event.getHand() == EquipmentSlot.HAND) {

    		if (player.getEquipment().getItemInMainHand().getType() == Material.SUGAR) {
    			// If it doesn't exist in the map, add it to the map and set it to true
    			if (!(sugarUsed.containsKey(UUID))) {
    				speedEffectTriggered(player);
    			}
    			// Check if the UUID in the map has the object
    			else if (sugarUsed.get(UUID) == false) {
    				speedEffectTriggered(player);
    			}
    			// If the UUID exists and if it is false, tell the user to wait a while
    			else {
    				player.sendMessage("You've used a sugar recently, wait a while before using one again.");
    			}
    		}
    	}
    }

}
