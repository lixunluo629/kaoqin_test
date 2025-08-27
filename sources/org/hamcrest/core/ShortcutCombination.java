package org.hamcrest.core;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

/* loaded from: hamcrest-core-1.3.jar:org/hamcrest/core/ShortcutCombination.class */
abstract class ShortcutCombination<T> extends BaseMatcher<T> {
    private final Iterable<Matcher<? super T>> matchers;

    @Override // org.hamcrest.Matcher
    public abstract boolean matches(Object obj);

    @Override // org.hamcrest.SelfDescribing
    public abstract void describeTo(Description description);

    public ShortcutCombination(Iterable<Matcher<? super T>> matchers) {
        this.matchers = matchers;
    }

    protected boolean matches(Object o, boolean shortcut) {
        for (Matcher<? super T> matcher : this.matchers) {
            if (matcher.matches(o) == shortcut) {
                return shortcut;
            }
        }
        return !shortcut;
    }

    public void describeTo(Description description, String operator) {
        description.appendList("(", SymbolConstants.SPACE_SYMBOL + operator + SymbolConstants.SPACE_SYMBOL, ")", this.matchers);
    }
}
