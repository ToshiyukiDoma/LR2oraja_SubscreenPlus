package lr2touch;
import bms.model.*;import bms.player.beatoraja.*;import bms.player.beatoraja.play.*;import bms.player.beatoraja.play.bga.*;import bms.player.beatoraja.audio.*;import bms.player.beatoraja.select.*;import bms.player.beatoraja.song.*;import java.lang.reflect.*;
public class GameVerify {
 static sun.misc.Unsafe unsafe;static{try{Field f=sun.misc.Unsafe.class.getDeclaredField("theUnsafe");f.setAccessible(true);unsafe=(sun.misc.Unsafe)f.get(null);}catch(Exception e){throw new RuntimeException(e);}}
 static <T>T allocate(Class<T> c)throws Exception{return c.cast(unsafe.allocateInstance(c));}
 static void field(Object o,String n,Object value)throws Exception{for(Class<?> c=o.getClass();c!=null;c=c.getSuperclass())try{Field f=c.getDeclaredField(n);f.setAccessible(true);f.set(o,value);return;}catch(NoSuchFieldException e){}throw new NoSuchFieldException(n);}
 static void check(boolean ok,String m){if(!ok)throw new AssertionError(m);}
 public static void main(String[] a)throws Exception{
  MainController main=allocate(MainController.class);PlayerResource r=allocate(PlayerResource.class);PlayerConfig p=new PlayerConfig();field(r,"pconfig",p);field(main,"resource",r);MusicSelector select=allocate(MusicSelector.class);field(main,"current",select);
  GameSettings.apply(main,Mode.BEAT_7K,"gauge",4);check(p.getGauge()==4,"gauge mapping");GameSettings.apply(main,Mode.BEAT_7K,"random",9);check(p.getRandom()==9,"random mapping");GameSettings.apply(main,Mode.BEAT_7K,"duration",321);check(p.getPlayConfig(Mode.BEAT_7K).getPlayconfig().getDuration()==321,"green number");GameSettings.apply(main,Mode.BEAT_7K,"offset",-17);check(p.getJudgetiming()==-17,"offset ms");GameSettings.apply(main,Mode.BEAT_7K,"autoOffset",1);check(p.isNotesDisplayTimingAutoAdjust(),"auto offset");
  BMSPlayer play=allocate(BMSPlayer.class);LaneRenderer lane=allocate(LaneRenderer.class);PlayConfig live=new PlayConfig();field(lane,"playconfig",live);field(lane,"basebpm",120d);field(play,"lanerender",lane);field(play,"state",0);field(main,"current",play);
  try{GameSettings.apply(main,Mode.BEAT_7K,"gauge",1);throw new AssertionError("gauge allowed during play");}catch(IllegalStateException expected){}
  GameSettings.apply(main,Mode.BEAT_7K,"duration",280);GameSettings.apply(main,Mode.BEAT_7K,"lane",333);check(live.getDuration()==280&&Math.abs(live.getLanecover()-.333f)<.00001,"live renderer adjustment");
  CourseData course=new CourseData();course.setConstraint(new CourseData.CourseDataConstraint[]{CourseData.CourseDataConstraint.NO_SPEED});field(r,"coursedata",course);try{GameSettings.apply(main,Mode.BEAT_7K,"lane",1);throw new AssertionError("course bypass");}catch(IllegalStateException expected){}field(r,"coursedata",null);
  SongData song=new SongData();song.setTitle("Sample track");song.setArtist("Test artist");song.setGenre("TRANCE");song.setDifficulty(4);song.setLevel(12);field(r,"songdata",song);field(r,"tablename","Test table");field(r,"tablelevel","★12");field(r,"tablefull","");field(r,"model",new BMSModel());
  AudioDriver audio=(AudioDriver)Proxy.newProxyInstance(GameVerify.class.getClassLoader(),new Class<?>[]{AudioDriver.class},(o,m,args)->m.getName().equals("getProgress")?.63f:null);field(main,"audio",audio);BGAProcessor bga=allocate(BGAProcessor.class);field(bga,"progress",.42f);BMSResource media=allocate(BMSResource.class);field(media,"bga",bga);field(r,"bmsresource",media);
  GameMonitor.Snapshot s=GameMonitor.read(main,play);check(s.loading&&s.concentration&&s.notesReady,"loading phase");check(s.audio==.63f&&s.bga==.42f,"real progress values");check(s.title.equals("Sample track")&&s.artist.equals("Test artist")&&s.difficulty.contains("Another"),"metadata");field(play,"state",4);check(!GameMonitor.read(main,play).loading,"progress hidden while playing");
  System.out.println("PASS: installed config writes, live lane controls, gameplay locks, course restrictions, chart metadata and loader progress");
 }
}
