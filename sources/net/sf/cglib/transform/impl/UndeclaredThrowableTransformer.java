package net.sf.cglib.transform.impl;

import java.lang.reflect.Constructor;
import net.sf.cglib.core.Block;
import net.sf.cglib.core.CodeEmitter;
import net.sf.cglib.core.Constants;
import net.sf.cglib.core.EmitUtils;
import net.sf.cglib.core.Signature;
import net.sf.cglib.core.TypeUtils;
import net.sf.cglib.transform.ClassEmitterTransformer;
import org.objectweb.asm.Type;

/* loaded from: cglib-3.1.jar:net/sf/cglib/transform/impl/UndeclaredThrowableTransformer.class */
public class UndeclaredThrowableTransformer extends ClassEmitterTransformer {
    private Type wrapper;
    static Class class$java$lang$Throwable;

    public UndeclaredThrowableTransformer(Class wrapper) throws SecurityException {
        Class clsClass$;
        this.wrapper = Type.getType((Class<?>) wrapper);
        boolean found = false;
        Constructor[] cstructs = wrapper.getConstructors();
        int i = 0;
        while (true) {
            if (i >= cstructs.length) {
                break;
            }
            Class[] types = cstructs[i].getParameterTypes();
            if (types.length == 1) {
                Class cls = types[0];
                if (class$java$lang$Throwable == null) {
                    clsClass$ = class$("java.lang.Throwable");
                    class$java$lang$Throwable = clsClass$;
                } else {
                    clsClass$ = class$java$lang$Throwable;
                }
                if (cls.equals(clsClass$)) {
                    found = true;
                    break;
                }
            }
            i++;
        }
        if (!found) {
            throw new IllegalArgumentException(new StringBuffer().append(wrapper).append(" does not have a single-arg constructor that takes a Throwable").toString());
        }
    }

    static Class class$(String x0) {
        try {
            return Class.forName(x0);
        } catch (ClassNotFoundException x1) {
            throw new NoClassDefFoundError(x1.getMessage());
        }
    }

    @Override // net.sf.cglib.core.ClassEmitter
    public CodeEmitter begin_method(int access, Signature sig, Type[] exceptions) {
        CodeEmitter e = super.begin_method(access, sig, exceptions);
        if (TypeUtils.isAbstract(access) || sig.equals(Constants.SIG_STATIC)) {
            return e;
        }
        return new CodeEmitter(this, e, exceptions) { // from class: net.sf.cglib.transform.impl.UndeclaredThrowableTransformer.1
            private Block handler = begin_block();
            private final Type[] val$exceptions;
            private final UndeclaredThrowableTransformer this$0;

            {
                this.this$0 = this;
                this.val$exceptions = exceptions;
            }

            @Override // net.sf.cglib.core.CodeEmitter, net.sf.cglib.core.LocalVariablesSorter, org.objectweb.asm.MethodVisitor
            public void visitMaxs(int maxStack, int maxLocals) {
                this.handler.end();
                EmitUtils.wrap_undeclared_throwable(this, this.handler, this.val$exceptions, this.this$0.wrapper);
                super.visitMaxs(maxStack, maxLocals);
            }
        };
    }
}
