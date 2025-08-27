package org.springframework.data.domain;

import java.util.Iterator;
import java.util.List;
import org.springframework.core.convert.converter.Converter;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/domain/PageImpl.class */
public class PageImpl<T> extends Chunk<T> implements Page<T> {
    private static final long serialVersionUID = 867755909294344406L;
    private final long total;
    private final Pageable pageable;

    @Override // org.springframework.data.domain.Chunk, java.lang.Iterable
    public /* bridge */ /* synthetic */ Iterator iterator() {
        return super.iterator();
    }

    @Override // org.springframework.data.domain.Chunk, org.springframework.data.domain.Slice
    public /* bridge */ /* synthetic */ Sort getSort() {
        return super.getSort();
    }

    @Override // org.springframework.data.domain.Chunk, org.springframework.data.domain.Slice
    public /* bridge */ /* synthetic */ List getContent() {
        return super.getContent();
    }

    @Override // org.springframework.data.domain.Chunk, org.springframework.data.domain.Slice
    public /* bridge */ /* synthetic */ boolean hasContent() {
        return super.hasContent();
    }

    @Override // org.springframework.data.domain.Chunk, org.springframework.data.domain.Slice
    public /* bridge */ /* synthetic */ Pageable previousPageable() {
        return super.previousPageable();
    }

    @Override // org.springframework.data.domain.Chunk, org.springframework.data.domain.Slice
    public /* bridge */ /* synthetic */ Pageable nextPageable() {
        return super.nextPageable();
    }

    @Override // org.springframework.data.domain.Chunk, org.springframework.data.domain.Slice
    public /* bridge */ /* synthetic */ boolean isFirst() {
        return super.isFirst();
    }

    @Override // org.springframework.data.domain.Chunk, org.springframework.data.domain.Slice
    public /* bridge */ /* synthetic */ boolean hasPrevious() {
        return super.hasPrevious();
    }

    @Override // org.springframework.data.domain.Chunk, org.springframework.data.domain.Slice
    public /* bridge */ /* synthetic */ int getNumberOfElements() {
        return super.getNumberOfElements();
    }

    @Override // org.springframework.data.domain.Chunk, org.springframework.data.domain.Slice
    public /* bridge */ /* synthetic */ int getSize() {
        return super.getSize();
    }

    @Override // org.springframework.data.domain.Chunk, org.springframework.data.domain.Slice
    public /* bridge */ /* synthetic */ int getNumber() {
        return super.getNumber();
    }

    public PageImpl(List<T> content, Pageable pageable, long total) {
        super(content, pageable);
        this.pageable = pageable;
        this.total = (content.isEmpty() || pageable == null || ((long) (pageable.getOffset() + pageable.getPageSize())) <= total) ? total : pageable.getOffset() + content.size();
    }

    public PageImpl(List<T> content) {
        this(content, null, null == content ? 0L : content.size());
    }

    @Override // org.springframework.data.domain.Page
    public int getTotalPages() {
        if (getSize() == 0) {
            return 1;
        }
        return (int) Math.ceil(this.total / getSize());
    }

    @Override // org.springframework.data.domain.Page
    public long getTotalElements() {
        return this.total;
    }

    @Override // org.springframework.data.domain.Slice
    public boolean hasNext() {
        return getNumber() + 1 < getTotalPages();
    }

    @Override // org.springframework.data.domain.Chunk, org.springframework.data.domain.Slice
    public boolean isLast() {
        return !hasNext();
    }

    @Override // org.springframework.data.domain.Slice
    public <S> Page<S> map(Converter<? super T, ? extends S> converter) {
        return new PageImpl(getConvertedContent(converter), this.pageable, this.total);
    }

    public String toString() {
        String contentType = "UNKNOWN";
        List<T> content = getContent();
        if (content.size() > 0) {
            contentType = content.get(0).getClass().getName();
        }
        return String.format("Page %s of %d containing %s instances", Integer.valueOf(getNumber() + 1), Integer.valueOf(getTotalPages()), contentType);
    }

    @Override // org.springframework.data.domain.Chunk
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof PageImpl)) {
            return false;
        }
        PageImpl<?> that = (PageImpl) obj;
        return this.total == that.total && super.equals(obj);
    }

    @Override // org.springframework.data.domain.Chunk
    public int hashCode() {
        int result = 17 + (31 * ((int) (this.total ^ (this.total >>> 32))));
        return result + (31 * super.hashCode());
    }
}
