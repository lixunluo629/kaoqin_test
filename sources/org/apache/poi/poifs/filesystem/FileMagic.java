package org.apache.poi.poifs.filesystem;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.apache.poi.EmptyFileException;
import org.apache.poi.poifs.common.POIFSConstants;
import org.apache.poi.poifs.storage.HeaderBlockConstants;
import org.apache.poi.util.IOUtils;
import org.apache.poi.util.LittleEndian;
import org.apache.poi.util.LocaleUtil;

/* loaded from: poi-3.17.jar:org/apache/poi/poifs/filesystem/FileMagic.class */
public enum FileMagic {
    OLE2(HeaderBlockConstants._signature),
    OOXML((byte[][]) new byte[]{POIFSConstants.OOXML_FILE_HEADER}),
    XML((byte[][]) new byte[]{POIFSConstants.RAW_XML_FILE_HEADER}),
    BIFF2((byte[][]) new byte[]{new byte[]{9, 0, 4, 0, 0, 0, 112, 0}}),
    BIFF3((byte[][]) new byte[]{new byte[]{9, 2, 6, 0, 0, 0, 112, 0}}),
    BIFF4((byte[][]) new byte[]{new byte[]{9, 4, 6, 0, 0, 0, 112, 0}, new byte[]{9, 4, 6, 0, 0, 0, 0, 1}}),
    MSWRITE((byte[][]) new byte[]{new byte[]{49, -66, 0, 0}, new byte[]{50, -66, 0, 0}}),
    RTF("{\\rtf"),
    PDF("%PDF"),
    UNKNOWN((byte[][]) new byte[]{new byte[0]});

    final byte[][] magic;

    FileMagic(long magic) {
        this.magic = new byte[1][8];
        LittleEndian.putLong(this.magic[0], 0, magic);
    }

    FileMagic(byte[]... magic) {
        this.magic = magic;
    }

    /* JADX WARN: Type inference failed for: r3v1, types: [byte[], byte[][]] */
    FileMagic(String magic) {
        this((byte[][]) new byte[]{magic.getBytes(LocaleUtil.CHARSET_1252)});
    }

    public static FileMagic valueOf(byte[] magic) throws CloneNotSupportedException {
        FileMagic[] arr$ = values();
        for (FileMagic fm : arr$) {
            int i = 0;
            boolean found = true;
            byte[][] arr$2 = fm.magic;
            for (byte[] ma : arr$2) {
                for (byte m : ma) {
                    int i2 = i;
                    i++;
                    byte d = magic[i2];
                    if (d != m && (m != 112 || (d != 16 && d != 32 && d != 64))) {
                        found = false;
                        break;
                    }
                }
                if (found) {
                    return fm;
                }
            }
        }
        return UNKNOWN;
    }

    public static FileMagic valueOf(InputStream inp) throws EmptyFileException, IOException, CloneNotSupportedException {
        if (!inp.markSupported()) {
            throw new IOException("getFileMagic() only operates on streams which support mark(int)");
        }
        byte[] data = IOUtils.peekFirst8Bytes(inp);
        return valueOf(data);
    }

    public static InputStream prepareToCheckMagic(InputStream stream) {
        if (stream.markSupported()) {
            return stream;
        }
        return new BufferedInputStream(stream);
    }
}
