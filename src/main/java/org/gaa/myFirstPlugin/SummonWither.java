package org.gaa.myFirstPlugin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.entity.WitherSkeleton;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class SummonWither implements CommandExecutor {
    private final JavaPlugin plugin;

    public SummonWither(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            for (int i = 0; i < 10; i++) {
                // 위더 스켈레톤 소환
                WitherSkeleton witherSkeleton = (WitherSkeleton) player.getWorld().spawn(player.getLocation(), WitherSkeleton.class);

                // "Swither" 태그 추가
                witherSkeleton.setMetadata("Swither", new FixedMetadataValue(plugin, true));
            }
            player.sendMessage("10마리의 위더 스켈레톤이 소환되었습니다.");
            return true;
        } else {
            sender.sendMessage("이 명령어는 플레이어만 사용할 수 있습니다.");
            return false;
        }
    }
}
