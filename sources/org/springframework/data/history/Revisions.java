package org.springframework.data.history;

import java.lang.Comparable;
import java.lang.Number;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.springframework.util.Assert;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/history/Revisions.class */
public class Revisions<N extends Number & Comparable<N>, T> implements Iterable<Revision<N, T>> {
    private final List<Revision<N, T>> revisions;
    private final boolean latestLast;

    public Revisions(List<? extends Revision<N, T>> revisions) {
        this(revisions, true);
    }

    private Revisions(List<? extends Revision<N, T>> revisions, boolean latestLast) {
        Assert.notNull(revisions, "Revisions must not be null!");
        this.revisions = new ArrayList(revisions);
        this.latestLast = latestLast;
        Collections.sort(this.revisions);
        if (!latestLast) {
            Collections.reverse(this.revisions);
        }
    }

    public Revision<N, T> getLatestRevision() {
        int index = this.latestLast ? this.revisions.size() - 1 : 0;
        return this.revisions.get(index);
    }

    public Revisions<N, T> reverse() {
        List<Revision<N, T>> result = new ArrayList<>(this.revisions);
        Collections.reverse(result);
        return new Revisions<>(result, !this.latestLast);
    }

    @Override // java.lang.Iterable
    public Iterator<Revision<N, T>> iterator() {
        return this.revisions.iterator();
    }

    public List<Revision<N, T>> getContent() {
        return Collections.unmodifiableList(this.revisions);
    }
}
