package net.philocraft.bot.events;

import javax.annotation.Nonnull;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.md_5.bungee.api.ChatColor;

public class OnMessageReceived extends ListenerAdapter {

    private String channelId;
    
    public OnMessageReceived(String channelId) {
        this.channelId = channelId;
    }
    

    @Override
    public void onMessageReceived(@Nonnull MessageReceivedEvent event) {

        if(event.getChannel().getId().equals(this.channelId)) {

            if(event.getMember() == null) {
                return;
            }

            for (Player player : Bukkit.getOnlinePlayers()) {
                String message = ChatColor.translateAlternateColorCodes('&', "&9@" + event.getMember().getEffectiveName() + " &7»&f " + event.getMessage().getContentDisplay());
                player.sendMessage(message);
            }
            
            Bukkit.getLogger().info(event.getMember().getEffectiveName() + " » " + event.getMessage().getContentDisplay());
        }
    }
}

