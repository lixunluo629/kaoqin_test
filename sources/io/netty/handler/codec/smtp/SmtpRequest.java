package io.netty.handler.codec.smtp;

import java.util.List;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/codec/smtp/SmtpRequest.class */
public interface SmtpRequest {
    SmtpCommand command();

    List<CharSequence> parameters();
}
