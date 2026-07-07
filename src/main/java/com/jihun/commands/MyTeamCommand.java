package com.jihun.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import com.jihun.managers.PlayerDataManager;

public class MyTeamCommand implements CommandExecutor {

    private PlayerDataManager playerDataManager;

    public MyTeamCommand(PlayerDataManager playerDataManager) {
        this.playerDataManager = playerDataManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("플레이어만 사용 가능합니다.");
            return true;
        }

        Player player = (Player) sender;
        String team = playerDataManager.getTeam(player);

        if (team.equals("NONE")) {
            player.sendMessage("§e아직 팀에 참가하지 않았습니다. /team <RED|BLUE>");
        } else if (team.equals("RED")) {
            player.sendMessage("§c현재 팀: RED");
        } else {
            player.sendMessage("§b현재 팀: BLUE");
        }

        return true;
    }
}