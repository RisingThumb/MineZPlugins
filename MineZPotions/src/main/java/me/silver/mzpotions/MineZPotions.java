package me.silver.mzpotions;

import org.bukkit.plugin.java.JavaPlugin;

public class MineZPotions extends JavaPlugin {

    private static MineZPotions plugin;

    @Override
    public void onEnable() {
        plugin = this;

        getServer().getPluginManager().registerEvents(new PotionListener(), this);
    }

    @Override
    public void onDisable() {
        getServer().getScheduler().cancelAllTasks();
    }

    static MineZPotions getPlugin() {
        return plugin;
    }

}
