package org.hyperic.sigar.win32;

import java.util.Collection;
import java.util.List;
import java.util.Vector;

/* loaded from: sigar-1.6.4.jar:org/hyperic/sigar/win32/RegistryKey.class */
public class RegistryKey extends Win32 {
    private static final int HKEY_CLASSES_ROOT = Integer.MIN_VALUE;
    private static final int HKEY_CURRENT_USER = -2147483647;
    private static final int HKEY_LOCAL_MACHINE = -2147483646;
    private static final int HKEY_USERS = -2147483645;
    private static final int HKEY_PERFORMANCE_DATA = -2147483644;
    private static final int HKEY_CURRENT_CONFIG = -2147483643;
    private static final int HKEY_DYN_DATA = -2147483642;
    public static final RegistryKey ClassesRoot = new RegistryKey(-2147483648L);
    public static final RegistryKey CurrentUser = new RegistryKey(-2147483647L);
    public static final RegistryKey LocalMachine = new RegistryKey(-2147483646);
    private long m_hkey;
    private String subkey;

    private static native int RegCloseKey(long j);

    private static native long RegCreateKey(long j, String str);

    private static native int RegDeleteKey(long j, String str);

    private static native int RegDeleteValue(long j, String str);

    private static native String RegEnumKey(long j, int i);

    private static native String RegEnumValueName(long j, int i);

    private static native int RegFlushKey(long j);

    private static native int RegLoadKey(long j, String str, String str2);

    private static native long RegOpenKey(long j, String str);

    private static native byte[] RegQueryBufferValue(long j, String str);

    private static native int RegQueryIntValue(long j, String str);

    private static native String RegQueryStringValue(long j, String str);

    private static native void RegQueryMultiStringValue(long j, String str, List list);

    private static native int RegSetIntValue(long j, String str, int i);

    private static native int RegSetStringValue(long j, String str, String str2);

    private RegistryKey() {
    }

    private RegistryKey(long hkey) {
        this.m_hkey = hkey;
    }

    public synchronized void close() {
        if (this.m_hkey != 0) {
            RegCloseKey(this.m_hkey);
            this.m_hkey = 0L;
        }
    }

    public RegistryKey createSubKey(String subkey) {
        return new RegistryKey(RegCreateKey(this.m_hkey, subkey));
    }

    public String getSubKeyName() {
        return this.subkey;
    }

    public RegistryKey createSubKey(String subkey, String value) throws Win32Exception {
        long hkey = RegCreateKey(this.m_hkey, subkey);
        if (hkey != 0) {
            RegistryKey keyResult = new RegistryKey(hkey);
            if (keyResult != null) {
                keyResult.setStringValue(null, value);
            }
            return keyResult;
        }
        throw new Win32Exception("Error creating subkey");
    }

    public RegistryKey createSubKey(String subkey, int value) throws Win32Exception {
        long hkey = RegCreateKey(this.m_hkey, subkey);
        if (hkey != 0) {
            RegistryKey keyResult = new RegistryKey(hkey);
            if (keyResult != null) {
                keyResult.setIntValue(null, value);
            }
            return keyResult;
        }
        throw new Win32Exception("Error creating subkey");
    }

    public void deleteSubKey(String subkey) {
        RegDeleteKey(this.m_hkey, subkey);
    }

    public void deleteSubKeyTree(String subkey) {
    }

    public void deleteValue(String name) {
        RegDeleteValue(this.m_hkey, name);
    }

    public void flush() {
        RegFlushKey(this.m_hkey);
    }

    public int getIntValue(String name) throws Win32Exception {
        try {
            int iResult = RegQueryIntValue(this.m_hkey, name);
            return iResult;
        } catch (Throwable th) {
            throw new Win32Exception("Error getting int value");
        }
    }

    public int getIntValue(String name, int defaultValue) {
        int iResult;
        try {
            iResult = getIntValue(name);
        } catch (Win32Exception e) {
            iResult = defaultValue;
        }
        return iResult;
    }

    public String getStringValue(String name) throws Win32Exception {
        String strResult = RegQueryStringValue(this.m_hkey, name);
        if (strResult == null) {
            throw new Win32Exception("Error getting string value");
        }
        return strResult;
    }

    public void getMultiStringValue(String name, List values) throws Win32Exception {
        RegQueryMultiStringValue(this.m_hkey, name, values);
    }

    public String getStringValue(String name, String defaultValue) {
        String strResult;
        try {
            strResult = getStringValue(name);
        } catch (Win32Exception e) {
            strResult = defaultValue;
        }
        return strResult;
    }

    public String[] getSubKeyNames() {
        Collection coll = new Vector();
        int i = 0;
        while (true) {
            String strName = RegEnumKey(this.m_hkey, i);
            if (strName != null) {
                coll.add(strName);
                i++;
            } else {
                return (String[]) coll.toArray(new String[coll.size()]);
            }
        }
    }

    public String[] getValueNames() {
        Collection coll = new Vector();
        int i = 0;
        while (true) {
            String strName = RegEnumValueName(this.m_hkey, i);
            if (strName != null) {
                coll.add(strName);
                i++;
            } else {
                return (String[]) coll.toArray(new String[coll.size()]);
            }
        }
    }

    public RegistryKey openSubKey(String subkey) throws Win32Exception {
        long hkey = RegOpenKey(this.m_hkey, subkey);
        if (hkey == 0) {
            throw new Win32Exception("Error opening subkey");
        }
        RegistryKey key = new RegistryKey(hkey);
        key.subkey = subkey;
        return key;
    }

    public void setIntValue(String name, int value) throws Win32Exception {
        int iResult = RegSetIntValue(this.m_hkey, name, value);
        if (iResult != 0) {
            throw new Win32Exception("Error setting int value");
        }
    }

    public void setStringValue(String name, String value) throws Win32Exception {
        int iResult = RegSetStringValue(this.m_hkey, name, value);
        if (iResult != 0) {
            throw new Win32Exception("Error setting string value");
        }
    }

    protected void finalize() {
        close();
    }
}
