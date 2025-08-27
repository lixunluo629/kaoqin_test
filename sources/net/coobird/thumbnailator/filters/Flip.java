package net.coobird.thumbnailator.filters;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import net.coobird.thumbnailator.builders.BufferedImageBuilder;

/* loaded from: thumbnailator-0.4.8.jar:net/coobird/thumbnailator/filters/Flip.class */
public class Flip {
    public static final ImageFilter HORIZONTAL = new ImageFilter() { // from class: net.coobird.thumbnailator.filters.Flip.1
        @Override // net.coobird.thumbnailator.filters.ImageFilter
        public BufferedImage apply(BufferedImage bufferedImage) {
            int width = bufferedImage.getWidth();
            int height = bufferedImage.getHeight();
            BufferedImage bufferedImageBuild = new BufferedImageBuilder(width, height).build();
            Graphics graphics = bufferedImageBuild.getGraphics();
            graphics.drawImage(bufferedImage, width, 0, 0, height, 0, 0, width, height, (ImageObserver) null);
            graphics.dispose();
            return bufferedImageBuild;
        }
    };
    public static final ImageFilter VERTICAL = new ImageFilter() { // from class: net.coobird.thumbnailator.filters.Flip.2
        @Override // net.coobird.thumbnailator.filters.ImageFilter
        public BufferedImage apply(BufferedImage bufferedImage) {
            int width = bufferedImage.getWidth();
            int height = bufferedImage.getHeight();
            BufferedImage bufferedImageBuild = new BufferedImageBuilder(width, height).build();
            Graphics graphics = bufferedImageBuild.getGraphics();
            graphics.drawImage(bufferedImage, 0, height, width, 0, 0, 0, width, height, (ImageObserver) null);
            graphics.dispose();
            return bufferedImageBuild;
        }
    };
}
