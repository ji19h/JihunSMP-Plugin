        Bukkit.broadcastMessage("§c[현상금] §e" + killer.getName() + "§f님의 현상금이 §e"
                + newBounty + "원§f이 되었습니다!");
        handleKillStreakReward(killer, streak);
    }
    private void handleKillStreakReward(Player killer, int streak) {
        if (streak == 3) {
            playerDataManager.addCoins(killer, 500);
            killer.sendMessage("§6[킬스트릭] §e3연속 처치! 추가 500원 지급!");
        } else if (streak == 5) {
            killer.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 20 * 30, 0));
            Bukkit.broadcastMessage("§6[킬스트릭] §e" + killer.getName() + "§f님이 5연속 처치! 힘 버프 30초!");
        } else if (streak == 10) {
            playerDataManager.addCoins(killer, 2000);
            killer.getInventory().addItem(new ItemStack(Material.GOLDEN_APPLE, 3));
            Bukkit.broadcastMessage("§c[킬스트릭] " + killer.getName() + "님이 10연속 처치 중입니다!");
        }
    }
}
```
============================================================
4코드: src/main/java/com/jihun/listeners/EnhanceListener.java
============================================================
```java
package com.jihun.listeners;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import com.jihun.managers.PlayerDataManager;
import java.util.Random;
public class EnhanceListener implements Listener {
    private static final int MAX_LEVEL = 10;
    private static final int UPGRADE_COST = 100;
    private final PlayerDataManager playerDataManager;
    private final Random random = new Random();
    public EnhanceListener(PlayerDataManager playerDataManager) {
        this.playerDataManager = playerDataManager;
    }
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getHand() != EquipmentSlot.HAND) {
            return;
        }
        Action action = event.getAction();
        if (action != Action.RIGHT_CLICK_AIR && action != Action.RIGHT_CLICK_BLOCK) {
            return;
        }
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();
        if (item.getType() == Material.AIR) {
            return;
        }
        Enchantment enchantment = getTargetEnchantment(item.getType());
        if (enchantment == null) {
            return;
        }
        event.setCancelled(true);
        enhance(player, item, enchantment);
    }
    private void enhance(Player player, ItemStack item, Enchantment enchantment) {
        int currentLevel = item.getEnchantmentLevel(enchantment);
        if (currentLevel >= MAX_LEVEL) {
            player.sendMessage("§c이미 10강입니다.");
            return;
        }
        if (!playerDataManager.removeCoins(player, UPGRADE_COST)) {
            player.sendMessage("§c코인이 부족합니다. 강화에는 100원이 필요합니다.");
            return;
        }
        int nextLevel = currentLevel + 1;
        int chance = getSuccessChance(nextLevel);
        boolean success = random.nextInt(100) < chance;
        if (success) {
            item.addUnsafeEnchantment(enchantment, nextLevel);
            updateItemName(item, nextLevel);
            player.sendMessage("§a강화 성공! +" + nextLevel + "강 (" + chance + "%)");
        } else {
            player.sendMessage("§c강화 실패... 100원이 소모되었습니다. (" + chance + "%)");
        }
        player.sendMessage("§e남은 코인: §a" + playerDataManager.getCoins(player) + "원");
    }
    private int getSuccessChance(int targetLevel) {
        if (targetLevel >= 10) {
            return 1;
        }
        return 110 - (targetLevel * 10);
    }
    private Enchantment getTargetEnchantment(Material material) {
        String name = material.name();
        if (name.endsWith("_SWORD") || name.endsWith("_AXE")) {
            return Enchantment.DAMAGE_ALL;
        }
        if (name.endsWith("_HELMET") || name.endsWith("_CHESTPLATE")
                || name.endsWith("_LEGGINGS") || name.endsWith("_BOOTS")) {
            return Enchantment.PROTECTION_ENVIRONMENTAL;
        }
        return null;
    }
    private void updateItemName(ItemStack item, int level) {
        ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            return;
        }
        String baseName = getReadableName(item.getType());
        meta.setDisplayName("§b+" + level + "강 §f" + baseName);
        item.setItemMeta(meta);
    }
    private String getReadableName(Material material) {
        String[] parts = material.name().toLowerCase().split("_");
        StringBuilder builder = new StringBuilder();
        for (String part : parts) {
            if (builder.length() > 0) {
                builder.append(" ");
            }
            builder.append(Character.toUpperCase(part.charAt(0))).append(part.substring(1));
        }
        return builder.toString();
    }
}
