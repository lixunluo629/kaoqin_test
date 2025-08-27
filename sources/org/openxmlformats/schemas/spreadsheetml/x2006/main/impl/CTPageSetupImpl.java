package org.openxmlformats.schemas.spreadsheetml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.poi.openxml4j.opc.PackageRelationshipTypes;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.SimpleValue;
import org.apache.xmlbeans.XmlBoolean;
import org.apache.xmlbeans.XmlUnsignedInt;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;
import org.openxmlformats.schemas.officeDocument.x2006.relationships.STRelationshipId;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageSetup;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.STCellComments;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.STOrientation;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.STPageOrder;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.STPrintError;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.STPrintError$Enum;
import org.springframework.web.servlet.tags.BindErrorsTag;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/impl/CTPageSetupImpl.class */
public class CTPageSetupImpl extends XmlComplexContentImpl implements CTPageSetup {
    private static final QName PAPERSIZE$0 = new QName("", "paperSize");
    private static final QName SCALE$2 = new QName("", "scale");
    private static final QName FIRSTPAGENUMBER$4 = new QName("", "firstPageNumber");
    private static final QName FITTOWIDTH$6 = new QName("", "fitToWidth");
    private static final QName FITTOHEIGHT$8 = new QName("", "fitToHeight");
    private static final QName PAGEORDER$10 = new QName("", "pageOrder");
    private static final QName ORIENTATION$12 = new QName("", "orientation");
    private static final QName USEPRINTERDEFAULTS$14 = new QName("", "usePrinterDefaults");
    private static final QName BLACKANDWHITE$16 = new QName("", "blackAndWhite");
    private static final QName DRAFT$18 = new QName("", "draft");
    private static final QName CELLCOMMENTS$20 = new QName("", "cellComments");
    private static final QName USEFIRSTPAGENUMBER$22 = new QName("", "useFirstPageNumber");
    private static final QName ERRORS$24 = new QName("", BindErrorsTag.ERRORS_VARIABLE_NAME);
    private static final QName HORIZONTALDPI$26 = new QName("", "horizontalDpi");
    private static final QName VERTICALDPI$28 = new QName("", "verticalDpi");
    private static final QName COPIES$30 = new QName("", "copies");
    private static final QName ID$32 = new QName(PackageRelationshipTypes.CORE_PROPERTIES_ECMA376_NS, "id");

    public CTPageSetupImpl(SchemaType schemaType) {
        super(schemaType);
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageSetup
    public long getPaperSize() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(PAPERSIZE$0);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(PAPERSIZE$0);
            }
            if (simpleValue == null) {
                return 0L;
            }
            return simpleValue.getLongValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageSetup
    public XmlUnsignedInt xgetPaperSize() {
        XmlUnsignedInt xmlUnsignedInt;
        synchronized (monitor()) {
            check_orphaned();
            XmlUnsignedInt xmlUnsignedInt2 = (XmlUnsignedInt) get_store().find_attribute_user(PAPERSIZE$0);
            if (xmlUnsignedInt2 == null) {
                xmlUnsignedInt2 = (XmlUnsignedInt) get_default_attribute_value(PAPERSIZE$0);
            }
            xmlUnsignedInt = xmlUnsignedInt2;
        }
        return xmlUnsignedInt;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageSetup
    public boolean isSetPaperSize() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(PAPERSIZE$0) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageSetup
    public void setPaperSize(long j) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(PAPERSIZE$0);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(PAPERSIZE$0);
            }
            simpleValue.setLongValue(j);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageSetup
    public void xsetPaperSize(XmlUnsignedInt xmlUnsignedInt) {
        synchronized (monitor()) {
            check_orphaned();
            XmlUnsignedInt xmlUnsignedInt2 = (XmlUnsignedInt) get_store().find_attribute_user(PAPERSIZE$0);
            if (xmlUnsignedInt2 == null) {
                xmlUnsignedInt2 = (XmlUnsignedInt) get_store().add_attribute_user(PAPERSIZE$0);
            }
            xmlUnsignedInt2.set(xmlUnsignedInt);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageSetup
    public void unsetPaperSize() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PAPERSIZE$0);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageSetup
    public long getScale() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(SCALE$2);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(SCALE$2);
            }
            if (simpleValue == null) {
                return 0L;
            }
            return simpleValue.getLongValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageSetup
    public XmlUnsignedInt xgetScale() {
        XmlUnsignedInt xmlUnsignedInt;
        synchronized (monitor()) {
            check_orphaned();
            XmlUnsignedInt xmlUnsignedInt2 = (XmlUnsignedInt) get_store().find_attribute_user(SCALE$2);
            if (xmlUnsignedInt2 == null) {
                xmlUnsignedInt2 = (XmlUnsignedInt) get_default_attribute_value(SCALE$2);
            }
            xmlUnsignedInt = xmlUnsignedInt2;
        }
        return xmlUnsignedInt;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageSetup
    public boolean isSetScale() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(SCALE$2) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageSetup
    public void setScale(long j) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(SCALE$2);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(SCALE$2);
            }
            simpleValue.setLongValue(j);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageSetup
    public void xsetScale(XmlUnsignedInt xmlUnsignedInt) {
        synchronized (monitor()) {
            check_orphaned();
            XmlUnsignedInt xmlUnsignedInt2 = (XmlUnsignedInt) get_store().find_attribute_user(SCALE$2);
            if (xmlUnsignedInt2 == null) {
                xmlUnsignedInt2 = (XmlUnsignedInt) get_store().add_attribute_user(SCALE$2);
            }
            xmlUnsignedInt2.set(xmlUnsignedInt);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageSetup
    public void unsetScale() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(SCALE$2);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageSetup
    public long getFirstPageNumber() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(FIRSTPAGENUMBER$4);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(FIRSTPAGENUMBER$4);
            }
            if (simpleValue == null) {
                return 0L;
            }
            return simpleValue.getLongValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageSetup
    public XmlUnsignedInt xgetFirstPageNumber() {
        XmlUnsignedInt xmlUnsignedInt;
        synchronized (monitor()) {
            check_orphaned();
            XmlUnsignedInt xmlUnsignedInt2 = (XmlUnsignedInt) get_store().find_attribute_user(FIRSTPAGENUMBER$4);
            if (xmlUnsignedInt2 == null) {
                xmlUnsignedInt2 = (XmlUnsignedInt) get_default_attribute_value(FIRSTPAGENUMBER$4);
            }
            xmlUnsignedInt = xmlUnsignedInt2;
        }
        return xmlUnsignedInt;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageSetup
    public boolean isSetFirstPageNumber() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(FIRSTPAGENUMBER$4) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageSetup
    public void setFirstPageNumber(long j) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(FIRSTPAGENUMBER$4);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(FIRSTPAGENUMBER$4);
            }
            simpleValue.setLongValue(j);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageSetup
    public void xsetFirstPageNumber(XmlUnsignedInt xmlUnsignedInt) {
        synchronized (monitor()) {
            check_orphaned();
            XmlUnsignedInt xmlUnsignedInt2 = (XmlUnsignedInt) get_store().find_attribute_user(FIRSTPAGENUMBER$4);
            if (xmlUnsignedInt2 == null) {
                xmlUnsignedInt2 = (XmlUnsignedInt) get_store().add_attribute_user(FIRSTPAGENUMBER$4);
            }
            xmlUnsignedInt2.set(xmlUnsignedInt);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageSetup
    public void unsetFirstPageNumber() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(FIRSTPAGENUMBER$4);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageSetup
    public long getFitToWidth() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(FITTOWIDTH$6);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(FITTOWIDTH$6);
            }
            if (simpleValue == null) {
                return 0L;
            }
            return simpleValue.getLongValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageSetup
    public XmlUnsignedInt xgetFitToWidth() {
        XmlUnsignedInt xmlUnsignedInt;
        synchronized (monitor()) {
            check_orphaned();
            XmlUnsignedInt xmlUnsignedInt2 = (XmlUnsignedInt) get_store().find_attribute_user(FITTOWIDTH$6);
            if (xmlUnsignedInt2 == null) {
                xmlUnsignedInt2 = (XmlUnsignedInt) get_default_attribute_value(FITTOWIDTH$6);
            }
            xmlUnsignedInt = xmlUnsignedInt2;
        }
        return xmlUnsignedInt;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageSetup
    public boolean isSetFitToWidth() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(FITTOWIDTH$6) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageSetup
    public void setFitToWidth(long j) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(FITTOWIDTH$6);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(FITTOWIDTH$6);
            }
            simpleValue.setLongValue(j);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageSetup
    public void xsetFitToWidth(XmlUnsignedInt xmlUnsignedInt) {
        synchronized (monitor()) {
            check_orphaned();
            XmlUnsignedInt xmlUnsignedInt2 = (XmlUnsignedInt) get_store().find_attribute_user(FITTOWIDTH$6);
            if (xmlUnsignedInt2 == null) {
                xmlUnsignedInt2 = (XmlUnsignedInt) get_store().add_attribute_user(FITTOWIDTH$6);
            }
            xmlUnsignedInt2.set(xmlUnsignedInt);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageSetup
    public void unsetFitToWidth() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(FITTOWIDTH$6);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageSetup
    public long getFitToHeight() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(FITTOHEIGHT$8);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(FITTOHEIGHT$8);
            }
            if (simpleValue == null) {
                return 0L;
            }
            return simpleValue.getLongValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageSetup
    public XmlUnsignedInt xgetFitToHeight() {
        XmlUnsignedInt xmlUnsignedInt;
        synchronized (monitor()) {
            check_orphaned();
            XmlUnsignedInt xmlUnsignedInt2 = (XmlUnsignedInt) get_store().find_attribute_user(FITTOHEIGHT$8);
            if (xmlUnsignedInt2 == null) {
                xmlUnsignedInt2 = (XmlUnsignedInt) get_default_attribute_value(FITTOHEIGHT$8);
            }
            xmlUnsignedInt = xmlUnsignedInt2;
        }
        return xmlUnsignedInt;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageSetup
    public boolean isSetFitToHeight() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(FITTOHEIGHT$8) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageSetup
    public void setFitToHeight(long j) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(FITTOHEIGHT$8);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(FITTOHEIGHT$8);
            }
            simpleValue.setLongValue(j);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageSetup
    public void xsetFitToHeight(XmlUnsignedInt xmlUnsignedInt) {
        synchronized (monitor()) {
            check_orphaned();
            XmlUnsignedInt xmlUnsignedInt2 = (XmlUnsignedInt) get_store().find_attribute_user(FITTOHEIGHT$8);
            if (xmlUnsignedInt2 == null) {
                xmlUnsignedInt2 = (XmlUnsignedInt) get_store().add_attribute_user(FITTOHEIGHT$8);
            }
            xmlUnsignedInt2.set(xmlUnsignedInt);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageSetup
    public void unsetFitToHeight() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(FITTOHEIGHT$8);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageSetup
    public STPageOrder.Enum getPageOrder() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(PAGEORDER$10);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(PAGEORDER$10);
            }
            if (simpleValue == null) {
                return null;
            }
            return (STPageOrder.Enum) simpleValue.getEnumValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageSetup
    public STPageOrder xgetPageOrder() {
        STPageOrder sTPageOrder;
        synchronized (monitor()) {
            check_orphaned();
            STPageOrder sTPageOrder2 = (STPageOrder) get_store().find_attribute_user(PAGEORDER$10);
            if (sTPageOrder2 == null) {
                sTPageOrder2 = (STPageOrder) get_default_attribute_value(PAGEORDER$10);
            }
            sTPageOrder = sTPageOrder2;
        }
        return sTPageOrder;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageSetup
    public boolean isSetPageOrder() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(PAGEORDER$10) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageSetup
    public void setPageOrder(STPageOrder.Enum r4) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(PAGEORDER$10);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(PAGEORDER$10);
            }
            simpleValue.setEnumValue(r4);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageSetup
    public void xsetPageOrder(STPageOrder sTPageOrder) {
        synchronized (monitor()) {
            check_orphaned();
            STPageOrder sTPageOrder2 = (STPageOrder) get_store().find_attribute_user(PAGEORDER$10);
            if (sTPageOrder2 == null) {
                sTPageOrder2 = (STPageOrder) get_store().add_attribute_user(PAGEORDER$10);
            }
            sTPageOrder2.set(sTPageOrder);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageSetup
    public void unsetPageOrder() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PAGEORDER$10);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageSetup
    public STOrientation.Enum getOrientation() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(ORIENTATION$12);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(ORIENTATION$12);
            }
            if (simpleValue == null) {
                return null;
            }
            return (STOrientation.Enum) simpleValue.getEnumValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageSetup
    public STOrientation xgetOrientation() {
        STOrientation sTOrientation;
        synchronized (monitor()) {
            check_orphaned();
            STOrientation sTOrientation2 = (STOrientation) get_store().find_attribute_user(ORIENTATION$12);
            if (sTOrientation2 == null) {
                sTOrientation2 = (STOrientation) get_default_attribute_value(ORIENTATION$12);
            }
            sTOrientation = sTOrientation2;
        }
        return sTOrientation;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageSetup
    public boolean isSetOrientation() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(ORIENTATION$12) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageSetup
    public void setOrientation(STOrientation.Enum r4) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(ORIENTATION$12);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(ORIENTATION$12);
            }
            simpleValue.setEnumValue(r4);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageSetup
    public void xsetOrientation(STOrientation sTOrientation) {
        synchronized (monitor()) {
            check_orphaned();
            STOrientation sTOrientation2 = (STOrientation) get_store().find_attribute_user(ORIENTATION$12);
            if (sTOrientation2 == null) {
                sTOrientation2 = (STOrientation) get_store().add_attribute_user(ORIENTATION$12);
            }
            sTOrientation2.set(sTOrientation);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageSetup
    public void unsetOrientation() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(ORIENTATION$12);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageSetup
    public boolean getUsePrinterDefaults() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(USEPRINTERDEFAULTS$14);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(USEPRINTERDEFAULTS$14);
            }
            if (simpleValue == null) {
                return false;
            }
            return simpleValue.getBooleanValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageSetup
    public XmlBoolean xgetUsePrinterDefaults() {
        XmlBoolean xmlBoolean;
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(USEPRINTERDEFAULTS$14);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_default_attribute_value(USEPRINTERDEFAULTS$14);
            }
            xmlBoolean = xmlBoolean2;
        }
        return xmlBoolean;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageSetup
    public boolean isSetUsePrinterDefaults() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(USEPRINTERDEFAULTS$14) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageSetup
    public void setUsePrinterDefaults(boolean z) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(USEPRINTERDEFAULTS$14);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(USEPRINTERDEFAULTS$14);
            }
            simpleValue.setBooleanValue(z);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageSetup
    public void xsetUsePrinterDefaults(XmlBoolean xmlBoolean) {
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(USEPRINTERDEFAULTS$14);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_store().add_attribute_user(USEPRINTERDEFAULTS$14);
            }
            xmlBoolean2.set(xmlBoolean);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageSetup
    public void unsetUsePrinterDefaults() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(USEPRINTERDEFAULTS$14);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageSetup
    public boolean getBlackAndWhite() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(BLACKANDWHITE$16);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(BLACKANDWHITE$16);
            }
            if (simpleValue == null) {
                return false;
            }
            return simpleValue.getBooleanValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageSetup
    public XmlBoolean xgetBlackAndWhite() {
        XmlBoolean xmlBoolean;
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(BLACKANDWHITE$16);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_default_attribute_value(BLACKANDWHITE$16);
            }
            xmlBoolean = xmlBoolean2;
        }
        return xmlBoolean;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageSetup
    public boolean isSetBlackAndWhite() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(BLACKANDWHITE$16) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageSetup
    public void setBlackAndWhite(boolean z) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(BLACKANDWHITE$16);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(BLACKANDWHITE$16);
            }
            simpleValue.setBooleanValue(z);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageSetup
    public void xsetBlackAndWhite(XmlBoolean xmlBoolean) {
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(BLACKANDWHITE$16);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_store().add_attribute_user(BLACKANDWHITE$16);
            }
            xmlBoolean2.set(xmlBoolean);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageSetup
    public void unsetBlackAndWhite() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(BLACKANDWHITE$16);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageSetup
    public boolean getDraft() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(DRAFT$18);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(DRAFT$18);
            }
            if (simpleValue == null) {
                return false;
            }
            return simpleValue.getBooleanValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageSetup
    public XmlBoolean xgetDraft() {
        XmlBoolean xmlBoolean;
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(DRAFT$18);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_default_attribute_value(DRAFT$18);
            }
            xmlBoolean = xmlBoolean2;
        }
        return xmlBoolean;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageSetup
    public boolean isSetDraft() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(DRAFT$18) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageSetup
    public void setDraft(boolean z) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(DRAFT$18);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(DRAFT$18);
            }
            simpleValue.setBooleanValue(z);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageSetup
    public void xsetDraft(XmlBoolean xmlBoolean) {
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(DRAFT$18);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_store().add_attribute_user(DRAFT$18);
            }
            xmlBoolean2.set(xmlBoolean);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageSetup
    public void unsetDraft() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(DRAFT$18);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageSetup
    public STCellComments.Enum getCellComments() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(CELLCOMMENTS$20);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(CELLCOMMENTS$20);
            }
            if (simpleValue == null) {
                return null;
            }
            return (STCellComments.Enum) simpleValue.getEnumValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageSetup
    public STCellComments xgetCellComments() {
        STCellComments sTCellComments;
        synchronized (monitor()) {
            check_orphaned();
            STCellComments sTCellComments2 = (STCellComments) get_store().find_attribute_user(CELLCOMMENTS$20);
            if (sTCellComments2 == null) {
                sTCellComments2 = (STCellComments) get_default_attribute_value(CELLCOMMENTS$20);
            }
            sTCellComments = sTCellComments2;
        }
        return sTCellComments;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageSetup
    public boolean isSetCellComments() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(CELLCOMMENTS$20) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageSetup
    public void setCellComments(STCellComments.Enum r4) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(CELLCOMMENTS$20);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(CELLCOMMENTS$20);
            }
            simpleValue.setEnumValue(r4);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageSetup
    public void xsetCellComments(STCellComments sTCellComments) {
        synchronized (monitor()) {
            check_orphaned();
            STCellComments sTCellComments2 = (STCellComments) get_store().find_attribute_user(CELLCOMMENTS$20);
            if (sTCellComments2 == null) {
                sTCellComments2 = (STCellComments) get_store().add_attribute_user(CELLCOMMENTS$20);
            }
            sTCellComments2.set(sTCellComments);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageSetup
    public void unsetCellComments() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(CELLCOMMENTS$20);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageSetup
    public boolean getUseFirstPageNumber() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(USEFIRSTPAGENUMBER$22);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(USEFIRSTPAGENUMBER$22);
            }
            if (simpleValue == null) {
                return false;
            }
            return simpleValue.getBooleanValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageSetup
    public XmlBoolean xgetUseFirstPageNumber() {
        XmlBoolean xmlBoolean;
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(USEFIRSTPAGENUMBER$22);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_default_attribute_value(USEFIRSTPAGENUMBER$22);
            }
            xmlBoolean = xmlBoolean2;
        }
        return xmlBoolean;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageSetup
    public boolean isSetUseFirstPageNumber() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(USEFIRSTPAGENUMBER$22) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageSetup
    public void setUseFirstPageNumber(boolean z) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(USEFIRSTPAGENUMBER$22);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(USEFIRSTPAGENUMBER$22);
            }
            simpleValue.setBooleanValue(z);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageSetup
    public void xsetUseFirstPageNumber(XmlBoolean xmlBoolean) {
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(USEFIRSTPAGENUMBER$22);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_store().add_attribute_user(USEFIRSTPAGENUMBER$22);
            }
            xmlBoolean2.set(xmlBoolean);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageSetup
    public void unsetUseFirstPageNumber() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(USEFIRSTPAGENUMBER$22);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageSetup
    public STPrintError$Enum getErrors() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(ERRORS$24);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(ERRORS$24);
            }
            if (simpleValue == null) {
                return null;
            }
            return (STPrintError$Enum) simpleValue.getEnumValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageSetup
    public STPrintError xgetErrors() {
        STPrintError sTPrintError;
        synchronized (monitor()) {
            check_orphaned();
            STPrintError sTPrintErrorFind_attribute_user = get_store().find_attribute_user(ERRORS$24);
            if (sTPrintErrorFind_attribute_user == null) {
                sTPrintErrorFind_attribute_user = (STPrintError) get_default_attribute_value(ERRORS$24);
            }
            sTPrintError = sTPrintErrorFind_attribute_user;
        }
        return sTPrintError;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageSetup
    public boolean isSetErrors() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(ERRORS$24) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageSetup
    public void setErrors(STPrintError$Enum sTPrintError$Enum) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(ERRORS$24);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(ERRORS$24);
            }
            simpleValue.setEnumValue(sTPrintError$Enum);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageSetup
    public void xsetErrors(STPrintError sTPrintError) {
        synchronized (monitor()) {
            check_orphaned();
            STPrintError sTPrintErrorFind_attribute_user = get_store().find_attribute_user(ERRORS$24);
            if (sTPrintErrorFind_attribute_user == null) {
                sTPrintErrorFind_attribute_user = (STPrintError) get_store().add_attribute_user(ERRORS$24);
            }
            sTPrintErrorFind_attribute_user.set(sTPrintError);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageSetup
    public void unsetErrors() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(ERRORS$24);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageSetup
    public long getHorizontalDpi() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(HORIZONTALDPI$26);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(HORIZONTALDPI$26);
            }
            if (simpleValue == null) {
                return 0L;
            }
            return simpleValue.getLongValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageSetup
    public XmlUnsignedInt xgetHorizontalDpi() {
        XmlUnsignedInt xmlUnsignedInt;
        synchronized (monitor()) {
            check_orphaned();
            XmlUnsignedInt xmlUnsignedInt2 = (XmlUnsignedInt) get_store().find_attribute_user(HORIZONTALDPI$26);
            if (xmlUnsignedInt2 == null) {
                xmlUnsignedInt2 = (XmlUnsignedInt) get_default_attribute_value(HORIZONTALDPI$26);
            }
            xmlUnsignedInt = xmlUnsignedInt2;
        }
        return xmlUnsignedInt;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageSetup
    public boolean isSetHorizontalDpi() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(HORIZONTALDPI$26) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageSetup
    public void setHorizontalDpi(long j) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(HORIZONTALDPI$26);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(HORIZONTALDPI$26);
            }
            simpleValue.setLongValue(j);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageSetup
    public void xsetHorizontalDpi(XmlUnsignedInt xmlUnsignedInt) {
        synchronized (monitor()) {
            check_orphaned();
            XmlUnsignedInt xmlUnsignedInt2 = (XmlUnsignedInt) get_store().find_attribute_user(HORIZONTALDPI$26);
            if (xmlUnsignedInt2 == null) {
                xmlUnsignedInt2 = (XmlUnsignedInt) get_store().add_attribute_user(HORIZONTALDPI$26);
            }
            xmlUnsignedInt2.set(xmlUnsignedInt);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageSetup
    public void unsetHorizontalDpi() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(HORIZONTALDPI$26);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageSetup
    public long getVerticalDpi() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(VERTICALDPI$28);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(VERTICALDPI$28);
            }
            if (simpleValue == null) {
                return 0L;
            }
            return simpleValue.getLongValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageSetup
    public XmlUnsignedInt xgetVerticalDpi() {
        XmlUnsignedInt xmlUnsignedInt;
        synchronized (monitor()) {
            check_orphaned();
            XmlUnsignedInt xmlUnsignedInt2 = (XmlUnsignedInt) get_store().find_attribute_user(VERTICALDPI$28);
            if (xmlUnsignedInt2 == null) {
                xmlUnsignedInt2 = (XmlUnsignedInt) get_default_attribute_value(VERTICALDPI$28);
            }
            xmlUnsignedInt = xmlUnsignedInt2;
        }
        return xmlUnsignedInt;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageSetup
    public boolean isSetVerticalDpi() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(VERTICALDPI$28) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageSetup
    public void setVerticalDpi(long j) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(VERTICALDPI$28);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(VERTICALDPI$28);
            }
            simpleValue.setLongValue(j);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageSetup
    public void xsetVerticalDpi(XmlUnsignedInt xmlUnsignedInt) {
        synchronized (monitor()) {
            check_orphaned();
            XmlUnsignedInt xmlUnsignedInt2 = (XmlUnsignedInt) get_store().find_attribute_user(VERTICALDPI$28);
            if (xmlUnsignedInt2 == null) {
                xmlUnsignedInt2 = (XmlUnsignedInt) get_store().add_attribute_user(VERTICALDPI$28);
            }
            xmlUnsignedInt2.set(xmlUnsignedInt);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageSetup
    public void unsetVerticalDpi() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(VERTICALDPI$28);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageSetup
    public long getCopies() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(COPIES$30);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(COPIES$30);
            }
            if (simpleValue == null) {
                return 0L;
            }
            return simpleValue.getLongValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageSetup
    public XmlUnsignedInt xgetCopies() {
        XmlUnsignedInt xmlUnsignedInt;
        synchronized (monitor()) {
            check_orphaned();
            XmlUnsignedInt xmlUnsignedInt2 = (XmlUnsignedInt) get_store().find_attribute_user(COPIES$30);
            if (xmlUnsignedInt2 == null) {
                xmlUnsignedInt2 = (XmlUnsignedInt) get_default_attribute_value(COPIES$30);
            }
            xmlUnsignedInt = xmlUnsignedInt2;
        }
        return xmlUnsignedInt;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageSetup
    public boolean isSetCopies() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(COPIES$30) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageSetup
    public void setCopies(long j) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(COPIES$30);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(COPIES$30);
            }
            simpleValue.setLongValue(j);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageSetup
    public void xsetCopies(XmlUnsignedInt xmlUnsignedInt) {
        synchronized (monitor()) {
            check_orphaned();
            XmlUnsignedInt xmlUnsignedInt2 = (XmlUnsignedInt) get_store().find_attribute_user(COPIES$30);
            if (xmlUnsignedInt2 == null) {
                xmlUnsignedInt2 = (XmlUnsignedInt) get_store().add_attribute_user(COPIES$30);
            }
            xmlUnsignedInt2.set(xmlUnsignedInt);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageSetup
    public void unsetCopies() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(COPIES$30);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageSetup
    public String getId() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(ID$32);
            if (simpleValue == null) {
                return null;
            }
            return simpleValue.getStringValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageSetup
    public STRelationshipId xgetId() {
        STRelationshipId sTRelationshipId;
        synchronized (monitor()) {
            check_orphaned();
            sTRelationshipId = (STRelationshipId) get_store().find_attribute_user(ID$32);
        }
        return sTRelationshipId;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageSetup
    public boolean isSetId() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(ID$32) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageSetup
    public void setId(String str) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(ID$32);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(ID$32);
            }
            simpleValue.setStringValue(str);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageSetup
    public void xsetId(STRelationshipId sTRelationshipId) {
        synchronized (monitor()) {
            check_orphaned();
            STRelationshipId sTRelationshipId2 = (STRelationshipId) get_store().find_attribute_user(ID$32);
            if (sTRelationshipId2 == null) {
                sTRelationshipId2 = (STRelationshipId) get_store().add_attribute_user(ID$32);
            }
            sTRelationshipId2.set(sTRelationshipId);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageSetup
    public void unsetId() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(ID$32);
        }
    }
}
