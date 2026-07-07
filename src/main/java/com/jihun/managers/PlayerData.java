package com.jihun.managers;

import java.util.*;

public class PlayerData {

    private String playerName;
    private int coins = 100; // 초기 코인
    private String team = "NONE";
    private int killCount = 0;
    private Map<String, Integer> enchantments = new HashMap<>();

    public PlayerData(String playerName) {
        this.playerName = playerName;
        initEnchantments();
    }

    private void initEnchantments() {
        // 무기 강화
        enchantments.put("SHARPNESS", 0);
        // 방어구 강화
        enchantments.put("PROTECTION", 0);
    }

    public String getPlayerName() {
        return playerName;
    }

    public int getCoins() {
        return coins;
    }

    public void addCoins(int amount) {
        coins += amount;
    }

    public void removeCoins(int amount) {
        coins = Math.max(0, coins - amount);
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

    public int getEnchantmentLevel(String enchantment) {
        return enchantments.getOrDefault(enchantment, 0);
    }

    public boolean upgradeEnchantment(String enchantment) {
        int currentLevel = getEnchantmentLevel(enchantment);
        if (currentLevel >= 10) return false;
        
        // 확률 계산: 초기 90%, 1강마다 10%씩 감소, 10강은 1%
        int successChance;
        if (currentLevel == 9) {
            successChance = 1; // 10강: 1%
        } else {
            successChance = 90 - (currentLevel * 10);
        }
        
        Random random = new Random();
        
        if (random.nextInt(100) < successChance) {
            enchantments.put(enchantment, currentLevel + 1);
            return true;
        }
        return false;
    }

    public Map<String, Integer> getEnchantments() {
        return enchantments;
    }
}