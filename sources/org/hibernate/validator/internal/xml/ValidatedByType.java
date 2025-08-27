package org.hibernate.validator.internal.xml;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "validated-byType", propOrder = {"value"})
/* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/internal/xml/ValidatedByType.class */
public class ValidatedByType {

    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected List<String> value;

    @XmlAttribute(name = "include-existing-validators")
    protected Boolean includeExistingValidators;

    public List<String> getValue() {
        if (this.value == null) {
            this.value = new ArrayList();
        }
        return this.value;
    }

    public Boolean getIncludeExistingValidators() {
        return this.includeExistingValidators;
    }

    public void setIncludeExistingValidators(Boolean value) {
        this.includeExistingValidators = value;
    }
}
