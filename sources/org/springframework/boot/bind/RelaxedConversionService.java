package org.springframework.boot.bind;

import java.util.EnumSet;
import java.util.Iterator;
import java.util.Locale;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.core.convert.support.GenericConversionService;
import org.springframework.util.Assert;

/* loaded from: spring-boot-1.5.22.RELEASE.jar:org/springframework/boot/bind/RelaxedConversionService.class */
class RelaxedConversionService implements ConversionService {
    private final ConversionService conversionService;
    private final GenericConversionService additionalConverters = new GenericConversionService();

    RelaxedConversionService(ConversionService conversionService) {
        this.conversionService = conversionService;
        DefaultConversionService.addCollectionConverters(this.additionalConverters);
        this.additionalConverters.addConverterFactory(new StringToEnumIgnoringCaseConverterFactory());
        this.additionalConverters.addConverter(new StringToCharArrayConverter());
    }

    @Override // org.springframework.core.convert.ConversionService
    public boolean canConvert(Class<?> sourceType, Class<?> targetType) {
        return (this.conversionService != null && this.conversionService.canConvert(sourceType, targetType)) || this.additionalConverters.canConvert(sourceType, targetType);
    }

    @Override // org.springframework.core.convert.ConversionService
    public boolean canConvert(TypeDescriptor sourceType, TypeDescriptor targetType) {
        return (this.conversionService != null && this.conversionService.canConvert(sourceType, targetType)) || this.additionalConverters.canConvert(sourceType, targetType);
    }

    @Override // org.springframework.core.convert.ConversionService
    public <T> T convert(Object obj, Class<T> cls) {
        Assert.notNull(cls, "The targetType to convert to cannot be null");
        return (T) convert(obj, TypeDescriptor.forObject(obj), TypeDescriptor.valueOf(cls));
    }

    @Override // org.springframework.core.convert.ConversionService
    public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
        if (this.conversionService != null) {
            try {
                return this.conversionService.convert(source, sourceType, targetType);
            } catch (ConversionFailedException e) {
            }
        }
        return this.additionalConverters.convert(source, sourceType, targetType);
    }

    /* loaded from: spring-boot-1.5.22.RELEASE.jar:org/springframework/boot/bind/RelaxedConversionService$StringToEnumIgnoringCaseConverterFactory.class */
    private static class StringToEnumIgnoringCaseConverterFactory implements ConverterFactory<String, Enum> {
        private StringToEnumIgnoringCaseConverterFactory() {
        }

        @Override // org.springframework.core.convert.converter.ConverterFactory
        public <T extends Enum> Converter<String, T> getConverter(Class<T> targetType) {
            Class<T> cls;
            Class<T> superclass = targetType;
            while (true) {
                cls = superclass;
                if (cls == null || cls.isEnum()) {
                    break;
                }
                superclass = cls.getSuperclass();
            }
            Assert.notNull(cls, "The target type " + targetType.getName() + " does not refer to an enum");
            return new StringToEnum(cls);
        }

        /* loaded from: spring-boot-1.5.22.RELEASE.jar:org/springframework/boot/bind/RelaxedConversionService$StringToEnumIgnoringCaseConverterFactory$StringToEnum.class */
        private class StringToEnum<T extends Enum> implements Converter<String, T> {
            private final Class<T> enumType;

            StringToEnum(Class<T> enumType) {
                this.enumType = enumType;
            }

            @Override // org.springframework.core.convert.converter.Converter
            /* renamed from: convert, reason: avoid collision after fix types in other method */
            public T convert2(String source) {
                if (source.isEmpty()) {
                    return null;
                }
                String source2 = source.trim();
                for (T t : EnumSet.allOf(this.enumType)) {
                    RelaxedNames names = new RelaxedNames(t.name().replace('_', '-').toLowerCase(Locale.ENGLISH));
                    Iterator<String> it = names.iterator();
                    while (it.hasNext()) {
                        String name = it.next();
                        if (name.equals(source2)) {
                            return t;
                        }
                    }
                    if (t.name().equalsIgnoreCase(source2)) {
                        return t;
                    }
                }
                throw new IllegalArgumentException("No enum constant " + this.enumType.getCanonicalName() + "." + source2);
            }
        }
    }
}
