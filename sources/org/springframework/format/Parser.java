package org.springframework.format;

import java.text.ParseException;
import java.util.Locale;

/* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/format/Parser.class */
public interface Parser<T> {
    T parse(String str, Locale locale) throws ParseException;
}
