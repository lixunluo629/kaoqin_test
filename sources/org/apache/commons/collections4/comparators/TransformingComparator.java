package org.apache.commons.collections4.comparators;

import java.io.Serializable;
import java.util.Comparator;
import org.apache.commons.collections4.ComparatorUtils;
import org.apache.commons.collections4.Transformer;

/* loaded from: commons-collections4-4.1.jar:org/apache/commons/collections4/comparators/TransformingComparator.class */
public class TransformingComparator<I, O> implements Comparator<I>, Serializable {
    private static final long serialVersionUID = 3456940356043606220L;
    private final Comparator<O> decorated;
    private final Transformer<? super I, ? extends O> transformer;

    public TransformingComparator(Transformer<? super I, ? extends O> transformer) {
        this(transformer, ComparatorUtils.NATURAL_COMPARATOR);
    }

    public TransformingComparator(Transformer<? super I, ? extends O> transformer, Comparator<O> decorated) {
        this.decorated = decorated;
        this.transformer = transformer;
    }

    @Override // java.util.Comparator
    public int compare(I obj1, I obj2) {
        O value1 = this.transformer.transform(obj1);
        O value2 = this.transformer.transform(obj2);
        return this.decorated.compare(value1, value2);
    }

    public int hashCode() {
        int total = (17 * 37) + (this.decorated == null ? 0 : this.decorated.hashCode());
        return (total * 37) + (this.transformer == null ? 0 : this.transformer.hashCode());
    }

    @Override // java.util.Comparator
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (null != object && object.getClass().equals(getClass())) {
            TransformingComparator<?, ?> comp = (TransformingComparator) object;
            if (null != this.decorated ? this.decorated.equals(comp.decorated) : null == comp.decorated) {
                if (null != this.transformer ? this.transformer.equals(comp.transformer) : null == comp.transformer) {
                    return true;
                }
            }
            return false;
        }
        return false;
    }
}
