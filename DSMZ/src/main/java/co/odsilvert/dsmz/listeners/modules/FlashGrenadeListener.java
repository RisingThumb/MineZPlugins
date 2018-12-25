package co.odsilvert.dsmz.listeners.modules;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import com.google.inject.Inject;

import co.odsilvert.dsmz.main.DSMZ;

public class FlashGrenadeListener {
	private DSMZ plugin;
	
	@Inject
    public FlashGrenadeListener(DSMZ plugin) {
        this.plugin = plugin;
	}
	
	private void explode(Item flashGrenade) {
		BukkitRunnable explodeTask = new BukkitRunnable() {
			public void run() {
				List<Entity> nearbyEntities = flashGrenade.getNearbyEntities(5, 5, 5);
				for(Entity entity : nearbyEntities) {
					if (entity instanceof Player) {
						PotionEffect blindEffect = new PotionEffect(PotionEffectType.BLINDNESS, 100, 1);
						PotionEffect confusionEffect = new PotionEffect(PotionEffectType.CONFUSION, 100, 1);
						
						Player effectedPerson = (Player) entity;
						effectedPerson.addPotionEffect(blindEffect);
						effectedPerson.addPotionEffect(confusionEffect);
					}
				}
				
				flashGrenade.getWorld().spawnParticle(Particle.EXPLOSION_LARGE, flashGrenade.getLocation().getX(), flashGrenade.getLocation().getY(), flashGrenade.getLocation().getZ(), 10, 1, 1, 1);
				flashGrenade.remove();
			}
    	};
    	explodeTask.runTaskLater(plugin, 30L);
		
	}

	public void action(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		if (player.getItemInHand().getType() == Material.SLIME_BALL) {

			player.getInventory().setItemInMainHand(null);
			final Item flashGrenade = player.getWorld().dropItem(player.getEyeLocation().add(0, 0, 0), new ItemStack(Material.SLIME_BALL));
			flashGrenade.setInvulnerable(true);
			flashGrenade.setPickupDelay(32767);

			Location playerLoct = player.getEyeLocation();
			playerLoct.setPitch(playerLoct.getPitch()-30);

			flashGrenade.setVelocity(playerLoct.getDirection());
			explode(flashGrenade);

		}
	}

}
