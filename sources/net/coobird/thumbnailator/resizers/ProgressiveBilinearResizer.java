package net.coobird.thumbnailator.resizers;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.util.Collections;
import java.util.Map;

/* loaded from: thumbnailator-0.4.8.jar:net/coobird/thumbnailator/resizers/ProgressiveBilinearResizer.class */
public class ProgressiveBilinearResizer extends AbstractResizer {
    public ProgressiveBilinearResizer() {
        this(Collections.emptyMap());
    }

    public ProgressiveBilinearResizer(Map<RenderingHints.Key, Object> map) {
        super(RenderingHints.VALUE_INTERPOLATION_BILINEAR, map);
    }

    @Override // net.coobird.thumbnailator.resizers.AbstractResizer, net.coobird.thumbnailator.resizers.Resizer
    public void resize(BufferedImage bufferedImage, BufferedImage bufferedImage2) throws NullPointerException {
        int i;
        super.performChecks(bufferedImage, bufferedImage2);
        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();
        int width2 = bufferedImage2.getWidth();
        int height2 = bufferedImage2.getHeight();
        if (width2 * 2 >= width && height2 * 2 >= height) {
            Graphics2D graphics2DCreateGraphics = bufferedImage2.createGraphics();
            graphics2DCreateGraphics.drawImage(bufferedImage, 0, 0, width2, height2, (ImageObserver) null);
            graphics2DCreateGraphics.dispose();
            return;
        }
        BufferedImage bufferedImage3 = new BufferedImage(width, height, bufferedImage2.getType());
        Graphics2D graphics2DCreateGraphics2 = bufferedImage3.createGraphics();
        graphics2DCreateGraphics2.setRenderingHints(this.RENDERING_HINTS);
        graphics2DCreateGraphics2.setComposite(AlphaComposite.Src);
        int i2 = width2;
        int i3 = height2;
        while (true) {
            i = i3;
            if (i2 >= width || i >= height) {
                break;
            }
            i2 *= 2;
            i3 = i * 2;
        }
        int i4 = i2 / 2;
        int i5 = i / 2;
        graphics2DCreateGraphics2.drawImage(bufferedImage, 0, 0, i4, i5, (ImageObserver) null);
        while (i4 >= width2 * 2 && i5 >= height2 * 2) {
            i4 /= 2;
            i5 /= 2;
            if (i4 < width2) {
                i4 = width2;
            }
            if (i5 < height2) {
                i5 = height2;
            }
            graphics2DCreateGraphics2.drawImage(bufferedImage3, 0, 0, i4, i5, 0, 0, i4 * 2, i5 * 2, (ImageObserver) null);
        }
        graphics2DCreateGraphics2.dispose();
        Graphics2D graphics2DCreateGraphics3 = bufferedImage2.createGraphics();
        graphics2DCreateGraphics3.drawImage(bufferedImage3, 0, 0, width2, height2, 0, 0, i4, i5, (ImageObserver) null);
        graphics2DCreateGraphics3.dispose();
    }
}
