package org.springframework.core.io;

import java.io.IOException;
import java.io.InputStream;

/* loaded from: spring-core-4.3.25.RELEASE.jar:org/springframework/core/io/InputStreamResource.class */
public class InputStreamResource extends AbstractResource {
    private final InputStream inputStream;
    private final String description;
    private boolean read;

    public InputStreamResource(InputStream inputStream) {
        this(inputStream, "resource loaded through InputStream");
    }

    public InputStreamResource(InputStream inputStream, String description) {
        this.read = false;
        if (inputStream == null) {
            throw new IllegalArgumentException("InputStream must not be null");
        }
        this.inputStream = inputStream;
        this.description = description != null ? description : "";
    }

    @Override // org.springframework.core.io.AbstractResource, org.springframework.core.io.Resource
    public boolean exists() {
        return true;
    }

    @Override // org.springframework.core.io.AbstractResource, org.springframework.core.io.Resource
    public boolean isOpen() {
        return true;
    }

    @Override // org.springframework.core.io.InputStreamSource
    public InputStream getInputStream() throws IllegalStateException, IOException {
        if (this.read) {
            throw new IllegalStateException("InputStream has already been read - do not use InputStreamResource if a stream needs to be read multiple times");
        }
        this.read = true;
        return this.inputStream;
    }

    @Override // org.springframework.core.io.Resource
    public String getDescription() {
        return "InputStream resource [" + this.description + "]";
    }

    @Override // org.springframework.core.io.AbstractResource
    public boolean equals(Object obj) {
        return obj == this || ((obj instanceof InputStreamResource) && ((InputStreamResource) obj).inputStream.equals(this.inputStream));
    }

    @Override // org.springframework.core.io.AbstractResource
    public int hashCode() {
        return this.inputStream.hashCode();
    }
}
