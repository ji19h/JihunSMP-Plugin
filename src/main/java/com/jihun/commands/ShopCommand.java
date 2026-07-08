package com.jihun.commands;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import com.jihun.managers.PlayerDataManager;
public class ShopCommand implements CommandExecutor {
    private final PlayerDataManager playerDataManager;
    public ShopCommand(PlayerDataManager playerDataManager) {
        this.playerDataManager = playerDataManager;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("플레이어만 사용 가능합니다.");
            return true;
        }
        Player player = (Player) sender;
        if (args.length == 0 || args[0].equalsIgnoreCase("list")) {
            sendShopList(player);
            return true;
        }
        if (args.length != 2 || !args[0].equalsIgnoreCase("buy")) {
            player.sendMessage("§c사용법: /shop buy <아이템>");
            return true;
        }
        buy(player, args[1].toLowerCase());
        return true;
    }
    private void sendShopList(Player player) {
        player.sendMessage("§6===== 상점 =====");
        player.sendMessage("§e/shop buy gapple §7- 황금사과 1개 / 500원");
        player.sendMessage("§e/shop buy pearl §7- 엔더진주 4개 / 400원");
        player.sendMessage("§e/shop buy arrows §7- 화살 32개 / 200원");
        player.sendMessage("§e/shop buy tnt §7- TNT 8개 / 700원");
        player.sendMessage("§e/shop buy strength §7- 힘 버프 30초 / 800원");
    }
    private void buy(Player player, String itemName) {
        int cost;
        switch (itemName) {
            case "gapple":
                cost = 500;
                if (!charge(player, cost)) return;
                player.getInventory().addItem(new ItemStack(Material.GOLDEN_APPLE, 1));
                break;
            case "pearl":
                cost = 400;
                if (!charge(player, cost)) return;
                player.getInventory().addItem(new ItemStack(Material.ENDER_PEARL, 4));
                break;
            case "arrows":
                cost = 200;
                if (!charge(player, cost)) return;
                player.getInventory().addItem(new ItemStack(Material.ARROW, 32));
                break;
            case "tnt":
                cost = 700;
                if (!charge(player, cost)) return;
                player.getInventory().addItem(new ItemStack(Material.TNT, 8));
                break;
            case "strength":
                cost = 800;
                if (!charge(player, cost)) return;
                player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 20 * 30, 0));
                break;
            default:
                player.sendMessage("§c없는 상품입니다. /shop 으로 목록을 확인하세요.");
                return;
        }
        player.sendMessage("§a구매 완료! 남은 코인: " + playerDataManager.getCoins(player) + "원");
    }
    private boolean charge(Player player, int cost) {
        if (!playerDataManager.removeCoins(player, cost)) {
            player.sendMessage("§c코인이 부족합니다. 필요 코인: " + cost + "원");
            return false;
        }
        return true;
    }
}
