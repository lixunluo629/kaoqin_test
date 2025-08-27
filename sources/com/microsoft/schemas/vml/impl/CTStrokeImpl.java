package com.microsoft.schemas.vml.impl;

import com.microsoft.schemas.office.office.CTStrokeChild;
import com.microsoft.schemas.office.office.STTrueFalse$Enum;
import com.microsoft.schemas.vml.CTStroke;
import com.microsoft.schemas.vml.STColorType;
import com.microsoft.schemas.vml.STFillType;
import com.microsoft.schemas.vml.STFillType$Enum;
import com.microsoft.schemas.vml.STImageAspect;
import com.microsoft.schemas.vml.STImageAspect$Enum;
import com.microsoft.schemas.vml.STStrokeArrowLength;
import com.microsoft.schemas.vml.STStrokeArrowLength$Enum;
import com.microsoft.schemas.vml.STStrokeArrowType;
import com.microsoft.schemas.vml.STStrokeArrowType$Enum;
import com.microsoft.schemas.vml.STStrokeArrowWidth;
import com.microsoft.schemas.vml.STStrokeArrowWidth$Enum;
import com.microsoft.schemas.vml.STStrokeEndCap;
import com.microsoft.schemas.vml.STStrokeEndCap$Enum;
import com.microsoft.schemas.vml.STStrokeJoinStyle;
import com.microsoft.schemas.vml.STStrokeLineStyle;
import com.microsoft.schemas.vml.STStrokeLineStyle$Enum;
import com.microsoft.schemas.vml.STTrueFalse;
import java.math.BigDecimal;
import javax.xml.namespace.QName;
import org.apache.poi.openxml4j.opc.PackageRelationshipTypes;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.SimpleValue;
import org.apache.xmlbeans.XmlDecimal;
import org.apache.xmlbeans.XmlString;
import org.apache.xmlbeans.impl.jam.xml.JamXmlElements;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;
import org.openxmlformats.schemas.officeDocument.x2006.relationships.STRelationshipId;
import org.springframework.beans.propertyeditors.CustomBooleanEditor;

/* loaded from: poi-ooxml-schemas-3.17.jar:com/microsoft/schemas/vml/impl/CTStrokeImpl.class */
public class CTStrokeImpl extends XmlComplexContentImpl implements CTStroke {
    private static final QName LEFT$0 = new QName("urn:schemas-microsoft-com:office:office", "left");
    private static final QName TOP$2 = new QName("urn:schemas-microsoft-com:office:office", "top");
    private static final QName RIGHT$4 = new QName("urn:schemas-microsoft-com:office:office", "right");
    private static final QName BOTTOM$6 = new QName("urn:schemas-microsoft-com:office:office", "bottom");
    private static final QName COLUMN$8 = new QName("urn:schemas-microsoft-com:office:office", JamXmlElements.COLUMN);
    private static final QName ID$10 = new QName("", "id");
    private static final QName ON$12 = new QName("", CustomBooleanEditor.VALUE_ON);
    private static final QName WEIGHT$14 = new QName("", "weight");
    private static final QName COLOR$16 = new QName("", "color");
    private static final QName OPACITY$18 = new QName("", "opacity");
    private static final QName LINESTYLE$20 = new QName("", "linestyle");
    private static final QName MITERLIMIT$22 = new QName("", "miterlimit");
    private static final QName JOINSTYLE$24 = new QName("", "joinstyle");
    private static final QName ENDCAP$26 = new QName("", "endcap");
    private static final QName DASHSTYLE$28 = new QName("", "dashstyle");
    private static final QName FILLTYPE$30 = new QName("", "filltype");
    private static final QName SRC$32 = new QName("", "src");
    private static final QName IMAGEASPECT$34 = new QName("", "imageaspect");
    private static final QName IMAGESIZE$36 = new QName("", "imagesize");
    private static final QName IMAGEALIGNSHAPE$38 = new QName("", "imagealignshape");
    private static final QName COLOR2$40 = new QName("", "color2");
    private static final QName STARTARROW$42 = new QName("", "startarrow");
    private static final QName STARTARROWWIDTH$44 = new QName("", "startarrowwidth");
    private static final QName STARTARROWLENGTH$46 = new QName("", "startarrowlength");
    private static final QName ENDARROW$48 = new QName("", "endarrow");
    private static final QName ENDARROWWIDTH$50 = new QName("", "endarrowwidth");
    private static final QName ENDARROWLENGTH$52 = new QName("", "endarrowlength");
    private static final QName HREF$54 = new QName("urn:schemas-microsoft-com:office:office", "href");
    private static final QName ALTHREF$56 = new QName("urn:schemas-microsoft-com:office:office", "althref");
    private static final QName TITLE$58 = new QName("urn:schemas-microsoft-com:office:office", "title");
    private static final QName FORCEDASH$60 = new QName("urn:schemas-microsoft-com:office:office", "forcedash");
    private static final QName ID2$62 = new QName(PackageRelationshipTypes.CORE_PROPERTIES_ECMA376_NS, "id");
    private static final QName INSETPEN$64 = new QName("", "insetpen");
    private static final QName RELID$66 = new QName("urn:schemas-microsoft-com:office:office", "relid");

    public CTStrokeImpl(SchemaType schemaType) {
        super(schemaType);
    }

    @Override // com.microsoft.schemas.vml.CTStroke
    public CTStrokeChild getLeft() {
        synchronized (monitor()) {
            check_orphaned();
            CTStrokeChild cTStrokeChildFind_element_user = get_store().find_element_user(LEFT$0, 0);
            if (cTStrokeChildFind_element_user == null) {
                return null;
            }
            return cTStrokeChildFind_element_user;
        }
    }

    @Override // com.microsoft.schemas.vml.CTStroke
    public boolean isSetLeft() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().count_elements(LEFT$0) != 0;
        }
        return z;
    }

    @Override // com.microsoft.schemas.vml.CTStroke
    public void setLeft(CTStrokeChild cTStrokeChild) {
        synchronized (monitor()) {
            check_orphaned();
            CTStrokeChild cTStrokeChildFind_element_user = get_store().find_element_user(LEFT$0, 0);
            if (cTStrokeChildFind_element_user == null) {
                cTStrokeChildFind_element_user = (CTStrokeChild) get_store().add_element_user(LEFT$0);
            }
            cTStrokeChildFind_element_user.set(cTStrokeChild);
        }
    }

    @Override // com.microsoft.schemas.vml.CTStroke
    public CTStrokeChild addNewLeft() {
        CTStrokeChild cTStrokeChildAdd_element_user;
        synchronized (monitor()) {
            check_orphaned();
            cTStrokeChildAdd_element_user = get_store().add_element_user(LEFT$0);
        }
        return cTStrokeChildAdd_element_user;
    }

    @Override // com.microsoft.schemas.vml.CTStroke
    public void unsetLeft() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(LEFT$0, 0);
        }
    }

    @Override // com.microsoft.schemas.vml.CTStroke
    public CTStrokeChild getTop() {
        synchronized (monitor()) {
            check_orphaned();
            CTStrokeChild cTStrokeChildFind_element_user = get_store().find_element_user(TOP$2, 0);
            if (cTStrokeChildFind_element_user == null) {
                return null;
            }
            return cTStrokeChildFind_element_user;
        }
    }

    @Override // com.microsoft.schemas.vml.CTStroke
    public boolean isSetTop() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().count_elements(TOP$2) != 0;
        }
        return z;
    }

    @Override // com.microsoft.schemas.vml.CTStroke
    public void setTop(CTStrokeChild cTStrokeChild) {
        synchronized (monitor()) {
            check_orphaned();
            CTStrokeChild cTStrokeChildFind_element_user = get_store().find_element_user(TOP$2, 0);
            if (cTStrokeChildFind_element_user == null) {
                cTStrokeChildFind_element_user = (CTStrokeChild) get_store().add_element_user(TOP$2);
            }
            cTStrokeChildFind_element_user.set(cTStrokeChild);
        }
    }

    @Override // com.microsoft.schemas.vml.CTStroke
    public CTStrokeChild addNewTop() {
        CTStrokeChild cTStrokeChildAdd_element_user;
        synchronized (monitor()) {
            check_orphaned();
            cTStrokeChildAdd_element_user = get_store().add_element_user(TOP$2);
        }
        return cTStrokeChildAdd_element_user;
    }

    @Override // com.microsoft.schemas.vml.CTStroke
    public void unsetTop() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(TOP$2, 0);
        }
    }

    @Override // com.microsoft.schemas.vml.CTStroke
    public CTStrokeChild getRight() {
        synchronized (monitor()) {
            check_orphaned();
            CTStrokeChild cTStrokeChildFind_element_user = get_store().find_element_user(RIGHT$4, 0);
            if (cTStrokeChildFind_element_user == null) {
                return null;
            }
            return cTStrokeChildFind_element_user;
        }
    }

    @Override // com.microsoft.schemas.vml.CTStroke
    public boolean isSetRight() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().count_elements(RIGHT$4) != 0;
        }
        return z;
    }

    @Override // com.microsoft.schemas.vml.CTStroke
    public void setRight(CTStrokeChild cTStrokeChild) {
        synchronized (monitor()) {
            check_orphaned();
            CTStrokeChild cTStrokeChildFind_element_user = get_store().find_element_user(RIGHT$4, 0);
            if (cTStrokeChildFind_element_user == null) {
                cTStrokeChildFind_element_user = (CTStrokeChild) get_store().add_element_user(RIGHT$4);
            }
            cTStrokeChildFind_element_user.set(cTStrokeChild);
        }
    }

    @Override // com.microsoft.schemas.vml.CTStroke
    public CTStrokeChild addNewRight() {
        CTStrokeChild cTStrokeChildAdd_element_user;
        synchronized (monitor()) {
            check_orphaned();
            cTStrokeChildAdd_element_user = get_store().add_element_user(RIGHT$4);
        }
        return cTStrokeChildAdd_element_user;
    }

    @Override // com.microsoft.schemas.vml.CTStroke
    public void unsetRight() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(RIGHT$4, 0);
        }
    }

    @Override // com.microsoft.schemas.vml.CTStroke
    public CTStrokeChild getBottom() {
        synchronized (monitor()) {
            check_orphaned();
            CTStrokeChild cTStrokeChildFind_element_user = get_store().find_element_user(BOTTOM$6, 0);
            if (cTStrokeChildFind_element_user == null) {
                return null;
            }
            return cTStrokeChildFind_element_user;
        }
    }

    @Override // com.microsoft.schemas.vml.CTStroke
    public boolean isSetBottom() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().count_elements(BOTTOM$6) != 0;
        }
        return z;
    }

    @Override // com.microsoft.schemas.vml.CTStroke
    public void setBottom(CTStrokeChild cTStrokeChild) {
        synchronized (monitor()) {
            check_orphaned();
            CTStrokeChild cTStrokeChildFind_element_user = get_store().find_element_user(BOTTOM$6, 0);
            if (cTStrokeChildFind_element_user == null) {
                cTStrokeChildFind_element_user = (CTStrokeChild) get_store().add_element_user(BOTTOM$6);
            }
            cTStrokeChildFind_element_user.set(cTStrokeChild);
        }
    }

    @Override // com.microsoft.schemas.vml.CTStroke
    public CTStrokeChild addNewBottom() {
        CTStrokeChild cTStrokeChildAdd_element_user;
        synchronized (monitor()) {
            check_orphaned();
            cTStrokeChildAdd_element_user = get_store().add_element_user(BOTTOM$6);
        }
        return cTStrokeChildAdd_element_user;
    }

    @Override // com.microsoft.schemas.vml.CTStroke
    public void unsetBottom() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(BOTTOM$6, 0);
        }
    }

    @Override // com.microsoft.schemas.vml.CTStroke
    public CTStrokeChild getColumn() {
        synchronized (monitor()) {
            check_orphaned();
            CTStrokeChild cTStrokeChildFind_element_user = get_store().find_element_user(COLUMN$8, 0);
            if (cTStrokeChildFind_element_user == null) {
                return null;
            }
            return cTStrokeChildFind_element_user;
        }
    }

    @Override // com.microsoft.schemas.vml.CTStroke
    public boolean isSetColumn() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().count_elements(COLUMN$8) != 0;
        }
        return z;
    }

    @Override // com.microsoft.schemas.vml.CTStroke
    public void setColumn(CTStrokeChild cTStrokeChild) {
        synchronized (monitor()) {
            check_orphaned();
            CTStrokeChild cTStrokeChildFind_element_user = get_store().find_element_user(COLUMN$8, 0);
            if (cTStrokeChildFind_element_user == null) {
                cTStrokeChildFind_element_user = (CTStrokeChild) get_store().add_element_user(COLUMN$8);
            }
            cTStrokeChildFind_element_user.set(cTStrokeChild);
        }
    }

    @Override // com.microsoft.schemas.vml.CTStroke
    public CTStrokeChild addNewColumn() {
        CTStrokeChild cTStrokeChildAdd_element_user;
        synchronized (monitor()) {
            check_orphaned();
            cTStrokeChildAdd_element_user = get_store().add_element_user(COLUMN$8);
        }
        return cTStrokeChildAdd_element_user;
    }

    @Override // com.microsoft.schemas.vml.CTStroke
    public void unsetColumn() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(COLUMN$8, 0);
        }
    }

    @Override // com.microsoft.schemas.vml.CTStroke
    public String getId() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(ID$10);
            if (simpleValue == null) {
                return null;
            }
            return simpleValue.getStringValue();
        }
    }

    @Override // com.microsoft.schemas.vml.CTStroke
    public XmlString xgetId() {
        XmlString xmlString;
        synchronized (monitor()) {
            check_orphaned();
            xmlString = (XmlString) get_store().find_attribute_user(ID$10);
        }
        return xmlString;
    }

    @Override // com.microsoft.schemas.vml.CTStroke
    public boolean isSetId() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(ID$10) != null;
        }
        return z;
    }

    @Override // com.microsoft.schemas.vml.CTStroke
    public void setId(String str) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(ID$10);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(ID$10);
            }
            simpleValue.setStringValue(str);
        }
    }

    @Override // com.microsoft.schemas.vml.CTStroke
    public void xsetId(XmlString xmlString) {
        synchronized (monitor()) {
            check_orphaned();
            XmlString xmlString2 = (XmlString) get_store().find_attribute_user(ID$10);
            if (xmlString2 == null) {
                xmlString2 = (XmlString) get_store().add_attribute_user(ID$10);
            }
            xmlString2.set(xmlString);
        }
    }

    @Override // com.microsoft.schemas.vml.CTStroke
    public void unsetId() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(ID$10);
        }
    }

    @Override // com.microsoft.schemas.vml.CTStroke
    public STTrueFalse.Enum getOn() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(ON$12);
            if (simpleValue == null) {
                return null;
            }
            return (STTrueFalse.Enum) simpleValue.getEnumValue();
        }
    }

    @Override // com.microsoft.schemas.vml.CTStroke
    public STTrueFalse xgetOn() {
        STTrueFalse sTTrueFalse;
        synchronized (monitor()) {
            check_orphaned();
            sTTrueFalse = (STTrueFalse) get_store().find_attribute_user(ON$12);
        }
        return sTTrueFalse;
    }

    @Override // com.microsoft.schemas.vml.CTStroke
    public boolean isSetOn() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(ON$12) != null;
        }
        return z;
    }

    @Override // com.microsoft.schemas.vml.CTStroke
    public void setOn(STTrueFalse.Enum r4) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(ON$12);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(ON$12);
            }
            simpleValue.setEnumValue(r4);
        }
    }

    @Override // com.microsoft.schemas.vml.CTStroke
    public void xsetOn(STTrueFalse sTTrueFalse) {
        synchronized (monitor()) {
            check_orphaned();
            STTrueFalse sTTrueFalse2 = (STTrueFalse) get_store().find_attribute_user(ON$12);
            if (sTTrueFalse2 == null) {
                sTTrueFalse2 = (STTrueFalse) get_store().add_attribute_user(ON$12);
            }
            sTTrueFalse2.set(sTTrueFalse);
        }
    }

    @Override // com.microsoft.schemas.vml.CTStroke
    public void unsetOn() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(ON$12);
        }
    }

    @Override // com.microsoft.schemas.vml.CTStroke
    public String getWeight() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(WEIGHT$14);
            if (simpleValue == null) {
                return null;
            }
            return simpleValue.getStringValue();
        }
    }

    @Override // com.microsoft.schemas.vml.CTStroke
    public XmlString xgetWeight() {
        XmlString xmlString;
        synchronized (monitor()) {
            check_orphaned();
            xmlString = (XmlString) get_store().find_attribute_user(WEIGHT$14);
        }
        return xmlString;
    }

    @Override // com.microsoft.schemas.vml.CTStroke
    public boolean isSetWeight() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(WEIGHT$14) != null;
        }
        return z;
    }

    @Override // com.microsoft.schemas.vml.CTStroke
    public void setWeight(String str) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(WEIGHT$14);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(WEIGHT$14);
            }
            simpleValue.setStringValue(str);
        }
    }

    @Override // com.microsoft.schemas.vml.CTStroke
    public void xsetWeight(XmlString xmlString) {
        synchronized (monitor()) {
            check_orphaned();
            XmlString xmlString2 = (XmlString) get_store().find_attribute_user(WEIGHT$14);
            if (xmlString2 == null) {
                xmlString2 = (XmlString) get_store().add_attribute_user(WEIGHT$14);
            }
            xmlString2.set(xmlString);
        }
    }

    @Override // com.microsoft.schemas.vml.CTStroke
    public void unsetWeight() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(WEIGHT$14);
        }
    }

    @Override // com.microsoft.schemas.vml.CTStroke
    public String getColor() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(COLOR$16);
            if (simpleValue == null) {
                return null;
            }
            return simpleValue.getStringValue();
        }
    }

    @Override // com.microsoft.schemas.vml.CTStroke
    public STColorType xgetColor() {
        STColorType sTColorType;
        synchronized (monitor()) {
            check_orphaned();
            sTColorType = (STColorType) get_store().find_attribute_user(COLOR$16);
        }
        return sTColorType;
    }

    @Override // com.microsoft.schemas.vml.CTStroke
    public boolean isSetColor() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(COLOR$16) != null;
        }
        return z;
    }

    @Override // com.microsoft.schemas.vml.CTStroke
    public void setColor(String str) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(COLOR$16);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(COLOR$16);
            }
            simpleValue.setStringValue(str);
        }
    }

    @Override // com.microsoft.schemas.vml.CTStroke
    public void xsetColor(STColorType sTColorType) {
        synchronized (monitor()) {
            check_orphaned();
            STColorType sTColorType2 = (STColorType) get_store().find_attribute_user(COLOR$16);
            if (sTColorType2 == null) {
                sTColorType2 = (STColorType) get_store().add_attribute_user(COLOR$16);
            }
            sTColorType2.set(sTColorType);
        }
    }

    @Override // com.microsoft.schemas.vml.CTStroke
    public void unsetColor() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(COLOR$16);
        }
    }

    @Override // com.microsoft.schemas.vml.CTStroke
    public String getOpacity() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(OPACITY$18);
            if (simpleValue == null) {
                return null;
            }
            return simpleValue.getStringValue();
        }
    }

    @Override // com.microsoft.schemas.vml.CTStroke
    public XmlString xgetOpacity() {
        XmlString xmlString;
        synchronized (monitor()) {
            check_orphaned();
            xmlString = (XmlString) get_store().find_attribute_user(OPACITY$18);
        }
        return xmlString;
    }

    @Override // com.microsoft.schemas.vml.CTStroke
    public boolean isSetOpacity() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(OPACITY$18) != null;
        }
        return z;
    }

    @Override // com.microsoft.schemas.vml.CTStroke
    public void setOpacity(String str) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(OPACITY$18);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(OPACITY$18);
            }
            simpleValue.setStringValue(str);
        }
    }

    @Override // com.microsoft.schemas.vml.CTStroke
    public void xsetOpacity(XmlString xmlString) {
        synchronized (monitor()) {
            check_orphaned();
            XmlString xmlString2 = (XmlString) get_store().find_attribute_user(OPACITY$18);
            if (xmlString2 == null) {
                xmlString2 = (XmlString) get_store().add_attribute_user(OPACITY$18);
            }
            xmlString2.set(xmlString);
        }
    }

    @Override // com.microsoft.schemas.vml.CTStroke
    public void unsetOpacity() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(OPACITY$18);
        }
    }

    @Override // com.microsoft.schemas.vml.CTStroke
    public STStrokeLineStyle$Enum getLinestyle() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(LINESTYLE$20);
            if (simpleValue == null) {
                return null;
            }
            return (STStrokeLineStyle$Enum) simpleValue.getEnumValue();
        }
    }

    @Override // com.microsoft.schemas.vml.CTStroke
    public STStrokeLineStyle xgetLinestyle() {
        STStrokeLineStyle sTStrokeLineStyleFind_attribute_user;
        synchronized (monitor()) {
            check_orphaned();
            sTStrokeLineStyleFind_attribute_user = get_store().find_attribute_user(LINESTYLE$20);
        }
        return sTStrokeLineStyleFind_attribute_user;
    }

    @Override // com.microsoft.schemas.vml.CTStroke
    public boolean isSetLinestyle() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(LINESTYLE$20) != null;
        }
        return z;
    }

    @Override // com.microsoft.schemas.vml.CTStroke
    public void setLinestyle(STStrokeLineStyle$Enum sTStrokeLineStyle$Enum) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(LINESTYLE$20);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(LINESTYLE$20);
            }
            simpleValue.setEnumValue(sTStrokeLineStyle$Enum);
        }
    }

    @Override // com.microsoft.schemas.vml.CTStroke
    public void xsetLinestyle(STStrokeLineStyle sTStrokeLineStyle) {
        synchronized (monitor()) {
            check_orphaned();
            STStrokeLineStyle sTStrokeLineStyleFind_attribute_user = get_store().find_attribute_user(LINESTYLE$20);
            if (sTStrokeLineStyleFind_attribute_user == null) {
                sTStrokeLineStyleFind_attribute_user = (STStrokeLineStyle) get_store().add_attribute_user(LINESTYLE$20);
            }
            sTStrokeLineStyleFind_attribute_user.set(sTStrokeLineStyle);
        }
    }

    @Override // com.microsoft.schemas.vml.CTStroke
    public void unsetLinestyle() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(LINESTYLE$20);
        }
    }

    @Override // com.microsoft.schemas.vml.CTStroke
    public BigDecimal getMiterlimit() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(MITERLIMIT$22);
            if (simpleValue == null) {
                return null;
            }
            return simpleValue.getBigDecimalValue();
        }
    }

    @Override // com.microsoft.schemas.vml.CTStroke
    public XmlDecimal xgetMiterlimit() {
        XmlDecimal xmlDecimal;
        synchronized (monitor()) {
            check_orphaned();
            xmlDecimal = (XmlDecimal) get_store().find_attribute_user(MITERLIMIT$22);
        }
        return xmlDecimal;
    }

    @Override // com.microsoft.schemas.vml.CTStroke
    public boolean isSetMiterlimit() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(MITERLIMIT$22) != null;
        }
        return z;
    }

    @Override // com.microsoft.schemas.vml.CTStroke
    public void setMiterlimit(BigDecimal bigDecimal) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(MITERLIMIT$22);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(MITERLIMIT$22);
            }
            simpleValue.setBigDecimalValue(bigDecimal);
        }
    }

    @Override // com.microsoft.schemas.vml.CTStroke
    public void xsetMiterlimit(XmlDecimal xmlDecimal) {
        synchronized (monitor()) {
            check_orphaned();
            XmlDecimal xmlDecimal2 = (XmlDecimal) get_store().find_attribute_user(MITERLIMIT$22);
            if (xmlDecimal2 == null) {
                xmlDecimal2 = (XmlDecimal) get_store().add_attribute_user(MITERLIMIT$22);
            }
            xmlDecimal2.set(xmlDecimal);
        }
    }

    @Override // com.microsoft.schemas.vml.CTStroke
    public void unsetMiterlimit() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(MITERLIMIT$22);
        }
    }

    @Override // com.microsoft.schemas.vml.CTStroke
    public STStrokeJoinStyle.Enum getJoinstyle() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(JOINSTYLE$24);
            if (simpleValue == null) {
                return null;
            }
            return (STStrokeJoinStyle.Enum) simpleValue.getEnumValue();
        }
    }

    @Override // com.microsoft.schemas.vml.CTStroke
    public STStrokeJoinStyle xgetJoinstyle() {
        STStrokeJoinStyle sTStrokeJoinStyle;
        synchronized (monitor()) {
            check_orphaned();
            sTStrokeJoinStyle = (STStrokeJoinStyle) get_store().find_attribute_user(JOINSTYLE$24);
        }
        return sTStrokeJoinStyle;
    }

    @Override // com.microsoft.schemas.vml.CTStroke
    public boolean isSetJoinstyle() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(JOINSTYLE$24) != null;
        }
        return z;
    }

    @Override // com.microsoft.schemas.vml.CTStroke
    public void setJoinstyle(STStrokeJoinStyle.Enum r4) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(JOINSTYLE$24);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(JOINSTYLE$24);
            }
            simpleValue.setEnumValue(r4);
        }
    }

    @Override // com.microsoft.schemas.vml.CTStroke
    public void xsetJoinstyle(STStrokeJoinStyle sTStrokeJoinStyle) {
        synchronized (monitor()) {
            check_orphaned();
            STStrokeJoinStyle sTStrokeJoinStyle2 = (STStrokeJoinStyle) get_store().find_attribute_user(JOINSTYLE$24);
            if (sTStrokeJoinStyle2 == null) {
                sTStrokeJoinStyle2 = (STStrokeJoinStyle) get_store().add_attribute_user(JOINSTYLE$24);
            }
            sTStrokeJoinStyle2.set(sTStrokeJoinStyle);
        }
    }

    @Override // com.microsoft.schemas.vml.CTStroke
    public void unsetJoinstyle() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(JOINSTYLE$24);
        }
    }

    @Override // com.microsoft.schemas.vml.CTStroke
    public STStrokeEndCap$Enum getEndcap() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(ENDCAP$26);
            if (simpleValue == null) {
                return null;
            }
            return (STStrokeEndCap$Enum) simpleValue.getEnumValue();
        }
    }

    @Override // com.microsoft.schemas.vml.CTStroke
    public STStrokeEndCap xgetEndcap() {
        STStrokeEndCap sTStrokeEndCapFind_attribute_user;
        synchronized (monitor()) {
            check_orphaned();
            sTStrokeEndCapFind_attribute_user = get_store().find_attribute_user(ENDCAP$26);
        }
        return sTStrokeEndCapFind_attribute_user;
    }

    @Override // com.microsoft.schemas.vml.CTStroke
    public boolean isSetEndcap() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(ENDCAP$26) != null;
        }
        return z;
    }

    @Override // com.microsoft.schemas.vml.CTStroke
    public void setEndcap(STStrokeEndCap$Enum sTStrokeEndCap$Enum) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(ENDCAP$26);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(ENDCAP$26);
            }
            simpleValue.setEnumValue(sTStrokeEndCap$Enum);
        }
    }

    @Override // com.microsoft.schemas.vml.CTStroke
    public void xsetEndcap(STStrokeEndCap sTStrokeEndCap) {
        synchronized (monitor()) {
            check_orphaned();
            STStrokeEndCap sTStrokeEndCapFind_attribute_user = get_store().find_attribute_user(ENDCAP$26);
            if (sTStrokeEndCapFind_attribute_user == null) {
                sTStrokeEndCapFind_attribute_user = (STStrokeEndCap) get_store().add_attribute_user(ENDCAP$26);
            }
            sTStrokeEndCapFind_attribute_user.set(sTStrokeEndCap);
        }
    }

    @Override // com.microsoft.schemas.vml.CTStroke
    public void unsetEndcap() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(ENDCAP$26);
        }
    }

    @Override // com.microsoft.schemas.vml.CTStroke
    public String getDashstyle() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(DASHSTYLE$28);
            if (simpleValue == null) {
                return null;
            }
            return simpleValue.getStringValue();
        }
    }

    @Override // com.microsoft.schemas.vml.CTStroke
    public XmlString xgetDashstyle() {
        XmlString xmlString;
        synchronized (monitor()) {
            check_orphaned();
            xmlString = (XmlString) get_store().find_attribute_user(DASHSTYLE$28);
        }
        return xmlString;
    }

    @Override // com.microsoft.schemas.vml.CTStroke
    public boolean isSetDashstyle() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(DASHSTYLE$28) != null;
        }
        return z;
    }

    @Override // com.microsoft.schemas.vml.CTStroke
    public void setDashstyle(String str) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(DASHSTYLE$28);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(DASHSTYLE$28);
            }
            simpleValue.setStringValue(str);
        }
    }

    @Override // com.microsoft.schemas.vml.CTStroke
    public void xsetDashstyle(XmlString xmlString) {
        synchronized (monitor()) {
            check_orphaned();
            XmlString xmlString2 = (XmlString) get_store().find_attribute_user(DASHSTYLE$28);
            if (xmlString2 == null) {
                xmlString2 = (XmlString) get_store().add_attribute_user(DASHSTYLE$28);
            }
            xmlString2.set(xmlString);
        }
    }

    @Override // com.microsoft.schemas.vml.CTStroke
    public void unsetDashstyle() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(DASHSTYLE$28);
        }
    }

    @Override // com.microsoft.schemas.vml.CTStroke
    public STFillType$Enum getFilltype() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(FILLTYPE$30);
            if (simpleValue == null) {
                return null;
            }
            return (STFillType$Enum) simpleValue.getEnumValue();
        }
    }

    @Override // com.microsoft.schemas.vml.CTStroke
    public STFillType xgetFilltype() {
        STFillType sTFillTypeFind_attribute_user;
        synchronized (monitor()) {
            check_orphaned();
            sTFillTypeFind_attribute_user = get_store().find_attribute_user(FILLTYPE$30);
        }
        return sTFillTypeFind_attribute_user;
    }

    @Override // com.microsoft.schemas.vml.CTStroke
    public boolean isSetFilltype() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(FILLTYPE$30) != null;
        }
        return z;
    }

    @Override // com.microsoft.schemas.vml.CTStroke
    public void setFilltype(STFillType$Enum sTFillType$Enum) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(FILLTYPE$30);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(FILLTYPE$30);
            }
            simpleValue.setEnumValue(sTFillType$Enum);
        }
    }

    @Override // com.microsoft.schemas.vml.CTStroke
    public void xsetFilltype(STFillType sTFillType) {
        synchronized (monitor()) {
            check_orphaned();
            STFillType sTFillTypeFind_attribute_user = get_store().find_attribute_user(FILLTYPE$30);
            if (sTFillTypeFind_attribute_user == null) {
                sTFillTypeFind_attribute_user = (STFillType) get_store().add_attribute_user(FILLTYPE$30);
            }
            sTFillTypeFind_attribute_user.set(sTFillType);
        }
    }

    @Override // com.microsoft.schemas.vml.CTStroke
    public void unsetFilltype() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(FILLTYPE$30);
        }
    }

    @Override // com.microsoft.schemas.vml.CTStroke
    public String getSrc() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(SRC$32);
            if (simpleValue == null) {
                return null;
            }
            return simpleValue.getStringValue();
        }
    }

    @Override // com.microsoft.schemas.vml.CTStroke
    public XmlString xgetSrc() {
        XmlString xmlString;
        synchronized (monitor()) {
            check_orphaned();
            xmlString = (XmlString) get_store().find_attribute_user(SRC$32);
        }
        return xmlString;
    }

    @Override // com.microsoft.schemas.vml.CTStroke
    public boolean isSetSrc() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(SRC$32) != null;
        }
        return z;
    }

    @Override // com.microsoft.schemas.vml.CTStroke
    public void setSrc(String str) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(SRC$32);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(SRC$32);
            }
            simpleValue.setStringValue(str);
        }
    }

    @Override // com.microsoft.schemas.vml.CTStroke
    public void xsetSrc(XmlString xmlString) {
        synchronized (monitor()) {
            check_orphaned();
            XmlString xmlString2 = (XmlString) get_store().find_attribute_user(SRC$32);
            if (xmlString2 == null) {
                xmlString2 = (XmlString) get_store().add_attribute_user(SRC$32);
            }
            xmlString2.set(xmlString);
        }
    }

    @Override // com.microsoft.schemas.vml.CTStroke
    public void unsetSrc() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(SRC$32);
        }
    }

    @Override // com.microsoft.schemas.vml.CTStroke
    public STImageAspect$Enum getImageaspect() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(IMAGEASPECT$34);
            if (simpleValue == null) {
                return null;
            }
            return (STImageAspect$Enum) simpleValue.getEnumValue();
        }
    }

    @Override // com.microsoft.schemas.vml.CTStroke
    public STImageAspect xgetImageaspect() {
        STImageAspect sTImageAspectFind_attribute_user;
        synchronized (monitor()) {
            check_orphaned();
            sTImageAspectFind_attribute_user = get_store().find_attribute_user(IMAGEASPECT$34);
        }
        return sTImageAspectFind_attribute_user;
    }

    @Override // com.microsoft.schemas.vml.CTStroke
    public boolean isSetImageaspect() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(IMAGEASPECT$34) != null;
        }
        return z;
    }

    @Override // com.microsoft.schemas.vml.CTStroke
    public void setImageaspect(STImageAspect$Enum sTImageAspect$Enum) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(IMAGEASPECT$34);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(IMAGEASPECT$34);
            }
            simpleValue.setEnumValue(sTImageAspect$Enum);
        }
    }

    @Override // com.microsoft.schemas.vml.CTStroke
    public void xsetImageaspect(STImageAspect sTImageAspect) {
        synchronized (monitor()) {
            check_orphaned();
            STImageAspect sTImageAspectFind_attribute_user = get_store().find_attribute_user(IMAGEASPECT$34);
            if (sTImageAspectFind_attribute_user == null) {
                sTImageAspectFind_attribute_user = (STImageAspect) get_store().add_attribute_user(IMAGEASPECT$34);
            }
            sTImageAspectFind_attribute_user.set(sTImageAspect);
        }
    }

    @Override // com.microsoft.schemas.vml.CTStroke
    public void unsetImageaspect() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(IMAGEASPECT$34);
        }
    }

    @Override // com.microsoft.schemas.vml.CTStroke
    public String getImagesize() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(IMAGESIZE$36);
            if (simpleValue == null) {
                return null;
            }
            return simpleValue.getStringValue();
        }
    }

    @Override // com.microsoft.schemas.vml.CTStroke
    public XmlString xgetImagesize() {
        XmlString xmlString;
        synchronized (monitor()) {
            check_orphaned();
            xmlString = (XmlString) get_store().find_attribute_user(IMAGESIZE$36);
        }
        return xmlString;
    }

    @Override // com.microsoft.schemas.vml.CTStroke
    public boolean isSetImagesize() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(IMAGESIZE$36) != null;
        }
        return z;
    }

    @Override // com.microsoft.schemas.vml.CTStroke
    public void setImagesize(String str) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(IMAGESIZE$36);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(IMAGESIZE$36);
            }
            simpleValue.setStringValue(str);
        }
    }

    @Override // com.microsoft.schemas.vml.CTStroke
    public void xsetImagesize(XmlString xmlString) {
        synchronized (monitor()) {
            check_orphaned();
            XmlString xmlString2 = (XmlString) get_store().find_attribute_user(IMAGESIZE$36);
            if (xmlString2 == null) {
                xmlString2 = (XmlString) get_store().add_attribute_user(IMAGESIZE$36);
            }
            xmlString2.set(xmlString);
        }
    }

    @Override // com.microsoft.schemas.vml.CTStroke
    public void unsetImagesize() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(IMAGESIZE$36);
        }
    }

    @Override // com.microsoft.schemas.vml.CTStroke
    public STTrueFalse.Enum getImagealignshape() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(IMAGEALIGNSHAPE$38);
            if (simpleValue == null) {
                return null;
            }
            return (STTrueFalse.Enum) simpleValue.getEnumValue();
        }
    }

    @Override // com.microsoft.schemas.vml.CTStroke
    public STTrueFalse xgetImagealignshape() {
        STTrueFalse sTTrueFalse;
        synchronized (monitor()) {
            check_orphaned();
            sTTrueFalse = (STTrueFalse) get_store().find_attribute_user(IMAGEALIGNSHAPE$38);
        }
        return sTTrueFalse;
    }

    @Override // com.microsoft.schemas.vml.CTStroke
    public boolean isSetImagealignshape() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(IMAGEALIGNSHAPE$38) != null;
        }
        return z;
    }

    @Override // com.microsoft.schemas.vml.CTStroke
    public void setImagealignshape(STTrueFalse.Enum r4) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(IMAGEALIGNSHAPE$38);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(IMAGEALIGNSHAPE$38);
            }
            simpleValue.setEnumValue(r4);
        }
    }

    @Override // com.microsoft.schemas.vml.CTStroke
    public void xsetImagealignshape(STTrueFalse sTTrueFalse) {
        synchronized (monitor()) {
            check_orphaned();
            STTrueFalse sTTrueFalse2 = (STTrueFalse) get_store().find_attribute_user(IMAGEALIGNSHAPE$38);
            if (sTTrueFalse2 == null) {
                sTTrueFalse2 = (STTrueFalse) get_store().add_attribute_user(IMAGEALIGNSHAPE$38);
            }
            sTTrueFalse2.set(sTTrueFalse);
        }
    }

    @Override // com.microsoft.schemas.vml.CTStroke
    public void unsetImagealignshape() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(IMAGEALIGNSHAPE$38);
        }
    }

    @Override // com.microsoft.schemas.vml.CTStroke
    public String getColor2() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(COLOR2$40);
            if (simpleValue == null) {
                return null;
            }
            return simpleValue.getStringValue();
        }
    }

    @Override // com.microsoft.schemas.vml.CTStroke
    public STColorType xgetColor2() {
        STColorType sTColorType;
        synchronized (monitor()) {
            check_orphaned();
            sTColorType = (STColorType) get_store().find_attribute_user(COLOR2$40);
        }
        return sTColorType;
    }

    @Override // com.microsoft.schemas.vml.CTStroke
    public boolean isSetColor2() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(COLOR2$40) != null;
        }
        return z;
    }

    @Override // com.microsoft.schemas.vml.CTStroke
    public void setColor2(String str) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(COLOR2$40);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(COLOR2$40);
            }
            simpleValue.setStringValue(str);
        }
    }

    @Override // com.microsoft.schemas.vml.CTStroke
    public void xsetColor2(STColorType sTColorType) {
        synchronized (monitor()) {
            check_orphaned();
            STColorType sTColorType2 = (STColorType) get_store().find_attribute_user(COLOR2$40);
            if (sTColorType2 == null) {
                sTColorType2 = (STColorType) get_store().add_attribute_user(COLOR2$40);
            }
            sTColorType2.set(sTColorType);
        }
    }

    @Override // com.microsoft.schemas.vml.CTStroke
    public void unsetColor2() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(COLOR2$40);
        }
    }

    @Override // com.microsoft.schemas.vml.CTStroke
    public STStrokeArrowType$Enum getStartarrow() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(STARTARROW$42);
            if (simpleValue == null) {
                return null;
            }
            return (STStrokeArrowType$Enum) simpleValue.getEnumValue();
        }
    }

    @Override // com.microsoft.schemas.vml.CTStroke
    public STStrokeArrowType xgetStartarrow() {
        STStrokeArrowType sTStrokeArrowTypeFind_attribute_user;
        synchronized (monitor()) {
            check_orphaned();
            sTStrokeArrowTypeFind_attribute_user = get_store().find_attribute_user(STARTARROW$42);
        }
        return sTStrokeArrowTypeFind_attribute_user;
    }

    @Override // com.microsoft.schemas.vml.CTStroke
    public boolean isSetStartarrow() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(STARTARROW$42) != null;
        }
        return z;
    }

    @Override // com.microsoft.schemas.vml.CTStroke
    public void setStartarrow(STStrokeArrowType$Enum sTStrokeArrowType$Enum) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(STARTARROW$42);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(STARTARROW$42);
            }
            simpleValue.setEnumValue(sTStrokeArrowType$Enum);
        }
    }

    @Override // com.microsoft.schemas.vml.CTStroke
    public void xsetStartarrow(STStrokeArrowType sTStrokeArrowType) {
        synchronized (monitor()) {
            check_orphaned();
            STStrokeArrowType sTStrokeArrowTypeFind_attribute_user = get_store().find_attribute_user(STARTARROW$42);
            if (sTStrokeArrowTypeFind_attribute_user == null) {
                sTStrokeArrowTypeFind_attribute_user = (STStrokeArrowType) get_store().add_attribute_user(STARTARROW$42);
            }
            sTStrokeArrowTypeFind_attribute_user.set(sTStrokeArrowType);
        }
    }

    @Override // com.microsoft.schemas.vml.CTStroke
    public void unsetStartarrow() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(STARTARROW$42);
        }
    }

    @Override // com.microsoft.schemas.vml.CTStroke
    public STStrokeArrowWidth$Enum getStartarrowwidth() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(STARTARROWWIDTH$44);
            if (simpleValue == null) {
                return null;
            }
            return (STStrokeArrowWidth$Enum) simpleValue.getEnumValue();
        }
    }

    @Override // com.microsoft.schemas.vml.CTStroke
    public STStrokeArrowWidth xgetStartarrowwidth() {
        STStrokeArrowWidth sTStrokeArrowWidthFind_attribute_user;
        synchronized (monitor()) {
            check_orphaned();
            sTStrokeArrowWidthFind_attribute_user = get_store().find_attribute_user(STARTARROWWIDTH$44);
        }
        return sTStrokeArrowWidthFind_attribute_user;
    }

    @Override // com.microsoft.schemas.vml.CTStroke
    public boolean isSetStartarrowwidth() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(STARTARROWWIDTH$44) != null;
        }
        return z;
    }

    @Override // com.microsoft.schemas.vml.CTStroke
    public void setStartarrowwidth(STStrokeArrowWidth$Enum sTStrokeArrowWidth$Enum) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(STARTARROWWIDTH$44);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(STARTARROWWIDTH$44);
            }
            simpleValue.setEnumValue(sTStrokeArrowWidth$Enum);
        }
    }

    @Override // com.microsoft.schemas.vml.CTStroke
    public void xsetStartarrowwidth(STStrokeArrowWidth sTStrokeArrowWidth) {
        synchronized (monitor()) {
            check_orphaned();
            STStrokeArrowWidth sTStrokeArrowWidthFind_attribute_user = get_store().find_attribute_user(STARTARROWWIDTH$44);
            if (sTStrokeArrowWidthFind_attribute_user == null) {
                sTStrokeArrowWidthFind_attribute_user = (STStrokeArrowWidth) get_store().add_attribute_user(STARTARROWWIDTH$44);
            }
            sTStrokeArrowWidthFind_attribute_user.set(sTStrokeArrowWidth);
        }
    }

    @Override // com.microsoft.schemas.vml.CTStroke
    public void unsetStartarrowwidth() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(STARTARROWWIDTH$44);
        }
    }

    @Override // com.microsoft.schemas.vml.CTStroke
    public STStrokeArrowLength$Enum getStartarrowlength() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(STARTARROWLENGTH$46);
            if (simpleValue == null) {
                return null;
            }
            return (STStrokeArrowLength$Enum) simpleValue.getEnumValue();
        }
    }

    @Override // com.microsoft.schemas.vml.CTStroke
    public STStrokeArrowLength xgetStartarrowlength() {
        STStrokeArrowLength sTStrokeArrowLengthFind_attribute_user;
        synchronized (monitor()) {
            check_orphaned();
            sTStrokeArrowLengthFind_attribute_user = get_store().find_attribute_user(STARTARROWLENGTH$46);
        }
        return sTStrokeArrowLengthFind_attribute_user;
    }

    @Override // com.microsoft.schemas.vml.CTStroke
    public boolean isSetStartarrowlength() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(STARTARROWLENGTH$46) != null;
        }
        return z;
    }

    @Override // com.microsoft.schemas.vml.CTStroke
    public void setStartarrowlength(STStrokeArrowLength$Enum sTStrokeArrowLength$Enum) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(STARTARROWLENGTH$46);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(STARTARROWLENGTH$46);
            }
            simpleValue.setEnumValue(sTStrokeArrowLength$Enum);
        }
    }

    @Override // com.microsoft.schemas.vml.CTStroke
    public void xsetStartarrowlength(STStrokeArrowLength sTStrokeArrowLength) {
        synchronized (monitor()) {
            check_orphaned();
            STStrokeArrowLength sTStrokeArrowLengthFind_attribute_user = get_store().find_attribute_user(STARTARROWLENGTH$46);
            if (sTStrokeArrowLengthFind_attribute_user == null) {
                sTStrokeArrowLengthFind_attribute_user = (STStrokeArrowLength) get_store().add_attribute_user(STARTARROWLENGTH$46);
            }
            sTStrokeArrowLengthFind_attribute_user.set(sTStrokeArrowLength);
        }
    }

    @Override // com.microsoft.schemas.vml.CTStroke
    public void unsetStartarrowlength() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(STARTARROWLENGTH$46);
        }
    }

    @Override // com.microsoft.schemas.vml.CTStroke
    public STStrokeArrowType$Enum getEndarrow() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(ENDARROW$48);
            if (simpleValue == null) {
                return null;
            }
            return (STStrokeArrowType$Enum) simpleValue.getEnumValue();
        }
    }

    @Override // com.microsoft.schemas.vml.CTStroke
    public STStrokeArrowType xgetEndarrow() {
        STStrokeArrowType sTStrokeArrowTypeFind_attribute_user;
        synchronized (monitor()) {
            check_orphaned();
            sTStrokeArrowTypeFind_attribute_user = get_store().find_attribute_user(ENDARROW$48);
        }
        return sTStrokeArrowTypeFind_attribute_user;
    }

    @Override // com.microsoft.schemas.vml.CTStroke
    public boolean isSetEndarrow() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(ENDARROW$48) != null;
        }
        return z;
    }

    @Override // com.microsoft.schemas.vml.CTStroke
    public void setEndarrow(STStrokeArrowType$Enum sTStrokeArrowType$Enum) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(ENDARROW$48);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(ENDARROW$48);
            }
            simpleValue.setEnumValue(sTStrokeArrowType$Enum);
        }
    }

    @Override // com.microsoft.schemas.vml.CTStroke
    public void xsetEndarrow(STStrokeArrowType sTStrokeArrowType) {
        synchronized (monitor()) {
            check_orphaned();
            STStrokeArrowType sTStrokeArrowTypeFind_attribute_user = get_store().find_attribute_user(ENDARROW$48);
            if (sTStrokeArrowTypeFind_attribute_user == null) {
                sTStrokeArrowTypeFind_attribute_user = (STStrokeArrowType) get_store().add_attribute_user(ENDARROW$48);
            }
            sTStrokeArrowTypeFind_attribute_user.set(sTStrokeArrowType);
        }
    }

    @Override // com.microsoft.schemas.vml.CTStroke
    public void unsetEndarrow() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(ENDARROW$48);
        }
    }

    @Override // com.microsoft.schemas.vml.CTStroke
    public STStrokeArrowWidth$Enum getEndarrowwidth() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(ENDARROWWIDTH$50);
            if (simpleValue == null) {
                return null;
            }
            return (STStrokeArrowWidth$Enum) simpleValue.getEnumValue();
        }
    }

    @Override // com.microsoft.schemas.vml.CTStroke
    public STStrokeArrowWidth xgetEndarrowwidth() {
        STStrokeArrowWidth sTStrokeArrowWidthFind_attribute_user;
        synchronized (monitor()) {
            check_orphaned();
            sTStrokeArrowWidthFind_attribute_user = get_store().find_attribute_user(ENDARROWWIDTH$50);
        }
        return sTStrokeArrowWidthFind_attribute_user;
    }

    @Override // com.microsoft.schemas.vml.CTStroke
    public boolean isSetEndarrowwidth() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(ENDARROWWIDTH$50) != null;
        }
        return z;
    }

    @Override // com.microsoft.schemas.vml.CTStroke
    public void setEndarrowwidth(STStrokeArrowWidth$Enum sTStrokeArrowWidth$Enum) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(ENDARROWWIDTH$50);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(ENDARROWWIDTH$50);
            }
            simpleValue.setEnumValue(sTStrokeArrowWidth$Enum);
        }
    }

    @Override // com.microsoft.schemas.vml.CTStroke
    public void xsetEndarrowwidth(STStrokeArrowWidth sTStrokeArrowWidth) {
        synchronized (monitor()) {
            check_orphaned();
            STStrokeArrowWidth sTStrokeArrowWidthFind_attribute_user = get_store().find_attribute_user(ENDARROWWIDTH$50);
            if (sTStrokeArrowWidthFind_attribute_user == null) {
                sTStrokeArrowWidthFind_attribute_user = (STStrokeArrowWidth) get_store().add_attribute_user(ENDARROWWIDTH$50);
            }
            sTStrokeArrowWidthFind_attribute_user.set(sTStrokeArrowWidth);
        }
    }

    @Override // com.microsoft.schemas.vml.CTStroke
    public void unsetEndarrowwidth() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(ENDARROWWIDTH$50);
        }
    }

    @Override // com.microsoft.schemas.vml.CTStroke
    public STStrokeArrowLength$Enum getEndarrowlength() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(ENDARROWLENGTH$52);
            if (simpleValue == null) {
                return null;
            }
            return (STStrokeArrowLength$Enum) simpleValue.getEnumValue();
        }
    }

    @Override // com.microsoft.schemas.vml.CTStroke
    public STStrokeArrowLength xgetEndarrowlength() {
        STStrokeArrowLength sTStrokeArrowLengthFind_attribute_user;
        synchronized (monitor()) {
            check_orphaned();
            sTStrokeArrowLengthFind_attribute_user = get_store().find_attribute_user(ENDARROWLENGTH$52);
        }
        return sTStrokeArrowLengthFind_attribute_user;
    }

    @Override // com.microsoft.schemas.vml.CTStroke
    public boolean isSetEndarrowlength() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(ENDARROWLENGTH$52) != null;
        }
        return z;
    }

    @Override // com.microsoft.schemas.vml.CTStroke
    public void setEndarrowlength(STStrokeArrowLength$Enum sTStrokeArrowLength$Enum) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(ENDARROWLENGTH$52);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(ENDARROWLENGTH$52);
            }
            simpleValue.setEnumValue(sTStrokeArrowLength$Enum);
        }
    }

    @Override // com.microsoft.schemas.vml.CTStroke
    public void xsetEndarrowlength(STStrokeArrowLength sTStrokeArrowLength) {
        synchronized (monitor()) {
            check_orphaned();
            STStrokeArrowLength sTStrokeArrowLengthFind_attribute_user = get_store().find_attribute_user(ENDARROWLENGTH$52);
            if (sTStrokeArrowLengthFind_attribute_user == null) {
                sTStrokeArrowLengthFind_attribute_user = (STStrokeArrowLength) get_store().add_attribute_user(ENDARROWLENGTH$52);
            }
            sTStrokeArrowLengthFind_attribute_user.set(sTStrokeArrowLength);
        }
    }

    @Override // com.microsoft.schemas.vml.CTStroke
    public void unsetEndarrowlength() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(ENDARROWLENGTH$52);
        }
    }

    @Override // com.microsoft.schemas.vml.CTStroke
    public String getHref() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(HREF$54);
            if (simpleValue == null) {
                return null;
            }
            return simpleValue.getStringValue();
        }
    }

    @Override // com.microsoft.schemas.vml.CTStroke
    public XmlString xgetHref() {
        XmlString xmlString;
        synchronized (monitor()) {
            check_orphaned();
            xmlString = (XmlString) get_store().find_attribute_user(HREF$54);
        }
        return xmlString;
    }

    @Override // com.microsoft.schemas.vml.CTStroke
    public boolean isSetHref() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(HREF$54) != null;
        }
        return z;
    }

    @Override // com.microsoft.schemas.vml.CTStroke
    public void setHref(String str) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(HREF$54);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(HREF$54);
            }
            simpleValue.setStringValue(str);
        }
    }

    @Override // com.microsoft.schemas.vml.CTStroke
    public void xsetHref(XmlString xmlString) {
        synchronized (monitor()) {
            check_orphaned();
            XmlString xmlString2 = (XmlString) get_store().find_attribute_user(HREF$54);
            if (xmlString2 == null) {
                xmlString2 = (XmlString) get_store().add_attribute_user(HREF$54);
            }
            xmlString2.set(xmlString);
        }
    }

    @Override // com.microsoft.schemas.vml.CTStroke
    public void unsetHref() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(HREF$54);
        }
    }

    @Override // com.microsoft.schemas.vml.CTStroke
    public String getAlthref() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(ALTHREF$56);
            if (simpleValue == null) {
                return null;
            }
            return simpleValue.getStringValue();
        }
    }

    @Override // com.microsoft.schemas.vml.CTStroke
    public XmlString xgetAlthref() {
        XmlString xmlString;
        synchronized (monitor()) {
            check_orphaned();
            xmlString = (XmlString) get_store().find_attribute_user(ALTHREF$56);
        }
        return xmlString;
    }

    @Override // com.microsoft.schemas.vml.CTStroke
    public boolean isSetAlthref() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(ALTHREF$56) != null;
        }
        return z;
    }

    @Override // com.microsoft.schemas.vml.CTStroke
    public void setAlthref(String str) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(ALTHREF$56);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(ALTHREF$56);
            }
            simpleValue.setStringValue(str);
        }
    }

    @Override // com.microsoft.schemas.vml.CTStroke
    public void xsetAlthref(XmlString xmlString) {
        synchronized (monitor()) {
            check_orphaned();
            XmlString xmlString2 = (XmlString) get_store().find_attribute_user(ALTHREF$56);
            if (xmlString2 == null) {
                xmlString2 = (XmlString) get_store().add_attribute_user(ALTHREF$56);
            }
            xmlString2.set(xmlString);
        }
    }

    @Override // com.microsoft.schemas.vml.CTStroke
    public void unsetAlthref() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(ALTHREF$56);
        }
    }

    @Override // com.microsoft.schemas.vml.CTStroke
    public String getTitle() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(TITLE$58);
            if (simpleValue == null) {
                return null;
            }
            return simpleValue.getStringValue();
        }
    }

    @Override // com.microsoft.schemas.vml.CTStroke
    public XmlString xgetTitle() {
        XmlString xmlString;
        synchronized (monitor()) {
            check_orphaned();
            xmlString = (XmlString) get_store().find_attribute_user(TITLE$58);
        }
        return xmlString;
    }

    @Override // com.microsoft.schemas.vml.CTStroke
    public boolean isSetTitle() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(TITLE$58) != null;
        }
        return z;
    }

    @Override // com.microsoft.schemas.vml.CTStroke
    public void setTitle(String str) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(TITLE$58);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(TITLE$58);
            }
            simpleValue.setStringValue(str);
        }
    }

    @Override // com.microsoft.schemas.vml.CTStroke
    public void xsetTitle(XmlString xmlString) {
        synchronized (monitor()) {
            check_orphaned();
            XmlString xmlString2 = (XmlString) get_store().find_attribute_user(TITLE$58);
            if (xmlString2 == null) {
                xmlString2 = (XmlString) get_store().add_attribute_user(TITLE$58);
            }
            xmlString2.set(xmlString);
        }
    }

    @Override // com.microsoft.schemas.vml.CTStroke
    public void unsetTitle() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(TITLE$58);
        }
    }

    @Override // com.microsoft.schemas.vml.CTStroke
    public STTrueFalse$Enum getForcedash() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(FORCEDASH$60);
            if (simpleValue == null) {
                return null;
            }
            return (STTrueFalse$Enum) simpleValue.getEnumValue();
        }
    }

    @Override // com.microsoft.schemas.vml.CTStroke
    public com.microsoft.schemas.office.office.STTrueFalse xgetForcedash() {
        com.microsoft.schemas.office.office.STTrueFalse sTTrueFalseFind_attribute_user;
        synchronized (monitor()) {
            check_orphaned();
            sTTrueFalseFind_attribute_user = get_store().find_attribute_user(FORCEDASH$60);
        }
        return sTTrueFalseFind_attribute_user;
    }

    @Override // com.microsoft.schemas.vml.CTStroke
    public boolean isSetForcedash() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(FORCEDASH$60) != null;
        }
        return z;
    }

    @Override // com.microsoft.schemas.vml.CTStroke
    public void setForcedash(STTrueFalse$Enum sTTrueFalse$Enum) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(FORCEDASH$60);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(FORCEDASH$60);
            }
            simpleValue.setEnumValue(sTTrueFalse$Enum);
        }
    }

    @Override // com.microsoft.schemas.vml.CTStroke
    public void xsetForcedash(com.microsoft.schemas.office.office.STTrueFalse sTTrueFalse) {
        synchronized (monitor()) {
            check_orphaned();
            com.microsoft.schemas.office.office.STTrueFalse sTTrueFalseFind_attribute_user = get_store().find_attribute_user(FORCEDASH$60);
            if (sTTrueFalseFind_attribute_user == null) {
                sTTrueFalseFind_attribute_user = (com.microsoft.schemas.office.office.STTrueFalse) get_store().add_attribute_user(FORCEDASH$60);
            }
            sTTrueFalseFind_attribute_user.set(sTTrueFalse);
        }
    }

    @Override // com.microsoft.schemas.vml.CTStroke
    public void unsetForcedash() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(FORCEDASH$60);
        }
    }

    @Override // com.microsoft.schemas.vml.CTStroke
    public String getId2() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(ID2$62);
            if (simpleValue == null) {
                return null;
            }
            return simpleValue.getStringValue();
        }
    }

    @Override // com.microsoft.schemas.vml.CTStroke
    public STRelationshipId xgetId2() {
        STRelationshipId sTRelationshipId;
        synchronized (monitor()) {
            check_orphaned();
            sTRelationshipId = (STRelationshipId) get_store().find_attribute_user(ID2$62);
        }
        return sTRelationshipId;
    }

    @Override // com.microsoft.schemas.vml.CTStroke
    public boolean isSetId2() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(ID2$62) != null;
        }
        return z;
    }

    @Override // com.microsoft.schemas.vml.CTStroke
    public void setId2(String str) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(ID2$62);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(ID2$62);
            }
            simpleValue.setStringValue(str);
        }
    }

    @Override // com.microsoft.schemas.vml.CTStroke
    public void xsetId2(STRelationshipId sTRelationshipId) {
        synchronized (monitor()) {
            check_orphaned();
            STRelationshipId sTRelationshipId2 = (STRelationshipId) get_store().find_attribute_user(ID2$62);
            if (sTRelationshipId2 == null) {
                sTRelationshipId2 = (STRelationshipId) get_store().add_attribute_user(ID2$62);
            }
            sTRelationshipId2.set(sTRelationshipId);
        }
    }

    @Override // com.microsoft.schemas.vml.CTStroke
    public void unsetId2() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(ID2$62);
        }
    }

    @Override // com.microsoft.schemas.vml.CTStroke
    public STTrueFalse.Enum getInsetpen() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(INSETPEN$64);
            if (simpleValue == null) {
                return null;
            }
            return (STTrueFalse.Enum) simpleValue.getEnumValue();
        }
    }

    @Override // com.microsoft.schemas.vml.CTStroke
    public STTrueFalse xgetInsetpen() {
        STTrueFalse sTTrueFalse;
        synchronized (monitor()) {
            check_orphaned();
            sTTrueFalse = (STTrueFalse) get_store().find_attribute_user(INSETPEN$64);
        }
        return sTTrueFalse;
    }

    @Override // com.microsoft.schemas.vml.CTStroke
    public boolean isSetInsetpen() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(INSETPEN$64) != null;
        }
        return z;
    }

    @Override // com.microsoft.schemas.vml.CTStroke
    public void setInsetpen(STTrueFalse.Enum r4) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(INSETPEN$64);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(INSETPEN$64);
            }
            simpleValue.setEnumValue(r4);
        }
    }

    @Override // com.microsoft.schemas.vml.CTStroke
    public void xsetInsetpen(STTrueFalse sTTrueFalse) {
        synchronized (monitor()) {
            check_orphaned();
            STTrueFalse sTTrueFalse2 = (STTrueFalse) get_store().find_attribute_user(INSETPEN$64);
            if (sTTrueFalse2 == null) {
                sTTrueFalse2 = (STTrueFalse) get_store().add_attribute_user(INSETPEN$64);
            }
            sTTrueFalse2.set(sTTrueFalse);
        }
    }

    @Override // com.microsoft.schemas.vml.CTStroke
    public void unsetInsetpen() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(INSETPEN$64);
        }
    }

    @Override // com.microsoft.schemas.vml.CTStroke
    public String getRelid() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(RELID$66);
            if (simpleValue == null) {
                return null;
            }
            return simpleValue.getStringValue();
        }
    }

    @Override // com.microsoft.schemas.vml.CTStroke
    public com.microsoft.schemas.office.office.STRelationshipId xgetRelid() {
        com.microsoft.schemas.office.office.STRelationshipId sTRelationshipIdFind_attribute_user;
        synchronized (monitor()) {
            check_orphaned();
            sTRelationshipIdFind_attribute_user = get_store().find_attribute_user(RELID$66);
        }
        return sTRelationshipIdFind_attribute_user;
    }

    @Override // com.microsoft.schemas.vml.CTStroke
    public boolean isSetRelid() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(RELID$66) != null;
        }
        return z;
    }

    @Override // com.microsoft.schemas.vml.CTStroke
    public void setRelid(String str) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(RELID$66);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(RELID$66);
            }
            simpleValue.setStringValue(str);
        }
    }

    @Override // com.microsoft.schemas.vml.CTStroke
    public void xsetRelid(com.microsoft.schemas.office.office.STRelationshipId sTRelationshipId) {
        synchronized (monitor()) {
            check_orphaned();
            com.microsoft.schemas.office.office.STRelationshipId sTRelationshipIdFind_attribute_user = get_store().find_attribute_user(RELID$66);
            if (sTRelationshipIdFind_attribute_user == null) {
                sTRelationshipIdFind_attribute_user = (com.microsoft.schemas.office.office.STRelationshipId) get_store().add_attribute_user(RELID$66);
            }
            sTRelationshipIdFind_attribute_user.set(sTRelationshipId);
        }
    }

    @Override // com.microsoft.schemas.vml.CTStroke
    public void unsetRelid() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(RELID$66);
        }
    }
}
