package org.springframework.core.convert.support;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Properties;
import org.springframework.core.convert.converter.Converter;

/* loaded from: spring-core-4.3.25.RELEASE.jar:org/springframework/core/convert/support/StringToPropertiesConverter.class */
final class StringToPropertiesConverter implements Converter<String, Properties> {
    StringToPropertiesConverter() {
    }

    @Override // org.springframework.core.convert.converter.Converter
    public Properties convert(String source) throws IOException {
        try {
            Properties props = new Properties();
            props.load(new ByteArrayInputStream(source.getBytes("ISO-8859-1")));
            return props;
        } catch (Exception ex) {
            throw new IllegalArgumentException("Failed to parse [" + source + "] into Properties", ex);
        }
    }
}
