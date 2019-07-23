package co.odsilvert.dsmz.timers;

import co.odsilvert.dsmz.listeners.modules.PlayerWaterHandler;
import com.google.inject.Inject;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class PlayerDehydrationTimer implements Runnable {

    @Inject
    private PlayerWaterHandler playerWaterHandler;

    @Override
    public void run() {
		ArrayList<Player> dehydratingPlayers = playerWaterHandler.getDehydratingPlayers();

		for (Player player : dehydratingPlayers) {
		    // Disable for creative mode players (prevents chat spam)
		    if (!player.getGameMode().equals(GameMode.CREATIVE)){
                player.damage(2);

                player.sendMessage(ChatColor.DARK_RED + "You are dying of dehydration!");
            }
		}
    }
}
