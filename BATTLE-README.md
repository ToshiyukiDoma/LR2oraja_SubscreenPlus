# LR2oraja LAN Battle 0.1

A two-player LAN battle extension integrated into LR2oraja Subscreen. This is an **experimental first build**: software integration and local TCP tests passed, but a real battle on two PCs and audible synchronization have not been verified.

## Setup

1. Close the game on both PCs. Back up your existing `Subscreen/lr2touch.jar`, then extract this package beside `beatoraja.exe` on each PC. Keep your existing settings, messages, backgrounds and profile styles.
2. Use identical `beatoraja.jar` builds, copies of the chart folders, and the same LN/CN/HCN mode. Refresh each song database if charts changed. Disable Rate Modifier and Judge Trainer.
3. On the host PC, run **Launch Battle Server.cmd**. The host may be one of the players or a separate PC. Its window shows its LAN IP, port and six-digit room code.
4. On each player PC, run **Launch LR2Touch.cmd** and enter song selection. Open **Battle** in the subscreen.
5. Enter the host's IP, port **24880**, and room code, then **Connect**. Every field has an **Edit** button with the full-size virtual keyboard. IP/port are remembered. When playing on the host PC itself, `127.0.0.1` works.

Connection is manual; discovery is not required. Allow the host's Java process or inbound TCP 24880 through Windows Firewall on your **private** network. For browser control and separate-PC OBS, allow inbound TCP 24881 and enable **WEB CONTROL: LOCAL ONLY** in the server window. Wi-Fi client/AP isolation or guest networks can prevent connections. No port forwarding is needed; this build is for a trusted LAN.

## Browser control

On the server PC, enable **WEB CONTROL: LOCAL ONLY** after reviewing the LAN access confirmation. Open the displayed `http://HOST-IP:24881/control.html` address on a phone connected to the same Wi-Fi/LAN. Sign in as **admin** using the generated password shown in the server window. The page reports player connection/score and chart state and can choose either offer, check sync, start or cancel the round, or toggle single-player test mode. The password is generated for each server launch. Disable LAN access when finished; the OBS feed remains read-only.

## Single-client testing

With one server PC and one game PC, tap the server's top **MODE: TWO PLAYER BATTLE** button to switch to **SINGLE PLAYER TEST** before loading a chart. Connect the game PC using the server IP, offer a chart, choose **Use player 1 chart**, then **Check sync** and **START BATTLE** once READY appears. The server does not need a running game.

This checks the connection, local chart loading, READY hold, server-controlled start, EX score and OBS feed with one client. It does not verify matching assets between PCs or synchronization between two players. The overlay marks the difference as TEST. Only one client is accepted in test mode. Finish or cancel the round before changing modes; disconnect player 2 before enabling it. The server defaults to two-player mode on every launch.

## Start a battle

1. Each player may select a chart in the game and tap **Offer selected song**.
2. The host chooses **Use player 1 chart** or **Use player 2 chart**. Both clients must have the chosen chart. The host checks chart-folder fingerprints before loading either game.
3. Both games load and wait at the gameplay READY screen. The host waits for **READY**, presses **Check sync**, then **START BATTLE**.
4. A shared start is scheduled five seconds ahead, with acknowledgements from both clients. EX scores update five times per second; the host displays P1 minus P2.
5. After the chart, return both games to song selection before offering another chart.

Connection means the host may load and start the agreed chart. Cancel/disconnect before the start returns waiting clients to selection. A round interrupted after starting is marked invalid; clients already playing may finish locally. Normal game score/IR behavior is unchanged.

## OBS elements

The server's **WEB CONTROL: LOCAL ONLY** button enables LAN viewing after a confirmation. Then **OBS ELEMENT URLS / COPY** opens seven transparent Browser Source URLs using the host PC's LAN IP. On the streaming PC, allow inbound TCP 24881 through Windows Firewall on a private network. Disable LAN access when remote viewing is no longer needed. The local-only setting remains the default. Use one source per field and click **COPY** beside its URL:

- Song title: `/title.html`
- Artist: `/artist.html`
- Player 1 name and score: `/player1-name.html`, `/player1-score.html`
- Player 2 name and score: `/player2-name.html`, `/player2-score.html`
- Score difference: `/score-difference.html` (shows the absolute difference in single-player mode too; `<` marks a P1 lead and `>` marks a P2 lead). The number stays centered while the marker sits beside it and inherits its OBS text style.

All pages have a transparent background and unstyled text so you can apply fonts, colors, outlines and positioning in OBS. Song title and artist automatically scroll horizontally when longer than the source width; size and position their viewport with `#viewport` CSS. Text placement for other fields is controlled in OBS Custom CSS rather than fixed by the page. For example, `body { display: flex; justify-content: center; align-items: center; }` centers each regular field. On the score difference page, use `#score { justify-content: center; align-items: center; }`; left/right markers follow the number. Set each Browser Source size to suit the element. Each page refreshes from the local read-only feed every 200 ms.

## OBS overlay

On the **server PC**, add an OBS Browser Source:

`http://127.0.0.1:24881/`

Set width **1280**, height **320**. Edit `Subscreen/battle-overlay/style.css` to change colors, fonts and layout; refresh the browser source afterward. `index.html` is also editable. The read-only JSON feed is `/state.json`. No score files are rewritten during play. The server is localhost-only by default; enabling LAN web access also makes the read-only overlay and authenticated admin controls reachable on the private LAN.

## Limits and troubleshooting

If a selected offer is cancelled with a chart path, that client could not open any indexed copy of the chart at that location. Confirm the offered chart exists on both game PCs and refresh each song database. Duplicate database entries are now checked until a valid copy is found.


- Current support: two clients, normal single-chart play, self-contained BMS/BME/BML/PMS/BMSON folders. Charts with `#RANDOM` / `#SETRANDOM`, linked assets or media outside the chart folder are rejected. The entire folder is compared, including other difficulty files; extra/different files cause a mismatch. No songs are transferred.
- Clock checks require at least eight samples, RTT at most 20 ms and estimated clock uncertainty at most 10 ms per client. Unstable links block starting. Wired LAN is recommended.
- Frame timing and independent audio devices still add latency. Reported software start skew measures gate release, **not audible speaker synchronization**. Starts more than 40 ms late are refused. A last-moment network partition cannot guarantee that both machines stop simultaneously.
- To choose other ports: `"Launch Battle Server.cmd" 24890 24891`. Optional third argument: a six-digit room code. The OBS URL uses the second port.
- The separate server package needs Java 17+ on PATH, or the game's `jre` folder. The full client package uses the game's bundled runtime. No new runtime download is needed on a game PC.

See `SUBSCREEN-README.md` for backgrounds, optional FFmpeg, other tabs and uninstall instructions. Restore your previous add-on JAR to roll back the battle extension. Original game binaries are never rewritten.
