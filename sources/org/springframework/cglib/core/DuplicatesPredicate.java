package org.springframework.cglib.core;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

/* loaded from: spring-core-4.3.25.RELEASE.jar:org/springframework/cglib/core/DuplicatesPredicate.class */
public class DuplicatesPredicate implements Predicate {
    private Set unique = new HashSet();

    @Override // org.springframework.cglib.core.Predicate
    public boolean evaluate(Object arg) {
        return this.unique.add(MethodWrapper.create((Method) arg));
    }
}
