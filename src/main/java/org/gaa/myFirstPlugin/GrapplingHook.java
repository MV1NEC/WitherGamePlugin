package org.gaa.myFirstPlugin;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class GrapplingHook implements Listener {
    private final MyFirstPlugin plugin;
    private final Map<UUID, Long> cooldowns = new HashMap<>();
    private final Map<UUID, Arrow> activeArrows = new HashMap<>();

    public GrapplingHook(MyFirstPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();

        // 그래플링 훅 아이템인지 확인
        if (item != null && item.getType() == Material.LEAD) {
            ItemMeta meta = item.getItemMeta();
            if (meta != null && (ChatColor.GREEN.toString() + "그래플링 훅").equals(meta.getDisplayName())) {
                if (GrapplingHookCommand.isGrapplingHookEnabled()) {
                    if (!isCooldownActive(player)) {
                        event.setCancelled(true); // 기본 상호작용 동작 취소
                        setCooldown(player, 30); // 1.5초 쿨타임 (40틱)

                        // 플레이어가 바라보는 방향으로 화살 발사
                        Arrow arrow = player.launchProjectile(Arrow.class);
                        arrow.setVelocity(player.getLocation().getDirection().multiply(2)); // 속도 조절
                        arrow.setCustomName("GrapplingHookArrow"); // 화살에 이름 설정
                        arrow.setDamage(4); // 화살의 대미지를 4로 설정

                        activeArrows.put(player.getUniqueId(), arrow);

                        // 20초 후 화살 제거
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                if (!arrow.isDead()) {
                                    arrow.remove();
                                }
                            }
                        }.runTaskLater(plugin, 4 * 20); // 20초 후 실행
                    }
                } else {
                    player.sendMessage("그래플링 훅 기능이 비활성화되어 있습니다.");
                }
            }
        }
    }

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent event) {
        if (event.getEntity() instanceof Arrow) {
            Arrow arrow = (Arrow) event.getEntity();
            if ("GrapplingHookArrow".equals(arrow.getCustomName())) {
                Player shooter = (Player) arrow.getShooter();
                Entity hitEntity = event.getHitEntity();

                if (hitEntity instanceof LivingEntity) {
                    // 몬스터에 맞았을 때 0.7초 쿨타임 적용
                    setCooldown(shooter, 14); // 0.7초
                } else if (arrow.getLocation().getBlock() != null) {
                    // 벽에 맞았을 때 0.5초 쿨타임 적용
                    setCooldown(shooter, 10); // 0.5초

                    // 벽에 맞았을 때 플레이어를 이동시킵니다.
                    Vector playerLocation = shooter.getLocation().toVector();
                    Vector arrowLocation = arrow.getLocation().toVector();
                    Vector travelVector = arrowLocation.subtract(playerLocation).normalize().multiply(2);

                    shooter.setVelocity(travelVector);
                }
                arrow.remove();
            }
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            ItemStack item = player.getInventory().getItemInMainHand();
            if (item != null && item.getType() == Material.LEAD) {
                ItemMeta meta = item.getItemMeta();
                if (meta != null && (ChatColor.GREEN.toString() + "그래플링 훅").equals(meta.getDisplayName())) {
                    if (event.getCause() == EntityDamageEvent.DamageCause.FALL) {
                        event.setDamage(event.getDamage() * 0.25); // 낙하 대미지를 75% 감소
                    }
                }
            }
        }
    }

    private boolean isCooldownActive(Player player) {
        if (cooldowns.containsKey(player.getUniqueId())) {
            long cooldownEnd = cooldowns.get(player.getUniqueId());
            return System.currentTimeMillis() < cooldownEnd;
        }
        return false;
    }

    private void setCooldown(Player player, long ticks) {
        long cooldownEnd = System.currentTimeMillis() + (ticks * 50); // 1틱 = 50ms
        cooldowns.put(player.getUniqueId(), cooldownEnd);

        new BukkitRunnable() {
            @Override
            public void run() {
                if (isCooldownActive(player)) {
                    double timeLeft = (cooldownEnd - System.currentTimeMillis()) / 1000.0;
                    player.sendActionBar(ChatColor.GOLD + "쿨타임: " + ChatColor.RED + String.format("%.1f", timeLeft) + "초 남음");
                } else {
                    player.sendActionBar(ChatColor.GREEN + "그래플링 훅 사용 가능!");
                    this.cancel();
                }
            }
        }.runTaskTimer(plugin, 0L, 2L); // 2틱(0.1초)마다 실행
    }
}
