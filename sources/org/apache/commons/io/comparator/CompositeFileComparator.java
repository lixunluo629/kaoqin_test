package org.apache.commons.io.comparator;

import java.io.File;
import java.io.Serializable;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/* loaded from: commons-io-2.18.0.jar:org/apache/commons/io/comparator/CompositeFileComparator.class */
public class CompositeFileComparator extends AbstractFileComparator implements Serializable {
    private static final Comparator<?>[] EMPTY_COMPARATOR_ARRAY = new Comparator[0];
    private static final long serialVersionUID = -2224170307287243428L;
    private final Comparator<File>[] delegates;

    @Override // org.apache.commons.io.comparator.AbstractFileComparator
    public /* bridge */ /* synthetic */ List sort(List list) {
        return super.sort((List<File>) list);
    }

    @Override // org.apache.commons.io.comparator.AbstractFileComparator
    public /* bridge */ /* synthetic */ File[] sort(File[] fileArr) {
        return super.sort(fileArr);
    }

    public CompositeFileComparator(Comparator<File>... delegates) {
        this.delegates = delegates == null ? emptyArray() : (Comparator[]) delegates.clone();
    }

    public CompositeFileComparator(Iterable<Comparator<File>> delegates) {
        this.delegates = delegates == null ? emptyArray() : (Comparator[]) StreamSupport.stream(delegates.spliterator(), false).toArray(x$0 -> {
            return new Comparator[x$0];
        });
    }

    @Override // java.util.Comparator
    public int compare(File file1, File file2) {
        return ((Integer) Stream.of((Object[]) this.delegates).map(delegate -> {
            return Integer.valueOf(delegate.compare(file1, file2));
        }).filter(r -> {
            return r.intValue() != 0;
        }).findFirst().orElse(0)).intValue();
    }

    /* JADX WARN: Type inference failed for: r0v0, types: [java.util.Comparator<?>[], java.util.Comparator<java.io.File>[]] */
    private Comparator<File>[] emptyArray() {
        return EMPTY_COMPARATOR_ARRAY;
    }

    @Override // org.apache.commons.io.comparator.AbstractFileComparator
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(super.toString());
        builder.append('{');
        for (int i = 0; i < this.delegates.length; i++) {
            if (i > 0) {
                builder.append(',');
            }
            builder.append(this.delegates[i]);
        }
        builder.append('}');
        return builder.toString();
    }
}
