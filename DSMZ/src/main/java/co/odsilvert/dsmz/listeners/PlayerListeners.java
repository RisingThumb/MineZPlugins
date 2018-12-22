package co.odsilvert.dsmz.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;

public class PlayerListeners implements Listener {
	
	private PotionsRemove potionsremove = new PotionsRemove();
    private SugarSpeed sugarspeed = new SugarSpeed();
    
    
	@EventHandler(priority=EventPriority.HIGH)
    public void onPlayerInteract(PlayerInteractEvent event){
    	// All actions for that event would be listed here
		sugarspeed.action(event);
    }
	
    @EventHandler
    public void onPotionConsume(PlayerItemConsumeEvent event) {
    	potionsremove.action(event);
    }
}
