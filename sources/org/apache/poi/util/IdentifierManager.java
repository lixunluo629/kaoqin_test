package org.apache.poi.util;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;
import org.springframework.beans.PropertyAccessor;

/* loaded from: poi-ooxml-3.17.jar:org/apache/poi/util/IdentifierManager.class */
public class IdentifierManager {
    public static final long MAX_ID = 9223372036854775806L;
    public static final long MIN_ID = 0;
    private final long upperbound;
    private final long lowerbound;
    private LinkedList<Segment> segments;

    public IdentifierManager(long lowerbound, long upperbound) {
        if (lowerbound > upperbound) {
            throw new IllegalArgumentException("lowerbound must not be greater than upperbound, had " + lowerbound + " and " + upperbound);
        }
        if (lowerbound < 0) {
            String message = "lowerbound must be greater than or equal to " + Long.toString(0L);
            throw new IllegalArgumentException(message);
        }
        if (upperbound > MAX_ID) {
            throw new IllegalArgumentException("upperbound must be less than or equal to " + Long.toString(MAX_ID) + " but had " + upperbound);
        }
        this.lowerbound = lowerbound;
        this.upperbound = upperbound;
        this.segments = new LinkedList<>();
        this.segments.add(new Segment(lowerbound, upperbound));
    }

    public long reserve(long id) {
        if (id < this.lowerbound || id > this.upperbound) {
            throw new IllegalArgumentException("Value for parameter 'id' was out of bounds, had " + id + ", but should be within [" + this.lowerbound + ":" + this.upperbound + "]");
        }
        verifyIdentifiersLeft();
        if (id == this.upperbound) {
            Segment lastSegment = this.segments.getLast();
            if (lastSegment.end == this.upperbound) {
                lastSegment.end = this.upperbound - 1;
                if (lastSegment.start > lastSegment.end) {
                    this.segments.removeLast();
                }
                return id;
            }
            return reserveNew();
        }
        if (id == this.lowerbound) {
            Segment firstSegment = this.segments.getFirst();
            if (firstSegment.start == this.lowerbound) {
                firstSegment.start = this.lowerbound + 1;
                if (firstSegment.end < firstSegment.start) {
                    this.segments.removeFirst();
                }
                return id;
            }
            return reserveNew();
        }
        ListIterator<Segment> iter = this.segments.listIterator();
        while (true) {
            if (!iter.hasNext()) {
                break;
            }
            Segment segment = iter.next();
            if (segment.end >= id) {
                if (segment.start <= id) {
                    if (segment.start == id) {
                        segment.start = id + 1;
                        if (segment.end < segment.start) {
                            iter.remove();
                        }
                        return id;
                    }
                    if (segment.end == id) {
                        segment.end = id - 1;
                        if (segment.start > segment.end) {
                            iter.remove();
                        }
                        return id;
                    }
                    iter.add(new Segment(id + 1, segment.end));
                    segment.end = id - 1;
                    return id;
                }
            }
        }
        return reserveNew();
    }

    public long reserveNew() {
        verifyIdentifiersLeft();
        Segment segment = this.segments.getFirst();
        long result = segment.start;
        segment.start++;
        if (segment.start > segment.end) {
            this.segments.removeFirst();
        }
        return result;
    }

    public boolean release(long id) {
        if (id < this.lowerbound || id > this.upperbound) {
            throw new IllegalArgumentException("Value for parameter 'id' was out of bounds, had " + id + ", but should be within [" + this.lowerbound + ":" + this.upperbound + "]");
        }
        if (id == this.upperbound) {
            Segment lastSegment = this.segments.getLast();
            if (lastSegment.end == this.upperbound - 1) {
                lastSegment.end = this.upperbound;
                return true;
            }
            if (lastSegment.end == this.upperbound) {
                return false;
            }
            this.segments.add(new Segment(this.upperbound, this.upperbound));
            return true;
        }
        if (id == this.lowerbound) {
            Segment firstSegment = this.segments.getFirst();
            if (firstSegment.start == this.lowerbound + 1) {
                firstSegment.start = this.lowerbound;
                return true;
            }
            if (firstSegment.start == this.lowerbound) {
                return false;
            }
            this.segments.addFirst(new Segment(this.lowerbound, this.lowerbound));
            return true;
        }
        long higher = id + 1;
        long lower = id - 1;
        ListIterator<Segment> iter = this.segments.listIterator();
        while (iter.hasNext()) {
            Segment segment = iter.next();
            if (segment.end >= lower) {
                if (segment.start > higher) {
                    iter.previous();
                    iter.add(new Segment(id, id));
                    return true;
                }
                if (segment.start == higher) {
                    segment.start = id;
                    return true;
                }
                if (segment.end == lower) {
                    segment.end = id;
                    if (iter.hasNext()) {
                        Segment next = iter.next();
                        if (next.start == segment.end + 1) {
                            segment.end = next.end;
                            iter.remove();
                            return true;
                        }
                        return true;
                    }
                    return true;
                }
                return false;
            }
        }
        return false;
    }

    public long getRemainingIdentifiers() {
        long result = 0;
        Iterator i$ = this.segments.iterator();
        while (i$.hasNext()) {
            Segment segment = i$.next();
            result = (result - segment.start) + segment.end + 1;
        }
        return result;
    }

    private void verifyIdentifiersLeft() {
        if (this.segments.isEmpty()) {
            throw new IllegalStateException("No identifiers left");
        }
    }

    /* loaded from: poi-ooxml-3.17.jar:org/apache/poi/util/IdentifierManager$Segment.class */
    private static class Segment {
        public long start;
        public long end;

        public Segment(long start, long end) {
            this.start = start;
            this.end = end;
        }

        public String toString() {
            return PropertyAccessor.PROPERTY_KEY_PREFIX + this.start + "; " + this.end + "]";
        }
    }
}
