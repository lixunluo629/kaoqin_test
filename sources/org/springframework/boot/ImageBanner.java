package org.springframework.boot;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import javax.imageio.ImageIO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.ansi.AnsiBackground;
import org.springframework.boot.ansi.AnsiColor;
import org.springframework.boot.ansi.AnsiColors;
import org.springframework.boot.ansi.AnsiElement;
import org.springframework.boot.ansi.AnsiOutput;
import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.core.env.Environment;
import org.springframework.core.env.PropertyResolver;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;

/* loaded from: spring-boot-1.5.22.RELEASE.jar:org/springframework/boot/ImageBanner.class */
public class ImageBanner implements Banner {
    private static final int LUMINANCE_INCREMENT = 10;
    private final Resource image;
    private static final Log logger = LogFactory.getLog(ImageBanner.class);
    private static final double[] RGB_WEIGHT = {0.2126d, 0.7152d, 0.0722d};
    private static final char[] PIXEL = {' ', '.', '*', ':', 'o', '&', '8', '#', '@'};
    private static final int LUMINANCE_START = 10 * PIXEL.length;

    public ImageBanner(Resource image) {
        Assert.notNull(image, "Image must not be null");
        Assert.isTrue(image.exists(), "Image must exist");
        this.image = image;
    }

    @Override // org.springframework.boot.Banner
    public void printBanner(Environment environment, Class<?> sourceClass, PrintStream out) {
        String headless = System.getProperty("java.awt.headless");
        try {
            try {
                System.setProperty("java.awt.headless", "true");
                printBanner(environment, out);
                if (headless == null) {
                    System.clearProperty("java.awt.headless");
                } else {
                    System.setProperty("java.awt.headless", headless);
                }
            } catch (Throwable ex) {
                logger.warn("Image banner not printable: " + this.image + " (" + ex.getClass() + ": '" + ex.getMessage() + "')");
                logger.debug("Image banner printing failure", ex);
                if (headless == null) {
                    System.clearProperty("java.awt.headless");
                } else {
                    System.setProperty("java.awt.headless", headless);
                }
            }
        } catch (Throwable th) {
            if (headless == null) {
                System.clearProperty("java.awt.headless");
            } else {
                System.setProperty("java.awt.headless", headless);
            }
            throw th;
        }
    }

    private void printBanner(Environment environment, PrintStream out) throws IOException {
        PropertyResolver properties = new RelaxedPropertyResolver(environment, "banner.image.");
        int width = ((Integer) properties.getProperty("width", Integer.class, 76)).intValue();
        int height = ((Integer) properties.getProperty("height", Integer.class, 0)).intValue();
        int margin = ((Integer) properties.getProperty("margin", Integer.class, 2)).intValue();
        boolean invert = ((Boolean) properties.getProperty("invert", Boolean.class, false)).booleanValue();
        BufferedImage image = readImage(width, height);
        printBanner(image, margin, invert, out);
    }

    private BufferedImage readImage(int width, int height) throws IOException {
        InputStream inputStream = this.image.getInputStream();
        try {
            BufferedImage image = ImageIO.read(inputStream);
            BufferedImage bufferedImageResizeImage = resizeImage(image, width, height);
            inputStream.close();
            return bufferedImageResizeImage;
        } catch (Throwable th) {
            inputStream.close();
            throw th;
        }
    }

    private BufferedImage resizeImage(BufferedImage image, int width, int height) {
        if (width < 1) {
            width = 1;
        }
        if (height <= 0) {
            double aspectRatio = (width / image.getWidth()) * 0.5d;
            height = (int) Math.ceil(image.getHeight() * aspectRatio);
        }
        BufferedImage resized = new BufferedImage(width, height, 1);
        Image scaled = image.getScaledInstance(width, height, 1);
        resized.getGraphics().drawImage(scaled, 0, 0, (ImageObserver) null);
        return resized;
    }

    private void printBanner(BufferedImage image, int margin, boolean invert, PrintStream out) {
        AnsiElement background = invert ? AnsiBackground.BLACK : AnsiBackground.DEFAULT;
        out.print(AnsiOutput.encode(AnsiColor.DEFAULT));
        out.print(AnsiOutput.encode(background));
        out.println();
        out.println();
        AnsiColor lastColor = AnsiColor.DEFAULT;
        for (int y = 0; y < image.getHeight(); y++) {
            for (int i = 0; i < margin; i++) {
                out.print(SymbolConstants.SPACE_SYMBOL);
            }
            for (int x = 0; x < image.getWidth(); x++) {
                Color color = new Color(image.getRGB(x, y), false);
                AnsiColor ansiColor = AnsiColors.getClosest(color);
                if (ansiColor != lastColor) {
                    out.print(AnsiOutput.encode(ansiColor));
                    lastColor = ansiColor;
                }
                out.print(getAsciiPixel(color, invert));
            }
            out.println();
        }
        out.print(AnsiOutput.encode(AnsiColor.DEFAULT));
        out.print(AnsiOutput.encode(AnsiBackground.DEFAULT));
        out.println();
    }

    private char getAsciiPixel(Color color, boolean dark) {
        double luminance = getLuminance(color, dark);
        for (int i = 0; i < PIXEL.length; i++) {
            if (luminance >= LUMINANCE_START - (i * 10)) {
                return PIXEL[i];
            }
        }
        return PIXEL[PIXEL.length - 1];
    }

    private int getLuminance(Color color, boolean inverse) {
        double luminance = 0.0d + getLuminance(color.getRed(), inverse, RGB_WEIGHT[0]);
        return (int) Math.ceil((((luminance + getLuminance(color.getGreen(), inverse, RGB_WEIGHT[1])) + getLuminance(color.getBlue(), inverse, RGB_WEIGHT[2])) / 255.0d) * 100.0d);
    }

    private double getLuminance(int component, boolean inverse, double weight) {
        return (inverse ? 255 - component : component) * weight;
    }
}
