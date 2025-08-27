package org.springframework.data.redis.serializer;

import java.nio.charset.Charset;
import org.springframework.beans.BeansException;
import org.springframework.beans.TypeConverter;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.util.Assert;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/serializer/GenericToStringSerializer.class */
public class GenericToStringSerializer<T> implements RedisSerializer<T>, BeanFactoryAware {
    private final Charset charset;
    private GenericToStringSerializer<T>.Converter converter;
    private Class<T> type;

    public GenericToStringSerializer(Class<T> type) {
        this(type, Charset.forName("UTF8"));
    }

    public GenericToStringSerializer(Class<T> type, Charset charset) {
        this.converter = new Converter(new DefaultConversionService());
        Assert.notNull(type, "tyoe must not be null!");
        this.type = type;
        this.charset = charset;
    }

    public void setConversionService(ConversionService conversionService) {
        Assert.notNull(conversionService, "non null conversion service required");
        this.converter = new Converter(conversionService);
    }

    public void setTypeConverter(TypeConverter typeConverter) {
        Assert.notNull(typeConverter, "non null type converter required");
        this.converter = new Converter(typeConverter);
    }

    @Override // org.springframework.data.redis.serializer.RedisSerializer
    public T deserialize(byte[] bArr) {
        if (bArr == null) {
            return null;
        }
        return (T) this.converter.convert(new String(bArr, this.charset), this.type);
    }

    @Override // org.springframework.data.redis.serializer.RedisSerializer
    public byte[] serialize(T object) {
        if (object == null) {
            return null;
        }
        String string = (String) this.converter.convert(object, String.class);
        return string.getBytes(this.charset);
    }

    @Override // org.springframework.beans.factory.BeanFactoryAware
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        if (this.converter == null && (beanFactory instanceof ConfigurableBeanFactory)) {
            ConfigurableBeanFactory cFB = (ConfigurableBeanFactory) beanFactory;
            ConversionService conversionService = cFB.getConversionService();
            this.converter = conversionService != null ? new Converter(conversionService) : new Converter(cFB.getTypeConverter());
        }
    }

    /* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/serializer/GenericToStringSerializer$Converter.class */
    private class Converter {
        private final ConversionService conversionService;
        private final TypeConverter typeConverter;

        public Converter(ConversionService conversionService) {
            this.conversionService = conversionService;
            this.typeConverter = null;
        }

        public Converter(TypeConverter typeConverter) {
            this.conversionService = null;
            this.typeConverter = typeConverter;
        }

        <E> E convert(Object obj, Class<E> cls) {
            if (this.conversionService != null) {
                return (E) this.conversionService.convert(obj, cls);
            }
            return (E) this.typeConverter.convertIfNecessary(obj, cls);
        }
    }
}
