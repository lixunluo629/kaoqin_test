package org.apache.xmlbeans;

import com.mysql.jdbc.MysqlErrorNumbers;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import org.apache.ibatis.javassist.compiler.TokenId;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/GDateBuilder.class */
public final class GDateBuilder implements GDateSpecification, Serializable {
    private static final long serialVersionUID = 1;
    private int _bits;
    private int _CY;
    private int _M;
    private int _D;
    private int _h;
    private int _m;
    private int _s;
    private BigDecimal _fs;
    private int _tzsign;
    private int _tzh;
    private int _tzm;
    static final BigInteger TEN;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !GDateBuilder.class.desiredAssertionStatus();
        TEN = BigInteger.valueOf(10L);
    }

    public GDateBuilder() {
    }

    public Object clone() {
        return new GDateBuilder(this);
    }

    public GDate toGDate() {
        return new GDate(this);
    }

    public GDateBuilder(GDateSpecification gdate) {
        if (gdate.hasTimeZone()) {
            setTimeZone(gdate.getTimeZoneSign(), gdate.getTimeZoneHour(), gdate.getTimeZoneMinute());
        }
        if (gdate.hasTime()) {
            setTime(gdate.getHour(), gdate.getMinute(), gdate.getSecond(), gdate.getFraction());
        }
        if (gdate.hasDay()) {
            setDay(gdate.getDay());
        }
        if (gdate.hasMonth()) {
            setMonth(gdate.getMonth());
        }
        if (gdate.hasYear()) {
            setYear(gdate.getYear());
        }
    }

    public GDateBuilder(CharSequence string) {
        this(new GDate(string));
    }

    public GDateBuilder(Calendar calendar) {
        this(new GDate(calendar));
    }

    public GDateBuilder(int year, int month, int day, int hour, int minute, int second, BigDecimal fraction) {
        this._bits = 30;
        if (year == 0) {
            throw new IllegalArgumentException();
        }
        this._CY = year > 0 ? year : year + 1;
        this._M = month;
        this._D = day;
        this._h = hour;
        this._m = minute;
        this._s = second;
        this._fs = fraction == null ? GDate._zero : fraction;
        if (!isValid()) {
            throw new IllegalArgumentException();
        }
    }

    public GDateBuilder(int year, int month, int day, int hour, int minute, int second, BigDecimal fraction, int tzSign, int tzHour, int tzMinute) {
        this._bits = 31;
        if (year == 0) {
            throw new IllegalArgumentException();
        }
        this._CY = year > 0 ? year : year + 1;
        this._M = month;
        this._D = day;
        this._h = hour;
        this._m = minute;
        this._s = second;
        this._fs = fraction == null ? GDate._zero : fraction;
        this._tzsign = tzSign;
        this._tzh = tzHour;
        this._tzm = tzMinute;
        if (!isValid()) {
            throw new IllegalArgumentException();
        }
    }

    public GDateBuilder(Date date) {
        setDate(date);
    }

    @Override // org.apache.xmlbeans.GDateSpecification
    public boolean isImmutable() {
        return false;
    }

    @Override // org.apache.xmlbeans.GDateSpecification
    public int getFlags() {
        return this._bits;
    }

    @Override // org.apache.xmlbeans.GDateSpecification
    public final boolean hasTimeZone() {
        return (this._bits & 1) != 0;
    }

    @Override // org.apache.xmlbeans.GDateSpecification
    public final boolean hasYear() {
        return (this._bits & 2) != 0;
    }

    @Override // org.apache.xmlbeans.GDateSpecification
    public final boolean hasMonth() {
        return (this._bits & 4) != 0;
    }

    @Override // org.apache.xmlbeans.GDateSpecification
    public final boolean hasDay() {
        return (this._bits & 8) != 0;
    }

    @Override // org.apache.xmlbeans.GDateSpecification
    public final boolean hasTime() {
        return (this._bits & 16) != 0;
    }

    @Override // org.apache.xmlbeans.GDateSpecification
    public final boolean hasDate() {
        return (this._bits & 14) == 14;
    }

    @Override // org.apache.xmlbeans.GDateSpecification
    public final int getYear() {
        return this._CY > 0 ? this._CY : this._CY - 1;
    }

    @Override // org.apache.xmlbeans.GDateSpecification
    public final int getMonth() {
        return this._M;
    }

    @Override // org.apache.xmlbeans.GDateSpecification
    public final int getDay() {
        return this._D;
    }

    @Override // org.apache.xmlbeans.GDateSpecification
    public final int getHour() {
        return this._h;
    }

    @Override // org.apache.xmlbeans.GDateSpecification
    public final int getMinute() {
        return this._m;
    }

    @Override // org.apache.xmlbeans.GDateSpecification
    public final int getSecond() {
        return this._s;
    }

    @Override // org.apache.xmlbeans.GDateSpecification
    public final BigDecimal getFraction() {
        return this._fs;
    }

    @Override // org.apache.xmlbeans.GDateSpecification
    public final int getMillisecond() {
        if (this._fs == null || this._fs == GDate._zero) {
            return 0;
        }
        return this._fs.setScale(3, 4).unscaledValue().intValue();
    }

    @Override // org.apache.xmlbeans.GDateSpecification
    public final int getTimeZoneSign() {
        return this._tzsign;
    }

    @Override // org.apache.xmlbeans.GDateSpecification
    public final int getTimeZoneHour() {
        return this._tzh;
    }

    @Override // org.apache.xmlbeans.GDateSpecification
    public final int getTimeZoneMinute() {
        return this._tzm;
    }

    public void setYear(int year) {
        if (year < -292275295 || year > 292277265) {
            throw new IllegalArgumentException("year out of range");
        }
        if (year == 0) {
            throw new IllegalArgumentException("year cannot be 0");
        }
        this._bits |= 2;
        this._CY = year > 0 ? year : year + 1;
    }

    public void setMonth(int month) {
        if (month < 1 || month > 12) {
            throw new IllegalArgumentException("month out of range");
        }
        this._bits |= 4;
        this._M = month;
    }

    public void setDay(int day) {
        if (day < 1 || day > 31) {
            throw new IllegalArgumentException("day out of range");
        }
        this._bits |= 8;
        this._D = day;
    }

    public void setTime(int hour, int minute, int second, BigDecimal fraction) {
        if (hour < 0 || hour > 24) {
            throw new IllegalArgumentException("hour out of range");
        }
        if (minute < 0 || minute > 59) {
            throw new IllegalArgumentException("minute out of range");
        }
        if (second < 0 || second > 59) {
            throw new IllegalArgumentException("second out of range");
        }
        if (fraction != null && (fraction.signum() < 0 || fraction.compareTo(GDate._one) > 1)) {
            throw new IllegalArgumentException("fraction out of range");
        }
        if (hour == 24 && (minute != 0 || second != 0 || (fraction != null && GDate._zero.compareTo(fraction) != 0))) {
            throw new IllegalArgumentException("when hour is 24, min sec and fracton must be 0");
        }
        this._bits |= 16;
        this._h = hour;
        this._m = minute;
        this._s = second;
        this._fs = fraction == null ? GDate._zero : fraction;
    }

    public void setTimeZone(int tzSign, int tzHour, int tzMinute) {
        if ((tzSign != 0 || tzHour != 0 || tzMinute != 0) && ((tzSign != -1 && tzSign != 1) || tzHour < 0 || tzMinute < 0 || ((tzHour != 14 || tzMinute != 0) && (tzHour >= 14 || tzMinute >= 60)))) {
            throw new IllegalArgumentException("time zone out of range (-14:00 to +14:00). (" + (tzSign < 0 ? "-" : "+") + tzHour + ":" + tzMinute + ")");
        }
        this._bits |= 1;
        this._tzsign = tzSign;
        this._tzh = tzHour;
        this._tzm = tzMinute;
    }

    public void setTimeZone(int tzTotalMinutes) {
        if (tzTotalMinutes < -840 || tzTotalMinutes > 840) {
            throw new IllegalArgumentException("time zone out of range (-840 to 840 minutes). (" + tzTotalMinutes + ")");
        }
        int tzSign = tzTotalMinutes < 0 ? -1 : tzTotalMinutes > 0 ? 1 : 0;
        int tzTotalMinutes2 = tzTotalMinutes * tzSign;
        int tzH = tzTotalMinutes2 / 60;
        int tzM = tzTotalMinutes2 - (tzH * 60);
        setTimeZone(tzSign, tzH, tzM);
    }

    public void clearYear() {
        this._bits &= -3;
        this._CY = 0;
    }

    public void clearMonth() {
        this._bits &= -5;
        this._M = 0;
    }

    public void clearDay() {
        this._bits &= -9;
        this._D = 0;
    }

    public void clearTime() {
        this._bits &= -17;
        this._h = 0;
        this._m = 0;
        this._s = 0;
        this._fs = null;
    }

    public void clearTimeZone() {
        this._bits &= -2;
        this._tzsign = 0;
        this._tzh = 0;
        this._tzm = 0;
    }

    @Override // org.apache.xmlbeans.GDateSpecification
    public boolean isValid() {
        return isValidGDate(this);
    }

    static final boolean isValidGDate(GDateSpecification date) {
        if (date.hasYear() && date.getYear() == 0) {
            return false;
        }
        if (date.hasMonth() && (date.getMonth() < 1 || date.getMonth() > 12)) {
            return false;
        }
        if (date.hasDay()) {
            if (date.getDay() < 1 || date.getDay() > 31) {
                return false;
            }
            if (date.getDay() > 28 && date.hasMonth()) {
                if (date.hasYear()) {
                    if (date.getDay() > _maxDayInMonthFor(date.getYear() > 0 ? date.getYear() : date.getYear() + 1, date.getMonth())) {
                        return false;
                    }
                } else if (date.getDay() > _maxDayInMonth(date.getMonth())) {
                    return false;
                }
            }
        }
        if (date.hasTime() && ((date.getHour() < 0 || date.getHour() > 23 || date.getMinute() < 0 || date.getMinute() > 59 || date.getSecond() < 0 || date.getSecond() > 59 || date.getFraction().signum() < 0 || date.getFraction().compareTo(GDate._one) >= 0) && (date.getHour() != 24 || date.getMinute() != 0 || date.getSecond() != 0 || date.getFraction().compareTo(GDate._zero) != 0))) {
            return false;
        }
        if (!date.hasTimeZone()) {
            return true;
        }
        if (date.getTimeZoneSign() == 0 && date.getTimeZoneHour() == 0 && date.getTimeZoneMinute() == 0) {
            return true;
        }
        if ((date.getTimeZoneSign() != -1 && date.getTimeZoneSign() != 1) || date.getTimeZoneHour() < 0 || date.getTimeZoneMinute() < 0) {
            return false;
        }
        if (date.getTimeZoneHour() == 14 && date.getTimeZoneMinute() == 0) {
            return true;
        }
        if (date.getTimeZoneHour() >= 14 || date.getTimeZoneMinute() >= 60) {
            return false;
        }
        return true;
    }

    public void normalize() {
        if (hasDay() == hasMonth() && hasDay() == hasYear() && hasTimeZone() && hasTime()) {
            normalizeToTimeZone(0, 0, 0);
        } else {
            _normalizeTimeAndDate();
        }
        if (hasTime() && this._fs != null && this._fs.scale() > 0) {
            if (this._fs.signum() == 0) {
                this._fs = GDate._zero;
                return;
            }
            BigInteger bi = this._fs.unscaledValue();
            String str = bi.toString();
            int lastzero = str.length();
            while (lastzero > 0 && str.charAt(lastzero - 1) == '0') {
                lastzero--;
            }
            if (lastzero < str.length()) {
                this._fs = this._fs.setScale((this._fs.scale() - str.length()) + lastzero);
            }
        }
    }

    void normalize24h() {
        if (!hasTime() || getHour() != 24) {
            return;
        }
        _normalizeTimeAndDate();
    }

    private void _normalizeTimeAndDate() {
        long carry = 0;
        if (hasTime()) {
            carry = _normalizeTime();
        }
        if (hasDay()) {
            this._D = (int) (this._D + carry);
        }
        if (hasDate()) {
            _normalizeDate();
            return;
        }
        if (hasMonth()) {
            if (this._M < 1 || this._M > 12) {
                int temp = this._M;
                this._M = _modulo(temp, 1, 13);
                if (hasYear()) {
                    this._CY += (int) _fQuotient(temp, 1, 13);
                }
            }
        }
    }

    public void normalizeToTimeZone(int tzSign, int tzHour, int tzMinute) {
        if ((tzSign != 0 || tzHour != 0 || tzMinute != 0) && ((tzSign != -1 && tzSign != 1) || tzHour < 0 || tzMinute < 0 || ((tzHour != 14 || tzMinute != 0) && (tzHour >= 14 || tzMinute >= 60)))) {
            throw new IllegalArgumentException("time zone must be between -14:00 and +14:00");
        }
        if (!hasTimeZone() || !hasTime()) {
            throw new IllegalStateException("cannot normalize time zone without both time and timezone");
        }
        if (hasDay() != hasMonth() || hasDay() != hasYear()) {
            throw new IllegalStateException("cannot do date math without a complete date");
        }
        int hshift = (tzSign * tzHour) - (this._tzsign * this._tzh);
        int mshift = (tzSign * tzMinute) - (this._tzsign * this._tzm);
        this._tzsign = tzSign;
        this._tzh = tzHour;
        this._tzm = tzMinute;
        addDuration(1, 0, 0, 0, hshift, mshift, 0, null);
    }

    public void normalizeToTimeZone(int tzTotalMinutes) {
        if (tzTotalMinutes < -840 || tzTotalMinutes > 840) {
            throw new IllegalArgumentException("time zone out of range (-840 to 840 minutes). (" + tzTotalMinutes + ")");
        }
        int tzSign = tzTotalMinutes < 0 ? -1 : tzTotalMinutes > 0 ? 1 : 0;
        int tzTotalMinutes2 = tzTotalMinutes * tzSign;
        int tzH = tzTotalMinutes2 / 60;
        int tzM = tzTotalMinutes2 - (tzH * 60);
        normalizeToTimeZone(tzSign, tzH, tzM);
    }

    public void addGDuration(GDurationSpecification duration) {
        addDuration(duration.getSign(), duration.getYear(), duration.getMonth(), duration.getDay(), duration.getHour(), duration.getMinute(), duration.getSecond(), duration.getFraction());
    }

    public void subtractGDuration(GDurationSpecification duration) {
        addDuration(-duration.getSign(), duration.getYear(), duration.getMonth(), duration.getDay(), duration.getHour(), duration.getMinute(), duration.getSecond(), duration.getFraction());
    }

    private void _normalizeDate() {
        if (this._M < 1 || this._M > 12 || this._D < 1 || this._D > _maxDayInMonthFor(this._CY, this._M)) {
            int temp = this._M;
            this._M = _modulo(temp, 1, 13);
            this._CY += (int) _fQuotient(temp, 1, 13);
            int extradays = this._D - 1;
            this._D = 1;
            setJulianDate(getJulianDate() + extradays);
        }
    }

    private long _normalizeTime() {
        long carry = 0;
        if (this._fs != null && (this._fs.signum() < 0 || this._fs.compareTo(GDate._one) >= 0)) {
            BigDecimal bdcarry = this._fs.setScale(0, 3);
            this._fs = this._fs.subtract(bdcarry);
            carry = bdcarry.longValue();
        }
        if (carry != 0 || this._s < 0 || this._s > 59 || this._m < 0 || this._m > 50 || this._h < 0 || this._h > 23) {
            long temp = this._s + carry;
            long carry2 = _fQuotient(temp, 60);
            this._s = _mod(temp, 60, carry2);
            long temp2 = this._m + carry2;
            long carry3 = _fQuotient(temp2, 60);
            this._m = _mod(temp2, 60, carry3);
            long temp3 = this._h + carry3;
            carry = _fQuotient(temp3, 24);
            this._h = _mod(temp3, 24, carry);
        }
        return carry;
    }

    public void addDuration(int sign, int year, int month, int day, int hour, int minute, int second, BigDecimal fraction) {
        boolean timemath = (hour == 0 && minute == 0 && second == 0 && (fraction == null || fraction.signum() == 0)) ? false : true;
        if (timemath && !hasTime()) {
            throw new IllegalStateException("cannot do time math without a complete time");
        }
        boolean datemath = hasDay() && (day != 0 || timemath);
        if (datemath && !hasDate()) {
            throw new IllegalStateException("cannot do date math without a complete date");
        }
        if (month != 0 || year != 0) {
            if (hasDay()) {
                _normalizeDate();
            }
            int temp = this._M + (sign * month);
            this._M = _modulo(temp, 1, 13);
            this._CY = this._CY + (sign * year) + ((int) _fQuotient(temp, 1, 13));
            if (hasDay()) {
                if (!$assertionsDisabled && this._D < 1) {
                    throw new AssertionError();
                }
                int temp2 = _maxDayInMonthFor(this._CY, this._M);
                if (this._D > temp2) {
                    this._D = temp2;
                }
            }
        }
        long carry = 0;
        if (timemath) {
            if (fraction != null && fraction.signum() != 0) {
                if (this._fs.signum() == 0 && sign == 1) {
                    this._fs = fraction;
                } else {
                    this._fs = sign == 1 ? this._fs.add(fraction) : this._fs.subtract(fraction);
                }
            }
            this._s += sign * second;
            this._m += sign * minute;
            this._h += sign * hour;
            carry = _normalizeTime();
        }
        if (datemath) {
            this._D = (int) (this._D + (sign * day) + carry);
            _normalizeDate();
        }
    }

    private static int _maxDayInMonthFor(int year, int month) {
        if (month == 4 || month == 6 || month == 9 || month == 11) {
            return 30;
        }
        if (month == 2) {
            return _isLeapYear(year) ? 29 : 28;
        }
        return 31;
    }

    private static int _maxDayInMonth(int month) {
        if (month == 4 || month == 6 || month == 9 || month == 11) {
            return 30;
        }
        if (month == 2) {
            return 29;
        }
        return 31;
    }

    @Override // org.apache.xmlbeans.GDateSpecification
    public final int getJulianDate() {
        return julianDateForGDate(this);
    }

    public void setJulianDate(int julianday) {
        if (julianday < 0) {
            throw new IllegalArgumentException("date before year -4713");
        }
        int temp = julianday + 68569;
        int qepoc = (4 * temp) / 146097;
        int temp2 = temp - (((146097 * qepoc) + 3) / 4);
        this._CY = (4000 * (temp2 + 1)) / 1461001;
        int temp3 = (temp2 - ((MysqlErrorNumbers.ER_MAX_PREPARED_STMT_COUNT_REACHED * this._CY) / 4)) + 31;
        this._M = (80 * temp3) / 2447;
        this._D = temp3 - ((2447 * this._M) / 80);
        int temp4 = this._M / 11;
        this._M = (this._M + 2) - (12 * temp4);
        this._CY = (100 * (qepoc - 49)) + this._CY + temp4;
        this._bits |= 14;
    }

    public void setDate(Date date) {
        TimeZone dtz = TimeZone.getDefault();
        int offset = dtz.getOffset(date.getTime());
        int offsetsign = 1;
        if (offset < 0) {
            offsetsign = -1;
            offset = -offset;
        }
        int offsetmin = offset / 60000;
        int offsethr = offsetmin / 60;
        int offsetmin2 = offsetmin - (offsethr * 60);
        setTimeZone(offsetsign, offsethr, offsetmin2);
        int roundedoffset = offsetsign * ((offsethr * 60) + offsetmin2) * 60 * 1000;
        setTime(0, 0, 0, GDate._zero);
        this._bits |= 14;
        this._CY = 1970;
        this._M = 1;
        this._D = 1;
        addGDuration(new GDuration(1, 0, 0, 0, 0, 0, 0, BigDecimal.valueOf(date.getTime() + roundedoffset, 3)));
        if (this._fs.signum() == 0) {
            this._fs = GDate._zero;
        }
    }

    public void setGDate(GDateSpecification gdate) {
        this._bits = gdate.getFlags() & 31;
        int year = gdate.getYear();
        this._CY = year > 0 ? year : year + 1;
        this._M = gdate.getMonth();
        this._D = gdate.getDay();
        this._h = gdate.getHour();
        this._m = gdate.getMinute();
        this._s = gdate.getSecond();
        this._fs = gdate.getFraction();
        this._tzsign = gdate.getTimeZoneSign();
        this._tzh = gdate.getTimeZoneHour();
        this._tzm = gdate.getTimeZoneMinute();
    }

    @Override // org.apache.xmlbeans.GDateSpecification
    public XmlCalendar getCalendar() {
        return new XmlCalendar(this);
    }

    @Override // org.apache.xmlbeans.GDateSpecification
    public Date getDate() {
        return dateForGDate(this);
    }

    static int julianDateForGDate(GDateSpecification date) {
        if (!date.hasDate()) {
            throw new IllegalStateException("cannot do date math without a complete date");
        }
        int day = date.getDay();
        int month = date.getMonth();
        int year = date.getYear();
        int year2 = year > 0 ? year : year + 1;
        int result = (((day - 32075) + ((MysqlErrorNumbers.ER_MAX_PREPARED_STMT_COUNT_REACHED * ((year2 + 4800) + ((month - 14) / 12))) / 4)) + ((TokenId.RSHIFT_E * ((month - 2) - (((month - 14) / 12) * 12))) / 12)) - ((3 * (((year2 + 4900) + ((month - 14) / 12)) / 100)) / 4);
        if (result < 0) {
            throw new IllegalStateException("date too far in the past (year allowed to -4713)");
        }
        return result;
    }

    static Date dateForGDate(GDateSpecification date) {
        long to1970Ms;
        long jDate = julianDateForGDate(date);
        long to1970Date = jDate - 2440588;
        long to1970Ms2 = (86400000 * to1970Date) + date.getMillisecond() + (date.getSecond() * 1000) + (date.getMinute() * 60 * 1000) + (date.getHour() * 60 * 60 * 1000);
        if (date.hasTimeZone()) {
            to1970Ms = (to1970Ms2 - (((date.getTimeZoneMinute() * date.getTimeZoneSign()) * 60) * 1000)) - ((((date.getTimeZoneHour() * date.getTimeZoneSign()) * 60) * 60) * 1000);
        } else {
            TimeZone def = TimeZone.getDefault();
            int offset = def.getOffset(to1970Ms2);
            to1970Ms = to1970Ms2 - offset;
        }
        return new Date(to1970Ms);
    }

    private static boolean _isLeapYear(int year) {
        return year % 4 == 0 && (year % 100 != 0 || year % 400 == 0);
    }

    private static final long _fQuotient(long a, int b) {
        if ((a < 0) == (b < 0)) {
            return a / b;
        }
        return -(((b - a) - serialVersionUID) / b);
    }

    private static int _mod(long a, int b, long quotient) {
        return (int) (a - (quotient * b));
    }

    private static final int _modulo(long temp, int low, int high) {
        long a = temp - low;
        int b = high - low;
        return _mod(a, b, _fQuotient(a, b)) + low;
    }

    private static final long _fQuotient(long temp, int low, int high) {
        return _fQuotient(temp - low, high - low);
    }

    private void _setToFirstMoment() {
        if (!hasYear()) {
            setYear(MysqlErrorNumbers.ER_WRONG_PARAMETERS_TO_STORED_FCT);
        }
        if (!hasMonth()) {
            setMonth(1);
        }
        if (!hasDay()) {
            setDay(1);
        }
        if (!hasTime()) {
            setTime(0, 0, 0, GDate._zero);
        }
    }

    @Override // org.apache.xmlbeans.GDateSpecification
    public final int compareToGDate(GDateSpecification datespec) {
        return compareGDate(this, datespec);
    }

    static final int compareGDate(GDateSpecification tdate, GDateSpecification datespec) {
        int bitdiff = tdate.getFlags() ^ datespec.getFlags();
        if ((bitdiff & 31) == 0) {
            if (tdate.hasTimeZone() && (datespec.getTimeZoneHour() != tdate.getTimeZoneHour() || datespec.getTimeZoneMinute() != tdate.getTimeZoneMinute() || datespec.getTimeZoneSign() != tdate.getTimeZoneSign())) {
                datespec = new GDateBuilder(datespec);
                int flags = tdate.getFlags() & 14;
                if ((flags != 0 && flags != 14) || !tdate.hasTime()) {
                    ((GDateBuilder) datespec)._setToFirstMoment();
                    tdate = new GDateBuilder(tdate);
                    ((GDateBuilder) tdate)._setToFirstMoment();
                }
                ((GDateBuilder) datespec).normalizeToTimeZone(tdate.getTimeZoneSign(), tdate.getTimeZoneHour(), tdate.getTimeZoneMinute());
            }
            return fieldwiseCompare(tdate, datespec);
        }
        if ((bitdiff & 30) != 0) {
            return 2;
        }
        if (!tdate.hasTimeZone()) {
            int result = compareGDate(datespec, tdate);
            if (result == 2) {
                return 2;
            }
            return -result;
        }
        GDateBuilder pdate = new GDateBuilder(tdate);
        if ((tdate.getFlags() & 14) == 12) {
            if (tdate.getDay() == 28 && tdate.getMonth() == 2) {
                if (datespec.getDay() == 1 && datespec.getMonth() == 3) {
                    pdate.setDay(29);
                }
            } else if (datespec.getDay() == 28 && datespec.getMonth() == 2 && tdate.getDay() == 1 && tdate.getMonth() == 3) {
                pdate.setMonth(2);
                pdate.setDay(29);
            }
        }
        pdate._setToFirstMoment();
        GDateBuilder qplusdate = new GDateBuilder(datespec);
        qplusdate._setToFirstMoment();
        qplusdate.setTimeZone(1, 14, 0);
        qplusdate.normalizeToTimeZone(tdate.getTimeZoneSign(), tdate.getTimeZoneHour(), tdate.getTimeZoneMinute());
        if (fieldwiseCompare(pdate, qplusdate) == -1) {
            return -1;
        }
        qplusdate.setGDate(datespec);
        qplusdate._setToFirstMoment();
        qplusdate.setTimeZone(-1, 14, 0);
        qplusdate.normalizeToTimeZone(tdate.getTimeZoneSign(), tdate.getTimeZoneHour(), tdate.getTimeZoneMinute());
        if (fieldwiseCompare(pdate, qplusdate) == 1) {
            return 1;
        }
        return 2;
    }

    private static int fieldwiseCompare(GDateSpecification tdate, GDateSpecification date) {
        if (tdate.hasYear()) {
            int CY = date.getYear();
            int TCY = tdate.getYear();
            if (TCY < CY) {
                return -1;
            }
            if (TCY > CY) {
                return 1;
            }
        }
        if (tdate.hasMonth()) {
            int M = date.getMonth();
            int TM = tdate.getMonth();
            if (TM < M) {
                return -1;
            }
            if (TM > M) {
                return 1;
            }
        }
        if (tdate.hasDay()) {
            int D = date.getDay();
            int TD = tdate.getDay();
            if (TD < D) {
                return -1;
            }
            if (TD > D) {
                return 1;
            }
        }
        if (tdate.hasTime()) {
            int h = date.getHour();
            int th = tdate.getHour();
            if (th < h) {
                return -1;
            }
            if (th > h) {
                return 1;
            }
            int m = date.getMinute();
            int tm = tdate.getMinute();
            if (tm < m) {
                return -1;
            }
            if (tm > m) {
                return 1;
            }
            int s = date.getSecond();
            int ts = tdate.getSecond();
            if (ts < s) {
                return -1;
            }
            if (ts > s) {
                return 1;
            }
            BigDecimal fs = date.getFraction();
            BigDecimal tfs = tdate.getFraction();
            if (tfs == null && fs == null) {
                return 0;
            }
            return (tfs == null ? GDate._zero : tfs).compareTo(fs == null ? GDate._zero : fs);
        }
        return 0;
    }

    @Override // org.apache.xmlbeans.GDateSpecification
    public final int getBuiltinTypeCode() {
        return btcForFlags(this._bits);
    }

    static int btcForFlags(int flags) {
        switch (flags & 30) {
            case 2:
                return 18;
            case 3:
            case 5:
            case 7:
            case 9:
            case 10:
            case 11:
            case 13:
            case 15:
            case 17:
            case 18:
            case 19:
            case 20:
            case 21:
            case 22:
            case 23:
            case 24:
            case 25:
            case 26:
            case 27:
            case 28:
            case 29:
            default:
                return 0;
            case 4:
                return 21;
            case 6:
                return 17;
            case 8:
                return 20;
            case 12:
                return 19;
            case 14:
                return 16;
            case 16:
                return 15;
            case 30:
                return 14;
        }
    }

    public void setBuiltinTypeCode(int typeCode) {
        switch (typeCode) {
            case 14:
                return;
            case 15:
                clearYear();
                clearMonth();
                clearDay();
                return;
            case 16:
                clearTime();
                return;
            case 17:
                clearDay();
                clearTime();
                return;
            case 18:
                clearMonth();
                clearDay();
                clearTime();
                return;
            case 19:
                clearYear();
                clearTime();
                return;
            case 20:
                clearYear();
                clearMonth();
                clearTime();
                return;
            case 21:
                clearYear();
                clearDay();
                clearTime();
                return;
            default:
                throw new IllegalArgumentException("codeType must be one of SchemaType BTC_  DATE TIME related types.");
        }
    }

    @Override // org.apache.xmlbeans.GDateSpecification
    public String canonicalString() {
        boolean needNormalize = hasTimeZone() && getTimeZoneSign() != 0 && hasTime() && hasDay() == hasMonth() && hasDay() == hasYear();
        if (!needNormalize && getFraction() != null && getFraction().scale() > 0) {
            BigInteger bi = getFraction().unscaledValue();
            needNormalize = bi.mod(TEN).signum() == 0;
        }
        if (!needNormalize) {
            return toString();
        }
        GDateBuilder cdate = new GDateBuilder(this);
        cdate.normalize();
        return cdate.toString();
    }

    @Override // org.apache.xmlbeans.GDateSpecification
    public final String toString() {
        return GDate.formatGDate(this);
    }
}
