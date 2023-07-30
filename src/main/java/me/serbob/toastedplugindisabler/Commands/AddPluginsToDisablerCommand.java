package me.serbob.toastedplugindisabler.Commands;

import me.serbob.toastedplugindisabler.ToastedPluginDisabler;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.List;

public class AddPluginsToDisablerCommand implements CommandExecutor {
    private final ToastedPluginDisabler plugin;

    public AddPluginsToDisablerCommand(ToastedPluginDisabler plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "This command can only be used by players.");
            return false;
        }

        Player player = (Player) sender;
        if (!player.hasPermission("toastedplugindisabler.overall")) {
            player.sendMessage(ChatColor.RED + "You do not have permission to use this command.");
            return true;
        }

        if (args.length == 0 || args.length == 1 && args[0].equalsIgnoreCase("list")) {
            sendUsageMessage(player);
            return true;
        }

        String subCommand = args[0].toLowerCase();
        String pluginName = args[1];

        if (subCommand.equals("add")) {
            handleAddPlugin(player, pluginName);
        } else if (subCommand.equals("load")) {
            handleLoadPlugin(player, pluginName);
        } else {
            sendUsageMessage(player);
        }

        return true;
    }

    private void handleAddPlugin(Player player, String pluginName) {
        List<String> disabledPlugins = plugin.getConfig().getStringList("disabled-plugins");
        Plugin pluginToBeDisabled = Bukkit.getPluginManager().getPlugin(pluginName);

        if (disabledPlugins.contains(pluginName)) {
            player.sendMessage(ChatColor.RED + "You have already added this plugin to be disabled!");
            return;
        }

        disabledPlugins.add(pluginName);
        plugin.getConfig().set("disabled-plugins", disabledPlugins);
        plugin.saveConfig();
        Bukkit.getPluginManager().disablePlugin(pluginToBeDisabled);
        player.sendMessage(ChatColor.GREEN + "Plugin added to disabled-plugins list and disabled: " + ChatColor.RED + pluginName);
    }

    private void handleLoadPlugin(Player player, String pluginName) {
        List<String> disabledPlugins = plugin.getConfig().getStringList("disabled-plugins");

        if (!disabledPlugins.contains(pluginName)) {
            player.sendMessage(ChatColor.RED + "That plugin doesn't exist in config.yml!");
            return;
        }

        disabledPlugins.remove(pluginName);
        plugin.getConfig().set("disabled-plugins", disabledPlugins);
        plugin.saveConfig();

        Plugin pluginToBeEnabled = Bukkit.getPluginManager().getPlugin(pluginName);
        Bukkit.getPluginManager().enablePlugin(pluginToBeEnabled);
        player.sendMessage(ChatColor.RED + pluginName + ChatColor.GREEN + " has been removed from the list and loaded!");
    }

    private void sendUsageMessage(Player player) {
        player.sendMessage(ChatColor.RED + "Usage:");
        player.sendMessage(ChatColor.RED + "/toastedplugindisabler add <Plugin's name>");
        player.sendMessage(ChatColor.RED + "/toastedplugindisabler load <Plugin's name>");
        player.sendMessage(ChatColor.RED + "/toastedplugindisabler list");
    }
}

