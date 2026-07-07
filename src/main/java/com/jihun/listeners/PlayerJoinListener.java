package com.jihun.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import com.jihun.managers.PlayerDataManager;

public class PlayerJoinListener implements Listener {

    private JavaPlugin plugin;
    private PlayerDataManager playerDataManager;

    public PlayerJoinListener(JavaPlugin plugin, PlayerDataManager playerDataManager) {
        this.plugin = plugin;
        this.playerDataManager = playerDataManager;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        var playerData = playerDataManager.getPlayerData(event.getPlayer());
        event.getPlayer().sendMessage("§6========================================");
        event.getPlayer().sendMessage("§e지훈 SMP에 오신 것을 환영합니다!");
        event.getPlayer().sendMessage("§a초기 코인: 100원");
        event.getPlayer().sendMessage("§b팀 선택: /team <RED|BLUE>");
        event.getPlayer().sendMessage("§6========================================");
    }
}