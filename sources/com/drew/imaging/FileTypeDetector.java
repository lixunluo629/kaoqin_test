package com.drew.imaging;

import com.drew.lang.ByteTrie;
import com.drew.lang.annotations.NotNull;
import java.io.BufferedInputStream;
import java.io.IOException;

/* loaded from: metadata-extractor-2.10.1.jar:com/drew/imaging/FileTypeDetector.class */
public class FileTypeDetector {
    private static final ByteTrie<FileType> _root = new ByteTrie<>();

    /* JADX WARN: Type inference failed for: r2v1, types: [byte[], byte[][]] */
    /* JADX WARN: Type inference failed for: r2v11, types: [byte[], byte[][]] */
    /* JADX WARN: Type inference failed for: r2v13, types: [byte[], byte[][]] */
    /* JADX WARN: Type inference failed for: r2v15, types: [byte[], byte[][]] */
    /* JADX WARN: Type inference failed for: r2v17, types: [byte[], byte[][]] */
    /* JADX WARN: Type inference failed for: r2v19, types: [byte[], byte[][]] */
    /* JADX WARN: Type inference failed for: r2v21, types: [byte[], byte[][]] */
    /* JADX WARN: Type inference failed for: r2v23, types: [byte[], byte[][]] */
    /* JADX WARN: Type inference failed for: r2v25, types: [byte[], byte[][]] */
    /* JADX WARN: Type inference failed for: r2v27, types: [byte[], byte[][]] */
    /* JADX WARN: Type inference failed for: r2v29, types: [byte[], byte[][]] */
    /* JADX WARN: Type inference failed for: r2v3, types: [byte[], byte[][]] */
    /* JADX WARN: Type inference failed for: r2v31, types: [byte[], byte[][]] */
    /* JADX WARN: Type inference failed for: r2v33, types: [byte[], byte[][]] */
    /* JADX WARN: Type inference failed for: r2v35, types: [byte[], byte[][]] */
    /* JADX WARN: Type inference failed for: r2v37, types: [byte[], byte[][]] */
    /* JADX WARN: Type inference failed for: r2v39, types: [byte[], byte[][]] */
    /* JADX WARN: Type inference failed for: r2v41, types: [byte[], byte[][]] */
    /* JADX WARN: Type inference failed for: r2v43, types: [byte[], byte[][]] */
    /* JADX WARN: Type inference failed for: r2v45, types: [byte[], byte[][]] */
    /* JADX WARN: Type inference failed for: r2v5, types: [byte[], byte[][]] */
    /* JADX WARN: Type inference failed for: r2v7, types: [byte[], byte[][]] */
    /* JADX WARN: Type inference failed for: r2v9, types: [byte[], byte[][]] */
    static {
        _root.setDefaultValue(FileType.Unknown);
        _root.addPath(FileType.Jpeg, new byte[]{new byte[]{-1, -40}});
        _root.addPath(FileType.Tiff, new byte[]{"II".getBytes(), new byte[]{42, 0}});
        _root.addPath(FileType.Tiff, new byte[]{"MM".getBytes(), new byte[]{0, 42}});
        _root.addPath(FileType.Psd, new byte[]{"8BPS".getBytes()});
        _root.addPath(FileType.Png, new byte[]{new byte[]{-119, 80, 78, 71, 13, 10, 26, 10, 0, 0, 0, 13, 73, 72, 68, 82}});
        _root.addPath(FileType.Bmp, new byte[]{"BM".getBytes()});
        _root.addPath(FileType.Gif, new byte[]{"GIF87a".getBytes()});
        _root.addPath(FileType.Gif, new byte[]{"GIF89a".getBytes()});
        _root.addPath(FileType.Ico, new byte[]{new byte[]{0, 0, 1, 0}});
        _root.addPath(FileType.Pcx, new byte[]{new byte[]{10, 0, 1}});
        _root.addPath(FileType.Pcx, new byte[]{new byte[]{10, 2, 1}});
        _root.addPath(FileType.Pcx, new byte[]{new byte[]{10, 3, 1}});
        _root.addPath(FileType.Pcx, new byte[]{new byte[]{10, 5, 1}});
        _root.addPath(FileType.Riff, new byte[]{"RIFF".getBytes()});
        _root.addPath(FileType.Arw, new byte[]{"II".getBytes(), new byte[]{42, 0, 8, 0}});
        _root.addPath(FileType.Crw, new byte[]{"II".getBytes(), new byte[]{26, 0, 0, 0}, "HEAPCCDR".getBytes()});
        _root.addPath(FileType.Cr2, new byte[]{"II".getBytes(), new byte[]{42, 0, 16, 0, 0, 0, 67, 82}});
        _root.addPath(FileType.Nef, new byte[]{"MM".getBytes(), new byte[]{0, 42, 0, 0, 0, Byte.MIN_VALUE, 0}});
        _root.addPath(FileType.Orf, new byte[]{"IIRO".getBytes(), new byte[]{8, 0}});
        _root.addPath(FileType.Orf, new byte[]{"MMOR".getBytes(), new byte[]{0, 0}});
        _root.addPath(FileType.Orf, new byte[]{"IIRS".getBytes(), new byte[]{8, 0}});
        _root.addPath(FileType.Raf, new byte[]{"FUJIFILMCCD-RAW".getBytes()});
        _root.addPath(FileType.Rw2, new byte[]{"II".getBytes(), new byte[]{85, 0}});
    }

    private FileTypeDetector() throws Exception {
        throw new Exception("Not intended for instantiation");
    }

    @NotNull
    public static FileType detectFileType(@NotNull BufferedInputStream inputStream) throws IOException {
        if (!inputStream.markSupported()) {
            throw new IOException("Stream must support mark/reset");
        }
        int maxByteCount = _root.getMaxDepth();
        inputStream.mark(maxByteCount);
        byte[] bytes = new byte[maxByteCount];
        int bytesRead = inputStream.read(bytes);
        if (bytesRead == -1) {
            throw new IOException("Stream ended before file's magic number could be determined.");
        }
        inputStream.reset();
        return _root.find(bytes);
    }
}
