package co.odsilvert.dsmz.listeners.modules;

import java.util.ArrayList;

import org.bukkit.entity.Player;

import com.google.inject.Singleton;

@Singleton
public class PlayerBleedingHandler {

    private ArrayList<Player> bleeding = new ArrayList<>();

    public void setBleeding(Player player, Boolean state) {
        if (state) {
            if (!bleeding.contains(player)) {
            	bleeding.add(player);
            	//player.sendMessage("You start bleeding!");
            	//System.out.println(getBleedingPlayers().toString());
            }
        } else {
        	bleeding.remove(player);
        }
    }

    public ArrayList<Player> getBleedingPlayers() {
        return bleeding;
    }
}
