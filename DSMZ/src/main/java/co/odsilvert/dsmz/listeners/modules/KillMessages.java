package co.odsilvert.dsmz.listeners.modules;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

import com.google.inject.Singleton;

@Singleton
public class KillMessages {
	
	public void action(PlayerDeathEvent event) {
		Player p = event.getEntity().getPlayer();
		Player k = event.getEntity().getKiller();
		ItemStack item = k.getEquipment().getItemInMainHand();
		String itemname = "";
		if (item.hasItemMeta()) {
			itemname = item.getItemMeta().getDisplayName().toUpperCase();
		}
		else {
			itemname = item.getType().toString().replace("_"," ");
		}
		event.setDeathMessage(ChatColor.YELLOW + k.getName() + ChatColor.RED + " MURDERED " + ChatColor.YELLOW + p.getName() + ChatColor.RED + " with a " + ChatColor.YELLOW + itemname +"!");
	}
}
