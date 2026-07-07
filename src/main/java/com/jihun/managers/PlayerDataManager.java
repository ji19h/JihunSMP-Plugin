package com.jihun.managers;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.*;

public class PlayerDataManager {

    private JavaPlugin plugin;
    private Map<String, PlayerData> playerDataMap = new HashMap<>();

    public PlayerDataManager(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public PlayerData getPlayerData(Player player) {
        return playerDataMap.computeIfAbsent(player.getName(), k -> new PlayerData(player.getName()));
    }

    public void saveAllData() {
        // 나중에 데이터베이스나 파일로 저장 구현
    }

    public void removePlayerData(Player player) {
        playerDataMap.remove(player.getName());
    }

    public int getCoins(Player player) {
        return getPlayerData(player).getCoins();
    }

    public void addCoins(Player player, int amount) {
        getPlayerData(player).addCoins(amount);
    }

    public void removeCoins(Player player, int amount) {
        getPlayerData(player).removeCoins(amount);
    }

    public String getTeam(Player player) {
        return getPlayerData(player).getTeam();
    }

    public void setTeam(Player player, String team) {
        getPlayerData(player).setTeam(team);
    }

    public int getKillCount(Player player) {
        return getPlayerData(player).getKillCount();
    }

    public void addKill(Player player) {
        getPlayerData(player).addKill();
    }
}