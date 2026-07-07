package com.jihun.managers;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;
import java.util.*;

public class TPAManager {

    private JavaPlugin plugin;
    private Map<String, String> tpaRequests = new HashMap<>(); // requester -> target
    private Map<String, BukkitTask> tpaTasks = new HashMap<>();

    public TPAManager(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void sendTPARequest(Player requester, Player target) {
        String key = requester.getName() + ":" + target.getName();
        tpaRequests.put(key, target.getName());
        
        target.sendMessage("§e[TPA] " + requester.getName() + "님이 텔레포트를 요청했습니다!");
        target.sendMessage("§a/tpaccept " + requester.getName() + " §f또는 §c/tpadeny " + requester.getName());
        requester.sendMessage("§a텔레포트 요청을 보냈습니다. 대기 중...");
        
        // 60초 후 자동 취소
        BukkitTask task = plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
            if (tpaRequests.containsKey(key)) {
                tpaRequests.remove(key);
                requester.sendMessage("§c텔레포트 요청이 만료되었습니다.");
                target.sendMessage("§c" + requester.getName() + "님의 텔레포트 요청이 만료되었습니다.");
            }
        }, 1200L); // 60초
        
        tpaTasks.put(key, task);
    }

    public boolean acceptTPA(Player target, Player requester) {
        String key = requester.getName() + ":" + target.getName();
        
        if (!tpaRequests.containsKey(key)) {
            target.sendMessage("§c요청이 없습니다.");
            return false;
        }
        
        requester.teleport(target);
        requester.sendMessage("§a텔레포트되었습니다!");
        target.sendMessage("§a" + requester.getName() + "님을 텔레포트했습니다!");
        
        tpaRequests.remove(key);
        BukkitTask task = tpaTasks.remove(key);
        if (task != null) task.cancel();
        
        return true;
    }

    public boolean denyTPA(Player target, Player requester) {
        String key = requester.getName() + ":" + target.getName();
        
        if (!tpaRequests.containsKey(key)) {
            target.sendMessage("§c요청이 없습니다.");
            return false;
        }
        
        requester.sendMessage("§c" + target.getName() + "님이 텔레포트 요청을 거절했습니다.");
        target.sendMessage("§a텔레포트 요청을 거절했습니다.");
        
        tpaRequests.remove(key);
        BukkitTask task = tpaTasks.remove(key);
        if (task != null) task.cancel();
        
        return true;
    }
}