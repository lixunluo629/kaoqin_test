package org.openxmlformats.schemas.wordprocessingml.x2006.main.impl;

import java.math.BigInteger;
import javax.xml.namespace.QName;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.SimpleValue;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHpsMeasure;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STHpsMeasure;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/wordprocessingml/x2006/main/impl/CTHpsMeasureImpl.class */
public class CTHpsMeasureImpl extends XmlComplexContentImpl implements CTHpsMeasure {
    private static final QName VAL$0 = new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "val");

    public CTHpsMeasureImpl(SchemaType schemaType) {
        super(schemaType);
    }

    @Override // org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHpsMeasure
    public BigInteger getVal() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(VAL$0);
            if (simpleValue == null) {
                return null;
            }
            return simpleValue.getBigIntegerValue();
        }
    }

    @Override // org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHpsMeasure
    public STHpsMeasure xgetVal() {
        STHpsMeasure sTHpsMeasure;
        synchronized (monitor()) {
            check_orphaned();
            sTHpsMeasure = (STHpsMeasure) get_store().find_attribute_user(VAL$0);
        }
        return sTHpsMeasure;
    }

    @Override // org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHpsMeasure
    public void setVal(BigInteger bigInteger) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(VAL$0);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(VAL$0);
            }
            simpleValue.setBigIntegerValue(bigInteger);
        }
    }

    @Override // org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHpsMeasure
    public void xsetVal(STHpsMeasure sTHpsMeasure) {
        synchronized (monitor()) {
            check_orphaned();
            STHpsMeasure sTHpsMeasure2 = (STHpsMeasure) get_store().find_attribute_user(VAL$0);
            if (sTHpsMeasure2 == null) {
                sTHpsMeasure2 = (STHpsMeasure) get_store().add_attribute_user(VAL$0);
            }
            sTHpsMeasure2.set(sTHpsMeasure);
        }
    }
}
