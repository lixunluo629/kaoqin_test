package org.apache.commons.lang.text;

import java.io.Reader;
import java.io.Writer;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.SystemUtils;

/* loaded from: commons-lang-2.6.jar:org/apache/commons/lang/text/StrBuilder.class */
public class StrBuilder implements Cloneable {
    static final int CAPACITY = 32;
    private static final long serialVersionUID = 7628716375283629643L;
    protected char[] buffer;
    protected int size;
    private String newLine;
    private String nullText;

    public StrBuilder() {
        this(32);
    }

    public StrBuilder(int initialCapacity) {
        this.buffer = new char[initialCapacity <= 0 ? 32 : initialCapacity];
    }

    public StrBuilder(String str) {
        if (str == null) {
            this.buffer = new char[32];
        } else {
            this.buffer = new char[str.length() + 32];
            append(str);
        }
    }

    public String getNewLineText() {
        return this.newLine;
    }

    public StrBuilder setNewLineText(String newLine) {
        this.newLine = newLine;
        return this;
    }

    public String getNullText() {
        return this.nullText;
    }

    public StrBuilder setNullText(String nullText) {
        if (nullText != null && nullText.length() == 0) {
            nullText = null;
        }
        this.nullText = nullText;
        return this;
    }

    public int length() {
        return this.size;
    }

    public StrBuilder setLength(int length) {
        if (length < 0) {
            throw new StringIndexOutOfBoundsException(length);
        }
        if (length < this.size) {
            this.size = length;
        } else if (length > this.size) {
            ensureCapacity(length);
            int oldEnd = this.size;
            this.size = length;
            for (int i = oldEnd; i < length; i++) {
                this.buffer[i] = 0;
            }
        }
        return this;
    }

    public int capacity() {
        return this.buffer.length;
    }

    public StrBuilder ensureCapacity(int capacity) {
        if (capacity > this.buffer.length) {
            char[] old = this.buffer;
            this.buffer = new char[capacity * 2];
            System.arraycopy(old, 0, this.buffer, 0, this.size);
        }
        return this;
    }

    public StrBuilder minimizeCapacity() {
        if (this.buffer.length > length()) {
            char[] old = this.buffer;
            this.buffer = new char[length()];
            System.arraycopy(old, 0, this.buffer, 0, this.size);
        }
        return this;
    }

    public int size() {
        return this.size;
    }

    public boolean isEmpty() {
        return this.size == 0;
    }

    public StrBuilder clear() {
        this.size = 0;
        return this;
    }

    public char charAt(int index) {
        if (index < 0 || index >= length()) {
            throw new StringIndexOutOfBoundsException(index);
        }
        return this.buffer[index];
    }

    public StrBuilder setCharAt(int index, char ch2) {
        if (index < 0 || index >= length()) {
            throw new StringIndexOutOfBoundsException(index);
        }
        this.buffer[index] = ch2;
        return this;
    }

    public StrBuilder deleteCharAt(int index) {
        if (index < 0 || index >= this.size) {
            throw new StringIndexOutOfBoundsException(index);
        }
        deleteImpl(index, index + 1, 1);
        return this;
    }

    public char[] toCharArray() {
        if (this.size == 0) {
            return ArrayUtils.EMPTY_CHAR_ARRAY;
        }
        char[] chars = new char[this.size];
        System.arraycopy(this.buffer, 0, chars, 0, this.size);
        return chars;
    }

    public char[] toCharArray(int startIndex, int endIndex) {
        int len = validateRange(startIndex, endIndex) - startIndex;
        if (len == 0) {
            return ArrayUtils.EMPTY_CHAR_ARRAY;
        }
        char[] chars = new char[len];
        System.arraycopy(this.buffer, startIndex, chars, 0, len);
        return chars;
    }

    public char[] getChars(char[] destination) {
        int len = length();
        if (destination == null || destination.length < len) {
            destination = new char[len];
        }
        System.arraycopy(this.buffer, 0, destination, 0, len);
        return destination;
    }

    public void getChars(int startIndex, int endIndex, char[] destination, int destinationIndex) {
        if (startIndex < 0) {
            throw new StringIndexOutOfBoundsException(startIndex);
        }
        if (endIndex < 0 || endIndex > length()) {
            throw new StringIndexOutOfBoundsException(endIndex);
        }
        if (startIndex > endIndex) {
            throw new StringIndexOutOfBoundsException("end < start");
        }
        System.arraycopy(this.buffer, startIndex, destination, destinationIndex, endIndex - startIndex);
    }

    public StrBuilder appendNewLine() {
        if (this.newLine == null) {
            append(SystemUtils.LINE_SEPARATOR);
            return this;
        }
        return append(this.newLine);
    }

    public StrBuilder appendNull() {
        if (this.nullText == null) {
            return this;
        }
        return append(this.nullText);
    }

    public StrBuilder append(Object obj) {
        if (obj == null) {
            return appendNull();
        }
        return append(obj.toString());
    }

    public StrBuilder append(String str) {
        if (str == null) {
            return appendNull();
        }
        int strLen = str.length();
        if (strLen > 0) {
            int len = length();
            ensureCapacity(len + strLen);
            str.getChars(0, strLen, this.buffer, len);
            this.size += strLen;
        }
        return this;
    }

    public StrBuilder append(String str, int startIndex, int length) {
        if (str == null) {
            return appendNull();
        }
        if (startIndex < 0 || startIndex > str.length()) {
            throw new StringIndexOutOfBoundsException("startIndex must be valid");
        }
        if (length < 0 || startIndex + length > str.length()) {
            throw new StringIndexOutOfBoundsException("length must be valid");
        }
        if (length > 0) {
            int len = length();
            ensureCapacity(len + length);
            str.getChars(startIndex, startIndex + length, this.buffer, len);
            this.size += length;
        }
        return this;
    }

    public StrBuilder append(StringBuffer str) {
        if (str == null) {
            return appendNull();
        }
        int strLen = str.length();
        if (strLen > 0) {
            int len = length();
            ensureCapacity(len + strLen);
            str.getChars(0, strLen, this.buffer, len);
            this.size += strLen;
        }
        return this;
    }

    public StrBuilder append(StringBuffer str, int startIndex, int length) {
        if (str == null) {
            return appendNull();
        }
        if (startIndex < 0 || startIndex > str.length()) {
            throw new StringIndexOutOfBoundsException("startIndex must be valid");
        }
        if (length < 0 || startIndex + length > str.length()) {
            throw new StringIndexOutOfBoundsException("length must be valid");
        }
        if (length > 0) {
            int len = length();
            ensureCapacity(len + length);
            str.getChars(startIndex, startIndex + length, this.buffer, len);
            this.size += length;
        }
        return this;
    }

    public StrBuilder append(StrBuilder str) {
        if (str == null) {
            return appendNull();
        }
        int strLen = str.length();
        if (strLen > 0) {
            int len = length();
            ensureCapacity(len + strLen);
            System.arraycopy(str.buffer, 0, this.buffer, len, strLen);
            this.size += strLen;
        }
        return this;
    }

    public StrBuilder append(StrBuilder str, int startIndex, int length) {
        if (str == null) {
            return appendNull();
        }
        if (startIndex < 0 || startIndex > str.length()) {
            throw new StringIndexOutOfBoundsException("startIndex must be valid");
        }
        if (length < 0 || startIndex + length > str.length()) {
            throw new StringIndexOutOfBoundsException("length must be valid");
        }
        if (length > 0) {
            int len = length();
            ensureCapacity(len + length);
            str.getChars(startIndex, startIndex + length, this.buffer, len);
            this.size += length;
        }
        return this;
    }

    public StrBuilder append(char[] chars) {
        if (chars == null) {
            return appendNull();
        }
        int strLen = chars.length;
        if (strLen > 0) {
            int len = length();
            ensureCapacity(len + strLen);
            System.arraycopy(chars, 0, this.buffer, len, strLen);
            this.size += strLen;
        }
        return this;
    }

    public StrBuilder append(char[] chars, int startIndex, int length) {
        if (chars == null) {
            return appendNull();
        }
        if (startIndex < 0 || startIndex > chars.length) {
            throw new StringIndexOutOfBoundsException(new StringBuffer().append("Invalid startIndex: ").append(length).toString());
        }
        if (length < 0 || startIndex + length > chars.length) {
            throw new StringIndexOutOfBoundsException(new StringBuffer().append("Invalid length: ").append(length).toString());
        }
        if (length > 0) {
            int len = length();
            ensureCapacity(len + length);
            System.arraycopy(chars, startIndex, this.buffer, len, length);
            this.size += length;
        }
        return this;
    }

    public StrBuilder append(boolean value) {
        if (value) {
            ensureCapacity(this.size + 4);
            char[] cArr = this.buffer;
            int i = this.size;
            this.size = i + 1;
            cArr[i] = 't';
            char[] cArr2 = this.buffer;
            int i2 = this.size;
            this.size = i2 + 1;
            cArr2[i2] = 'r';
            char[] cArr3 = this.buffer;
            int i3 = this.size;
            this.size = i3 + 1;
            cArr3[i3] = 'u';
            char[] cArr4 = this.buffer;
            int i4 = this.size;
            this.size = i4 + 1;
            cArr4[i4] = 'e';
        } else {
            ensureCapacity(this.size + 5);
            char[] cArr5 = this.buffer;
            int i5 = this.size;
            this.size = i5 + 1;
            cArr5[i5] = 'f';
            char[] cArr6 = this.buffer;
            int i6 = this.size;
            this.size = i6 + 1;
            cArr6[i6] = 'a';
            char[] cArr7 = this.buffer;
            int i7 = this.size;
            this.size = i7 + 1;
            cArr7[i7] = 'l';
            char[] cArr8 = this.buffer;
            int i8 = this.size;
            this.size = i8 + 1;
            cArr8[i8] = 's';
            char[] cArr9 = this.buffer;
            int i9 = this.size;
            this.size = i9 + 1;
            cArr9[i9] = 'e';
        }
        return this;
    }

    public StrBuilder append(char ch2) {
        int len = length();
        ensureCapacity(len + 1);
        char[] cArr = this.buffer;
        int i = this.size;
        this.size = i + 1;
        cArr[i] = ch2;
        return this;
    }

    public StrBuilder append(int value) {
        return append(String.valueOf(value));
    }

    public StrBuilder append(long value) {
        return append(String.valueOf(value));
    }

    public StrBuilder append(float value) {
        return append(String.valueOf(value));
    }

    public StrBuilder append(double value) {
        return append(String.valueOf(value));
    }

    public StrBuilder appendln(Object obj) {
        return append(obj).appendNewLine();
    }

    public StrBuilder appendln(String str) {
        return append(str).appendNewLine();
    }

    public StrBuilder appendln(String str, int startIndex, int length) {
        return append(str, startIndex, length).appendNewLine();
    }

    public StrBuilder appendln(StringBuffer str) {
        return append(str).appendNewLine();
    }

    public StrBuilder appendln(StringBuffer str, int startIndex, int length) {
        return append(str, startIndex, length).appendNewLine();
    }

    public StrBuilder appendln(StrBuilder str) {
        return append(str).appendNewLine();
    }

    public StrBuilder appendln(StrBuilder str, int startIndex, int length) {
        return append(str, startIndex, length).appendNewLine();
    }

    public StrBuilder appendln(char[] chars) {
        return append(chars).appendNewLine();
    }

    public StrBuilder appendln(char[] chars, int startIndex, int length) {
        return append(chars, startIndex, length).appendNewLine();
    }

    public StrBuilder appendln(boolean value) {
        return append(value).appendNewLine();
    }

    public StrBuilder appendln(char ch2) {
        return append(ch2).appendNewLine();
    }

    public StrBuilder appendln(int value) {
        return append(value).appendNewLine();
    }

    public StrBuilder appendln(long value) {
        return append(value).appendNewLine();
    }

    public StrBuilder appendln(float value) {
        return append(value).appendNewLine();
    }

    public StrBuilder appendln(double value) {
        return append(value).appendNewLine();
    }

    public StrBuilder appendAll(Object[] array) {
        if (array != null && array.length > 0) {
            for (Object obj : array) {
                append(obj);
            }
        }
        return this;
    }

    public StrBuilder appendAll(Collection coll) {
        if (coll != null && coll.size() > 0) {
            Iterator it = coll.iterator();
            while (it.hasNext()) {
                append(it.next());
            }
        }
        return this;
    }

    public StrBuilder appendAll(Iterator it) {
        if (it != null) {
            while (it.hasNext()) {
                append(it.next());
            }
        }
        return this;
    }

    public StrBuilder appendWithSeparators(Object[] array, String separator) {
        if (array != null && array.length > 0) {
            String separator2 = separator == null ? "" : separator;
            append(array[0]);
            for (int i = 1; i < array.length; i++) {
                append(separator2);
                append(array[i]);
            }
        }
        return this;
    }

    public StrBuilder appendWithSeparators(Collection coll, String separator) {
        if (coll != null && coll.size() > 0) {
            String separator2 = separator == null ? "" : separator;
            Iterator it = coll.iterator();
            while (it.hasNext()) {
                append(it.next());
                if (it.hasNext()) {
                    append(separator2);
                }
            }
        }
        return this;
    }

    public StrBuilder appendWithSeparators(Iterator it, String separator) {
        if (it != null) {
            String separator2 = separator == null ? "" : separator;
            while (it.hasNext()) {
                append(it.next());
                if (it.hasNext()) {
                    append(separator2);
                }
            }
        }
        return this;
    }

    public StrBuilder appendSeparator(String separator) {
        return appendSeparator(separator, (String) null);
    }

    public StrBuilder appendSeparator(String standard, String defaultIfEmpty) {
        String str = isEmpty() ? defaultIfEmpty : standard;
        if (str != null) {
            append(str);
        }
        return this;
    }

    public StrBuilder appendSeparator(char separator) {
        if (size() > 0) {
            append(separator);
        }
        return this;
    }

    public StrBuilder appendSeparator(char standard, char defaultIfEmpty) {
        if (size() > 0) {
            append(standard);
        } else {
            append(defaultIfEmpty);
        }
        return this;
    }

    public StrBuilder appendSeparator(String separator, int loopIndex) {
        if (separator != null && loopIndex > 0) {
            append(separator);
        }
        return this;
    }

    public StrBuilder appendSeparator(char separator, int loopIndex) {
        if (loopIndex > 0) {
            append(separator);
        }
        return this;
    }

    public StrBuilder appendPadding(int length, char padChar) {
        if (length >= 0) {
            ensureCapacity(this.size + length);
            for (int i = 0; i < length; i++) {
                char[] cArr = this.buffer;
                int i2 = this.size;
                this.size = i2 + 1;
                cArr[i2] = padChar;
            }
        }
        return this;
    }

    public StrBuilder appendFixedWidthPadLeft(Object obj, int width, char padChar) {
        if (width > 0) {
            ensureCapacity(this.size + width);
            String str = obj == null ? getNullText() : obj.toString();
            if (str == null) {
                str = "";
            }
            int strLen = str.length();
            if (strLen >= width) {
                str.getChars(strLen - width, strLen, this.buffer, this.size);
            } else {
                int padLen = width - strLen;
                for (int i = 0; i < padLen; i++) {
                    this.buffer[this.size + i] = padChar;
                }
                str.getChars(0, strLen, this.buffer, this.size + padLen);
            }
            this.size += width;
        }
        return this;
    }

    public StrBuilder appendFixedWidthPadLeft(int value, int width, char padChar) {
        return appendFixedWidthPadLeft(String.valueOf(value), width, padChar);
    }

    public StrBuilder appendFixedWidthPadRight(Object obj, int width, char padChar) {
        if (width > 0) {
            ensureCapacity(this.size + width);
            String str = obj == null ? getNullText() : obj.toString();
            if (str == null) {
                str = "";
            }
            int strLen = str.length();
            if (strLen >= width) {
                str.getChars(0, width, this.buffer, this.size);
            } else {
                int padLen = width - strLen;
                str.getChars(0, strLen, this.buffer, this.size);
                for (int i = 0; i < padLen; i++) {
                    this.buffer[this.size + strLen + i] = padChar;
                }
            }
            this.size += width;
        }
        return this;
    }

    public StrBuilder appendFixedWidthPadRight(int value, int width, char padChar) {
        return appendFixedWidthPadRight(String.valueOf(value), width, padChar);
    }

    public StrBuilder insert(int index, Object obj) {
        if (obj == null) {
            return insert(index, this.nullText);
        }
        return insert(index, obj.toString());
    }

    public StrBuilder insert(int index, String str) {
        validateIndex(index);
        if (str == null) {
            str = this.nullText;
        }
        int strLen = str == null ? 0 : str.length();
        if (strLen > 0) {
            int newSize = this.size + strLen;
            ensureCapacity(newSize);
            System.arraycopy(this.buffer, index, this.buffer, index + strLen, this.size - index);
            this.size = newSize;
            str.getChars(0, strLen, this.buffer, index);
        }
        return this;
    }

    public StrBuilder insert(int index, char[] chars) {
        validateIndex(index);
        if (chars == null) {
            return insert(index, this.nullText);
        }
        int len = chars.length;
        if (len > 0) {
            ensureCapacity(this.size + len);
            System.arraycopy(this.buffer, index, this.buffer, index + len, this.size - index);
            System.arraycopy(chars, 0, this.buffer, index, len);
            this.size += len;
        }
        return this;
    }

    public StrBuilder insert(int index, char[] chars, int offset, int length) {
        validateIndex(index);
        if (chars == null) {
            return insert(index, this.nullText);
        }
        if (offset < 0 || offset > chars.length) {
            throw new StringIndexOutOfBoundsException(new StringBuffer().append("Invalid offset: ").append(offset).toString());
        }
        if (length < 0 || offset + length > chars.length) {
            throw new StringIndexOutOfBoundsException(new StringBuffer().append("Invalid length: ").append(length).toString());
        }
        if (length > 0) {
            ensureCapacity(this.size + length);
            System.arraycopy(this.buffer, index, this.buffer, index + length, this.size - index);
            System.arraycopy(chars, offset, this.buffer, index, length);
            this.size += length;
        }
        return this;
    }

    public StrBuilder insert(int index, boolean value) {
        validateIndex(index);
        if (value) {
            ensureCapacity(this.size + 4);
            System.arraycopy(this.buffer, index, this.buffer, index + 4, this.size - index);
            int index2 = index + 1;
            this.buffer[index] = 't';
            int index3 = index2 + 1;
            this.buffer[index2] = 'r';
            this.buffer[index3] = 'u';
            this.buffer[index3 + 1] = 'e';
            this.size += 4;
        } else {
            ensureCapacity(this.size + 5);
            System.arraycopy(this.buffer, index, this.buffer, index + 5, this.size - index);
            int index4 = index + 1;
            this.buffer[index] = 'f';
            int index5 = index4 + 1;
            this.buffer[index4] = 'a';
            int index6 = index5 + 1;
            this.buffer[index5] = 'l';
            this.buffer[index6] = 's';
            this.buffer[index6 + 1] = 'e';
            this.size += 5;
        }
        return this;
    }

    public StrBuilder insert(int index, char value) {
        validateIndex(index);
        ensureCapacity(this.size + 1);
        System.arraycopy(this.buffer, index, this.buffer, index + 1, this.size - index);
        this.buffer[index] = value;
        this.size++;
        return this;
    }

    public StrBuilder insert(int index, int value) {
        return insert(index, String.valueOf(value));
    }

    public StrBuilder insert(int index, long value) {
        return insert(index, String.valueOf(value));
    }

    public StrBuilder insert(int index, float value) {
        return insert(index, String.valueOf(value));
    }

    public StrBuilder insert(int index, double value) {
        return insert(index, String.valueOf(value));
    }

    private void deleteImpl(int startIndex, int endIndex, int len) {
        System.arraycopy(this.buffer, endIndex, this.buffer, startIndex, this.size - endIndex);
        this.size -= len;
    }

    public StrBuilder delete(int startIndex, int endIndex) {
        int endIndex2 = validateRange(startIndex, endIndex);
        int len = endIndex2 - startIndex;
        if (len > 0) {
            deleteImpl(startIndex, endIndex2, len);
        }
        return this;
    }

    public StrBuilder deleteAll(char ch2) {
        int i = 0;
        while (i < this.size) {
            if (this.buffer[i] == ch2) {
                int start = i;
                do {
                    i++;
                    if (i >= this.size) {
                        break;
                    }
                } while (this.buffer[i] == ch2);
                int len = i - start;
                deleteImpl(start, i, len);
                i -= len;
            }
            i++;
        }
        return this;
    }

    public StrBuilder deleteFirst(char ch2) {
        int i = 0;
        while (true) {
            if (i >= this.size) {
                break;
            }
            if (this.buffer[i] != ch2) {
                i++;
            } else {
                deleteImpl(i, i + 1, 1);
                break;
            }
        }
        return this;
    }

    public StrBuilder deleteAll(String str) {
        int len = str == null ? 0 : str.length();
        if (len > 0) {
            int iIndexOf = indexOf(str, 0);
            while (true) {
                int index = iIndexOf;
                if (index < 0) {
                    break;
                }
                deleteImpl(index, index + len, len);
                iIndexOf = indexOf(str, index);
            }
        }
        return this;
    }

    public StrBuilder deleteFirst(String str) {
        int index;
        int len = str == null ? 0 : str.length();
        if (len > 0 && (index = indexOf(str, 0)) >= 0) {
            deleteImpl(index, index + len, len);
        }
        return this;
    }

    public StrBuilder deleteAll(StrMatcher matcher) {
        return replace(matcher, null, 0, this.size, -1);
    }

    public StrBuilder deleteFirst(StrMatcher matcher) {
        return replace(matcher, null, 0, this.size, 1);
    }

    private void replaceImpl(int startIndex, int endIndex, int removeLen, String insertStr, int insertLen) {
        int newSize = (this.size - removeLen) + insertLen;
        if (insertLen != removeLen) {
            ensureCapacity(newSize);
            System.arraycopy(this.buffer, endIndex, this.buffer, startIndex + insertLen, this.size - endIndex);
            this.size = newSize;
        }
        if (insertLen > 0) {
            insertStr.getChars(0, insertLen, this.buffer, startIndex);
        }
    }

    public StrBuilder replace(int startIndex, int endIndex, String replaceStr) {
        int endIndex2 = validateRange(startIndex, endIndex);
        int insertLen = replaceStr == null ? 0 : replaceStr.length();
        replaceImpl(startIndex, endIndex2, endIndex2 - startIndex, replaceStr, insertLen);
        return this;
    }

    public StrBuilder replaceAll(char search, char replace) {
        if (search != replace) {
            for (int i = 0; i < this.size; i++) {
                if (this.buffer[i] == search) {
                    this.buffer[i] = replace;
                }
            }
        }
        return this;
    }

    public StrBuilder replaceFirst(char search, char replace) {
        if (search != replace) {
            int i = 0;
            while (true) {
                if (i >= this.size) {
                    break;
                }
                if (this.buffer[i] != search) {
                    i++;
                } else {
                    this.buffer[i] = replace;
                    break;
                }
            }
        }
        return this;
    }

    public StrBuilder replaceAll(String searchStr, String replaceStr) {
        int searchLen = searchStr == null ? 0 : searchStr.length();
        if (searchLen > 0) {
            int replaceLen = replaceStr == null ? 0 : replaceStr.length();
            int iIndexOf = indexOf(searchStr, 0);
            while (true) {
                int index = iIndexOf;
                if (index < 0) {
                    break;
                }
                replaceImpl(index, index + searchLen, searchLen, replaceStr, replaceLen);
                iIndexOf = indexOf(searchStr, index + replaceLen);
            }
        }
        return this;
    }

    public StrBuilder replaceFirst(String searchStr, String replaceStr) {
        int index;
        int searchLen = searchStr == null ? 0 : searchStr.length();
        if (searchLen > 0 && (index = indexOf(searchStr, 0)) >= 0) {
            int replaceLen = replaceStr == null ? 0 : replaceStr.length();
            replaceImpl(index, index + searchLen, searchLen, replaceStr, replaceLen);
        }
        return this;
    }

    public StrBuilder replaceAll(StrMatcher matcher, String replaceStr) {
        return replace(matcher, replaceStr, 0, this.size, -1);
    }

    public StrBuilder replaceFirst(StrMatcher matcher, String replaceStr) {
        return replace(matcher, replaceStr, 0, this.size, 1);
    }

    public StrBuilder replace(StrMatcher matcher, String replaceStr, int startIndex, int endIndex, int replaceCount) {
        return replaceImpl(matcher, replaceStr, startIndex, validateRange(startIndex, endIndex), replaceCount);
    }

    private StrBuilder replaceImpl(StrMatcher matcher, String replaceStr, int from, int to, int replaceCount) {
        if (matcher == null || this.size == 0) {
            return this;
        }
        int replaceLen = replaceStr == null ? 0 : replaceStr.length();
        char[] buf = this.buffer;
        int i = from;
        while (i < to && replaceCount != 0) {
            int removeLen = matcher.isMatch(buf, i, from, to);
            if (removeLen > 0) {
                replaceImpl(i, i + removeLen, removeLen, replaceStr, replaceLen);
                to = (to - removeLen) + replaceLen;
                i = (i + replaceLen) - 1;
                if (replaceCount > 0) {
                    replaceCount--;
                }
            }
            i++;
        }
        return this;
    }

    public StrBuilder reverse() {
        if (this.size == 0) {
            return this;
        }
        int half = this.size / 2;
        char[] buf = this.buffer;
        int leftIdx = 0;
        int rightIdx = this.size - 1;
        while (leftIdx < half) {
            char swap = buf[leftIdx];
            buf[leftIdx] = buf[rightIdx];
            buf[rightIdx] = swap;
            leftIdx++;
            rightIdx--;
        }
        return this;
    }

    public StrBuilder trim() {
        if (this.size == 0) {
            return this;
        }
        int len = this.size;
        char[] buf = this.buffer;
        int pos = 0;
        while (pos < len && buf[pos] <= ' ') {
            pos++;
        }
        while (pos < len && buf[len - 1] <= ' ') {
            len--;
        }
        if (len < this.size) {
            delete(len, this.size);
        }
        if (pos > 0) {
            delete(0, pos);
        }
        return this;
    }

    public boolean startsWith(String str) {
        if (str == null) {
            return false;
        }
        int len = str.length();
        if (len == 0) {
            return true;
        }
        if (len > this.size) {
            return false;
        }
        for (int i = 0; i < len; i++) {
            if (this.buffer[i] != str.charAt(i)) {
                return false;
            }
        }
        return true;
    }

    public boolean endsWith(String str) {
        if (str == null) {
            return false;
        }
        int len = str.length();
        if (len == 0) {
            return true;
        }
        if (len > this.size) {
            return false;
        }
        int pos = this.size - len;
        int i = 0;
        while (i < len) {
            if (this.buffer[pos] == str.charAt(i)) {
                i++;
                pos++;
            } else {
                return false;
            }
        }
        return true;
    }

    public String substring(int start) {
        return substring(start, this.size);
    }

    public String substring(int startIndex, int endIndex) {
        return new String(this.buffer, startIndex, validateRange(startIndex, endIndex) - startIndex);
    }

    public String leftString(int length) {
        if (length <= 0) {
            return "";
        }
        if (length >= this.size) {
            return new String(this.buffer, 0, this.size);
        }
        return new String(this.buffer, 0, length);
    }

    public String rightString(int length) {
        if (length <= 0) {
            return "";
        }
        if (length >= this.size) {
            return new String(this.buffer, 0, this.size);
        }
        return new String(this.buffer, this.size - length, length);
    }

    public String midString(int index, int length) {
        if (index < 0) {
            index = 0;
        }
        if (length <= 0 || index >= this.size) {
            return "";
        }
        if (this.size <= index + length) {
            return new String(this.buffer, index, this.size - index);
        }
        return new String(this.buffer, index, length);
    }

    public boolean contains(char ch2) {
        char[] thisBuf = this.buffer;
        for (int i = 0; i < this.size; i++) {
            if (thisBuf[i] == ch2) {
                return true;
            }
        }
        return false;
    }

    public boolean contains(String str) {
        return indexOf(str, 0) >= 0;
    }

    public boolean contains(StrMatcher matcher) {
        return indexOf(matcher, 0) >= 0;
    }

    public int indexOf(char ch2) {
        return indexOf(ch2, 0);
    }

    public int indexOf(char ch2, int startIndex) {
        int startIndex2 = startIndex < 0 ? 0 : startIndex;
        if (startIndex2 >= this.size) {
            return -1;
        }
        char[] thisBuf = this.buffer;
        for (int i = startIndex2; i < this.size; i++) {
            if (thisBuf[i] == ch2) {
                return i;
            }
        }
        return -1;
    }

    public int indexOf(String str) {
        return indexOf(str, 0);
    }

    /* JADX WARN: Code restructure failed: missing block: B:36:0x007d, code lost:
    
        r11 = r11 + 1;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public int indexOf(java.lang.String r6, int r7) {
        /*
            r5 = this;
            r0 = r7
            if (r0 >= 0) goto L8
            r0 = 0
            goto L9
        L8:
            r0 = r7
        L9:
            r7 = r0
            r0 = r6
            if (r0 == 0) goto L16
            r0 = r7
            r1 = r5
            int r1 = r1.size
            if (r0 < r1) goto L18
        L16:
            r0 = -1
            return r0
        L18:
            r0 = r6
            int r0 = r0.length()
            r8 = r0
            r0 = r8
            r1 = 1
            if (r0 != r1) goto L2d
            r0 = r5
            r1 = r6
            r2 = 0
            char r1 = r1.charAt(r2)
            r2 = r7
            int r0 = r0.indexOf(r1, r2)
            return r0
        L2d:
            r0 = r8
            if (r0 != 0) goto L33
            r0 = r7
            return r0
        L33:
            r0 = r8
            r1 = r5
            int r1 = r1.size
            if (r0 <= r1) goto L3d
            r0 = -1
            return r0
        L3d:
            r0 = r5
            char[] r0 = r0.buffer
            r9 = r0
            r0 = r5
            int r0 = r0.size
            r1 = r8
            int r0 = r0 - r1
            r1 = 1
            int r0 = r0 + r1
            r10 = r0
            r0 = r7
            r11 = r0
        L50:
            r0 = r11
            r1 = r10
            if (r0 >= r1) goto L83
            r0 = 0
            r12 = r0
        L5a:
            r0 = r12
            r1 = r8
            if (r0 >= r1) goto L7a
            r0 = r6
            r1 = r12
            char r0 = r0.charAt(r1)
            r1 = r9
            r2 = r11
            r3 = r12
            int r2 = r2 + r3
            char r1 = r1[r2]
            if (r0 == r1) goto L74
            goto L7d
        L74:
            int r12 = r12 + 1
            goto L5a
        L7a:
            r0 = r11
            return r0
        L7d:
            int r11 = r11 + 1
            goto L50
        L83:
            r0 = -1
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.lang.text.StrBuilder.indexOf(java.lang.String, int):int");
    }

    public int indexOf(StrMatcher matcher) {
        return indexOf(matcher, 0);
    }

    public int indexOf(StrMatcher matcher, int startIndex) {
        int startIndex2 = startIndex < 0 ? 0 : startIndex;
        if (matcher == null || startIndex2 >= this.size) {
            return -1;
        }
        int len = this.size;
        char[] buf = this.buffer;
        for (int i = startIndex2; i < len; i++) {
            if (matcher.isMatch(buf, i, startIndex2, len) > 0) {
                return i;
            }
        }
        return -1;
    }

    public int lastIndexOf(char ch2) {
        return lastIndexOf(ch2, this.size - 1);
    }

    public int lastIndexOf(char ch2, int startIndex) {
        int startIndex2 = startIndex >= this.size ? this.size - 1 : startIndex;
        if (startIndex2 < 0) {
            return -1;
        }
        for (int i = startIndex2; i >= 0; i--) {
            if (this.buffer[i] == ch2) {
                return i;
            }
        }
        return -1;
    }

    public int lastIndexOf(String str) {
        return lastIndexOf(str, this.size - 1);
    }

    /* JADX WARN: Code restructure failed: missing block: B:32:0x0072, code lost:
    
        r9 = r9 - 1;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public int lastIndexOf(java.lang.String r6, int r7) {
        /*
            r5 = this;
            r0 = r7
            r1 = r5
            int r1 = r1.size
            if (r0 < r1) goto L11
            r0 = r5
            int r0 = r0.size
            r1 = 1
            int r0 = r0 - r1
            goto L12
        L11:
            r0 = r7
        L12:
            r7 = r0
            r0 = r6
            if (r0 == 0) goto L1b
            r0 = r7
            if (r0 >= 0) goto L1d
        L1b:
            r0 = -1
            return r0
        L1d:
            r0 = r6
            int r0 = r0.length()
            r8 = r0
            r0 = r8
            if (r0 <= 0) goto L7b
            r0 = r8
            r1 = r5
            int r1 = r1.size
            if (r0 > r1) goto L7b
            r0 = r8
            r1 = 1
            if (r0 != r1) goto L3e
            r0 = r5
            r1 = r6
            r2 = 0
            char r1 = r1.charAt(r2)
            r2 = r7
            int r0 = r0.lastIndexOf(r1, r2)
            return r0
        L3e:
            r0 = r7
            r1 = r8
            int r0 = r0 - r1
            r1 = 1
            int r0 = r0 + r1
            r9 = r0
        L45:
            r0 = r9
            if (r0 < 0) goto L78
            r0 = 0
            r10 = r0
        L4d:
            r0 = r10
            r1 = r8
            if (r0 >= r1) goto L6f
            r0 = r6
            r1 = r10
            char r0 = r0.charAt(r1)
            r1 = r5
            char[] r1 = r1.buffer
            r2 = r9
            r3 = r10
            int r2 = r2 + r3
            char r1 = r1[r2]
            if (r0 == r1) goto L69
            goto L72
        L69:
            int r10 = r10 + 1
            goto L4d
        L6f:
            r0 = r9
            return r0
        L72:
            int r9 = r9 + (-1)
            goto L45
        L78:
            goto L81
        L7b:
            r0 = r8
            if (r0 != 0) goto L81
            r0 = r7
            return r0
        L81:
            r0 = -1
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.lang.text.StrBuilder.lastIndexOf(java.lang.String, int):int");
    }

    public int lastIndexOf(StrMatcher matcher) {
        return lastIndexOf(matcher, this.size);
    }

    public int lastIndexOf(StrMatcher matcher, int startIndex) {
        int startIndex2 = startIndex >= this.size ? this.size - 1 : startIndex;
        if (matcher == null || startIndex2 < 0) {
            return -1;
        }
        char[] buf = this.buffer;
        int endIndex = startIndex2 + 1;
        for (int i = startIndex2; i >= 0; i--) {
            if (matcher.isMatch(buf, i, 0, endIndex) > 0) {
                return i;
            }
        }
        return -1;
    }

    public StrTokenizer asTokenizer() {
        return new StrBuilderTokenizer(this);
    }

    public Reader asReader() {
        return new StrBuilderReader(this);
    }

    public Writer asWriter() {
        return new StrBuilderWriter(this);
    }

    public boolean equalsIgnoreCase(StrBuilder other) {
        if (this == other) {
            return true;
        }
        if (this.size != other.size) {
            return false;
        }
        char[] thisBuf = this.buffer;
        char[] otherBuf = other.buffer;
        for (int i = this.size - 1; i >= 0; i--) {
            char c1 = thisBuf[i];
            char c2 = otherBuf[i];
            if (c1 != c2 && Character.toUpperCase(c1) != Character.toUpperCase(c2)) {
                return false;
            }
        }
        return true;
    }

    public boolean equals(StrBuilder other) {
        if (this == other) {
            return true;
        }
        if (this.size != other.size) {
            return false;
        }
        char[] thisBuf = this.buffer;
        char[] otherBuf = other.buffer;
        for (int i = this.size - 1; i >= 0; i--) {
            if (thisBuf[i] != otherBuf[i]) {
                return false;
            }
        }
        return true;
    }

    public boolean equals(Object obj) {
        if (obj instanceof StrBuilder) {
            return equals((StrBuilder) obj);
        }
        return false;
    }

    public int hashCode() {
        char[] buf = this.buffer;
        int hash = 0;
        for (int i = this.size - 1; i >= 0; i--) {
            hash = (31 * hash) + buf[i];
        }
        return hash;
    }

    public String toString() {
        return new String(this.buffer, 0, this.size);
    }

    public StringBuffer toStringBuffer() {
        return new StringBuffer(this.size).append(this.buffer, 0, this.size);
    }

    public Object clone() throws CloneNotSupportedException {
        StrBuilder clone = (StrBuilder) super.clone();
        clone.buffer = new char[this.buffer.length];
        System.arraycopy(this.buffer, 0, clone.buffer, 0, this.buffer.length);
        return clone;
    }

    protected int validateRange(int startIndex, int endIndex) {
        if (startIndex < 0) {
            throw new StringIndexOutOfBoundsException(startIndex);
        }
        if (endIndex > this.size) {
            endIndex = this.size;
        }
        if (startIndex > endIndex) {
            throw new StringIndexOutOfBoundsException("end < start");
        }
        return endIndex;
    }

    protected void validateIndex(int index) {
        if (index < 0 || index > this.size) {
            throw new StringIndexOutOfBoundsException(index);
        }
    }

    /* loaded from: commons-lang-2.6.jar:org/apache/commons/lang/text/StrBuilder$StrBuilderTokenizer.class */
    class StrBuilderTokenizer extends StrTokenizer {
        private final StrBuilder this$0;

        StrBuilderTokenizer(StrBuilder strBuilder) {
            this.this$0 = strBuilder;
        }

        @Override // org.apache.commons.lang.text.StrTokenizer
        protected List tokenize(char[] chars, int offset, int count) {
            if (chars == null) {
                return super.tokenize(this.this$0.buffer, 0, this.this$0.size());
            }
            return super.tokenize(chars, offset, count);
        }

        @Override // org.apache.commons.lang.text.StrTokenizer
        public String getContent() {
            String str = super.getContent();
            if (str == null) {
                return this.this$0.toString();
            }
            return str;
        }
    }

    /* loaded from: commons-lang-2.6.jar:org/apache/commons/lang/text/StrBuilder$StrBuilderReader.class */
    class StrBuilderReader extends Reader {
        private int pos;
        private int mark;
        private final StrBuilder this$0;

        StrBuilderReader(StrBuilder strBuilder) {
            this.this$0 = strBuilder;
        }

        @Override // java.io.Reader, java.io.Closeable, java.lang.AutoCloseable
        public void close() {
        }

        @Override // java.io.Reader
        public int read() {
            if (!ready()) {
                return -1;
            }
            StrBuilder strBuilder = this.this$0;
            int i = this.pos;
            this.pos = i + 1;
            return strBuilder.charAt(i);
        }

        @Override // java.io.Reader
        public int read(char[] b, int off, int len) {
            if (off < 0 || len < 0 || off > b.length || off + len > b.length || off + len < 0) {
                throw new IndexOutOfBoundsException();
            }
            if (len == 0) {
                return 0;
            }
            if (this.pos >= this.this$0.size()) {
                return -1;
            }
            if (this.pos + len > this.this$0.size()) {
                len = this.this$0.size() - this.pos;
            }
            this.this$0.getChars(this.pos, this.pos + len, b, off);
            this.pos += len;
            return len;
        }

        @Override // java.io.Reader
        public long skip(long n) {
            if (this.pos + n > this.this$0.size()) {
                n = this.this$0.size() - this.pos;
            }
            if (n < 0) {
                return 0L;
            }
            this.pos = (int) (this.pos + n);
            return n;
        }

        @Override // java.io.Reader
        public boolean ready() {
            return this.pos < this.this$0.size();
        }

        @Override // java.io.Reader
        public boolean markSupported() {
            return true;
        }

        @Override // java.io.Reader
        public void mark(int readAheadLimit) {
            this.mark = this.pos;
        }

        @Override // java.io.Reader
        public void reset() {
            this.pos = this.mark;
        }
    }

    /* loaded from: commons-lang-2.6.jar:org/apache/commons/lang/text/StrBuilder$StrBuilderWriter.class */
    class StrBuilderWriter extends Writer {
        private final StrBuilder this$0;

        StrBuilderWriter(StrBuilder strBuilder) {
            this.this$0 = strBuilder;
        }

        @Override // java.io.Writer, java.io.Closeable, java.lang.AutoCloseable
        public void close() {
        }

        @Override // java.io.Writer, java.io.Flushable
        public void flush() {
        }

        @Override // java.io.Writer
        public void write(int c) {
            this.this$0.append((char) c);
        }

        @Override // java.io.Writer
        public void write(char[] cbuf) {
            this.this$0.append(cbuf);
        }

        @Override // java.io.Writer
        public void write(char[] cbuf, int off, int len) {
            this.this$0.append(cbuf, off, len);
        }

        @Override // java.io.Writer
        public void write(String str) {
            this.this$0.append(str);
        }

        @Override // java.io.Writer
        public void write(String str, int off, int len) {
            this.this$0.append(str, off, len);
        }
    }
}
