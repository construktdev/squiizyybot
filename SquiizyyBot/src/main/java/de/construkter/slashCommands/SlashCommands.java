package de.construkter.slashCommands;

import de.construkter.Main;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.awt.*;

public class SlashCommands extends ListenerAdapter {
    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        switch (event.getName()) {
            case "socials":
                EmbedBuilder embedBuilder = new EmbedBuilder();
                embedBuilder.setTitle("Squiizzy's Kanäle:");
                embedBuilder.setDescription("<:twitch:1253761831630143630> **Twitch:** https://twitch.tv/xsquiizyy \n<:tiktok:1253762186896080926> **TikTok:** https://tiktok.com/@xsquiizyy \n<:instagram:1253762184811647128> **Instagram:** https://instagram.com/xsquiizyy \n<:yt:1253762188506697798> **YouTube:** https://www.youtube.com/@xSquiizyy");
                embedBuilder.setColor(Color.CYAN);
                embedBuilder.setFooter("Squiizyy Bot Beta");
                event.replyEmbeds(embedBuilder.build()).queue();
                break;
            case "help":
                EmbedBuilder helpEmbed = new EmbedBuilder();
                helpEmbed.setTitle("\uD83D\uDEE0\uFE0F Squiizyy Bot Commands: \uD83D\uDEE0\uFE0F");
                helpEmbed.setDescription("**/help** -> Zeigt dir diese Liste \n**/socials** -> Zeigt dir eine Liste mit allen Squiizyy Social Media Kanälen \n**/whois** (beta) -> Zeigt dir Informationen zu einer Person");
                helpEmbed.addField("\uD83E\uDE9B - Updates", "Wir arbeiten konstant an neuen Commands, also wird diese Liste noch länger werden", false);
                helpEmbed.setColor(Color.CYAN);
                helpEmbed.setFooter("Squiizyy Bot Beta");
                event.replyEmbeds(helpEmbed.build()).queue();
                break;
            case "info":
                EmbedBuilder infoEmbed = new EmbedBuilder();
                infoEmbed.setTitle("ℹ\uFE0F Infos: ℹ\uFE0F");
                infoEmbed.setDescription("\uD83E\uDD16 **Programmiersprache:** Java \n\uD83D\uDC68\u200D\uD83D\uDCBB **Entwickler:** Construkter \n⚙\uFE0F **JDK-Version:** Oracle Open JDK 22.0.1");
                infoEmbed.setFooter("Squiizyy Bot Beta");
                infoEmbed.setColor(Color.CYAN);
                event.replyEmbeds(infoEmbed.build()).queue();
                break;
            case "whois":
                if (event.getOption("id") != null){
                    long id = event.getOption("id").getAsLong();
                    User user = (User) event.getJDA().retrieveUserById(id);
                    String name = user.getName();
                    String avatar = user.getAvatarUrl();
                    String timeCreated = String.valueOf(user.getTimeCreated());
                    EmbedBuilder whoisEmbedID = new EmbedBuilder();
                    whoisEmbedID.setTitle("WhoIs " + id);
                    whoisEmbedID.addField("ID", String.valueOf(id), false);
                    whoisEmbedID.addField("Name", name, false);
                    whoisEmbedID.addField("Avatar", avatar, false);
                    whoisEmbedID.addField("Time Created", timeCreated, false);
                    whoisEmbedID.setColor(Color.CYAN);
                    whoisEmbedID.setFooter("Squiizyy Bot Beta");
                    event.replyEmbeds(whoisEmbedID.build()).queue();
                } else {
                    User user = event.getUser();
                    String name = user.getName();
                    String avatar = user.getAvatarUrl();
                    String timeCreated = String.valueOf(user.getTimeCreated());
                    EmbedBuilder whoisEmbed = new EmbedBuilder();
                    whoisEmbed.setTitle("WhoIs " + user.getId());
                    whoisEmbed.addField("ID", String.valueOf(user.getId()), false);
                    whoisEmbed.addField("Name", name, false);
                    whoisEmbed.addField("Avatar", avatar, false);
                    whoisEmbed.addField("Time Created", timeCreated, false);
                    whoisEmbed.setColor(Color.CYAN);
                    whoisEmbed.setFooter("Squiizyy Bot Beta");
                    event.replyEmbeds(whoisEmbed.build()).queue();
                }
                break;
            case "test":
                if (event.getUser().getName().equals("construkter"))
                    event.reply("java.lang.NullpointerException: NullpointerException!").queue();
                break;
            case "check":
                Main.checkyt();
                event.reply("Succesfully checked for new YouTube Video!").setEphemeral(true).queue();
        }
        System.out.println(event.getUser().getName() + " used /" + event.getName() + " in the guild " + event.getGuild().getName() + " in the channel " + event.getChannel().getName());
        EmbedBuilder testEmbed = new EmbedBuilder();
        testEmbed.setTitle("Command Logger");
        testEmbed.setDescription("A command got executed");
        testEmbed.addField("Command", "/" + event.getName(), false);
        testEmbed.addField("User", event.getUser().getName(), true);
        testEmbed.addField("ID", event.getUser().getId(), true);
        testEmbed.addField("Guild", event.getGuild().getName(), false);
        testEmbed.addField("Channel", event.getChannel().getName(), true);
        testEmbed.addField("Channel ID", event.getChannel().getId(), true);
        testEmbed.setColor(Color.CYAN);
        testEmbed.setFooter("Squiizyy Bot Logging Beta (automated message)");
        event.getJDA().getTextChannelById("1253796469819838605").sendMessageEmbeds(testEmbed.build()).queue();
    }
}
