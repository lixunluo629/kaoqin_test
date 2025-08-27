package org.apache.xmlbeans;

import java.math.BigDecimal;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/XmlCalendar.class */
public class XmlCalendar extends GregorianCalendar {
    private static final int DEFAULT_DEFAULT_YEAR = 0;
    private static int defaultYear = Integer.MIN_VALUE;
    private static Date _beginningOfTime = new Date(Long.MIN_VALUE);

    public XmlCalendar(String xmlSchemaDateString) {
        this(new GDate(xmlSchemaDateString));
    }

    public XmlCalendar(GDateSpecification date) {
        this(GDate.timeZoneForGDate(date), date);
    }

    private XmlCalendar(TimeZone tz, GDateSpecification date) {
        super(tz);
        setGregorianChange(_beginningOfTime);
        clear();
        if (date.hasYear()) {
            int y = date.getYear();
            if (y > 0) {
                set(0, 1);
            } else {
                set(0, 0);
                y = -y;
            }
            set(1, y);
        }
        if (date.hasMonth()) {
            set(2, date.getMonth() - 1);
        }
        if (date.hasDay()) {
            set(5, date.getDay());
        }
        if (date.hasTime()) {
            set(11, date.getHour());
            set(12, date.getMinute());
            set(13, date.getSecond());
            if (date.getFraction().scale() > 0) {
                set(14, date.getMillisecond());
            }
        }
        if (date.hasTimeZone()) {
            set(15, date.getTimeZoneSign() * 1000 * 60 * ((date.getTimeZoneHour() * 60) + date.getTimeZoneMinute()));
            set(16, 0);
        }
    }

    public XmlCalendar(Date date) {
        this(TimeZone.getDefault(), new GDate(date));
        complete();
    }

    public XmlCalendar(int year, int month, int day, int hour, int minute, int second, BigDecimal fraction) {
        this(TimeZone.getDefault(), new GDate(year, month, day, hour, minute, second, fraction));
    }

    public XmlCalendar(int year, int month, int day, int hour, int minute, int second, BigDecimal fraction, int tzSign, int tzHour, int tzMinute) {
        this(new GDate(year, month, day, hour, minute, second, fraction, tzSign, tzHour, tzMinute));
    }

    @Override // java.util.Calendar
    public int get(int field) {
        if (!isSet(field) || this.isTimeSet) {
            return super.get(field);
        }
        return internalGet(field);
    }

    public XmlCalendar() {
        setGregorianChange(_beginningOfTime);
        clear();
    }

    public static int getDefaultYear() {
        if (defaultYear == Integer.MIN_VALUE) {
            try {
                String yearstring = SystemProperties.getProperty("user.defaultyear");
                if (yearstring != null) {
                    defaultYear = Integer.parseInt(yearstring);
                } else {
                    defaultYear = 0;
                }
            } catch (Throwable th) {
                defaultYear = 0;
            }
        }
        return defaultYear;
    }

    public static void setDefaultYear(int year) {
        defaultYear = year;
    }

    @Override // java.util.GregorianCalendar, java.util.Calendar
    protected void computeTime() {
        boolean unsetYear = !isSet(1);
        if (unsetYear) {
            set(1, getDefaultYear());
        }
        try {
            super.computeTime();
            if (unsetYear) {
                clear(1);
            }
        } catch (Throwable th) {
            if (unsetYear) {
                clear(1);
            }
            throw th;
        }
    }

    @Override // java.util.Calendar
    public String toString() {
        return new GDate(this).toString();
    }
}
