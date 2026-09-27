package lr2touch;
import java.nio.file.*;
public class BattleSingleVerify extends BattleVerify {
 public static void main(String[] args)throws Exception {
  try(BattleServer s=new BattleServer(0,0,"123456",Paths.get("work/battle-release/Subscreen/battle-overlay"));Fake p=new Fake(s,"Solo","123456")) {
   waitFor(()->p.slot==1,3000);p.offer();waitFor(()->!s.peers[0].offer.isEmpty(),2000);
   check(!s.select(0),"default mode requires two clients");check(s.setSinglePlayer(true),"enable single test");
   try(Fake extra=new Fake(s,"Extra","123456")){waitFor(()->extra.wire.closed,3000);check(extra.slot==0,"test mode accepts only one");}
   waitFor(()->s.clockGood(s.peers[0]),5000);check(s.select(0),"select solo chart");waitFor(()->s.phase.equals("READY"),3000);
   check(!s.setSinglePlayer(false),"mode locked during round");check(!s.startRound(),"explicit clock check required");check(s.checkSync()&&s.startRound(),"start solo");waitFor(()->s.phase.equals("PLAYING"),7000);
   p.wire.send("SCORE",p.id,"432",""+p.target,"FINISHED");waitFor(()->s.phase.equals("FINISHED"),2000);check(s.json().contains("\"singlePlayer\":true")&&s.json().contains("\"ex\":432"),"solo OBS state");
   p.skipArm=true;check(s.select(0),"retry");waitFor(()->s.phase.equals("READY"),2000);check(s.checkSync()&&s.startRound(),"missing ack test");waitFor(()->s.phase.equals("CANCELLED"),6500);
   check(s.select(0),"disconnect test");waitFor(()->s.phase.equals("READY"),2000);p.close();waitFor(()->s.phase.equals("CANCELLED"),2000);check(s.setSinglePlayer(false),"restore battle mode");check(!s.select(0),"no clients cannot start");
  }
  System.out.println("PASS: single-client full round, score/OBS, mode guards, missing ACK and disconnect; default still requires two clients");
 }
}
