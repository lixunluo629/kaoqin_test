package org.hyperic.sigar.win32;

import java.util.Collection;
import java.util.Vector;

/* loaded from: sigar-1.6.4.jar:org/hyperic/sigar/win32/MetaBase.class */
public class MetaBase extends Win32 {
    private static int IIS_MD_SERVER_BASE = 1000;
    private static int IIS_MD_HTTP_BASE = 2000;
    public static int MD_SERVER_COMMAND = IIS_MD_SERVER_BASE + 12;
    public static int MD_CONNECTION_TIMEOUT = IIS_MD_SERVER_BASE + 13;
    public static int MD_MAX_CONNECTIONS = IIS_MD_SERVER_BASE + 14;
    public static int MD_SERVER_COMMENT = IIS_MD_SERVER_BASE + 15;
    public static int MD_SERVER_STATE = IIS_MD_SERVER_BASE + 16;
    public static int MD_SERVER_AUTOSTART = IIS_MD_SERVER_BASE + 17;
    public static int MD_SERVER_SIZE = IIS_MD_SERVER_BASE + 18;
    public static int MD_SERVER_LISTEN_BACKLOG = IIS_MD_SERVER_BASE + 19;
    public static int MD_SERVER_LISTEN_TIMEOUT = IIS_MD_SERVER_BASE + 20;
    public static int MD_DOWNLEVEL_ADMIN_INSTANCE = IIS_MD_SERVER_BASE + 21;
    public static int MD_LEVELS_TO_SCAN = IIS_MD_SERVER_BASE + 22;
    public static int MD_SERVER_BINDINGS = IIS_MD_SERVER_BASE + 23;
    public static int MD_MAX_ENDPOINT_CONNECTIONS = IIS_MD_SERVER_BASE + 24;
    public static int MD_SERVER_CONFIGURATION_INFO = IIS_MD_SERVER_BASE + 27;
    public static int MD_IISADMIN_EXTENSIONS = IIS_MD_SERVER_BASE + 28;
    public static int MD_LOGFILEDIRECTORY = 4001;
    public static int MD_SECURE_BINDINGS = IIS_MD_HTTP_BASE + 21;
    private int m_handle;
    private long pIMeta = MetaBaseInit();

    private final native long MetaBaseInit();

    private final native void MetaBaseClose();

    private final native void MetaBaseRelease();

    private final native String MetaBaseEnumKey(int i);

    private final native void MetaBaseOpenSubKey(String str);

    private final native void MetaBaseOpenSubKeyAbs(String str);

    private final native int MetaBaseGetIntValue(int i);

    private final native String MetaBaseGetStringValue(int i);

    private final native String[] MetaBaseGetMultiStringValue(int i);

    public void close() {
        MetaBaseClose();
        MetaBaseRelease();
    }

    public void OpenSubKey(String subkey) {
        if (subkey.startsWith("/")) {
            MetaBaseOpenSubKeyAbs(subkey);
        } else {
            MetaBaseOpenSubKey(subkey);
        }
    }

    public int getIntValue(int datakey) throws Win32Exception {
        try {
            int iResult = MetaBaseGetIntValue(datakey);
            return iResult;
        } catch (Throwable th) {
            throw new Win32Exception("Error getting int value");
        }
    }

    public int getIntValue(int datakey, int defaultValue) {
        int iResult;
        try {
            iResult = getIntValue(datakey);
        } catch (Win32Exception e) {
            iResult = defaultValue;
        }
        return iResult;
    }

    public String getStringValue(int datakey) throws Win32Exception {
        String strResult = MetaBaseGetStringValue(datakey);
        if (strResult == null) {
            throw new Win32Exception("Error getting string value");
        }
        return strResult;
    }

    public String getStringValue(int datakey, String defaultValue) {
        String strResult;
        try {
            strResult = getStringValue(datakey);
        } catch (Win32Exception e) {
            strResult = defaultValue;
        }
        return strResult;
    }

    public String[] getMultiStringValue(int datakey) throws Win32Exception {
        String[] strResult = MetaBaseGetMultiStringValue(datakey);
        return strResult;
    }

    public String[] getSubKeyNames() {
        Collection coll = new Vector();
        int i = 0;
        while (true) {
            String strName = MetaBaseEnumKey(i);
            if (strName != null) {
                coll.add(strName);
                i++;
            } else {
                return (String[]) coll.toArray(new String[coll.size()]);
            }
        }
    }

    public static void main(String[] args) throws NumberFormatException {
        try {
            MetaBase mb = new MetaBase();
            mb.OpenSubKey("/LM/W3SVC");
            String logdir = mb.getStringValue(MD_LOGFILEDIRECTORY);
            System.out.println(new StringBuffer().append("Logdir: ").append(logdir).toString());
            String[] keys = mb.getSubKeyNames();
            System.out.println("Listing IIS Web Sites");
            for (String str : keys) {
                try {
                    int serverNum = Integer.parseInt(str);
                    MetaBase vhost = new MetaBase();
                    vhost.OpenSubKey(new StringBuffer().append("/LM/W3SVC").append("/").append(serverNum).toString());
                    String[] bindings = vhost.getMultiStringValue(MD_SERVER_BINDINGS);
                    String hostname = vhost.getStringValue(MD_SERVER_COMMENT);
                    System.out.println("");
                    System.out.println(new StringBuffer().append("Host: ").append(hostname).toString());
                    for (String str2 : bindings) {
                        System.out.println(new StringBuffer().append("Bindings: ").append(str2).toString());
                    }
                    vhost.close();
                } catch (NumberFormatException e) {
                }
            }
            mb.close();
        } catch (Win32Exception e2) {
            System.out.println("Unable to query MetaBase for IIS Web Sites");
        }
    }
}
