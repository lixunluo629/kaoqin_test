package org.springframework.core.serializer;

import java.io.IOException;
import java.io.OutputStream;

/* loaded from: spring-core-4.3.25.RELEASE.jar:org/springframework/core/serializer/Serializer.class */
public interface Serializer<T> {
    void serialize(T t, OutputStream outputStream) throws IOException;
}
