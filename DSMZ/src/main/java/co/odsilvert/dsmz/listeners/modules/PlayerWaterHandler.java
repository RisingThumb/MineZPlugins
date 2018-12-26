package co.odsilvert.dsmz.listeners.modules;

import com.google.inject.Singleton;
import org.bukkit.entity.Player;

import java.util.ArrayList;

@Singleton
public class PlayerWaterHandler {

    private ArrayList<Player> dehydrating = new ArrayList<>();

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

    public ArrayList<Player> getDehydratingPlayers() {
        return dehydrating;
    }
}
