package com.adobe.xmp.options;

import com.adobe.xmp.XMPException;
import java.util.HashMap;
import java.util.Map;

/* loaded from: xmpcore-5.1.3.jar:com/adobe/xmp/options/Options.class */
public abstract class Options {
    private int options = 0;
    private Map optionNames = null;

    public Options() {
    }

    public Options(int i) throws XMPException {
        assertOptionsValid(i);
        setOptions(i);
    }

    public void clear() {
        this.options = 0;
    }

    public boolean isExactly(int i) {
        return getOptions() == i;
    }

    public boolean containsAllOptions(int i) {
        return (getOptions() & i) == i;
    }

    public boolean containsOneOf(int i) {
        return (getOptions() & i) != 0;
    }

    protected boolean getOption(int i) {
        return (this.options & i) != 0;
    }

    public void setOption(int i, boolean z) {
        this.options = z ? this.options | i : this.options & (i ^ (-1));
    }

    public int getOptions() {
        return this.options;
    }

    public void setOptions(int i) throws XMPException {
        assertOptionsValid(i);
        this.options = i;
    }

    public boolean equals(Object obj) {
        return getOptions() == ((Options) obj).getOptions();
    }

    public int hashCode() {
        return getOptions();
    }

    public String getOptionsString() {
        if (this.options == 0) {
            return "<none>";
        }
        StringBuffer stringBuffer = new StringBuffer();
        int i = this.options;
        while (true) {
            int i2 = i;
            if (i2 == 0) {
                return stringBuffer.toString();
            }
            int i3 = i2 & (i2 - 1);
            stringBuffer.append(getOptionName(i2 ^ i3));
            if (i3 != 0) {
                stringBuffer.append(" | ");
            }
            i = i3;
        }
    }

    public String toString() {
        return "0x" + Integer.toHexString(this.options);
    }

    protected abstract int getValidOptions();

    protected abstract String defineOptionName(int i);

    protected void assertConsistency(int i) throws XMPException {
    }

    private void assertOptionsValid(int i) throws XMPException {
        int validOptions = i & (getValidOptions() ^ (-1));
        if (validOptions != 0) {
            throw new XMPException("The option bit(s) 0x" + Integer.toHexString(validOptions) + " are invalid!", 103);
        }
        assertConsistency(i);
    }

    private String getOptionName(int i) {
        Map mapProcureOptionNames = procureOptionNames();
        Integer num = new Integer(i);
        String strDefineOptionName = (String) mapProcureOptionNames.get(num);
        if (strDefineOptionName == null) {
            strDefineOptionName = defineOptionName(i);
            if (strDefineOptionName != null) {
                mapProcureOptionNames.put(num, strDefineOptionName);
            } else {
                strDefineOptionName = "<option name not defined>";
            }
        }
        return strDefineOptionName;
    }

    private Map procureOptionNames() {
        if (this.optionNames == null) {
            this.optionNames = new HashMap();
        }
        return this.optionNames;
    }
}
