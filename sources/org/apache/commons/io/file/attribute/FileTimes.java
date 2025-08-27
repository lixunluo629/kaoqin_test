package org.apache.commons.io.file.attribute;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileTime;
import java.time.Instant;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/* loaded from: commons-io-2.18.0.jar:org/apache/commons/io/file/attribute/FileTimes.class */
public final class FileTimes {
    static final long WINDOWS_EPOCH_OFFSET = -116444736000000000L;
    public static final FileTime EPOCH = FileTime.from(Instant.EPOCH);
    private static final long HUNDRED_NANOS_PER_SECOND = TimeUnit.SECONDS.toNanos(1) / 100;
    static final long HUNDRED_NANOS_PER_MILLISECOND = TimeUnit.MILLISECONDS.toNanos(1) / 100;

    public static FileTime fromUnixTime(long time) {
        return FileTime.from(time, TimeUnit.SECONDS);
    }

    public static boolean isUnixTime(FileTime time) {
        return isUnixTime(toUnixTime(time));
    }

    public static boolean isUnixTime(long seconds) {
        return -2147483648L <= seconds && seconds <= 2147483647L;
    }

    public static FileTime minusMillis(FileTime fileTime, long millisToSubtract) {
        return FileTime.from(fileTime.toInstant().minusMillis(millisToSubtract));
    }

    public static FileTime minusNanos(FileTime fileTime, long nanosToSubtract) {
        return FileTime.from(fileTime.toInstant().minusNanos(nanosToSubtract));
    }

    public static FileTime minusSeconds(FileTime fileTime, long secondsToSubtract) {
        return FileTime.from(fileTime.toInstant().minusSeconds(secondsToSubtract));
    }

    public static FileTime now() {
        return FileTime.from(Instant.now());
    }

    public static Date ntfsTimeToDate(long ntfsTime) {
        long javaHundredNanos = Math.addExact(ntfsTime, WINDOWS_EPOCH_OFFSET);
        long javaMillis = Math.floorDiv(javaHundredNanos, HUNDRED_NANOS_PER_MILLISECOND);
        return new Date(javaMillis);
    }

    public static FileTime ntfsTimeToFileTime(long ntfsTime) {
        long javaHundredsNanos = Math.addExact(ntfsTime, WINDOWS_EPOCH_OFFSET);
        long javaSeconds = Math.floorDiv(javaHundredsNanos, HUNDRED_NANOS_PER_SECOND);
        long javaNanos = Math.floorMod(javaHundredsNanos, HUNDRED_NANOS_PER_SECOND) * 100;
        return FileTime.from(Instant.ofEpochSecond(javaSeconds, javaNanos));
    }

    public static FileTime plusMillis(FileTime fileTime, long millisToAdd) {
        return FileTime.from(fileTime.toInstant().plusMillis(millisToAdd));
    }

    public static FileTime plusNanos(FileTime fileTime, long nanosToSubtract) {
        return FileTime.from(fileTime.toInstant().plusNanos(nanosToSubtract));
    }

    public static FileTime plusSeconds(FileTime fileTime, long secondsToAdd) {
        return FileTime.from(fileTime.toInstant().plusSeconds(secondsToAdd));
    }

    public static void setLastModifiedTime(Path path) throws IOException {
        Files.setLastModifiedTime(path, now());
    }

    public static Date toDate(FileTime fileTime) {
        if (fileTime != null) {
            return new Date(fileTime.toMillis());
        }
        return null;
    }

    public static FileTime toFileTime(Date date) {
        if (date != null) {
            return FileTime.fromMillis(date.getTime());
        }
        return null;
    }

    public static long toNtfsTime(Date date) {
        long javaHundredNanos = date.getTime() * HUNDRED_NANOS_PER_MILLISECOND;
        return Math.subtractExact(javaHundredNanos, WINDOWS_EPOCH_OFFSET);
    }

    public static long toNtfsTime(FileTime fileTime) {
        Instant instant = fileTime.toInstant();
        long javaHundredNanos = (instant.getEpochSecond() * HUNDRED_NANOS_PER_SECOND) + (instant.getNano() / 100);
        return Math.subtractExact(javaHundredNanos, WINDOWS_EPOCH_OFFSET);
    }

    public static long toNtfsTime(long javaTime) {
        long javaHundredNanos = javaTime * HUNDRED_NANOS_PER_MILLISECOND;
        return Math.subtractExact(javaHundredNanos, WINDOWS_EPOCH_OFFSET);
    }

    public static long toUnixTime(FileTime fileTime) {
        if (fileTime != null) {
            return fileTime.to(TimeUnit.SECONDS);
        }
        return 0L;
    }

    private FileTimes() {
    }
}
