package net.coobird.thumbnailator.resizers;

import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.Collections;
import java.util.Map;

/* loaded from: thumbnailator-0.4.8.jar:net/coobird/thumbnailator/resizers/BilinearResizer.class */
public class BilinearResizer extends AbstractResizer {
    public BilinearResizer() {
        this(Collections.emptyMap());
    }

    public BilinearResizer(Map<RenderingHints.Key, Object> map) {
        super(RenderingHints.VALUE_INTERPOLATION_BILINEAR, map);
    }

    @Override // net.coobird.thumbnailator.resizers.AbstractResizer, net.coobird.thumbnailator.resizers.Resizer
    public void resize(BufferedImage bufferedImage, BufferedImage bufferedImage2) throws NullPointerException {
        super.resize(bufferedImage, bufferedImage2);
    }
}
