package org.springframework.data.domain;

import java.util.Iterator;
import java.util.List;
import org.springframework.core.convert.converter.Converter;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/domain/SliceImpl.class */
public class SliceImpl<T> extends Chunk<T> {
    private static final long serialVersionUID = 867755909294344406L;
    private final boolean hasNext;
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
    public /* bridge */ /* synthetic */ boolean isLast() {
        return super.isLast();
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

    public SliceImpl(List<T> content, Pageable pageable, boolean hasNext) {
        super(content, pageable);
        this.hasNext = hasNext;
        this.pageable = pageable;
    }

    public SliceImpl(List<T> content) {
        this(content, null, false);
    }

    @Override // org.springframework.data.domain.Slice
    public boolean hasNext() {
        return this.hasNext;
    }

    @Override // org.springframework.data.domain.Slice
    public <S> Slice<S> map(Converter<? super T, ? extends S> converter) {
        return new SliceImpl(getConvertedContent(converter), this.pageable, this.hasNext);
    }

    public String toString() {
        String contentType = "UNKNOWN";
        List<T> content = getContent();
        if (content.size() > 0) {
            contentType = content.get(0).getClass().getName();
        }
        return String.format("Slice %d containing %s instances", Integer.valueOf(getNumber()), contentType);
    }

    @Override // org.springframework.data.domain.Chunk
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof SliceImpl)) {
            return false;
        }
        SliceImpl<?> that = (SliceImpl) obj;
        return this.hasNext == that.hasNext && super.equals(obj);
    }

    @Override // org.springframework.data.domain.Chunk
    public int hashCode() {
        int result = 17 + (31 * (this.hasNext ? 1 : 0));
        return result + (31 * super.hashCode());
    }
}
