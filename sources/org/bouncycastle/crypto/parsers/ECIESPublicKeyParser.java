package org.bouncycastle.crypto.parsers;

import java.io.IOException;
import java.io.InputStream;
import org.bouncycastle.crypto.KeyParser;
import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle.crypto.params.ECDomainParameters;
import org.bouncycastle.crypto.params.ECPublicKeyParameters;
import org.bouncycastle.util.io.Streams;

/* loaded from: bcprov-jdk15on-1.64.jar:org/bouncycastle/crypto/parsers/ECIESPublicKeyParser.class */
public class ECIESPublicKeyParser implements KeyParser {
    private ECDomainParameters ecParams;

    public ECIESPublicKeyParser(ECDomainParameters eCDomainParameters) {
        this.ecParams = eCDomainParameters;
    }

    @Override // org.bouncycastle.crypto.KeyParser
    public AsymmetricKeyParameter readKey(InputStream inputStream) throws IOException {
        byte[] bArr;
        int i = inputStream.read();
        switch (i) {
            case 0:
                throw new IOException("Sender's public key invalid.");
            case 1:
            case 5:
            default:
                throw new IOException("Sender's public key has invalid point encoding 0x" + Integer.toString(i, 16));
            case 2:
            case 3:
                bArr = new byte[1 + ((this.ecParams.getCurve().getFieldSize() + 7) / 8)];
                break;
            case 4:
            case 6:
            case 7:
                bArr = new byte[1 + (2 * ((this.ecParams.getCurve().getFieldSize() + 7) / 8))];
                break;
        }
        bArr[0] = (byte) i;
        Streams.readFully(inputStream, bArr, 1, bArr.length - 1);
        return new ECPublicKeyParameters(this.ecParams.getCurve().decodePoint(bArr), this.ecParams);
    }
}
