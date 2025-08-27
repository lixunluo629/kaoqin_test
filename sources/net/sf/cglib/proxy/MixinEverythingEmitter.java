package net.sf.cglib.proxy;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.sf.cglib.core.CollectionUtils;
import net.sf.cglib.core.ReflectUtils;
import net.sf.cglib.core.RejectModifierPredicate;
import org.objectweb.asm.ClassVisitor;

/* loaded from: cglib-3.1.jar:net/sf/cglib/proxy/MixinEverythingEmitter.class */
class MixinEverythingEmitter extends MixinEmitter {
    public MixinEverythingEmitter(ClassVisitor v, String className, Class[] classes) {
        super(v, className, classes, null);
    }

    @Override // net.sf.cglib.proxy.MixinEmitter
    protected Class[] getInterfaces(Class[] classes) {
        List list = new ArrayList();
        for (Class cls : classes) {
            ReflectUtils.addAllInterfaces(cls, list);
        }
        return (Class[]) list.toArray(new Class[list.size()]);
    }

    @Override // net.sf.cglib.proxy.MixinEmitter
    protected Method[] getMethods(Class type) {
        List methods = new ArrayList(Arrays.asList(type.getMethods()));
        CollectionUtils.filter(methods, new RejectModifierPredicate(24));
        return (Method[]) methods.toArray(new Method[methods.size()]);
    }
}
