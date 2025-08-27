package org.apache.commons.lang.p000enum;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.util.List;
import org.apache.commons.lang.ClassUtils;
import org.springframework.beans.PropertyAccessor;

/* loaded from: commons-lang-2.6.jar:org/apache/commons/lang/enum/ValuedEnum.class */
public abstract class ValuedEnum extends Enum {
    private static final long serialVersionUID = -7129650521543789085L;
    private final int iValue;

    protected ValuedEnum(String name, int value) {
        super(name);
        this.iValue = value;
    }

    protected static Enum getEnum(Class enumClass, int value) throws ClassNotFoundException {
        if (enumClass == null) {
            throw new IllegalArgumentException("The Enum Class must not be null");
        }
        List<ValuedEnum> list = Enum.getEnumList(enumClass);
        for (ValuedEnum enumeration : list) {
            if (enumeration.getValue() == value) {
                return enumeration;
            }
        }
        return null;
    }

    public final int getValue() {
        return this.iValue;
    }

    @Override // org.apache.commons.lang.p000enum.Enum, java.lang.Comparable
    public int compareTo(Object other) {
        return this.iValue - ((ValuedEnum) other).iValue;
    }

    @Override // org.apache.commons.lang.p000enum.Enum
    public String toString() {
        if (this.iToString == null) {
            String shortName = ClassUtils.getShortClassName(getEnumClass());
            this.iToString = new StringBuffer().append(shortName).append(PropertyAccessor.PROPERTY_KEY_PREFIX).append(getName()).append(SymbolConstants.EQUAL_SYMBOL).append(getValue()).append("]").toString();
        }
        return this.iToString;
    }
}
