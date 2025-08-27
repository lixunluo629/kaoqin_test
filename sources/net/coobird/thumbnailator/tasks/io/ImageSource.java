package net.coobird.thumbnailator.tasks.io;

import java.awt.image.BufferedImage;
import java.io.IOException;
import net.coobird.thumbnailator.ThumbnailParameter;

/* loaded from: thumbnailator-0.4.8.jar:net/coobird/thumbnailator/tasks/io/ImageSource.class */
public interface ImageSource<T> {
    BufferedImage read() throws IOException;

    String getInputFormatName();

    void setThumbnailParameter(ThumbnailParameter thumbnailParameter);

    T getSource();
}
