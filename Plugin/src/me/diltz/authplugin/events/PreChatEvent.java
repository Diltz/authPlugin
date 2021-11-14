/*

    author: @diltz
    date: 06.11.21 (DD/MM/YY)
    description: class handles PlayerCommandPreprocessEvent with highest priority to prevent not logged in player from using commands

 */

package me.diltz.authplugin.events;

import me.diltz.authplugin.Main;
import me.diltz.authplugin.http.HttpWrapper;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

public class PreChatEvent implements Listener {
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onCommand(PlayerCommandPreprocessEvent event) throws IOException, InterruptedException {
        String[] args = event.getMessage().substring(1).split(" ");
        String command = args[0].toString().toLowerCase(Locale.ROOT);
        Player player = event.getPlayer();

        if (Main.isLoggedIn(player)) {
            return;
        }

        if (command.equalsIgnoreCase("login")) {
            boolean isRegistered = HttpWrapper.isPlayerRegistered(player);
            if (!isRegistered) {
                player.sendMessage(ChatColor.RED + "You are not registered, please use /register password password");
                event.setCancelled(true);

                return;
            }

            if (args.length < 2) {
                player.sendMessage(ChatColor.RED + "Incorrent command usage!");
                event.setCancelled(true);

                return;
            }

            if (args[1].length() < 1) {
                player.sendMessage(ChatColor.RED + "Enter password!");
                event.setCancelled(true);

                return;
            }

            boolean loggedIn = HttpWrapper.login(player, args[1]);

            if (loggedIn) {
                HttpWrapper.register(player, args[1]);
                Main.loggedUsers.add(player.getUniqueId());
                player.setGameMode(GameMode.SURVIVAL);
            } else {
                player.sendMessage(ChatColor.RED + "Incorrect password!");
                event.setCancelled(true);
            }
        } else if (command.equalsIgnoreCase("register")) {
            boolean isRegistered = HttpWrapper.isPlayerRegistered(player);
            if (isRegistered) {
                if (!Main.isLoggedIn(player)) {
                    player.sendMessage(ChatColor.RED + "You are not registered, please use /register password password");
                }

                event.setCancelled(true);

                return;
            }

            if (args.length < 2) {
                player.sendMessage(ChatColor.RED + "Incorrent command usage!");
                event.setCancelled(true);

                return;
            }

            if (args[1].length() < 6) {
                player.sendMessage(ChatColor.RED + "Your password must be at least 6 symbols");
                event.setCancelled(true);

                return;
            } else if (!args[1].equalsIgnoreCase(args[2])) {
                player.sendMessage(ChatColor.RED + "Passwords doesn't match");
                event.setCancelled(true);

                return;
            }

            HttpWrapper.register(player, args[1]);
            Main.loggedUsers.add(player.getUniqueId());
            player.setGameMode(GameMode.SURVIVAL);
            player.sendMessage(ChatColor.YELLOW + "You are now registered! Don't forget your password!");
        } else if (!Main.isLoggedIn(player)) {
            player.sendMessage(ChatColor.RED + "You must login before using commands!");
            event.setCancelled(true);
        }

        event.setCancelled(true);
    }
}