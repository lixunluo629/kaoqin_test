package io.jsonwebtoken.impl.compression;

import io.jsonwebtoken.CompressionCodec;
import io.jsonwebtoken.lang.Objects;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/* loaded from: jjwt-0.6.0.jar:io/jsonwebtoken/impl/compression/GzipCompressionCodec.class */
public class GzipCompressionCodec extends AbstractCompressionCodec implements CompressionCodec {
    private static final String GZIP = "GZIP";

    @Override // io.jsonwebtoken.CompressionCodec
    public String getAlgorithmName() {
        return GZIP;
    }

    @Override // io.jsonwebtoken.impl.compression.AbstractCompressionCodec
    protected byte[] doDecompress(byte[] compressed) throws IOException {
        byte[] buffer = new byte[512];
        ByteArrayOutputStream outputStream = null;
        GZIPInputStream gzipInputStream = null;
        ByteArrayInputStream inputStream = null;
        try {
            inputStream = new ByteArrayInputStream(compressed);
            gzipInputStream = new GZIPInputStream(inputStream);
            outputStream = new ByteArrayOutputStream();
            while (true) {
                int read = gzipInputStream.read(buffer);
                if (read != -1) {
                    outputStream.write(buffer, 0, read);
                } else {
                    byte[] byteArray = outputStream.toByteArray();
                    Objects.nullSafeClose(inputStream, gzipInputStream, outputStream);
                    return byteArray;
                }
            }
        } catch (Throwable th) {
            Objects.nullSafeClose(inputStream, gzipInputStream, outputStream);
            throw th;
        }
    }

    @Override // io.jsonwebtoken.impl.compression.AbstractCompressionCodec
    protected byte[] doCompress(byte[] payload) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        GZIPOutputStream compressorOutputStream = new GZIPOutputStream((OutputStream) outputStream, true);
        try {
            compressorOutputStream.write(payload, 0, payload.length);
            compressorOutputStream.finish();
            byte[] byteArray = outputStream.toByteArray();
            Objects.nullSafeClose(compressorOutputStream, outputStream);
            return byteArray;
        } catch (Throwable th) {
            Objects.nullSafeClose(compressorOutputStream, outputStream);
            throw th;
        }
    }
}
