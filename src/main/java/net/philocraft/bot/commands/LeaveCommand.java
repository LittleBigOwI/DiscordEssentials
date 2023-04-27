package net.philocraft.bot.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.managers.AudioManager;
import net.philocraft.constants.Colors;
import net.philocraft.constants.Emojis;

import java.util.Objects;

import javax.annotation.Nonnull;

public class LeaveCommand extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(@Nonnull SlashCommandInteractionEvent event) {

        if(event.getName().equals("leave")) {

            if(!Objects.requireNonNull(Objects.requireNonNull(Objects.requireNonNull(event.getMember())).getVoiceState()).inAudioChannel()) { event.reply("You need to be in a voice channel for this command to work.").queue(); return;}
            if(!Objects.requireNonNull(Objects.requireNonNull(Objects.requireNonNull(event.getGuild()).getSelfMember()).getVoiceState()).inAudioChannel()) { event.reply("I am not in a voice channel.").queue(); }

            final AudioManager audioManager = Objects.requireNonNull(event.getGuild()).getAudioManager();

            audioManager.closeAudioConnection();
            event.replyEmbeds(new EmbedBuilder().setDescription(Emojis.MUSIC_LEAVE.get() + " Left voice channel.").setColor(Colors.DISCORD.getHashCode()).build()).queue();
        }

    }

}
