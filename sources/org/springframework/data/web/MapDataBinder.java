package org.springframework.data.web;

import java.beans.PropertyDescriptor;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Generated;
import lombok.NonNull;
import org.springframework.beans.AbstractPropertyAccessor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.ConfigurablePropertyAccessor;
import org.springframework.beans.NotWritablePropertyException;
import org.springframework.context.expression.MapAccessor;
import org.springframework.core.CollectionFactory;
import org.springframework.core.MethodParameter;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.data.mapping.PropertyPath;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.data.util.TypeInformation;
import org.springframework.expression.AccessException;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.EvaluationException;
import org.springframework.expression.Expression;
import org.springframework.expression.TypedValue;
import org.springframework.expression.spel.SpelEvaluationException;
import org.springframework.expression.spel.SpelParserConfiguration;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.SimpleEvaluationContext;
import org.springframework.util.Assert;
import org.springframework.web.bind.WebDataBinder;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/web/MapDataBinder.class */
class MapDataBinder extends WebDataBinder {
    private final Class<?> type;
    private final ConversionService conversionService;

    public MapDataBinder(Class<?> type, ConversionService conversionService) {
        super(new HashMap());
        this.type = type;
        this.conversionService = conversionService;
    }

    @Override // org.springframework.validation.DataBinder
    public Map<String, Object> getTarget() {
        return (Map) super.getTarget();
    }

    @Override // org.springframework.validation.DataBinder
    protected ConfigurablePropertyAccessor getPropertyAccessor() {
        return new MapPropertyAccessor(this.type, getTarget(), this.conversionService);
    }

    /* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/web/MapDataBinder$MapPropertyAccessor.class */
    private static class MapPropertyAccessor extends AbstractPropertyAccessor {
        private static final SpelExpressionParser PARSER = new SpelExpressionParser(new SpelParserConfiguration(false, true));

        @NonNull
        private final Class<?> type;

        @NonNull
        private final Map<String, Object> map;

        @NonNull
        private final ConversionService conversionService;

        @Generated
        public MapPropertyAccessor(@NonNull Class<?> type, @NonNull Map<String, Object> map, @NonNull ConversionService conversionService) {
            if (type == null) {
                throw new IllegalArgumentException("type is marked @NonNull but is null");
            }
            if (map == null) {
                throw new IllegalArgumentException("map is marked @NonNull but is null");
            }
            if (conversionService == null) {
                throw new IllegalArgumentException("conversionService is marked @NonNull but is null");
            }
            this.type = type;
            this.map = map;
            this.conversionService = conversionService;
        }

        @Override // org.springframework.beans.PropertyAccessor
        public boolean isReadableProperty(String propertyName) {
            throw new UnsupportedOperationException();
        }

        @Override // org.springframework.beans.PropertyAccessor
        public boolean isWritableProperty(String propertyName) {
            try {
                return getPropertyPath(propertyName) != null;
            } catch (PropertyReferenceException e) {
                return false;
            }
        }

        @Override // org.springframework.beans.PropertyAccessor
        public TypeDescriptor getPropertyTypeDescriptor(String propertyName) throws BeansException {
            throw new UnsupportedOperationException();
        }

        @Override // org.springframework.beans.AbstractPropertyAccessor, org.springframework.beans.PropertyAccessor
        public Object getPropertyValue(String propertyName) throws BeansException {
            throw new UnsupportedOperationException();
        }

        @Override // org.springframework.beans.AbstractPropertyAccessor, org.springframework.beans.PropertyAccessor
        public void setPropertyValue(String propertyName, Object value) throws BeansException, EvaluationException {
            if (!isWritableProperty(propertyName)) {
                throw new NotWritablePropertyException(this.type, propertyName);
            }
            PropertyPath leafProperty = getPropertyPath(propertyName).getLeafProperty();
            TypeInformation<?> owningType = leafProperty.getOwningType();
            TypeInformation<?> propertyType = owningType.getProperty(leafProperty.getSegment());
            if (conversionRequired(value, (propertyName.endsWith("]") ? propertyType.getActualType() : propertyType).getType())) {
                PropertyDescriptor descriptor = BeanUtils.getPropertyDescriptor(owningType.getType(), leafProperty.getSegment());
                MethodParameter methodParameter = new MethodParameter(descriptor.getReadMethod(), -1);
                TypeDescriptor typeDescriptor = TypeDescriptor.nested(methodParameter, 0);
                value = this.conversionService.convert(value, TypeDescriptor.forObject(value), typeDescriptor);
            }
            EvaluationContext context = SimpleEvaluationContext.forPropertyAccessors(new PropertyTraversingMapAccessor(this.type, this.conversionService)).withConversionService(this.conversionService).withRootObject(this.map).build();
            Expression expression = PARSER.parseExpression(propertyName);
            try {
                expression.setValue(context, value);
            } catch (SpelEvaluationException o_O) {
                throw new NotWritablePropertyException(this.type, propertyName, "Could not write property!", o_O);
            }
        }

        private boolean conversionRequired(Object source, Class<?> targetType) {
            if (source == null || targetType.isInstance(source)) {
                return false;
            }
            return this.conversionService.canConvert(source.getClass(), targetType);
        }

        private PropertyPath getPropertyPath(String propertyName) {
            String plainPropertyPath = propertyName.replaceAll("\\[.*?\\]", "");
            return PropertyPath.from(plainPropertyPath, this.type);
        }

        /* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/web/MapDataBinder$MapPropertyAccessor$PropertyTraversingMapAccessor.class */
        private static final class PropertyTraversingMapAccessor extends MapAccessor {
            private final ConversionService conversionService;
            private Class<?> type;

            public PropertyTraversingMapAccessor(Class<?> type, ConversionService conversionService) {
                Assert.notNull(type, "Type must not be null!");
                Assert.notNull(conversionService, "ConversionService must not be null!");
                this.type = type;
                this.conversionService = conversionService;
            }

            @Override // org.springframework.context.expression.MapAccessor, org.springframework.expression.PropertyAccessor
            public boolean canRead(EvaluationContext context, Object target, String name) throws AccessException {
                return true;
            }

            @Override // org.springframework.context.expression.MapAccessor, org.springframework.expression.PropertyAccessor
            public TypedValue read(EvaluationContext context, Object target, String name) throws AccessException {
                PropertyPath path = PropertyPath.from(name, this.type);
                try {
                    try {
                        TypedValue typedValue = super.read(context, target, name);
                        this.type = path.getType();
                        return typedValue;
                    } catch (AccessException e) {
                        Object emptyResult = path.isCollection() ? CollectionFactory.createCollection(List.class, 0) : CollectionFactory.createMap(Map.class, 0);
                        ((Map) target).put(name, emptyResult);
                        TypedValue typedValue2 = new TypedValue(emptyResult, getDescriptor(path, emptyResult));
                        this.type = path.getType();
                        return typedValue2;
                    }
                } catch (Throwable th) {
                    this.type = path.getType();
                    throw th;
                }
            }

            private TypeDescriptor getDescriptor(PropertyPath path, Object emptyValue) {
                TypeDescriptor typeDescriptorValueOf;
                Class<?> actualPropertyType = path.getType();
                if (this.conversionService.canConvert(String.class, actualPropertyType)) {
                    typeDescriptorValueOf = TypeDescriptor.valueOf(String.class);
                } else {
                    typeDescriptorValueOf = TypeDescriptor.valueOf(HashMap.class);
                }
                TypeDescriptor valueDescriptor = typeDescriptorValueOf;
                return path.isCollection() ? TypeDescriptor.collection(emptyValue.getClass(), valueDescriptor) : TypeDescriptor.map(emptyValue.getClass(), TypeDescriptor.valueOf(String.class), valueDescriptor);
            }
        }
    }
}
