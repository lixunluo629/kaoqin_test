package com.drew.imaging.tiff;

import com.drew.imaging.ImageProcessingException;
import com.drew.lang.annotations.Nullable;

/* loaded from: metadata-extractor-2.10.1.jar:com/drew/imaging/tiff/TiffProcessingException.class */
public class TiffProcessingException extends ImageProcessingException {
    private static final long serialVersionUID = -1658134119488001891L;

    public TiffProcessingException(@Nullable String message) {
        super(message);
    }

    public TiffProcessingException(@Nullable String message, @Nullable Throwable cause) {
        super(message, cause);
    }

    public TiffProcessingException(@Nullable Throwable cause) {
        super(cause);
    }
}
