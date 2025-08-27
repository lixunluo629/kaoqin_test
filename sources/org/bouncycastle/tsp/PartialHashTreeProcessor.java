package org.bouncycastle.tsp;

import java.io.IOException;
import java.io.OutputStream;
import org.bouncycastle.asn1.tsp.PartialHashtree;
import org.bouncycastle.operator.DigestCalculator;
import org.bouncycastle.util.Arrays;

/* loaded from: bcpkix-jdk15on-1.64.jar:org/bouncycastle/tsp/PartialHashTreeProcessor.class */
public class PartialHashTreeProcessor {
    private final byte[][] values;

    public PartialHashTreeProcessor(PartialHashtree partialHashtree) {
        this.values = partialHashtree.getValues();
    }

    public byte[] getHash(DigestCalculator digestCalculator) throws IOException {
        if (this.values.length == 1) {
            return this.values[0];
        }
        try {
            OutputStream outputStream = digestCalculator.getOutputStream();
            for (int i = 1; i != this.values.length; i++) {
                outputStream.write(this.values[i]);
            }
            return digestCalculator.getDigest();
        } catch (IOException e) {
            throw new IllegalStateException("calculator failed: " + e.getMessage());
        }
    }

    public void verifyContainsHash(byte[] bArr) throws PartialHashTreeVerificationException {
        if (!containsHash(bArr)) {
            throw new PartialHashTreeVerificationException("calculated hash is not present in partial hash tree");
        }
    }

    public boolean containsHash(byte[] bArr) {
        for (int i = 1; i != this.values.length; i++) {
            if (Arrays.areEqual(bArr, this.values[i])) {
                return true;
            }
        }
        return false;
    }
}
