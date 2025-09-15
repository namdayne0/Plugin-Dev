package com.namdev.customboat;

import org.bukkit.Particle;
import org.bukkit.entity.Boat;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.vehicle.VehicleMoveEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BoatListener implements Listener {
    private final BoatPlugin plugin;
    private final Map<UUID, UUID> managing = new HashMap<>();

    public BoatListener(BoatPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;
        String title = event.getView().getTitle();
        if (BoatMenu.isCreationMenu(title)) {
            event.setCancelled(true);
            if (event.getCurrentItem() == null) return;
            String localized = event.getCurrentItem().getItemMeta().getLocalizedName();
            double speed = Double.parseDouble(localized);
            Boat boat = player.getWorld().spawn(player.getLocation(), Boat.class);
            boat.setVelocity(player.getLocation().getDirection().multiply(speed));
            plugin.getBoatManager().addBoat(boat, player.getUniqueId(), speed);
            player.closeInventory();
        } else if (BoatMenu.isManageMenu(title)) {
            event.setCancelled(true);
            if (event.getCurrentItem() == null) return;
            if (event.getCurrentItem().getType().name().contains("BARRIER")) {
                UUID boatId = managing.remove(player.getUniqueId());
                if (boatId != null) {
                    Boat boat = (Boat) player.getWorld().getEntity(boatId);
                    if (boat != null) boat.remove();
                    plugin.getBoatManager().removeBoat(boatId);
                }
                player.closeInventory();
            }
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEntityEvent event) {
        if (!(event.getRightClicked() instanceof Boat boat)) return;
        BoatManager.BoatData data = plugin.getBoatManager().getBoat(boat.getUniqueId());
        if (data != null && data.owner.equals(event.getPlayer().getUniqueId())) {
            managing.put(event.getPlayer().getUniqueId(), boat.getUniqueId());
            BoatMenu.openManageMenu(event.getPlayer());
        }
    }

    @EventHandler
    public void onMove(VehicleMoveEvent event) {
        if (!(event.getVehicle() instanceof Boat boat)) return;
        BoatManager.BoatData data = plugin.getBoatManager().getBoat(boat.getUniqueId());
        if (data != null) {
            if (boat.getVelocity().length() > 0) {
                boat.setVelocity(boat.getVelocity().normalize().multiply(data.speed));
            }
            boat.getWorld().spawnParticle(Particle.WATER_SPLASH, boat.getLocation(), 5, 0.2, 0.1, 0.2, 0.01);
        }
    }
}
