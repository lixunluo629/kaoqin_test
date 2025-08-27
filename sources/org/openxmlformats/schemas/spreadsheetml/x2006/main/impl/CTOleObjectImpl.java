package org.openxmlformats.schemas.spreadsheetml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.naming.EjbRef;
import org.apache.poi.openxml4j.opc.PackageRelationshipTypes;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.SimpleValue;
import org.apache.xmlbeans.XmlBoolean;
import org.apache.xmlbeans.XmlString;
import org.apache.xmlbeans.XmlUnsignedInt;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;
import org.openxmlformats.schemas.officeDocument.x2006.relationships.STRelationshipId;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTOleObject;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.STDvAspect;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.STDvAspect$Enum;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.STOleUpdate;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.STOleUpdate$Enum;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.STXstring;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/impl/CTOleObjectImpl.class */
public class CTOleObjectImpl extends XmlComplexContentImpl implements CTOleObject {
    private static final QName PROGID$0 = new QName("", "progId");
    private static final QName DVASPECT$2 = new QName("", "dvAspect");
    private static final QName LINK$4 = new QName("", EjbRef.LINK);
    private static final QName OLEUPDATE$6 = new QName("", "oleUpdate");
    private static final QName AUTOLOAD$8 = new QName("", "autoLoad");
    private static final QName SHAPEID$10 = new QName("", "shapeId");
    private static final QName ID$12 = new QName(PackageRelationshipTypes.CORE_PROPERTIES_ECMA376_NS, "id");

    public CTOleObjectImpl(SchemaType schemaType) {
        super(schemaType);
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTOleObject
    public String getProgId() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(PROGID$0);
            if (simpleValue == null) {
                return null;
            }
            return simpleValue.getStringValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTOleObject
    public XmlString xgetProgId() {
        XmlString xmlString;
        synchronized (monitor()) {
            check_orphaned();
            xmlString = (XmlString) get_store().find_attribute_user(PROGID$0);
        }
        return xmlString;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTOleObject
    public boolean isSetProgId() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(PROGID$0) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTOleObject
    public void setProgId(String str) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(PROGID$0);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(PROGID$0);
            }
            simpleValue.setStringValue(str);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTOleObject
    public void xsetProgId(XmlString xmlString) {
        synchronized (monitor()) {
            check_orphaned();
            XmlString xmlString2 = (XmlString) get_store().find_attribute_user(PROGID$0);
            if (xmlString2 == null) {
                xmlString2 = (XmlString) get_store().add_attribute_user(PROGID$0);
            }
            xmlString2.set(xmlString);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTOleObject
    public void unsetProgId() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROGID$0);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTOleObject
    public STDvAspect$Enum getDvAspect() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(DVASPECT$2);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(DVASPECT$2);
            }
            if (simpleValue == null) {
                return null;
            }
            return (STDvAspect$Enum) simpleValue.getEnumValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTOleObject
    public STDvAspect xgetDvAspect() {
        STDvAspect sTDvAspect;
        synchronized (monitor()) {
            check_orphaned();
            STDvAspect sTDvAspectFind_attribute_user = get_store().find_attribute_user(DVASPECT$2);
            if (sTDvAspectFind_attribute_user == null) {
                sTDvAspectFind_attribute_user = (STDvAspect) get_default_attribute_value(DVASPECT$2);
            }
            sTDvAspect = sTDvAspectFind_attribute_user;
        }
        return sTDvAspect;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTOleObject
    public boolean isSetDvAspect() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(DVASPECT$2) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTOleObject
    public void setDvAspect(STDvAspect$Enum sTDvAspect$Enum) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(DVASPECT$2);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(DVASPECT$2);
            }
            simpleValue.setEnumValue(sTDvAspect$Enum);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTOleObject
    public void xsetDvAspect(STDvAspect sTDvAspect) {
        synchronized (monitor()) {
            check_orphaned();
            STDvAspect sTDvAspectFind_attribute_user = get_store().find_attribute_user(DVASPECT$2);
            if (sTDvAspectFind_attribute_user == null) {
                sTDvAspectFind_attribute_user = (STDvAspect) get_store().add_attribute_user(DVASPECT$2);
            }
            sTDvAspectFind_attribute_user.set(sTDvAspect);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTOleObject
    public void unsetDvAspect() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(DVASPECT$2);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTOleObject
    public String getLink() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(LINK$4);
            if (simpleValue == null) {
                return null;
            }
            return simpleValue.getStringValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTOleObject
    public STXstring xgetLink() {
        STXstring sTXstring;
        synchronized (monitor()) {
            check_orphaned();
            sTXstring = (STXstring) get_store().find_attribute_user(LINK$4);
        }
        return sTXstring;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTOleObject
    public boolean isSetLink() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(LINK$4) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTOleObject
    public void setLink(String str) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(LINK$4);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(LINK$4);
            }
            simpleValue.setStringValue(str);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTOleObject
    public void xsetLink(STXstring sTXstring) {
        synchronized (monitor()) {
            check_orphaned();
            STXstring sTXstring2 = (STXstring) get_store().find_attribute_user(LINK$4);
            if (sTXstring2 == null) {
                sTXstring2 = (STXstring) get_store().add_attribute_user(LINK$4);
            }
            sTXstring2.set(sTXstring);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTOleObject
    public void unsetLink() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(LINK$4);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTOleObject
    public STOleUpdate$Enum getOleUpdate() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(OLEUPDATE$6);
            if (simpleValue == null) {
                return null;
            }
            return (STOleUpdate$Enum) simpleValue.getEnumValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTOleObject
    public STOleUpdate xgetOleUpdate() {
        STOleUpdate sTOleUpdateFind_attribute_user;
        synchronized (monitor()) {
            check_orphaned();
            sTOleUpdateFind_attribute_user = get_store().find_attribute_user(OLEUPDATE$6);
        }
        return sTOleUpdateFind_attribute_user;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTOleObject
    public boolean isSetOleUpdate() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(OLEUPDATE$6) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTOleObject
    public void setOleUpdate(STOleUpdate$Enum sTOleUpdate$Enum) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(OLEUPDATE$6);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(OLEUPDATE$6);
            }
            simpleValue.setEnumValue(sTOleUpdate$Enum);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTOleObject
    public void xsetOleUpdate(STOleUpdate sTOleUpdate) {
        synchronized (monitor()) {
            check_orphaned();
            STOleUpdate sTOleUpdateFind_attribute_user = get_store().find_attribute_user(OLEUPDATE$6);
            if (sTOleUpdateFind_attribute_user == null) {
                sTOleUpdateFind_attribute_user = (STOleUpdate) get_store().add_attribute_user(OLEUPDATE$6);
            }
            sTOleUpdateFind_attribute_user.set(sTOleUpdate);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTOleObject
    public void unsetOleUpdate() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(OLEUPDATE$6);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTOleObject
    public boolean getAutoLoad() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(AUTOLOAD$8);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(AUTOLOAD$8);
            }
            if (simpleValue == null) {
                return false;
            }
            return simpleValue.getBooleanValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTOleObject
    public XmlBoolean xgetAutoLoad() {
        XmlBoolean xmlBoolean;
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(AUTOLOAD$8);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_default_attribute_value(AUTOLOAD$8);
            }
            xmlBoolean = xmlBoolean2;
        }
        return xmlBoolean;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTOleObject
    public boolean isSetAutoLoad() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(AUTOLOAD$8) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTOleObject
    public void setAutoLoad(boolean z) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(AUTOLOAD$8);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(AUTOLOAD$8);
            }
            simpleValue.setBooleanValue(z);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTOleObject
    public void xsetAutoLoad(XmlBoolean xmlBoolean) {
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(AUTOLOAD$8);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_store().add_attribute_user(AUTOLOAD$8);
            }
            xmlBoolean2.set(xmlBoolean);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTOleObject
    public void unsetAutoLoad() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(AUTOLOAD$8);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTOleObject
    public long getShapeId() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(SHAPEID$10);
            if (simpleValue == null) {
                return 0L;
            }
            return simpleValue.getLongValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTOleObject
    public XmlUnsignedInt xgetShapeId() {
        XmlUnsignedInt xmlUnsignedInt;
        synchronized (monitor()) {
            check_orphaned();
            xmlUnsignedInt = (XmlUnsignedInt) get_store().find_attribute_user(SHAPEID$10);
        }
        return xmlUnsignedInt;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTOleObject
    public void setShapeId(long j) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(SHAPEID$10);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(SHAPEID$10);
            }
            simpleValue.setLongValue(j);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTOleObject
    public void xsetShapeId(XmlUnsignedInt xmlUnsignedInt) {
        synchronized (monitor()) {
            check_orphaned();
            XmlUnsignedInt xmlUnsignedInt2 = (XmlUnsignedInt) get_store().find_attribute_user(SHAPEID$10);
            if (xmlUnsignedInt2 == null) {
                xmlUnsignedInt2 = (XmlUnsignedInt) get_store().add_attribute_user(SHAPEID$10);
            }
            xmlUnsignedInt2.set(xmlUnsignedInt);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTOleObject
    public String getId() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(ID$12);
            if (simpleValue == null) {
                return null;
            }
            return simpleValue.getStringValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTOleObject
    public STRelationshipId xgetId() {
        STRelationshipId sTRelationshipId;
        synchronized (monitor()) {
            check_orphaned();
            sTRelationshipId = (STRelationshipId) get_store().find_attribute_user(ID$12);
        }
        return sTRelationshipId;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTOleObject
    public boolean isSetId() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(ID$12) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTOleObject
    public void setId(String str) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(ID$12);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(ID$12);
            }
            simpleValue.setStringValue(str);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTOleObject
    public void xsetId(STRelationshipId sTRelationshipId) {
        synchronized (monitor()) {
            check_orphaned();
            STRelationshipId sTRelationshipId2 = (STRelationshipId) get_store().find_attribute_user(ID$12);
            if (sTRelationshipId2 == null) {
                sTRelationshipId2 = (STRelationshipId) get_store().add_attribute_user(ID$12);
            }
            sTRelationshipId2.set(sTRelationshipId);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTOleObject
    public void unsetId() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(ID$12);
        }
    }
}
