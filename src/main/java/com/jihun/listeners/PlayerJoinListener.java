package com.jihun.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import com.jihun.managers.PlayerDataManager;

public class PlayerJoinListener implements Listener {

    private final JavaPlugin plugin;
    private final PlayerDataManager playerDataManager;

    // 생성자 추가
    public PlayerJoinListener(JavaPlugin plugin, PlayerDataManager playerDataManager) {
        this.plugin = plugin;
        this.playerDataManager = playerDataManager;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        event.getPlayer().sendMessage("§a서버에 오신 것을 환영합니다!");

        // 필요하다면 playerDataManager 사용 가능
        playerDataManager.addCoins(event.getPlayer(), 100); // 예시: 접속 시 100코인 지급
    }
}
