package org.springframework.data.redis.serializer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.oxm.Marshaller;
import org.springframework.oxm.Unmarshaller;
import org.springframework.util.Assert;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/serializer/OxmSerializer.class */
public class OxmSerializer implements InitializingBean, RedisSerializer<Object> {
    private Marshaller marshaller;
    private Unmarshaller unmarshaller;

    public OxmSerializer() {
    }

    public OxmSerializer(Marshaller marshaller, Unmarshaller unmarshaller) {
        this.marshaller = marshaller;
        this.unmarshaller = unmarshaller;
        afterPropertiesSet();
    }

    @Override // org.springframework.beans.factory.InitializingBean
    public void afterPropertiesSet() {
        Assert.notNull(this.marshaller, "non-null marshaller required");
        Assert.notNull(this.unmarshaller, "non-null unmarshaller required");
    }

    public void setMarshaller(Marshaller marshaller) {
        this.marshaller = marshaller;
    }

    public void setUnmarshaller(Unmarshaller unmarshaller) {
        this.unmarshaller = unmarshaller;
    }

    @Override // org.springframework.data.redis.serializer.RedisSerializer
    public Object deserialize(byte[] bytes) throws SerializationException {
        if (SerializationUtils.isEmpty(bytes)) {
            return null;
        }
        try {
            return this.unmarshaller.unmarshal(new StreamSource(new ByteArrayInputStream(bytes)));
        } catch (Exception ex) {
            throw new SerializationException("Cannot deserialize bytes", ex);
        }
    }

    @Override // org.springframework.data.redis.serializer.RedisSerializer
    public byte[] serialize(Object t) throws SerializationException {
        if (t == null) {
            return SerializationUtils.EMPTY_ARRAY;
        }
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        StreamResult result = new StreamResult(stream);
        try {
            this.marshaller.marshal(t, result);
            return stream.toByteArray();
        } catch (Exception ex) {
            throw new SerializationException("Cannot serialize object", ex);
        }
    }
}
