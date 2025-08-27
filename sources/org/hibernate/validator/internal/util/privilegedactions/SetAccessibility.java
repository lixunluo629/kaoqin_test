package org.hibernate.validator.internal.util.privilegedactions;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Member;
import java.security.PrivilegedAction;

/* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/internal/util/privilegedactions/SetAccessibility.class */
public final class SetAccessibility implements PrivilegedAction<Object> {
    private final Member member;

    public static SetAccessibility action(Member member) {
        return new SetAccessibility(member);
    }

    private SetAccessibility(Member member) {
        this.member = member;
    }

    @Override // java.security.PrivilegedAction
    public Object run() throws SecurityException {
        ((AccessibleObject) this.member).setAccessible(true);
        return this.member;
    }
}
