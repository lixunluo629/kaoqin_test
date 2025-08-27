package org.springframework.core.convert.support;

import java.util.UUID;
import org.springframework.core.convert.converter.Converter;
import org.springframework.util.StringUtils;

/* loaded from: spring-core-4.3.25.RELEASE.jar:org/springframework/core/convert/support/StringToUUIDConverter.class */
final class StringToUUIDConverter implements Converter<String, UUID> {
    StringToUUIDConverter() {
    }

    @Override // org.springframework.core.convert.converter.Converter
    public UUID convert(String source) {
        if (StringUtils.hasLength(source)) {
            return UUID.fromString(source.trim());
        }
        return null;
    }
}
