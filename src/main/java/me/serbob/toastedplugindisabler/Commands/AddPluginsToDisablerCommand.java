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
        Player player = (Player) sender;
        if(!sender.hasPermission("toastedplugindisabler.overall")) {
            sender.sendMessage(ChatColor.RED + "You do not have permission to use this command.");
            return false;
        }
        if(args.length==0) {
            sender.sendMessage(ChatColor.RED + "Invalid! Usage: /toastedplugindisabler add/remove <Plugin's name>");
            return false;
        }
        if(args.length<2) {
            if(!args[0].equals("list")) {
                sender.sendMessage(ChatColor.RED + "Invalid! Usage: /toastedplugindisabler add/remove <Plugin's name>");
                return false;
            }
        }
        if(!args[0].equals("list")) {
            if(Bukkit.getPluginManager().getPlugin(args[1])==null) {
                sender.sendMessage(ChatColor.RED + "This plugin does not exist!");
                return false;
            }
        }
        if(args[0].equalsIgnoreCase("add")) {
            List<String> disabledPlugins = plugin.getConfig().getStringList("disabled-plugins");
            String pluginName = args[1];
            Plugin pluginToBeDisabled = Bukkit.getPluginManager().getPlugin(pluginName);
            if (disabledPlugins.contains(pluginName)) {
                sender.sendMessage(ChatColor.RED + "You have already added this plugin to be disabled!");
                return false;
            }
            disabledPlugins.add(pluginName);
            plugin.getConfig().set("disabled-plugins", disabledPlugins);
            plugin.saveConfig();
            Bukkit.getPluginManager().disablePlugin(pluginToBeDisabled);
            sender.sendMessage(ChatColor.GREEN + "Plugin added to disabled-plugins list and disabled: " + ChatColor.RED + pluginName);
        } else if(args[0].equalsIgnoreCase("load")) {
            List<String> disabledPlugins = plugin.getConfig().getStringList("disabled-plugins");
            String pluginName = args[1];
            if (!disabledPlugins.contains(pluginName)) {
                sender.sendMessage(ChatColor.RED + "That plugin doesn't exist in config.yml!");
                return false;
            }
            disabledPlugins.remove(pluginName);
            plugin.getConfig().set("disabled-plugins", disabledPlugins);
            plugin.saveConfig();
            Plugin pluginToBeEnabled = Bukkit.getPluginManager().getPlugin(pluginName);
            Bukkit.getPluginManager().enablePlugin(pluginToBeEnabled);
            sender.sendMessage(ChatColor.RED + pluginName + ChatColor.GREEN + " has been removed from the list and loaded!");
        } else if(args[0].equalsIgnoreCase("list")) {
            sender.sendMessage(ChatColor.GOLD + "The following plugins are"  + ChatColor.RED + " disabled" + ChatColor.GOLD + ":");
            List<String> disabledPlugins = plugin.getConfig().getStringList("disabled-plugins");
            for(String key: disabledPlugins) {
                sender.sendMessage(ChatColor.GOLD + " - " + ChatColor.RED + key);
            }
        }
        return true;
    }
}
