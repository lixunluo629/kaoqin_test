package com.itextpdf.io.source;

import com.alibaba.excel.constant.ExcelXmlConstants;
import com.itextpdf.io.LogMessageConstant;
import com.itextpdf.io.util.MessageFormatUtil;
import java.io.Closeable;
import java.io.IOException;
import java.io.Serializable;
import java.util.Arrays;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/* loaded from: io-7.1.10.jar:com/itextpdf/io/source/PdfTokenizer.class */
public class PdfTokenizer implements Closeable, Serializable {
    private static final long serialVersionUID = -2949864233416670521L;
    public static final boolean[] delims;
    public static final byte[] Obj;
    public static final byte[] R;
    public static final byte[] Xref;
    public static final byte[] Startxref;
    public static final byte[] Stream;
    public static final byte[] Trailer;
    public static final byte[] N;
    public static final byte[] F;
    public static final byte[] Null;
    public static final byte[] True;
    public static final byte[] False;
    protected TokenType type;
    protected int reference;
    protected int generation;
    protected boolean hexString;
    private final RandomAccessFileOrArray file;
    static final /* synthetic */ boolean $assertionsDisabled;
    private boolean closeStream = true;
    protected ByteBuffer outBuf = new ByteBuffer();

    /* loaded from: io-7.1.10.jar:com/itextpdf/io/source/PdfTokenizer$TokenType.class */
    public enum TokenType {
        Number,
        String,
        Name,
        Comment,
        StartArray,
        EndArray,
        StartDic,
        EndDic,
        Ref,
        Obj,
        EndObj,
        Other,
        EndOfFile
    }

    static {
        $assertionsDisabled = !PdfTokenizer.class.desiredAssertionStatus();
        delims = new boolean[]{true, true, false, false, false, false, false, false, false, false, true, true, false, true, true, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, true, false, false, false, false, true, false, false, true, true, false, false, false, false, false, true, false, false, false, false, false, false, false, false, false, false, false, false, true, false, true, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, true, false, true, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false};
        Obj = ByteUtils.getIsoBytes("obj");
        R = ByteUtils.getIsoBytes("R");
        Xref = ByteUtils.getIsoBytes("xref");
        Startxref = ByteUtils.getIsoBytes("startxref");
        Stream = ByteUtils.getIsoBytes("stream");
        Trailer = ByteUtils.getIsoBytes("trailer");
        N = ByteUtils.getIsoBytes("n");
        F = ByteUtils.getIsoBytes(ExcelXmlConstants.CELL_FORMULA_TAG);
        Null = ByteUtils.getIsoBytes("null");
        True = ByteUtils.getIsoBytes("true");
        False = ByteUtils.getIsoBytes("false");
    }

    public PdfTokenizer(RandomAccessFileOrArray file) {
        this.file = file;
    }

    public void seek(long pos) throws IOException {
        this.file.seek(pos);
    }

    public void readFully(byte[] bytes) throws IOException {
        this.file.readFully(bytes);
    }

    public long getPosition() throws IOException {
        return this.file.getPosition();
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        if (this.closeStream) {
            this.file.close();
        }
    }

    public long length() throws IOException {
        return this.file.length();
    }

    public int read() throws IOException {
        return this.file.read();
    }

    public String readString(int size) throws IOException {
        int ch2;
        StringBuilder buf = new StringBuilder();
        while (true) {
            int i = size;
            size--;
            if (i <= 0 || (ch2 = read()) == -1) {
                break;
            }
            buf.append((char) ch2);
        }
        return buf.toString();
    }

    public TokenType getTokenType() {
        return this.type;
    }

    public byte[] getByteContent() {
        return this.outBuf.toByteArray();
    }

    public String getStringValue() {
        return new String(this.outBuf.getInternalBuffer(), 0, this.outBuf.size());
    }

    public byte[] getDecodedStringContent() {
        return decodeStringContent(this.outBuf.getInternalBuffer(), 0, this.outBuf.size() - 1, isHexString());
    }

    public boolean tokenValueEqualsTo(byte[] cmp) {
        int size;
        if (cmp == null || this.outBuf.size() != (size = cmp.length)) {
            return false;
        }
        for (int i = 0; i < size; i++) {
            if (cmp[i] != this.outBuf.getInternalBuffer()[i]) {
                return false;
            }
        }
        return true;
    }

    public int getObjNr() {
        return this.reference;
    }

    public int getGenNr() {
        return this.generation;
    }

    public void backOnePosition(int ch2) {
        if (ch2 != -1) {
            this.file.pushBack((byte) ch2);
        }
    }

    public int getHeaderOffset() throws IOException {
        String str = readString(1024);
        int idx = str.indexOf("%PDF-");
        if (idx < 0) {
            idx = str.indexOf("%FDF-");
            if (idx < 0) {
                throw new com.itextpdf.io.IOException(com.itextpdf.io.IOException.PdfHeaderNotFound, this);
            }
        }
        return idx;
    }

    public String checkPdfHeader() throws IOException {
        this.file.seek(0L);
        String str = readString(1024);
        int idx = str.indexOf("%PDF-");
        if (idx != 0) {
            throw new com.itextpdf.io.IOException(com.itextpdf.io.IOException.PdfHeaderNotFound, this);
        }
        return str.substring(idx + 1, idx + 8);
    }

    public void checkFdfHeader() throws IOException {
        this.file.seek(0L);
        String str = readString(1024);
        int idx = str.indexOf("%FDF-");
        if (idx != 0) {
            throw new com.itextpdf.io.IOException(com.itextpdf.io.IOException.FdfStartxrefNotFound, this);
        }
    }

    public long getStartxref() throws IOException {
        long fileLength = this.file.length();
        long pos = fileLength - 1024;
        if (pos < 1) {
            pos = 1;
        }
        while (pos > 0) {
            this.file.seek(pos);
            String str = readString(1024);
            int idx = str.lastIndexOf("startxref");
            if (idx >= 0) {
                return pos + idx;
            }
            pos = (pos - 1024) + 9;
        }
        throw new com.itextpdf.io.IOException("PDF startxref not found.", this);
    }

    public void nextValidToken() throws IOException {
        int level = 0;
        byte[] n1 = null;
        byte[] n2 = null;
        long ptr = 0;
        while (nextToken()) {
            if (this.type != TokenType.Comment) {
                switch (level) {
                    case 0:
                        if (this.type != TokenType.Number) {
                            return;
                        }
                        ptr = this.file.getPosition();
                        n1 = getByteContent();
                        level++;
                        break;
                    case 1:
                        if (this.type != TokenType.Number) {
                            this.file.seek(ptr);
                            this.type = TokenType.Number;
                            this.outBuf.reset().append(n1);
                            return;
                        } else {
                            n2 = getByteContent();
                            level++;
                            break;
                        }
                    case 2:
                        if (this.type == TokenType.Other) {
                            if (tokenValueEqualsTo(R)) {
                                if (!$assertionsDisabled && n2 == null) {
                                    throw new AssertionError();
                                }
                                this.type = TokenType.Ref;
                                try {
                                    this.reference = Integer.parseInt(new String(n1));
                                    this.generation = Integer.parseInt(new String(n2));
                                    return;
                                } catch (Exception e) {
                                    Logger logger = LoggerFactory.getLogger((Class<?>) PdfTokenizer.class);
                                    logger.error(MessageFormatUtil.format(LogMessageConstant.INVALID_INDIRECT_REFERENCE, new String(n1), new String(n2)));
                                    this.reference = -1;
                                    this.generation = 0;
                                    return;
                                }
                            }
                            if (tokenValueEqualsTo(Obj)) {
                                if (!$assertionsDisabled && n2 == null) {
                                    throw new AssertionError();
                                }
                                this.type = TokenType.Obj;
                                this.reference = Integer.parseInt(new String(n1));
                                this.generation = Integer.parseInt(new String(n2));
                                return;
                            }
                        }
                        this.file.seek(ptr);
                        this.type = TokenType.Number;
                        this.outBuf.reset().append(n1);
                        return;
                }
            }
        }
        if (level == 1) {
            this.type = TokenType.Number;
            this.outBuf.reset().append(n1);
        }
    }

    public boolean nextToken() throws IOException {
        int ch2;
        int ch3;
        int ch4;
        this.outBuf.reset();
        do {
            ch2 = this.file.read();
            if (ch2 == -1) {
                break;
            }
        } while (isWhitespace(ch2));
        if (ch2 == -1) {
            this.type = TokenType.EndOfFile;
            return false;
        }
        switch (ch2) {
            case 37:
                this.type = TokenType.Comment;
                do {
                    ch4 = this.file.read();
                    if (ch4 == -1 || ch4 == 13) {
                        return true;
                    }
                } while (ch4 != 10);
                return true;
            case 40:
                this.type = TokenType.String;
                this.hexString = false;
                int nesting = 0;
                while (true) {
                    ch3 = this.file.read();
                    if (ch3 != -1) {
                        if (ch3 == 40) {
                            nesting++;
                        } else if (ch3 == 41) {
                            nesting--;
                            if (nesting == -1) {
                            }
                        } else if (ch3 == 92) {
                            this.outBuf.append(92);
                            ch3 = this.file.read();
                            if (ch3 < 0) {
                            }
                        } else {
                            continue;
                        }
                        this.outBuf.append(ch3);
                    }
                }
                if (ch3 == -1) {
                    throwError(com.itextpdf.io.IOException.ErrorReadingString, new Object[0]);
                    return true;
                }
                return true;
            case 47:
                this.type = TokenType.Name;
                while (true) {
                    int ch5 = this.file.read();
                    if (!delims[ch5 + 1]) {
                        this.outBuf.append(ch5);
                    } else {
                        backOnePosition(ch5);
                        return true;
                    }
                }
            case 60:
                int v1 = this.file.read();
                if (v1 == 60) {
                    this.type = TokenType.StartDic;
                    return true;
                }
                this.type = TokenType.String;
                this.hexString = true;
                int v2 = 0;
                while (true) {
                    if (isWhitespace(v1)) {
                        v1 = this.file.read();
                    } else if (v1 != 62) {
                        this.outBuf.append(v1);
                        v1 = ByteBuffer.getHex(v1);
                        if (v1 >= 0) {
                            int i = this.file.read();
                            while (true) {
                                v2 = i;
                                if (isWhitespace(v2)) {
                                    i = this.file.read();
                                } else if (v2 != 62) {
                                    this.outBuf.append(v2);
                                    v2 = ByteBuffer.getHex(v2);
                                    if (v2 >= 0) {
                                        v1 = this.file.read();
                                    }
                                }
                            }
                        }
                    }
                }
                if (v1 < 0 || v2 < 0) {
                    throwError(com.itextpdf.io.IOException.ErrorReadingString, new Object[0]);
                    return true;
                }
                return true;
            case 62:
                if (this.file.read() != 62) {
                    throwError(com.itextpdf.io.IOException.GtNotExpected, new Object[0]);
                }
                this.type = TokenType.EndDic;
                return true;
            case 91:
                this.type = TokenType.StartArray;
                return true;
            case 93:
                this.type = TokenType.EndArray;
                return true;
            default:
                if (ch2 == 45 || ch2 == 43 || ch2 == 46 || (ch2 >= 48 && ch2 <= 57)) {
                    this.type = TokenType.Number;
                    boolean isReal = false;
                    int numberOfMinuses = 0;
                    if (ch2 == 45) {
                        do {
                            numberOfMinuses++;
                            ch2 = this.file.read();
                        } while (ch2 == 45);
                        this.outBuf.append(45);
                    } else {
                        this.outBuf.append(ch2);
                        ch2 = this.file.read();
                    }
                    while (ch2 >= 48 && ch2 <= 57) {
                        this.outBuf.append(ch2);
                        ch2 = this.file.read();
                    }
                    if (ch2 == 46) {
                        isReal = true;
                        this.outBuf.append(ch2);
                        ch2 = this.file.read();
                        int numberOfMinusesAfterDot = 0;
                        if (ch2 == 45) {
                            numberOfMinusesAfterDot = 0 + 1;
                            ch2 = this.file.read();
                        }
                        while (ch2 >= 48 && ch2 <= 57) {
                            if (numberOfMinusesAfterDot == 0) {
                                this.outBuf.append(ch2);
                            }
                            ch2 = this.file.read();
                        }
                    }
                    if (numberOfMinuses > 1 && !isReal) {
                        this.outBuf.reset();
                        this.outBuf.append(48);
                    }
                } else {
                    this.type = TokenType.Other;
                    do {
                        this.outBuf.append(ch2);
                        ch2 = this.file.read();
                    } while (!delims[ch2 + 1]);
                }
                if (ch2 != -1) {
                    backOnePosition(ch2);
                    return true;
                }
                return true;
        }
    }

    public long getLongValue() {
        return Long.parseLong(getStringValue());
    }

    public int getIntValue() {
        return Integer.parseInt(getStringValue());
    }

    public boolean isHexString() {
        return this.hexString;
    }

    public boolean isCloseStream() {
        return this.closeStream;
    }

    public void setCloseStream(boolean closeStream) {
        this.closeStream = closeStream;
    }

    public RandomAccessFileOrArray getSafeFile() {
        return this.file.createView();
    }

    protected static byte[] decodeStringContent(byte[] content, int from, int to, boolean hexWriting) {
        ByteBuffer buffer = new ByteBuffer((to - from) + 1);
        if (hexWriting) {
            int i = from;
            while (true) {
                if (i > to) {
                    break;
                }
                int i2 = i;
                int i3 = i + 1;
                int v1 = ByteBuffer.getHex(content[i2]);
                if (i3 > to) {
                    buffer.append(v1 << 4);
                    break;
                }
                i = i3 + 1;
                int v2 = ByteBuffer.getHex(content[i3]);
                buffer.append((v1 << 4) + v2);
            }
        } else {
            int i4 = from;
            while (i4 <= to) {
                int i5 = i4;
                i4++;
                int ch2 = content[i5];
                if (ch2 == 92) {
                    boolean lineBreak = false;
                    i4++;
                    ch2 = content[i4];
                    switch (ch2) {
                        case 10:
                            lineBreak = true;
                            break;
                        case 13:
                            lineBreak = true;
                            if (i4 <= to) {
                                i4++;
                                if (content[i4] != 10) {
                                    i4--;
                                    break;
                                }
                            }
                            break;
                        case 40:
                        case 41:
                        case 92:
                            break;
                        case 98:
                            ch2 = 8;
                            break;
                        case 102:
                            ch2 = 12;
                            break;
                        case 110:
                            ch2 = 10;
                            break;
                        case 114:
                            ch2 = 13;
                            break;
                        case 116:
                            ch2 = 9;
                            break;
                        default:
                            if (ch2 >= 48 && ch2 <= 55) {
                                int octal = ch2 - 48;
                                int i6 = i4 + 1;
                                byte b = content[i4];
                                if (b < 48 || b > 55) {
                                    i4 = i6 - 1;
                                    ch2 = octal;
                                    break;
                                } else {
                                    int octal2 = ((octal << 3) + b) - 48;
                                    i4 = i6 + 1;
                                    byte b2 = content[i6];
                                    if (b2 < 48 || b2 > 55) {
                                        i4--;
                                        ch2 = octal2;
                                        break;
                                    } else {
                                        ch2 = (((octal2 << 3) + b2) - 48) & 255;
                                        break;
                                    }
                                }
                            }
                            break;
                    }
                    if (lineBreak) {
                    }
                } else if (ch2 == 13) {
                    ch2 = 10;
                    if (i4 <= to) {
                        i4++;
                        if (content[i4] != 10) {
                            i4--;
                        }
                    }
                }
                buffer.append(ch2);
            }
        }
        return buffer.toByteArray();
    }

    public static byte[] decodeStringContent(byte[] content, boolean hexWriting) {
        return decodeStringContent(content, 0, content.length - 1, hexWriting);
    }

    public static boolean isWhitespace(int ch2) {
        return isWhitespace(ch2, true);
    }

    protected static boolean isWhitespace(int ch2, boolean isWhitespace) {
        return (isWhitespace && ch2 == 0) || ch2 == 9 || ch2 == 10 || ch2 == 12 || ch2 == 13 || ch2 == 32;
    }

    protected static boolean isDelimiter(int ch2) {
        return ch2 == 40 || ch2 == 41 || ch2 == 60 || ch2 == 62 || ch2 == 91 || ch2 == 93 || ch2 == 47 || ch2 == 37;
    }

    protected static boolean isDelimiterWhitespace(int ch2) {
        return delims[ch2 + 1];
    }

    public void throwError(String error, Object... messageParams) {
        try {
            throw new com.itextpdf.io.IOException(com.itextpdf.io.IOException.ErrorAtFilePointer1, (Throwable) new com.itextpdf.io.IOException(error).setMessageParams(messageParams)).setMessageParams(Long.valueOf(this.file.getPosition()));
        } catch (IOException e) {
            throw new com.itextpdf.io.IOException(com.itextpdf.io.IOException.ErrorAtFilePointer1, (Throwable) new com.itextpdf.io.IOException(error).setMessageParams(messageParams)).setMessageParams(error, "no position");
        }
    }

    public static boolean checkTrailer(ByteBuffer line) {
        if (Trailer.length > line.size()) {
            return false;
        }
        for (int i = 0; i < Trailer.length; i++) {
            if (Trailer[i] != line.get(i)) {
                return false;
            }
        }
        return true;
    }

    public boolean readLineSegment(ByteBuffer buffer) throws IOException {
        return readLineSegment(buffer, true);
    }

    public boolean readLineSegment(ByteBuffer buffer, boolean isNullWhitespace) throws IOException {
        int i;
        int c;
        boolean eol = false;
        do {
            i = read();
            c = i;
        } while (isWhitespace(i, isNullWhitespace));
        boolean prevWasWhitespace = false;
        while (!eol) {
            switch (c) {
                case -1:
                case 10:
                    eol = true;
                    break;
                case 9:
                case 12:
                case 32:
                    if (!prevWasWhitespace) {
                        prevWasWhitespace = true;
                        buffer.append((byte) c);
                        break;
                    }
                    break;
                case 13:
                    eol = true;
                    long cur = getPosition();
                    if (read() != 10) {
                        seek(cur);
                        break;
                    }
                    break;
                default:
                    prevWasWhitespace = false;
                    buffer.append((byte) c);
                    break;
            }
            if (eol || buffer.size() == buffer.capacity()) {
                eol = true;
            } else {
                c = read();
            }
        }
        if (buffer.size() == buffer.capacity()) {
            boolean eol2 = false;
            while (!eol2) {
                int i2 = read();
                c = i2;
                switch (i2) {
                    case -1:
                    case 10:
                        eol2 = true;
                        break;
                    case 13:
                        eol2 = true;
                        long cur2 = getPosition();
                        if (read() == 10) {
                            break;
                        } else {
                            seek(cur2);
                            break;
                        }
                }
            }
        }
        return (c == -1 && buffer.isEmpty()) ? false : true;
    }

    public static int[] checkObjectStart(PdfTokenizer lineTokenizer) {
        try {
            lineTokenizer.seek(0L);
            if (!lineTokenizer.nextToken() || lineTokenizer.getTokenType() != TokenType.Number) {
                return null;
            }
            int num = lineTokenizer.getIntValue();
            if (!lineTokenizer.nextToken() || lineTokenizer.getTokenType() != TokenType.Number) {
                return null;
            }
            int gen = lineTokenizer.getIntValue();
            if (!lineTokenizer.nextToken() || !Arrays.equals(Obj, lineTokenizer.getByteContent())) {
                return null;
            }
            return new int[]{num, gen};
        } catch (Exception e) {
            return null;
        }
    }

    @Deprecated
    /* loaded from: io-7.1.10.jar:com/itextpdf/io/source/PdfTokenizer$ReusableRandomAccessSource.class */
    protected static class ReusableRandomAccessSource implements IRandomAccessSource {
        private ByteBuffer buffer;

        public ReusableRandomAccessSource(ByteBuffer buffer) {
            if (buffer == null) {
                throw new IllegalArgumentException("Passed byte buffer can not be null.");
            }
            this.buffer = buffer;
        }

        @Override // com.itextpdf.io.source.IRandomAccessSource
        public int get(long offset) {
            if (offset >= this.buffer.size()) {
                return -1;
            }
            return 255 & this.buffer.getInternalBuffer()[(int) offset];
        }

        @Override // com.itextpdf.io.source.IRandomAccessSource
        public int get(long offset, byte[] bytes, int off, int len) {
            if (this.buffer == null) {
                throw new IllegalStateException("Already closed");
            }
            if (offset >= this.buffer.size()) {
                return -1;
            }
            if (offset + len > this.buffer.size()) {
                len = (int) (this.buffer.size() - offset);
            }
            System.arraycopy(this.buffer.getInternalBuffer(), (int) offset, bytes, off, len);
            return len;
        }

        @Override // com.itextpdf.io.source.IRandomAccessSource
        public long length() {
            return this.buffer.size();
        }

        @Override // com.itextpdf.io.source.IRandomAccessSource
        public void close() throws IOException {
            this.buffer = null;
        }
    }
}
