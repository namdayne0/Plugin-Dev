package com.example.customboat;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Boat;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BoatManager {
    private final BoatPlugin plugin;
    private final Map<UUID, BoatData> boats = new HashMap<>();

    public BoatManager(BoatPlugin plugin) {
        this.plugin = plugin;
        loadBoats();
    }

    public void addBoat(Boat boat, UUID owner, double speed) {
        BoatData data = new BoatData(owner, speed);
        boats.put(boat.getUniqueId(), data);
        saveBoat(boat, data);
    }

    public BoatData getBoat(UUID boatId) {
        return boats.get(boatId);
    }

    public void removeBoat(UUID boatId) {
        boats.remove(boatId);
        plugin.getConfig().set("boats." + boatId, null);
        plugin.saveConfig();
    }

    private void saveBoat(Boat boat, BoatData data) {
        FileConfiguration config = plugin.getConfig();
        String path = "boats." + boat.getUniqueId();
        config.set(path + ".owner", data.owner.toString());
        config.set(path + ".speed", data.speed);
        plugin.saveConfig();
    }

    private void loadBoats() {
        FileConfiguration config = plugin.getConfig();
        if (config.getConfigurationSection("boats") == null) return;
        for (String key : config.getConfigurationSection("boats").getKeys(false)) {
            UUID boatId = UUID.fromString(key);
            UUID owner = UUID.fromString(config.getString("boats." + key + ".owner"));
            double speed = config.getDouble("boats." + key + ".speed");
            boats.put(boatId, new BoatData(owner, speed));
        }
    }

    public static class BoatData {
        public final UUID owner;
        public final double speed;
        public BoatData(UUID owner, double speed) {
            this.owner = owner;
            this.speed = speed;
        }
    }
}
