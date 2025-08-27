package org.openxmlformats.schemas.spreadsheetml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.SimpleValue;
import org.apache.xmlbeans.XmlBoolean;
import org.apache.xmlbeans.XmlString;
import org.apache.xmlbeans.XmlUnsignedInt;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorkbookPr;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.STObjects;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.STObjects$Enum;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.STUpdateLinks;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.STUpdateLinks$Enum;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/impl/CTWorkbookPrImpl.class */
public class CTWorkbookPrImpl extends XmlComplexContentImpl implements CTWorkbookPr {
    private static final QName DATE1904$0 = new QName("", "date1904");
    private static final QName SHOWOBJECTS$2 = new QName("", "showObjects");
    private static final QName SHOWBORDERUNSELECTEDTABLES$4 = new QName("", "showBorderUnselectedTables");
    private static final QName FILTERPRIVACY$6 = new QName("", "filterPrivacy");
    private static final QName PROMPTEDSOLUTIONS$8 = new QName("", "promptedSolutions");
    private static final QName SHOWINKANNOTATION$10 = new QName("", "showInkAnnotation");
    private static final QName BACKUPFILE$12 = new QName("", "backupFile");
    private static final QName SAVEEXTERNALLINKVALUES$14 = new QName("", "saveExternalLinkValues");
    private static final QName UPDATELINKS$16 = new QName("", "updateLinks");
    private static final QName CODENAME$18 = new QName("", "codeName");
    private static final QName HIDEPIVOTFIELDLIST$20 = new QName("", "hidePivotFieldList");
    private static final QName SHOWPIVOTCHARTFILTER$22 = new QName("", "showPivotChartFilter");
    private static final QName ALLOWREFRESHQUERY$24 = new QName("", "allowRefreshQuery");
    private static final QName PUBLISHITEMS$26 = new QName("", "publishItems");
    private static final QName CHECKCOMPATIBILITY$28 = new QName("", "checkCompatibility");
    private static final QName AUTOCOMPRESSPICTURES$30 = new QName("", "autoCompressPictures");
    private static final QName REFRESHALLCONNECTIONS$32 = new QName("", "refreshAllConnections");
    private static final QName DEFAULTTHEMEVERSION$34 = new QName("", "defaultThemeVersion");

    public CTWorkbookPrImpl(SchemaType schemaType) {
        super(schemaType);
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorkbookPr
    public boolean getDate1904() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(DATE1904$0);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(DATE1904$0);
            }
            if (simpleValue == null) {
                return false;
            }
            return simpleValue.getBooleanValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorkbookPr
    public XmlBoolean xgetDate1904() {
        XmlBoolean xmlBoolean;
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(DATE1904$0);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_default_attribute_value(DATE1904$0);
            }
            xmlBoolean = xmlBoolean2;
        }
        return xmlBoolean;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorkbookPr
    public boolean isSetDate1904() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(DATE1904$0) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorkbookPr
    public void setDate1904(boolean z) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(DATE1904$0);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(DATE1904$0);
            }
            simpleValue.setBooleanValue(z);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorkbookPr
    public void xsetDate1904(XmlBoolean xmlBoolean) {
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(DATE1904$0);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_store().add_attribute_user(DATE1904$0);
            }
            xmlBoolean2.set(xmlBoolean);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorkbookPr
    public void unsetDate1904() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(DATE1904$0);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorkbookPr
    public STObjects$Enum getShowObjects() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(SHOWOBJECTS$2);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(SHOWOBJECTS$2);
            }
            if (simpleValue == null) {
                return null;
            }
            return (STObjects$Enum) simpleValue.getEnumValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorkbookPr
    public STObjects xgetShowObjects() {
        STObjects sTObjects;
        synchronized (monitor()) {
            check_orphaned();
            STObjects sTObjectsFind_attribute_user = get_store().find_attribute_user(SHOWOBJECTS$2);
            if (sTObjectsFind_attribute_user == null) {
                sTObjectsFind_attribute_user = (STObjects) get_default_attribute_value(SHOWOBJECTS$2);
            }
            sTObjects = sTObjectsFind_attribute_user;
        }
        return sTObjects;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorkbookPr
    public boolean isSetShowObjects() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(SHOWOBJECTS$2) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorkbookPr
    public void setShowObjects(STObjects$Enum sTObjects$Enum) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(SHOWOBJECTS$2);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(SHOWOBJECTS$2);
            }
            simpleValue.setEnumValue(sTObjects$Enum);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorkbookPr
    public void xsetShowObjects(STObjects sTObjects) {
        synchronized (monitor()) {
            check_orphaned();
            STObjects sTObjectsFind_attribute_user = get_store().find_attribute_user(SHOWOBJECTS$2);
            if (sTObjectsFind_attribute_user == null) {
                sTObjectsFind_attribute_user = (STObjects) get_store().add_attribute_user(SHOWOBJECTS$2);
            }
            sTObjectsFind_attribute_user.set(sTObjects);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorkbookPr
    public void unsetShowObjects() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(SHOWOBJECTS$2);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorkbookPr
    public boolean getShowBorderUnselectedTables() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(SHOWBORDERUNSELECTEDTABLES$4);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(SHOWBORDERUNSELECTEDTABLES$4);
            }
            if (simpleValue == null) {
                return false;
            }
            return simpleValue.getBooleanValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorkbookPr
    public XmlBoolean xgetShowBorderUnselectedTables() {
        XmlBoolean xmlBoolean;
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(SHOWBORDERUNSELECTEDTABLES$4);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_default_attribute_value(SHOWBORDERUNSELECTEDTABLES$4);
            }
            xmlBoolean = xmlBoolean2;
        }
        return xmlBoolean;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorkbookPr
    public boolean isSetShowBorderUnselectedTables() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(SHOWBORDERUNSELECTEDTABLES$4) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorkbookPr
    public void setShowBorderUnselectedTables(boolean z) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(SHOWBORDERUNSELECTEDTABLES$4);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(SHOWBORDERUNSELECTEDTABLES$4);
            }
            simpleValue.setBooleanValue(z);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorkbookPr
    public void xsetShowBorderUnselectedTables(XmlBoolean xmlBoolean) {
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(SHOWBORDERUNSELECTEDTABLES$4);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_store().add_attribute_user(SHOWBORDERUNSELECTEDTABLES$4);
            }
            xmlBoolean2.set(xmlBoolean);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorkbookPr
    public void unsetShowBorderUnselectedTables() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(SHOWBORDERUNSELECTEDTABLES$4);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorkbookPr
    public boolean getFilterPrivacy() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(FILTERPRIVACY$6);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(FILTERPRIVACY$6);
            }
            if (simpleValue == null) {
                return false;
            }
            return simpleValue.getBooleanValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorkbookPr
    public XmlBoolean xgetFilterPrivacy() {
        XmlBoolean xmlBoolean;
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(FILTERPRIVACY$6);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_default_attribute_value(FILTERPRIVACY$6);
            }
            xmlBoolean = xmlBoolean2;
        }
        return xmlBoolean;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorkbookPr
    public boolean isSetFilterPrivacy() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(FILTERPRIVACY$6) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorkbookPr
    public void setFilterPrivacy(boolean z) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(FILTERPRIVACY$6);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(FILTERPRIVACY$6);
            }
            simpleValue.setBooleanValue(z);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorkbookPr
    public void xsetFilterPrivacy(XmlBoolean xmlBoolean) {
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(FILTERPRIVACY$6);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_store().add_attribute_user(FILTERPRIVACY$6);
            }
            xmlBoolean2.set(xmlBoolean);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorkbookPr
    public void unsetFilterPrivacy() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(FILTERPRIVACY$6);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorkbookPr
    public boolean getPromptedSolutions() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(PROMPTEDSOLUTIONS$8);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(PROMPTEDSOLUTIONS$8);
            }
            if (simpleValue == null) {
                return false;
            }
            return simpleValue.getBooleanValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorkbookPr
    public XmlBoolean xgetPromptedSolutions() {
        XmlBoolean xmlBoolean;
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(PROMPTEDSOLUTIONS$8);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_default_attribute_value(PROMPTEDSOLUTIONS$8);
            }
            xmlBoolean = xmlBoolean2;
        }
        return xmlBoolean;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorkbookPr
    public boolean isSetPromptedSolutions() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(PROMPTEDSOLUTIONS$8) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorkbookPr
    public void setPromptedSolutions(boolean z) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(PROMPTEDSOLUTIONS$8);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(PROMPTEDSOLUTIONS$8);
            }
            simpleValue.setBooleanValue(z);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorkbookPr
    public void xsetPromptedSolutions(XmlBoolean xmlBoolean) {
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(PROMPTEDSOLUTIONS$8);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_store().add_attribute_user(PROMPTEDSOLUTIONS$8);
            }
            xmlBoolean2.set(xmlBoolean);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorkbookPr
    public void unsetPromptedSolutions() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROMPTEDSOLUTIONS$8);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorkbookPr
    public boolean getShowInkAnnotation() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(SHOWINKANNOTATION$10);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(SHOWINKANNOTATION$10);
            }
            if (simpleValue == null) {
                return false;
            }
            return simpleValue.getBooleanValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorkbookPr
    public XmlBoolean xgetShowInkAnnotation() {
        XmlBoolean xmlBoolean;
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(SHOWINKANNOTATION$10);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_default_attribute_value(SHOWINKANNOTATION$10);
            }
            xmlBoolean = xmlBoolean2;
        }
        return xmlBoolean;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorkbookPr
    public boolean isSetShowInkAnnotation() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(SHOWINKANNOTATION$10) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorkbookPr
    public void setShowInkAnnotation(boolean z) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(SHOWINKANNOTATION$10);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(SHOWINKANNOTATION$10);
            }
            simpleValue.setBooleanValue(z);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorkbookPr
    public void xsetShowInkAnnotation(XmlBoolean xmlBoolean) {
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(SHOWINKANNOTATION$10);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_store().add_attribute_user(SHOWINKANNOTATION$10);
            }
            xmlBoolean2.set(xmlBoolean);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorkbookPr
    public void unsetShowInkAnnotation() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(SHOWINKANNOTATION$10);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorkbookPr
    public boolean getBackupFile() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(BACKUPFILE$12);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(BACKUPFILE$12);
            }
            if (simpleValue == null) {
                return false;
            }
            return simpleValue.getBooleanValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorkbookPr
    public XmlBoolean xgetBackupFile() {
        XmlBoolean xmlBoolean;
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(BACKUPFILE$12);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_default_attribute_value(BACKUPFILE$12);
            }
            xmlBoolean = xmlBoolean2;
        }
        return xmlBoolean;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorkbookPr
    public boolean isSetBackupFile() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(BACKUPFILE$12) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorkbookPr
    public void setBackupFile(boolean z) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(BACKUPFILE$12);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(BACKUPFILE$12);
            }
            simpleValue.setBooleanValue(z);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorkbookPr
    public void xsetBackupFile(XmlBoolean xmlBoolean) {
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(BACKUPFILE$12);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_store().add_attribute_user(BACKUPFILE$12);
            }
            xmlBoolean2.set(xmlBoolean);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorkbookPr
    public void unsetBackupFile() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(BACKUPFILE$12);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorkbookPr
    public boolean getSaveExternalLinkValues() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(SAVEEXTERNALLINKVALUES$14);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(SAVEEXTERNALLINKVALUES$14);
            }
            if (simpleValue == null) {
                return false;
            }
            return simpleValue.getBooleanValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorkbookPr
    public XmlBoolean xgetSaveExternalLinkValues() {
        XmlBoolean xmlBoolean;
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(SAVEEXTERNALLINKVALUES$14);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_default_attribute_value(SAVEEXTERNALLINKVALUES$14);
            }
            xmlBoolean = xmlBoolean2;
        }
        return xmlBoolean;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorkbookPr
    public boolean isSetSaveExternalLinkValues() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(SAVEEXTERNALLINKVALUES$14) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorkbookPr
    public void setSaveExternalLinkValues(boolean z) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(SAVEEXTERNALLINKVALUES$14);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(SAVEEXTERNALLINKVALUES$14);
            }
            simpleValue.setBooleanValue(z);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorkbookPr
    public void xsetSaveExternalLinkValues(XmlBoolean xmlBoolean) {
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(SAVEEXTERNALLINKVALUES$14);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_store().add_attribute_user(SAVEEXTERNALLINKVALUES$14);
            }
            xmlBoolean2.set(xmlBoolean);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorkbookPr
    public void unsetSaveExternalLinkValues() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(SAVEEXTERNALLINKVALUES$14);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorkbookPr
    public STUpdateLinks$Enum getUpdateLinks() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(UPDATELINKS$16);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(UPDATELINKS$16);
            }
            if (simpleValue == null) {
                return null;
            }
            return (STUpdateLinks$Enum) simpleValue.getEnumValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorkbookPr
    public STUpdateLinks xgetUpdateLinks() {
        STUpdateLinks sTUpdateLinks;
        synchronized (monitor()) {
            check_orphaned();
            STUpdateLinks sTUpdateLinksFind_attribute_user = get_store().find_attribute_user(UPDATELINKS$16);
            if (sTUpdateLinksFind_attribute_user == null) {
                sTUpdateLinksFind_attribute_user = (STUpdateLinks) get_default_attribute_value(UPDATELINKS$16);
            }
            sTUpdateLinks = sTUpdateLinksFind_attribute_user;
        }
        return sTUpdateLinks;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorkbookPr
    public boolean isSetUpdateLinks() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(UPDATELINKS$16) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorkbookPr
    public void setUpdateLinks(STUpdateLinks$Enum sTUpdateLinks$Enum) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(UPDATELINKS$16);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(UPDATELINKS$16);
            }
            simpleValue.setEnumValue(sTUpdateLinks$Enum);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorkbookPr
    public void xsetUpdateLinks(STUpdateLinks sTUpdateLinks) {
        synchronized (monitor()) {
            check_orphaned();
            STUpdateLinks sTUpdateLinksFind_attribute_user = get_store().find_attribute_user(UPDATELINKS$16);
            if (sTUpdateLinksFind_attribute_user == null) {
                sTUpdateLinksFind_attribute_user = (STUpdateLinks) get_store().add_attribute_user(UPDATELINKS$16);
            }
            sTUpdateLinksFind_attribute_user.set(sTUpdateLinks);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorkbookPr
    public void unsetUpdateLinks() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(UPDATELINKS$16);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorkbookPr
    public String getCodeName() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(CODENAME$18);
            if (simpleValue == null) {
                return null;
            }
            return simpleValue.getStringValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorkbookPr
    public XmlString xgetCodeName() {
        XmlString xmlString;
        synchronized (monitor()) {
            check_orphaned();
            xmlString = (XmlString) get_store().find_attribute_user(CODENAME$18);
        }
        return xmlString;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorkbookPr
    public boolean isSetCodeName() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(CODENAME$18) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorkbookPr
    public void setCodeName(String str) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(CODENAME$18);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(CODENAME$18);
            }
            simpleValue.setStringValue(str);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorkbookPr
    public void xsetCodeName(XmlString xmlString) {
        synchronized (monitor()) {
            check_orphaned();
            XmlString xmlString2 = (XmlString) get_store().find_attribute_user(CODENAME$18);
            if (xmlString2 == null) {
                xmlString2 = (XmlString) get_store().add_attribute_user(CODENAME$18);
            }
            xmlString2.set(xmlString);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorkbookPr
    public void unsetCodeName() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(CODENAME$18);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorkbookPr
    public boolean getHidePivotFieldList() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(HIDEPIVOTFIELDLIST$20);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(HIDEPIVOTFIELDLIST$20);
            }
            if (simpleValue == null) {
                return false;
            }
            return simpleValue.getBooleanValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorkbookPr
    public XmlBoolean xgetHidePivotFieldList() {
        XmlBoolean xmlBoolean;
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(HIDEPIVOTFIELDLIST$20);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_default_attribute_value(HIDEPIVOTFIELDLIST$20);
            }
            xmlBoolean = xmlBoolean2;
        }
        return xmlBoolean;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorkbookPr
    public boolean isSetHidePivotFieldList() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(HIDEPIVOTFIELDLIST$20) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorkbookPr
    public void setHidePivotFieldList(boolean z) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(HIDEPIVOTFIELDLIST$20);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(HIDEPIVOTFIELDLIST$20);
            }
            simpleValue.setBooleanValue(z);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorkbookPr
    public void xsetHidePivotFieldList(XmlBoolean xmlBoolean) {
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(HIDEPIVOTFIELDLIST$20);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_store().add_attribute_user(HIDEPIVOTFIELDLIST$20);
            }
            xmlBoolean2.set(xmlBoolean);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorkbookPr
    public void unsetHidePivotFieldList() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(HIDEPIVOTFIELDLIST$20);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorkbookPr
    public boolean getShowPivotChartFilter() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(SHOWPIVOTCHARTFILTER$22);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(SHOWPIVOTCHARTFILTER$22);
            }
            if (simpleValue == null) {
                return false;
            }
            return simpleValue.getBooleanValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorkbookPr
    public XmlBoolean xgetShowPivotChartFilter() {
        XmlBoolean xmlBoolean;
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(SHOWPIVOTCHARTFILTER$22);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_default_attribute_value(SHOWPIVOTCHARTFILTER$22);
            }
            xmlBoolean = xmlBoolean2;
        }
        return xmlBoolean;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorkbookPr
    public boolean isSetShowPivotChartFilter() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(SHOWPIVOTCHARTFILTER$22) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorkbookPr
    public void setShowPivotChartFilter(boolean z) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(SHOWPIVOTCHARTFILTER$22);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(SHOWPIVOTCHARTFILTER$22);
            }
            simpleValue.setBooleanValue(z);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorkbookPr
    public void xsetShowPivotChartFilter(XmlBoolean xmlBoolean) {
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(SHOWPIVOTCHARTFILTER$22);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_store().add_attribute_user(SHOWPIVOTCHARTFILTER$22);
            }
            xmlBoolean2.set(xmlBoolean);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorkbookPr
    public void unsetShowPivotChartFilter() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(SHOWPIVOTCHARTFILTER$22);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorkbookPr
    public boolean getAllowRefreshQuery() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(ALLOWREFRESHQUERY$24);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(ALLOWREFRESHQUERY$24);
            }
            if (simpleValue == null) {
                return false;
            }
            return simpleValue.getBooleanValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorkbookPr
    public XmlBoolean xgetAllowRefreshQuery() {
        XmlBoolean xmlBoolean;
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(ALLOWREFRESHQUERY$24);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_default_attribute_value(ALLOWREFRESHQUERY$24);
            }
            xmlBoolean = xmlBoolean2;
        }
        return xmlBoolean;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorkbookPr
    public boolean isSetAllowRefreshQuery() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(ALLOWREFRESHQUERY$24) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorkbookPr
    public void setAllowRefreshQuery(boolean z) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(ALLOWREFRESHQUERY$24);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(ALLOWREFRESHQUERY$24);
            }
            simpleValue.setBooleanValue(z);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorkbookPr
    public void xsetAllowRefreshQuery(XmlBoolean xmlBoolean) {
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(ALLOWREFRESHQUERY$24);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_store().add_attribute_user(ALLOWREFRESHQUERY$24);
            }
            xmlBoolean2.set(xmlBoolean);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorkbookPr
    public void unsetAllowRefreshQuery() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(ALLOWREFRESHQUERY$24);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorkbookPr
    public boolean getPublishItems() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(PUBLISHITEMS$26);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(PUBLISHITEMS$26);
            }
            if (simpleValue == null) {
                return false;
            }
            return simpleValue.getBooleanValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorkbookPr
    public XmlBoolean xgetPublishItems() {
        XmlBoolean xmlBoolean;
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(PUBLISHITEMS$26);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_default_attribute_value(PUBLISHITEMS$26);
            }
            xmlBoolean = xmlBoolean2;
        }
        return xmlBoolean;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorkbookPr
    public boolean isSetPublishItems() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(PUBLISHITEMS$26) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorkbookPr
    public void setPublishItems(boolean z) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(PUBLISHITEMS$26);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(PUBLISHITEMS$26);
            }
            simpleValue.setBooleanValue(z);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorkbookPr
    public void xsetPublishItems(XmlBoolean xmlBoolean) {
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(PUBLISHITEMS$26);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_store().add_attribute_user(PUBLISHITEMS$26);
            }
            xmlBoolean2.set(xmlBoolean);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorkbookPr
    public void unsetPublishItems() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PUBLISHITEMS$26);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorkbookPr
    public boolean getCheckCompatibility() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(CHECKCOMPATIBILITY$28);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(CHECKCOMPATIBILITY$28);
            }
            if (simpleValue == null) {
                return false;
            }
            return simpleValue.getBooleanValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorkbookPr
    public XmlBoolean xgetCheckCompatibility() {
        XmlBoolean xmlBoolean;
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(CHECKCOMPATIBILITY$28);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_default_attribute_value(CHECKCOMPATIBILITY$28);
            }
            xmlBoolean = xmlBoolean2;
        }
        return xmlBoolean;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorkbookPr
    public boolean isSetCheckCompatibility() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(CHECKCOMPATIBILITY$28) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorkbookPr
    public void setCheckCompatibility(boolean z) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(CHECKCOMPATIBILITY$28);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(CHECKCOMPATIBILITY$28);
            }
            simpleValue.setBooleanValue(z);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorkbookPr
    public void xsetCheckCompatibility(XmlBoolean xmlBoolean) {
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(CHECKCOMPATIBILITY$28);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_store().add_attribute_user(CHECKCOMPATIBILITY$28);
            }
            xmlBoolean2.set(xmlBoolean);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorkbookPr
    public void unsetCheckCompatibility() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(CHECKCOMPATIBILITY$28);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorkbookPr
    public boolean getAutoCompressPictures() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(AUTOCOMPRESSPICTURES$30);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(AUTOCOMPRESSPICTURES$30);
            }
            if (simpleValue == null) {
                return false;
            }
            return simpleValue.getBooleanValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorkbookPr
    public XmlBoolean xgetAutoCompressPictures() {
        XmlBoolean xmlBoolean;
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(AUTOCOMPRESSPICTURES$30);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_default_attribute_value(AUTOCOMPRESSPICTURES$30);
            }
            xmlBoolean = xmlBoolean2;
        }
        return xmlBoolean;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorkbookPr
    public boolean isSetAutoCompressPictures() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(AUTOCOMPRESSPICTURES$30) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorkbookPr
    public void setAutoCompressPictures(boolean z) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(AUTOCOMPRESSPICTURES$30);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(AUTOCOMPRESSPICTURES$30);
            }
            simpleValue.setBooleanValue(z);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorkbookPr
    public void xsetAutoCompressPictures(XmlBoolean xmlBoolean) {
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(AUTOCOMPRESSPICTURES$30);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_store().add_attribute_user(AUTOCOMPRESSPICTURES$30);
            }
            xmlBoolean2.set(xmlBoolean);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorkbookPr
    public void unsetAutoCompressPictures() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(AUTOCOMPRESSPICTURES$30);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorkbookPr
    public boolean getRefreshAllConnections() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(REFRESHALLCONNECTIONS$32);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(REFRESHALLCONNECTIONS$32);
            }
            if (simpleValue == null) {
                return false;
            }
            return simpleValue.getBooleanValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorkbookPr
    public XmlBoolean xgetRefreshAllConnections() {
        XmlBoolean xmlBoolean;
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(REFRESHALLCONNECTIONS$32);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_default_attribute_value(REFRESHALLCONNECTIONS$32);
            }
            xmlBoolean = xmlBoolean2;
        }
        return xmlBoolean;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorkbookPr
    public boolean isSetRefreshAllConnections() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(REFRESHALLCONNECTIONS$32) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorkbookPr
    public void setRefreshAllConnections(boolean z) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(REFRESHALLCONNECTIONS$32);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(REFRESHALLCONNECTIONS$32);
            }
            simpleValue.setBooleanValue(z);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorkbookPr
    public void xsetRefreshAllConnections(XmlBoolean xmlBoolean) {
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(REFRESHALLCONNECTIONS$32);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_store().add_attribute_user(REFRESHALLCONNECTIONS$32);
            }
            xmlBoolean2.set(xmlBoolean);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorkbookPr
    public void unsetRefreshAllConnections() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(REFRESHALLCONNECTIONS$32);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorkbookPr
    public long getDefaultThemeVersion() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(DEFAULTTHEMEVERSION$34);
            if (simpleValue == null) {
                return 0L;
            }
            return simpleValue.getLongValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorkbookPr
    public XmlUnsignedInt xgetDefaultThemeVersion() {
        XmlUnsignedInt xmlUnsignedInt;
        synchronized (monitor()) {
            check_orphaned();
            xmlUnsignedInt = (XmlUnsignedInt) get_store().find_attribute_user(DEFAULTTHEMEVERSION$34);
        }
        return xmlUnsignedInt;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorkbookPr
    public boolean isSetDefaultThemeVersion() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(DEFAULTTHEMEVERSION$34) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorkbookPr
    public void setDefaultThemeVersion(long j) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(DEFAULTTHEMEVERSION$34);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(DEFAULTTHEMEVERSION$34);
            }
            simpleValue.setLongValue(j);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorkbookPr
    public void xsetDefaultThemeVersion(XmlUnsignedInt xmlUnsignedInt) {
        synchronized (monitor()) {
            check_orphaned();
            XmlUnsignedInt xmlUnsignedInt2 = (XmlUnsignedInt) get_store().find_attribute_user(DEFAULTTHEMEVERSION$34);
            if (xmlUnsignedInt2 == null) {
                xmlUnsignedInt2 = (XmlUnsignedInt) get_store().add_attribute_user(DEFAULTTHEMEVERSION$34);
            }
            xmlUnsignedInt2.set(xmlUnsignedInt);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorkbookPr
    public void unsetDefaultThemeVersion() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(DEFAULTTHEMEVERSION$34);
        }
    }
}
