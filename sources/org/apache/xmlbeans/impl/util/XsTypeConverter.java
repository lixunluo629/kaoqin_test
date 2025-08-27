package org.apache.xmlbeans.impl.util;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URI;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import javax.xml.namespace.NamespaceContext;
import javax.xml.namespace.QName;
import org.apache.poi.ss.usermodel.Font;
import org.apache.xmlbeans.GDate;
import org.apache.xmlbeans.GDateBuilder;
import org.apache.xmlbeans.GDateSpecification;
import org.apache.xmlbeans.XmlCalendar;
import org.apache.xmlbeans.XmlError;
import org.apache.xmlbeans.impl.common.InvalidLexicalValueException;
import org.springframework.beans.PropertyAccessor;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/util/XsTypeConverter.class */
public final class XsTypeConverter {
    private static final String POS_INF_LEX = "INF";
    private static final String NEG_INF_LEX = "-INF";
    private static final String NAN_LEX = "NaN";
    private static final char NAMESPACE_SEP = ':';
    private static final String EMPTY_PREFIX = "";
    private static final BigDecimal DECIMAL__ZERO;
    private static final String[] URI_CHARS_TO_BE_REPLACED;
    private static final String[] URI_CHARS_REPLACED_WITH;
    private static final char[] CH_ZEROS;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !XsTypeConverter.class.desiredAssertionStatus();
        DECIMAL__ZERO = new BigDecimal(0.0d);
        URI_CHARS_TO_BE_REPLACED = new String[]{SymbolConstants.SPACE_SYMBOL, "{", "}", "|", "\\", "^", PropertyAccessor.PROPERTY_KEY_PREFIX, "]", "`"};
        URI_CHARS_REPLACED_WITH = new String[]{"%20", "%7b", "%7d", "%7c", "%5c", "%5e", "%5b", "%5d", "%60"};
        CH_ZEROS = new char[]{'0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0'};
    }

    public static float lexFloat(CharSequence cs) throws NumberFormatException {
        char ch2;
        String v = cs.toString();
        try {
            if (cs.length() > 0 && (((ch2 = cs.charAt(cs.length() - 1)) == 'f' || ch2 == 'F') && cs.charAt(cs.length() - 2) != 'N')) {
                throw new NumberFormatException("Invalid char '" + ch2 + "' in float.");
            }
            return Float.parseFloat(v);
        } catch (NumberFormatException e) {
            if (v.equals(POS_INF_LEX)) {
                return Float.POSITIVE_INFINITY;
            }
            if (v.equals(NEG_INF_LEX)) {
                return Float.NEGATIVE_INFINITY;
            }
            if (v.equals(NAN_LEX)) {
                return Float.NaN;
            }
            throw e;
        }
    }

    public static float lexFloat(CharSequence cs, Collection errors) {
        try {
            return lexFloat(cs);
        } catch (NumberFormatException e) {
            String msg = "invalid float: " + ((Object) cs);
            errors.add(XmlError.forMessage(msg));
            return Float.NaN;
        }
    }

    public static String printFloat(float value) {
        if (value == Float.POSITIVE_INFINITY) {
            return POS_INF_LEX;
        }
        if (value == Float.NEGATIVE_INFINITY) {
            return NEG_INF_LEX;
        }
        if (Float.isNaN(value)) {
            return NAN_LEX;
        }
        return Float.toString(value);
    }

    public static double lexDouble(CharSequence cs) throws NumberFormatException {
        char ch2;
        String v = cs.toString();
        try {
            if (cs.length() > 0 && ((ch2 = cs.charAt(cs.length() - 1)) == 'd' || ch2 == 'D')) {
                throw new NumberFormatException("Invalid char '" + ch2 + "' in double.");
            }
            return Double.parseDouble(v);
        } catch (NumberFormatException e) {
            if (v.equals(POS_INF_LEX)) {
                return Double.POSITIVE_INFINITY;
            }
            if (v.equals(NEG_INF_LEX)) {
                return Double.NEGATIVE_INFINITY;
            }
            if (v.equals(NAN_LEX)) {
                return Double.NaN;
            }
            throw e;
        }
    }

    public static double lexDouble(CharSequence cs, Collection errors) {
        try {
            return lexDouble(cs);
        } catch (NumberFormatException e) {
            String msg = "invalid double: " + ((Object) cs);
            errors.add(XmlError.forMessage(msg));
            return Double.NaN;
        }
    }

    public static String printDouble(double value) {
        if (value == Double.POSITIVE_INFINITY) {
            return POS_INF_LEX;
        }
        if (value == Double.NEGATIVE_INFINITY) {
            return NEG_INF_LEX;
        }
        if (Double.isNaN(value)) {
            return NAN_LEX;
        }
        return Double.toString(value);
    }

    public static BigDecimal lexDecimal(CharSequence cs) throws NumberFormatException {
        String v = cs.toString();
        return new BigDecimal(trimTrailingZeros(v));
    }

    public static BigDecimal lexDecimal(CharSequence cs, Collection errors) {
        try {
            return lexDecimal(cs);
        } catch (NumberFormatException e) {
            String msg = "invalid long: " + ((Object) cs);
            errors.add(XmlError.forMessage(msg));
            return DECIMAL__ZERO;
        }
    }

    public static String printDecimal(BigDecimal value) {
        String intStr = value.unscaledValue().toString();
        int scale = value.scale();
        if (scale == 0 || (value.longValue() == 0 && scale < 0)) {
            return intStr;
        }
        int begin = value.signum() < 0 ? 1 : 0;
        int delta = scale;
        StringBuffer result = new StringBuffer(intStr.length() + 1 + Math.abs(scale));
        if (begin == 1) {
            result.append('-');
        }
        if (scale > 0) {
            int delta2 = delta - (intStr.length() - begin);
            if (delta2 >= 0) {
                result.append("0.");
                while (delta2 > CH_ZEROS.length) {
                    result.append(CH_ZEROS);
                    delta2 -= CH_ZEROS.length;
                }
                result.append(CH_ZEROS, 0, delta2);
                result.append(intStr.substring(begin));
            } else {
                int delta3 = begin - delta2;
                result.append(intStr.substring(begin, delta3));
                result.append('.');
                result.append(intStr.substring(delta3));
            }
        } else {
            result.append(intStr.substring(begin));
            while (delta < (-CH_ZEROS.length)) {
                result.append(CH_ZEROS);
                delta += CH_ZEROS.length;
            }
            result.append(CH_ZEROS, 0, -delta);
        }
        return result.toString();
    }

    public static BigInteger lexInteger(CharSequence cs) throws NumberFormatException {
        if (cs.length() > 1 && cs.charAt(0) == '+' && cs.charAt(1) == '-') {
            throw new NumberFormatException("Illegal char sequence '+-'");
        }
        String v = cs.toString();
        return new BigInteger(trimInitialPlus(v));
    }

    public static BigInteger lexInteger(CharSequence cs, Collection errors) {
        try {
            return lexInteger(cs);
        } catch (NumberFormatException e) {
            String msg = "invalid long: " + ((Object) cs);
            errors.add(XmlError.forMessage(msg));
            return BigInteger.ZERO;
        }
    }

    public static String printInteger(BigInteger value) {
        return value.toString();
    }

    public static long lexLong(CharSequence cs) throws NumberFormatException {
        String v = cs.toString();
        return Long.parseLong(trimInitialPlus(v));
    }

    public static long lexLong(CharSequence cs, Collection errors) {
        try {
            return lexLong(cs);
        } catch (NumberFormatException e) {
            String msg = "invalid long: " + ((Object) cs);
            errors.add(XmlError.forMessage(msg));
            return 0L;
        }
    }

    public static String printLong(long value) {
        return Long.toString(value);
    }

    public static short lexShort(CharSequence cs) throws NumberFormatException {
        return parseShort(cs);
    }

    public static short lexShort(CharSequence cs, Collection errors) {
        try {
            return lexShort(cs);
        } catch (NumberFormatException e) {
            String msg = "invalid short: " + ((Object) cs);
            errors.add(XmlError.forMessage(msg));
            return (short) 0;
        }
    }

    public static String printShort(short value) {
        return Short.toString(value);
    }

    public static int lexInt(CharSequence cs) throws NumberFormatException {
        return parseInt(cs);
    }

    public static int lexInt(CharSequence cs, Collection errors) {
        try {
            return lexInt(cs);
        } catch (NumberFormatException e) {
            String msg = "invalid int:" + ((Object) cs);
            errors.add(XmlError.forMessage(msg));
            return 0;
        }
    }

    public static String printInt(int value) {
        return Integer.toString(value);
    }

    public static byte lexByte(CharSequence cs) throws NumberFormatException {
        return parseByte(cs);
    }

    public static byte lexByte(CharSequence cs, Collection errors) {
        try {
            return lexByte(cs);
        } catch (NumberFormatException e) {
            String msg = "invalid byte: " + ((Object) cs);
            errors.add(XmlError.forMessage(msg));
            return (byte) 0;
        }
    }

    public static String printByte(byte value) {
        return Byte.toString(value);
    }

    public static boolean lexBoolean(CharSequence v) {
        switch (v.length()) {
            case 1:
                char c = v.charAt(0);
                if ('0' == c) {
                    return false;
                }
                if ('1' == c) {
                    return true;
                }
                break;
            case 4:
                if ('t' == v.charAt(0) && 'r' == v.charAt(1) && 'u' == v.charAt(2) && 'e' == v.charAt(3)) {
                    return true;
                }
                break;
            case 5:
                if ('f' == v.charAt(0) && 'a' == v.charAt(1) && 'l' == v.charAt(2) && 's' == v.charAt(3) && 'e' == v.charAt(4)) {
                    return false;
                }
                break;
        }
        String msg = "invalid boolean: " + ((Object) v);
        throw new InvalidLexicalValueException(msg);
    }

    public static boolean lexBoolean(CharSequence value, Collection errors) {
        try {
            return lexBoolean(value);
        } catch (InvalidLexicalValueException e) {
            errors.add(XmlError.forMessage(e.getMessage()));
            return false;
        }
    }

    public static String printBoolean(boolean value) {
        return value ? "true" : "false";
    }

    public static String lexString(CharSequence cs, Collection errors) {
        String v = cs.toString();
        return v;
    }

    public static String lexString(CharSequence lexical_value) {
        return lexical_value.toString();
    }

    public static String printString(String value) {
        return value;
    }

    public static QName lexQName(CharSequence charSeq, NamespaceContext nscontext) {
        String prefix;
        String localname;
        boolean hasFirstCollon = false;
        int firstcolon = 0;
        while (true) {
            if (firstcolon >= charSeq.length()) {
                break;
            }
            if (charSeq.charAt(firstcolon) != ':') {
                firstcolon++;
            } else {
                hasFirstCollon = true;
                break;
            }
        }
        if (hasFirstCollon) {
            prefix = charSeq.subSequence(0, firstcolon).toString();
            localname = charSeq.subSequence(firstcolon + 1, charSeq.length()).toString();
            if (firstcolon == 0) {
                throw new InvalidLexicalValueException("invalid xsd:QName '" + charSeq.toString() + "'");
            }
        } else {
            prefix = "";
            localname = charSeq.toString();
        }
        String uri = nscontext.getNamespaceURI(prefix);
        if (uri == null) {
            if (prefix != null && prefix.length() > 0) {
                throw new InvalidLexicalValueException("Can't resolve prefix: " + prefix);
            }
            uri = "";
        }
        return new QName(uri, localname);
    }

    public static QName lexQName(String xsd_qname, Collection errors, NamespaceContext nscontext) {
        try {
            return lexQName(xsd_qname, nscontext);
        } catch (InvalidLexicalValueException e) {
            errors.add(XmlError.forMessage(e.getMessage()));
            int idx = xsd_qname.indexOf(58);
            return new QName(null, xsd_qname.substring(idx));
        }
    }

    public static String printQName(QName qname, NamespaceContext nsContext, Collection errors) {
        String prefix;
        String uri = qname.getNamespaceURI();
        if (!$assertionsDisabled && uri == null) {
            throw new AssertionError();
        }
        if (uri.length() > 0) {
            prefix = nsContext.getPrefix(uri);
            if (prefix == null) {
                String msg = "NamespaceContext does not provide prefix for namespaceURI " + uri;
                errors.add(XmlError.forMessage(msg));
            }
        } else {
            prefix = null;
        }
        return getQNameString(uri, qname.getLocalPart(), prefix);
    }

    public static String getQNameString(String uri, String localpart, String prefix) {
        if (prefix != null && uri != null && uri.length() > 0 && prefix.length() > 0) {
            return prefix + ':' + localpart;
        }
        return localpart;
    }

    public static GDate lexGDate(CharSequence charSeq) {
        return new GDate(charSeq);
    }

    public static GDate lexGDate(String xsd_gdate, Collection errors) {
        try {
            return lexGDate(xsd_gdate);
        } catch (IllegalArgumentException e) {
            errors.add(XmlError.forMessage(e.getMessage()));
            return new GDateBuilder().toGDate();
        }
    }

    public static String printGDate(GDate gdate, Collection errors) {
        return gdate.toString();
    }

    public static XmlCalendar lexDateTime(CharSequence v) {
        GDateSpecification value = getGDateValue(v, 14);
        return value.getCalendar();
    }

    public static String printDateTime(Calendar c) {
        return printDateTime(c, 14);
    }

    public static String printTime(Calendar c) {
        return printDateTime(c, 15);
    }

    public static String printDate(Calendar c) {
        return printDateTime(c, 16);
    }

    public static String printDate(Date d) {
        GDateSpecification value = getGDateValue(d, 16);
        return value.toString();
    }

    public static String printDateTime(Calendar c, int type_code) {
        GDateSpecification value = getGDateValue(c, type_code);
        return value.toString();
    }

    public static String printDateTime(Date c) {
        GDateSpecification value = getGDateValue(c, 14);
        return value.toString();
    }

    public static CharSequence printHexBinary(byte[] val) {
        return HexBin.bytesToString(val);
    }

    public static byte[] lexHexBinary(CharSequence lexical_value) {
        byte[] buf = HexBin.decode(lexical_value.toString().getBytes());
        if (buf != null) {
            return buf;
        }
        throw new InvalidLexicalValueException("invalid hexBinary value");
    }

    public static CharSequence printBase64Binary(byte[] val) {
        byte[] bytes = Base64.encode(val);
        return new String(bytes);
    }

    public static byte[] lexBase64Binary(CharSequence lexical_value) {
        byte[] buf = Base64.decode(lexical_value.toString().getBytes());
        if (buf != null) {
            return buf;
        }
        throw new InvalidLexicalValueException("invalid base64Binary value");
    }

    public static GDateSpecification getGDateValue(Date d, int builtin_type_code) {
        GDateBuilder gDateBuilder = new GDateBuilder(d);
        gDateBuilder.setBuiltinTypeCode(builtin_type_code);
        GDate value = gDateBuilder.toGDate();
        return value;
    }

    public static GDateSpecification getGDateValue(Calendar c, int builtin_type_code) {
        GDateBuilder gDateBuilder = new GDateBuilder(c);
        gDateBuilder.setBuiltinTypeCode(builtin_type_code);
        GDate value = gDateBuilder.toGDate();
        return value;
    }

    public static GDateSpecification getGDateValue(CharSequence v, int builtin_type_code) {
        GDateBuilder gDateBuilder = new GDateBuilder(v);
        gDateBuilder.setBuiltinTypeCode(builtin_type_code);
        GDate value = gDateBuilder.toGDate();
        return value;
    }

    private static String trimInitialPlus(String xml) {
        if (xml.length() > 0 && xml.charAt(0) == '+') {
            return xml.substring(1);
        }
        return xml;
    }

    private static String trimTrailingZeros(String xsd_decimal) {
        int last_point;
        int last_char_idx = xsd_decimal.length() - 1;
        if (xsd_decimal.charAt(last_char_idx) == '0' && (last_point = xsd_decimal.lastIndexOf(46)) >= 0) {
            for (int idx = last_char_idx; idx > last_point; idx--) {
                if (xsd_decimal.charAt(idx) != '0') {
                    return xsd_decimal.substring(0, idx + 1);
                }
            }
            return xsd_decimal.substring(0, last_point);
        }
        return xsd_decimal;
    }

    private static int parseInt(CharSequence cs) {
        return parseIntXsdNumber(cs, Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    private static short parseShort(CharSequence cs) {
        return (short) parseIntXsdNumber(cs, -32768, Font.COLOR_NORMAL);
    }

    private static byte parseByte(CharSequence cs) {
        return (byte) parseIntXsdNumber(cs, -128, 127);
    }

    private static int parseIntXsdNumber(CharSequence ch2, int min_value, int max_value) {
        int limit;
        int limit2;
        int length = ch2.length();
        if (length < 1) {
            throw new NumberFormatException("For input string: \"" + ch2.toString() + SymbolConstants.QUOTES_SYMBOL);
        }
        int sign = 1;
        int result = 0;
        int start = 0;
        char c = ch2.charAt(0);
        if (c == '-') {
            start = 0 + 1;
            limit = min_value / 10;
            limit2 = -(min_value % 10);
        } else if (c == '+') {
            start = 0 + 1;
            sign = -1;
            limit = -(max_value / 10);
            limit2 = max_value % 10;
        } else {
            sign = -1;
            limit = -(max_value / 10);
            limit2 = max_value % 10;
        }
        for (int i = 0; i < length - start; i++) {
            int v = Character.digit(ch2.charAt(i + start), 10);
            if (v < 0) {
                throw new NumberFormatException("For input string: \"" + ch2.toString() + SymbolConstants.QUOTES_SYMBOL);
            }
            if (result < limit || (result == limit && v > limit2)) {
                throw new NumberFormatException("For input string: \"" + ch2.toString() + SymbolConstants.QUOTES_SYMBOL);
            }
            result = (result * 10) - v;
        }
        return sign * result;
    }

    public static CharSequence printAnyURI(CharSequence val) {
        return val;
    }

    public static CharSequence lexAnyURI(CharSequence lexical_value) {
        StringBuffer s = new StringBuffer(lexical_value.toString());
        for (int ic = 0; ic < URI_CHARS_TO_BE_REPLACED.length; ic++) {
            int i = 0;
            while (true) {
                int i2 = s.indexOf(URI_CHARS_TO_BE_REPLACED[ic], i);
                if (i2 >= 0) {
                    s.replace(i2, i2 + 1, URI_CHARS_REPLACED_WITH[ic]);
                    i = i2 + 3;
                }
            }
        }
        try {
            URI.create(s.toString());
            return lexical_value;
        } catch (IllegalArgumentException e) {
            throw new InvalidLexicalValueException("invalid anyURI value: " + ((Object) lexical_value), e);
        }
    }
}
