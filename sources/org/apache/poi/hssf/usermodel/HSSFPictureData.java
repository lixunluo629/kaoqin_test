package org.apache.poi.hssf.usermodel;

import org.apache.poi.ddf.EscherBitmapBlip;
import org.apache.poi.ddf.EscherBlipRecord;
import org.apache.poi.openxml4j.opc.ContentTypes;
import org.apache.poi.ss.usermodel.PictureData;
import org.apache.poi.util.PngUtils;

/* loaded from: poi-3.17.jar:org/apache/poi/hssf/usermodel/HSSFPictureData.class */
public class HSSFPictureData implements PictureData {
    public static final short MSOBI_WMF = 8544;
    public static final short MSOBI_EMF = 15680;
    public static final short MSOBI_PICT = 21536;
    public static final short MSOBI_PNG = 28160;
    public static final short MSOBI_JPEG = 18080;
    public static final short MSOBI_DIB = 31360;
    public static final short FORMAT_MASK = -16;
    private EscherBlipRecord blip;

    public HSSFPictureData(EscherBlipRecord blip) {
        this.blip = blip;
    }

    @Override // org.apache.poi.ss.usermodel.PictureData
    public byte[] getData() {
        byte[] pictureData = this.blip.getPicturedata();
        if (PngUtils.matchesPngHeader(pictureData, 16)) {
            byte[] png = new byte[pictureData.length - 16];
            System.arraycopy(pictureData, 16, png, 0, png.length);
            pictureData = png;
        }
        return pictureData;
    }

    public int getFormat() {
        return this.blip.getRecordId() - EscherBlipRecord.RECORD_ID_START;
    }

    @Override // org.apache.poi.ss.usermodel.PictureData
    public String suggestFileExtension() {
        switch (this.blip.getRecordId()) {
            case -4070:
                return "emf";
            case -4069:
                return "wmf";
            case -4068:
                return "pict";
            case EscherBitmapBlip.RECORD_ID_JPEG /* -4067 */:
                return ContentTypes.EXTENSION_JPG_2;
            case EscherBitmapBlip.RECORD_ID_PNG /* -4066 */:
                return ContentTypes.EXTENSION_PNG;
            case EscherBitmapBlip.RECORD_ID_DIB /* -4065 */:
                return "dib";
            default:
                return "";
        }
    }

    @Override // org.apache.poi.ss.usermodel.PictureData
    public String getMimeType() {
        switch (this.blip.getRecordId()) {
            case -4070:
                return "image/x-emf";
            case -4069:
                return "image/x-wmf";
            case -4068:
                return "image/x-pict";
            case EscherBitmapBlip.RECORD_ID_JPEG /* -4067 */:
                return "image/jpeg";
            case EscherBitmapBlip.RECORD_ID_PNG /* -4066 */:
                return "image/png";
            case EscherBitmapBlip.RECORD_ID_DIB /* -4065 */:
                return "image/bmp";
            default:
                return "image/unknown";
        }
    }

    @Override // org.apache.poi.ss.usermodel.PictureData
    public int getPictureType() {
        switch (this.blip.getRecordId()) {
            case -4070:
                return 2;
            case -4069:
                return 3;
            case -4068:
                return 4;
            case EscherBitmapBlip.RECORD_ID_JPEG /* -4067 */:
                return 5;
            case EscherBitmapBlip.RECORD_ID_PNG /* -4066 */:
                return 6;
            case EscherBitmapBlip.RECORD_ID_DIB /* -4065 */:
                return 7;
            default:
                return -1;
        }
    }
}
