package org.springframework.boot.bind;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.util.StringUtils;

/* loaded from: spring-boot-1.5.22.RELEASE.jar:org/springframework/boot/bind/RelaxedNames.class */
public final class RelaxedNames implements Iterable<String> {
    private static final Pattern CAMEL_CASE_PATTERN = Pattern.compile("([^A-Z-])([A-Z])");
    private static final Pattern SEPARATED_TO_CAMEL_CASE_PATTERN = Pattern.compile("[_\\-.]");
    private final String name;
    private final Set<String> values = new LinkedHashSet();

    /* loaded from: spring-boot-1.5.22.RELEASE.jar:org/springframework/boot/bind/RelaxedNames$Variation.class */
    enum Variation {
        NONE { // from class: org.springframework.boot.bind.RelaxedNames.Variation.1
            @Override // org.springframework.boot.bind.RelaxedNames.Variation
            public String apply(String value) {
                return value;
            }
        },
        LOWERCASE { // from class: org.springframework.boot.bind.RelaxedNames.Variation.2
            @Override // org.springframework.boot.bind.RelaxedNames.Variation
            public String apply(String value) {
                return value.isEmpty() ? value : value.toLowerCase(Locale.ENGLISH);
            }
        },
        UPPERCASE { // from class: org.springframework.boot.bind.RelaxedNames.Variation.3
            @Override // org.springframework.boot.bind.RelaxedNames.Variation
            public String apply(String value) {
                return value.isEmpty() ? value : value.toUpperCase(Locale.ENGLISH);
            }
        };

        public abstract String apply(String str);
    }

    public RelaxedNames(String name) {
        this.name = name != null ? name : "";
        initialize(this.name, this.values);
    }

    @Override // java.lang.Iterable
    public Iterator<String> iterator() {
        return this.values.iterator();
    }

    private void initialize(String name, Set<String> values) {
        if (values.contains(name)) {
            return;
        }
        for (Variation variation : Variation.values()) {
            for (Manipulation manipulation : Manipulation.values()) {
                String result = variation.apply(manipulation.apply(name));
                values.add(result);
                initialize(result, values);
            }
        }
    }

    /* loaded from: spring-boot-1.5.22.RELEASE.jar:org/springframework/boot/bind/RelaxedNames$Manipulation.class */
    enum Manipulation {
        NONE { // from class: org.springframework.boot.bind.RelaxedNames.Manipulation.1
            @Override // org.springframework.boot.bind.RelaxedNames.Manipulation
            public String apply(String value) {
                return value;
            }
        },
        HYPHEN_TO_UNDERSCORE { // from class: org.springframework.boot.bind.RelaxedNames.Manipulation.2
            @Override // org.springframework.boot.bind.RelaxedNames.Manipulation
            public String apply(String value) {
                return value.indexOf(45) != -1 ? value.replace('-', '_') : value;
            }
        },
        UNDERSCORE_TO_PERIOD { // from class: org.springframework.boot.bind.RelaxedNames.Manipulation.3
            @Override // org.springframework.boot.bind.RelaxedNames.Manipulation
            public String apply(String value) {
                return value.indexOf(95) != -1 ? value.replace('_', '.') : value;
            }
        },
        PERIOD_TO_UNDERSCORE { // from class: org.springframework.boot.bind.RelaxedNames.Manipulation.4
            @Override // org.springframework.boot.bind.RelaxedNames.Manipulation
            public String apply(String value) {
                return value.indexOf(46) != -1 ? value.replace('.', '_') : value;
            }
        },
        CAMELCASE_TO_UNDERSCORE { // from class: org.springframework.boot.bind.RelaxedNames.Manipulation.5
            @Override // org.springframework.boot.bind.RelaxedNames.Manipulation
            public String apply(String value) {
                if (!value.isEmpty()) {
                    Matcher matcher = RelaxedNames.CAMEL_CASE_PATTERN.matcher(value);
                    if (!matcher.find()) {
                        return value;
                    }
                    Matcher matcher2 = matcher.reset();
                    StringBuffer result = new StringBuffer();
                    while (matcher2.find()) {
                        matcher2.appendReplacement(result, matcher2.group(1) + '_' + StringUtils.uncapitalize(matcher2.group(2)));
                    }
                    matcher2.appendTail(result);
                    return result.toString();
                }
                return value;
            }
        },
        CAMELCASE_TO_HYPHEN { // from class: org.springframework.boot.bind.RelaxedNames.Manipulation.6
            @Override // org.springframework.boot.bind.RelaxedNames.Manipulation
            public String apply(String value) {
                if (!value.isEmpty()) {
                    Matcher matcher = RelaxedNames.CAMEL_CASE_PATTERN.matcher(value);
                    if (!matcher.find()) {
                        return value;
                    }
                    Matcher matcher2 = matcher.reset();
                    StringBuffer result = new StringBuffer();
                    while (matcher2.find()) {
                        matcher2.appendReplacement(result, matcher2.group(1) + '-' + StringUtils.uncapitalize(matcher2.group(2)));
                    }
                    matcher2.appendTail(result);
                    return result.toString();
                }
                return value;
            }
        },
        SEPARATED_TO_CAMELCASE { // from class: org.springframework.boot.bind.RelaxedNames.Manipulation.7
            @Override // org.springframework.boot.bind.RelaxedNames.Manipulation
            public String apply(String value) {
                return Manipulation.separatedToCamelCase(value, false);
            }
        },
        CASE_INSENSITIVE_SEPARATED_TO_CAMELCASE { // from class: org.springframework.boot.bind.RelaxedNames.Manipulation.8
            @Override // org.springframework.boot.bind.RelaxedNames.Manipulation
            public String apply(String value) {
                return Manipulation.separatedToCamelCase(value, true);
            }
        };

        private static final char[] SUFFIXES = {'_', '-', '.'};

        public abstract String apply(String str);

        /* JADX INFO: Access modifiers changed from: private */
        public static String separatedToCamelCase(String value, boolean caseInsensitive) {
            if (value.isEmpty()) {
                return value;
            }
            StringBuilder builder = new StringBuilder();
            for (String field : RelaxedNames.SEPARATED_TO_CAMEL_CASE_PATTERN.split(value)) {
                String field2 = caseInsensitive ? field.toLowerCase(Locale.ENGLISH) : field;
                builder.append(builder.length() != 0 ? StringUtils.capitalize(field2) : field2);
            }
            char lastChar = value.charAt(value.length() - 1);
            char[] cArr = SUFFIXES;
            int length = cArr.length;
            int i = 0;
            while (true) {
                if (i >= length) {
                    break;
                }
                char suffix = cArr[i];
                if (lastChar != suffix) {
                    i++;
                } else {
                    builder.append(suffix);
                    break;
                }
            }
            return builder.toString();
        }
    }

    public static RelaxedNames forCamelCase(String name) {
        StringBuilder result = new StringBuilder();
        for (char c : name.toCharArray()) {
            result.append((!Character.isUpperCase(c) || result.length() <= 0 || result.charAt(result.length() - 1) == '-') ? Character.valueOf(c) : "-" + Character.toLowerCase(c));
        }
        return new RelaxedNames(result.toString());
    }
}
