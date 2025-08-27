package com.microsoft.schemas.office.visio.x2012.main.impl;

import com.microsoft.schemas.office.visio.x2012.main.IconType;
import com.microsoft.schemas.office.visio.x2012.main.MasterType;
import com.microsoft.schemas.office.visio.x2012.main.PageSheetType;
import com.microsoft.schemas.office.visio.x2012.main.RelType;
import javax.xml.namespace.QName;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.SimpleValue;
import org.apache.xmlbeans.XmlBoolean;
import org.apache.xmlbeans.XmlString;
import org.apache.xmlbeans.XmlUnsignedInt;
import org.apache.xmlbeans.XmlUnsignedShort;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;

/* loaded from: poi-ooxml-schemas-3.17.jar:com/microsoft/schemas/office/visio/x2012/main/impl/MasterTypeImpl.class */
public class MasterTypeImpl extends XmlComplexContentImpl implements MasterType {
    private static final QName PAGESHEET$0 = new QName("http://schemas.microsoft.com/office/visio/2012/main", "PageSheet");
    private static final QName REL$2 = new QName("http://schemas.microsoft.com/office/visio/2012/main", "Rel");
    private static final QName ICON$4 = new QName("http://schemas.microsoft.com/office/visio/2012/main", "Icon");
    private static final QName ID$6 = new QName("", "ID");
    private static final QName BASEID$8 = new QName("", "BaseID");
    private static final QName UNIQUEID$10 = new QName("", "UniqueID");
    private static final QName MATCHBYNAME$12 = new QName("", "MatchByName");
    private static final QName NAME$14 = new QName("", "Name");
    private static final QName NAMEU$16 = new QName("", "NameU");
    private static final QName ISCUSTOMNAME$18 = new QName("", "IsCustomName");
    private static final QName ISCUSTOMNAMEU$20 = new QName("", "IsCustomNameU");
    private static final QName ICONSIZE$22 = new QName("", "IconSize");
    private static final QName PATTERNFLAGS$24 = new QName("", "PatternFlags");
    private static final QName PROMPT$26 = new QName("", "Prompt");
    private static final QName HIDDEN$28 = new QName("", "Hidden");
    private static final QName ICONUPDATE$30 = new QName("", "IconUpdate");
    private static final QName ALIGNNAME$32 = new QName("", "AlignName");
    private static final QName MASTERTYPE$34 = new QName("", "MasterType");

    public MasterTypeImpl(SchemaType schemaType) {
        super(schemaType);
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.MasterType
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

    @Override // com.microsoft.schemas.office.visio.x2012.main.MasterType
    public boolean isSetPageSheet() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().count_elements(PAGESHEET$0) != 0;
        }
        return z;
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.MasterType
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

    @Override // com.microsoft.schemas.office.visio.x2012.main.MasterType
    public PageSheetType addNewPageSheet() {
        PageSheetType pageSheetType;
        synchronized (monitor()) {
            check_orphaned();
            pageSheetType = (PageSheetType) get_store().add_element_user(PAGESHEET$0);
        }
        return pageSheetType;
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.MasterType
    public void unsetPageSheet() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PAGESHEET$0, 0);
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.MasterType
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

    @Override // com.microsoft.schemas.office.visio.x2012.main.MasterType
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

    @Override // com.microsoft.schemas.office.visio.x2012.main.MasterType
    public RelType addNewRel() {
        RelType relType;
        synchronized (monitor()) {
            check_orphaned();
            relType = (RelType) get_store().add_element_user(REL$2);
        }
        return relType;
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.MasterType
    public IconType getIcon() {
        synchronized (monitor()) {
            check_orphaned();
            IconType iconTypeFind_element_user = get_store().find_element_user(ICON$4, 0);
            if (iconTypeFind_element_user == null) {
                return null;
            }
            return iconTypeFind_element_user;
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.MasterType
    public boolean isSetIcon() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().count_elements(ICON$4) != 0;
        }
        return z;
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.MasterType
    public void setIcon(IconType iconType) {
        synchronized (monitor()) {
            check_orphaned();
            IconType iconTypeFind_element_user = get_store().find_element_user(ICON$4, 0);
            if (iconTypeFind_element_user == null) {
                iconTypeFind_element_user = (IconType) get_store().add_element_user(ICON$4);
            }
            iconTypeFind_element_user.set(iconType);
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.MasterType
    public IconType addNewIcon() {
        IconType iconTypeAdd_element_user;
        synchronized (monitor()) {
            check_orphaned();
            iconTypeAdd_element_user = get_store().add_element_user(ICON$4);
        }
        return iconTypeAdd_element_user;
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.MasterType
    public void unsetIcon() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(ICON$4, 0);
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.MasterType
    public long getID() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(ID$6);
            if (simpleValue == null) {
                return 0L;
            }
            return simpleValue.getLongValue();
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.MasterType
    public XmlUnsignedInt xgetID() {
        XmlUnsignedInt xmlUnsignedInt;
        synchronized (monitor()) {
            check_orphaned();
            xmlUnsignedInt = (XmlUnsignedInt) get_store().find_attribute_user(ID$6);
        }
        return xmlUnsignedInt;
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.MasterType
    public void setID(long j) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(ID$6);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(ID$6);
            }
            simpleValue.setLongValue(j);
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.MasterType
    public void xsetID(XmlUnsignedInt xmlUnsignedInt) {
        synchronized (monitor()) {
            check_orphaned();
            XmlUnsignedInt xmlUnsignedInt2 = (XmlUnsignedInt) get_store().find_attribute_user(ID$6);
            if (xmlUnsignedInt2 == null) {
                xmlUnsignedInt2 = (XmlUnsignedInt) get_store().add_attribute_user(ID$6);
            }
            xmlUnsignedInt2.set(xmlUnsignedInt);
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.MasterType
    public String getBaseID() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(BASEID$8);
            if (simpleValue == null) {
                return null;
            }
            return simpleValue.getStringValue();
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.MasterType
    public XmlString xgetBaseID() {
        XmlString xmlString;
        synchronized (monitor()) {
            check_orphaned();
            xmlString = (XmlString) get_store().find_attribute_user(BASEID$8);
        }
        return xmlString;
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.MasterType
    public boolean isSetBaseID() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(BASEID$8) != null;
        }
        return z;
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.MasterType
    public void setBaseID(String str) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(BASEID$8);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(BASEID$8);
            }
            simpleValue.setStringValue(str);
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.MasterType
    public void xsetBaseID(XmlString xmlString) {
        synchronized (monitor()) {
            check_orphaned();
            XmlString xmlString2 = (XmlString) get_store().find_attribute_user(BASEID$8);
            if (xmlString2 == null) {
                xmlString2 = (XmlString) get_store().add_attribute_user(BASEID$8);
            }
            xmlString2.set(xmlString);
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.MasterType
    public void unsetBaseID() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(BASEID$8);
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.MasterType
    public String getUniqueID() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(UNIQUEID$10);
            if (simpleValue == null) {
                return null;
            }
            return simpleValue.getStringValue();
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.MasterType
    public XmlString xgetUniqueID() {
        XmlString xmlString;
        synchronized (monitor()) {
            check_orphaned();
            xmlString = (XmlString) get_store().find_attribute_user(UNIQUEID$10);
        }
        return xmlString;
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.MasterType
    public boolean isSetUniqueID() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(UNIQUEID$10) != null;
        }
        return z;
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.MasterType
    public void setUniqueID(String str) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(UNIQUEID$10);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(UNIQUEID$10);
            }
            simpleValue.setStringValue(str);
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.MasterType
    public void xsetUniqueID(XmlString xmlString) {
        synchronized (monitor()) {
            check_orphaned();
            XmlString xmlString2 = (XmlString) get_store().find_attribute_user(UNIQUEID$10);
            if (xmlString2 == null) {
                xmlString2 = (XmlString) get_store().add_attribute_user(UNIQUEID$10);
            }
            xmlString2.set(xmlString);
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.MasterType
    public void unsetUniqueID() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(UNIQUEID$10);
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.MasterType
    public boolean getMatchByName() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(MATCHBYNAME$12);
            if (simpleValue == null) {
                return false;
            }
            return simpleValue.getBooleanValue();
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.MasterType
    public XmlBoolean xgetMatchByName() {
        XmlBoolean xmlBoolean;
        synchronized (monitor()) {
            check_orphaned();
            xmlBoolean = (XmlBoolean) get_store().find_attribute_user(MATCHBYNAME$12);
        }
        return xmlBoolean;
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.MasterType
    public boolean isSetMatchByName() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(MATCHBYNAME$12) != null;
        }
        return z;
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.MasterType
    public void setMatchByName(boolean z) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(MATCHBYNAME$12);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(MATCHBYNAME$12);
            }
            simpleValue.setBooleanValue(z);
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.MasterType
    public void xsetMatchByName(XmlBoolean xmlBoolean) {
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(MATCHBYNAME$12);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_store().add_attribute_user(MATCHBYNAME$12);
            }
            xmlBoolean2.set(xmlBoolean);
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.MasterType
    public void unsetMatchByName() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(MATCHBYNAME$12);
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.MasterType
    public String getName() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(NAME$14);
            if (simpleValue == null) {
                return null;
            }
            return simpleValue.getStringValue();
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.MasterType
    public XmlString xgetName() {
        XmlString xmlString;
        synchronized (monitor()) {
            check_orphaned();
            xmlString = (XmlString) get_store().find_attribute_user(NAME$14);
        }
        return xmlString;
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.MasterType
    public boolean isSetName() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(NAME$14) != null;
        }
        return z;
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.MasterType
    public void setName(String str) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(NAME$14);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(NAME$14);
            }
            simpleValue.setStringValue(str);
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.MasterType
    public void xsetName(XmlString xmlString) {
        synchronized (monitor()) {
            check_orphaned();
            XmlString xmlString2 = (XmlString) get_store().find_attribute_user(NAME$14);
            if (xmlString2 == null) {
                xmlString2 = (XmlString) get_store().add_attribute_user(NAME$14);
            }
            xmlString2.set(xmlString);
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.MasterType
    public void unsetName() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(NAME$14);
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.MasterType
    public String getNameU() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(NAMEU$16);
            if (simpleValue == null) {
                return null;
            }
            return simpleValue.getStringValue();
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.MasterType
    public XmlString xgetNameU() {
        XmlString xmlString;
        synchronized (monitor()) {
            check_orphaned();
            xmlString = (XmlString) get_store().find_attribute_user(NAMEU$16);
        }
        return xmlString;
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.MasterType
    public boolean isSetNameU() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(NAMEU$16) != null;
        }
        return z;
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.MasterType
    public void setNameU(String str) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(NAMEU$16);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(NAMEU$16);
            }
            simpleValue.setStringValue(str);
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.MasterType
    public void xsetNameU(XmlString xmlString) {
        synchronized (monitor()) {
            check_orphaned();
            XmlString xmlString2 = (XmlString) get_store().find_attribute_user(NAMEU$16);
            if (xmlString2 == null) {
                xmlString2 = (XmlString) get_store().add_attribute_user(NAMEU$16);
            }
            xmlString2.set(xmlString);
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.MasterType
    public void unsetNameU() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(NAMEU$16);
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.MasterType
    public boolean getIsCustomName() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(ISCUSTOMNAME$18);
            if (simpleValue == null) {
                return false;
            }
            return simpleValue.getBooleanValue();
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.MasterType
    public XmlBoolean xgetIsCustomName() {
        XmlBoolean xmlBoolean;
        synchronized (monitor()) {
            check_orphaned();
            xmlBoolean = (XmlBoolean) get_store().find_attribute_user(ISCUSTOMNAME$18);
        }
        return xmlBoolean;
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.MasterType
    public boolean isSetIsCustomName() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(ISCUSTOMNAME$18) != null;
        }
        return z;
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.MasterType
    public void setIsCustomName(boolean z) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(ISCUSTOMNAME$18);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(ISCUSTOMNAME$18);
            }
            simpleValue.setBooleanValue(z);
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.MasterType
    public void xsetIsCustomName(XmlBoolean xmlBoolean) {
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(ISCUSTOMNAME$18);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_store().add_attribute_user(ISCUSTOMNAME$18);
            }
            xmlBoolean2.set(xmlBoolean);
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.MasterType
    public void unsetIsCustomName() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(ISCUSTOMNAME$18);
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.MasterType
    public boolean getIsCustomNameU() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(ISCUSTOMNAMEU$20);
            if (simpleValue == null) {
                return false;
            }
            return simpleValue.getBooleanValue();
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.MasterType
    public XmlBoolean xgetIsCustomNameU() {
        XmlBoolean xmlBoolean;
        synchronized (monitor()) {
            check_orphaned();
            xmlBoolean = (XmlBoolean) get_store().find_attribute_user(ISCUSTOMNAMEU$20);
        }
        return xmlBoolean;
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.MasterType
    public boolean isSetIsCustomNameU() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(ISCUSTOMNAMEU$20) != null;
        }
        return z;
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.MasterType
    public void setIsCustomNameU(boolean z) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(ISCUSTOMNAMEU$20);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(ISCUSTOMNAMEU$20);
            }
            simpleValue.setBooleanValue(z);
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.MasterType
    public void xsetIsCustomNameU(XmlBoolean xmlBoolean) {
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(ISCUSTOMNAMEU$20);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_store().add_attribute_user(ISCUSTOMNAMEU$20);
            }
            xmlBoolean2.set(xmlBoolean);
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.MasterType
    public void unsetIsCustomNameU() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(ISCUSTOMNAMEU$20);
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.MasterType
    public int getIconSize() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(ICONSIZE$22);
            if (simpleValue == null) {
                return 0;
            }
            return simpleValue.getIntValue();
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.MasterType
    public XmlUnsignedShort xgetIconSize() {
        XmlUnsignedShort xmlUnsignedShort;
        synchronized (monitor()) {
            check_orphaned();
            xmlUnsignedShort = (XmlUnsignedShort) get_store().find_attribute_user(ICONSIZE$22);
        }
        return xmlUnsignedShort;
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.MasterType
    public boolean isSetIconSize() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(ICONSIZE$22) != null;
        }
        return z;
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.MasterType
    public void setIconSize(int i) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(ICONSIZE$22);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(ICONSIZE$22);
            }
            simpleValue.setIntValue(i);
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.MasterType
    public void xsetIconSize(XmlUnsignedShort xmlUnsignedShort) {
        synchronized (monitor()) {
            check_orphaned();
            XmlUnsignedShort xmlUnsignedShort2 = (XmlUnsignedShort) get_store().find_attribute_user(ICONSIZE$22);
            if (xmlUnsignedShort2 == null) {
                xmlUnsignedShort2 = (XmlUnsignedShort) get_store().add_attribute_user(ICONSIZE$22);
            }
            xmlUnsignedShort2.set(xmlUnsignedShort);
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.MasterType
    public void unsetIconSize() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(ICONSIZE$22);
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.MasterType
    public int getPatternFlags() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(PATTERNFLAGS$24);
            if (simpleValue == null) {
                return 0;
            }
            return simpleValue.getIntValue();
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.MasterType
    public XmlUnsignedShort xgetPatternFlags() {
        XmlUnsignedShort xmlUnsignedShort;
        synchronized (monitor()) {
            check_orphaned();
            xmlUnsignedShort = (XmlUnsignedShort) get_store().find_attribute_user(PATTERNFLAGS$24);
        }
        return xmlUnsignedShort;
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.MasterType
    public boolean isSetPatternFlags() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(PATTERNFLAGS$24) != null;
        }
        return z;
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.MasterType
    public void setPatternFlags(int i) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(PATTERNFLAGS$24);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(PATTERNFLAGS$24);
            }
            simpleValue.setIntValue(i);
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.MasterType
    public void xsetPatternFlags(XmlUnsignedShort xmlUnsignedShort) {
        synchronized (monitor()) {
            check_orphaned();
            XmlUnsignedShort xmlUnsignedShort2 = (XmlUnsignedShort) get_store().find_attribute_user(PATTERNFLAGS$24);
            if (xmlUnsignedShort2 == null) {
                xmlUnsignedShort2 = (XmlUnsignedShort) get_store().add_attribute_user(PATTERNFLAGS$24);
            }
            xmlUnsignedShort2.set(xmlUnsignedShort);
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.MasterType
    public void unsetPatternFlags() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PATTERNFLAGS$24);
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.MasterType
    public String getPrompt() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(PROMPT$26);
            if (simpleValue == null) {
                return null;
            }
            return simpleValue.getStringValue();
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.MasterType
    public XmlString xgetPrompt() {
        XmlString xmlString;
        synchronized (monitor()) {
            check_orphaned();
            xmlString = (XmlString) get_store().find_attribute_user(PROMPT$26);
        }
        return xmlString;
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.MasterType
    public boolean isSetPrompt() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(PROMPT$26) != null;
        }
        return z;
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.MasterType
    public void setPrompt(String str) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(PROMPT$26);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(PROMPT$26);
            }
            simpleValue.setStringValue(str);
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.MasterType
    public void xsetPrompt(XmlString xmlString) {
        synchronized (monitor()) {
            check_orphaned();
            XmlString xmlString2 = (XmlString) get_store().find_attribute_user(PROMPT$26);
            if (xmlString2 == null) {
                xmlString2 = (XmlString) get_store().add_attribute_user(PROMPT$26);
            }
            xmlString2.set(xmlString);
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.MasterType
    public void unsetPrompt() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROMPT$26);
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.MasterType
    public boolean getHidden() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(HIDDEN$28);
            if (simpleValue == null) {
                return false;
            }
            return simpleValue.getBooleanValue();
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.MasterType
    public XmlBoolean xgetHidden() {
        XmlBoolean xmlBoolean;
        synchronized (monitor()) {
            check_orphaned();
            xmlBoolean = (XmlBoolean) get_store().find_attribute_user(HIDDEN$28);
        }
        return xmlBoolean;
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.MasterType
    public boolean isSetHidden() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(HIDDEN$28) != null;
        }
        return z;
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.MasterType
    public void setHidden(boolean z) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(HIDDEN$28);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(HIDDEN$28);
            }
            simpleValue.setBooleanValue(z);
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.MasterType
    public void xsetHidden(XmlBoolean xmlBoolean) {
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(HIDDEN$28);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_store().add_attribute_user(HIDDEN$28);
            }
            xmlBoolean2.set(xmlBoolean);
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.MasterType
    public void unsetHidden() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(HIDDEN$28);
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.MasterType
    public boolean getIconUpdate() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(ICONUPDATE$30);
            if (simpleValue == null) {
                return false;
            }
            return simpleValue.getBooleanValue();
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.MasterType
    public XmlBoolean xgetIconUpdate() {
        XmlBoolean xmlBoolean;
        synchronized (monitor()) {
            check_orphaned();
            xmlBoolean = (XmlBoolean) get_store().find_attribute_user(ICONUPDATE$30);
        }
        return xmlBoolean;
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.MasterType
    public boolean isSetIconUpdate() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(ICONUPDATE$30) != null;
        }
        return z;
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.MasterType
    public void setIconUpdate(boolean z) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(ICONUPDATE$30);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(ICONUPDATE$30);
            }
            simpleValue.setBooleanValue(z);
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.MasterType
    public void xsetIconUpdate(XmlBoolean xmlBoolean) {
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(ICONUPDATE$30);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_store().add_attribute_user(ICONUPDATE$30);
            }
            xmlBoolean2.set(xmlBoolean);
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.MasterType
    public void unsetIconUpdate() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(ICONUPDATE$30);
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.MasterType
    public int getAlignName() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(ALIGNNAME$32);
            if (simpleValue == null) {
                return 0;
            }
            return simpleValue.getIntValue();
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.MasterType
    public XmlUnsignedShort xgetAlignName() {
        XmlUnsignedShort xmlUnsignedShort;
        synchronized (monitor()) {
            check_orphaned();
            xmlUnsignedShort = (XmlUnsignedShort) get_store().find_attribute_user(ALIGNNAME$32);
        }
        return xmlUnsignedShort;
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.MasterType
    public boolean isSetAlignName() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(ALIGNNAME$32) != null;
        }
        return z;
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.MasterType
    public void setAlignName(int i) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(ALIGNNAME$32);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(ALIGNNAME$32);
            }
            simpleValue.setIntValue(i);
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.MasterType
    public void xsetAlignName(XmlUnsignedShort xmlUnsignedShort) {
        synchronized (monitor()) {
            check_orphaned();
            XmlUnsignedShort xmlUnsignedShort2 = (XmlUnsignedShort) get_store().find_attribute_user(ALIGNNAME$32);
            if (xmlUnsignedShort2 == null) {
                xmlUnsignedShort2 = (XmlUnsignedShort) get_store().add_attribute_user(ALIGNNAME$32);
            }
            xmlUnsignedShort2.set(xmlUnsignedShort);
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.MasterType
    public void unsetAlignName() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(ALIGNNAME$32);
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.MasterType
    public int getMasterType() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(MASTERTYPE$34);
            if (simpleValue == null) {
                return 0;
            }
            return simpleValue.getIntValue();
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.MasterType
    public XmlUnsignedShort xgetMasterType() {
        XmlUnsignedShort xmlUnsignedShort;
        synchronized (monitor()) {
            check_orphaned();
            xmlUnsignedShort = (XmlUnsignedShort) get_store().find_attribute_user(MASTERTYPE$34);
        }
        return xmlUnsignedShort;
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.MasterType
    public boolean isSetMasterType() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(MASTERTYPE$34) != null;
        }
        return z;
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.MasterType
    public void setMasterType(int i) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(MASTERTYPE$34);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(MASTERTYPE$34);
            }
            simpleValue.setIntValue(i);
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.MasterType
    public void xsetMasterType(XmlUnsignedShort xmlUnsignedShort) {
        synchronized (monitor()) {
            check_orphaned();
            XmlUnsignedShort xmlUnsignedShort2 = (XmlUnsignedShort) get_store().find_attribute_user(MASTERTYPE$34);
            if (xmlUnsignedShort2 == null) {
                xmlUnsignedShort2 = (XmlUnsignedShort) get_store().add_attribute_user(MASTERTYPE$34);
            }
            xmlUnsignedShort2.set(xmlUnsignedShort);
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.MasterType
    public void unsetMasterType() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(MASTERTYPE$34);
        }
    }
}
