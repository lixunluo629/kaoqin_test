package org.springframework.data.querydsl;

import com.querydsl.core.types.OrderSpecifier;
import org.springframework.data.domain.AbstractPageRequest;
import org.springframework.data.domain.Pageable;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/querydsl/QPageRequest.class */
public class QPageRequest extends AbstractPageRequest {
    private static final long serialVersionUID = 7529171950267879273L;
    private final QSort sort;

    public QPageRequest(int page, int size) {
        this(page, size, (QSort) null);
    }

    public QPageRequest(int page, int size, OrderSpecifier<?>... orderSpecifiers) {
        this(page, size, new QSort(orderSpecifiers));
    }

    public QPageRequest(int page, int size, QSort sort) {
        super(page, size);
        this.sort = sort;
    }

    @Override // org.springframework.data.domain.Pageable
    public QSort getSort() {
        return this.sort;
    }

    @Override // org.springframework.data.domain.AbstractPageRequest, org.springframework.data.domain.Pageable
    public Pageable next() {
        return new QPageRequest(getPageNumber() + 1, getPageSize(), getSort());
    }

    @Override // org.springframework.data.domain.AbstractPageRequest
    public Pageable previous() {
        return new QPageRequest(getPageNumber() - 1, getPageSize(), getSort());
    }

    @Override // org.springframework.data.domain.AbstractPageRequest, org.springframework.data.domain.Pageable
    public Pageable first() {
        return new QPageRequest(0, getPageSize(), getSort());
    }
}
