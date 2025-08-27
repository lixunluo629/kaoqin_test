package net.coobird.thumbnailator.resizers;

import java.awt.Dimension;

/* loaded from: thumbnailator-0.4.8.jar:net/coobird/thumbnailator/resizers/FixedResizerFactory.class */
public class FixedResizerFactory implements ResizerFactory {
    private final Resizer resizer;

    public FixedResizerFactory(Resizer resizer) {
        this.resizer = resizer;
    }

    @Override // net.coobird.thumbnailator.resizers.ResizerFactory
    public Resizer getResizer() {
        return this.resizer;
    }

    @Override // net.coobird.thumbnailator.resizers.ResizerFactory
    public Resizer getResizer(Dimension dimension, Dimension dimension2) {
        return this.resizer;
    }
}
