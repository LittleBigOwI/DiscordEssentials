package net.philocraft.models;

import net.philocraft.DiscordEssentials;

public class Database {
    
    private static String token;
    private static String channel;
    private static String status;

    private DiscordEssentials plugin;

    private Database(DiscordEssentials plugin) {
        this.plugin = plugin;
    }

    public static Database init(DiscordEssentials discordEssentials) {
        discordEssentials.saveDefaultConfig();

        token = discordEssentials.getConfig().getString("token");
        channel = discordEssentials.getConfig().getString("channel");
        status = discordEssentials.getConfig().getString("status");

        return new Database(discordEssentials);
    }

    public static String getToken() {
        return token;
    }

    public static String getChannel() {
        return channel;
    }

    public static String getStatus() {
        return status;
    }

    public DiscordEssentials getPlugin() {
        return this.plugin;
    }

}
