package org.springframework.data.repository.query;

import java.util.Iterator;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/repository/query/ParameterAccessor.class */
public interface ParameterAccessor extends Iterable<Object> {
    Pageable getPageable();

    Sort getSort();

    Class<?> getDynamicProjection();

    Object getBindableValue(int i);

    boolean hasBindableNullValue();

    @Override // java.lang.Iterable
    Iterator<Object> iterator();
}
