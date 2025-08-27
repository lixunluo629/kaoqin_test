package org.springframework.web.multipart;

/* loaded from: spring-web-4.3.25.RELEASE.jar:org/springframework/web/multipart/MaxUploadSizeExceededException.class */
public class MaxUploadSizeExceededException extends MultipartException {
    private final long maxUploadSize;

    public MaxUploadSizeExceededException(long maxUploadSize) {
        this(maxUploadSize, null);
    }

    public MaxUploadSizeExceededException(long maxUploadSize, Throwable ex) {
        super("Maximum upload size of " + maxUploadSize + " bytes exceeded", ex);
        this.maxUploadSize = maxUploadSize;
    }

    public long getMaxUploadSize() {
        return this.maxUploadSize;
    }
}
