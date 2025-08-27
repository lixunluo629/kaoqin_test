package net.coobird.thumbnailator.tasks.io;

import java.awt.image.BufferedImage;
import java.io.IOException;
import net.coobird.thumbnailator.ThumbnailParameter;

/* loaded from: thumbnailator-0.4.8.jar:net/coobird/thumbnailator/tasks/io/AbstractImageSink.class */
public abstract class AbstractImageSink<T> implements ImageSink<T> {
    protected String outputFormat;
    protected ThumbnailParameter param;

    protected AbstractImageSink() {
    }

    @Override // net.coobird.thumbnailator.tasks.io.ImageSink
    public void setOutputFormatName(String str) {
        this.outputFormat = str;
    }

    @Override // net.coobird.thumbnailator.tasks.io.ImageSink
    public void setThumbnailParameter(ThumbnailParameter thumbnailParameter) {
        this.param = thumbnailParameter;
    }

    @Override // net.coobird.thumbnailator.tasks.io.ImageSink
    public void write(BufferedImage bufferedImage) throws IOException {
        if (bufferedImage == null) {
            throw new NullPointerException("Cannot write a null image.");
        }
        if (ThumbnailParameter.DETERMINE_FORMAT.equals(this.outputFormat)) {
            this.outputFormat = preferredOutputFormatName();
        }
    }

    @Override // net.coobird.thumbnailator.tasks.io.ImageSink
    public String preferredOutputFormatName() {
        return ThumbnailParameter.ORIGINAL_FORMAT;
    }
}
