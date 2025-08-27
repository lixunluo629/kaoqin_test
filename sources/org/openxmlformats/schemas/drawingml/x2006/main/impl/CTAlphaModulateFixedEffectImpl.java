package org.openxmlformats.schemas.drawingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.SimpleValue;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;
import org.openxmlformats.schemas.drawingml.x2006.main.CTAlphaModulateFixedEffect;
import org.openxmlformats.schemas.drawingml.x2006.main.STPositivePercentage;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/main/impl/CTAlphaModulateFixedEffectImpl.class */
public class CTAlphaModulateFixedEffectImpl extends XmlComplexContentImpl implements CTAlphaModulateFixedEffect {
    private static final QName AMT$0 = new QName("", "amt");

    public CTAlphaModulateFixedEffectImpl(SchemaType schemaType) {
        super(schemaType);
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTAlphaModulateFixedEffect
    public int getAmt() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(AMT$0);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(AMT$0);
            }
            if (simpleValue == null) {
                return 0;
            }
            return simpleValue.getIntValue();
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTAlphaModulateFixedEffect
    public STPositivePercentage xgetAmt() {
        STPositivePercentage sTPositivePercentage;
        synchronized (monitor()) {
            check_orphaned();
            STPositivePercentage sTPositivePercentage2 = (STPositivePercentage) get_store().find_attribute_user(AMT$0);
            if (sTPositivePercentage2 == null) {
                sTPositivePercentage2 = (STPositivePercentage) get_default_attribute_value(AMT$0);
            }
            sTPositivePercentage = sTPositivePercentage2;
        }
        return sTPositivePercentage;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTAlphaModulateFixedEffect
    public boolean isSetAmt() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(AMT$0) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTAlphaModulateFixedEffect
    public void setAmt(int i) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(AMT$0);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(AMT$0);
            }
            simpleValue.setIntValue(i);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTAlphaModulateFixedEffect
    public void xsetAmt(STPositivePercentage sTPositivePercentage) {
        synchronized (monitor()) {
            check_orphaned();
            STPositivePercentage sTPositivePercentage2 = (STPositivePercentage) get_store().find_attribute_user(AMT$0);
            if (sTPositivePercentage2 == null) {
                sTPositivePercentage2 = (STPositivePercentage) get_store().add_attribute_user(AMT$0);
            }
            sTPositivePercentage2.set(sTPositivePercentage);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTAlphaModulateFixedEffect
    public void unsetAmt() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(AMT$0);
        }
    }
}
