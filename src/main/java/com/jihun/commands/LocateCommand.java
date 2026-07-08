package com.jihun.commands;

import com.jihun.managers.PlayerDataManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class LocateCommand implements CommandExecutor {
    private static final int DIAMOND_COST = 5;

    public LocateCommand(JavaPlugin plugin, PlayerDataManager dataManager) {
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("플레이어만 사용할 수 있습니다.");
            return true;
        }
        if (args.length != 1) {
            player.sendMessage("§c사용법: /locate <플레이어>");
            return true;
        }
        Player target = Bukkit.getPlayerExact(args[0]);
        if (target == null || !target.isOnline()) {
            player.sendMessage("§c접속 중인 플레이어를 찾을 수 없습니다.");
            return true;
        }
        if (target.equals(player)) {
            player.sendMessage("§c자기 자신은 추적할 수 없습니다.");
            return true;
        }
        if (!player.getInventory().containsAtLeast(new ItemStack(Material.DIAMOND), DIAMOND_COST)) {
            player.sendMessage("§c추적에는 다이아몬드 5개가 필요합니다.");
            return true;
        }
        player.getInventory().removeItem(new ItemStack(Material.DIAMOND, DIAMOND_COST));
        Location location = target.getLocation();
        player.sendMessage("§6" + target.getName() + "님의 위치");
        player.sendMessage("§e월드: §f" + location.getWorld().getName());
        player.sendMessage("§e좌표: §fX " + location.getBlockX() + ", Y " + location.getBlockY()
                + ", Z " + location.getBlockZ());
        return true;
    }
}
