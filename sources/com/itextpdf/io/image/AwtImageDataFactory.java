package com.itextpdf.io.image;

import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.PixelGrabber;
import java.io.IOException;

/* loaded from: io-7.1.10.jar:com/itextpdf/io/image/AwtImageDataFactory.class */
class AwtImageDataFactory {
    AwtImageDataFactory() {
    }

    public static ImageData create(Image image, Color color) throws IOException {
        return create(image, color, false);
    }

    public static ImageData create(Image image, Color color, boolean forceBW) throws IOException {
        if (image instanceof BufferedImage) {
            BufferedImage bi = (BufferedImage) image;
            if (bi.getType() == 12 && bi.getColorModel().getPixelSize() == 1) {
                forceBW = true;
            }
        }
        PixelGrabber pg = new PixelGrabber(image, 0, 0, -1, -1, true);
        try {
            pg.grabPixels();
            if ((pg.getStatus() & 128) != 0) {
                throw new IOException("Java.awt.image fetch aborted or errored");
            }
            int w = pg.getWidth();
            int h = pg.getHeight();
            int[] pixels = (int[]) pg.getPixels();
            if (forceBW) {
                int byteWidth = (w / 8) + ((w & 7) != 0 ? 1 : 0);
                byte[] pixelsByte = new byte[byteWidth * h];
                int index = 0;
                int size = h * w;
                int transColor = 1;
                if (color != null) {
                    transColor = (color.getRed() + color.getGreen()) + color.getBlue() < 384 ? 0 : 1;
                }
                int[] transparency = null;
                int cbyte = 128;
                int wMarker = 0;
                int currByte = 0;
                if (color != null) {
                    for (int j = 0; j < size; j++) {
                        if (((pixels[j] >> 24) & 255) < 250) {
                            if (transColor == 1) {
                                currByte |= cbyte;
                            }
                        } else if ((pixels[j] & 2184) != 0) {
                            currByte |= cbyte;
                        }
                        cbyte >>= 1;
                        if (cbyte == 0 || wMarker + 1 >= w) {
                            int i = index;
                            index++;
                            pixelsByte[i] = (byte) currByte;
                            cbyte = 128;
                            currByte = 0;
                        }
                        wMarker++;
                        if (wMarker >= w) {
                            wMarker = 0;
                        }
                    }
                } else {
                    for (int j2 = 0; j2 < size; j2++) {
                        if (transparency == null && ((pixels[j2] >> 24) & 255) == 0) {
                            transparency = new int[2];
                            int i2 = (pixels[j2] & 2184) != 0 ? 255 : 0;
                            transparency[1] = i2;
                            transparency[0] = i2;
                        }
                        if ((pixels[j2] & 2184) != 0) {
                            currByte |= cbyte;
                        }
                        cbyte >>= 1;
                        if (cbyte == 0 || wMarker + 1 >= w) {
                            int i3 = index;
                            index++;
                            pixelsByte[i3] = (byte) currByte;
                            cbyte = 128;
                            currByte = 0;
                        }
                        wMarker++;
                        if (wMarker >= w) {
                            wMarker = 0;
                        }
                    }
                }
                return ImageDataFactory.create(w, h, 1, 1, pixelsByte, transparency);
            }
            byte[] pixelsByte2 = new byte[w * h * 3];
            byte[] smask = null;
            int index2 = 0;
            int size2 = h * w;
            int red = 255;
            int green = 255;
            int blue = 255;
            if (color != null) {
                red = color.getRed();
                green = color.getGreen();
                blue = color.getBlue();
            }
            int[] transparency2 = null;
            if (color != null) {
                for (int j3 = 0; j3 < size2; j3++) {
                    if (((pixels[j3] >> 24) & 255) < 250) {
                        int i4 = index2;
                        int index3 = index2 + 1;
                        pixelsByte2[i4] = (byte) red;
                        int index4 = index3 + 1;
                        pixelsByte2[index3] = (byte) green;
                        index2 = index4 + 1;
                        pixelsByte2[index4] = (byte) blue;
                    } else {
                        int i5 = index2;
                        int index5 = index2 + 1;
                        pixelsByte2[i5] = (byte) ((pixels[j3] >> 16) & 255);
                        int index6 = index5 + 1;
                        pixelsByte2[index5] = (byte) ((pixels[j3] >> 8) & 255);
                        index2 = index6 + 1;
                        pixelsByte2[index6] = (byte) (pixels[j3] & 255);
                    }
                }
            } else {
                int transparentPixel = 0;
                smask = new byte[w * h];
                boolean shades = false;
                for (int j4 = 0; j4 < size2; j4++) {
                    byte alpha = (byte) ((pixels[j4] >> 24) & 255);
                    smask[j4] = alpha;
                    if (!shades) {
                        if (alpha != 0 && alpha != -1) {
                            shades = true;
                        } else if (transparency2 == null) {
                            if (alpha == 0) {
                                transparentPixel = pixels[j4] & 16777215;
                                int i6 = (transparentPixel >> 16) & 255;
                                int i7 = (transparentPixel >> 8) & 255;
                                int i8 = transparentPixel & 255;
                                transparency2 = new int[]{i6, i6, i7, i7, i8, i8};
                                int prevPixel = 0;
                                while (true) {
                                    if (prevPixel >= j4) {
                                        break;
                                    }
                                    if ((pixels[prevPixel] & 16777215) != transparentPixel) {
                                        prevPixel++;
                                    } else {
                                        shades = true;
                                        break;
                                    }
                                }
                            }
                        } else if ((pixels[j4] & 16777215) != transparentPixel && alpha == 0) {
                            shades = true;
                        } else if ((pixels[j4] & 16777215) == transparentPixel && alpha != 0) {
                            shades = true;
                        }
                    }
                    int i9 = index2;
                    int index7 = index2 + 1;
                    pixelsByte2[i9] = (byte) ((pixels[j4] >> 16) & 255);
                    int index8 = index7 + 1;
                    pixelsByte2[index7] = (byte) ((pixels[j4] >> 8) & 255);
                    index2 = index8 + 1;
                    pixelsByte2[index8] = (byte) (pixels[j4] & 255);
                }
                if (shades) {
                    transparency2 = null;
                } else {
                    smask = null;
                }
            }
            ImageData img = ImageDataFactory.create(w, h, 3, 8, pixelsByte2, transparency2);
            if (smask != null) {
                ImageData sm = ImageDataFactory.create(w, h, 1, 8, smask, null);
                sm.makeMask();
                img.setImageMask(sm);
            }
            return img;
        } catch (InterruptedException e) {
            throw new IOException("Java.awt.image was interrupted. Waiting for pixels");
        }
    }
}
