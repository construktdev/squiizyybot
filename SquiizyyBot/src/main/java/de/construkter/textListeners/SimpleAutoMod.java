package de.construkter.textListeners;

import de.construkter.utils.LogBuilder;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.awt.*;

public class SimpleAutoMod extends ListenerAdapter {
    String[] badwords = {"hure", "niga", "nigga", "nigger", "niger", "heil", "hitler", "hail"};

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        for (String badword : badwords) {
            if (event.getMessage().getContentRaw().toLowerCase().contains(badword)) {
                event.getMessage().delete().queue();
                EmbedBuilder embed = new EmbedBuilder();
                embed.setTitle("⚠\uFE0F Warnung ⚠\uFE0F");
                embed.setDescription("Du hast gegen die Regeln verstoßen und ein unzulässiges Wort gesendet!");
                embed.addField("Nachricht", event.getMessage().getContentRaw(), true);
                embed.addField("Wort", badword, true);
                embed.setFooter("Beta - Fehler bei construkter melden");
                event.getChannel().sendMessageEmbeds(embed.build()).queue();
                TextChannel channel = event.getJDA().getTextChannelById("1255186666369323040");
                //channel.sendMessageEmbeds(LogBuilder.sendLog("AutoMod Alert", "A user sent a badword", "Beta", Color.red, event.getAuthor().getName(), event.getAuthor().getIdLong(), event.getChannel().getName(), event.getChannel().getIdLong())).queue();
            }
        }
    }
}
