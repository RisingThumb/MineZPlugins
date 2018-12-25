package co.odsilvert.dsmz.timers;

import co.odsilvert.dsmz.listeners.modules.PlayerWaterHandler;
import co.odsilvert.dsmz.main.DSMZ;
import com.google.inject.Inject;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class PlayerWaterTimer implements Runnable {

    @Inject
    private DSMZ plugin;

    @Inject
    private PlayerWaterHandler playerWaterHandler;
//            = plugin.getInjector().getInstance(PlayerWaterHandler.class);

    @Override
	public void run() {
		for (Player player : plugin.getServer().getOnlinePlayers()) {
			int xp = player.getLevel();

			if (xp > 0) {
				playerWaterHandler.setWaterLevel(player, xp - 1);
			}

			switch (xp) {
			case 0:
				playerWaterHandler.setDehydrating(player, true);
				break;
			case 5:
				player.sendMessage(ChatColor.RED + "You're very thirsty");
				break;
			case 10:
				player.sendMessage(ChatColor.YELLOW + "You're thirsty");
				break;
			}

		}
	}
}
