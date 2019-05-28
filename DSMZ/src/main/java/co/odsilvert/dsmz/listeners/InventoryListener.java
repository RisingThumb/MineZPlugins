package co.odsilvert.dsmz.listeners;

import co.odsilvert.dsmz.config.ItemDataConfig;
import co.odsilvert.dsmz.main.DSMZ;
import co.odsilvert.dsmz.config.modules.ItemData;
import com.google.inject.Inject;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class InventoryListener implements Listener {

    @Inject private DSMZ plugin;
    @Inject private ItemDataConfig itemConfig;

    /**
     * Because I'm too lazy to type plugin.getLogger().info() every time
     * @param message The message to be logged
     */
    private void log(String message) {
        plugin.getLogger().info(message);
    }

//    @EventHandler
//    public void onItemHold(PlayerItemHeldEvent event) {
//        ItemStack item = event.getPlayer().getInventory().getItem(event.getNewSlot());
//        ItemData itemData = itemConfig.getItemData(item.getType());
//
//        log("Player is holding item: " + itemData.getName() + ", " + itemData.getMaxStackSize() + ", " + itemData.isCraftable());

//    }

    @EventHandler
    public void onItemPickup(EntityPickupItemEvent event) {
//        if (event.getEntity() instanceof Player) {
//            Player player = (Player)event.getEntity();
//
//            if (!player.getGameMode().equals(GameMode.CREATIVE)) {
//                event.getItem().getItemStack().setAmount(1);
//                event.setCancelled(true);
//            }
//        } else {
//            event.setCancelled(true);
//        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        log(event.getClick().toString());
        log(event.getAction().toString());
        Inventory inventory = event.getClickedInventory();

        if (event.getClick().equals(ClickType.DOUBLE_CLICK)) {
            event.setCancelled(true);

            if (event.getCursor().getAmount() < 15) {
                HashMap<Integer, ? extends ItemStack> itemStacks = inventory.all(event.getCursor().getType());
                List<ItemStack> sortedItems = new ArrayList<>(itemStacks.values());

                sortedItems.sort((o1, o2) -> {
                    if (o1.getAmount() == o2.getAmount()) return 0;
                    return (o1.getAmount() < o2.getAmount()) ? -1 : 1;
                });

                int stackSize = event.getCursor().getAmount();

                for (ItemStack item : sortedItems) {
                    if (item == event.getCursor()) continue;

                    if (item.getItemMeta().equals(event.getCursor().getItemMeta())) {
                        if (item.getAmount() + stackSize <= 15) {
                            stackSize += item.getAmount();
                            item.setAmount(0);
                        } else {
                            item.setAmount(item.getAmount() - (15 - stackSize));
                            stackSize = 15;
                            break;
                        }
                    }
                }

                event.getCursor().setAmount(stackSize);
            }

            if (event.getWhoClicked() instanceof Player) {
                ((Player)event.getWhoClicked()).updateInventory();
            }
        }

//        if (inventory.getType().equals(InventoryType.CRAFTING) ||
//                inventory.getType().equals(InventoryType.WORKBENCH)) {
//            CraftingInventory craftingInventory = (CraftingInventory) inventory;
//            ItemStack result = craftingInventory.getResult();
//        }
    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event) {
        Player player = (Player)event.getWhoClicked();

        log("Inventory drag event");
        // Use event.getNewItems() to get a list of all ItemStacks (for custom stack sizes)



    }

    @EventHandler
    public void onRecipePrepare(PrepareItemCraftEvent event) {
        log("Duh.");

        Player player = (Player)event.getViewers().get(0);

        log(player.getGameMode().toString());

        // Don't cancel if players are repairing items
        if (event.isRepair()) return;

        // Don't disable crafting for creative mode players
        // This assumes the first person to open the crafting GUI (e.g. crafting table) is
        // in creative mode.
        if (!player.getGameMode().equals(GameMode.CREATIVE)) {
            CraftingInventory inventory = event.getInventory();
            ItemStack result = inventory.getResult();

            if (!(result == null)) {
                ItemData itemData = itemConfig.getItemData(result.getType());

                if (!itemData.isCraftable()) {
                    inventory.setResult(null);
                }
            }
        }
    }

    @EventHandler
    public void onCraft(CraftItemEvent event) {
        // Cancel if not craftable (Purely as a failsafe)
    }
}
