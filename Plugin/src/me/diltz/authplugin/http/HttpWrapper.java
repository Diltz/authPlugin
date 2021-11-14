/*

    author: @diltz
    date: 06.11.21 (DD/MM/YY)
    description: class wrapper for managing database

 */

package me.diltz.authplugin.http;

import com.google.gson.Gson;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

class POST_BODY {
    String uuid;
    String pass;

    public void build(String uuid, String pass) {
        this.uuid = uuid;
        this.pass = pass;
    }
}

class ENDPOINTS {
    private static String Host = "http://mc.auth.diltz.link";

    public static String Register = Host + "/register";
    public static String UserExist = Host + "/user-exist?user=";
    public static String Login = Host + "/login";
}

public class HttpWrapper {
    private static String API_KEY = "5CWqfG@,9!Enym[y%?D7B@AZCWi,dw-J";
    private static final HttpClient httpClient = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_1_1)
            .connectTimeout(Duration.ofSeconds(10))
            .build();

    public static void register(Player player, String password) throws IOException, InterruptedException {
        POST_BODY postData = new POST_BODY();
        postData.build(player.getUniqueId().toString(), password);

        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(new Gson().toJson(postData)))
                .uri(URI.create(ENDPOINTS.Register))
                .setHeader("User-Agent", "java-http") // add request header
                .setHeader("Content-Type", "application/json")
                .setHeader("authorization", API_KEY)
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    }

    public static boolean login(Player player, String password) throws IOException, InterruptedException {
        POST_BODY postData = new POST_BODY();
        postData.build(player.getUniqueId().toString(), password);

        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(new Gson().toJson(postData)))
                .uri(URI.create(ENDPOINTS.Login))
                .setHeader("User-Agent", "java-http") // add request header
                .setHeader("Content-Type", "application/json")
                .setHeader("authorization", API_KEY)
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        return response.statusCode() == 200 || false;
    }

    public static boolean isPlayerRegistered(Player player) throws IOException, InterruptedException {
        POST_BODY req = new POST_BODY();
        req.build(player.getUniqueId().toString(), "test");

        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(ENDPOINTS.UserExist + player.getName()))
                .setHeader("User-Agent", "java-http") // add request header
                .setHeader("authorization", API_KEY)
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        return response.statusCode() == 200 || false;
    }
}
