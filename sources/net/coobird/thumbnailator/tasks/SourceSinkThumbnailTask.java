package net.coobird.thumbnailator.tasks;

import java.awt.image.BufferedImage;
import java.io.IOException;
import net.coobird.thumbnailator.ThumbnailParameter;
import net.coobird.thumbnailator.tasks.io.ImageSink;
import net.coobird.thumbnailator.tasks.io.ImageSource;

/* loaded from: thumbnailator-0.4.8.jar:net/coobird/thumbnailator/tasks/SourceSinkThumbnailTask.class */
public class SourceSinkThumbnailTask<S, D> extends ThumbnailTask<S, D> {
    private final ImageSource<S> source;
    private final ImageSink<D> destination;

    public SourceSinkThumbnailTask(ThumbnailParameter thumbnailParameter, ImageSource<S> imageSource, ImageSink<D> imageSink) {
        super(thumbnailParameter);
        if (imageSource == null) {
            throw new NullPointerException("ImageSource cannot be null.");
        }
        if (imageSink == null) {
            throw new NullPointerException("ImageSink cannot be null.");
        }
        imageSource.setThumbnailParameter(thumbnailParameter);
        this.source = imageSource;
        imageSink.setThumbnailParameter(thumbnailParameter);
        this.destination = imageSink;
    }

    @Override // net.coobird.thumbnailator.tasks.ThumbnailTask
    public BufferedImage read() throws IOException {
        BufferedImage bufferedImage = this.source.read();
        this.inputFormatName = this.source.getInputFormatName();
        return bufferedImage;
    }

    @Override // net.coobird.thumbnailator.tasks.ThumbnailTask
    public void write(BufferedImage bufferedImage) throws IOException {
        String str;
        String outputFormat = this.param.getOutputFormat();
        if (ThumbnailParameter.DETERMINE_FORMAT.equals(outputFormat)) {
            outputFormat = this.destination.preferredOutputFormatName();
        }
        if (outputFormat == ThumbnailParameter.ORIGINAL_FORMAT) {
            str = this.inputFormatName;
        } else {
            str = outputFormat;
        }
        this.destination.setOutputFormatName(str);
        this.destination.write(bufferedImage);
    }

    @Override // net.coobird.thumbnailator.tasks.ThumbnailTask
    public S getSource() {
        return this.source.getSource();
    }

    @Override // net.coobird.thumbnailator.tasks.ThumbnailTask
    public D getDestination() {
        return this.destination.getSink();
    }
}
