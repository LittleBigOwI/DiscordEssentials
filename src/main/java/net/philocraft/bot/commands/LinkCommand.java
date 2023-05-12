package net.philocraft.bot.commands;

import java.sql.SQLException;

import org.bukkit.Bukkit;

import dev.littlebigowl.api.models.EssentialsScoreboard;
import dev.littlebigowl.api.models.EssentialsTeam;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.philocraft.DiscordEssentials;
import net.philocraft.models.Link;
import net.philocraft.utils.DatabaseUtil;

public class LinkCommand extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {

        if(event.getName().equals("link")) {
            OptionMapping codeOption = event.getOption("code");
            String userid = event.getUser().getId();

            String response;

            if(codeOption == null) {
                Link link = DatabaseUtil.getLinkbyUserID(userid);
                String code;

                if(link == null) {
                    try {
                        code = DatabaseUtil.createLink(userid);
                    } catch (SQLException e) {
                        code = null;
                        DiscordEssentials.getPlugin().getLogger().severe("Couldn't create link : " + e.getMessage());
                    }

                    response = "Your code is : " + code;

                } else {
                    code = link.getCode();

                    if(link.hasUUID() && link.hasUserID()) {
                        response = "Your account is already linked.";
                    } else {
                        response = "Your code is : " + code;
                    }
                }

            } else {
                String givenCode = codeOption.getAsString();
                Link link = DatabaseUtil.getLinkbyCode(givenCode);
                Link playerLink = DatabaseUtil.getLinkbyUserID(userid);

                if(playerLink != null && playerLink.isComplete()) {
                    response = "Your account is already linked.";
                
                } else if(link == null) {
                    response = "Couldn't find link for given code.";
                
                } else if(link.hasUUID() && link.hasUserID()) {
                    response = "Your account is already linked.";

                } else if(link.hasUserID()) {
                    response = "You need to execute this command on the minecraft server.";
                
                } else {
                    try {
                        DatabaseUtil.completeLink(link.getUUID(), userid);
                    } catch (SQLException e) {
                        DiscordEssentials.getPlugin().getLogger().severe("Couldn't complete link : " + e.getMessage());
                        return;
                    }
                    
                    link = DatabaseUtil.getLinkbyUserID(event.getMember().getId());

                    for(EssentialsTeam team : DiscordEssentials.api.scoreboard.getEssentialsTeams()) {
                        if(team.getPlaytime() >= 0 &&team.getPlaytime() <= EssentialsScoreboard.getPlaytime(Bukkit.getOfflinePlayer(link.getUUID()))) {
                            for(Guild guild : DiscordEssentials.getBot().getGuilds()) {
                                
                                Member member = event.getMember();
                                Role role = guild.getRoleById(team.getRoleId());
                                Role linkedRole = guild.getRoleById(DiscordEssentials.api.discord.getLinkedRole());
                
                                if(!member.getRoles().contains(linkedRole)) {
                                    guild.addRoleToMember(member, linkedRole).queue();
                                }
                
                                if(!member.getRoles().contains(role)) {
                                    guild.addRoleToMember(member, role).queue();
                                }
                            }
                        }
                    }

                    response = "Successfully linked your account.";
                }
            }

            event.reply(response).setEphemeral(true).queue();
        }

    }

}
