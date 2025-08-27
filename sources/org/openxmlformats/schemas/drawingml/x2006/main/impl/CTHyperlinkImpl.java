package org.openxmlformats.schemas.drawingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.poi.openxml4j.opc.PackageRelationshipTypes;
import org.apache.poi.xssf.usermodel.XSSFRelation;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.SimpleValue;
import org.apache.xmlbeans.XmlBoolean;
import org.apache.xmlbeans.XmlString;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;
import org.openxmlformats.schemas.drawingml.x2006.main.CTEmbeddedWAVAudioFile;
import org.openxmlformats.schemas.drawingml.x2006.main.CTHyperlink;
import org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeArtExtensionList;
import org.openxmlformats.schemas.officeDocument.x2006.relationships.STRelationshipId;
import org.springframework.web.servlet.mvc.multiaction.ParameterMethodNameResolver;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/main/impl/CTHyperlinkImpl.class */
public class CTHyperlinkImpl extends XmlComplexContentImpl implements CTHyperlink {
    private static final QName SND$0 = new QName(XSSFRelation.NS_DRAWINGML, "snd");
    private static final QName EXTLST$2 = new QName(XSSFRelation.NS_DRAWINGML, "extLst");
    private static final QName ID$4 = new QName(PackageRelationshipTypes.CORE_PROPERTIES_ECMA376_NS, "id");
    private static final QName INVALIDURL$6 = new QName("", "invalidUrl");
    private static final QName ACTION$8 = new QName("", ParameterMethodNameResolver.DEFAULT_PARAM_NAME);
    private static final QName TGTFRAME$10 = new QName("", "tgtFrame");
    private static final QName TOOLTIP$12 = new QName("", "tooltip");
    private static final QName HISTORY$14 = new QName("", "history");
    private static final QName HIGHLIGHTCLICK$16 = new QName("", "highlightClick");
    private static final QName ENDSND$18 = new QName("", "endSnd");

    public CTHyperlinkImpl(SchemaType schemaType) {
        super(schemaType);
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTHyperlink
    public CTEmbeddedWAVAudioFile getSnd() {
        synchronized (monitor()) {
            check_orphaned();
            CTEmbeddedWAVAudioFile cTEmbeddedWAVAudioFileFind_element_user = get_store().find_element_user(SND$0, 0);
            if (cTEmbeddedWAVAudioFileFind_element_user == null) {
                return null;
            }
            return cTEmbeddedWAVAudioFileFind_element_user;
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTHyperlink
    public boolean isSetSnd() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().count_elements(SND$0) != 0;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTHyperlink
    public void setSnd(CTEmbeddedWAVAudioFile cTEmbeddedWAVAudioFile) {
        synchronized (monitor()) {
            check_orphaned();
            CTEmbeddedWAVAudioFile cTEmbeddedWAVAudioFileFind_element_user = get_store().find_element_user(SND$0, 0);
            if (cTEmbeddedWAVAudioFileFind_element_user == null) {
                cTEmbeddedWAVAudioFileFind_element_user = (CTEmbeddedWAVAudioFile) get_store().add_element_user(SND$0);
            }
            cTEmbeddedWAVAudioFileFind_element_user.set(cTEmbeddedWAVAudioFile);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTHyperlink
    public CTEmbeddedWAVAudioFile addNewSnd() {
        CTEmbeddedWAVAudioFile cTEmbeddedWAVAudioFileAdd_element_user;
        synchronized (monitor()) {
            check_orphaned();
            cTEmbeddedWAVAudioFileAdd_element_user = get_store().add_element_user(SND$0);
        }
        return cTEmbeddedWAVAudioFileAdd_element_user;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTHyperlink
    public void unsetSnd() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(SND$0, 0);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTHyperlink
    public CTOfficeArtExtensionList getExtLst() {
        synchronized (monitor()) {
            check_orphaned();
            CTOfficeArtExtensionList cTOfficeArtExtensionList = (CTOfficeArtExtensionList) get_store().find_element_user(EXTLST$2, 0);
            if (cTOfficeArtExtensionList == null) {
                return null;
            }
            return cTOfficeArtExtensionList;
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTHyperlink
    public boolean isSetExtLst() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().count_elements(EXTLST$2) != 0;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTHyperlink
    public void setExtLst(CTOfficeArtExtensionList cTOfficeArtExtensionList) {
        synchronized (monitor()) {
            check_orphaned();
            CTOfficeArtExtensionList cTOfficeArtExtensionList2 = (CTOfficeArtExtensionList) get_store().find_element_user(EXTLST$2, 0);
            if (cTOfficeArtExtensionList2 == null) {
                cTOfficeArtExtensionList2 = (CTOfficeArtExtensionList) get_store().add_element_user(EXTLST$2);
            }
            cTOfficeArtExtensionList2.set(cTOfficeArtExtensionList);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTHyperlink
    public CTOfficeArtExtensionList addNewExtLst() {
        CTOfficeArtExtensionList cTOfficeArtExtensionList;
        synchronized (monitor()) {
            check_orphaned();
            cTOfficeArtExtensionList = (CTOfficeArtExtensionList) get_store().add_element_user(EXTLST$2);
        }
        return cTOfficeArtExtensionList;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTHyperlink
    public void unsetExtLst() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(EXTLST$2, 0);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTHyperlink
    public String getId() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(ID$4);
            if (simpleValue == null) {
                return null;
            }
            return simpleValue.getStringValue();
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTHyperlink
    public STRelationshipId xgetId() {
        STRelationshipId sTRelationshipId;
        synchronized (monitor()) {
            check_orphaned();
            sTRelationshipId = (STRelationshipId) get_store().find_attribute_user(ID$4);
        }
        return sTRelationshipId;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTHyperlink
    public boolean isSetId() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(ID$4) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTHyperlink
    public void setId(String str) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(ID$4);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(ID$4);
            }
            simpleValue.setStringValue(str);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTHyperlink
    public void xsetId(STRelationshipId sTRelationshipId) {
        synchronized (monitor()) {
            check_orphaned();
            STRelationshipId sTRelationshipId2 = (STRelationshipId) get_store().find_attribute_user(ID$4);
            if (sTRelationshipId2 == null) {
                sTRelationshipId2 = (STRelationshipId) get_store().add_attribute_user(ID$4);
            }
            sTRelationshipId2.set(sTRelationshipId);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTHyperlink
    public void unsetId() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(ID$4);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTHyperlink
    public String getInvalidUrl() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(INVALIDURL$6);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(INVALIDURL$6);
            }
            if (simpleValue == null) {
                return null;
            }
            return simpleValue.getStringValue();
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTHyperlink
    public XmlString xgetInvalidUrl() {
        XmlString xmlString;
        synchronized (monitor()) {
            check_orphaned();
            XmlString xmlString2 = (XmlString) get_store().find_attribute_user(INVALIDURL$6);
            if (xmlString2 == null) {
                xmlString2 = (XmlString) get_default_attribute_value(INVALIDURL$6);
            }
            xmlString = xmlString2;
        }
        return xmlString;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTHyperlink
    public boolean isSetInvalidUrl() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(INVALIDURL$6) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTHyperlink
    public void setInvalidUrl(String str) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(INVALIDURL$6);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(INVALIDURL$6);
            }
            simpleValue.setStringValue(str);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTHyperlink
    public void xsetInvalidUrl(XmlString xmlString) {
        synchronized (monitor()) {
            check_orphaned();
            XmlString xmlString2 = (XmlString) get_store().find_attribute_user(INVALIDURL$6);
            if (xmlString2 == null) {
                xmlString2 = (XmlString) get_store().add_attribute_user(INVALIDURL$6);
            }
            xmlString2.set(xmlString);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTHyperlink
    public void unsetInvalidUrl() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(INVALIDURL$6);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTHyperlink
    public String getAction() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(ACTION$8);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(ACTION$8);
            }
            if (simpleValue == null) {
                return null;
            }
            return simpleValue.getStringValue();
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTHyperlink
    public XmlString xgetAction() {
        XmlString xmlString;
        synchronized (monitor()) {
            check_orphaned();
            XmlString xmlString2 = (XmlString) get_store().find_attribute_user(ACTION$8);
            if (xmlString2 == null) {
                xmlString2 = (XmlString) get_default_attribute_value(ACTION$8);
            }
            xmlString = xmlString2;
        }
        return xmlString;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTHyperlink
    public boolean isSetAction() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(ACTION$8) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTHyperlink
    public void setAction(String str) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(ACTION$8);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(ACTION$8);
            }
            simpleValue.setStringValue(str);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTHyperlink
    public void xsetAction(XmlString xmlString) {
        synchronized (monitor()) {
            check_orphaned();
            XmlString xmlString2 = (XmlString) get_store().find_attribute_user(ACTION$8);
            if (xmlString2 == null) {
                xmlString2 = (XmlString) get_store().add_attribute_user(ACTION$8);
            }
            xmlString2.set(xmlString);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTHyperlink
    public void unsetAction() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(ACTION$8);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTHyperlink
    public String getTgtFrame() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(TGTFRAME$10);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(TGTFRAME$10);
            }
            if (simpleValue == null) {
                return null;
            }
            return simpleValue.getStringValue();
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTHyperlink
    public XmlString xgetTgtFrame() {
        XmlString xmlString;
        synchronized (monitor()) {
            check_orphaned();
            XmlString xmlString2 = (XmlString) get_store().find_attribute_user(TGTFRAME$10);
            if (xmlString2 == null) {
                xmlString2 = (XmlString) get_default_attribute_value(TGTFRAME$10);
            }
            xmlString = xmlString2;
        }
        return xmlString;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTHyperlink
    public boolean isSetTgtFrame() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(TGTFRAME$10) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTHyperlink
    public void setTgtFrame(String str) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(TGTFRAME$10);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(TGTFRAME$10);
            }
            simpleValue.setStringValue(str);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTHyperlink
    public void xsetTgtFrame(XmlString xmlString) {
        synchronized (monitor()) {
            check_orphaned();
            XmlString xmlString2 = (XmlString) get_store().find_attribute_user(TGTFRAME$10);
            if (xmlString2 == null) {
                xmlString2 = (XmlString) get_store().add_attribute_user(TGTFRAME$10);
            }
            xmlString2.set(xmlString);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTHyperlink
    public void unsetTgtFrame() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(TGTFRAME$10);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTHyperlink
    public String getTooltip() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(TOOLTIP$12);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(TOOLTIP$12);
            }
            if (simpleValue == null) {
                return null;
            }
            return simpleValue.getStringValue();
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTHyperlink
    public XmlString xgetTooltip() {
        XmlString xmlString;
        synchronized (monitor()) {
            check_orphaned();
            XmlString xmlString2 = (XmlString) get_store().find_attribute_user(TOOLTIP$12);
            if (xmlString2 == null) {
                xmlString2 = (XmlString) get_default_attribute_value(TOOLTIP$12);
            }
            xmlString = xmlString2;
        }
        return xmlString;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTHyperlink
    public boolean isSetTooltip() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(TOOLTIP$12) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTHyperlink
    public void setTooltip(String str) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(TOOLTIP$12);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(TOOLTIP$12);
            }
            simpleValue.setStringValue(str);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTHyperlink
    public void xsetTooltip(XmlString xmlString) {
        synchronized (monitor()) {
            check_orphaned();
            XmlString xmlString2 = (XmlString) get_store().find_attribute_user(TOOLTIP$12);
            if (xmlString2 == null) {
                xmlString2 = (XmlString) get_store().add_attribute_user(TOOLTIP$12);
            }
            xmlString2.set(xmlString);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTHyperlink
    public void unsetTooltip() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(TOOLTIP$12);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTHyperlink
    public boolean getHistory() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(HISTORY$14);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(HISTORY$14);
            }
            if (simpleValue == null) {
                return false;
            }
            return simpleValue.getBooleanValue();
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTHyperlink
    public XmlBoolean xgetHistory() {
        XmlBoolean xmlBoolean;
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(HISTORY$14);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_default_attribute_value(HISTORY$14);
            }
            xmlBoolean = xmlBoolean2;
        }
        return xmlBoolean;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTHyperlink
    public boolean isSetHistory() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(HISTORY$14) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTHyperlink
    public void setHistory(boolean z) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(HISTORY$14);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(HISTORY$14);
            }
            simpleValue.setBooleanValue(z);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTHyperlink
    public void xsetHistory(XmlBoolean xmlBoolean) {
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(HISTORY$14);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_store().add_attribute_user(HISTORY$14);
            }
            xmlBoolean2.set(xmlBoolean);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTHyperlink
    public void unsetHistory() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(HISTORY$14);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTHyperlink
    public boolean getHighlightClick() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(HIGHLIGHTCLICK$16);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(HIGHLIGHTCLICK$16);
            }
            if (simpleValue == null) {
                return false;
            }
            return simpleValue.getBooleanValue();
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTHyperlink
    public XmlBoolean xgetHighlightClick() {
        XmlBoolean xmlBoolean;
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(HIGHLIGHTCLICK$16);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_default_attribute_value(HIGHLIGHTCLICK$16);
            }
            xmlBoolean = xmlBoolean2;
        }
        return xmlBoolean;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTHyperlink
    public boolean isSetHighlightClick() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(HIGHLIGHTCLICK$16) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTHyperlink
    public void setHighlightClick(boolean z) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(HIGHLIGHTCLICK$16);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(HIGHLIGHTCLICK$16);
            }
            simpleValue.setBooleanValue(z);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTHyperlink
    public void xsetHighlightClick(XmlBoolean xmlBoolean) {
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(HIGHLIGHTCLICK$16);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_store().add_attribute_user(HIGHLIGHTCLICK$16);
            }
            xmlBoolean2.set(xmlBoolean);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTHyperlink
    public void unsetHighlightClick() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(HIGHLIGHTCLICK$16);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTHyperlink
    public boolean getEndSnd() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(ENDSND$18);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(ENDSND$18);
            }
            if (simpleValue == null) {
                return false;
            }
            return simpleValue.getBooleanValue();
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTHyperlink
    public XmlBoolean xgetEndSnd() {
        XmlBoolean xmlBoolean;
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(ENDSND$18);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_default_attribute_value(ENDSND$18);
            }
            xmlBoolean = xmlBoolean2;
        }
        return xmlBoolean;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTHyperlink
    public boolean isSetEndSnd() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(ENDSND$18) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTHyperlink
    public void setEndSnd(boolean z) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(ENDSND$18);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(ENDSND$18);
            }
            simpleValue.setBooleanValue(z);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTHyperlink
    public void xsetEndSnd(XmlBoolean xmlBoolean) {
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(ENDSND$18);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_store().add_attribute_user(ENDSND$18);
            }
            xmlBoolean2.set(xmlBoolean);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTHyperlink
    public void unsetEndSnd() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(ENDSND$18);
        }
    }
}
