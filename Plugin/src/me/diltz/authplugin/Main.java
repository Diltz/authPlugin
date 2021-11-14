package me.diltz.authplugin;

// main classes

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;
import java.util.logging.Logger;

// custom classes

import me.diltz.authplugin.events.*;
import me.diltz.authplugin.ConfigManager;

public class Main extends JavaPlugin {
    @Override
    public Logger getLogger() {
        return super.getLogger();
    }

    //public static FileConfiguration defaultConfig = null;
    public static ArrayList<UUID> loggedUsers = new ArrayList<UUID>();
    public static boolean isLoggedIn(Player player) {
        return loggedUsers.contains(player.getUniqueId());
    }
    // main handles



    @Override
    public void onEnable(){
        // config

        try {
            ConfigManager.setup();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // events

        PreChatEvent prechatEvent = new PreChatEvent();
        PlayerEvents playerEvents = new PlayerEvents();

        this.getServer().getPluginManager().registerEvents(new PreChatEvent(), this); // chat
        this.getServer().getPluginManager().registerEvents(new PlayerEvents(), this); // player

        // cmds

        //this.getCommand("login").setExecutor();
        //this.getCommand("register").setExecutor();

        getLogger().info("Plugin loaded");
    }

    @Override
    public void onDisable() {
        //Fired when the server stops and disables all plugins

        getLogger().info("Plugin unloaded");
    }
}