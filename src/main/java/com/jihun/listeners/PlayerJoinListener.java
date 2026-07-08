        addCoins(killer, bounty);
        return bounty;
    }
    public String getBountyListText() {
        if (!dataConfig.contains("bounties")) {
            return "§7등록된 현상금이 없습니다.";
        }
        Set<String> keys = dataConfig.getConfigurationSection("bounties").getKeys(false);
        if (keys.isEmpty()) {
            return "§7등록된 현상금이 없습니다.";
        }
        StringBuilder builder = new StringBuilder();
        for (String key : keys) {
            String name = dataConfig.getString("bounties." + key + ".name", "Unknown");
            int amount = dataConfig.getInt("bounties." + key + ".amount", 0);
            if (amount > 0) {
                builder.append("§c").append(name).append(" §f- §e").append(amount).append("원\n");
            }
        }
        return builder.length() == 0 ? "§7등록된 현상금이 없습니다." : builder.toString();
    }
    public OfflinePlayer findOfflinePlayer(String name) {
        Player online = Bukkit.getPlayerExact(name);
        if (online != null) {
            return online;
        }
        for (OfflinePlayer offlinePlayer : Bukkit.getOfflinePlayers()) {
            if (offlinePlayer.getName() != null && offlinePlayer.getName().equalsIgnoreCase(name)) {
                return offlinePlayer;
            }
        }
        return null;
    }
}
```
============================================================
3코드: src/main/java/com/jihun/listeners/PlayerDeathListener.java
============================================================
```java
package com.jihun.listeners;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import com.jihun.managers.PlayerDataManager;
public class PlayerDeathListener implements Listener {
    private final JavaPlugin plugin;
    private final PlayerDataManager playerDataManager;
    public PlayerDeathListener(JavaPlugin plugin, PlayerDataManager playerDataManager) {
        this.plugin = plugin;
        this.playerDataManager = playerDataManager;
    }
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player victim = event.getEntity();
        Player killer = victim.getKiller();
        playerDataManager.resetKillStreak(victim);
        if (killer == null || killer.equals(victim)) {
            return;
        }
        int claimedBounty = playerDataManager.claimBounty(killer, victim);
        playerDataManager.addCoins(killer, 300);
        playerDataManager.addKill(killer);
        int streak = playerDataManager.addKillStreak(killer);
        int newBounty = playerDataManager.increaseBounty(killer, 100);
        killer.sendMessage("§a플레이어 처치! +300원 (킬: " + playerDataManager.getKillCount(killer) + ")");
        killer.sendMessage("§c당신의 현상금이 " + newBounty + "원으로 증가했습니다.");
        victim.sendMessage("§c" + killer.getName() + "님에게 처치당했습니다.");
        if (claimedBounty > 0) {
            Bukkit.broadcastMessage("§6[현상금] §e" + killer.getName() + "§f님이 §c" + victim.getName()
                    + "§f님의 현상금 §e" + claimedBounty + "원§f을 획득했습니다!");
        }
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
