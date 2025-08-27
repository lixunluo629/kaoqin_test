package org.ehcache.expiry;

import java.util.concurrent.TimeUnit;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/expiry/Duration.class */
public final class Duration {
    public static final Duration INFINITE = new Duration(0, null, true);
    public static final Duration ZERO = new Duration(0, TimeUnit.NANOSECONDS, false);
    private final TimeUnit timeUnit;
    private final long length;

    public static Duration of(long length, TimeUnit timeUnit) {
        return new Duration(length, timeUnit);
    }

    public Duration(long length, TimeUnit timeUnit) {
        this(length, timeUnit, false);
    }

    private Duration(long length, TimeUnit timeUnit, boolean forever) {
        if (!forever) {
            if (length < 0) {
                throw new IllegalArgumentException("length must be greater than or equal to zero: " + length);
            }
            if (timeUnit == null) {
                throw new NullPointerException("TimeUnit must not be null");
            }
        }
        this.length = length;
        this.timeUnit = timeUnit;
    }

    public long getLength() {
        checkInfinite();
        return this.length;
    }

    public TimeUnit getTimeUnit() {
        checkInfinite();
        return this.timeUnit;
    }

    private void checkInfinite() {
        if (isInfinite()) {
            throw new IllegalStateException("The calling code should be checking explicitly for Duration#INFINITE or isInfinite()");
        }
    }

    public boolean isInfinite() {
        return this.timeUnit == null;
    }

    public int hashCode() {
        int result;
        int result2 = (31 * 1) + ((int) (this.length ^ (this.length >>> 32)));
        if (this.length != 0) {
            result = (31 * result2) + (this.timeUnit == null ? 0 : this.timeUnit.hashCode());
        } else {
            result = (31 * result2) + (this.timeUnit == null ? 0 : 1);
        }
        return result;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Duration other = (Duration) obj;
        if (this.length != other.length) {
            return false;
        }
        if (this.timeUnit == null || other.timeUnit == null) {
            return this.timeUnit == other.timeUnit;
        }
        if (this.timeUnit == other.timeUnit || this.length == 0) {
            return true;
        }
        return false;
    }

    public String toString() {
        if (isInfinite()) {
            return "Duration[INFINITE]";
        }
        if (this.length == 0) {
            return "Duration[ZERO]";
        }
        return "Duration[length=" + this.length + ", timeUnit=" + this.timeUnit.name() + "]";
    }
}
