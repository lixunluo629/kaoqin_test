package org.hamcrest.internal;

import java.util.Iterator;
import org.hamcrest.SelfDescribing;

/* loaded from: hamcrest-core-1.3.jar:org/hamcrest/internal/SelfDescribingValueIterator.class */
public class SelfDescribingValueIterator<T> implements Iterator<SelfDescribing> {
    private Iterator<T> values;

    public SelfDescribingValueIterator(Iterator<T> values) {
        this.values = values;
    }

    @Override // java.util.Iterator
    public boolean hasNext() {
        return this.values.hasNext();
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // java.util.Iterator
    public SelfDescribing next() {
        return new SelfDescribingValue(this.values.next());
    }

    @Override // java.util.Iterator
    public void remove() {
        this.values.remove();
    }
}
