package com.jihun.listeners;

import com.jihun.managers.PlayerDataManager;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

public class EnhanceListener implements Listener {
    private static final int COST = 100;
    private static final double[] CHANCES = {100, 90, 80, 70, 60, 50, 40, 30, 20, 1};
    private static final Set<Material> WEAPONS = Set.of(
            Material.WOODEN_SWORD, Material.STONE_SWORD, Material.IRON_SWORD,
            Material.GOLDEN_SWORD, Material.DIAMOND_SWORD, Material.NETHERITE_SWORD,
            Material.WOODEN_AXE, Material.STONE_AXE, Material.IRON_AXE,
            Material.GOLDEN_AXE, Material.DIAMOND_AXE, Material.NETHERITE_AXE);
    private final PlayerDataManager dataManager;

    public EnhanceListener(PlayerDataManager dataManager) {
        this.dataManager = dataManager;
    }

    @EventHandler
    public void onRightClick(PlayerInteractEvent event) {
        if (event.getHand() != EquipmentSlot.HAND) return;
        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        ItemStack item = event.getItem();
        if (item == null || item.getType().isAir()) return;

        Enchantment enchantment;
        if (WEAPONS.contains(item.getType())) {
            enchantment = Enchantment.getByKey(NamespacedKey.minecraft("sharpness"));
        } else if (isArmor(item.getType())) {
            enchantment = Enchantment.getByKey(NamespacedKey.minecraft("protection"));
        } else {
            return;
        }

        if (enchantment == null) return;
        event.setCancelled(true);
        Player player = event.getPlayer();
        int current = item.getEnchantmentLevel(enchantment);
        if (current >= 10) {
            player.sendMessage("§e이미 10강인 장비입니다.");
            return;
        }
        if (!dataManager.removeCoins(player, COST)) {
            player.sendMessage("§c강화에는 100원이 필요합니다. 현재 " + dataManager.getCoins(player) + "원");
            return;
        }

        int next = current + 1;
        double chance = CHANCES[next - 1];
        if (ThreadLocalRandom.current().nextDouble(100.0) < chance) {
            item.addUnsafeEnchantment(enchantment, next);
            player.sendMessage("§a강화 성공! §f" + next + "강 §7(성공 확률 " + formatChance(chance) + "%)");
        } else {
            player.sendMessage("§c강화 실패! §7장비는 유지됩니다. (성공 확률 " + formatChance(chance) + "%)");
        }
    }

    private boolean isArmor(Material material) {
        String name = material.name();
        return name.endsWith("_HELMET") || name.endsWith("_CHESTPLATE")
                || name.endsWith("_LEGGINGS") || name.endsWith("_BOOTS");
    }

    private String formatChance(double chance) {
        return chance == Math.rint(chance) ? Integer.toString((int) chance) : Double.toString(chance);
    }
}
