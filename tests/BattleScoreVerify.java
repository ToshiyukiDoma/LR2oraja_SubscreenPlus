package lr2touch;
import bms.player.beatoraja.*;import bms.player.beatoraja.play.*;import java.nio.file.*;
public class BattleScoreVerify {
 public static void main(String[] args)throws Exception{
  BattleClient client=GameVerify.allocate(BattleClient.class);Touch ui=GameVerify.allocate(Touch.class);MainController main=GameVerify.allocate(MainController.class);ui.attachedMain=main;GameVerify.field(client,"ui",ui);BMSPlayer player=GameVerify.allocate(BMSPlayer.class);JudgeManager judge=GameVerify.allocate(JudgeManager.class);ScoreData score=new ScoreData();score.setEpg(100);score.setEgr(30);GameVerify.field(judge,"score",score);GameVerify.field(player,"judge",judge);GameVerify.field(player,"state",BMSPlayer.STATE_PLAY);GameVerify.field(main,"current",player);
  BattleClient.Round round=new BattleClient.Round("score","a".repeat(64),"0");round.started=true;round.readySent=true;client.round=round;client.sample(round);GameVerify.check(round.ex==230&&!round.finished,"live score from installed JudgeManager");GameVerify.field(player,"state",BMSPlayer.STATE_FINISHED);client.sample(round);GameVerify.check(round.finished&&round.ex==230,"final score");score.setEpg(999);client.sample(round);GameVerify.check(round.ex==230,"final score remains frozen");
  Path root=Files.createTempDirectory("battle-paths-");Path chart=root.resolve("chart.bms");BattleClient.checkAssetPaths(chart,"#WAV01 sounds/test.wav\n#BMP01 image.png");try{BattleClient.checkAssetPaths(chart,"#WAV01 ../outside.wav");throw new AssertionError("outside media accepted");}catch(java.io.IOException expected){}Files.writeString(root.resolve("chart.bmson"),"{\"sound_channels\":[{\"name\":\"../outside.wav\"}]}");try{BattleClient.checkAssetPaths(root.resolve("chart.bmson"),"");throw new AssertionError("outside BMSON media accepted");}catch(java.io.IOException expected){}
  System.out.println("PASS: actual JudgeManager EX score sampling, final-score freeze, BMS/BMSON external-media rejection");
 }
}
