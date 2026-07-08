package com.jihun.commands;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import com.jihun.managers.PlayerDataManager;
public class CoinAdminCommand implements CommandExecutor {
    private final PlayerDataManager playerDataManager;
    public CoinAdminCommand(PlayerDataManager playerDataManager) {
        this.playerDataManager = playerDataManager;
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
            sender.sendMessage("§c접속 중인 플레이어만 수정할 수 있습니다.");
            return true;
        }
        int amount;
        try {
            amount = Integer.parseInt(args[2]);
        } catch (NumberFormatException e) {
            sender.sendMessage("§c금액은 숫자로 입력해야 합니다.");
            return true;
        }
        if (amount < 0) {
            sender.sendMessage("§c금액은 0 이상이어야 합니다.");
            return true;
        }
        String action = args[0].toLowerCase();
        if (action.equals("give")) {
            playerDataManager.addCoins(target, amount);
            sender.sendMessage("§a" + target.getName() + "님에게 " + amount + "원을 지급했습니다.");
        } else if (action.equals("take")) {
            playerDataManager.removeCoins(target, amount);
            sender.sendMessage("§a" + target.getName() + "님에게서 " + amount + "원을 차감했습니다.");
        } else if (action.equals("set")) {
            playerDataManager.setCoins(target, amount);
            sender.sendMessage("§a" + target.getName() + "님의 코인을 " + amount + "원으로 설정했습니다.");
        } else {
            sender.sendMessage("§c사용법: /coin <give|take|set> <플레이어> <금액>");
        }
        return true;
    }
}
