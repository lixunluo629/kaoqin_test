package org.openxmlformats.schemas.wordprocessingml.x2006.main.impl;

import java.math.BigInteger;
import javax.xml.namespace.QName;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.SimpleValue;
import org.apache.xmlbeans.impl.jam.xml.JamXmlElements;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSpacing;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STDecimalNumber;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STLineSpacingRule;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STOnOff;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STSignedTwipsMeasure;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STTwipsMeasure;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/wordprocessingml/x2006/main/impl/CTSpacingImpl.class */
public class CTSpacingImpl extends XmlComplexContentImpl implements CTSpacing {
    private static final QName BEFORE$0 = new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "before");
    private static final QName BEFORELINES$2 = new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "beforeLines");
    private static final QName BEFOREAUTOSPACING$4 = new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "beforeAutospacing");
    private static final QName AFTER$6 = new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "after");
    private static final QName AFTERLINES$8 = new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "afterLines");
    private static final QName AFTERAUTOSPACING$10 = new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "afterAutospacing");
    private static final QName LINE$12 = new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", JamXmlElements.LINE);
    private static final QName LINERULE$14 = new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "lineRule");

    public CTSpacingImpl(SchemaType schemaType) {
        super(schemaType);
    }

    @Override // org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSpacing
    public BigInteger getBefore() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(BEFORE$0);
            if (simpleValue == null) {
                return null;
            }
            return simpleValue.getBigIntegerValue();
        }
    }

    @Override // org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSpacing
    public STTwipsMeasure xgetBefore() {
        STTwipsMeasure sTTwipsMeasure;
        synchronized (monitor()) {
            check_orphaned();
            sTTwipsMeasure = (STTwipsMeasure) get_store().find_attribute_user(BEFORE$0);
        }
        return sTTwipsMeasure;
    }

    @Override // org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSpacing
    public boolean isSetBefore() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(BEFORE$0) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSpacing
    public void setBefore(BigInteger bigInteger) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(BEFORE$0);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(BEFORE$0);
            }
            simpleValue.setBigIntegerValue(bigInteger);
        }
    }

    @Override // org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSpacing
    public void xsetBefore(STTwipsMeasure sTTwipsMeasure) {
        synchronized (monitor()) {
            check_orphaned();
            STTwipsMeasure sTTwipsMeasure2 = (STTwipsMeasure) get_store().find_attribute_user(BEFORE$0);
            if (sTTwipsMeasure2 == null) {
                sTTwipsMeasure2 = (STTwipsMeasure) get_store().add_attribute_user(BEFORE$0);
            }
            sTTwipsMeasure2.set(sTTwipsMeasure);
        }
    }

    @Override // org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSpacing
    public void unsetBefore() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(BEFORE$0);
        }
    }

    @Override // org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSpacing
    public BigInteger getBeforeLines() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(BEFORELINES$2);
            if (simpleValue == null) {
                return null;
            }
            return simpleValue.getBigIntegerValue();
        }
    }

    @Override // org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSpacing
    public STDecimalNumber xgetBeforeLines() {
        STDecimalNumber sTDecimalNumber;
        synchronized (monitor()) {
            check_orphaned();
            sTDecimalNumber = (STDecimalNumber) get_store().find_attribute_user(BEFORELINES$2);
        }
        return sTDecimalNumber;
    }

    @Override // org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSpacing
    public boolean isSetBeforeLines() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(BEFORELINES$2) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSpacing
    public void setBeforeLines(BigInteger bigInteger) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(BEFORELINES$2);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(BEFORELINES$2);
            }
            simpleValue.setBigIntegerValue(bigInteger);
        }
    }

    @Override // org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSpacing
    public void xsetBeforeLines(STDecimalNumber sTDecimalNumber) {
        synchronized (monitor()) {
            check_orphaned();
            STDecimalNumber sTDecimalNumber2 = (STDecimalNumber) get_store().find_attribute_user(BEFORELINES$2);
            if (sTDecimalNumber2 == null) {
                sTDecimalNumber2 = (STDecimalNumber) get_store().add_attribute_user(BEFORELINES$2);
            }
            sTDecimalNumber2.set(sTDecimalNumber);
        }
    }

    @Override // org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSpacing
    public void unsetBeforeLines() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(BEFORELINES$2);
        }
    }

    @Override // org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSpacing
    public STOnOff.Enum getBeforeAutospacing() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(BEFOREAUTOSPACING$4);
            if (simpleValue == null) {
                return null;
            }
            return (STOnOff.Enum) simpleValue.getEnumValue();
        }
    }

    @Override // org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSpacing
    public STOnOff xgetBeforeAutospacing() {
        STOnOff sTOnOff;
        synchronized (monitor()) {
            check_orphaned();
            sTOnOff = (STOnOff) get_store().find_attribute_user(BEFOREAUTOSPACING$4);
        }
        return sTOnOff;
    }

    @Override // org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSpacing
    public boolean isSetBeforeAutospacing() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(BEFOREAUTOSPACING$4) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSpacing
    public void setBeforeAutospacing(STOnOff.Enum r4) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(BEFOREAUTOSPACING$4);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(BEFOREAUTOSPACING$4);
            }
            simpleValue.setEnumValue(r4);
        }
    }

    @Override // org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSpacing
    public void xsetBeforeAutospacing(STOnOff sTOnOff) {
        synchronized (monitor()) {
            check_orphaned();
            STOnOff sTOnOff2 = (STOnOff) get_store().find_attribute_user(BEFOREAUTOSPACING$4);
            if (sTOnOff2 == null) {
                sTOnOff2 = (STOnOff) get_store().add_attribute_user(BEFOREAUTOSPACING$4);
            }
            sTOnOff2.set(sTOnOff);
        }
    }

    @Override // org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSpacing
    public void unsetBeforeAutospacing() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(BEFOREAUTOSPACING$4);
        }
    }

    @Override // org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSpacing
    public BigInteger getAfter() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(AFTER$6);
            if (simpleValue == null) {
                return null;
            }
            return simpleValue.getBigIntegerValue();
        }
    }

    @Override // org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSpacing
    public STTwipsMeasure xgetAfter() {
        STTwipsMeasure sTTwipsMeasure;
        synchronized (monitor()) {
            check_orphaned();
            sTTwipsMeasure = (STTwipsMeasure) get_store().find_attribute_user(AFTER$6);
        }
        return sTTwipsMeasure;
    }

    @Override // org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSpacing
    public boolean isSetAfter() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(AFTER$6) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSpacing
    public void setAfter(BigInteger bigInteger) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(AFTER$6);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(AFTER$6);
            }
            simpleValue.setBigIntegerValue(bigInteger);
        }
    }

    @Override // org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSpacing
    public void xsetAfter(STTwipsMeasure sTTwipsMeasure) {
        synchronized (monitor()) {
            check_orphaned();
            STTwipsMeasure sTTwipsMeasure2 = (STTwipsMeasure) get_store().find_attribute_user(AFTER$6);
            if (sTTwipsMeasure2 == null) {
                sTTwipsMeasure2 = (STTwipsMeasure) get_store().add_attribute_user(AFTER$6);
            }
            sTTwipsMeasure2.set(sTTwipsMeasure);
        }
    }

    @Override // org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSpacing
    public void unsetAfter() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(AFTER$6);
        }
    }

    @Override // org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSpacing
    public BigInteger getAfterLines() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(AFTERLINES$8);
            if (simpleValue == null) {
                return null;
            }
            return simpleValue.getBigIntegerValue();
        }
    }

    @Override // org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSpacing
    public STDecimalNumber xgetAfterLines() {
        STDecimalNumber sTDecimalNumber;
        synchronized (monitor()) {
            check_orphaned();
            sTDecimalNumber = (STDecimalNumber) get_store().find_attribute_user(AFTERLINES$8);
        }
        return sTDecimalNumber;
    }

    @Override // org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSpacing
    public boolean isSetAfterLines() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(AFTERLINES$8) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSpacing
    public void setAfterLines(BigInteger bigInteger) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(AFTERLINES$8);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(AFTERLINES$8);
            }
            simpleValue.setBigIntegerValue(bigInteger);
        }
    }

    @Override // org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSpacing
    public void xsetAfterLines(STDecimalNumber sTDecimalNumber) {
        synchronized (monitor()) {
            check_orphaned();
            STDecimalNumber sTDecimalNumber2 = (STDecimalNumber) get_store().find_attribute_user(AFTERLINES$8);
            if (sTDecimalNumber2 == null) {
                sTDecimalNumber2 = (STDecimalNumber) get_store().add_attribute_user(AFTERLINES$8);
            }
            sTDecimalNumber2.set(sTDecimalNumber);
        }
    }

    @Override // org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSpacing
    public void unsetAfterLines() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(AFTERLINES$8);
        }
    }

    @Override // org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSpacing
    public STOnOff.Enum getAfterAutospacing() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(AFTERAUTOSPACING$10);
            if (simpleValue == null) {
                return null;
            }
            return (STOnOff.Enum) simpleValue.getEnumValue();
        }
    }

    @Override // org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSpacing
    public STOnOff xgetAfterAutospacing() {
        STOnOff sTOnOff;
        synchronized (monitor()) {
            check_orphaned();
            sTOnOff = (STOnOff) get_store().find_attribute_user(AFTERAUTOSPACING$10);
        }
        return sTOnOff;
    }

    @Override // org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSpacing
    public boolean isSetAfterAutospacing() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(AFTERAUTOSPACING$10) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSpacing
    public void setAfterAutospacing(STOnOff.Enum r4) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(AFTERAUTOSPACING$10);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(AFTERAUTOSPACING$10);
            }
            simpleValue.setEnumValue(r4);
        }
    }

    @Override // org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSpacing
    public void xsetAfterAutospacing(STOnOff sTOnOff) {
        synchronized (monitor()) {
            check_orphaned();
            STOnOff sTOnOff2 = (STOnOff) get_store().find_attribute_user(AFTERAUTOSPACING$10);
            if (sTOnOff2 == null) {
                sTOnOff2 = (STOnOff) get_store().add_attribute_user(AFTERAUTOSPACING$10);
            }
            sTOnOff2.set(sTOnOff);
        }
    }

    @Override // org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSpacing
    public void unsetAfterAutospacing() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(AFTERAUTOSPACING$10);
        }
    }

    @Override // org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSpacing
    public BigInteger getLine() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(LINE$12);
            if (simpleValue == null) {
                return null;
            }
            return simpleValue.getBigIntegerValue();
        }
    }

    @Override // org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSpacing
    public STSignedTwipsMeasure xgetLine() {
        STSignedTwipsMeasure sTSignedTwipsMeasure;
        synchronized (monitor()) {
            check_orphaned();
            sTSignedTwipsMeasure = (STSignedTwipsMeasure) get_store().find_attribute_user(LINE$12);
        }
        return sTSignedTwipsMeasure;
    }

    @Override // org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSpacing
    public boolean isSetLine() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(LINE$12) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSpacing
    public void setLine(BigInteger bigInteger) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(LINE$12);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(LINE$12);
            }
            simpleValue.setBigIntegerValue(bigInteger);
        }
    }

    @Override // org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSpacing
    public void xsetLine(STSignedTwipsMeasure sTSignedTwipsMeasure) {
        synchronized (monitor()) {
            check_orphaned();
            STSignedTwipsMeasure sTSignedTwipsMeasure2 = (STSignedTwipsMeasure) get_store().find_attribute_user(LINE$12);
            if (sTSignedTwipsMeasure2 == null) {
                sTSignedTwipsMeasure2 = (STSignedTwipsMeasure) get_store().add_attribute_user(LINE$12);
            }
            sTSignedTwipsMeasure2.set(sTSignedTwipsMeasure);
        }
    }

    @Override // org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSpacing
    public void unsetLine() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(LINE$12);
        }
    }

    @Override // org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSpacing
    public STLineSpacingRule.Enum getLineRule() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(LINERULE$14);
            if (simpleValue == null) {
                return null;
            }
            return (STLineSpacingRule.Enum) simpleValue.getEnumValue();
        }
    }

    @Override // org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSpacing
    public STLineSpacingRule xgetLineRule() {
        STLineSpacingRule sTLineSpacingRule;
        synchronized (monitor()) {
            check_orphaned();
            sTLineSpacingRule = (STLineSpacingRule) get_store().find_attribute_user(LINERULE$14);
        }
        return sTLineSpacingRule;
    }

    @Override // org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSpacing
    public boolean isSetLineRule() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(LINERULE$14) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSpacing
    public void setLineRule(STLineSpacingRule.Enum r4) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(LINERULE$14);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(LINERULE$14);
            }
            simpleValue.setEnumValue(r4);
        }
    }

    @Override // org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSpacing
    public void xsetLineRule(STLineSpacingRule sTLineSpacingRule) {
        synchronized (monitor()) {
            check_orphaned();
            STLineSpacingRule sTLineSpacingRule2 = (STLineSpacingRule) get_store().find_attribute_user(LINERULE$14);
            if (sTLineSpacingRule2 == null) {
                sTLineSpacingRule2 = (STLineSpacingRule) get_store().add_attribute_user(LINERULE$14);
            }
            sTLineSpacingRule2.set(sTLineSpacingRule);
        }
    }

    @Override // org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSpacing
    public void unsetLineRule() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(LINERULE$14);
        }
    }
}
