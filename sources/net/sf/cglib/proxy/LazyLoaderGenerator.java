package net.sf.cglib.proxy;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import net.sf.cglib.core.ClassEmitter;
import net.sf.cglib.core.CodeEmitter;
import net.sf.cglib.core.Constants;
import net.sf.cglib.core.MethodInfo;
import net.sf.cglib.core.Signature;
import net.sf.cglib.core.TypeUtils;
import net.sf.cglib.proxy.CallbackGenerator;
import org.objectweb.asm.Label;
import org.objectweb.asm.Type;

/* loaded from: cglib-3.1.jar:net/sf/cglib/proxy/LazyLoaderGenerator.class */
class LazyLoaderGenerator implements CallbackGenerator {
    public static final LazyLoaderGenerator INSTANCE = new LazyLoaderGenerator();
    private static final Signature LOAD_OBJECT = TypeUtils.parseSignature("Object loadObject()");
    private static final Type LAZY_LOADER = TypeUtils.parseType("net.sf.cglib.proxy.LazyLoader");

    LazyLoaderGenerator() {
    }

    @Override // net.sf.cglib.proxy.CallbackGenerator
    public void generate(ClassEmitter ce, CallbackGenerator.Context context, List methods) {
        Set indexes = new HashSet();
        Iterator it = methods.iterator();
        while (it.hasNext()) {
            MethodInfo method = (MethodInfo) it.next();
            if (!TypeUtils.isProtected(method.getModifiers())) {
                int index = context.getIndex(method);
                indexes.add(new Integer(index));
                CodeEmitter e = context.beginMethod(ce, method);
                e.load_this();
                e.dup();
                e.invoke_virtual_this(loadMethod(index));
                e.checkcast(method.getClassInfo().getType());
                e.load_args();
                e.invoke(method);
                e.return_value();
                e.end_method();
            }
        }
        Iterator it2 = indexes.iterator();
        while (it2.hasNext()) {
            int index2 = ((Integer) it2.next()).intValue();
            String delegate = new StringBuffer().append("CGLIB$LAZY_LOADER_").append(index2).toString();
            ce.declare_field(2, delegate, Constants.TYPE_OBJECT, null);
            CodeEmitter e2 = ce.begin_method(50, loadMethod(index2), null);
            e2.load_this();
            e2.getfield(delegate);
            e2.dup();
            Label end = e2.make_label();
            e2.ifnonnull(end);
            e2.pop();
            e2.load_this();
            context.emitCallback(e2, index2);
            e2.invoke_interface(LAZY_LOADER, LOAD_OBJECT);
            e2.dup_x1();
            e2.putfield(delegate);
            e2.mark(end);
            e2.return_value();
            e2.end_method();
        }
    }

    private Signature loadMethod(int index) {
        return new Signature(new StringBuffer().append("CGLIB$LOAD_PRIVATE_").append(index).toString(), Constants.TYPE_OBJECT, Constants.TYPES_EMPTY);
    }

    @Override // net.sf.cglib.proxy.CallbackGenerator
    public void generateStatic(CodeEmitter e, CallbackGenerator.Context context, List methods) {
    }
}
