package net.philocraft.commands;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import dev.littlebigowl.api.constants.Colors;
import dev.littlebigowl.api.errors.InvalidArgumentsException;
import dev.littlebigowl.api.errors.InvalidSenderException;
import net.philocraft.DiscordEssentials;
import net.philocraft.models.Link;
import net.philocraft.utils.DatabaseUtil;

public class LinkCommand implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        
        if(!(sender instanceof Player && label.equalsIgnoreCase("link"))) {
            return new InvalidSenderException("You need to be a player to use this command.").sendCause(sender);
        }

        if(args.length > 1) {
            return new InvalidArgumentsException().sendCause(sender);
        }
        
        Player player = (Player)sender;
        String response;

        if(args.length == 0) {
            Link link = DatabaseUtil.getLinkbyUUID(player.getUniqueId());
            String code;

            if(link == null) {
                try {
                    code = DatabaseUtil.createLink(player.getUniqueId());
                } catch (SQLException e) {
                    code = null;
                    DiscordEssentials.getPlugin().getLogger().severe("Couldn't create link : " + e.getMessage());
                }

                response = Colors.INFO.getChatColor() + "Your code is : " + Colors.MAJOR.getChatColor() + code;

            } else {
                code = link.getCode();

                if(link.hasUUID() && link.hasUserID()) {
                    response = Colors.SUCCESS.getChatColor() + "Your account is already linked.";
                } else {
                    response = Colors.INFO.getChatColor() + "Your code is : " + Colors.MAJOR.getChatColor() + code;
                }
            }
        
        } else {
            String givenCode = args[0];
            Link link = DatabaseUtil.getLinkbyCode(givenCode);
            Link playerLink = DatabaseUtil.getLinkbyUUID(player.getUniqueId());

            if(playerLink != null && playerLink.isComplete()) {
                response = Colors.SUCCESS.getChatColor() + "Your account is already linked.";

            } else if(link == null) {
                response = Colors.FAILURE.getChatColor() + "Couldn't find link for given code.";
            
            } else if(link.hasUUID()) {
                response = Colors.INFO.getChatColor() + "You need to execute this command in discord to link your account.";
            
            } else {
                try {
                    DatabaseUtil.completeLink(player.getUniqueId(), link.getUserID());
                } catch (SQLException e) {
                    DiscordEssentials.getPlugin().getLogger().severe("Couldn't complete link : " + e.getMessage());
                    return true;
                }

                response = Colors.SUCCESS.getChatColor() + "Successfully linked your account.";
            }
        }

        player.sendMessage(response);
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return new ArrayList<>();
    }
    
}
