package net.philocraft.bot.events;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionType;

import javax.annotation.Nonnull;

public class OnReadyEvent extends ListenerAdapter{
    
    private final JDA bot;
    public OnReadyEvent(JDA bot) {
        this.bot = bot;
    }

    public void onReady(@Nonnull ReadyEvent event) {

        for(Guild guild : this.bot.getGuilds()) {
            
            guild.upsertCommand("online", "see who's online on the server").queue();
            guild.upsertCommand("link", "link your discord account to your minecraft account")
                .addOption(OptionType.STRING, "code", "generated code to link your account")
                .queue();
            

        }
    }
}
