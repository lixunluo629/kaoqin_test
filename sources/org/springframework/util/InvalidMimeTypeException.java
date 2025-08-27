package org.springframework.util;

/* loaded from: spring-core-4.3.25.RELEASE.jar:org/springframework/util/InvalidMimeTypeException.class */
public class InvalidMimeTypeException extends IllegalArgumentException {
    private final String mimeType;

    public InvalidMimeTypeException(String mimeType, String message) {
        super("Invalid mime type \"" + mimeType + "\": " + message);
        this.mimeType = mimeType;
    }

    public String getMimeType() {
        return this.mimeType;
    }
}
