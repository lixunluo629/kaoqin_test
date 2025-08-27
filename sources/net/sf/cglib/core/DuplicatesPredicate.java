package net.sf.cglib.core;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

/* loaded from: cglib-3.1.jar:net/sf/cglib/core/DuplicatesPredicate.class */
public class DuplicatesPredicate implements Predicate {
    private Set unique = new HashSet();

    @Override // net.sf.cglib.core.Predicate
    public boolean evaluate(Object arg) {
        return this.unique.add(MethodWrapper.create((Method) arg));
    }
}
