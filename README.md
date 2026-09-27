The original repository was from here: https://github.com/ToshiyukiDoma/LR2oraja_Subscreen

It is recommended to have a secondary display with touchscreen capabilities or just a second display but not required. Tho you will end up alt-tabbing every time you need to navigate into the second screen. I'll work on having an option so that it is integrated into the game instead as a mod menu.

This adds the local matching feature that allows control of sync and gameplay start of two clients.
You can use the the server while running the client on it but it can be also used on a separate system unit.

Example:
```
- PC 1: Server
- PC 2: Player 1
- PC 3: Player 2

OR

- PC 1: Server and Player 1
- PC 2: Player 2
```

- You can also control the Server via Web UI which allows access via mobile device. You'll only need to login using the provided user and password on the server application. This will be updated because it was annoying typing username and password. lol
- Clients must have the exact copies of the charts. I cannot verify yet if the folder structure can be different but I believe I asked it to also just check file hashes. Otherwise, I'll improve this later on.

# Initial setup
1. Start the server application from the host PC using `Launch Battle Server.cmd`
2. Ensure that the server is enabled for two players (it is by default)
3. Player 1 and Player 2 must run the game by running `Launch LR2Touch.cmd`
4. Both players must navigate into the `BATTLE` tab of the subscreen then connect to the server by typing in the Server's IP Address, Port, and Pin Code. Pin code is provided on the server application.

# Gameplay setup
1. Once both system is connected to the server, Player 1 and Player 2 can select charts they want to play then offer it to the server on the subscreen.
2. Server will select which chart to play by accepting either Play 1 or Player 2's chart selection. Once selected, the server application will verify if the chart file also exists on the other client. Once verified, it will automatically go to decide screen then gameplay screen.
3. Once gameplay screen is reached, it will not start the game immediately after the "GAME START" shows up.
4. Press the sync button to ensure the sync of each player.
5. Once it was verified, pressing "Game Start" on the server will schedule the actual game start of the charts on both clients.

# Server Additional setup
- You can enable the localhost for Web-based UI controls. You will need to login with the provided credentials from the server application.

# Current Limitations / Plans for Improvements
1. Pacemaker display is purely local and cannot be synced to the server - unsure if it is possible without modifying the game itself.
2. Limited to two players only. Can add for more players later on, I guess?
3. Chart verification may take some time but I'm unsure if there's a potential memory leak because sometimes it can be fixed by restarting both clients.
4. Players on different networks will not work. Will work on support for that without exposing your Private IP Address to anyone.
5. There's probably more. I'm writing this while thinking about the issues in real time. lmao

# Cautions
This is purely vibe-coded so please use it at your own risk.
This is made with an intention of purely personal use and for LAN Party when friends are over.
