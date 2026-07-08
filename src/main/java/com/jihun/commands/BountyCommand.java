package com.jihun.commands;

import com.jihun.managers.PlayerData;
import com.jihun.managers.PlayerDataManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.Comparator;
import java.util.List;

public class BountyCommand implements CommandExecutor {
    private final PlayerDataManager dataManager;

    public BountyCommand(PlayerDataManager dataManager) {
        this.dataManager = dataManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        List<PlayerData> targets = dataManager.getAllPlayerData().stream()
                .filter(data -> dataManager.getBounty(data.getUuid()) > 0)
                .sorted(Comparator.comparingInt((PlayerData data) ->
                        dataManager.getBounty(data.getUuid())).reversed())
                .toList();
        sender.sendMessage("§6===== 현상금 순위 =====");
        if (targets.isEmpty()) {
            sender.sendMessage("§7현재 현상금이 걸린 플레이어가 없습니다.");
            return true;
        }
        for (int i = 0; i < Math.min(10, targets.size()); i++) {
            PlayerData target = targets.get(i);
            sender.sendMessage("§e" + (i + 1) + ". §f" + target.getPlayerName()
                    + " §c" + dataManager.getBounty(target.getUuid()) + "원");
        }
        return true;
    }
}
