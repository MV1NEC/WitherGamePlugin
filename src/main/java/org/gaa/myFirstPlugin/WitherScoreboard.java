package org.gaa.myFirstPlugin;

import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
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
    private BossBar bossBar;
    private Scoreboard scoreboard;
    private Objective objective;
    private boolean display = false; // 보스바 및 스코어보드 표시 여부
    private int initialWitherSkeletonCount = 0; // 명령어 입력 시의 초기 위더 스켈레톤 수

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
                        startDisplayingBossBarAndScoreboard(); // 보스바 및 스코어보드 표시
                        player.sendMessage("위더 스켈레톤 수 표시가 활성화되었습니다.");
                    } else {
                        player.sendMessage("위더 스켈레톤 수 표시가 이미 활성화되어 있습니다.");
                    }
                } else if (action.equals("stop")) {
                    if (display) {
                        stopDisplayingBossBarAndScoreboard(); // 보스바 및 스코어보드 표시 중단
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

    private void startDisplayingBossBarAndScoreboard() {
        if (!display) {
            initialWitherSkeletonCount = calculateWitherSkeletonCount(); // 초기 위더 스켈레톤 수 계산

            bossBar = Bukkit.createBossBar("위더 스켈레톤 수", BarColor.RED, BarStyle.SOLID);

            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                bossBar.addPlayer(onlinePlayer);
            }

            scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
            objective = scoreboard.registerNewObjective("witherCount", "dummy", "§e위더 스켈레톤 수");
            objective.setDisplaySlot(DisplaySlot.SIDEBAR);

            new BukkitRunnable() {
                @Override
                public void run() {
                    int currentWitherSkeletonCount = calculateWitherSkeletonCount();

                    // 보스바의 백분율 설정
                    double percentage = (double) currentWitherSkeletonCount / initialWitherSkeletonCount;
                    bossBar.setProgress(Math.min(percentage, 1.0)); // 최대 값이 1.0을 넘지 않도록 설정

                    // 보스바 제목 업데이트 (백분율로 표시)
                    int percentValue = (int) (percentage * 100);
                    bossBar.setTitle("위더 스켈레톤 수: " + percentValue + "%");

                    // 스코어보드에 남은 위더 스켈레톤 수 표시
                    Score score = objective.getScore("§7남은 스켈레톤의 수");
                    score.setScore(currentWitherSkeletonCount);
                    //aa
                    for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                        onlinePlayer.setScoreboard(scoreboard);
                    }
                }
            }.runTaskTimer(plugin, 0L, 20L); // 1초마다 업데이트

            display = true;
        }
    }

    private void stopDisplayingBossBarAndScoreboard() {
        if (display) {
            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                bossBar.removePlayer(onlinePlayer);
                onlinePlayer.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard()); // 스코어보드 초기화
            }
            bossBar = null; // 기존 보스바 객체를 null로 설정
            scoreboard = null; // 기존 스코어보드 객체를 null로 설정
            display = false;
        }
    }

    private int calculateWitherSkeletonCount() {
        int count = 0;
        for (Entity entity : Bukkit.getWorlds().get(0).getEntities()) {
            if (entity instanceof WitherSkeleton) {
                WitherSkeleton witherSkeleton = (WitherSkeleton) entity;
                if (witherSkeleton.hasMetadata("Swither")) {
                    count++;
                }
            }
        }
        return count;
    }
}
