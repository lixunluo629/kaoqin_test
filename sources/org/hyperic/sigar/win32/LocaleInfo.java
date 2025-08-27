package org.hyperic.sigar.win32;

/* loaded from: sigar-1.6.4.jar:org/hyperic/sigar/win32/LocaleInfo.class */
public class LocaleInfo extends Win32 {
    public static final int LOCALE_SENGLANGUAGE = 4097;
    public static final int LOCALE_SENGCOUNTRY = 4098;
    public static final int LANG_ENGLISH = 9;
    private int id;

    private static native int getSystemDefaultLCID();

    private static native String getAttribute(int i, int i2);

    public LocaleInfo() {
        this(getSystemDefaultLCID());
    }

    public static final int makeLangId(int primary, int sub) {
        return (sub << 10) | primary;
    }

    public LocaleInfo(Integer id) {
        this(id.intValue());
    }

    public LocaleInfo(int id) {
        this.id = id;
    }

    public LocaleInfo(int primary, int sub) {
        this(makeLangId(primary, sub));
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private static int getPrimaryLangId(int id) {
        return id & 1023;
    }

    public int getPrimaryLangId() {
        return getPrimaryLangId(this.id);
    }

    private static int getSubLangId(int id) {
        return id >> 10;
    }

    public int getSubLangId() {
        return getSubLangId(this.id);
    }

    public static boolean isEnglish() {
        int id = getSystemDefaultLCID();
        return getPrimaryLangId(id) == 9;
    }

    public String getPerflibLangId() {
        String id = Integer.toHexString(getPrimaryLangId()).toUpperCase();
        int pad = 3 - id.length();
        StringBuffer fid = new StringBuffer(3);
        while (true) {
            int i = pad;
            pad = i - 1;
            if (i > 0) {
                fid.append("0");
            } else {
                fid.append(id);
                return fid.toString();
            }
        }
    }

    public String getAttribute(int attr) {
        return getAttribute(this.id, attr);
    }

    public String getEnglishLanguageName() {
        return getAttribute(4097);
    }

    public String getEnglishCountryName() {
        return getAttribute(4098);
    }

    public String toString() {
        return new StringBuffer().append(getId()).append(":").append(getEnglishLanguageName()).append(" (").append(getEnglishCountryName()).append(")").toString();
    }
}
