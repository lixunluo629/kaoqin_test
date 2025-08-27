package org.apache.commons.collections4.comparators;

import java.io.Serializable;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* loaded from: commons-collections4-4.1.jar:org/apache/commons/collections4/comparators/FixedOrderComparator.class */
public class FixedOrderComparator<T> implements Comparator<T>, Serializable {
    private static final long serialVersionUID = 82794675842863201L;
    private final Map<T, Integer> map = new HashMap();
    private int counter = 0;
    private boolean isLocked = false;
    private UnknownObjectBehavior unknownObjectBehavior = UnknownObjectBehavior.EXCEPTION;

    /* loaded from: commons-collections4-4.1.jar:org/apache/commons/collections4/comparators/FixedOrderComparator$UnknownObjectBehavior.class */
    public enum UnknownObjectBehavior {
        BEFORE,
        AFTER,
        EXCEPTION
    }

    public FixedOrderComparator() {
    }

    public FixedOrderComparator(T... items) {
        if (items == null) {
            throw new NullPointerException("The list of items must not be null");
        }
        for (T item : items) {
            add(item);
        }
    }

    public FixedOrderComparator(List<T> items) {
        if (items == null) {
            throw new NullPointerException("The list of items must not be null");
        }
        for (T t : items) {
            add(t);
        }
    }

    public boolean isLocked() {
        return this.isLocked;
    }

    protected void checkLocked() {
        if (isLocked()) {
            throw new UnsupportedOperationException("Cannot modify a FixedOrderComparator after a comparison");
        }
    }

    public UnknownObjectBehavior getUnknownObjectBehavior() {
        return this.unknownObjectBehavior;
    }

    public void setUnknownObjectBehavior(UnknownObjectBehavior unknownObjectBehavior) {
        checkLocked();
        if (unknownObjectBehavior == null) {
            throw new NullPointerException("Unknown object behavior must not be null");
        }
        this.unknownObjectBehavior = unknownObjectBehavior;
    }

    public boolean add(T obj) {
        checkLocked();
        Map<T, Integer> map = this.map;
        int i = this.counter;
        this.counter = i + 1;
        Integer position = map.put(obj, Integer.valueOf(i));
        return position == null;
    }

    public boolean addAsEqual(T existingObj, T newObj) {
        checkLocked();
        Integer position = this.map.get(existingObj);
        if (position == null) {
            throw new IllegalArgumentException(existingObj + " not known to " + this);
        }
        Integer result = this.map.put(newObj, position);
        return result == null;
    }

    @Override // java.util.Comparator
    public int compare(T obj1, T obj2) {
        this.isLocked = true;
        Integer position1 = this.map.get(obj1);
        Integer position2 = this.map.get(obj2);
        if (position1 == null || position2 == null) {
            switch (this.unknownObjectBehavior) {
                case BEFORE:
                    if (position1 == null) {
                        return position2 == null ? 0 : -1;
                    }
                    return 1;
                case AFTER:
                    if (position1 == null) {
                        return position2 == null ? 0 : 1;
                    }
                    return -1;
                case EXCEPTION:
                    Object unknownObj = position1 == null ? obj1 : obj2;
                    throw new IllegalArgumentException("Attempting to compare unknown object " + unknownObj);
                default:
                    throw new UnsupportedOperationException("Unknown unknownObjectBehavior: " + this.unknownObjectBehavior);
            }
        }
        return position1.compareTo(position2);
    }

    public int hashCode() {
        int total = (17 * 37) + (this.map == null ? 0 : this.map.hashCode());
        return (((((total * 37) + (this.unknownObjectBehavior == null ? 0 : this.unknownObjectBehavior.hashCode())) * 37) + this.counter) * 37) + (this.isLocked ? 0 : 1);
    }

    @Override // java.util.Comparator
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (null != object && object.getClass().equals(getClass())) {
            FixedOrderComparator<?> comp = (FixedOrderComparator) object;
            if (null != this.map ? this.map.equals(comp.map) : null == comp.map) {
                if (null != this.unknownObjectBehavior ? !(this.unknownObjectBehavior != comp.unknownObjectBehavior || this.counter != comp.counter || this.isLocked != comp.isLocked || this.unknownObjectBehavior != comp.unknownObjectBehavior) : null == comp.unknownObjectBehavior) {
                    return true;
                }
            }
            return false;
        }
        return false;
    }
}
