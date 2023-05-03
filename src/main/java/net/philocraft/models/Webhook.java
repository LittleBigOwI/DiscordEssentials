package net.philocraft.models;

import java.awt.Color;

import org.bukkit.entity.Player;

import club.minnced.discord.webhook.WebhookClient;
import club.minnced.discord.webhook.WebhookClientBuilder;
import club.minnced.discord.webhook.send.WebhookEmbedBuilder;
import club.minnced.discord.webhook.send.WebhookMessage;
import club.minnced.discord.webhook.send.WebhookMessageBuilder;
import club.minnced.discord.webhook.send.WebhookEmbed.EmbedAuthor;

public class Webhook {

    private WebhookMessageBuilder messageBuilder;
    private WebhookClientBuilder clientBuilder;

    public Webhook(String webhookURL) {
        this.messageBuilder = new WebhookMessageBuilder();
        this.clientBuilder = new WebhookClientBuilder(webhookURL);
    }

    public static String getAvatarURL(String username) {
        return "https://minotar.net/avatar/" + username + ".png";
    }

    public static String getAvatarURL(Player player) {
        return "https://minotar.net/avatar/" + player.getName() + ".png";
    }

    public void sendMessage(String username, String message) {
        String avatarURL = getAvatarURL(username);
        
        this.messageBuilder.setContent(message);
        this.messageBuilder.setAvatarUrl(avatarURL);
        this.messageBuilder.setUsername(username);

        WebhookMessage webhookMessage = this.messageBuilder.build();

        WebhookClient client = this.clientBuilder.build();
        client.send(webhookMessage);
    }

    public void sendEmbed(Color color, String avatarURL, String authorAvatarURL, String username, String message) {
        this.messageBuilder.addEmbeds(new WebhookEmbedBuilder().setColor(color.hashCode()).setAuthor(new EmbedAuthor(message, authorAvatarURL, null)).build());
        this.messageBuilder.setAvatarUrl(avatarURL);
        this.messageBuilder.setUsername(username);

        WebhookMessage webhookMessage = this.messageBuilder.build();

        WebhookClient client = this.clientBuilder.build();
        client.send(webhookMessage);
    }
    
}
