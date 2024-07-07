package de.construkter.listeners;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class MemberLeave extends ListenerAdapter {
    @Override
    public void onGuildMemberRemove(GuildMemberRemoveEvent event) {
        System.out.println(event.getMember().getUser().getName() + " left " + event.getGuild().getName());
        TextChannel console;
        console = event.getJDA().getTextChannelById("1253435238273650731");
        console.sendMessage(event.getMember().getUser().getName() + "left the Server").queue();
        System.out.println(event.getMember().getUser().getName() + " left the Server " + event.getGuild().getName());
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("Nutzer verlassen");
        embed.addField("Name", event.getMember().getUser().getName(), false);
        embed.addField("ID", event.getMember().getUser().getId(), false);
        embed.addField("Account Creation", String.valueOf(event.getMember().getTimeCreated()), false);
        event.getJDA().getTextChannelById("1253796469819838605").sendMessageEmbeds(embed.build()).queue();
    }
}
