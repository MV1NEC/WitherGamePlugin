package org.gaa.myFirstPlugin;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.WitherSkeleton;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerListener implements Listener {
    private final MyFirstPlugin plugin;
    private final Map<UUID, Integer> cooldowns = new HashMap<>();
    private final int COOLDOWN_TIME = 60; // 3초 (60틱)

    public PlayerListener(MyFirstPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInOffHand(); // 왼손 아이템

        if (item != null && item.hasItemMeta() && item.getItemMeta().hasDisplayName()) {
            String displayName = item.getItemMeta().getDisplayName();
            if (displayName.equals("넉백 스틱")) {
                UUID uuid = player.getUniqueId();

                // 위더 스켈레톤이 근처에 있는지 확인
                boolean hasWitherSkeletonNearby = player.getNearbyEntities(5, 5, 5).stream()
                        .anyMatch(entity -> entity instanceof WitherSkeleton);

                if (hasWitherSkeletonNearby) {
                    if (!cooldowns.containsKey(uuid) || cooldowns.get(uuid) <= 0) {
                        // 넉백 스틱을 들고 있는 경우
                        cooldowns.put(uuid, COOLDOWN_TIME); // 쿨타임 초기화

                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                int remainingCooldown = cooldowns.getOrDefault(uuid, 0);

                                if (remainingCooldown > 0) {
                                    // ActionBar에 쿨타임 표시 (금색)
                                    player.sendActionBar(Component.text("쿨타임: ")
                                            .append(Component.text(remainingCooldown / 20 + "초")
                                                    .color(TextColor.color(255, 215, 0))) // 금색
                                            .decorate(TextDecoration.BOLD)); // 굵게

                                    cooldowns.put(uuid, remainingCooldown - 1);
                                } else {
                                    player.sendActionBar(Component.text("쿨타임: 0초")
                                            .color(TextColor.color(255, 215, 0)) // 금색
                                            .decorate(TextDecoration.BOLD)); // 굵게
                                    cancel(); // 쿨타임이 끝나면 스케줄러 종료
                                }
                            }
                        }.runTaskTimer(plugin, 0L, 1L); // 1틱(0.05초)마다 실행

                        // 위더 스켈레톤 넉백
                        for (Entity entity : player.getNearbyEntities(5, 5, 5)) {
                            if (entity instanceof WitherSkeleton) {
                                WitherSkeleton witherSkeleton = (WitherSkeleton) entity;
                                witherSkeleton.setVelocity(witherSkeleton.getLocation().toVector().subtract(player.getLocation().toVector()).normalize().multiply(2).setY(1));
                            }
                        }
                    }
                }
            }
        }
    }
}
