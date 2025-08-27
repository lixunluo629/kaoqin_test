package org.springframework.data.repository.support;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Map;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.util.MultiValueMap;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/repository/support/RepositoryInvoker.class */
public interface RepositoryInvoker extends RepositoryInvocationInformation {
    <T> T invokeSave(T t);

    <T> T invokeFindOne(Serializable serializable);

    Iterable<Object> invokeFindAll(Pageable pageable);

    Iterable<Object> invokeFindAll(Sort sort);

    void invokeDelete(Serializable serializable);

    @Deprecated
    Object invokeQueryMethod(Method method, Map<String, String[]> map, Pageable pageable, Sort sort);

    Object invokeQueryMethod(Method method, MultiValueMap<String, ? extends Object> multiValueMap, Pageable pageable, Sort sort);
}
