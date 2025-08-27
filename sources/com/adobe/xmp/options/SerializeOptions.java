package com.adobe.xmp.options;

import com.adobe.xmp.XMPException;
import org.springframework.jdbc.datasource.init.ScriptUtils;

/* loaded from: xmpcore-5.1.3.jar:com/adobe/xmp/options/SerializeOptions.class */
public final class SerializeOptions extends Options {
    public static final int OMIT_PACKET_WRAPPER = 16;
    public static final int READONLY_PACKET = 32;
    public static final int USE_COMPACT_FORMAT = 64;
    public static final int USE_CANONICAL_FORMAT = 128;
    public static final int INCLUDE_THUMBNAIL_PAD = 256;
    public static final int EXACT_PACKET_LENGTH = 512;
    public static final int OMIT_XMPMETA_ELEMENT = 4096;
    public static final int SORT = 8192;
    private static final int LITTLEENDIAN_BIT = 1;
    private static final int UTF16_BIT = 2;
    public static final int ENCODE_UTF8 = 0;
    public static final int ENCODE_UTF16BE = 2;
    public static final int ENCODE_UTF16LE = 3;
    private static final int ENCODING_MASK = 3;
    private int padding;
    private String newline;
    private String indent;
    private int baseIndent;
    private boolean omitVersionAttribute;

    public SerializeOptions() {
        this.padding = 2048;
        this.newline = ScriptUtils.FALLBACK_STATEMENT_SEPARATOR;
        this.indent = "  ";
        this.baseIndent = 0;
        this.omitVersionAttribute = false;
    }

    public SerializeOptions(int i) throws XMPException {
        super(i);
        this.padding = 2048;
        this.newline = ScriptUtils.FALLBACK_STATEMENT_SEPARATOR;
        this.indent = "  ";
        this.baseIndent = 0;
        this.omitVersionAttribute = false;
    }

    public boolean getOmitPacketWrapper() {
        return getOption(16);
    }

    public SerializeOptions setOmitPacketWrapper(boolean z) {
        setOption(16, z);
        return this;
    }

    public boolean getOmitXmpMetaElement() {
        return getOption(4096);
    }

    public SerializeOptions setOmitXmpMetaElement(boolean z) {
        setOption(4096, z);
        return this;
    }

    public boolean getReadOnlyPacket() {
        return getOption(32);
    }

    public SerializeOptions setReadOnlyPacket(boolean z) {
        setOption(32, z);
        return this;
    }

    public boolean getUseCompactFormat() {
        return getOption(64);
    }

    public SerializeOptions setUseCompactFormat(boolean z) {
        setOption(64, z);
        return this;
    }

    public boolean getUseCanonicalFormat() {
        return getOption(128);
    }

    public SerializeOptions setUseCanonicalFormat(boolean z) {
        setOption(128, z);
        return this;
    }

    public boolean getIncludeThumbnailPad() {
        return getOption(256);
    }

    public SerializeOptions setIncludeThumbnailPad(boolean z) {
        setOption(256, z);
        return this;
    }

    public boolean getExactPacketLength() {
        return getOption(512);
    }

    public SerializeOptions setExactPacketLength(boolean z) {
        setOption(512, z);
        return this;
    }

    public boolean getSort() {
        return getOption(8192);
    }

    public SerializeOptions setSort(boolean z) {
        setOption(8192, z);
        return this;
    }

    public boolean getEncodeUTF16BE() {
        return (getOptions() & 3) == 2;
    }

    public SerializeOptions setEncodeUTF16BE(boolean z) {
        setOption(3, false);
        setOption(2, z);
        return this;
    }

    public boolean getEncodeUTF16LE() {
        return (getOptions() & 3) == 3;
    }

    public SerializeOptions setEncodeUTF16LE(boolean z) {
        setOption(3, false);
        setOption(3, z);
        return this;
    }

    public int getBaseIndent() {
        return this.baseIndent;
    }

    public SerializeOptions setBaseIndent(int i) {
        this.baseIndent = i;
        return this;
    }

    public String getIndent() {
        return this.indent;
    }

    public SerializeOptions setIndent(String str) {
        this.indent = str;
        return this;
    }

    public String getNewline() {
        return this.newline;
    }

    public SerializeOptions setNewline(String str) {
        this.newline = str;
        return this;
    }

    public int getPadding() {
        return this.padding;
    }

    public SerializeOptions setPadding(int i) {
        this.padding = i;
        return this;
    }

    public boolean getOmitVersionAttribute() {
        return this.omitVersionAttribute;
    }

    public String getEncoding() {
        return getEncodeUTF16BE() ? "UTF-16BE" : getEncodeUTF16LE() ? "UTF-16LE" : "UTF-8";
    }

    public Object clone() throws CloneNotSupportedException {
        try {
            SerializeOptions serializeOptions = new SerializeOptions(getOptions());
            serializeOptions.setBaseIndent(this.baseIndent);
            serializeOptions.setIndent(this.indent);
            serializeOptions.setNewline(this.newline);
            serializeOptions.setPadding(this.padding);
            return serializeOptions;
        } catch (XMPException e) {
            return null;
        }
    }

    @Override // com.adobe.xmp.options.Options
    protected String defineOptionName(int i) {
        switch (i) {
            case 16:
                return "OMIT_PACKET_WRAPPER";
            case 32:
                return "READONLY_PACKET";
            case 64:
                return "USE_COMPACT_FORMAT";
            case 256:
                return "INCLUDE_THUMBNAIL_PAD";
            case 512:
                return "EXACT_PACKET_LENGTH";
            case 4096:
                return "OMIT_XMPMETA_ELEMENT";
            case 8192:
                return "NORMALIZED";
            default:
                return null;
        }
    }

    @Override // com.adobe.xmp.options.Options
    protected int getValidOptions() {
        return 13168;
    }
}
