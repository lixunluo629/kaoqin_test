package org.springframework.data.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.springframework.core.convert.converter.Converter;
import org.springframework.util.Assert;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/domain/Chunk.class */
abstract class Chunk<T> implements Slice<T>, Serializable {
    private static final long serialVersionUID = 867755909294344406L;
    private final List<T> content = new ArrayList();
    private final Pageable pageable;

    public Chunk(List<T> content, Pageable pageable) {
        Assert.notNull(content, "Content must not be null!");
        this.content.addAll(content);
        this.pageable = pageable;
    }

    @Override // org.springframework.data.domain.Slice
    public int getNumber() {
        if (this.pageable == null) {
            return 0;
        }
        return this.pageable.getPageNumber();
    }

    @Override // org.springframework.data.domain.Slice
    public int getSize() {
        if (this.pageable == null) {
            return 0;
        }
        return this.pageable.getPageSize();
    }

    @Override // org.springframework.data.domain.Slice
    public int getNumberOfElements() {
        return this.content.size();
    }

    @Override // org.springframework.data.domain.Slice
    public boolean hasPrevious() {
        return getNumber() > 0;
    }

    @Override // org.springframework.data.domain.Slice
    public boolean isFirst() {
        return !hasPrevious();
    }

    @Override // org.springframework.data.domain.Slice
    public boolean isLast() {
        return !hasNext();
    }

    @Override // org.springframework.data.domain.Slice
    public Pageable nextPageable() {
        if (hasNext()) {
            return this.pageable.next();
        }
        return null;
    }

    @Override // org.springframework.data.domain.Slice
    public Pageable previousPageable() {
        if (hasPrevious()) {
            return this.pageable.previousOrFirst();
        }
        return null;
    }

    @Override // org.springframework.data.domain.Slice
    public boolean hasContent() {
        return !this.content.isEmpty();
    }

    @Override // org.springframework.data.domain.Slice
    public List<T> getContent() {
        return Collections.unmodifiableList(this.content);
    }

    @Override // org.springframework.data.domain.Slice
    public Sort getSort() {
        if (this.pageable == null) {
            return null;
        }
        return this.pageable.getSort();
    }

    @Override // java.lang.Iterable
    public Iterator<T> iterator() {
        return this.content.iterator();
    }

    protected <S> List<S> getConvertedContent(Converter<? super T, ? extends S> converter) {
        Assert.notNull(converter, "Converter must not be null!");
        List<S> result = new ArrayList<>(this.content.size());
        Iterator<T> it = iterator();
        while (it.hasNext()) {
            T element = it.next();
            result.add(converter.convert(element));
        }
        return result;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Chunk)) {
            return false;
        }
        Chunk<?> that = (Chunk) obj;
        boolean contentEqual = this.content.equals(that.content);
        boolean pageableEqual = this.pageable == null ? that.pageable == null : this.pageable.equals(that.pageable);
        return contentEqual && pageableEqual;
    }

    public int hashCode() {
        int result = 17 + (31 * (this.pageable == null ? 0 : this.pageable.hashCode()));
        return result + (31 * this.content.hashCode());
    }
}
