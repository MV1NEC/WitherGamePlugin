package org.gaa.myFirstPlugin;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.WitherSkeleton;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.*;

import org.jetbrains.annotations.NotNull;

public class WitherScoreboard implements CommandExecutor {
    private final JavaPlugin plugin;
    private Scoreboard scoreboard;
    private Objective objective;
    private boolean display = false; // 스코어보드 표시 여부

    public WitherScoreboard(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (!player.isOp() && !" _HPMS_".equals(player.getName())) {
                player.sendMessage("이 명령어를 실행할 권한이 없습니다.");
                return true;
            }

            if (args.length > 0) {
                String action = args[0].toLowerCase();

                if (action.equals("start")) {
                    if (!display) {
                        startDisplayingScoreboard();
                        player.sendMessage("위더 스켈레톤 수 표시가 활성화되었습니다.");
                    } else {
                        player.sendMessage("위더 스켈레톤 수 표시가 이미 활성화되어 있습니다.");
                    }
                } else if (action.equals("stop")) {
                    if (display) {
                        stopDisplayingScoreboard();
                        player.sendMessage("위더 스켈레톤 수 표시가 비활성화되었습니다.");
                    } else {
                        player.sendMessage("위더 스켈레톤 수 표시가 이미 비활성화되어 있습니다.");
                    }
                } else {
                    player.sendMessage("명령어 인자가 올바르지 않습니다. 사용법: /wither start|stop");
                }
            } else {
                player.sendMessage("명령어 인자가 부족합니다. 사용법: /wither start|stop");
            }

            return true;
        } else {
            sender.sendMessage("이 명령어는 플레이어만 사용할 수 있습니다.");
            return false;
        }
    }

    private void startDisplayingScoreboard() {
        if (!display) {
            scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
            objective = scoreboard.registerNewObjective("witherCount", "dummy", "§e위더 스켈레톤 수");
            objective.setDisplaySlot(DisplaySlot.SIDEBAR);

            new BukkitRunnable() {
                @Override
                public void run() {
                    int witherSkeletonCount = 0;

                    for (Entity entity : Bukkit.getWorlds().get(0).getEntities()) {
                        if (entity instanceof WitherSkeleton) {
                            WitherSkeleton witherSkeleton = (WitherSkeleton) entity;
                            if (witherSkeleton.hasMetadata("Swither")) {
                                witherSkeletonCount++;
                            }
                        }
                    }

                    Score score = objective.getScore("§7현재 남은 스켈레톤의 수");
                    score.setScore(witherSkeletonCount);

                    for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                        onlinePlayer.setScoreboard(scoreboard);
                    }
                }
            }.runTaskTimer(plugin, 0L, 20L); // 1초마다 업데이트

            display = true;
        }
    }

    private void stopDisplayingScoreboard() {
        if (display) {
            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                onlinePlayer.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
            }
            scoreboard = null; // 기존 스코어보드 객체를 null로 설정
            display = false;
        }
    }
}