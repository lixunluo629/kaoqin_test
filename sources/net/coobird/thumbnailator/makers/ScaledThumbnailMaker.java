package net.coobird.thumbnailator.makers;

import java.awt.image.BufferedImage;

/* loaded from: thumbnailator-0.4.8.jar:net/coobird/thumbnailator/makers/ScaledThumbnailMaker.class */
public final class ScaledThumbnailMaker extends ThumbnailMaker {
    private static final String PARAM_SCALE = "scale";
    private double widthFactor;
    private double heightFactor;

    public ScaledThumbnailMaker() {
        this.ready.unset(PARAM_SCALE);
    }

    public ScaledThumbnailMaker(double d) {
        this();
        scale(d);
    }

    public ScaledThumbnailMaker(double d, double d2) {
        this();
        scale(d, d2);
    }

    public ScaledThumbnailMaker scale(double d) {
        return scale(d, d);
    }

    public ScaledThumbnailMaker scale(double d, double d2) {
        if (this.ready.isSet(PARAM_SCALE)) {
            throw new IllegalStateException("The scaling factor has already been set.");
        }
        if (d <= 0.0d || d2 <= 0.0d) {
            throw new IllegalArgumentException("The scaling factor must be greater than zero.");
        }
        this.widthFactor = d;
        this.heightFactor = d2;
        this.ready.set(PARAM_SCALE);
        return this;
    }

    @Override // net.coobird.thumbnailator.makers.ThumbnailMaker
    public BufferedImage make(BufferedImage bufferedImage) {
        int iRound = (int) Math.round(bufferedImage.getWidth() * this.widthFactor);
        int iRound2 = (int) Math.round(bufferedImage.getHeight() * this.heightFactor);
        return super.makeThumbnail(bufferedImage, iRound == 0 ? 1 : iRound, iRound2 == 0 ? 1 : iRound2);
    }
}
