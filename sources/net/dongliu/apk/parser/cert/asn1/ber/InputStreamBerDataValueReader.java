package net.dongliu.apk.parser.cert.asn1.ber;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

/* loaded from: apk-parser-2.5.3.jar:net/dongliu/apk/parser/cert/asn1/ber/InputStreamBerDataValueReader.class */
public class InputStreamBerDataValueReader implements BerDataValueReader {
    private final InputStream mIn;

    public InputStreamBerDataValueReader(InputStream in) {
        if (in == null) {
            throw new NullPointerException("in == null");
        }
        this.mIn = in;
    }

    @Override // net.dongliu.apk.parser.cert.asn1.ber.BerDataValueReader
    public BerDataValue readDataValue() throws BerDataValueFormatException {
        return readDataValue(this.mIn);
    }

    private static BerDataValue readDataValue(InputStream input) throws BerDataValueFormatException {
        int contentsOffsetInDataValue;
        int iSkipPrimitiveIndefiniteLengthContents;
        int contentsLength;
        RecordingInputStream in = new RecordingInputStream(input);
        try {
            int firstIdentifierByte = in.read();
            if (firstIdentifierByte == -1) {
                return null;
            }
            int tagNumber = readTagNumber(in, firstIdentifierByte);
            int firstLengthByte = in.read();
            if (firstLengthByte == -1) {
                throw new BerDataValueFormatException("Missing length");
            }
            boolean constructed = BerEncoding.isConstructed((byte) firstIdentifierByte);
            if ((firstLengthByte & 128) == 0) {
                contentsLength = readShortFormLength(firstLengthByte);
                contentsOffsetInDataValue = in.getReadByteCount();
                skipDefiniteLengthContents(in, contentsLength);
            } else if ((firstLengthByte & 255) != 128) {
                contentsLength = readLongFormLength(in, firstLengthByte);
                contentsOffsetInDataValue = in.getReadByteCount();
                skipDefiniteLengthContents(in, contentsLength);
            } else {
                contentsOffsetInDataValue = in.getReadByteCount();
                if (constructed) {
                    iSkipPrimitiveIndefiniteLengthContents = skipConstructedIndefiniteLengthContents(in);
                } else {
                    iSkipPrimitiveIndefiniteLengthContents = skipPrimitiveIndefiniteLengthContents(in);
                }
                contentsLength = iSkipPrimitiveIndefiniteLengthContents;
            }
            byte[] encoded = in.getReadBytes();
            ByteBuffer encodedContents = ByteBuffer.wrap(encoded, contentsOffsetInDataValue, contentsLength);
            return new BerDataValue(ByteBuffer.wrap(encoded), encodedContents, BerEncoding.getTagClass((byte) firstIdentifierByte), constructed, tagNumber);
        } catch (IOException e) {
            throw new BerDataValueFormatException("Failed to read data value", e);
        }
    }

    private static int readTagNumber(InputStream in, int firstIdentifierByte) throws BerDataValueFormatException, IOException {
        int tagNumber = BerEncoding.getTagNumber((byte) firstIdentifierByte);
        if (tagNumber == 31) {
            return readHighTagNumber(in);
        }
        return tagNumber;
    }

    private static int readHighTagNumber(InputStream in) throws BerDataValueFormatException, IOException {
        int b;
        int result = 0;
        do {
            b = in.read();
            if (b == -1) {
                throw new BerDataValueFormatException("Truncated tag number");
            }
            if (result > 16777215) {
                throw new BerDataValueFormatException("Tag number too large");
            }
            result = (result << 7) | (b & 127);
        } while ((b & 128) != 0);
        return result;
    }

    private static int readShortFormLength(int firstLengthByte) {
        return firstLengthByte & 127;
    }

    private static int readLongFormLength(InputStream in, int firstLengthByte) throws BerDataValueFormatException, IOException {
        int byteCount = firstLengthByte & 127;
        if (byteCount > 4) {
            throw new BerDataValueFormatException("Length too large: " + byteCount + " bytes");
        }
        int result = 0;
        for (int i = 0; i < byteCount; i++) {
            int b = in.read();
            if (b == -1) {
                throw new BerDataValueFormatException("Truncated length");
            }
            if (result > 8388607) {
                throw new BerDataValueFormatException("Length too large");
            }
            result = (result << 8) | (b & 255);
        }
        return result;
    }

    private static void skipDefiniteLengthContents(InputStream in, int len) throws BerDataValueFormatException, IOException {
        long j = 0;
        while (true) {
            long bytesRead = j;
            if (len > 0) {
                int skipped = (int) in.skip(len);
                if (skipped <= 0) {
                    throw new BerDataValueFormatException("Truncated definite-length contents: " + bytesRead + " bytes read, " + len + " missing");
                }
                len -= skipped;
                j = bytesRead + skipped;
            } else {
                return;
            }
        }
    }

    private static int skipPrimitiveIndefiniteLengthContents(InputStream in) throws BerDataValueFormatException, IOException {
        boolean prevZeroByte = false;
        int bytesRead = 0;
        while (true) {
            int b = in.read();
            if (b == -1) {
                throw new BerDataValueFormatException("Truncated indefinite-length contents: " + bytesRead + " bytes read");
            }
            bytesRead++;
            if (bytesRead < 0) {
                throw new BerDataValueFormatException("Indefinite-length contents too long");
            }
            if (b == 0) {
                if (prevZeroByte) {
                    return bytesRead - 2;
                }
                prevZeroByte = true;
            } else {
                prevZeroByte = false;
            }
        }
    }

    private static int skipConstructedIndefiniteLengthContents(RecordingInputStream in) throws BerDataValueFormatException {
        int readByteCountBefore = in.getReadByteCount();
        while (true) {
            BerDataValue dataValue = readDataValue(in);
            if (dataValue == null) {
                throw new BerDataValueFormatException("Truncated indefinite-length contents: " + (in.getReadByteCount() - readByteCountBefore) + " bytes read");
            }
            if (in.getReadByteCount() <= 0) {
                throw new BerDataValueFormatException("Indefinite-length contents too long");
            }
            ByteBuffer encoded = dataValue.getEncoded();
            if (encoded.remaining() == 2 && encoded.get(0) == 0 && encoded.get(1) == 0) {
                return (in.getReadByteCount() - readByteCountBefore) - 2;
            }
        }
    }

    /* loaded from: apk-parser-2.5.3.jar:net/dongliu/apk/parser/cert/asn1/ber/InputStreamBerDataValueReader$RecordingInputStream.class */
    private static class RecordingInputStream extends InputStream {
        private final InputStream mIn;
        private final ByteArrayOutputStream mBuf;

        private RecordingInputStream(InputStream in) {
            this.mIn = in;
            this.mBuf = new ByteArrayOutputStream();
        }

        public byte[] getReadBytes() {
            return this.mBuf.toByteArray();
        }

        public int getReadByteCount() {
            return this.mBuf.size();
        }

        @Override // java.io.InputStream
        public int read() throws IOException {
            int b = this.mIn.read();
            if (b != -1) {
                this.mBuf.write(b);
            }
            return b;
        }

        @Override // java.io.InputStream
        public int read(byte[] b) throws IOException {
            int len = this.mIn.read(b);
            if (len > 0) {
                this.mBuf.write(b, 0, len);
            }
            return len;
        }

        @Override // java.io.InputStream
        public int read(byte[] b, int off, int len) throws IOException {
            int len2 = this.mIn.read(b, off, len);
            if (len2 > 0) {
                this.mBuf.write(b, off, len2);
            }
            return len2;
        }

        @Override // java.io.InputStream
        public long skip(long n) throws IOException {
            if (n <= 0) {
                return this.mIn.skip(n);
            }
            byte[] buf = new byte[4096];
            int len = this.mIn.read(buf, 0, (int) Math.min(buf.length, n));
            if (len > 0) {
                this.mBuf.write(buf, 0, len);
            }
            if (len < 0) {
                return 0L;
            }
            return len;
        }

        @Override // java.io.InputStream
        public int available() throws IOException {
            return super.available();
        }

        @Override // java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
        public void close() throws IOException {
            super.close();
        }

        @Override // java.io.InputStream
        public synchronized void mark(int readlimit) {
        }

        @Override // java.io.InputStream
        public synchronized void reset() throws IOException {
            throw new IOException("mark/reset not supported");
        }

        @Override // java.io.InputStream
        public boolean markSupported() {
            return false;
        }
    }
}
