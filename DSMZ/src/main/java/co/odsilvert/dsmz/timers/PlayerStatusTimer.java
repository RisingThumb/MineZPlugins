package co.odsilvert.dsmz.timers;

import co.odsilvert.dsmz.listeners.modules.PlayerStatusHandler;
import com.google.inject.Inject;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class PlayerStatusTimer implements Runnable {

    @Inject
    private PlayerStatusHandler playerStatusHandler;

    @Override
    public void run() {
		ArrayList<Player> bleedingPlayers = playerStatusHandler.getBleedingPlayers();
		ArrayList<Player> infectedPlayers = playerStatusHandler.getInfectedPlayers();
		
		for (Player player : bleedingPlayers) {
			player.damage(1);

			player.sendMessage(ChatColor.DARK_RED + "You are bleeding out!");
		}
		
		for (Player player : infectedPlayers) {
			player.damage(1);

			player.sendMessage(ChatColor.DARK_RED + "You are infected!");
		}
    }
}
