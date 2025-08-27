package net.sf.cglib.proxy;

import java.lang.reflect.Method;
import net.sf.cglib.core.ReflectUtils;
import org.objectweb.asm.ClassVisitor;

/* loaded from: cglib-3.1.jar:net/sf/cglib/proxy/MixinBeanEmitter.class */
class MixinBeanEmitter extends MixinEmitter {
    public MixinBeanEmitter(ClassVisitor v, String className, Class[] classes) {
        super(v, className, classes, null);
    }

    @Override // net.sf.cglib.proxy.MixinEmitter
    protected Class[] getInterfaces(Class[] classes) {
        return null;
    }

    @Override // net.sf.cglib.proxy.MixinEmitter
    protected Method[] getMethods(Class type) {
        return ReflectUtils.getPropertyMethods(ReflectUtils.getBeanProperties(type), true, true);
    }
}
