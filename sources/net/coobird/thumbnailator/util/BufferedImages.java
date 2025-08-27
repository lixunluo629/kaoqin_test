package net.coobird.thumbnailator.util;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;

/* loaded from: thumbnailator-0.4.8.jar:net/coobird/thumbnailator/util/BufferedImages.class */
public final class BufferedImages {
    private BufferedImages() {
    }

    public static BufferedImage copy(BufferedImage bufferedImage) {
        return copy(bufferedImage, bufferedImage.getType());
    }

    public static BufferedImage copy(BufferedImage bufferedImage, int i) {
        BufferedImage bufferedImage2 = new BufferedImage(bufferedImage.getWidth(), bufferedImage.getHeight(), i);
        Graphics2D graphics2DCreateGraphics = bufferedImage2.createGraphics();
        graphics2DCreateGraphics.drawImage(bufferedImage, 0, 0, (ImageObserver) null);
        graphics2DCreateGraphics.dispose();
        return bufferedImage2;
    }
}
