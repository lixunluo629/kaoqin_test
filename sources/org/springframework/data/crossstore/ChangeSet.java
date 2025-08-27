package org.springframework.data.crossstore;

import java.util.Map;
import org.springframework.core.convert.ConversionService;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/crossstore/ChangeSet.class */
public interface ChangeSet {
    <T> T get(String str, Class<T> cls, ConversionService conversionService);

    void set(String str, Object obj);

    Map<String, Object> getValues();

    Object removeProperty(String str);
}
