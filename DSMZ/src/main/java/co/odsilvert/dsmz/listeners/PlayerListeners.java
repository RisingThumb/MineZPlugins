package co.odsilvert.dsmz.listeners;

import co.odsilvert.dsmz.listeners.modules.*;
import co.odsilvert.dsmz.main.DSMZ;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.*;

import com.google.inject.Inject;

import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionType;

public class PlayerListeners implements Listener {

	// Constructor parameter list was getting ridiculous
	@Inject private DSMZ plugin;
	@Inject private PotionsRemove potionsRemove;
	@Inject private SugarSpeed sugarSpeed;
	@Inject private KillMessages killMessages;
	@Inject private GrenadeListener grenadeListener;
	@Inject private FlashGrenadeListener flashGrenadeListener;
	@Inject private PlayerWaterHandler playerWaterHandler;
//	= plugin.getInjector().getInstance(PlayerWaterHandler.class);
    
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
    public void onConsume(PlayerItemConsumeEvent event) {
		Player player = event.getPlayer();
		ItemStack item = player.getItemInHand();

		switch (item.getType()) {
			case POTION:
            case MILK_BUCKET:
				potionsRemove.action(event);
				PotionMeta potion = (PotionMeta)item.getItemMeta();

				if (potion.getBasePotionData().getType().equals(PotionType.WATER)) {
					playerWaterHandler.setWaterLevel(player, 20);
					playerWaterHandler.setDehydrating(player, false);

					player.sendMessage(ChatColor.BLUE + "Ahh, much better");
				}
				break;
			case GOLDEN_APPLE:
				// Modify absorption hearts
				break;
		}
    }

    @EventHandler
    public void onPlayerConnect(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        if (player.getLevel() == 0) {
            // Start damage timer after an arbitrary amount of time
            playerWaterHandler.setDehydrating(player, true, 40L);
        }
    }

    @EventHandler
    public void onPlayerDisconnect(PlayerQuitEvent event) {
	    Player player = event.getPlayer();
        playerWaterHandler.setDehydrating(player, false);
    }

    @EventHandler
	public void onPlayerDeath(PlayerDeathEvent event){
        killMessages.action(event);

        event.setKeepLevel(true);
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
	    Player player = event.getPlayer();

	    playerWaterHandler.setWaterLevel(player, 20);
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
