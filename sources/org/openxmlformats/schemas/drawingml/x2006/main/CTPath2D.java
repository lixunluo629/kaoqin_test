package org.openxmlformats.schemas.drawingml.x2006.main;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URL;
import java.util.List;
import javax.xml.stream.XMLStreamReader;
import org.apache.poi.POIXMLTypeLoader;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.XmlBeans;
import org.apache.xmlbeans.XmlBoolean;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.xml.stream.XMLInputStream;
import org.apache.xmlbeans.xml.stream.XMLStreamException;
import org.w3c.dom.Node;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/main/CTPath2D.class */
public interface CTPath2D extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTPath2D.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("ctpath2d73d2type");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/main/CTPath2D$Factory.class */
    public static final class Factory {
        public static CTPath2D newInstance() {
            return (CTPath2D) POIXMLTypeLoader.newInstance(CTPath2D.type, null);
        }

        public static CTPath2D newInstance(XmlOptions xmlOptions) {
            return (CTPath2D) POIXMLTypeLoader.newInstance(CTPath2D.type, xmlOptions);
        }

        public static CTPath2D parse(String str) throws XmlException {
            return (CTPath2D) POIXMLTypeLoader.parse(str, CTPath2D.type, (XmlOptions) null);
        }

        public static CTPath2D parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTPath2D) POIXMLTypeLoader.parse(str, CTPath2D.type, xmlOptions);
        }

        public static CTPath2D parse(File file) throws XmlException, IOException {
            return (CTPath2D) POIXMLTypeLoader.parse(file, CTPath2D.type, (XmlOptions) null);
        }

        public static CTPath2D parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTPath2D) POIXMLTypeLoader.parse(file, CTPath2D.type, xmlOptions);
        }

        public static CTPath2D parse(URL url) throws XmlException, IOException {
            return (CTPath2D) POIXMLTypeLoader.parse(url, CTPath2D.type, (XmlOptions) null);
        }

        public static CTPath2D parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTPath2D) POIXMLTypeLoader.parse(url, CTPath2D.type, xmlOptions);
        }

        public static CTPath2D parse(InputStream inputStream) throws XmlException, IOException {
            return (CTPath2D) POIXMLTypeLoader.parse(inputStream, CTPath2D.type, (XmlOptions) null);
        }

        public static CTPath2D parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTPath2D) POIXMLTypeLoader.parse(inputStream, CTPath2D.type, xmlOptions);
        }

        public static CTPath2D parse(Reader reader) throws XmlException, IOException {
            return (CTPath2D) POIXMLTypeLoader.parse(reader, CTPath2D.type, (XmlOptions) null);
        }

        public static CTPath2D parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTPath2D) POIXMLTypeLoader.parse(reader, CTPath2D.type, xmlOptions);
        }

        public static CTPath2D parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTPath2D) POIXMLTypeLoader.parse(xMLStreamReader, CTPath2D.type, (XmlOptions) null);
        }

        public static CTPath2D parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTPath2D) POIXMLTypeLoader.parse(xMLStreamReader, CTPath2D.type, xmlOptions);
        }

        public static CTPath2D parse(Node node) throws XmlException {
            return (CTPath2D) POIXMLTypeLoader.parse(node, CTPath2D.type, (XmlOptions) null);
        }

        public static CTPath2D parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTPath2D) POIXMLTypeLoader.parse(node, CTPath2D.type, xmlOptions);
        }

        public static CTPath2D parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTPath2D) POIXMLTypeLoader.parse(xMLInputStream, CTPath2D.type, (XmlOptions) null);
        }

        public static CTPath2D parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTPath2D) POIXMLTypeLoader.parse(xMLInputStream, CTPath2D.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTPath2D.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTPath2D.type, xmlOptions);
        }

        private Factory() {
        }
    }

    List<CTPath2DClose> getCloseList();

    CTPath2DClose[] getCloseArray();

    CTPath2DClose getCloseArray(int i);

    int sizeOfCloseArray();

    void setCloseArray(CTPath2DClose[] cTPath2DCloseArr);

    void setCloseArray(int i, CTPath2DClose cTPath2DClose);

    CTPath2DClose insertNewClose(int i);

    CTPath2DClose addNewClose();

    void removeClose(int i);

    List<CTPath2DMoveTo> getMoveToList();

    CTPath2DMoveTo[] getMoveToArray();

    CTPath2DMoveTo getMoveToArray(int i);

    int sizeOfMoveToArray();

    void setMoveToArray(CTPath2DMoveTo[] cTPath2DMoveToArr);

    void setMoveToArray(int i, CTPath2DMoveTo cTPath2DMoveTo);

    CTPath2DMoveTo insertNewMoveTo(int i);

    CTPath2DMoveTo addNewMoveTo();

    void removeMoveTo(int i);

    List<CTPath2DLineTo> getLnToList();

    CTPath2DLineTo[] getLnToArray();

    CTPath2DLineTo getLnToArray(int i);

    int sizeOfLnToArray();

    void setLnToArray(CTPath2DLineTo[] cTPath2DLineToArr);

    void setLnToArray(int i, CTPath2DLineTo cTPath2DLineTo);

    CTPath2DLineTo insertNewLnTo(int i);

    CTPath2DLineTo addNewLnTo();

    void removeLnTo(int i);

    List<CTPath2DArcTo> getArcToList();

    CTPath2DArcTo[] getArcToArray();

    CTPath2DArcTo getArcToArray(int i);

    int sizeOfArcToArray();

    void setArcToArray(CTPath2DArcTo[] cTPath2DArcToArr);

    void setArcToArray(int i, CTPath2DArcTo cTPath2DArcTo);

    CTPath2DArcTo insertNewArcTo(int i);

    CTPath2DArcTo addNewArcTo();

    void removeArcTo(int i);

    List<CTPath2DQuadBezierTo> getQuadBezToList();

    CTPath2DQuadBezierTo[] getQuadBezToArray();

    CTPath2DQuadBezierTo getQuadBezToArray(int i);

    int sizeOfQuadBezToArray();

    void setQuadBezToArray(CTPath2DQuadBezierTo[] cTPath2DQuadBezierToArr);

    void setQuadBezToArray(int i, CTPath2DQuadBezierTo cTPath2DQuadBezierTo);

    CTPath2DQuadBezierTo insertNewQuadBezTo(int i);

    CTPath2DQuadBezierTo addNewQuadBezTo();

    void removeQuadBezTo(int i);

    List<CTPath2DCubicBezierTo> getCubicBezToList();

    CTPath2DCubicBezierTo[] getCubicBezToArray();

    CTPath2DCubicBezierTo getCubicBezToArray(int i);

    int sizeOfCubicBezToArray();

    void setCubicBezToArray(CTPath2DCubicBezierTo[] cTPath2DCubicBezierToArr);

    void setCubicBezToArray(int i, CTPath2DCubicBezierTo cTPath2DCubicBezierTo);

    CTPath2DCubicBezierTo insertNewCubicBezTo(int i);

    CTPath2DCubicBezierTo addNewCubicBezTo();

    void removeCubicBezTo(int i);

    long getW();

    STPositiveCoordinate xgetW();

    boolean isSetW();

    void setW(long j);

    void xsetW(STPositiveCoordinate sTPositiveCoordinate);

    void unsetW();

    long getH();

    STPositiveCoordinate xgetH();

    boolean isSetH();

    void setH(long j);

    void xsetH(STPositiveCoordinate sTPositiveCoordinate);

    void unsetH();

    STPathFillMode$Enum getFill();

    STPathFillMode xgetFill();

    boolean isSetFill();

    void setFill(STPathFillMode$Enum sTPathFillMode$Enum);

    void xsetFill(STPathFillMode sTPathFillMode);

    void unsetFill();

    boolean getStroke();

    XmlBoolean xgetStroke();

    boolean isSetStroke();

    void setStroke(boolean z);

    void xsetStroke(XmlBoolean xmlBoolean);

    void unsetStroke();

    boolean getExtrusionOk();

    XmlBoolean xgetExtrusionOk();

    boolean isSetExtrusionOk();

    void setExtrusionOk(boolean z);

    void xsetExtrusionOk(XmlBoolean xmlBoolean);

    void unsetExtrusionOk();
}
