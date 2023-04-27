package net.philocraft;

import javax.security.auth.login.LoginException;

import org.bukkit.plugin.java.JavaPlugin;

import net.philocraft.bot.DiscordBot;
import net.philocraft.models.Database;

public final class DiscordEssentials extends JavaPlugin {

    private static Database database;
    private static DiscordBot bot;

    public static Database getDatabase() {
        return database;
    }

    public static DiscordBot getBot() {
        return bot;
    }

    @Override
    public void onEnable() {
        database = Database.init(this);
        
        try {
            bot = new DiscordBot(Database.getToken(), Database.getStatus(), Database.getChannel());
        } catch (LoginException e) {
            this.getLogger().severe("Couldn't login with token.");
        }
        
        this.getLogger().info("Plugin enabled.");
    }

    @Override
    public void onDisable() {
        if(bot != null) {
            bot.stop();
            this.getLogger().info("Bot disabled.");
        }
        this.getLogger().info("Plugin disabled.");
    }

}