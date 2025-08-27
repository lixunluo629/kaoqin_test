package io.jsonwebtoken.impl.compression;

import io.jsonwebtoken.CompressionCodec;
import io.jsonwebtoken.CompressionException;
import io.jsonwebtoken.lang.Assert;
import java.io.IOException;

/* loaded from: jjwt-0.6.0.jar:io/jsonwebtoken/impl/compression/AbstractCompressionCodec.class */
public abstract class AbstractCompressionCodec implements CompressionCodec {
    protected abstract byte[] doCompress(byte[] bArr) throws IOException;

    protected abstract byte[] doDecompress(byte[] bArr) throws IOException;

    @Override // io.jsonwebtoken.CompressionCodec
    public final byte[] compress(byte[] payload) {
        Assert.notNull(payload, "payload cannot be null.");
        try {
            return doCompress(payload);
        } catch (IOException e) {
            throw new CompressionException("Unable to compress payload.", e);
        }
    }

    @Override // io.jsonwebtoken.CompressionCodec
    public final byte[] decompress(byte[] compressed) {
        Assert.notNull(compressed, "compressed bytes cannot be null.");
        try {
            return doDecompress(compressed);
        } catch (IOException e) {
            throw new CompressionException("Unable to decompress bytes.", e);
        }
    }
}
