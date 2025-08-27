package org.apache.tomcat.util.codec;

@Deprecated
/* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/tomcat/util/codec/EncoderException.class */
public class EncoderException extends Exception {
    private static final long serialVersionUID = 1;

    public EncoderException() {
    }

    public EncoderException(String message) {
        super(message);
    }

    public EncoderException(String message, Throwable cause) {
        super(message, cause);
    }

    public EncoderException(Throwable cause) {
        super(cause);
    }
}
