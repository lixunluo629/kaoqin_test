package org.hamcrest.core;

import org.hamcrest.Factory;
import org.hamcrest.Matcher;

/* loaded from: hamcrest-core-1.3.jar:org/hamcrest/core/StringContains.class */
public class StringContains extends SubstringMatcher {
    public StringContains(String substring) {
        super(substring);
    }

    @Override // org.hamcrest.core.SubstringMatcher
    protected boolean evalSubstringOf(String s) {
        return s.indexOf(this.substring) >= 0;
    }

    @Override // org.hamcrest.core.SubstringMatcher
    protected String relationship() {
        return "containing";
    }

    @Factory
    public static Matcher<String> containsString(String substring) {
        return new StringContains(substring);
    }
}
