package org.springframework.core.convert.support;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Properties;
import org.springframework.core.convert.converter.Converter;

/* loaded from: spring-core-4.3.25.RELEASE.jar:org/springframework/core/convert/support/PropertiesToStringConverter.class */
final class PropertiesToStringConverter implements Converter<Properties, String> {
    PropertiesToStringConverter() {
    }

    @Override // org.springframework.core.convert.converter.Converter
    public String convert(Properties source) throws IOException {
        try {
            ByteArrayOutputStream os = new ByteArrayOutputStream(256);
            source.store(os, (String) null);
            return os.toString("ISO-8859-1");
        } catch (IOException ex) {
            throw new IllegalArgumentException("Failed to store [" + source + "] into String", ex);
        }
    }
}
