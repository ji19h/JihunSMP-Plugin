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
        this.coins = coins;
        this.team = team;
        this.killCount = killCount;
        this.killStreak = killStreak;
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
    public void setCoins(int amount) {
        coins = Math.max(0, amount);
    }
    public void addCoins(int amount) {
        coins += amount;
    }
    JihunSMP final copy order
복사할 때 ```java, ```yml, ``` 이 줄은 빼고 코드 안쪽만 복사하세요.
적용 순서:
1. PlayerData.java 교체
2. PlayerDataManager.java 교체
3. PlayerDeathListener.java 교체
4. EnhanceListener.java 추가 또는 교체
5. LocateCommand.java 교체
6. CoinAdminCommand.java 추가 또는 교체
7. BountyCommand.java 추가 또는 교체
8. ShopCommand.java 추가 또는 교체
9. TeamDamageListener.java 추가 또는 교체
10. CombatLogListener.java 추가 또는 교체
11. SupplyCrateManager.java 추가 또는 교체
12. jihunSMP.java 교체
13. plugin.yml 교체
============================================================
1코드: src/main/java/com/jihun/managers/PlayerData.java
============================================================
```java
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
        this.coins = coins;
        this.team = team;
        this.killCount = killCount;
        this.killStreak = killStreak;
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
    public void setCoins(int amount) {
        coins = Math.max(0, amount);
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
    public int getKillStreak() {
        return killStreak;
    }
    public int addKillStreak() {
        killStreak++;
        return killStreak;
    }
    public void resetKillStreak() {
        killStreak = 0;
    }
}
