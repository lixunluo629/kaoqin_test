package net.coobird.thumbnailator.tasks.io;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.stream.ImageOutputStream;
import net.coobird.thumbnailator.ThumbnailParameter;
import net.coobird.thumbnailator.tasks.UnsupportedFormatException;
import net.coobird.thumbnailator.util.BufferedImages;
import net.coobird.thumbnailator.util.ThumbnailatorUtils;
import org.apache.poi.openxml4j.opc.ContentTypes;

/* loaded from: thumbnailator-0.4.8.jar:net/coobird/thumbnailator/tasks/io/FileImageSink.class */
public class FileImageSink extends AbstractImageSink<File> {
    private File destinationFile;
    private final boolean allowOverwrite;

    public FileImageSink(File file) {
        this(file, true);
    }

    public FileImageSink(File file, boolean z) {
        if (file == null) {
            throw new NullPointerException("File cannot be null.");
        }
        this.destinationFile = file;
        this.outputFormat = getExtension(file);
        this.allowOverwrite = z;
    }

    public FileImageSink(String str) {
        this(str, true);
    }

    public FileImageSink(String str, boolean z) {
        if (str == null) {
            throw new NullPointerException("File cannot be null.");
        }
        this.destinationFile = new File(str);
        this.outputFormat = getExtension(this.destinationFile);
        this.allowOverwrite = z;
    }

    private static boolean isMatchingFormat(String str, String str2) {
        if (str == null || str2 == null) {
            return false;
        }
        try {
            for (String str3 : ((ImageWriter) ImageIO.getImageWritersByFormatName(str).next()).getOriginatingProvider().getFileSuffixes()) {
                if (str2.equalsIgnoreCase(str3)) {
                    return true;
                }
            }
            return false;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    private static String getExtension(File file) {
        String name = file.getName();
        if (name.indexOf(46) != -1 && name.lastIndexOf(46) != name.length() - 1) {
            return name.substring(name.lastIndexOf(46) + 1);
        }
        return null;
    }

    @Override // net.coobird.thumbnailator.tasks.io.AbstractImageSink, net.coobird.thumbnailator.tasks.io.ImageSink
    public String preferredOutputFormatName() {
        String extension = getExtension(this.destinationFile);
        if (extension != null) {
            Iterator imageReadersBySuffix = ImageIO.getImageReadersBySuffix(extension);
            if (imageReadersBySuffix.hasNext()) {
                try {
                    return ((ImageReader) imageReadersBySuffix.next()).getFormatName();
                } catch (IOException e) {
                    return ThumbnailParameter.ORIGINAL_FORMAT;
                }
            }
        }
        return this.outputFormat;
    }

    @Override // net.coobird.thumbnailator.tasks.io.AbstractImageSink, net.coobird.thumbnailator.tasks.io.ImageSink
    public void write(BufferedImage bufferedImage) throws IOException {
        super.write(bufferedImage);
        String extension = getExtension(this.destinationFile);
        String formatName = this.outputFormat;
        if (formatName != null && (extension == null || !isMatchingFormat(formatName, extension))) {
            this.destinationFile = new File(this.destinationFile.getAbsolutePath() + "." + formatName);
        }
        if (!this.allowOverwrite && this.destinationFile.exists()) {
            throw new IllegalArgumentException("The destination file exists.");
        }
        if (formatName == null && extension != null) {
            Iterator imageReadersBySuffix = ImageIO.getImageReadersBySuffix(extension);
            if (imageReadersBySuffix.hasNext()) {
                formatName = ((ImageReader) imageReadersBySuffix.next()).getFormatName();
            }
        }
        if (formatName == null) {
            throw new UnsupportedFormatException(formatName, "Could not determine output format.");
        }
        Iterator imageWritersByFormatName = ImageIO.getImageWritersByFormatName(formatName);
        if (!imageWritersByFormatName.hasNext()) {
            throw new UnsupportedFormatException(formatName, "No suitable ImageWriter found for " + formatName + ".");
        }
        ImageWriter imageWriter = (ImageWriter) imageWritersByFormatName.next();
        ImageWriteParam defaultWriteParam = imageWriter.getDefaultWriteParam();
        if (defaultWriteParam.canWriteCompressed() && this.param != null) {
            defaultWriteParam.setCompressionMode(2);
            if (this.param.getOutputFormatType() != ThumbnailParameter.DEFAULT_FORMAT_TYPE) {
                defaultWriteParam.setCompressionType(this.param.getOutputFormatType());
            } else {
                List<String> supportedOutputFormatTypes = ThumbnailatorUtils.getSupportedOutputFormatTypes(formatName);
                if (!supportedOutputFormatTypes.isEmpty()) {
                    defaultWriteParam.setCompressionType(supportedOutputFormatTypes.get(0));
                }
            }
            if (!Float.isNaN(this.param.getOutputQuality())) {
                defaultWriteParam.setCompressionQuality(this.param.getOutputQuality());
            }
        }
        FileOutputStream fileOutputStream = new FileOutputStream(this.destinationFile);
        ImageOutputStream imageOutputStreamCreateImageOutputStream = ImageIO.createImageOutputStream(fileOutputStream);
        if (imageOutputStreamCreateImageOutputStream == null || fileOutputStream == null) {
            throw new IOException("Could not open output file.");
        }
        if (formatName.equalsIgnoreCase(ContentTypes.EXTENSION_JPG_1) || formatName.equalsIgnoreCase(ContentTypes.EXTENSION_JPG_2) || formatName.equalsIgnoreCase("bmp")) {
            bufferedImage = BufferedImages.copy(bufferedImage, 1);
        }
        imageWriter.setOutput(imageOutputStreamCreateImageOutputStream);
        imageWriter.write((IIOMetadata) null, new IIOImage(bufferedImage, (List) null, (IIOMetadata) null), defaultWriteParam);
        imageWriter.dispose();
        imageOutputStreamCreateImageOutputStream.close();
        fileOutputStream.close();
    }

    @Override // net.coobird.thumbnailator.tasks.io.ImageSink
    public File getSink() {
        return this.destinationFile;
    }
}
