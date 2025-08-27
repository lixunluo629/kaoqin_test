package org.apache.poi.xssf.usermodel;

import java.io.IOException;
import org.apache.poi.POIXMLDocumentPart;
import org.apache.poi.POIXMLException;
import org.apache.poi.POIXMLRelation;
import org.apache.poi.openxml4j.opc.PackagePart;
import org.apache.poi.ss.usermodel.PictureData;
import org.apache.poi.util.IOUtils;

/* loaded from: poi-ooxml-3.17.jar:org/apache/poi/xssf/usermodel/XSSFPictureData.class */
public class XSSFPictureData extends POIXMLDocumentPart implements PictureData {
    protected static final POIXMLRelation[] RELATIONS = new POIXMLRelation[13];

    static {
        RELATIONS[2] = XSSFRelation.IMAGE_EMF;
        RELATIONS[3] = XSSFRelation.IMAGE_WMF;
        RELATIONS[4] = XSSFRelation.IMAGE_PICT;
        RELATIONS[5] = XSSFRelation.IMAGE_JPEG;
        RELATIONS[6] = XSSFRelation.IMAGE_PNG;
        RELATIONS[7] = XSSFRelation.IMAGE_DIB;
        RELATIONS[8] = XSSFRelation.IMAGE_GIF;
        RELATIONS[9] = XSSFRelation.IMAGE_TIFF;
        RELATIONS[10] = XSSFRelation.IMAGE_EPS;
        RELATIONS[11] = XSSFRelation.IMAGE_BMP;
        RELATIONS[12] = XSSFRelation.IMAGE_WPG;
    }

    protected XSSFPictureData() {
    }

    protected XSSFPictureData(PackagePart part) {
        super(part);
    }

    @Override // org.apache.poi.ss.usermodel.PictureData
    public byte[] getData() {
        try {
            return IOUtils.toByteArray(getPackagePart().getInputStream());
        } catch (IOException e) {
            throw new POIXMLException(e);
        }
    }

    @Override // org.apache.poi.ss.usermodel.PictureData
    public String suggestFileExtension() {
        return getPackagePart().getPartName().getExtension();
    }

    @Override // org.apache.poi.ss.usermodel.PictureData
    public int getPictureType() {
        String contentType = getPackagePart().getContentType();
        for (int i = 0; i < RELATIONS.length; i++) {
            if (RELATIONS[i] != null && RELATIONS[i].getContentType().equals(contentType)) {
                return i;
            }
        }
        return 0;
    }

    @Override // org.apache.poi.ss.usermodel.PictureData
    public String getMimeType() {
        return getPackagePart().getContentType();
    }

    @Override // org.apache.poi.POIXMLDocumentPart
    protected void prepareForCommit() {
    }
}
