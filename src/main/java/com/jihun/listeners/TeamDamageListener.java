package com.jihun.listeners;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import com.jihun.managers.PlayerDataManager;
public class TeamDamageListener implements Listener {
    private final PlayerDataManager playerDataManager;
    public TeamDamageListener(PlayerDataManager playerDataManager) {
        this.playerDataManager = playerDataManager;
    }
    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player)) {
            return;
        }
        Player victim = (Player) event.getEntity();
        Player attacker = getAttackingPlayer(event.getDamager());
        if (attacker == null) {
            return;
        }
        String attackerTeam = playerDataManager.getTeam(attacker);
        String victimTeam = playerDataManager.getTeam(victim);
        if (!attackerTeam.equals("NONE") && attackerTeam.equals(victimTeam)) {
            event.setCancelled(true);
            attacker.sendMessage("§c같은 팀은 공격할 수 없습니다.");
        }
    }
    private Player getAttackingPlayer(Entity damager) {
        if (damager instanceof Player) {
            return (Player) damager;
        }
        return null;
    }
}
