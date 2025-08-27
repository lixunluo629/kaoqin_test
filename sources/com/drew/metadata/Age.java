package com.drew.metadata;

import com.drew.lang.annotations.NotNull;
import com.drew.lang.annotations.Nullable;

/* loaded from: metadata-extractor-2.10.1.jar:com/drew/metadata/Age.class */
public class Age {
    private final int _years;
    private final int _months;
    private final int _days;
    private final int _hours;
    private final int _minutes;
    private final int _seconds;

    @Nullable
    public static Age fromPanasonicString(@NotNull String s) throws NumberFormatException {
        if (s.length() != 19 || s.startsWith("9999:99:99")) {
            return null;
        }
        try {
            int years = Integer.parseInt(s.substring(0, 4));
            int months = Integer.parseInt(s.substring(5, 7));
            int days = Integer.parseInt(s.substring(8, 10));
            int hours = Integer.parseInt(s.substring(11, 13));
            int minutes = Integer.parseInt(s.substring(14, 16));
            int seconds = Integer.parseInt(s.substring(17, 19));
            return new Age(years, months, days, hours, minutes, seconds);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public Age(int years, int months, int days, int hours, int minutes, int seconds) {
        this._years = years;
        this._months = months;
        this._days = days;
        this._hours = hours;
        this._minutes = minutes;
        this._seconds = seconds;
    }

    public int getYears() {
        return this._years;
    }

    public int getMonths() {
        return this._months;
    }

    public int getDays() {
        return this._days;
    }

    public int getHours() {
        return this._hours;
    }

    public int getMinutes() {
        return this._minutes;
    }

    public int getSeconds() {
        return this._seconds;
    }

    public String toString() {
        return String.format("%04d:%02d:%02d %02d:%02d:%02d", Integer.valueOf(this._years), Integer.valueOf(this._months), Integer.valueOf(this._days), Integer.valueOf(this._hours), Integer.valueOf(this._minutes), Integer.valueOf(this._seconds));
    }

    public String toFriendlyString() {
        StringBuilder result = new StringBuilder();
        appendAgePart(result, this._years, "year");
        appendAgePart(result, this._months, "month");
        appendAgePart(result, this._days, "day");
        appendAgePart(result, this._hours, "hour");
        appendAgePart(result, this._minutes, "minute");
        appendAgePart(result, this._seconds, "second");
        return result.toString();
    }

    private static void appendAgePart(StringBuilder result, int num, String singularName) {
        if (num == 0) {
            return;
        }
        if (result.length() != 0) {
            result.append(' ');
        }
        result.append(num).append(' ').append(singularName);
        if (num != 1) {
            result.append('s');
        }
    }

    public boolean equals(@Nullable Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Age age = (Age) o;
        return this._days == age._days && this._hours == age._hours && this._minutes == age._minutes && this._months == age._months && this._seconds == age._seconds && this._years == age._years;
    }

    public int hashCode() {
        int result = this._years;
        return (31 * ((31 * ((31 * ((31 * ((31 * result) + this._months)) + this._days)) + this._hours)) + this._minutes)) + this._seconds;
    }
}
