package net.coobird.thumbnailator.resizers;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/* loaded from: thumbnailator-0.4.8.jar:net/coobird/thumbnailator/resizers/AbstractResizer.class */
public abstract class AbstractResizer implements Resizer {
    protected final Map<RenderingHints.Key, Object> RENDERING_HINTS = new HashMap();
    protected final Map<RenderingHints.Key, Object> UNMODIFIABLE_RENDERING_HINTS;
    protected static final RenderingHints.Key KEY_INTERPOLATION = RenderingHints.KEY_INTERPOLATION;

    protected AbstractResizer(Object obj, Map<RenderingHints.Key, Object> map) {
        this.RENDERING_HINTS.put(KEY_INTERPOLATION, obj);
        if (map.containsKey(KEY_INTERPOLATION) && !obj.equals(map.get(KEY_INTERPOLATION))) {
            throw new IllegalArgumentException("Cannot change the RenderingHints.KEY_INTERPOLATION value.");
        }
        this.RENDERING_HINTS.putAll(map);
        this.UNMODIFIABLE_RENDERING_HINTS = Collections.unmodifiableMap(this.RENDERING_HINTS);
    }

    @Override // net.coobird.thumbnailator.resizers.Resizer
    public void resize(BufferedImage bufferedImage, BufferedImage bufferedImage2) {
        performChecks(bufferedImage, bufferedImage2);
        int width = bufferedImage2.getWidth();
        int height = bufferedImage2.getHeight();
        Graphics2D graphics2DCreateGraphics = bufferedImage2.createGraphics();
        graphics2DCreateGraphics.setRenderingHints(this.RENDERING_HINTS);
        graphics2DCreateGraphics.drawImage(bufferedImage, 0, 0, width, height, (ImageObserver) null);
        graphics2DCreateGraphics.dispose();
    }

    protected void performChecks(BufferedImage bufferedImage, BufferedImage bufferedImage2) {
        if (bufferedImage == null || bufferedImage2 == null) {
            throw new NullPointerException("The source and/or destination image is null.");
        }
    }

    public Map<RenderingHints.Key, Object> getRenderingHints() {
        return this.UNMODIFIABLE_RENDERING_HINTS;
    }
}
