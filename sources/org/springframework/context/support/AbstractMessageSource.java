package org.springframework.context.support;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import org.springframework.context.HierarchicalMessageSource;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.NoSuchMessageException;
import org.springframework.util.ObjectUtils;

/* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/context/support/AbstractMessageSource.class */
public abstract class AbstractMessageSource extends MessageSourceSupport implements HierarchicalMessageSource {
    private MessageSource parentMessageSource;
    private Properties commonMessages;
    private boolean useCodeAsDefaultMessage = false;

    protected abstract MessageFormat resolveCode(String str, Locale locale);

    @Override // org.springframework.context.HierarchicalMessageSource
    public void setParentMessageSource(MessageSource parent) {
        this.parentMessageSource = parent;
    }

    @Override // org.springframework.context.HierarchicalMessageSource
    public MessageSource getParentMessageSource() {
        return this.parentMessageSource;
    }

    public void setCommonMessages(Properties commonMessages) {
        this.commonMessages = commonMessages;
    }

    protected Properties getCommonMessages() {
        return this.commonMessages;
    }

    public void setUseCodeAsDefaultMessage(boolean useCodeAsDefaultMessage) {
        this.useCodeAsDefaultMessage = useCodeAsDefaultMessage;
    }

    protected boolean isUseCodeAsDefaultMessage() {
        return this.useCodeAsDefaultMessage;
    }

    @Override // org.springframework.context.MessageSource
    public final String getMessage(String code, Object[] args, String defaultMessage, Locale locale) {
        String fallback;
        String msg = getMessageInternal(code, args, locale);
        if (msg != null) {
            return msg;
        }
        if (defaultMessage == null && (fallback = getDefaultMessage(code)) != null) {
            return fallback;
        }
        return renderDefaultMessage(defaultMessage, args, locale);
    }

    @Override // org.springframework.context.MessageSource
    public final String getMessage(String code, Object[] args, Locale locale) throws NoSuchMessageException {
        String msg = getMessageInternal(code, args, locale);
        if (msg != null) {
            return msg;
        }
        String fallback = getDefaultMessage(code);
        if (fallback != null) {
            return fallback;
        }
        throw new NoSuchMessageException(code, locale);
    }

    @Override // org.springframework.context.MessageSource
    public final String getMessage(MessageSourceResolvable resolvable, Locale locale) throws NoSuchMessageException {
        String[] codes = resolvable.getCodes();
        if (codes != null) {
            for (String code : codes) {
                String message = getMessageInternal(code, resolvable.getArguments(), locale);
                if (message != null) {
                    return message;
                }
            }
        }
        String defaultMessage = getDefaultMessage(resolvable, locale);
        if (defaultMessage != null) {
            return defaultMessage;
        }
        throw new NoSuchMessageException(!ObjectUtils.isEmpty((Object[]) codes) ? codes[codes.length - 1] : null, locale);
    }

    protected String getMessageInternal(String code, Object[] args, Locale locale) {
        String str;
        String commonMessage;
        if (code == null) {
            return null;
        }
        if (locale == null) {
            locale = Locale.getDefault();
        }
        Object[] argsToUse = args;
        if (!isAlwaysUseMessageFormat() && ObjectUtils.isEmpty(args)) {
            String message = resolveCodeWithoutArguments(code, locale);
            if (message != null) {
                return message;
            }
        } else {
            argsToUse = resolveArguments(args, locale);
            MessageFormat messageFormat = resolveCode(code, locale);
            if (messageFormat != null) {
                synchronized (messageFormat) {
                    str = messageFormat.format(argsToUse);
                }
                return str;
            }
        }
        Properties commonMessages = getCommonMessages();
        if (commonMessages != null && (commonMessage = commonMessages.getProperty(code)) != null) {
            return formatMessage(commonMessage, args, locale);
        }
        return getMessageFromParent(code, argsToUse, locale);
    }

    protected String getMessageFromParent(String code, Object[] args, Locale locale) {
        MessageSource parent = getParentMessageSource();
        if (parent != null) {
            if (parent instanceof AbstractMessageSource) {
                return ((AbstractMessageSource) parent).getMessageInternal(code, args, locale);
            }
            return parent.getMessage(code, args, null, locale);
        }
        return null;
    }

    protected String getDefaultMessage(MessageSourceResolvable resolvable, Locale locale) {
        String defaultMessage = resolvable.getDefaultMessage();
        String[] codes = resolvable.getCodes();
        if (defaultMessage != null) {
            if (!ObjectUtils.isEmpty((Object[]) codes) && defaultMessage.equals(codes[0])) {
                return defaultMessage;
            }
            return renderDefaultMessage(defaultMessage, resolvable.getArguments(), locale);
        }
        if (ObjectUtils.isEmpty((Object[]) codes)) {
            return null;
        }
        return getDefaultMessage(codes[0]);
    }

    protected String getDefaultMessage(String code) {
        if (isUseCodeAsDefaultMessage()) {
            return code;
        }
        return null;
    }

    @Override // org.springframework.context.support.MessageSourceSupport
    protected Object[] resolveArguments(Object[] args, Locale locale) {
        if (args == null) {
            return new Object[0];
        }
        List<Object> resolvedArgs = new ArrayList<>(args.length);
        for (Object arg : args) {
            if (arg instanceof MessageSourceResolvable) {
                resolvedArgs.add(getMessage((MessageSourceResolvable) arg, locale));
            } else {
                resolvedArgs.add(arg);
            }
        }
        return resolvedArgs.toArray();
    }

    protected String resolveCodeWithoutArguments(String code, Locale locale) {
        String str;
        MessageFormat messageFormat = resolveCode(code, locale);
        if (messageFormat != null) {
            synchronized (messageFormat) {
                str = messageFormat.format(new Object[0]);
            }
            return str;
        }
        return null;
    }
}
