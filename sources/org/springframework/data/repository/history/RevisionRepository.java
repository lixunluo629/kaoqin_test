package org.springframework.data.repository.history;

import java.io.Serializable;
import java.lang.Comparable;
import java.lang.Number;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.history.Revision;
import org.springframework.data.history.Revisions;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.Repository;

@NoRepositoryBean
/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/repository/history/RevisionRepository.class */
public interface RevisionRepository<T, ID extends Serializable, N extends Number & Comparable<N>> extends Repository<T, ID> {
    Revision<N, T> findLastChangeRevision(ID id);

    Revisions<N, T> findRevisions(ID id);

    Page<Revision<N, T>> findRevisions(ID id, Pageable pageable);

    Revision<N, T> findRevision(ID id, N n);
}
