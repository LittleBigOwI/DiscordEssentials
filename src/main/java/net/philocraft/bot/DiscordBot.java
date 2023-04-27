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
    
    private JDA self;
    private String channelId;

    public DiscordBot(String token, String suffix, String channelId) throws LoginException {
        
        this.channelId = channelId;

        self = JDABuilder.create(
            token, 
            GatewayIntent.GUILD_MEMBERS,
            GatewayIntent.GUILD_VOICE_STATES,
            GatewayIntent.GUILD_PRESENCES,
            GatewayIntent.GUILD_EMOJIS,
            GatewayIntent.GUILD_MESSAGES,
            GatewayIntent.GUILD_MESSAGE_REACTIONS
        )
        .enableCache(CacheFlag.VOICE_STATE)
        .setActivity(Activity.playing(Bukkit.getOnlinePlayers().size() + suffix))
        .build();

        //!REGISTER COMMANDS
        self.addEventListener(new PlayCommand());
        self.addEventListener(new PauseCommand());
        self.addEventListener(new ResumeCommand());
        self.addEventListener(new StopCommand());
        self.addEventListener(new ShuffleCommand());
        self.addEventListener(new QueueCommand());
        self.addEventListener(new LoopCommand());
        self.addEventListener(new NowplayingCommand());
        self.addEventListener(new SkipCommand());
        self.addEventListener(new RemoveCommnand());
        self.addEventListener(new JoinCommand());
        self.addEventListener(new LeaveCommand());

        self.addEventListener(new OnlineCommand());
        self.addEventListener(new OnMessageReceived(this.channelId));

        self.addEventListener(new OnReadyEvent(self));
        self.addEventListener(new OnGuildVoiceLeave());
    }


}
