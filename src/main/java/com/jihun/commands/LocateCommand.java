package com.jihun.commands;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import com.jihun.managers.PlayerDataManager;
public class LocateCommand implements CommandExecutor {
    private static final int DIAMOND_COST = 5;
    private final JavaPlugin plugin;
    private final PlayerDataManager playerDataManager;
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
        if (!player.getInventory().contains(Material.DIAMOND, DIAMOND_COST)) {
            player.sendMessage("§c다이아 " + DIAMOND_COST + "개가 필요합니다.");
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
        player.getInventory().removeItem(new ItemStack(Material.DIAMOND, DIAMOND_COST));
        int x = target.getLocation().getBlockX();
        int y = target.getLocation().getBlockY();
        int z = target.getLocation().getBlockZ();
        String coordinates = "§e" + targetName + "의 위치: X:" + x + " Y:" + y + " Z:" + z;
        player.sendActionBar(coordinates);
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            player.sendActionBar("§c위치 추적 종료");
        }, 60L);
        player.sendMessage("§a다이아 " + DIAMOND_COST + "개를 소비하여 위치를 확인했습니다.");
        return true;
    }
}
