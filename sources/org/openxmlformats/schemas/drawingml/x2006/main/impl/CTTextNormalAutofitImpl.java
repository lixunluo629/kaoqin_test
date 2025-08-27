package org.openxmlformats.schemas.drawingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.SimpleValue;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;
import org.openxmlformats.schemas.drawingml.x2006.main.CTTextNormalAutofit;
import org.openxmlformats.schemas.drawingml.x2006.main.STTextFontScalePercent;
import org.openxmlformats.schemas.drawingml.x2006.main.STTextSpacingPercent;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/main/impl/CTTextNormalAutofitImpl.class */
public class CTTextNormalAutofitImpl extends XmlComplexContentImpl implements CTTextNormalAutofit {
    private static final QName FONTSCALE$0 = new QName("", "fontScale");
    private static final QName LNSPCREDUCTION$2 = new QName("", "lnSpcReduction");

    public CTTextNormalAutofitImpl(SchemaType schemaType) {
        super(schemaType);
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextNormalAutofit
    public int getFontScale() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(FONTSCALE$0);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(FONTSCALE$0);
            }
            if (simpleValue == null) {
                return 0;
            }
            return simpleValue.getIntValue();
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextNormalAutofit
    public STTextFontScalePercent xgetFontScale() {
        STTextFontScalePercent sTTextFontScalePercent;
        synchronized (monitor()) {
            check_orphaned();
            STTextFontScalePercent sTTextFontScalePercent2 = (STTextFontScalePercent) get_store().find_attribute_user(FONTSCALE$0);
            if (sTTextFontScalePercent2 == null) {
                sTTextFontScalePercent2 = (STTextFontScalePercent) get_default_attribute_value(FONTSCALE$0);
            }
            sTTextFontScalePercent = sTTextFontScalePercent2;
        }
        return sTTextFontScalePercent;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextNormalAutofit
    public boolean isSetFontScale() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(FONTSCALE$0) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextNormalAutofit
    public void setFontScale(int i) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(FONTSCALE$0);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(FONTSCALE$0);
            }
            simpleValue.setIntValue(i);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextNormalAutofit
    public void xsetFontScale(STTextFontScalePercent sTTextFontScalePercent) {
        synchronized (monitor()) {
            check_orphaned();
            STTextFontScalePercent sTTextFontScalePercent2 = (STTextFontScalePercent) get_store().find_attribute_user(FONTSCALE$0);
            if (sTTextFontScalePercent2 == null) {
                sTTextFontScalePercent2 = (STTextFontScalePercent) get_store().add_attribute_user(FONTSCALE$0);
            }
            sTTextFontScalePercent2.set(sTTextFontScalePercent);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextNormalAutofit
    public void unsetFontScale() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(FONTSCALE$0);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextNormalAutofit
    public int getLnSpcReduction() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(LNSPCREDUCTION$2);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(LNSPCREDUCTION$2);
            }
            if (simpleValue == null) {
                return 0;
            }
            return simpleValue.getIntValue();
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextNormalAutofit
    public STTextSpacingPercent xgetLnSpcReduction() {
        STTextSpacingPercent sTTextSpacingPercent;
        synchronized (monitor()) {
            check_orphaned();
            STTextSpacingPercent sTTextSpacingPercent2 = (STTextSpacingPercent) get_store().find_attribute_user(LNSPCREDUCTION$2);
            if (sTTextSpacingPercent2 == null) {
                sTTextSpacingPercent2 = (STTextSpacingPercent) get_default_attribute_value(LNSPCREDUCTION$2);
            }
            sTTextSpacingPercent = sTTextSpacingPercent2;
        }
        return sTTextSpacingPercent;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextNormalAutofit
    public boolean isSetLnSpcReduction() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(LNSPCREDUCTION$2) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextNormalAutofit
    public void setLnSpcReduction(int i) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(LNSPCREDUCTION$2);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(LNSPCREDUCTION$2);
            }
            simpleValue.setIntValue(i);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextNormalAutofit
    public void xsetLnSpcReduction(STTextSpacingPercent sTTextSpacingPercent) {
        synchronized (monitor()) {
            check_orphaned();
            STTextSpacingPercent sTTextSpacingPercent2 = (STTextSpacingPercent) get_store().find_attribute_user(LNSPCREDUCTION$2);
            if (sTTextSpacingPercent2 == null) {
                sTTextSpacingPercent2 = (STTextSpacingPercent) get_store().add_attribute_user(LNSPCREDUCTION$2);
            }
            sTTextSpacingPercent2.set(sTTextSpacingPercent);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextNormalAutofit
    public void unsetLnSpcReduction() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(LNSPCREDUCTION$2);
        }
    }
}
