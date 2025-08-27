package org.springframework.data.domain;

import org.springframework.core.convert.converter.Converter;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/domain/Page.class */
public interface Page<T> extends Slice<T> {
    int getTotalPages();

    long getTotalElements();

    @Override // org.springframework.data.domain.Slice
    <S> Page<S> map(Converter<? super T, ? extends S> converter);
}
