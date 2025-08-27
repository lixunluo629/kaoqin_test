package org.springframework.context.support;

import java.util.Locale;
import org.springframework.context.HierarchicalMessageSource;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.NoSuchMessageException;

/* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/context/support/DelegatingMessageSource.class */
public class DelegatingMessageSource extends MessageSourceSupport implements HierarchicalMessageSource {
    private MessageSource parentMessageSource;

    @Override // org.springframework.context.HierarchicalMessageSource
    public void setParentMessageSource(MessageSource parent) {
        this.parentMessageSource = parent;
    }

    @Override // org.springframework.context.HierarchicalMessageSource
    public MessageSource getParentMessageSource() {
        return this.parentMessageSource;
    }

    @Override // org.springframework.context.MessageSource
    public String getMessage(String code, Object[] args, String defaultMessage, Locale locale) {
        if (this.parentMessageSource != null) {
            return this.parentMessageSource.getMessage(code, args, defaultMessage, locale);
        }
        return renderDefaultMessage(defaultMessage, args, locale);
    }

    @Override // org.springframework.context.MessageSource
    public String getMessage(String code, Object[] args, Locale locale) throws NoSuchMessageException {
        if (this.parentMessageSource != null) {
            return this.parentMessageSource.getMessage(code, args, locale);
        }
        throw new NoSuchMessageException(code, locale);
    }

    @Override // org.springframework.context.MessageSource
    public String getMessage(MessageSourceResolvable resolvable, Locale locale) throws NoSuchMessageException {
        if (this.parentMessageSource != null) {
            return this.parentMessageSource.getMessage(resolvable, locale);
        }
        if (resolvable.getDefaultMessage() != null) {
            return renderDefaultMessage(resolvable.getDefaultMessage(), resolvable.getArguments(), locale);
        }
        String[] codes = resolvable.getCodes();
        String code = (codes == null || codes.length <= 0) ? null : codes[0];
        throw new NoSuchMessageException(code, locale);
    }
}
