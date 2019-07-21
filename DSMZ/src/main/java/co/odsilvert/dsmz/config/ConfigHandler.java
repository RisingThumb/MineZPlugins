package co.odsilvert.dsmz.config;

import co.odsilvert.dsmz.main.DSMZ;
import com.google.inject.Inject;
import java.io.File;
import java.io.IOException;
import org.bukkit.configuration.file.YamlConfiguration;

public class ConfigHandler {

    private DSMZ plugin;
    private ItemDataConfig itemDataConfig;

    @Inject
    public ConfigHandler(DSMZ pluign, ItemDataConfig itemDataConfig) {
        this.plugin = pluign;
        this.itemDataConfig = itemDataConfig;
    }

    public void loadConfigurations() {
        String dataFile = this.plugin.getDataFolder().getAbsolutePath();
        this.plugin.getLogger().info("Loading configuration files...");

        YamlConfiguration itemConfig = getConfig(dataFile, "items.yml");
        itemDataConfig.loadItemData(itemConfig);
    }

    // Return values of mkdirs() and createNewFile() are unimportant.
    private YamlConfiguration getConfig(String dataPath, String fileName) {
        File configFile = new File(dataPath, fileName);
        if (!configFile.exists()) {
            try {
                this.plugin.getLogger().info("Config file '" + fileName
                        + "' not found, attempting to create new one...");
                if (!configFile.getParentFile().exists()) {
                    configFile.getParentFile().mkdirs();
                }

                configFile.createNewFile();
            } catch (IOException e) {
                this.plugin.getLogger().info("Error creating file at '" + configFile.getAbsolutePath() + "'");
                e.printStackTrace();
            }
        }

        return YamlConfiguration.loadConfiguration(configFile);
    }
}
