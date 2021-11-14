/*

    author: @diltz
    date: 06.11.21 (DD/MM/YY)
    description: class that doesn't let player to do anything until he will log in

 */

package me.diltz.authplugin.events;

import me.diltz.authplugin.ConfigManager;
import me.diltz.authplugin.FormatString;
import me.diltz.authplugin.http.HttpWrapper;
import me.diltz.authplugin.Main;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;

import java.io.IOException;

public class PlayerEvents implements Listener {
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        if (!Main.isLoggedIn(player)) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if (!Main.isLoggedIn(player)) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerJoin(PlayerJoinEvent event) throws IOException, InterruptedException {
        Player player = event.getPlayer();
        String WelcomeMessageRegistered = FormatString.format(ConfigManager.config.getString("WelcomeReg"), player);
        String WelcomeMessageUnRegistered = FormatString.format(ConfigManager.config.getString("WelcomeUnReg"), player);
        boolean Registered = HttpWrapper.isPlayerRegistered(player);

        if (Registered) {
            player.sendMessage(WelcomeMessageRegistered);
        } else {
            player.sendMessage(WelcomeMessageUnRegistered);
        }

        player.setGameMode(GameMode.CREATIVE);
        //player.setFlying(true);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerQuit(PlayerQuitEvent event){
        Main.loggedUsers.remove(event.getPlayer().getUniqueId());
    }
}