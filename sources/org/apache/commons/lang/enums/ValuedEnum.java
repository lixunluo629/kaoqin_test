package org.apache.commons.lang.enums;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import org.apache.commons.lang.ClassUtils;
import org.springframework.beans.PropertyAccessor;

/* loaded from: commons-lang-2.6.jar:org/apache/commons/lang/enums/ValuedEnum.class */
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

    @Override // org.apache.commons.lang.enums.Enum, java.lang.Comparable
    public int compareTo(Object other) {
        if (other == this) {
            return 0;
        }
        if (other.getClass() != getClass()) {
            if (other.getClass().getName().equals(getClass().getName())) {
                return this.iValue - getValueInOtherClassLoader(other);
            }
            throw new ClassCastException(new StringBuffer().append("Different enum class '").append(ClassUtils.getShortClassName(other.getClass())).append("'").toString());
        }
        return this.iValue - ((ValuedEnum) other).iValue;
    }

    private int getValueInOtherClassLoader(Object other) throws NoSuchMethodException, SecurityException {
        try {
            Method mth = other.getClass().getMethod("getValue", null);
            Integer value = (Integer) mth.invoke(other, null);
            return value.intValue();
        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            throw new IllegalStateException("This should not happen");
        }
    }

    @Override // org.apache.commons.lang.enums.Enum
    public String toString() {
        if (this.iToString == null) {
            String shortName = ClassUtils.getShortClassName(getEnumClass());
            this.iToString = new StringBuffer().append(shortName).append(PropertyAccessor.PROPERTY_KEY_PREFIX).append(getName()).append(SymbolConstants.EQUAL_SYMBOL).append(getValue()).append("]").toString();
        }
        return this.iToString;
    }
}
