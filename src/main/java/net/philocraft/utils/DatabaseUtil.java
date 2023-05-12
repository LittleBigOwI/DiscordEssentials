package net.philocraft.utils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import dev.littlebigowl.api.models.EssentialsTeam;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.philocraft.DiscordEssentials;
import net.philocraft.models.Link;

public class DatabaseUtil {

    private static final ArrayList<Link> links = new ArrayList<>();
    private static final HashMap<Player, EssentialsTeam> playerTeams = new HashMap<>();
    
    private static String generateCode() {
        String upper = "BCDFGHJKLMNPQRSTVWXZ";
        String lower = "bcdfghjklmnpqrstvwxz";
        String number = "0123456789";
        
        int len = (int)(Math.random()*(12-8)) + 8;

        String code = "";

        for(int i = 0; i < len; i++) {
            int random = (int)(Math.random()*3);
            switch(random) {
                case 0 : code += String.valueOf(number.charAt((int)(number.length()*Math.random()))); break;
                case 1 : code += String.valueOf(lower.charAt((int)(lower.length()*Math.random()))); break;
                case 2 : code += String.valueOf(upper.charAt((int)(upper.length()*Math.random()))); break;
            }
        }

        return code;
    }

    private static ArrayList<String> getCodes() {
        ArrayList<String> codes = new ArrayList<>();

        for(Link link : DatabaseUtil.links) {
            codes.add(link.getCode());
        }

        return codes;
    }

    public static void loadLinks() throws SQLException {
        ResultSet databaseCodes = DiscordEssentials.api.database.fetch("SELECT * FROM Accounts;");

        while(databaseCodes.next()) {
            UUID uuid;
            String databaseUUID = databaseCodes.getString("uuid");
            
            if(databaseUUID == null) {
                uuid = null;
            } else {
                uuid = UUID.fromString(databaseUUID);
            }

            DatabaseUtil.links.add(
                new Link(
                    uuid,
                    databaseCodes.getString("userid"),
                    databaseCodes.getString("code")
                )
            );
        }
    }

    public static String createLink(String userid) throws SQLException {
        String code = DatabaseUtil.generateCode();
        
        while(DatabaseUtil.getCodes().contains(code)) {
            code = DatabaseUtil.generateCode();
        }

        DiscordEssentials.api.database.update(
            "INSERT INTO Accounts(uuid, userid, code) VALUES(" +
            "null, '" +
            userid + "', '" +
            code + "');"
        );

        DatabaseUtil.links.add(new Link(null, userid, code));
        return code;
    }

    public static String createLink(UUID uuid) throws SQLException {
        String code = DatabaseUtil.generateCode();
        
        while(DatabaseUtil.getCodes().contains(code)) {
            code = DatabaseUtil.generateCode();
        }

        DiscordEssentials.api.database.update(
            "INSERT INTO Accounts(uuid, userid, code) VALUES('" +
            uuid + "', " +
            "null, '" +
            code + "');"        );

        DatabaseUtil.links.add(new Link(uuid, null, code));
        return code;
    }

    public static Link getLinkbyCode(String code) {
        for(Link link : DatabaseUtil.links) {
            if(link.getCode().equals(code)) {
                return link;
            }
        }

        return null;
    }

    public static Link getLinkbyUserID(String userid) {
        for(Link link : DatabaseUtil.links) {
            if(link.getUserID() != null && link.getUserID().equals(userid)) {
                return link;
            }
        }

        return null;
    }

    public static Link getLinkbyUUID(UUID uuid) {
        for(Link link : DatabaseUtil.links) {
            if(link.getUUID() != null && link.getUUID().equals(uuid)) {
                return link;
            }
        }

        return null;
    }

    public static void completeLink(UUID uuid, String userid) throws SQLException {

        DiscordEssentials.api.database.update("DELETE FROM Accounts WHERE userid='" + userid + "';");
        DiscordEssentials.api.database.update("UPDATE Accounts SET userid='" + userid + "' WHERE uuid='" + uuid + "';");

        for(Link link : DatabaseUtil.links) {
            if(link.hasUserID() && link.getUserID().equals(userid)) {
                links.remove(link);      
            }
        }

        Link link = DatabaseUtil.getLinkbyUUID(uuid);
        link.setUUID(uuid);
        link.setUserID(userid);

        links.add(link);
    }

    public static void checkRoles() {

        DiscordEssentials.getPlugin().getServer().getScheduler().runTaskTimer(DiscordEssentials.getPlugin(), () -> {
            for(Player player : Bukkit.getOnlinePlayers()) {
                
                EssentialsTeam team = DiscordEssentials.api.scoreboard.getEssentialsTeam(player);
                Link link = DatabaseUtil.getLinkbyUUID(player.getUniqueId());

                if(playerTeams.get(player) == null) {
                    playerTeams.put(player, team);
                }

                if(!playerTeams.get(player).getName().equals(DiscordEssentials.api.scoreboard.getEssentialsTeam(player).getName()) && link != null && link.hasUserID()) {                    
                    for(Guild guild : DiscordEssentials.getBot().getGuilds()) {
                        Member member = guild.getMemberById(link.getUserID());

                        Role role = guild.getRoleById(team.getRoleId());
                        guild.addRoleToMember(member, role).queue();
                    }

                }

                playerTeams.put(player, team);
            }
        }, 0, 20);

    }

    public static ArrayList<Link> getLinks() {
        return DatabaseUtil.links;
    }
}
