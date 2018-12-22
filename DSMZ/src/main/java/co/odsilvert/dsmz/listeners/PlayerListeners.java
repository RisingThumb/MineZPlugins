package co.odsilvert.dsmz.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import com.google.inject.Inject;

import co.odsilvert.dsmz.listeners.modules.FlashGrenadeListener;
import co.odsilvert.dsmz.listeners.modules.GrenadeListener;
import co.odsilvert.dsmz.listeners.modules.KillMessages;
import co.odsilvert.dsmz.listeners.modules.PotionsRemove;
import co.odsilvert.dsmz.listeners.modules.SugarSpeed;

public class PlayerListeners implements Listener {
	
	private PotionsRemove potionsRemove;
	private SugarSpeed sugarSpeed;
	private KillMessages killMessages;
	private GrenadeListener grenadeListener;
	private FlashGrenadeListener flashGrenadeListener;
	
    @Inject
    public PlayerListeners(PotionsRemove potionsRemove, SugarSpeed sugarSpeed, KillMessages killMessages, GrenadeListener grenadeListener,
    		FlashGrenadeListener flashGrenadeListener) {
        this.potionsRemove = potionsRemove;
        this.sugarSpeed = sugarSpeed;
        this.killMessages = killMessages;
        this.grenadeListener = grenadeListener;
        this.flashGrenadeListener = flashGrenadeListener;
	}
    
	@EventHandler
    public void onPlayerInteract(PlayerInteractEvent event){
    	// All actions for that event would be listed here
		sugarSpeed.action(event);
		flashGrenadeListener.action(event);
		
    }
    @EventHandler
    public void onPotionConsume(PlayerItemConsumeEvent event) {
    	potionsRemove.action(event);
    }
    @EventHandler
	public void onPlayerDeath(PlayerDeathEvent event){
    	killMessages.action(event);
    }
	@EventHandler
	public void onProjectileHit(ProjectileHitEvent event){
		grenadeListener.explodeAction(event);
	}
	@EventHandler
	public void onTeleport(PlayerTeleportEvent event){
		grenadeListener.teleportAction(event);
	}
		
}
