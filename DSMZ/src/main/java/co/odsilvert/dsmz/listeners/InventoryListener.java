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
import org.bukkit.inventory.InventoryView;
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

    @EventHandler
    public void onItemPickup(EntityPickupItemEvent event) {

        if (event.getEntity() instanceof Player) {
            Player player = (Player)event.getEntity();
            Item item = event.getItem();

            if (!player.getGameMode().equals(GameMode.CREATIVE)) {
                event.setCancelled(true);
                int amount = addItemStack(item.getItemStack(), player.getInventory());

                if (amount == 0) {
                    sendCollectPacket(player, item);
                }

                item.getItemStack().setAmount(amount);
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
        log("Slot: " + event.getSlot() + ", Raw: " + event.getRawSlot() + ", Type: " + event.getSlotType().toString());
        Inventory inventory = event.getClickedInventory();

        log("Inventory raw size: " + inventory.getSize());
        if (event.getWhoClicked() instanceof Player) {
            Player player = (Player)event.getWhoClicked();

            if (!player.getGameMode().equals(GameMode.CREATIVE)) {
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
                    event.setCancelled(true);
                    handleShiftClick(event);
                }
            }
        }
    }

    private void handleShiftClick(InventoryClickEvent event) {
        ItemStack itemStack = event.getCurrentItem();
        InventoryView inventoryView = event.getView();
        Inventory inventory = event.getClickedInventory();
        int startIndex;
        int endIndex;
        Inventory targetInventory;

        if (inventoryView.getTopInventory().getType().equals(InventoryType.CRAFTING)) {
            targetInventory = inventoryView.getBottomInventory();

            if (inventory == inventoryView.getBottomInventory()) {
                if (event.getSlotType().equals(InventoryType.SlotType.QUICKBAR)) {
                    startIndex = 9;
                    endIndex = 36;
                } else {
                    startIndex = 36;
                    endIndex = 45;
                }
            } else {
                if (event.getSlotType().equals(InventoryType.SlotType.RESULT)) {
                    endIndex = 45;
                    startIndex = 9;
                } else {
                    startIndex = 9;
                    endIndex = 45;
                }
            }
        } else if (inventoryView.getTopInventory().getType().equals(InventoryType.CHEST)) {
            if (inventory == inventoryView.getTopInventory()) {
                targetInventory = inventoryView.getBottomInventory();
                endIndex = inventoryView.getTopInventory().getSize();

                // Even inside of a chest inventory, the player inventory adds 5 extra slots to the total count
                // because of the armor/shield slots, despite not being visible in the InventoryView. To fix this,
                // we subtract 5 to remove those extra slots, so InventoryView#convertSlot() does its math right.
                startIndex = endIndex + targetInventory.getSize() - 5;
            } else {
                targetInventory = inventoryView.getTopInventory();
                startIndex = 0;
                endIndex = inventoryView.getTopInventory().getSize();
            }
        } else {
            String inventoryType = inventoryView.getTopInventory().getType().toString().replace('_', ' ').toLowerCase();
            //TODO: Implement these
            log("Error: Shift clicking in the " + inventoryType + " GUI is currently not implemented.");
            return;
        }

        int amount = addRaw(itemStack, targetInventory, inventoryView, startIndex, endIndex);

        if (event.getSlotType().equals(InventoryType.SlotType.RESULT)) {
            // TODO: Implement this, too
            log("Shift clicking from the crafting result slot is currently not implemented");
        } else {
            event.getCurrentItem().setAmount(amount);
        }
    }

    private int addRaw(ItemStack item, Inventory targetInventory, InventoryView view, int startIndex, int endIndex) {
        int iterator = 1;
        int stackSize = item.getAmount();
        ArrayList<Integer> emptySlots = new ArrayList<>();

        if (startIndex > endIndex) {
            iterator = -1;
            startIndex -= 1;
            endIndex -= 1;
        }

        for (int i = startIndex; i != endIndex; i += iterator) {
            ItemStack itemStack = targetInventory.getItem(view.convertSlot(i));

            if (itemStack == null) {
                emptySlots.add(i);
            } else if (itemStack.isSimilar(item) && itemStack != item) {
                log("Found " + itemStack.getAmount() + " " + itemStack.getType().toString() + " at [" + i + "] (" + view.convertSlot(i) + ")");

                if (itemStack.getAmount() < 15 && item.getItemMeta().equals(itemStack.getItemMeta())) {
                    int difference = Math.min(15 - itemStack.getAmount(), stackSize);
                    log(" - Adding " + difference + " to stack");

                    itemStack.setAmount(itemStack.getAmount() + difference);
                    stackSize -= difference;
                    log(" - Set stack size to " + stackSize);

                    if (stackSize == 0) {
                        return 0;
                    }
                }
            }
        }

        for (int slot : emptySlots) {
            log("Found empty slot at " + slot);
            int amount = Math.min(15, stackSize);

            ItemStack newItem = item.clone();
            newItem.setAmount(amount);

            targetInventory.setItem(view.convertSlot(slot), newItem);
            log(" - Created item with amount " + newItem.getAmount() + " at [" + slot + "] (" + view.convertSlot(slot) + ")");
            stackSize -= amount;
            log(" - Set stack size to " + stackSize);
            if (stackSize == 0) {
                return 0;
            }
        }
        return stackSize;
    }

    private int addItemStack(ItemStack item, Inventory inventory) {
//        HashMap<Integer, ? extends ItemStack> items = inventory.all(item.getType());
        ArrayList<Integer> emptySlots = new ArrayList<>();
        ItemStack[] items = inventory.getStorageContents();
        int stackSize = item.getAmount();

        for (int slot = 0; slot < inventory.getStorageContents().length; slot++) {
            if (items[slot] != null) {
                ItemStack itemStack = items[slot];
//                if (itemStack == item) continue;
                if (itemStack.isSimilar(item) && item != itemStack) {
                    log("Found " + itemStack.getAmount() + " " + itemStack.getType().toString() + " at [" + slot + "]");

                    if (itemStack.getAmount() < 15 && item.getItemMeta().equals(itemStack.getItemMeta())) {
                        int difference = Math.min(15 - itemStack.getAmount(), stackSize);
                        log(" - Adding " + difference + " to stack");

                        itemStack.setAmount(itemStack.getAmount() + difference);
                        stackSize -= difference;
                        log(" - Set stack size to " + stackSize);

                        if (stackSize == 0) {
                            return 0;
                        }
                    }
                }
            } else {
                emptySlots.add(slot);
            }
        }

        for (int slot : emptySlots) {
            log("Found empty slot at " + slot);
            int amount = Math.min(15, stackSize);

            ItemStack newItem = item.clone();
            newItem.setAmount(amount);

            inventory.setItem(slot, newItem);
            log(" - Created item with amount " + newItem.getAmount() + " at [" + slot + "]");
            stackSize -= amount;
            log(" - Set stack size to " + stackSize);
            if (stackSize == 0) {
                return 0;
            }
        }
        return stackSize;
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

//            log(player.getGameMode().toString());

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
