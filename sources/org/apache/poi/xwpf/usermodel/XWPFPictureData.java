package org.apache.poi.xwpf.usermodel;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import org.apache.poi.POIXMLDocumentPart;
import org.apache.poi.POIXMLException;
import org.apache.poi.POIXMLRelation;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.openxml4j.opc.PackagePart;
import org.apache.poi.util.IOUtils;

/* loaded from: poi-ooxml-3.17.jar:org/apache/poi/xwpf/usermodel/XWPFPictureData.class */
public class XWPFPictureData extends POIXMLDocumentPart {
    protected static final POIXMLRelation[] RELATIONS = new POIXMLRelation[13];
    private Long checksum;

    static {
        RELATIONS[2] = XWPFRelation.IMAGE_EMF;
        RELATIONS[3] = XWPFRelation.IMAGE_WMF;
        RELATIONS[4] = XWPFRelation.IMAGE_PICT;
        RELATIONS[5] = XWPFRelation.IMAGE_JPEG;
        RELATIONS[6] = XWPFRelation.IMAGE_PNG;
        RELATIONS[7] = XWPFRelation.IMAGE_DIB;
        RELATIONS[8] = XWPFRelation.IMAGE_GIF;
        RELATIONS[9] = XWPFRelation.IMAGE_TIFF;
        RELATIONS[10] = XWPFRelation.IMAGE_EPS;
        RELATIONS[11] = XWPFRelation.IMAGE_BMP;
        RELATIONS[12] = XWPFRelation.IMAGE_WPG;
    }

    protected XWPFPictureData() {
    }

    public XWPFPictureData(PackagePart part) {
        super(part);
    }

    @Override // org.apache.poi.POIXMLDocumentPart
    protected void onDocumentRead() throws IOException {
        super.onDocumentRead();
    }

    public byte[] getData() {
        try {
            return IOUtils.toByteArray(getPackagePart().getInputStream());
        } catch (IOException e) {
            throw new POIXMLException(e);
        }
    }

    public String getFileName() {
        String name = getPackagePart().getPartName().getName();
        return name.substring(name.lastIndexOf(47) + 1);
    }

    public String suggestFileExtension() {
        return getPackagePart().getPartName().getExtension();
    }

    public int getPictureType() {
        String contentType = getPackagePart().getContentType();
        for (int i = 0; i < RELATIONS.length; i++) {
            if (RELATIONS[i] != null && RELATIONS[i].getContentType().equals(contentType)) {
                return i;
            }
        }
        return 0;
    }

    public Long getChecksum() throws IOException {
        if (this.checksum == null) {
            InputStream is = null;
            try {
                try {
                    is = getPackagePart().getInputStream();
                    byte[] data = IOUtils.toByteArray(is);
                    IOUtils.closeQuietly(is);
                    this.checksum = Long.valueOf(IOUtils.calculateChecksum(data));
                } catch (IOException e) {
                    throw new POIXMLException(e);
                }
            } catch (Throwable th) {
                IOUtils.closeQuietly(is);
                throw th;
            }
        }
        return this.checksum;
    }

    public boolean equals(Object obj) throws IOException {
        if (obj == this) {
            return true;
        }
        if (obj == null || !(obj instanceof XWPFPictureData)) {
            return false;
        }
        XWPFPictureData picData = (XWPFPictureData) obj;
        PackagePart foreignPackagePart = picData.getPackagePart();
        PackagePart ownPackagePart = getPackagePart();
        if (foreignPackagePart != null && ownPackagePart == null) {
            return false;
        }
        if (foreignPackagePart == null && ownPackagePart != null) {
            return false;
        }
        if (ownPackagePart != null) {
            OPCPackage foreignPackage = foreignPackagePart.getPackage();
            OPCPackage ownPackage = ownPackagePart.getPackage();
            if (foreignPackage != null && ownPackage == null) {
                return false;
            }
            if (foreignPackage == null && ownPackage != null) {
                return false;
            }
            if (ownPackage != null && !ownPackage.equals(foreignPackage)) {
                return false;
            }
        }
        Long foreignChecksum = picData.getChecksum();
        Long localChecksum = getChecksum();
        if (!localChecksum.equals(foreignChecksum)) {
            return false;
        }
        return Arrays.equals(getData(), picData.getData());
    }

    public int hashCode() {
        return getChecksum().hashCode();
    }

    @Override // org.apache.poi.POIXMLDocumentPart
    protected void prepareForCommit() {
    }
}
