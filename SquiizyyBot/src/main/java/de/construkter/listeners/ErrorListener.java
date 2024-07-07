package de.construkter.listeners;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class ErrorListener extends ListenerAdapter {
    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) {
            String message = event.getMessage().getContentRaw();
            if (message.startsWith("java.")) {
                TextChannel errorTextChannel = event.getJDA().getTextChannelById("1254405378318405693");
                EmbedBuilder embedBuilder = new EmbedBuilder();
                embedBuilder.setTitle("Possible Error");
                embedBuilder.setDescription("A Bots message startet with `java.` which could be a possible Error Message. Please check out the bots Console");
                embedBuilder.addField("Message", message, false);
                embedBuilder.addField("User", event.getAuthor().getName(), true);
                embedBuilder.addField("ID", event.getAuthor().getId(), true);
                embedBuilder.addField("Channel", event.getChannel().getName(), false);
                embedBuilder.addField("ChannelID", event.getChannel().getId(), true);
                embedBuilder.setColor(Color.ORANGE);
                embedBuilder.setFooter("Automated Message");
                if (message.toLowerCase().contains("nullpointer")){
                    embedBuilder.addField("", "", false);
                    embedBuilder.addField("Possible Solution", "Double check that you aren't using variables that are null (empty). Maybe use ```java\nif(var != null) {\n//your task\n}\n``` or ```java\nObjects.requireNonNull(\"Your Task\")```", false);
                } else {
                    embedBuilder.addField("", "", false);
                    embedBuilder.addField("Possible Solution", "*No data entry found for this exception*", false);
                }
                assert errorTextChannel != null;
                errorTextChannel.sendMessageEmbeds(embedBuilder.build()).queue();

            }
        }
    }
}
