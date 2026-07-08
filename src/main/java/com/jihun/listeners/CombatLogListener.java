package com.jihun.listeners;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
public class CombatLogListener implements Listener {
    private static final long COMBAT_TIME_MS = 15_000L;
    private final JavaPlugin plugin;
    private final Map<UUID, Long> combatUntil = new HashMap<>();
    public CombatLogListener(JavaPlugin plugin) {
        this.plugin = plugin;
    }
    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player)) {
            return;
        }
        Player victim = (Player) event.getEntity();
        Player attacker = getAttackingPlayer(event.getDamager());
        if (attacker == null || attacker.equals(victim)) {
            return;
        }
        long until = System.currentTimeMillis() + COMBAT_TIME_MS;
        combatUntil.put(victim.getUniqueId(), until);
        combatUntil.put(attacker.getUniqueId(), until);
    }
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if (!isInCombat(player)) {
            combatUntil.remove(player.getUniqueId());
            return;
        }
        player.setHealth(0.0);
        combatUntil.remove(player.getUniqueId());
        Bukkit.broadcastMessage("§c[전투로그] " + player.getName() + "님이 전투 중 나가서 사망 처리되었습니다.");
    }
    private boolean isInCombat(Player player) {
        Long until = combatUntil.get(player.getUniqueId());
        return until != null && until > System.currentTimeMillis();
    }
    private Player getAttackingPlayer(Entity damager) {
        if (damager instanceof Player) {
            return (Player) damager;
        }
        return null;
    }
}
