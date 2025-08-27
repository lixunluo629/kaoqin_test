package net.sf.cglib.core;

import java.lang.reflect.Member;

/* loaded from: cglib-3.1.jar:net/sf/cglib/core/RejectModifierPredicate.class */
public class RejectModifierPredicate implements Predicate {
    private int rejectMask;

    public RejectModifierPredicate(int rejectMask) {
        this.rejectMask = rejectMask;
    }

    @Override // net.sf.cglib.core.Predicate
    public boolean evaluate(Object arg) {
        return (((Member) arg).getModifiers() & this.rejectMask) == 0;
    }
}
