package org.gaa.myFirstPlugin;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EnderCrystal;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.entity.WitherSkeleton;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Humanwave implements Listener {
    private final MyFirstPlugin plugin;
    private final Map<UUID, BukkitRunnable> damageTasks = new HashMap<>();
    private final int DAMAGE_INTERVAL = 20; // 1초 (20틱)
    private final int DAMAGE_AMOUNT = 4; // 대미지 4

    public Humanwave(MyFirstPlugin plugin) {
        this.plugin = plugin;
        startDamageTasks();
    }

    private void startDamageTasks() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player player : plugin.getServer().getOnlinePlayers()) {
                    ItemStack item = player.getInventory().getItemInOffHand(); // 왼손 아이템

                    if (item != null && item.hasItemMeta() && item.getItemMeta().hasDisplayName()) {
                        String displayName = item.getItemMeta().getDisplayName();
                        if (displayName.equals("휴먼 웨이브의 크리스탈")) {
                            // 플레이어 기준 3칸 안의 위더 스켈레톤에게 대미지 입히기
                            for (Entity entity : player.getNearbyEntities(3, 3, 3)) {
                                if (entity instanceof WitherSkeleton) {
                                    WitherSkeleton witherSkeleton = (WitherSkeleton) entity;
                                    double newHealth = Math.max(0, witherSkeleton.getHealth() - DAMAGE_AMOUNT);
                                    witherSkeleton.setHealth(newHealth); // 체력 설정
                                    // 피해를 입힐 때 플레이어에게 사운드 재생
                                    player.playSound(player.getLocation(), Sound.ENTITY_WITHER_SKELETON_HURT, 1.0f, 1.0f);
                                }
                            }
                        }
                    }
                }
            }
        }.runTaskTimer(plugin, 0L, DAMAGE_INTERVAL); // 1초마다 실행
    }
}
