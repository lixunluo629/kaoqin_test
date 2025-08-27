package net.coobird.thumbnailator.makers;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import net.coobird.thumbnailator.builders.BufferedImageBuilder;
import net.coobird.thumbnailator.resizers.DefaultResizerFactory;
import net.coobird.thumbnailator.resizers.FixedResizerFactory;
import net.coobird.thumbnailator.resizers.Resizer;
import net.coobird.thumbnailator.resizers.ResizerFactory;

/* loaded from: thumbnailator-0.4.8.jar:net/coobird/thumbnailator/makers/ThumbnailMaker.class */
public abstract class ThumbnailMaker {
    private static final String NOT_READY_FOR_MAKE = "Maker not ready to make thumbnail.";
    private static final String PARAM_IMAGE_TYPE = "imageType";
    private static final String PARAM_RESIZER = "resizer";
    private static final String PARAM_RESIZERFACTORY = "resizerFactory";
    protected final ReadinessTracker ready = new ReadinessTracker();
    private static final int DEFAULT_IMAGE_TYPE = 2;
    protected int imageType;
    protected ResizerFactory resizerFactory;

    public abstract BufferedImage make(BufferedImage bufferedImage);

    /* loaded from: thumbnailator-0.4.8.jar:net/coobird/thumbnailator/makers/ThumbnailMaker$ReadinessTracker.class */
    protected static final class ReadinessTracker {
        private final Map<String, Boolean> alreadySetMap = new HashMap();

        protected ReadinessTracker() {
        }

        protected boolean isReady() {
            Iterator<Map.Entry<String, Boolean>> it = this.alreadySetMap.entrySet().iterator();
            while (it.hasNext()) {
                if (!it.next().getValue().booleanValue()) {
                    return false;
                }
            }
            return true;
        }

        protected void unset(String str) {
            this.alreadySetMap.put(str, false);
        }

        protected void set(String str) {
            this.alreadySetMap.put(str, true);
        }

        protected boolean isSet(String str) {
            return this.alreadySetMap.get(str).booleanValue();
        }
    }

    public ThumbnailMaker() {
        this.ready.unset(PARAM_IMAGE_TYPE);
        this.ready.unset(PARAM_RESIZER);
        this.ready.unset(PARAM_RESIZERFACTORY);
        defaultImageType();
        defaultResizerFactory();
    }

    protected BufferedImage makeThumbnail(BufferedImage bufferedImage, int i, int i2) {
        if (!this.ready.isReady()) {
            throw new IllegalStateException(NOT_READY_FOR_MAKE);
        }
        if (i <= 0) {
            throw new IllegalArgumentException("Width must be greater than zero.");
        }
        if (i2 <= 0) {
            throw new IllegalArgumentException("Height must be greater than zero.");
        }
        BufferedImage bufferedImageBuild = new BufferedImageBuilder(i, i2, this.imageType).build();
        this.resizerFactory.getResizer(new Dimension(bufferedImage.getWidth(), bufferedImage.getHeight()), new Dimension(i, i2)).resize(bufferedImage, bufferedImageBuild);
        return bufferedImageBuild;
    }

    public ThumbnailMaker imageType(int i) {
        this.imageType = i;
        this.ready.set(PARAM_IMAGE_TYPE);
        return this;
    }

    public ThumbnailMaker defaultImageType() {
        return imageType(2);
    }

    public ThumbnailMaker resizer(Resizer resizer) {
        this.resizerFactory = new FixedResizerFactory(resizer);
        this.ready.set(PARAM_RESIZER);
        this.ready.set(PARAM_RESIZERFACTORY);
        return this;
    }

    public ThumbnailMaker defaultResizer() {
        return defaultResizerFactory();
    }

    public ThumbnailMaker resizerFactory(ResizerFactory resizerFactory) {
        this.resizerFactory = resizerFactory;
        this.ready.set(PARAM_RESIZER);
        this.ready.set(PARAM_RESIZERFACTORY);
        return this;
    }

    public ThumbnailMaker defaultResizerFactory() {
        this.resizerFactory = DefaultResizerFactory.getInstance();
        this.ready.set(PARAM_RESIZER);
        this.ready.set(PARAM_RESIZERFACTORY);
        return this;
    }
}
