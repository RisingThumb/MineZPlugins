package co.odsilvert.dsmz.util;

import org.bukkit.Material;

/**
 * Custom data about game items. This can be expanded as necessary.
 */
public class ItemData {
    private Material material;
    private String customName;
    private int maxStackSize;

    public ItemData(Material material, String customName, int maxStackSize) {
        this.material = material;
        this.customName = customName;
        this.maxStackSize = maxStackSize;
    }

    public Material getMaterial() {
        return this.material;
    }

    public String getName() {
        return this.customName;
    }

    public int getMaxStackSize() {
        return this.maxStackSize;
    }
}
