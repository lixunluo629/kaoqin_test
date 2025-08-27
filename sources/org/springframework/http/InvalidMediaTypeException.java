package org.springframework.http;

import org.springframework.util.InvalidMimeTypeException;

/* loaded from: spring-web-4.3.25.RELEASE.jar:org/springframework/http/InvalidMediaTypeException.class */
public class InvalidMediaTypeException extends IllegalArgumentException {
    private String mediaType;

    public InvalidMediaTypeException(String mediaType, String message) {
        super("Invalid media type \"" + mediaType + "\": " + message);
        this.mediaType = mediaType;
    }

    InvalidMediaTypeException(InvalidMimeTypeException ex) {
        super(ex.getMessage(), ex);
        this.mediaType = ex.getMimeType();
    }

    public String getMediaType() {
        return this.mediaType;
    }
}
