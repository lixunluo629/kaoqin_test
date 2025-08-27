package org.springframework.data.convert;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/convert/EntityWriter.class */
public interface EntityWriter<T, S> {
    void write(T t, S s);
}
