package org.hamcrest.core;

import org.hamcrest.Description;
import org.hamcrest.DiagnosingMatcher;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;

/* loaded from: hamcrest-core-1.3.jar:org/hamcrest/core/IsInstanceOf.class */
public class IsInstanceOf extends DiagnosingMatcher<Object> {
    private final Class<?> expectedClass;
    private final Class<?> matchableClass;

    public IsInstanceOf(Class<?> expectedClass) {
        this.expectedClass = expectedClass;
        this.matchableClass = matchableClass(expectedClass);
    }

    private static Class<?> matchableClass(Class<?> expectedClass) {
        return Boolean.TYPE.equals(expectedClass) ? Boolean.class : Byte.TYPE.equals(expectedClass) ? Byte.class : Character.TYPE.equals(expectedClass) ? Character.class : Double.TYPE.equals(expectedClass) ? Double.class : Float.TYPE.equals(expectedClass) ? Float.class : Integer.TYPE.equals(expectedClass) ? Integer.class : Long.TYPE.equals(expectedClass) ? Long.class : Short.TYPE.equals(expectedClass) ? Short.class : expectedClass;
    }

    @Override // org.hamcrest.DiagnosingMatcher
    protected boolean matches(Object item, Description mismatch) {
        if (null == item) {
            mismatch.appendText("null");
            return false;
        }
        if (!this.matchableClass.isInstance(item)) {
            mismatch.appendValue(item).appendText(" is a " + item.getClass().getName());
            return false;
        }
        return true;
    }

    @Override // org.hamcrest.SelfDescribing
    public void describeTo(Description description) {
        description.appendText("an instance of ").appendText(this.expectedClass.getName());
    }

    @Factory
    public static <T> Matcher<T> instanceOf(Class<?> type) {
        return new IsInstanceOf(type);
    }

    @Factory
    public static <T> Matcher<T> any(Class<T> type) {
        return new IsInstanceOf(type);
    }
}
