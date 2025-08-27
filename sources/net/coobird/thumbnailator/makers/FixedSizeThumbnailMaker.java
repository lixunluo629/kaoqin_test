package net.coobird.thumbnailator.makers;

import java.awt.image.BufferedImage;

/* loaded from: thumbnailator-0.4.8.jar:net/coobird/thumbnailator/makers/FixedSizeThumbnailMaker.class */
public final class FixedSizeThumbnailMaker extends ThumbnailMaker {
    private static final String PARAM_SIZE = "size";
    private static final String PARAM_KEEP_RATIO = "keepRatio";
    private static final String PARAM_FIT_WITHIN = "fitWithinDimensions";
    private int width;
    private int height;
    private boolean keepRatio;
    private boolean fitWithinDimensions;

    public FixedSizeThumbnailMaker() {
        this.ready.unset("size");
        this.ready.unset(PARAM_KEEP_RATIO);
        this.ready.unset(PARAM_FIT_WITHIN);
    }

    public FixedSizeThumbnailMaker(int i, int i2) {
        this();
        size(i, i2);
    }

    public FixedSizeThumbnailMaker(int i, int i2, boolean z) {
        this();
        size(i, i2);
        keepAspectRatio(z);
    }

    public FixedSizeThumbnailMaker(int i, int i2, boolean z, boolean z2) {
        this();
        size(i, i2);
        keepAspectRatio(z);
        fitWithinDimensions(z2);
    }

    public FixedSizeThumbnailMaker size(int i, int i2) {
        if (this.ready.isSet("size")) {
            throw new IllegalStateException("The size has already been set.");
        }
        if (i <= 0) {
            throw new IllegalArgumentException("Width must be greater than zero.");
        }
        if (i2 <= 0) {
            throw new IllegalArgumentException("Height must be greater than zero.");
        }
        this.width = i;
        this.height = i2;
        this.ready.set("size");
        return this;
    }

    public FixedSizeThumbnailMaker keepAspectRatio(boolean z) {
        if (this.ready.isSet(PARAM_KEEP_RATIO)) {
            throw new IllegalStateException("Whether to keep the aspect ratio has already been set.");
        }
        this.keepRatio = z;
        this.ready.set(PARAM_KEEP_RATIO);
        return this;
    }

    public FixedSizeThumbnailMaker fitWithinDimensions(boolean z) {
        if (this.ready.isSet(PARAM_FIT_WITHIN)) {
            throw new IllegalStateException("Whether to fit within dimensions has already been set.");
        }
        this.fitWithinDimensions = z;
        this.ready.set(PARAM_FIT_WITHIN);
        return this;
    }

    @Override // net.coobird.thumbnailator.makers.ThumbnailMaker
    public BufferedImage make(BufferedImage bufferedImage) {
        int iRound = this.width;
        int iRound2 = this.height;
        if (this.keepRatio) {
            double width = bufferedImage.getWidth() / bufferedImage.getHeight();
            double d = iRound / iRound2;
            if (Double.compare(width, d) != 0) {
                if (this.fitWithinDimensions) {
                    if (width > d) {
                        iRound = this.width;
                        iRound2 = (int) Math.round(iRound / width);
                    } else {
                        iRound = (int) Math.round(iRound2 * width);
                        iRound2 = this.height;
                    }
                } else if (width > d) {
                    iRound = (int) Math.round(iRound2 * width);
                    iRound2 = this.height;
                } else {
                    iRound = this.width;
                    iRound2 = (int) Math.round(iRound / width);
                }
            }
        }
        return super.makeThumbnail(bufferedImage, iRound == 0 ? 1 : iRound, iRound2 == 0 ? 1 : iRound2);
    }
}
