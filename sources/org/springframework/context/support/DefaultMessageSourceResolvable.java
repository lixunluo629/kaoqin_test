package org.springframework.context.support;

import java.io.Serializable;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

/* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/context/support/DefaultMessageSourceResolvable.class */
public class DefaultMessageSourceResolvable implements MessageSourceResolvable, Serializable {
    private final String[] codes;
    private final Object[] arguments;
    private final String defaultMessage;

    public DefaultMessageSourceResolvable(String code) {
        this(new String[]{code}, null, null);
    }

    public DefaultMessageSourceResolvable(String[] codes) {
        this(codes, null, null);
    }

    public DefaultMessageSourceResolvable(String[] codes, String defaultMessage) {
        this(codes, null, defaultMessage);
    }

    public DefaultMessageSourceResolvable(String[] codes, Object[] arguments) {
        this(codes, arguments, null);
    }

    public DefaultMessageSourceResolvable(String[] codes, Object[] arguments, String defaultMessage) {
        this.codes = codes;
        this.arguments = arguments;
        this.defaultMessage = defaultMessage;
    }

    public DefaultMessageSourceResolvable(MessageSourceResolvable resolvable) {
        this(resolvable.getCodes(), resolvable.getArguments(), resolvable.getDefaultMessage());
    }

    public String getCode() {
        if (this.codes == null || this.codes.length <= 0) {
            return null;
        }
        return this.codes[this.codes.length - 1];
    }

    @Override // org.springframework.context.MessageSourceResolvable
    public String[] getCodes() {
        return this.codes;
    }

    @Override // org.springframework.context.MessageSourceResolvable
    public Object[] getArguments() {
        return this.arguments;
    }

    @Override // org.springframework.context.MessageSourceResolvable
    public String getDefaultMessage() {
        return this.defaultMessage;
    }

    protected final String resolvableToString() {
        StringBuilder result = new StringBuilder(64);
        result.append("codes [").append(StringUtils.arrayToDelimitedString(this.codes, ","));
        result.append("]; arguments [").append(StringUtils.arrayToDelimitedString(this.arguments, ","));
        result.append("]; default message [").append(this.defaultMessage).append(']');
        return result.toString();
    }

    public String toString() {
        return getClass().getName() + ": " + resolvableToString();
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof MessageSourceResolvable)) {
            return false;
        }
        MessageSourceResolvable otherResolvable = (MessageSourceResolvable) other;
        return ObjectUtils.nullSafeEquals(getCodes(), otherResolvable.getCodes()) && ObjectUtils.nullSafeEquals(getArguments(), otherResolvable.getArguments()) && ObjectUtils.nullSafeEquals(getDefaultMessage(), otherResolvable.getDefaultMessage());
    }

    public int hashCode() {
        int hashCode = ObjectUtils.nullSafeHashCode((Object[]) getCodes());
        return (29 * ((29 * hashCode) + ObjectUtils.nullSafeHashCode(getArguments()))) + ObjectUtils.nullSafeHashCode(getDefaultMessage());
    }
}
