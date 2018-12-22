package co.odsilvert.dsmz.listeners;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import com.google.inject.Inject;

import co.odsilvert.dsmz.main.DSMZ;

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
    private void deleteConsumedItem(Player player, Material itemType, int slot) {
        BukkitRunnable deleteItem = new BukkitRunnable() {
            @Override
            public void run() {
                ItemStack item = player.getInventory().getItem(slot);

                if (item.getType().equals(itemType)) {
                    player.getInventory().setItem(slot, null);
                }
            }
        };

        deleteItem.runTaskLater(plugin, 1L);
    }

}