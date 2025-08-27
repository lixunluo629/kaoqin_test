package net.coobird.thumbnailator.resizers;

import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.Collections;
import java.util.Map;

/* loaded from: thumbnailator-0.4.8.jar:net/coobird/thumbnailator/resizers/BicubicResizer.class */
public class BicubicResizer extends AbstractResizer {
    public BicubicResizer() {
        this(Collections.emptyMap());
    }

    public BicubicResizer(Map<RenderingHints.Key, Object> map) {
        super(RenderingHints.VALUE_INTERPOLATION_BICUBIC, map);
    }

    @Override // net.coobird.thumbnailator.resizers.AbstractResizer, net.coobird.thumbnailator.resizers.Resizer
    public void resize(BufferedImage bufferedImage, BufferedImage bufferedImage2) throws NullPointerException {
        super.resize(bufferedImage, bufferedImage2);
    }
}
