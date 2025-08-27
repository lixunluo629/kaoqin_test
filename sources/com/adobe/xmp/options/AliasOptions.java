package com.adobe.xmp.options;

import com.adobe.xmp.XMPException;

/* loaded from: xmpcore-5.1.3.jar:com/adobe/xmp/options/AliasOptions.class */
public final class AliasOptions extends Options {
    public static final int PROP_DIRECT = 0;
    public static final int PROP_ARRAY = 512;
    public static final int PROP_ARRAY_ORDERED = 1024;
    public static final int PROP_ARRAY_ALTERNATE = 2048;
    public static final int PROP_ARRAY_ALT_TEXT = 4096;

    public AliasOptions() {
    }

    public AliasOptions(int i) throws XMPException {
        super(i);
    }

    public boolean isSimple() {
        return getOptions() == 0;
    }

    public boolean isArray() {
        return getOption(512);
    }

    public AliasOptions setArray(boolean z) {
        setOption(512, z);
        return this;
    }

    public boolean isArrayOrdered() {
        return getOption(1024);
    }

    public AliasOptions setArrayOrdered(boolean z) {
        setOption(1536, z);
        return this;
    }

    public boolean isArrayAlternate() {
        return getOption(2048);
    }

    public AliasOptions setArrayAlternate(boolean z) {
        setOption(3584, z);
        return this;
    }

    public boolean isArrayAltText() {
        return getOption(4096);
    }

    public AliasOptions setArrayAltText(boolean z) {
        setOption(7680, z);
        return this;
    }

    public PropertyOptions toPropertyOptions() throws XMPException {
        return new PropertyOptions(getOptions());
    }

    @Override // com.adobe.xmp.options.Options
    protected String defineOptionName(int i) {
        switch (i) {
            case 0:
                return "PROP_DIRECT";
            case 512:
                return "ARRAY";
            case 1024:
                return "ARRAY_ORDERED";
            case 2048:
                return "ARRAY_ALTERNATE";
            case 4096:
                return "ARRAY_ALT_TEXT";
            default:
                return null;
        }
    }

    @Override // com.adobe.xmp.options.Options
    protected int getValidOptions() {
        return 7680;
    }
}
