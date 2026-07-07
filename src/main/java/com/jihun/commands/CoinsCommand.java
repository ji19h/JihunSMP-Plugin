package com.jihun.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import com.jihun.managers.PlayerDataManager;

public class CoinsCommand implements CommandExecutor {

    private PlayerDataManager playerDataManager;

    public CoinsCommand(PlayerDataManager playerDataManager) {
        this.playerDataManager = playerDataManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("플레이어만 사용 가능합니다.");
            return true;
        }

        Player player = (Player) sender;
        int coins = playerDataManager.getCoins(player);

        player.sendMessage("§6========================================");
        player.sendMessage("§e현재 코인: §a" + coins + "원");
        player.sendMessage("§6========================================");
        return true;
    }
}