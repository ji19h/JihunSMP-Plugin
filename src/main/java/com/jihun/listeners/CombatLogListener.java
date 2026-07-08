package com.jihun.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.projectiles.ProjectileSource;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CombatLogListener implements Listener {
    private static final long COMBAT_TIME_MS = 15_000L;
    private final Map<UUID, Long> combatUntil = new HashMap<>();

    public CombatLogListener(JavaPlugin plugin) {
        Bukkit.getScheduler().runTaskTimer(plugin,
                () -> combatUntil.entrySet().removeIf(entry -> entry.getValue() <= System.currentTimeMillis()),
                20L, 20L);
    }

    @EventHandler(ignoreCancelled = true)
    public void onDamage(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player victim)) return;
        Player attacker = findAttacker(event.getDamager());
        if (attacker == null || attacker.equals(victim)) return;
        long until = System.currentTimeMillis() + COMBAT_TIME_MS;
        combatUntil.put(victim.getUniqueId(), until);
        combatUntil.put(attacker.getUniqueId(), until);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        Long until = combatUntil.remove(player.getUniqueId());
        if (until != null && until > System.currentTimeMillis() && !player.isDead()) {
            Bukkit.broadcastMessage("§c" + player.getName() + "님이 전투 중 종료하여 사망 처리되었습니다.");
            player.setHealth(0.0);
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        combatUntil.remove(event.getEntity().getUniqueId());
    }

    private Player findAttacker(Entity damager) {
        if (damager instanceof Player player) return player;
        if (damager instanceof Projectile projectile) {
            ProjectileSource shooter = projectile.getShooter();
            if (shooter instanceof Player player) return player;
        }
        return null;
    }
}
