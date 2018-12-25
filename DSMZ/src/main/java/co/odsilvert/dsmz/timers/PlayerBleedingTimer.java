package co.odsilvert.dsmz.timers;

import co.odsilvert.dsmz.listeners.modules.PlayerBleedingHandler;
import com.google.inject.Inject;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class PlayerBleedingTimer implements Runnable {

    @Inject
    private PlayerBleedingHandler playerBleedingHandler;

    @Override
    public void run() {
		ArrayList<Player> bleedingPlayers = playerBleedingHandler.getBleedingPlayers();
		
		for (Player player : bleedingPlayers) {
			player.damage(1);

			player.sendMessage(ChatColor.DARK_RED + "You are bleeding out!");
		}
    }
}
