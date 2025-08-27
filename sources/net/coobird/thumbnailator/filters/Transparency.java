package net.coobird.thumbnailator.filters;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;

/* loaded from: thumbnailator-0.4.8.jar:net/coobird/thumbnailator/filters/Transparency.class */
public class Transparency implements ImageFilter {
    private final AlphaComposite composite;

    public Transparency(float f) {
        if (f < 0.0f || f > 1.0f) {
            throw new IllegalArgumentException("The alpha must be between 0.0f and 1.0f, inclusive.");
        }
        this.composite = AlphaComposite.getInstance(3, f);
    }

    public Transparency(double d) {
        this((float) d);
    }

    @Override // net.coobird.thumbnailator.filters.ImageFilter
    public BufferedImage apply(BufferedImage bufferedImage) {
        BufferedImage bufferedImage2 = new BufferedImage(bufferedImage.getWidth(), bufferedImage.getHeight(), 2);
        Graphics2D graphics2DCreateGraphics = bufferedImage2.createGraphics();
        graphics2DCreateGraphics.setComposite(this.composite);
        graphics2DCreateGraphics.drawImage(bufferedImage, 0, 0, (ImageObserver) null);
        graphics2DCreateGraphics.dispose();
        return bufferedImage2;
    }

    public float getAlpha() {
        return this.composite.getAlpha();
    }
}
