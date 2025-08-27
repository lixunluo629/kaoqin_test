package org.hibernate.validator.internal.xml;

import javax.validation.executable.ExecutableType;
import javax.xml.bind.annotation.adapters.XmlAdapter;

/* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/internal/xml/Adapter1.class */
public class Adapter1 extends XmlAdapter<String, ExecutableType> {
    public ExecutableType unmarshal(String value) {
        return ExecutableType.valueOf(value);
    }

    public String marshal(ExecutableType value) {
        if (value == null) {
            return null;
        }
        return value.toString();
    }
}
