package co.odsilvert.dsmz.listeners.modules.legendaries;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import co.odsilvert.dsmz.listeners.modules.PlayerStatusHandler;
import co.odsilvert.dsmz.main.DSMZ;

@Singleton
public class LoneSword {
	private DSMZ plugin;
	
	@Inject PlayerStatusHandler playerStatusHandler;
	
	private HashMap<UUID, Boolean> loneSwordUsed = new HashMap<>();
	
	public void action(PlayerInteractEvent event) {
		final Player player = event.getPlayer();
		
		if (!loneSwordUsed.containsKey(player.getUniqueId())) {
			loneSwordUsed.put(player.getUniqueId(), false);
		}
		String itemName = player.getEquipment().getItemInMainHand().getItemMeta().getDisplayName();
		switch(itemName) {
		case "Lone Sword":
			loneSwordAction(player);
			break;
		default:
			break;
		}
	}
	
	private void loneSwordAction(final Player player) {
		BukkitRunnable resetTask = new BukkitRunnable() {
			public void run() {
				loneSwordUsed.put(player.getUniqueId(), false);
			}
		};
		if (player.getEquipment().getItemInMainHand().getItemMeta().getDisplayName().equals("Lone Sword") && !loneSwordUsed.get(player.getUniqueId())) {
			player.setHealth(player.getHealth()+4);
			player.sendMessage(ChatColor.GREEN+"You healed yourself with your lone sword!");
			player.getEquipment().getItemInMainHand().setDurability((short) (player.getEquipment().getItemInMainHand().getDurability()-10));
			resetTask.runTaskLater(this.plugin, 1200L);
		}
		else {
			player.sendMessage(ChatColor.RED+"You need to wait before using this item again!");
		}
	}
}
