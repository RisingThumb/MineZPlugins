package co.odsilvert.dsmz.timers;

import co.odsilvert.dsmz.listeners.modules.PlayerWaterHandler;
import com.google.inject.Inject;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class PlayerDehydrationTimer implements Runnable {

    @Inject
    private PlayerWaterHandler playerWaterHandler;

    @Override
    public void run() {
		ArrayList<Player> dehydratingPlayers = playerWaterHandler.getDehydratingPlayers();

		for (Player player : dehydratingPlayers) {
			player.damage(2);

			player.sendMessage(ChatColor.DARK_RED + "You are dying of dehydration!");
		}
    }
}
