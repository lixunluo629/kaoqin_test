package com.adobe.xmp.options;

/* loaded from: xmpcore-5.1.3.jar:com/adobe/xmp/options/ParseOptions.class */
public final class ParseOptions extends Options {
    public static final int REQUIRE_XMP_META = 1;
    public static final int STRICT_ALIASING = 4;
    public static final int FIX_CONTROL_CHARS = 8;
    public static final int ACCEPT_LATIN_1 = 16;
    public static final int OMIT_NORMALIZATION = 32;
    public static final int DISALLOW_DOCTYPE = 64;

    public ParseOptions() {
        setOption(88, true);
    }

    public boolean getRequireXMPMeta() {
        return getOption(1);
    }

    public ParseOptions setRequireXMPMeta(boolean z) {
        setOption(1, z);
        return this;
    }

    public boolean getStrictAliasing() {
        return getOption(4);
    }

    public ParseOptions setStrictAliasing(boolean z) {
        setOption(4, z);
        return this;
    }

    public boolean getFixControlChars() {
        return getOption(8);
    }

    public ParseOptions setFixControlChars(boolean z) {
        setOption(8, z);
        return this;
    }

    public boolean getAcceptLatin1() {
        return getOption(16);
    }

    public ParseOptions setOmitNormalization(boolean z) {
        setOption(32, z);
        return this;
    }

    public boolean getOmitNormalization() {
        return getOption(32);
    }

    public ParseOptions setDisallowDoctype(boolean z) {
        setOption(64, z);
        return this;
    }

    public boolean getDisallowDoctype() {
        return getOption(64);
    }

    public ParseOptions setAcceptLatin1(boolean z) {
        setOption(16, z);
        return this;
    }

    @Override // com.adobe.xmp.options.Options
    protected String defineOptionName(int i) {
        switch (i) {
            case 1:
                return "REQUIRE_XMP_META";
            case 4:
                return "STRICT_ALIASING";
            case 8:
                return "FIX_CONTROL_CHARS";
            case 16:
                return "ACCEPT_LATIN_1";
            case 32:
                return "OMIT_NORMALIZATION";
            case 64:
                return "DISALLOW_DOCTYPE";
            default:
                return null;
        }
    }

    @Override // com.adobe.xmp.options.Options
    protected int getValidOptions() {
        return 125;
    }
}
