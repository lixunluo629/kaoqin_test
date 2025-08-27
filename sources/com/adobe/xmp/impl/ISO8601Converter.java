package com.adobe.xmp.impl;

import com.adobe.xmp.XMPDateTime;
import com.adobe.xmp.XMPException;
import com.drew.metadata.exif.makernotes.NikonType2MakernoteDirectory;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import java.util.SimpleTimeZone;
import org.apache.commons.compress.archivers.tar.TarConstants;
import org.apache.commons.lang.time.DateUtils;

/* loaded from: xmpcore-5.1.3.jar:com/adobe/xmp/impl/ISO8601Converter.class */
public final class ISO8601Converter {
    private ISO8601Converter() {
    }

    public static XMPDateTime parse(String str) throws XMPException {
        return parse(str, new XMPDateTimeImpl());
    }

    public static XMPDateTime parse(String str, XMPDateTime xMPDateTime) throws XMPException {
        if (str == null) {
            throw new XMPException("Parameter must not be null", 4);
        }
        if (str.length() == 0) {
            return xMPDateTime;
        }
        ParseState parseState = new ParseState(str);
        if (parseState.ch(0) == '-') {
            parseState.skip();
        }
        int iGatherInt = parseState.gatherInt("Invalid year in date string", 9999);
        if (parseState.hasNext() && parseState.ch() != '-') {
            throw new XMPException("Invalid date string, after year", 5);
        }
        if (parseState.ch(0) == '-') {
            iGatherInt = -iGatherInt;
        }
        xMPDateTime.setYear(iGatherInt);
        if (!parseState.hasNext()) {
            return xMPDateTime;
        }
        parseState.skip();
        int iGatherInt2 = parseState.gatherInt("Invalid month in date string", 12);
        if (parseState.hasNext() && parseState.ch() != '-') {
            throw new XMPException("Invalid date string, after month", 5);
        }
        xMPDateTime.setMonth(iGatherInt2);
        if (!parseState.hasNext()) {
            return xMPDateTime;
        }
        parseState.skip();
        int iGatherInt3 = parseState.gatherInt("Invalid day in date string", 31);
        if (parseState.hasNext() && parseState.ch() != 'T') {
            throw new XMPException("Invalid date string, after day", 5);
        }
        xMPDateTime.setDay(iGatherInt3);
        if (!parseState.hasNext()) {
            return xMPDateTime;
        }
        parseState.skip();
        xMPDateTime.setHour(parseState.gatherInt("Invalid hour in date string", 23));
        if (!parseState.hasNext()) {
            return xMPDateTime;
        }
        if (parseState.ch() == ':') {
            parseState.skip();
            int iGatherInt4 = parseState.gatherInt("Invalid minute in date string", 59);
            if (parseState.hasNext() && parseState.ch() != ':' && parseState.ch() != 'Z' && parseState.ch() != '+' && parseState.ch() != '-') {
                throw new XMPException("Invalid date string, after minute", 5);
            }
            xMPDateTime.setMinute(iGatherInt4);
        }
        if (!parseState.hasNext()) {
            return xMPDateTime;
        }
        if (parseState.hasNext() && parseState.ch() == ':') {
            parseState.skip();
            int iGatherInt5 = parseState.gatherInt("Invalid whole seconds in date string", 59);
            if (parseState.hasNext() && parseState.ch() != '.' && parseState.ch() != 'Z' && parseState.ch() != '+' && parseState.ch() != '-') {
                throw new XMPException("Invalid date string, after whole seconds", 5);
            }
            xMPDateTime.setSecond(iGatherInt5);
            if (parseState.ch() == '.') {
                parseState.skip();
                int iPos = parseState.pos();
                int iGatherInt6 = parseState.gatherInt("Invalid fractional seconds in date string", 999999999);
                if (parseState.hasNext() && parseState.ch() != 'Z' && parseState.ch() != '+' && parseState.ch() != '-') {
                    throw new XMPException("Invalid date string, after fractional second", 5);
                }
                int iPos2 = parseState.pos() - iPos;
                while (iPos2 > 9) {
                    iGatherInt6 /= 10;
                    iPos2--;
                }
                while (iPos2 < 9) {
                    iGatherInt6 *= 10;
                    iPos2++;
                }
                xMPDateTime.setNanoSecond(iGatherInt6);
            }
        } else if (parseState.ch() != 'Z' && parseState.ch() != '+' && parseState.ch() != '-') {
            throw new XMPException("Invalid date string, after time", 5);
        }
        int i = 0;
        int iGatherInt7 = 0;
        int iGatherInt8 = 0;
        if (!parseState.hasNext()) {
            return xMPDateTime;
        }
        if (parseState.ch() == 'Z') {
            parseState.skip();
        } else if (parseState.hasNext()) {
            if (parseState.ch() == '+') {
                i = 1;
            } else {
                if (parseState.ch() != '-') {
                    throw new XMPException("Time zone must begin with 'Z', '+', or '-'", 5);
                }
                i = -1;
            }
            parseState.skip();
            iGatherInt7 = parseState.gatherInt("Invalid time zone hour in date string", 23);
            if (parseState.hasNext()) {
                if (parseState.ch() != ':') {
                    throw new XMPException("Invalid date string, after time zone hour", 5);
                }
                parseState.skip();
                iGatherInt8 = parseState.gatherInt("Invalid time zone minute in date string", 59);
            }
        }
        xMPDateTime.setTimeZone(new SimpleTimeZone(((iGatherInt7 * NikonType2MakernoteDirectory.TAG_NIKON_SCAN * 1000) + (iGatherInt8 * 60 * 1000)) * i, ""));
        if (parseState.hasNext()) {
            throw new XMPException("Invalid date string, extra chars at end", 5);
        }
        return xMPDateTime;
    }

    public static String render(XMPDateTime xMPDateTime) {
        StringBuffer stringBuffer = new StringBuffer();
        if (xMPDateTime.hasDate()) {
            DecimalFormat decimalFormat = new DecimalFormat("0000", new DecimalFormatSymbols(Locale.ENGLISH));
            stringBuffer.append(decimalFormat.format(xMPDateTime.getYear()));
            if (xMPDateTime.getMonth() == 0) {
                return stringBuffer.toString();
            }
            decimalFormat.applyPattern("'-'00");
            stringBuffer.append(decimalFormat.format(xMPDateTime.getMonth()));
            if (xMPDateTime.getDay() == 0) {
                return stringBuffer.toString();
            }
            stringBuffer.append(decimalFormat.format(xMPDateTime.getDay()));
            if (xMPDateTime.hasTime()) {
                stringBuffer.append('T');
                decimalFormat.applyPattern(TarConstants.VERSION_POSIX);
                stringBuffer.append(decimalFormat.format(xMPDateTime.getHour()));
                stringBuffer.append(':');
                stringBuffer.append(decimalFormat.format(xMPDateTime.getMinute()));
                if (xMPDateTime.getSecond() != 0 || xMPDateTime.getNanoSecond() != 0) {
                    decimalFormat.applyPattern(":00.#########");
                    stringBuffer.append(decimalFormat.format(xMPDateTime.getSecond() + (xMPDateTime.getNanoSecond() / 1.0E9d)));
                }
                if (xMPDateTime.hasTimeZone()) {
                    int offset = xMPDateTime.getTimeZone().getOffset(xMPDateTime.getCalendar().getTimeInMillis());
                    if (offset == 0) {
                        stringBuffer.append('Z');
                    } else {
                        int i = offset / DateUtils.MILLIS_IN_HOUR;
                        int iAbs = Math.abs((offset % DateUtils.MILLIS_IN_HOUR) / 60000);
                        decimalFormat.applyPattern("+00;-00");
                        stringBuffer.append(decimalFormat.format(i));
                        decimalFormat.applyPattern(":00");
                        stringBuffer.append(decimalFormat.format(iAbs));
                    }
                }
            }
        }
        return stringBuffer.toString();
    }
}
