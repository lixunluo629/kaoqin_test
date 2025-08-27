package net.coobird.thumbnailator.tasks;

import java.awt.image.BufferedImage;
import java.io.IOException;
import net.coobird.thumbnailator.ThumbnailParameter;

/* loaded from: thumbnailator-0.4.8.jar:net/coobird/thumbnailator/tasks/ThumbnailTask.class */
public abstract class ThumbnailTask<S, D> {
    protected final ThumbnailParameter param;
    protected String inputFormatName;
    protected static final int FIRST_IMAGE_INDEX = 0;

    public abstract BufferedImage read() throws IOException;

    public abstract void write(BufferedImage bufferedImage) throws IOException;

    public abstract S getSource();

    public abstract D getDestination();

    protected ThumbnailTask(ThumbnailParameter thumbnailParameter) {
        if (thumbnailParameter == null) {
            throw new NullPointerException("The parameter is null.");
        }
        this.param = thumbnailParameter;
    }

    public ThumbnailParameter getParam() {
        return this.param;
    }
}
