package org.springframework.core.serializer;

import java.io.IOException;
import java.io.InputStream;

/* loaded from: spring-core-4.3.25.RELEASE.jar:org/springframework/core/serializer/Deserializer.class */
public interface Deserializer<T> {
    T deserialize(InputStream inputStream) throws IOException;
}
