package com.jihun;

import org.bukkit.plugin.java.JavaPlugin;
import com.jihun.listeners.*;
import com.jihun.commands.*;
import com.jihun.managers.*;

public class jihunSMP extends JavaPlugin {

    private PlayerDataManager playerDataManager;
    private TPAManager tpaManager;

    @Override
    public void onEnable() {
        getLogger().info("========================================");
        getLogger().info("  JihunSMP Plugin v1.0.0 활성화!");
        getLogger().info("========================================");

        // 데이터 매니저 초기화
        playerDataManager = new PlayerDataManager(this);
        tpaManager = new TPAManager(this);

        // 리스너 등록
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(this, playerDataManager), this);
        getServer().getPluginManager().registerEvents(new PlayerDeathListener(this, playerDataManager), this);
        getServer().getPluginManager().registerEvents(new PlayerQuitListener(this, playerDataManager), this);

        // 커맨드 등록
        getCommand("tpa").setExecutor(new TPACommand(tpaManager));
        getCommand("tpaccept").setExecutor(new TPAAcceptCommand(tpaManager));
        getCommand("tpadeny").setExecutor(new TPADenyCommand(tpaManager));
        getCommand("locate").setExecutor(new LocateCommand(this, playerDataManager));
        getCommand("coins").setExecutor(new CoinsCommand(playerDataManager));
        getCommand("team").setExecutor(new TeamCommand(playerDataManager));
        getCommand("myteam").setExecutor(new MyTeamCommand(playerDataManager));
        getCommand("kills").setExecutor(new KillsCommand(playerDataManager));
    }

    @Override
    public void onDisable() {
        if (playerDataManager != null) {
            playerDataManager.saveAllData();
        }
        getLogger().info("JihunSMP Plugin 비활성화!");
    }

    public PlayerDataManager getPlayerDataManager() {
        return playerDataManager;
    }

    public TPAManager getTPAManager() {
        return tpaManager;
    }
}