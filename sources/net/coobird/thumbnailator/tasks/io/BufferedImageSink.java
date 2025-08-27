package net.coobird.thumbnailator.tasks.io;

import java.awt.image.BufferedImage;
import java.io.IOException;

/* loaded from: thumbnailator-0.4.8.jar:net/coobird/thumbnailator/tasks/io/BufferedImageSink.class */
public class BufferedImageSink extends AbstractImageSink<BufferedImage> {
    private BufferedImage img;
    private boolean written = false;

    @Override // net.coobird.thumbnailator.tasks.io.AbstractImageSink, net.coobird.thumbnailator.tasks.io.ImageSink
    public void write(BufferedImage bufferedImage) throws IOException {
        super.write(bufferedImage);
        this.img = bufferedImage;
        this.written = true;
    }

    @Override // net.coobird.thumbnailator.tasks.io.ImageSink
    public BufferedImage getSink() {
        if (!this.written) {
            throw new IllegalStateException("BufferedImageSink has not been written to yet.");
        }
        return this.img;
    }

    @Override // net.coobird.thumbnailator.tasks.io.AbstractImageSink, net.coobird.thumbnailator.tasks.io.ImageSink
    public void setOutputFormatName(String str) {
    }
}
