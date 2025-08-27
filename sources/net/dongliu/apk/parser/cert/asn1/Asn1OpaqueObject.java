package net.dongliu.apk.parser.cert.asn1;

import java.nio.ByteBuffer;

/* loaded from: apk-parser-2.5.3.jar:net/dongliu/apk/parser/cert/asn1/Asn1OpaqueObject.class */
public class Asn1OpaqueObject {
    private final ByteBuffer mEncoded;

    public Asn1OpaqueObject(ByteBuffer encoded) {
        this.mEncoded = encoded.slice();
    }

    public Asn1OpaqueObject(byte[] encoded) {
        this.mEncoded = ByteBuffer.wrap(encoded);
    }

    public ByteBuffer getEncoded() {
        return this.mEncoded.slice();
    }
}
