package net.philocraft.bot.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import dev.littlebigowl.api.constants.Colors;

import java.util.ArrayList;

import javax.annotation.Nonnull;

public class OnlineCommand extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(@Nonnull SlashCommandInteractionEvent event) {

        if(event.getName().equals("online")){
            ArrayList<String> players = new ArrayList<>();
            
            for(Player player : Bukkit.getOnlinePlayers()) {

                players.add(player.getName());

            }
            
            if(Bukkit.getOnlinePlayers().size() == 0) { 
                event.replyEmbeds(new EmbedBuilder().setDescription("There are no players online.").setColor(Colors.DISCORD.getColor()).build()).queue(); 
                return; 
            }

            StringBuffer embedContent = new StringBuffer();
            for(String playerName : players) {
                embedContent.append("`" + playerName + "`, ");
            }
            embedContent.delete(embedContent.length()-2, embedContent.length());

            event.replyEmbeds(new EmbedBuilder().setDescription(embedContent).setColor(Colors.DISCORD.getColor()).setTitle("Online Players").build()).queue();
        }
    }
}
