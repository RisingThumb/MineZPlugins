package co.odsilvert.dsmz.listeners;

import com.google.inject.Inject;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;

public class PlayerListeners implements Listener {

    private PotionsRemove potionsRemove;
    private SugarSpeed sugarSpeed;

    @Inject
    public PlayerListeners(PotionsRemove potionsRemove, SugarSpeed sugarSpeed) {
        this.potionsRemove = potionsRemove;
        this.sugarSpeed = sugarSpeed;
    }


    @EventHandler(priority=EventPriority.HIGH)
    public void onPlayerInteract(PlayerInteractEvent event){
        // All actions for that event would be listed here
        sugarSpeed.action(event);
    }

    @EventHandler
    public void onPotionConsume(PlayerItemConsumeEvent event) {
        potionsRemove.action(event);
    }
}