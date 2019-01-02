package co.odsilvert.dsmz.config;

import co.odsilvert.dsmz.main.DSMZ;
import co.odsilvert.dsmz.util.ItemData;
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
            Material material = Material.getMaterial((String) dataObjects.get("material"));
            String name = (String) dataObjects.get("name");
            int maxStackSize;

            try {
                maxStackSize = Integer.parseInt((String) dataObjects.get("stack"));
            } catch (NumberFormatException e) {
                plugin.getLogger().info("Warning: Item stack size for item with material '" + material.toString()
                        + "' not specified. Using default value (1).");
                maxStackSize = 1;
            }

            ItemData item = new ItemData(material, name, maxStackSize);
            this.itemData.put(material, item);
        }

    }

    public ItemData getItemData(Material material) {
        return itemData.get(material);
    }
}

