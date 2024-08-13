package org.gaa.myFirstPlugin;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CureStick implements Listener {
    private final MyFirstPlugin plugin;
    private final Map<UUID, BukkitRunnable> cooldownTasks = new HashMap<>();
    private final int COOLDOWN_INTERVAL = 40; // 2초 (40틱)

    public CureStick(MyFirstPlugin plugin) {
        this.plugin = plugin;
        startCooldownTasks();
    }

    private void startCooldownTasks() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player player : plugin.getServer().getOnlinePlayers()) {
                    ItemStack item = player.getInventory().getItemInOffHand(); // 왼손 아이템

                    if (item != null && item.getType() == Material.BLAZE_ROD && item.hasItemMeta() && item.getItemMeta().hasDisplayName()) {
                        String displayName = item.getItemMeta().getDisplayName();
                        if (displayName.equals(ChatColor.RED + "큐어의 스틱")) {
                            // 이미 쿨타임 작업이 실행 중인지 확인
                            if (!cooldownTasks.containsKey(player.getUniqueId())) {
                                startCooldownTask(player);
                            }
                        }
                    } else {
                        // 아이템이 없거나 "큐어의 스틱"이 아닌 경우 쿨타임 작업 취소
                        if (cooldownTasks.containsKey(player.getUniqueId())) {
                            cooldownTasks.get(player.getUniqueId()).cancel();
                            cooldownTasks.remove(player.getUniqueId());
                        }
                    }
                }
            }
        }.runTaskTimer(plugin, 0L, 20L); // 매초 실행
    }

    private void startCooldownTask(Player player) {
        BukkitRunnable task = new BukkitRunnable() {
            private int cooldown = COOLDOWN_INTERVAL;

            @Override
            public void run() {
                if (cooldown > 0) {
                    // 쿨타임이 남았을 때 액션바에 표시
                    player.sendActionBar(ChatColor.GOLD + "큐어의 스틱: " + ChatColor.RED + cooldown / 20 + "초 남음");
                    cooldown--;
                } else {
                    // 쿨타임이 끝났을 때 회복 물약 지급
                    giveHealingPotion(player);
                    player.sendActionBar(ChatColor.GREEN + "큐어의 스틱: " + ChatColor.RED + "회복 물약 지급됨!");
                    cooldown = COOLDOWN_INTERVAL;
                }
            }
        };
        task.runTaskTimer(plugin, 0L, 1L); // 매틱 실행 (0.05초마다)
        cooldownTasks.put(player.getUniqueId(), task);
    }

    private void giveHealingPotion(Player player) {
        ItemStack potion = new ItemStack(Material.SPLASH_POTION);
        PotionMeta meta = (PotionMeta) potion.getItemMeta();

        if (meta != null) {
            meta.setMainEffect(PotionEffectType.INSTANT_HEALTH);
            meta.addCustomEffect(new PotionEffect(PotionEffectType.INSTANT_HEALTH, 1, 1), true);
            meta.setDisplayName(ChatColor.RED + "큐어의 회복 물약");
            potion.setItemMeta(meta);
        }

        player.getInventory().addItem(potion);
        player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);
    }
}
