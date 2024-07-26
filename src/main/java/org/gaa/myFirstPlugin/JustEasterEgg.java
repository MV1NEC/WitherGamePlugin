package org.gaa.myFirstPlugin;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

public class JustEasterEgg implements Listener {
    private final JavaPlugin plugin;
    private final Map<String, Integer> blockClickCounts = new HashMap<>();
    private final int requiredClicks = 5; // 원하는 클릭 횟수
    private final String targetWorld = "world"; // 네더 월드 이름
    private final int targetX = 57; // 57 -41 -1
    private final int targetY = -41;
    private final int targetZ = -1;

    public JustEasterEgg(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Block block = event.getClickedBlock();
        if (block != null && event.getAction() == org.bukkit.event.block.Action.LEFT_CLICK_BLOCK) {
            if (block.getWorld().getName().equals(targetWorld) &&
                    block.getX() == targetX &&
                    block.getY() == targetY &&
                    block.getZ() == targetZ) {

                String playerKey = block.getWorld().getName() + block.getX() + "," + block.getY() + "," + block.getZ() + player.getName();

                int clickCount = blockClickCounts.getOrDefault(playerKey, 0) + 1;
                blockClickCounts.put(playerKey, clickCount);
                String block1 = block.toString();
                if (clickCount >= requiredClicks) {
                    player.sendTitle(ChatColor.GREEN + "축하합니다!",ChatColor.YELLOW + "구석탱이의 블록을 " + requiredClicks + "번 클릭했습니다!", 10, 70, 20);
                    player.playSound(player.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, 1.0f, 1.0f);

                    // 클릭 횟수 리셋
                    blockClickCounts.remove(playerKey);
                }
            }
        }
    }
}
