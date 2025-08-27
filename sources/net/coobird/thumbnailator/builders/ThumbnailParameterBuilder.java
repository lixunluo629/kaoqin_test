package net.coobird.thumbnailator.builders;

import java.awt.Dimension;
import java.util.Collections;
import java.util.List;
import net.coobird.thumbnailator.ThumbnailParameter;
import net.coobird.thumbnailator.filters.ImageFilter;
import net.coobird.thumbnailator.geometry.Region;
import net.coobird.thumbnailator.resizers.DefaultResizerFactory;
import net.coobird.thumbnailator.resizers.FixedResizerFactory;
import net.coobird.thumbnailator.resizers.Resizer;
import net.coobird.thumbnailator.resizers.ResizerFactory;

/* loaded from: thumbnailator-0.4.8.jar:net/coobird/thumbnailator/builders/ThumbnailParameterBuilder.class */
public final class ThumbnailParameterBuilder {
    private static final int UNINITIALIZED = -1;
    private int width = -1;
    private int height = -1;
    private double widthScalingFactor = Double.NaN;
    private double heightScalingFactor = Double.NaN;
    private int imageType = 2;
    private boolean keepAspectRatio = true;
    private float thumbnailQuality = Float.NaN;
    private String thumbnailFormat = ThumbnailParameter.ORIGINAL_FORMAT;
    private String thumbnailFormatType = ThumbnailParameter.DEFAULT_FORMAT_TYPE;
    private List<ImageFilter> filters = Collections.emptyList();
    private ResizerFactory resizerFactory = DefaultResizerFactory.getInstance();
    private Region sourceRegion = null;
    private boolean fitWithinDimensions = true;
    private boolean useExifOrientation = true;

    public ThumbnailParameterBuilder imageType(int i) {
        this.imageType = i;
        return this;
    }

    public ThumbnailParameterBuilder size(Dimension dimension) {
        size(dimension.width, dimension.height);
        return this;
    }

    public ThumbnailParameterBuilder size(int i, int i2) {
        if (i < 0) {
            throw new IllegalArgumentException("Width must be greater than 0.");
        }
        if (i2 < 0) {
            throw new IllegalArgumentException("Height must be greater than 0.");
        }
        this.width = i;
        this.height = i2;
        return this;
    }

    public ThumbnailParameterBuilder scale(double d) {
        return scale(d, d);
    }

    public ThumbnailParameterBuilder scale(double d, double d2) {
        if (d <= 0.0d || d2 <= 0.0d) {
            throw new IllegalArgumentException("Scaling factor is less than or equal to 0.");
        }
        if (Double.isNaN(d) || Double.isInfinite(d)) {
            throw new IllegalArgumentException("Scaling factor must be a rational number.");
        }
        if (Double.isNaN(d2) || Double.isInfinite(d2)) {
            throw new IllegalArgumentException("Scaling factor must be a rational number.");
        }
        this.widthScalingFactor = d;
        this.heightScalingFactor = d2;
        return this;
    }

    public ThumbnailParameterBuilder region(Region region) {
        this.sourceRegion = region;
        return this;
    }

    public ThumbnailParameterBuilder keepAspectRatio(boolean z) {
        this.keepAspectRatio = z;
        return this;
    }

    public ThumbnailParameterBuilder quality(float f) {
        this.thumbnailQuality = f;
        return this;
    }

    public ThumbnailParameterBuilder format(String str) {
        this.thumbnailFormat = str;
        return this;
    }

    public ThumbnailParameterBuilder formatType(String str) {
        this.thumbnailFormatType = str;
        return this;
    }

    public ThumbnailParameterBuilder filters(List<ImageFilter> list) {
        if (list == null) {
            throw new NullPointerException("Filters is null.");
        }
        this.filters = list;
        return this;
    }

    public ThumbnailParameterBuilder resizer(Resizer resizer) {
        if (resizer == null) {
            throw new NullPointerException("Resizer is null.");
        }
        this.resizerFactory = new FixedResizerFactory(resizer);
        return this;
    }

    public ThumbnailParameterBuilder resizerFactory(ResizerFactory resizerFactory) {
        if (resizerFactory == null) {
            throw new NullPointerException("Resizer is null.");
        }
        this.resizerFactory = resizerFactory;
        return this;
    }

    public ThumbnailParameterBuilder fitWithinDimensions(boolean z) {
        this.fitWithinDimensions = z;
        return this;
    }

    public ThumbnailParameterBuilder useExifOrientation(boolean z) {
        this.useExifOrientation = z;
        return this;
    }

    public ThumbnailParameter build() {
        if (!Double.isNaN(this.widthScalingFactor)) {
            return new ThumbnailParameter(this.widthScalingFactor, this.heightScalingFactor, this.sourceRegion, this.keepAspectRatio, this.thumbnailFormat, this.thumbnailFormatType, this.thumbnailQuality, this.imageType, this.filters, this.resizerFactory, this.fitWithinDimensions, this.useExifOrientation);
        }
        if (this.width != -1 && this.height != -1) {
            return new ThumbnailParameter(new Dimension(this.width, this.height), this.sourceRegion, this.keepAspectRatio, this.thumbnailFormat, this.thumbnailFormatType, this.thumbnailQuality, this.imageType, this.filters, this.resizerFactory, this.fitWithinDimensions, this.useExifOrientation);
        }
        throw new IllegalStateException("The size nor the scaling factor has been set.");
    }
}
