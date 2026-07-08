package com.jihun.listeners;

import com.jihun.managers.PlayerDataManager;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.projectiles.ProjectileSource;

public class TeamDamageListener implements Listener {
    private final PlayerDataManager dataManager;

    public TeamDamageListener(PlayerDataManager dataManager) {
        this.dataManager = dataManager;
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player victim)) return;
        Player attacker = findAttacker(event.getDamager());
        if (attacker == null || attacker.equals(victim)) return;
        String attackerTeam = dataManager.getTeam(attacker);
        String victimTeam = dataManager.getTeam(victim);
        if (!attackerTeam.equals("NONE") && attackerTeam.equals(victimTeam)) {
            event.setCancelled(true);
            attacker.sendMessage("§c같은 팀원은 공격할 수 없습니다.");
        }
    }

    private Player findAttacker(Entity damager) {
        if (damager instanceof Player player) return player;
        if (damager instanceof Arrow arrow) {
            ProjectileSource shooter = arrow.getShooter();
            if (shooter instanceof Player player) return player;
        }
        return null;
    }
}
