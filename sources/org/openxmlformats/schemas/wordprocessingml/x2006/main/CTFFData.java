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

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/wordprocessingml/x2006/main/CTFFData.class */
public interface CTFFData extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTFFData.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("ctffdataaa7etype");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/wordprocessingml/x2006/main/CTFFData$Factory.class */
    public static final class Factory {
        public static CTFFData newInstance() {
            return (CTFFData) POIXMLTypeLoader.newInstance(CTFFData.type, null);
        }

        public static CTFFData newInstance(XmlOptions xmlOptions) {
            return (CTFFData) POIXMLTypeLoader.newInstance(CTFFData.type, xmlOptions);
        }

        public static CTFFData parse(String str) throws XmlException {
            return (CTFFData) POIXMLTypeLoader.parse(str, CTFFData.type, (XmlOptions) null);
        }

        public static CTFFData parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTFFData) POIXMLTypeLoader.parse(str, CTFFData.type, xmlOptions);
        }

        public static CTFFData parse(File file) throws XmlException, IOException {
            return (CTFFData) POIXMLTypeLoader.parse(file, CTFFData.type, (XmlOptions) null);
        }

        public static CTFFData parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTFFData) POIXMLTypeLoader.parse(file, CTFFData.type, xmlOptions);
        }

        public static CTFFData parse(URL url) throws XmlException, IOException {
            return (CTFFData) POIXMLTypeLoader.parse(url, CTFFData.type, (XmlOptions) null);
        }

        public static CTFFData parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTFFData) POIXMLTypeLoader.parse(url, CTFFData.type, xmlOptions);
        }

        public static CTFFData parse(InputStream inputStream) throws XmlException, IOException {
            return (CTFFData) POIXMLTypeLoader.parse(inputStream, CTFFData.type, (XmlOptions) null);
        }

        public static CTFFData parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTFFData) POIXMLTypeLoader.parse(inputStream, CTFFData.type, xmlOptions);
        }

        public static CTFFData parse(Reader reader) throws XmlException, IOException {
            return (CTFFData) POIXMLTypeLoader.parse(reader, CTFFData.type, (XmlOptions) null);
        }

        public static CTFFData parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTFFData) POIXMLTypeLoader.parse(reader, CTFFData.type, xmlOptions);
        }

        public static CTFFData parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTFFData) POIXMLTypeLoader.parse(xMLStreamReader, CTFFData.type, (XmlOptions) null);
        }

        public static CTFFData parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTFFData) POIXMLTypeLoader.parse(xMLStreamReader, CTFFData.type, xmlOptions);
        }

        public static CTFFData parse(Node node) throws XmlException {
            return (CTFFData) POIXMLTypeLoader.parse(node, CTFFData.type, (XmlOptions) null);
        }

        public static CTFFData parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTFFData) POIXMLTypeLoader.parse(node, CTFFData.type, xmlOptions);
        }

        public static CTFFData parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTFFData) POIXMLTypeLoader.parse(xMLInputStream, CTFFData.type, (XmlOptions) null);
        }

        public static CTFFData parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTFFData) POIXMLTypeLoader.parse(xMLInputStream, CTFFData.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTFFData.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTFFData.type, xmlOptions);
        }

        private Factory() {
        }
    }

    List<CTFFName> getNameList();

    CTFFName[] getNameArray();

    CTFFName getNameArray(int i);

    int sizeOfNameArray();

    void setNameArray(CTFFName[] cTFFNameArr);

    void setNameArray(int i, CTFFName cTFFName);

    CTFFName insertNewName(int i);

    CTFFName addNewName();

    void removeName(int i);

    List<CTOnOff> getEnabledList();

    CTOnOff[] getEnabledArray();

    CTOnOff getEnabledArray(int i);

    int sizeOfEnabledArray();

    void setEnabledArray(CTOnOff[] cTOnOffArr);

    void setEnabledArray(int i, CTOnOff cTOnOff);

    CTOnOff insertNewEnabled(int i);

    CTOnOff addNewEnabled();

    void removeEnabled(int i);

    List<CTOnOff> getCalcOnExitList();

    CTOnOff[] getCalcOnExitArray();

    CTOnOff getCalcOnExitArray(int i);

    int sizeOfCalcOnExitArray();

    void setCalcOnExitArray(CTOnOff[] cTOnOffArr);

    void setCalcOnExitArray(int i, CTOnOff cTOnOff);

    CTOnOff insertNewCalcOnExit(int i);

    CTOnOff addNewCalcOnExit();

    void removeCalcOnExit(int i);

    List<CTMacroName> getEntryMacroList();

    CTMacroName[] getEntryMacroArray();

    CTMacroName getEntryMacroArray(int i);

    int sizeOfEntryMacroArray();

    void setEntryMacroArray(CTMacroName[] cTMacroNameArr);

    void setEntryMacroArray(int i, CTMacroName cTMacroName);

    CTMacroName insertNewEntryMacro(int i);

    CTMacroName addNewEntryMacro();

    void removeEntryMacro(int i);

    List<CTMacroName> getExitMacroList();

    CTMacroName[] getExitMacroArray();

    CTMacroName getExitMacroArray(int i);

    int sizeOfExitMacroArray();

    void setExitMacroArray(CTMacroName[] cTMacroNameArr);

    void setExitMacroArray(int i, CTMacroName cTMacroName);

    CTMacroName insertNewExitMacro(int i);

    CTMacroName addNewExitMacro();

    void removeExitMacro(int i);

    List<CTFFHelpText> getHelpTextList();

    CTFFHelpText[] getHelpTextArray();

    CTFFHelpText getHelpTextArray(int i);

    int sizeOfHelpTextArray();

    void setHelpTextArray(CTFFHelpText[] cTFFHelpTextArr);

    void setHelpTextArray(int i, CTFFHelpText cTFFHelpText);

    CTFFHelpText insertNewHelpText(int i);

    CTFFHelpText addNewHelpText();

    void removeHelpText(int i);

    List<CTFFStatusText> getStatusTextList();

    CTFFStatusText[] getStatusTextArray();

    CTFFStatusText getStatusTextArray(int i);

    int sizeOfStatusTextArray();

    void setStatusTextArray(CTFFStatusText[] cTFFStatusTextArr);

    void setStatusTextArray(int i, CTFFStatusText cTFFStatusText);

    CTFFStatusText insertNewStatusText(int i);

    CTFFStatusText addNewStatusText();

    void removeStatusText(int i);

    List<CTFFCheckBox> getCheckBoxList();

    CTFFCheckBox[] getCheckBoxArray();

    CTFFCheckBox getCheckBoxArray(int i);

    int sizeOfCheckBoxArray();

    void setCheckBoxArray(CTFFCheckBox[] cTFFCheckBoxArr);

    void setCheckBoxArray(int i, CTFFCheckBox cTFFCheckBox);

    CTFFCheckBox insertNewCheckBox(int i);

    CTFFCheckBox addNewCheckBox();

    void removeCheckBox(int i);

    List<CTFFDDList> getDdListList();

    CTFFDDList[] getDdListArray();

    CTFFDDList getDdListArray(int i);

    int sizeOfDdListArray();

    void setDdListArray(CTFFDDList[] cTFFDDListArr);

    void setDdListArray(int i, CTFFDDList cTFFDDList);

    CTFFDDList insertNewDdList(int i);

    CTFFDDList addNewDdList();

    void removeDdList(int i);

    List<CTFFTextInput> getTextInputList();

    CTFFTextInput[] getTextInputArray();

    CTFFTextInput getTextInputArray(int i);

    int sizeOfTextInputArray();

    void setTextInputArray(CTFFTextInput[] cTFFTextInputArr);

    void setTextInputArray(int i, CTFFTextInput cTFFTextInput);

    CTFFTextInput insertNewTextInput(int i);

    CTFFTextInput addNewTextInput();

    void removeTextInput(int i);
}
