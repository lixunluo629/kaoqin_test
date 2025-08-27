package org.junit.internal;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.springframework.beans.PropertyAccessor;

/* loaded from: junit-4.12.jar:org/junit/internal/ArrayComparisonFailure.class */
public class ArrayComparisonFailure extends AssertionError {
    private static final long serialVersionUID = 1;
    private final List<Integer> fIndices = new ArrayList();
    private final String fMessage;

    public ArrayComparisonFailure(String message, AssertionError cause, int index) {
        this.fMessage = message;
        initCause(cause);
        addDimension(index);
    }

    public void addDimension(int index) {
        this.fIndices.add(0, Integer.valueOf(index));
    }

    @Override // java.lang.Throwable
    public String getMessage() {
        StringBuilder sb = new StringBuilder();
        if (this.fMessage != null) {
            sb.append(this.fMessage);
        }
        sb.append("arrays first differed at element ");
        Iterator i$ = this.fIndices.iterator();
        while (i$.hasNext()) {
            int each = i$.next().intValue();
            sb.append(PropertyAccessor.PROPERTY_KEY_PREFIX);
            sb.append(each);
            sb.append("]");
        }
        sb.append("; ");
        sb.append(getCause().getMessage());
        return sb.toString();
    }

    @Override // java.lang.Throwable
    public String toString() {
        return getMessage();
    }
}
