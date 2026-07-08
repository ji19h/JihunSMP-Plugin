package com.jihun.commands;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import com.jihun.managers.PlayerDataManager;
public class BountyCommand implements CommandExecutor {
    private final PlayerDataManager playerDataManager;
    public BountyCommand(PlayerDataManager playerDataManager) {
        this.playerDataManager = playerDataManager;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        sender.sendMessage("§6===== 현상금 목록 =====");
        sender.sendMessage(playerDataManager.getBountyListText());
        sender.sendMessage("§7현상금은 플레이어를 처치할 때마다 자동으로 100원씩 증가합니다.");
        return true;
    }
}
