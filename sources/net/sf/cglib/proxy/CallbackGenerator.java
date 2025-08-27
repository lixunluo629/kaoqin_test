package net.sf.cglib.proxy;

import java.util.List;
import net.sf.cglib.core.ClassEmitter;
import net.sf.cglib.core.CodeEmitter;
import net.sf.cglib.core.MethodInfo;
import net.sf.cglib.core.Signature;

/* loaded from: cglib-3.1.jar:net/sf/cglib/proxy/CallbackGenerator.class */
interface CallbackGenerator {

    /* loaded from: cglib-3.1.jar:net/sf/cglib/proxy/CallbackGenerator$Context.class */
    public interface Context {
        ClassLoader getClassLoader();

        CodeEmitter beginMethod(ClassEmitter classEmitter, MethodInfo methodInfo);

        int getOriginalModifiers(MethodInfo methodInfo);

        int getIndex(MethodInfo methodInfo);

        void emitCallback(CodeEmitter codeEmitter, int i);

        Signature getImplSignature(MethodInfo methodInfo);

        void emitInvoke(CodeEmitter codeEmitter, MethodInfo methodInfo);
    }

    void generate(ClassEmitter classEmitter, Context context, List list) throws Exception;

    void generateStatic(CodeEmitter codeEmitter, Context context, List list) throws Exception;
}
