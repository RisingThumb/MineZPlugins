package co.odsilvert.dsmz.listeners.modules.legendaries;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import co.odsilvert.dsmz.listeners.modules.PlayerStatusHandler;
import co.odsilvert.dsmz.main.DSMZ;

@Singleton
public class LoneSword {
	@Inject private DSMZ plugin;

	@Inject PlayerStatusHandler playerStatusHandler;

	private HashMap<UUID, Boolean> loneSwordUsed = new HashMap<>();

	public void action(final Player player) {

		if (!loneSwordUsed.containsKey(player.getUniqueId()) || !loneSwordUsed.get(player.getUniqueId())) {
			ItemStack loneSword = player.getEquipment().getItemInMainHand();

			// Can't set health to a value above 20; throws an exception.
			player.setHealth(player.getHealth() + Math.min(4, 20 - player.getHealth()));
			player.sendMessage(ChatColor.GREEN+"You healed yourself with your lone sword!");

			// Durability is backwards. Might be referencing durability lost instead of durability left.
			loneSword.setDurability((short) (player.getEquipment().getItemInMainHand().getDurability() + 10));
			// Remove lone sword if damage exceeds 250 (iron sword max durability) to prevent getting item with negative durability
			if (loneSword.getDurability() > loneSword.getType().getMaxDurability()) {
				player.getInventory().setItemInMainHand(null);
			}
			loneSwordUsed.put(player.getUniqueId(), true);

			// No point creating the task if it's not going to be run
			BukkitRunnable resetTask = new BukkitRunnable() {
				public void run() {
					loneSwordUsed.put(player.getUniqueId(), false);
				}
			};
			resetTask.runTaskLater(this.plugin, 200L);
		} else {
			player.sendMessage(ChatColor.RED+"You need to wait before using this item again!");
		}
	}
}
