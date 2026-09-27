package lr2touch;
import java.util.*;
/** NTP-style monotonic clock estimate; best RTT sample over a short rolling window. */
final class BattleClock {
 record Sample(long offset,long rtt,long received){}
 final ArrayDeque<Sample> samples=new ArrayDeque<>();volatile long offset,rtt=Long.MAX_VALUE,uncertainty=Long.MAX_VALUE,last;volatile int count;
 synchronized void accept(long t0,long t1,long t2,long t3){long delay=(t3-t0)-(t2-t1);if(delay<0||delay>1_000_000_000L||t2<t1)return;samples.add(new Sample(((t1-t0)+(t2-t3))/2,delay,t3));while(samples.size()>20)samples.remove();Sample best=samples.stream().min(Comparator.comparingLong(Sample::rtt)).orElseThrow();offset=best.offset;rtt=best.rtt;long spread=0;for(Sample s:samples)if(s.rtt<=best.rtt+2_000_000)spread=Math.max(spread,Math.abs(s.offset-best.offset));uncertainty=best.rtt/2+spread;last=t3;count=samples.size();}
 boolean good(){return count>=8&&rtt<=20_000_000&&uncertainty<=10_000_000&&System.nanoTime()-last<2_000_000_000L;}
}
