package net.coobird.thumbnailator.tasks.io;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;

/* loaded from: thumbnailator-0.4.8.jar:net/coobird/thumbnailator/tasks/io/BufferedImageSource.class */
public class BufferedImageSource extends AbstractImageSource<BufferedImage> {
    private final BufferedImage img;

    public BufferedImageSource(BufferedImage bufferedImage) {
        if (bufferedImage == null) {
            throw new NullPointerException("Image cannot be null.");
        }
        this.img = bufferedImage;
    }

    @Override // net.coobird.thumbnailator.tasks.io.ImageSource
    public BufferedImage read() throws IOException {
        this.inputFormatName = null;
        if (this.param != null && this.param.getSourceRegion() != null) {
            Rectangle rectangleCalculate = this.param.getSourceRegion().calculate(this.img.getWidth(), this.img.getHeight());
            return (BufferedImage) finishedReading(this.img.getSubimage(rectangleCalculate.x, rectangleCalculate.y, rectangleCalculate.width, rectangleCalculate.height));
        }
        return (BufferedImage) finishedReading(this.img);
    }

    @Override // net.coobird.thumbnailator.tasks.io.ImageSource
    public BufferedImage getSource() {
        return this.img;
    }
}
