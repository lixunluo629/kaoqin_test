package net.coobird.thumbnailator.filters;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import net.coobird.thumbnailator.geometry.Position;

/* loaded from: thumbnailator-0.4.8.jar:net/coobird/thumbnailator/filters/Canvas.class */
public class Canvas implements ImageFilter {
    private final int width;
    private final int height;
    private final Position position;
    private final Color fillColor;
    private final boolean crop;

    public Canvas(int i, int i2, Position position) {
        this(i, i2, position, true, null);
    }

    public Canvas(int i, int i2, Position position, boolean z) {
        this(i, i2, position, z, null);
    }

    public Canvas(int i, int i2, Position position, Color color) {
        this(i, i2, position, true, color);
    }

    public Canvas(int i, int i2, Position position, boolean z, Color color) {
        this.width = i;
        this.height = i2;
        this.position = position;
        this.crop = z;
        this.fillColor = color;
    }

    @Override // net.coobird.thumbnailator.filters.ImageFilter
    public BufferedImage apply(BufferedImage bufferedImage) {
        int width = this.width;
        int height = this.height;
        if (!this.crop && bufferedImage.getWidth() > this.width) {
            width = bufferedImage.getWidth();
        }
        if (!this.crop && bufferedImage.getHeight() > this.height) {
            height = bufferedImage.getHeight();
        }
        Point pointCalculate = this.position.calculate(width, height, bufferedImage.getWidth(), bufferedImage.getHeight(), 0, 0, 0, 0);
        BufferedImage bufferedImage2 = new BufferedImage(width, height, bufferedImage.getType());
        Graphics graphics = bufferedImage2.getGraphics();
        if (this.fillColor == null && !bufferedImage.getColorModel().hasAlpha()) {
            graphics.setColor(Color.black);
            graphics.fillRect(0, 0, this.width, this.height);
        } else if (this.fillColor != null) {
            graphics.setColor(this.fillColor);
            graphics.fillRect(0, 0, width, height);
        }
        graphics.drawImage(bufferedImage, pointCalculate.x, pointCalculate.y, (ImageObserver) null);
        graphics.dispose();
        return bufferedImage2;
    }
}
