package org.gaa.myFirstPlugin;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

public class isCommandTest implements CommandExecutor {

    private final MyFirstPlugin plugin;

    public isCommandTest(MyFirstPlugin plugin) {
        this.plugin = plugin;

    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("이 명령어는 플레이어만 사용할 수 있습니다.");
            return true;
        }
        if(((Player) sender).isOp() && ((Player) sender).getScoreboardTags().contains("windpressure") ){
        Player player = (Player) sender;

        // "넉백 스틱" 아이템 생성
        ItemStack knockbackStick = new ItemStack(Material.STICK);
        ItemMeta meta = knockbackStick.getItemMeta();
        if (meta != null) {
            meta.setDisplayName("넉백 스틱");
            knockbackStick.setItemMeta(meta);
        }

        // 플레이어에게 아이템 지급
        player.getInventory().addItem(knockbackStick);
        player.sendMessage("넉백 스틱이 지급되었습니다!");

        return true;
        } else if (!((Player) sender).isOp()){
            sender.sendMessage("You are not an op Player!");

        } else if (!((Player) sender).getScoreboardTags().contains("windpressure")){
            sender.sendMessage("You don't have windpressure tag!");

        }
return true;
    }
}
