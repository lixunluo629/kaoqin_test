package net.coobird.thumbnailator.resizers;

import java.awt.Dimension;

/* loaded from: thumbnailator-0.4.8.jar:net/coobird/thumbnailator/resizers/ResizerFactory.class */
public interface ResizerFactory {
    Resizer getResizer();

    Resizer getResizer(Dimension dimension, Dimension dimension2);
}
