package org.apache.commons.compress.archivers.sevenz;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.commons.compress.MemoryLimitException;
import org.tukaani.xz.FinishableWrapperOutputStream;
import org.tukaani.xz.LZMA2InputStream;
import org.tukaani.xz.LZMA2Options;

/* loaded from: commons-compress-1.19.jar:org/apache/commons/compress/archivers/sevenz/LZMA2Decoder.class */
class LZMA2Decoder extends CoderBase {
    LZMA2Decoder() {
        super(LZMA2Options.class, Number.class);
    }

    @Override // org.apache.commons.compress.archivers.sevenz.CoderBase
    InputStream decode(String archiveName, InputStream in, long uncompressedLength, Coder coder, byte[] password, int maxMemoryLimitInKb) throws IOException {
        try {
            int dictionarySize = getDictionarySize(coder);
            int memoryUsageInKb = LZMA2InputStream.getMemoryUsage(dictionarySize);
            if (memoryUsageInKb > maxMemoryLimitInKb) {
                throw new MemoryLimitException(memoryUsageInKb, maxMemoryLimitInKb);
            }
            return new LZMA2InputStream(in, dictionarySize);
        } catch (IllegalArgumentException ex) {
            throw new IOException(ex.getMessage());
        }
    }

    @Override // org.apache.commons.compress.archivers.sevenz.CoderBase
    OutputStream encode(OutputStream out, Object opts) throws IOException {
        LZMA2Options options = getOptions(opts);
        return options.getOutputStream(new FinishableWrapperOutputStream(out));
    }

    @Override // org.apache.commons.compress.archivers.sevenz.CoderBase
    byte[] getOptionsAsProperties(Object opts) {
        int dictSize = getDictSize(opts);
        int lead = Integer.numberOfLeadingZeros(dictSize);
        int secondBit = (dictSize >>> (30 - lead)) - 2;
        return new byte[]{(byte) (((19 - lead) * 2) + secondBit)};
    }

    @Override // org.apache.commons.compress.archivers.sevenz.CoderBase
    Object getOptionsFromCoder(Coder coder, InputStream in) throws IOException {
        return Integer.valueOf(getDictionarySize(coder));
    }

    private int getDictSize(Object opts) {
        if (opts instanceof LZMA2Options) {
            return ((LZMA2Options) opts).getDictSize();
        }
        return numberOptionOrDefault(opts);
    }

    private int getDictionarySize(Coder coder) throws IOException {
        if (coder.properties == null) {
            throw new IOException("Missing LZMA2 properties");
        }
        if (coder.properties.length < 1) {
            throw new IOException("LZMA2 properties too short");
        }
        int dictionarySizeBits = 255 & coder.properties[0];
        if ((dictionarySizeBits & (-64)) != 0) {
            throw new IOException("Unsupported LZMA2 property bits");
        }
        if (dictionarySizeBits > 40) {
            throw new IOException("Dictionary larger than 4GiB maximum size");
        }
        if (dictionarySizeBits == 40) {
            return -1;
        }
        return (2 | (dictionarySizeBits & 1)) << ((dictionarySizeBits / 2) + 11);
    }

    private LZMA2Options getOptions(Object opts) throws IOException {
        if (opts instanceof LZMA2Options) {
            return (LZMA2Options) opts;
        }
        LZMA2Options options = new LZMA2Options();
        options.setDictSize(numberOptionOrDefault(opts));
        return options;
    }

    private int numberOptionOrDefault(Object opts) {
        return numberOptionOrDefault(opts, 8388608);
    }
}
