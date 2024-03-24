package net.philocraft.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import net.philocraft.DiscordEssentials;
import net.philocraft.bot.DiscordBot;

public class OnPlayerChatEvent implements Listener{
    
    @EventHandler
    public void onMessageReceived(AsyncPlayerChatEvent event) {
        
        DiscordBot bot = DiscordEssentials.getBot();
        Player player = event.getPlayer();

        bot.getWebhook().sendMessage(
            player.getUniqueId(),
            player.getName(),
            event.getMessage()
        );
    }

}
