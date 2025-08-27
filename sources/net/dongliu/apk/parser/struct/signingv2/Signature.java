package net.dongliu.apk.parser.struct.signingv2;

/* loaded from: apk-parser-2.5.3.jar:net/dongliu/apk/parser/struct/signingv2/Signature.class */
public class Signature {
    private int algorithmID;
    private byte[] data;

    public Signature(int algorithmID, byte[] data) {
        this.algorithmID = algorithmID;
        this.data = data;
    }

    public int getAlgorithmID() {
        return this.algorithmID;
    }

    public byte[] getData() {
        return this.data;
    }
}
