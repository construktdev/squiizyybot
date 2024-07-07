package de.construkter;

import de.construkter.autoFAQ.HugoFAQ;
import de.construkter.listeners.ErrorListener;
import de.construkter.listeners.MemberJoin;
import de.construkter.listeners.MemberLeave;
import de.construkter.slashCommands.SlashCommands;
import de.construkter.listeners.OnReady;
import de.construkter.textListeners.SimpleAutoMod;
import de.construkter.utils.TimeManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;

import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

public class Main {
    private static ShardManager shardManager = null;
    private static final String YOUTUBE_API_KEY = "AIzaSyDuqM_0UFxmhWvzJptS2LWX6T2o2p_vnBk";
    private static final String CHANNEL_ID = "UCVh1TonywzRUIzY1UEax_nA";
    private static final String INSTAGRAM_ACCESS_TOKEN = "YOUR_INSTAGRAM_ACCESS_TOKEN";
    private static final String INSTAGRAM_USER_ID = "YOUR_INSTAGRAM_USER_ID";
    private static final String DISCORD_CHANNEL_ID = "1253783270227836978";
    private static final String LAST_VIDEO_ID_FILE = "lastVideoId.txt";
    private static final String LAST_REEL_ID_FILE = "last_reel_id.txt";

    public Main(){
        DefaultShardManagerBuilder builder = DefaultShardManagerBuilder.createDefault(Token.getToken());
        builder.setStatus(OnlineStatus.ONLINE);
        builder.setActivity(Activity.streaming("Squiizyy", "https://twitch.tv/xsquiizyy"));
        builder.enableIntents(GatewayIntent.MESSAGE_CONTENT);
        builder.addEventListeners(new OnReady());
        builder.addEventListeners(new MemberLeave());
        builder.addEventListeners(new MemberJoin());
        builder.addEventListeners(new ErrorListener());
        builder.addEventListeners(new SlashCommands());
        builder.addEventListeners(new HugoFAQ());
        builder.addEventListeners(new SimpleAutoMod());

        shardManager = builder.build();

        CommandData socials = Commands.slash("socials", "Zeigt dir Squiizyy's Social Media Kanäle");
        CommandData help = Commands.slash("help", "Zeigt dir die Befehle des Bots");
        CommandData info = Commands.slash("info", "Zeigt dir diverse Infos zum Bot");
        CommandData whois = Commands.slash("whois", "Zeigt dir infos über die angegebene Person")
                .addOption(OptionType.NUMBER, "user-id", "Die Benutzer ID des gesuchten Benutzers");
        CommandData test = Commands.slash("test", "Testet die automatische Fehler Erkennung");
        CommandData check = Commands.slash("check", "Überprüfe in echtzeit ob ein neues Video hochgeladen wurde");

        shardManager.getShards().forEach(jda -> jda.updateCommands().addCommands(socials, help, info, whois, test, check).queue());
    }

    public static ShardManager getShardManager() {
        return shardManager;
    }

    public static void main(String[] args) {
        Main bot = new Main();
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new CheckYouTubeTask(), 0, 1016949);
        //timer.scheduleAtFixedRate(new CheckInstagramReelsTask(), 0, 60000);
    }
    private static class CheckYouTubeTask extends TimerTask {
        private final OkHttpClient httpClient = new OkHttpClient();

        @Override
        public void run() {
            EmbedBuilder checkEmbed = new EmbedBuilder();
            checkEmbed.setDescription("Succesfully checked for new video!");
            try {
                String latestVideoId = getLatestVideoId();
                if (latestVideoId != null && !latestVideoId.equals(getLastSavedVideoId())) {
                    String[] videoDetails = getVideoDetails(latestVideoId);
                    if (videoDetails != null) {
                        sendDiscordNotification(latestVideoId, videoDetails[0], videoDetails[1]);
                        saveLastVideoId(latestVideoId);
                        checkEmbed.setTitle("New Video found");
                        checkEmbed.setColor(Color.GREEN);
                        checkEmbed.setFooter(TimeManager.gettimestamp());
                        TextChannel channel = shardManager.getTextChannelById("1253800906047229975");
                        channel.sendMessageEmbeds(checkEmbed.build()).queue();
                    }
                } else {
                    checkEmbed.setTitle("No new Video found");
                    checkEmbed.setFooter(TimeManager.gettimestamp());
                    checkEmbed.setColor(Color.RED);
                    try {
                        Objects.requireNonNull(shardManager.getTextChannelById("1253800906047229975")).sendMessageEmbeds(checkEmbed.build()).queue();
                    } catch (Exception e){
                        System.err.println("Possible YouTube Alert embed failure...");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private String getLatestVideoId() {
            String url = String.format("https://www.googleapis.com/youtube/v3/search?key=%s&channelId=%s&part=snippet,id&order=date&maxResults=1", YOUTUBE_API_KEY, CHANNEL_ID);
            Request request = new Request.Builder().url(url).build();
            try (Response response = httpClient.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    EmbedBuilder embed = new EmbedBuilder();
                    embed.setTitle("YouTube Alert Error");
                    embed.setColor(Color.RED);
                    embed.setDescription("Got an unhandled Error from the Youtube API");
                    embed.addField("Response", String.valueOf(response), false);
                    embed.setFooter("Automated Message");
                    try {
                        Objects.requireNonNull(shardManager.getTextChannelById("1254405378318405693")).sendMessageEmbeds(embed.build()).queue();
                    } catch (Exception e){
                        System.err.println("Possible YouTube Alert embed failure...");
                    }


                    throw new IOException("Unexpected code " + response);
                }
                JSONObject jsonObject = new JSONObject(response.body().string());
                JSONArray items = jsonObject.getJSONArray("items");
                if (items.length() > 0) {
                    return items.getJSONObject(0).getJSONObject("id").getString("videoId");
                }
            } catch (IOException e) {
                System.err.println("Failed to get latest video ID: " + e.getMessage());
                e.printStackTrace();
            }
            return null;
        }


        private String[] getVideoDetails(String videoId) throws IOException {
            String url = String.format("https://www.googleapis.com/youtube/v3/videos?key=%s&id=%s&part=snippet", YOUTUBE_API_KEY, videoId);
            Request request = new Request.Builder().url(url).build();
            try (Response response = httpClient.newCall(request).execute()) {
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                JSONObject jsonObject = new JSONObject(response.body().string());
                JSONArray items = jsonObject.getJSONArray("items");
                if (items.length() > 0) {
                    JSONObject snippet = items.getJSONObject(0).getJSONObject("snippet");
                    String title = snippet.getString("title");
                    String thumbnailUrl = snippet.getJSONObject("thumbnails").getJSONObject("high").getString("url");
                    return new String[]{title, thumbnailUrl};
                }
            }
            return null;
        }

        private void sendDiscordNotification(String videoId, String videoTitle, String thumbnailUrl) {
            TextChannel channel = shardManager.getTextChannelById(DISCORD_CHANNEL_ID);
            if (channel != null) {
                String videoUrl = "https://www.youtube.com/watch?v=" + videoId;
                EmbedBuilder embedBuilder = new EmbedBuilder();
                embedBuilder.setTitle("Squiizyy hat ein neues Video hochgeladen");
                embedBuilder.setDescription("**" + videoTitle + "** \n [Jetzt anschauen](https://www.youtube.com/watch?v=" + videoId + ")\n");
                embedBuilder.setImage(thumbnailUrl);
                embedBuilder.setColor(Color.RED);
                channel.sendMessage("@everyone Squiizyy hat ein neues Video hochgeladen").queue();
                channel.sendMessageEmbeds(embedBuilder.build()).queue();
            } else {
                System.err.println("TextChannel is null! Check if the channel ID is correct or if the bot has access to the channel.");
            }
        }

        private void saveLastVideoId(String videoId) throws IOException {
            Files.write(Paths.get(LAST_VIDEO_ID_FILE), videoId.getBytes());
        }

        private String getLastSavedVideoId() throws IOException {
            Path path = Paths.get(LAST_VIDEO_ID_FILE);
            if (Files.exists(path)) {
                return new String(Files.readAllBytes(path));
            }
            return null;
        }
    }

    private static class CheckInstagramReelsTask extends TimerTask {
        private final OkHttpClient httpClient = new OkHttpClient();

        @Override
        public void run() {
            try {
                String latestReelId = getLatestReelId();
                if (latestReelId != null && !latestReelId.equals(getLastSavedReelId())) {
                    String[] reelDetails = getReelDetails(latestReelId);
                    if (reelDetails != null) {
                        sendDiscordNotification(latestReelId, reelDetails[0], reelDetails[1]);
                        saveLastReelId(latestReelId);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private String getLatestReelId() throws IOException {
            String url = String.format("https://graph.instagram.com/%s/media?fields=id,media_type&access_token=%s", INSTAGRAM_USER_ID, INSTAGRAM_ACCESS_TOKEN);
            Request request = new Request.Builder().url(url).build();
            try (Response response = httpClient.newCall(request).execute()) {
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                JSONObject jsonObject = new JSONObject(response.body().string());
                JSONArray data = jsonObject.getJSONArray("data");
                if (data.length() > 0) {
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject item = data.getJSONObject(i);
                        if ("VIDEO".equals(item.getString("media_type"))) {
                            return item.getString("id");
                        }
                    }
                }
            }
            return null;
        }

        private String[] getReelDetails(String reelId) throws IOException {
            String url = String.format("https://graph.instagram.com/%s?fields=id,caption,thumbnail_url&access_token=%s", reelId, INSTAGRAM_ACCESS_TOKEN);
            Request request = new Request.Builder().url(url).build();
            try (Response response = httpClient.newCall(request).execute()) {
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                JSONObject jsonObject = new JSONObject(response.body().string());
                String caption = jsonObject.optString("caption", "No title");
                String thumbnailUrl = jsonObject.optString("thumbnail_url", "");
                return new String[]{caption, thumbnailUrl};
            }
        }

        private void sendDiscordNotification(String reelId, String reelTitle, String thumbnailUrl) {
            TextChannel channel = shardManager.getTextChannelById(DISCORD_CHANNEL_ID);
            if (channel != null) {
                String reelUrl = "https://www.instagram.com/reel/" + reelId;
                EmbedBuilder embedBuilder = new EmbedBuilder();
                embedBuilder.setTitle("New Instagram Reel!");
                embedBuilder.setDescription("Check out the new reel: [**" + reelTitle + "**](" + reelUrl + ")");
                embedBuilder.setThumbnail(thumbnailUrl);
                embedBuilder.setColor(Color.MAGENTA);

                channel.sendMessageEmbeds(embedBuilder.build()).queue(
                        success -> System.out.println("Successfully sent message to Discord"),
                        error -> System.err.println("Failed to send message to Discord: " + error.getMessage())
                );
            } else {
                System.err.println("TextChannel is null! Check if the channel ID is correct or if the bot has access to the channel.");
            }
        }

        private void saveLastReelId(String reelId) throws IOException {
            java.nio.file.Files.write(java.nio.file.Paths.get(LAST_REEL_ID_FILE), reelId.getBytes());
        }

        private String getLastSavedReelId() throws IOException {
            java.nio.file.Path path = java.nio.file.Paths.get(LAST_REEL_ID_FILE);
            if (java.nio.file.Files.exists(path)) {
                return new String(java.nio.file.Files.readAllBytes(path));
            }
            return null;
        }
    }
    public static void checkyt(){
        new CheckYouTubeTask().run();
    }
}