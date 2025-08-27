package org.springframework.context.support;

import java.util.Locale;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;

/* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/context/support/MessageSourceAccessor.class */
public class MessageSourceAccessor {
    private final MessageSource messageSource;
    private final Locale defaultLocale;

    public MessageSourceAccessor(MessageSource messageSource) {
        this.messageSource = messageSource;
        this.defaultLocale = null;
    }

    public MessageSourceAccessor(MessageSource messageSource, Locale defaultLocale) {
        this.messageSource = messageSource;
        this.defaultLocale = defaultLocale;
    }

    protected Locale getDefaultLocale() {
        return this.defaultLocale != null ? this.defaultLocale : LocaleContextHolder.getLocale();
    }

    public String getMessage(String code, String defaultMessage) {
        return this.messageSource.getMessage(code, null, defaultMessage, getDefaultLocale());
    }

    public String getMessage(String code, String defaultMessage, Locale locale) {
        return this.messageSource.getMessage(code, null, defaultMessage, locale);
    }

    public String getMessage(String code, Object[] args, String defaultMessage) {
        return this.messageSource.getMessage(code, args, defaultMessage, getDefaultLocale());
    }

    public String getMessage(String code, Object[] args, String defaultMessage, Locale locale) {
        return this.messageSource.getMessage(code, args, defaultMessage, locale);
    }

    public String getMessage(String code) throws NoSuchMessageException {
        return this.messageSource.getMessage(code, null, getDefaultLocale());
    }

    public String getMessage(String code, Locale locale) throws NoSuchMessageException {
        return this.messageSource.getMessage(code, null, locale);
    }

    public String getMessage(String code, Object[] args) throws NoSuchMessageException {
        return this.messageSource.getMessage(code, args, getDefaultLocale());
    }

    public String getMessage(String code, Object[] args, Locale locale) throws NoSuchMessageException {
        return this.messageSource.getMessage(code, args, locale);
    }

    public String getMessage(MessageSourceResolvable resolvable) throws NoSuchMessageException {
        return this.messageSource.getMessage(resolvable, getDefaultLocale());
    }

    public String getMessage(MessageSourceResolvable resolvable, Locale locale) throws NoSuchMessageException {
        return this.messageSource.getMessage(resolvable, locale);
    }
}
