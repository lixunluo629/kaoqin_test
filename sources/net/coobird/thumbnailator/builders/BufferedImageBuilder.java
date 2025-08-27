package net.coobird.thumbnailator.builders;

import java.awt.Dimension;
import java.awt.image.BufferedImage;

/* loaded from: thumbnailator-0.4.8.jar:net/coobird/thumbnailator/builders/BufferedImageBuilder.class */
public final class BufferedImageBuilder {
    private static final int DEFAULT_TYPE = 2;
    private int imageType;
    private int width;
    private int height;

    public BufferedImageBuilder(Dimension dimension) {
        this(dimension.width, dimension.height);
    }

    public BufferedImageBuilder(Dimension dimension, int i) {
        this(dimension.width, dimension.height, i);
    }

    public BufferedImageBuilder(int i, int i2) {
        this(i, i2, 2);
    }

    public BufferedImageBuilder(int i, int i2, int i3) {
        size(i, i2);
        imageType(i3);
    }

    public BufferedImage build() {
        return new BufferedImage(this.width, this.height, this.imageType);
    }

    public BufferedImageBuilder imageType(int i) {
        this.imageType = i;
        return this;
    }

    public BufferedImageBuilder size(int i, int i2) {
        width(i);
        height(i2);
        return this;
    }

    public BufferedImageBuilder width(int i) {
        if (i <= 0) {
            throw new IllegalArgumentException("Width must be greater than 0.");
        }
        this.width = i;
        return this;
    }

    public BufferedImageBuilder height(int i) {
        if (i <= 0) {
            throw new IllegalArgumentException("Height must be greater than 0.");
        }
        this.height = i;
        return this;
    }
}
