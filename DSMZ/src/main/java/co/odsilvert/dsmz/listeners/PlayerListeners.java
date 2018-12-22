package co.odsilvert.dsmz.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;

import com.google.inject.Inject;

public class PlayerListeners implements Listener {
	
	private PotionsRemove potionsRemove;
	private SugarSpeed sugarSpeed;
	private KillMessages killMessages;
	
    @Inject
    public PlayerListeners(PotionsRemove potionsRemove, SugarSpeed sugarSpeed, KillMessages killMessages) {
        this.potionsRemove = potionsRemove;
        this.sugarSpeed = sugarSpeed;
        this.killMessages = killMessages;
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
    
    @EventHandler
	public void onPlayerDeath(PlayerDeathEvent event){
    	killMessages.action(event);
    }
}
