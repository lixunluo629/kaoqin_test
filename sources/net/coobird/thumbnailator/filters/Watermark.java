package net.coobird.thumbnailator.filters;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import net.coobird.thumbnailator.builders.BufferedImageBuilder;
import net.coobird.thumbnailator.geometry.Position;

/* loaded from: thumbnailator-0.4.8.jar:net/coobird/thumbnailator/filters/Watermark.class */
public class Watermark implements ImageFilter {
    private final Position position;
    private final BufferedImage watermarkImg;
    private final float opacity;

    public Watermark(Position position, BufferedImage bufferedImage, float f) {
        if (position == null) {
            throw new NullPointerException("Position is null.");
        }
        if (bufferedImage == null) {
            throw new NullPointerException("Watermark image is null.");
        }
        if (f > 1.0f || f < 0.0f) {
            throw new IllegalArgumentException("Opacity is out of range of between 0.0f and 1.0f.");
        }
        this.position = position;
        this.watermarkImg = bufferedImage;
        this.opacity = f;
    }

    @Override // net.coobird.thumbnailator.filters.ImageFilter
    public BufferedImage apply(BufferedImage bufferedImage) {
        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();
        BufferedImage bufferedImageBuild = new BufferedImageBuilder(width, height, bufferedImage.getType()).build();
        Point pointCalculate = this.position.calculate(width, height, this.watermarkImg.getWidth(), this.watermarkImg.getHeight(), 0, 0, 0, 0);
        Graphics2D graphics2DCreateGraphics = bufferedImageBuild.createGraphics();
        graphics2DCreateGraphics.drawImage(bufferedImage, 0, 0, (ImageObserver) null);
        graphics2DCreateGraphics.setComposite(AlphaComposite.getInstance(3, this.opacity));
        graphics2DCreateGraphics.drawImage(this.watermarkImg, pointCalculate.x, pointCalculate.y, (ImageObserver) null);
        graphics2DCreateGraphics.dispose();
        return bufferedImageBuild;
    }
}
