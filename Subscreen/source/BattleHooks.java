package lr2touch;
import java.lang.instrument.*;import java.security.*;import org.objectweb.asm.*;
/** Hold only the existing READY -> PLAY comparison. No busy waiting or network I/O. */
public final class BattleHooks implements ClassFileTransformer {
 public static volatile boolean installed;
 public byte[] transform(ClassLoader loader,String name,Class<?> cls,ProtectionDomain pd,byte[] bytes){if(!"bms/player/beatoraja/play/BMSPlayer".equals(name))return null;
  try{ClassReader r=new ClassReader(bytes);ClassWriter w=new ClassWriter(r,ClassWriter.COMPUTE_MAXS);int[] hits={0};r.accept(new ClassVisitor(Opcodes.ASM9,w){public MethodVisitor visitMethod(int a,String n,String d,String s,String[] ex){MethodVisitor m=super.visitMethod(a,n,d,s,ex);if(!n.equals("render")||!d.equals("()V"))return m;return new MethodVisitor(Opcodes.ASM9,m){public void visitMethodInsn(int op,String owner,String method,String desc,boolean itf){super.visitMethodInsn(op,owner,method,desc,itf);if(method.equals("getPlaystart")&&desc.equals("()I")){visitVarInsn(Opcodes.ALOAD,0);super.visitMethodInsn(Opcodes.INVOKESTATIC,"lr2touch/BattleClient","gate","(ILjava/lang/Object;)I",false);hits[0]++;}}};}},0);if(hits[0]!=1)throw new IllegalStateException("Unsupported READY comparison");installed=true;return w.toByteArray();}catch(Throwable e){System.err.println("Battle disabled: "+e);return null;}
 }
}
