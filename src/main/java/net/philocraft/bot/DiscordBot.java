package net.philocraft.bot;

import javax.security.auth.login.LoginException;

import org.bukkit.Bukkit;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import net.philocraft.bot.commands.JoinCommand;
import net.philocraft.bot.commands.LeaveCommand;
import net.philocraft.bot.commands.LoopCommand;
import net.philocraft.bot.commands.NowplayingCommand;
import net.philocraft.bot.commands.OnlineCommand;
import net.philocraft.bot.commands.PauseCommand;
import net.philocraft.bot.commands.PlayCommand;
import net.philocraft.bot.commands.QueueCommand;
import net.philocraft.bot.commands.RemoveCommnand;
import net.philocraft.bot.commands.ResumeCommand;
import net.philocraft.bot.commands.ShuffleCommand;
import net.philocraft.bot.commands.SkipCommand;
import net.philocraft.bot.commands.StopCommand;
import net.philocraft.bot.events.OnGuildVoiceLeave;
import net.philocraft.bot.events.OnMessageReceived;
import net.philocraft.bot.events.OnReadyEvent;

public class DiscordBot {
    
    private JDA bot;
    private String channelId;

    public DiscordBot(String token, String suffix, String channelId) throws LoginException {
        
        this.channelId = channelId;

        suffix = suffix.replace("{playerCount}", Bukkit.getOnlinePlayers().size() + "");

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
        .setActivity(Activity.playing(suffix))
        .build();

        //!REGISTER COMMANDS
        bot.addEventListener(new PlayCommand());
        bot.addEventListener(new PauseCommand());
        bot.addEventListener(new ResumeCommand());
        bot.addEventListener(new StopCommand());
        bot.addEventListener(new ShuffleCommand());
        bot.addEventListener(new QueueCommand());
        bot.addEventListener(new LoopCommand());
        bot.addEventListener(new NowplayingCommand());
        bot.addEventListener(new SkipCommand());
        bot.addEventListener(new RemoveCommnand());
        bot.addEventListener(new JoinCommand());
        bot.addEventListener(new LeaveCommand());

        bot.addEventListener(new OnlineCommand());
        bot.addEventListener(new OnMessageReceived(this.channelId));

        bot.addEventListener(new OnReadyEvent(bot));
        bot.addEventListener(new OnGuildVoiceLeave());
    }

    public void stop() {
        this.bot.shutdown();
    }
}
