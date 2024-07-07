package de.construkter.listeners;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.awt.*;

public class OnReady extends ListenerAdapter {
    @Override
    public void onReady(ReadyEvent event) {
        System.out.println("Logged in as " + event.getJDA().getSelfUser().getName());
        System.out.println("Invite: " + event.getJDA().getInviteUrl());
        System.out.println("Guilds: " + event.getJDA().getGuilds().size());
        System.out.println("Users: " + event.getJDA().getUsers().size());
        TextChannel console;
        console = event.getJDA().getTextChannelById("1253796469819838605");
        EmbedBuilder readyEmbed = new EmbedBuilder();
        readyEmbed.setTitle("Ready");
        readyEmbed.setDescription("Diese Instanz des Squiizyy bots ist nun Bereit \n**Status:**");
        readyEmbed.addField("SlashCommands", "Bereit", false);
        readyEmbed.addField("Member Logging", "Bereit", true);
        readyEmbed.addField("YouTube Logging", "Bereit", true);
        readyEmbed.setColor(Color.GREEN);
        readyEmbed.setFooter("Squiizyy Bot by Construkter", event.getJDA().getSelfUser().getEffectiveAvatarUrl());
        console.sendMessageEmbeds(readyEmbed.build()).queue();
    }
}
