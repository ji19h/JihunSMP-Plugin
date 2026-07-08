package com.jihun.managers;

import java.util.UUID;

public class PlayerData {

    private final UUID uuid;
    private String playerName;
    private int coins;
    private String team;
    private int killCount;

    public PlayerData(UUID uuid, String playerName) {
        this(uuid, playerName, 100, "NONE", 0);
    }

    public PlayerData(UUID uuid, String playerName, int coins, String team, int killCount) {
        this.uuid = uuid;
        this.playerName = playerName;
        this.coins = coins;
        this.team = team;
        this.killCount = killCount;
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public int getCoins() {
        return coins;
    }

    public void addCoins(int amount) {
        coins += amount;
    }

    public boolean hasCoins(int amount) {
        return coins >= amount;
    }

    public boolean removeCoins(int amount) {
        if (!hasCoins(amount)) {
            return false;
        }
        coins -= amount;
        return true;
    }

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public int getKillCount() {
        return killCount;
    }

    public void addKill() {
        killCount++;
    }
}
