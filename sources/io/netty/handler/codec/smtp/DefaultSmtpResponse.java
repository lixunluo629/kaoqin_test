package io.netty.handler.codec.smtp;

import java.util.Collections;
import java.util.List;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/codec/smtp/DefaultSmtpResponse.class */
public final class DefaultSmtpResponse implements SmtpResponse {
    private final int code;
    private final List<CharSequence> details;

    public DefaultSmtpResponse(int code) {
        this(code, (List<CharSequence>) null);
    }

    public DefaultSmtpResponse(int code, CharSequence... details) {
        this(code, SmtpUtils.toUnmodifiableList(details));
    }

    DefaultSmtpResponse(int code, List<CharSequence> details) {
        if (code < 100 || code > 599) {
            throw new IllegalArgumentException("code must be 100 <= code <= 599");
        }
        this.code = code;
        if (details == null) {
            this.details = Collections.emptyList();
        } else {
            this.details = Collections.unmodifiableList(details);
        }
    }

    @Override // io.netty.handler.codec.smtp.SmtpResponse
    public int code() {
        return this.code;
    }

    @Override // io.netty.handler.codec.smtp.SmtpResponse
    public List<CharSequence> details() {
        return this.details;
    }

    public int hashCode() {
        return (this.code * 31) + this.details.hashCode();
    }

    public boolean equals(Object o) {
        if (!(o instanceof DefaultSmtpResponse)) {
            return false;
        }
        if (o == this) {
            return true;
        }
        DefaultSmtpResponse other = (DefaultSmtpResponse) o;
        return code() == other.code() && details().equals(other.details());
    }

    public String toString() {
        return "DefaultSmtpResponse{code=" + this.code + ", details=" + this.details + '}';
    }
}
