package org.bouncycastle.asn1;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/* JADX WARN: Classes with same name are omitted:
  bcprov-jdk15on-1.64.jar:org/bouncycastle/asn1/ASN1StreamParser.class
 */
/* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/asn1/ASN1StreamParser.class */
public class ASN1StreamParser {
    private final InputStream _in;
    private final int _limit;

    public ASN1StreamParser(InputStream inputStream) {
        this(inputStream, ASN1InputStream.findLimit(inputStream));
    }

    public ASN1StreamParser(InputStream inputStream, int i) {
        this._in = inputStream;
        this._limit = i;
    }

    public ASN1StreamParser(byte[] bArr) {
        this(new ByteArrayInputStream(bArr), bArr.length);
    }

    DEREncodable readIndef(int i) throws IOException {
        switch (i) {
            case 4:
                return new BEROctetStringParser(this);
            case 8:
                return new DERExternalParser(this);
            case 16:
                return new BERSequenceParser(this);
            case 17:
                return new BERSetParser(this);
            default:
                throw new ASN1Exception("unknown BER object encountered: 0x" + Integer.toHexString(i));
        }
    }

    DEREncodable readImplicit(boolean z, int i) throws IOException {
        if (this._in instanceof IndefiniteLengthInputStream) {
            if (z) {
                return readIndef(i);
            }
            throw new IOException("indefinite length primitive encoding encountered");
        }
        if (z) {
            switch (i) {
                case 4:
                    return new BEROctetStringParser(this);
                case 16:
                    return new DERSequenceParser(this);
                case 17:
                    return new DERSetParser(this);
            }
        }
        switch (i) {
            case 4:
                return new DEROctetStringParser((DefiniteLengthInputStream) this._in);
            case 16:
                throw new ASN1Exception("sets must use constructed encoding (see X.690 8.11.1/8.12.1)");
            case 17:
                throw new ASN1Exception("sequences must use constructed encoding (see X.690 8.9.1/8.10.1)");
        }
        throw new RuntimeException("implicit tagging not implemented");
    }

    DERObject readTaggedObject(boolean z, int i) throws IOException {
        if (!z) {
            return new DERTaggedObject(false, i, new DEROctetString(((DefiniteLengthInputStream) this._in).toByteArray()));
        }
        ASN1EncodableVector vector = readVector();
        return this._in instanceof IndefiniteLengthInputStream ? vector.size() == 1 ? new BERTaggedObject(true, i, vector.get(0)) : new BERTaggedObject(false, i, BERFactory.createSequence(vector)) : vector.size() == 1 ? new DERTaggedObject(true, i, vector.get(0)) : new DERTaggedObject(false, i, DERFactory.createSequence(vector));
    }

    public DEREncodable readObject() throws IOException {
        int i = this._in.read();
        if (i == -1) {
            return null;
        }
        set00Check(false);
        int tagNumber = ASN1InputStream.readTagNumber(this._in, i);
        boolean z = (i & 32) != 0;
        int length = ASN1InputStream.readLength(this._in, this._limit);
        if (length < 0) {
            if (!z) {
                throw new IOException("indefinite length primitive encoding encountered");
            }
            ASN1StreamParser aSN1StreamParser = new ASN1StreamParser(new IndefiniteLengthInputStream(this._in, this._limit), this._limit);
            return (i & 64) != 0 ? new BERApplicationSpecificParser(tagNumber, aSN1StreamParser) : (i & 128) != 0 ? new BERTaggedObjectParser(true, tagNumber, aSN1StreamParser) : aSN1StreamParser.readIndef(tagNumber);
        }
        DefiniteLengthInputStream definiteLengthInputStream = new DefiniteLengthInputStream(this._in, length);
        if ((i & 64) != 0) {
            return new DERApplicationSpecific(z, tagNumber, definiteLengthInputStream.toByteArray());
        }
        if ((i & 128) != 0) {
            return new BERTaggedObjectParser(z, tagNumber, new ASN1StreamParser(definiteLengthInputStream));
        }
        if (!z) {
            switch (tagNumber) {
                case 4:
                    return new DEROctetStringParser(definiteLengthInputStream);
                default:
                    try {
                        return ASN1InputStream.createPrimitiveDERObject(tagNumber, definiteLengthInputStream.toByteArray());
                    } catch (IllegalArgumentException e) {
                        throw new ASN1Exception("corrupted stream detected", e);
                    }
            }
        }
        switch (tagNumber) {
            case 4:
                return new BEROctetStringParser(new ASN1StreamParser(definiteLengthInputStream));
            case 8:
                return new DERExternalParser(new ASN1StreamParser(definiteLengthInputStream));
            case 16:
                return new DERSequenceParser(new ASN1StreamParser(definiteLengthInputStream));
            case 17:
                return new DERSetParser(new ASN1StreamParser(definiteLengthInputStream));
            default:
                return new DERUnknownTag(true, tagNumber, definiteLengthInputStream.toByteArray());
        }
    }

    private void set00Check(boolean z) {
        if (this._in instanceof IndefiniteLengthInputStream) {
            ((IndefiniteLengthInputStream) this._in).setEofOn00(z);
        }
    }

    ASN1EncodableVector readVector() throws IOException {
        ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
        while (true) {
            DEREncodable object = readObject();
            if (object == null) {
                return aSN1EncodableVector;
            }
            if (object instanceof InMemoryRepresentable) {
                aSN1EncodableVector.add(((InMemoryRepresentable) object).getLoadedObject());
            } else {
                aSN1EncodableVector.add(object.getDERObject());
            }
        }
    }
}
