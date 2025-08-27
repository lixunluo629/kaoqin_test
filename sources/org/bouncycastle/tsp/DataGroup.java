package org.bouncycastle.tsp;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;
import org.bouncycastle.operator.DigestCalculator;
import org.bouncycastle.util.Arrays;

/* loaded from: bcpkix-jdk15on-1.64.jar:org/bouncycastle/tsp/DataGroup.class */
public class DataGroup {
    private List<byte[]> dataObjects;
    private byte[] groupHash;
    private TreeSet<byte[]> hashes;

    /* loaded from: bcpkix-jdk15on-1.64.jar:org/bouncycastle/tsp/DataGroup$ByteArrayComparator.class */
    private class ByteArrayComparator implements Comparator {
        private ByteArrayComparator() {
        }

        @Override // java.util.Comparator
        public int compare(Object obj, Object obj2) {
            byte[] bArr = (byte[]) obj;
            byte[] bArr2 = (byte[]) obj2;
            int length = bArr.length < bArr2.length ? bArr.length : bArr2.length;
            for (int i = 0; i != length; i++) {
                int i2 = bArr[i] & 255;
                int i3 = bArr2[i] & 255;
                if (i2 != i3) {
                    return i2 - i3;
                }
            }
            return bArr.length - bArr2.length;
        }
    }

    public DataGroup(List<byte[]> list) {
        this.dataObjects = list;
    }

    public DataGroup(byte[] bArr) {
        this.dataObjects = new ArrayList();
        this.dataObjects.add(bArr);
    }

    public TreeSet<byte[]> getHashes(DigestCalculator digestCalculator) {
        return getHashes(digestCalculator, null);
    }

    private TreeSet<byte[]> getHashes(DigestCalculator digestCalculator, byte[] bArr) {
        if (this.hashes == null) {
            this.hashes = new TreeSet<>(new ByteArrayComparator());
            for (int i = 0; i != this.dataObjects.size(); i++) {
                byte[] bArr2 = this.dataObjects.get(i);
                if (bArr != null) {
                    this.hashes.add(calcDigest(digestCalculator, Arrays.concatenate(calcDigest(digestCalculator, bArr2), bArr)));
                } else {
                    this.hashes.add(calcDigest(digestCalculator, bArr2));
                }
            }
        }
        return this.hashes;
    }

    public byte[] getHash(DigestCalculator digestCalculator) {
        if (this.groupHash == null) {
            TreeSet<byte[]> hashes = getHashes(digestCalculator);
            if (hashes.size() > 1) {
                byte[] bArrConcatenate = new byte[0];
                Iterator<byte[]> it = hashes.iterator();
                while (it.hasNext()) {
                    bArrConcatenate = Arrays.concatenate(bArrConcatenate, it.next());
                }
                this.groupHash = calcDigest(digestCalculator, bArrConcatenate);
            } else {
                this.groupHash = hashes.first();
            }
        }
        return this.groupHash;
    }

    static byte[] calcDigest(DigestCalculator digestCalculator, byte[] bArr) throws IOException {
        try {
            OutputStream outputStream = digestCalculator.getOutputStream();
            outputStream.write(bArr);
            outputStream.close();
            return digestCalculator.getDigest();
        } catch (IOException e) {
            throw new IllegalStateException("digest calculator failure: " + e.getMessage());
        }
    }
}
