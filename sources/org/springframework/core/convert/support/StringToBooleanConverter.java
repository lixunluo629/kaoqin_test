package org.springframework.core.convert.support;

import java.util.HashSet;
import java.util.Set;
import org.springframework.beans.propertyeditors.CustomBooleanEditor;
import org.springframework.core.convert.converter.Converter;

/* loaded from: spring-core-4.3.25.RELEASE.jar:org/springframework/core/convert/support/StringToBooleanConverter.class */
final class StringToBooleanConverter implements Converter<String, Boolean> {
    private static final Set<String> trueValues = new HashSet(4);
    private static final Set<String> falseValues = new HashSet(4);

    StringToBooleanConverter() {
    }

    static {
        trueValues.add("true");
        trueValues.add(CustomBooleanEditor.VALUE_ON);
        trueValues.add(CustomBooleanEditor.VALUE_YES);
        trueValues.add("1");
        falseValues.add("false");
        falseValues.add(CustomBooleanEditor.VALUE_OFF);
        falseValues.add("no");
        falseValues.add("0");
    }

    @Override // org.springframework.core.convert.converter.Converter
    public Boolean convert(String source) {
        String value = source.trim();
        if ("".equals(value)) {
            return null;
        }
        String value2 = value.toLowerCase();
        if (trueValues.contains(value2)) {
            return Boolean.TRUE;
        }
        if (falseValues.contains(value2)) {
            return Boolean.FALSE;
        }
        throw new IllegalArgumentException("Invalid boolean value '" + source + "'");
    }
}
