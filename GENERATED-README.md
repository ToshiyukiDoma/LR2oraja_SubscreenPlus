# LAN Battle 0.1 source

Production Java source, offline ECJ compiler and ASM are under `Subscreen/source`. The game dependency is not bundled.

Build from PowerShell:

```powershell
& .\Subscreen\source\Build.ps1 -GameDirectory 'D:\beatoraja0.8.8-jre-win64'
```

The resulting `Subscreen/lr2touch.jar` works in the client package and the standalone host. Java 17 bytecode; tested with the installed game's Java 21 runtime. Build output and tests are excluded from runtime packages.

## Design

- `BattleHooks`: fail-closed compatibility check for the single `BMSPlayer.render` play-start comparison. It delegates to a constant-time READY gate; no network or file work happens there.
- `BattleClient`: explicit IP/port/room-code connection, background SHA-256 checks, sampled score reads on the game thread, and Swing controls. File hashing uses one worker; telemetry is sampled at 5 Hz with only one queued game task.
- `BattleClock`: monotonic NTP-style offset samples, minimum RTT and a rolling 20-sample uncertainty estimate. Scheduled starts freeze the offset at ARM.
- `BattleServer`: exactly two authenticated slots and round IDs. CHECK → LOAD → READY → ARM → COMMIT → PLAYING → FINISHED, with abort paths, timeouts and bounded connection counts. Both ARM and COMMIT acknowledgements are required. This is not a distributed atomic-start guarantee.
- `BattleWire`: bounded UTF frames (16 fields, 4096 characters each), bounded outbound queues, socket timeouts and no Java serialization. The room code is a LAN access check, not encryption or anti-cheat.
- `BattleHttp`: loopback-only by default, bounded worker queue and request sizes; serves read-only overlay/state plus admin-password protected LAN controls when explicitly enabled. Remote text uses textContent.

## Verification

`tests/BattleVerify.java` runs clock/gate checks and real TCP loopback tests with two synthetic clients, including mismatch rejection, explicit sync confirmation, scheduled starts, EX/OBS data, bad-code/third-client rejection, missing acknowledgements and disconnects. Compile tests to a separate directory using ECJ with the add-on JAR and installed `beatoraja.jar` on the classpath. The fixture currently expects a `work` directory and `work/battle-release/Subscreen/battle-overlay`; adapt those paths outside the original workspace.

`BattleSingleVerify` exercises a one-client round, scores, mode guards, missing acknowledgements and disconnects. `BattleHttpVerify` checks that LAN commands reject invalid credentials and route authenticated actions. `BattleAgentVerify` verifies the actual premain transformation of the installed BMSPlayer class. `BattlePreview` renders the UI and virtual keyboard. Fixture success does not establish two-PC gameplay, audio-device alignment, driver behavior or resource performance under load.

Addon GPL-3.0-or-later. ASM BSD-3-Clause (source and license included). ECJ 3.39.0 EPL-2.0: see compiler-license.html. Corresponding ECJ source: https://repo.maven.apache.org/maven2/org/eclipse/jdt/ecj/3.39.0/ecj-3.39.0-sources.jar
