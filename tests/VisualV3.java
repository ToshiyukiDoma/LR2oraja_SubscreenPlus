package lr2touch;
import java.nio.file.*;import java.awt.*;import java.awt.image.*;import javax.swing.*;import javax.imageio.*;
public class VisualV3 {
 static void save(Component c,Path path)throws Exception{BufferedImage im=new BufferedImage(c.getWidth(),c.getHeight(),BufferedImage.TYPE_INT_RGB);Graphics2D g=im.createGraphics();c.printAll(g);g.dispose();ImageIO.write(im,"png",path.toFile());}
 public static void main(String[] args)throws Exception{Path out=Paths.get(args[1]);Files.createDirectories(out);SwingUtilities.invokeAndWait(()->{try{Touch.base=Paths.get(args[0]);Touch.preview=true;Touch t=new Touch();t.frame.setBounds(0,0,1600,1000);t.showPage("keyboard");t.frame.validate();save(t.surface,out.resolve("keyboard-large.png"));
  GameMonitor.Snapshot s=new GameMonitor.Snapshot();s.title="HORIZON";s.artist="Sample Artist";s.genre="PROGRESSIVE TRANCE";s.difficulty="Another · Lv 12";s.table="Sample Table · ★12";s.concentration=true;s.loading=true;s.audio=.67f;s.bga=.42f;s.notesReady=true;s.phase="Preparing chart";t.receive(s);t.frame.validate();save(t.surface,out.resolve("concentration-loading.png"));s.loading=false;t.receive(s);t.frame.validate();save(t.surface,out.resolve("concentration-playing.png"));
  t.concentrating=false;t.searchPanel.setVisible(true);t.showPage("gameplay");t.gameSettings.open("speed");t.frame.validate();save(t.surface,out.resolve("gameplay-settings.png"));
  javax.swing.Timer timer=new javax.swing.Timer(250,e->{for(Window w:Window.getWindows())if(w instanceof JDialog&&w.isVisible())try{JDialog d=(JDialog)w;save(d.getContentPane(),out.resolve("confirmation-large.png"));d.dispose();}catch(Exception ex){throw new RuntimeException(ex);}});timer.setRepeats(false);timer.start();if(TouchWidgets.confirm(t,"Restart with this profile?","Profile change will restart the game.\nYour current settings will be saved first."))throw new AssertionError("dismiss must cancel");
  t.backgrounds.close();t.frame.dispose();System.out.println("PASS: large keyboard, centered concentration, gameplay settings and confirmation layouts");
 }catch(Exception e){throw new RuntimeException(e);}});}
}
