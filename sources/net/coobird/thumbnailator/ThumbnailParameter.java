package net.coobird.thumbnailator;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;
import net.coobird.thumbnailator.filters.ImageFilter;
import net.coobird.thumbnailator.geometry.Region;
import net.coobird.thumbnailator.resizers.FixedResizerFactory;
import net.coobird.thumbnailator.resizers.Resizer;
import net.coobird.thumbnailator.resizers.ResizerFactory;

/* loaded from: thumbnailator-0.4.8.jar:net/coobird/thumbnailator/ThumbnailParameter.class */
public class ThumbnailParameter {
    public static final String DETERMINE_FORMAT = "��";
    public static final float DEFAULT_QUALITY = Float.NaN;
    public static final int ORIGINAL_IMAGE_TYPE = -1;
    public static final int DEFAULT_IMAGE_TYPE = 2;
    private final Dimension thumbnailSize;
    private final double widthScalingFactor;
    private final double heightScalingFactor;
    private final boolean keepAspectRatio;
    private final String outputFormat;
    private final String outputFormatType;
    private final float outputQuality;
    private final int imageType;
    private final List<ImageFilter> filters;
    private final ResizerFactory resizerFactory;
    private final Region sourceRegion;
    private final boolean fitWithinDimensions;
    private final boolean useExifOrientation;
    public static final String ORIGINAL_FORMAT = null;
    public static final String DEFAULT_FORMAT_TYPE = null;

    private ThumbnailParameter(Dimension dimension, double d, double d2, Region region, boolean z, String str, String str2, float f, int i, List<ImageFilter> list, ResizerFactory resizerFactory, boolean z2, boolean z3) {
        this.thumbnailSize = dimension;
        this.widthScalingFactor = d;
        this.heightScalingFactor = d2;
        this.keepAspectRatio = z;
        this.sourceRegion = region;
        this.outputFormat = str;
        this.outputFormatType = str2;
        if ((f < 0.0f || f > 1.0f) && !Float.isNaN(f)) {
            throw new IllegalArgumentException("The output quality must be between 0.0f and 1.0f, or Float.NaN to use the default compression quality of codec being used.");
        }
        this.outputQuality = f;
        this.imageType = i;
        if (list == null) {
            this.filters = new ArrayList();
        } else {
            this.filters = new ArrayList(list);
        }
        if (resizerFactory == null) {
            throw new IllegalArgumentException("Resizer cannot be null");
        }
        this.resizerFactory = resizerFactory;
        this.fitWithinDimensions = z2;
        this.useExifOrientation = z3;
    }

    private void validateThumbnailSize() {
        if (this.thumbnailSize == null) {
            throw new IllegalArgumentException("Thumbnail size cannot be null.");
        }
        if (this.thumbnailSize.width < 0 || this.thumbnailSize.height < 0) {
            throw new IllegalArgumentException("Thumbnail dimensions must be greater than 0.");
        }
    }

    private void validateScalingFactor() {
        if (this.widthScalingFactor <= 0.0d || this.heightScalingFactor <= 0.0d) {
            throw new IllegalArgumentException("Scaling factor is less than or equal to 0.");
        }
        if (Double.isNaN(this.widthScalingFactor) || Double.isInfinite(this.widthScalingFactor)) {
            throw new IllegalArgumentException("Scaling factor must be a rational number.");
        }
        if (Double.isNaN(this.heightScalingFactor) || Double.isInfinite(this.heightScalingFactor)) {
            throw new IllegalArgumentException("Scaling factor must be a rational number.");
        }
    }

    public ThumbnailParameter(Dimension dimension, Region region, boolean z, String str, String str2, float f, int i, List<ImageFilter> list, Resizer resizer, boolean z2, boolean z3) {
        this(dimension, Double.NaN, Double.NaN, region, z, str, str2, f, i, list, new FixedResizerFactory(resizer), z2, z3);
        validateThumbnailSize();
    }

    public ThumbnailParameter(double d, double d2, Region region, boolean z, String str, String str2, float f, int i, List<ImageFilter> list, Resizer resizer, boolean z2, boolean z3) {
        this(null, d, d2, region, z, str, str2, f, i, list, new FixedResizerFactory(resizer), z2, z3);
        validateScalingFactor();
    }

    public ThumbnailParameter(Dimension dimension, Region region, boolean z, String str, String str2, float f, int i, List<ImageFilter> list, ResizerFactory resizerFactory, boolean z2, boolean z3) {
        this(dimension, Double.NaN, Double.NaN, region, z, str, str2, f, i, list, resizerFactory, z2, z3);
        validateThumbnailSize();
    }

    public ThumbnailParameter(double d, double d2, Region region, boolean z, String str, String str2, float f, int i, List<ImageFilter> list, ResizerFactory resizerFactory, boolean z2, boolean z3) {
        this(null, d, d2, region, z, str, str2, f, i, list, resizerFactory, z2, z3);
        validateScalingFactor();
    }

    public Dimension getSize() {
        if (this.thumbnailSize != null) {
            return (Dimension) this.thumbnailSize.clone();
        }
        return null;
    }

    public double getWidthScalingFactor() {
        return this.widthScalingFactor;
    }

    public double getHeightScalingFactor() {
        return this.heightScalingFactor;
    }

    public int getType() {
        return this.imageType;
    }

    public boolean isKeepAspectRatio() {
        return this.keepAspectRatio;
    }

    public String getOutputFormat() {
        return this.outputFormat;
    }

    public String getOutputFormatType() {
        return this.outputFormatType;
    }

    public float getOutputQuality() {
        return this.outputQuality;
    }

    public List<ImageFilter> getImageFilters() {
        return this.filters;
    }

    public Resizer getResizer() {
        return this.resizerFactory.getResizer();
    }

    public ResizerFactory getResizerFactory() {
        return this.resizerFactory;
    }

    public boolean useOriginalImageType() {
        return this.imageType == -1;
    }

    public Region getSourceRegion() {
        return this.sourceRegion;
    }

    public boolean fitWithinDimenions() {
        return this.fitWithinDimensions;
    }

    public boolean useExifOrientation() {
        return this.useExifOrientation;
    }
}
