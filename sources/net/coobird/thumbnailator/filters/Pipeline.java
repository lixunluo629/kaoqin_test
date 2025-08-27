package net.coobird.thumbnailator.filters;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import net.coobird.thumbnailator.util.BufferedImages;

/* loaded from: thumbnailator-0.4.8.jar:net/coobird/thumbnailator/filters/Pipeline.class */
public final class Pipeline implements ImageFilter {
    private final List<ImageFilter> filtersToApply;
    private final List<ImageFilter> unmodifiableFiltersToApply;

    public Pipeline() {
        this((List<ImageFilter>) Collections.emptyList());
    }

    public Pipeline(ImageFilter... imageFilterArr) {
        this((List<ImageFilter>) Arrays.asList(imageFilterArr));
    }

    public Pipeline(List<ImageFilter> list) {
        if (list == null) {
            throw new NullPointerException("Cannot instantiate with a nulllist of image filters.");
        }
        this.filtersToApply = new ArrayList(list);
        this.unmodifiableFiltersToApply = Collections.unmodifiableList(this.filtersToApply);
    }

    public void add(ImageFilter imageFilter) {
        if (imageFilter == null) {
            throw new NullPointerException("An image filter must not be null.");
        }
        this.filtersToApply.add(imageFilter);
    }

    public void addFirst(ImageFilter imageFilter) {
        if (imageFilter == null) {
            throw new NullPointerException("An image filter must not be null.");
        }
        this.filtersToApply.add(0, imageFilter);
    }

    public void addAll(List<ImageFilter> list) {
        if (list == null) {
            throw new NullPointerException("A list of image filters must not be null.");
        }
        this.filtersToApply.addAll(list);
    }

    public List<ImageFilter> getFilters() {
        return this.unmodifiableFiltersToApply;
    }

    @Override // net.coobird.thumbnailator.filters.ImageFilter
    public BufferedImage apply(BufferedImage bufferedImage) {
        if (this.filtersToApply.isEmpty()) {
            return bufferedImage;
        }
        BufferedImage bufferedImageCopy = BufferedImages.copy(bufferedImage);
        Iterator<ImageFilter> it = this.filtersToApply.iterator();
        while (it.hasNext()) {
            bufferedImageCopy = it.next().apply(bufferedImageCopy);
        }
        return bufferedImageCopy;
    }
}
