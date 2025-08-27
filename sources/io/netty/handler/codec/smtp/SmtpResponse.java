package io.netty.handler.codec.smtp;

import java.util.List;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/codec/smtp/SmtpResponse.class */
public interface SmtpResponse {
    int code();

    List<CharSequence> details();
}
