package com.namdev.customboat;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class BoatMenu {
    private static final String CREATE_TITLE = ChatColor.BLUE + "Create Boat";
    private static final String MANAGE_TITLE = ChatColor.DARK_GREEN + "Boat Options";

    public static void openCreationMenu(Player player) {
        Inventory inv = Bukkit.createInventory(null, 9, CREATE_TITLE);
        inv.setItem(3, createItem(Material.OAK_BOAT, ChatColor.GREEN + "Slow Boat", 0.2));
        inv.setItem(4, createItem(Material.SPRUCE_BOAT, ChatColor.YELLOW + "Normal Boat", 0.4));
        inv.setItem(5, createItem(Material.BIRCH_BOAT, ChatColor.RED + "Fast Boat", 0.6));
        player.openInventory(inv);
    }

    public static void openManageMenu(Player player) {
        Inventory inv = Bukkit.createInventory(null, 9, MANAGE_TITLE);
        inv.setItem(4, createItem(Material.BARRIER, ChatColor.RED + "Remove Boat", -1));
        player.openInventory(inv);
    }

    private static ItemStack createItem(Material material, String name, double speed) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(name);
            meta.setLocalizedName(String.valueOf(speed));
            item.setItemMeta(meta);
        }
        return item;
    }

    public static boolean isCreationMenu(String title) {
        return CREATE_TITLE.equals(title);
    }

    public static boolean isManageMenu(String title) {
        return MANAGE_TITLE.equals(title);
    }
}
