package org.gaa.myFirstPlugin;

import org.bukkit.entity.Entity;
import org.bukkit.entity.WitherSkeleton;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

public class witheritem implements Listener {

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof WitherSkeleton) {
            WitherSkeleton witherSkeleton = (WitherSkeleton) entity;

            // 특정 태그를 가진 위더 스켈레톤인지 확인
            if (witherSkeleton.hasMetadata("Swither")) { // "specificTag"는 실제 태그로 바꿔주세요
                // 아이템 드롭 방지
                event.getDrops().clear();
            }
        }
    }
}
