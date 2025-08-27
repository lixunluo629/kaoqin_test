package org.hamcrest.core;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

/* loaded from: hamcrest-core-1.3.jar:org/hamcrest/core/SubstringMatcher.class */
public abstract class SubstringMatcher extends TypeSafeMatcher<String> {
    protected final String substring;

    protected abstract boolean evalSubstringOf(String str);

    protected abstract String relationship();

    protected SubstringMatcher(String substring) {
        this.substring = substring;
    }

    @Override // org.hamcrest.TypeSafeMatcher
    public boolean matchesSafely(String item) {
        return evalSubstringOf(item);
    }

    @Override // org.hamcrest.TypeSafeMatcher
    public void describeMismatchSafely(String item, Description mismatchDescription) {
        mismatchDescription.appendText("was \"").appendText(item).appendText(SymbolConstants.QUOTES_SYMBOL);
    }

    @Override // org.hamcrest.SelfDescribing
    public void describeTo(Description description) {
        description.appendText("a string ").appendText(relationship()).appendText(SymbolConstants.SPACE_SYMBOL).appendValue(this.substring);
    }
}
