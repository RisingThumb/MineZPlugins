package co.odsilvert.dsmz.util;

import org.bukkit.Material;

/**
 * Custom data about game items. This can be expanded as necessary.
 */
public class ItemData {
    private Material material;
    private String customName;
    private boolean craftable;
    private int maxStackSize;

    public ItemData(Material material, String customName, boolean craftable, int maxStackSize) {
        this.material = material;
        this.customName = customName;
        this.craftable = craftable;
        this.maxStackSize = maxStackSize;
    }

    public Material getMaterial() {
        return this.material;
    }

    public String getName() {
        return this.customName;
    }

    public boolean isCraftable() {
        return this.craftable;
    }

    public int getMaxStackSize() {
        return this.maxStackSize;
    }
}
