package com.August.voidrestrict;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public record VoidCommand(VoidRestrictPlugin plugin) implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("voidrestrict.admin")) {
            sender.sendMessage(ChatColor.RED + "You do not have permission to use this command.");
            return true;
        }

        if (args.length == 0) {
            sender.sendMessage(ChatColor.GOLD + "Usage:");
            sender.sendMessage(ChatColor.YELLOW + "/vr reload" + ChatColor.WHITE + " - Reload the config");
            sender.sendMessage(ChatColor.YELLOW + "/vr toggle <place|break|attack>" + ChatColor.WHITE + " - Toggle a setting");
            sender.sendMessage(ChatColor.YELLOW + "/vr world <overworld|nether>" + ChatColor.WHITE + " - Toggle worlds");
            return true;
        }

        FileConfiguration config = plugin.getConfig();

        if (args[0].equalsIgnoreCase("reload")) {
            plugin.reloadConfig();
            sender.sendMessage(ChatColor.GREEN + "VoidRestrict configuration reloaded!");
            return true;
        }

        if (args[0].equalsIgnoreCase("toggle") && args.length == 2) {
            String setting = args[1].toLowerCase();
            if (!Arrays.asList("place", "break", "attack").contains(setting)) {
                sender.sendMessage(ChatColor.RED + "Invalid argument! Use: place, break, attack.");
                return true;
            }

            String path = "settings.prevent-" + setting;
            boolean currentState = config.getBoolean(path);
            config.set(path, !currentState);
            plugin.saveConfig();

            sender.sendMessage(ChatColor.GREEN + "Setting " + setting + " is now: " + (!currentState ? "enabled" : "disabled"));
            return true;
        }

        if (args[0].equalsIgnoreCase("world") && args.length == 2) {
            String world = args[1].toLowerCase();
            if (!Arrays.asList("overworld", "nether").contains(world)) {
                sender.sendMessage(ChatColor.RED + "Invalid world! Use: overworld, nether.");
                return true;
            }

            String path = "worlds." + world;
            boolean currentState = config.getBoolean(path);
            config.set(path, !currentState);
            plugin.saveConfig();

            sender.sendMessage(ChatColor.GREEN + "Restrictions for " + world + " are now: " + (!currentState ? "enabled" : "disabled"));
            return true;
        }

        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            completions.addAll(Arrays.asList("reload", "toggle", "world"));
        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("toggle")) {
                completions.addAll(Arrays.asList("place", "break", "attack"));
            } else if (args[0].equalsIgnoreCase("world")) {
                completions.addAll(Arrays.asList("overworld", "nether"));
            }
        }
        return completions;
    }
}