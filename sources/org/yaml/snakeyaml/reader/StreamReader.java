package org.yaml.snakeyaml.reader;

import java.io.IOException;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.coobird.thumbnailator.ThumbnailParameter;
import org.yaml.snakeyaml.error.Mark;
import org.yaml.snakeyaml.error.YAMLException;
import org.yaml.snakeyaml.scanner.Constant;

/* loaded from: snakeyaml-1.17.jar:org/yaml/snakeyaml/reader/StreamReader.class */
public class StreamReader {
    public static final Pattern NON_PRINTABLE = Pattern.compile("[^\t\n\r -~\u0085 -\ud7ff\ue000-�]");
    private String name;
    private final Reader stream;
    private int pointer;
    private boolean eof;
    private String buffer;
    private int index;
    private int line;
    private int column;
    private char[] data;

    public StreamReader(String stream) {
        this.pointer = 0;
        this.eof = true;
        this.index = 0;
        this.line = 0;
        this.column = 0;
        this.name = "'string'";
        this.buffer = "";
        checkPrintable(stream);
        this.buffer = stream + ThumbnailParameter.DETERMINE_FORMAT;
        this.stream = null;
        this.eof = true;
        this.data = null;
    }

    public StreamReader(Reader reader) throws IOException {
        this.pointer = 0;
        this.eof = true;
        this.index = 0;
        this.line = 0;
        this.column = 0;
        this.name = "'reader'";
        this.buffer = "";
        this.stream = reader;
        this.eof = false;
        this.data = new char[1024];
        update();
    }

    void checkPrintable(CharSequence data) {
        Matcher em = NON_PRINTABLE.matcher(data);
        if (em.find()) {
            int position = ((this.index + this.buffer.length()) - this.pointer) + em.start();
            throw new ReaderException(this.name, position, em.group().charAt(0), "special characters are not allowed");
        }
    }

    void checkPrintable(char[] chars, int begin, int end) {
        for (int i = begin; i < end; i++) {
            char c = chars[i];
            if (!isPrintable(c)) {
                int position = ((this.index + this.buffer.length()) - this.pointer) + i;
                throw new ReaderException(this.name, position, c, "special characters are not allowed");
            }
        }
    }

    public static boolean isPrintable(char c) {
        return (c >= ' ' && c <= '~') || c == '\n' || c == '\r' || c == '\t' || c == 133 || (c >= 160 && c <= 55295) || (c >= 57344 && c <= 65533);
    }

    public Mark getMark() {
        return new Mark(this.name, this.index, this.line, this.column, this.buffer, this.pointer);
    }

    public void forward() throws IOException {
        forward(1);
    }

    public void forward(int length) throws IOException {
        if (this.pointer + length + 1 >= this.buffer.length()) {
            update();
        }
        for (int i = 0; i < length; i++) {
            char ch2 = this.buffer.charAt(this.pointer);
            this.pointer++;
            this.index++;
            if (Constant.LINEBR.has(ch2) || (ch2 == '\r' && this.buffer.charAt(this.pointer) != '\n')) {
                this.line++;
                this.column = 0;
            } else if (ch2 != 65279) {
                this.column++;
            }
        }
    }

    public char peek() {
        return this.buffer.charAt(this.pointer);
    }

    public char peek(int index) throws IOException {
        if (this.pointer + index + 1 > this.buffer.length()) {
            update();
        }
        return this.buffer.charAt(this.pointer + index);
    }

    public String prefix(int length) throws IOException {
        if (this.pointer + length >= this.buffer.length()) {
            update();
        }
        if (this.pointer + length > this.buffer.length()) {
            return this.buffer.substring(this.pointer);
        }
        return this.buffer.substring(this.pointer, this.pointer + length);
    }

    public String prefixForward(int length) throws IOException {
        String prefix = prefix(length);
        this.pointer += length;
        this.index += length;
        this.column += length;
        return prefix;
    }

    private void update() throws IOException {
        if (!this.eof) {
            this.buffer = this.buffer.substring(this.pointer);
            this.pointer = 0;
            try {
                int converted = this.stream.read(this.data);
                if (converted > 0) {
                    checkPrintable(this.data, 0, converted);
                    this.buffer = new StringBuilder(this.buffer.length() + converted).append(this.buffer).append(this.data, 0, converted).toString();
                } else {
                    this.eof = true;
                    this.buffer += ThumbnailParameter.DETERMINE_FORMAT;
                }
            } catch (IOException ioe) {
                throw new YAMLException(ioe);
            }
        }
    }

    public int getColumn() {
        return this.column;
    }

    public Charset getEncoding() {
        return Charset.forName(((UnicodeReader) this.stream).getEncoding());
    }

    public int getIndex() {
        return this.index;
    }

    public int getLine() {
        return this.line;
    }
}
