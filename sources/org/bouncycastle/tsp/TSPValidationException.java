package org.bouncycastle.tsp;

/* loaded from: bcpkix-jdk15on-1.64.jar:org/bouncycastle/tsp/TSPValidationException.class */
public class TSPValidationException extends TSPException {
    private int failureCode;

    public TSPValidationException(String str) {
        super(str);
        this.failureCode = -1;
    }

    public TSPValidationException(String str, int i) {
        super(str);
        this.failureCode = -1;
        this.failureCode = i;
    }

    public int getFailureCode() {
        return this.failureCode;
    }
}
