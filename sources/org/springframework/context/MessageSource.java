package org.springframework.context;

import java.util.Locale;

/* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/context/MessageSource.class */
public interface MessageSource {
    String getMessage(String str, Object[] objArr, String str2, Locale locale);

    String getMessage(String str, Object[] objArr, Locale locale) throws NoSuchMessageException;

    String getMessage(MessageSourceResolvable messageSourceResolvable, Locale locale) throws NoSuchMessageException;
}
