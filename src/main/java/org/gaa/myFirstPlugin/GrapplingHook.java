package org.gaa.myFirstPlugin;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
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

public class GrapplingHook implements Listener {
    private final MyFirstPlugin plugin;

    public GrapplingHook(MyFirstPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand(); // 메인 핸드 아이템

        // 그래플링 훅 아이템인지 확인
        if (item != null && item.getType() == Material.LEAD) {
            ItemMeta meta = item.getItemMeta();
            if (meta != null && (ChatColor.GREEN.toString() + "그래플링 훅").equals(meta.getDisplayName())) {
                if (GrapplingHookCommand.isGrapplingHookEnabled()) {
                    event.setCancelled(true); // 기본 상호작용 동작 취소

                    // 플레이어가 바라보는 방향으로 화살 발사
                    Arrow arrow = player.launchProjectile(Arrow.class);
                    arrow.setVelocity(player.getLocation().getDirection().multiply(2)); // 속도 조절
                    arrow.setCustomName("GrapplingHookArrow"); // 화살에 이름 설정
                    arrow.setDamage(0); // 화살의 대미지를 0으로 설정

                    // 20초 후 화살 제거
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            if (!arrow.isDead()) {
                                arrow.remove();
                            }
                        }
                    }.runTaskLater(plugin, 4 * 20); // 20초 후 실행

                    // 화살이 벽에 닿으면 플레이어 이동
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            // 이 Runnable을 매 틱마다 실행하여 화살의 상태를 체크합니다.
                            if (arrow.isOnGround() || arrow.isDead()) {
                                if ("GrapplingHookArrow".equals(arrow.getCustomName())) {
                                    // 화살이 벽에 닿았는지 확인
                                    if (arrow.isOnGround()) {
                                        Vector arrowLocation = arrow.getLocation().toVector();
                                        Vector playerLocation = player.getLocation().toVector();
                                        Vector travelVector = arrowLocation.subtract(playerLocation).normalize().multiply(2);

                                        player.setVelocity(travelVector);
                                        this.cancel(); // 이동이 완료되면 Runnable을 취소
                                    }
                                }
                            }
                        }
                    }.runTaskTimer(plugin, 0L, 1L); // 매 틱마다 실행
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
                // 화살이 벽에 닿았을 때만 플레이어 이동을 처리합니다.
                if (arrow.isOnGround()) {
                    // 플레이어에게 이동 효과 적용
                    // 이동 처리 로직은 이미 위에서 수행하였으므로, 추가적인 처리가 필요 없으면 이 부분은 생략 가능합니다.
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
}
