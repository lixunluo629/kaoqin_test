package org.bouncycastle.crypto.tls;

import java.util.Vector;

/* loaded from: bcprov-jdk15on-1.64.jar:org/bouncycastle/crypto/tls/DTLSReassembler.class */
class DTLSReassembler {
    private short msg_type;
    private byte[] body;
    private Vector missing = new Vector();

    /* loaded from: bcprov-jdk15on-1.64.jar:org/bouncycastle/crypto/tls/DTLSReassembler$Range.class */
    private static class Range {
        private int start;
        private int end;

        Range(int i, int i2) {
            this.start = i;
            this.end = i2;
        }

        public int getStart() {
            return this.start;
        }

        public void setStart(int i) {
            this.start = i;
        }

        public int getEnd() {
            return this.end;
        }

        public void setEnd(int i) {
            this.end = i;
        }
    }

    DTLSReassembler(short s, int i) {
        this.msg_type = s;
        this.body = new byte[i];
        this.missing.addElement(new Range(0, i));
    }

    short getMsgType() {
        return this.msg_type;
    }

    byte[] getBodyIfComplete() {
        if (this.missing.isEmpty()) {
            return this.body;
        }
        return null;
    }

    void contributeFragment(short s, int i, byte[] bArr, int i2, int i3, int i4) {
        int i5 = i3 + i4;
        if (this.msg_type == s && this.body.length == i && i5 <= i) {
            if (i4 == 0) {
                if (i3 == 0 && !this.missing.isEmpty() && ((Range) this.missing.firstElement()).getEnd() == 0) {
                    this.missing.removeElementAt(0);
                    return;
                }
                return;
            }
            int i6 = 0;
            while (i6 < this.missing.size()) {
                Range range = (Range) this.missing.elementAt(i6);
                if (range.getStart() >= i5) {
                    return;
                }
                if (range.getEnd() > i3) {
                    int iMax = Math.max(range.getStart(), i3);
                    int iMin = Math.min(range.getEnd(), i5);
                    System.arraycopy(bArr, (i2 + iMax) - i3, this.body, iMax, iMin - iMax);
                    if (iMax != range.getStart()) {
                        if (iMin != range.getEnd()) {
                            i6++;
                            this.missing.insertElementAt(new Range(iMin, range.getEnd()), i6);
                        }
                        range.setEnd(iMax);
                    } else if (iMin == range.getEnd()) {
                        int i7 = i6;
                        i6--;
                        this.missing.removeElementAt(i7);
                    } else {
                        range.setStart(iMin);
                    }
                }
                i6++;
            }
        }
    }

    void reset() {
        this.missing.removeAllElements();
        this.missing.addElement(new Range(0, this.body.length));
    }
}
