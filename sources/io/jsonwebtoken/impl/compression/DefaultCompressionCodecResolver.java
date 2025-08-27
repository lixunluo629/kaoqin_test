package io.jsonwebtoken.impl.compression;

import io.jsonwebtoken.CompressionCodec;
import io.jsonwebtoken.CompressionCodecResolver;
import io.jsonwebtoken.CompressionException;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.lang.Assert;
import io.jsonwebtoken.lang.Strings;

/* loaded from: jjwt-0.6.0.jar:io/jsonwebtoken/impl/compression/DefaultCompressionCodecResolver.class */
public class DefaultCompressionCodecResolver implements CompressionCodecResolver {
    @Override // io.jsonwebtoken.CompressionCodecResolver
    public CompressionCodec resolveCompressionCodec(Header header) {
        String cmpAlg = getAlgorithmFromHeader(header);
        boolean hasCompressionAlgorithm = Strings.hasText(cmpAlg);
        if (!hasCompressionAlgorithm) {
            return null;
        }
        if (CompressionCodecs.DEFLATE.getAlgorithmName().equalsIgnoreCase(cmpAlg)) {
            return CompressionCodecs.DEFLATE;
        }
        if (CompressionCodecs.GZIP.getAlgorithmName().equalsIgnoreCase(cmpAlg)) {
            return CompressionCodecs.GZIP;
        }
        throw new CompressionException("Unsupported compression algorithm '" + cmpAlg + "'");
    }

    private String getAlgorithmFromHeader(Header header) {
        Assert.notNull(header, "header cannot be null.");
        return header.getCompressionAlgorithm();
    }
}
