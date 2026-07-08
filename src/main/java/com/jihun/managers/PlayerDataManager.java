package com.jihun.managers;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerDataManager {

    private final JavaPlugin plugin;
    private final File dataFile;
    private final FileConfiguration dataConfig;
    private final Map<UUID, PlayerData> playerDataMap = new HashMap<>();

    public PlayerDataManager(JavaPlugin plugin) {
        this.plugin = plugin;
        this.dataFile = new File(plugin.getDataFolder(), "players.yml");

        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdirs();
        }

        this.dataConfig = YamlConfiguration.loadConfiguration(dataFile);
    }

    public PlayerData getPlayerData(Player player) {
        UUID uuid = player.getUniqueId();
        PlayerData data = playerDataMap.computeIfAbsent(uuid, ignored -> loadPlayerData(player));
        data.setPlayerName(player.getName());
        return data;
    }

    private PlayerData loadPlayerData(Player player) {
        UUID uuid = player.getUniqueId();
        String path = "players." + uuid;

        if (!dataConfig.contains(path)) {
            PlayerData data = new PlayerData(uuid, player.getName());
            savePlayerData(data);
            return data;
        }

        String name = dataConfig.getString(path + ".name", player.getName());
        int coins = dataConfig.getInt(path + ".coins", 100);
        String team = dataConfig.getString(path + ".team", "NONE");
        int kills = dataConfig.getInt(path + ".kills", 0);
        return new PlayerData(uuid, name, coins, team, kills);
    }

    private void savePlayerData(PlayerData data) {
        String path = "players." + data.getUuid();

        dataConfig.set(path + ".name", data.getPlayerName());
        dataConfig.set(path + ".coins", data.getCoins());
        dataConfig.set(path + ".team", data.getTeam());
        dataConfig.set(path + ".kills", data.getKillCount());

        try {
            dataConfig.save(dataFile);
        } catch (IOException e) {
            plugin.getLogger().warning("플레이어 데이터 저장 실패: " + e.getMessage());
        }
    }

    public void saveAllData() {
        for (PlayerData data : playerDataMap.values()) {
            savePlayerData(data);
        }
    }

    public void removePlayerData(Player player) {
        PlayerData data = playerDataMap.remove(player.getUniqueId());
        if (data != null) {
            savePlayerData(data);
        }
    }

    public int getCoins(Player player) {
        return getPlayerData(player).getCoins();
    }

    public void addCoins(Player player, int amount) {
        PlayerData data = getPlayerData(player);
        data.addCoins(amount);
        savePlayerData(data);
    }

    public boolean removeCoins(Player player, int amount) {
        PlayerData data = getPlayerData(player);
        boolean removed = data.removeCoins(amount);
        if (removed) {
            savePlayerData(data);
        }
        return removed;
    }

    public String getTeam(Player player) {
        return getPlayerData(player).getTeam();
    }

    public void setTeam(Player player, String team) {
        PlayerData data = getPlayerData(player);
        data.setTeam(team);
        savePlayerData(data);
    }

    public int getKillCount(Player player) {
        return getPlayerData(player).getKillCount();
    }

    public void addKill(Player player) {
        PlayerData data = getPlayerData(player);
        data.addKill();
        savePlayerData(data);
    }

    public boolean hasCoins(Player player, int amount) {
        return getPlayerData(player).hasCoins(amount);
    }
}
