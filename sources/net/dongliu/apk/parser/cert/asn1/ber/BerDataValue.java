package net.dongliu.apk.parser.cert.asn1.ber;

import java.nio.ByteBuffer;

/* loaded from: apk-parser-2.5.3.jar:net/dongliu/apk/parser/cert/asn1/ber/BerDataValue.class */
public class BerDataValue {
    private final ByteBuffer mEncoded;
    private final ByteBuffer mEncodedContents;
    private final int mTagClass;
    private final boolean mConstructed;
    private final int mTagNumber;

    BerDataValue(ByteBuffer encoded, ByteBuffer encodedContents, int tagClass, boolean constructed, int tagNumber) {
        this.mEncoded = encoded;
        this.mEncodedContents = encodedContents;
        this.mTagClass = tagClass;
        this.mConstructed = constructed;
        this.mTagNumber = tagNumber;
    }

    public int getTagClass() {
        return this.mTagClass;
    }

    public boolean isConstructed() {
        return this.mConstructed;
    }

    public int getTagNumber() {
        return this.mTagNumber;
    }

    public ByteBuffer getEncoded() {
        return this.mEncoded.slice();
    }

    public ByteBuffer getEncodedContents() {
        return this.mEncodedContents.slice();
    }

    public BerDataValueReader contentsReader() {
        return new ByteBufferBerDataValueReader(getEncodedContents());
    }

    public BerDataValueReader dataValueReader() {
        return new ParsedValueReader(this);
    }

    /* loaded from: apk-parser-2.5.3.jar:net/dongliu/apk/parser/cert/asn1/ber/BerDataValue$ParsedValueReader.class */
    private static final class ParsedValueReader implements BerDataValueReader {
        private final BerDataValue mValue;
        private boolean mValueOutput;

        public ParsedValueReader(BerDataValue value) {
            this.mValue = value;
        }

        @Override // net.dongliu.apk.parser.cert.asn1.ber.BerDataValueReader
        public BerDataValue readDataValue() throws BerDataValueFormatException {
            if (this.mValueOutput) {
                return null;
            }
            this.mValueOutput = true;
            return this.mValue;
        }
    }
}
