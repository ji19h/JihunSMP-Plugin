package com.jihun.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import com.jihun.managers.PlayerDataManager;

public class PlayerDeathListener implements Listener {

    private JavaPlugin plugin;
    private PlayerDataManager playerDataManager;

    public PlayerDeathListener(JavaPlugin plugin, PlayerDataManager playerDataManager) {
        this.plugin = plugin;
        this.playerDataManager = playerDataManager;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player victim = event.getEntity();
        Player killer = victim.getKiller();

        if (killer != null) {
            // 킬러에게 300원 지급
            playerDataManager.addCoins(killer, 300);
            playerDataManager.addKill(killer);
            
            killer.sendMessage("§a+300원을 얻었습니다! (킬: " + playerDataManager.getKillCount(killer) + ")");
            victim.sendMessage("§c-300원을 잃었습니다.");
            
            // 피해자의 코인 감소
            playerDataManager.removeCoins(victim, 300);
        }
    }
}