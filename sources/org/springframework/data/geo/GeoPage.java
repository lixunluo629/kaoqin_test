package org.springframework.data.geo;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.ObjectUtils;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/geo/GeoPage.class */
public class GeoPage<T> extends PageImpl<GeoResult<T>> {
    private static final long serialVersionUID = -5655267379242128600L;
    private final Distance averageDistance;

    public GeoPage(GeoResults<T> results) {
        super(results.getContent());
        this.averageDistance = results.getAverageDistance();
    }

    public GeoPage(GeoResults<T> results, Pageable pageable, long total) {
        super(results.getContent(), pageable, total);
        this.averageDistance = results.getAverageDistance();
    }

    public Distance getAverageDistance() {
        return this.averageDistance;
    }

    @Override // org.springframework.data.domain.PageImpl, org.springframework.data.domain.Chunk
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof GeoPage)) {
            return false;
        }
        GeoPage<?> that = (GeoPage) obj;
        return super.equals(obj) && ObjectUtils.nullSafeEquals(this.averageDistance, that.averageDistance);
    }

    @Override // org.springframework.data.domain.PageImpl, org.springframework.data.domain.Chunk
    public int hashCode() {
        return super.hashCode() + ObjectUtils.nullSafeHashCode(this.averageDistance);
    }
}
