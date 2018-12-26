package co.odsilvert.dsmz.listeners.modules;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.google.inject.Singleton;

@Singleton
public class PlayerStatusHandler {

    private ArrayList<Player> bleeding = new ArrayList<>();
    private ArrayList<Player> infected = new ArrayList<>();

    public void setBleeding(Player player, Boolean state) {
        if (state) {
            if (!bleeding.contains(player)) {
            	bleeding.add(player);
            	player.sendMessage(ChatColor.RED + "You've started bleeding! Bandage your wounds!");
            }
        } else {
        	bleeding.remove(player);
        }
    }

    public ArrayList<Player> getBleedingPlayers() {
        return bleeding;
    }

    public void setInfected(Player player, Boolean state) {
        if (state) {
            if (!infected.contains(player)) {
            	infected.add(player);
            	String message = ChatColor.RED + "You've become infected! Find milk to cure your infection!";
            	
            	player.sendMessage(message);
            }
        } else {
        	infected.remove(player);
        }
    }
    
    public ArrayList<Player> getInfectedPlayers() {
        return infected;
    }
}
