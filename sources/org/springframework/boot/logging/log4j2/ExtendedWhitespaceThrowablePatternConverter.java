package org.springframework.boot.logging.log4j2;

import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.pattern.ConverterKeys;
import org.apache.logging.log4j.core.pattern.ExtendedThrowablePatternConverter;
import org.apache.logging.log4j.core.pattern.ThrowablePatternConverter;

@ConverterKeys({"xwEx", "xwThrowable", "xwException"})
@Plugin(name = "ExtendedWhitespaceThrowablePatternConverter", category = "Converter")
/* loaded from: spring-boot-1.5.22.RELEASE.jar:org/springframework/boot/logging/log4j2/ExtendedWhitespaceThrowablePatternConverter.class */
public final class ExtendedWhitespaceThrowablePatternConverter extends ThrowablePatternConverter {
    private final ExtendedThrowablePatternConverter delegate;

    private ExtendedWhitespaceThrowablePatternConverter(String[] options) {
        super("WhitespaceExtendedThrowable", "throwable", options);
        this.delegate = ExtendedThrowablePatternConverter.newInstance(options);
    }

    public void format(LogEvent event, StringBuilder buffer) {
        if (event.getThrown() != null) {
            buffer.append(this.options.getSeparator());
            this.delegate.format(event, buffer);
            buffer.append(this.options.getSeparator());
        }
    }

    public static ExtendedWhitespaceThrowablePatternConverter newInstance(String[] options) {
        return new ExtendedWhitespaceThrowablePatternConverter(options);
    }
}
