package com.jihun.listeners;

import com.jihun.managers.PlayerDataManager;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

public class EnhanceListener implements Listener {
    private static final int COST = 100;

    private static final double[] SWORD_ARMOR_CHANCES = {
            90, 80, 70, 60, 50, 40, 30, 20, 10, 1
    };

    private static final double[] TOOL_CHANCES = {
            90, 80, 70, 60, 50, 10, 5, 1, 0.01, 0.001
    };

    private static final Set<Material> SWORDS = Set.of(
            Material.WOODEN_SWORD, Material.STONE_SWORD, Material.IRON_SWORD,
            Material.GOLDEN_SWORD, Material.DIAMOND_SWORD, Material.NETHERITE_SWORD
    );

    private static final Set<Material> AXES = Set.of(
            Material.WOODEN_AXE, Material.STONE_AXE, Material.IRON_AXE,
            Material.GOLDEN_AXE, Material.DIAMOND_AXE, Material.NETHERITE_AXE
    );

    private static final Set<Material> PICKAXES = Set.of(
            Material.WOODEN_PICKAXE, Material.STONE_PICKAXE, Material.IRON_PICKAXE,
            Material.GOLDEN_PICKAXE, Material.DIAMOND_PICKAXE, Material.NETHERITE_PICKAXE
    );

    private final PlayerDataManager dataManager;

    public EnhanceListener(PlayerDataManager dataManager) {
        this.dataManager = dataManager;
    }

    @EventHandler
    public void onShiftRightClick(PlayerInteractEvent event) {
        if (event.getHand() != EquipmentSlot.HAND) return;
        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        Player player = event.getPlayer();
        if (!player.isSneaking()) return;

        ItemStack item = event.getItem();
        if (item == null || item.getType().isAir()) return;

        EnhanceType enhanceType = getEnhanceType(item.getType());
        if (enhanceType == null) return;

        event.setCancelled(true);

        Enchantment enchantment = enhanceType.enchantment;
        int current = item.getEnchantmentLevel(enchantment);

        if (current >= 10) {
            player.sendMessage("§e이미 10강입니다.");
            return;
        }

        if (!dataManager.removeCoins(player, COST)) {
            player.sendMessage("§c강화에는 100원이 필요합니다. 현재 코인: " + dataManager.getCoins(player));
            return;
        }

        int next = current + 1;
        double chance = enhanceType.chances[next - 1];

        if (ThreadLocalRandom.current().nextDouble(100.0) < chance) {
            if (AXES.contains(item.getType())) {
                Enchantment sharpness = Enchantment.getByKey(NamespacedKey.minecraft("sharpness"));
                if (sharpness != null) item.removeEnchantment(sharpness);
            }

            item.addUnsafeEnchantment(enchantment, next);
            player.sendMessage("§a강화 성공! §f" + next + "강 §7(성공 확률 " + formatChance(chance) + "%)");
        } else {
            player.sendMessage("§c강화 실패! §7아이템은 유지됩니다. (성공 확률 " + formatChance(chance) + "%)");
        }
    }

    @EventHandler
    public void onPrepareAnvil(PrepareAnvilEvent event) {
        ItemStack left = event.getInventory().getItem(0);
        ItemStack right = event.getInventory().getItem(1);
        ItemStack result = event.getResult();

        if (left == null || right == null || result == null) return;
        if (left.getType().isAir() || right.getType().isAir() || result.getType().isAir()) return;

        EnhanceType enhanceType = getEnhanceType(left.getType());
        if (enhanceType == null) return;

        Enchantment enchantment = enhanceType.enchantment;

        int leftLevel = left.getEnchantmentLevel(enchantment);
        int rightLevel = right.getEnchantmentLevel(enchantment);

        if (leftLevel <= 0 || rightLevel <= 0) return;
        if (leftLevel != rightLevel) return;

        int nextLevel = Math.min(leftLevel + 1, 10);
        if (nextLevel <= result.getEnchantmentLevel(enchantment)) return;

        ItemStack fixedResult = result.clone();
        fixedResult.addUnsafeEnchantment(enchantment, nextLevel);

        event.setResult(fixedResult);
        event.getInventory().setRepairCost(Math.min(39, nextLevel * 2));
    }

    private EnhanceType getEnhanceType(Material material) {
        Enchantment sharpness = Enchantment.getByKey(NamespacedKey.minecraft("sharpness"));
        Enchantment protection = Enchantment.getByKey(NamespacedKey.minecraft("protection"));
        Enchantment efficiency = Enchantment.getByKey(NamespacedKey.minecraft("efficiency"));

        if (SWORDS.contains(material) && sharpness != null) {
            return new EnhanceType(sharpness, SWORD_ARMOR_CHANCES);
        }

        if (isArmor(material) && protection != null) {
            return new EnhanceType(protection, SWORD_ARMOR_CHANCES);
        }

        if ((AXES.contains(material) || PICKAXES.contains(material)) && efficiency != null) {
            return new EnhanceType(efficiency, TOOL_CHANCES);
        }

        return null;
    }

    private boolean isArmor(Material material) {
        String name = material.name();
        return name.endsWith("_HELMET")
                || name.endsWith("_CHESTPLATE")
                || name.endsWith("_LEGGINGS")
                || name.endsWith("_BOOTS");
    }

    private String formatChance(double chance) {
        if (chance == Math.rint(chance)) {
            return Integer.toString((int) chance);
        }
        return Double.toString(chance);
    }

    private static class EnhanceType {
        private final Enchantment enchantment;
        private final double[] chances;

        private EnhanceType(Enchantment enchantment, double[] chances) {
            this.enchantment = enchantment;
            this.chances = chances;
        }
    }
}
