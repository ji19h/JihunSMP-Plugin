package com.jihun.managers;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.Random;
public class SupplyCrateManager {
    private final JavaPlugin plugin;
    private final Random random = new Random();
    public SupplyCrateManager(JavaPlugin plugin) {
        this.plugin = plugin;
    }
    public void start() {
        Bukkit.getScheduler().runTaskTimer(plugin, this::spawnSupplyCrate, 20L * 60L * 10L, 20L * 60L * 30L);
    }
    private void spawnSupplyCrate() {
        World world = Bukkit.getWorlds().get(0);
        int x = random.nextInt(1201) - 600;
        int z = random.nextInt(1201) - 600;
        int y = world.getHighestBlockYAt(x, z) + 1;
        Location location = new Location(world, x, y, z);
        Block block = location.getBlock();
        block.setType(Material.CHEST);
        if (!(block.getState() instanceof Chest)) {
            return;
        }
        Chest chest = (Chest) block.getState();
        chest.getInventory().addItem(new ItemStack(Material.GOLDEN_APPLE, random.nextInt(3) + 1));
        chest.getInventory().addItem(new ItemStack(Material.ENDER_PEARL, random.nextInt(4) + 2));
        chest.getInventory().addItem(new ItemStack(Material.DIAMOND, random.nextInt(5) + 3));
        chest.getInventory().addItem(new ItemStack(Material.ARROW, 32));
        if (random.nextBoolean()) {
            chest.getInventory().addItem(new ItemStack(Material.TNT, random.nextInt(5) + 4));
        }
        chest.update();
        Bukkit.broadcastMessage("§6[보급] §eX:" + x + " Y:" + y + " Z:" + z + "§f에 보급상자가 생성되었습니다!");
    }
}
