package com.jihun;

import com.jihun.commands.*;
import com.jihun.listeners.*;
import com.jihun.managers.PlayerDataManager;
import com.jihun.managers.SupplyCrateManager;
import com.jihun.managers.TPAManager;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

public class jihunSMP extends JavaPlugin {
    private PlayerDataManager playerDataManager;
    private TPAManager tpaManager;

    @Override
    public void onEnable() {
        playerDataManager = new PlayerDataManager(this);
        tpaManager = new TPAManager(this);

        getServer().getPluginManager().registerEvents(new PlayerDeathListener(this, playerDataManager), this);
        getServer().getPluginManager().registerEvents(new PlayerQuitListener(this, playerDataManager), this);
        getServer().getPluginManager().registerEvents(new EnhanceListener(playerDataManager), this);
        getServer().getPluginManager().registerEvents(new TeamDamageListener(playerDataManager), this);
        getServer().getPluginManager().registerEvents(new CombatLogListener(this), this);

        command("tpa").setExecutor(new TPACommand(tpaManager));
        command("tpaccept").setExecutor(new TPAAcceptCommand(tpaManager));
        command("tpadeny").setExecutor(new TPADenyCommand(tpaManager));
        command("locate").setExecutor(new LocateCommand(this, playerDataManager));
        command("coins").setExecutor(new CoinsCommand(playerDataManager));
        command("coin").setExecutor(new CoinAdminCommand(playerDataManager));
        command("bounty").setExecutor(new BountyCommand(playerDataManager));
        command("shop").setExecutor(new ShopCommand(playerDataManager));
        command("team").setExecutor(new TeamCommand(playerDataManager));
        command("myteam").setExecutor(new MyTeamCommand(playerDataManager));
        command("kills").setExecutor(new KillsCommand(playerDataManager));

        new SupplyCrateManager(this).start();
        getLogger().info("JihunSMP Plugin v1.1.0 활성화!");
    }

    private PluginCommand command(String name) {
        PluginCommand command = getCommand(name);
        if (command == null) throw new IllegalStateException("plugin.yml에 명령어가 없습니다: " + name);
        return command;
    }

    @Override
    public void onDisable() {
        if (playerDataManager != null) playerDataManager.saveAllData();
    }

    public PlayerDataManager getPlayerDataManager() { return playerDataManager; }
    public TPAManager getTPAManager() { return tpaManager; }
}
