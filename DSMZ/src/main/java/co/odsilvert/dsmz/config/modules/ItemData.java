package co.odsilvert.dsmz.config.modules;

import org.bukkit.Material;

/**
 * Custom data about game items. This can be expanded as necessary.
 */
public class ItemData {
    private Material material;
    private String customName;
    private int maxStackSize;
    private boolean craftable;

    public ItemData(Material material, String customName, int maxStackSize, boolean craftable) {
        this.material = material;
        this.customName = customName;
        this.maxStackSize = maxStackSize;
        this.craftable = craftable;
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

    public boolean isCraftable() {
        return craftable;
    }
}
