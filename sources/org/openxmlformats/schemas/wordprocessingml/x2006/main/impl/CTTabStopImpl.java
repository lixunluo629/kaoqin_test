package org.openxmlformats.schemas.wordprocessingml.x2006.main.impl;

import java.math.BigInteger;
import javax.xml.namespace.QName;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.SimpleValue;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTabStop;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STSignedTwipsMeasure;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STTabJc;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STTabTlc;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/wordprocessingml/x2006/main/impl/CTTabStopImpl.class */
public class CTTabStopImpl extends XmlComplexContentImpl implements CTTabStop {
    private static final QName VAL$0 = new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "val");
    private static final QName LEADER$2 = new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "leader");
    private static final QName POS$4 = new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "pos");

    public CTTabStopImpl(SchemaType schemaType) {
        super(schemaType);
    }

    @Override // org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTabStop
    public STTabJc.Enum getVal() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(VAL$0);
            if (simpleValue == null) {
                return null;
            }
            return (STTabJc.Enum) simpleValue.getEnumValue();
        }
    }

    @Override // org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTabStop
    public STTabJc xgetVal() {
        STTabJc sTTabJc;
        synchronized (monitor()) {
            check_orphaned();
            sTTabJc = (STTabJc) get_store().find_attribute_user(VAL$0);
        }
        return sTTabJc;
    }

    @Override // org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTabStop
    public void setVal(STTabJc.Enum r4) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(VAL$0);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(VAL$0);
            }
            simpleValue.setEnumValue(r4);
        }
    }

    @Override // org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTabStop
    public void xsetVal(STTabJc sTTabJc) {
        synchronized (monitor()) {
            check_orphaned();
            STTabJc sTTabJc2 = (STTabJc) get_store().find_attribute_user(VAL$0);
            if (sTTabJc2 == null) {
                sTTabJc2 = (STTabJc) get_store().add_attribute_user(VAL$0);
            }
            sTTabJc2.set(sTTabJc);
        }
    }

    @Override // org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTabStop
    public STTabTlc.Enum getLeader() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(LEADER$2);
            if (simpleValue == null) {
                return null;
            }
            return (STTabTlc.Enum) simpleValue.getEnumValue();
        }
    }

    @Override // org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTabStop
    public STTabTlc xgetLeader() {
        STTabTlc sTTabTlc;
        synchronized (monitor()) {
            check_orphaned();
            sTTabTlc = (STTabTlc) get_store().find_attribute_user(LEADER$2);
        }
        return sTTabTlc;
    }

    @Override // org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTabStop
    public boolean isSetLeader() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(LEADER$2) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTabStop
    public void setLeader(STTabTlc.Enum r4) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(LEADER$2);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(LEADER$2);
            }
            simpleValue.setEnumValue(r4);
        }
    }

    @Override // org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTabStop
    public void xsetLeader(STTabTlc sTTabTlc) {
        synchronized (monitor()) {
            check_orphaned();
            STTabTlc sTTabTlc2 = (STTabTlc) get_store().find_attribute_user(LEADER$2);
            if (sTTabTlc2 == null) {
                sTTabTlc2 = (STTabTlc) get_store().add_attribute_user(LEADER$2);
            }
            sTTabTlc2.set(sTTabTlc);
        }
    }

    @Override // org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTabStop
    public void unsetLeader() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(LEADER$2);
        }
    }

    @Override // org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTabStop
    public BigInteger getPos() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(POS$4);
            if (simpleValue == null) {
                return null;
            }
            return simpleValue.getBigIntegerValue();
        }
    }

    @Override // org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTabStop
    public STSignedTwipsMeasure xgetPos() {
        STSignedTwipsMeasure sTSignedTwipsMeasure;
        synchronized (monitor()) {
            check_orphaned();
            sTSignedTwipsMeasure = (STSignedTwipsMeasure) get_store().find_attribute_user(POS$4);
        }
        return sTSignedTwipsMeasure;
    }

    @Override // org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTabStop
    public void setPos(BigInteger bigInteger) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(POS$4);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(POS$4);
            }
            simpleValue.setBigIntegerValue(bigInteger);
        }
    }

    @Override // org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTabStop
    public void xsetPos(STSignedTwipsMeasure sTSignedTwipsMeasure) {
        synchronized (monitor()) {
            check_orphaned();
            STSignedTwipsMeasure sTSignedTwipsMeasure2 = (STSignedTwipsMeasure) get_store().find_attribute_user(POS$4);
            if (sTSignedTwipsMeasure2 == null) {
                sTSignedTwipsMeasure2 = (STSignedTwipsMeasure) get_store().add_attribute_user(POS$4);
            }
            sTSignedTwipsMeasure2.set(sTSignedTwipsMeasure);
        }
    }
}
