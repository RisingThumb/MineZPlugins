package co.odsilvert.dsmz.listeners;

import co.odsilvert.dsmz.config.ItemDataConfig;
import co.odsilvert.dsmz.main.DSMZ;
import co.odsilvert.dsmz.config.modules.ItemData;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.google.inject.Inject;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.InvocationTargetException;
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

        if (event.getEntity() instanceof Player) {
            Player player = (Player)event.getEntity();
            Item item = event.getItem();

            if (!player.getGameMode().equals(GameMode.CREATIVE)) {
                event.setCancelled(true);

                HashMap<Integer, ? extends ItemStack> items = player.getInventory().all(item.getItemStack().getType());
                int stackSize = item.getItemStack().getAmount();

                for (ItemStack itemStack : items.values()) {
                    // TODO: Update to skip if maxStackSize is 1
                    if (itemStack.getAmount() < 15 && item.getItemStack().getItemMeta().equals(itemStack.getItemMeta())) {
                        int difference = Math.min(15 - itemStack.getAmount(), stackSize);

                        itemStack.setAmount(itemStack.getAmount() + difference);
                        stackSize -= difference;

                        if (stackSize == 0) {
                            sendCollectPacket(player, item);
                            break;
                        }
                    }
                }

                while (stackSize > 0) {
                    int slot = player.getInventory().firstEmpty();
                    int amount = Math.min(15, stackSize);

                    if (slot > -1) {
                        player.getInventory().setItem(slot, new ItemStack(item.getItemStack().getType(), amount));
                        stackSize -= amount;

                        if (item.getItemStack().hasItemMeta()) {
                            player.getInventory().getItem(slot).setItemMeta(item.getItemStack().getItemMeta());
                        }

                        if (stackSize == 0) {
                            sendCollectPacket(player, item);
                            break;
                        }
                    } else {
                        break;
                    }
                }
                item.getItemStack().setAmount(stackSize);
            }
        } else {
            // Disable picking up items for entities other than players
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
//        log(event.getClick().toString());
//        log(event.getAction().toString());
        Inventory inventory = event.getClickedInventory();

        if (event.getAction().equals(InventoryAction.COLLECT_TO_CURSOR)) {
            event.setCancelled(true);
            handleDoubleClick(event.getCursor(), inventory);
        } else if (event.getAction().equals(InventoryAction.PLACE_ALL)
                || event.getAction().equals(InventoryAction.PLACE_SOME)) {
            ItemStack current = event.getCurrentItem();
            ItemStack cursor = event.getCursor();
            int amount = Math.min(current.getMaxStackSize() - current.getAmount(), cursor.getAmount());

            if (!event.getCurrentItem().getType().equals(Material.AIR)) {
                event.setCancelled(true);
                int difference = 15 - current.getAmount();

                if (difference > 0) {
                    current.setAmount(current.getAmount() + Math.min(difference, amount));
                    cursor.setAmount(cursor.getAmount() - Math.min(difference, amount));
                }
            }
        } else if (event.getAction().equals(InventoryAction.PLACE_ONE)) {
            if (!event.getCurrentItem().getType().equals(Material.AIR)) {
                if (event.getCurrentItem().getAmount() >= 15) {
                    event.setCancelled(true);
                }
            }
        } else if (event.getAction().equals(InventoryAction.MOVE_TO_OTHER_INVENTORY)) {
            log(event.getAction().toString());
            log(event.getClickedInventory().getType().toString());
        }

//        if (event.getWhoClicked() instanceof Player) {
//            ((Player)event.getWhoClicked()).updateInventory();
//        }
    }

    private void handleDoubleClick(ItemStack cursorItem, Inventory inventory) {
        if (cursorItem.getAmount() < 15) {
            HashMap<Integer, ? extends ItemStack> itemStacks = inventory.all(cursorItem.getType());
            List<ItemStack> sortedItems = new ArrayList<>(itemStacks.values());

            sortedItems.sort((o1, o2) -> {
                if (o1.getAmount() == o2.getAmount()) return 0;
                return (o1.getAmount() < o2.getAmount()) ? -1 : 1;
            });

            int stackSize = cursorItem.getAmount();

            for (ItemStack item : sortedItems) {
                if (item == cursorItem) continue;

                if (item.getItemMeta().equals(cursorItem.getItemMeta())) {
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

            cursorItem.setAmount(stackSize);
        }
    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event) {
        Player player = (Player)event.getWhoClicked();

        log("Inventory drag event");
        // Use event.getNewItems() to get a list of all ItemStacks (for custom stack sizes)



    }

    @EventHandler
    public void onRecipePrepare(PrepareItemCraftEvent event) {
        if (event.getInventory().getHolder() instanceof Player) {
            Player player = (Player)event.getInventory().getHolder();

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
    }

    @EventHandler
    public void onCraft(CraftItemEvent event) {
        // Cancel if not craftable (Purely as a failsafe)
    }

    // Send packet to display the item pickup animation, despite the item being deleted
    private void sendCollectPacket(Player player, Item item) {
        PacketContainer itemPickupAnimation = plugin.getProtocolManager().createPacket(PacketType.Play.Server.COLLECT);

        itemPickupAnimation.getIntegers()
                .write(0, item.getEntityId())
                .write(1, player.getEntityId())
                .write(2, item.getItemStack().getAmount());

        try {
            plugin.getProtocolManager().sendServerPacket(player, itemPickupAnimation);

            for (Entity entity : player.getNearbyEntities(30, 30, 30)) {
                if (entity instanceof Player) {
                    plugin.getProtocolManager().sendServerPacket((Player)entity, itemPickupAnimation);
                }
            }
        } catch (InvocationTargetException ex) {
            throw new RuntimeException("Error: Failed to send packet " + itemPickupAnimation, ex);
        }
    }
}
