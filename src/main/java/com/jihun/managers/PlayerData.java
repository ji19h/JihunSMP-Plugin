package com.jihun.managers;

import java.util.UUID;

public class PlayerData {
    private final UUID uuid;
    private String playerName;
    private int coins;
    private String team;
    private int killCount;
    private int killStreak;

    public PlayerData(UUID uuid, String playerName) {
        this(uuid, playerName, 100, "NONE", 0, 0);
    }

    public PlayerData(UUID uuid, String playerName, int coins, String team, int killCount, int killStreak) {
        this.uuid = uuid;
        this.playerName = playerName;
        this.coins = Math.max(0, coins);
        this.team = team == null ? "NONE" : team;
        this.killCount = Math.max(0, killCount);
        this.killStreak = Math.max(0, killStreak);
    }

    public UUID getUuid() { return uuid; }
    public String getPlayerName() { return playerName; }
    public void setPlayerName(String playerName) { this.playerName = playerName; }
    public int getCoins() { return coins; }
    public void setCoins(int amount) { coins = Math.max(0, amount); }
    public void addCoins(int amount) { setCoins(coins + amount); }
    public boolean hasCoins(int amount) { return amount >= 0 && coins >= amount; }
    public boolean removeCoins(int amount) {
        if (!hasCoins(amount)) return false;
        coins -= amount;
        return true;
    }
    public String getTeam() { return team; }
    public void setTeam(String team) { this.team = team == null ? "NONE" : team; }
    public int getKillCount() { return killCount; }
    public void addKill() { killCount++; }
    public int getKillStreak() { return killStreak; }
    public int addKillStreak() { return ++killStreak; }
    public void resetKillStreak() { killStreak = 0; }
}
