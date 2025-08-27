package com.microsoft.schemas.office.office.impl;

import com.microsoft.schemas.office.office.CTLock;
import com.microsoft.schemas.office.office.STTrueFalse;
import com.microsoft.schemas.office.office.STTrueFalse$Enum;
import com.microsoft.schemas.vml.STExt;
import javax.xml.namespace.QName;
import org.apache.poi.ss.util.CellUtil;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.SimpleValue;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;
import org.bouncycastle.i18n.TextBundle;

/* loaded from: poi-ooxml-schemas-3.17.jar:com/microsoft/schemas/office/office/impl/CTLockImpl.class */
public class CTLockImpl extends XmlComplexContentImpl implements CTLock {
    private static final QName EXT$0 = new QName("urn:schemas-microsoft-com:vml", "ext");
    private static final QName POSITION$2 = new QName("", "position");
    private static final QName SELECTION$4 = new QName("", "selection");
    private static final QName GROUPING$6 = new QName("", "grouping");
    private static final QName UNGROUPING$8 = new QName("", "ungrouping");
    private static final QName ROTATION$10 = new QName("", CellUtil.ROTATION);
    private static final QName CROPPING$12 = new QName("", "cropping");
    private static final QName VERTICIES$14 = new QName("", "verticies");
    private static final QName ADJUSTHANDLES$16 = new QName("", "adjusthandles");
    private static final QName TEXT$18 = new QName("", TextBundle.TEXT_ENTRY);
    private static final QName ASPECTRATIO$20 = new QName("", "aspectratio");
    private static final QName SHAPETYPE$22 = new QName("", "shapetype");

    public CTLockImpl(SchemaType schemaType) {
        super(schemaType);
    }

    @Override // com.microsoft.schemas.office.office.CTLock
    public STExt.Enum getExt() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(EXT$0);
            if (simpleValue == null) {
                return null;
            }
            return (STExt.Enum) simpleValue.getEnumValue();
        }
    }

    @Override // com.microsoft.schemas.office.office.CTLock
    public STExt xgetExt() {
        STExt sTExt;
        synchronized (monitor()) {
            check_orphaned();
            sTExt = (STExt) get_store().find_attribute_user(EXT$0);
        }
        return sTExt;
    }

    @Override // com.microsoft.schemas.office.office.CTLock
    public boolean isSetExt() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(EXT$0) != null;
        }
        return z;
    }

    @Override // com.microsoft.schemas.office.office.CTLock
    public void setExt(STExt.Enum r4) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(EXT$0);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(EXT$0);
            }
            simpleValue.setEnumValue(r4);
        }
    }

    @Override // com.microsoft.schemas.office.office.CTLock
    public void xsetExt(STExt sTExt) {
        synchronized (monitor()) {
            check_orphaned();
            STExt sTExt2 = (STExt) get_store().find_attribute_user(EXT$0);
            if (sTExt2 == null) {
                sTExt2 = (STExt) get_store().add_attribute_user(EXT$0);
            }
            sTExt2.set(sTExt);
        }
    }

    @Override // com.microsoft.schemas.office.office.CTLock
    public void unsetExt() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(EXT$0);
        }
    }

    @Override // com.microsoft.schemas.office.office.CTLock
    public STTrueFalse$Enum getPosition() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(POSITION$2);
            if (simpleValue == null) {
                return null;
            }
            return (STTrueFalse$Enum) simpleValue.getEnumValue();
        }
    }

    @Override // com.microsoft.schemas.office.office.CTLock
    public STTrueFalse xgetPosition() {
        STTrueFalse sTTrueFalseFind_attribute_user;
        synchronized (monitor()) {
            check_orphaned();
            sTTrueFalseFind_attribute_user = get_store().find_attribute_user(POSITION$2);
        }
        return sTTrueFalseFind_attribute_user;
    }

    @Override // com.microsoft.schemas.office.office.CTLock
    public boolean isSetPosition() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(POSITION$2) != null;
        }
        return z;
    }

    @Override // com.microsoft.schemas.office.office.CTLock
    public void setPosition(STTrueFalse$Enum sTTrueFalse$Enum) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(POSITION$2);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(POSITION$2);
            }
            simpleValue.setEnumValue(sTTrueFalse$Enum);
        }
    }

    @Override // com.microsoft.schemas.office.office.CTLock
    public void xsetPosition(STTrueFalse sTTrueFalse) {
        synchronized (monitor()) {
            check_orphaned();
            STTrueFalse sTTrueFalseFind_attribute_user = get_store().find_attribute_user(POSITION$2);
            if (sTTrueFalseFind_attribute_user == null) {
                sTTrueFalseFind_attribute_user = (STTrueFalse) get_store().add_attribute_user(POSITION$2);
            }
            sTTrueFalseFind_attribute_user.set(sTTrueFalse);
        }
    }

    @Override // com.microsoft.schemas.office.office.CTLock
    public void unsetPosition() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(POSITION$2);
        }
    }

    @Override // com.microsoft.schemas.office.office.CTLock
    public STTrueFalse$Enum getSelection() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(SELECTION$4);
            if (simpleValue == null) {
                return null;
            }
            return (STTrueFalse$Enum) simpleValue.getEnumValue();
        }
    }

    @Override // com.microsoft.schemas.office.office.CTLock
    public STTrueFalse xgetSelection() {
        STTrueFalse sTTrueFalseFind_attribute_user;
        synchronized (monitor()) {
            check_orphaned();
            sTTrueFalseFind_attribute_user = get_store().find_attribute_user(SELECTION$4);
        }
        return sTTrueFalseFind_attribute_user;
    }

    @Override // com.microsoft.schemas.office.office.CTLock
    public boolean isSetSelection() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(SELECTION$4) != null;
        }
        return z;
    }

    @Override // com.microsoft.schemas.office.office.CTLock
    public void setSelection(STTrueFalse$Enum sTTrueFalse$Enum) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(SELECTION$4);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(SELECTION$4);
            }
            simpleValue.setEnumValue(sTTrueFalse$Enum);
        }
    }

    @Override // com.microsoft.schemas.office.office.CTLock
    public void xsetSelection(STTrueFalse sTTrueFalse) {
        synchronized (monitor()) {
            check_orphaned();
            STTrueFalse sTTrueFalseFind_attribute_user = get_store().find_attribute_user(SELECTION$4);
            if (sTTrueFalseFind_attribute_user == null) {
                sTTrueFalseFind_attribute_user = (STTrueFalse) get_store().add_attribute_user(SELECTION$4);
            }
            sTTrueFalseFind_attribute_user.set(sTTrueFalse);
        }
    }

    @Override // com.microsoft.schemas.office.office.CTLock
    public void unsetSelection() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(SELECTION$4);
        }
    }

    @Override // com.microsoft.schemas.office.office.CTLock
    public STTrueFalse$Enum getGrouping() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(GROUPING$6);
            if (simpleValue == null) {
                return null;
            }
            return (STTrueFalse$Enum) simpleValue.getEnumValue();
        }
    }

    @Override // com.microsoft.schemas.office.office.CTLock
    public STTrueFalse xgetGrouping() {
        STTrueFalse sTTrueFalseFind_attribute_user;
        synchronized (monitor()) {
            check_orphaned();
            sTTrueFalseFind_attribute_user = get_store().find_attribute_user(GROUPING$6);
        }
        return sTTrueFalseFind_attribute_user;
    }

    @Override // com.microsoft.schemas.office.office.CTLock
    public boolean isSetGrouping() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(GROUPING$6) != null;
        }
        return z;
    }

    @Override // com.microsoft.schemas.office.office.CTLock
    public void setGrouping(STTrueFalse$Enum sTTrueFalse$Enum) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(GROUPING$6);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(GROUPING$6);
            }
            simpleValue.setEnumValue(sTTrueFalse$Enum);
        }
    }

    @Override // com.microsoft.schemas.office.office.CTLock
    public void xsetGrouping(STTrueFalse sTTrueFalse) {
        synchronized (monitor()) {
            check_orphaned();
            STTrueFalse sTTrueFalseFind_attribute_user = get_store().find_attribute_user(GROUPING$6);
            if (sTTrueFalseFind_attribute_user == null) {
                sTTrueFalseFind_attribute_user = (STTrueFalse) get_store().add_attribute_user(GROUPING$6);
            }
            sTTrueFalseFind_attribute_user.set(sTTrueFalse);
        }
    }

    @Override // com.microsoft.schemas.office.office.CTLock
    public void unsetGrouping() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(GROUPING$6);
        }
    }

    @Override // com.microsoft.schemas.office.office.CTLock
    public STTrueFalse$Enum getUngrouping() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(UNGROUPING$8);
            if (simpleValue == null) {
                return null;
            }
            return (STTrueFalse$Enum) simpleValue.getEnumValue();
        }
    }

    @Override // com.microsoft.schemas.office.office.CTLock
    public STTrueFalse xgetUngrouping() {
        STTrueFalse sTTrueFalseFind_attribute_user;
        synchronized (monitor()) {
            check_orphaned();
            sTTrueFalseFind_attribute_user = get_store().find_attribute_user(UNGROUPING$8);
        }
        return sTTrueFalseFind_attribute_user;
    }

    @Override // com.microsoft.schemas.office.office.CTLock
    public boolean isSetUngrouping() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(UNGROUPING$8) != null;
        }
        return z;
    }

    @Override // com.microsoft.schemas.office.office.CTLock
    public void setUngrouping(STTrueFalse$Enum sTTrueFalse$Enum) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(UNGROUPING$8);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(UNGROUPING$8);
            }
            simpleValue.setEnumValue(sTTrueFalse$Enum);
        }
    }

    @Override // com.microsoft.schemas.office.office.CTLock
    public void xsetUngrouping(STTrueFalse sTTrueFalse) {
        synchronized (monitor()) {
            check_orphaned();
            STTrueFalse sTTrueFalseFind_attribute_user = get_store().find_attribute_user(UNGROUPING$8);
            if (sTTrueFalseFind_attribute_user == null) {
                sTTrueFalseFind_attribute_user = (STTrueFalse) get_store().add_attribute_user(UNGROUPING$8);
            }
            sTTrueFalseFind_attribute_user.set(sTTrueFalse);
        }
    }

    @Override // com.microsoft.schemas.office.office.CTLock
    public void unsetUngrouping() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(UNGROUPING$8);
        }
    }

    @Override // com.microsoft.schemas.office.office.CTLock
    public STTrueFalse$Enum getRotation() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(ROTATION$10);
            if (simpleValue == null) {
                return null;
            }
            return (STTrueFalse$Enum) simpleValue.getEnumValue();
        }
    }

    @Override // com.microsoft.schemas.office.office.CTLock
    public STTrueFalse xgetRotation() {
        STTrueFalse sTTrueFalseFind_attribute_user;
        synchronized (monitor()) {
            check_orphaned();
            sTTrueFalseFind_attribute_user = get_store().find_attribute_user(ROTATION$10);
        }
        return sTTrueFalseFind_attribute_user;
    }

    @Override // com.microsoft.schemas.office.office.CTLock
    public boolean isSetRotation() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(ROTATION$10) != null;
        }
        return z;
    }

    @Override // com.microsoft.schemas.office.office.CTLock
    public void setRotation(STTrueFalse$Enum sTTrueFalse$Enum) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(ROTATION$10);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(ROTATION$10);
            }
            simpleValue.setEnumValue(sTTrueFalse$Enum);
        }
    }

    @Override // com.microsoft.schemas.office.office.CTLock
    public void xsetRotation(STTrueFalse sTTrueFalse) {
        synchronized (monitor()) {
            check_orphaned();
            STTrueFalse sTTrueFalseFind_attribute_user = get_store().find_attribute_user(ROTATION$10);
            if (sTTrueFalseFind_attribute_user == null) {
                sTTrueFalseFind_attribute_user = (STTrueFalse) get_store().add_attribute_user(ROTATION$10);
            }
            sTTrueFalseFind_attribute_user.set(sTTrueFalse);
        }
    }

    @Override // com.microsoft.schemas.office.office.CTLock
    public void unsetRotation() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(ROTATION$10);
        }
    }

    @Override // com.microsoft.schemas.office.office.CTLock
    public STTrueFalse$Enum getCropping() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(CROPPING$12);
            if (simpleValue == null) {
                return null;
            }
            return (STTrueFalse$Enum) simpleValue.getEnumValue();
        }
    }

    @Override // com.microsoft.schemas.office.office.CTLock
    public STTrueFalse xgetCropping() {
        STTrueFalse sTTrueFalseFind_attribute_user;
        synchronized (monitor()) {
            check_orphaned();
            sTTrueFalseFind_attribute_user = get_store().find_attribute_user(CROPPING$12);
        }
        return sTTrueFalseFind_attribute_user;
    }

    @Override // com.microsoft.schemas.office.office.CTLock
    public boolean isSetCropping() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(CROPPING$12) != null;
        }
        return z;
    }

    @Override // com.microsoft.schemas.office.office.CTLock
    public void setCropping(STTrueFalse$Enum sTTrueFalse$Enum) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(CROPPING$12);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(CROPPING$12);
            }
            simpleValue.setEnumValue(sTTrueFalse$Enum);
        }
    }

    @Override // com.microsoft.schemas.office.office.CTLock
    public void xsetCropping(STTrueFalse sTTrueFalse) {
        synchronized (monitor()) {
            check_orphaned();
            STTrueFalse sTTrueFalseFind_attribute_user = get_store().find_attribute_user(CROPPING$12);
            if (sTTrueFalseFind_attribute_user == null) {
                sTTrueFalseFind_attribute_user = (STTrueFalse) get_store().add_attribute_user(CROPPING$12);
            }
            sTTrueFalseFind_attribute_user.set(sTTrueFalse);
        }
    }

    @Override // com.microsoft.schemas.office.office.CTLock
    public void unsetCropping() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(CROPPING$12);
        }
    }

    @Override // com.microsoft.schemas.office.office.CTLock
    public STTrueFalse$Enum getVerticies() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(VERTICIES$14);
            if (simpleValue == null) {
                return null;
            }
            return (STTrueFalse$Enum) simpleValue.getEnumValue();
        }
    }

    @Override // com.microsoft.schemas.office.office.CTLock
    public STTrueFalse xgetVerticies() {
        STTrueFalse sTTrueFalseFind_attribute_user;
        synchronized (monitor()) {
            check_orphaned();
            sTTrueFalseFind_attribute_user = get_store().find_attribute_user(VERTICIES$14);
        }
        return sTTrueFalseFind_attribute_user;
    }

    @Override // com.microsoft.schemas.office.office.CTLock
    public boolean isSetVerticies() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(VERTICIES$14) != null;
        }
        return z;
    }

    @Override // com.microsoft.schemas.office.office.CTLock
    public void setVerticies(STTrueFalse$Enum sTTrueFalse$Enum) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(VERTICIES$14);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(VERTICIES$14);
            }
            simpleValue.setEnumValue(sTTrueFalse$Enum);
        }
    }

    @Override // com.microsoft.schemas.office.office.CTLock
    public void xsetVerticies(STTrueFalse sTTrueFalse) {
        synchronized (monitor()) {
            check_orphaned();
            STTrueFalse sTTrueFalseFind_attribute_user = get_store().find_attribute_user(VERTICIES$14);
            if (sTTrueFalseFind_attribute_user == null) {
                sTTrueFalseFind_attribute_user = (STTrueFalse) get_store().add_attribute_user(VERTICIES$14);
            }
            sTTrueFalseFind_attribute_user.set(sTTrueFalse);
        }
    }

    @Override // com.microsoft.schemas.office.office.CTLock
    public void unsetVerticies() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(VERTICIES$14);
        }
    }

    @Override // com.microsoft.schemas.office.office.CTLock
    public STTrueFalse$Enum getAdjusthandles() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(ADJUSTHANDLES$16);
            if (simpleValue == null) {
                return null;
            }
            return (STTrueFalse$Enum) simpleValue.getEnumValue();
        }
    }

    @Override // com.microsoft.schemas.office.office.CTLock
    public STTrueFalse xgetAdjusthandles() {
        STTrueFalse sTTrueFalseFind_attribute_user;
        synchronized (monitor()) {
            check_orphaned();
            sTTrueFalseFind_attribute_user = get_store().find_attribute_user(ADJUSTHANDLES$16);
        }
        return sTTrueFalseFind_attribute_user;
    }

    @Override // com.microsoft.schemas.office.office.CTLock
    public boolean isSetAdjusthandles() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(ADJUSTHANDLES$16) != null;
        }
        return z;
    }

    @Override // com.microsoft.schemas.office.office.CTLock
    public void setAdjusthandles(STTrueFalse$Enum sTTrueFalse$Enum) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(ADJUSTHANDLES$16);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(ADJUSTHANDLES$16);
            }
            simpleValue.setEnumValue(sTTrueFalse$Enum);
        }
    }

    @Override // com.microsoft.schemas.office.office.CTLock
    public void xsetAdjusthandles(STTrueFalse sTTrueFalse) {
        synchronized (monitor()) {
            check_orphaned();
            STTrueFalse sTTrueFalseFind_attribute_user = get_store().find_attribute_user(ADJUSTHANDLES$16);
            if (sTTrueFalseFind_attribute_user == null) {
                sTTrueFalseFind_attribute_user = (STTrueFalse) get_store().add_attribute_user(ADJUSTHANDLES$16);
            }
            sTTrueFalseFind_attribute_user.set(sTTrueFalse);
        }
    }

    @Override // com.microsoft.schemas.office.office.CTLock
    public void unsetAdjusthandles() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(ADJUSTHANDLES$16);
        }
    }

    @Override // com.microsoft.schemas.office.office.CTLock
    public STTrueFalse$Enum getText() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(TEXT$18);
            if (simpleValue == null) {
                return null;
            }
            return (STTrueFalse$Enum) simpleValue.getEnumValue();
        }
    }

    @Override // com.microsoft.schemas.office.office.CTLock
    public STTrueFalse xgetText() {
        STTrueFalse sTTrueFalseFind_attribute_user;
        synchronized (monitor()) {
            check_orphaned();
            sTTrueFalseFind_attribute_user = get_store().find_attribute_user(TEXT$18);
        }
        return sTTrueFalseFind_attribute_user;
    }

    @Override // com.microsoft.schemas.office.office.CTLock
    public boolean isSetText() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(TEXT$18) != null;
        }
        return z;
    }

    @Override // com.microsoft.schemas.office.office.CTLock
    public void setText(STTrueFalse$Enum sTTrueFalse$Enum) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(TEXT$18);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(TEXT$18);
            }
            simpleValue.setEnumValue(sTTrueFalse$Enum);
        }
    }

    @Override // com.microsoft.schemas.office.office.CTLock
    public void xsetText(STTrueFalse sTTrueFalse) {
        synchronized (monitor()) {
            check_orphaned();
            STTrueFalse sTTrueFalseFind_attribute_user = get_store().find_attribute_user(TEXT$18);
            if (sTTrueFalseFind_attribute_user == null) {
                sTTrueFalseFind_attribute_user = (STTrueFalse) get_store().add_attribute_user(TEXT$18);
            }
            sTTrueFalseFind_attribute_user.set(sTTrueFalse);
        }
    }

    @Override // com.microsoft.schemas.office.office.CTLock
    public void unsetText() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(TEXT$18);
        }
    }

    @Override // com.microsoft.schemas.office.office.CTLock
    public STTrueFalse$Enum getAspectratio() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(ASPECTRATIO$20);
            if (simpleValue == null) {
                return null;
            }
            return (STTrueFalse$Enum) simpleValue.getEnumValue();
        }
    }

    @Override // com.microsoft.schemas.office.office.CTLock
    public STTrueFalse xgetAspectratio() {
        STTrueFalse sTTrueFalseFind_attribute_user;
        synchronized (monitor()) {
            check_orphaned();
            sTTrueFalseFind_attribute_user = get_store().find_attribute_user(ASPECTRATIO$20);
        }
        return sTTrueFalseFind_attribute_user;
    }

    @Override // com.microsoft.schemas.office.office.CTLock
    public boolean isSetAspectratio() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(ASPECTRATIO$20) != null;
        }
        return z;
    }

    @Override // com.microsoft.schemas.office.office.CTLock
    public void setAspectratio(STTrueFalse$Enum sTTrueFalse$Enum) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(ASPECTRATIO$20);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(ASPECTRATIO$20);
            }
            simpleValue.setEnumValue(sTTrueFalse$Enum);
        }
    }

    @Override // com.microsoft.schemas.office.office.CTLock
    public void xsetAspectratio(STTrueFalse sTTrueFalse) {
        synchronized (monitor()) {
            check_orphaned();
            STTrueFalse sTTrueFalseFind_attribute_user = get_store().find_attribute_user(ASPECTRATIO$20);
            if (sTTrueFalseFind_attribute_user == null) {
                sTTrueFalseFind_attribute_user = (STTrueFalse) get_store().add_attribute_user(ASPECTRATIO$20);
            }
            sTTrueFalseFind_attribute_user.set(sTTrueFalse);
        }
    }

    @Override // com.microsoft.schemas.office.office.CTLock
    public void unsetAspectratio() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(ASPECTRATIO$20);
        }
    }

    @Override // com.microsoft.schemas.office.office.CTLock
    public STTrueFalse$Enum getShapetype() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(SHAPETYPE$22);
            if (simpleValue == null) {
                return null;
            }
            return (STTrueFalse$Enum) simpleValue.getEnumValue();
        }
    }

    @Override // com.microsoft.schemas.office.office.CTLock
    public STTrueFalse xgetShapetype() {
        STTrueFalse sTTrueFalseFind_attribute_user;
        synchronized (monitor()) {
            check_orphaned();
            sTTrueFalseFind_attribute_user = get_store().find_attribute_user(SHAPETYPE$22);
        }
        return sTTrueFalseFind_attribute_user;
    }

    @Override // com.microsoft.schemas.office.office.CTLock
    public boolean isSetShapetype() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(SHAPETYPE$22) != null;
        }
        return z;
    }

    @Override // com.microsoft.schemas.office.office.CTLock
    public void setShapetype(STTrueFalse$Enum sTTrueFalse$Enum) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(SHAPETYPE$22);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(SHAPETYPE$22);
            }
            simpleValue.setEnumValue(sTTrueFalse$Enum);
        }
    }

    @Override // com.microsoft.schemas.office.office.CTLock
    public void xsetShapetype(STTrueFalse sTTrueFalse) {
        synchronized (monitor()) {
            check_orphaned();
            STTrueFalse sTTrueFalseFind_attribute_user = get_store().find_attribute_user(SHAPETYPE$22);
            if (sTTrueFalseFind_attribute_user == null) {
                sTTrueFalseFind_attribute_user = (STTrueFalse) get_store().add_attribute_user(SHAPETYPE$22);
            }
            sTTrueFalseFind_attribute_user.set(sTTrueFalse);
        }
    }

    @Override // com.microsoft.schemas.office.office.CTLock
    public void unsetShapetype() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(SHAPETYPE$22);
        }
    }
}
