package de.construkter.utils;

import de.construkter.Main;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

import java.awt.*;

public class LogBuilder {
    public static MessageEmbed sendLog(String embedTitle, String embedDescription, String embedFooter, Color color, String user, long userid, String channel, long channelID) {
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle(embedTitle);
        embed.setDescription(embedDescription);
        embed.setFooter(embedFooter);
        embed.setColor(color);
        embed.addField("User", user, false);
        embed.addField("Channel", channel, false);
        embed.addField("User-ID", String.valueOf(userid), false);
        embed.addField("Channel-ID", String.valueOf(channelID), false);
        return embed.build();
    }
}
