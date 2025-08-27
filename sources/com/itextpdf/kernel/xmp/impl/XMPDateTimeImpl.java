package com.itextpdf.kernel.xmp.impl;

import com.itextpdf.kernel.xmp.XMPDateTime;
import com.itextpdf.kernel.xmp.XMPException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;
import org.apache.xmlbeans.SchemaType;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/xmp/impl/XMPDateTimeImpl.class */
public class XMPDateTimeImpl implements XMPDateTime {
    private int year;
    private int month;
    private int day;
    private int hour;
    private int minute;
    private int second;
    private TimeZone timeZone;
    private int nanoSeconds;
    private boolean hasDate;
    private boolean hasTime;
    private boolean hasTimeZone;

    public XMPDateTimeImpl() {
        this.year = 0;
        this.month = 0;
        this.day = 0;
        this.hour = 0;
        this.minute = 0;
        this.second = 0;
        this.timeZone = null;
        this.hasDate = false;
        this.hasTime = false;
        this.hasTimeZone = false;
    }

    public XMPDateTimeImpl(Calendar calendar) {
        this.year = 0;
        this.month = 0;
        this.day = 0;
        this.hour = 0;
        this.minute = 0;
        this.second = 0;
        this.timeZone = null;
        this.hasDate = false;
        this.hasTime = false;
        this.hasTimeZone = false;
        Date date = calendar.getTime();
        TimeZone zone = calendar.getTimeZone();
        GregorianCalendar intCalendar = (GregorianCalendar) Calendar.getInstance(Locale.US);
        intCalendar.setGregorianChange(new Date(Long.MIN_VALUE));
        intCalendar.setTimeZone(zone);
        intCalendar.setTime(date);
        this.year = intCalendar.get(1);
        this.month = intCalendar.get(2) + 1;
        this.day = intCalendar.get(5);
        this.hour = intCalendar.get(11);
        this.minute = intCalendar.get(12);
        this.second = intCalendar.get(13);
        this.nanoSeconds = intCalendar.get(14) * SchemaType.SIZE_BIG_INTEGER;
        this.timeZone = intCalendar.getTimeZone();
        this.hasTimeZone = true;
        this.hasTime = true;
        this.hasDate = true;
    }

    public XMPDateTimeImpl(Date date, TimeZone timeZone) {
        this.year = 0;
        this.month = 0;
        this.day = 0;
        this.hour = 0;
        this.minute = 0;
        this.second = 0;
        this.timeZone = null;
        this.hasDate = false;
        this.hasTime = false;
        this.hasTimeZone = false;
        GregorianCalendar calendar = new GregorianCalendar(timeZone);
        calendar.setTime(date);
        this.year = calendar.get(1);
        this.month = calendar.get(2) + 1;
        this.day = calendar.get(5);
        this.hour = calendar.get(11);
        this.minute = calendar.get(12);
        this.second = calendar.get(13);
        this.nanoSeconds = calendar.get(14) * SchemaType.SIZE_BIG_INTEGER;
        this.timeZone = timeZone;
        this.hasTimeZone = true;
        this.hasTime = true;
        this.hasDate = true;
    }

    public XMPDateTimeImpl(String strValue) throws XMPException {
        this.year = 0;
        this.month = 0;
        this.day = 0;
        this.hour = 0;
        this.minute = 0;
        this.second = 0;
        this.timeZone = null;
        this.hasDate = false;
        this.hasTime = false;
        this.hasTimeZone = false;
        ISO8601Converter.parse(strValue, this);
    }

    @Override // com.itextpdf.kernel.xmp.XMPDateTime
    public int getYear() {
        return this.year;
    }

    @Override // com.itextpdf.kernel.xmp.XMPDateTime
    public void setYear(int year) {
        this.year = Math.min(Math.abs(year), 9999);
        this.hasDate = true;
    }

    @Override // com.itextpdf.kernel.xmp.XMPDateTime
    public int getMonth() {
        return this.month;
    }

    @Override // com.itextpdf.kernel.xmp.XMPDateTime
    public void setMonth(int month) {
        if (month < 1) {
            this.month = 1;
        } else if (month > 12) {
            this.month = 12;
        } else {
            this.month = month;
        }
        this.hasDate = true;
    }

    @Override // com.itextpdf.kernel.xmp.XMPDateTime
    public int getDay() {
        return this.day;
    }

    @Override // com.itextpdf.kernel.xmp.XMPDateTime
    public void setDay(int day) {
        if (day < 1) {
            this.day = 1;
        } else if (day > 31) {
            this.day = 31;
        } else {
            this.day = day;
        }
        this.hasDate = true;
    }

    @Override // com.itextpdf.kernel.xmp.XMPDateTime
    public int getHour() {
        return this.hour;
    }

    @Override // com.itextpdf.kernel.xmp.XMPDateTime
    public void setHour(int hour) {
        this.hour = Math.min(Math.abs(hour), 23);
        this.hasTime = true;
    }

    @Override // com.itextpdf.kernel.xmp.XMPDateTime
    public int getMinute() {
        return this.minute;
    }

    @Override // com.itextpdf.kernel.xmp.XMPDateTime
    public void setMinute(int minute) {
        this.minute = Math.min(Math.abs(minute), 59);
        this.hasTime = true;
    }

    @Override // com.itextpdf.kernel.xmp.XMPDateTime
    public int getSecond() {
        return this.second;
    }

    @Override // com.itextpdf.kernel.xmp.XMPDateTime
    public void setSecond(int second) {
        this.second = Math.min(Math.abs(second), 59);
        this.hasTime = true;
    }

    @Override // com.itextpdf.kernel.xmp.XMPDateTime
    public int getNanoSecond() {
        return this.nanoSeconds;
    }

    @Override // com.itextpdf.kernel.xmp.XMPDateTime
    public void setNanoSecond(int nanoSecond) {
        this.nanoSeconds = nanoSecond;
        this.hasTime = true;
    }

    @Override // java.lang.Comparable
    public int compareTo(Object dt) {
        long d = getCalendar().getTimeInMillis() - ((XMPDateTime) dt).getCalendar().getTimeInMillis();
        if (d != 0) {
            return (int) Math.signum(d);
        }
        return (int) Math.signum(this.nanoSeconds - ((XMPDateTime) dt).getNanoSecond());
    }

    @Override // com.itextpdf.kernel.xmp.XMPDateTime
    public TimeZone getTimeZone() {
        return this.timeZone;
    }

    @Override // com.itextpdf.kernel.xmp.XMPDateTime
    public void setTimeZone(TimeZone timeZone) {
        this.timeZone = timeZone;
        this.hasTime = true;
        this.hasTimeZone = true;
    }

    @Override // com.itextpdf.kernel.xmp.XMPDateTime
    public boolean hasDate() {
        return this.hasDate;
    }

    @Override // com.itextpdf.kernel.xmp.XMPDateTime
    public boolean hasTime() {
        return this.hasTime;
    }

    @Override // com.itextpdf.kernel.xmp.XMPDateTime
    public boolean hasTimeZone() {
        return this.hasTimeZone;
    }

    @Override // com.itextpdf.kernel.xmp.XMPDateTime
    public Calendar getCalendar() {
        GregorianCalendar calendar = (GregorianCalendar) Calendar.getInstance(Locale.US);
        calendar.setGregorianChange(new Date(Long.MIN_VALUE));
        if (this.hasTimeZone) {
            calendar.setTimeZone(this.timeZone);
        }
        calendar.set(1, this.year);
        calendar.set(2, this.month - 1);
        calendar.set(5, this.day);
        calendar.set(11, this.hour);
        calendar.set(12, this.minute);
        calendar.set(13, this.second);
        calendar.set(14, this.nanoSeconds / SchemaType.SIZE_BIG_INTEGER);
        return calendar;
    }

    @Override // com.itextpdf.kernel.xmp.XMPDateTime
    public String getISO8601String() {
        return ISO8601Converter.render(this);
    }

    public String toString() {
        return getISO8601String();
    }
}
