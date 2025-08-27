package org.bouncycastle.asn1;

import java.io.ByteArrayInputStream;
import java.io.EOFException;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.bouncycastle.util.io.Streams;

/* JADX WARN: Classes with same name are omitted:
  bcprov-jdk15on-1.64.jar:org/bouncycastle/asn1/ASN1InputStream.class
 */
/* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/asn1/ASN1InputStream.class */
public class ASN1InputStream extends FilterInputStream implements DERTags {
    private final int limit;
    private final boolean lazyEvaluate;

    static int findLimit(InputStream inputStream) {
        if (inputStream instanceof LimitedInputStream) {
            return ((LimitedInputStream) inputStream).getRemaining();
        }
        if (inputStream instanceof ByteArrayInputStream) {
            return ((ByteArrayInputStream) inputStream).available();
        }
        return Integer.MAX_VALUE;
    }

    public ASN1InputStream(InputStream inputStream) {
        this(inputStream, findLimit(inputStream));
    }

    public ASN1InputStream(byte[] bArr) {
        this(new ByteArrayInputStream(bArr), bArr.length);
    }

    public ASN1InputStream(byte[] bArr, boolean z) {
        this(new ByteArrayInputStream(bArr), bArr.length, z);
    }

    public ASN1InputStream(InputStream inputStream, int i) {
        this(inputStream, i, false);
    }

    public ASN1InputStream(InputStream inputStream, int i, boolean z) {
        super(inputStream);
        this.limit = i;
        this.lazyEvaluate = z;
    }

    protected int readLength() throws IOException {
        return readLength(this, this.limit);
    }

    protected void readFully(byte[] bArr) throws IOException {
        if (Streams.readFully(this, bArr) != bArr.length) {
            throw new EOFException("EOF encountered in middle of object");
        }
    }

    protected DERObject buildObject(int i, int i2, int i3) throws IOException {
        boolean z = (i & 32) != 0;
        DefiniteLengthInputStream definiteLengthInputStream = new DefiniteLengthInputStream(this, i3);
        if ((i & 64) != 0) {
            return new DERApplicationSpecific(z, i2, definiteLengthInputStream.toByteArray());
        }
        if ((i & 128) != 0) {
            return new ASN1StreamParser(definiteLengthInputStream).readTaggedObject(z, i2);
        }
        if (!z) {
            return createPrimitiveDERObject(i2, definiteLengthInputStream.toByteArray());
        }
        switch (i2) {
            case 4:
                return new BERConstructedOctetString(buildDEREncodableVector(definiteLengthInputStream).v);
            case 8:
                return new DERExternal(buildDEREncodableVector(definiteLengthInputStream));
            case 16:
                return this.lazyEvaluate ? new LazyDERSequence(definiteLengthInputStream.toByteArray()) : DERFactory.createSequence(buildDEREncodableVector(definiteLengthInputStream));
            case 17:
                return DERFactory.createSet(buildDEREncodableVector(definiteLengthInputStream), false);
            default:
                return new DERUnknownTag(true, i2, definiteLengthInputStream.toByteArray());
        }
    }

    ASN1EncodableVector buildEncodableVector() throws IOException {
        ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
        while (true) {
            DERObject object = readObject();
            if (object == null) {
                return aSN1EncodableVector;
            }
            aSN1EncodableVector.add(object);
        }
    }

    ASN1EncodableVector buildDEREncodableVector(DefiniteLengthInputStream definiteLengthInputStream) throws IOException {
        return new ASN1InputStream(definiteLengthInputStream).buildEncodableVector();
    }

    public DERObject readObject() throws IOException {
        int i = read();
        if (i <= 0) {
            if (i == 0) {
                throw new IOException("unexpected end-of-contents marker");
            }
            return null;
        }
        int tagNumber = readTagNumber(this, i);
        boolean z = (i & 32) != 0;
        int length = readLength();
        if (length >= 0) {
            try {
                return buildObject(i, tagNumber, length);
            } catch (IllegalArgumentException e) {
                throw new ASN1Exception("corrupted stream detected", e);
            }
        }
        if (!z) {
            throw new IOException("indefinite length primitive encoding encountered");
        }
        ASN1StreamParser aSN1StreamParser = new ASN1StreamParser(new IndefiniteLengthInputStream(this, this.limit), this.limit);
        if ((i & 64) != 0) {
            return new BERApplicationSpecificParser(tagNumber, aSN1StreamParser).getLoadedObject();
        }
        if ((i & 128) != 0) {
            return new BERTaggedObjectParser(true, tagNumber, aSN1StreamParser).getLoadedObject();
        }
        switch (tagNumber) {
            case 4:
                return new BEROctetStringParser(aSN1StreamParser).getLoadedObject();
            case 8:
                return new DERExternalParser(aSN1StreamParser).getLoadedObject();
            case 16:
                return new BERSequenceParser(aSN1StreamParser).getLoadedObject();
            case 17:
                return new BERSetParser(aSN1StreamParser).getLoadedObject();
            default:
                throw new IOException("unknown BER object encountered");
        }
    }

    static int readTagNumber(InputStream inputStream, int i) throws IOException {
        int i2 = i & 31;
        if (i2 == 31) {
            int i3 = 0;
            int i4 = inputStream.read();
            if ((i4 & 127) == 0) {
                throw new IOException("corrupted stream - invalid high tag number found");
            }
            while (i4 >= 0 && (i4 & 128) != 0) {
                i3 = (i3 | (i4 & 127)) << 7;
                i4 = inputStream.read();
            }
            if (i4 < 0) {
                throw new EOFException("EOF found inside tag value.");
            }
            i2 = i3 | (i4 & 127);
        }
        return i2;
    }

    static int readLength(InputStream inputStream, int i) throws IOException {
        int i2 = inputStream.read();
        if (i2 < 0) {
            throw new EOFException("EOF found when length expected");
        }
        if (i2 == 128) {
            return -1;
        }
        if (i2 > 127) {
            int i3 = i2 & 127;
            if (i3 > 4) {
                throw new IOException("DER length more than 4 bytes: " + i3);
            }
            i2 = 0;
            for (int i4 = 0; i4 < i3; i4++) {
                int i5 = inputStream.read();
                if (i5 < 0) {
                    throw new EOFException("EOF found reading length");
                }
                i2 = (i2 << 8) + i5;
            }
            if (i2 < 0) {
                throw new IOException("corrupted stream - negative length found");
            }
            if (i2 >= i) {
                throw new IOException("corrupted stream - out of bounds length found");
            }
        }
        return i2;
    }

    static DERObject createPrimitiveDERObject(int i, byte[] bArr) {
        switch (i) {
            case 1:
                return new ASN1Boolean(bArr);
            case 2:
                return new ASN1Integer(bArr);
            case 3:
                return DERBitString.fromOctetString(bArr);
            case 4:
                return new DEROctetString(bArr);
            case 5:
                return DERNull.INSTANCE;
            case 6:
                return new ASN1ObjectIdentifier(bArr);
            case 7:
            case 8:
            case 9:
            case 11:
            case 13:
            case 14:
            case 15:
            case 16:
            case 17:
            case 21:
            case 25:
            case 29:
            default:
                return new DERUnknownTag(false, i, bArr);
            case 10:
                return new ASN1Enumerated(bArr);
            case 12:
                return new DERUTF8String(bArr);
            case 18:
                return new DERNumericString(bArr);
            case 19:
                return new DERPrintableString(bArr);
            case 20:
                return new DERT61String(bArr);
            case 22:
                return new DERIA5String(bArr);
            case 23:
                return new ASN1UTCTime(bArr);
            case 24:
                return new ASN1GeneralizedTime(bArr);
            case 26:
                return new DERVisibleString(bArr);
            case 27:
                return new DERGeneralString(bArr);
            case 28:
                return new DERUniversalString(bArr);
            case 30:
                return new DERBMPString(bArr);
        }
    }
}
