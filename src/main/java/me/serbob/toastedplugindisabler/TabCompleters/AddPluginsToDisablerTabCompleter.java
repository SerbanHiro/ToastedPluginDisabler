package me.serbob.toastedplugindisabler.TabCompleters;

import me.serbob.toastedplugindisabler.ToastedPluginDisabler;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class AddPluginsToDisablerTabCompleter implements TabCompleter {
    private final ToastedPluginDisabler plugin;
    public AddPluginsToDisablerTabCompleter(ToastedPluginDisabler plugin) {
        this.plugin = plugin;
    }
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if(!sender.hasPermission("toastedplugindisabler.add")) {
            return Collections.emptyList();
        }
        List<String> list=new ArrayList<>();
        if(args.length==1) {
            list.add("add");
            list.add("load");
            list.add("list");
            return list;
        }
        else if(args[0].equalsIgnoreCase("add")) {
            for(Plugin plugin: Bukkit.getPluginManager().getPlugins()) {
                list.add(plugin.getName());
            }
            String letters = args[1].toLowerCase();
            list = list.stream().filter(s -> s.toLowerCase().startsWith(letters)).collect(Collectors.toList());
            return list;
        } else if(args[0].equalsIgnoreCase("load")) {
            List<String> disabledPlugins = plugin.getConfig().getStringList("disabled-plugins");
            for(String key: disabledPlugins) {
                list.add(key);
            }
            String letters = args[1].toLowerCase();
            list = list.stream().filter(s -> s.toLowerCase().startsWith(letters)).collect(Collectors.toList());
            return list;
        }
        return Collections.emptyList();
    }
}
