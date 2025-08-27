package org.springframework.validation;

import java.beans.PropertyEditor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.ConfigurablePropertyAccessor;
import org.springframework.beans.PropertyAccessorUtils;
import org.springframework.beans.PropertyEditorRegistry;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.support.ConvertingPropertyEditorAdapter;
import org.springframework.util.Assert;

/* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/validation/AbstractPropertyBindingResult.class */
public abstract class AbstractPropertyBindingResult extends AbstractBindingResult {
    private transient ConversionService conversionService;

    public abstract ConfigurablePropertyAccessor getPropertyAccessor();

    protected AbstractPropertyBindingResult(String objectName) {
        super(objectName);
    }

    public void initConversion(ConversionService conversionService) {
        Assert.notNull(conversionService, "ConversionService must not be null");
        this.conversionService = conversionService;
        if (getTarget() != null) {
            getPropertyAccessor().setConversionService(conversionService);
        }
    }

    @Override // org.springframework.validation.AbstractBindingResult, org.springframework.validation.BindingResult
    public PropertyEditorRegistry getPropertyEditorRegistry() {
        return getPropertyAccessor();
    }

    @Override // org.springframework.validation.AbstractErrors
    protected String canonicalFieldName(String field) {
        return PropertyAccessorUtils.canonicalPropertyName(field);
    }

    @Override // org.springframework.validation.AbstractBindingResult, org.springframework.validation.AbstractErrors, org.springframework.validation.Errors
    public Class<?> getFieldType(String field) {
        return getPropertyAccessor().getPropertyType(fixedField(field));
    }

    @Override // org.springframework.validation.AbstractBindingResult
    protected Object getActualFieldValue(String field) {
        return getPropertyAccessor().getPropertyValue(field);
    }

    @Override // org.springframework.validation.AbstractBindingResult
    protected Object formatFieldValue(String field, Object value) throws ClassNotFoundException {
        String fixedField = fixedField(field);
        PropertyEditor customEditor = getCustomEditor(fixedField);
        if (customEditor != null) {
            customEditor.setValue(value);
            String textValue = customEditor.getAsText();
            if (textValue != null) {
                return textValue;
            }
        }
        if (this.conversionService != null) {
            TypeDescriptor fieldDesc = getPropertyAccessor().getPropertyTypeDescriptor(fixedField);
            TypeDescriptor strDesc = TypeDescriptor.valueOf(String.class);
            if (fieldDesc != null && this.conversionService.canConvert(fieldDesc, strDesc)) {
                return this.conversionService.convert(value, fieldDesc, strDesc);
            }
        }
        return value;
    }

    protected PropertyEditor getCustomEditor(String fixedField) throws ClassNotFoundException {
        Class<?> targetType = getPropertyAccessor().getPropertyType(fixedField);
        PropertyEditor editor = getPropertyAccessor().findCustomEditor(targetType, fixedField);
        if (editor == null) {
            editor = BeanUtils.findEditorByConvention(targetType);
        }
        return editor;
    }

    @Override // org.springframework.validation.AbstractBindingResult, org.springframework.validation.BindingResult
    public PropertyEditor findEditor(String field, Class<?> valueType) {
        Class<?> valueTypeForLookup = valueType;
        if (valueTypeForLookup == null) {
            valueTypeForLookup = getFieldType(field);
        }
        ConvertingPropertyEditorAdapter convertingPropertyEditorAdapterFindEditor = super.findEditor(field, valueTypeForLookup);
        if (convertingPropertyEditorAdapterFindEditor == null && this.conversionService != null) {
            TypeDescriptor td = null;
            if (field != null) {
                TypeDescriptor ptd = getPropertyAccessor().getPropertyTypeDescriptor(fixedField(field));
                if (valueType == null || valueType.isAssignableFrom(ptd.getType())) {
                    td = ptd;
                }
            }
            if (td == null) {
                td = TypeDescriptor.valueOf(valueTypeForLookup);
            }
            if (this.conversionService.canConvert(TypeDescriptor.valueOf(String.class), td)) {
                convertingPropertyEditorAdapterFindEditor = new ConvertingPropertyEditorAdapter(this.conversionService, td);
            }
        }
        return convertingPropertyEditorAdapterFindEditor;
    }
}
