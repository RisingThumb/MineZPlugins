package co.odsilvert.dsmz.listeners.modules;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import co.odsilvert.dsmz.main.DSMZ;

@Singleton
public class PotionsRemove {
	
	private DSMZ plugin;
	
    @Inject
    public PotionsRemove(DSMZ plugin) {
        this.plugin = plugin;
    }

	public void action(PlayerItemConsumeEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();
        int itemSlot = player.getInventory().getHeldItemSlot();

        switch (item.getType()) {
            case POTION:
                deleteConsumedItem(player, Material.GLASS_BOTTLE, itemSlot);
                break;
            case MILK_BUCKET:
                deleteConsumedItem(player, Material.BUCKET, itemSlot);
                break;
            default:
            	break;
        }
	}
    private void deleteConsumedItem(final Player player, final Material itemType, final int slot) {
        BukkitRunnable deleteItem = new BukkitRunnable() {
            @Override
            public void run() {
                ItemStack item = player.getInventory().getItem(slot);

                if (item.getType().equals(itemType)) {
                    player.getInventory().setItem(slot, null);
                }
            }
        };

        deleteItem.runTaskLater(this.plugin, 1L);
    }

}
