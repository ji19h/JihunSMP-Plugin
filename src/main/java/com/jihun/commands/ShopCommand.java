package com.jihun.commands;

import com.jihun.managers.PlayerDataManager;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ShopCommand implements CommandExecutor {
    private final PlayerDataManager dataManager;

    public ShopCommand(PlayerDataManager dataManager) {
        this.dataManager = dataManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("플레이어만 사용할 수 있습니다.");
            return true;
        }
        if (args.length == 0) {
            player.sendMessage("§6===== 코인 상점 =====");
            player.sendMessage("§e/shop buy 1 §f- 황금사과 1개 §a300원");
            player.sendMessage("§e/shop buy 2 §f- 엔더진주 4개 §a400원");
            player.sendMessage("§e/shop buy 3 §f- 다이아몬드 1개 §a500원");
            player.sendMessage("§e/shop buy 4 §f- 경험치 병 16개 §a600원");
            player.sendMessage("§e/shop buy 5 §f- 네더라이트 주괴 1개 §a3000원");
            return true;
        }
        if (args.length != 2 || !args[0].equalsIgnoreCase("buy")) {
            player.sendMessage("§c사용법: /shop 또는 /shop buy <번호>");
            return true;
        }

        Material material;
        int amount;
        int price;
        switch (args[1]) {
            case "1" -> { material = Material.GOLDEN_APPLE; amount = 1; price = 300; }
            case "2" -> { material = Material.ENDER_PEARL; amount = 4; price = 400; }
            case "3" -> { material = Material.DIAMOND; amount = 1; price = 500; }
            case "4" -> { material = Material.EXPERIENCE_BOTTLE; amount = 16; price = 600; }
            case "5" -> { material = Material.NETHERITE_INGOT; amount = 1; price = 3000; }
            default -> {
                player.sendMessage("§c상품 번호는 1~5입니다.");
                return true;
            }
        }
        if (!dataManager.removeCoins(player, price)) {
            player.sendMessage("§c코인이 부족합니다. 현재 " + dataManager.getCoins(player) + "원");
            return true;
        }
        player.getInventory().addItem(new ItemStack(material, amount)).values()
                .forEach(item -> player.getWorld().dropItemNaturally(player.getLocation(), item));
        player.sendMessage("§a구매 완료! §f" + material + " x" + amount);
        return true;
    }
}
