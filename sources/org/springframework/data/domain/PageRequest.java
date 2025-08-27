package org.springframework.data.domain;

import org.springframework.data.domain.Sort;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/domain/PageRequest.class */
public class PageRequest extends AbstractPageRequest {
    private static final long serialVersionUID = -4541509938956089562L;
    private final Sort sort;

    public PageRequest(int page, int size) {
        this(page, size, null);
    }

    public PageRequest(int page, int size, Sort.Direction direction, String... properties) {
        this(page, size, new Sort(direction, properties));
    }

    public PageRequest(int page, int size, Sort sort) {
        super(page, size);
        this.sort = sort;
    }

    @Override // org.springframework.data.domain.Pageable
    public Sort getSort() {
        return this.sort;
    }

    @Override // org.springframework.data.domain.AbstractPageRequest, org.springframework.data.domain.Pageable
    public Pageable next() {
        return new PageRequest(getPageNumber() + 1, getPageSize(), getSort());
    }

    @Override // org.springframework.data.domain.AbstractPageRequest
    public PageRequest previous() {
        return getPageNumber() == 0 ? this : new PageRequest(getPageNumber() - 1, getPageSize(), getSort());
    }

    @Override // org.springframework.data.domain.AbstractPageRequest, org.springframework.data.domain.Pageable
    public Pageable first() {
        return new PageRequest(0, getPageSize(), getSort());
    }

    @Override // org.springframework.data.domain.AbstractPageRequest
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof PageRequest)) {
            return false;
        }
        PageRequest that = (PageRequest) obj;
        boolean sortEqual = this.sort == null ? that.sort == null : this.sort.equals(that.sort);
        return super.equals(that) && sortEqual;
    }

    @Override // org.springframework.data.domain.AbstractPageRequest
    public int hashCode() {
        return (31 * super.hashCode()) + (null == this.sort ? 0 : this.sort.hashCode());
    }

    public String toString() {
        Object[] objArr = new Object[3];
        objArr[0] = Integer.valueOf(getPageNumber());
        objArr[1] = Integer.valueOf(getPageSize());
        objArr[2] = this.sort == null ? null : this.sort.toString();
        return String.format("Page request [number: %d, size %d, sort: %s]", objArr);
    }
}
