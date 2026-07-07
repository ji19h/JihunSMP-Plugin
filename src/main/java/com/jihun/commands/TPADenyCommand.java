package com.jihun.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import com.jihun.managers.TPAManager;

public class TPADenyCommand implements CommandExecutor {

    private TPAManager tpaManager;

    public TPADenyCommand(TPAManager tpaManager) {
        this.tpaManager = tpaManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("플레이어만 사용 가능합니다.");
            return true;
        }

        Player player = (Player) sender;

        if (args.length == 0) {
            player.sendMessage("§c사용법: /tpadeny <플레이어명>");
            return true;
        }

        String requesterName = args[0];
        Player requester = player.getServer().getPlayer(requesterName);

        if (requester == null) {
            player.sendMessage("§c" + requesterName + "님을 찾을 수 없습니다.");
            return true;
        }

        tpaManager.denyTPA(player, requester);
        return true;
    }
}