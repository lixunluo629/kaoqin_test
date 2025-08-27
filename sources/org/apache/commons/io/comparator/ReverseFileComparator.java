package org.apache.commons.io.comparator;

import java.io.File;
import java.io.Serializable;
import java.util.Comparator;
import java.util.Objects;
import org.springframework.beans.PropertyAccessor;

/* loaded from: commons-io-2.18.0.jar:org/apache/commons/io/comparator/ReverseFileComparator.class */
final class ReverseFileComparator extends AbstractFileComparator implements Serializable {
    private static final long serialVersionUID = -4808255005272229056L;
    private final Comparator<File> delegate;

    public ReverseFileComparator(Comparator<File> delegate) {
        this.delegate = (Comparator) Objects.requireNonNull(delegate, "delegate");
    }

    @Override // java.util.Comparator
    public int compare(File file1, File file2) {
        return this.delegate.compare(file2, file1);
    }

    @Override // org.apache.commons.io.comparator.AbstractFileComparator
    public String toString() {
        return super.toString() + PropertyAccessor.PROPERTY_KEY_PREFIX + this.delegate.toString() + "]";
    }
}
