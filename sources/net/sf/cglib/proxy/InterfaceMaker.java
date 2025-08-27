package net.sf.cglib.proxy;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import net.sf.cglib.core.AbstractClassGenerator;
import net.sf.cglib.core.ClassEmitter;
import net.sf.cglib.core.ReflectUtils;
import net.sf.cglib.core.Signature;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Type;

/* loaded from: cglib-3.1.jar:net/sf/cglib/proxy/InterfaceMaker.class */
public class InterfaceMaker extends AbstractClassGenerator {
    private static final AbstractClassGenerator.Source SOURCE;
    private Map signatures;
    static Class class$net$sf$cglib$proxy$InterfaceMaker;

    static Class class$(String x0) {
        try {
            return Class.forName(x0);
        } catch (ClassNotFoundException x1) {
            throw new NoClassDefFoundError(x1.getMessage());
        }
    }

    static {
        Class clsClass$;
        if (class$net$sf$cglib$proxy$InterfaceMaker == null) {
            clsClass$ = class$("net.sf.cglib.proxy.InterfaceMaker");
            class$net$sf$cglib$proxy$InterfaceMaker = clsClass$;
        } else {
            clsClass$ = class$net$sf$cglib$proxy$InterfaceMaker;
        }
        SOURCE = new AbstractClassGenerator.Source(clsClass$.getName());
    }

    public InterfaceMaker() {
        super(SOURCE);
        this.signatures = new HashMap();
    }

    public void add(Signature sig, Type[] exceptions) {
        this.signatures.put(sig, exceptions);
    }

    public void add(Method method) {
        add(ReflectUtils.getSignature(method), ReflectUtils.getExceptionTypes(method));
    }

    public void add(Class clazz) throws SecurityException {
        Method[] methods = clazz.getMethods();
        for (Method m : methods) {
            if (!m.getDeclaringClass().getName().equals("java.lang.Object")) {
                add(m);
            }
        }
    }

    public Class create() {
        setUseCache(false);
        return (Class) super.create(this);
    }

    @Override // net.sf.cglib.core.AbstractClassGenerator
    protected ClassLoader getDefaultClassLoader() {
        return null;
    }

    @Override // net.sf.cglib.core.AbstractClassGenerator
    protected Object firstInstance(Class type) {
        return type;
    }

    @Override // net.sf.cglib.core.AbstractClassGenerator
    protected Object nextInstance(Object instance) {
        throw new IllegalStateException("InterfaceMaker does not cache");
    }

    @Override // net.sf.cglib.core.ClassGenerator
    public void generateClass(ClassVisitor v) throws Exception {
        ClassEmitter ce = new ClassEmitter(v);
        ce.begin_class(46, 513, getClassName(), null, null, "<generated>");
        for (Signature sig : this.signatures.keySet()) {
            Type[] exceptions = (Type[]) this.signatures.get(sig);
            ce.begin_method(1025, sig, exceptions).end_method();
        }
        ce.end_class();
    }
}
