package com.jihun.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import com.jihun.managers.PlayerDataManager;

public class TeamCommand implements CommandExecutor {

    private PlayerDataManager playerDataManager;

    public TeamCommand(PlayerDataManager playerDataManager) {
        this.playerDataManager = playerDataManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("플레이어만 사용 가능합니다.");
            return true;
        }

        Player player = (Player) sender;

        if (args.length == 0) {
            player.sendMessage("§c사용법: /team <RED|BLUE>");
            return true;
        }

        String teamName = args[0].toUpperCase();

        if (!teamName.equals("RED") && !teamName.equals("BLUE")) {
            player.sendMessage("§cRED 또는 BLUE만 선택 가능합니다.");
            return true;
        }

        playerDataManager.setTeam(player, teamName);
        
        if (teamName.equals("RED")) {
            player.sendMessage("§c레드 팀에 참가했습니다!");
            player.setDisplayName("§c[RED] " + player.getName());
        } else {
            player.sendMessage("§b블루 팀에 참가했습니다!");
            player.setDisplayName("§b[BLUE] " + player.getName());
        }

        return true;
    }
}