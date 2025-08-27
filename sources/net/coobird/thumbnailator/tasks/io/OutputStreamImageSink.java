package net.coobird.thumbnailator.tasks.io;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.List;
import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.stream.ImageOutputStream;
import net.coobird.thumbnailator.ThumbnailParameter;
import net.coobird.thumbnailator.tasks.UnsupportedFormatException;
import net.coobird.thumbnailator.util.BufferedImages;
import net.coobird.thumbnailator.util.ThumbnailatorUtils;
import org.apache.poi.openxml4j.opc.ContentTypes;

/* loaded from: thumbnailator-0.4.8.jar:net/coobird/thumbnailator/tasks/io/OutputStreamImageSink.class */
public class OutputStreamImageSink extends AbstractImageSink<OutputStream> {
    private final OutputStream os;

    public OutputStreamImageSink(OutputStream outputStream) {
        if (outputStream == null) {
            throw new NullPointerException("OutputStream cannot be null.");
        }
        this.os = outputStream;
    }

    @Override // net.coobird.thumbnailator.tasks.io.AbstractImageSink, net.coobird.thumbnailator.tasks.io.ImageSink
    public void write(BufferedImage bufferedImage) throws IOException {
        super.write(bufferedImage);
        if (this.outputFormat == null) {
            throw new IllegalStateException("Output format has not been set.");
        }
        String str = this.outputFormat;
        Iterator imageWritersByFormatName = ImageIO.getImageWritersByFormatName(str);
        if (!imageWritersByFormatName.hasNext()) {
            throw new UnsupportedFormatException(str, "No suitable ImageWriter found for " + str + ".");
        }
        ImageWriter imageWriter = (ImageWriter) imageWritersByFormatName.next();
        ImageWriteParam defaultWriteParam = imageWriter.getDefaultWriteParam();
        if (defaultWriteParam.canWriteCompressed() && this.param != null) {
            defaultWriteParam.setCompressionMode(2);
            if (this.param.getOutputFormatType() != ThumbnailParameter.DEFAULT_FORMAT_TYPE) {
                defaultWriteParam.setCompressionType(this.param.getOutputFormatType());
            } else {
                List<String> supportedOutputFormatTypes = ThumbnailatorUtils.getSupportedOutputFormatTypes(str);
                if (!supportedOutputFormatTypes.isEmpty()) {
                    defaultWriteParam.setCompressionType(supportedOutputFormatTypes.get(0));
                }
            }
            if (!Float.isNaN(this.param.getOutputQuality())) {
                defaultWriteParam.setCompressionQuality(this.param.getOutputQuality());
            }
        }
        ImageOutputStream imageOutputStreamCreateImageOutputStream = ImageIO.createImageOutputStream(this.os);
        if (imageOutputStreamCreateImageOutputStream == null) {
            throw new IOException("Could not open OutputStream.");
        }
        if (str.equalsIgnoreCase(ContentTypes.EXTENSION_JPG_1) || str.equalsIgnoreCase(ContentTypes.EXTENSION_JPG_2) || str.equalsIgnoreCase("bmp")) {
            bufferedImage = BufferedImages.copy(bufferedImage, 1);
        }
        imageWriter.setOutput(imageOutputStreamCreateImageOutputStream);
        imageWriter.write((IIOMetadata) null, new IIOImage(bufferedImage, (List) null, (IIOMetadata) null), defaultWriteParam);
        imageWriter.dispose();
        imageOutputStreamCreateImageOutputStream.close();
    }

    @Override // net.coobird.thumbnailator.tasks.io.ImageSink
    public OutputStream getSink() {
        return this.os;
    }
}
