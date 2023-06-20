package net.philocraft.bot.events;

import javax.annotation.Nonnull;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import dev.littlebigowl.api.constants.Colors;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.md_5.bungee.api.ChatColor;
import net.philocraft.DiscordEssentials;

public class OnMessageReceived extends ListenerAdapter {

    @Override
    public void onMessageReceived(@Nonnull MessageReceivedEvent event) {

        if(event.getChannel().getId().equals(DiscordEssentials.api.discord.getChannel())) {

            Member member = event.getMember();
            Message message = event.getMessage();
            Message reply = message.getReferencedMessage();

            String chat = "";

            if(event.getMember() == null || message == null) {
                return;
            }

            String name = member.getEffectiveName();
            if(member.getNickname() != null) {
                name = member.getNickname();
            }

            
            chat = 
                ChatColor.WHITE + "[" + 
                Colors.DISCORD_OLD.getChatColor() + "Discord" + 
                ChatColor.WHITE + " | " + 
                ChatColor.of(member.getRoles().get(0).getColor()) + name + 
                ChatColor.WHITE + "] ";
            
            if(reply != null) {
                Member replyMember = reply.getMember();
                User user = reply.getAuthor();

                String replyName;
                if(replyMember != null) {
                    replyName = member.getEffectiveName();
                    if(replyMember.getNickname() != null) { replyName = replyMember.getNickname(); }
                
                } else if(user != null) {
                    replyName = user.getName();
                
                } else {
                    replyName = "null";

                }

                chat += ChatColor.GRAY + "(Replying to @" + replyName + ": " + reply.getContentDisplay() + ") ";
            }

            chat += ChatColor.WHITE + message.getContentDisplay();

            for (Player player : Bukkit.getOnlinePlayers()) {
                player.sendMessage(chat);
            }
            
            Bukkit.getLogger().info(chat);
        }
    }
}

