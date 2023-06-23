package net.philocraft.events;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import dev.littlebigowl.api.constants.Colors;
import dev.littlebigowl.api.models.EssentialsPermission;
import dev.littlebigowl.api.models.EssentialsTeam;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.philocraft.DiscordEssentials;
import net.philocraft.bot.DiscordBot;
import net.philocraft.models.Link;
import net.philocraft.models.Webhook;
import net.philocraft.utils.DatabaseUtil;

public class OnPlayerJoinEvent implements Listener {
    
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        
        Player player = event.getPlayer();
        DiscordBot bot = DiscordEssentials.getBot();

        EssentialsTeam team = DiscordEssentials.api.scoreboard.getEssentialsTeam(player);
        if(team == null) {
            team = DiscordEssentials.api.scoreboard.setTeam(player);
        }
        
        String prefix = "[" + team.getPrefix() + "]";
        Link link = DatabaseUtil.getLinkbyUUID(player.getUniqueId());

        if(!EssentialsPermission.isVanished(player)) {
            bot.getWebhook().sendEmbed(
                Colors.SUCCESS.getColor(),
                DiscordEssentials.api.discord.getWebhookAvatarURL(),
                Webhook.getAvatarURL(player),
                "Server",
                prefix + " " + player.getName() + " joined the game."
            );
        }

        if(link != null && link.hasUserID()) {
            for(Guild guild : DiscordEssentials.getBot().getGuilds()) {
                
                Member member = guild.getMemberById(link.getUserID());
                Role role = guild.getRoleById(team.getRoleId());
                Role linkedRole = guild.getRoleById(DiscordEssentials.api.discord.getLinkedRole());

                if(role == null) {
                    Bukkit.getLogger().warning("[DEBUG] : GuildId{" + guild.getId() + "}, RoleId{" + team.getRoleId() + "}");
                    return;
                }

                if(linkedRole == null) {
                    Bukkit.getLogger().warning("[DEBUG] : GuildId{" + guild.getId() + "}, RoleId{" + DiscordEssentials.api.discord.getLinkedRole() + "}");
                    return;
                }

                if(!member.getRoles().contains(linkedRole)) {
                    guild.addRoleToMember(member, linkedRole).queue();
                }

                if(!member.getRoles().contains(role)) {
                    guild.addRoleToMember(member, role).queue();
                }
            }

        }
    }

}