package net.coobird.thumbnailator.resizers;

import java.awt.Graphics;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.util.Collections;
import java.util.Map;

/* loaded from: thumbnailator-0.4.8.jar:net/coobird/thumbnailator/resizers/NullResizer.class */
public class NullResizer extends AbstractResizer {
    public NullResizer() {
        this(RenderingHints.VALUE_INTERPOLATION_BILINEAR, Collections.emptyMap());
    }

    private NullResizer(Object obj, Map<RenderingHints.Key, Object> map) {
        super(obj, map);
    }

    @Override // net.coobird.thumbnailator.resizers.AbstractResizer, net.coobird.thumbnailator.resizers.Resizer
    public void resize(BufferedImage bufferedImage, BufferedImage bufferedImage2) {
        super.performChecks(bufferedImage, bufferedImage2);
        Graphics graphics = bufferedImage2.getGraphics();
        graphics.drawImage(bufferedImage, 0, 0, (ImageObserver) null);
        graphics.dispose();
    }
}
