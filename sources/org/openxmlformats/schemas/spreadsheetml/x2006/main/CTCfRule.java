package org.openxmlformats.schemas.spreadsheetml.x2006.main;

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
import org.apache.xmlbeans.XmlInt;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.XmlString;
import org.apache.xmlbeans.XmlUnsignedInt;
import org.apache.xmlbeans.xml.stream.XMLInputStream;
import org.apache.xmlbeans.xml.stream.XMLStreamException;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.STCfType;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.STConditionalFormattingOperator;
import org.w3c.dom.Node;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/CTCfRule.class */
public interface CTCfRule extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTCfRule.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("ctcfrule3548type");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/CTCfRule$Factory.class */
    public static final class Factory {
        public static CTCfRule newInstance() {
            return (CTCfRule) POIXMLTypeLoader.newInstance(CTCfRule.type, null);
        }

        public static CTCfRule newInstance(XmlOptions xmlOptions) {
            return (CTCfRule) POIXMLTypeLoader.newInstance(CTCfRule.type, xmlOptions);
        }

        public static CTCfRule parse(String str) throws XmlException {
            return (CTCfRule) POIXMLTypeLoader.parse(str, CTCfRule.type, (XmlOptions) null);
        }

        public static CTCfRule parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTCfRule) POIXMLTypeLoader.parse(str, CTCfRule.type, xmlOptions);
        }

        public static CTCfRule parse(File file) throws XmlException, IOException {
            return (CTCfRule) POIXMLTypeLoader.parse(file, CTCfRule.type, (XmlOptions) null);
        }

        public static CTCfRule parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTCfRule) POIXMLTypeLoader.parse(file, CTCfRule.type, xmlOptions);
        }

        public static CTCfRule parse(URL url) throws XmlException, IOException {
            return (CTCfRule) POIXMLTypeLoader.parse(url, CTCfRule.type, (XmlOptions) null);
        }

        public static CTCfRule parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTCfRule) POIXMLTypeLoader.parse(url, CTCfRule.type, xmlOptions);
        }

        public static CTCfRule parse(InputStream inputStream) throws XmlException, IOException {
            return (CTCfRule) POIXMLTypeLoader.parse(inputStream, CTCfRule.type, (XmlOptions) null);
        }

        public static CTCfRule parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTCfRule) POIXMLTypeLoader.parse(inputStream, CTCfRule.type, xmlOptions);
        }

        public static CTCfRule parse(Reader reader) throws XmlException, IOException {
            return (CTCfRule) POIXMLTypeLoader.parse(reader, CTCfRule.type, (XmlOptions) null);
        }

        public static CTCfRule parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTCfRule) POIXMLTypeLoader.parse(reader, CTCfRule.type, xmlOptions);
        }

        public static CTCfRule parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTCfRule) POIXMLTypeLoader.parse(xMLStreamReader, CTCfRule.type, (XmlOptions) null);
        }

        public static CTCfRule parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTCfRule) POIXMLTypeLoader.parse(xMLStreamReader, CTCfRule.type, xmlOptions);
        }

        public static CTCfRule parse(Node node) throws XmlException {
            return (CTCfRule) POIXMLTypeLoader.parse(node, CTCfRule.type, (XmlOptions) null);
        }

        public static CTCfRule parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTCfRule) POIXMLTypeLoader.parse(node, CTCfRule.type, xmlOptions);
        }

        public static CTCfRule parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTCfRule) POIXMLTypeLoader.parse(xMLInputStream, CTCfRule.type, (XmlOptions) null);
        }

        public static CTCfRule parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTCfRule) POIXMLTypeLoader.parse(xMLInputStream, CTCfRule.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTCfRule.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTCfRule.type, xmlOptions);
        }

        private Factory() {
        }
    }

    List<String> getFormulaList();

    String[] getFormulaArray();

    String getFormulaArray(int i);

    List<STFormula> xgetFormulaList();

    STFormula[] xgetFormulaArray();

    STFormula xgetFormulaArray(int i);

    int sizeOfFormulaArray();

    void setFormulaArray(String[] strArr);

    void setFormulaArray(int i, String str);

    void xsetFormulaArray(STFormula[] sTFormulaArr);

    void xsetFormulaArray(int i, STFormula sTFormula);

    void insertFormula(int i, String str);

    void addFormula(String str);

    STFormula insertNewFormula(int i);

    STFormula addNewFormula();

    void removeFormula(int i);

    CTColorScale getColorScale();

    boolean isSetColorScale();

    void setColorScale(CTColorScale cTColorScale);

    CTColorScale addNewColorScale();

    void unsetColorScale();

    CTDataBar getDataBar();

    boolean isSetDataBar();

    void setDataBar(CTDataBar cTDataBar);

    CTDataBar addNewDataBar();

    void unsetDataBar();

    CTIconSet getIconSet();

    boolean isSetIconSet();

    void setIconSet(CTIconSet cTIconSet);

    CTIconSet addNewIconSet();

    void unsetIconSet();

    CTExtensionList getExtLst();

    boolean isSetExtLst();

    void setExtLst(CTExtensionList cTExtensionList);

    CTExtensionList addNewExtLst();

    void unsetExtLst();

    STCfType.Enum getType();

    STCfType xgetType();

    boolean isSetType();

    void setType(STCfType.Enum r1);

    void xsetType(STCfType sTCfType);

    void unsetType();

    long getDxfId();

    STDxfId xgetDxfId();

    boolean isSetDxfId();

    void setDxfId(long j);

    void xsetDxfId(STDxfId sTDxfId);

    void unsetDxfId();

    int getPriority();

    XmlInt xgetPriority();

    void setPriority(int i);

    void xsetPriority(XmlInt xmlInt);

    boolean getStopIfTrue();

    XmlBoolean xgetStopIfTrue();

    boolean isSetStopIfTrue();

    void setStopIfTrue(boolean z);

    void xsetStopIfTrue(XmlBoolean xmlBoolean);

    void unsetStopIfTrue();

    boolean getAboveAverage();

    XmlBoolean xgetAboveAverage();

    boolean isSetAboveAverage();

    void setAboveAverage(boolean z);

    void xsetAboveAverage(XmlBoolean xmlBoolean);

    void unsetAboveAverage();

    boolean getPercent();

    XmlBoolean xgetPercent();

    boolean isSetPercent();

    void setPercent(boolean z);

    void xsetPercent(XmlBoolean xmlBoolean);

    void unsetPercent();

    boolean getBottom();

    XmlBoolean xgetBottom();

    boolean isSetBottom();

    void setBottom(boolean z);

    void xsetBottom(XmlBoolean xmlBoolean);

    void unsetBottom();

    STConditionalFormattingOperator.Enum getOperator();

    STConditionalFormattingOperator xgetOperator();

    boolean isSetOperator();

    void setOperator(STConditionalFormattingOperator.Enum r1);

    void xsetOperator(STConditionalFormattingOperator sTConditionalFormattingOperator);

    void unsetOperator();

    String getText();

    XmlString xgetText();

    boolean isSetText();

    void setText(String str);

    void xsetText(XmlString xmlString);

    void unsetText();

    STTimePeriod$Enum getTimePeriod();

    STTimePeriod xgetTimePeriod();

    boolean isSetTimePeriod();

    void setTimePeriod(STTimePeriod$Enum sTTimePeriod$Enum);

    void xsetTimePeriod(STTimePeriod sTTimePeriod);

    void unsetTimePeriod();

    long getRank();

    XmlUnsignedInt xgetRank();

    boolean isSetRank();

    void setRank(long j);

    void xsetRank(XmlUnsignedInt xmlUnsignedInt);

    void unsetRank();

    int getStdDev();

    XmlInt xgetStdDev();

    boolean isSetStdDev();

    void setStdDev(int i);

    void xsetStdDev(XmlInt xmlInt);

    void unsetStdDev();

    boolean getEqualAverage();

    XmlBoolean xgetEqualAverage();

    boolean isSetEqualAverage();

    void setEqualAverage(boolean z);

    void xsetEqualAverage(XmlBoolean xmlBoolean);

    void unsetEqualAverage();
}
