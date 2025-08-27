package net.coobird.thumbnailator.tasks.io;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.Iterator;
import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import net.coobird.thumbnailator.tasks.UnsupportedFormatException;
import net.coobird.thumbnailator.util.exif.ExifFilterUtils;
import net.coobird.thumbnailator.util.exif.ExifUtils;
import net.coobird.thumbnailator.util.exif.Orientation;

/* loaded from: thumbnailator-0.4.8.jar:net/coobird/thumbnailator/tasks/io/InputStreamImageSource.class */
public class InputStreamImageSource extends AbstractImageSource<InputStream> {
    private static final int FIRST_IMAGE_INDEX = 0;
    private final InputStream is;

    public InputStreamImageSource(InputStream inputStream) {
        if (inputStream == null) {
            throw new NullPointerException("InputStream cannot be null.");
        }
        this.is = inputStream;
    }

    @Override // net.coobird.thumbnailator.tasks.io.ImageSource
    public BufferedImage read() throws IllegalAccessException, NoSuchFieldException, IOException, IllegalArgumentException {
        Orientation exifOrientation;
        ImageInputStream imageInputStreamCreateImageInputStream = ImageIO.createImageInputStream(this.is);
        if (imageInputStreamCreateImageInputStream == null) {
            throw new IOException("Could not open InputStream.");
        }
        Iterator imageReaders = ImageIO.getImageReaders(imageInputStreamCreateImageInputStream);
        if (!imageReaders.hasNext()) {
            throw new UnsupportedFormatException(UnsupportedFormatException.UNKNOWN, "No suitable ImageReader found for source data.");
        }
        ImageReader imageReader = (ImageReader) imageReaders.next();
        imageReader.setInput(imageInputStreamCreateImageInputStream);
        this.inputFormatName = imageReader.getFormatName();
        try {
            if (this.param.useExifOrientation() && (exifOrientation = ExifUtils.getExifOrientation(imageReader, 0)) != null && exifOrientation != Orientation.TOP_LEFT) {
                this.param.getImageFilters().add(0, ExifFilterUtils.getFilterForOrientation(exifOrientation));
            }
        } catch (Exception e) {
        }
        ImageReadParam defaultReadParam = imageReader.getDefaultReadParam();
        int width = imageReader.getWidth(0);
        int height = imageReader.getHeight(0);
        if (this.param != null && this.param.getSourceRegion() != null) {
            defaultReadParam.setSourceRegion(this.param.getSourceRegion().calculate(width, height));
        }
        if (this.param != null && "true".equals(System.getProperty("thumbnailator.conserveMemoryWorkaround")) && width > 1800 && height > 1800 && width * height * 4 > Runtime.getRuntime().freeMemory() / 4) {
            int iMax = 1;
            if (this.param.getSize() != null && this.param.getSize().width * 2 < width && this.param.getSize().height * 2 < height) {
                iMax = (int) Math.floor(Math.min(width / this.param.getSize().width, height / this.param.getSize().height));
            } else if (this.param.getSize() == null) {
                iMax = (int) Math.max(1.0d, Math.floor(1.0d / Math.max(this.param.getHeightScalingFactor(), this.param.getWidthScalingFactor())));
            }
            while (true) {
                if (width / iMax >= 600 && height / iMax >= 600) {
                    break;
                }
                iMax--;
            }
            if (this.param.getSize() == null) {
                try {
                    Class<?> cls = this.param.getClass();
                    Field declaredField = cls.getDeclaredField("heightScalingFactor");
                    Field declaredField2 = cls.getDeclaredField("widthScalingFactor");
                    declaredField.setAccessible(true);
                    declaredField2.setAccessible(true);
                    declaredField.set(this.param, Double.valueOf(this.param.getHeightScalingFactor() * iMax));
                    declaredField2.set(this.param, Double.valueOf(this.param.getWidthScalingFactor() * iMax));
                } catch (Exception e2) {
                    iMax = 1;
                }
            }
            defaultReadParam.setSourceSubsampling(iMax, iMax, 0, 0);
        }
        BufferedImage bufferedImage = imageReader.read(0, defaultReadParam);
        imageReader.dispose();
        imageInputStreamCreateImageInputStream.close();
        return (BufferedImage) finishedReading(bufferedImage);
    }

    @Override // net.coobird.thumbnailator.tasks.io.ImageSource
    public InputStream getSource() {
        return this.is;
    }
}
