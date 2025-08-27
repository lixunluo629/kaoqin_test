package com.drew.imaging;

import com.drew.lang.CompoundException;
import com.drew.lang.annotations.Nullable;

/* loaded from: metadata-extractor-2.10.1.jar:com/drew/imaging/ImageProcessingException.class */
public class ImageProcessingException extends CompoundException {
    private static final long serialVersionUID = -9115669182209912676L;

    public ImageProcessingException(@Nullable String message) {
        super(message);
    }

    public ImageProcessingException(@Nullable String message, @Nullable Throwable cause) {
        super(message, cause);
    }

    public ImageProcessingException(@Nullable Throwable cause) {
        super(cause);
    }
}
