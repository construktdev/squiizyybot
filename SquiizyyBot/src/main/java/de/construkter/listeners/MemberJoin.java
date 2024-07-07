package de.construkter.listeners;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class MemberJoin extends ListenerAdapter {
    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent event) {
        System.out.println(event.getMember().getUser().getName() + " joined " + event.getGuild().getName());
        TextChannel console;
        console = event.getJDA().getTextChannelById("1253435238273650731");
        console.sendMessage(event.getMember().getUser().getName() + "joined the Server").queue();
        System.out.println(event.getMember().getUser().getName() + " joined the Server " + event.getGuild().getName());
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("Nutzer beigetreten");
        embed.addField("Name", event.getMember().getUser().getName(), false);
        embed.addField("ID", event.getMember().getUser().getId(), false);
        embed.addField("Account Creation", String.valueOf(event.getMember().getTimeCreated()), false);
        console.sendMessageEmbeds(embed.build()).queue();
    }
}
