package org.gaa.myFirstPlugin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WitherScoreboardTabCompleter implements TabCompleter {
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        List<String> completions = new ArrayList<>();

        // 첫 번째 인자에 대해 자동 완성
        if (args.length == 1) {
            completions.addAll(Arrays.asList("start", "stop"));
        }

        return completions;
    }
}
