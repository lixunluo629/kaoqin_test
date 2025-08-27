package com.zaxxer.hikari.util;

import ch.qos.logback.core.pattern.color.ANSIConstants;
import ch.qos.logback.core.rolling.helper.DateTokenConverter;
import com.alibaba.excel.constant.ExcelXmlConstants;
import java.util.concurrent.TimeUnit;

/* loaded from: HikariCP-3.4.5.jar:com/zaxxer/hikari/util/ClockSource.class */
public interface ClockSource {
    public static final ClockSource CLOCK = Factory.create();
    public static final TimeUnit[] TIMEUNITS_DESCENDING = {TimeUnit.DAYS, TimeUnit.HOURS, TimeUnit.MINUTES, TimeUnit.SECONDS, TimeUnit.MILLISECONDS, TimeUnit.MICROSECONDS, TimeUnit.NANOSECONDS};
    public static final String[] TIMEUNIT_DISPLAY_VALUES = {"ns", "µs", "ms", ExcelXmlConstants.CELL_DATA_FORMAT_TAG, ANSIConstants.ESC_END, "h", DateTokenConverter.CONVERTER_KEY};

    long currentTime0();

    long toMillis0(long j);

    long toNanos0(long j);

    long elapsedMillis0(long j);

    long elapsedMillis0(long j, long j2);

    long elapsedNanos0(long j);

    long elapsedNanos0(long j, long j2);

    long plusMillis0(long j, long j2);

    TimeUnit getSourceTimeUnit0();

    static long currentTime() {
        return CLOCK.currentTime0();
    }

    static long toMillis(long time) {
        return CLOCK.toMillis0(time);
    }

    static long toNanos(long time) {
        return CLOCK.toNanos0(time);
    }

    static long elapsedMillis(long startTime) {
        return CLOCK.elapsedMillis0(startTime);
    }

    static long elapsedMillis(long startTime, long endTime) {
        return CLOCK.elapsedMillis0(startTime, endTime);
    }

    static long elapsedNanos(long startTime) {
        return CLOCK.elapsedNanos0(startTime);
    }

    static long elapsedNanos(long startTime, long endTime) {
        return CLOCK.elapsedNanos0(startTime, endTime);
    }

    static long plusMillis(long time, long millis) {
        return CLOCK.plusMillis0(time, millis);
    }

    static TimeUnit getSourceTimeUnit() {
        return CLOCK.getSourceTimeUnit0();
    }

    static String elapsedDisplayString(long startTime, long endTime) {
        return CLOCK.elapsedDisplayString0(startTime, endTime);
    }

    default String elapsedDisplayString0(long startTime, long endTime) {
        long elapsedNanos = elapsedNanos0(startTime, endTime);
        StringBuilder sb = new StringBuilder(elapsedNanos < 0 ? "-" : "");
        long elapsedNanos2 = Math.abs(elapsedNanos);
        for (TimeUnit unit : TIMEUNITS_DESCENDING) {
            long converted = unit.convert(elapsedNanos2, TimeUnit.NANOSECONDS);
            if (converted > 0) {
                sb.append(converted).append(TIMEUNIT_DISPLAY_VALUES[unit.ordinal()]);
                elapsedNanos2 -= TimeUnit.NANOSECONDS.convert(converted, unit);
            }
        }
        return sb.toString();
    }

    /* loaded from: HikariCP-3.4.5.jar:com/zaxxer/hikari/util/ClockSource$Factory.class */
    public static class Factory {
        /* JADX INFO: Access modifiers changed from: private */
        public static ClockSource create() {
            String os = System.getProperty("os.name");
            if ("Mac OS X".equals(os)) {
                return new MillisecondClockSource();
            }
            return new NanosecondClockSource();
        }
    }

    /* loaded from: HikariCP-3.4.5.jar:com/zaxxer/hikari/util/ClockSource$MillisecondClockSource.class */
    public static final class MillisecondClockSource implements ClockSource {
        @Override // com.zaxxer.hikari.util.ClockSource
        public long currentTime0() {
            return System.currentTimeMillis();
        }

        @Override // com.zaxxer.hikari.util.ClockSource
        public long elapsedMillis0(long startTime) {
            return System.currentTimeMillis() - startTime;
        }

        @Override // com.zaxxer.hikari.util.ClockSource
        public long elapsedMillis0(long startTime, long endTime) {
            return endTime - startTime;
        }

        @Override // com.zaxxer.hikari.util.ClockSource
        public long elapsedNanos0(long startTime) {
            return TimeUnit.MILLISECONDS.toNanos(System.currentTimeMillis() - startTime);
        }

        @Override // com.zaxxer.hikari.util.ClockSource
        public long elapsedNanos0(long startTime, long endTime) {
            return TimeUnit.MILLISECONDS.toNanos(endTime - startTime);
        }

        @Override // com.zaxxer.hikari.util.ClockSource
        public long toMillis0(long time) {
            return time;
        }

        @Override // com.zaxxer.hikari.util.ClockSource
        public long toNanos0(long time) {
            return TimeUnit.MILLISECONDS.toNanos(time);
        }

        @Override // com.zaxxer.hikari.util.ClockSource
        public long plusMillis0(long time, long millis) {
            return time + millis;
        }

        @Override // com.zaxxer.hikari.util.ClockSource
        public TimeUnit getSourceTimeUnit0() {
            return TimeUnit.MILLISECONDS;
        }
    }

    /* loaded from: HikariCP-3.4.5.jar:com/zaxxer/hikari/util/ClockSource$NanosecondClockSource.class */
    public static class NanosecondClockSource implements ClockSource {
        @Override // com.zaxxer.hikari.util.ClockSource
        public long currentTime0() {
            return System.nanoTime();
        }

        @Override // com.zaxxer.hikari.util.ClockSource
        public long toMillis0(long time) {
            return TimeUnit.NANOSECONDS.toMillis(time);
        }

        @Override // com.zaxxer.hikari.util.ClockSource
        public long toNanos0(long time) {
            return time;
        }

        @Override // com.zaxxer.hikari.util.ClockSource
        public long elapsedMillis0(long startTime) {
            return TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startTime);
        }

        @Override // com.zaxxer.hikari.util.ClockSource
        public long elapsedMillis0(long startTime, long endTime) {
            return TimeUnit.NANOSECONDS.toMillis(endTime - startTime);
        }

        @Override // com.zaxxer.hikari.util.ClockSource
        public long elapsedNanos0(long startTime) {
            return System.nanoTime() - startTime;
        }

        @Override // com.zaxxer.hikari.util.ClockSource
        public long elapsedNanos0(long startTime, long endTime) {
            return endTime - startTime;
        }

        @Override // com.zaxxer.hikari.util.ClockSource
        public long plusMillis0(long time, long millis) {
            return time + TimeUnit.MILLISECONDS.toNanos(millis);
        }

        @Override // com.zaxxer.hikari.util.ClockSource
        public TimeUnit getSourceTimeUnit0() {
            return TimeUnit.NANOSECONDS;
        }
    }
}
