package com.August.voidrestrict;

import org.bukkit.plugin.java.JavaPlugin;

public class VoidRestrictPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        saveDefaultConfig();
        getServer().getPluginManager().registerEvents(new VoidListener(this), this);
        getCommand("voidrestrict").setExecutor(new VoidCommand(this));
        getCommand("voidrestrict").setTabCompleter(new VoidCommand(this));
        getLogger().info("VoidRestrict successfully enabled!");
    }
}