package com.jihun.listeners;

import com.jihun.managers.PlayerDataManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class PlayerDeathListener implements Listener {
    private final PlayerDataManager dataManager;

    public PlayerDeathListener(JavaPlugin plugin, PlayerDataManager dataManager) {
        this.dataManager = dataManager;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player victim = event.getEntity();
        dataManager.resetKillStreak(victim);
        Player killer = victim.getKiller();
        if (killer == null || killer.equals(victim)) return;

        dataManager.addKill(killer);
        int streak = dataManager.addKillStreak(killer);
        dataManager.addCoins(killer, 300);
        dataManager.addBounty(killer, 100);
        int claimed = dataManager.takeBounty(victim);
        if (claimed > 0) dataManager.addCoins(killer, claimed);

        killer.sendMessage("§a킬 보상으로 300원을 받았습니다.");
        killer.sendMessage("§6현재 내 현상금: " + dataManager.getBounty(killer) + "원");
        if (claimed > 0) {
            Bukkit.broadcastMessage("§c" + killer.getName() + "님이 " + victim.getName()
                    + "님의 현상금 " + claimed + "원을 획득했습니다!");
        }
        if (streak >= 3) {
            Bukkit.broadcastMessage("§e" + killer.getName() + "님이 " + streak + " 연속 킬 중입니다!");
        }
    }
}
