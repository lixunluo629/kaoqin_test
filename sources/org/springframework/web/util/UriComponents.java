package org.springframework.web.util;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.util.Assert;
import org.springframework.util.MultiValueMap;

/* loaded from: spring-web-4.3.25.RELEASE.jar:org/springframework/web/util/UriComponents.class */
public abstract class UriComponents implements Serializable {
    private static final String DEFAULT_ENCODING = "UTF-8";
    private static final Pattern NAMES_PATTERN = Pattern.compile("\\{([^/]+?)\\}");
    private final String scheme;
    private final String fragment;

    /* loaded from: spring-web-4.3.25.RELEASE.jar:org/springframework/web/util/UriComponents$UriTemplateVariables.class */
    public interface UriTemplateVariables {
        public static final Object SKIP_VALUE = UriTemplateVariables.class;

        Object getValue(String str);
    }

    public abstract String getSchemeSpecificPart();

    public abstract String getUserInfo();

    public abstract String getHost();

    public abstract int getPort();

    public abstract String getPath();

    public abstract List<String> getPathSegments();

    public abstract String getQuery();

    public abstract MultiValueMap<String, String> getQueryParams();

    public abstract UriComponents encode(String str) throws UnsupportedEncodingException;

    abstract UriComponents expandInternal(UriTemplateVariables uriTemplateVariables);

    public abstract UriComponents normalize();

    public abstract String toUriString();

    public abstract URI toUri();

    protected abstract void copyToUriComponentsBuilder(UriComponentsBuilder uriComponentsBuilder);

    protected UriComponents(String scheme, String fragment) {
        this.scheme = scheme;
        this.fragment = fragment;
    }

    public final String getScheme() {
        return this.scheme;
    }

    public final String getFragment() {
        return this.fragment;
    }

    public final UriComponents encode() {
        try {
            return encode("UTF-8");
        } catch (UnsupportedEncodingException ex) {
            throw new IllegalStateException(ex);
        }
    }

    public final UriComponents expand(Map<String, ?> uriVariables) {
        Assert.notNull(uriVariables, "'uriVariables' must not be null");
        return expandInternal(new MapTemplateVariables(uriVariables));
    }

    public final UriComponents expand(Object... uriVariableValues) {
        Assert.notNull(uriVariableValues, "'uriVariableValues' must not be null");
        return expandInternal(new VarArgsTemplateVariables(uriVariableValues));
    }

    public final UriComponents expand(UriTemplateVariables uriVariables) {
        Assert.notNull(uriVariables, "'uriVariables' must not be null");
        return expandInternal(uriVariables);
    }

    public final String toString() {
        return toUriString();
    }

    static String expandUriComponent(String source, UriTemplateVariables uriVariables) {
        if (source == null) {
            return null;
        }
        if (source.indexOf(123) == -1) {
            return source;
        }
        if (source.indexOf(58) != -1) {
            source = sanitizeSource(source);
        }
        Matcher matcher = NAMES_PATTERN.matcher(source);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            String match = matcher.group(1);
            String variableName = getVariableName(match);
            Object variableValue = uriVariables.getValue(variableName);
            if (!UriTemplateVariables.SKIP_VALUE.equals(variableValue)) {
                String variableValueString = getVariableValueAsString(variableValue);
                String replacement = Matcher.quoteReplacement(variableValueString);
                matcher.appendReplacement(sb, replacement);
            }
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    private static String sanitizeSource(String source) {
        int level = 0;
        StringBuilder sb = new StringBuilder();
        for (char c : source.toCharArray()) {
            if (c == '{') {
                level++;
            }
            if (c == '}') {
                level--;
            }
            if (level <= 1 && (level != 1 || c != '}')) {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    private static String getVariableName(String match) {
        int colonIdx = match.indexOf(58);
        return colonIdx != -1 ? match.substring(0, colonIdx) : match;
    }

    private static String getVariableValueAsString(Object variableValue) {
        return variableValue != null ? variableValue.toString() : "";
    }

    /* loaded from: spring-web-4.3.25.RELEASE.jar:org/springframework/web/util/UriComponents$MapTemplateVariables.class */
    private static class MapTemplateVariables implements UriTemplateVariables {
        private final Map<String, ?> uriVariables;

        public MapTemplateVariables(Map<String, ?> uriVariables) {
            this.uriVariables = uriVariables;
        }

        @Override // org.springframework.web.util.UriComponents.UriTemplateVariables
        public Object getValue(String name) {
            if (!this.uriVariables.containsKey(name)) {
                throw new IllegalArgumentException("Map has no value for '" + name + "'");
            }
            return this.uriVariables.get(name);
        }
    }

    /* loaded from: spring-web-4.3.25.RELEASE.jar:org/springframework/web/util/UriComponents$VarArgsTemplateVariables.class */
    private static class VarArgsTemplateVariables implements UriTemplateVariables {
        private final Iterator<Object> valueIterator;

        public VarArgsTemplateVariables(Object... uriVariableValues) {
            this.valueIterator = Arrays.asList(uriVariableValues).iterator();
        }

        @Override // org.springframework.web.util.UriComponents.UriTemplateVariables
        public Object getValue(String name) {
            if (!this.valueIterator.hasNext()) {
                throw new IllegalArgumentException("Not enough variable values available to expand '" + name + "'");
            }
            return this.valueIterator.next();
        }
    }
}
