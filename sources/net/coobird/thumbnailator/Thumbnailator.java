package net.coobird.thumbnailator;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import net.coobird.thumbnailator.builders.BufferedImageBuilder;
import net.coobird.thumbnailator.builders.ThumbnailParameterBuilder;
import net.coobird.thumbnailator.filters.ImageFilter;
import net.coobird.thumbnailator.makers.FixedSizeThumbnailMaker;
import net.coobird.thumbnailator.makers.ScaledThumbnailMaker;
import net.coobird.thumbnailator.name.Rename;
import net.coobird.thumbnailator.resizers.DefaultResizerFactory;
import net.coobird.thumbnailator.tasks.ThumbnailTask;

/* loaded from: thumbnailator-0.4.8.jar:net/coobird/thumbnailator/Thumbnailator.class */
public final class Thumbnailator {
    private Thumbnailator() {
    }

    public static void createThumbnail(ThumbnailTask<?, ?> thumbnailTask) throws IOException {
        BufferedImage bufferedImageMake;
        ThumbnailParameter param = thumbnailTask.getParam();
        BufferedImage bufferedImage = thumbnailTask.read();
        int type = param.getType();
        if (param.useOriginalImageType()) {
            if (bufferedImage.getType() == 0) {
                type = 2;
            } else {
                type = bufferedImage.getType();
            }
        }
        if (param.getSize() != null) {
            bufferedImageMake = new FixedSizeThumbnailMaker().size(param.getSize().width, param.getSize().height).keepAspectRatio(param.isKeepAspectRatio()).fitWithinDimensions(param.fitWithinDimenions()).imageType(type).resizerFactory(param.getResizerFactory()).make(bufferedImage);
        } else if (!Double.isNaN(param.getWidthScalingFactor())) {
            bufferedImageMake = new ScaledThumbnailMaker().scale(param.getWidthScalingFactor(), param.getHeightScalingFactor()).imageType(type).resizerFactory(param.getResizerFactory()).make(bufferedImage);
        } else {
            throw new IllegalStateException("Parameters to make thumbnail does not have scaling factor nor thumbnail size specified.");
        }
        Iterator<ImageFilter> it = param.getImageFilters().iterator();
        while (it.hasNext()) {
            bufferedImageMake = it.next().apply(bufferedImageMake);
        }
        thumbnailTask.write(bufferedImageMake);
        bufferedImage.flush();
        bufferedImageMake.flush();
    }

    public static BufferedImage createThumbnail(BufferedImage bufferedImage, int i, int i2) {
        validateDimensions(i, i2);
        return new FixedSizeThumbnailMaker(i, i2, true, true).resizer(DefaultResizerFactory.getInstance().getResizer(new Dimension(bufferedImage.getWidth(), bufferedImage.getHeight()), new Dimension(i, i2))).make(bufferedImage);
    }

    public static void createThumbnail(File file, File file2, int i, int i2) throws IOException {
        validateDimensions(i, i2);
        if (file == null) {
            throw new NullPointerException("Input file is null.");
        }
        if (file2 == null) {
            throw new NullPointerException("Output file is null.");
        }
        if (!file.exists()) {
            throw new IOException("Input file does not exist.");
        }
        Thumbnails.of(file).size(i, i2).toFile(file2);
    }

    public static BufferedImage createThumbnail(File file, int i, int i2) throws IOException {
        validateDimensions(i, i2);
        if (file == null) {
            throw new NullPointerException("Input file is null.");
        }
        return Thumbnails.of(file).size(i, i2).asBufferedImage();
    }

    public static Image createThumbnail(Image image, int i, int i2) {
        validateDimensions(i, i2);
        BufferedImage bufferedImageBuild = new BufferedImageBuilder(image.getWidth((ImageObserver) null), image.getHeight((ImageObserver) null)).build();
        Graphics2D graphics2DCreateGraphics = bufferedImageBuild.createGraphics();
        graphics2DCreateGraphics.drawImage(image, i, i2, (ImageObserver) null);
        graphics2DCreateGraphics.dispose();
        return createThumbnail(bufferedImageBuild, i, i2);
    }

    public static void createThumbnail(InputStream inputStream, OutputStream outputStream, int i, int i2) throws IOException {
        createThumbnail(inputStream, outputStream, ThumbnailParameter.ORIGINAL_FORMAT, i, i2);
    }

    public static void createThumbnail(InputStream inputStream, OutputStream outputStream, String str, int i, int i2) throws IOException {
        validateDimensions(i, i2);
        if (inputStream == null) {
            throw new NullPointerException("InputStream is null.");
        }
        if (outputStream == null) {
            throw new NullPointerException("OutputStream is null.");
        }
        Thumbnails.of(inputStream).size(i, i2).outputFormat(str).toOutputStream(outputStream);
    }

    public static Collection<File> createThumbnailsAsCollection(Collection<? extends File> collection, Rename rename, int i, int i2) throws IOException {
        validateDimensions(i, i2);
        if (collection == null) {
            throw new NullPointerException("Collection of Files is null.");
        }
        if (rename == null) {
            throw new NullPointerException("Rename is null.");
        }
        ArrayList arrayList = new ArrayList();
        ThumbnailParameter thumbnailParameterBuild = new ThumbnailParameterBuilder().size(i, i2).build();
        for (File file : collection) {
            File file2 = new File(file.getParent(), rename.apply(file.getName(), thumbnailParameterBuild));
            createThumbnail(file, file2, i, i2);
            arrayList.add(file2);
        }
        return Collections.unmodifiableList(arrayList);
    }

    public static void createThumbnails(Collection<? extends File> collection, Rename rename, int i, int i2) throws IOException {
        validateDimensions(i, i2);
        if (collection == null) {
            throw new NullPointerException("Collection of Files is null.");
        }
        if (rename == null) {
            throw new NullPointerException("Rename is null.");
        }
        ThumbnailParameter thumbnailParameterBuild = new ThumbnailParameterBuilder().size(i, i2).build();
        for (File file : collection) {
            createThumbnail(file, new File(file.getParent(), rename.apply(file.getName(), thumbnailParameterBuild)), i, i2);
        }
    }

    private static void validateDimensions(int i, int i2) {
        if (i <= 0 && i2 <= 0) {
            throw new IllegalArgumentException("Destination image dimensions must not be less than 0 pixels.");
        }
        if (i <= 0 || i2 <= 0) {
            throw new IllegalArgumentException("Destination image " + (i == 0 ? "width" : "height") + " must not be less than or equal to 0 pixels.");
        }
    }
}
