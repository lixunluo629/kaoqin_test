package org.springframework.data.history;

import java.lang.Comparable;
import java.lang.Number;
import org.joda.time.DateTime;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/history/RevisionMetadata.class */
public interface RevisionMetadata<N extends Number & Comparable<N>> {
    N getRevisionNumber();

    DateTime getRevisionDate();

    <T> T getDelegate();
}
