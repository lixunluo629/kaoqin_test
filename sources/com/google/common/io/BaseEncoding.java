package com.google.common.io;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Ascii;
import com.google.common.base.CharMatcher;
import com.google.common.base.Preconditions;
import com.google.common.io.GwtWorkarounds;
import com.google.common.math.IntMath;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.math.RoundingMode;
import java.util.Arrays;
import javax.annotation.CheckReturnValue;
import javax.annotation.Nullable;

@Beta
@GwtCompatible(emulated = true)
/* loaded from: guava-18.0.jar:com/google/common/io/BaseEncoding.class */
public abstract class BaseEncoding {
    private static final BaseEncoding BASE64 = new StandardBaseEncoding("base64()", "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/", '=');
    private static final BaseEncoding BASE64_URL = new StandardBaseEncoding("base64Url()", "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-_", '=');
    private static final BaseEncoding BASE32 = new StandardBaseEncoding("base32()", "ABCDEFGHIJKLMNOPQRSTUVWXYZ234567", '=');
    private static final BaseEncoding BASE32_HEX = new StandardBaseEncoding("base32Hex()", "0123456789ABCDEFGHIJKLMNOPQRSTUV", '=');
    private static final BaseEncoding BASE16 = new StandardBaseEncoding("base16()", "0123456789ABCDEF", null);

    abstract int maxEncodedSize(int i);

    abstract GwtWorkarounds.ByteOutput encodingStream(GwtWorkarounds.CharOutput charOutput);

    abstract int maxDecodedSize(int i);

    abstract GwtWorkarounds.ByteInput decodingStream(GwtWorkarounds.CharInput charInput);

    abstract CharMatcher padding();

    @CheckReturnValue
    public abstract BaseEncoding omitPadding();

    @CheckReturnValue
    public abstract BaseEncoding withPadChar(char c);

    @CheckReturnValue
    public abstract BaseEncoding withSeparator(String str, int i);

    @CheckReturnValue
    public abstract BaseEncoding upperCase();

    @CheckReturnValue
    public abstract BaseEncoding lowerCase();

    BaseEncoding() {
    }

    /* loaded from: guava-18.0.jar:com/google/common/io/BaseEncoding$DecodingException.class */
    public static final class DecodingException extends IOException {
        DecodingException(String message) {
            super(message);
        }

        DecodingException(Throwable cause) {
            super(cause);
        }
    }

    public String encode(byte[] bytes) {
        return encode((byte[]) Preconditions.checkNotNull(bytes), 0, bytes.length);
    }

    public final String encode(byte[] bytes, int off, int len) {
        Preconditions.checkNotNull(bytes);
        Preconditions.checkPositionIndexes(off, off + len, bytes.length);
        GwtWorkarounds.CharOutput result = GwtWorkarounds.stringBuilderOutput(maxEncodedSize(len));
        GwtWorkarounds.ByteOutput byteOutput = encodingStream(result);
        for (int i = 0; i < len; i++) {
            try {
                byteOutput.write(bytes[off + i]);
            } catch (IOException e) {
                throw new AssertionError("impossible");
            }
        }
        byteOutput.close();
        return result.toString();
    }

    @GwtIncompatible("Writer,OutputStream")
    public final OutputStream encodingStream(Writer writer) {
        return GwtWorkarounds.asOutputStream(encodingStream(GwtWorkarounds.asCharOutput(writer)));
    }

    @GwtIncompatible("ByteSink,CharSink")
    public final ByteSink encodingSink(final CharSink encodedSink) {
        Preconditions.checkNotNull(encodedSink);
        return new ByteSink() { // from class: com.google.common.io.BaseEncoding.1
            @Override // com.google.common.io.ByteSink
            public OutputStream openStream() throws IOException {
                return BaseEncoding.this.encodingStream(encodedSink.openStream());
            }
        };
    }

    private static byte[] extract(byte[] result, int length) {
        if (length == result.length) {
            return result;
        }
        byte[] trunc = new byte[length];
        System.arraycopy(result, 0, trunc, 0, length);
        return trunc;
    }

    public final byte[] decode(CharSequence chars) {
        try {
            return decodeChecked(chars);
        } catch (DecodingException badInput) {
            throw new IllegalArgumentException(badInput);
        }
    }

    final byte[] decodeChecked(CharSequence chars) throws DecodingException {
        CharSequence chars2 = padding().trimTrailingFrom(chars);
        GwtWorkarounds.ByteInput decodedInput = decodingStream(GwtWorkarounds.asCharInput(chars2));
        byte[] tmp = new byte[maxDecodedSize(chars2.length())];
        int index = 0;
        try {
            for (int i = decodedInput.read(); i != -1; i = decodedInput.read()) {
                int i2 = index;
                index++;
                tmp[i2] = (byte) i;
            }
            return extract(tmp, index);
        } catch (DecodingException badInput) {
            throw badInput;
        } catch (IOException impossible) {
            throw new AssertionError(impossible);
        }
    }

    @GwtIncompatible("Reader,InputStream")
    public final InputStream decodingStream(Reader reader) {
        return GwtWorkarounds.asInputStream(decodingStream(GwtWorkarounds.asCharInput(reader)));
    }

    @GwtIncompatible("ByteSource,CharSource")
    public final ByteSource decodingSource(final CharSource encodedSource) {
        Preconditions.checkNotNull(encodedSource);
        return new ByteSource() { // from class: com.google.common.io.BaseEncoding.2
            @Override // com.google.common.io.ByteSource
            public InputStream openStream() throws IOException {
                return BaseEncoding.this.decodingStream(encodedSource.openStream());
            }
        };
    }

    public static BaseEncoding base64() {
        return BASE64;
    }

    public static BaseEncoding base64Url() {
        return BASE64_URL;
    }

    public static BaseEncoding base32() {
        return BASE32;
    }

    public static BaseEncoding base32Hex() {
        return BASE32_HEX;
    }

    public static BaseEncoding base16() {
        return BASE16;
    }

    /* loaded from: guava-18.0.jar:com/google/common/io/BaseEncoding$Alphabet.class */
    private static final class Alphabet extends CharMatcher {
        private final String name;
        private final char[] chars;
        final int mask;
        final int bitsPerChar;
        final int charsPerChunk;
        final int bytesPerChunk;
        private final byte[] decodabet;
        private final boolean[] validPadding;

        Alphabet(String name, char[] chars) {
            this.name = (String) Preconditions.checkNotNull(name);
            this.chars = (char[]) Preconditions.checkNotNull(chars);
            try {
                this.bitsPerChar = IntMath.log2(chars.length, RoundingMode.UNNECESSARY);
                int gcd = Math.min(8, Integer.lowestOneBit(this.bitsPerChar));
                this.charsPerChunk = 8 / gcd;
                this.bytesPerChunk = this.bitsPerChar / gcd;
                this.mask = chars.length - 1;
                byte[] decodabet = new byte[128];
                Arrays.fill(decodabet, (byte) -1);
                for (int i = 0; i < chars.length; i++) {
                    char c = chars[i];
                    Preconditions.checkArgument(CharMatcher.ASCII.matches(c), "Non-ASCII character: %s", Character.valueOf(c));
                    Preconditions.checkArgument(decodabet[c] == -1, "Duplicate character: %s", Character.valueOf(c));
                    decodabet[c] = (byte) i;
                }
                this.decodabet = decodabet;
                boolean[] validPadding = new boolean[this.charsPerChunk];
                for (int i2 = 0; i2 < this.bytesPerChunk; i2++) {
                    validPadding[IntMath.divide(i2 * 8, this.bitsPerChar, RoundingMode.CEILING)] = true;
                }
                this.validPadding = validPadding;
            } catch (ArithmeticException e) {
                throw new IllegalArgumentException(new StringBuilder(35).append("Illegal alphabet length ").append(chars.length).toString(), e);
            }
        }

        char encode(int bits) {
            return this.chars[bits];
        }

        boolean isValidPaddingStartPosition(int index) {
            return this.validPadding[index % this.charsPerChunk];
        }

        int decode(char ch2) throws IOException {
            if (ch2 > 127 || this.decodabet[ch2] == -1) {
                throw new DecodingException(new StringBuilder(25).append("Unrecognized character: ").append(ch2).toString());
            }
            return this.decodabet[ch2];
        }

        private boolean hasLowerCase() {
            char[] arr$ = this.chars;
            for (char c : arr$) {
                if (Ascii.isLowerCase(c)) {
                    return true;
                }
            }
            return false;
        }

        private boolean hasUpperCase() {
            char[] arr$ = this.chars;
            for (char c : arr$) {
                if (Ascii.isUpperCase(c)) {
                    return true;
                }
            }
            return false;
        }

        Alphabet upperCase() {
            if (!hasLowerCase()) {
                return this;
            }
            Preconditions.checkState(!hasUpperCase(), "Cannot call upperCase() on a mixed-case alphabet");
            char[] upperCased = new char[this.chars.length];
            for (int i = 0; i < this.chars.length; i++) {
                upperCased[i] = Ascii.toUpperCase(this.chars[i]);
            }
            return new Alphabet(String.valueOf(this.name).concat(".upperCase()"), upperCased);
        }

        Alphabet lowerCase() {
            if (!hasUpperCase()) {
                return this;
            }
            Preconditions.checkState(!hasLowerCase(), "Cannot call lowerCase() on a mixed-case alphabet");
            char[] lowerCased = new char[this.chars.length];
            for (int i = 0; i < this.chars.length; i++) {
                lowerCased[i] = Ascii.toLowerCase(this.chars[i]);
            }
            return new Alphabet(String.valueOf(this.name).concat(".lowerCase()"), lowerCased);
        }

        @Override // com.google.common.base.CharMatcher
        public boolean matches(char c) {
            return CharMatcher.ASCII.matches(c) && this.decodabet[c] != -1;
        }

        @Override // com.google.common.base.CharMatcher
        public String toString() {
            return this.name;
        }
    }

    /* loaded from: guava-18.0.jar:com/google/common/io/BaseEncoding$StandardBaseEncoding.class */
    static final class StandardBaseEncoding extends BaseEncoding {
        private final Alphabet alphabet;

        @Nullable
        private final Character paddingChar;
        private transient BaseEncoding upperCase;
        private transient BaseEncoding lowerCase;

        StandardBaseEncoding(String name, String alphabetChars, @Nullable Character paddingChar) {
            this(new Alphabet(name, alphabetChars.toCharArray()), paddingChar);
        }

        StandardBaseEncoding(Alphabet alphabet, @Nullable Character paddingChar) {
            this.alphabet = (Alphabet) Preconditions.checkNotNull(alphabet);
            Preconditions.checkArgument(paddingChar == null || !alphabet.matches(paddingChar.charValue()), "Padding character %s was already in alphabet", paddingChar);
            this.paddingChar = paddingChar;
        }

        @Override // com.google.common.io.BaseEncoding
        CharMatcher padding() {
            return this.paddingChar == null ? CharMatcher.NONE : CharMatcher.is(this.paddingChar.charValue());
        }

        @Override // com.google.common.io.BaseEncoding
        int maxEncodedSize(int bytes) {
            return this.alphabet.charsPerChunk * IntMath.divide(bytes, this.alphabet.bytesPerChunk, RoundingMode.CEILING);
        }

        @Override // com.google.common.io.BaseEncoding
        GwtWorkarounds.ByteOutput encodingStream(final GwtWorkarounds.CharOutput out) {
            Preconditions.checkNotNull(out);
            return new GwtWorkarounds.ByteOutput() { // from class: com.google.common.io.BaseEncoding.StandardBaseEncoding.1
                int bitBuffer = 0;
                int bitBufferLength = 0;
                int writtenChars = 0;

                @Override // com.google.common.io.GwtWorkarounds.ByteOutput
                public void write(byte b) throws IOException {
                    this.bitBuffer <<= 8;
                    this.bitBuffer |= b & 255;
                    this.bitBufferLength += 8;
                    while (this.bitBufferLength >= StandardBaseEncoding.this.alphabet.bitsPerChar) {
                        int charIndex = (this.bitBuffer >> (this.bitBufferLength - StandardBaseEncoding.this.alphabet.bitsPerChar)) & StandardBaseEncoding.this.alphabet.mask;
                        out.write(StandardBaseEncoding.this.alphabet.encode(charIndex));
                        this.writtenChars++;
                        this.bitBufferLength -= StandardBaseEncoding.this.alphabet.bitsPerChar;
                    }
                }

                @Override // com.google.common.io.GwtWorkarounds.ByteOutput
                public void flush() throws IOException {
                    out.flush();
                }

                @Override // com.google.common.io.GwtWorkarounds.ByteOutput
                public void close() throws IOException {
                    if (this.bitBufferLength > 0) {
                        int charIndex = (this.bitBuffer << (StandardBaseEncoding.this.alphabet.bitsPerChar - this.bitBufferLength)) & StandardBaseEncoding.this.alphabet.mask;
                        out.write(StandardBaseEncoding.this.alphabet.encode(charIndex));
                        this.writtenChars++;
                        if (StandardBaseEncoding.this.paddingChar != null) {
                            while (this.writtenChars % StandardBaseEncoding.this.alphabet.charsPerChunk != 0) {
                                out.write(StandardBaseEncoding.this.paddingChar.charValue());
                                this.writtenChars++;
                            }
                        }
                    }
                    out.close();
                }
            };
        }

        @Override // com.google.common.io.BaseEncoding
        int maxDecodedSize(int chars) {
            return (int) (((this.alphabet.bitsPerChar * chars) + 7) / 8);
        }

        @Override // com.google.common.io.BaseEncoding
        GwtWorkarounds.ByteInput decodingStream(final GwtWorkarounds.CharInput reader) {
            Preconditions.checkNotNull(reader);
            return new GwtWorkarounds.ByteInput() { // from class: com.google.common.io.BaseEncoding.StandardBaseEncoding.2
                int bitBuffer = 0;
                int bitBufferLength = 0;
                int readChars = 0;
                boolean hitPadding = false;
                final CharMatcher paddingMatcher;

                {
                    this.paddingMatcher = StandardBaseEncoding.this.padding();
                }

                /* JADX WARN: Code restructure failed: missing block: B:21:0x00a6, code lost:
                
                    throw new com.google.common.io.BaseEncoding.DecodingException(new java.lang.StringBuilder(41).append("Padding cannot start at index ").append(r6.readChars).toString());
                 */
                @Override // com.google.common.io.GwtWorkarounds.ByteInput
                /*
                    Code decompiled incorrectly, please refer to instructions dump.
                    To view partially-correct code enable 'Show inconsistent code' option in preferences
                */
                public int read() throws java.io.IOException {
                    /*
                        Method dump skipped, instructions count: 324
                        To view this dump change 'Code comments level' option to 'DEBUG'
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.google.common.io.BaseEncoding.StandardBaseEncoding.AnonymousClass2.read():int");
                }

                @Override // com.google.common.io.GwtWorkarounds.ByteInput
                public void close() throws IOException {
                    reader.close();
                }
            };
        }

        @Override // com.google.common.io.BaseEncoding
        public BaseEncoding omitPadding() {
            return this.paddingChar == null ? this : new StandardBaseEncoding(this.alphabet, null);
        }

        @Override // com.google.common.io.BaseEncoding
        public BaseEncoding withPadChar(char padChar) {
            if (8 % this.alphabet.bitsPerChar == 0 || (this.paddingChar != null && this.paddingChar.charValue() == padChar)) {
                return this;
            }
            return new StandardBaseEncoding(this.alphabet, Character.valueOf(padChar));
        }

        @Override // com.google.common.io.BaseEncoding
        public BaseEncoding withSeparator(String separator, int afterEveryChars) {
            Preconditions.checkNotNull(separator);
            Preconditions.checkArgument(padding().or(this.alphabet).matchesNoneOf(separator), "Separator cannot contain alphabet or padding characters");
            return new SeparatedBaseEncoding(this, separator, afterEveryChars);
        }

        @Override // com.google.common.io.BaseEncoding
        public BaseEncoding upperCase() {
            BaseEncoding result = this.upperCase;
            if (result == null) {
                Alphabet upper = this.alphabet.upperCase();
                BaseEncoding standardBaseEncoding = upper == this.alphabet ? this : new StandardBaseEncoding(upper, this.paddingChar);
                this.upperCase = standardBaseEncoding;
                result = standardBaseEncoding;
            }
            return result;
        }

        @Override // com.google.common.io.BaseEncoding
        public BaseEncoding lowerCase() {
            BaseEncoding result = this.lowerCase;
            if (result == null) {
                Alphabet lower = this.alphabet.lowerCase();
                BaseEncoding standardBaseEncoding = lower == this.alphabet ? this : new StandardBaseEncoding(lower, this.paddingChar);
                this.lowerCase = standardBaseEncoding;
                result = standardBaseEncoding;
            }
            return result;
        }

        public String toString() {
            StringBuilder builder = new StringBuilder("BaseEncoding.");
            builder.append(this.alphabet.toString());
            if (8 % this.alphabet.bitsPerChar != 0) {
                if (this.paddingChar == null) {
                    builder.append(".omitPadding()");
                } else {
                    builder.append(".withPadChar(").append(this.paddingChar).append(')');
                }
            }
            return builder.toString();
        }
    }

    static GwtWorkarounds.CharInput ignoringInput(final GwtWorkarounds.CharInput delegate, final CharMatcher toIgnore) {
        Preconditions.checkNotNull(delegate);
        Preconditions.checkNotNull(toIgnore);
        return new GwtWorkarounds.CharInput() { // from class: com.google.common.io.BaseEncoding.3
            @Override // com.google.common.io.GwtWorkarounds.CharInput
            public int read() throws IOException {
                int readChar;
                do {
                    readChar = delegate.read();
                    if (readChar == -1) {
                        break;
                    }
                } while (toIgnore.matches((char) readChar));
                return readChar;
            }

            @Override // com.google.common.io.GwtWorkarounds.CharInput
            public void close() throws IOException {
                delegate.close();
            }
        };
    }

    static GwtWorkarounds.CharOutput separatingOutput(final GwtWorkarounds.CharOutput delegate, final String separator, final int afterEveryChars) {
        Preconditions.checkNotNull(delegate);
        Preconditions.checkNotNull(separator);
        Preconditions.checkArgument(afterEveryChars > 0);
        return new GwtWorkarounds.CharOutput() { // from class: com.google.common.io.BaseEncoding.4
            int charsUntilSeparator;

            {
                this.charsUntilSeparator = afterEveryChars;
            }

            @Override // com.google.common.io.GwtWorkarounds.CharOutput
            public void write(char c) throws IOException {
                if (this.charsUntilSeparator == 0) {
                    for (int i = 0; i < separator.length(); i++) {
                        delegate.write(separator.charAt(i));
                    }
                    this.charsUntilSeparator = afterEveryChars;
                }
                delegate.write(c);
                this.charsUntilSeparator--;
            }

            @Override // com.google.common.io.GwtWorkarounds.CharOutput
            public void flush() throws IOException {
                delegate.flush();
            }

            @Override // com.google.common.io.GwtWorkarounds.CharOutput
            public void close() throws IOException {
                delegate.close();
            }
        };
    }

    /* loaded from: guava-18.0.jar:com/google/common/io/BaseEncoding$SeparatedBaseEncoding.class */
    static final class SeparatedBaseEncoding extends BaseEncoding {
        private final BaseEncoding delegate;
        private final String separator;
        private final int afterEveryChars;
        private final CharMatcher separatorChars;

        SeparatedBaseEncoding(BaseEncoding delegate, String separator, int afterEveryChars) {
            this.delegate = (BaseEncoding) Preconditions.checkNotNull(delegate);
            this.separator = (String) Preconditions.checkNotNull(separator);
            this.afterEveryChars = afterEveryChars;
            Preconditions.checkArgument(afterEveryChars > 0, "Cannot add a separator after every %s chars", Integer.valueOf(afterEveryChars));
            this.separatorChars = CharMatcher.anyOf(separator).precomputed();
        }

        @Override // com.google.common.io.BaseEncoding
        CharMatcher padding() {
            return this.delegate.padding();
        }

        @Override // com.google.common.io.BaseEncoding
        int maxEncodedSize(int bytes) {
            int unseparatedSize = this.delegate.maxEncodedSize(bytes);
            return unseparatedSize + (this.separator.length() * IntMath.divide(Math.max(0, unseparatedSize - 1), this.afterEveryChars, RoundingMode.FLOOR));
        }

        @Override // com.google.common.io.BaseEncoding
        GwtWorkarounds.ByteOutput encodingStream(GwtWorkarounds.CharOutput output) {
            return this.delegate.encodingStream(separatingOutput(output, this.separator, this.afterEveryChars));
        }

        @Override // com.google.common.io.BaseEncoding
        int maxDecodedSize(int chars) {
            return this.delegate.maxDecodedSize(chars);
        }

        @Override // com.google.common.io.BaseEncoding
        GwtWorkarounds.ByteInput decodingStream(GwtWorkarounds.CharInput input) {
            return this.delegate.decodingStream(ignoringInput(input, this.separatorChars));
        }

        @Override // com.google.common.io.BaseEncoding
        public BaseEncoding omitPadding() {
            return this.delegate.omitPadding().withSeparator(this.separator, this.afterEveryChars);
        }

        @Override // com.google.common.io.BaseEncoding
        public BaseEncoding withPadChar(char padChar) {
            return this.delegate.withPadChar(padChar).withSeparator(this.separator, this.afterEveryChars);
        }

        @Override // com.google.common.io.BaseEncoding
        public BaseEncoding withSeparator(String separator, int afterEveryChars) {
            throw new UnsupportedOperationException("Already have a separator");
        }

        @Override // com.google.common.io.BaseEncoding
        public BaseEncoding upperCase() {
            return this.delegate.upperCase().withSeparator(this.separator, this.afterEveryChars);
        }

        @Override // com.google.common.io.BaseEncoding
        public BaseEncoding lowerCase() {
            return this.delegate.lowerCase().withSeparator(this.separator, this.afterEveryChars);
        }

        public String toString() {
            String strValueOf = String.valueOf(String.valueOf(this.delegate.toString()));
            String strValueOf2 = String.valueOf(String.valueOf(this.separator));
            return new StringBuilder(31 + strValueOf.length() + strValueOf2.length()).append(strValueOf).append(".withSeparator(\"").append(strValueOf2).append("\", ").append(this.afterEveryChars).append(")").toString();
        }
    }
}
