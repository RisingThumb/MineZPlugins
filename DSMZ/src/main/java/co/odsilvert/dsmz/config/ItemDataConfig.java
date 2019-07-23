package co.odsilvert.dsmz.config;

import co.odsilvert.dsmz.main.DSMZ;
import co.odsilvert.dsmz.config.modules.ItemData;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;

@Singleton
public class ItemDataConfig {

    private DSMZ plugin;
    private HashMap<Material, ItemData> itemData = new HashMap<>();

    @Inject
    public ItemDataConfig(DSMZ plugin) {
        this.plugin = plugin;
    }

    // Pull all data from config file and get it into a usable form
    void loadItemData(YamlConfiguration config) {
        List<Map<?, ?>> configData = config.getMapList("items");

        for (Map<?, ?> dataObjects : configData) {
            Material material = Material.getMaterial(dataObjects.get("material").toString());

            if (material == null) {
                plugin.getLogger().info("Error: Item material \"" + dataObjects.get("material").toString()
                        + "\" is invalid. Skipping this item.");
                continue;
            }

            String name = dataObjects.get("name").toString();
            int maxStackSize;
            boolean craftable = false;

            try {
                maxStackSize = (Integer)dataObjects.get("stack");
                craftable = (Boolean)dataObjects.get("craftable");
            } catch (ClassCastException e) {
                plugin.getLogger().info("Warning: Item stack size for item with material '" + material.toString()
                        + "' is invalid or not specified. Using default value (1).");
                maxStackSize = 1;
            }

            ItemData item = new ItemData(material, name, maxStackSize, craftable);

            /*
                DEBUG THINGY
             */
            System.out.printf("Successfully created an item with data: %s, %s, %d", material.name(), name, maxStackSize);
            this.itemData.put(material, item);
        }

    }

    public ItemData getItemData(Material material) {
        if (itemData.containsKey(material)) {
            return itemData.get(material);
        } else {
            return new ItemData(material, material.toString(), 1, false);
        }
    }
}