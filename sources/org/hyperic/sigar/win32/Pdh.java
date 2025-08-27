package org.hyperic.sigar.win32;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import org.hyperic.sigar.SigarLoader;

/* loaded from: sigar-1.6.4.jar:org/hyperic/sigar/win32/Pdh.class */
public class Pdh extends Win32 {
    public static final int VALID_DATA = 0;
    public static final int NO_INSTANCE = -2147481647;
    public static final int NO_COUNTER = -1073738823;
    public static final int NO_OBJECT = -1073738824;
    public static final int NO_MACHINE = -2147481648;
    public static final int BAD_COUNTERNAME = -1073738816;
    public static final String PERFLIB_KEY = "SOFTWARE\\Microsoft\\Windows NT\\CurrentVersion\\Perflib";
    public static final long PERF_TYPE_NUMBER = 0;
    public static final long PERF_TYPE_COUNTER = 1024;
    public static final long PERF_TYPE_TEXT = 2048;
    public static final long PERF_TYPE_ZERO = 3072;
    private long query;
    private String hostname;
    private static Map counters = null;
    private static final String DELIM = "\\";

    /* renamed from: org.hyperic.sigar.win32.Pdh$1, reason: invalid class name */
    /* loaded from: sigar-1.6.4.jar:org/hyperic/sigar/win32/Pdh$1.class */
    static class AnonymousClass1 {
    }

    public static final native int validate(String str);

    private static final native void pdhConnectMachine(String str) throws Win32Exception;

    private static final native long pdhOpenQuery() throws Win32Exception;

    private static final native void pdhCloseQuery(long j) throws Win32Exception;

    private static final native long pdhAddCounter(long j, String str) throws Win32Exception;

    private static final native void pdhRemoveCounter(long j) throws Win32Exception;

    private static final native double pdhGetValue(long j, long j2, boolean z) throws Win32Exception;

    private static final native String pdhGetDescription(long j) throws Win32Exception;

    private static final native long pdhGetCounterType(long j) throws Win32Exception;

    private static final native String[] pdhGetInstances(String str) throws Win32Exception;

    private static final native String[] pdhGetKeys(String str) throws Win32Exception;

    private static final native String[] pdhGetObjects() throws Win32Exception;

    private static final native String pdhLookupPerfName(int i) throws Win32Exception;

    private static final native int pdhLookupPerfIndex(String str) throws Win32Exception;

    static {
        if (SigarLoader.IS_WIN32 && !"false".equals(System.getProperty("sigar.pdh.enableTranslation"))) {
            try {
                enableTranslation();
            } catch (Exception e) {
                System.err.println(new StringBuffer().append("sigar.pdh.enableTranslation: ").append(e.getMessage()).toString());
            }
        }
    }

    public Pdh() throws Win32Exception {
        this.query = -1L;
        this.hostname = null;
        this.query = pdhOpenQuery();
    }

    public Pdh(String hostName) throws Win32Exception {
        this();
        this.hostname = hostName;
    }

    protected void finalize() throws Throwable {
        try {
            close();
            super.finalize();
        } catch (Throwable th) {
            super.finalize();
            throw th;
        }
    }

    public synchronized void close() throws Win32Exception {
        if (this.query != -1) {
            pdhCloseQuery(this.query);
            this.query = -1L;
        }
    }

    public static void enableTranslation() throws Win32Exception {
        if (counters != null || LocaleInfo.isEnglish()) {
            return;
        }
        counters = getEnglishPerflibCounterMap();
    }

    /* loaded from: sigar-1.6.4.jar:org/hyperic/sigar/win32/Pdh$PerflibCounterMap.class */
    private static class PerflibCounterMap extends ArrayList {
        private Map map;
        private String index;

        private PerflibCounterMap() {
            this.map = new HashMap();
            this.index = null;
        }

        PerflibCounterMap(AnonymousClass1 x0) {
            this();
        }

        @Override // java.util.ArrayList, java.util.AbstractList, java.util.AbstractCollection, java.util.Collection, java.util.List
        public boolean add(Object o) {
            int[] ix;
            if (this.index == null) {
                this.index = (String) o;
                return true;
            }
            String name = ((String) o).trim().toLowerCase();
            int[] ix2 = (int[]) this.map.get(name);
            if (ix2 == null) {
                ix = new int[1];
            } else {
                ix = new int[ix2.length + 1];
                System.arraycopy(ix2, 0, ix, 1, ix2.length);
            }
            ix[0] = Integer.parseInt(this.index);
            this.map.put(name, ix);
            this.index = null;
            return true;
        }
    }

    public static Map getEnglishPerflibCounterMap() throws Win32Exception {
        LocaleInfo locale = new LocaleInfo(9);
        return getPerflibCounterMap(locale);
    }

    public static Map getPerflibCounterMap(LocaleInfo locale) throws Win32Exception {
        String path = new StringBuffer().append("SOFTWARE\\Microsoft\\Windows NT\\CurrentVersion\\Perflib\\").append(locale.getPerflibLangId()).toString();
        RegistryKey key = RegistryKey.LocalMachine.openSubKey(path);
        PerflibCounterMap counters2 = new PerflibCounterMap(null);
        try {
            key.getMultiStringValue("Counter", counters2);
            key.close();
            return counters2.map;
        } catch (Throwable th) {
            key.close();
            throw th;
        }
    }

    public static String getCounterName(int index) throws Win32Exception {
        String name = pdhLookupPerfName(index).trim();
        return name;
    }

    public double getSingleValue(String path) throws Win32Exception {
        return getRawValue(path);
    }

    public double getRawValue(String path) throws Win32Exception {
        return getValue(path, false);
    }

    public double getFormattedValue(String path) throws Win32Exception {
        return getValue(path, true);
    }

    private static int[] getCounterIndex(String englishName) {
        if (counters == null) {
            return null;
        }
        return (int[]) counters.get(englishName.toLowerCase());
    }

    private static String getCounterName(String englishName) throws Win32Exception {
        int[] ix = getCounterIndex(englishName);
        if (ix == null) {
            return englishName;
        }
        String name = getCounterName(ix[0]);
        return name;
    }

    public static String translate(String path) throws Win32Exception {
        if (counters == null) {
            return path;
        }
        StringBuffer trans = new StringBuffer();
        StringTokenizer tok = new StringTokenizer(path, DELIM);
        int num = tok.countTokens();
        if (num == 3) {
            String hostname = tok.nextToken();
            trans.append(DELIM).append(DELIM).append(hostname);
        }
        String object = tok.nextToken();
        String instance = null;
        int ix = object.indexOf(40);
        if (ix != -1) {
            instance = object.substring(ix);
            object = object.substring(0, ix);
        }
        trans.append(DELIM).append(getCounterName(object));
        if (instance != null) {
            trans.append(instance);
        }
        String counter = tok.nextToken();
        trans.append(DELIM);
        int[] cix = getCounterIndex(counter);
        if (cix != null) {
            if (cix.length == 1) {
                counter = getCounterName(cix[0]);
            } else {
                int i = 0;
                while (true) {
                    if (i >= cix.length) {
                        break;
                    }
                    String name = getCounterName(cix[i]);
                    if (validate(new StringBuffer().append((Object) trans).append(name).toString()) != 0) {
                        i++;
                    } else {
                        counter = name;
                        break;
                    }
                }
            }
        }
        trans.append(counter);
        return trans.toString();
    }

    private double getValue(String path, boolean format) throws Win32Exception {
        if (this.hostname != null) {
            pdhConnectMachine(this.hostname);
        }
        long counter = pdhAddCounter(this.query, translate(path));
        try {
            double dPdhGetValue = pdhGetValue(this.query, counter, format);
            pdhRemoveCounter(counter);
            return dPdhGetValue;
        } catch (Throwable th) {
            pdhRemoveCounter(counter);
            throw th;
        }
    }

    public String getDescription(String path) throws Win32Exception {
        long counter = pdhAddCounter(this.query, translate(path));
        try {
            String strPdhGetDescription = pdhGetDescription(counter);
            pdhRemoveCounter(counter);
            return strPdhGetDescription;
        } catch (Throwable th) {
            pdhRemoveCounter(counter);
            throw th;
        }
    }

    public long getCounterType(String path) throws Win32Exception {
        long counter = pdhAddCounter(this.query, translate(path));
        try {
            long jPdhGetCounterType = pdhGetCounterType(counter);
            pdhRemoveCounter(counter);
            return jPdhGetCounterType;
        } catch (Throwable th) {
            pdhRemoveCounter(counter);
            throw th;
        }
    }

    /* loaded from: sigar-1.6.4.jar:org/hyperic/sigar/win32/Pdh$InstanceIndex.class */
    private static final class InstanceIndex {
        long index;

        private InstanceIndex() {
            this.index = 0L;
        }

        InstanceIndex(AnonymousClass1 x0) {
            this();
        }
    }

    public static String[] getInstances(String path) throws Win32Exception {
        String[] instances = pdhGetInstances(getCounterName(path));
        HashMap names = new HashMap(instances.length);
        for (int i = 0; i < instances.length; i++) {
            InstanceIndex ix = (InstanceIndex) names.get(instances[i]);
            if (ix == null) {
                names.put(instances[i], new InstanceIndex(null));
            } else {
                ix.index++;
                instances[i] = new StringBuffer().append(instances[i]).append("#").append(ix.index).toString();
            }
        }
        return instances;
    }

    public static String[] getKeys(String path) throws Win32Exception {
        return pdhGetKeys(getCounterName(path));
    }

    public static String[] getObjects() throws Win32Exception {
        return pdhGetObjects();
    }

    public static void main(String[] args) {
        String[] objects;
        String objectName = null;
        String partialName = null;
        boolean showValues = false;
        boolean showInstances = false;
        boolean showKeys = false;
        if (args.length > 0) {
            for (int i = 0; i < args.length; i++) {
                if (args[i].equals("-h") || args[i].equals("-help") || args[i].equals("--help")) {
                    System.out.println("Usage: Pdh [OPTION]");
                    System.out.println("Show information from the Windows PDH");
                    System.out.println("");
                    System.out.println("    --object=NAME    only print info on this object");
                    System.out.println("    --contains=NAME  only print info on objects that");
                    System.out.println("                     contain this substring");
                    System.out.println("-i, --instance       show instances [default=no]");
                    System.out.println("-k, --keys           show keys [default=no]");
                    System.out.println("-v, --values         include key values [default=no]");
                    System.out.println("-h, --help           display help and exit");
                    return;
                }
                if (args[i].equals("-v") || args[i].equals("--values")) {
                    showKeys = true;
                    showValues = true;
                } else if (args[i].equals("-i") || args[i].equals("--instances")) {
                    showInstances = true;
                } else if (args[i].equals("-k") || args[i].equals("--keys")) {
                    showKeys = true;
                } else if (args[i].startsWith("--contains=")) {
                    int idx = args[i].indexOf(SymbolConstants.EQUAL_SYMBOL);
                    partialName = args[i].substring(idx + 1);
                } else if (args[i].startsWith("--object=")) {
                    int idx2 = args[i].indexOf(SymbolConstants.EQUAL_SYMBOL);
                    objectName = args[i].substring(idx2 + 1);
                } else {
                    System.out.println(new StringBuffer().append("Unknown option: ").append(args[i]).toString());
                    System.out.println("Use --help for usage information");
                    return;
                }
            }
        }
        try {
            Pdh pdh = new Pdh();
            if (partialName != null) {
                List matching = new ArrayList();
                String[] allObjects = getObjects();
                for (int i2 = 0; i2 < allObjects.length; i2++) {
                    if (allObjects[i2].toUpperCase().indexOf(partialName.toUpperCase()) != -1) {
                        matching.add(allObjects[i2]);
                    }
                }
                objects = (String[]) matching.toArray(new String[0]);
            } else if (objectName != null) {
                objects = new String[]{objectName};
            } else {
                objects = getObjects();
            }
            for (int o = 0; o < objects.length; o++) {
                System.out.println(objects[o]);
                try {
                    String[] keys = getKeys(objects[o]);
                    int pad = getLongestKey(keys);
                    String[] instances = getInstances(objects[o]);
                    if (instances.length == 0) {
                        if (showKeys) {
                            for (int k = 0; k < keys.length; k++) {
                                if (showValues) {
                                    String query = new StringBuffer().append(DELIM).append(objects[o]).append(DELIM).append(keys[k]).toString();
                                    try {
                                        double val = pdh.getRawValue(query);
                                        String out = pad(keys[k], pad, ' ');
                                        System.out.println(new StringBuffer().append("  ").append(out).append(" = ").append(val).toString());
                                    } catch (Win32Exception e) {
                                        System.err.println(new StringBuffer().append("Unable to get value for  key=").append(query).append(" Reason: ").append(e.getMessage()).toString());
                                    }
                                } else {
                                    System.out.println(new StringBuffer().append("  ").append(keys[k]).toString());
                                }
                            }
                        }
                    } else if (showInstances) {
                        for (int i3 = 0; i3 < instances.length; i3++) {
                            System.out.println(new StringBuffer().append("  ").append(instances[i3]).toString());
                            if (showKeys) {
                                for (int k2 = 0; k2 < keys.length; k2++) {
                                    if (showValues) {
                                        String query2 = new StringBuffer().append(DELIM).append(objects[o]).append("(").append(instances[i3]).append(")").append(DELIM).append(keys[k2]).toString();
                                        try {
                                            double val2 = pdh.getRawValue(query2);
                                            String out2 = pad(keys[k2], pad, ' ');
                                            System.out.println(new StringBuffer().append("    ").append(out2).append(" = ").append(val2).toString());
                                        } catch (Win32Exception e2) {
                                            System.err.println(new StringBuffer().append("Unable to get value for key=").append(query2).append(" Reason: ").append(e2.getMessage()).toString());
                                        }
                                    } else {
                                        System.out.println(new StringBuffer().append("    ").append(keys[k2]).toString());
                                    }
                                }
                            }
                        }
                    }
                } catch (Win32Exception e3) {
                    System.err.println(new StringBuffer().append("Unable to get keys for object=").append(objects[o]).append(" Reason: ").append(e3.getMessage()).toString());
                }
            }
            pdh.close();
        } catch (Win32Exception e4) {
            System.err.println(new StringBuffer().append("Unable to dump PDH data: ").append(e4.getMessage()).toString());
        }
    }

    private static String pad(String value, int length, char ch2) {
        StringBuffer padder = new StringBuffer(value);
        if (value.length() < length) {
            for (int i = 0; i < length - value.length(); i++) {
                padder.append(ch2);
            }
        }
        return padder.toString();
    }

    private static int getLongestKey(String[] keys) {
        int longest = 0;
        for (String str : keys) {
            int len = str.length();
            if (len > longest) {
                longest = len;
            }
        }
        return longest;
    }
}
