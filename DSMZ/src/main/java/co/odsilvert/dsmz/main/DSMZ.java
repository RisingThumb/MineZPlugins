package co.odsilvert.dsmz.main;

import co.odsilvert.dsmz.listeners.PlayerListeners;
import org.bukkit.plugin.java.JavaPlugin;

public class DSMZ extends JavaPlugin {

    private static DSMZ plugin;

    @Override
    public void onEnable() {
        plugin = this;

        System.out.println("DSMZ v0.0.1 Enabled");
        // Note: This will also be updated to support DI
        getServer().getPluginManager().registerEvents(new PlayerListeners(), this);
    }

    // TODO: Replace static getter function with DI implementation
    public static DSMZ getPlugin() {
        return plugin;
    }
}
