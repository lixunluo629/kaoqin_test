package org.springframework.data.domain;

import java.util.List;
import org.springframework.core.convert.converter.Converter;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/domain/Slice.class */
public interface Slice<T> extends Iterable<T> {
    int getNumber();

    int getSize();

    int getNumberOfElements();

    List<T> getContent();

    boolean hasContent();

    Sort getSort();

    boolean isFirst();

    boolean isLast();

    boolean hasNext();

    boolean hasPrevious();

    Pageable nextPageable();

    Pageable previousPageable();

    <S> Slice<S> map(Converter<? super T, ? extends S> converter);
}
