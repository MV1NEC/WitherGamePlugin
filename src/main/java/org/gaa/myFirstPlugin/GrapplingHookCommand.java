package org.gaa.myFirstPlugin;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

public class GrapplingHookCommand implements CommandExecutor {
    private static boolean grapplingHookEnabled = false;

    public static boolean isGrapplingHookEnabled() {
        return grapplingHookEnabled;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("이 명령어는 플레이어만 사용할 수 있습니다.");
            return true;
        }

        Player player = (Player) sender;

        if (args.length > 0) {
            if ("enable".equalsIgnoreCase(args[0])) {
                grapplingHookEnabled = true;
                player.sendMessage(ChatColor.GREEN + "그래플링 훅 기능이 활성화되었습니다.");
            } else if ("disable".equalsIgnoreCase(args[0])) {
                grapplingHookEnabled = false;
                player.sendMessage(ChatColor.RED + "그래플링 훅 기능이 비활성화되었습니다.");
            } else if ("give".equalsIgnoreCase(args[0])) {
                // 특정 태그 확인
                if (player.getScoreboardTags().contains("Grap")) {
                    ItemStack grapplingHook = new ItemStack(Material.LEAD);
                    ItemMeta meta = grapplingHook.getItemMeta();
                    meta.setDisplayName(ChatColor.GREEN + "그래플링 훅");
                    grapplingHook.setItemMeta(meta);

                    player.getInventory().addItem(grapplingHook);
                    player.sendMessage(ChatColor.GREEN + "그래플링 훅이 지급되었습니다.");
                } else {
                    player.sendMessage(ChatColor.RED + "그래플링 훅을 받을 권한이 없습니다.");
                }
            } else {
                player.sendMessage(ChatColor.RED + "잘못된 사용법입니다. 사용법: /grapplinghook <enable|disable|give>");
            }
        } else {
            player.sendMessage(ChatColor.RED + "잘못된 사용법입니다. 사용법: /grapplinghook <enable|disable|give>");
        }

        return true;
    }
}
