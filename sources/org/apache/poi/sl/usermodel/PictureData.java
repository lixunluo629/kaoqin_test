package org.apache.poi.sl.usermodel;

import java.awt.Dimension;
import java.io.IOException;
import org.apache.poi.openxml4j.opc.ContentTypes;

/* loaded from: poi-3.17.jar:org/apache/poi/sl/usermodel/PictureData.class */
public interface PictureData {
    String getContentType();

    PictureType getType();

    byte[] getData();

    void setData(byte[] bArr) throws IOException;

    byte[] getChecksum();

    Dimension getImageDimension();

    Dimension getImageDimensionInPixels();

    /* loaded from: poi-3.17.jar:org/apache/poi/sl/usermodel/PictureData$PictureType.class */
    public enum PictureType {
        EMF(2, 2, "image/x-emf", ".emf"),
        WMF(3, 3, "image/x-wmf", ".wmf"),
        PICT(4, 4, ContentTypes.IMAGE_PICT, ".pict"),
        JPEG(5, 5, "image/jpeg", ".jpg"),
        PNG(6, 6, "image/png", ".png"),
        DIB(7, 7, "image/dib", ".dib"),
        GIF(-1, 8, "image/gif", ".gif"),
        TIFF(-1, 9, ContentTypes.IMAGE_TIFF, ".tif"),
        EPS(-1, 10, "image/x-eps", ".eps"),
        BMP(-1, 11, "image/x-ms-bmp", ".bmp"),
        WPG(-1, 12, "image/x-wpg", ".wpg"),
        WDP(-1, 13, "image/vnd.ms-photo", ".wdp");

        public final int nativeId;
        public final int ooxmlId;
        public final String contentType;
        public final String extension;

        PictureType(int nativeId, int ooxmlId, String contentType, String extension) {
            this.nativeId = nativeId;
            this.ooxmlId = ooxmlId;
            this.contentType = contentType;
            this.extension = extension;
        }

        public static PictureType forNativeID(int nativeId) {
            PictureType[] arr$ = values();
            for (PictureType ans : arr$) {
                if (ans.nativeId == nativeId) {
                    return ans;
                }
            }
            return null;
        }

        public static PictureType forOoxmlID(int ooxmlId) {
            PictureType[] arr$ = values();
            for (PictureType ans : arr$) {
                if (ans.ooxmlId == ooxmlId) {
                    return ans;
                }
            }
            return null;
        }
    }
}
