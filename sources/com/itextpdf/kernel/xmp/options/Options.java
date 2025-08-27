package com.itextpdf.kernel.xmp.options;

import com.itextpdf.kernel.xmp.XMPException;
import java.util.HashMap;
import java.util.Map;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/xmp/options/Options.class */
public abstract class Options {
    private int options = 0;
    private Map optionNames = null;

    protected abstract int getValidOptions();

    protected abstract String defineOptionName(int i);

    public Options() {
    }

    public Options(int options) throws XMPException {
        assertOptionsValid(options);
        setOptions(options);
    }

    public void clear() {
        this.options = 0;
    }

    public boolean isExactly(int optionBits) {
        return getOptions() == optionBits;
    }

    public boolean containsAllOptions(int optionBits) {
        return (getOptions() & optionBits) == optionBits;
    }

    public boolean containsOneOf(int optionBits) {
        return (getOptions() & optionBits) != 0;
    }

    protected boolean getOption(int optionBit) {
        return (this.options & optionBit) != 0;
    }

    public void setOption(int optionBits, boolean value) {
        this.options = value ? this.options | optionBits : this.options & (optionBits ^ (-1));
    }

    public int getOptions() {
        return this.options;
    }

    public void setOptions(int options) throws XMPException {
        assertOptionsValid(options);
        this.options = options;
    }

    public boolean equals(Object obj) {
        return getOptions() == ((Options) obj).getOptions();
    }

    public int hashCode() {
        return getOptions();
    }

    public String getOptionsString() {
        if (this.options != 0) {
            StringBuffer sb = new StringBuffer();
            int i = this.options;
            while (true) {
                int theBits = i;
                if (theBits != 0) {
                    int oneLessBit = theBits & (theBits - 1);
                    int singleBit = theBits ^ oneLessBit;
                    String bitName = getOptionName(singleBit);
                    sb.append(bitName);
                    if (oneLessBit != 0) {
                        sb.append(" | ");
                    }
                    i = oneLessBit;
                } else {
                    return sb.toString();
                }
            }
        } else {
            return "<none>";
        }
    }

    public String toString() {
        return "0x" + Integer.toHexString(this.options);
    }

    protected void assertConsistency(int options) throws XMPException {
    }

    private void assertOptionsValid(int options) throws XMPException {
        int invalidOptions = options & (getValidOptions() ^ (-1));
        if (invalidOptions == 0) {
            assertConsistency(options);
            return;
        }
        throw new XMPException("The option bit(s) 0x" + Integer.toHexString(invalidOptions) + " are invalid!", 103);
    }

    private String getOptionName(int option) {
        HashMap optionsNames = procureOptionNames();
        Integer key = new Integer(option);
        String result = null;
        if (optionsNames.containsKey(key)) {
            result = defineOptionName(option);
            if (result != null) {
                optionsNames.put(key, result);
            } else {
                result = "<option name not defined>";
            }
        }
        return result;
    }

    private HashMap procureOptionNames() {
        if (this.optionNames == null) {
            this.optionNames = new HashMap();
        }
        return (HashMap) this.optionNames;
    }
}
