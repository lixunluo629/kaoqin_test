package net.coobird.thumbnailator.util.exif;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import javax.imageio.ImageReader;
import org.w3c.dom.NodeList;

/* loaded from: thumbnailator-0.4.8.jar:net/coobird/thumbnailator/util/exif/ExifUtils.class */
public final class ExifUtils {
    private static final String EXIF_MAGIC_STRING = "Exif";

    private ExifUtils() {
    }

    public static Orientation getExifOrientation(ImageReader imageReader, int i) throws IOException {
        NodeList childNodes = imageReader.getImageMetadata(i).getAsTree("javax_imageio_jpeg_image_1.0").getChildNodes();
        for (int i2 = 0; i2 < childNodes.getLength(); i2++) {
            if ("markerSequence".equals(childNodes.item(i2).getNodeName())) {
                NodeList childNodes2 = childNodes.item(i2).getChildNodes();
                for (int i3 = 0; i3 < childNodes2.getLength(); i3++) {
                    byte[] bArr = (byte[]) childNodes2.item(i3).getUserObject();
                    if (bArr != null) {
                        byte[] bArr2 = new byte[4];
                        ByteBuffer.wrap(bArr).get(bArr2);
                        if (EXIF_MAGIC_STRING.equals(new String(bArr2))) {
                            return getOrientationFromExif(bArr);
                        }
                    }
                }
            }
        }
        return null;
    }

    private static Orientation getOrientationFromExif(byte[] bArr) {
        ByteOrder byteOrder;
        ByteBuffer byteBufferWrap = ByteBuffer.wrap(bArr);
        byte[] bArr2 = new byte[4];
        byteBufferWrap.get(bArr2);
        if (!EXIF_MAGIC_STRING.equals(new String(bArr2))) {
            return null;
        }
        byteBufferWrap.get();
        byteBufferWrap.get();
        byte[] bArr3 = new byte[8];
        byteBufferWrap.get(bArr3);
        if (bArr3[0] == 73 && bArr3[1] == 73) {
            byteOrder = ByteOrder.LITTLE_ENDIAN;
        } else {
            byteOrder = ByteOrder.BIG_ENDIAN;
        }
        byte[] bArr4 = new byte[2];
        byteBufferWrap.get(bArr4);
        int i = ByteBuffer.wrap(bArr4).order(byteOrder).getShort();
        byte[] bArr5 = new byte[12];
        for (int i2 = 0; i2 < i; i2++) {
            byteBufferWrap.get(bArr5);
            IfdStructure ifd = readIFD(bArr5, byteOrder);
            if (ifd.getTag() == 274) {
                return Orientation.typeOf(ifd.getOffsetValue());
            }
        }
        return null;
    }

    private static IfdStructure readIFD(byte[] bArr, ByteOrder byteOrder) {
        ByteBuffer byteBufferOrder = ByteBuffer.wrap(bArr).order(byteOrder);
        short s = byteBufferOrder.getShort();
        short s2 = byteBufferOrder.getShort();
        int i = byteBufferOrder.getInt();
        IfdType ifdTypeTypeOf = IfdType.typeOf(s2);
        short s3 = 0;
        if (i * ifdTypeTypeOf.size() <= 4) {
            if (ifdTypeTypeOf == IfdType.SHORT) {
                for (int i2 = 0; i2 < i; i2++) {
                    s3 = byteBufferOrder.getShort();
                }
            } else if (ifdTypeTypeOf == IfdType.BYTE || ifdTypeTypeOf == IfdType.ASCII || ifdTypeTypeOf == IfdType.UNDEFINED) {
                for (int i3 = 0; i3 < i; i3++) {
                    s3 = byteBufferOrder.get();
                }
            } else {
                s3 = byteBufferOrder.getInt();
            }
        } else {
            s3 = byteBufferOrder.getInt();
        }
        return new IfdStructure(s, s2, i, s3);
    }
}
