package com.example.customboat;

import org.bukkit.plugin.java.JavaPlugin;

public class BoatPlugin extends JavaPlugin {
    private BoatManager boatManager;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        boatManager = new BoatManager(this);
        getCommand("boats").setExecutor(new BoatCommand(this));
        getServer().getPluginManager().registerEvents(new BoatListener(this), this);
    }

    public BoatManager getBoatManager() {
        return boatManager;
    }
}
