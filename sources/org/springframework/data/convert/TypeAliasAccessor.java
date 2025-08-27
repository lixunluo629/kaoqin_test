package org.springframework.data.convert;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/convert/TypeAliasAccessor.class */
public interface TypeAliasAccessor<S> {
    Object readAliasFrom(S s);

    void writeTypeTo(S s, Object obj);
}
