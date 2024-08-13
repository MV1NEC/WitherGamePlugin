package org.gaa.myFirstPlugin;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class CureStickCommand implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("curestick")) {
            if (args.length == 1) {
                Player targetPlayer = Bukkit.getPlayer(args[0]);

                if (targetPlayer != null && targetPlayer.isOnline()) {
                    giveCureStick(targetPlayer);
                    sender.sendMessage(ChatColor.GREEN + "큐어의 스틱이 " + targetPlayer.getName() + "님에게 지급되었습니다.");
                    targetPlayer.sendMessage(ChatColor.GREEN + "큐어의 스틱이 지급되었습니다.");
                } else {
                    sender.sendMessage(ChatColor.RED + "해당 플레이어를 찾을 수 없습니다.");
                }
            } else {
                sender.sendMessage(ChatColor.RED + "사용법: /curestick <플레이어 이름>");
            }
            return true;
        }
        return false;
    }

    private void giveCureStick(Player player) {
        ItemStack cureStick = new ItemStack(Material.BLAZE_ROD);
        ItemMeta meta = cureStick.getItemMeta();

        if (meta != null) {
            meta.setDisplayName(ChatColor.RED + "큐어의 스틱");
            cureStick.setItemMeta(meta);
        }

        player.getInventory().addItem(cureStick);
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (command.getName().equalsIgnoreCase("curestick")) {
            if (args.length == 1) {
                List<String> playerNames = new ArrayList<>();
                for (Player player : Bukkit.getOnlinePlayers()) {
                    playerNames.add(player.getName());
                }
                return playerNames;
            }
        }
        return null;
    }
}