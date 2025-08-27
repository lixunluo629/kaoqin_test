package com.drew.imaging.png;

import com.drew.lang.ByteConvert;
import com.drew.lang.Charsets;
import com.drew.lang.DateUtil;
import com.drew.lang.KeyValuePair;
import com.drew.lang.RandomAccessStreamReader;
import com.drew.lang.SequentialByteArrayReader;
import com.drew.lang.SequentialReader;
import com.drew.lang.StreamReader;
import com.drew.lang.StreamUtil;
import com.drew.lang.annotations.NotNull;
import com.drew.metadata.Metadata;
import com.drew.metadata.StringValue;
import com.drew.metadata.file.FileMetadataReader;
import com.drew.metadata.icc.IccReader;
import com.drew.metadata.png.PngChromaticitiesDirectory;
import com.drew.metadata.png.PngDirectory;
import com.drew.metadata.xmp.XmpReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.zip.InflaterInputStream;
import java.util.zip.ZipException;

/* loaded from: metadata-extractor-2.10.1.jar:com/drew/imaging/png/PngMetadataReader.class */
public class PngMetadataReader {
    private static Set<PngChunkType> _desiredChunkTypes;
    private static Charset _latin1Encoding = Charsets.ISO_8859_1;

    static {
        Set<PngChunkType> desiredChunkTypes = new HashSet<>();
        desiredChunkTypes.add(PngChunkType.IHDR);
        desiredChunkTypes.add(PngChunkType.PLTE);
        desiredChunkTypes.add(PngChunkType.tRNS);
        desiredChunkTypes.add(PngChunkType.cHRM);
        desiredChunkTypes.add(PngChunkType.sRGB);
        desiredChunkTypes.add(PngChunkType.gAMA);
        desiredChunkTypes.add(PngChunkType.iCCP);
        desiredChunkTypes.add(PngChunkType.bKGD);
        desiredChunkTypes.add(PngChunkType.tEXt);
        desiredChunkTypes.add(PngChunkType.zTXt);
        desiredChunkTypes.add(PngChunkType.iTXt);
        desiredChunkTypes.add(PngChunkType.tIME);
        desiredChunkTypes.add(PngChunkType.pHYs);
        desiredChunkTypes.add(PngChunkType.sBIT);
        _desiredChunkTypes = Collections.unmodifiableSet(desiredChunkTypes);
    }

    @NotNull
    public static Metadata readMetadata(@NotNull File file) throws IOException, PngProcessingException {
        InputStream inputStream = new FileInputStream(file);
        try {
            Metadata metadata = readMetadata(inputStream);
            inputStream.close();
            new FileMetadataReader().read(file, metadata);
            return metadata;
        } catch (Throwable th) {
            inputStream.close();
            throw th;
        }
    }

    @NotNull
    public static Metadata readMetadata(@NotNull InputStream inputStream) throws IOException, PngProcessingException {
        Iterable<PngChunk> chunks = new PngChunkReader().extract(new StreamReader(inputStream), _desiredChunkTypes);
        Metadata metadata = new Metadata();
        for (PngChunk chunk : chunks) {
            try {
                processChunk(metadata, chunk);
            } catch (Exception e) {
                e.printStackTrace(System.err);
            }
        }
        return metadata;
    }

    private static void processChunk(@NotNull Metadata metadata, @NotNull PngChunk chunk) throws IOException, PngProcessingException {
        PngChunkType chunkType = chunk.getType();
        byte[] bytes = chunk.getBytes();
        if (chunkType.equals(PngChunkType.IHDR)) {
            PngHeader header = new PngHeader(bytes);
            PngDirectory directory = new PngDirectory(PngChunkType.IHDR);
            directory.setInt(1, header.getImageWidth());
            directory.setInt(2, header.getImageHeight());
            directory.setInt(3, header.getBitsPerSample());
            directory.setInt(4, header.getColorType().getNumericValue());
            directory.setInt(5, header.getCompressionType() & 255);
            directory.setInt(6, header.getFilterMethod());
            directory.setInt(7, header.getInterlaceMethod());
            metadata.addDirectory(directory);
            return;
        }
        if (chunkType.equals(PngChunkType.PLTE)) {
            PngDirectory directory2 = new PngDirectory(PngChunkType.PLTE);
            directory2.setInt(8, bytes.length / 3);
            metadata.addDirectory(directory2);
            return;
        }
        if (chunkType.equals(PngChunkType.tRNS)) {
            PngDirectory directory3 = new PngDirectory(PngChunkType.tRNS);
            directory3.setInt(9, 1);
            metadata.addDirectory(directory3);
            return;
        }
        if (chunkType.equals(PngChunkType.sRGB)) {
            byte b = bytes[0];
            PngDirectory directory4 = new PngDirectory(PngChunkType.sRGB);
            directory4.setInt(10, b);
            metadata.addDirectory(directory4);
            return;
        }
        if (chunkType.equals(PngChunkType.cHRM)) {
            PngChromaticities chromaticities = new PngChromaticities(bytes);
            PngChromaticitiesDirectory directory5 = new PngChromaticitiesDirectory();
            directory5.setInt(1, chromaticities.getWhitePointX());
            directory5.setInt(2, chromaticities.getWhitePointY());
            directory5.setInt(3, chromaticities.getRedX());
            directory5.setInt(4, chromaticities.getRedY());
            directory5.setInt(5, chromaticities.getGreenX());
            directory5.setInt(6, chromaticities.getGreenY());
            directory5.setInt(7, chromaticities.getBlueX());
            directory5.setInt(8, chromaticities.getBlueY());
            metadata.addDirectory(directory5);
            return;
        }
        if (chunkType.equals(PngChunkType.gAMA)) {
            int gammaInt = ByteConvert.toInt32BigEndian(bytes);
            new SequentialByteArrayReader(bytes).getInt32();
            PngDirectory directory6 = new PngDirectory(PngChunkType.gAMA);
            directory6.setDouble(11, gammaInt / 100000.0d);
            metadata.addDirectory(directory6);
            return;
        }
        if (chunkType.equals(PngChunkType.iCCP)) {
            SequentialReader reader = new SequentialByteArrayReader(bytes);
            byte[] profileNameBytes = reader.getNullTerminatedBytes(80);
            PngDirectory directory7 = new PngDirectory(PngChunkType.iCCP);
            directory7.setStringValue(12, new StringValue(profileNameBytes, _latin1Encoding));
            byte compressionMethod = reader.getInt8();
            if (compressionMethod == 0) {
                byte[] compressedProfile = reader.getBytes(bytes.length - ((profileNameBytes.length + 1) + 1));
                try {
                    InflaterInputStream inflateStream = new InflaterInputStream(new ByteArrayInputStream(compressedProfile));
                    new IccReader().extract(new RandomAccessStreamReader(inflateStream), metadata, directory7);
                    inflateStream.close();
                } catch (ZipException zex) {
                    directory7.addError(String.format("Exception decompressing PNG iCCP chunk : %s", zex.getMessage()));
                    metadata.addDirectory(directory7);
                }
            } else {
                directory7.addError("Invalid compression method value");
            }
            metadata.addDirectory(directory7);
            return;
        }
        if (chunkType.equals(PngChunkType.bKGD)) {
            PngDirectory directory8 = new PngDirectory(PngChunkType.bKGD);
            directory8.setByteArray(15, bytes);
            metadata.addDirectory(directory8);
            return;
        }
        if (chunkType.equals(PngChunkType.tEXt)) {
            SequentialReader reader2 = new SequentialByteArrayReader(bytes);
            StringValue keywordsv = reader2.getNullTerminatedStringValue(80, _latin1Encoding);
            String keyword = keywordsv.toString();
            StringValue value = reader2.getNullTerminatedStringValue(bytes.length - (keywordsv.getBytes().length + 1), _latin1Encoding);
            List<KeyValuePair> textPairs = new ArrayList<>();
            textPairs.add(new KeyValuePair(keyword, value));
            PngDirectory directory9 = new PngDirectory(PngChunkType.tEXt);
            directory9.setObject(13, textPairs);
            metadata.addDirectory(directory9);
            return;
        }
        if (chunkType.equals(PngChunkType.zTXt)) {
            SequentialReader reader3 = new SequentialByteArrayReader(bytes);
            StringValue keywordsv2 = reader3.getNullTerminatedStringValue(80, _latin1Encoding);
            String keyword2 = keywordsv2.toString();
            byte compressionMethod2 = reader3.getInt8();
            int bytesLeft = bytes.length - ((keywordsv2.getBytes().length + 1) + 1);
            byte[] textBytes = null;
            if (compressionMethod2 == 0) {
                try {
                    textBytes = StreamUtil.readAllBytes(new InflaterInputStream(new ByteArrayInputStream(bytes, bytes.length - bytesLeft, bytesLeft)));
                } catch (ZipException zex2) {
                    textBytes = null;
                    PngDirectory directory10 = new PngDirectory(PngChunkType.zTXt);
                    directory10.addError(String.format("Exception decompressing PNG zTXt chunk with keyword \"%s\": %s", keyword2, zex2.getMessage()));
                    metadata.addDirectory(directory10);
                }
            } else {
                PngDirectory directory11 = new PngDirectory(PngChunkType.zTXt);
                directory11.addError("Invalid compression method value");
                metadata.addDirectory(directory11);
            }
            if (textBytes != null) {
                if (keyword2.equals("XML:com.adobe.xmp")) {
                    new XmpReader().extract(textBytes, metadata);
                    return;
                }
                List<KeyValuePair> textPairs2 = new ArrayList<>();
                textPairs2.add(new KeyValuePair(keyword2, new StringValue(textBytes, _latin1Encoding)));
                PngDirectory directory12 = new PngDirectory(PngChunkType.zTXt);
                directory12.setObject(13, textPairs2);
                metadata.addDirectory(directory12);
                return;
            }
            return;
        }
        if (!chunkType.equals(PngChunkType.iTXt)) {
            if (chunkType.equals(PngChunkType.tIME)) {
                SequentialByteArrayReader reader4 = new SequentialByteArrayReader(bytes);
                int year = reader4.getUInt16();
                int month = reader4.getUInt8();
                int day = reader4.getUInt8();
                int hour = reader4.getUInt8();
                int minute = reader4.getUInt8();
                int second = reader4.getUInt8();
                PngDirectory directory13 = new PngDirectory(PngChunkType.tIME);
                if (DateUtil.isValidDate(year, month - 1, day) && DateUtil.isValidTime(hour, minute, second)) {
                    String dateString = String.format("%04d:%02d:%02d %02d:%02d:%02d", Integer.valueOf(year), Integer.valueOf(month), Integer.valueOf(day), Integer.valueOf(hour), Integer.valueOf(minute), Integer.valueOf(second));
                    directory13.setString(14, dateString);
                } else {
                    directory13.addError(String.format("PNG tIME data describes an invalid date/time: year=%d month=%d day=%d hour=%d minute=%d second=%d", Integer.valueOf(year), Integer.valueOf(month), Integer.valueOf(day), Integer.valueOf(hour), Integer.valueOf(minute), Integer.valueOf(second)));
                }
                metadata.addDirectory(directory13);
                return;
            }
            if (!chunkType.equals(PngChunkType.pHYs)) {
                if (chunkType.equals(PngChunkType.sBIT)) {
                    PngDirectory directory14 = new PngDirectory(PngChunkType.sBIT);
                    directory14.setByteArray(19, bytes);
                    metadata.addDirectory(directory14);
                    return;
                }
                return;
            }
            SequentialByteArrayReader reader5 = new SequentialByteArrayReader(bytes);
            int pixelsPerUnitX = reader5.getInt32();
            int pixelsPerUnitY = reader5.getInt32();
            byte unitSpecifier = reader5.getInt8();
            PngDirectory directory15 = new PngDirectory(PngChunkType.pHYs);
            directory15.setInt(16, pixelsPerUnitX);
            directory15.setInt(17, pixelsPerUnitY);
            directory15.setInt(18, unitSpecifier);
            metadata.addDirectory(directory15);
            return;
        }
        SequentialReader reader6 = new SequentialByteArrayReader(bytes);
        StringValue keywordsv3 = reader6.getNullTerminatedStringValue(80, _latin1Encoding);
        String keyword3 = keywordsv3.toString();
        byte compressionFlag = reader6.getInt8();
        byte compressionMethod3 = reader6.getInt8();
        byte[] languageTagBytes = reader6.getNullTerminatedBytes(bytes.length);
        byte[] translatedKeywordBytes = reader6.getNullTerminatedBytes(bytes.length);
        int bytesLeft2 = bytes.length - (((((((keywordsv3.getBytes().length + 1) + 1) + 1) + languageTagBytes.length) + 1) + translatedKeywordBytes.length) + 1);
        byte[] textBytes2 = null;
        if (compressionFlag == 0) {
            textBytes2 = reader6.getNullTerminatedBytes(bytesLeft2);
        } else if (compressionFlag == 1) {
            if (compressionMethod3 == 0) {
                try {
                    textBytes2 = StreamUtil.readAllBytes(new InflaterInputStream(new ByteArrayInputStream(bytes, bytes.length - bytesLeft2, bytesLeft2)));
                } catch (ZipException zex3) {
                    textBytes2 = null;
                    PngDirectory directory16 = new PngDirectory(PngChunkType.iTXt);
                    directory16.addError(String.format("Exception decompressing PNG iTXt chunk with keyword \"%s\": %s", keyword3, zex3.getMessage()));
                    metadata.addDirectory(directory16);
                }
            } else {
                PngDirectory directory17 = new PngDirectory(PngChunkType.iTXt);
                directory17.addError("Invalid compression method value");
                metadata.addDirectory(directory17);
            }
        } else {
            PngDirectory directory18 = new PngDirectory(PngChunkType.iTXt);
            directory18.addError("Invalid compression flag value");
            metadata.addDirectory(directory18);
        }
        if (textBytes2 != null) {
            if (keyword3.equals("XML:com.adobe.xmp")) {
                new XmpReader().extract(textBytes2, metadata);
                return;
            }
            List<KeyValuePair> textPairs3 = new ArrayList<>();
            textPairs3.add(new KeyValuePair(keyword3, new StringValue(textBytes2, _latin1Encoding)));
            PngDirectory directory19 = new PngDirectory(PngChunkType.iTXt);
            directory19.setObject(13, textPairs3);
            metadata.addDirectory(directory19);
        }
    }
}
