package com.drew.metadata.gif;

import com.drew.lang.ByteArrayReader;
import com.drew.lang.Charsets;
import com.drew.lang.SequentialReader;
import com.drew.lang.annotations.NotNull;
import com.drew.lang.annotations.Nullable;
import com.drew.metadata.Directory;
import com.drew.metadata.ErrorDirectory;
import com.drew.metadata.Metadata;
import com.drew.metadata.StringValue;
import com.drew.metadata.gif.GifControlDirectory;
import com.drew.metadata.icc.IccReader;
import com.drew.metadata.xmp.XmpReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.apache.poi.poifs.common.POIFSConstants;

/* loaded from: metadata-extractor-2.10.1.jar:com/drew/metadata/gif/GifReader.class */
public class GifReader {
    private static final String GIF_87A_VERSION_IDENTIFIER = "87a";
    private static final String GIF_89A_VERSION_IDENTIFIER = "89a";

    public void extract(@NotNull SequentialReader reader, @NotNull Metadata metadata) {
        reader.setMotorolaByteOrder(false);
        try {
            GifHeaderDirectory header = readGifHeader(reader);
            metadata.addDirectory(header);
            if (header.hasErrors()) {
                return;
            }
            try {
                Integer globalColorTableSize = header.getInteger(4);
                if (globalColorTableSize != null) {
                    reader.skip(3 * globalColorTableSize.intValue());
                }
                while (true) {
                    try {
                        byte marker = reader.getInt8();
                        switch (marker) {
                            case 33:
                                readGifExtensionBlock(reader, metadata);
                                break;
                            case 44:
                                metadata.addDirectory(readImageBlock(reader));
                                skipBlocks(reader);
                                break;
                            case 59:
                                return;
                            default:
                                return;
                        }
                    } catch (IOException e) {
                        return;
                    }
                }
            } catch (IOException e2) {
                metadata.addDirectory(new ErrorDirectory("IOException processing GIF data"));
            }
        } catch (IOException e3) {
            metadata.addDirectory(new ErrorDirectory("IOException processing GIF data"));
        }
    }

    private static GifHeaderDirectory readGifHeader(@NotNull SequentialReader reader) throws IOException {
        GifHeaderDirectory headerDirectory = new GifHeaderDirectory();
        String signature = reader.getString(3);
        if (!signature.equals("GIF")) {
            headerDirectory.addError("Invalid GIF file signature");
            return headerDirectory;
        }
        String version = reader.getString(3);
        if (!version.equals(GIF_87A_VERSION_IDENTIFIER) && !version.equals(GIF_89A_VERSION_IDENTIFIER)) {
            headerDirectory.addError("Unexpected GIF version");
            return headerDirectory;
        }
        headerDirectory.setString(1, version);
        headerDirectory.setInt(2, reader.getUInt16());
        headerDirectory.setInt(3, reader.getUInt16());
        short flags = reader.getUInt8();
        int colorTableSize = 1 << ((flags & 7) + 1);
        int bitsPerPixel = ((flags & 112) >> 4) + 1;
        boolean hasGlobalColorTable = (flags & 15) != 0;
        headerDirectory.setInt(4, colorTableSize);
        if (version.equals(GIF_89A_VERSION_IDENTIFIER)) {
            boolean isColorTableSorted = (flags & 8) != 0;
            headerDirectory.setBoolean(5, isColorTableSorted);
        }
        headerDirectory.setInt(6, bitsPerPixel);
        headerDirectory.setBoolean(7, hasGlobalColorTable);
        headerDirectory.setInt(8, reader.getUInt8());
        int aspectRatioByte = reader.getUInt8();
        if (aspectRatioByte != 0) {
            float pixelAspectRatio = (float) ((aspectRatioByte + 15.0d) / 64.0d);
            headerDirectory.setFloat(9, pixelAspectRatio);
        }
        return headerDirectory;
    }

    private static void readGifExtensionBlock(SequentialReader reader, Metadata metadata) throws IOException {
        byte extensionLabel = reader.getInt8();
        short blockSizeBytes = reader.getUInt8();
        long blockStartPos = reader.getPosition();
        switch (extensionLabel) {
            case -7:
                metadata.addDirectory(readControlBlock(reader, blockSizeBytes));
                break;
            case -6:
            case POIFSConstants.LARGEST_REGULAR_SECTOR_NUMBER /* -5 */:
            case POIFSConstants.DIFAT_SECTOR_BLOCK /* -4 */:
            case -3:
            case 0:
            default:
                metadata.addDirectory(new ErrorDirectory(String.format("Unsupported GIF extension block with type 0x%02X.", Byte.valueOf(extensionLabel))));
                break;
            case -2:
                metadata.addDirectory(readCommentBlock(reader, blockSizeBytes));
                break;
            case -1:
                readApplicationExtensionBlock(reader, blockSizeBytes, metadata);
                break;
            case 1:
                Directory plainTextBlock = readPlainTextBlock(reader, blockSizeBytes);
                if (plainTextBlock != null) {
                    metadata.addDirectory(plainTextBlock);
                    break;
                }
                break;
        }
        long skipCount = (blockStartPos + blockSizeBytes) - reader.getPosition();
        if (skipCount > 0) {
            reader.skip(skipCount);
        }
    }

    @Nullable
    private static Directory readPlainTextBlock(SequentialReader reader, int blockSizeBytes) throws IOException {
        if (blockSizeBytes != 12) {
            return new ErrorDirectory(String.format("Invalid GIF plain text block size. Expected 12, got %d.", Integer.valueOf(blockSizeBytes)));
        }
        reader.skip(12L);
        skipBlocks(reader);
        return null;
    }

    private static GifCommentDirectory readCommentBlock(SequentialReader reader, int blockSizeBytes) throws IOException {
        byte[] buffer = gatherBytes(reader, blockSizeBytes);
        return new GifCommentDirectory(new StringValue(buffer, Charsets.ASCII));
    }

    private static void readApplicationExtensionBlock(SequentialReader reader, int blockSizeBytes, Metadata metadata) throws IOException {
        if (blockSizeBytes != 11) {
            metadata.addDirectory(new ErrorDirectory(String.format("Invalid GIF application extension block size. Expected 11, got %d.", Integer.valueOf(blockSizeBytes))));
            return;
        }
        String extensionType = reader.getString(blockSizeBytes, Charsets.UTF_8);
        if (extensionType.equals("XMP DataXMP")) {
            byte[] xmpBytes = gatherBytes(reader);
            new XmpReader().extract(xmpBytes, 0, xmpBytes.length - 257, metadata, null);
            return;
        }
        if (extensionType.equals("ICCRGBG1012")) {
            byte[] iccBytes = gatherBytes(reader, reader.getByte());
            if (iccBytes.length != 0) {
                new IccReader().extract(new ByteArrayReader(iccBytes), metadata);
                return;
            }
            return;
        }
        if (extensionType.equals("NETSCAPE2.0")) {
            reader.skip(2L);
            int iterationCount = reader.getUInt16();
            reader.skip(1L);
            GifAnimationDirectory animationDirectory = new GifAnimationDirectory();
            animationDirectory.setInt(1, iterationCount);
            metadata.addDirectory(animationDirectory);
            return;
        }
        skipBlocks(reader);
    }

    private static GifControlDirectory readControlBlock(SequentialReader reader, int blockSizeBytes) throws IOException {
        if (blockSizeBytes < 4) {
        }
        GifControlDirectory directory = new GifControlDirectory();
        short packedFields = reader.getUInt8();
        directory.setObject(2, GifControlDirectory.DisposalMethod.typeOf((packedFields >> 2) & 7));
        directory.setBoolean(3, ((packedFields & 2) >> 1) == 1);
        directory.setBoolean(4, (packedFields & 1) == 1);
        directory.setInt(1, reader.getUInt16());
        directory.setInt(5, reader.getUInt8());
        reader.skip(1L);
        return directory;
    }

    private static GifImageDirectory readImageBlock(SequentialReader reader) throws IOException {
        GifImageDirectory imageDirectory = new GifImageDirectory();
        imageDirectory.setInt(1, reader.getUInt16());
        imageDirectory.setInt(2, reader.getUInt16());
        imageDirectory.setInt(3, reader.getUInt16());
        imageDirectory.setInt(4, reader.getUInt16());
        byte flags = reader.getByte();
        boolean hasColorTable = (flags & 7) != 0;
        boolean isInterlaced = (flags & 64) != 0;
        boolean isColorTableSorted = (flags & 32) != 0;
        imageDirectory.setBoolean(5, hasColorTable);
        imageDirectory.setBoolean(6, isInterlaced);
        if (hasColorTable) {
            imageDirectory.setBoolean(7, isColorTableSorted);
            int bitsPerPixel = (flags & 7) + 1;
            imageDirectory.setInt(8, bitsPerPixel);
            reader.skip(3 * (2 << (flags & 7)));
        }
        reader.getByte();
        return imageDirectory;
    }

    private static byte[] gatherBytes(SequentialReader reader) throws IOException {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        byte[] buffer = new byte[257];
        while (true) {
            byte b = reader.getByte();
            if (b == 0) {
                return bytes.toByteArray();
            }
            int bInt = b & 255;
            buffer[0] = b;
            reader.getBytes(buffer, 1, bInt);
            bytes.write(buffer, 0, bInt + 1);
        }
    }

    private static byte[] gatherBytes(SequentialReader reader, int firstLength) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        int i = firstLength;
        while (true) {
            int length = i;
            if (length > 0) {
                buffer.write(reader.getBytes(length), 0, length);
                i = reader.getByte();
            } else {
                return buffer.toByteArray();
            }
        }
    }

    private static void skipBlocks(SequentialReader reader) throws IOException {
        while (true) {
            short length = reader.getUInt8();
            if (length == 0) {
                return;
            } else {
                reader.skip(length);
            }
        }
    }
}
