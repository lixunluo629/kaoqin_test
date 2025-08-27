package org.springframework.data.repository.core.support;

import java.util.Collection;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.util.Assert;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/repository/core/support/ExampleMatcherAccessor.class */
public class ExampleMatcherAccessor {
    private final ExampleMatcher matcher;

    public ExampleMatcherAccessor(ExampleMatcher matcher) {
        Assert.notNull(matcher, "ExampleMatcher must not be null!");
        this.matcher = matcher;
    }

    public Collection<ExampleMatcher.PropertySpecifier> getPropertySpecifiers() {
        return this.matcher.getPropertySpecifiers().getSpecifiers();
    }

    public boolean hasPropertySpecifier(String path) {
        return this.matcher.getPropertySpecifiers().hasSpecifierForPath(path);
    }

    public ExampleMatcher.PropertySpecifier getPropertySpecifier(String path) {
        return this.matcher.getPropertySpecifiers().getForPath(path);
    }

    public boolean hasPropertySpecifiers() {
        return this.matcher.getPropertySpecifiers().hasValues();
    }

    public ExampleMatcher.StringMatcher getStringMatcherForPath(String path) {
        if (!hasPropertySpecifier(path)) {
            return this.matcher.getDefaultStringMatcher();
        }
        ExampleMatcher.PropertySpecifier specifier = getPropertySpecifier(path);
        return specifier.getStringMatcher() != null ? specifier.getStringMatcher() : this.matcher.getDefaultStringMatcher();
    }

    public ExampleMatcher.NullHandler getNullHandler() {
        return this.matcher.getNullHandler();
    }

    public ExampleMatcher.StringMatcher getDefaultStringMatcher() {
        return this.matcher.getDefaultStringMatcher();
    }

    public boolean isIgnoreCaseEnabled() {
        return this.matcher.isIgnoreCaseEnabled();
    }

    public boolean isIgnoredPath(String path) {
        return this.matcher.isIgnoredPath(path);
    }

    public boolean isIgnoreCaseForPath(String path) {
        if (!hasPropertySpecifier(path)) {
            return this.matcher.isIgnoreCaseEnabled();
        }
        ExampleMatcher.PropertySpecifier specifier = getPropertySpecifier(path);
        return specifier.getIgnoreCase() != null ? specifier.getIgnoreCase().booleanValue() : this.matcher.isIgnoreCaseEnabled();
    }

    public ExampleMatcher.PropertyValueTransformer getValueTransformerForPath(String path) {
        if (!hasPropertySpecifier(path)) {
            return ExampleMatcher.NoOpPropertyValueTransformer.INSTANCE;
        }
        return getPropertySpecifier(path).getPropertyValueTransformer();
    }
}
