package com.itextpdf.io.image;

import com.itextpdf.io.IOException;
import com.itextpdf.io.codec.CCITTG4Encoder;
import com.itextpdf.io.codec.TIFFFaxDecoder;
import com.itextpdf.io.util.UrlUtil;
import java.awt.Color;
import java.awt.Image;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/* loaded from: io-7.1.10.jar:com/itextpdf/io/image/ImageDataFactory.class */
public final class ImageDataFactory {
    private static final byte[] gif = {71, 73, 70};
    private static final byte[] jpeg = {-1, -40};
    private static final byte[] jpeg2000_1 = {0, 0, 0, 12};
    private static final byte[] jpeg2000_2 = {-1, 79, -1, 81};
    private static final byte[] png = {-119, 80, 78, 71};
    private static final byte[] wmf = {-41, -51};
    private static final byte[] bmp = {66, 77};
    private static final byte[] tiff_1 = {77, 77, 0, 42};
    private static final byte[] tiff_2 = {73, 73, 42, 0};
    private static final byte[] jbig2 = {-105, 74, 66, 50, 13, 10, 26, 10};

    private ImageDataFactory() {
    }

    public static ImageData create(byte[] bytes, boolean recoverImage) {
        return createImageInstance(bytes, recoverImage);
    }

    public static ImageData create(byte[] bytes) {
        return create(bytes, false);
    }

    public static ImageData create(URL url, boolean recoverImage) {
        return createImageInstance(url, recoverImage);
    }

    public static ImageData create(URL url) {
        return create(url, false);
    }

    public static ImageData create(String filename, boolean recoverImage) throws MalformedURLException {
        return create(UrlUtil.toURL(filename), recoverImage);
    }

    public static ImageData create(String filename) throws MalformedURLException {
        return create(filename, false);
    }

    public static ImageData create(int width, int height, boolean reverseBits, int typeCCITT, int parameters, byte[] data, int[] transparency) {
        if (transparency != null && transparency.length != 2) {
            throw new IOException(IOException.TransparencyLengthMustBeEqualTo2WithCcittImages);
        }
        if (typeCCITT != 256 && typeCCITT != 257 && typeCCITT != 258) {
            throw new IOException(IOException.CcittCompressionTypeMustBeCcittg4Ccittg3_1dOrCcittg3_2d);
        }
        if (reverseBits) {
            TIFFFaxDecoder.reverseBits(data);
        }
        RawImageData image = new RawImageData(data, ImageType.RAW);
        image.setTypeCcitt(typeCCITT);
        image.height = height;
        image.width = width;
        image.colorSpace = parameters;
        image.transparency = transparency;
        return image;
    }

    public static ImageData create(int width, int height, int components, int bpc, byte[] data, int[] transparency) {
        if (transparency != null && transparency.length != components * 2) {
            throw new IOException(IOException.TransparencyLengthMustBeEqualTo2WithCcittImages);
        }
        if (components == 1 && bpc == 1) {
            byte[] g4 = CCITTG4Encoder.compress(data, width, height);
            return create(width, height, false, 256, 1, g4, transparency);
        }
        RawImageData image = new RawImageData(data, ImageType.RAW);
        image.height = height;
        image.width = width;
        if (components != 1 && components != 3 && components != 4) {
            throw new IOException(IOException.ComponentsMustBe1_3Or4);
        }
        if (bpc != 1 && bpc != 2 && bpc != 4 && bpc != 8) {
            throw new IOException(IOException.BitsPerComponentMustBe1_2_4or8);
        }
        image.colorSpace = components;
        image.bpc = bpc;
        image.data = data;
        image.transparency = transparency;
        return image;
    }

    public static ImageData create(Image image, Color color) throws java.io.IOException {
        return create(image, color, false);
    }

    public static ImageData create(Image image, Color color, boolean forceBW) throws java.io.IOException {
        return AwtImageDataFactory.create(image, color, forceBW);
    }

    public static ImageData createBmp(URL url, boolean noHeader, int size) throws java.io.IOException {
        byte[] imageType = readImageType(url);
        if (imageTypeIs(imageType, bmp)) {
            ImageData image = new BmpImageData(url, noHeader, size);
            BmpImageHelper.processImage(image);
            return image;
        }
        throw new IllegalArgumentException("BMP image expected.");
    }

    public static ImageData createBmp(byte[] bytes, boolean noHeader, int size) throws java.io.IOException {
        byte[] imageType = readImageType(bytes);
        if (noHeader || imageTypeIs(imageType, bmp)) {
            ImageData image = new BmpImageData(bytes, noHeader, size);
            BmpImageHelper.processImage(image);
            return image;
        }
        throw new IllegalArgumentException("BMP image expected.");
    }

    public static GifImageData createGif(byte[] bytes) throws java.io.IOException {
        byte[] imageType = readImageType(bytes);
        if (imageTypeIs(imageType, gif)) {
            GifImageData image = new GifImageData(bytes);
            GifImageHelper.processImage(image);
            return image;
        }
        throw new IllegalArgumentException("GIF image expected.");
    }

    public static ImageData createGifFrame(URL url, int frame) throws java.io.IOException {
        byte[] imageType = readImageType(url);
        if (imageTypeIs(imageType, gif)) {
            GifImageData image = new GifImageData(url);
            GifImageHelper.processImage(image, frame - 1);
            return image.getFrames().get(frame - 1);
        }
        throw new IllegalArgumentException("GIF image expected.");
    }

    public static ImageData createGifFrame(byte[] bytes, int frame) throws java.io.IOException {
        byte[] imageType = readImageType(bytes);
        if (imageTypeIs(imageType, gif)) {
            GifImageData image = new GifImageData(bytes);
            GifImageHelper.processImage(image, frame - 1);
            return image.getFrames().get(frame - 1);
        }
        throw new IllegalArgumentException("GIF image expected.");
    }

    public static List<ImageData> createGifFrames(byte[] bytes, int[] frameNumbers) throws java.io.IOException {
        byte[] imageType = readImageType(bytes);
        if (imageTypeIs(imageType, gif)) {
            GifImageData image = new GifImageData(bytes);
            Arrays.sort(frameNumbers);
            GifImageHelper.processImage(image, frameNumbers[frameNumbers.length - 1] - 1);
            List<ImageData> frames = new ArrayList<>();
            for (int frame : frameNumbers) {
                frames.add(image.getFrames().get(frame - 1));
            }
            return frames;
        }
        throw new IllegalArgumentException("GIF image expected.");
    }

    public static List<ImageData> createGifFrames(URL url, int[] frameNumbers) throws java.io.IOException {
        byte[] imageType = readImageType(url);
        if (imageTypeIs(imageType, gif)) {
            GifImageData image = new GifImageData(url);
            Arrays.sort(frameNumbers);
            GifImageHelper.processImage(image, frameNumbers[frameNumbers.length - 1] - 1);
            List<ImageData> frames = new ArrayList<>();
            for (int frame : frameNumbers) {
                frames.add(image.getFrames().get(frame - 1));
            }
            return frames;
        }
        throw new IllegalArgumentException("GIF image expected.");
    }

    public static List<ImageData> createGifFrames(byte[] bytes) throws java.io.IOException {
        byte[] imageType = readImageType(bytes);
        if (imageTypeIs(imageType, gif)) {
            GifImageData image = new GifImageData(bytes);
            GifImageHelper.processImage(image);
            return image.getFrames();
        }
        throw new IllegalArgumentException("GIF image expected.");
    }

    public static List<ImageData> createGifFrames(URL url) throws java.io.IOException {
        byte[] imageType = readImageType(url);
        if (imageTypeIs(imageType, gif)) {
            GifImageData image = new GifImageData(url);
            GifImageHelper.processImage(image);
            return image.getFrames();
        }
        throw new IllegalArgumentException("GIF image expected.");
    }

    public static ImageData createJbig2(URL url, int page) throws java.io.IOException {
        if (page < 1) {
            throw new IllegalArgumentException("The page number must be greater than 0");
        }
        byte[] imageType = readImageType(url);
        if (imageTypeIs(imageType, jbig2)) {
            ImageData image = new Jbig2ImageData(url, page);
            Jbig2ImageHelper.processImage(image);
            return image;
        }
        throw new IllegalArgumentException("JBIG2 image expected.");
    }

    public static ImageData createJbig2(byte[] bytes, int page) throws java.io.IOException {
        if (page < 1) {
            throw new IllegalArgumentException("The page number must be greater than 0");
        }
        byte[] imageType = readImageType(bytes);
        if (imageTypeIs(imageType, jbig2)) {
            ImageData image = new Jbig2ImageData(bytes, page);
            Jbig2ImageHelper.processImage(image);
            return image;
        }
        throw new IllegalArgumentException("JBIG2 image expected.");
    }

    public static ImageData createJpeg(URL url) throws java.io.IOException {
        byte[] imageType = readImageType(url);
        if (imageTypeIs(imageType, jpeg)) {
            ImageData image = new JpegImageData(url);
            JpegImageHelper.processImage(image);
            return image;
        }
        throw new IllegalArgumentException("JPEG image expected.");
    }

    public static ImageData createJpeg(byte[] bytes) throws java.io.IOException {
        byte[] imageType = readImageType(bytes);
        if (imageTypeIs(imageType, jpeg)) {
            ImageData image = new JpegImageData(bytes);
            JpegImageHelper.processImage(image);
            return image;
        }
        throw new IllegalArgumentException("JPEG image expected.");
    }

    public static ImageData createJpeg2000(URL url) throws java.io.IOException {
        byte[] imageType = readImageType(url);
        if (imageTypeIs(imageType, jpeg2000_1) || imageTypeIs(imageType, jpeg2000_2)) {
            ImageData image = new Jpeg2000ImageData(url);
            Jpeg2000ImageHelper.processImage(image);
            return image;
        }
        throw new IllegalArgumentException("JPEG2000 image expected.");
    }

    public static ImageData createJpeg2000(byte[] bytes) throws java.io.IOException {
        byte[] imageType = readImageType(bytes);
        if (imageTypeIs(imageType, jpeg2000_1) || imageTypeIs(imageType, jpeg2000_2)) {
            ImageData image = new Jpeg2000ImageData(bytes);
            Jpeg2000ImageHelper.processImage(image);
            return image;
        }
        throw new IllegalArgumentException("JPEG2000 image expected.");
    }

    public static ImageData createPng(URL url) throws java.io.IOException {
        byte[] imageType = readImageType(url);
        if (imageTypeIs(imageType, png)) {
            ImageData image = new PngImageData(url);
            PngImageHelper.processImage(image);
            return image;
        }
        throw new IllegalArgumentException("PNG image expected.");
    }

    public static ImageData createPng(byte[] bytes) throws java.io.IOException {
        byte[] imageType = readImageType(bytes);
        if (imageTypeIs(imageType, png)) {
            ImageData image = new PngImageData(bytes);
            PngImageHelper.processImage(image);
            return image;
        }
        throw new IllegalArgumentException("PNG image expected.");
    }

    public static ImageData createTiff(URL url, boolean recoverFromImageError, int page, boolean direct) throws java.io.IOException {
        byte[] imageType = readImageType(url);
        if (imageTypeIs(imageType, tiff_1) || imageTypeIs(imageType, tiff_2)) {
            ImageData image = new TiffImageData(url, recoverFromImageError, page, direct);
            TiffImageHelper.processImage(image);
            return image;
        }
        throw new IllegalArgumentException("TIFF image expected.");
    }

    public static ImageData createTiff(byte[] bytes, boolean recoverFromImageError, int page, boolean direct) throws java.io.IOException {
        byte[] imageType = readImageType(bytes);
        if (imageTypeIs(imageType, tiff_1) || imageTypeIs(imageType, tiff_2)) {
            ImageData image = new TiffImageData(bytes, recoverFromImageError, page, direct);
            TiffImageHelper.processImage(image);
            return image;
        }
        throw new IllegalArgumentException("TIFF image expected.");
    }

    public static ImageData createRawImage(byte[] bytes) {
        return new RawImageData(bytes, ImageType.RAW);
    }

    public static boolean isSupportedType(byte[] source) throws java.io.IOException {
        if (source == null) {
            return false;
        }
        byte[] imageType = readImageType(source);
        return imageTypeIs(imageType, gif) || imageTypeIs(imageType, jpeg) || imageTypeIs(imageType, jpeg2000_1) || imageTypeIs(imageType, jpeg2000_2) || imageTypeIs(imageType, png) || imageTypeIs(imageType, bmp) || imageTypeIs(imageType, tiff_1) || imageTypeIs(imageType, tiff_2) || imageTypeIs(imageType, jbig2);
    }

    public static boolean isSupportedType(URL source) throws java.io.IOException {
        if (source == null) {
            return false;
        }
        byte[] imageType = readImageType(source);
        return imageTypeIs(imageType, gif) || imageTypeIs(imageType, jpeg) || imageTypeIs(imageType, jpeg2000_1) || imageTypeIs(imageType, jpeg2000_2) || imageTypeIs(imageType, png) || imageTypeIs(imageType, bmp) || imageTypeIs(imageType, tiff_1) || imageTypeIs(imageType, tiff_2) || imageTypeIs(imageType, jbig2);
    }

    private static ImageData createImageInstance(URL source, boolean recoverImage) throws java.io.IOException {
        byte[] imageType = readImageType(source);
        if (imageTypeIs(imageType, gif)) {
            GifImageData image = new GifImageData(source);
            GifImageHelper.processImage(image, 0);
            return image.getFrames().get(0);
        }
        if (imageTypeIs(imageType, jpeg)) {
            ImageData image2 = new JpegImageData(source);
            JpegImageHelper.processImage(image2);
            return image2;
        }
        if (imageTypeIs(imageType, jpeg2000_1) || imageTypeIs(imageType, jpeg2000_2)) {
            ImageData image3 = new Jpeg2000ImageData(source);
            Jpeg2000ImageHelper.processImage(image3);
            return image3;
        }
        if (imageTypeIs(imageType, png)) {
            ImageData image4 = new PngImageData(source);
            PngImageHelper.processImage(image4);
            return image4;
        }
        if (imageTypeIs(imageType, bmp)) {
            ImageData image5 = new BmpImageData(source, false, 0);
            BmpImageHelper.processImage(image5);
            return image5;
        }
        if (imageTypeIs(imageType, tiff_1) || imageTypeIs(imageType, tiff_2)) {
            ImageData image6 = new TiffImageData(source, recoverImage, 1, false);
            TiffImageHelper.processImage(image6);
            return image6;
        }
        if (imageTypeIs(imageType, jbig2)) {
            ImageData image7 = new Jbig2ImageData(source, 1);
            Jbig2ImageHelper.processImage(image7);
            return image7;
        }
        throw new IOException(IOException.ImageFormatCannotBeRecognized);
    }

    private static ImageData createImageInstance(byte[] bytes, boolean recoverImage) throws java.io.IOException {
        byte[] imageType = readImageType(bytes);
        if (imageTypeIs(imageType, gif)) {
            GifImageData image = new GifImageData(bytes);
            GifImageHelper.processImage(image, 0);
            return image.getFrames().get(0);
        }
        if (imageTypeIs(imageType, jpeg)) {
            ImageData image2 = new JpegImageData(bytes);
            JpegImageHelper.processImage(image2);
            return image2;
        }
        if (imageTypeIs(imageType, jpeg2000_1) || imageTypeIs(imageType, jpeg2000_2)) {
            ImageData image3 = new Jpeg2000ImageData(bytes);
            Jpeg2000ImageHelper.processImage(image3);
            return image3;
        }
        if (imageTypeIs(imageType, png)) {
            ImageData image4 = new PngImageData(bytes);
            PngImageHelper.processImage(image4);
            return image4;
        }
        if (imageTypeIs(imageType, bmp)) {
            ImageData image5 = new BmpImageData(bytes, false, 0);
            BmpImageHelper.processImage(image5);
            return image5;
        }
        if (imageTypeIs(imageType, tiff_1) || imageTypeIs(imageType, tiff_2)) {
            ImageData image6 = new TiffImageData(bytes, recoverImage, 1, false);
            TiffImageHelper.processImage(image6);
            return image6;
        }
        if (imageTypeIs(imageType, jbig2)) {
            ImageData image7 = new Jbig2ImageData(bytes, 1);
            Jbig2ImageHelper.processImage(image7);
            return image7;
        }
        throw new IOException(IOException.ImageFormatCannotBeRecognized);
    }

    private static boolean imageTypeIs(byte[] imageType, byte[] compareWith) {
        for (int i = 0; i < compareWith.length; i++) {
            if (imageType[i] != compareWith[i]) {
                return false;
            }
        }
        return true;
    }

    private static byte[] readImageType(URL source) throws java.io.IOException {
        InputStream stream = null;
        try {
            try {
                stream = UrlUtil.openStream(source);
                byte[] bytes = new byte[8];
                stream.read(bytes);
                if (stream != null) {
                    try {
                        stream.close();
                    } catch (java.io.IOException e) {
                    }
                }
                return bytes;
            } catch (Throwable th) {
                if (stream != null) {
                    try {
                        stream.close();
                    } catch (java.io.IOException e2) {
                    }
                }
                throw th;
            }
        } catch (java.io.IOException e3) {
            throw new IOException("I/O exception.", (Throwable) e3);
        }
    }

    private static byte[] readImageType(byte[] source) throws java.io.IOException {
        try {
            InputStream stream = new ByteArrayInputStream(source);
            byte[] bytes = new byte[8];
            stream.read(bytes);
            return bytes;
        } catch (java.io.IOException e) {
            return null;
        }
    }
}
