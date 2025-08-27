package net.sf.cglib.core;

import java.lang.reflect.Member;
import java.lang.reflect.Modifier;
import org.objectweb.asm.Type;

/* loaded from: cglib-3.1.jar:net/sf/cglib/core/VisibilityPredicate.class */
public class VisibilityPredicate implements Predicate {
    private boolean protectedOk;
    private String pkg;

    public VisibilityPredicate(Class source, boolean protectedOk) {
        this.protectedOk = protectedOk;
        this.pkg = TypeUtils.getPackageName(Type.getType((Class<?>) source));
    }

    @Override // net.sf.cglib.core.Predicate
    public boolean evaluate(Object arg) {
        int mod = arg instanceof Member ? ((Member) arg).getModifiers() : ((Integer) arg).intValue();
        if (Modifier.isPrivate(mod)) {
            return false;
        }
        if (Modifier.isPublic(mod)) {
            return true;
        }
        if (Modifier.isProtected(mod)) {
            return this.protectedOk;
        }
        return this.pkg.equals(TypeUtils.getPackageName(Type.getType(((Member) arg).getDeclaringClass())));
    }
}
