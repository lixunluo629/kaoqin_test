package org.springframework.data.history;

import org.springframework.data.domain.Sort;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/history/RevisionSort.class */
public class RevisionSort extends Sort {
    private static final long serialVersionUID = 618238321589063537L;
    private static final String PROPERTY = "__revisionNumber__";
    private static final RevisionSort ASC = new RevisionSort(Sort.Direction.ASC);
    private static final RevisionSort DESC = new RevisionSort(Sort.Direction.DESC);

    private RevisionSort(Sort.Direction direction) {
        super(direction, PROPERTY);
    }

    public static RevisionSort asc() {
        return ASC;
    }

    public static RevisionSort desc() {
        return DESC;
    }

    public static Sort.Direction getRevisionDirection(Sort sort) {
        if (sort == null) {
            return Sort.Direction.ASC;
        }
        Sort.Order order = sort.getOrderFor(PROPERTY);
        return order == null ? Sort.Direction.ASC : order.getDirection();
    }
}
