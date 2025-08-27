package io.jsonwebtoken.impl.compression;

import io.jsonwebtoken.CompressionCodec;

/* loaded from: jjwt-0.6.0.jar:io/jsonwebtoken/impl/compression/CompressionCodecs.class */
public final class CompressionCodecs {
    private static final CompressionCodecs I = new CompressionCodecs();
    public static final CompressionCodec DEFLATE = new DeflateCompressionCodec();
    public static final CompressionCodec GZIP = new GzipCompressionCodec();

    private CompressionCodecs() {
    }
}
