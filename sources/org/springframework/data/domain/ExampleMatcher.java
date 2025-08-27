package org.springframework.data.domain;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import lombok.Generated;
import org.springframework.core.convert.converter.Converter;
import org.springframework.util.Assert;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/domain/ExampleMatcher.class */
public class ExampleMatcher {
    private final NullHandler nullHandler;
    private final StringMatcher defaultStringMatcher;
    private final PropertySpecifiers propertySpecifiers;
    private final Set<String> ignoredPaths;
    private final boolean defaultIgnoreCase;
    private final MatchMode mode;

    /* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/domain/ExampleMatcher$MatchMode.class */
    private enum MatchMode {
        ALL,
        ANY
    }

    /* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/domain/ExampleMatcher$MatcherConfigurer.class */
    public interface MatcherConfigurer<T> {
        void configureMatcher(T t);
    }

    /* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/domain/ExampleMatcher$NullHandler.class */
    public enum NullHandler {
        INCLUDE,
        IGNORE
    }

    /* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/domain/ExampleMatcher$PropertyValueTransformer.class */
    public interface PropertyValueTransformer extends Converter<Object, Object> {
    }

    /* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/domain/ExampleMatcher$StringMatcher.class */
    public enum StringMatcher {
        DEFAULT,
        EXACT,
        STARTING,
        ENDING,
        CONTAINING,
        REGEX
    }

    @Generated
    public String toString() {
        return "ExampleMatcher(nullHandler=" + getNullHandler() + ", defaultStringMatcher=" + getDefaultStringMatcher() + ", propertySpecifiers=" + getPropertySpecifiers() + ", ignoredPaths=" + getIgnoredPaths() + ", defaultIgnoreCase=" + this.defaultIgnoreCase + ", mode=" + this.mode + ")";
    }

    @Generated
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ExampleMatcher)) {
            return false;
        }
        ExampleMatcher other = (ExampleMatcher) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$nullHandler = getNullHandler();
        Object other$nullHandler = other.getNullHandler();
        if (this$nullHandler == null) {
            if (other$nullHandler != null) {
                return false;
            }
        } else if (!this$nullHandler.equals(other$nullHandler)) {
            return false;
        }
        Object this$defaultStringMatcher = getDefaultStringMatcher();
        Object other$defaultStringMatcher = other.getDefaultStringMatcher();
        if (this$defaultStringMatcher == null) {
            if (other$defaultStringMatcher != null) {
                return false;
            }
        } else if (!this$defaultStringMatcher.equals(other$defaultStringMatcher)) {
            return false;
        }
        Object this$propertySpecifiers = getPropertySpecifiers();
        Object other$propertySpecifiers = other.getPropertySpecifiers();
        if (this$propertySpecifiers == null) {
            if (other$propertySpecifiers != null) {
                return false;
            }
        } else if (!this$propertySpecifiers.equals(other$propertySpecifiers)) {
            return false;
        }
        Object this$ignoredPaths = getIgnoredPaths();
        Object other$ignoredPaths = other.getIgnoredPaths();
        if (this$ignoredPaths == null) {
            if (other$ignoredPaths != null) {
                return false;
            }
        } else if (!this$ignoredPaths.equals(other$ignoredPaths)) {
            return false;
        }
        if (this.defaultIgnoreCase != other.defaultIgnoreCase) {
            return false;
        }
        Object this$mode = this.mode;
        Object other$mode = other.mode;
        return this$mode == null ? other$mode == null : this$mode.equals(other$mode);
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof ExampleMatcher;
    }

    @Generated
    public int hashCode() {
        Object $nullHandler = getNullHandler();
        int result = (1 * 59) + ($nullHandler == null ? 43 : $nullHandler.hashCode());
        Object $defaultStringMatcher = getDefaultStringMatcher();
        int result2 = (result * 59) + ($defaultStringMatcher == null ? 43 : $defaultStringMatcher.hashCode());
        Object $propertySpecifiers = getPropertySpecifiers();
        int result3 = (result2 * 59) + ($propertySpecifiers == null ? 43 : $propertySpecifiers.hashCode());
        Object $ignoredPaths = getIgnoredPaths();
        int result4 = (((result3 * 59) + ($ignoredPaths == null ? 43 : $ignoredPaths.hashCode())) * 59) + (this.defaultIgnoreCase ? 79 : 97);
        Object $mode = this.mode;
        return (result4 * 59) + ($mode == null ? 43 : $mode.hashCode());
    }

    @Generated
    private ExampleMatcher(NullHandler nullHandler, StringMatcher defaultStringMatcher, PropertySpecifiers propertySpecifiers, Set<String> ignoredPaths, boolean defaultIgnoreCase, MatchMode mode) {
        this.nullHandler = nullHandler;
        this.defaultStringMatcher = defaultStringMatcher;
        this.propertySpecifiers = propertySpecifiers;
        this.ignoredPaths = ignoredPaths;
        this.defaultIgnoreCase = defaultIgnoreCase;
        this.mode = mode;
    }

    @Generated
    private ExampleMatcher withMode(MatchMode mode) {
        return this.mode == mode ? this : new ExampleMatcher(this.nullHandler, this.defaultStringMatcher, this.propertySpecifiers, this.ignoredPaths, this.defaultIgnoreCase, mode);
    }

    private ExampleMatcher() {
        this(NullHandler.IGNORE, StringMatcher.DEFAULT, new PropertySpecifiers(), Collections.emptySet(), false, MatchMode.ALL);
    }

    public static ExampleMatcher matching() {
        return matchingAll();
    }

    public static ExampleMatcher matchingAny() {
        return new ExampleMatcher().withMode(MatchMode.ANY);
    }

    public static ExampleMatcher matchingAll() {
        return new ExampleMatcher().withMode(MatchMode.ALL);
    }

    public ExampleMatcher withIgnorePaths(String... ignoredPaths) {
        Assert.notEmpty(ignoredPaths, "IgnoredPaths must not be empty!");
        Assert.noNullElements(ignoredPaths, "IgnoredPaths must not contain null elements!");
        Set<String> newIgnoredPaths = new LinkedHashSet<>(this.ignoredPaths);
        newIgnoredPaths.addAll(Arrays.asList(ignoredPaths));
        return new ExampleMatcher(this.nullHandler, this.defaultStringMatcher, this.propertySpecifiers, newIgnoredPaths, this.defaultIgnoreCase, this.mode);
    }

    public ExampleMatcher withStringMatcher(StringMatcher defaultStringMatcher) {
        Assert.notNull(this.ignoredPaths, "DefaultStringMatcher must not be empty!");
        return new ExampleMatcher(this.nullHandler, defaultStringMatcher, this.propertySpecifiers, this.ignoredPaths, this.defaultIgnoreCase, this.mode);
    }

    public ExampleMatcher withIgnoreCase() {
        return withIgnoreCase(true);
    }

    public ExampleMatcher withIgnoreCase(boolean defaultIgnoreCase) {
        return new ExampleMatcher(this.nullHandler, this.defaultStringMatcher, this.propertySpecifiers, this.ignoredPaths, defaultIgnoreCase, this.mode);
    }

    public ExampleMatcher withMatcher(String propertyPath, MatcherConfigurer<GenericPropertyMatcher> matcherConfigurer) {
        Assert.hasText(propertyPath, "PropertyPath must not be empty!");
        Assert.notNull(matcherConfigurer, "MatcherConfigurer must not be empty!");
        GenericPropertyMatcher genericPropertyMatcher = new GenericPropertyMatcher();
        matcherConfigurer.configureMatcher(genericPropertyMatcher);
        return withMatcher(propertyPath, genericPropertyMatcher);
    }

    public ExampleMatcher withMatcher(String propertyPath, GenericPropertyMatcher genericPropertyMatcher) {
        Assert.hasText(propertyPath, "PropertyPath must not be empty!");
        Assert.notNull(genericPropertyMatcher, "GenericPropertyMatcher must not be empty!");
        PropertySpecifiers propertySpecifiers = new PropertySpecifiers(this.propertySpecifiers);
        PropertySpecifier propertySpecifier = new PropertySpecifier(propertyPath);
        if (genericPropertyMatcher.ignoreCase != null) {
            propertySpecifier = propertySpecifier.withIgnoreCase(genericPropertyMatcher.ignoreCase.booleanValue());
        }
        if (genericPropertyMatcher.stringMatcher != null) {
            propertySpecifier = propertySpecifier.withStringMatcher(genericPropertyMatcher.stringMatcher);
        }
        if (genericPropertyMatcher.valueTransformer != null) {
            propertySpecifier = propertySpecifier.withValueTransformer(genericPropertyMatcher.valueTransformer);
        }
        propertySpecifiers.add(propertySpecifier);
        return new ExampleMatcher(this.nullHandler, this.defaultStringMatcher, propertySpecifiers, this.ignoredPaths, this.defaultIgnoreCase, this.mode);
    }

    public ExampleMatcher withTransformer(String propertyPath, PropertyValueTransformer propertyValueTransformer) {
        Assert.hasText(propertyPath, "PropertyPath must not be empty!");
        Assert.notNull(propertyValueTransformer, "PropertyValueTransformer must not be empty!");
        PropertySpecifiers propertySpecifiers = new PropertySpecifiers(this.propertySpecifiers);
        PropertySpecifier propertySpecifier = getOrCreatePropertySpecifier(propertyPath, propertySpecifiers);
        propertySpecifiers.add(propertySpecifier.withValueTransformer(propertyValueTransformer));
        return new ExampleMatcher(this.nullHandler, this.defaultStringMatcher, propertySpecifiers, this.ignoredPaths, this.defaultIgnoreCase, this.mode);
    }

    public ExampleMatcher withIgnoreCase(String... propertyPaths) {
        Assert.notEmpty(propertyPaths, "PropertyPaths must not be empty!");
        Assert.noNullElements(propertyPaths, "PropertyPaths must not contain null elements!");
        PropertySpecifiers propertySpecifiers = new PropertySpecifiers(this.propertySpecifiers);
        for (String propertyPath : propertyPaths) {
            PropertySpecifier propertySpecifier = getOrCreatePropertySpecifier(propertyPath, propertySpecifiers);
            propertySpecifiers.add(propertySpecifier.withIgnoreCase(true));
        }
        return new ExampleMatcher(this.nullHandler, this.defaultStringMatcher, propertySpecifiers, this.ignoredPaths, this.defaultIgnoreCase, this.mode);
    }

    private PropertySpecifier getOrCreatePropertySpecifier(String propertyPath, PropertySpecifiers propertySpecifiers) {
        if (propertySpecifiers.hasSpecifierForPath(propertyPath)) {
            return propertySpecifiers.getForPath(propertyPath);
        }
        return new PropertySpecifier(propertyPath);
    }

    public ExampleMatcher withIncludeNullValues() {
        return withNullHandler(NullHandler.INCLUDE);
    }

    public ExampleMatcher withIgnoreNullValues() {
        return withNullHandler(NullHandler.IGNORE);
    }

    public ExampleMatcher withNullHandler(NullHandler nullHandler) {
        Assert.notNull(nullHandler, "NullHandler must not be null!");
        return new ExampleMatcher(nullHandler, this.defaultStringMatcher, this.propertySpecifiers, this.ignoredPaths, this.defaultIgnoreCase, this.mode);
    }

    public NullHandler getNullHandler() {
        return this.nullHandler;
    }

    public StringMatcher getDefaultStringMatcher() {
        return this.defaultStringMatcher;
    }

    public boolean isIgnoreCaseEnabled() {
        return this.defaultIgnoreCase;
    }

    public boolean isIgnoredPath(String path) {
        return this.ignoredPaths.contains(path);
    }

    public Set<String> getIgnoredPaths() {
        return this.ignoredPaths;
    }

    public PropertySpecifiers getPropertySpecifiers() {
        return this.propertySpecifiers;
    }

    public boolean isAllMatching() {
        return this.mode.equals(MatchMode.ALL);
    }

    public boolean isAnyMatching() {
        return this.mode.equals(MatchMode.ANY);
    }

    /* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/domain/ExampleMatcher$GenericPropertyMatcher.class */
    public static class GenericPropertyMatcher {
        StringMatcher stringMatcher = null;
        Boolean ignoreCase = null;
        PropertyValueTransformer valueTransformer = NoOpPropertyValueTransformer.INSTANCE;

        @Generated
        public boolean equals(Object o) {
            if (o == this) {
                return true;
            }
            if (!(o instanceof GenericPropertyMatcher)) {
                return false;
            }
            GenericPropertyMatcher other = (GenericPropertyMatcher) o;
            if (!other.canEqual(this)) {
                return false;
            }
            Object this$stringMatcher = this.stringMatcher;
            Object other$stringMatcher = other.stringMatcher;
            if (this$stringMatcher == null) {
                if (other$stringMatcher != null) {
                    return false;
                }
            } else if (!this$stringMatcher.equals(other$stringMatcher)) {
                return false;
            }
            Object this$ignoreCase = this.ignoreCase;
            Object other$ignoreCase = other.ignoreCase;
            if (this$ignoreCase == null) {
                if (other$ignoreCase != null) {
                    return false;
                }
            } else if (!this$ignoreCase.equals(other$ignoreCase)) {
                return false;
            }
            Object this$valueTransformer = this.valueTransformer;
            Object other$valueTransformer = other.valueTransformer;
            return this$valueTransformer == null ? other$valueTransformer == null : this$valueTransformer.equals(other$valueTransformer);
        }

        @Generated
        protected boolean canEqual(Object other) {
            return other instanceof GenericPropertyMatcher;
        }

        @Generated
        public int hashCode() {
            Object $stringMatcher = this.stringMatcher;
            int result = (1 * 59) + ($stringMatcher == null ? 43 : $stringMatcher.hashCode());
            Object $ignoreCase = this.ignoreCase;
            int result2 = (result * 59) + ($ignoreCase == null ? 43 : $ignoreCase.hashCode());
            Object $valueTransformer = this.valueTransformer;
            return (result2 * 59) + ($valueTransformer == null ? 43 : $valueTransformer.hashCode());
        }

        public static GenericPropertyMatcher of(StringMatcher stringMatcher, boolean ignoreCase) {
            return new GenericPropertyMatcher().stringMatcher(stringMatcher).ignoreCase(ignoreCase);
        }

        public static GenericPropertyMatcher of(StringMatcher stringMatcher) {
            return new GenericPropertyMatcher().stringMatcher(stringMatcher);
        }

        public GenericPropertyMatcher ignoreCase() {
            this.ignoreCase = true;
            return this;
        }

        public GenericPropertyMatcher ignoreCase(boolean ignoreCase) {
            this.ignoreCase = Boolean.valueOf(ignoreCase);
            return this;
        }

        public GenericPropertyMatcher caseSensitive() {
            this.ignoreCase = false;
            return this;
        }

        public GenericPropertyMatcher contains() {
            this.stringMatcher = StringMatcher.CONTAINING;
            return this;
        }

        public GenericPropertyMatcher endsWith() {
            this.stringMatcher = StringMatcher.ENDING;
            return this;
        }

        public GenericPropertyMatcher startsWith() {
            this.stringMatcher = StringMatcher.STARTING;
            return this;
        }

        public GenericPropertyMatcher exact() {
            this.stringMatcher = StringMatcher.EXACT;
            return this;
        }

        public GenericPropertyMatcher storeDefaultMatching() {
            this.stringMatcher = StringMatcher.DEFAULT;
            return this;
        }

        public GenericPropertyMatcher regex() {
            this.stringMatcher = StringMatcher.REGEX;
            return this;
        }

        public GenericPropertyMatcher stringMatcher(StringMatcher stringMatcher) {
            Assert.notNull(stringMatcher, "StringMatcher must not be null!");
            this.stringMatcher = stringMatcher;
            return this;
        }

        public GenericPropertyMatcher transform(PropertyValueTransformer propertyValueTransformer) {
            Assert.notNull(propertyValueTransformer, "PropertyValueTransformer must not be null!");
            this.valueTransformer = propertyValueTransformer;
            return this;
        }
    }

    /* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/domain/ExampleMatcher$GenericPropertyMatchers.class */
    public static class GenericPropertyMatchers {
        public static GenericPropertyMatcher ignoreCase() {
            return new GenericPropertyMatcher().ignoreCase();
        }

        public static GenericPropertyMatcher caseSensitive() {
            return new GenericPropertyMatcher().caseSensitive();
        }

        public static GenericPropertyMatcher contains() {
            return new GenericPropertyMatcher().contains();
        }

        public static GenericPropertyMatcher endsWith() {
            return new GenericPropertyMatcher().endsWith();
        }

        public static GenericPropertyMatcher startsWith() {
            return new GenericPropertyMatcher().startsWith();
        }

        public static GenericPropertyMatcher exact() {
            return new GenericPropertyMatcher().exact();
        }

        public static GenericPropertyMatcher storeDefaultMatching() {
            return new GenericPropertyMatcher().storeDefaultMatching();
        }

        public static GenericPropertyMatcher regex() {
            return new GenericPropertyMatcher().regex();
        }
    }

    /* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/domain/ExampleMatcher$NoOpPropertyValueTransformer.class */
    public enum NoOpPropertyValueTransformer implements PropertyValueTransformer {
        INSTANCE;

        @Override // org.springframework.core.convert.converter.Converter
        public Object convert(Object source) {
            return source;
        }
    }

    /* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/domain/ExampleMatcher$PropertySpecifier.class */
    public static class PropertySpecifier {
        private final String path;
        private final StringMatcher stringMatcher;
        private final Boolean ignoreCase;
        private final PropertyValueTransformer valueTransformer;

        @Generated
        private PropertySpecifier(String path, StringMatcher stringMatcher, Boolean ignoreCase, PropertyValueTransformer valueTransformer) {
            this.path = path;
            this.stringMatcher = stringMatcher;
            this.ignoreCase = ignoreCase;
            this.valueTransformer = valueTransformer;
        }

        @Generated
        public boolean equals(Object o) {
            if (o == this) {
                return true;
            }
            if (!(o instanceof PropertySpecifier)) {
                return false;
            }
            PropertySpecifier other = (PropertySpecifier) o;
            if (!other.canEqual(this)) {
                return false;
            }
            Object this$path = getPath();
            Object other$path = other.getPath();
            if (this$path == null) {
                if (other$path != null) {
                    return false;
                }
            } else if (!this$path.equals(other$path)) {
                return false;
            }
            Object this$stringMatcher = getStringMatcher();
            Object other$stringMatcher = other.getStringMatcher();
            if (this$stringMatcher == null) {
                if (other$stringMatcher != null) {
                    return false;
                }
            } else if (!this$stringMatcher.equals(other$stringMatcher)) {
                return false;
            }
            Object this$ignoreCase = getIgnoreCase();
            Object other$ignoreCase = other.getIgnoreCase();
            if (this$ignoreCase == null) {
                if (other$ignoreCase != null) {
                    return false;
                }
            } else if (!this$ignoreCase.equals(other$ignoreCase)) {
                return false;
            }
            Object this$valueTransformer = this.valueTransformer;
            Object other$valueTransformer = other.valueTransformer;
            return this$valueTransformer == null ? other$valueTransformer == null : this$valueTransformer.equals(other$valueTransformer);
        }

        @Generated
        protected boolean canEqual(Object other) {
            return other instanceof PropertySpecifier;
        }

        @Generated
        public int hashCode() {
            Object $path = getPath();
            int result = (1 * 59) + ($path == null ? 43 : $path.hashCode());
            Object $stringMatcher = getStringMatcher();
            int result2 = (result * 59) + ($stringMatcher == null ? 43 : $stringMatcher.hashCode());
            Object $ignoreCase = getIgnoreCase();
            int result3 = (result2 * 59) + ($ignoreCase == null ? 43 : $ignoreCase.hashCode());
            Object $valueTransformer = this.valueTransformer;
            return (result3 * 59) + ($valueTransformer == null ? 43 : $valueTransformer.hashCode());
        }

        PropertySpecifier(String path) {
            Assert.hasText(path, "Path must not be null/empty!");
            this.path = path;
            this.stringMatcher = null;
            this.ignoreCase = null;
            this.valueTransformer = NoOpPropertyValueTransformer.INSTANCE;
        }

        public PropertySpecifier withStringMatcher(StringMatcher stringMatcher) {
            Assert.notNull(stringMatcher, "StringMatcher must not be null!");
            return new PropertySpecifier(this.path, stringMatcher, this.ignoreCase, this.valueTransformer);
        }

        public PropertySpecifier withIgnoreCase(boolean ignoreCase) {
            return new PropertySpecifier(this.path, this.stringMatcher, Boolean.valueOf(ignoreCase), this.valueTransformer);
        }

        public PropertySpecifier withValueTransformer(PropertyValueTransformer valueTransformer) {
            Assert.notNull(valueTransformer, "PropertyValueTransformer must not be null!");
            return new PropertySpecifier(this.path, this.stringMatcher, this.ignoreCase, valueTransformer);
        }

        public String getPath() {
            return this.path;
        }

        public StringMatcher getStringMatcher() {
            return this.stringMatcher;
        }

        public Boolean getIgnoreCase() {
            return this.ignoreCase;
        }

        public PropertyValueTransformer getPropertyValueTransformer() {
            return this.valueTransformer == null ? NoOpPropertyValueTransformer.INSTANCE : this.valueTransformer;
        }

        public Object transformValue(Object source) {
            return getPropertyValueTransformer().convert(source);
        }
    }

    /* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/domain/ExampleMatcher$PropertySpecifiers.class */
    public static class PropertySpecifiers {
        private final Map<String, PropertySpecifier> propertySpecifiers = new LinkedHashMap();

        @Generated
        public boolean equals(Object o) {
            if (o == this) {
                return true;
            }
            if (!(o instanceof PropertySpecifiers)) {
                return false;
            }
            PropertySpecifiers other = (PropertySpecifiers) o;
            if (!other.canEqual(this)) {
                return false;
            }
            Object this$propertySpecifiers = this.propertySpecifiers;
            Object other$propertySpecifiers = other.propertySpecifiers;
            return this$propertySpecifiers == null ? other$propertySpecifiers == null : this$propertySpecifiers.equals(other$propertySpecifiers);
        }

        @Generated
        protected boolean canEqual(Object other) {
            return other instanceof PropertySpecifiers;
        }

        @Generated
        public int hashCode() {
            Object $propertySpecifiers = this.propertySpecifiers;
            int result = (1 * 59) + ($propertySpecifiers == null ? 43 : $propertySpecifiers.hashCode());
            return result;
        }

        PropertySpecifiers() {
        }

        PropertySpecifiers(PropertySpecifiers propertySpecifiers) {
            this.propertySpecifiers.putAll(propertySpecifiers.propertySpecifiers);
        }

        public void add(PropertySpecifier specifier) {
            Assert.notNull(specifier, "PropertySpecifier must not be null!");
            this.propertySpecifiers.put(specifier.getPath(), specifier);
        }

        public boolean hasSpecifierForPath(String path) {
            return this.propertySpecifiers.containsKey(path);
        }

        public PropertySpecifier getForPath(String path) {
            return this.propertySpecifiers.get(path);
        }

        public boolean hasValues() {
            return !this.propertySpecifiers.isEmpty();
        }

        public Collection<PropertySpecifier> getSpecifiers() {
            return this.propertySpecifiers.values();
        }
    }
}
