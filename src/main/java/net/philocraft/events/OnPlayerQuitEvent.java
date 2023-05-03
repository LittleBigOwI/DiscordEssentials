package net.philocraft.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import dev.littlebigowl.api.constants.Colors;
import dev.littlebigowl.api.models.EssentialsTeam;
import net.philocraft.DiscordEssentials;
import net.philocraft.bot.DiscordBot;
import net.philocraft.models.Webhook;

public class OnPlayerQuitEvent implements Listener {
    
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {

        Player player = event.getPlayer();
        DiscordBot bot = DiscordEssentials.getBot();

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

}
