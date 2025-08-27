package org.bouncycastle.pqc.crypto.newhope;

import com.drew.metadata.exif.makernotes.CasioType2MakernoteDirectory;

/* loaded from: bcprov-jdk15on-1.64.jar:org/bouncycastle/pqc/crypto/newhope/Reduce.class */
class Reduce {
    static final int QInv = 12287;
    static final int RLog = 18;
    static final int RMask = 262143;

    Reduce() {
    }

    static short montgomery(int i) {
        return (short) (((((i * QInv) & RMask) * CasioType2MakernoteDirectory.TAG_SELF_TIMER) + i) >>> 18);
    }

    static short barrett(short s) {
        int i = s & 65535;
        return (short) (i - (((i * 5) >>> 16) * CasioType2MakernoteDirectory.TAG_SELF_TIMER));
    }
}
