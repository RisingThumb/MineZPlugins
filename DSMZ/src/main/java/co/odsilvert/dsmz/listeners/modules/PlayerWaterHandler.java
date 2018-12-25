package co.odsilvert.dsmz.listeners.modules;

import co.odsilvert.dsmz.main.DSMZ;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.ArrayList;

@Singleton
public class PlayerWaterHandler {

    @Inject
    private DSMZ plugin;

    private ArrayList<Player> dehydrating = new ArrayList<>();

    public Boolean isDisabled = false;

    public void setWaterLevel(Player player, int level) {
        if (level >= 20) {
            player.setLevel(20);
        } else if (level < 0) {
            player.setLevel(0);
        } else {
            player.setLevel(level);
        }
    }

    public void setDehydrating(Player player, Boolean state) {
        if (state) {
            if (!dehydrating.contains(player)) {
                dehydrating.add(player);
            }
        } else {
            dehydrating.remove(player);
        }
    }

    public void setDehydrating(Player player, Boolean state, long delay) {
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            if (state) {
                if (!dehydrating.contains(player)) {
                    dehydrating.add(player);
                }
            } else {
                dehydrating.remove(player);
            }
        }, delay);
    }

    public ArrayList<Player> getDehydratingPlayers() {
        return dehydrating;
    }
}
