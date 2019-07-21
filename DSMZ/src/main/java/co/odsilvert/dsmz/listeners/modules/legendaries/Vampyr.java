package co.odsilvert.dsmz.listeners.modules.legendaries;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import co.odsilvert.dsmz.listeners.modules.PlayerStatusHandler;

@Singleton
public class Vampyr {
	@Inject PlayerStatusHandler playerStatusHandler;

	public void action(final EntityDamageByEntityEvent event) {
		Player player = (Player) event.getDamager();
		player.setHealth(player.getHealth() + Math.min(1, 20 - player.getHealth()));
		player.sendMessage(ChatColor.RED+ "You sucked some blood" );
	}
}
