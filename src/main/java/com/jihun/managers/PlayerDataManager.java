package com.jihun.managers;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
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

        PlayerData data = playerDataMap.computeIfAbsent(uuid,
                ignored -> loadPlayerData(player));

        data.setPlayerName(player.getName());
        return data;
    }

    private PlayerData loadPlayerData(Player player) {
    UUID uuid = player.getUniqueId();
    String path = "players." + uuid;

    String name = dataConfig.getString(path + ".name", player.getName());
    int coins = dataConfig.getInt(path + ".coins", 100);
    String team = dataConfig.getString(path + ".team", "NONE");
    int kills = dataConfig.getInt(path + ".kills", 0);
    int killStreak = dataConfig.getInt(path + ".kill-streak", 0);

    // 반드시 6개 인자를 넘겨야 함
    return new PlayerData(uuid, name, coins, team, kills, killStreak);
}



    private void savePlayerData(PlayerData data) {
        String path = "players." + data.getUuid();

        dataConfig.set(path + ".name", data.getPlayerName());
        dataConfig.set(path + ".coins", data.getCoins());
        dataConfig.set(path + ".team", data.getTeam());
        dataConfig.set(path + ".kills", data.getKillCount());
        dataConfig.set(path + ".kill-streak", data.getKillStreak());

        saveConfig();
    }

    private void saveConfig() {
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

    public void setCoins(Player player, int amount) {
        PlayerData data = getPlayerData(player);
        data.setCoins(amount);
        savePlayerData(data);
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

    public int addKillStreak(Player player) {
        PlayerData data = getPlayerData(player);
        int streak = data.addKillStreak();
        savePlayerData(data);
        return streak;
    }

    public void resetKillStreak(Player player) {
        PlayerData data = getPlayerData(player);
        data.resetKillStreak();
        savePlayerData(data);
    }

    public boolean hasCoins(Player player, int amount) {
        return getPlayerData(player).hasCoins(amount);
    }

    public int getBounty(UUID uuid) {
        return dataConfig.getInt("bounties." + uuid + ".amount", 0);
    }
    public int increaseBounty(Player player, int amount) {
        String path = "bounties." + player.getUniqueId();

        int current = dataConfig.getInt(path + ".amount", 0);
        int next = current + amount;

        dataConfig.set(path + ".name", player.getName());
        dataConfig.set(path + ".amount", next);

        saveConfig();
        return next;
    }

    public int claimBounty(Player killer, Player victim) {
        UUID victimId = victim.getUniqueId();

        int bounty = getBounty(victimId);

        if (bounty <= 0) {
            return 0;
        }

        dataConfig.set("bounties." + victimId, null);
        saveConfig();

        addCoins(killer, bounty);

        return bounty;
    }

    public String getBountyListText() {
        if (!dataConfig.contains("bounties")) {
            return "§7등록된 현상금이 없습니다.";
        }

        Set<String> keys =
                dataConfig.getConfigurationSection("bounties").getKeys(false);

        if (keys.isEmpty()) {
            return "§7등록된 현상금이 없습니다.";
        }

        StringBuilder builder = new StringBuilder();

        for (String key : keys) {
            String name = dataConfig.getString(
                    "bounties." + key + ".name",
                    "Unknown"
            );

            int amount = dataConfig.getInt(
                    "bounties." + key + ".amount",
                    0
            );

            if (amount > 0) {
                builder.append("§c")
                        .append(name)
                        .append(" §f- §e")
                        .append(amount)
                        .append("원\n");
            }
        }

        return builder.length() == 0
                ? "§7등록된 현상금이 없습니다."
                : builder.toString();
    }

    public OfflinePlayer findOfflinePlayer(String name) {
        Player online = Bukkit.getPlayerExact(name);

        if (online != null) {
            return online;
        }
        for (OfflinePlayer offlinePlayer : Bukkit.getOfflinePlayers()) {
            if (offlinePlayer.getName() != null &&
                    offlinePlayer.getName().equalsIgnoreCase(name)) {
                return offlinePlayer;
            }
        }

        return null;
    }
}
