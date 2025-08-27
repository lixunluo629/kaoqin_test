package net.sf.cglib.transform.impl;

import java.lang.reflect.Method;
import net.sf.cglib.core.CodeEmitter;
import net.sf.cglib.core.Constants;
import net.sf.cglib.core.MethodInfo;
import net.sf.cglib.core.ReflectUtils;
import net.sf.cglib.core.Signature;
import net.sf.cglib.transform.ClassEmitterTransformer;
import org.objectweb.asm.Type;

/* loaded from: cglib-3.1.jar:net/sf/cglib/transform/impl/AddInitTransformer.class */
public class AddInitTransformer extends ClassEmitterTransformer {
    private MethodInfo info;

    public AddInitTransformer(Method method) {
        this.info = ReflectUtils.getMethodInfo(method);
        Type[] types = this.info.getSignature().getArgumentTypes();
        if (types.length != 1 || !types[0].equals(Constants.TYPE_OBJECT) || !this.info.getSignature().getReturnType().equals(Type.VOID_TYPE)) {
            throw new IllegalArgumentException(new StringBuffer().append(method).append(" illegal signature").toString());
        }
    }

    @Override // net.sf.cglib.core.ClassEmitter
    public CodeEmitter begin_method(int access, Signature sig, Type[] exceptions) {
        CodeEmitter emitter = super.begin_method(access, sig, exceptions);
        if (sig.getName().equals("<init>")) {
            return new CodeEmitter(this, emitter) { // from class: net.sf.cglib.transform.impl.AddInitTransformer.1
                private final AddInitTransformer this$0;

                {
                    this.this$0 = this;
                }

                @Override // org.objectweb.asm.MethodVisitor
                public void visitInsn(int opcode) {
                    if (opcode == 177) {
                        load_this();
                        invoke(this.this$0.info);
                    }
                    super.visitInsn(opcode);
                }
            };
        }
        return emitter;
    }
}
