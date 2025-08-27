package org.apache.tomcat.util.codec;

@Deprecated
/* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/tomcat/util/codec/DecoderException.class */
public class DecoderException extends Exception {
    private static final long serialVersionUID = 1;

    public DecoderException() {
    }

    public DecoderException(String message) {
        super(message);
    }

    public DecoderException(String message, Throwable cause) {
        super(message, cause);
    }

    public DecoderException(Throwable cause) {
        super(cause);
    }
}
