package net.coobird.thumbnailator;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import net.coobird.thumbnailator.filters.Canvas;
import net.coobird.thumbnailator.filters.ImageFilter;
import net.coobird.thumbnailator.filters.Pipeline;
import net.coobird.thumbnailator.filters.Rotation;
import net.coobird.thumbnailator.filters.Watermark;
import net.coobird.thumbnailator.geometry.AbsoluteSize;
import net.coobird.thumbnailator.geometry.Coordinate;
import net.coobird.thumbnailator.geometry.Position;
import net.coobird.thumbnailator.geometry.Positions;
import net.coobird.thumbnailator.geometry.Region;
import net.coobird.thumbnailator.geometry.Size;
import net.coobird.thumbnailator.name.Rename;
import net.coobird.thumbnailator.resizers.BicubicResizer;
import net.coobird.thumbnailator.resizers.BilinearResizer;
import net.coobird.thumbnailator.resizers.DefaultResizerFactory;
import net.coobird.thumbnailator.resizers.FixedResizerFactory;
import net.coobird.thumbnailator.resizers.ProgressiveBilinearResizer;
import net.coobird.thumbnailator.resizers.Resizer;
import net.coobird.thumbnailator.resizers.ResizerFactory;
import net.coobird.thumbnailator.resizers.configurations.AlphaInterpolation;
import net.coobird.thumbnailator.resizers.configurations.Antialiasing;
import net.coobird.thumbnailator.resizers.configurations.Dithering;
import net.coobird.thumbnailator.resizers.configurations.Rendering;
import net.coobird.thumbnailator.resizers.configurations.ScalingMode;
import net.coobird.thumbnailator.tasks.SourceSinkThumbnailTask;
import net.coobird.thumbnailator.tasks.io.BufferedImageSink;
import net.coobird.thumbnailator.tasks.io.BufferedImageSource;
import net.coobird.thumbnailator.tasks.io.FileImageSink;
import net.coobird.thumbnailator.tasks.io.FileImageSource;
import net.coobird.thumbnailator.tasks.io.ImageSource;
import net.coobird.thumbnailator.tasks.io.InputStreamImageSource;
import net.coobird.thumbnailator.tasks.io.OutputStreamImageSink;
import net.coobird.thumbnailator.tasks.io.URLImageSource;
import net.coobird.thumbnailator.util.ThumbnailatorUtils;
import org.springframework.web.servlet.tags.form.InputTag;

/* loaded from: thumbnailator-0.4.8.jar:net/coobird/thumbnailator/Thumbnails.class */
public final class Thumbnails {
    private Thumbnails() {
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void validateDimensions(int i, int i2) {
        if (i <= 0 && i2 <= 0) {
            throw new IllegalArgumentException("Destination image dimensions must not be less than 0 pixels.");
        }
        if (i <= 0 || i2 <= 0) {
            throw new IllegalArgumentException("Destination image " + (i == 0 ? "width" : "height") + " must not be less than or equal to 0 pixels.");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void checkForNull(Object obj, String str) {
        if (obj == null) {
            throw new NullPointerException(str);
        }
    }

    private static void checkForEmpty(Object[] objArr, String str) {
        if (objArr.length == 0) {
            throw new IllegalArgumentException(str);
        }
    }

    private static void checkForEmpty(Iterable<?> iterable, String str) {
        if (!iterable.iterator().hasNext()) {
            throw new IllegalArgumentException(str);
        }
    }

    public static Builder<File> of(String... strArr) {
        checkForNull(strArr, "Cannot specify null for input files.");
        checkForEmpty(strArr, "Cannot specify an empty array for input files.");
        return Builder.ofStrings(Arrays.asList(strArr));
    }

    public static Builder<File> of(File... fileArr) {
        checkForNull(fileArr, "Cannot specify null for input files.");
        checkForEmpty(fileArr, "Cannot specify an empty array for input files.");
        return Builder.ofFiles(Arrays.asList(fileArr));
    }

    public static Builder<URL> of(URL... urlArr) {
        checkForNull(urlArr, "Cannot specify null for input URLs.");
        checkForEmpty(urlArr, "Cannot specify an empty array for input URLs.");
        return Builder.ofUrls(Arrays.asList(urlArr));
    }

    public static Builder<? extends InputStream> of(InputStream... inputStreamArr) {
        checkForNull(inputStreamArr, "Cannot specify null for InputStreams.");
        checkForEmpty(inputStreamArr, "Cannot specify an empty array for InputStreams.");
        return Builder.ofInputStreams(Arrays.asList(inputStreamArr));
    }

    public static Builder<BufferedImage> of(BufferedImage... bufferedImageArr) {
        checkForNull(bufferedImageArr, "Cannot specify null for images.");
        checkForEmpty(bufferedImageArr, "Cannot specify an empty array for images.");
        return Builder.ofBufferedImages(Arrays.asList(bufferedImageArr));
    }

    public static Builder<File> fromFilenames(Iterable<String> iterable) {
        checkForNull(iterable, "Cannot specify null for input files.");
        checkForEmpty(iterable, "Cannot specify an empty collection for input files.");
        return Builder.ofStrings(iterable);
    }

    public static Builder<File> fromFiles(Iterable<File> iterable) {
        checkForNull(iterable, "Cannot specify null for input files.");
        checkForEmpty(iterable, "Cannot specify an empty collection for input files.");
        return Builder.ofFiles(iterable);
    }

    public static Builder<URL> fromURLs(Iterable<URL> iterable) {
        checkForNull(iterable, "Cannot specify null for input URLs.");
        checkForEmpty(iterable, "Cannot specify an empty collection for input URLs.");
        return Builder.ofUrls(iterable);
    }

    public static Builder<InputStream> fromInputStreams(Iterable<? extends InputStream> iterable) {
        checkForNull(iterable, "Cannot specify null for InputStreams.");
        checkForEmpty(iterable, "Cannot specify an empty collection for InputStreams.");
        return Builder.ofInputStreams(iterable);
    }

    public static Builder<BufferedImage> fromImages(Iterable<BufferedImage> iterable) {
        checkForNull(iterable, "Cannot specify null for images.");
        checkForEmpty((Iterable<?>) iterable, "Cannot specify an empty collection for images.");
        return Builder.ofBufferedImages(iterable);
    }

    /* loaded from: thumbnailator-0.4.8.jar:net/coobird/thumbnailator/Thumbnails$Builder.class */
    public static class Builder<T> {
        private final Iterable<ImageSource<T>> sources;
        private final Map<Properties, Status> statusMap = new HashMap();
        private static int IMAGE_TYPE_UNSPECIFIED = -1;
        private static final int DIMENSION_NOT_SPECIFIED = -1;
        private int width;
        private int height;
        private double scaleWidth;
        private double scaleHeight;
        private Region sourceRegion;
        private int imageType;
        private boolean keepAspectRatio;
        private String outputFormat;
        private String outputFormatType;
        private float outputQuality;
        private ScalingMode scalingMode;
        private AlphaInterpolation alphaInterpolation;
        private Dithering dithering;
        private Antialiasing antialiasing;
        private Rendering rendering;
        private ResizerFactory resizerFactory;
        private boolean allowOverwrite;
        private boolean fitWithinDimenions;
        private boolean useExifOrientation;
        private Position croppingPosition;
        private Pipeline filterPipeline;

        /* loaded from: thumbnailator-0.4.8.jar:net/coobird/thumbnailator/Thumbnails$Builder$Property.class */
        private interface Property {
            String getName();
        }

        /* loaded from: thumbnailator-0.4.8.jar:net/coobird/thumbnailator/Thumbnails$Builder$Status.class */
        private enum Status {
            OPTIONAL,
            READY,
            NOT_READY,
            ALREADY_SET,
            CANNOT_SET
        }

        private Builder(Iterable<ImageSource<T>> iterable) {
            this.statusMap.put(Properties.SIZE, Status.NOT_READY);
            this.statusMap.put(Properties.WIDTH, Status.OPTIONAL);
            this.statusMap.put(Properties.HEIGHT, Status.OPTIONAL);
            this.statusMap.put(Properties.SCALE, Status.NOT_READY);
            this.statusMap.put(Properties.SOURCE_REGION, Status.OPTIONAL);
            this.statusMap.put(Properties.IMAGE_TYPE, Status.OPTIONAL);
            this.statusMap.put(Properties.SCALING_MODE, Status.OPTIONAL);
            this.statusMap.put(Properties.ALPHA_INTERPOLATION, Status.OPTIONAL);
            this.statusMap.put(Properties.ANTIALIASING, Status.OPTIONAL);
            this.statusMap.put(Properties.DITHERING, Status.OPTIONAL);
            this.statusMap.put(Properties.RENDERING, Status.OPTIONAL);
            this.statusMap.put(Properties.KEEP_ASPECT_RATIO, Status.OPTIONAL);
            this.statusMap.put(Properties.OUTPUT_FORMAT, Status.OPTIONAL);
            this.statusMap.put(Properties.OUTPUT_FORMAT_TYPE, Status.OPTIONAL);
            this.statusMap.put(Properties.OUTPUT_QUALITY, Status.OPTIONAL);
            this.statusMap.put(Properties.RESIZER, Status.OPTIONAL);
            this.statusMap.put(Properties.RESIZER_FACTORY, Status.OPTIONAL);
            this.statusMap.put(Properties.ALLOW_OVERWRITE, Status.OPTIONAL);
            this.statusMap.put(Properties.CROP, Status.OPTIONAL);
            this.statusMap.put(Properties.USE_EXIF_ORIENTATION, Status.OPTIONAL);
            this.width = -1;
            this.height = -1;
            this.scaleWidth = Double.NaN;
            this.scaleHeight = Double.NaN;
            this.imageType = IMAGE_TYPE_UNSPECIFIED;
            this.keepAspectRatio = true;
            this.outputFormat = ThumbnailParameter.DETERMINE_FORMAT;
            this.outputFormatType = ThumbnailParameter.DEFAULT_FORMAT_TYPE;
            this.outputQuality = Float.NaN;
            this.scalingMode = ScalingMode.PROGRESSIVE_BILINEAR;
            this.alphaInterpolation = AlphaInterpolation.DEFAULT;
            this.dithering = Dithering.DEFAULT;
            this.antialiasing = Antialiasing.DEFAULT;
            this.rendering = Rendering.DEFAULT;
            this.resizerFactory = DefaultResizerFactory.getInstance();
            this.allowOverwrite = true;
            this.fitWithinDimenions = true;
            this.useExifOrientation = true;
            this.croppingPosition = null;
            this.filterPipeline = new Pipeline();
            this.sources = iterable;
            this.statusMap.put(Properties.OUTPUT_FORMAT, Status.OPTIONAL);
        }

        /* loaded from: thumbnailator-0.4.8.jar:net/coobird/thumbnailator/Thumbnails$Builder$StringImageSourceIterator.class */
        private static final class StringImageSourceIterator implements Iterable<ImageSource<File>> {
            private final Iterable<String> filenames;

            private StringImageSourceIterator(Iterable<String> iterable) {
                this.filenames = iterable;
            }

            @Override // java.lang.Iterable
            public Iterator<ImageSource<File>> iterator() {
                return new Iterator<ImageSource<File>>() { // from class: net.coobird.thumbnailator.Thumbnails.Builder.StringImageSourceIterator.1
                    Iterator<String> iter;

                    @Override // java.util.Iterator
                    public boolean hasNext() {
                        return this.iter.hasNext();
                    }

                    /* JADX WARN: Can't rename method to resolve collision */
                    @Override // java.util.Iterator
                    public ImageSource<File> next() {
                        return new FileImageSource(this.iter.next());
                    }

                    {
                        this.iter = StringImageSourceIterator.this.filenames.iterator();
                    }

                    @Override // java.util.Iterator
                    public void remove() {
                        throw new UnsupportedOperationException();
                    }
                };
            }
        }

        /* loaded from: thumbnailator-0.4.8.jar:net/coobird/thumbnailator/Thumbnails$Builder$FileImageSourceIterator.class */
        private static final class FileImageSourceIterator implements Iterable<ImageSource<File>> {
            private final Iterable<File> files;

            private FileImageSourceIterator(Iterable<File> iterable) {
                this.files = iterable;
            }

            @Override // java.lang.Iterable
            public Iterator<ImageSource<File>> iterator() {
                return new Iterator<ImageSource<File>>() { // from class: net.coobird.thumbnailator.Thumbnails.Builder.FileImageSourceIterator.1
                    Iterator<File> iter;

                    @Override // java.util.Iterator
                    public boolean hasNext() {
                        return this.iter.hasNext();
                    }

                    /* JADX WARN: Can't rename method to resolve collision */
                    @Override // java.util.Iterator
                    public ImageSource<File> next() {
                        return new FileImageSource(this.iter.next());
                    }

                    {
                        this.iter = FileImageSourceIterator.this.files.iterator();
                    }

                    @Override // java.util.Iterator
                    public void remove() {
                        throw new UnsupportedOperationException();
                    }
                };
            }
        }

        /* loaded from: thumbnailator-0.4.8.jar:net/coobird/thumbnailator/Thumbnails$Builder$URLImageSourceIterator.class */
        private static final class URLImageSourceIterator implements Iterable<ImageSource<URL>> {
            private final Iterable<URL> urls;

            private URLImageSourceIterator(Iterable<URL> iterable) {
                this.urls = iterable;
            }

            @Override // java.lang.Iterable
            public Iterator<ImageSource<URL>> iterator() {
                return new Iterator<ImageSource<URL>>() { // from class: net.coobird.thumbnailator.Thumbnails.Builder.URLImageSourceIterator.1
                    Iterator<URL> iter;

                    @Override // java.util.Iterator
                    public boolean hasNext() {
                        return this.iter.hasNext();
                    }

                    /* JADX WARN: Can't rename method to resolve collision */
                    @Override // java.util.Iterator
                    public ImageSource<URL> next() {
                        return new URLImageSource(this.iter.next());
                    }

                    {
                        this.iter = URLImageSourceIterator.this.urls.iterator();
                    }

                    @Override // java.util.Iterator
                    public void remove() {
                        throw new UnsupportedOperationException();
                    }
                };
            }
        }

        /* loaded from: thumbnailator-0.4.8.jar:net/coobird/thumbnailator/Thumbnails$Builder$InputStreamImageSourceIterator.class */
        private static final class InputStreamImageSourceIterator implements Iterable<ImageSource<InputStream>> {
            private final Iterable<? extends InputStream> inputStreams;

            private InputStreamImageSourceIterator(Iterable<? extends InputStream> iterable) {
                this.inputStreams = iterable;
            }

            @Override // java.lang.Iterable
            public Iterator<ImageSource<InputStream>> iterator() {
                return new Iterator<ImageSource<InputStream>>() { // from class: net.coobird.thumbnailator.Thumbnails.Builder.InputStreamImageSourceIterator.1
                    Iterator<? extends InputStream> iter;

                    @Override // java.util.Iterator
                    public boolean hasNext() {
                        return this.iter.hasNext();
                    }

                    /* JADX WARN: Can't rename method to resolve collision */
                    @Override // java.util.Iterator
                    public ImageSource<InputStream> next() {
                        return new InputStreamImageSource(this.iter.next());
                    }

                    {
                        this.iter = InputStreamImageSourceIterator.this.inputStreams.iterator();
                    }

                    @Override // java.util.Iterator
                    public void remove() {
                        throw new UnsupportedOperationException();
                    }
                };
            }
        }

        /* loaded from: thumbnailator-0.4.8.jar:net/coobird/thumbnailator/Thumbnails$Builder$BufferedImageImageSourceIterator.class */
        private static final class BufferedImageImageSourceIterator implements Iterable<ImageSource<BufferedImage>> {
            private final Iterable<BufferedImage> image;

            private BufferedImageImageSourceIterator(Iterable<BufferedImage> iterable) {
                this.image = iterable;
            }

            @Override // java.lang.Iterable
            public Iterator<ImageSource<BufferedImage>> iterator() {
                return new Iterator<ImageSource<BufferedImage>>() { // from class: net.coobird.thumbnailator.Thumbnails.Builder.BufferedImageImageSourceIterator.1
                    Iterator<BufferedImage> iter;

                    @Override // java.util.Iterator
                    public boolean hasNext() {
                        return this.iter.hasNext();
                    }

                    /* JADX WARN: Can't rename method to resolve collision */
                    @Override // java.util.Iterator
                    public ImageSource<BufferedImage> next() {
                        return new BufferedImageSource(this.iter.next());
                    }

                    {
                        this.iter = BufferedImageImageSourceIterator.this.image.iterator();
                    }

                    @Override // java.util.Iterator
                    public void remove() {
                        throw new UnsupportedOperationException();
                    }
                };
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static Builder<File> ofStrings(Iterable<String> iterable) {
            return new Builder<>(new StringImageSourceIterator(iterable));
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static Builder<File> ofFiles(Iterable<File> iterable) {
            return new Builder<>(new FileImageSourceIterator(iterable));
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static Builder<URL> ofUrls(Iterable<URL> iterable) {
            return new Builder<>(new URLImageSourceIterator(iterable));
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static Builder<InputStream> ofInputStreams(Iterable<? extends InputStream> iterable) {
            return new Builder<>(new InputStreamImageSourceIterator(iterable));
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static Builder<BufferedImage> ofBufferedImages(Iterable<BufferedImage> iterable) {
            return new Builder<>(new BufferedImageImageSourceIterator(iterable));
        }

        /* loaded from: thumbnailator-0.4.8.jar:net/coobird/thumbnailator/Thumbnails$Builder$BufferedImageIterable.class */
        private final class BufferedImageIterable implements Iterable<BufferedImage> {
            private BufferedImageIterable() {
            }

            @Override // java.lang.Iterable
            public Iterator<BufferedImage> iterator() {
                return new Iterator<BufferedImage>() { // from class: net.coobird.thumbnailator.Thumbnails.Builder.BufferedImageIterable.1
                    Iterator<ImageSource<T>> sourceIter;

                    @Override // java.util.Iterator
                    public boolean hasNext() {
                        return this.sourceIter.hasNext();
                    }

                    /* JADX WARN: Can't rename method to resolve collision */
                    @Override // java.util.Iterator
                    public BufferedImage next() {
                        ImageSource<T> next = this.sourceIter.next();
                        BufferedImageSink bufferedImageSink = new BufferedImageSink();
                        try {
                            Thumbnailator.createThumbnail(new SourceSinkThumbnailTask(Builder.this.makeParam(), next, bufferedImageSink));
                            return bufferedImageSink.getSink();
                        } catch (IOException e) {
                            return null;
                        }
                    }

                    {
                        this.sourceIter = Builder.this.sources.iterator();
                    }

                    @Override // java.util.Iterator
                    public void remove() {
                        throw new UnsupportedOperationException("Cannot remove elements from this iterator.");
                    }
                };
            }
        }

        /* loaded from: thumbnailator-0.4.8.jar:net/coobird/thumbnailator/Thumbnails$Builder$Properties.class */
        private enum Properties implements Property {
            SIZE(InputTag.SIZE_ATTRIBUTE),
            WIDTH("width"),
            HEIGHT("height"),
            SCALE("scale"),
            IMAGE_TYPE("imageType"),
            SCALING_MODE("scalingMode"),
            ALPHA_INTERPOLATION("alphaInterpolation"),
            ANTIALIASING("antialiasing"),
            DITHERING("dithering"),
            RENDERING("rendering"),
            KEEP_ASPECT_RATIO("keepAspectRatio"),
            OUTPUT_FORMAT("outputFormat"),
            OUTPUT_FORMAT_TYPE("outputFormatType"),
            OUTPUT_QUALITY("outputQuality"),
            RESIZER("resizer"),
            SOURCE_REGION("sourceRegion"),
            RESIZER_FACTORY("resizerFactory"),
            ALLOW_OVERWRITE("allowOverwrite"),
            CROP("crop"),
            USE_EXIF_ORIENTATION("useExifOrientation");

            private final String name;

            Properties(String str) {
                this.name = str;
            }

            @Override // net.coobird.thumbnailator.Thumbnails.Builder.Property
            public String getName() {
                return this.name;
            }
        }

        private void updateStatus(Properties properties, Status status) {
            if (this.statusMap.get(properties) == Status.ALREADY_SET) {
                throw new IllegalStateException(properties.getName() + " is already set.");
            }
            if (status != Status.CANNOT_SET && this.statusMap.get(properties) == Status.CANNOT_SET) {
                throw new IllegalStateException(properties.getName() + " cannot be set.");
            }
            this.statusMap.put(properties, status);
        }

        public Builder<T> size(int i, int i2) {
            updateStatus(Properties.SIZE, Status.ALREADY_SET);
            updateStatus(Properties.SCALE, Status.CANNOT_SET);
            Thumbnails.validateDimensions(i, i2);
            this.width = i;
            this.height = i2;
            return this;
        }

        public Builder<T> width(int i) {
            if (this.statusMap.get(Properties.SIZE) != Status.CANNOT_SET) {
                updateStatus(Properties.SIZE, Status.CANNOT_SET);
            }
            if (this.statusMap.get(Properties.SCALE) != Status.CANNOT_SET) {
                updateStatus(Properties.SCALE, Status.CANNOT_SET);
            }
            updateStatus(Properties.WIDTH, Status.ALREADY_SET);
            Thumbnails.validateDimensions(i, Integer.MAX_VALUE);
            this.width = i;
            return this;
        }

        public Builder<T> height(int i) {
            if (this.statusMap.get(Properties.SIZE) != Status.CANNOT_SET) {
                updateStatus(Properties.SIZE, Status.CANNOT_SET);
            }
            if (this.statusMap.get(Properties.SCALE) != Status.CANNOT_SET) {
                updateStatus(Properties.SCALE, Status.CANNOT_SET);
            }
            updateStatus(Properties.HEIGHT, Status.ALREADY_SET);
            Thumbnails.validateDimensions(Integer.MAX_VALUE, i);
            this.height = i;
            return this;
        }

        public Builder<T> forceSize(int i, int i2) {
            updateStatus(Properties.SIZE, Status.ALREADY_SET);
            updateStatus(Properties.KEEP_ASPECT_RATIO, Status.ALREADY_SET);
            updateStatus(Properties.SCALE, Status.CANNOT_SET);
            Thumbnails.validateDimensions(i, i2);
            this.width = i;
            this.height = i2;
            this.keepAspectRatio = false;
            return this;
        }

        public Builder<T> scale(double d) {
            return scale(d, d);
        }

        public Builder<T> scale(double d, double d2) {
            updateStatus(Properties.SCALE, Status.ALREADY_SET);
            updateStatus(Properties.SIZE, Status.CANNOT_SET);
            updateStatus(Properties.KEEP_ASPECT_RATIO, Status.CANNOT_SET);
            if (d <= 0.0d || d2 <= 0.0d) {
                throw new IllegalArgumentException("The scaling factor is equal to or less than 0.");
            }
            if (Double.isNaN(d) || Double.isNaN(d2)) {
                throw new IllegalArgumentException("The scaling factor is not a number.");
            }
            if (Double.isInfinite(d) || Double.isInfinite(d2)) {
                throw new IllegalArgumentException("The scaling factor cannot be infinity.");
            }
            this.scaleWidth = d;
            this.scaleHeight = d2;
            return this;
        }

        public Builder<T> sourceRegion(Region region) {
            if (region == null) {
                throw new NullPointerException("Region cannot be null.");
            }
            updateStatus(Properties.SOURCE_REGION, Status.ALREADY_SET);
            this.sourceRegion = region;
            return this;
        }

        public Builder<T> sourceRegion(Position position, Size size) {
            if (position == null) {
                throw new NullPointerException("Position cannot be null.");
            }
            if (size == null) {
                throw new NullPointerException("Size cannot be null.");
            }
            return sourceRegion(new Region(position, size));
        }

        public Builder<T> sourceRegion(int i, int i2, int i3, int i4) {
            if (i3 <= 0 || i4 <= 0) {
                throw new IllegalArgumentException("Width and height must be greater than 0.");
            }
            return sourceRegion(new Coordinate(i, i2), new AbsoluteSize(i3, i4));
        }

        public Builder<T> sourceRegion(Position position, int i, int i2) {
            if (position == null) {
                throw new NullPointerException("Position cannot be null.");
            }
            if (i <= 0 || i2 <= 0) {
                throw new IllegalArgumentException("Width and height must be greater than 0.");
            }
            return sourceRegion(position, new AbsoluteSize(i, i2));
        }

        public Builder<T> sourceRegion(Rectangle rectangle) {
            if (rectangle == null) {
                throw new NullPointerException("Region cannot be null.");
            }
            return sourceRegion(new Coordinate(rectangle.x, rectangle.y), new AbsoluteSize(rectangle.getSize()));
        }

        public Builder<T> crop(Position position) {
            Thumbnails.checkForNull(position, "Position cannot be null.");
            updateStatus(Properties.CROP, Status.ALREADY_SET);
            updateStatus(Properties.SCALE, Status.CANNOT_SET);
            this.croppingPosition = position;
            this.fitWithinDimenions = false;
            return this;
        }

        public Builder<T> allowOverwrite(boolean z) {
            updateStatus(Properties.ALLOW_OVERWRITE, Status.ALREADY_SET);
            this.allowOverwrite = z;
            return this;
        }

        public Builder<T> imageType(int i) {
            updateStatus(Properties.IMAGE_TYPE, Status.ALREADY_SET);
            this.imageType = i;
            return this;
        }

        public Builder<T> scalingMode(ScalingMode scalingMode) {
            Thumbnails.checkForNull(scalingMode, "Scaling mode is null.");
            updateStatus(Properties.SCALING_MODE, Status.ALREADY_SET);
            updateStatus(Properties.RESIZER, Status.CANNOT_SET);
            updateStatus(Properties.RESIZER_FACTORY, Status.CANNOT_SET);
            this.scalingMode = scalingMode;
            return this;
        }

        public Builder<T> resizer(Resizer resizer) {
            Thumbnails.checkForNull(resizer, "Resizer is null.");
            updateStatus(Properties.RESIZER, Status.ALREADY_SET);
            updateStatus(Properties.RESIZER_FACTORY, Status.CANNOT_SET);
            updateStatus(Properties.SCALING_MODE, Status.CANNOT_SET);
            this.resizerFactory = new FixedResizerFactory(resizer);
            return this;
        }

        public Builder<T> resizerFactory(ResizerFactory resizerFactory) {
            Thumbnails.checkForNull(resizerFactory, "ResizerFactory is null.");
            updateStatus(Properties.RESIZER_FACTORY, Status.ALREADY_SET);
            updateStatus(Properties.RESIZER, Status.CANNOT_SET);
            updateStatus(Properties.SCALING_MODE, Status.CANNOT_SET);
            updateStatus(Properties.ALPHA_INTERPOLATION, Status.CANNOT_SET);
            updateStatus(Properties.DITHERING, Status.CANNOT_SET);
            updateStatus(Properties.ANTIALIASING, Status.CANNOT_SET);
            updateStatus(Properties.RENDERING, Status.CANNOT_SET);
            this.resizerFactory = resizerFactory;
            return this;
        }

        public Builder<T> alphaInterpolation(AlphaInterpolation alphaInterpolation) {
            Thumbnails.checkForNull(alphaInterpolation, "Alpha interpolation is null.");
            updateStatus(Properties.RESIZER_FACTORY, Status.CANNOT_SET);
            updateStatus(Properties.ALPHA_INTERPOLATION, Status.ALREADY_SET);
            this.alphaInterpolation = alphaInterpolation;
            return this;
        }

        public Builder<T> dithering(Dithering dithering) {
            Thumbnails.checkForNull(dithering, "Dithering is null.");
            updateStatus(Properties.RESIZER_FACTORY, Status.CANNOT_SET);
            updateStatus(Properties.DITHERING, Status.ALREADY_SET);
            this.dithering = dithering;
            return this;
        }

        public Builder<T> antialiasing(Antialiasing antialiasing) {
            Thumbnails.checkForNull(antialiasing, "Antialiasing is null.");
            updateStatus(Properties.RESIZER_FACTORY, Status.CANNOT_SET);
            updateStatus(Properties.ANTIALIASING, Status.ALREADY_SET);
            this.antialiasing = antialiasing;
            return this;
        }

        public Builder<T> rendering(Rendering rendering) {
            Thumbnails.checkForNull(rendering, "Rendering is null.");
            updateStatus(Properties.RESIZER_FACTORY, Status.CANNOT_SET);
            updateStatus(Properties.RENDERING, Status.ALREADY_SET);
            this.rendering = rendering;
            return this;
        }

        public Builder<T> keepAspectRatio(boolean z) {
            if (this.statusMap.get(Properties.SCALE) == Status.ALREADY_SET) {
                throw new IllegalStateException("Cannot specify whether to keep the aspect ratio if the scaling factor has already been specified.");
            }
            if (this.statusMap.get(Properties.SIZE) == Status.NOT_READY) {
                throw new IllegalStateException("Cannot specify whether to keep the aspect ratio unless the size parameter has already been specified.");
            }
            if ((this.statusMap.get(Properties.WIDTH) == Status.ALREADY_SET || this.statusMap.get(Properties.HEIGHT) == Status.ALREADY_SET) && !z) {
                throw new IllegalStateException("The aspect ratio must be preserved when the width and/or height parameter has already been specified.");
            }
            updateStatus(Properties.KEEP_ASPECT_RATIO, Status.ALREADY_SET);
            this.keepAspectRatio = z;
            return this;
        }

        public Builder<T> outputQuality(float f) {
            if (f < 0.0f || f > 1.0f) {
                throw new IllegalArgumentException("The quality setting must be in the range 0.0f and 1.0f, inclusive.");
            }
            updateStatus(Properties.OUTPUT_QUALITY, Status.ALREADY_SET);
            this.outputQuality = f;
            return this;
        }

        public Builder<T> outputQuality(double d) {
            if (d < 0.0d || d > 1.0d) {
                throw new IllegalArgumentException("The quality setting must be in the range 0.0d and 1.0d, inclusive.");
            }
            updateStatus(Properties.OUTPUT_QUALITY, Status.ALREADY_SET);
            this.outputQuality = (float) d;
            if (this.outputQuality < 0.0f) {
                this.outputQuality = 0.0f;
            } else if (this.outputQuality > 1.0f) {
                this.outputQuality = 1.0f;
            }
            return this;
        }

        public Builder<T> outputFormat(String str) {
            if (!ThumbnailatorUtils.isSupportedOutputFormat(str)) {
                throw new IllegalArgumentException("Specified format is not supported: " + str);
            }
            updateStatus(Properties.OUTPUT_FORMAT, Status.ALREADY_SET);
            this.outputFormat = str;
            return this;
        }

        public Builder<T> useOriginalFormat() {
            updateStatus(Properties.OUTPUT_FORMAT, Status.ALREADY_SET);
            this.outputFormat = ThumbnailParameter.ORIGINAL_FORMAT;
            return this;
        }

        public Builder<T> useExifOrientation(boolean z) {
            updateStatus(Properties.USE_EXIF_ORIENTATION, Status.ALREADY_SET);
            this.useExifOrientation = z;
            return this;
        }

        public Builder<T> determineOutputFormat() {
            updateStatus(Properties.OUTPUT_FORMAT, Status.ALREADY_SET);
            this.outputFormat = ThumbnailParameter.DETERMINE_FORMAT;
            return this;
        }

        private boolean isOutputFormatNotSet() {
            return this.outputFormat == null || ThumbnailParameter.DETERMINE_FORMAT.equals(this.outputFormat);
        }

        public Builder<T> outputFormatType(String str) {
            if (str != ThumbnailParameter.DEFAULT_FORMAT_TYPE && isOutputFormatNotSet()) {
                throw new IllegalArgumentException("Cannot set the format type if a specific output format has not been specified.");
            }
            if (!ThumbnailatorUtils.isSupportedOutputFormatType(this.outputFormat, str)) {
                throw new IllegalArgumentException("Specified format type (" + str + ") is not  supported for the format: " + this.outputFormat);
            }
            updateStatus(Properties.OUTPUT_FORMAT_TYPE, Status.ALREADY_SET);
            if (!this.statusMap.containsKey(Properties.OUTPUT_FORMAT)) {
                updateStatus(Properties.OUTPUT_FORMAT, Status.CANNOT_SET);
            }
            this.outputFormatType = str;
            return this;
        }

        public Builder<T> watermark(Watermark watermark) {
            if (watermark == null) {
                throw new NullPointerException("Watermark is null.");
            }
            this.filterPipeline.add(watermark);
            return this;
        }

        public Builder<T> watermark(BufferedImage bufferedImage) {
            return watermark(Positions.CENTER, bufferedImage, 0.5f);
        }

        public Builder<T> watermark(BufferedImage bufferedImage, float f) {
            return watermark(Positions.CENTER, bufferedImage, f);
        }

        public Builder<T> watermark(Position position, BufferedImage bufferedImage, float f) {
            this.filterPipeline.add(new Watermark(position, bufferedImage, f));
            return this;
        }

        public Builder<T> rotate(double d) {
            this.filterPipeline.add(Rotation.newRotator(d));
            return this;
        }

        public Builder<T> addFilter(ImageFilter imageFilter) {
            if (imageFilter == null) {
                throw new NullPointerException("Filter is null.");
            }
            this.filterPipeline.add(imageFilter);
            return this;
        }

        public Builder<T> addFilters(List<ImageFilter> list) {
            if (list == null) {
                throw new NullPointerException("Filters is null.");
            }
            this.filterPipeline.addAll(list);
            return this;
        }

        private void checkReadiness() {
            for (Map.Entry<Properties, Status> entry : this.statusMap.entrySet()) {
                if (entry.getValue() == Status.NOT_READY) {
                    throw new IllegalStateException(entry.getKey().getName() + " is not set.");
                }
            }
        }

        private Resizer makeResizer(ScalingMode scalingMode) {
            HashMap map = new HashMap();
            map.put(RenderingHints.KEY_ALPHA_INTERPOLATION, this.alphaInterpolation.getValue());
            map.put(RenderingHints.KEY_DITHERING, this.dithering.getValue());
            map.put(RenderingHints.KEY_ANTIALIASING, this.antialiasing.getValue());
            map.put(RenderingHints.KEY_RENDERING, this.rendering.getValue());
            if (scalingMode == ScalingMode.BILINEAR) {
                return new BilinearResizer(map);
            }
            if (scalingMode == ScalingMode.BICUBIC) {
                return new BicubicResizer(map);
            }
            if (scalingMode == ScalingMode.PROGRESSIVE_BILINEAR) {
                return new ProgressiveBilinearResizer(map);
            }
            return new ProgressiveBilinearResizer(map);
        }

        private void prepareResizerFactory() {
            if (this.statusMap.get(Properties.SCALING_MODE) == Status.ALREADY_SET) {
                this.resizerFactory = new FixedResizerFactory(makeResizer(this.scalingMode));
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public ThumbnailParameter makeParam() {
            prepareResizerFactory();
            int i = this.imageType;
            if (this.imageType == IMAGE_TYPE_UNSPECIFIED) {
                i = -1;
            }
            if (this.croppingPosition != null) {
                this.filterPipeline.addFirst(new Canvas(this.width, this.height, this.croppingPosition));
            }
            if (Double.isNaN(this.scaleWidth)) {
                if (this.width == -1 && this.height == -1) {
                    throw new IllegalStateException("The width or height must be specified. If this exception is thrown, it is due to a bug in the Thumbnailator library.");
                }
                if (this.width == -1) {
                    this.width = Integer.MAX_VALUE;
                }
                if (this.height == -1) {
                    this.height = Integer.MAX_VALUE;
                }
                return new ThumbnailParameter(new Dimension(this.width, this.height), this.sourceRegion, this.keepAspectRatio, this.outputFormat, this.outputFormatType, this.outputQuality, i, this.filterPipeline.getFilters(), this.resizerFactory, this.fitWithinDimenions, this.useExifOrientation);
            }
            return new ThumbnailParameter(this.scaleWidth, this.scaleHeight, this.sourceRegion, this.keepAspectRatio, this.outputFormat, this.outputFormatType, this.outputQuality, i, this.filterPipeline.getFilters(), this.resizerFactory, this.fitWithinDimenions, this.useExifOrientation);
        }

        public Iterable<BufferedImage> iterableBufferedImages() {
            checkReadiness();
            return new BufferedImageIterable();
        }

        public List<BufferedImage> asBufferedImages() throws IOException {
            checkReadiness();
            ArrayList arrayList = new ArrayList();
            for (ImageSource<T> imageSource : this.sources) {
                BufferedImageSink bufferedImageSink = new BufferedImageSink();
                Thumbnailator.createThumbnail(new SourceSinkThumbnailTask(makeParam(), imageSource, bufferedImageSink));
                arrayList.add(bufferedImageSink.getSink());
            }
            return arrayList;
        }

        public BufferedImage asBufferedImage() throws IOException {
            checkReadiness();
            Iterator<ImageSource<T>> it = this.sources.iterator();
            ImageSource<T> next = it.next();
            if (it.hasNext()) {
                throw new IllegalArgumentException("Cannot create one thumbnail from multiple original images.");
            }
            BufferedImageSink bufferedImageSink = new BufferedImageSink();
            Thumbnailator.createThumbnail(new SourceSinkThumbnailTask(makeParam(), next, bufferedImageSink));
            return bufferedImageSink.getSink();
        }

        public List<File> asFiles(Iterable<File> iterable) throws IOException {
            checkReadiness();
            if (iterable == null) {
                throw new NullPointerException("File name iterable is null.");
            }
            ArrayList arrayList = new ArrayList();
            Iterator<File> it = iterable.iterator();
            for (ImageSource<T> imageSource : this.sources) {
                if (!it.hasNext()) {
                    throw new IndexOutOfBoundsException("Not enough file names provided by iterator.");
                }
                ThumbnailParameter thumbnailParameterMakeParam = makeParam();
                FileImageSink fileImageSink = new FileImageSink(it.next(), this.allowOverwrite);
                try {
                    Thumbnailator.createThumbnail(new SourceSinkThumbnailTask(thumbnailParameterMakeParam, imageSource, fileImageSink));
                    arrayList.add(fileImageSink.getSink());
                } catch (IllegalArgumentException e) {
                }
            }
            return arrayList;
        }

        public void toFiles(Iterable<File> iterable) throws IOException {
            asFiles(iterable);
        }

        public List<File> asFiles(Rename rename) throws IOException {
            return asFiles(null, rename);
        }

        public List<File> asFiles(File file, Rename rename) throws IOException {
            checkReadiness();
            if (rename == null) {
                throw new NullPointerException("Rename is null.");
            }
            if (file != null && !file.isDirectory()) {
                throw new IllegalArgumentException("Given destination is not a directory.");
            }
            ArrayList arrayList = new ArrayList();
            for (ImageSource<T> imageSource : this.sources) {
                if (!(imageSource instanceof FileImageSource)) {
                    throw new IllegalStateException("Cannot create thumbnails to files if original images are not from files.");
                }
                ThumbnailParameter thumbnailParameterMakeParam = makeParam();
                File source = ((FileImageSource) imageSource).getSource();
                FileImageSink fileImageSink = new FileImageSink(new File(file == null ? source.getParentFile() : file, rename.apply(source.getName(), thumbnailParameterMakeParam)), this.allowOverwrite);
                try {
                    Thumbnailator.createThumbnail(new SourceSinkThumbnailTask(thumbnailParameterMakeParam, imageSource, fileImageSink));
                    arrayList.add(fileImageSink.getSink());
                } catch (IllegalArgumentException e) {
                }
            }
            return arrayList;
        }

        public void toFiles(Rename rename) throws IOException {
            toFiles(null, rename);
        }

        public void toFiles(File file, Rename rename) throws IOException {
            asFiles(file, rename);
        }

        public void toFile(File file) throws IOException {
            checkReadiness();
            Iterator<ImageSource<T>> it = this.sources.iterator();
            ImageSource<T> next = it.next();
            if (it.hasNext()) {
                throw new IllegalArgumentException("Cannot output multiple thumbnails to one file.");
            }
            Thumbnailator.createThumbnail(new SourceSinkThumbnailTask(makeParam(), next, new FileImageSink(file, this.allowOverwrite)));
        }

        public void toFile(String str) throws IOException {
            checkReadiness();
            Iterator<ImageSource<T>> it = this.sources.iterator();
            ImageSource<T> next = it.next();
            if (it.hasNext()) {
                throw new IllegalArgumentException("Cannot output multiple thumbnails to one file.");
            }
            Thumbnailator.createThumbnail(new SourceSinkThumbnailTask(makeParam(), next, new FileImageSink(str, this.allowOverwrite)));
        }

        public void toOutputStream(OutputStream outputStream) throws IOException {
            checkReadiness();
            Iterator<ImageSource<T>> it = this.sources.iterator();
            ImageSource<T> next = it.next();
            if (it.hasNext()) {
                throw new IllegalArgumentException("Cannot output multiple thumbnails to a single OutputStream.");
            }
            if ((next instanceof BufferedImageSource) && isOutputFormatNotSet()) {
                throw new IllegalStateException("Output format not specified.");
            }
            Thumbnailator.createThumbnail(new SourceSinkThumbnailTask(makeParam(), next, new OutputStreamImageSink(outputStream)));
        }

        public void toOutputStreams(Iterable<? extends OutputStream> iterable) throws IOException {
            checkReadiness();
            if (iterable == null) {
                throw new NullPointerException("OutputStream iterable is null.");
            }
            Iterator<? extends OutputStream> it = iterable.iterator();
            for (ImageSource<T> imageSource : this.sources) {
                if ((imageSource instanceof BufferedImageSource) && isOutputFormatNotSet()) {
                    throw new IllegalStateException("Output format not specified.");
                }
                if (!it.hasNext()) {
                    throw new IndexOutOfBoundsException("Not enough file names provided by iterator.");
                }
                Thumbnailator.createThumbnail(new SourceSinkThumbnailTask(makeParam(), imageSource, new OutputStreamImageSink(it.next())));
            }
        }
    }
}
