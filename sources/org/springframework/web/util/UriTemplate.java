package org.springframework.web.util;

import java.io.Serializable;
import java.net.URI;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.util.Assert;

/* loaded from: spring-web-4.3.25.RELEASE.jar:org/springframework/web/util/UriTemplate.class */
public class UriTemplate implements Serializable {
    private final String uriTemplate;
    private final UriComponents uriComponents;
    private final List<String> variableNames;
    private final Pattern matchPattern;

    public UriTemplate(String uriTemplate) {
        Assert.hasText(uriTemplate, "'uriTemplate' must not be null");
        this.uriTemplate = uriTemplate;
        this.uriComponents = UriComponentsBuilder.fromUriString(uriTemplate).build();
        TemplateInfo info = TemplateInfo.parse(uriTemplate);
        this.variableNames = Collections.unmodifiableList(info.getVariableNames());
        this.matchPattern = info.getMatchPattern();
    }

    public List<String> getVariableNames() {
        return this.variableNames;
    }

    public URI expand(Map<String, ?> uriVariables) {
        UriComponents expandedComponents = this.uriComponents.expand(uriVariables);
        UriComponents encodedComponents = expandedComponents.encode();
        return encodedComponents.toUri();
    }

    public URI expand(Object... uriVariableValues) {
        UriComponents expandedComponents = this.uriComponents.expand(uriVariableValues);
        UriComponents encodedComponents = expandedComponents.encode();
        return encodedComponents.toUri();
    }

    public boolean matches(String uri) {
        if (uri == null) {
            return false;
        }
        Matcher matcher = this.matchPattern.matcher(uri);
        return matcher.matches();
    }

    public Map<String, String> match(String uri) {
        Assert.notNull(uri, "'uri' must not be null");
        Map<String, String> result = new LinkedHashMap<>(this.variableNames.size());
        Matcher matcher = this.matchPattern.matcher(uri);
        if (matcher.find()) {
            for (int i = 1; i <= matcher.groupCount(); i++) {
                String name = this.variableNames.get(i - 1);
                String value = matcher.group(i);
                result.put(name, value);
            }
        }
        return result;
    }

    public String toString() {
        return this.uriTemplate;
    }

    /* loaded from: spring-web-4.3.25.RELEASE.jar:org/springframework/web/util/UriTemplate$TemplateInfo.class */
    private static class TemplateInfo {
        private final List<String> variableNames;
        private final Pattern pattern;

        private TemplateInfo(List<String> vars, Pattern pattern) {
            this.variableNames = vars;
            this.pattern = pattern;
        }

        public List<String> getVariableNames() {
            return this.variableNames;
        }

        public Pattern getMatchPattern() {
            return this.pattern;
        }

        /* JADX WARN: Removed duplicated region for block: B:23:0x00f8 A[PHI: r6
  0x00f8: PHI (r6v3 'level' int) = (r6v1 'level' int), (r6v2 'level' int), (r6v5 'level' int) binds: [B:11:0x0058, B:13:0x005f, B:8:0x003b] A[DONT_GENERATE, DONT_INLINE]] */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        public static org.springframework.web.util.UriTemplate.TemplateInfo parse(java.lang.String r5) {
            /*
                Method dump skipped, instructions count: 296
                To view this dump change 'Code comments level' option to 'DEBUG'
            */
            throw new UnsupportedOperationException("Method not decompiled: org.springframework.web.util.UriTemplate.TemplateInfo.parse(java.lang.String):org.springframework.web.util.UriTemplate$TemplateInfo");
        }

        private static String quote(StringBuilder builder) {
            return builder.length() > 0 ? Pattern.quote(builder.toString()) : "";
        }
    }
}
