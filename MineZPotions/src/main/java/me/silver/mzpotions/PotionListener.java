package me.silver.mzpotions;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class PotionListener implements Listener {

    private MineZPotions plugin = MineZPotions.getPlugin();

    @EventHandler
    public void onPotionConsume(PlayerItemConsumeEvent e) {
        Player player = e.getPlayer();
        ItemStack item = e.getItem();
        int itemSlot = player.getInventory().getHeldItemSlot();

//        player.sendMessage(item.getType().toString());

        switch (item.getType()) {
            case POTION:
                deleteConsumedItem(player, Material.GLASS_BOTTLE, itemSlot);
                break;
            case MILK_BUCKET:
                deleteConsumedItem(player, Material.BUCKET, itemSlot);
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
