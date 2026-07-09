package com.jihun.managers;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class SupplyCrateManager {
    private final JavaPlugin plugin;

    public SupplyCrateManager(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void start() {
        long firstDrop = 20L * 60L * 10L;
        long interval = 20L * 60L * 10L;
        Bukkit.getScheduler().runTaskTimer(plugin, this::dropCrate, firstDrop, interval);
    }

    private void dropCrate() {
        List<? extends Player> players = Bukkit.getOnlinePlayers().stream().toList();
        if (players.isEmpty()) return;

        Player center = players.get(ThreadLocalRandom.current().nextInt(players.size()));
        World world = center.getWorld();

        int x = center.getLocation().getBlockX() + ThreadLocalRandom.current().nextInt(-200, 201);
        int z = center.getLocation().getBlockZ() + ThreadLocalRandom.current().nextInt(-200, 201);
        int y = world.getHighestBlockYAt(x, z) + 1;

        Location location = new Location(world, x, y, z);
        Block block = location.getBlock();
        block.setType(Material.CHEST);

        if (!(block.getState() instanceof Chest chest)) return;

        Inventory inventory = chest.getInventory();
        inventory.clear();

        placeRandom(inventory, new ItemStack(Material.IRON_INGOT, randomAmount()));
        placeRandom(inventory, new ItemStack(Material.GOLD_INGOT, randomAmount()));
        placeRandom(inventory, new ItemStack(Material.COAL, randomAmount()));
        placeRandom(inventory, new ItemStack(Material.EMERALD, randomAmount()));
        placeRandom(inventory, new ItemStack(Material.DIAMOND, ThreadLocalRandom.current().nextInt(1, 4)));

        chest.update(true);

        Bukkit.broadcastMessage("§6[보급상자] §e보급상자가 생성되었습니다!");
        Bukkit.broadcastMessage("§e월드: §f" + world.getName() + " §e좌표: §fX " + x + ", Y " + y + ", Z " + z);
    }

    private void placeRandom(Inventory inventory, ItemStack item) {
        List<Integer> emptySlots = new ArrayList<>();

        for (int i = 0; i < inventory.getSize(); i++) {
            if (inventory.getItem(i) == null) {
                emptySlots.add(i);
            }
        }

        if (emptySlots.isEmpty()) return;

        Collections.shuffle(emptySlots);
        inventory.setItem(emptySlots.get(0), item);
    }

    private int randomAmount() {
        return ThreadLocalRandom.current().nextInt(1, 11);
    }
}
