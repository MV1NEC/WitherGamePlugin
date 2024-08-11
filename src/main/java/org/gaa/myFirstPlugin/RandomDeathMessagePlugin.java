package org.gaa.myFirstPlugin;

import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.WitherSkeleton;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Random;

public class RandomDeathMessagePlugin implements Listener {

    public RandomDeathMessagePlugin(JavaPlugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();

        // 마지막으로 플레이어에게 피해를 준 엔티티를 확인
        if (player.getLastDamageCause() instanceof EntityDamageByEntityEvent) {
            EntityDamageByEntityEvent damageEvent = (EntityDamageByEntityEvent) player.getLastDamageCause();
            Entity damager = damageEvent.getDamager();

            if (damager instanceof WitherSkeleton) {
                WitherSkeleton witherSkeleton = (WitherSkeleton) damager;
                if (witherSkeleton.hasMetadata("Swither")) {
                    // 랜덤 메시지 목록
                    String[] messages = {
                            ChatColor.RED + player.getName() + "님이 위더에게 처치당했습니다!",
                            ChatColor.RED + player.getName() + "님이 거세 당하는 운명을 맞이하였습니다 :(",
                            ChatColor.RED + "Swither가 " + player.getName() + "님의 여정을 끝냈습니다.",
                            ChatColor.RED + player.getName() + "님이 위더의 위력에 굴복했습니다.",
                            ChatColor.RED + player.getName() + "님이 위더의 힘에 짓눌려 사망을 면치 못하였습니다.",
                            ChatColor.RED + "고작 이정돈가요?"
                    };

                    // 랜덤 메시지 선택
                    Random random = new Random();
                    String message = messages[random.nextInt(messages.length)];

                    // 메시지 설정
                    event.setDeathMessage(message);
                }
            }
        }
    }
}
