package org.springframework.format;

import java.util.Locale;

/* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/format/Printer.class */
public interface Printer<T> {
    String print(T t, Locale locale);
}
