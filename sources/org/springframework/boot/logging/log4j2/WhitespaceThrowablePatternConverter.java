package org.springframework.boot.logging.log4j2;

import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.pattern.ConverterKeys;
import org.apache.logging.log4j.core.pattern.ThrowablePatternConverter;

@ConverterKeys({"wEx", "wThrowable", "wException"})
@Plugin(name = "WhitespaceThrowablePatternConverter", category = "Converter")
/* loaded from: spring-boot-1.5.22.RELEASE.jar:org/springframework/boot/logging/log4j2/WhitespaceThrowablePatternConverter.class */
public final class WhitespaceThrowablePatternConverter extends ThrowablePatternConverter {
    private WhitespaceThrowablePatternConverter(String[] options) {
        super("WhitespaceThrowable", "throwable", options);
    }

    public void format(LogEvent event, StringBuilder buffer) {
        if (event.getThrown() != null) {
            buffer.append(this.options.getSeparator());
            super.format(event, buffer);
            buffer.append(this.options.getSeparator());
        }
    }

    public static WhitespaceThrowablePatternConverter newInstance(String[] options) {
        return new WhitespaceThrowablePatternConverter(options);
    }
}
