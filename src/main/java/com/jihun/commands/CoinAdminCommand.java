package com.jihun.commands;

import com.jihun.managers.PlayerDataManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CoinAdminCommand implements CommandExecutor {
    private final PlayerDataManager dataManager;

    public CoinAdminCommand(PlayerDataManager dataManager) {
        this.dataManager = dataManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("jihunsmp.admin")) {
            sender.sendMessage("§c권한이 없습니다.");
            return true;
        }
        if (args.length != 3) {
            sender.sendMessage("§c사용법: /coin <give|take|set> <플레이어> <금액>");
            return true;
        }
        Player target = Bukkit.getPlayerExact(args[1]);
        if (target == null) {
            sender.sendMessage("§c해당 플레이어가 접속 중이 아닙니다.");
            return true;
        }
        int amount;
        try {
            amount = Integer.parseInt(args[2]);
            if (amount < 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            sender.sendMessage("§c금액은 0 이상의 정수여야 합니다.");
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "give" -> dataManager.addCoins(target, amount);
            case "take" -> dataManager.setCoins(target, dataManager.getCoins(target) - amount);
            case "set" -> dataManager.setCoins(target, amount);
            default -> {
                sender.sendMessage("§c사용법: /coin <give|take|set> <플레이어> <금액>");
                return true;
            }
        }
        sender.sendMessage("§a" + target.getName() + "님의 코인이 "
                + dataManager.getCoins(target) + "원이 되었습니다.");
        target.sendMessage("§e관리자가 코인을 변경했습니다. 현재 " + dataManager.getCoins(target) + "원");
        return true;
    }
}
