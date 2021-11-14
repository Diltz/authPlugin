package me.diltz.authplugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class FormatString {
    private static Object Colors;

    public static String format(String stringToFormat, Player player) {
        stringToFormat = stringToFormat.replaceAll("PLAYER_NAME", player.getName())
                .replaceAll("SERVER_NAME", Bukkit.getServer().getMotd());

        return stringToFormat;
    }
}
