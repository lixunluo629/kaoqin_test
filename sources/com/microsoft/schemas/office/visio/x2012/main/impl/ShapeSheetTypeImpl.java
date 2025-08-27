package com.microsoft.schemas.office.visio.x2012.main.impl;

import com.microsoft.schemas.office.visio.x2012.main.DataType;
import com.microsoft.schemas.office.visio.x2012.main.ForeignDataType;
import com.microsoft.schemas.office.visio.x2012.main.ShapeSheetType;
import com.microsoft.schemas.office.visio.x2012.main.ShapesType;
import com.microsoft.schemas.office.visio.x2012.main.TextType;
import javax.xml.namespace.QName;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.SimpleValue;
import org.apache.xmlbeans.XmlBoolean;
import org.apache.xmlbeans.XmlString;
import org.apache.xmlbeans.XmlToken;
import org.apache.xmlbeans.XmlUnsignedInt;

/* loaded from: poi-ooxml-schemas-3.17.jar:com/microsoft/schemas/office/visio/x2012/main/impl/ShapeSheetTypeImpl.class */
public class ShapeSheetTypeImpl extends SheetTypeImpl implements ShapeSheetType {
    private static final QName TEXT$0 = new QName("http://schemas.microsoft.com/office/visio/2012/main", "Text");
    private static final QName DATA1$2 = new QName("http://schemas.microsoft.com/office/visio/2012/main", "Data1");
    private static final QName DATA2$4 = new QName("http://schemas.microsoft.com/office/visio/2012/main", "Data2");
    private static final QName DATA3$6 = new QName("http://schemas.microsoft.com/office/visio/2012/main", "Data3");
    private static final QName FOREIGNDATA$8 = new QName("http://schemas.microsoft.com/office/visio/2012/main", "ForeignData");
    private static final QName SHAPES$10 = new QName("http://schemas.microsoft.com/office/visio/2012/main", "Shapes");
    private static final QName ID$12 = new QName("", "ID");
    private static final QName ORIGINALID$14 = new QName("", "OriginalID");
    private static final QName DEL$16 = new QName("", "Del");
    private static final QName MASTERSHAPE$18 = new QName("", "MasterShape");
    private static final QName UNIQUEID$20 = new QName("", "UniqueID");
    private static final QName NAME$22 = new QName("", "Name");
    private static final QName NAMEU$24 = new QName("", "NameU");
    private static final QName ISCUSTOMNAME$26 = new QName("", "IsCustomName");
    private static final QName ISCUSTOMNAMEU$28 = new QName("", "IsCustomNameU");
    private static final QName MASTER$30 = new QName("", "Master");
    private static final QName TYPE$32 = new QName("", "Type");

    public ShapeSheetTypeImpl(SchemaType schemaType) {
        super(schemaType);
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.ShapeSheetType
    public TextType getText() {
        synchronized (monitor()) {
            check_orphaned();
            TextType textType = (TextType) get_store().find_element_user(TEXT$0, 0);
            if (textType == null) {
                return null;
            }
            return textType;
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.ShapeSheetType
    public boolean isSetText() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().count_elements(TEXT$0) != 0;
        }
        return z;
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.ShapeSheetType
    public void setText(TextType textType) {
        synchronized (monitor()) {
            check_orphaned();
            TextType textType2 = (TextType) get_store().find_element_user(TEXT$0, 0);
            if (textType2 == null) {
                textType2 = (TextType) get_store().add_element_user(TEXT$0);
            }
            textType2.set(textType);
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.ShapeSheetType
    public TextType addNewText() {
        TextType textType;
        synchronized (monitor()) {
            check_orphaned();
            textType = (TextType) get_store().add_element_user(TEXT$0);
        }
        return textType;
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.ShapeSheetType
    public void unsetText() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(TEXT$0, 0);
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.ShapeSheetType
    public DataType getData1() {
        synchronized (monitor()) {
            check_orphaned();
            DataType dataTypeFind_element_user = get_store().find_element_user(DATA1$2, 0);
            if (dataTypeFind_element_user == null) {
                return null;
            }
            return dataTypeFind_element_user;
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.ShapeSheetType
    public boolean isSetData1() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().count_elements(DATA1$2) != 0;
        }
        return z;
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.ShapeSheetType
    public void setData1(DataType dataType) {
        synchronized (monitor()) {
            check_orphaned();
            DataType dataTypeFind_element_user = get_store().find_element_user(DATA1$2, 0);
            if (dataTypeFind_element_user == null) {
                dataTypeFind_element_user = (DataType) get_store().add_element_user(DATA1$2);
            }
            dataTypeFind_element_user.set(dataType);
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.ShapeSheetType
    public DataType addNewData1() {
        DataType dataTypeAdd_element_user;
        synchronized (monitor()) {
            check_orphaned();
            dataTypeAdd_element_user = get_store().add_element_user(DATA1$2);
        }
        return dataTypeAdd_element_user;
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.ShapeSheetType
    public void unsetData1() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(DATA1$2, 0);
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.ShapeSheetType
    public DataType getData2() {
        synchronized (monitor()) {
            check_orphaned();
            DataType dataTypeFind_element_user = get_store().find_element_user(DATA2$4, 0);
            if (dataTypeFind_element_user == null) {
                return null;
            }
            return dataTypeFind_element_user;
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.ShapeSheetType
    public boolean isSetData2() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().count_elements(DATA2$4) != 0;
        }
        return z;
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.ShapeSheetType
    public void setData2(DataType dataType) {
        synchronized (monitor()) {
            check_orphaned();
            DataType dataTypeFind_element_user = get_store().find_element_user(DATA2$4, 0);
            if (dataTypeFind_element_user == null) {
                dataTypeFind_element_user = (DataType) get_store().add_element_user(DATA2$4);
            }
            dataTypeFind_element_user.set(dataType);
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.ShapeSheetType
    public DataType addNewData2() {
        DataType dataTypeAdd_element_user;
        synchronized (monitor()) {
            check_orphaned();
            dataTypeAdd_element_user = get_store().add_element_user(DATA2$4);
        }
        return dataTypeAdd_element_user;
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.ShapeSheetType
    public void unsetData2() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(DATA2$4, 0);
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.ShapeSheetType
    public DataType getData3() {
        synchronized (monitor()) {
            check_orphaned();
            DataType dataTypeFind_element_user = get_store().find_element_user(DATA3$6, 0);
            if (dataTypeFind_element_user == null) {
                return null;
            }
            return dataTypeFind_element_user;
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.ShapeSheetType
    public boolean isSetData3() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().count_elements(DATA3$6) != 0;
        }
        return z;
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.ShapeSheetType
    public void setData3(DataType dataType) {
        synchronized (monitor()) {
            check_orphaned();
            DataType dataTypeFind_element_user = get_store().find_element_user(DATA3$6, 0);
            if (dataTypeFind_element_user == null) {
                dataTypeFind_element_user = (DataType) get_store().add_element_user(DATA3$6);
            }
            dataTypeFind_element_user.set(dataType);
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.ShapeSheetType
    public DataType addNewData3() {
        DataType dataTypeAdd_element_user;
        synchronized (monitor()) {
            check_orphaned();
            dataTypeAdd_element_user = get_store().add_element_user(DATA3$6);
        }
        return dataTypeAdd_element_user;
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.ShapeSheetType
    public void unsetData3() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(DATA3$6, 0);
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.ShapeSheetType
    public ForeignDataType getForeignData() {
        synchronized (monitor()) {
            check_orphaned();
            ForeignDataType foreignDataTypeFind_element_user = get_store().find_element_user(FOREIGNDATA$8, 0);
            if (foreignDataTypeFind_element_user == null) {
                return null;
            }
            return foreignDataTypeFind_element_user;
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.ShapeSheetType
    public boolean isSetForeignData() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().count_elements(FOREIGNDATA$8) != 0;
        }
        return z;
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.ShapeSheetType
    public void setForeignData(ForeignDataType foreignDataType) {
        synchronized (monitor()) {
            check_orphaned();
            ForeignDataType foreignDataTypeFind_element_user = get_store().find_element_user(FOREIGNDATA$8, 0);
            if (foreignDataTypeFind_element_user == null) {
                foreignDataTypeFind_element_user = (ForeignDataType) get_store().add_element_user(FOREIGNDATA$8);
            }
            foreignDataTypeFind_element_user.set(foreignDataType);
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.ShapeSheetType
    public ForeignDataType addNewForeignData() {
        ForeignDataType foreignDataTypeAdd_element_user;
        synchronized (monitor()) {
            check_orphaned();
            foreignDataTypeAdd_element_user = get_store().add_element_user(FOREIGNDATA$8);
        }
        return foreignDataTypeAdd_element_user;
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.ShapeSheetType
    public void unsetForeignData() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(FOREIGNDATA$8, 0);
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.ShapeSheetType
    public ShapesType getShapes() {
        synchronized (monitor()) {
            check_orphaned();
            ShapesType shapesType = (ShapesType) get_store().find_element_user(SHAPES$10, 0);
            if (shapesType == null) {
                return null;
            }
            return shapesType;
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.ShapeSheetType
    public boolean isSetShapes() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().count_elements(SHAPES$10) != 0;
        }
        return z;
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.ShapeSheetType
    public void setShapes(ShapesType shapesType) {
        synchronized (monitor()) {
            check_orphaned();
            ShapesType shapesType2 = (ShapesType) get_store().find_element_user(SHAPES$10, 0);
            if (shapesType2 == null) {
                shapesType2 = (ShapesType) get_store().add_element_user(SHAPES$10);
            }
            shapesType2.set(shapesType);
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.ShapeSheetType
    public ShapesType addNewShapes() {
        ShapesType shapesType;
        synchronized (monitor()) {
            check_orphaned();
            shapesType = (ShapesType) get_store().add_element_user(SHAPES$10);
        }
        return shapesType;
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.ShapeSheetType
    public void unsetShapes() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(SHAPES$10, 0);
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.ShapeSheetType
    public long getID() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(ID$12);
            if (simpleValue == null) {
                return 0L;
            }
            return simpleValue.getLongValue();
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.ShapeSheetType
    public XmlUnsignedInt xgetID() {
        XmlUnsignedInt xmlUnsignedInt;
        synchronized (monitor()) {
            check_orphaned();
            xmlUnsignedInt = (XmlUnsignedInt) get_store().find_attribute_user(ID$12);
        }
        return xmlUnsignedInt;
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.ShapeSheetType
    public void setID(long j) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(ID$12);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(ID$12);
            }
            simpleValue.setLongValue(j);
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.ShapeSheetType
    public void xsetID(XmlUnsignedInt xmlUnsignedInt) {
        synchronized (monitor()) {
            check_orphaned();
            XmlUnsignedInt xmlUnsignedInt2 = (XmlUnsignedInt) get_store().find_attribute_user(ID$12);
            if (xmlUnsignedInt2 == null) {
                xmlUnsignedInt2 = (XmlUnsignedInt) get_store().add_attribute_user(ID$12);
            }
            xmlUnsignedInt2.set(xmlUnsignedInt);
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.ShapeSheetType
    public long getOriginalID() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(ORIGINALID$14);
            if (simpleValue == null) {
                return 0L;
            }
            return simpleValue.getLongValue();
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.ShapeSheetType
    public XmlUnsignedInt xgetOriginalID() {
        XmlUnsignedInt xmlUnsignedInt;
        synchronized (monitor()) {
            check_orphaned();
            xmlUnsignedInt = (XmlUnsignedInt) get_store().find_attribute_user(ORIGINALID$14);
        }
        return xmlUnsignedInt;
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.ShapeSheetType
    public boolean isSetOriginalID() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(ORIGINALID$14) != null;
        }
        return z;
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.ShapeSheetType
    public void setOriginalID(long j) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(ORIGINALID$14);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(ORIGINALID$14);
            }
            simpleValue.setLongValue(j);
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.ShapeSheetType
    public void xsetOriginalID(XmlUnsignedInt xmlUnsignedInt) {
        synchronized (monitor()) {
            check_orphaned();
            XmlUnsignedInt xmlUnsignedInt2 = (XmlUnsignedInt) get_store().find_attribute_user(ORIGINALID$14);
            if (xmlUnsignedInt2 == null) {
                xmlUnsignedInt2 = (XmlUnsignedInt) get_store().add_attribute_user(ORIGINALID$14);
            }
            xmlUnsignedInt2.set(xmlUnsignedInt);
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.ShapeSheetType
    public void unsetOriginalID() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(ORIGINALID$14);
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.ShapeSheetType
    public boolean getDel() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(DEL$16);
            if (simpleValue == null) {
                return false;
            }
            return simpleValue.getBooleanValue();
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.ShapeSheetType
    public XmlBoolean xgetDel() {
        XmlBoolean xmlBoolean;
        synchronized (monitor()) {
            check_orphaned();
            xmlBoolean = (XmlBoolean) get_store().find_attribute_user(DEL$16);
        }
        return xmlBoolean;
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.ShapeSheetType
    public boolean isSetDel() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(DEL$16) != null;
        }
        return z;
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.ShapeSheetType
    public void setDel(boolean z) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(DEL$16);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(DEL$16);
            }
            simpleValue.setBooleanValue(z);
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.ShapeSheetType
    public void xsetDel(XmlBoolean xmlBoolean) {
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(DEL$16);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_store().add_attribute_user(DEL$16);
            }
            xmlBoolean2.set(xmlBoolean);
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.ShapeSheetType
    public void unsetDel() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(DEL$16);
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.ShapeSheetType
    public long getMasterShape() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(MASTERSHAPE$18);
            if (simpleValue == null) {
                return 0L;
            }
            return simpleValue.getLongValue();
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.ShapeSheetType
    public XmlUnsignedInt xgetMasterShape() {
        XmlUnsignedInt xmlUnsignedInt;
        synchronized (monitor()) {
            check_orphaned();
            xmlUnsignedInt = (XmlUnsignedInt) get_store().find_attribute_user(MASTERSHAPE$18);
        }
        return xmlUnsignedInt;
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.ShapeSheetType
    public boolean isSetMasterShape() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(MASTERSHAPE$18) != null;
        }
        return z;
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.ShapeSheetType
    public void setMasterShape(long j) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(MASTERSHAPE$18);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(MASTERSHAPE$18);
            }
            simpleValue.setLongValue(j);
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.ShapeSheetType
    public void xsetMasterShape(XmlUnsignedInt xmlUnsignedInt) {
        synchronized (monitor()) {
            check_orphaned();
            XmlUnsignedInt xmlUnsignedInt2 = (XmlUnsignedInt) get_store().find_attribute_user(MASTERSHAPE$18);
            if (xmlUnsignedInt2 == null) {
                xmlUnsignedInt2 = (XmlUnsignedInt) get_store().add_attribute_user(MASTERSHAPE$18);
            }
            xmlUnsignedInt2.set(xmlUnsignedInt);
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.ShapeSheetType
    public void unsetMasterShape() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(MASTERSHAPE$18);
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.ShapeSheetType
    public String getUniqueID() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(UNIQUEID$20);
            if (simpleValue == null) {
                return null;
            }
            return simpleValue.getStringValue();
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.ShapeSheetType
    public XmlString xgetUniqueID() {
        XmlString xmlString;
        synchronized (monitor()) {
            check_orphaned();
            xmlString = (XmlString) get_store().find_attribute_user(UNIQUEID$20);
        }
        return xmlString;
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.ShapeSheetType
    public boolean isSetUniqueID() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(UNIQUEID$20) != null;
        }
        return z;
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.ShapeSheetType
    public void setUniqueID(String str) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(UNIQUEID$20);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(UNIQUEID$20);
            }
            simpleValue.setStringValue(str);
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.ShapeSheetType
    public void xsetUniqueID(XmlString xmlString) {
        synchronized (monitor()) {
            check_orphaned();
            XmlString xmlString2 = (XmlString) get_store().find_attribute_user(UNIQUEID$20);
            if (xmlString2 == null) {
                xmlString2 = (XmlString) get_store().add_attribute_user(UNIQUEID$20);
            }
            xmlString2.set(xmlString);
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.ShapeSheetType
    public void unsetUniqueID() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(UNIQUEID$20);
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.ShapeSheetType
    public String getName() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(NAME$22);
            if (simpleValue == null) {
                return null;
            }
            return simpleValue.getStringValue();
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.ShapeSheetType
    public XmlString xgetName() {
        XmlString xmlString;
        synchronized (monitor()) {
            check_orphaned();
            xmlString = (XmlString) get_store().find_attribute_user(NAME$22);
        }
        return xmlString;
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.ShapeSheetType
    public boolean isSetName() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(NAME$22) != null;
        }
        return z;
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.ShapeSheetType
    public void setName(String str) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(NAME$22);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(NAME$22);
            }
            simpleValue.setStringValue(str);
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.ShapeSheetType
    public void xsetName(XmlString xmlString) {
        synchronized (monitor()) {
            check_orphaned();
            XmlString xmlString2 = (XmlString) get_store().find_attribute_user(NAME$22);
            if (xmlString2 == null) {
                xmlString2 = (XmlString) get_store().add_attribute_user(NAME$22);
            }
            xmlString2.set(xmlString);
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.ShapeSheetType
    public void unsetName() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(NAME$22);
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.ShapeSheetType
    public String getNameU() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(NAMEU$24);
            if (simpleValue == null) {
                return null;
            }
            return simpleValue.getStringValue();
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.ShapeSheetType
    public XmlString xgetNameU() {
        XmlString xmlString;
        synchronized (monitor()) {
            check_orphaned();
            xmlString = (XmlString) get_store().find_attribute_user(NAMEU$24);
        }
        return xmlString;
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.ShapeSheetType
    public boolean isSetNameU() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(NAMEU$24) != null;
        }
        return z;
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.ShapeSheetType
    public void setNameU(String str) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(NAMEU$24);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(NAMEU$24);
            }
            simpleValue.setStringValue(str);
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.ShapeSheetType
    public void xsetNameU(XmlString xmlString) {
        synchronized (monitor()) {
            check_orphaned();
            XmlString xmlString2 = (XmlString) get_store().find_attribute_user(NAMEU$24);
            if (xmlString2 == null) {
                xmlString2 = (XmlString) get_store().add_attribute_user(NAMEU$24);
            }
            xmlString2.set(xmlString);
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.ShapeSheetType
    public void unsetNameU() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(NAMEU$24);
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.ShapeSheetType
    public boolean getIsCustomName() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(ISCUSTOMNAME$26);
            if (simpleValue == null) {
                return false;
            }
            return simpleValue.getBooleanValue();
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.ShapeSheetType
    public XmlBoolean xgetIsCustomName() {
        XmlBoolean xmlBoolean;
        synchronized (monitor()) {
            check_orphaned();
            xmlBoolean = (XmlBoolean) get_store().find_attribute_user(ISCUSTOMNAME$26);
        }
        return xmlBoolean;
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.ShapeSheetType
    public boolean isSetIsCustomName() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(ISCUSTOMNAME$26) != null;
        }
        return z;
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.ShapeSheetType
    public void setIsCustomName(boolean z) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(ISCUSTOMNAME$26);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(ISCUSTOMNAME$26);
            }
            simpleValue.setBooleanValue(z);
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.ShapeSheetType
    public void xsetIsCustomName(XmlBoolean xmlBoolean) {
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(ISCUSTOMNAME$26);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_store().add_attribute_user(ISCUSTOMNAME$26);
            }
            xmlBoolean2.set(xmlBoolean);
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.ShapeSheetType
    public void unsetIsCustomName() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(ISCUSTOMNAME$26);
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.ShapeSheetType
    public boolean getIsCustomNameU() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(ISCUSTOMNAMEU$28);
            if (simpleValue == null) {
                return false;
            }
            return simpleValue.getBooleanValue();
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.ShapeSheetType
    public XmlBoolean xgetIsCustomNameU() {
        XmlBoolean xmlBoolean;
        synchronized (monitor()) {
            check_orphaned();
            xmlBoolean = (XmlBoolean) get_store().find_attribute_user(ISCUSTOMNAMEU$28);
        }
        return xmlBoolean;
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.ShapeSheetType
    public boolean isSetIsCustomNameU() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(ISCUSTOMNAMEU$28) != null;
        }
        return z;
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.ShapeSheetType
    public void setIsCustomNameU(boolean z) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(ISCUSTOMNAMEU$28);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(ISCUSTOMNAMEU$28);
            }
            simpleValue.setBooleanValue(z);
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.ShapeSheetType
    public void xsetIsCustomNameU(XmlBoolean xmlBoolean) {
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(ISCUSTOMNAMEU$28);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_store().add_attribute_user(ISCUSTOMNAMEU$28);
            }
            xmlBoolean2.set(xmlBoolean);
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.ShapeSheetType
    public void unsetIsCustomNameU() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(ISCUSTOMNAMEU$28);
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.ShapeSheetType
    public long getMaster() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(MASTER$30);
            if (simpleValue == null) {
                return 0L;
            }
            return simpleValue.getLongValue();
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.ShapeSheetType
    public XmlUnsignedInt xgetMaster() {
        XmlUnsignedInt xmlUnsignedInt;
        synchronized (monitor()) {
            check_orphaned();
            xmlUnsignedInt = (XmlUnsignedInt) get_store().find_attribute_user(MASTER$30);
        }
        return xmlUnsignedInt;
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.ShapeSheetType
    public boolean isSetMaster() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(MASTER$30) != null;
        }
        return z;
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.ShapeSheetType
    public void setMaster(long j) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(MASTER$30);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(MASTER$30);
            }
            simpleValue.setLongValue(j);
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.ShapeSheetType
    public void xsetMaster(XmlUnsignedInt xmlUnsignedInt) {
        synchronized (monitor()) {
            check_orphaned();
            XmlUnsignedInt xmlUnsignedInt2 = (XmlUnsignedInt) get_store().find_attribute_user(MASTER$30);
            if (xmlUnsignedInt2 == null) {
                xmlUnsignedInt2 = (XmlUnsignedInt) get_store().add_attribute_user(MASTER$30);
            }
            xmlUnsignedInt2.set(xmlUnsignedInt);
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.ShapeSheetType
    public void unsetMaster() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(MASTER$30);
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.ShapeSheetType
    public String getType() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(TYPE$32);
            if (simpleValue == null) {
                return null;
            }
            return simpleValue.getStringValue();
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.ShapeSheetType
    public XmlToken xgetType() {
        XmlToken xmlToken;
        synchronized (monitor()) {
            check_orphaned();
            xmlToken = (XmlToken) get_store().find_attribute_user(TYPE$32);
        }
        return xmlToken;
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.ShapeSheetType
    public boolean isSetType() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(TYPE$32) != null;
        }
        return z;
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.ShapeSheetType
    public void setType(String str) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(TYPE$32);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(TYPE$32);
            }
            simpleValue.setStringValue(str);
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.ShapeSheetType
    public void xsetType(XmlToken xmlToken) {
        synchronized (monitor()) {
            check_orphaned();
            XmlToken xmlToken2 = (XmlToken) get_store().find_attribute_user(TYPE$32);
            if (xmlToken2 == null) {
                xmlToken2 = (XmlToken) get_store().add_attribute_user(TYPE$32);
            }
            xmlToken2.set(xmlToken);
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.ShapeSheetType
    public void unsetType() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(TYPE$32);
        }
    }
}
