package net.coobird.thumbnailator.filters;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import net.coobird.thumbnailator.builders.BufferedImageBuilder;

/* loaded from: thumbnailator-0.4.8.jar:net/coobird/thumbnailator/filters/Colorize.class */
public final class Colorize implements ImageFilter {
    private final Color c;

    public Colorize(Color color) {
        this.c = color;
    }

    public Colorize(Color color, float f) {
        this(color, (int) (255.0f * f));
    }

    public Colorize(Color color, int i) {
        if (i > 255 || i < 0) {
            throw new IllegalArgumentException("Specified alpha value is outside the range of allowed values.");
        }
        this.c = new Color(color.getRed(), color.getGreen(), color.getBlue(), i);
    }

    @Override // net.coobird.thumbnailator.filters.ImageFilter
    public BufferedImage apply(BufferedImage bufferedImage) {
        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();
        BufferedImage bufferedImageBuild = new BufferedImageBuilder(width, height).build();
        Graphics2D graphics2DCreateGraphics = bufferedImageBuild.createGraphics();
        graphics2DCreateGraphics.drawImage(bufferedImage, 0, 0, (ImageObserver) null);
        graphics2DCreateGraphics.setColor(this.c);
        graphics2DCreateGraphics.fillRect(0, 0, width, height);
        graphics2DCreateGraphics.dispose();
        return bufferedImageBuild;
    }
}
