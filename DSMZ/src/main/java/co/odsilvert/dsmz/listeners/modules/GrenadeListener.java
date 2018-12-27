package co.odsilvert.dsmz.listeners.modules;

import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.Projectile;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

import com.google.inject.Singleton;

@Singleton
public class GrenadeListener {
	public void explodeAction(ProjectileHitEvent event) {
		Projectile projectile = event.getEntity();
		// Check if it is an ender pearl. If not return and end prematurely
		if (!(projectile instanceof EnderPearl)) return;
		// Cast to EnderPearl
		EnderPearl enderPearl = (EnderPearl) projectile;
		// Explosion
		enderPearl.getWorld().createExplosion(enderPearl.getLocation().getX(), enderPearl.getLocation().getY(), enderPearl.getLocation().getZ(), 1.25F, false, false);
	}
	
	public void teleportAction(PlayerTeleportEvent event) {
		if(event.getCause() == TeleportCause.ENDER_PEARL){
			event.setCancelled(true);
		}
	}
}
