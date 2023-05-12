package net.philocraft;

import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import dev.littlebigowl.api.EssentialsAPI;
import dev.littlebigowl.api.constants.Colors;
import dev.littlebigowl.api.models.EssentialsTeam;
import net.philocraft.bot.DiscordBot;
import net.philocraft.commands.LinkCommand;
import net.philocraft.events.OnAdvancementDoneEvent;
import net.philocraft.events.OnPlayerChatEvent;
import net.philocraft.events.OnPlayerDeath;
import net.philocraft.events.OnPlayerJoinEvent;
import net.philocraft.events.OnPlayerQuitEvent;
import net.philocraft.models.Webhook;
import net.philocraft.utils.DatabaseUtil;

public final class DiscordEssentials extends JavaPlugin {

    public static final EssentialsAPI api = (EssentialsAPI) Bukkit.getServer().getPluginManager().getPlugin("EssentialsAPI");

    private static DiscordBot bot;
    private static DiscordEssentials plugin;

    public static DiscordBot getBot() {
        return bot;
    }

    public static DiscordEssentials getPlugin() {
        return plugin;
    }

    @Override
    public void onEnable() {

        //!REGISTER COMMANDS
        this.getCommand("link").setExecutor(new LinkCommand());

        //!REGISTER EVENTS
        this.getServer().getPluginManager().registerEvents(new OnAdvancementDoneEvent(), this);
        this.getServer().getPluginManager().registerEvents(new OnPlayerChatEvent(), this);
        this.getServer().getPluginManager().registerEvents(new OnPlayerDeath(), this);
        this.getServer().getPluginManager().registerEvents(new OnPlayerJoinEvent(), this);
        this.getServer().getPluginManager().registerEvents(new OnPlayerQuitEvent(), this);
        
        plugin = this;
        bot = DiscordBot.init(this);

        try {
            DatabaseUtil.loadLinks();
        } catch (SQLException e) {
            this.getLogger().severe("Couldn't load links : " + e.getMessage());
        }

        if(bot != null) {
            bot.getWebhook().sendEmbed(
                Colors.SUCCESS.getColor(),
                api.discord.getWebhookAvatarURL(),
                null,
                "Server",
                "Server started."    
            );
        }
        
        DatabaseUtil.checkRoles();
        this.getLogger().info("Plugin enabled.");
    }

    @Override
    public void onDisable() {

        for(Player player : Bukkit.getOnlinePlayers()) {
            EssentialsTeam team = DiscordEssentials.api.scoreboard.getEssentialsTeam(player);
            if(team == null) {
                team = DiscordEssentials.api.scoreboard.setTeam(player);
            }
            
            String prefix = "[" + team.getPrefix() + "]";

            bot.getWebhook().sendEmbed(
                Colors.FAILURE.getColor(),
                DiscordEssentials.api.discord.getWebhookAvatarURL(),
                Webhook.getAvatarURL(player),
                "Server",
                prefix + " " + player.getName() + " left the game."
            );

        }
        
        if(bot != null) {
            bot.getWebhook().sendEmbed(
                Colors.FAILURE.getColor(),
                api.discord.getWebhookAvatarURL(),
                null,
                "Server",
                "Server stopped."    
            );

            bot.stop();
            this.getLogger().info("Bot disabled.");
        }

        this.getLogger().info("Plugin disabled.");
    }

}