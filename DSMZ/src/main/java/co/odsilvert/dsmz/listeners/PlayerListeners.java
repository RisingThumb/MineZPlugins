package co.odsilvert.dsmz.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
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
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public class PlayerListeners implements Listener {

	// Constructor parameter list was getting ridiculous
	@Inject
	private PotionsRemove potionsRemove;
	@Inject
	private SugarSpeed sugarSpeed;
	@Inject
	private KillMessages killMessages;
	@Inject
	private GrenadeListener grenadeListener;
	@Inject
	private FlashGrenadeListener flashGrenadeListener;
    
	@EventHandler
    public void onPlayerInteract(PlayerInteractEvent event){
    	// All actions for that event would be listed here
		Player player = event.getPlayer();
		ItemStack item = player.getItemInHand();

		Action action = event.getAction();
		if ((action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) && event.getHand() == EquipmentSlot.HAND) {
			switch(item.getType()) {
				case SLIME_BALL:
					flashGrenadeListener.action(event);
					break;
				case SUGAR:
					sugarSpeed.action(event);
					break;
			}

		}


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
