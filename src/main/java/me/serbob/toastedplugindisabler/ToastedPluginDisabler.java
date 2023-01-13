package me.serbob.toastedplugindisabler;

import me.serbob.toastedplugindisabler.Commands.AddPluginsToDisablerCommand;
import me.serbob.toastedplugindisabler.TabCompleters.AddPluginsToDisablerTabCompleter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public final class ToastedPluginDisabler extends JavaPlugin {
    @Override
    public void onEnable() {
        saveDefaultConfig();
        reloadConfig();
        getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
            @Override
            public void run() {
                getLogger().info("All plugins have been loaded!");
                List<String> disabledPlugins = getConfig().getStringList("disabled-plugins");
                if (disabledPlugins != null) {
                    for (String pluginName : disabledPlugins) {
                        // Get the plugin from the PluginManager
                        Plugin plugin = Bukkit.getPluginManager().getPlugin(pluginName);
                        System.out.println(plugin);
                        if (plugin != null) {
                            // Disable the plugin
                            System.out.println(plugin);
                            Bukkit.getPluginManager().disablePlugin(plugin);
                        }
                    }
                }
            }
        });
        getCommand("toastedplugindisabler").setExecutor(new AddPluginsToDisablerCommand(this));
        getCommand("toastedplugindisabler").setTabCompleter(new AddPluginsToDisablerTabCompleter(this));
    }


    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
