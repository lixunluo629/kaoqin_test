package org.apache.poi.ddf;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import org.apache.poi.util.HexDump;

/* loaded from: poi-3.17.jar:org/apache/poi/ddf/EscherBoolProperty.class */
public class EscherBoolProperty extends EscherSimpleProperty {
    public EscherBoolProperty(short propertyNumber, int value) {
        super(propertyNumber, value);
    }

    public boolean isTrue() {
        return getPropertyValue() != 0;
    }

    @Deprecated
    public boolean isFalse() {
        return !isTrue();
    }

    @Override // org.apache.poi.ddf.EscherSimpleProperty, org.apache.poi.ddf.EscherProperty
    public String toXml(String tab) {
        StringBuilder builder = new StringBuilder();
        builder.append(tab).append("<").append(getClass().getSimpleName()).append(" id=\"0x").append(HexDump.toHex(getId())).append("\" name=\"").append(getName()).append("\" simpleValue=\"").append(getPropertyValue()).append("\" blipId=\"").append(isBlipId()).append("\" value=\"").append(isTrue()).append(SymbolConstants.QUOTES_SYMBOL).append("/>");
        return builder.toString();
    }
}
