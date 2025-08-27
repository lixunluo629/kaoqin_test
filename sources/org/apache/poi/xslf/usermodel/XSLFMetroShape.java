package org.apache.poi.xslf.usermodel;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import org.apache.poi.POIXMLTypeLoader;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.openxml4j.opc.PackagePart;
import org.apache.poi.openxml4j.opc.PackagePartName;
import org.apache.poi.openxml4j.opc.PackagingURIHelper;
import org.apache.poi.sl.usermodel.Shape;
import org.apache.poi.util.Internal;
import org.apache.xmlbeans.XmlException;
import org.openxmlformats.schemas.presentationml.x2006.main.CTGroupShape;

@Internal
/* loaded from: poi-ooxml-3.17.jar:org/apache/poi/xslf/usermodel/XSLFMetroShape.class */
public class XSLFMetroShape {
    public static Shape<?, ?> parseShape(byte[] metroBytes) throws XmlException, InvalidFormatException, IOException {
        PackagePartName shapePN = PackagingURIHelper.createPartName("/drs/shapexml.xml");
        OPCPackage pkg = null;
        try {
            pkg = OPCPackage.open(new ByteArrayInputStream(metroBytes));
            PackagePart shapePart = pkg.getPart(shapePN);
            CTGroupShape gs = CTGroupShape.Factory.parse(shapePart.getInputStream(), POIXMLTypeLoader.DEFAULT_XML_OPTIONS);
            XSLFGroupShape xgs = new XSLFGroupShape(gs, null);
            XSLFShape xSLFShape = xgs.getShapes().get(0);
            if (pkg != null) {
                pkg.close();
            }
            return xSLFShape;
        } catch (Throwable th) {
            if (pkg != null) {
                pkg.close();
            }
            throw th;
        }
    }
}
