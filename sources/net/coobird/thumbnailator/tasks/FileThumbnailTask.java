package net.coobird.thumbnailator.tasks;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import net.coobird.thumbnailator.ThumbnailParameter;
import net.coobird.thumbnailator.tasks.io.FileImageSink;
import net.coobird.thumbnailator.tasks.io.FileImageSource;

/* loaded from: thumbnailator-0.4.8.jar:net/coobird/thumbnailator/tasks/FileThumbnailTask.class */
public class FileThumbnailTask extends ThumbnailTask<File, File> {
    private final SourceSinkThumbnailTask<File, File> task;

    public FileThumbnailTask(ThumbnailParameter thumbnailParameter, File file, File file2) {
        super(thumbnailParameter);
        this.task = new SourceSinkThumbnailTask<>(thumbnailParameter, new FileImageSource(file), new FileImageSink(file2));
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
    public File getSource() {
        return this.task.getSource();
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // net.coobird.thumbnailator.tasks.ThumbnailTask
    public File getDestination() {
        return this.task.getDestination();
    }
}
