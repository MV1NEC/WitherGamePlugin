package org.gaa.myFirstPlugin;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class TeamChatPlugin implements Listener {

    private final JavaPlugin plugin;

    public TeamChatPlugin(JavaPlugin plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        Scoreboard scoreboard = player.getScoreboard();
        Team team = scoreboard.getEntryTeam(player.getName());

        if (team != null) {
            // 팀의 DisplayName과 색상 가져오기
            String teamDisplayName = team.getDisplayName();
            ChatColor teamColor = team.getColor();

            // 팀 이름과 대괄호에 색상 적용
            String teamDisplayNameColored = teamColor+ teamDisplayName + ChatColor.RESET;

            // 채팅 메시지 색상 설정
            ChatColor messageColor = ChatColor.WHITE;

            // 포맷 설정: 팀 이름과 대괄호 색상 적용, 메시지 색상 구별
            String formattedMessage = teamDisplayNameColored + "" + player.getDisplayName() + ": " + messageColor + event.getMessage();

            // 채팅 포맷 설정
            event.setFormat(formattedMessage);
        } else {
            // 팀이 없을 경우 기본 포맷 유지
            event.setFormat(player.getDisplayName() + ": " + event.getMessage());
        }
    }
}
