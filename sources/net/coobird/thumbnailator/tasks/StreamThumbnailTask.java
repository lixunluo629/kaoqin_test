package net.coobird.thumbnailator.tasks;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import net.coobird.thumbnailator.ThumbnailParameter;
import net.coobird.thumbnailator.tasks.io.InputStreamImageSource;
import net.coobird.thumbnailator.tasks.io.OutputStreamImageSink;

/* loaded from: thumbnailator-0.4.8.jar:net/coobird/thumbnailator/tasks/StreamThumbnailTask.class */
public class StreamThumbnailTask extends ThumbnailTask<InputStream, OutputStream> {
    private final SourceSinkThumbnailTask<InputStream, OutputStream> task;

    public StreamThumbnailTask(ThumbnailParameter thumbnailParameter, InputStream inputStream, OutputStream outputStream) {
        super(thumbnailParameter);
        this.task = new SourceSinkThumbnailTask<>(thumbnailParameter, new InputStreamImageSource(inputStream), new OutputStreamImageSink(outputStream));
    }

    @Override // net.coobird.thumbnailator.tasks.ThumbnailTask
    public BufferedImage read() throws IOException {
        return this.task.read();
    }

    @Override // net.coobird.thumbnailator.tasks.ThumbnailTask
    public void write(BufferedImage bufferedImage) throws IOException {
        this.task.write(bufferedImage);
    }

    @Override // net.coobird.thumbnailator.tasks.ThumbnailTask
    public ThumbnailParameter getParam() {
        return this.task.getParam();
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // net.coobird.thumbnailator.tasks.ThumbnailTask
    public InputStream getSource() {
        return this.task.getSource();
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // net.coobird.thumbnailator.tasks.ThumbnailTask
    public OutputStream getDestination() {
        return this.task.getDestination();
    }
}
