package co.odsilvert.dsmz.main;

import co.odsilvert.dsmz.config.ConfigHandler;
import co.odsilvert.dsmz.listeners.InventoryListener;
import co.odsilvert.dsmz.listeners.PlayerListeners;
import co.odsilvert.dsmz.timers.PlayerStatusTimer;
import co.odsilvert.dsmz.timers.PlayerDehydrationTimer;
import co.odsilvert.dsmz.timers.PlayerWaterTimer;
import co.odsilvert.dsmz.util.BinderModule;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.google.inject.Inject;
import com.google.inject.Injector;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

public class DSMZ extends JavaPlugin {

    private Injector injector;
    private ProtocolManager protocolManager;

    @Inject private PlayerListeners playerListeners;
    @Inject private PlayerWaterTimer playerWaterTimer;
    @Inject private PlayerDehydrationTimer playerDehydrationTimer;
    @Inject private PlayerStatusTimer playerBleedingTimer;
    @Inject private ConfigHandler configHandler;
    @Inject private InventoryListener inventoryListener;

    @Override
    public void onEnable() {
        // Setup Dependency Injection
        BinderModule module = new BinderModule(this);
        injector = module.createInjector();
        injector.injectMembers(this);

        startTimers();
        registerListeners();

        configHandler.loadConfigurations();

        protocolManager = ProtocolLibrary.getProtocolManager();
    }

    public Injector getInjector() {
        return this.injector;
    };

    public ProtocolManager getProtocolManager() {
        return this.protocolManager;
    }
    
    // Handle global repeating tasks
    private void startTimers() {
        BukkitScheduler scheduler = getServer().getScheduler();
        
        scheduler.scheduleSyncRepeatingTask(this, playerWaterTimer, 0, 1200L);
        scheduler.scheduleSyncRepeatingTask(this, playerDehydrationTimer, 0, 40L);
        scheduler.scheduleSyncRepeatingTask(this, playerBleedingTimer, 0, 120L);
    }
    
    private void registerListeners() {
        PluginManager pluginManager = getServer().getPluginManager();

        pluginManager.registerEvents(this.playerListeners, this);
        pluginManager.registerEvents(this.inventoryListener, this);
    }

}
