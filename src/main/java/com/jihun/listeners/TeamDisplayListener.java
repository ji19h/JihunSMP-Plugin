package com.jihun.listeners;

import com.jihun.commands.TeamCommand;
import com.jihun.managers.PlayerDataManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class TeamDisplayListener implements Listener {
    private final PlayerDataManager dataManager;

    public TeamDisplayListener(PlayerDataManager dataManager) {
        this.dataManager = dataManager;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        TeamCommand.applyTeamColor(dataManager, event.getPlayer());
    }
}
