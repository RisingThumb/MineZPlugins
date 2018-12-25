package co.odsilvert.dsmz.listeners;

import co.odsilvert.dsmz.listeners.modules.*;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.*;

import com.google.inject.Inject;

import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionType;

public class PlayerListeners implements Listener {
	
	// Modifiable variables to remove "Magic Numbers"
	private int bleedRange = 2;
	private int maxWaterLevel = 20;
	

	// Constructor parameter list was getting ridiculous
	@Inject private PotionsRemove potionsRemove;
	@Inject private SugarSpeed sugarSpeed;
	@Inject private KillMessages killMessages;
	@Inject private GrenadeListener grenadeListener;
	@Inject private FlashGrenadeListener flashGrenadeListener;
	@Inject private PlayerWaterHandler playerWaterHandler;
	@Inject private PlayerBleedingHandler playerBleedingHandler;
	@Inject private BandageItem bandageItem;
//	= plugin.getInjector().getInstance(PlayerWaterHandler.class);
    
	@EventHandler
    public void onPlayerInteract(PlayerInteractEvent event){
    	// All actions for that event would be listed here
		Player player = event.getPlayer();
		ItemStack item = player.getEquipment().getItemInMainHand();

		Action action = event.getAction();
		if ((action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) && event.getHand() == EquipmentSlot.HAND) {
			switch(item.getType()) {
				case SLIME_BALL:
					flashGrenadeListener.action(event);
					break;
				case SUGAR:
					sugarSpeed.action(event);
					break;
				case PAPER:
					bandageItem.action(event);
				default:
					break;
			}
		}
    }

    @EventHandler
    public void onConsume(PlayerItemConsumeEvent event) {
		Player player = event.getPlayer();
		ItemStack item = player.getEquipment().getItemInMainHand();

		switch (item.getType()) {
			case POTION:
            case MILK_BUCKET:
				potionsRemove.action(event);
				PotionMeta potion = (PotionMeta)item.getItemMeta();

				if (potion.getBasePotionData().getType().equals(PotionType.WATER)) {
					playerWaterHandler.setWaterLevel(player, maxWaterLevel);
					playerWaterHandler.setDehydrating(player, false);

					player.sendMessage(ChatColor.BLUE + "Ahh, much better");
				}
				break;
			case GOLDEN_APPLE:
				// Modify absorption hearts
				break;
				
			
			// Default case to stop eclipse screaming at me
			default:
				break;
		}
    }

    @EventHandler
    public void onPlayerConnect(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        if (player.getLevel() == 0) {
            // Start damage timer after an arbitrary amount of time
        	// Is this really necessary?
            playerWaterHandler.setDehydrating(player, true, 40L);
        }
    }

    @EventHandler
    public void onPlayerDisconnect(PlayerQuitEvent event) {
	    Player player = event.getPlayer();
	    playerBleedingHandler.setBleeding(player, false);
        playerWaterHandler.setDehydrating(player, false);
    }
    
    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event) {
    	Entity victim = event.getEntity();
    	System.out.println("DAMAGE DETECTED");
    	
    	if (victim instanceof Player) {
    		Player playerHurt = (Player) victim;
    		System.out.println("PLAYER DETECTED");
    		// Bleeding check
    		{
	    		int random = (int )(Math.random() * bleedRange + 1);
	    		System.out.println(random);
	    		if (random == 1) {
	    			playerHurt.sendMessage("You start bleeding!");
	    			playerBleedingHandler.setBleeding(playerHurt, true);
	    		}
    		}
    		
    		
    	}
    }

    @EventHandler
	public void onPlayerDeath(PlayerDeathEvent event){
        killMessages.action(event);

        event.setKeepLevel(true);
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
	    Player player = event.getPlayer();

	    playerWaterHandler.setWaterLevel(player, maxWaterLevel);
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
