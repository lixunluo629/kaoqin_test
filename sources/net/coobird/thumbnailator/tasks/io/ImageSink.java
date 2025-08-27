package net.coobird.thumbnailator.tasks.io;

import java.awt.image.BufferedImage;
import java.io.IOException;
import net.coobird.thumbnailator.ThumbnailParameter;

/* loaded from: thumbnailator-0.4.8.jar:net/coobird/thumbnailator/tasks/io/ImageSink.class */
public interface ImageSink<T> {
    void write(BufferedImage bufferedImage) throws IOException;

    void setOutputFormatName(String str);

    void setThumbnailParameter(ThumbnailParameter thumbnailParameter);

    String preferredOutputFormatName();

    T getSink();
}
