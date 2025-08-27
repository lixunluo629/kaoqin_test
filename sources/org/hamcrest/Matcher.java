package org.hamcrest;

/* loaded from: hamcrest-core-1.3.jar:org/hamcrest/Matcher.class */
public interface Matcher<T> extends SelfDescribing {
    boolean matches(Object obj);

    void describeMismatch(Object obj, Description description);

    @Deprecated
    void _dont_implement_Matcher___instead_extend_BaseMatcher_();
}
