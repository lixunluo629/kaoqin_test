package org.apache.xmlbeans;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import org.apache.commons.lang.time.DateUtils;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/GDate.class */
public final class GDate implements GDateSpecification, Serializable {
    private static final long serialVersionUID = 1;
    static final int MAX_YEAR = 292277265;
    static final int MIN_YEAR = -292275295;
    private transient String _canonicalString;
    private transient String _string;
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
    static final BigDecimal _zero;
    static final BigDecimal _one;
    private static final char[] _tensDigit;
    private static final char[] _onesDigit;
    private static final TimeZone GMTZONE;
    private static final TimeZone[] MINUSZONE;
    private static final TimeZone[] PLUSZONE;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !GDate.class.desiredAssertionStatus();
        _zero = BigDecimal.valueOf(0L);
        _one = BigDecimal.valueOf(serialVersionUID);
        _tensDigit = new char[]{'0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '2', '2', '2', '2', '2', '2', '2', '2', '2', '2', '3', '3', '3', '3', '3', '3', '3', '3', '3', '3', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '6', '6', '6', '6', '6', '6', '6', '6', '6', '6', '7', '7', '7', '7', '7', '7', '7', '7', '7', '7', '8', '8', '8', '8', '8', '8', '8', '8', '8', '8', '9', '9', '9', '9', '9', '9', '9', '9', '9', '9'};
        _onesDigit = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
        GMTZONE = TimeZone.getTimeZone("GMT");
        MINUSZONE = new TimeZone[]{TimeZone.getTimeZone("GMT-00:00"), TimeZone.getTimeZone("GMT-01:00"), TimeZone.getTimeZone("GMT-02:00"), TimeZone.getTimeZone("GMT-03:00"), TimeZone.getTimeZone("GMT-04:00"), TimeZone.getTimeZone("GMT-05:00"), TimeZone.getTimeZone("GMT-06:00"), TimeZone.getTimeZone("GMT-07:00"), TimeZone.getTimeZone("GMT-08:00"), TimeZone.getTimeZone("GMT-09:00"), TimeZone.getTimeZone("GMT-10:00"), TimeZone.getTimeZone("GMT-11:00"), TimeZone.getTimeZone("GMT-12:00"), TimeZone.getTimeZone("GMT-13:00"), TimeZone.getTimeZone("GMT-14:00")};
        PLUSZONE = new TimeZone[]{TimeZone.getTimeZone("GMT+00:00"), TimeZone.getTimeZone("GMT+01:00"), TimeZone.getTimeZone("GMT+02:00"), TimeZone.getTimeZone("GMT+03:00"), TimeZone.getTimeZone("GMT+04:00"), TimeZone.getTimeZone("GMT+05:00"), TimeZone.getTimeZone("GMT+06:00"), TimeZone.getTimeZone("GMT+07:00"), TimeZone.getTimeZone("GMT+08:00"), TimeZone.getTimeZone("GMT+09:00"), TimeZone.getTimeZone("GMT+10:00"), TimeZone.getTimeZone("GMT+11:00"), TimeZone.getTimeZone("GMT+12:00"), TimeZone.getTimeZone("GMT+13:00"), TimeZone.getTimeZone("GMT+14:00")};
    }

    public GDate(CharSequence string) {
        int tzsign;
        int value;
        int value2;
        int len = string.length();
        int start = 0;
        while (len > 0 && isSpace(string.charAt(len - 1))) {
            len--;
        }
        while (start < len && isSpace(string.charAt(start))) {
            start++;
        }
        if (len - start >= 1 && string.charAt(len - 1) == 'Z') {
            this._bits |= 1;
            len--;
        } else if (len - start >= 6 && string.charAt(len - 3) == ':') {
            switch (string.charAt(len - 6)) {
                case '+':
                    tzsign = 1;
                    break;
                case '-':
                    tzsign = -1;
                    break;
            }
            int tzhour = twoDigit(string, len - 5);
            int tzminute = twoDigit(string, len - 2);
            if (tzhour > 14) {
                throw new IllegalArgumentException("time zone hour must be two digits between -14 and +14");
            }
            if (tzminute > 59) {
                throw new IllegalArgumentException("time zone minute must be two digits between 00 and 59");
            }
            this._bits |= 1;
            this._tzsign = tzsign;
            this._tzh = tzhour;
            this._tzm = tzminute;
            len -= 6;
        }
        if (start < len && (start + 2 >= len || string.charAt(start + 2) != ':')) {
            boolean negyear = false;
            if (start < len && string.charAt(start) == '-') {
                negyear = true;
                start++;
            }
            int value3 = 0;
            int digits = -start;
            boolean startsWithZero = start < len && digitVal(string.charAt(start)) == 0;
            while (true) {
                char ch2 = start < len ? string.charAt(start) : (char) 0;
                if (isDigit(ch2)) {
                    if (startsWithZero && start + digits >= 4) {
                        throw new IllegalArgumentException("year value starting with zero must be 4 or less digits: " + ((Object) string));
                    }
                    value3 = (value3 * 10) + digitVal(ch2);
                    start++;
                } else {
                    int digits2 = digits + start;
                    if (digits2 > 9) {
                        throw new IllegalArgumentException("year too long (up to 9 digits)");
                    }
                    if (digits2 >= 4) {
                        this._bits |= 2;
                        this._CY = negyear ? -value3 : value3;
                        if (this._CY == 0) {
                            throw new IllegalArgumentException("year must not be zero");
                        }
                    } else if (digits2 > 0) {
                        throw new IllegalArgumentException("year must be four digits (may pad with zeroes, e.g., 0560)");
                    }
                    if (this._CY > MAX_YEAR) {
                        throw new IllegalArgumentException("year value not supported: too big, must be less than 292277265");
                    }
                    if (this._CY < MIN_YEAR) {
                        throw new IllegalArgumentException("year values not supported: too small, must be bigger than -292275295");
                    }
                    if (ch2 != '-') {
                        if (negyear && !hasYear()) {
                            throw new IllegalArgumentException();
                        }
                    } else {
                        start++;
                        if (len - start >= 2 && (value2 = twoDigit(string, start)) >= 1 && value2 <= 12) {
                            this._bits |= 4;
                            this._M = value2;
                            start += 2;
                        }
                        if ((start < len ? string.charAt(start) : (char) 0) != '-') {
                            if (!hasMonth()) {
                                throw new IllegalArgumentException();
                            }
                        } else {
                            start++;
                            if (len - start >= 2 && (value = twoDigit(string, start)) >= 1 && value <= 31) {
                                this._bits |= 8;
                                this._D = value;
                                start += 2;
                            }
                            if (!hasDay()) {
                                if (hasMonth() && !hasYear()) {
                                    if ((start < len ? string.charAt(start) : (char) 0) == '-') {
                                        start++;
                                    }
                                }
                                throw new IllegalArgumentException();
                            }
                        }
                    }
                }
            }
        }
        if (start < len) {
            if (hasYear() || hasMonth() || hasDay()) {
                if (string.charAt(start) != 'T') {
                    throw new IllegalArgumentException("date and time must be separated by 'T'");
                }
                start++;
            }
            if (len < start + 8 || string.charAt(start + 2) != ':' || string.charAt(start + 5) != ':') {
                throw new IllegalArgumentException();
            }
            int h = twoDigit(string, start);
            if (h > 24) {
                throw new IllegalArgumentException("hour must be between 00 and 23");
            }
            int m = twoDigit(string, start + 3);
            if (m >= 60) {
                throw new IllegalArgumentException("minute must be between 00 and 59");
            }
            int s = twoDigit(string, start + 6);
            if (s >= 60) {
                throw new IllegalArgumentException("second must be between 00 and 59");
            }
            int start2 = start + 8;
            BigDecimal fs = _zero;
            if (start2 < len) {
                if (string.charAt(start2) != '.') {
                    throw new IllegalArgumentException();
                }
                if (start2 + 1 < len) {
                    for (int i = start2 + 1; i < len; i++) {
                        if (!isDigit(string.charAt(i))) {
                            throw new IllegalArgumentException();
                        }
                    }
                    try {
                        fs = new BigDecimal(string.subSequence(start2, len).toString());
                    } finally {
                        IllegalArgumentException illegalArgumentException = new IllegalArgumentException();
                    }
                }
            }
            this._bits |= 16;
            this._h = h;
            this._m = m;
            this._s = s;
            this._fs = fs;
        }
        if (hasTime() && this._h == 24) {
            if (this._m != 0 || this._s != 0 || this._fs.compareTo(_zero) != 0) {
                throw new IllegalArgumentException("if hour is 24, minutes, seconds and fraction must be 0");
            }
            if (hasDate()) {
                GDateBuilder gdb = new GDateBuilder(this._CY, this._M, this._D, this._h, this._m, this._s, this._fs, this._tzsign, this._tzh, this._tzm);
                gdb.normalize24h();
                this._D = gdb.getDay();
                this._M = gdb.getMonth();
                this._CY = gdb.getYear();
                this._h = 0;
            } else if (hasDay()) {
                this._D++;
                this._h = 0;
            }
        }
        if (!isValid()) {
            throw new IllegalArgumentException("invalid date");
        }
    }

    public GDate(int year, int month, int day, int hour, int minute, int second, BigDecimal fraction) {
        this._bits = 30;
        this._CY = year;
        this._M = month;
        this._D = day;
        this._h = hour;
        this._m = minute;
        this._s = second;
        this._fs = fraction == null ? _zero : fraction;
        if (!isValid()) {
            throw new IllegalArgumentException();
        }
    }

    public GDate(int year, int month, int day, int hour, int minute, int second, BigDecimal fraction, int tzSign, int tzHour, int tzMinute) {
        this._bits = 31;
        this._CY = year;
        this._M = month;
        this._D = day;
        this._h = hour;
        this._m = minute;
        this._s = second;
        this._fs = fraction == null ? _zero : fraction;
        this._tzsign = tzSign;
        this._tzh = tzHour;
        this._tzm = tzMinute;
        if (!isValid()) {
            throw new IllegalArgumentException();
        }
    }

    public GDate(Date date) {
        this(new GDateBuilder(date));
    }

    public GDate(Calendar calendar) {
        boolean isSetYear = calendar.isSet(1);
        boolean isSetEra = calendar.isSet(0);
        boolean isSetMonth = calendar.isSet(2);
        boolean isSetDay = calendar.isSet(5);
        boolean isSetHourOfDay = calendar.isSet(11);
        boolean isSetHour = calendar.isSet(10);
        boolean isSetAmPm = calendar.isSet(9);
        boolean isSetMinute = calendar.isSet(12);
        boolean isSetSecond = calendar.isSet(13);
        boolean isSetMillis = calendar.isSet(14);
        boolean isSetZone = calendar.isSet(15);
        boolean isSetDst = calendar.isSet(16);
        if (isSetYear) {
            int y = calendar.get(1);
            if (isSetEra && (calendar instanceof GregorianCalendar) && calendar.get(0) == 0) {
                y = -y;
            }
            this._bits |= 2;
            this._CY = y;
        }
        if (isSetMonth) {
            this._bits |= 4;
            this._M = calendar.get(2) + 1;
        }
        if (isSetDay) {
            this._bits |= 8;
            this._D = calendar.get(5);
        }
        boolean gotTime = false;
        int h = 0;
        int m = 0;
        int s = 0;
        BigDecimal fs = _zero;
        if (isSetHourOfDay) {
            h = calendar.get(11);
            gotTime = true;
        } else if (isSetHour && isSetAmPm) {
            h = calendar.get(10) + (calendar.get(9) * 12);
            gotTime = true;
        }
        if (isSetMinute) {
            m = calendar.get(12);
            gotTime = true;
        }
        if (isSetSecond) {
            s = calendar.get(13);
            gotTime = true;
        }
        if (isSetMillis) {
            fs = BigDecimal.valueOf(calendar.get(14), 3);
            gotTime = true;
        }
        if (gotTime) {
            this._bits |= 16;
            this._h = h;
            this._m = m;
            this._s = s;
            this._fs = fs;
        }
        if (isSetZone) {
            int zoneOffsetInMilliseconds = calendar.get(15);
            zoneOffsetInMilliseconds = isSetDst ? zoneOffsetInMilliseconds + calendar.get(16) : zoneOffsetInMilliseconds;
            this._bits |= 1;
            if (zoneOffsetInMilliseconds == 0) {
                this._tzsign = 0;
                this._tzh = 0;
                this._tzm = 0;
                TimeZone zone = calendar.getTimeZone();
                String id = zone.getID();
                if (id != null && id.length() > 3) {
                    switch (id.charAt(3)) {
                        case '+':
                            this._tzsign = 1;
                            break;
                        case '-':
                            this._tzsign = -1;
                            break;
                    }
                }
                return;
            }
            this._tzsign = zoneOffsetInMilliseconds < 0 ? -1 : 1;
            int zoneOffsetInMilliseconds2 = zoneOffsetInMilliseconds * this._tzsign;
            this._tzh = zoneOffsetInMilliseconds2 / DateUtils.MILLIS_IN_HOUR;
            this._tzm = (zoneOffsetInMilliseconds2 - (this._tzh * DateUtils.MILLIS_IN_HOUR)) / 60000;
        }
    }

    public GDate(GDateSpecification gdate) {
        if (gdate.hasTimeZone()) {
            this._bits |= 1;
            this._tzsign = gdate.getTimeZoneSign();
            this._tzh = gdate.getTimeZoneHour();
            this._tzm = gdate.getTimeZoneMinute();
        }
        if (gdate.hasTime()) {
            this._bits |= 16;
            this._h = gdate.getHour();
            this._m = gdate.getMinute();
            this._s = gdate.getSecond();
            this._fs = gdate.getFraction();
        }
        if (gdate.hasDay()) {
            this._bits |= 8;
            this._D = gdate.getDay();
        }
        if (gdate.hasMonth()) {
            this._bits |= 4;
            this._M = gdate.getMonth();
        }
        if (gdate.hasYear()) {
            this._bits |= 2;
            this._CY = gdate.getYear();
        }
    }

    static final boolean isDigit(char ch2) {
        return ((char) (ch2 - '0')) <= '\t';
    }

    static final boolean isSpace(char ch2) {
        switch (ch2) {
            case '\t':
            case '\n':
            case '\r':
            case ' ':
                return true;
            default:
                return false;
        }
    }

    static final int digitVal(char ch2) {
        return ch2 - '0';
    }

    private static final int twoDigit(CharSequence str, int index) {
        char ch1 = str.charAt(index);
        char ch2 = str.charAt(index + 1);
        if (!isDigit(ch1) || !isDigit(ch2)) {
            return 100;
        }
        return (digitVal(ch1) * 10) + digitVal(ch2);
    }

    @Override // org.apache.xmlbeans.GDateSpecification
    public final boolean isImmutable() {
        return true;
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
        return this._CY;
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

    @Override // org.apache.xmlbeans.GDateSpecification
    public int getMillisecond() {
        if (this._fs == null) {
            return 0;
        }
        return this._fs.setScale(3, 1).unscaledValue().intValue();
    }

    @Override // org.apache.xmlbeans.GDateSpecification
    public String canonicalString() {
        ensureCanonicalString();
        return this._canonicalString;
    }

    @Override // org.apache.xmlbeans.GDateSpecification
    public boolean isValid() {
        return GDateBuilder.isValidGDate(this);
    }

    @Override // org.apache.xmlbeans.GDateSpecification
    public int getJulianDate() {
        return GDateBuilder.julianDateForGDate(this);
    }

    @Override // org.apache.xmlbeans.GDateSpecification
    public XmlCalendar getCalendar() {
        return new XmlCalendar(this);
    }

    @Override // org.apache.xmlbeans.GDateSpecification
    public Date getDate() {
        return GDateBuilder.dateForGDate(this);
    }

    @Override // org.apache.xmlbeans.GDateSpecification
    public int compareToGDate(GDateSpecification datespec) {
        return GDateBuilder.compareGDate(this, datespec);
    }

    @Override // org.apache.xmlbeans.GDateSpecification
    public int getBuiltinTypeCode() {
        return GDateBuilder.btcForFlags(this._bits);
    }

    public GDate add(GDurationSpecification duration) {
        GDateBuilder builder = new GDateBuilder(this);
        builder.addGDuration(duration);
        return builder.toGDate();
    }

    public GDate subtract(GDurationSpecification duration) {
        GDateBuilder builder = new GDateBuilder(this);
        builder.subtractGDuration(duration);
        return builder.toGDate();
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof GDate)) {
            return false;
        }
        ensureCanonicalString();
        return this._canonicalString.equals(((GDate) obj).canonicalString());
    }

    public int hashCode() {
        ensureCanonicalString();
        return this._canonicalString.hashCode();
    }

    private void ensureCanonicalString() {
        if (this._canonicalString != null) {
            return;
        }
        boolean needNormalize = hasTimeZone() && getTimeZoneSign() != 0 && hasTime() && hasDay() == hasMonth() && hasDay() == hasYear();
        if (!needNormalize && getFraction() != null && getFraction().scale() > 0) {
            BigInteger bi = getFraction().unscaledValue();
            needNormalize = bi.mod(GDateBuilder.TEN).signum() == 0;
        }
        if (!needNormalize) {
            this._canonicalString = toString();
            return;
        }
        GDateBuilder gdb = new GDateBuilder(this);
        gdb.normalize();
        this._canonicalString = gdb.toString();
    }

    @Override // org.apache.xmlbeans.GDateSpecification
    public String toString() {
        if (this._string == null) {
            this._string = formatGDate(this);
        }
        return this._string;
    }

    private static final int _padTwoAppend(char[] b, int i, int n) {
        if (!$assertionsDisabled && (n < 0 || n >= 100)) {
            throw new AssertionError();
        }
        b[i] = _tensDigit[n];
        b[i + 1] = _onesDigit[n];
        return i + 2;
    }

    private static final int _padFourAppend(char[] b, int i, int n) {
        if (n < 0) {
            i++;
            b[i] = '-';
            n = -n;
        }
        if (n >= 10000) {
            String s = Integer.toString(n);
            s.getChars(0, s.length(), b, i);
            return i + s.length();
        }
        int q = n / 100;
        int r = n - (q * 100);
        b[i] = _tensDigit[q];
        b[i + 1] = _onesDigit[q];
        b[i + 2] = _tensDigit[r];
        b[i + 3] = _onesDigit[r];
        return i + 4;
    }

    static final TimeZone timeZoneForGDate(GDateSpecification date) {
        if (!date.hasTimeZone()) {
            return TimeZone.getDefault();
        }
        if (date.getTimeZoneSign() == 0) {
            return GMTZONE;
        }
        if (date.getTimeZoneMinute() == 0 && date.getTimeZoneHour() <= 14 && date.getTimeZoneHour() >= 0) {
            return date.getTimeZoneSign() < 0 ? MINUSZONE[date.getTimeZoneHour()] : PLUSZONE[date.getTimeZoneHour()];
        }
        char[] zb = new char[9];
        zb[0] = 'G';
        zb[1] = 'M';
        zb[2] = 'T';
        zb[3] = date.getTimeZoneSign() < 0 ? '-' : '+';
        _padTwoAppend(zb, 4, date.getTimeZoneHour());
        zb[6] = ':';
        _padTwoAppend(zb, 7, date.getTimeZoneMinute());
        return TimeZone.getTimeZone(new String(zb));
    }

    static String formatGDate(GDateSpecification spec) {
        String frac;
        int point;
        BigDecimal fs = spec.getFraction();
        char[] message = new char[33 + (fs == null ? 0 : fs.scale())];
        int i = 0;
        if (spec.hasYear() || spec.hasMonth() || spec.hasDay()) {
            if (!spec.hasYear()) {
                i = 0 + 1;
                message[0] = '-';
            } else {
                i = _padFourAppend(message, 0, spec.getYear());
            }
            if (spec.hasMonth() || spec.hasDay()) {
                int i2 = i;
                i++;
                message[i2] = '-';
                if (spec.hasMonth()) {
                    i = _padTwoAppend(message, i, spec.getMonth());
                }
                if (spec.hasDay()) {
                    message[i] = '-';
                    i = _padTwoAppend(message, i + 1, spec.getDay());
                }
            }
            if (spec.hasTime()) {
                int i3 = i;
                i++;
                message[i3] = 'T';
            }
        }
        if (spec.hasTime()) {
            int i4 = _padTwoAppend(message, i, spec.getHour());
            message[i4] = ':';
            int i5 = _padTwoAppend(message, i4 + 1, spec.getMinute());
            message[i5] = ':';
            i = _padTwoAppend(message, i5 + 1, spec.getSecond());
            if (fs != _zero && (point = (frac = fs.toString()).indexOf(46)) >= 0) {
                frac.getChars(point, frac.length(), message, i);
                i += frac.length() - point;
            }
        }
        if (spec.hasTimeZone()) {
            if (spec.getTimeZoneSign() == 0) {
                int i6 = i;
                i++;
                message[i6] = 'Z';
            } else {
                int i7 = i;
                int i8 = i + 1;
                message[i7] = spec.getTimeZoneSign() > 0 ? '+' : '-';
                int i9 = _padTwoAppend(message, i8, spec.getTimeZoneHour());
                message[i9] = ':';
                i = _padTwoAppend(message, i9 + 1, spec.getTimeZoneMinute());
            }
        }
        return new String(message, 0, i);
    }
}
