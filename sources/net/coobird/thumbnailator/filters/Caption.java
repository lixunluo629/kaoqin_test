package net.coobird.thumbnailator.filters;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import net.coobird.thumbnailator.geometry.Position;
import net.coobird.thumbnailator.util.BufferedImages;

/* loaded from: thumbnailator-0.4.8.jar:net/coobird/thumbnailator/filters/Caption.class */
public class Caption implements ImageFilter {
    private final String caption;
    private final Font font;
    private final Color c;
    private final float alpha;
    private final Position position;
    private final int insets;

    public Caption(String str, Font font, Color color, float f, Position position, int i) {
        this.caption = str;
        this.font = font;
        this.c = color;
        this.alpha = f;
        this.position = position;
        this.insets = i;
    }

    public Caption(String str, Font font, Color color, Position position, int i) {
        this.caption = str;
        this.font = font;
        this.c = color;
        this.alpha = 1.0f;
        this.position = position;
        this.insets = i;
    }

    @Override // net.coobird.thumbnailator.filters.ImageFilter
    public BufferedImage apply(BufferedImage bufferedImage) {
        BufferedImage bufferedImageCopy = BufferedImages.copy(bufferedImage);
        Graphics2D graphics2DCreateGraphics = bufferedImageCopy.createGraphics();
        graphics2DCreateGraphics.setFont(this.font);
        graphics2DCreateGraphics.setColor(this.c);
        graphics2DCreateGraphics.setComposite(AlphaComposite.getInstance(3, this.alpha));
        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();
        int iStringWidth = graphics2DCreateGraphics.getFontMetrics().stringWidth(this.caption);
        int height2 = graphics2DCreateGraphics.getFontMetrics().getHeight() / 2;
        Point pointCalculate = this.position.calculate(width, height, iStringWidth, 0, this.insets, this.insets, this.insets, this.insets);
        graphics2DCreateGraphics.drawString(this.caption, pointCalculate.x, pointCalculate.y + ((int) ((1.0d - (pointCalculate.y / bufferedImage.getHeight())) * height2)));
        graphics2DCreateGraphics.dispose();
        return bufferedImageCopy;
    }
}
