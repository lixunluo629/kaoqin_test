package org.bouncycastle.tsp;

import org.apache.commons.compress.archivers.tar.TarConstants;
import org.bouncycastle.asn1.ASN1Integer;
import org.bouncycastle.asn1.tsp.Accuracy;

/* loaded from: bcpkix-jdk15on-1.64.jar:org/bouncycastle/tsp/GenTimeAccuracy.class */
public class GenTimeAccuracy {
    private Accuracy accuracy;

    public GenTimeAccuracy(Accuracy accuracy) {
        this.accuracy = accuracy;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public int getSeconds() {
        return getTimeComponent(this.accuracy.getSeconds());
    }

    /* JADX WARN: Multi-variable type inference failed */
    public int getMillis() {
        return getTimeComponent(this.accuracy.getMillis());
    }

    /* JADX WARN: Multi-variable type inference failed */
    public int getMicros() {
        return getTimeComponent(this.accuracy.getMicros());
    }

    private int getTimeComponent(ASN1Integer aSN1Integer) {
        if (aSN1Integer != null) {
            return aSN1Integer.intValueExact();
        }
        return 0;
    }

    public String toString() {
        return getSeconds() + "." + format(getMillis()) + format(getMicros());
    }

    private String format(int i) {
        return i < 10 ? TarConstants.VERSION_POSIX + i : i < 100 ? "0" + i : Integer.toString(i);
    }
}
