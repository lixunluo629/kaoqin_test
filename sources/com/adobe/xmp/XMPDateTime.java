package com.adobe.xmp;

import java.util.Calendar;
import java.util.TimeZone;

/* loaded from: xmpcore-5.1.3.jar:com/adobe/xmp/XMPDateTime.class */
public interface XMPDateTime extends Comparable {
    int getYear();

    void setYear(int i);

    int getMonth();

    void setMonth(int i);

    int getDay();

    void setDay(int i);

    int getHour();

    void setHour(int i);

    int getMinute();

    void setMinute(int i);

    int getSecond();

    void setSecond(int i);

    int getNanoSecond();

    void setNanoSecond(int i);

    TimeZone getTimeZone();

    void setTimeZone(TimeZone timeZone);

    boolean hasDate();

    boolean hasTime();

    boolean hasTimeZone();

    Calendar getCalendar();

    String getISO8601String();
}
