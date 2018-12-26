package co.odsilvert.dsmz.listeners.modules;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;

import com.google.inject.Inject;

public class BandageItem {
	
	@Inject PlayerStatusHandler playerBleedingHandler;
	
	public void action(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		playerBleedingHandler.setBleeding(player, false);
		player.setHealth(player.getHealth()+1);
		player.getInventory().setItemInMainHand(null);
		player.sendMessage("You bandaged yourself.");
	}

}
