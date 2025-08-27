package org.apache.poi.openxml4j.opc.internal;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import org.apache.poi.EmptyFileException;
import org.apache.poi.openxml4j.exceptions.NotOfficeXmlFileException;
import org.apache.poi.openxml4j.exceptions.OLE2NotOfficeXmlFileException;
import org.apache.poi.openxml4j.opc.PackageRelationship;
import org.apache.poi.openxml4j.opc.PackageRelationshipTypes;
import org.apache.poi.openxml4j.opc.ZipPackage;
import org.apache.poi.openxml4j.util.ZipSecureFile;
import org.apache.poi.poifs.filesystem.FileMagic;
import org.apache.poi.util.Internal;
import org.apache.poi.util.Removal;

@Internal
/* loaded from: poi-ooxml-3.17.jar:org/apache/poi/openxml4j/opc/internal/ZipHelper.class */
public final class ZipHelper {
    private static final String FORWARD_SLASH = "/";

    @Removal(version = "3.18")
    @Deprecated
    public static final int READ_WRITE_FILE_BUFFER_SIZE = 8192;

    private ZipHelper() {
    }

    public static ZipEntry getCorePropertiesZipEntry(ZipPackage pkg) {
        PackageRelationship corePropsRel = pkg.getRelationshipsByType(PackageRelationshipTypes.CORE_PROPERTIES).getRelationship(0);
        if (corePropsRel == null) {
            return null;
        }
        return new ZipEntry(corePropsRel.getTargetURI().getPath());
    }

    public static ZipEntry getContentTypeZipEntry(ZipPackage pkg) {
        Enumeration<? extends ZipEntry> entries = pkg.getZipArchive().getEntries();
        while (entries.hasMoreElements()) {
            ZipEntry entry = entries.nextElement();
            if (entry.getName().equals(ContentTypeManager.CONTENT_TYPES_PART_NAME)) {
                return entry;
            }
        }
        return null;
    }

    public static String getOPCNameFromZipItemName(String zipItemName) {
        if (zipItemName == null) {
            throw new IllegalArgumentException("zipItemName cannot be null");
        }
        if (zipItemName.startsWith("/")) {
            return zipItemName;
        }
        return "/" + zipItemName;
    }

    public static String getZipItemNameFromOPCName(String opcItemName) {
        if (opcItemName == null) {
            throw new IllegalArgumentException("opcItemName cannot be null");
        }
        String strSubstring = opcItemName;
        while (true) {
            String retVal = strSubstring;
            if (retVal.startsWith("/")) {
                strSubstring = retVal.substring(1);
            } else {
                return retVal;
            }
        }
    }

    public static URI getZipURIFromOPCName(String opcItemName) {
        if (opcItemName == null) {
            throw new IllegalArgumentException("opcItemName");
        }
        String strSubstring = opcItemName;
        while (true) {
            String retVal = strSubstring;
            if (retVal.startsWith("/")) {
                strSubstring = retVal.substring(1);
            } else {
                try {
                    return new URI(retVal);
                } catch (URISyntaxException e) {
                    return null;
                }
            }
        }
    }

    public static void verifyZipHeader(InputStream stream) throws NotOfficeXmlFileException, EmptyFileException, IOException, CloneNotSupportedException {
        InputStream is = FileMagic.prepareToCheckMagic(stream);
        FileMagic fm = FileMagic.valueOf(is);
        switch (fm) {
            case OLE2:
                throw new OLE2NotOfficeXmlFileException("The supplied data appears to be in the OLE2 Format. You are calling the part of POI that deals with OOXML (Office Open XML) Documents. You need to call a different part of POI to process this data (eg HSSF instead of XSSF)");
            case XML:
                throw new NotOfficeXmlFileException("The supplied data appears to be a raw XML file. Formats such as Office 2003 XML are not supported");
            case OOXML:
            case UNKNOWN:
            default:
                return;
        }
    }

    public static ZipSecureFile.ThresholdInputStream openZipStream(InputStream stream) throws IOException {
        InputStream checkedStream = FileMagic.prepareToCheckMagic(stream);
        verifyZipHeader(checkedStream);
        InputStream zis = new ZipInputStream(checkedStream);
        return ZipSecureFile.addThreshold(zis);
    }

    public static ZipFile openZipFile(File file) throws NotOfficeXmlFileException, IOException {
        if (!file.exists()) {
            throw new FileNotFoundException("File does not exist");
        }
        if (file.isDirectory()) {
            throw new IOException("File is a directory");
        }
        FileInputStream input = new FileInputStream(file);
        try {
            verifyZipHeader(input);
            input.close();
            return new ZipSecureFile(file);
        } catch (Throwable th) {
            input.close();
            throw th;
        }
    }

    public static ZipFile openZipFile(String path) throws IOException {
        return openZipFile(new File(path));
    }
}
