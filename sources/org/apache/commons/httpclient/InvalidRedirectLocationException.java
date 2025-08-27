package org.apache.commons.httpclient;

/* loaded from: commons-httpclient-3.1.jar:org/apache/commons/httpclient/InvalidRedirectLocationException.class */
public class InvalidRedirectLocationException extends RedirectException {
    private final String location;

    public InvalidRedirectLocationException(String message, String location) {
        super(message);
        this.location = location;
    }

    public InvalidRedirectLocationException(String message, String location, Throwable cause) {
        super(message, cause);
        this.location = location;
    }

    public String getLocation() {
        return this.location;
    }
}
