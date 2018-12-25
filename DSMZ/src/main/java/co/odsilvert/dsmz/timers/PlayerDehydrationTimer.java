package co.odsilvert.dsmz.timers;

import co.odsilvert.dsmz.listeners.modules.PlayerWaterHandler;
import co.odsilvert.dsmz.main.DSMZ;
import com.google.inject.Inject;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.ArrayList;

public class PlayerDehydrationTimer implements Runnable {

    @Inject
    private DSMZ plugin;

    @Inject
    private PlayerWaterHandler playerWaterHandler;
//            = plugin.getInjector().getInstance(PlayerWaterHandler.class);

    @Override
    public void run() {
//        System.out.println("Killing players is fun!");
        if (!playerWaterHandler.isDisabled) {
            ArrayList<Player> dehydratingPlayers = playerWaterHandler.getDehydratingPlayers();

            for (Player player : dehydratingPlayers) {
//            System.out.println(player.getName());
                player.damage(1);

                player.sendMessage(ChatColor.DARK_RED + "You are dying of dehydration!");
            }
        }
    }
}
