package org.bouncycastle.util;

/* JADX WARN: Classes with same name are omitted:
  bcprov-jdk15on-1.64.jar:org/bouncycastle/util/IPAddress.class
 */
/* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/util/IPAddress.class */
public class IPAddress {
    public static boolean isValid(String str) {
        return isValidIPv4(str) || isValidIPv6(str);
    }

    public static boolean isValidWithNetMask(String str) {
        return isValidIPv4WithNetmask(str) || isValidIPv6WithNetmask(str);
    }

    public static boolean isValidIPv4(String str) throws NumberFormatException {
        int iIndexOf;
        if (str.length() == 0) {
            return false;
        }
        int i = 0;
        String str2 = str + ".";
        int i2 = 0;
        while (i2 < str2.length() && (iIndexOf = str2.indexOf(46, i2)) > i2) {
            if (i == 4) {
                return false;
            }
            try {
                int i3 = Integer.parseInt(str2.substring(i2, iIndexOf));
                if (i3 < 0 || i3 > 255) {
                    return false;
                }
                i2 = iIndexOf + 1;
                i++;
            } catch (NumberFormatException e) {
                return false;
            }
        }
        return i == 4;
    }

    public static boolean isValidIPv4WithNetmask(String str) {
        int iIndexOf = str.indexOf("/");
        String strSubstring = str.substring(iIndexOf + 1);
        return iIndexOf > 0 && isValidIPv4(str.substring(0, iIndexOf)) && (isValidIPv4(strSubstring) || isMaskValue(strSubstring, 32));
    }

    public static boolean isValidIPv6WithNetmask(String str) {
        int iIndexOf = str.indexOf("/");
        String strSubstring = str.substring(iIndexOf + 1);
        return iIndexOf > 0 && isValidIPv6(str.substring(0, iIndexOf)) && (isValidIPv6(strSubstring) || isMaskValue(strSubstring, 128));
    }

    private static boolean isMaskValue(String str, int i) throws NumberFormatException {
        try {
            int i2 = Integer.parseInt(str);
            return i2 >= 0 && i2 <= i;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isValidIPv6(String str) throws NumberFormatException {
        int iIndexOf;
        if (str.length() == 0) {
            return false;
        }
        int i = 0;
        String str2 = str + ":";
        boolean z = false;
        int i2 = 0;
        while (i2 < str2.length() && (iIndexOf = str2.indexOf(58, i2)) >= i2) {
            if (i == 8) {
                return false;
            }
            if (i2 != iIndexOf) {
                String strSubstring = str2.substring(i2, iIndexOf);
                if (iIndexOf != str2.length() - 1 || strSubstring.indexOf(46) <= 0) {
                    try {
                        int i3 = Integer.parseInt(str2.substring(i2, iIndexOf), 16);
                        if (i3 < 0 || i3 > 65535) {
                            return false;
                        }
                    } catch (NumberFormatException e) {
                        return false;
                    }
                } else {
                    if (!isValidIPv4(strSubstring)) {
                        return false;
                    }
                    i++;
                }
            } else {
                if (iIndexOf != 1 && iIndexOf != str2.length() - 1 && z) {
                    return false;
                }
                z = true;
            }
            i2 = iIndexOf + 1;
            i++;
        }
        return i == 8 || z;
    }
}
