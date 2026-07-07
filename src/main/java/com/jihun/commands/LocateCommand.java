package com.jihun.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Material;
import org.bukkit.plugin.java.JavaPlugin;
import com.jihun.managers.PlayerDataManager;

public class LocateCommand implements CommandExecutor {

    private JavaPlugin plugin;
    private PlayerDataManager playerDataManager;

    public LocateCommand(JavaPlugin plugin, PlayerDataManager playerDataManager) {
        this.plugin = plugin;
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
            player.sendMessage("§c사용법: /locate <플레이어명>");
            return true;
        }

        // 다이아 확인
        if (!player.getInventory().contains(Material.DIAMOND, 1)) {
            player.sendMessage("§c다이아 1개가 필요합니다.");
            return true;
        }

        String targetName = args[0];
        Player target = player.getServer().getPlayer(targetName);

        if (target == null) {
            player.sendMessage("§c" + targetName + "님을 찾을 수 없습니다.");
            return true;
        }

        if (target.equals(player)) {
            player.sendMessage("§c자신의 위치는 볼 수 없습니다.");
            return true;
        }

        // 다이아 1개 소비
        ItemStack diamond = new ItemStack(Material.DIAMOND, 1);
        player.getInventory().removeItem(diamond);

        // 3초간 액션바에 좌표 표시
        int x = target.getLocation().getBlockX();
        int y = target.getLocation().getBlockY();
        int z = target.getLocation().getBlockZ();
        String coordinates = "§e" + targetName + "의 위치: X:" + x + " Y:" + y + " Z:" + z;
        
        player.sendActionBar(coordinates);

        // 3초 후 메시지로 한 번 더 표시
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            player.sendActionBar("§c위치 추적 종료");
        }, 60L); // 3초 = 60틱

        player.sendMessage("§a다이아 1개를 소비하여 위치를 확인했습니다.");
        return true;
    }
}