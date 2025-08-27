package org.springframework.data.repository.query.parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.data.mapping.PropertyPath;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/repository/query/parser/Part.class */
public class Part {
    private static final Pattern IGNORE_CASE = Pattern.compile("Ignor(ing|e)Case");
    private final PropertyPath propertyPath;
    private final Type type;
    private IgnoreCaseType ignoreCase;

    /* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/repository/query/parser/Part$IgnoreCaseType.class */
    public enum IgnoreCaseType {
        NEVER,
        ALWAYS,
        WHEN_POSSIBLE
    }

    public Part(String source, Class<?> clazz) {
        this(source, clazz, false);
    }

    public Part(String source, Class<?> clazz, boolean alwaysIgnoreCase) {
        this.ignoreCase = IgnoreCaseType.NEVER;
        Assert.hasText(source, "Part source must not be null or empty!");
        Assert.notNull(clazz, "Type must not be null!");
        String partToUse = detectAndSetIgnoreCase(source);
        if (alwaysIgnoreCase && this.ignoreCase != IgnoreCaseType.ALWAYS) {
            this.ignoreCase = IgnoreCaseType.WHEN_POSSIBLE;
        }
        this.type = Type.fromProperty(partToUse);
        this.propertyPath = PropertyPath.from(this.type.extractProperty(partToUse), clazz);
    }

    private String detectAndSetIgnoreCase(String part) {
        Matcher matcher = IGNORE_CASE.matcher(part);
        String result = part;
        if (matcher.find()) {
            this.ignoreCase = IgnoreCaseType.ALWAYS;
            result = part.substring(0, matcher.start()) + part.substring(matcher.end(), part.length());
        }
        return result;
    }

    public boolean getParameterRequired() {
        return getNumberOfArguments() > 0;
    }

    public int getNumberOfArguments() {
        return this.type.getNumberOfArguments();
    }

    public PropertyPath getProperty() {
        return this.propertyPath;
    }

    public Type getType() {
        return this.type;
    }

    public IgnoreCaseType shouldIgnoreCase() {
        return this.ignoreCase;
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || !getClass().equals(obj.getClass())) {
            return false;
        }
        Part that = (Part) obj;
        return this.propertyPath.equals(that.propertyPath) && this.type.equals(that.type);
    }

    public int hashCode() {
        int result = 37 + (17 * this.propertyPath.hashCode());
        return result + (17 * this.type.hashCode());
    }

    public String toString() {
        return String.format("%s %s", this.propertyPath.getSegment(), this.type);
    }

    /* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/repository/query/parser/Part$Type.class */
    public enum Type {
        BETWEEN(2, "IsBetween", "Between"),
        IS_NOT_NULL(0, "IsNotNull", "NotNull"),
        IS_NULL(0, "IsNull", "Null"),
        LESS_THAN("IsLessThan", "LessThan"),
        LESS_THAN_EQUAL("IsLessThanEqual", "LessThanEqual"),
        GREATER_THAN("IsGreaterThan", "GreaterThan"),
        GREATER_THAN_EQUAL("IsGreaterThanEqual", "GreaterThanEqual"),
        BEFORE("IsBefore", "Before"),
        AFTER("IsAfter", "After"),
        NOT_LIKE("IsNotLike", "NotLike"),
        LIKE("IsLike", "Like"),
        STARTING_WITH("IsStartingWith", "StartingWith", "StartsWith"),
        ENDING_WITH("IsEndingWith", "EndingWith", "EndsWith"),
        NOT_CONTAINING("IsNotContaining", "NotContaining", "NotContains"),
        CONTAINING("IsContaining", "Containing", "Contains"),
        NOT_IN("IsNotIn", "NotIn"),
        IN("IsIn", "In"),
        NEAR("IsNear", "Near"),
        WITHIN("IsWithin", "Within"),
        REGEX("MatchesRegex", "Matches", "Regex"),
        EXISTS(0, "Exists"),
        TRUE(0, "IsTrue", "True"),
        FALSE(0, "IsFalse", "False"),
        NEGATING_SIMPLE_PROPERTY("IsNot", "Not"),
        SIMPLE_PROPERTY("Is", "Equals");

        public static final Collection<String> ALL_KEYWORDS;
        private final List<String> keywords;
        private final int numberOfArguments;
        private static final List<Type> ALL = Arrays.asList(IS_NOT_NULL, IS_NULL, BETWEEN, LESS_THAN, LESS_THAN_EQUAL, GREATER_THAN, GREATER_THAN_EQUAL, BEFORE, AFTER, NOT_LIKE, LIKE, STARTING_WITH, ENDING_WITH, NOT_CONTAINING, CONTAINING, NOT_IN, IN, NEAR, WITHIN, REGEX, EXISTS, TRUE, FALSE, NEGATING_SIMPLE_PROPERTY, SIMPLE_PROPERTY);

        static {
            List<String> allKeywords = new ArrayList<>();
            for (Type type : ALL) {
                allKeywords.addAll(type.keywords);
            }
            ALL_KEYWORDS = Collections.unmodifiableList(allKeywords);
        }

        Type(int numberOfArguments, String... keywords) {
            this.numberOfArguments = numberOfArguments;
            this.keywords = Arrays.asList(keywords);
        }

        Type(String... keywords) {
            this(1, keywords);
        }

        public static Type fromProperty(String rawProperty) {
            for (Type type : ALL) {
                if (type.supports(rawProperty)) {
                    return type;
                }
            }
            return SIMPLE_PROPERTY;
        }

        public Collection<String> getKeywords() {
            return Collections.unmodifiableList(this.keywords);
        }

        protected boolean supports(String property) {
            if (this.keywords == null) {
                return true;
            }
            for (String keyword : this.keywords) {
                if (property.endsWith(keyword)) {
                    return true;
                }
            }
            return false;
        }

        public int getNumberOfArguments() {
            return this.numberOfArguments;
        }

        public String extractProperty(String part) {
            String candidate = StringUtils.uncapitalize(part);
            for (String keyword : this.keywords) {
                if (candidate.endsWith(keyword)) {
                    return candidate.substring(0, candidate.length() - keyword.length());
                }
            }
            return candidate;
        }

        @Override // java.lang.Enum
        public String toString() {
            return String.format("%s (%s): %s", name(), Integer.valueOf(getNumberOfArguments()), getKeywords());
        }
    }
}
