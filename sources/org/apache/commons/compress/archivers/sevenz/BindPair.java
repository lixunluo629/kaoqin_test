package org.apache.commons.compress.archivers.sevenz;

/* loaded from: commons-compress-1.19.jar:org/apache/commons/compress/archivers/sevenz/BindPair.class */
class BindPair {
    long inIndex;
    long outIndex;

    BindPair() {
    }

    public String toString() {
        return "BindPair binding input " + this.inIndex + " to output " + this.outIndex;
    }
}
