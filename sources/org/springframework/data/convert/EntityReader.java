package org.springframework.data.convert;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/convert/EntityReader.class */
public interface EntityReader<T, S> {
    <R extends T> R read(Class<R> cls, S s);
}
