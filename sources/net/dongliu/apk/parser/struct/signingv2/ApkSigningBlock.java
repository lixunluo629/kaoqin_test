package net.dongliu.apk.parser.struct.signingv2;

import java.util.List;

/* loaded from: apk-parser-2.5.3.jar:net/dongliu/apk/parser/struct/signingv2/ApkSigningBlock.class */
public class ApkSigningBlock {
    public static final int SIGNING_V2_ID = 1896449818;
    public static final String MAGIC = "APK Sig Block 42";
    private List<SignerBlock> signerBlocks;

    public ApkSigningBlock(List<SignerBlock> signerBlocks) {
        this.signerBlocks = signerBlocks;
    }

    public List<SignerBlock> getSignerBlocks() {
        return this.signerBlocks;
    }
}
