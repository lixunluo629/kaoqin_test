package net.coobird.thumbnailator.resizers;

import java.awt.image.BufferedImage;

/* loaded from: thumbnailator-0.4.8.jar:net/coobird/thumbnailator/resizers/Resizers.class */
public enum Resizers implements Resizer {
    NULL(new NullResizer()),
    BILINEAR(new BilinearResizer()),
    BICUBIC(new BicubicResizer()),
    PROGRESSIVE(new ProgressiveBilinearResizer());

    private final Resizer resizer;

    Resizers(Resizer resizer) {
        this.resizer = resizer;
    }

    @Override // net.coobird.thumbnailator.resizers.Resizer
    public void resize(BufferedImage bufferedImage, BufferedImage bufferedImage2) {
        this.resizer.resize(bufferedImage, bufferedImage2);
    }
}
