package com.jihun.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import com.jihun.managers.TPAManager;

public class TPACommand implements CommandExecutor {

    private TPAManager tpaManager;

    public TPACommand(TPAManager tpaManager) {
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
            player.sendMessage("§c사용법: /tpa <플레이어명>");
            return true;
        }

        String targetName = args[0];
        Player target = player.getServer().getPlayer(targetName);

        if (target == null) {
            player.sendMessage("§c" + targetName + "님을 찾을 수 없습니다.");
            return true;
        }

        if (target.equals(player)) {
            player.sendMessage("§c자신에게는 TPA를 할 수 없습니다.");
            return true;
        }

        tpaManager.sendTPARequest(player, target);
        return true;
    }
}