package org.junit.internal.matchers;

import java.lang.Throwable;
import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;

/* loaded from: junit-4.12.jar:org/junit/internal/matchers/ThrowableMessageMatcher.class */
public class ThrowableMessageMatcher<T extends Throwable> extends org.hamcrest.TypeSafeMatcher<T> {
    private final Matcher<String> matcher;

    public ThrowableMessageMatcher(Matcher<String> matcher) {
        this.matcher = matcher;
    }

    @Override // org.hamcrest.SelfDescribing
    public void describeTo(Description description) {
        description.appendText("exception with message ");
        description.appendDescriptionOf(this.matcher);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.hamcrest.TypeSafeMatcher
    public boolean matchesSafely(T item) {
        return this.matcher.matches(item.getMessage());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.hamcrest.TypeSafeMatcher
    public void describeMismatchSafely(T item, Description description) {
        description.appendText("message ");
        this.matcher.describeMismatch(item.getMessage(), description);
    }

    @Factory
    public static <T extends Throwable> Matcher<T> hasMessage(Matcher<String> matcher) {
        return new ThrowableMessageMatcher(matcher);
    }
}
