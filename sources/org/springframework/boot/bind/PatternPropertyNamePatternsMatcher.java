package org.springframework.boot.bind;

import java.util.Collection;
import org.springframework.util.PatternMatchUtils;

/* loaded from: spring-boot-1.5.22.RELEASE.jar:org/springframework/boot/bind/PatternPropertyNamePatternsMatcher.class */
class PatternPropertyNamePatternsMatcher implements PropertyNamePatternsMatcher {
    private final String[] patterns;

    PatternPropertyNamePatternsMatcher(Collection<String> patterns) {
        this.patterns = patterns != null ? (String[]) patterns.toArray(new String[patterns.size()]) : new String[0];
    }

    @Override // org.springframework.boot.bind.PropertyNamePatternsMatcher
    public boolean matches(String propertyName) {
        return PatternMatchUtils.simpleMatch(this.patterns, propertyName);
    }
}
