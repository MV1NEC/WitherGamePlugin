package org.gaa.myFirstPlugin;

import org.bukkit.plugin.java.JavaPlugin;

public final class MyFirstPlugin extends JavaPlugin {

    private static MyFirstPlugin instance;

    @Override
    public void onEnable() {
        instance = this;
        getLogger().info("This plugin has been successfully enabled!");

        // Command and Event registrations
        getCommand("sw").setExecutor(new SummonWither(this));
        getCommand("wither").setExecutor(new WitherScoreboard(this));
        getCommand("wither").setTabCompleter(new WitherScoreboardTabCompleter());
        getCommand("grapplinghook").setExecutor(new GrapplingHookCommand());
        getCommand("grapplinghook").setTabCompleter(new GrapplingHookTabCompleter());

        getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
        getServer().getPluginManager().registerEvents(new Humanwave(this), this);
        getServer().getPluginManager().registerEvents(new witheritem(), this);
        getServer().getPluginManager().registerEvents(new GrapplingHook(this), this);
        getServer().getPluginManager().registerEvents(new JustEasterEgg(this), this);
        getServer().getPluginManager().registerEvents(new RandomDeathMessagePlugin(this), this);  // RandomDeathMessagePlugin 등록
        getServer().getPluginManager().registerEvents(new TeamChatPlugin(this), this);
        new WitherHealth(this);

        // Command registration checks (if necessary)
        if (this.getCommand("giveknockbackstick") != null) {
            this.getCommand("giveknockbackstick").setExecutor(new isCommandTest(this));
        } else {
            getLogger().warning("Command 'giveknockbackstick' is not defined in plugin.yml.");
        }
    }

    @Override
    public void onDisable() {
        getLogger().info("This plugin has been successfully disabled!");
    }

    public static MyFirstPlugin getInstance() {
        return instance;
    }
}
