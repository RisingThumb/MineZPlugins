package co.odsilvert.dsmz.main;

import co.odsilvert.dsmz.listeners.PlayerListeners;
import com.google.inject.Inject;
import com.google.inject.Injector;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import util.BinderModule;

public class DSMZ extends JavaPlugin {

//    private static DSMZ plugin;

    @Inject
    private PlayerListeners playerListeners;

    @Override
    public void onEnable() {
        // Getting dependencies/setting up DI
        BinderModule module = new BinderModule(this);
        Injector injector = module.createInjector();
        injector.injectMembers(this);

        registerListeners();
    }

    private void registerListeners() {
        PluginManager pluginManager = getServer().getPluginManager();

        pluginManager.registerEvents(this.playerListeners, this);
    }
}
