package com.mysql.jdbc;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.UnsupportedCharsetException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.poi.ss.usermodel.Font;
import org.springframework.aop.framework.autoproxy.target.QuickTargetSourceCreator;
import org.springframework.jdbc.datasource.init.ScriptUtils;

/* loaded from: mysql-connector-java-5.1.48.jar:com/mysql/jdbc/StringUtils.class */
public class StringUtils {
    private static final int NON_COMMENTS_MYSQL_VERSION_REF_LENGTH = 5;
    private static final int BYTE_RANGE = 256;
    private static Method toPlainStringMethod;
    private static final int WILD_COMPARE_MATCH = 0;
    private static final int WILD_COMPARE_CONTINUE_WITH_WILD = 1;
    private static final int WILD_COMPARE_NO_MATCH = -1;
    static final char WILDCARD_MANY = '%';
    static final char WILDCARD_ONE = '_';
    static final char WILDCARD_ESCAPE = '\\';
    private static final String VALID_ID_CHARS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIGKLMNOPQRSTUVWXYZ0123456789$_#@";
    private static final char[] HEX_DIGITS;
    public static final Set<SearchMode> SEARCH_MODE__ALL = Collections.unmodifiableSet(EnumSet.allOf(SearchMode.class));
    public static final Set<SearchMode> SEARCH_MODE__MRK_COM_WS = Collections.unmodifiableSet(EnumSet.of(SearchMode.SKIP_BETWEEN_MARKERS, SearchMode.SKIP_BLOCK_COMMENTS, SearchMode.SKIP_LINE_COMMENTS, SearchMode.SKIP_WHITE_SPACE));
    public static final Set<SearchMode> SEARCH_MODE__BSESC_COM_WS = Collections.unmodifiableSet(EnumSet.of(SearchMode.ALLOW_BACKSLASH_ESCAPE, SearchMode.SKIP_BLOCK_COMMENTS, SearchMode.SKIP_LINE_COMMENTS, SearchMode.SKIP_WHITE_SPACE));
    public static final Set<SearchMode> SEARCH_MODE__BSESC_MRK_WS = Collections.unmodifiableSet(EnumSet.of(SearchMode.ALLOW_BACKSLASH_ESCAPE, SearchMode.SKIP_BETWEEN_MARKERS, SearchMode.SKIP_WHITE_SPACE));
    public static final Set<SearchMode> SEARCH_MODE__COM_WS = Collections.unmodifiableSet(EnumSet.of(SearchMode.SKIP_BLOCK_COMMENTS, SearchMode.SKIP_LINE_COMMENTS, SearchMode.SKIP_WHITE_SPACE));
    public static final Set<SearchMode> SEARCH_MODE__MRK_WS = Collections.unmodifiableSet(EnumSet.of(SearchMode.SKIP_BETWEEN_MARKERS, SearchMode.SKIP_WHITE_SPACE));
    public static final Set<SearchMode> SEARCH_MODE__NONE = Collections.unmodifiableSet(EnumSet.noneOf(SearchMode.class));
    private static byte[] allBytes = new byte[256];
    private static char[] byteToChars = new char[256];
    private static final ConcurrentHashMap<String, Charset> charsetsByAlias = new ConcurrentHashMap<>();
    private static final String platformEncoding = System.getProperty("file.encoding");

    /* loaded from: mysql-connector-java-5.1.48.jar:com/mysql/jdbc/StringUtils$SearchMode.class */
    public enum SearchMode {
        ALLOW_BACKSLASH_ESCAPE,
        SKIP_BETWEEN_MARKERS,
        SKIP_BLOCK_COMMENTS,
        SKIP_LINE_COMMENTS,
        SKIP_WHITE_SPACE
    }

    static {
        for (int i = -128; i <= 127; i++) {
            allBytes[i - (-128)] = (byte) i;
        }
        String allBytesString = new String(allBytes, 0, 255);
        int allBytesStringLen = allBytesString.length();
        for (int i2 = 0; i2 < 255 && i2 < allBytesStringLen; i2++) {
            byteToChars[i2] = allBytesString.charAt(i2);
        }
        try {
            toPlainStringMethod = BigDecimal.class.getMethod("toPlainString", new Class[0]);
        } catch (NoSuchMethodException e) {
        }
        HEX_DIGITS = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
    }

    static Charset findCharset(String alias) throws UnsupportedEncodingException {
        try {
            Charset cs = charsetsByAlias.get(alias);
            if (cs == null) {
                cs = Charset.forName(alias);
                Charset oldCs = charsetsByAlias.putIfAbsent(alias, cs);
                if (oldCs != null) {
                    cs = oldCs;
                }
            }
            return cs;
        } catch (IllegalCharsetNameException e) {
            throw new UnsupportedEncodingException(alias);
        } catch (UnsupportedCharsetException e2) {
            throw new UnsupportedEncodingException(alias);
        } catch (IllegalArgumentException e3) {
            throw new UnsupportedEncodingException(alias);
        }
    }

    public static String consistentToString(BigDecimal decimal) {
        if (decimal == null) {
            return null;
        }
        if (toPlainStringMethod != null) {
            try {
                return (String) toPlainStringMethod.invoke(decimal, (Object[]) null);
            } catch (IllegalAccessException e) {
            } catch (InvocationTargetException e2) {
            }
        }
        return decimal.toString();
    }

    public static String dumpAsHex(byte[] byteBuffer, int length) {
        StringBuilder outputBuilder = new StringBuilder(length * 4);
        int p = 0;
        int rows = length / 8;
        for (int i = 0; i < rows && p < length; i++) {
            int ptemp = p;
            for (int j = 0; j < 8; j++) {
                String hexVal = Integer.toHexString(byteBuffer[ptemp] & 255);
                if (hexVal.length() == 1) {
                    hexVal = "0" + hexVal;
                }
                outputBuilder.append(hexVal + SymbolConstants.SPACE_SYMBOL);
                ptemp++;
            }
            outputBuilder.append("    ");
            for (int j2 = 0; j2 < 8; j2++) {
                int b = 255 & byteBuffer[p];
                if (b > 32 && b < 127) {
                    outputBuilder.append(((char) b) + SymbolConstants.SPACE_SYMBOL);
                } else {
                    outputBuilder.append(". ");
                }
                p++;
            }
            outputBuilder.append(ScriptUtils.FALLBACK_STATEMENT_SEPARATOR);
        }
        int n = 0;
        for (int i2 = p; i2 < length; i2++) {
            String hexVal2 = Integer.toHexString(byteBuffer[i2] & 255);
            if (hexVal2.length() == 1) {
                hexVal2 = "0" + hexVal2;
            }
            outputBuilder.append(hexVal2 + SymbolConstants.SPACE_SYMBOL);
            n++;
        }
        for (int i3 = n; i3 < 8; i3++) {
            outputBuilder.append("   ");
        }
        outputBuilder.append("    ");
        for (int i4 = p; i4 < length; i4++) {
            int b2 = 255 & byteBuffer[i4];
            if (b2 > 32 && b2 < 127) {
                outputBuilder.append(((char) b2) + SymbolConstants.SPACE_SYMBOL);
            } else {
                outputBuilder.append(". ");
            }
        }
        outputBuilder.append(ScriptUtils.FALLBACK_STATEMENT_SEPARATOR);
        return outputBuilder.toString();
    }

    private static boolean endsWith(byte[] dataFrom, String suffix) {
        for (int i = 1; i <= suffix.length(); i++) {
            int dfOffset = dataFrom.length - i;
            int suffixOffset = suffix.length() - i;
            if (dataFrom[dfOffset] != suffix.charAt(suffixOffset)) {
                return false;
            }
        }
        return true;
    }

    public static byte[] escapeEasternUnicodeByteStream(byte[] origBytes, String origString) {
        if (origBytes == null) {
            return null;
        }
        if (origBytes.length == 0) {
            return new byte[0];
        }
        int bytesLen = origBytes.length;
        int bufIndex = 0;
        int strIndex = 0;
        ByteArrayOutputStream bytesOut = new ByteArrayOutputStream(bytesLen);
        while (true) {
            if (origString.charAt(strIndex) == '\\') {
                int i = bufIndex;
                bufIndex++;
                bytesOut.write(origBytes[i]);
            } else {
                int loByte = origBytes[bufIndex];
                if (loByte < 0) {
                    loByte += 256;
                }
                bytesOut.write(loByte);
                if (loByte >= 128) {
                    if (bufIndex < bytesLen - 1) {
                        int hiByte = origBytes[bufIndex + 1];
                        if (hiByte < 0) {
                            hiByte += 256;
                        }
                        bytesOut.write(hiByte);
                        bufIndex++;
                        if (hiByte == 92) {
                            bytesOut.write(hiByte);
                        }
                    }
                } else if (loByte == 92 && bufIndex < bytesLen - 1) {
                    int hiByte2 = origBytes[bufIndex + 1];
                    if (hiByte2 < 0) {
                        hiByte2 += 256;
                    }
                    if (hiByte2 == 98) {
                        bytesOut.write(92);
                        bytesOut.write(98);
                        bufIndex++;
                    }
                }
                bufIndex++;
            }
            if (bufIndex < bytesLen) {
                strIndex++;
            } else {
                return bytesOut.toByteArray();
            }
        }
    }

    public static char firstNonWsCharUc(String searchIn) {
        return firstNonWsCharUc(searchIn, 0);
    }

    public static char firstNonWsCharUc(String searchIn, int startAt) {
        if (searchIn == null) {
            return (char) 0;
        }
        int length = searchIn.length();
        for (int i = startAt; i < length; i++) {
            char c = searchIn.charAt(i);
            if (!Character.isWhitespace(c)) {
                return Character.toUpperCase(c);
            }
        }
        return (char) 0;
    }

    public static char firstAlphaCharUc(String searchIn, int startAt) {
        if (searchIn == null) {
            return (char) 0;
        }
        int length = searchIn.length();
        for (int i = startAt; i < length; i++) {
            char c = searchIn.charAt(i);
            if (Character.isLetter(c)) {
                return Character.toUpperCase(c);
            }
        }
        return (char) 0;
    }

    public static String fixDecimalExponent(String dString) {
        char maybeMinusChar;
        int ePos = dString.indexOf(69);
        if (ePos == -1) {
            ePos = dString.indexOf(101);
        }
        if (ePos != -1 && dString.length() > ePos + 1 && (maybeMinusChar = dString.charAt(ePos + 1)) != '-' && maybeMinusChar != '+') {
            StringBuilder strBuilder = new StringBuilder(dString.length() + 1);
            strBuilder.append(dString.substring(0, ePos + 1));
            strBuilder.append('+');
            strBuilder.append(dString.substring(ePos + 1, dString.length()));
            dString = strBuilder.toString();
        }
        return dString;
    }

    public static byte[] getBytes(char[] c, SingleByteCharsetConverter converter, String encoding, String serverEncoding, boolean parserKnowsUnicode, ExceptionInterceptor exceptionInterceptor) throws SQLException {
        byte[] b;
        try {
            if (converter != null) {
                b = converter.toBytes(c);
            } else if (encoding == null) {
                b = getBytes(c);
            } else {
                b = getBytes(c, encoding);
                if (!parserKnowsUnicode && CharsetMapping.requiresEscapeEasternUnicode(encoding) && !encoding.equalsIgnoreCase(serverEncoding)) {
                    b = escapeEasternUnicodeByteStream(b, new String(c));
                }
            }
            return b;
        } catch (UnsupportedEncodingException e) {
            throw SQLError.createSQLException(Messages.getString("StringUtils.0") + encoding + Messages.getString("StringUtils.1"), SQLError.SQL_STATE_ILLEGAL_ARGUMENT, exceptionInterceptor);
        }
    }

    public static byte[] getBytes(char[] c, SingleByteCharsetConverter converter, String encoding, String serverEncoding, int offset, int length, boolean parserKnowsUnicode, ExceptionInterceptor exceptionInterceptor) throws SQLException {
        byte[] b;
        try {
            if (converter != null) {
                b = converter.toBytes(c, offset, length);
            } else if (encoding == null) {
                b = getBytes(c, offset, length);
            } else {
                b = getBytes(c, offset, length, encoding);
                if (!parserKnowsUnicode && CharsetMapping.requiresEscapeEasternUnicode(encoding) && !encoding.equalsIgnoreCase(serverEncoding)) {
                    b = escapeEasternUnicodeByteStream(b, new String(c, offset, length));
                }
            }
            return b;
        } catch (UnsupportedEncodingException e) {
            throw SQLError.createSQLException(Messages.getString("StringUtils.0") + encoding + Messages.getString("StringUtils.1"), SQLError.SQL_STATE_ILLEGAL_ARGUMENT, exceptionInterceptor);
        }
    }

    public static byte[] getBytes(char[] c, String encoding, String serverEncoding, boolean parserKnowsUnicode, MySQLConnection conn, ExceptionInterceptor exceptionInterceptor) throws SQLException {
        try {
            SingleByteCharsetConverter converter = conn != null ? conn.getCharsetConverter(encoding) : SingleByteCharsetConverter.getInstance(encoding, null);
            return getBytes(c, converter, encoding, serverEncoding, parserKnowsUnicode, exceptionInterceptor);
        } catch (UnsupportedEncodingException e) {
            throw SQLError.createSQLException(Messages.getString("StringUtils.0") + encoding + Messages.getString("StringUtils.1"), SQLError.SQL_STATE_ILLEGAL_ARGUMENT, exceptionInterceptor);
        }
    }

    public static byte[] getBytes(String s, SingleByteCharsetConverter converter, String encoding, String serverEncoding, boolean parserKnowsUnicode, ExceptionInterceptor exceptionInterceptor) throws SQLException {
        byte[] b;
        try {
            if (converter != null) {
                b = converter.toBytes(s);
            } else if (encoding == null) {
                b = getBytes(s);
            } else {
                b = getBytes(s, encoding);
                if (!parserKnowsUnicode && CharsetMapping.requiresEscapeEasternUnicode(encoding) && !encoding.equalsIgnoreCase(serverEncoding)) {
                    b = escapeEasternUnicodeByteStream(b, s);
                }
            }
            return b;
        } catch (UnsupportedEncodingException e) {
            throw SQLError.createSQLException(Messages.getString("StringUtils.5") + encoding + Messages.getString("StringUtils.6"), SQLError.SQL_STATE_ILLEGAL_ARGUMENT, exceptionInterceptor);
        }
    }

    public static byte[] getBytes(String s, SingleByteCharsetConverter converter, String encoding, String serverEncoding, int offset, int length, boolean parserKnowsUnicode, ExceptionInterceptor exceptionInterceptor) throws SQLException {
        byte[] b;
        try {
            if (converter != null) {
                b = converter.toBytes(s, offset, length);
            } else if (encoding == null) {
                b = getBytes(s, offset, length);
            } else {
                String s2 = s.substring(offset, offset + length);
                b = getBytes(s2, encoding);
                if (!parserKnowsUnicode && CharsetMapping.requiresEscapeEasternUnicode(encoding) && !encoding.equalsIgnoreCase(serverEncoding)) {
                    b = escapeEasternUnicodeByteStream(b, s2);
                }
            }
            return b;
        } catch (UnsupportedEncodingException e) {
            throw SQLError.createSQLException(Messages.getString("StringUtils.5") + encoding + Messages.getString("StringUtils.6"), SQLError.SQL_STATE_ILLEGAL_ARGUMENT, exceptionInterceptor);
        }
    }

    public static byte[] getBytes(String s, String encoding, String serverEncoding, boolean parserKnowsUnicode, MySQLConnection conn, ExceptionInterceptor exceptionInterceptor) throws SQLException {
        try {
            SingleByteCharsetConverter converter = conn != null ? conn.getCharsetConverter(encoding) : SingleByteCharsetConverter.getInstance(encoding, null);
            return getBytes(s, converter, encoding, serverEncoding, parserKnowsUnicode, exceptionInterceptor);
        } catch (UnsupportedEncodingException e) {
            throw SQLError.createSQLException(Messages.getString("StringUtils.5") + encoding + Messages.getString("StringUtils.6"), SQLError.SQL_STATE_ILLEGAL_ARGUMENT, exceptionInterceptor);
        }
    }

    public static final byte[] getBytes(String s, String encoding, String serverEncoding, int offset, int length, boolean parserKnowsUnicode, MySQLConnection conn, ExceptionInterceptor exceptionInterceptor) throws SQLException {
        try {
            SingleByteCharsetConverter converter = conn != null ? conn.getCharsetConverter(encoding) : SingleByteCharsetConverter.getInstance(encoding, null);
            return getBytes(s, converter, encoding, serverEncoding, offset, length, parserKnowsUnicode, exceptionInterceptor);
        } catch (UnsupportedEncodingException e) {
            throw SQLError.createSQLException(Messages.getString("StringUtils.5") + encoding + Messages.getString("StringUtils.6"), SQLError.SQL_STATE_ILLEGAL_ARGUMENT, exceptionInterceptor);
        }
    }

    public static byte[] getBytesWrapped(String s, char beginWrap, char endWrap, SingleByteCharsetConverter converter, String encoding, String serverEncoding, boolean parserKnowsUnicode, ExceptionInterceptor exceptionInterceptor) throws SQLException {
        byte[] b;
        try {
            if (converter != null) {
                b = converter.toBytesWrapped(s, beginWrap, endWrap);
            } else if (encoding == null) {
                StringBuilder strBuilder = new StringBuilder(s.length() + 2);
                strBuilder.append(beginWrap);
                strBuilder.append(s);
                strBuilder.append(endWrap);
                b = getBytes(strBuilder.toString());
            } else {
                StringBuilder strBuilder2 = new StringBuilder(s.length() + 2);
                strBuilder2.append(beginWrap);
                strBuilder2.append(s);
                strBuilder2.append(endWrap);
                String s2 = strBuilder2.toString();
                b = getBytes(s2, encoding);
                if (!parserKnowsUnicode && CharsetMapping.requiresEscapeEasternUnicode(encoding) && !encoding.equalsIgnoreCase(serverEncoding)) {
                    b = escapeEasternUnicodeByteStream(b, s2);
                }
            }
            return b;
        } catch (UnsupportedEncodingException e) {
            throw SQLError.createSQLException(Messages.getString("StringUtils.10") + encoding + Messages.getString("StringUtils.11"), SQLError.SQL_STATE_ILLEGAL_ARGUMENT, exceptionInterceptor);
        }
    }

    public static int getInt(byte[] buf) throws NumberFormatException {
        return getInt(buf, 0, buf.length);
    }

    public static int getInt(byte[] buf, int offset, int endPos) throws NumberFormatException {
        char c;
        int s = offset;
        while (s < endPos && Character.isWhitespace((char) buf[s])) {
            s++;
        }
        if (s == endPos) {
            throw new NumberFormatException(toString(buf));
        }
        boolean negative = false;
        if (((char) buf[s]) == '-') {
            negative = true;
            s++;
        } else if (((char) buf[s]) == '+') {
            s++;
        }
        int save = s;
        int cutoff = Integer.MAX_VALUE / 10;
        int cutlim = Integer.MAX_VALUE % 10;
        if (negative) {
            cutlim++;
        }
        boolean overflow = false;
        int i = 0;
        while (s < endPos) {
            char c2 = (char) buf[s];
            if (Character.isDigit(c2)) {
                c = (char) (c2 - '0');
            } else {
                if (!Character.isLetter(c2)) {
                    break;
                }
                c = (char) ((Character.toUpperCase(c2) - 'A') + 10);
            }
            if (c >= '\n') {
                break;
            }
            if (i > cutoff || (i == cutoff && c > cutlim)) {
                overflow = true;
            } else {
                i = (i * 10) + c;
            }
            s++;
        }
        if (s == save) {
            throw new NumberFormatException(toString(buf));
        }
        if (overflow) {
            throw new NumberFormatException(toString(buf));
        }
        return negative ? -i : i;
    }

    public static long getLong(byte[] buf) throws NumberFormatException {
        return getLong(buf, 0, buf.length);
    }

    public static long getLong(byte[] buf, int offset, int endpos) throws NumberFormatException {
        char c;
        int s = offset;
        while (s < endpos && Character.isWhitespace((char) buf[s])) {
            s++;
        }
        if (s == endpos) {
            throw new NumberFormatException(toString(buf));
        }
        boolean negative = false;
        if (((char) buf[s]) == '-') {
            negative = true;
            s++;
        } else if (((char) buf[s]) == '+') {
            s++;
        }
        int save = s;
        long cutoff = Long.MAX_VALUE / 10;
        long cutlim = (int) (Long.MAX_VALUE % 10);
        if (negative) {
            cutlim++;
        }
        boolean overflow = false;
        long i = 0;
        while (s < endpos) {
            char c2 = (char) buf[s];
            if (Character.isDigit(c2)) {
                c = (char) (c2 - '0');
            } else {
                if (!Character.isLetter(c2)) {
                    break;
                }
                c = (char) ((Character.toUpperCase(c2) - 'A') + 10);
            }
            if (c >= '\n') {
                break;
            }
            if (i > cutoff || (i == cutoff && c > cutlim)) {
                overflow = true;
            } else {
                i = (i * 10) + c;
            }
            s++;
        }
        if (s == save) {
            throw new NumberFormatException(toString(buf));
        }
        if (overflow) {
            throw new NumberFormatException(toString(buf));
        }
        return negative ? -i : i;
    }

    public static short getShort(byte[] buf) throws NumberFormatException {
        return getShort(buf, 0, buf.length);
    }

    public static short getShort(byte[] buf, int offset, int endpos) throws NumberFormatException {
        short c;
        int s = offset;
        while (s < endpos && Character.isWhitespace((char) buf[s])) {
            s++;
        }
        if (s == endpos) {
            throw new NumberFormatException(toString(buf));
        }
        boolean negative = false;
        if (((char) buf[s]) == '-') {
            negative = true;
            s++;
        } else if (((char) buf[s]) == '+') {
            s++;
        }
        int save = s;
        short cutoff = (short) (Font.COLOR_NORMAL / 10);
        short cutlim = (short) (Font.COLOR_NORMAL % 10);
        if (negative) {
            cutlim = (short) (cutlim + 1);
        }
        boolean overflow = false;
        short i = 0;
        while (s < endpos) {
            char c2 = (char) buf[s];
            if (Character.isDigit(c2)) {
                c = (char) (c2 - '0');
            } else {
                if (!Character.isLetter(c2)) {
                    break;
                }
                c = (char) ((Character.toUpperCase(c2) - 'A') + 10);
            }
            if (c >= 10) {
                break;
            }
            if (i > cutoff || (i == cutoff && c > cutlim)) {
                overflow = true;
            } else {
                i = (short) (((short) (i * 10)) + c);
            }
            s++;
        }
        if (s == save) {
            throw new NumberFormatException(toString(buf));
        }
        if (overflow) {
            throw new NumberFormatException(toString(buf));
        }
        return negative ? (short) (-i) : i;
    }

    public static int indexOfIgnoreCase(String searchIn, String searchFor) {
        return indexOfIgnoreCase(0, searchIn, searchFor);
    }

    public static int indexOfIgnoreCase(int startingPosition, String searchIn, String searchFor) {
        if (searchIn == null || searchFor == null) {
            return -1;
        }
        int searchInLength = searchIn.length();
        int searchForLength = searchFor.length();
        int stopSearchingAt = searchInLength - searchForLength;
        if (startingPosition > stopSearchingAt || searchForLength == 0) {
            return -1;
        }
        char firstCharOfSearchForUc = Character.toUpperCase(searchFor.charAt(0));
        char firstCharOfSearchForLc = Character.toLowerCase(searchFor.charAt(0));
        int i = startingPosition;
        while (i <= stopSearchingAt) {
            if (isCharAtPosNotEqualIgnoreCase(searchIn, i, firstCharOfSearchForUc, firstCharOfSearchForLc)) {
                do {
                    i++;
                    if (i > stopSearchingAt) {
                        break;
                    }
                } while (isCharAtPosNotEqualIgnoreCase(searchIn, i, firstCharOfSearchForUc, firstCharOfSearchForLc));
            }
            if (i > stopSearchingAt || !startsWithIgnoreCase(searchIn, i, searchFor)) {
                i++;
            } else {
                return i;
            }
        }
        return -1;
    }

    public static int indexOfIgnoreCase(int startingPosition, String searchIn, String[] searchForSequence, String openingMarkers, String closingMarkers, Set<SearchMode> searchMode) {
        if (searchIn == null || searchForSequence == null) {
            return -1;
        }
        int searchInLength = searchIn.length();
        int searchForLength = 0;
        for (String searchForPart : searchForSequence) {
            searchForLength += searchForPart.length();
        }
        if (searchForLength == 0) {
            return -1;
        }
        int searchForWordsCount = searchForSequence.length;
        int stopSearchingAt = searchInLength - (searchForLength + (searchForWordsCount > 0 ? searchForWordsCount - 1 : 0));
        if (startingPosition > stopSearchingAt) {
            return -1;
        }
        if (searchMode.contains(SearchMode.SKIP_BETWEEN_MARKERS) && (openingMarkers == null || closingMarkers == null || openingMarkers.length() != closingMarkers.length())) {
            throw new IllegalArgumentException(Messages.getString("StringUtils.15", new String[]{openingMarkers, closingMarkers}));
        }
        if (Character.isWhitespace(searchForSequence[0].charAt(0)) && searchMode.contains(SearchMode.SKIP_WHITE_SPACE)) {
            searchMode = EnumSet.copyOf((Collection) searchMode);
            searchMode.remove(SearchMode.SKIP_WHITE_SPACE);
        }
        Set<SearchMode> searchMode2 = EnumSet.of(SearchMode.SKIP_WHITE_SPACE);
        searchMode2.addAll(searchMode);
        searchMode2.remove(SearchMode.SKIP_BETWEEN_MARKERS);
        int positionOfFirstWord = startingPosition;
        while (positionOfFirstWord <= stopSearchingAt) {
            int positionOfFirstWord2 = indexOfIgnoreCase(positionOfFirstWord, searchIn, searchForSequence[0], openingMarkers, closingMarkers, searchMode);
            if (positionOfFirstWord2 == -1 || positionOfFirstWord2 > stopSearchingAt) {
                return -1;
            }
            int startingPositionForNextWord = positionOfFirstWord2 + searchForSequence[0].length();
            int wc = 0;
            boolean match = true;
            while (true) {
                wc++;
                if (wc >= searchForWordsCount || !match) {
                    break;
                }
                int positionOfNextWord = indexOfNextChar(startingPositionForNextWord, searchInLength - 1, searchIn, null, null, null, searchMode2);
                if (startingPositionForNextWord == positionOfNextWord || !startsWithIgnoreCase(searchIn, positionOfNextWord, searchForSequence[wc])) {
                    match = false;
                } else {
                    startingPositionForNextWord = positionOfNextWord + searchForSequence[wc].length();
                }
            }
            if (!match) {
                positionOfFirstWord = positionOfFirstWord2 + 1;
            } else {
                return positionOfFirstWord2;
            }
        }
        return -1;
    }

    public static int indexOfIgnoreCase(int startingPosition, String searchIn, String searchFor, String openingMarkers, String closingMarkers, Set<SearchMode> searchMode) {
        return indexOfIgnoreCase(startingPosition, searchIn, searchFor, openingMarkers, closingMarkers, "", searchMode);
    }

    public static int indexOfIgnoreCase(int startingPosition, String searchIn, String searchFor, String openingMarkers, String closingMarkers, String overridingMarkers, Set<SearchMode> searchMode) {
        if (searchIn == null || searchFor == null) {
            return -1;
        }
        int searchInLength = searchIn.length();
        int searchForLength = searchFor.length();
        int stopSearchingAt = searchInLength - searchForLength;
        if (startingPosition > stopSearchingAt || searchForLength == 0) {
            return -1;
        }
        if (searchMode.contains(SearchMode.SKIP_BETWEEN_MARKERS)) {
            if (openingMarkers == null || closingMarkers == null || openingMarkers.length() != closingMarkers.length()) {
                throw new IllegalArgumentException(Messages.getString("StringUtils.15", new String[]{openingMarkers, closingMarkers}));
            }
            if (overridingMarkers == null) {
                throw new IllegalArgumentException(Messages.getString("StringUtils.16", new String[]{overridingMarkers, openingMarkers}));
            }
            char[] arr$ = overridingMarkers.toCharArray();
            for (char c : arr$) {
                if (openingMarkers.indexOf(c) == -1) {
                    throw new IllegalArgumentException(Messages.getString("StringUtils.16", new String[]{overridingMarkers, openingMarkers}));
                }
            }
        }
        char firstCharOfSearchForUc = Character.toUpperCase(searchFor.charAt(0));
        char firstCharOfSearchForLc = Character.toLowerCase(searchFor.charAt(0));
        if (Character.isWhitespace(firstCharOfSearchForLc) && searchMode.contains(SearchMode.SKIP_WHITE_SPACE)) {
            searchMode = EnumSet.copyOf((Collection) searchMode);
            searchMode.remove(SearchMode.SKIP_WHITE_SPACE);
        }
        int i = startingPosition;
        while (i <= stopSearchingAt) {
            int i2 = indexOfNextChar(i, stopSearchingAt, searchIn, openingMarkers, closingMarkers, overridingMarkers, searchMode);
            if (i2 == -1) {
                return -1;
            }
            char c2 = searchIn.charAt(i2);
            if (!isCharEqualIgnoreCase(c2, firstCharOfSearchForUc, firstCharOfSearchForLc) || !startsWithIgnoreCase(searchIn, i2, searchFor)) {
                i = i2 + 1;
            } else {
                return i2;
            }
        }
        return -1;
    }

    /* JADX WARN: Removed duplicated region for block: B:157:0x030b A[PHI: r16
  0x030b: PHI (r16v1 'dashDashCommentImmediateEnd' boolean) = 
  (r16v0 'dashDashCommentImmediateEnd' boolean)
  (r16v0 'dashDashCommentImmediateEnd' boolean)
  (r16v3 'dashDashCommentImmediateEnd' boolean)
 binds: [B:144:0x02e2, B:146:0x02e9, B:156:0x0308] A[DONT_GENERATE, DONT_INLINE]] */
    /* JADX WARN: Removed duplicated region for block: B:161:0x0317  */
    /* JADX WARN: Removed duplicated region for block: B:170:0x034e A[LOOP:5: B:170:0x034e->B:257:?, LOOP_START, PHI: r12 r15
  0x034e: PHI (r12v2 'c0' char) = (r12v1 'c0' char), (r12v4 'c0' char) binds: [B:160:0x0314, B:257:?] A[DONT_GENERATE, DONT_INLINE]
  0x034e: PHI (r15v2 'i' int) = (r15v1 'i' int), (r15v3 'i' int) binds: [B:160:0x0314, B:257:?] A[DONT_GENERATE, DONT_INLINE]] */
    /* JADX WARN: Removed duplicated region for block: B:195:0x03c8  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private static int indexOfNextChar(int r4, int r5, java.lang.String r6, java.lang.String r7, java.lang.String r8, java.lang.String r9, java.util.Set<com.mysql.jdbc.StringUtils.SearchMode> r10) {
        /*
            Method dump skipped, instructions count: 1000
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mysql.jdbc.StringUtils.indexOfNextChar(int, int, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.util.Set):int");
    }

    private static boolean isCharAtPosNotEqualIgnoreCase(String searchIn, int pos, char firstCharOfSearchForUc, char firstCharOfSearchForLc) {
        return (Character.toLowerCase(searchIn.charAt(pos)) == firstCharOfSearchForLc || Character.toUpperCase(searchIn.charAt(pos)) == firstCharOfSearchForUc) ? false : true;
    }

    private static boolean isCharEqualIgnoreCase(char charToCompare, char compareToCharUC, char compareToCharLC) {
        return Character.toLowerCase(charToCompare) == compareToCharLC || Character.toUpperCase(charToCompare) == compareToCharUC;
    }

    public static List<String> split(String stringToSplit, String delimiter, boolean trim) {
        if (stringToSplit == null) {
            return new ArrayList();
        }
        if (delimiter == null) {
            throw new IllegalArgumentException();
        }
        StringTokenizer tokenizer = new StringTokenizer(stringToSplit, delimiter, false);
        List<String> splitTokens = new ArrayList<>(tokenizer.countTokens());
        while (tokenizer.hasMoreTokens()) {
            String token = tokenizer.nextToken();
            if (trim) {
                token = token.trim();
            }
            splitTokens.add(token);
        }
        return splitTokens;
    }

    public static List<String> split(String stringToSplit, String delimiter, String openingMarkers, String closingMarkers, boolean trim) {
        return split(stringToSplit, delimiter, openingMarkers, closingMarkers, "", trim);
    }

    public static List<String> split(String stringToSplit, String delimiter, String openingMarkers, String closingMarkers, String overridingMarkers, boolean trim) {
        if (stringToSplit == null) {
            return new ArrayList();
        }
        if (delimiter == null) {
            throw new IllegalArgumentException();
        }
        int currentPos = 0;
        List<String> splitTokens = new ArrayList<>();
        while (true) {
            int delimPos = indexOfIgnoreCase(currentPos, stringToSplit, delimiter, openingMarkers, closingMarkers, overridingMarkers, SEARCH_MODE__MRK_COM_WS);
            if (delimPos == -1) {
                break;
            }
            String token = stringToSplit.substring(currentPos, delimPos);
            if (trim) {
                token = token.trim();
            }
            splitTokens.add(token);
            currentPos = delimPos + 1;
        }
        if (currentPos < stringToSplit.length()) {
            String token2 = stringToSplit.substring(currentPos);
            if (trim) {
                token2 = token2.trim();
            }
            splitTokens.add(token2);
        }
        return splitTokens;
    }

    private static boolean startsWith(byte[] dataFrom, String chars) {
        int charsLength = chars.length();
        if (dataFrom.length < charsLength) {
            return false;
        }
        for (int i = 0; i < charsLength; i++) {
            if (dataFrom[i] != chars.charAt(i)) {
                return false;
            }
        }
        return true;
    }

    public static boolean startsWithIgnoreCase(String searchIn, int startAt, String searchFor) {
        return searchIn.regionMatches(true, startAt, searchFor, 0, searchFor.length());
    }

    public static boolean startsWithIgnoreCase(String searchIn, String searchFor) {
        return startsWithIgnoreCase(searchIn, 0, searchFor);
    }

    public static boolean startsWithIgnoreCaseAndNonAlphaNumeric(String searchIn, String searchFor) {
        if (searchIn == null) {
            return searchFor == null;
        }
        int beginPos = 0;
        int inLength = searchIn.length();
        while (beginPos < inLength) {
            char c = searchIn.charAt(beginPos);
            if (Character.isLetterOrDigit(c)) {
                break;
            }
            beginPos++;
        }
        return startsWithIgnoreCase(searchIn, beginPos, searchFor);
    }

    public static boolean startsWithIgnoreCaseAndWs(String searchIn, String searchFor) {
        return startsWithIgnoreCaseAndWs(searchIn, searchFor, 0);
    }

    public static boolean startsWithIgnoreCaseAndWs(String searchIn, String searchFor, int beginPos) {
        if (searchIn == null) {
            return searchFor == null;
        }
        int inLength = searchIn.length();
        while (beginPos < inLength && Character.isWhitespace(searchIn.charAt(beginPos))) {
            beginPos++;
        }
        return startsWithIgnoreCase(searchIn, beginPos, searchFor);
    }

    public static int startsWithIgnoreCaseAndWs(String searchIn, String[] searchFor) {
        for (int i = 0; i < searchFor.length; i++) {
            if (startsWithIgnoreCaseAndWs(searchIn, searchFor[i], 0)) {
                return i;
            }
        }
        return -1;
    }

    public static byte[] stripEnclosure(byte[] source, String prefix, String suffix) {
        if (source.length >= prefix.length() + suffix.length() && startsWith(source, prefix) && endsWith(source, suffix)) {
            int totalToStrip = prefix.length() + suffix.length();
            int enclosedLength = source.length - totalToStrip;
            byte[] enclosed = new byte[enclosedLength];
            int startPos = prefix.length();
            int numToCopy = enclosed.length;
            System.arraycopy(source, startPos, enclosed, 0, numToCopy);
            return enclosed;
        }
        return source;
    }

    public static String toAsciiString(byte[] buffer) {
        return toAsciiString(buffer, 0, buffer.length);
    }

    public static String toAsciiString(byte[] buffer, int startPos, int length) {
        char[] charArray = new char[length];
        int readpoint = startPos;
        for (int i = 0; i < length; i++) {
            charArray[i] = (char) buffer[readpoint];
            readpoint++;
        }
        return new String(charArray);
    }

    public static boolean wildCompareIgnoreCase(String searchIn, String searchFor) {
        return wildCompareInternal(searchIn, searchFor) == 0;
    }

    /* JADX WARN: Code restructure failed: missing block: B:68:0x00fe, code lost:
    
        if (r6 != r0) goto L71;
     */
    /* JADX WARN: Code restructure failed: missing block: B:69:0x0101, code lost:
    
        return 0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:72:0x0107, code lost:
    
        if (r8 != r0) goto L75;
     */
    /* JADX WARN: Code restructure failed: missing block: B:73:0x010a, code lost:
    
        return -1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:75:0x010c, code lost:
    
        r0 = r5.charAt(r6);
        r11 = r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:76:0x0116, code lost:
    
        if (r0 != '\\') goto L80;
     */
    /* JADX WARN: Code restructure failed: missing block: B:78:0x011d, code lost:
    
        if ((r6 + 1) == r0) goto L80;
     */
    /* JADX WARN: Code restructure failed: missing block: B:79:0x0120, code lost:
    
        r6 = r6 + 1;
        r11 = r5.charAt(r6);
     */
    /* JADX WARN: Code restructure failed: missing block: B:80:0x012a, code lost:
    
        r6 = r6 + 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:82:0x0131, code lost:
    
        if (r8 == r0) goto L121;
     */
    /* JADX WARN: Code restructure failed: missing block: B:84:0x0142, code lost:
    
        if (java.lang.Character.toUpperCase(r4.charAt(r8)) == java.lang.Character.toUpperCase(r11)) goto L122;
     */
    /* JADX WARN: Code restructure failed: missing block: B:85:0x0145, code lost:
    
        r8 = r8 + 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:86:0x014b, code lost:
    
        r0 = r8;
        r8 = r8 + 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:87:0x0152, code lost:
    
        if (r0 != r0) goto L90;
     */
    /* JADX WARN: Code restructure failed: missing block: B:88:0x0155, code lost:
    
        return -1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:90:0x0157, code lost:
    
        r0 = wildCompareInternal(r4.substring(r8), r5.substring(r6));
     */
    /* JADX WARN: Code restructure failed: missing block: B:91:0x0169, code lost:
    
        if (r0 > 0) goto L94;
     */
    /* JADX WARN: Code restructure failed: missing block: B:93:0x016e, code lost:
    
        return r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:95:0x0173, code lost:
    
        if (r8 != r0) goto L128;
     */
    /* JADX WARN: Code restructure failed: missing block: B:96:0x0176, code lost:
    
        return -1;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private static int wildCompareInternal(java.lang.String r4, java.lang.String r5) {
        /*
            Method dump skipped, instructions count: 389
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mysql.jdbc.StringUtils.wildCompareInternal(java.lang.String, java.lang.String):int");
    }

    static byte[] s2b(String s, MySQLConnection conn) throws SQLException {
        if (s == null) {
            return null;
        }
        if (conn != null && conn.getUseUnicode()) {
            try {
                String encoding = conn.getEncoding();
                if (encoding == null) {
                    return s.getBytes();
                }
                SingleByteCharsetConverter converter = conn.getCharsetConverter(encoding);
                if (converter != null) {
                    return converter.toBytes(s);
                }
                return s.getBytes(encoding);
            } catch (UnsupportedEncodingException e) {
                return s.getBytes();
            }
        }
        return s.getBytes();
    }

    public static int lastIndexOf(byte[] s, char c) {
        if (s == null) {
            return -1;
        }
        for (int i = s.length - 1; i >= 0; i--) {
            if (s[i] == c) {
                return i;
            }
        }
        return -1;
    }

    public static int indexOf(byte[] s, char c) {
        if (s == null) {
            return -1;
        }
        int length = s.length;
        for (int i = 0; i < length; i++) {
            if (s[i] == c) {
                return i;
            }
        }
        return -1;
    }

    public static boolean isNullOrEmpty(String toTest) {
        return toTest == null || toTest.length() == 0;
    }

    /* JADX WARN: Removed duplicated region for block: B:115:0x01a6 A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:125:0x002c A[SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static java.lang.String stripComments(java.lang.String r4, java.lang.String r5, java.lang.String r6, boolean r7, boolean r8, boolean r9, boolean r10) throws java.io.IOException {
        /*
            Method dump skipped, instructions count: 445
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mysql.jdbc.StringUtils.stripComments(java.lang.String, java.lang.String, java.lang.String, boolean, boolean, boolean, boolean):java.lang.String");
    }

    public static String sanitizeProcOrFuncName(String src) {
        if (src == null || src.equals(QuickTargetSourceCreator.PREFIX_THREAD_LOCAL)) {
            return null;
        }
        return src;
    }

    public static List<String> splitDBdotName(String source, String catalog, String quoteId, boolean isNoBslashEscSet) {
        int dotIndex;
        String entityName;
        if (source == null || source.equals(QuickTargetSourceCreator.PREFIX_THREAD_LOCAL)) {
            return Collections.emptyList();
        }
        if (SymbolConstants.SPACE_SYMBOL.equals(quoteId)) {
            dotIndex = source.indexOf(".");
        } else {
            dotIndex = indexOfIgnoreCase(0, source, ".", quoteId, quoteId, isNoBslashEscSet ? SEARCH_MODE__MRK_WS : SEARCH_MODE__BSESC_MRK_WS);
        }
        String database = catalog;
        if (dotIndex != -1) {
            database = unQuoteIdentifier(source.substring(0, dotIndex), quoteId);
            entityName = unQuoteIdentifier(source.substring(dotIndex + 1), quoteId);
        } else {
            entityName = unQuoteIdentifier(source, quoteId);
        }
        return Arrays.asList(database, entityName);
    }

    public static boolean isEmptyOrWhitespaceOnly(String str) {
        if (str == null || str.length() == 0) {
            return true;
        }
        int length = str.length();
        for (int i = 0; i < length; i++) {
            if (!Character.isWhitespace(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static String escapeQuote(String src, String quotChar) {
        if (src == null) {
            return null;
        }
        String src2 = toString(stripEnclosure(src.getBytes(), quotChar, quotChar));
        int lastNdx = src2.indexOf(quotChar);
        String tmpSrc = src2.substring(0, lastNdx);
        String tmpSrc2 = tmpSrc + quotChar + quotChar;
        String tmpRest = src2.substring(lastNdx + 1, src2.length());
        int iIndexOf = tmpRest.indexOf(quotChar);
        while (true) {
            int lastNdx2 = iIndexOf;
            if (lastNdx2 > -1) {
                tmpSrc2 = (tmpSrc2 + tmpRest.substring(0, lastNdx2)) + quotChar + quotChar;
                tmpRest = tmpRest.substring(lastNdx2 + 1, tmpRest.length());
                iIndexOf = tmpRest.indexOf(quotChar);
            } else {
                return tmpSrc2 + tmpRest;
            }
        }
    }

    public static String quoteIdentifier(String identifier, String quoteChar, boolean isPedantic) {
        int quoteCharPos;
        int quoteCharNextExpectedPos;
        int quoteCharNextPosition;
        if (identifier == null) {
            return null;
        }
        String identifier2 = identifier.trim();
        int quoteCharLength = quoteChar.length();
        if (quoteCharLength == 0 || SymbolConstants.SPACE_SYMBOL.equals(quoteChar)) {
            return identifier2;
        }
        if (!isPedantic && identifier2.startsWith(quoteChar) && identifier2.endsWith(quoteChar)) {
            String identifierQuoteTrimmed = identifier2.substring(quoteCharLength, identifier2.length() - quoteCharLength);
            int iIndexOf = identifierQuoteTrimmed.indexOf(quoteChar);
            while (true) {
                quoteCharPos = iIndexOf;
                if (quoteCharPos < 0 || (quoteCharNextPosition = identifierQuoteTrimmed.indexOf(quoteChar, (quoteCharNextExpectedPos = quoteCharPos + quoteCharLength))) != quoteCharNextExpectedPos) {
                    break;
                }
                iIndexOf = identifierQuoteTrimmed.indexOf(quoteChar, quoteCharNextPosition + quoteCharLength);
            }
            if (quoteCharPos < 0) {
                return identifier2;
            }
        }
        return quoteChar + identifier2.replaceAll(quoteChar, quoteChar + quoteChar) + quoteChar;
    }

    public static String quoteIdentifier(String identifier, boolean isPedantic) {
        return quoteIdentifier(identifier, "`", isPedantic);
    }

    public static String unQuoteIdentifier(String identifier, String quoteChar) {
        if (identifier == null) {
            return null;
        }
        String identifier2 = identifier.trim();
        int quoteCharLength = quoteChar.length();
        if (quoteCharLength == 0 || SymbolConstants.SPACE_SYMBOL.equals(quoteChar)) {
            return identifier2;
        }
        if (identifier2.startsWith(quoteChar) && identifier2.endsWith(quoteChar)) {
            String identifierQuoteTrimmed = identifier2.substring(quoteCharLength, identifier2.length() - quoteCharLength);
            int iIndexOf = identifierQuoteTrimmed.indexOf(quoteChar);
            while (true) {
                int quoteCharPos = iIndexOf;
                if (quoteCharPos >= 0) {
                    int quoteCharNextExpectedPos = quoteCharPos + quoteCharLength;
                    int quoteCharNextPosition = identifierQuoteTrimmed.indexOf(quoteChar, quoteCharNextExpectedPos);
                    if (quoteCharNextPosition == quoteCharNextExpectedPos) {
                        iIndexOf = identifierQuoteTrimmed.indexOf(quoteChar, quoteCharNextPosition + quoteCharLength);
                    } else {
                        return identifier2;
                    }
                } else {
                    return identifier2.substring(quoteCharLength, identifier2.length() - quoteCharLength).replaceAll(quoteChar + quoteChar, quoteChar);
                }
            }
        } else {
            return identifier2;
        }
    }

    public static int indexOfQuoteDoubleAware(String searchIn, String quoteChar, int startFrom) {
        if (searchIn == null || quoteChar == null || quoteChar.length() == 0 || startFrom > searchIn.length()) {
            return -1;
        }
        int lastIndex = searchIn.length() - 1;
        int beginPos = startFrom;
        int pos = -1;
        boolean next = true;
        while (next) {
            pos = searchIn.indexOf(quoteChar, beginPos);
            if (pos == -1 || pos == lastIndex || !searchIn.startsWith(quoteChar, pos + 1)) {
                next = false;
            } else {
                beginPos = pos + 2;
            }
        }
        return pos;
    }

    public static String toString(byte[] value, int offset, int length, String encoding) throws UnsupportedEncodingException {
        Charset cs = findCharset(encoding);
        return cs.decode(ByteBuffer.wrap(value, offset, length)).toString();
    }

    public static String toString(byte[] value, String encoding) throws UnsupportedEncodingException {
        Charset cs = findCharset(encoding);
        return cs.decode(ByteBuffer.wrap(value)).toString();
    }

    public static String toString(byte[] value, int offset, int length) {
        try {
            Charset cs = findCharset(platformEncoding);
            return cs.decode(ByteBuffer.wrap(value, offset, length)).toString();
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    public static String toString(byte[] value) {
        try {
            Charset cs = findCharset(platformEncoding);
            return cs.decode(ByteBuffer.wrap(value)).toString();
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    public static byte[] getBytes(char[] value) {
        try {
            return getBytes(value, 0, value.length, platformEncoding);
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    public static byte[] getBytes(char[] value, int offset, int length) {
        try {
            return getBytes(value, offset, length, platformEncoding);
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    public static byte[] getBytes(char[] value, String encoding) throws UnsupportedEncodingException {
        return getBytes(value, 0, value.length, encoding);
    }

    public static byte[] getBytes(char[] value, int offset, int length, String encoding) throws UnsupportedEncodingException {
        Charset cs = findCharset(encoding);
        ByteBuffer buf = cs.encode(CharBuffer.wrap(value, offset, length));
        int encodedLen = buf.limit();
        byte[] asBytes = new byte[encodedLen];
        buf.get(asBytes, 0, encodedLen);
        return asBytes;
    }

    public static byte[] getBytes(String value) {
        try {
            return getBytes(value, 0, value.length(), platformEncoding);
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    public static byte[] getBytes(String value, int offset, int length) {
        try {
            return getBytes(value, offset, length, platformEncoding);
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    public static byte[] getBytes(String value, String encoding) throws UnsupportedEncodingException {
        return getBytes(value, 0, value.length(), encoding);
    }

    public static byte[] getBytes(String value, int offset, int length, String encoding) throws UnsupportedEncodingException {
        if (!Util.isJdbc4()) {
            if (offset != 0 || length != value.length()) {
                return value.substring(offset, offset + length).getBytes(encoding);
            }
            return value.getBytes(encoding);
        }
        Charset cs = findCharset(encoding);
        ByteBuffer buf = cs.encode(CharBuffer.wrap(value.toCharArray(), offset, length));
        int encodedLen = buf.limit();
        byte[] asBytes = new byte[encodedLen];
        buf.get(asBytes, 0, encodedLen);
        return asBytes;
    }

    public static final boolean isValidIdChar(char c) {
        return VALID_ID_CHARS.indexOf(c) != -1;
    }

    public static void appendAsHex(StringBuilder builder, byte[] bytes) {
        builder.append("0x");
        for (byte b : bytes) {
            builder.append(HEX_DIGITS[(b >>> 4) & 15]).append(HEX_DIGITS[b & 15]);
        }
    }

    public static void appendAsHex(StringBuilder builder, int value) {
        if (value == 0) {
            builder.append("0x0");
            return;
        }
        int shift = 32;
        boolean nonZeroFound = false;
        builder.append("0x");
        do {
            shift -= 4;
            byte nibble = (byte) ((value >>> shift) & 15);
            if (nonZeroFound) {
                builder.append(HEX_DIGITS[nibble]);
            } else if (nibble != 0) {
                builder.append(HEX_DIGITS[nibble]);
                nonZeroFound = true;
            }
        } while (shift != 0);
    }

    public static byte[] getBytesNullTerminated(String value, String encoding) throws UnsupportedEncodingException {
        Charset cs = findCharset(encoding);
        ByteBuffer buf = cs.encode(value);
        int encodedLen = buf.limit();
        byte[] asBytes = new byte[encodedLen + 1];
        buf.get(asBytes, 0, encodedLen);
        asBytes[encodedLen] = 0;
        return asBytes;
    }

    public static boolean isStrictlyNumeric(CharSequence cs) {
        if (cs == null || cs.length() == 0) {
            return false;
        }
        for (int i = 0; i < cs.length(); i++) {
            if (!Character.isDigit(cs.charAt(i))) {
                return false;
            }
        }
        return true;
    }
}
