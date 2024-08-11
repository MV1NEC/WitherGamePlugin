package org.gaa.myFirstPlugin;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.WitherSkeleton;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class WitherHealth {

    private final JavaPlugin plugin;

    public WitherHealth(JavaPlugin plugin) {
        this.plugin = plugin;
        startTask();

    }

    private void startTask() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Entity entity : Bukkit.getWorlds().get(0).getEntities()) {
                    if (entity instanceof WitherSkeleton) {
                        WitherSkeleton witherSkeleton = (WitherSkeleton) entity;

                        // Check if the WitherSkeleton has the "Swither" tag
                        boolean hasSwitherTag = witherSkeleton.hasMetadata("Swither");

                        if (hasSwitherTag) {
                            double health = witherSkeleton.getHealth();

                            Component healthText = Component.text("체력: ")
                                    .color(TextColor.color(255, 0, 0)) // 빨간색
                                    .decorate(TextDecoration.BOLD)
                                    .append(Component.text((int) health)
                                            .color(TextColor.color(255, 255, 255))); // 하얀색

                            witherSkeleton.customName(healthText);
                            witherSkeleton.setCustomNameVisible(true);
                        }
                    }
                }
            }
        }.runTaskTimer(plugin, 0L, 20L); // 1초(20틱)마다 실행
    }
}
