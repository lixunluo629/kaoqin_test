package org.apache.commons.httpclient;

/* loaded from: commons-httpclient-3.1.jar:org/apache/commons/httpclient/HttpContentTooLargeException.class */
public class HttpContentTooLargeException extends HttpException {
    private int maxlen;

    public HttpContentTooLargeException(String message, int maxlen) {
        super(message);
        this.maxlen = maxlen;
    }

    public int getMaxLength() {
        return this.maxlen;
    }
}
