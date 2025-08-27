package org.springframework.hateoas.mvc;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/* loaded from: spring-hateoas-0.23.0.RELEASE.jar:org/springframework/hateoas/mvc/ForwardedHeader.class */
class ForwardedHeader {
    public static String NAME = "Forwarded";
    private static final ForwardedHeader NO_HEADER = new ForwardedHeader(Collections.emptyMap());
    private final Map<String, String> elements;

    private ForwardedHeader(Map<String, String> elements) {
        this.elements = elements;
    }

    public static ForwardedHeader of(String source) {
        if (!StringUtils.hasText(source)) {
            return NO_HEADER;
        }
        Map<String, String> elements = new HashMap<>();
        for (String part : source.split(ScriptUtils.DEFAULT_STATEMENT_SEPARATOR)) {
            String[] keyValue = part.split(SymbolConstants.EQUAL_SYMBOL);
            if (keyValue.length == 2) {
                elements.put(keyValue[0].trim(), keyValue[1].trim());
            }
        }
        Assert.notNull(elements, "Forwarded elements must not be null!");
        Assert.isTrue(!elements.isEmpty(), "At least one forwarded element needs to be present!");
        return new ForwardedHeader(elements);
    }

    public String getProto() {
        return this.elements.get("proto");
    }

    public String getHost() {
        return this.elements.get("host");
    }
}
