package net.philocraft.bot;

import java.sql.SQLException;

import javax.security.auth.login.LoginException;

import org.bukkit.Bukkit;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import net.philocraft.DiscordEssentials;
import net.philocraft.bot.commands.LinkCommand;
import net.philocraft.bot.commands.OnlineCommand;
import net.philocraft.bot.events.OnMessageReceived;
import net.philocraft.bot.events.OnReadyEvent;
import net.philocraft.models.Webhook;

public class DiscordBot {
    
    private JDA bot;
    private String status;

    private DiscordBot(DiscordEssentials plugin, String token, String webhookURL, String status) throws LoginException {

        this.status = status;

        bot = JDABuilder.create(
            token, 
            GatewayIntent.GUILD_MEMBERS,
            GatewayIntent.GUILD_VOICE_STATES,
            GatewayIntent.GUILD_PRESENCES,
            GatewayIntent.GUILD_EMOJIS,
            GatewayIntent.GUILD_MESSAGES,
            GatewayIntent.GUILD_MESSAGE_REACTIONS
        )
        .enableCache(CacheFlag.VOICE_STATE)
        .setActivity(Activity.playing(status))
        .build();

        //!REGISTER COMMANDS
        bot.addEventListener(new OnlineCommand());
        bot.addEventListener(new LinkCommand());
        
        //!REGISTER EVENTS
        bot.addEventListener(new OnMessageReceived());
        bot.addEventListener(new OnReadyEvent(bot));

        plugin.getServer().getScheduler().runTaskTimer(plugin, () -> {
            
            bot.getPresence().setActivity(Activity.playing(
                this.status
                .replace("{playerCount}", Bukkit.getOnlinePlayers().size() + "")
                .replace("{maxPlayers}", Bukkit.getMaxPlayers() + ""))
            );

        }, 0, 1200);
    }

    public static DiscordBot init(DiscordEssentials plugin) {
        DiscordBot bot = null;
        
        try {
            bot = new DiscordBot(
                plugin,
                DiscordEssentials.api.discord.getToken(), 
                DiscordEssentials.api.discord.getWebhookURL(),
                DiscordEssentials.api.discord.getStatus()
            );

            DiscordEssentials.api.database.create(
                "CREATE TABLE IF NOT EXISTS Accounts(" +
                "id int NOT NULL UNIQUE AUTO_INCREMENT, " + 
                "uuid TEXT, " +
                "userid TEXT, " +
                "code TEXT UNIQUE NOT NULL);"
            );

            plugin.getLogger().info("Bot enabled.");
        } catch (LoginException e) {
            plugin.getLogger().severe("Couldn't login with token : " + e.getMessage());
        } catch (IllegalArgumentException e) {
            plugin.getLogger().severe("Invalid Webhook URL : " + e.getMessage());
        } catch (SQLException e) {
            plugin.getLogger().severe("Couldn't initialize table : " + e.getMessage());
        }

        return bot;
    }

    public Webhook getWebhook() {
        return new Webhook(DiscordEssentials.api.discord.getWebhookURL());
    }

    public String getStatus() {
        return this.status;
    }

    public void stop() {
        this.bot.shutdown();
    }
}
