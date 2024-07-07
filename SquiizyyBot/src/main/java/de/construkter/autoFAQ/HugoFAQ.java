package de.construkter.autoFAQ;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import java.awt.*;

public class HugoFAQ extends ListenerAdapter {
    long userid;
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        String message = event.getMessage().getContentRaw();
        if (message.toLowerCase().contains("hugo")) {
            userid = event.getAuthor().getIdLong();
            EmbedBuilder hugoFAQ = new EmbedBuilder();
            hugoFAQ.setTitle("Hugo FAQ");
            hugoFAQ.setDescription(event.getAuthor().getAsTag() + " **NEIN**, Squiizyy ist nicht mit Hugo verwandt oder versucht Hugo nachzumachen.  Squiizyy ist ein ganz normaler Streamer der seiner Leidenschaft nach geht auch wenn er so aussieht und ihm sehr ähnlich ist!");
            hugoFAQ.setFooter("Drücke den Button wenn dir diese Nachricht nicht weiterhilft, oder sie unpassend ist");
            hugoFAQ.setColor(Color.RED);
            Button deleteButton = Button.danger("delete_button", "Löschen");
            Button moreButton = Button.primary("moreButton", "Mehr erfahren");
            event.getChannel().sendMessageEmbeds(hugoFAQ.build())
                    .setActionRow(moreButton, deleteButton)
                    .queue();
        }
    }

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        if (event.getButton().getId().equals("delete_button")) {
            if (event.getUser().getIdLong() == userid) {
                event.deferReply(true).queue();
                event.getHook().sendMessage("Danke das du uns hilfst den Squiizyy Bot besser zu machen!").setEphemeral(true).queue();
                event.getMessage().delete().queue();
                return;
            }
            event.reply("Diese nachrcht gehört nicht dir").setEphemeral(true).queue();
        } else if (event.getButton().getId().equals("moreButton")) {
            event.deferReply(true).queue();
            EmbedBuilder hugoMoreFAQ = new EmbedBuilder();
            hugoMoreFAQ.setTitle("Hugo FAQ");
            hugoMoreFAQ.setDescription("Squiizyy hat nix mit Hugo zu tun und versucht ihn auch nicht nachzumachen. Er streamt so wie er auch immer spricht und lacht, wenn er nicht streamt. Squiizyy hat sogar schon gestreamt bevor Hugo so bekannt geworden ist. Also ist er kein Nachmacher sondern ein ganz normaler Streamer :)");
            hugoMoreFAQ.setColor(Color.CYAN);
            event.getHook().sendMessageEmbeds(hugoMoreFAQ.build()).queue();
        }
    }
}
