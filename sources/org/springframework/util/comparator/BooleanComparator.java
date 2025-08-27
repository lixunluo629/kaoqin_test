package org.springframework.util.comparator;

import java.io.Serializable;
import java.util.Comparator;

/* loaded from: spring-core-4.3.25.RELEASE.jar:org/springframework/util/comparator/BooleanComparator.class */
public final class BooleanComparator implements Comparator<Boolean>, Serializable {
    public static final BooleanComparator TRUE_LOW = new BooleanComparator(true);
    public static final BooleanComparator TRUE_HIGH = new BooleanComparator(false);
    private final boolean trueLow;

    public BooleanComparator(boolean trueLow) {
        this.trueLow = trueLow;
    }

    @Override // java.util.Comparator
    public int compare(Boolean v1, Boolean v2) {
        if (v1.booleanValue() ^ v2.booleanValue()) {
            return v1.booleanValue() ^ this.trueLow ? 1 : -1;
        }
        return 0;
    }

    @Override // java.util.Comparator
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        return (obj instanceof BooleanComparator) && this.trueLow == ((BooleanComparator) obj).trueLow;
    }

    public int hashCode() {
        return (this.trueLow ? -1 : 1) * getClass().hashCode();
    }

    public String toString() {
        return "BooleanComparator: " + (this.trueLow ? "true low" : "true high");
    }
}
