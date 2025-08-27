package com.moredian.onpremise.core.utils;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.imaging.jpeg.JpegProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.MetadataException;
import com.drew.metadata.exif.ExifDirectoryBase;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.stream.FileImageOutputStream;
import org.apache.poi.openxml4j.opc.ContentTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/utils/ImageMirrorUtils.class */
public class ImageMirrorUtils {
    private static final Logger logger = LoggerFactory.getLogger((Class<?>) ImageMirrorUtils.class);

    public static void mirrorImage() {
        try {
            File file = new File("C:\\Users\\Administrator\\Desktop\\图片\\IMG_0902.jpg");
            BufferedImage image = ImageIO.read(file);
            int width = image.getWidth();
            int height = image.getHeight();
            for (int j = 0; j < height; j++) {
                int l = 0;
                for (int r = width - 1; l < r; r--) {
                    int pl = image.getRGB(l, j);
                    int pr = image.getRGB(r, j);
                    image.setRGB(l, j, pr);
                    image.setRGB(r, j, pl);
                    l++;
                }
            }
            File file2 = new File("C:\\Users\\Administrator\\Desktop\\图片\\out.png");
            ImageIO.write(image, ContentTypes.EXTENSION_PNG, file2);
        } catch (IOException e) {
            logger.error("error:{}", (Throwable) e);
        }
    }

    private static String getSuffix(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    public static boolean rotateImg(String filePath, String newFilePath) {
        try {
            File file = new File(filePath);
            Metadata metadata = ImageMetadataReader.readMetadata(file);
            Directory directory = metadata.getFirstDirectoryOfType(ExifDirectoryBase.class);
            int orientation = 0;
            if (directory != null && directory.containsTag(274)) {
                orientation = directory.getInt(274);
            }
            int angle = 0;
            if (6 == orientation) {
                angle = 90;
            } else if (3 == orientation) {
                angle = 180;
            } else if (8 == orientation) {
                angle = 270;
            }
            BufferedImage src = ImageIO.read(file);
            BufferedImage newImg = new BufferedImage(src.getWidth(), src.getHeight(), 1);
            newImg.getGraphics().drawImage(src, 0, 0, (ImageObserver) null);
            BufferedImage des = rotate(newImg, angle);
            mirror(des);
            writeImage(des, newFilePath);
            return true;
        } catch (JpegProcessingException e) {
            logger.error("error:{}", (Throwable) e);
            return false;
        } catch (ImageProcessingException e2) {
            logger.error("error:{}", (Throwable) e2);
            return false;
        } catch (MetadataException e3) {
            logger.error("error:{}", (Throwable) e3);
            return false;
        } catch (IOException e4) {
            logger.error("error:{}", (Throwable) e4);
            return false;
        }
    }

    public static void writeImage(BufferedImage fileBufferd, String newFilePath) {
        File file = new File(newFilePath);
        Iterator<ImageWriter> iter = ImageIO.getImageWritersByFormatName(ContentTypes.EXTENSION_JPG_1);
        if (iter.hasNext()) {
            ImageWriter writer = iter.next();
            ImageWriteParam param = writer.getDefaultWriteParam();
            param.setCompressionMode(2);
            param.setCompressionQuality(1.0f);
            try {
                FileImageOutputStream out = new FileImageOutputStream(file);
                writer.setOutput(out);
                writer.write((IIOMetadata) null, new IIOImage(fileBufferd, (List) null, (IIOMetadata) null), param);
                out.close();
                writer.dispose();
            } catch (Exception e) {
                System.out.println("===异常了==");
            }
        }
    }

    private static BufferedImage rotate(BufferedImage src, int angel) {
        if (angel == 0) {
            return src;
        }
        int src_width = src.getWidth((ImageObserver) null);
        int src_height = src.getHeight((ImageObserver) null);
        Rectangle rect_des = calcRotatedSize(new Rectangle(new Dimension(src_width, src_height)), angel);
        BufferedImage res = new BufferedImage(rect_des.width, rect_des.height, 1);
        Graphics2D g2 = res.createGraphics();
        g2.translate((rect_des.width - src_width) / 2, (rect_des.height - src_height) / 2);
        g2.rotate(Math.toRadians(angel), src_width / 2, src_height / 2);
        g2.drawImage(src, (AffineTransform) null, (ImageObserver) null);
        return res;
    }

    private static Rectangle calcRotatedSize(Rectangle src, int angel) {
        if (angel >= 90) {
            if ((angel / 90) % 2 == 1) {
                int temp = src.height;
                src.height = src.width;
                src.width = temp;
            }
            angel %= 90;
        }
        double r = Math.sqrt((src.height * src.height) + (src.width * src.width)) / 2.0d;
        double len = 2.0d * Math.sin(Math.toRadians(angel) / 2.0d) * r;
        double angel_alpha = (3.141592653589793d - Math.toRadians(angel)) / 2.0d;
        double angel_dalta_width = Math.atan(src.height / src.width);
        double angel_dalta_height = Math.atan(src.width / src.height);
        int len_dalta_width = (int) (len * Math.cos((3.141592653589793d - angel_alpha) - angel_dalta_width));
        int len_dalta_height = (int) (len * Math.cos((3.141592653589793d - angel_alpha) - angel_dalta_height));
        int des_width = src.width + (len_dalta_width * 2);
        int des_height = src.height + (len_dalta_height * 2);
        return new Rectangle(new Dimension(des_width, des_height));
    }

    public static void mirror(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        for (int j = 0; j < height; j++) {
            int l = 0;
            for (int r = width - 1; l < r; r--) {
                int pl = image.getRGB(l, j);
                int pr = image.getRGB(r, j);
                image.setRGB(l, j, pr);
                image.setRGB(r, j, pl);
                l++;
            }
        }
    }
}
