package org.apache.commons.io.serialization;

import java.util.Objects;
import java.util.regex.Pattern;

/* loaded from: commons-io-2.18.0.jar:org/apache/commons/io/serialization/RegexpClassNameMatcher.class */
final class RegexpClassNameMatcher implements ClassNameMatcher {
    private final Pattern pattern;

    public RegexpClassNameMatcher(Pattern pattern) {
        this.pattern = (Pattern) Objects.requireNonNull(pattern, "pattern");
    }

    public RegexpClassNameMatcher(String regex) {
        this(Pattern.compile(regex));
    }

    @Override // org.apache.commons.io.serialization.ClassNameMatcher
    public boolean matches(String className) {
        return this.pattern.matcher(className).matches();
    }
}
