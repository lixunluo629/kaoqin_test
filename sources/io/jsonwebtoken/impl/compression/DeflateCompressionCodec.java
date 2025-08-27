package io.jsonwebtoken.impl.compression;

import io.jsonwebtoken.lang.Objects;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterOutputStream;

/* loaded from: jjwt-0.6.0.jar:io/jsonwebtoken/impl/compression/DeflateCompressionCodec.class */
public class DeflateCompressionCodec extends AbstractCompressionCodec {
    private static final String DEFLATE = "DEF";

    @Override // io.jsonwebtoken.CompressionCodec
    public String getAlgorithmName() {
        return DEFLATE;
    }

    @Override // io.jsonwebtoken.impl.compression.AbstractCompressionCodec
    public byte[] doCompress(byte[] payload) throws IOException {
        Deflater deflater = new Deflater(9);
        ByteArrayOutputStream outputStream = null;
        DeflaterOutputStream deflaterOutputStream = null;
        try {
            outputStream = new ByteArrayOutputStream();
            deflaterOutputStream = new DeflaterOutputStream((OutputStream) outputStream, deflater, true);
            deflaterOutputStream.write(payload, 0, payload.length);
            deflaterOutputStream.flush();
            byte[] byteArray = outputStream.toByteArray();
            Objects.nullSafeClose(outputStream, deflaterOutputStream);
            return byteArray;
        } catch (Throwable th) {
            Objects.nullSafeClose(outputStream, deflaterOutputStream);
            throw th;
        }
    }

    @Override // io.jsonwebtoken.impl.compression.AbstractCompressionCodec
    public byte[] doDecompress(byte[] compressed) throws IOException {
        InflaterOutputStream inflaterOutputStream = null;
        ByteArrayOutputStream decompressedOutputStream = null;
        try {
            decompressedOutputStream = new ByteArrayOutputStream();
            inflaterOutputStream = new InflaterOutputStream(decompressedOutputStream);
            inflaterOutputStream.write(compressed);
            inflaterOutputStream.flush();
            byte[] byteArray = decompressedOutputStream.toByteArray();
            Objects.nullSafeClose(decompressedOutputStream, inflaterOutputStream);
            return byteArray;
        } catch (Throwable th) {
            Objects.nullSafeClose(decompressedOutputStream, inflaterOutputStream);
            throw th;
        }
    }
}
