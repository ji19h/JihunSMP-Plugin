package com.jihun.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.plugin.java.JavaPlugin;

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

        if (killer == null || killer.equals(victim)) {
            return;
        }

        playerDataManager.addCoins(killer, 300);
        playerDataManager.addKill(killer);

        killer.sendMessage("§a플레이어 처치! +300원 (킬: " + playerDataManager.getKillCount(killer) + ")");
        victim.sendMessage("§c" + killer.getName() + "님에게 처치당했습니다.");
    }
}
