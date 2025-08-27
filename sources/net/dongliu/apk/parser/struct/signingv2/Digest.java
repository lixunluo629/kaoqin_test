package net.dongliu.apk.parser.struct.signingv2;

/* loaded from: apk-parser-2.5.3.jar:net/dongliu/apk/parser/struct/signingv2/Digest.class */
public class Digest {
    private int algorithmID;
    private byte[] value;

    public Digest(int algorithmID, byte[] value) {
        this.algorithmID = algorithmID;
        this.value = value;
    }

    public int getAlgorithmID() {
        return this.algorithmID;
    }

    public byte[] getValue() {
        return this.value;
    }
}
