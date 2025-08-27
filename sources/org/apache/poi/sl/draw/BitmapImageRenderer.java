package org.apache.poi.sl.draw;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.awt.image.RescaleOp;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.MemoryCacheImageInputStream;
import org.apache.commons.compress.compressors.bzip2.BZip2Constants;
import org.apache.poi.util.POILogFactory;
import org.apache.poi.util.POILogger;

/* loaded from: poi-3.17.jar:org/apache/poi/sl/draw/BitmapImageRenderer.class */
public class BitmapImageRenderer implements ImageRenderer {
    private static final POILogger LOG = POILogFactory.getLogger((Class<?>) ImageRenderer.class);
    protected BufferedImage img;

    @Override // org.apache.poi.sl.draw.ImageRenderer
    public void loadImage(InputStream data, String contentType) throws IOException {
        this.img = readImage(data, contentType);
    }

    @Override // org.apache.poi.sl.draw.ImageRenderer
    public void loadImage(byte[] data, String contentType) throws IOException {
        this.img = readImage(new ByteArrayInputStream(data), contentType);
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    private static BufferedImage readImage(InputStream data, String contentType) throws IOException {
        int y;
        IOException lastException = null;
        BufferedImage img = null;
        if (data.markSupported()) {
            data.mark(data.available());
        }
        ImageInputStream iis = new MemoryCacheImageInputStream(data);
        try {
            iis = new MemoryCacheImageInputStream(data);
            iis.mark();
            Iterator<ImageReader> iter = ImageIO.getImageReaders(iis);
            while (img == null && iter.hasNext()) {
                ImageReader reader = iter.next();
                ImageReadParam param = reader.getDefaultReadParam();
                int mode = 0;
                while (true) {
                    if (img != null || mode >= 3) {
                        break;
                    }
                    lastException = null;
                    try {
                        iis.reset();
                    } catch (IOException e) {
                        if (data.markSupported()) {
                            data.reset();
                            data.mark(data.available());
                            iis.close();
                            iis = new MemoryCacheImageInputStream(data);
                        } else {
                            lastException = e;
                            reader.dispose();
                        }
                    }
                    iis.mark();
                    try {
                    } catch (IOException e2) {
                        if (mode < 2) {
                            lastException = e2;
                        }
                    } catch (RuntimeException e3) {
                        if (mode < 2) {
                            lastException = new IOException("ImageIO runtime exception - " + (mode == 0 ? "normal" : "fallback"), e3);
                        }
                    }
                    switch (mode) {
                        case 0:
                            reader.setInput(iis, false, true);
                            img = reader.read(0, param);
                            mode++;
                        case 1:
                            Iterator<ImageTypeSpecifier> imageTypes = reader.getImageTypes(0);
                            while (true) {
                                if (imageTypes.hasNext()) {
                                    ImageTypeSpecifier imageTypeSpecifier = imageTypes.next();
                                    int bufferedImageType = imageTypeSpecifier.getBufferedImageType();
                                    if (bufferedImageType == 10) {
                                        param.setDestinationType(imageTypeSpecifier);
                                    }
                                }
                            }
                            reader.setInput(iis, false, true);
                            img = reader.read(0, param);
                            mode++;
                        case 2:
                            reader.setInput(iis, false, true);
                            int height = reader.getHeight(0);
                            int width = reader.getWidth(0);
                            Iterator<ImageTypeSpecifier> imageTypes2 = reader.getImageTypes(0);
                            if (imageTypes2.hasNext()) {
                                img = imageTypes2.next().createBufferedImage(width, height);
                                param.setDestination(img);
                                try {
                                    reader.read(0, param);
                                    if (img.getType() != 2) {
                                        int y2 = findTruncatedBlackBox(img, width, height);
                                        if (y2 < height) {
                                            BufferedImage argbImg = new BufferedImage(width, height, 2);
                                            Graphics2D g = argbImg.createGraphics();
                                            g.clipRect(0, 0, width, y2);
                                            g.drawImage(img, 0, 0, (ImageObserver) null);
                                            g.dispose();
                                            img.flush();
                                            img = argbImg;
                                        }
                                    }
                                } catch (Throwable th) {
                                    if (img.getType() != 2 && (y = findTruncatedBlackBox(img, width, height)) < height) {
                                        BufferedImage argbImg2 = new BufferedImage(width, height, 2);
                                        Graphics2D g2 = argbImg2.createGraphics();
                                        g2.clipRect(0, 0, width, y);
                                        g2.drawImage(img, 0, 0, (ImageObserver) null);
                                        g2.dispose();
                                        img.flush();
                                        img = argbImg2;
                                    }
                                    throw th;
                                    break;
                                }
                            } else {
                                lastException = new IOException("unable to load even a truncated version of the image.");
                            }
                            mode++;
                            break;
                        default:
                            mode++;
                    }
                }
            }
            iis.close();
            if (img == null) {
                if (lastException != null) {
                    throw lastException;
                }
                LOG.log(5, "Content-type: " + contentType + " is not support. Image ignored.");
                return null;
            }
            if (img.getType() != 2) {
                BufferedImage argbImg3 = new BufferedImage(img.getWidth(), img.getHeight(), 2);
                Graphics g3 = argbImg3.getGraphics();
                g3.drawImage(img, 0, 0, (ImageObserver) null);
                g3.dispose();
                return argbImg3;
            }
            return img;
        } catch (Throwable th2) {
            iis.close();
            throw th2;
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:13:0x0032, code lost:
    
        r7 = r7 - 1;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private static int findTruncatedBlackBox(java.awt.image.BufferedImage r4, int r5, int r6) {
        /*
            r0 = r6
            r1 = 1
            int r0 = r0 - r1
            r7 = r0
        L4:
            r0 = r7
            if (r0 <= 0) goto L38
            r0 = r5
            r1 = 1
            int r0 = r0 - r1
            r8 = r0
        Ld:
            r0 = r8
            if (r0 <= 0) goto L32
            r0 = r4
            r1 = r8
            r2 = r7
            int r0 = r0.getRGB(r1, r2)
            r9 = r0
            r0 = r9
            r1 = -16777216(0xffffffffff000000, float:-1.7014118E38)
            if (r0 == r1) goto L26
            r0 = r7
            r1 = 1
            int r0 = r0 + r1
            return r0
        L26:
            r0 = r8
            r1 = r5
            r2 = 10
            int r1 = r1 / r2
            int r0 = r0 - r1
            r8 = r0
            goto Ld
        L32:
            int r7 = r7 + (-1)
            goto L4
        L38:
            r0 = 0
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.poi.sl.draw.BitmapImageRenderer.findTruncatedBlackBox(java.awt.image.BufferedImage, int, int):int");
    }

    @Override // org.apache.poi.sl.draw.ImageRenderer
    public BufferedImage getImage() {
        return this.img;
    }

    @Override // org.apache.poi.sl.draw.ImageRenderer
    public BufferedImage getImage(Dimension dim) {
        double w_old = this.img.getWidth();
        double h_old = this.img.getHeight();
        BufferedImage scaled = new BufferedImage((int) w_old, (int) h_old, 2);
        double w_new = dim.getWidth();
        double h_new = dim.getHeight();
        AffineTransform at = new AffineTransform();
        at.scale(w_new / w_old, h_new / h_old);
        AffineTransformOp scaleOp = new AffineTransformOp(at, 2);
        scaleOp.filter(this.img, scaled);
        return scaled;
    }

    @Override // org.apache.poi.sl.draw.ImageRenderer
    public Dimension getDimension() {
        return this.img == null ? new Dimension(0, 0) : new Dimension(this.img.getWidth(), this.img.getHeight());
    }

    @Override // org.apache.poi.sl.draw.ImageRenderer
    public void setAlpha(double alpha) {
        if (this.img == null) {
            return;
        }
        Dimension dim = getDimension();
        BufferedImage newImg = new BufferedImage((int) dim.getWidth(), (int) dim.getHeight(), 2);
        Graphics2D g = newImg.createGraphics();
        RescaleOp op = new RescaleOp(new float[]{1.0f, 1.0f, 1.0f, (float) alpha}, new float[]{0.0f, 0.0f, 0.0f, 0.0f}, (RenderingHints) null);
        g.drawImage(this.img, op, 0, 0);
        g.dispose();
        this.img = newImg;
    }

    @Override // org.apache.poi.sl.draw.ImageRenderer
    public boolean drawImage(Graphics2D graphics, Rectangle2D anchor) {
        return drawImage(graphics, anchor, null);
    }

    @Override // org.apache.poi.sl.draw.ImageRenderer
    public boolean drawImage(Graphics2D graphics, Rectangle2D anchor, Insets clip) {
        if (this.img == null) {
            return false;
        }
        boolean isClipped = true;
        if (clip == null) {
            isClipped = false;
            clip = new Insets(0, 0, 0, 0);
        }
        int iw = this.img.getWidth();
        int ih = this.img.getHeight();
        double cw = ((BZip2Constants.BASEBLOCKSIZE - clip.left) - clip.right) / 100000.0d;
        double ch2 = ((BZip2Constants.BASEBLOCKSIZE - clip.top) - clip.bottom) / 100000.0d;
        double sx = anchor.getWidth() / (iw * cw);
        double sy = anchor.getHeight() / (ih * ch2);
        double tx = anchor.getX() - (((iw * sx) * clip.left) / 100000.0d);
        double ty = anchor.getY() - (((ih * sy) * clip.top) / 100000.0d);
        AffineTransform at = new AffineTransform(sx, 0.0d, 0.0d, sy, tx, ty);
        Shape clipOld = graphics.getClip();
        if (isClipped) {
            graphics.clip(anchor.getBounds2D());
        }
        graphics.drawRenderedImage(this.img, at);
        graphics.setClip(clipOld);
        return true;
    }
}
