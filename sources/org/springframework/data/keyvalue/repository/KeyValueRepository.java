package org.springframework.data.keyvalue.repository;

import java.io.Serializable;
import org.springframework.data.repository.PagingAndSortingRepository;

/* loaded from: spring-data-keyvalue-1.2.23.RELEASE.jar:org/springframework/data/keyvalue/repository/KeyValueRepository.class */
public interface KeyValueRepository<T, ID extends Serializable> extends PagingAndSortingRepository<T, ID> {
}
