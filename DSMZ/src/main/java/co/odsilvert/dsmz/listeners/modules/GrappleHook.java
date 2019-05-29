package co.odsilvert.dsmz.listeners.modules;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class GrappleHook {
	public void action(PlayerFishEvent event) {
		Player player = event.getPlayer();
		ItemStack item = player.getEquipment().getItemInMainHand();
		
		if (item.getType().equals(Material.FISHING_ROD)) {
			List<Entity> nearby = player.getNearbyEntities(30,30,30);
			Entity hook = null;
			for (Entity e : nearby) {
				if (e.getType().equals(EntityType.FISHING_HOOK)) {
					hook = e;
					break;
				}
			}
			if (hook == null) {
				return;
			}
			Vector hookVelocity = hook.getVelocity();
			if (!hookVelocity.equals(new Vector(0,0,0))) {
				player.sendMessage(ChatColor.RED+"Your hook is not attached to a block.");
				return;
			}
			Location hookPosition = hook.getLocation();
			Location playerPosition = player.getEyeLocation();
			Location distanceOf2Points = hookPosition.subtract(playerPosition);
			// We get distance of the 2 points as location. Location is X Y and Z data like a 3D Vector
			System.out.println("X:"+distanceOf2Points.getX()+"\nY:"+distanceOf2Points.getY()+"\nZ:"+distanceOf2Points.getZ());
			if (distanceOf2Points.getY()<0) {
				player.sendMessage(ChatColor.RED+"Your hook is not attached high enough");
				return;
			}
			Vector playerVelocity = new Vector(distanceOf2Points.getX()/5, distanceOf2Points.getY()/5, distanceOf2Points.getZ()/5);
			player.setVelocity(playerVelocity);
			item.setDurability((short) (player.getEquipment().getItemInMainHand().getDurability() + 9));
		}
	}
	
}
