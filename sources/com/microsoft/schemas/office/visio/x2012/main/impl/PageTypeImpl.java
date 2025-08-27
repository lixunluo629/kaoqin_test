package com.microsoft.schemas.office.visio.x2012.main.impl;

import com.microsoft.schemas.office.visio.x2012.main.PageSheetType;
import com.microsoft.schemas.office.visio.x2012.main.PageType;
import com.microsoft.schemas.office.visio.x2012.main.RelType;
import javax.xml.namespace.QName;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.SimpleValue;
import org.apache.xmlbeans.XmlBoolean;
import org.apache.xmlbeans.XmlDouble;
import org.apache.xmlbeans.XmlString;
import org.apache.xmlbeans.XmlUnsignedInt;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;

/* loaded from: poi-ooxml-schemas-3.17.jar:com/microsoft/schemas/office/visio/x2012/main/impl/PageTypeImpl.class */
public class PageTypeImpl extends XmlComplexContentImpl implements PageType {
    private static final QName PAGESHEET$0 = new QName("http://schemas.microsoft.com/office/visio/2012/main", "PageSheet");
    private static final QName REL$2 = new QName("http://schemas.microsoft.com/office/visio/2012/main", "Rel");
    private static final QName ID$4 = new QName("", "ID");
    private static final QName NAME$6 = new QName("", "Name");
    private static final QName NAMEU$8 = new QName("", "NameU");
    private static final QName ISCUSTOMNAME$10 = new QName("", "IsCustomName");
    private static final QName ISCUSTOMNAMEU$12 = new QName("", "IsCustomNameU");
    private static final QName BACKGROUND$14 = new QName("", "Background");
    private static final QName BACKPAGE$16 = new QName("", "BackPage");
    private static final QName VIEWSCALE$18 = new QName("", "ViewScale");
    private static final QName VIEWCENTERX$20 = new QName("", "ViewCenterX");
    private static final QName VIEWCENTERY$22 = new QName("", "ViewCenterY");
    private static final QName REVIEWERID$24 = new QName("", "ReviewerID");
    private static final QName ASSOCIATEDPAGE$26 = new QName("", "AssociatedPage");

    public PageTypeImpl(SchemaType schemaType) {
        super(schemaType);
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.PageType
    public PageSheetType getPageSheet() {
        synchronized (monitor()) {
            check_orphaned();
            PageSheetType pageSheetType = (PageSheetType) get_store().find_element_user(PAGESHEET$0, 0);
            if (pageSheetType == null) {
                return null;
            }
            return pageSheetType;
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.PageType
    public boolean isSetPageSheet() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().count_elements(PAGESHEET$0) != 0;
        }
        return z;
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.PageType
    public void setPageSheet(PageSheetType pageSheetType) {
        synchronized (monitor()) {
            check_orphaned();
            PageSheetType pageSheetType2 = (PageSheetType) get_store().find_element_user(PAGESHEET$0, 0);
            if (pageSheetType2 == null) {
                pageSheetType2 = (PageSheetType) get_store().add_element_user(PAGESHEET$0);
            }
            pageSheetType2.set(pageSheetType);
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.PageType
    public PageSheetType addNewPageSheet() {
        PageSheetType pageSheetType;
        synchronized (monitor()) {
            check_orphaned();
            pageSheetType = (PageSheetType) get_store().add_element_user(PAGESHEET$0);
        }
        return pageSheetType;
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.PageType
    public void unsetPageSheet() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PAGESHEET$0, 0);
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.PageType
    public RelType getRel() {
        synchronized (monitor()) {
            check_orphaned();
            RelType relType = (RelType) get_store().find_element_user(REL$2, 0);
            if (relType == null) {
                return null;
            }
            return relType;
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.PageType
    public void setRel(RelType relType) {
        synchronized (monitor()) {
            check_orphaned();
            RelType relType2 = (RelType) get_store().find_element_user(REL$2, 0);
            if (relType2 == null) {
                relType2 = (RelType) get_store().add_element_user(REL$2);
            }
            relType2.set(relType);
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.PageType
    public RelType addNewRel() {
        RelType relType;
        synchronized (monitor()) {
            check_orphaned();
            relType = (RelType) get_store().add_element_user(REL$2);
        }
        return relType;
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.PageType
    public long getID() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(ID$4);
            if (simpleValue == null) {
                return 0L;
            }
            return simpleValue.getLongValue();
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.PageType
    public XmlUnsignedInt xgetID() {
        XmlUnsignedInt xmlUnsignedInt;
        synchronized (monitor()) {
            check_orphaned();
            xmlUnsignedInt = (XmlUnsignedInt) get_store().find_attribute_user(ID$4);
        }
        return xmlUnsignedInt;
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.PageType
    public void setID(long j) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(ID$4);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(ID$4);
            }
            simpleValue.setLongValue(j);
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.PageType
    public void xsetID(XmlUnsignedInt xmlUnsignedInt) {
        synchronized (monitor()) {
            check_orphaned();
            XmlUnsignedInt xmlUnsignedInt2 = (XmlUnsignedInt) get_store().find_attribute_user(ID$4);
            if (xmlUnsignedInt2 == null) {
                xmlUnsignedInt2 = (XmlUnsignedInt) get_store().add_attribute_user(ID$4);
            }
            xmlUnsignedInt2.set(xmlUnsignedInt);
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.PageType
    public String getName() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(NAME$6);
            if (simpleValue == null) {
                return null;
            }
            return simpleValue.getStringValue();
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.PageType
    public XmlString xgetName() {
        XmlString xmlString;
        synchronized (monitor()) {
            check_orphaned();
            xmlString = (XmlString) get_store().find_attribute_user(NAME$6);
        }
        return xmlString;
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.PageType
    public boolean isSetName() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(NAME$6) != null;
        }
        return z;
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.PageType
    public void setName(String str) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(NAME$6);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(NAME$6);
            }
            simpleValue.setStringValue(str);
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.PageType
    public void xsetName(XmlString xmlString) {
        synchronized (monitor()) {
            check_orphaned();
            XmlString xmlString2 = (XmlString) get_store().find_attribute_user(NAME$6);
            if (xmlString2 == null) {
                xmlString2 = (XmlString) get_store().add_attribute_user(NAME$6);
            }
            xmlString2.set(xmlString);
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.PageType
    public void unsetName() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(NAME$6);
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.PageType
    public String getNameU() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(NAMEU$8);
            if (simpleValue == null) {
                return null;
            }
            return simpleValue.getStringValue();
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.PageType
    public XmlString xgetNameU() {
        XmlString xmlString;
        synchronized (monitor()) {
            check_orphaned();
            xmlString = (XmlString) get_store().find_attribute_user(NAMEU$8);
        }
        return xmlString;
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.PageType
    public boolean isSetNameU() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(NAMEU$8) != null;
        }
        return z;
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.PageType
    public void setNameU(String str) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(NAMEU$8);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(NAMEU$8);
            }
            simpleValue.setStringValue(str);
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.PageType
    public void xsetNameU(XmlString xmlString) {
        synchronized (monitor()) {
            check_orphaned();
            XmlString xmlString2 = (XmlString) get_store().find_attribute_user(NAMEU$8);
            if (xmlString2 == null) {
                xmlString2 = (XmlString) get_store().add_attribute_user(NAMEU$8);
            }
            xmlString2.set(xmlString);
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.PageType
    public void unsetNameU() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(NAMEU$8);
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.PageType
    public boolean getIsCustomName() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(ISCUSTOMNAME$10);
            if (simpleValue == null) {
                return false;
            }
            return simpleValue.getBooleanValue();
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.PageType
    public XmlBoolean xgetIsCustomName() {
        XmlBoolean xmlBoolean;
        synchronized (monitor()) {
            check_orphaned();
            xmlBoolean = (XmlBoolean) get_store().find_attribute_user(ISCUSTOMNAME$10);
        }
        return xmlBoolean;
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.PageType
    public boolean isSetIsCustomName() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(ISCUSTOMNAME$10) != null;
        }
        return z;
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.PageType
    public void setIsCustomName(boolean z) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(ISCUSTOMNAME$10);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(ISCUSTOMNAME$10);
            }
            simpleValue.setBooleanValue(z);
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.PageType
    public void xsetIsCustomName(XmlBoolean xmlBoolean) {
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(ISCUSTOMNAME$10);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_store().add_attribute_user(ISCUSTOMNAME$10);
            }
            xmlBoolean2.set(xmlBoolean);
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.PageType
    public void unsetIsCustomName() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(ISCUSTOMNAME$10);
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.PageType
    public boolean getIsCustomNameU() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(ISCUSTOMNAMEU$12);
            if (simpleValue == null) {
                return false;
            }
            return simpleValue.getBooleanValue();
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.PageType
    public XmlBoolean xgetIsCustomNameU() {
        XmlBoolean xmlBoolean;
        synchronized (monitor()) {
            check_orphaned();
            xmlBoolean = (XmlBoolean) get_store().find_attribute_user(ISCUSTOMNAMEU$12);
        }
        return xmlBoolean;
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.PageType
    public boolean isSetIsCustomNameU() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(ISCUSTOMNAMEU$12) != null;
        }
        return z;
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.PageType
    public void setIsCustomNameU(boolean z) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(ISCUSTOMNAMEU$12);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(ISCUSTOMNAMEU$12);
            }
            simpleValue.setBooleanValue(z);
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.PageType
    public void xsetIsCustomNameU(XmlBoolean xmlBoolean) {
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(ISCUSTOMNAMEU$12);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_store().add_attribute_user(ISCUSTOMNAMEU$12);
            }
            xmlBoolean2.set(xmlBoolean);
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.PageType
    public void unsetIsCustomNameU() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(ISCUSTOMNAMEU$12);
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.PageType
    public boolean getBackground() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(BACKGROUND$14);
            if (simpleValue == null) {
                return false;
            }
            return simpleValue.getBooleanValue();
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.PageType
    public XmlBoolean xgetBackground() {
        XmlBoolean xmlBoolean;
        synchronized (monitor()) {
            check_orphaned();
            xmlBoolean = (XmlBoolean) get_store().find_attribute_user(BACKGROUND$14);
        }
        return xmlBoolean;
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.PageType
    public boolean isSetBackground() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(BACKGROUND$14) != null;
        }
        return z;
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.PageType
    public void setBackground(boolean z) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(BACKGROUND$14);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(BACKGROUND$14);
            }
            simpleValue.setBooleanValue(z);
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.PageType
    public void xsetBackground(XmlBoolean xmlBoolean) {
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(BACKGROUND$14);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_store().add_attribute_user(BACKGROUND$14);
            }
            xmlBoolean2.set(xmlBoolean);
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.PageType
    public void unsetBackground() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(BACKGROUND$14);
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.PageType
    public long getBackPage() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(BACKPAGE$16);
            if (simpleValue == null) {
                return 0L;
            }
            return simpleValue.getLongValue();
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.PageType
    public XmlUnsignedInt xgetBackPage() {
        XmlUnsignedInt xmlUnsignedInt;
        synchronized (monitor()) {
            check_orphaned();
            xmlUnsignedInt = (XmlUnsignedInt) get_store().find_attribute_user(BACKPAGE$16);
        }
        return xmlUnsignedInt;
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.PageType
    public boolean isSetBackPage() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(BACKPAGE$16) != null;
        }
        return z;
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.PageType
    public void setBackPage(long j) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(BACKPAGE$16);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(BACKPAGE$16);
            }
            simpleValue.setLongValue(j);
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.PageType
    public void xsetBackPage(XmlUnsignedInt xmlUnsignedInt) {
        synchronized (monitor()) {
            check_orphaned();
            XmlUnsignedInt xmlUnsignedInt2 = (XmlUnsignedInt) get_store().find_attribute_user(BACKPAGE$16);
            if (xmlUnsignedInt2 == null) {
                xmlUnsignedInt2 = (XmlUnsignedInt) get_store().add_attribute_user(BACKPAGE$16);
            }
            xmlUnsignedInt2.set(xmlUnsignedInt);
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.PageType
    public void unsetBackPage() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(BACKPAGE$16);
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.PageType
    public double getViewScale() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(VIEWSCALE$18);
            if (simpleValue == null) {
                return 0.0d;
            }
            return simpleValue.getDoubleValue();
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.PageType
    public XmlDouble xgetViewScale() {
        XmlDouble xmlDouble;
        synchronized (monitor()) {
            check_orphaned();
            xmlDouble = (XmlDouble) get_store().find_attribute_user(VIEWSCALE$18);
        }
        return xmlDouble;
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.PageType
    public boolean isSetViewScale() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(VIEWSCALE$18) != null;
        }
        return z;
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.PageType
    public void setViewScale(double d) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(VIEWSCALE$18);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(VIEWSCALE$18);
            }
            simpleValue.setDoubleValue(d);
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.PageType
    public void xsetViewScale(XmlDouble xmlDouble) {
        synchronized (monitor()) {
            check_orphaned();
            XmlDouble xmlDouble2 = (XmlDouble) get_store().find_attribute_user(VIEWSCALE$18);
            if (xmlDouble2 == null) {
                xmlDouble2 = (XmlDouble) get_store().add_attribute_user(VIEWSCALE$18);
            }
            xmlDouble2.set(xmlDouble);
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.PageType
    public void unsetViewScale() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(VIEWSCALE$18);
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.PageType
    public double getViewCenterX() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(VIEWCENTERX$20);
            if (simpleValue == null) {
                return 0.0d;
            }
            return simpleValue.getDoubleValue();
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.PageType
    public XmlDouble xgetViewCenterX() {
        XmlDouble xmlDouble;
        synchronized (monitor()) {
            check_orphaned();
            xmlDouble = (XmlDouble) get_store().find_attribute_user(VIEWCENTERX$20);
        }
        return xmlDouble;
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.PageType
    public boolean isSetViewCenterX() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(VIEWCENTERX$20) != null;
        }
        return z;
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.PageType
    public void setViewCenterX(double d) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(VIEWCENTERX$20);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(VIEWCENTERX$20);
            }
            simpleValue.setDoubleValue(d);
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.PageType
    public void xsetViewCenterX(XmlDouble xmlDouble) {
        synchronized (monitor()) {
            check_orphaned();
            XmlDouble xmlDouble2 = (XmlDouble) get_store().find_attribute_user(VIEWCENTERX$20);
            if (xmlDouble2 == null) {
                xmlDouble2 = (XmlDouble) get_store().add_attribute_user(VIEWCENTERX$20);
            }
            xmlDouble2.set(xmlDouble);
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.PageType
    public void unsetViewCenterX() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(VIEWCENTERX$20);
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.PageType
    public double getViewCenterY() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(VIEWCENTERY$22);
            if (simpleValue == null) {
                return 0.0d;
            }
            return simpleValue.getDoubleValue();
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.PageType
    public XmlDouble xgetViewCenterY() {
        XmlDouble xmlDouble;
        synchronized (monitor()) {
            check_orphaned();
            xmlDouble = (XmlDouble) get_store().find_attribute_user(VIEWCENTERY$22);
        }
        return xmlDouble;
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.PageType
    public boolean isSetViewCenterY() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(VIEWCENTERY$22) != null;
        }
        return z;
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.PageType
    public void setViewCenterY(double d) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(VIEWCENTERY$22);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(VIEWCENTERY$22);
            }
            simpleValue.setDoubleValue(d);
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.PageType
    public void xsetViewCenterY(XmlDouble xmlDouble) {
        synchronized (monitor()) {
            check_orphaned();
            XmlDouble xmlDouble2 = (XmlDouble) get_store().find_attribute_user(VIEWCENTERY$22);
            if (xmlDouble2 == null) {
                xmlDouble2 = (XmlDouble) get_store().add_attribute_user(VIEWCENTERY$22);
            }
            xmlDouble2.set(xmlDouble);
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.PageType
    public void unsetViewCenterY() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(VIEWCENTERY$22);
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.PageType
    public long getReviewerID() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(REVIEWERID$24);
            if (simpleValue == null) {
                return 0L;
            }
            return simpleValue.getLongValue();
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.PageType
    public XmlUnsignedInt xgetReviewerID() {
        XmlUnsignedInt xmlUnsignedInt;
        synchronized (monitor()) {
            check_orphaned();
            xmlUnsignedInt = (XmlUnsignedInt) get_store().find_attribute_user(REVIEWERID$24);
        }
        return xmlUnsignedInt;
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.PageType
    public boolean isSetReviewerID() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(REVIEWERID$24) != null;
        }
        return z;
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.PageType
    public void setReviewerID(long j) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(REVIEWERID$24);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(REVIEWERID$24);
            }
            simpleValue.setLongValue(j);
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.PageType
    public void xsetReviewerID(XmlUnsignedInt xmlUnsignedInt) {
        synchronized (monitor()) {
            check_orphaned();
            XmlUnsignedInt xmlUnsignedInt2 = (XmlUnsignedInt) get_store().find_attribute_user(REVIEWERID$24);
            if (xmlUnsignedInt2 == null) {
                xmlUnsignedInt2 = (XmlUnsignedInt) get_store().add_attribute_user(REVIEWERID$24);
            }
            xmlUnsignedInt2.set(xmlUnsignedInt);
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.PageType
    public void unsetReviewerID() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(REVIEWERID$24);
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.PageType
    public long getAssociatedPage() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(ASSOCIATEDPAGE$26);
            if (simpleValue == null) {
                return 0L;
            }
            return simpleValue.getLongValue();
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.PageType
    public XmlUnsignedInt xgetAssociatedPage() {
        XmlUnsignedInt xmlUnsignedInt;
        synchronized (monitor()) {
            check_orphaned();
            xmlUnsignedInt = (XmlUnsignedInt) get_store().find_attribute_user(ASSOCIATEDPAGE$26);
        }
        return xmlUnsignedInt;
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.PageType
    public boolean isSetAssociatedPage() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(ASSOCIATEDPAGE$26) != null;
        }
        return z;
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.PageType
    public void setAssociatedPage(long j) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(ASSOCIATEDPAGE$26);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(ASSOCIATEDPAGE$26);
            }
            simpleValue.setLongValue(j);
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.PageType
    public void xsetAssociatedPage(XmlUnsignedInt xmlUnsignedInt) {
        synchronized (monitor()) {
            check_orphaned();
            XmlUnsignedInt xmlUnsignedInt2 = (XmlUnsignedInt) get_store().find_attribute_user(ASSOCIATEDPAGE$26);
            if (xmlUnsignedInt2 == null) {
                xmlUnsignedInt2 = (XmlUnsignedInt) get_store().add_attribute_user(ASSOCIATEDPAGE$26);
            }
            xmlUnsignedInt2.set(xmlUnsignedInt);
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.PageType
    public void unsetAssociatedPage() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(ASSOCIATEDPAGE$26);
        }
    }
}
