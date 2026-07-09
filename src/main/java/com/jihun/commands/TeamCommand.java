package com.jihun.commands;

import com.jihun.managers.PlayerDataManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class TeamCommand implements CommandExecutor {
    private static final String RED_TEAM = "JihunSMP_RED";
    private static final String BLUE_TEAM = "JihunSMP_BLUE";

    private final PlayerDataManager dataManager;

    public TeamCommand(PlayerDataManager dataManager) {
        this.dataManager = dataManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 1 || args.length > 2) {
            sender.sendMessage("§c사용법: /team <red|blue> [플레이어]");
            return true;
        }

        String team = normalizeTeam(args[0]);
        if (team == null) {
            sender.sendMessage("§c팀은 red 또는 blue만 가능합니다.");
            return true;
        }

        Player target;

        if (args.length == 2) {
            if (!sender.hasPermission("jihunsmp.admin")) {
                sender.sendMessage("§c다른 플레이어의 팀을 바꾸려면 관리자 권한이 필요합니다.");
                return true;
            }

            target = Bukkit.getPlayerExact(args[1]);
            if (target == null) {
                sender.sendMessage("§c해당 플레이어가 온라인이 아닙니다.");
                return true;
            }
        } else {
            if (!(sender instanceof Player player)) {
                sender.sendMessage("§c콘솔에서는 /team <red|blue> <플레이어> 로 사용하세요.");
                return true;
            }

            target = player;
        }

        dataManager.setTeam(target, team);
        applyTeamColor(dataManager, target);

        target.sendMessage("§a당신은 " + colorName(team) + " §a팀에 들어갔습니다.");

        if (!sender.getName().equals(target.getName())) {
            sender.sendMessage("§a" + target.getName() + "님을 " + colorName(team) + " §a팀으로 설정했습니다.");
        }

        return true;
    }

    public static void applyTeamColor(PlayerDataManager dataManager, Player player) {
        String team = dataManager.getTeam(player).toUpperCase();

        ChatColor color;
        if (team.equals("RED")) {
            color = ChatColor.RED;
        } else if (team.equals("BLUE")) {
            color = ChatColor.BLUE;
        } else {
            color = ChatColor.WHITE;
        }

        player.setDisplayName(color + player.getName() + ChatColor.RESET);
        player.setPlayerListName(color + player.getName());

        if (Bukkit.getScoreboardManager() == null) return;

        Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();

        Team red = getOrCreateTeam(scoreboard, RED_TEAM, ChatColor.RED, "§c");
        Team blue = getOrCreateTeam(scoreboard, BLUE_TEAM, ChatColor.BLUE, "§9");

        red.removeEntry(player.getName());
        blue.removeEntry(player.getName());

        if (team.equals("RED")) {
            red.addEntry(player.getName());
        } else if (team.equals("BLUE")) {
            blue.addEntry(player.getName());
        }

        player.setScoreboard(scoreboard);
    }

    private static Team getOrCreateTeam(Scoreboard scoreboard, String name, ChatColor color, String prefix) {
        Team team = scoreboard.getTeam(name);

        if (team == null) {
            team = scoreboard.registerNewTeam(name);
        }

        team.setColor(color);
        team.setPrefix(prefix);
        team.setAllowFriendlyFire(false);

        return team;
    }

    private String normalizeTeam(String team) {
        if (team.equalsIgnoreCase("red")) return "RED";
        if (team.equalsIgnoreCase("blue")) return "BLUE";
        return null;
    }

    private String colorName(String team) {
        if (team.equalsIgnoreCase("RED")) return "§cRED";
        if (team.equalsIgnoreCase("BLUE")) return "§9BLUE";
        return "§fNONE";
    }
}
