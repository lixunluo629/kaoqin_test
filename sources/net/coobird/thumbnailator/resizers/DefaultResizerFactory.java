package net.coobird.thumbnailator.resizers;

import java.awt.Dimension;

/* loaded from: thumbnailator-0.4.8.jar:net/coobird/thumbnailator/resizers/DefaultResizerFactory.class */
public class DefaultResizerFactory implements ResizerFactory {
    private static final DefaultResizerFactory INSTANCE = new DefaultResizerFactory();

    private DefaultResizerFactory() {
    }

    public static ResizerFactory getInstance() {
        return INSTANCE;
    }

    @Override // net.coobird.thumbnailator.resizers.ResizerFactory
    public Resizer getResizer() {
        return Resizers.PROGRESSIVE;
    }

    @Override // net.coobird.thumbnailator.resizers.ResizerFactory
    public Resizer getResizer(Dimension dimension, Dimension dimension2) {
        int i = dimension.width;
        int i2 = dimension.height;
        int i3 = dimension2.width;
        int i4 = dimension2.height;
        if (i3 < i && i4 < i2) {
            if (i3 < i / 2 && i4 < i2 / 2) {
                return Resizers.PROGRESSIVE;
            }
            return Resizers.BILINEAR;
        }
        if (i3 > i && i4 > i2) {
            return Resizers.BICUBIC;
        }
        if (i3 == i && i4 == i2) {
            return Resizers.NULL;
        }
        return getResizer();
    }
}
