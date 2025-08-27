package org.springframework.validation;

import java.beans.PropertyEditor;
import java.util.Map;
import org.springframework.beans.PropertyEditorRegistry;

/* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/validation/BindingResult.class */
public interface BindingResult extends Errors {
    public static final String MODEL_KEY_PREFIX = BindingResult.class.getName() + ".";

    Object getTarget();

    Map<String, Object> getModel();

    Object getRawFieldValue(String str);

    PropertyEditor findEditor(String str, Class<?> cls);

    PropertyEditorRegistry getPropertyEditorRegistry();

    void addError(ObjectError objectError);

    String[] resolveMessageCodes(String str);

    String[] resolveMessageCodes(String str, String str2);

    void recordSuppressedField(String str);

    String[] getSuppressedFields();
}
