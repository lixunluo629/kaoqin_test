package net.sf.cglib.transform;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;

/* loaded from: cglib-3.1.jar:net/sf/cglib/transform/ClassTransformerChain.class */
public class ClassTransformerChain extends AbstractClassTransformer {
    private ClassTransformer[] chain;

    public ClassTransformerChain(ClassTransformer[] chain) {
        this.chain = (ClassTransformer[]) chain.clone();
    }

    @Override // net.sf.cglib.transform.AbstractClassTransformer, net.sf.cglib.transform.ClassTransformer
    public void setTarget(ClassVisitor v) {
        super.setTarget(this.chain[0]);
        ClassVisitor next = v;
        for (int i = this.chain.length - 1; i >= 0; i--) {
            this.chain[i].setTarget(next);
            next = this.chain[i];
        }
    }

    @Override // org.objectweb.asm.ClassVisitor
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        return this.cv.visitMethod(access, name, desc, signature, exceptions);
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("ClassTransformerChain{");
        for (int i = 0; i < this.chain.length; i++) {
            if (i > 0) {
                sb.append(", ");
            }
            sb.append(this.chain[i].toString());
        }
        sb.append("}");
        return sb.toString();
    }
}
