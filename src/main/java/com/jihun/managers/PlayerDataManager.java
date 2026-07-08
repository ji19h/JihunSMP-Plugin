package com.jihun.managers;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerDataManager {
    private final JavaPlugin plugin;
    private final File dataFile;
    private final YamlConfiguration config;
    private final Map<UUID, PlayerData> data = new HashMap<>();
    private final Map<UUID, Integer> bounties = new HashMap<>();

    public PlayerDataManager(JavaPlugin plugin) {
        this.plugin = plugin;
        if (!plugin.getDataFolder().exists()) plugin.getDataFolder().mkdirs();
        dataFile = new File(plugin.getDataFolder(), "players.yml");
        config = YamlConfiguration.loadConfiguration(dataFile);
        loadAllData();
    }

    private void loadAllData() {
        ConfigurationSection players = config.getConfigurationSection("players");
        if (players != null) {
            for (String key : players.getKeys(false)) {
                try {
                    UUID uuid = UUID.fromString(key);
                    String path = "players." + key + ".";
                    data.put(uuid, new PlayerData(uuid,
                            config.getString(path + "name", "Unknown"),
                            config.getInt(path + "coins", 100),
                            config.getString(path + "team", "NONE"),
                            config.getInt(path + "kills", 0),
                            config.getInt(path + "kill-streak", 0)));
                } catch (IllegalArgumentException ignored) {
                    plugin.getLogger().warning("잘못된 UUID 데이터: " + key);
                }
            }
        }
        ConfigurationSection bountySection = config.getConfigurationSection("bounties");
        if (bountySection != null) {
            for (String key : bountySection.getKeys(false)) {
                try {
                    bounties.put(UUID.fromString(key), Math.max(0, bountySection.getInt(key)));
                } catch (IllegalArgumentException ignored) {
                    plugin.getLogger().warning("잘못된 현상금 UUID: " + key);
                }
            }
        }
    }

    public PlayerData getPlayerData(Player player) {
        PlayerData value = data.computeIfAbsent(player.getUniqueId(),
                uuid -> new PlayerData(uuid, player.getName()));
        value.setPlayerName(player.getName());
        return value;
    }

    public PlayerData getPlayerData(OfflinePlayer player) {
        String name = player.getName() == null ? "Unknown" : player.getName();
        PlayerData value = data.computeIfAbsent(player.getUniqueId(),
                uuid -> new PlayerData(uuid, name));
        if (player.getName() != null) value.setPlayerName(player.getName());
        return value;
    }

    public Collection<PlayerData> getAllPlayerData() { return data.values(); }
    public int getCoins(Player player) { return getPlayerData(player).getCoins(); }
    public void setCoins(Player player, int amount) { getPlayerData(player).setCoins(amount); saveAllData(); }
    public void addCoins(Player player, int amount) { getPlayerData(player).addCoins(amount); saveAllData(); }
    public boolean removeCoins(Player player, int amount) {
        boolean removed = getPlayerData(player).removeCoins(amount);
        if (removed) saveAllData();
        return removed;
    }
    public String getTeam(Player player) { return getPlayerData(player).getTeam(); }
    public void setTeam(Player player, String team) { getPlayerData(player).setTeam(team); saveAllData(); }
    public int getKillCount(Player player) { return getPlayerData(player).getKillCount(); }
    public void addKill(Player player) { getPlayerData(player).addKill(); saveAllData(); }
    public int addKillStreak(Player player) { int streak = getPlayerData(player).addKillStreak(); saveAllData(); return streak; }
    public void resetKillStreak(Player player) { getPlayerData(player).resetKillStreak(); saveAllData(); }
    public int getBounty(Player player) { return bounties.getOrDefault(player.getUniqueId(), 0); }
    public int getBounty(UUID uuid) { return bounties.getOrDefault(uuid, 0); }
    public void addBounty(Player player, int amount) {
        bounties.put(player.getUniqueId(), Math.max(0, getBounty(player) + amount));
        saveAllData();
    }
    public int takeBounty(Player victim) {
        int amount = bounties.getOrDefault(victim.getUniqueId(), 0);
        bounties.remove(victim.getUniqueId());
        saveAllData();
        return amount;
    }
    public OfflinePlayer findPlayer(String name) {
        Player online = Bukkit.getPlayerExact(name);
        if (online != null) return online;
        for (PlayerData value : data.values()) {
            if (value.getPlayerName().equalsIgnoreCase(name)) return Bukkit.getOfflinePlayer(value.getUuid());
        }
        return null;
    }
    public void removePlayerData(Player player) { saveAllData(); }

    public void saveAllData() {
        config.set("players", null);
        for (PlayerData value : data.values()) {
            String path = "players." + value.getUuid() + ".";
            config.set(path + "name", value.getPlayerName());
            config.set(path + "coins", value.getCoins());
            config.set(path + "team", value.getTeam());
            config.set(path + "kills", value.getKillCount());
            config.set(path + "kill-streak", value.getKillStreak());
        }
        config.set("bounties", null);
        bounties.forEach((uuid, amount) -> config.set("bounties." + uuid, amount));
        try {
            config.save(dataFile);
        } catch (IOException e) {
            plugin.getLogger().severe("players.yml 저장 실패: " + e.getMessage());
        }
    }
}
