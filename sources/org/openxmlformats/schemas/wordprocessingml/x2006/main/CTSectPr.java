package org.openxmlformats.schemas.wordprocessingml.x2006.main;

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
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.xml.stream.XMLInputStream;
import org.apache.xmlbeans.xml.stream.XMLStreamException;
import org.w3c.dom.Node;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/wordprocessingml/x2006/main/CTSectPr.class */
public interface CTSectPr extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTSectPr.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("ctsectpr1123type");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/wordprocessingml/x2006/main/CTSectPr$Factory.class */
    public static final class Factory {
        public static CTSectPr newInstance() {
            return (CTSectPr) POIXMLTypeLoader.newInstance(CTSectPr.type, null);
        }

        public static CTSectPr newInstance(XmlOptions xmlOptions) {
            return (CTSectPr) POIXMLTypeLoader.newInstance(CTSectPr.type, xmlOptions);
        }

        public static CTSectPr parse(String str) throws XmlException {
            return (CTSectPr) POIXMLTypeLoader.parse(str, CTSectPr.type, (XmlOptions) null);
        }

        public static CTSectPr parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTSectPr) POIXMLTypeLoader.parse(str, CTSectPr.type, xmlOptions);
        }

        public static CTSectPr parse(File file) throws XmlException, IOException {
            return (CTSectPr) POIXMLTypeLoader.parse(file, CTSectPr.type, (XmlOptions) null);
        }

        public static CTSectPr parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTSectPr) POIXMLTypeLoader.parse(file, CTSectPr.type, xmlOptions);
        }

        public static CTSectPr parse(URL url) throws XmlException, IOException {
            return (CTSectPr) POIXMLTypeLoader.parse(url, CTSectPr.type, (XmlOptions) null);
        }

        public static CTSectPr parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTSectPr) POIXMLTypeLoader.parse(url, CTSectPr.type, xmlOptions);
        }

        public static CTSectPr parse(InputStream inputStream) throws XmlException, IOException {
            return (CTSectPr) POIXMLTypeLoader.parse(inputStream, CTSectPr.type, (XmlOptions) null);
        }

        public static CTSectPr parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTSectPr) POIXMLTypeLoader.parse(inputStream, CTSectPr.type, xmlOptions);
        }

        public static CTSectPr parse(Reader reader) throws XmlException, IOException {
            return (CTSectPr) POIXMLTypeLoader.parse(reader, CTSectPr.type, (XmlOptions) null);
        }

        public static CTSectPr parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTSectPr) POIXMLTypeLoader.parse(reader, CTSectPr.type, xmlOptions);
        }

        public static CTSectPr parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTSectPr) POIXMLTypeLoader.parse(xMLStreamReader, CTSectPr.type, (XmlOptions) null);
        }

        public static CTSectPr parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTSectPr) POIXMLTypeLoader.parse(xMLStreamReader, CTSectPr.type, xmlOptions);
        }

        public static CTSectPr parse(Node node) throws XmlException {
            return (CTSectPr) POIXMLTypeLoader.parse(node, CTSectPr.type, (XmlOptions) null);
        }

        public static CTSectPr parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTSectPr) POIXMLTypeLoader.parse(node, CTSectPr.type, xmlOptions);
        }

        public static CTSectPr parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTSectPr) POIXMLTypeLoader.parse(xMLInputStream, CTSectPr.type, (XmlOptions) null);
        }

        public static CTSectPr parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTSectPr) POIXMLTypeLoader.parse(xMLInputStream, CTSectPr.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTSectPr.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTSectPr.type, xmlOptions);
        }

        private Factory() {
        }
    }

    List<CTHdrFtrRef> getHeaderReferenceList();

    CTHdrFtrRef[] getHeaderReferenceArray();

    CTHdrFtrRef getHeaderReferenceArray(int i);

    int sizeOfHeaderReferenceArray();

    void setHeaderReferenceArray(CTHdrFtrRef[] cTHdrFtrRefArr);

    void setHeaderReferenceArray(int i, CTHdrFtrRef cTHdrFtrRef);

    CTHdrFtrRef insertNewHeaderReference(int i);

    CTHdrFtrRef addNewHeaderReference();

    void removeHeaderReference(int i);

    List<CTHdrFtrRef> getFooterReferenceList();

    CTHdrFtrRef[] getFooterReferenceArray();

    CTHdrFtrRef getFooterReferenceArray(int i);

    int sizeOfFooterReferenceArray();

    void setFooterReferenceArray(CTHdrFtrRef[] cTHdrFtrRefArr);

    void setFooterReferenceArray(int i, CTHdrFtrRef cTHdrFtrRef);

    CTHdrFtrRef insertNewFooterReference(int i);

    CTHdrFtrRef addNewFooterReference();

    void removeFooterReference(int i);

    CTFtnProps getFootnotePr();

    boolean isSetFootnotePr();

    void setFootnotePr(CTFtnProps cTFtnProps);

    CTFtnProps addNewFootnotePr();

    void unsetFootnotePr();

    CTEdnProps getEndnotePr();

    boolean isSetEndnotePr();

    void setEndnotePr(CTEdnProps cTEdnProps);

    CTEdnProps addNewEndnotePr();

    void unsetEndnotePr();

    CTSectType getType();

    boolean isSetType();

    void setType(CTSectType cTSectType);

    CTSectType addNewType();

    void unsetType();

    CTPageSz getPgSz();

    boolean isSetPgSz();

    void setPgSz(CTPageSz cTPageSz);

    CTPageSz addNewPgSz();

    void unsetPgSz();

    CTPageMar getPgMar();

    boolean isSetPgMar();

    void setPgMar(CTPageMar cTPageMar);

    CTPageMar addNewPgMar();

    void unsetPgMar();

    CTPaperSource getPaperSrc();

    boolean isSetPaperSrc();

    void setPaperSrc(CTPaperSource cTPaperSource);

    CTPaperSource addNewPaperSrc();

    void unsetPaperSrc();

    CTPageBorders getPgBorders();

    boolean isSetPgBorders();

    void setPgBorders(CTPageBorders cTPageBorders);

    CTPageBorders addNewPgBorders();

    void unsetPgBorders();

    CTLineNumber getLnNumType();

    boolean isSetLnNumType();

    void setLnNumType(CTLineNumber cTLineNumber);

    CTLineNumber addNewLnNumType();

    void unsetLnNumType();

    CTPageNumber getPgNumType();

    boolean isSetPgNumType();

    void setPgNumType(CTPageNumber cTPageNumber);

    CTPageNumber addNewPgNumType();

    void unsetPgNumType();

    CTColumns getCols();

    boolean isSetCols();

    void setCols(CTColumns cTColumns);

    CTColumns addNewCols();

    void unsetCols();

    CTOnOff getFormProt();

    boolean isSetFormProt();

    void setFormProt(CTOnOff cTOnOff);

    CTOnOff addNewFormProt();

    void unsetFormProt();

    CTVerticalJc getVAlign();

    boolean isSetVAlign();

    void setVAlign(CTVerticalJc cTVerticalJc);

    CTVerticalJc addNewVAlign();

    void unsetVAlign();

    CTOnOff getNoEndnote();

    boolean isSetNoEndnote();

    void setNoEndnote(CTOnOff cTOnOff);

    CTOnOff addNewNoEndnote();

    void unsetNoEndnote();

    CTOnOff getTitlePg();

    boolean isSetTitlePg();

    void setTitlePg(CTOnOff cTOnOff);

    CTOnOff addNewTitlePg();

    void unsetTitlePg();

    CTTextDirection getTextDirection();

    boolean isSetTextDirection();

    void setTextDirection(CTTextDirection cTTextDirection);

    CTTextDirection addNewTextDirection();

    void unsetTextDirection();

    CTOnOff getBidi();

    boolean isSetBidi();

    void setBidi(CTOnOff cTOnOff);

    CTOnOff addNewBidi();

    void unsetBidi();

    CTOnOff getRtlGutter();

    boolean isSetRtlGutter();

    void setRtlGutter(CTOnOff cTOnOff);

    CTOnOff addNewRtlGutter();

    void unsetRtlGutter();

    CTDocGrid getDocGrid();

    boolean isSetDocGrid();

    void setDocGrid(CTDocGrid cTDocGrid);

    CTDocGrid addNewDocGrid();

    void unsetDocGrid();

    CTRel getPrinterSettings();

    boolean isSetPrinterSettings();

    void setPrinterSettings(CTRel cTRel);

    CTRel addNewPrinterSettings();

    void unsetPrinterSettings();

    CTSectPrChange getSectPrChange();

    boolean isSetSectPrChange();

    void setSectPrChange(CTSectPrChange cTSectPrChange);

    CTSectPrChange addNewSectPrChange();

    void unsetSectPrChange();

    byte[] getRsidRPr();

    STLongHexNumber xgetRsidRPr();

    boolean isSetRsidRPr();

    void setRsidRPr(byte[] bArr);

    void xsetRsidRPr(STLongHexNumber sTLongHexNumber);

    void unsetRsidRPr();

    byte[] getRsidDel();

    STLongHexNumber xgetRsidDel();

    boolean isSetRsidDel();

    void setRsidDel(byte[] bArr);

    void xsetRsidDel(STLongHexNumber sTLongHexNumber);

    void unsetRsidDel();

    byte[] getRsidR();

    STLongHexNumber xgetRsidR();

    boolean isSetRsidR();

    void setRsidR(byte[] bArr);

    void xsetRsidR(STLongHexNumber sTLongHexNumber);

    void unsetRsidR();

    byte[] getRsidSect();

    STLongHexNumber xgetRsidSect();

    boolean isSetRsidSect();

    void setRsidSect(byte[] bArr);

    void xsetRsidSect(STLongHexNumber sTLongHexNumber);

    void unsetRsidSect();
}
