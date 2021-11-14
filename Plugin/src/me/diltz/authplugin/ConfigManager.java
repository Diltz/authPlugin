package me.diltz.authplugin;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;

public class ConfigManager {
    public static FileConfiguration config = null;
    private static Plugin plugin = null;

    public static void setup() throws IOException {
        plugin = Bukkit.getServer().getPluginManager().getPlugin("AuthPlugin");
        plugin.saveDefaultConfig();
        config = YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder() + "/config.yml"));
    }
}
