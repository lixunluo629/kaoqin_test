package org.apache.poi.ss.util;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.Picture;
import org.apache.poi.ss.usermodel.PictureData;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.util.POILogFactory;
import org.apache.poi.util.POILogger;
import org.apache.poi.util.Units;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/* loaded from: poi-3.17.jar:org/apache/poi/ss/util/ImageUtils.class */
public class ImageUtils {
    private static final POILogger logger = POILogFactory.getLogger((Class<?>) ImageUtils.class);
    public static final int PIXEL_DPI = 96;

    /* JADX WARN: Finally extract failed */
    public static Dimension getImageDimension(InputStream is, int type) {
        Dimension size = new Dimension();
        switch (type) {
            case 5:
            case 6:
            case 7:
                try {
                    ImageInputStream iis = ImageIO.createImageInputStream(is);
                    try {
                        Iterator<ImageReader> i = ImageIO.getImageReaders(iis);
                        ImageReader r = i.next();
                        try {
                            r.setInput(iis);
                            BufferedImage img = r.read(0);
                            int[] dpi = getResolution(r);
                            if (dpi[0] == 0) {
                                dpi[0] = 96;
                            }
                            if (dpi[1] == 0) {
                                dpi[1] = 96;
                            }
                            size.width = (img.getWidth() * 96) / dpi[0];
                            size.height = (img.getHeight() * 96) / dpi[1];
                            r.dispose();
                            iis.close();
                            break;
                        } catch (Throwable th) {
                            r.dispose();
                            throw th;
                        }
                    } catch (Throwable th2) {
                        iis.close();
                        throw th2;
                    }
                } catch (IOException e) {
                    logger.log(5, e);
                    break;
                }
            default:
                logger.log(5, "Only JPEG, PNG and DIB pictures can be automatically sized");
                break;
        }
        return size;
    }

    public static int[] getResolution(ImageReader r) throws IOException {
        int hdpi = 96;
        int vdpi = 96;
        Element node = (Element) r.getImageMetadata(0).getAsTree("javax_imageio_1.0");
        NodeList lst = node.getElementsByTagName("HorizontalPixelSize");
        if (lst != null && lst.getLength() == 1) {
            hdpi = (int) (25.4d / Float.parseFloat(((Element) lst.item(0)).getAttribute("value")));
        }
        NodeList lst2 = node.getElementsByTagName("VerticalPixelSize");
        if (lst2 != null && lst2.getLength() == 1) {
            vdpi = (int) (25.4d / Float.parseFloat(((Element) lst2.item(0)).getAttribute("value")));
        }
        return new int[]{hdpi, vdpi};
    }

    public static Dimension setPreferredSize(Picture picture, double scaleX, double scaleY) {
        double dx1;
        double w;
        double dy1;
        double h;
        ClientAnchor anchor = picture.getClientAnchor();
        boolean isHSSF = anchor instanceof HSSFClientAnchor;
        PictureData data = picture.getPictureData();
        Sheet sheet = picture.getSheet();
        Dimension imgSize = getImageDimension(new ByteArrayInputStream(data.getData()), data.getPictureType());
        Dimension anchorSize = getDimensionFromAnchor(picture);
        double scaledWidth = scaleX == Double.MAX_VALUE ? imgSize.getWidth() : (anchorSize.getWidth() / 9525.0d) * scaleX;
        double scaledHeight = scaleY == Double.MAX_VALUE ? imgSize.getHeight() : (anchorSize.getHeight() / 9525.0d) * scaleY;
        int col2 = anchor.getCol1();
        int dx2 = 0;
        int col22 = col2 + 1;
        double w2 = sheet.getColumnWidthInPixels(col2);
        if (isHSSF) {
            dx1 = w2 * (1.0d - (anchor.getDx1() / 1024.0d));
        } else {
            dx1 = w2 - (anchor.getDx1() / 9525.0d);
        }
        while (true) {
            w = dx1;
            if (w >= scaledWidth) {
                break;
            }
            int i = col22;
            col22++;
            dx1 = w + sheet.getColumnWidthInPixels(i);
        }
        if (w > scaledWidth) {
            col22--;
            double cw = sheet.getColumnWidthInPixels(col22);
            double delta = w - scaledWidth;
            if (isHSSF) {
                dx2 = (int) (((cw - delta) / cw) * 1024.0d);
            } else {
                dx2 = (int) ((cw - delta) * 9525.0d);
            }
            if (dx2 < 0) {
                dx2 = 0;
            }
        }
        anchor.setCol2(col22);
        anchor.setDx2(dx2);
        int row2 = anchor.getRow1();
        int dy2 = 0;
        int row22 = row2 + 1;
        double h2 = getRowHeightInPixels(sheet, row2);
        if (isHSSF) {
            dy1 = h2 * (1.0d - (anchor.getDy1() / 256.0d));
        } else {
            dy1 = h2 - (anchor.getDy1() / 9525.0d);
        }
        while (true) {
            h = dy1;
            if (h >= scaledHeight) {
                break;
            }
            int i2 = row22;
            row22++;
            dy1 = h + getRowHeightInPixels(sheet, i2);
        }
        if (h > scaledHeight) {
            row22--;
            double ch2 = getRowHeightInPixels(sheet, row22);
            double delta2 = h - scaledHeight;
            if (isHSSF) {
                dy2 = (int) (((ch2 - delta2) / ch2) * 256.0d);
            } else {
                dy2 = (int) ((ch2 - delta2) * 9525.0d);
            }
            if (dy2 < 0) {
                dy2 = 0;
            }
        }
        anchor.setRow2(row22);
        anchor.setDy2(dy2);
        Dimension dim = new Dimension((int) Math.round(scaledWidth * 9525.0d), (int) Math.round(scaledHeight * 9525.0d));
        return dim;
    }

    public static Dimension getDimensionFromAnchor(Picture picture) {
        double dx1;
        double w;
        double w2;
        double dy1;
        double h;
        double h2;
        ClientAnchor anchor = picture.getClientAnchor();
        boolean isHSSF = anchor instanceof HSSFClientAnchor;
        Sheet sheet = picture.getSheet();
        int col2 = anchor.getCol1();
        int col22 = col2 + 1;
        double w3 = sheet.getColumnWidthInPixels(col2);
        if (isHSSF) {
            dx1 = w3 * (1.0d - (anchor.getDx1() / 1024.0d));
        } else {
            dx1 = w3 - (anchor.getDx1() / 9525.0d);
        }
        while (true) {
            w = dx1;
            if (col22 >= anchor.getCol2()) {
                break;
            }
            int i = col22;
            col22++;
            dx1 = w + sheet.getColumnWidthInPixels(i);
        }
        if (isHSSF) {
            w2 = w + ((sheet.getColumnWidthInPixels(col22) * anchor.getDx2()) / 1024.0d);
        } else {
            w2 = w + (anchor.getDx2() / 9525.0d);
        }
        int row2 = anchor.getRow1();
        int row22 = row2 + 1;
        double h3 = getRowHeightInPixels(sheet, row2);
        if (isHSSF) {
            dy1 = h3 * (1.0d - (anchor.getDy1() / 256.0d));
        } else {
            dy1 = h3 - (anchor.getDy1() / 9525.0d);
        }
        while (true) {
            h = dy1;
            if (row22 >= anchor.getRow2()) {
                break;
            }
            int i2 = row22;
            row22++;
            dy1 = h + getRowHeightInPixels(sheet, i2);
        }
        if (isHSSF) {
            h2 = h + ((getRowHeightInPixels(sheet, row22) * anchor.getDy2()) / 256.0d);
        } else {
            h2 = h + (anchor.getDy2() / 9525.0d);
        }
        return new Dimension((int) Math.rint(w2 * 9525.0d), (int) Math.rint(h2 * 9525.0d));
    }

    public static double getRowHeightInPixels(Sheet sheet, int rowNum) {
        Row r = sheet.getRow(rowNum);
        double points = r == null ? sheet.getDefaultRowHeightInPoints() : r.getHeightInPoints();
        return Units.toEMU(points) / 9525.0d;
    }
}
