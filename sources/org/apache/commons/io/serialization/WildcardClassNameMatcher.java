package org.apache.commons.io.serialization;

import org.apache.commons.io.FilenameUtils;

/* loaded from: commons-io-2.18.0.jar:org/apache/commons/io/serialization/WildcardClassNameMatcher.class */
final class WildcardClassNameMatcher implements ClassNameMatcher {
    private final String pattern;

    public WildcardClassNameMatcher(String pattern) {
        this.pattern = pattern;
    }

    @Override // org.apache.commons.io.serialization.ClassNameMatcher
    public boolean matches(String className) {
        return FilenameUtils.wildcardMatch(className, this.pattern);
    }
}
