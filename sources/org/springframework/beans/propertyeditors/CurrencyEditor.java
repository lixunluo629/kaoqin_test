package org.springframework.beans.propertyeditors;

import java.beans.PropertyEditorSupport;
import java.util.Currency;

/* loaded from: spring-beans-4.3.25.RELEASE.jar:org/springframework/beans/propertyeditors/CurrencyEditor.class */
public class CurrencyEditor extends PropertyEditorSupport {
    public void setAsText(String text) throws IllegalArgumentException {
        setValue(Currency.getInstance(text));
    }

    public String getAsText() {
        Currency value = (Currency) getValue();
        return value != null ? value.getCurrencyCode() : "";
    }
}
