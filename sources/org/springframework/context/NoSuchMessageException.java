package org.springframework.context;

import java.util.Locale;

/* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/context/NoSuchMessageException.class */
public class NoSuchMessageException extends RuntimeException {
    public NoSuchMessageException(String code, Locale locale) {
        super("No message found under code '" + code + "' for locale '" + locale + "'.");
    }

    public NoSuchMessageException(String code) {
        super("No message found under code '" + code + "' for locale '" + Locale.getDefault() + "'.");
    }
}
