package org.bouncycastle.crypto.tls;

/* loaded from: bcprov-jdk15on-1.64.jar:org/bouncycastle/crypto/tls/DTLSReplayWindow.class */
class DTLSReplayWindow {
    private static final long VALID_SEQ_MASK = 281474976710655L;
    private static final long WINDOW_SIZE = 64;
    private long latestConfirmedSeq = -1;
    private long bitmap = 0;

    DTLSReplayWindow() {
    }

    boolean shouldDiscard(long j) {
        if ((j & VALID_SEQ_MASK) != j) {
            return true;
        }
        if (j > this.latestConfirmedSeq) {
            return false;
        }
        long j2 = this.latestConfirmedSeq - j;
        return j2 >= WINDOW_SIZE || (this.bitmap & (1 << ((int) j2))) != 0;
    }

    void reportAuthenticated(long j) {
        if ((j & VALID_SEQ_MASK) != j) {
            throw new IllegalArgumentException("'seq' out of range");
        }
        if (j <= this.latestConfirmedSeq) {
            long j2 = this.latestConfirmedSeq - j;
            if (j2 < WINDOW_SIZE) {
                this.bitmap |= 1 << ((int) j2);
                return;
            }
            return;
        }
        long j3 = j - this.latestConfirmedSeq;
        if (j3 >= WINDOW_SIZE) {
            this.bitmap = 1L;
        } else {
            this.bitmap <<= (int) j3;
            this.bitmap |= 1;
        }
        this.latestConfirmedSeq = j;
    }

    void reset() {
        this.latestConfirmedSeq = -1L;
        this.bitmap = 0L;
    }
}
