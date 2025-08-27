package org.springframework.validation;

import java.beans.PropertyEditor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.ConfigurablePropertyAccessor;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.PropertyAccessException;
import org.springframework.beans.PropertyAccessorUtils;
import org.springframework.beans.PropertyBatchUpdateException;
import org.springframework.beans.PropertyEditorRegistry;
import org.springframework.beans.PropertyValue;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.SimpleTypeConverter;
import org.springframework.beans.TypeConverter;
import org.springframework.beans.TypeMismatchException;
import org.springframework.core.MethodParameter;
import org.springframework.core.convert.ConversionService;
import org.springframework.format.Formatter;
import org.springframework.format.support.FormatterPropertyEditorAdapter;
import org.springframework.lang.UsesJava8;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.PatternMatchUtils;
import org.springframework.util.StringUtils;

/* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/validation/DataBinder.class */
public class DataBinder implements PropertyEditorRegistry, TypeConverter {
    public static final String DEFAULT_OBJECT_NAME = "target";
    public static final int DEFAULT_AUTO_GROW_COLLECTION_LIMIT = 256;
    protected static final Log logger = LogFactory.getLog(DataBinder.class);
    private static Class<?> javaUtilOptionalClass;
    private final Object target;
    private final String objectName;
    private AbstractPropertyBindingResult bindingResult;
    private SimpleTypeConverter typeConverter;
    private boolean ignoreUnknownFields;
    private boolean ignoreInvalidFields;
    private boolean autoGrowNestedPaths;
    private int autoGrowCollectionLimit;
    private String[] allowedFields;
    private String[] disallowedFields;
    private String[] requiredFields;
    private ConversionService conversionService;
    private MessageCodesResolver messageCodesResolver;
    private BindingErrorProcessor bindingErrorProcessor;
    private final List<Validator> validators;

    static {
        javaUtilOptionalClass = null;
        try {
            javaUtilOptionalClass = ClassUtils.forName("java.util.Optional", DataBinder.class.getClassLoader());
        } catch (ClassNotFoundException e) {
        }
    }

    public DataBinder(Object target) {
        this(target, DEFAULT_OBJECT_NAME);
    }

    public DataBinder(Object target, String objectName) {
        this.ignoreUnknownFields = true;
        this.ignoreInvalidFields = false;
        this.autoGrowNestedPaths = true;
        this.autoGrowCollectionLimit = 256;
        this.bindingErrorProcessor = new DefaultBindingErrorProcessor();
        this.validators = new ArrayList();
        if (target != null && target.getClass() == javaUtilOptionalClass) {
            this.target = OptionalUnwrapper.unwrap(target);
        } else {
            this.target = target;
        }
        this.objectName = objectName;
    }

    public Object getTarget() {
        return this.target;
    }

    public String getObjectName() {
        return this.objectName;
    }

    public void setAutoGrowNestedPaths(boolean autoGrowNestedPaths) {
        Assert.state(this.bindingResult == null, "DataBinder is already initialized - call setAutoGrowNestedPaths before other configuration methods");
        this.autoGrowNestedPaths = autoGrowNestedPaths;
    }

    public boolean isAutoGrowNestedPaths() {
        return this.autoGrowNestedPaths;
    }

    public void setAutoGrowCollectionLimit(int autoGrowCollectionLimit) {
        Assert.state(this.bindingResult == null, "DataBinder is already initialized - call setAutoGrowCollectionLimit before other configuration methods");
        this.autoGrowCollectionLimit = autoGrowCollectionLimit;
    }

    public int getAutoGrowCollectionLimit() {
        return this.autoGrowCollectionLimit;
    }

    public void initBeanPropertyAccess() {
        Assert.state(this.bindingResult == null, "DataBinder is already initialized - call initBeanPropertyAccess before other configuration methods");
        this.bindingResult = createBeanPropertyBindingResult();
    }

    protected AbstractPropertyBindingResult createBeanPropertyBindingResult() {
        BeanPropertyBindingResult result = new BeanPropertyBindingResult(getTarget(), getObjectName(), isAutoGrowNestedPaths(), getAutoGrowCollectionLimit());
        if (this.conversionService != null) {
            result.initConversion(this.conversionService);
        }
        if (this.messageCodesResolver != null) {
            result.setMessageCodesResolver(this.messageCodesResolver);
        }
        return result;
    }

    public void initDirectFieldAccess() {
        Assert.state(this.bindingResult == null, "DataBinder is already initialized - call initDirectFieldAccess before other configuration methods");
        this.bindingResult = createDirectFieldBindingResult();
    }

    protected AbstractPropertyBindingResult createDirectFieldBindingResult() {
        DirectFieldBindingResult result = new DirectFieldBindingResult(getTarget(), getObjectName(), isAutoGrowNestedPaths());
        if (this.conversionService != null) {
            result.initConversion(this.conversionService);
        }
        if (this.messageCodesResolver != null) {
            result.setMessageCodesResolver(this.messageCodesResolver);
        }
        return result;
    }

    protected AbstractPropertyBindingResult getInternalBindingResult() {
        if (this.bindingResult == null) {
            initBeanPropertyAccess();
        }
        return this.bindingResult;
    }

    protected ConfigurablePropertyAccessor getPropertyAccessor() {
        return getInternalBindingResult().getPropertyAccessor();
    }

    protected SimpleTypeConverter getSimpleTypeConverter() {
        if (this.typeConverter == null) {
            this.typeConverter = new SimpleTypeConverter();
            if (this.conversionService != null) {
                this.typeConverter.setConversionService(this.conversionService);
            }
        }
        return this.typeConverter;
    }

    protected PropertyEditorRegistry getPropertyEditorRegistry() {
        if (getTarget() != null) {
            return getInternalBindingResult().getPropertyAccessor();
        }
        return getSimpleTypeConverter();
    }

    protected TypeConverter getTypeConverter() {
        if (getTarget() != null) {
            return getInternalBindingResult().getPropertyAccessor();
        }
        return getSimpleTypeConverter();
    }

    public BindingResult getBindingResult() {
        return getInternalBindingResult();
    }

    public void setIgnoreUnknownFields(boolean ignoreUnknownFields) {
        this.ignoreUnknownFields = ignoreUnknownFields;
    }

    public boolean isIgnoreUnknownFields() {
        return this.ignoreUnknownFields;
    }

    public void setIgnoreInvalidFields(boolean ignoreInvalidFields) {
        this.ignoreInvalidFields = ignoreInvalidFields;
    }

    public boolean isIgnoreInvalidFields() {
        return this.ignoreInvalidFields;
    }

    public void setAllowedFields(String... allowedFields) {
        this.allowedFields = PropertyAccessorUtils.canonicalPropertyNames(allowedFields);
    }

    public String[] getAllowedFields() {
        return this.allowedFields;
    }

    public void setDisallowedFields(String... disallowedFields) {
        this.disallowedFields = PropertyAccessorUtils.canonicalPropertyNames(disallowedFields);
    }

    public String[] getDisallowedFields() {
        return this.disallowedFields;
    }

    public void setRequiredFields(String... requiredFields) {
        this.requiredFields = PropertyAccessorUtils.canonicalPropertyNames(requiredFields);
        if (logger.isDebugEnabled()) {
            logger.debug("DataBinder requires binding of required fields [" + StringUtils.arrayToCommaDelimitedString(requiredFields) + "]");
        }
    }

    public String[] getRequiredFields() {
        return this.requiredFields;
    }

    @Deprecated
    public void setExtractOldValueForEditor(boolean extractOldValueForEditor) {
        getPropertyAccessor().setExtractOldValueForEditor(extractOldValueForEditor);
    }

    public void setMessageCodesResolver(MessageCodesResolver messageCodesResolver) {
        Assert.state(this.messageCodesResolver == null, "DataBinder is already initialized with MessageCodesResolver");
        this.messageCodesResolver = messageCodesResolver;
        if (this.bindingResult != null && messageCodesResolver != null) {
            this.bindingResult.setMessageCodesResolver(messageCodesResolver);
        }
    }

    public void setBindingErrorProcessor(BindingErrorProcessor bindingErrorProcessor) {
        Assert.notNull(bindingErrorProcessor, "BindingErrorProcessor must not be null");
        this.bindingErrorProcessor = bindingErrorProcessor;
    }

    public BindingErrorProcessor getBindingErrorProcessor() {
        return this.bindingErrorProcessor;
    }

    public void setValidator(Validator validator) {
        assertValidators(validator);
        this.validators.clear();
        this.validators.add(validator);
    }

    private void assertValidators(Validator... validators) {
        Assert.notNull(validators, "Validators required");
        Object target = getTarget();
        for (Validator validator : validators) {
            if (validator != null && target != null && !validator.supports(target.getClass())) {
                throw new IllegalStateException("Invalid target for Validator [" + validator + "]: " + target);
            }
        }
    }

    public void addValidators(Validator... validators) {
        assertValidators(validators);
        this.validators.addAll(Arrays.asList(validators));
    }

    public void replaceValidators(Validator... validators) {
        assertValidators(validators);
        this.validators.clear();
        this.validators.addAll(Arrays.asList(validators));
    }

    public Validator getValidator() {
        if (this.validators.size() > 0) {
            return this.validators.get(0);
        }
        return null;
    }

    public List<Validator> getValidators() {
        return Collections.unmodifiableList(this.validators);
    }

    public void setConversionService(ConversionService conversionService) {
        Assert.state(this.conversionService == null, "DataBinder is already initialized with ConversionService");
        this.conversionService = conversionService;
        if (this.bindingResult != null && conversionService != null) {
            this.bindingResult.initConversion(conversionService);
        }
    }

    public ConversionService getConversionService() {
        return this.conversionService;
    }

    public void addCustomFormatter(Formatter<?> formatter) {
        FormatterPropertyEditorAdapter adapter = new FormatterPropertyEditorAdapter(formatter);
        getPropertyEditorRegistry().registerCustomEditor(adapter.getFieldType(), adapter);
    }

    public void addCustomFormatter(Formatter<?> formatter, String... fields) {
        FormatterPropertyEditorAdapter adapter = new FormatterPropertyEditorAdapter(formatter);
        Class<?> fieldType = adapter.getFieldType();
        if (ObjectUtils.isEmpty((Object[]) fields)) {
            getPropertyEditorRegistry().registerCustomEditor(fieldType, adapter);
            return;
        }
        for (String field : fields) {
            getPropertyEditorRegistry().registerCustomEditor(fieldType, field, adapter);
        }
    }

    public void addCustomFormatter(Formatter<?> formatter, Class<?>... fieldTypes) {
        FormatterPropertyEditorAdapter adapter = new FormatterPropertyEditorAdapter(formatter);
        if (ObjectUtils.isEmpty((Object[]) fieldTypes)) {
            getPropertyEditorRegistry().registerCustomEditor(adapter.getFieldType(), adapter);
            return;
        }
        for (Class<?> fieldType : fieldTypes) {
            getPropertyEditorRegistry().registerCustomEditor(fieldType, adapter);
        }
    }

    @Override // org.springframework.beans.PropertyEditorRegistry
    public void registerCustomEditor(Class<?> requiredType, PropertyEditor propertyEditor) {
        getPropertyEditorRegistry().registerCustomEditor(requiredType, propertyEditor);
    }

    @Override // org.springframework.beans.PropertyEditorRegistry
    public void registerCustomEditor(Class<?> requiredType, String field, PropertyEditor propertyEditor) {
        getPropertyEditorRegistry().registerCustomEditor(requiredType, field, propertyEditor);
    }

    @Override // org.springframework.beans.PropertyEditorRegistry
    public PropertyEditor findCustomEditor(Class<?> requiredType, String propertyPath) {
        return getPropertyEditorRegistry().findCustomEditor(requiredType, propertyPath);
    }

    @Override // org.springframework.beans.TypeConverter
    public <T> T convertIfNecessary(Object obj, Class<T> cls) throws TypeMismatchException {
        return (T) getTypeConverter().convertIfNecessary(obj, cls);
    }

    @Override // org.springframework.beans.TypeConverter
    public <T> T convertIfNecessary(Object obj, Class<T> cls, MethodParameter methodParameter) throws TypeMismatchException {
        return (T) getTypeConverter().convertIfNecessary(obj, cls, methodParameter);
    }

    @Override // org.springframework.beans.TypeConverter
    public <T> T convertIfNecessary(Object obj, Class<T> cls, Field field) throws TypeMismatchException {
        return (T) getTypeConverter().convertIfNecessary(obj, cls, field);
    }

    public void bind(PropertyValues pvs) {
        MutablePropertyValues mpvs = pvs instanceof MutablePropertyValues ? (MutablePropertyValues) pvs : new MutablePropertyValues(pvs);
        doBind(mpvs);
    }

    protected void doBind(MutablePropertyValues mpvs) {
        checkAllowedFields(mpvs);
        checkRequiredFields(mpvs);
        applyPropertyValues(mpvs);
    }

    protected void checkAllowedFields(MutablePropertyValues mpvs) {
        PropertyValue[] pvs = mpvs.getPropertyValues();
        for (PropertyValue pv : pvs) {
            String field = PropertyAccessorUtils.canonicalPropertyName(pv.getName());
            if (!isAllowed(field)) {
                mpvs.removePropertyValue(pv);
                getBindingResult().recordSuppressedField(field);
                if (logger.isDebugEnabled()) {
                    logger.debug("Field [" + field + "] has been removed from PropertyValues and will not be bound, because it has not been found in the list of allowed fields");
                }
            }
        }
    }

    protected boolean isAllowed(String field) {
        String[] allowed = getAllowedFields();
        String[] disallowed = getDisallowedFields();
        return (ObjectUtils.isEmpty((Object[]) allowed) || PatternMatchUtils.simpleMatch(allowed, field)) && (ObjectUtils.isEmpty((Object[]) disallowed) || !PatternMatchUtils.simpleMatch(disallowed, field));
    }

    protected void checkRequiredFields(MutablePropertyValues mpvs) {
        String[] requiredFields = getRequiredFields();
        if (!ObjectUtils.isEmpty((Object[]) requiredFields)) {
            Map<String, PropertyValue> propertyValues = new HashMap<>();
            PropertyValue[] pvs = mpvs.getPropertyValues();
            for (PropertyValue pv : pvs) {
                String canonicalName = PropertyAccessorUtils.canonicalPropertyName(pv.getName());
                propertyValues.put(canonicalName, pv);
            }
            for (String field : requiredFields) {
                PropertyValue pv2 = propertyValues.get(field);
                boolean empty = pv2 == null || pv2.getValue() == null;
                if (!empty) {
                    if (pv2.getValue() instanceof String) {
                        empty = !StringUtils.hasText((String) pv2.getValue());
                    } else if (pv2.getValue() instanceof String[]) {
                        String[] values = (String[]) pv2.getValue();
                        empty = values.length == 0 || !StringUtils.hasText(values[0]);
                    }
                }
                if (empty) {
                    getBindingErrorProcessor().processMissingFieldError(field, getInternalBindingResult());
                    if (pv2 != null) {
                        mpvs.removePropertyValue(pv2);
                        propertyValues.remove(field);
                    }
                }
            }
        }
    }

    protected void applyPropertyValues(MutablePropertyValues mpvs) {
        try {
            getPropertyAccessor().setPropertyValues(mpvs, isIgnoreUnknownFields(), isIgnoreInvalidFields());
        } catch (PropertyBatchUpdateException ex) {
            for (PropertyAccessException pae : ex.getPropertyAccessExceptions()) {
                getBindingErrorProcessor().processPropertyAccessException(pae, getInternalBindingResult());
            }
        }
    }

    public void validate() {
        for (Validator validator : this.validators) {
            validator.validate(getTarget(), getBindingResult());
        }
    }

    public void validate(Object... validationHints) {
        for (Validator validator : getValidators()) {
            if (!ObjectUtils.isEmpty(validationHints) && (validator instanceof SmartValidator)) {
                ((SmartValidator) validator).validate(getTarget(), getBindingResult(), validationHints);
            } else if (validator != null) {
                validator.validate(getTarget(), getBindingResult());
            }
        }
    }

    public Map<?, ?> close() throws BindException {
        if (getBindingResult().hasErrors()) {
            throw new BindException(getBindingResult());
        }
        return getBindingResult().getModel();
    }

    @UsesJava8
    /* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/validation/DataBinder$OptionalUnwrapper.class */
    private static class OptionalUnwrapper {
        private OptionalUnwrapper() {
        }

        public static Object unwrap(Object optionalObject) {
            Optional<?> optional = (Optional) optionalObject;
            if (!optional.isPresent()) {
                return null;
            }
            Object result = optional.get();
            Assert.isTrue(!(result instanceof Optional), "Multi-level Optional usage not supported");
            return result;
        }
    }
}
