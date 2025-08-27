package com.itextpdf.io.image;

import com.drew.metadata.adobe.AdobeJpegReader;
import com.drew.metadata.icc.IccReader;
import com.itextpdf.io.colors.IccProfile;
import com.itextpdf.io.util.MessageFormatUtil;
import com.itextpdf.io.util.StreamUtil;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import org.aspectj.apache.bcel.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/* loaded from: io-7.1.10.jar:com/itextpdf/io/image/JpegImageHelper.class */
class JpegImageHelper {
    private static final int NOT_A_MARKER = -1;
    private static final int VALID_MARKER = 0;
    private static final int UNSUPPORTED_MARKER = 1;
    private static final int NOPARAM_MARKER = 2;
    private static final int M_APP0 = 224;
    private static final int M_APP2 = 226;
    private static final int M_APPE = 238;
    private static final int M_APPD = 237;
    private static final int[] VALID_MARKERS = {192, 193, 194};
    private static final int[] UNSUPPORTED_MARKERS = {195, 197, 198, 199, 200, 201, 202, 203, 205, 206, 207};
    private static final int[] NOPARAM_MARKERS = {208, Constants.PUTFIELD2_QUICK, Constants.GETSTATIC_QUICK, 211, Constants.GETSTATIC2_QUICK, 213, Constants.INVOKEVIRTUAL_QUICK, 215, Constants.INVOKESUPER_QUICK, 1};
    private static final byte[] JFIF_ID = {74, 70, 73, 70, 0};
    private static final byte[] PS_8BIM_RESO = {56, 66, 73, 77, 3, -19};

    JpegImageHelper() {
    }

    public static void processImage(ImageData image) throws IOException {
        String errorID;
        if (image.getOriginalType() != ImageType.JPEG) {
            throw new IllegalArgumentException("JPEG image expected");
        }
        InputStream jpegStream = null;
        try {
            try {
                if (image.getData() == null) {
                    image.loadData();
                    errorID = image.getUrl().toString();
                } else {
                    errorID = "Byte array";
                }
                jpegStream = new ByteArrayInputStream(image.getData());
                image.imageSize = image.getData().length;
                processParameters(jpegStream, errorID, image);
                if (jpegStream != null) {
                    try {
                        jpegStream.close();
                    } catch (IOException e) {
                    }
                }
                updateAttributes(image);
            } catch (Throwable th) {
                if (jpegStream != null) {
                    try {
                        jpegStream.close();
                    } catch (IOException e2) {
                    }
                }
                throw th;
            }
        } catch (IOException e3) {
            throw new com.itextpdf.io.IOException(com.itextpdf.io.IOException.JpegImageException, (Throwable) e3);
        }
    }

    private static void updateAttributes(ImageData image) {
        image.filter = "DCTDecode";
        if (image.getColorTransform() == 0) {
            Map<String, Object> decodeParms = new HashMap<>();
            decodeParms.put("ColorTransform", 0);
            image.decodeParms = decodeParms;
        }
        if (image.getColorSpace() != 1 && image.getColorSpace() != 3 && image.isInverted()) {
            image.decode = new float[]{1.0f, 0.0f, 1.0f, 0.0f, 1.0f, 0.0f, 1.0f, 0.0f};
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v56, types: [byte[]] */
    private static void processParameters(InputStream jpegStream, String errorID, ImageData image) throws IOException {
        byte[][] icc = (byte[][]) null;
        if (jpegStream.read() != 255 || jpegStream.read() != 216) {
            throw new com.itextpdf.io.IOException(com.itextpdf.io.IOException._1IsNotAValidJpegFile).setMessageParams(errorID);
        }
        boolean firstPass = true;
        while (true) {
            int v = jpegStream.read();
            if (v < 0) {
                throw new com.itextpdf.io.IOException(com.itextpdf.io.IOException.PrematureEofWhileReadingJpeg);
            }
            if (v == 255) {
                int marker = jpegStream.read();
                if (firstPass && marker == 224) {
                    firstPass = false;
                    int len = getShort(jpegStream);
                    if (len < 16) {
                        StreamUtil.skip(jpegStream, len - 2);
                    } else {
                        byte[] bcomp = new byte[JFIF_ID.length];
                        int r = jpegStream.read(bcomp);
                        if (r != bcomp.length) {
                            throw new com.itextpdf.io.IOException(com.itextpdf.io.IOException._1CorruptedJfifMarker).setMessageParams(errorID);
                        }
                        boolean found = true;
                        int k = 0;
                        while (true) {
                            if (k >= bcomp.length) {
                                break;
                            }
                            if (bcomp[k] == JFIF_ID[k]) {
                                k++;
                            } else {
                                found = false;
                                break;
                            }
                        }
                        if (!found) {
                            StreamUtil.skip(jpegStream, (len - 2) - bcomp.length);
                        } else {
                            StreamUtil.skip(jpegStream, 2L);
                            int units = jpegStream.read();
                            int dx = getShort(jpegStream);
                            int dy = getShort(jpegStream);
                            if (units == 1) {
                                image.setDpi(dx, dy);
                            } else if (units == 2) {
                                image.setDpi((int) ((dx * 2.54f) + 0.5f), (int) ((dy * 2.54f) + 0.5f));
                            }
                            StreamUtil.skip(jpegStream, ((len - 2) - bcomp.length) - 7);
                        }
                    }
                } else if (marker == M_APPE) {
                    int len2 = getShort(jpegStream) - 2;
                    byte[] byteappe = new byte[len2];
                    for (int k2 = 0; k2 < len2; k2++) {
                        byteappe[k2] = (byte) jpegStream.read();
                    }
                    if (byteappe.length >= 12) {
                        String appe = new String(byteappe, 0, 5, "ISO-8859-1");
                        if (appe.equals(AdobeJpegReader.PREAMBLE)) {
                            image.setInverted(true);
                        }
                    }
                } else if (marker == 226) {
                    int len3 = getShort(jpegStream) - 2;
                    byte[] byteapp2 = new byte[len3];
                    for (int k3 = 0; k3 < len3; k3++) {
                        byteapp2[k3] = (byte) jpegStream.read();
                    }
                    if (byteapp2.length >= 14) {
                        String app2 = new String(byteapp2, 0, 11, "ISO-8859-1");
                        if (app2.equals(IccReader.JPEG_SEGMENT_PREAMBLE)) {
                            int order = byteapp2[12] & 255;
                            int count = byteapp2[13] & 255;
                            if (order < 1) {
                                order = 1;
                            }
                            if (count < 1) {
                                count = 1;
                            }
                            if (icc == null) {
                                icc = new byte[count];
                            }
                            icc[order - 1] = byteapp2;
                        }
                    }
                } else if (marker == 237) {
                    int len4 = getShort(jpegStream) - 2;
                    byte[] byteappd = new byte[len4];
                    for (int k4 = 0; k4 < len4; k4++) {
                        byteappd[k4] = (byte) jpegStream.read();
                    }
                    int k5 = 0;
                    while (k5 < len4 - PS_8BIM_RESO.length) {
                        boolean found2 = true;
                        int j = 0;
                        while (true) {
                            if (j >= PS_8BIM_RESO.length) {
                                break;
                            }
                            if (byteappd[k5 + j] == PS_8BIM_RESO[j]) {
                                j++;
                            } else {
                                found2 = false;
                                break;
                            }
                        }
                        if (found2) {
                            break;
                        } else {
                            k5++;
                        }
                    }
                    int k6 = k5 + PS_8BIM_RESO.length;
                    if (k6 < len4 - PS_8BIM_RESO.length) {
                        byte namelength = (byte) (byteappd[k6] + 1);
                        if (namelength % 2 == 1) {
                            namelength = (byte) (namelength + 1);
                        }
                        int k7 = k6 + namelength;
                        int resosize = (byteappd[k7] << 24) + (byteappd[k7 + 1] << 16) + (byteappd[k7 + 2] << 8) + byteappd[k7 + 3];
                        if (resosize == 16) {
                            int k8 = k7 + 4;
                            int dx2 = (byteappd[k8] << 8) + (byteappd[k8 + 1] & 255);
                            int k9 = k8 + 2 + 2;
                            int unitsx = (byteappd[k9] << 8) + (byteappd[k9 + 1] & 255);
                            int k10 = k9 + 2 + 2;
                            int dy2 = (byteappd[k10] << 8) + (byteappd[k10 + 1] & 255);
                            int k11 = k10 + 2 + 2;
                            int unitsy = (byteappd[k11] << 8) + (byteappd[k11 + 1] & 255);
                            if (unitsx == 1 || unitsx == 2) {
                                dx2 = unitsx == 2 ? (int) ((dx2 * 2.54f) + 0.5f) : dx2;
                                if (image.getDpiX() != 0 && image.getDpiX() != dx2) {
                                    Logger logger = LoggerFactory.getLogger((Class<?>) JpegImageHelper.class);
                                    logger.debug(MessageFormatUtil.format("Inconsistent metadata (dpiX: {0} vs {1})", Integer.valueOf(image.getDpiX()), Integer.valueOf(dx2)));
                                } else {
                                    image.setDpi(dx2, image.getDpiY());
                                }
                            }
                            if (unitsy == 1 || unitsy == 2) {
                                int dy3 = unitsy == 2 ? (int) ((dy2 * 2.54f) + 0.5f) : dy2;
                                if (image.getDpiY() != 0 && image.getDpiY() != dy3) {
                                    Logger logger2 = LoggerFactory.getLogger((Class<?>) JpegImageHelper.class);
                                    logger2.debug(MessageFormatUtil.format("Inconsistent metadata (dpiY: {0} vs {1})", Integer.valueOf(image.getDpiY()), Integer.valueOf(dy3)));
                                } else {
                                    image.setDpi(image.getDpiX(), dx2);
                                }
                            }
                        }
                    }
                } else {
                    firstPass = false;
                    int markertype = marker(marker);
                    if (markertype == 0) {
                        StreamUtil.skip(jpegStream, 2L);
                        if (jpegStream.read() != 8) {
                            throw new com.itextpdf.io.IOException(com.itextpdf.io.IOException._1MustHave8BitsPerComponent).setMessageParams(errorID);
                        }
                        image.setHeight(getShort(jpegStream));
                        image.setWidth(getShort(jpegStream));
                        image.setColorSpace(jpegStream.read());
                        image.setBpc(8);
                        if (icc != null) {
                            int total = 0;
                            for (int k12 = 0; k12 < icc.length; k12++) {
                                if (icc[k12] == null) {
                                    return;
                                }
                                total += icc[k12].length - 14;
                            }
                            byte[] ficc = new byte[total];
                            int total2 = 0;
                            for (int k13 = 0; k13 < icc.length; k13++) {
                                System.arraycopy(icc[k13], 14, ficc, total2, icc[k13].length - 14);
                                total2 += icc[k13].length - 14;
                            }
                            try {
                                image.setProfile(IccProfile.getInstance(ficc, image.getColorSpace()));
                                return;
                            } catch (IllegalArgumentException e) {
                                return;
                            }
                        }
                        return;
                    }
                    if (markertype == 1) {
                        throw new com.itextpdf.io.IOException(com.itextpdf.io.IOException._1UnsupportedJpegMarker2).setMessageParams(errorID, Integer.toString(marker));
                    }
                    if (markertype != 2) {
                        StreamUtil.skip(jpegStream, getShort(jpegStream) - 2);
                    }
                }
            }
        }
    }

    private static int getShort(InputStream jpegStream) throws IOException {
        return (jpegStream.read() << 8) + jpegStream.read();
    }

    private static int marker(int marker) {
        for (int i = 0; i < VALID_MARKERS.length; i++) {
            if (marker == VALID_MARKERS[i]) {
                return 0;
            }
        }
        for (int i2 = 0; i2 < NOPARAM_MARKERS.length; i2++) {
            if (marker == NOPARAM_MARKERS[i2]) {
                return 2;
            }
        }
        for (int i3 = 0; i3 < UNSUPPORTED_MARKERS.length; i3++) {
            if (marker == UNSUPPORTED_MARKERS[i3]) {
                return 1;
            }
        }
        return -1;
    }
}
