package org.springframework.data.history;

import java.lang.Comparable;
import java.lang.Number;
import org.joda.time.DateTime;
import org.springframework.util.Assert;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/history/Revision.class */
public final class Revision<N extends Number & Comparable<N>, T> implements Comparable<Revision<N, ?>> {
    private final RevisionMetadata<N> metadata;
    private final T entity;

    public Revision(RevisionMetadata<N> metadata, T entity) {
        Assert.notNull(metadata, "Metadata must not be null!");
        Assert.notNull(entity, "Entity must not be null!");
        this.metadata = metadata;
        this.entity = entity;
    }

    public N getRevisionNumber() {
        return (N) this.metadata.getRevisionNumber();
    }

    public DateTime getRevisionDate() {
        return this.metadata.getRevisionDate();
    }

    public T getEntity() {
        return this.entity;
    }

    public RevisionMetadata<N> getMetadata() {
        return this.metadata;
    }

    @Override // java.lang.Comparable
    public int compareTo(Revision<N, ?> that) {
        return ((Comparable) getRevisionNumber()).compareTo(that.getRevisionNumber());
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Revision)) {
            return false;
        }
        Revision<N, T> that = (Revision) obj;
        boolean sameRevisionNumber = this.metadata.getRevisionNumber().equals(that.metadata.getRevisionNumber());
        if (sameRevisionNumber) {
            return this.entity.equals(that.entity);
        }
        return false;
    }

    public int hashCode() {
        int result = 17 + (31 * this.metadata.hashCode());
        return result + (31 * this.entity.hashCode());
    }

    public String toString() {
        return String.format("Revision %s of entity %s - Revision metadata %s", getRevisionNumber(), this.entity, this.metadata);
    }
}
