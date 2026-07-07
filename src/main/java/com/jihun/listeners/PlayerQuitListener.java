package com.jihun.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import com.jihun.managers.PlayerDataManager;

public class PlayerQuitListener implements Listener {

    private JavaPlugin plugin;
    private PlayerDataManager playerDataManager;

    public PlayerQuitListener(JavaPlugin plugin, PlayerDataManager playerDataManager) {
        this.plugin = plugin;
        this.playerDataManager = playerDataManager;
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        playerDataManager.removePlayerData(event.getPlayer());
    }
}