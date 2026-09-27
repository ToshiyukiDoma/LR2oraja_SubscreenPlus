package lr2touch;
import java.io.*;import java.net.*;import java.util.concurrent.*;import java.util.function.*;
/** Bounded UTF frames, never Java object deserialization. One reader/writer per peer. */
final class BattleWire implements AutoCloseable {
 final Socket socket;final ArrayBlockingQueue<String[]> outbound=new ArrayBlockingQueue<>(128);volatile boolean closed;final Consumer<String[]> receive;final Runnable lost;
 BattleWire(Socket socket,Consumer<String[]> receive,Runnable lost)throws IOException{this.socket=socket;this.receive=receive;this.lost=lost;socket.setTcpNoDelay(true);socket.setSoTimeout(6000);}
 void start(){thread("battle-read",()->{try{DataInputStream in=new DataInputStream(socket.getInputStream());while(!closed){int count=in.readUnsignedByte();if(count<1||count>16)throw new IOException("Invalid frame");String[] values=new String[count];for(int i=0;i<count;i++){values[i]=in.readUTF();if(values[i].length()>4096)throw new IOException("Field too long");}receive.accept(values);}}catch(Exception e){}finally{close();}});thread("battle-write",()->{try{DataOutputStream out=new DataOutputStream(socket.getOutputStream());while(!closed){String[] values=outbound.poll(1,TimeUnit.SECONDS);if(values==null)continue;out.writeByte(values.length);for(String value:values)out.writeUTF(value);out.flush();}}catch(Exception e){}finally{close();}});}
 boolean send(String... values){if(closed)return false;if(!outbound.offer(values)){close();return false;}return true;}
 public void close(){synchronized(this){if(closed)return;closed=true;}try{socket.close();}catch(IOException e){}outbound.clear();lost.run();}
 static void thread(String name,Runnable r){Thread t=new Thread(r,name);t.setDaemon(true);t.start();}
 static String clean(String s){if(s==null)return "";return s.replaceAll("[\\p{Cntrl}]"," ").substring(0,Math.min(200,s.replaceAll("[\\p{Cntrl}]"," ").length()));}
 static String json(String s){StringBuilder b=new StringBuilder("\"");for(char c:s.toCharArray()){if(c=='"'||c=='\\')b.append('\\').append(c);else if(c<32)b.append(' ');else b.append(c);}return b.append('"').toString();}
}
