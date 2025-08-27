package net.coobird.thumbnailator.util.exif;

import net.coobird.thumbnailator.filters.Flip;
import net.coobird.thumbnailator.filters.ImageFilter;
import net.coobird.thumbnailator.filters.Pipeline;
import net.coobird.thumbnailator.filters.Rotation;

/* loaded from: thumbnailator-0.4.8.jar:net/coobird/thumbnailator/util/exif/ExifFilterUtils.class */
public final class ExifFilterUtils {
    private ExifFilterUtils() {
    }

    public static ImageFilter getFilterForOrientation(Orientation orientation) {
        Pipeline pipeline = new Pipeline();
        if (orientation == Orientation.TOP_RIGHT) {
            pipeline.add(Flip.HORIZONTAL);
        } else if (orientation == Orientation.BOTTOM_RIGHT) {
            pipeline.add(Rotation.ROTATE_180_DEGREES);
        } else if (orientation == Orientation.BOTTOM_LEFT) {
            pipeline.add(Rotation.ROTATE_180_DEGREES);
            pipeline.add(Flip.HORIZONTAL);
        } else if (orientation == Orientation.LEFT_TOP) {
            pipeline.add(Rotation.RIGHT_90_DEGREES);
            pipeline.add(Flip.HORIZONTAL);
        } else if (orientation == Orientation.RIGHT_TOP) {
            pipeline.add(Rotation.RIGHT_90_DEGREES);
        } else if (orientation == Orientation.RIGHT_BOTTOM) {
            pipeline.add(Rotation.LEFT_90_DEGREES);
            pipeline.add(Flip.HORIZONTAL);
        } else if (orientation == Orientation.LEFT_BOTTOM) {
            pipeline.add(Rotation.LEFT_90_DEGREES);
        }
        return pipeline;
    }
}
