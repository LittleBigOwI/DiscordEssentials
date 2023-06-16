package net.philocraft.events;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import dev.littlebigowl.api.constants.Colors;
import dev.littlebigowl.api.models.EssentialsTeam;
import net.philocraft.DiscordEssentials;
import net.philocraft.models.Webhook;

public class OnPlayerDeath implements Listener {
    
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {

        if(event.getEntity() instanceof Player) {
            String deathMessage = event.getDeathMessage();
            Player player = event.getEntity();

            if(deathMessage == null) {
                deathMessage = player.getName() + " died";
            }

            EssentialsTeam team = DiscordEssentials.api.scoreboard.getEssentialsTeam(player);
            if(team == null) {
                team = DiscordEssentials.api.scoreboard.setTeam(player);
            }
            
            String prefix = "[" + team.getPrefix() + "]";

            DiscordEssentials.getBot().getWebhook().sendEmbed(
                Colors.MINOR.getColor(),
                DiscordEssentials.api.discord.getWebhookAvatarURL(),
                Webhook.getAvatarURL(player),
                "Server",
                prefix + " " + deathMessage

            );

            int x = (int) player.getLocation().getX();
            int y = (int) player.getLocation().getY();
            int z = (int) player.getLocation().getZ();

            Bukkit.getLogger().info("\u001b[38;5;46m [X: " + x + ", Y: " + y + ", Z: " + z + "]\u001b[0m");
        }
    }

}
