package net.sf.cglib.proxy;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import net.sf.cglib.core.Signature;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;

/* loaded from: cglib-3.1.jar:net/sf/cglib/proxy/BridgeMethodResolver.class */
class BridgeMethodResolver {
    private final Map declToBridge;

    public BridgeMethodResolver(Map declToBridge) {
        this.declToBridge = declToBridge;
    }

    public Map resolveAll() {
        Map resolved = new HashMap();
        for (Map.Entry entry : this.declToBridge.entrySet()) {
            Class owner = (Class) entry.getKey();
            Set bridges = (Set) entry.getValue();
            try {
                new ClassReader(owner.getName()).accept(new BridgedFinder(bridges, resolved), 6);
            } catch (IOException e) {
            }
        }
        return resolved;
    }

    /* loaded from: cglib-3.1.jar:net/sf/cglib/proxy/BridgeMethodResolver$BridgedFinder.class */
    private static class BridgedFinder extends ClassVisitor {
        private Map resolved;
        private Set eligableMethods;
        private Signature currentMethod;

        BridgedFinder(Set eligableMethods, Map resolved) {
            super(262144);
            this.currentMethod = null;
            this.resolved = resolved;
            this.eligableMethods = eligableMethods;
        }

        @Override // org.objectweb.asm.ClassVisitor
        public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        }

        @Override // org.objectweb.asm.ClassVisitor
        public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
            Signature sig = new Signature(name, desc);
            if (this.eligableMethods.remove(sig)) {
                this.currentMethod = sig;
                return new MethodVisitor(this, 262144) { // from class: net.sf.cglib.proxy.BridgeMethodResolver.BridgedFinder.1
                    private final BridgedFinder this$0;

                    {
                        this.this$0 = this;
                    }

                    @Override // org.objectweb.asm.MethodVisitor
                    public void visitMethodInsn(int opcode, String owner, String name2, String desc2) {
                        if (opcode == 183 && this.this$0.currentMethod != null) {
                            Signature target = new Signature(name2, desc2);
                            if (!target.equals(this.this$0.currentMethod)) {
                                this.this$0.resolved.put(this.this$0.currentMethod, target);
                            }
                            this.this$0.currentMethod = null;
                        }
                    }
                };
            }
            return null;
        }
    }
}
