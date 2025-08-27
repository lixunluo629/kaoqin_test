package org.springframework.data.domain;

import java.io.Serializable;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/domain/AbstractPageRequest.class */
public abstract class AbstractPageRequest implements Pageable, Serializable {
    private static final long serialVersionUID = 1232825578694716871L;
    private final int page;
    private final int size;

    @Override // org.springframework.data.domain.Pageable
    public abstract Pageable next();

    public abstract Pageable previous();

    @Override // org.springframework.data.domain.Pageable
    public abstract Pageable first();

    public AbstractPageRequest(int page, int size) {
        if (page < 0) {
            throw new IllegalArgumentException("Page index must not be less than zero!");
        }
        if (size < 1) {
            throw new IllegalArgumentException("Page size must not be less than one!");
        }
        this.page = page;
        this.size = size;
    }

    @Override // org.springframework.data.domain.Pageable
    public int getPageSize() {
        return this.size;
    }

    @Override // org.springframework.data.domain.Pageable
    public int getPageNumber() {
        return this.page;
    }

    @Override // org.springframework.data.domain.Pageable
    public int getOffset() {
        return this.page * this.size;
    }

    @Override // org.springframework.data.domain.Pageable
    public boolean hasPrevious() {
        return this.page > 0;
    }

    @Override // org.springframework.data.domain.Pageable
    public Pageable previousOrFirst() {
        return hasPrevious() ? previous() : first();
    }

    public int hashCode() {
        int result = (31 * 1) + this.page;
        return (31 * result) + this.size;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        AbstractPageRequest other = (AbstractPageRequest) obj;
        return this.page == other.page && this.size == other.size;
    }
}
