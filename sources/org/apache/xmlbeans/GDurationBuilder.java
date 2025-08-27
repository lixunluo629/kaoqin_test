package org.apache.xmlbeans;

import com.mysql.jdbc.MysqlErrorNumbers;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/GDurationBuilder.class */
public class GDurationBuilder implements GDurationSpecification, Serializable {
    private static final long serialVersionUID = 1;
    private int _sign;
    private int _CY;
    private int _M;
    private int _D;
    private int _h;
    private int _m;
    private int _s;
    private BigDecimal _fs;
    private static final GDate[] _compDate = {new GDate(MysqlErrorNumbers.ER_FAILED_READ_FROM_PAR_FILE, 9, 1, 0, 0, 0, null, 0, 0, 0), new GDate(MysqlErrorNumbers.ER_VALUES_IS_NOT_INT_TYPE_ERROR, 2, 1, 0, 0, 0, null, 0, 0, 0), new GDate(MysqlErrorNumbers.ER_INVALID_ARGUMENT_FOR_LOGARITHM, 3, 1, 0, 0, 0, null, 0, 0, 0), new GDate(MysqlErrorNumbers.ER_INVALID_ARGUMENT_FOR_LOGARITHM, 7, 1, 0, 0, 0, null, 0, 0, 0)};

    public GDurationBuilder() {
        this._sign = 1;
        this._fs = GDate._zero;
    }

    public GDurationBuilder(String s) {
        this(new GDuration(s));
    }

    public GDurationBuilder(int sign, int year, int month, int day, int hour, int minute, int second, BigDecimal fraction) {
        if (sign != 1 && sign != -1) {
            throw new IllegalArgumentException();
        }
        this._sign = sign;
        this._CY = year;
        this._M = month;
        this._D = day;
        this._h = hour;
        this._m = minute;
        this._s = second;
        this._fs = fraction == null ? GDate._zero : fraction;
    }

    public GDurationBuilder(GDurationSpecification gDuration) {
        this._sign = gDuration.getSign();
        this._CY = gDuration.getYear();
        this._M = gDuration.getMonth();
        this._D = gDuration.getDay();
        this._h = gDuration.getHour();
        this._m = gDuration.getMinute();
        this._s = gDuration.getSecond();
        this._fs = gDuration.getFraction();
    }

    public Object clone() {
        return new GDurationBuilder(this);
    }

    public GDuration toGDuration() {
        return new GDuration(this);
    }

    public void addGDuration(GDurationSpecification duration) {
        int sign = this._sign * duration.getSign();
        _add(duration, sign);
    }

    public void subtractGDuration(GDurationSpecification duration) {
        int sign = (-this._sign) * duration.getSign();
        _add(duration, sign);
    }

    private void _add(GDurationSpecification duration, int sign) {
        this._CY += sign * duration.getYear();
        this._M += sign * duration.getMonth();
        this._D += sign * duration.getDay();
        this._h += sign * duration.getHour();
        this._m += sign * duration.getMinute();
        this._s += sign * duration.getSecond();
        if (duration.getFraction().signum() == 0) {
            return;
        }
        if (this._fs.signum() == 0 && sign == 1) {
            this._fs = duration.getFraction();
        } else {
            this._fs = sign > 0 ? this._fs.add(duration.getFraction()) : this._fs.subtract(duration.getFraction());
        }
    }

    public final void setSign(int sign) {
        if (sign != 1 && sign != -1) {
            throw new IllegalArgumentException();
        }
        this._sign = sign;
    }

    public void setYear(int year) {
        this._CY = year;
    }

    public void setMonth(int month) {
        this._M = month;
    }

    public void setDay(int day) {
        this._D = day;
    }

    public void setHour(int hour) {
        this._h = hour;
    }

    public void setMinute(int minute) {
        this._m = minute;
    }

    public void setSecond(int second) {
        this._s = second;
    }

    public void setFraction(BigDecimal fraction) {
        this._fs = fraction == null ? GDate._zero : fraction;
    }

    @Override // org.apache.xmlbeans.GDurationSpecification
    public final boolean isImmutable() {
        return true;
    }

    @Override // org.apache.xmlbeans.GDurationSpecification
    public final int getSign() {
        return this._sign;
    }

    @Override // org.apache.xmlbeans.GDurationSpecification
    public final int getYear() {
        return this._CY;
    }

    @Override // org.apache.xmlbeans.GDurationSpecification
    public final int getMonth() {
        return this._M;
    }

    @Override // org.apache.xmlbeans.GDurationSpecification
    public final int getDay() {
        return this._D;
    }

    @Override // org.apache.xmlbeans.GDurationSpecification
    public final int getHour() {
        return this._h;
    }

    @Override // org.apache.xmlbeans.GDurationSpecification
    public final int getMinute() {
        return this._m;
    }

    @Override // org.apache.xmlbeans.GDurationSpecification
    public final int getSecond() {
        return this._s;
    }

    @Override // org.apache.xmlbeans.GDurationSpecification
    public BigDecimal getFraction() {
        return this._fs;
    }

    @Override // org.apache.xmlbeans.GDurationSpecification
    public boolean isValid() {
        return isValidDuration(this);
    }

    public void normalize() {
        _normalizeImpl(true);
    }

    private static final long _fQuotient(long a, int b) {
        if ((a < 0) == (b < 0)) {
            return a / b;
        }
        return -(((b - a) - serialVersionUID) / b);
    }

    private static final int _mod(long a, int b, long quotient) {
        return (int) (a - (quotient * b));
    }

    private void _normalizeImpl(boolean adjustSign) {
        if (this._M < 0 || this._M > 11) {
            long temp = this._M;
            long ycarry = _fQuotient(temp, 12);
            this._M = _mod(temp, 12, ycarry);
            this._CY = (int) (this._CY + ycarry);
        }
        long carry = 0;
        if (this._fs != null && (this._fs.signum() < 0 || this._fs.compareTo(GDate._one) >= 0)) {
            BigDecimal bdcarry = this._fs.setScale(0, 3);
            this._fs = this._fs.subtract(bdcarry);
            carry = bdcarry.intValue();
        }
        if (carry != 0 || this._s < 0 || this._s > 59 || this._m < 0 || this._m > 50 || this._h < 0 || this._h > 23) {
            long temp2 = this._s + carry;
            long carry2 = _fQuotient(temp2, 60);
            this._s = _mod(temp2, 60, carry2);
            long temp3 = this._m + carry2;
            long carry3 = _fQuotient(temp3, 60);
            this._m = _mod(temp3, 60, carry3);
            long temp4 = this._h + carry3;
            long carry4 = _fQuotient(temp4, 24);
            this._h = _mod(temp4, 24, carry4);
            this._D = (int) (this._D + carry4);
        }
        if (this._CY == 0 && this._M == 0 && this._D == 0 && this._h == 0 && this._m == 0 && this._s == 0 && (this._fs == null || this._fs.signum() == 0)) {
            this._sign = 1;
        }
        if (adjustSign) {
            if (this._D < 0 || this._CY < 0) {
                int sign = (this._D > 0 || (this._CY >= 0 && !(this._CY == 0 && this._M == 0))) ? _getTotalSignSlowly() : -this._sign;
                if (sign == 2) {
                    sign = this._CY < 0 ? -this._sign : this._sign;
                }
                if (sign == 0) {
                    sign = 1;
                }
                if (sign != this._sign) {
                    this._sign = sign;
                    this._CY = -this._CY;
                    this._M = -this._M;
                    this._D = -this._D;
                    this._h = -this._h;
                    this._m = -this._m;
                    this._s = -this._s;
                    if (this._fs != null) {
                        this._fs = this._fs.negate();
                    }
                }
                _normalizeImpl(false);
            }
        }
    }

    static boolean isValidDuration(GDurationSpecification spec) {
        return (spec.getSign() == 1 || spec.getSign() == -1) && spec.getYear() >= 0 && spec.getMonth() >= 0 && spec.getDay() >= 0 && spec.getHour() >= 0 && spec.getMinute() >= 0 && spec.getSecond() >= 0 && spec.getFraction().signum() >= 0;
    }

    @Override // org.apache.xmlbeans.GDurationSpecification
    public final int compareToGDuration(GDurationSpecification duration) {
        return compareDurations(this, duration);
    }

    public String toString() {
        return formatDuration(this);
    }

    static int compareDurations(GDurationSpecification d1, GDurationSpecification d2) {
        if (d1.getFraction().signum() == 0 && d2.getFraction().signum() == 0) {
            int s1 = d1.getSign();
            int s2 = d2.getSign();
            long month1 = s1 * ((d1.getYear() * 12) + d1.getMonth());
            long month2 = s2 * ((d2.getYear() * 12) + d2.getMonth());
            long sec1 = s1 * ((((((d1.getDay() * 24) + d1.getHour()) * 60) + d1.getMinute()) * 60) + d1.getSecond());
            long sec2 = s2 * ((((((d2.getDay() * 24) + d2.getHour()) * 60) + d2.getMinute()) * 60) + d2.getSecond());
            if (month1 == month2) {
                if (sec1 == sec2) {
                    return 0;
                }
                if (sec1 < sec2) {
                    return -1;
                }
                if (sec1 > sec2) {
                    return 1;
                }
            }
            if (month1 < month2 && sec1 - sec2 < 2419200) {
                return -1;
            }
            if (month1 > month2 && sec2 - sec1 < 2419200) {
                return 1;
            }
        }
        GDurationBuilder diff = new GDurationBuilder(d1);
        diff.subtractGDuration(d2);
        return diff._getTotalSignSlowly();
    }

    private int _getTotalSignSlowly() {
        int pos = 0;
        int neg = 0;
        int zer = 0;
        GDateBuilder enddate = new GDateBuilder();
        for (int i = 0; i < _compDate.length; i++) {
            enddate.setGDate(_compDate[i]);
            enddate.addGDuration(this);
            switch (enddate.compareToGDate(_compDate[i])) {
                case -1:
                    neg++;
                    break;
                case 0:
                    zer++;
                    break;
                case 1:
                    pos++;
                    break;
            }
        }
        if (pos == _compDate.length) {
            return 1;
        }
        if (neg == _compDate.length) {
            return -1;
        }
        if (zer == _compDate.length) {
            return 0;
        }
        return 2;
    }

    static String formatDuration(GDurationSpecification duration) {
        StringBuffer message = new StringBuffer(30);
        if (duration.getSign() < 0) {
            message.append('-');
        }
        message.append('P');
        if (duration.getYear() != 0) {
            message.append(duration.getYear());
            message.append('Y');
        }
        if (duration.getMonth() != 0) {
            message.append(duration.getMonth());
            message.append('M');
        }
        if (duration.getDay() != 0) {
            message.append(duration.getDay());
            message.append('D');
        }
        if (duration.getHour() != 0 || duration.getMinute() != 0 || duration.getSecond() != 0 || duration.getFraction().signum() != 0) {
            message.append('T');
        }
        if (duration.getHour() != 0) {
            message.append(duration.getHour());
            message.append('H');
        }
        if (duration.getMinute() != 0) {
            message.append(duration.getMinute());
            message.append('M');
        }
        if (duration.getFraction().signum() != 0) {
            BigDecimal s = duration.getFraction();
            if (duration.getSecond() != 0) {
                s = s.add(BigDecimal.valueOf(duration.getSecond()));
            }
            message.append(stripTrailingZeros(toPlainString(s)));
            message.append('S');
        } else if (duration.getSecond() != 0) {
            message.append(duration.getSecond());
            message.append('S');
        } else if (message.length() <= 2) {
            message.append("T0S");
        }
        return message.toString();
    }

    public static String toPlainString(BigDecimal bd) {
        BigInteger intVal = bd.unscaledValue();
        int scale = bd.scale();
        String intValStr = intVal.toString();
        if (scale == 0) {
            return intValStr;
        }
        boolean isNegative = intValStr.charAt(0) == '-';
        int point = (intValStr.length() - scale) - (isNegative ? 1 : 0);
        StringBuffer sb = new StringBuffer(intValStr.length() + 2 + (point <= 0 ? (-point) + 1 : 0));
        if (point <= 0) {
            if (isNegative) {
                sb.append('-');
            }
            sb.append('0').append('.');
            while (point < 0) {
                sb.append('0');
                point++;
            }
            sb.append(intValStr.substring(isNegative ? 1 : 0));
        } else if (point < intValStr.length()) {
            sb.append(intValStr);
            sb.insert(point + (isNegative ? 1 : 0), '.');
        } else {
            sb.append(intValStr);
            if (!intVal.equals(BigInteger.ZERO)) {
                for (int i = intValStr.length(); i < point; i++) {
                    sb.append('0');
                }
            }
        }
        return sb.toString();
    }

    /* JADX WARN: Code restructure failed: missing block: B:20:0x004b, code lost:
    
        if (r6 == false) goto L22;
     */
    /* JADX WARN: Code restructure failed: missing block: B:23:0x005a, code lost:
    
        return r5;
     */
    /* JADX WARN: Code restructure failed: missing block: B:30:?, code lost:
    
        return r5.substring(0, r8 + 1);
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static java.lang.String stripTrailingZeros(java.lang.String r5) {
        /*
            r0 = 0
            r6 = r0
            r0 = r5
            int r0 = r0.length()
            r1 = 1
            int r0 = r0 - r1
            r7 = r0
            r0 = r7
            r8 = r0
        Lb:
            r0 = r7
            if (r0 < 0) goto L25
            r0 = r5
            r1 = r7
            char r0 = r0.charAt(r1)
            r1 = 48
            if (r0 == r1) goto L1c
            goto L25
        L1c:
            int r7 = r7 + (-1)
            int r8 = r8 + (-1)
            goto Lb
        L25:
            r0 = r7
            if (r0 < 0) goto L4a
            r0 = r5
            r1 = r7
            char r0 = r0.charAt(r1)
            r1 = 69
            if (r0 != r1) goto L35
            r0 = r5
            return r0
        L35:
            r0 = r5
            r1 = r7
            char r0 = r0.charAt(r1)
            r1 = 46
            if (r0 != r1) goto L44
            r0 = 1
            r6 = r0
            goto L4a
        L44:
            int r7 = r7 + (-1)
            goto L25
        L4a:
            r0 = r6
            if (r0 == 0) goto L59
            r0 = r5
            r1 = 0
            r2 = r8
            r3 = 1
            int r2 = r2 + r3
            java.lang.String r0 = r0.substring(r1, r2)
            goto L5a
        L59:
            r0 = r5
        L5a:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.xmlbeans.GDurationBuilder.stripTrailingZeros(java.lang.String):java.lang.String");
    }
}
