package net.coobird.thumbnailator.tasks.io;

import net.coobird.thumbnailator.ThumbnailParameter;

/* loaded from: thumbnailator-0.4.8.jar:net/coobird/thumbnailator/tasks/io/AbstractImageSource.class */
public abstract class AbstractImageSource<T> implements ImageSource<T> {
    protected String inputFormatName;
    protected ThumbnailParameter param;
    protected boolean hasReadInput = false;

    protected AbstractImageSource() {
    }

    protected <V> V finishedReading(V v) {
        this.hasReadInput = true;
        return v;
    }

    @Override // net.coobird.thumbnailator.tasks.io.ImageSource
    public void setThumbnailParameter(ThumbnailParameter thumbnailParameter) {
        this.param = thumbnailParameter;
    }

    @Override // net.coobird.thumbnailator.tasks.io.ImageSource
    public String getInputFormatName() {
        if (!this.hasReadInput) {
            throw new IllegalStateException("Input has not been read yet.");
        }
        return this.inputFormatName;
    }
}
