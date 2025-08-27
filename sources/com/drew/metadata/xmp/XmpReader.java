package com.drew.metadata.xmp;

import com.adobe.xmp.XMPException;
import com.adobe.xmp.XMPIterator;
import com.adobe.xmp.XMPMeta;
import com.adobe.xmp.XMPMetaFactory;
import com.adobe.xmp.properties.XMPPropertyInfo;
import com.drew.imaging.jpeg.JpegSegmentMetadataReader;
import com.drew.imaging.jpeg.JpegSegmentType;
import com.drew.lang.SequentialByteArrayReader;
import com.drew.lang.SequentialReader;
import com.drew.lang.annotations.NotNull;
import com.drew.lang.annotations.Nullable;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.StringValue;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;

/* loaded from: metadata-extractor-2.10.1.jar:com/drew/metadata/xmp/XmpReader.class */
public class XmpReader implements JpegSegmentMetadataReader {

    @NotNull
    private static final String XMP_JPEG_PREAMBLE = "http://ns.adobe.com/xap/1.0/��";

    @NotNull
    private static final String XMP_EXTENSION_JPEG_PREAMBLE = "http://ns.adobe.com/xmp/extension/��";

    @NotNull
    private static final String SCHEMA_XMP_NOTES = "http://ns.adobe.com/xmp/note/";

    @NotNull
    private static final String ATTRIBUTE_EXTENDED_XMP = "xmpNote:HasExtendedXMP";
    private static final int EXTENDED_XMP_GUID_LENGTH = 32;
    private static final int EXTENDED_XMP_INT_LENGTH = 4;

    @Override // com.drew.imaging.jpeg.JpegSegmentMetadataReader
    @NotNull
    public Iterable<JpegSegmentType> getSegmentTypes() {
        return Collections.singletonList(JpegSegmentType.APP1);
    }

    @Override // com.drew.imaging.jpeg.JpegSegmentMetadataReader
    public void readJpegSegments(@NotNull Iterable<byte[]> segments, @NotNull Metadata metadata, @NotNull JpegSegmentType segmentType) {
        int preambleLength = XMP_JPEG_PREAMBLE.length();
        int extensionPreambleLength = XMP_EXTENSION_JPEG_PREAMBLE.length();
        String extendedXMPGUID = null;
        byte[] extendedXMPBuffer = null;
        for (byte[] segmentBytes : segments) {
            if (segmentBytes.length >= preambleLength && (XMP_JPEG_PREAMBLE.equalsIgnoreCase(new String(segmentBytes, 0, preambleLength)) || "XMP".equalsIgnoreCase(new String(segmentBytes, 0, 3)))) {
                byte[] xmlBytes = new byte[segmentBytes.length - preambleLength];
                System.arraycopy(segmentBytes, preambleLength, xmlBytes, 0, xmlBytes.length);
                extract(xmlBytes, metadata);
                extendedXMPGUID = getExtendedXMPGUID(metadata);
            } else if (extendedXMPGUID != null && segmentBytes.length >= extensionPreambleLength && XMP_EXTENSION_JPEG_PREAMBLE.equalsIgnoreCase(new String(segmentBytes, 0, extensionPreambleLength))) {
                extendedXMPBuffer = processExtendedXMPChunk(metadata, segmentBytes, extendedXMPGUID, extendedXMPBuffer);
            }
        }
        if (extendedXMPBuffer != null) {
            extract(extendedXMPBuffer, metadata);
        }
    }

    public void extract(@NotNull byte[] xmpBytes, @NotNull Metadata metadata) {
        extract(xmpBytes, metadata, (Directory) null);
    }

    public void extract(@NotNull byte[] xmpBytes, @NotNull Metadata metadata, @Nullable Directory parentDirectory) {
        extract(xmpBytes, 0, xmpBytes.length, metadata, parentDirectory);
    }

    /* JADX WARN: Removed duplicated region for block: B:10:0x0028 A[Catch: XMPException -> 0x0048, TryCatch #0 {XMPException -> 0x0048, blocks: (B:7:0x0019, B:9:0x001f, B:11:0x003e, B:10:0x0028), top: B:18:0x0019 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void extract(@com.drew.lang.annotations.NotNull byte[] r7, int r8, int r9, @com.drew.lang.annotations.NotNull com.drew.metadata.Metadata r10, @com.drew.lang.annotations.Nullable com.drew.metadata.Directory r11) {
        /*
            r6 = this;
            com.drew.metadata.xmp.XmpDirectory r0 = new com.drew.metadata.xmp.XmpDirectory
            r1 = r0
            r1.<init>()
            r12 = r0
            r0 = r11
            if (r0 == 0) goto L15
            r0 = r12
            r1 = r11
            r0.setParent(r1)
        L15:
            r0 = r8
            if (r0 != 0) goto L28
            r0 = r9
            r1 = r7
            int r1 = r1.length     // Catch: com.adobe.xmp.XMPException -> L48
            if (r0 != r1) goto L28
            r0 = r7
            com.adobe.xmp.XMPMeta r0 = com.adobe.xmp.XMPMetaFactory.parseFromBuffer(r0)     // Catch: com.adobe.xmp.XMPException -> L48
            r13 = r0
            goto L3e
        L28:
            com.adobe.xmp.impl.ByteBuffer r0 = new com.adobe.xmp.impl.ByteBuffer     // Catch: com.adobe.xmp.XMPException -> L48
            r1 = r0
            r2 = r7
            r3 = r8
            r4 = r9
            r1.<init>(r2, r3, r4)     // Catch: com.adobe.xmp.XMPException -> L48
            r14 = r0
            r0 = r14
            java.io.InputStream r0 = r0.getByteStream()     // Catch: com.adobe.xmp.XMPException -> L48
            com.adobe.xmp.XMPMeta r0 = com.adobe.xmp.XMPMetaFactory.parse(r0)     // Catch: com.adobe.xmp.XMPException -> L48
            r13 = r0
        L3e:
            r0 = r12
            r1 = r13
            r0.setXMPMeta(r1)     // Catch: com.adobe.xmp.XMPException -> L48
            goto L66
        L48:
            r13 = move-exception
            r0 = r12
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r2 = r1
            r2.<init>()
            java.lang.String r2 = "Error processing XMP data: "
            java.lang.StringBuilder r1 = r1.append(r2)
            r2 = r13
            java.lang.String r2 = r2.getMessage()
            java.lang.StringBuilder r1 = r1.append(r2)
            java.lang.String r1 = r1.toString()
            r0.addError(r1)
        L66:
            r0 = r12
            boolean r0 = r0.isEmpty()
            if (r0 != 0) goto L75
            r0 = r10
            r1 = r12
            r0.addDirectory(r1)
        L75:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.drew.metadata.xmp.XmpReader.extract(byte[], int, int, com.drew.metadata.Metadata, com.drew.metadata.Directory):void");
    }

    public void extract(@NotNull String xmpString, @NotNull Metadata metadata) {
        extract(xmpString, metadata, (Directory) null);
    }

    public void extract(@NotNull StringValue xmpString, @NotNull Metadata metadata) {
        extract(xmpString.getBytes(), metadata, (Directory) null);
    }

    public void extract(@NotNull String xmpString, @NotNull Metadata metadata, @Nullable Directory parentDirectory) {
        XmpDirectory directory = new XmpDirectory();
        if (parentDirectory != null) {
            directory.setParent(parentDirectory);
        }
        try {
            XMPMeta xmpMeta = XMPMetaFactory.parseFromString(xmpString);
            directory.setXMPMeta(xmpMeta);
        } catch (XMPException e) {
            directory.addError("Error processing XMP data: " + e.getMessage());
        }
        if (!directory.isEmpty()) {
            metadata.addDirectory(directory);
        }
    }

    @Nullable
    private static String getExtendedXMPGUID(@NotNull Metadata metadata) {
        Collection<XmpDirectory> xmpDirectories = metadata.getDirectoriesOfType(XmpDirectory.class);
        for (XmpDirectory directory : xmpDirectories) {
            XMPMeta xmpMeta = directory.getXMPMeta();
            try {
                XMPIterator itr = xmpMeta.iterator("http://ns.adobe.com/xmp/note/", null, null);
                if (itr != null) {
                    while (itr.hasNext()) {
                        XMPPropertyInfo pi = (XMPPropertyInfo) itr.next();
                        if (ATTRIBUTE_EXTENDED_XMP.equals(pi.getPath())) {
                            return pi.getValue();
                        }
                    }
                }
            } catch (XMPException e) {
            }
        }
        return null;
    }

    @Nullable
    private static byte[] processExtendedXMPChunk(@NotNull Metadata metadata, @NotNull byte[] segmentBytes, @NotNull String extendedXMPGUID, @Nullable byte[] extendedXMPBuffer) {
        int extensionPreambleLength = XMP_EXTENSION_JPEG_PREAMBLE.length();
        int segmentLength = segmentBytes.length;
        int totalOffset = extensionPreambleLength + 32 + 4 + 4;
        if (segmentLength >= totalOffset) {
            try {
                SequentialReader reader = new SequentialByteArrayReader(segmentBytes);
                reader.skip(extensionPreambleLength);
                String segmentGUID = reader.getString(32);
                if (extendedXMPGUID.equals(segmentGUID)) {
                    int fullLength = (int) reader.getUInt32();
                    int chunkOffset = (int) reader.getUInt32();
                    if (extendedXMPBuffer == null) {
                        extendedXMPBuffer = new byte[fullLength];
                    }
                    if (extendedXMPBuffer.length == fullLength) {
                        System.arraycopy(segmentBytes, totalOffset, extendedXMPBuffer, chunkOffset, segmentLength - totalOffset);
                    } else {
                        XmpDirectory directory = new XmpDirectory();
                        directory.addError(String.format("Inconsistent length for the Extended XMP buffer: %d instead of %d", Integer.valueOf(fullLength), Integer.valueOf(extendedXMPBuffer.length)));
                        metadata.addDirectory(directory);
                    }
                }
            } catch (IOException ex) {
                XmpDirectory directory2 = new XmpDirectory();
                directory2.addError(ex.getMessage());
                metadata.addDirectory(directory2);
            }
        }
        return extendedXMPBuffer;
    }
}
