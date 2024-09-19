# SquiizyyBot 
## Archiviert


## Idee
Die Idee war es einen Discord Bot zu entwickeln der auf Discord eine Benachrichtigung sendet, wenn der Streamer xSquiizyy ein neues Video bzw. Short hochlädt.

## Umsetzung
Das Projekt wurde letztendlich mit Java und der Java Discord API (JDA v5.0.0-beta.24 -> [Github](https://github.com/discord-jda/JDA)) umgesetzt. Es wurde die offizielle YouTube API genutzt. 


## Setup
Hier ist ein kleines Beispiel wie man die YouTube Alerts und den Bot aufsetzen kann:
<procedure title="Bot aufsetzen" id="setup-bot">
    <step>
        <p>Erstellen sie eine Application auf dem <a href="https://discord.com/developers/applications">Discord Developer Portal</a></p>
    </step>
    <step>
        <p>Füge den Token in der Token.java Klasse im Projekt ein. (<a href="https://github.com/construktdev/squiizyybot">Projekt Source</a>)</p>
    </step>
</procedure>

<procedure title="YouTube Alerts aufsetzen" id="setup-yt">
    <step>
        <p>Gehen Sie auf das Google Console Dashboard und melden Sie sich an.</p>
    </step>
    <step>
        <p>Erstellen sie ein neues Projekt und fügen sie die YouTube API hinzu</p>
        <img src="cloud1.png"></img>
        <img src="cloud2.png"></img>
    </step>
    <step>
        <p>Erstellen Sie einen API Key und fügen ihn im Projekt in der Klasse Main ein.</p>
    </step>
</procedure>

