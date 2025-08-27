package com.sun.jna;

import java.io.File;
import java.io.FilenameFilter;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

/* loaded from: jna-3.0.9.jar:com/sun/jna/NativeLibrary.class */
public class NativeLibrary {
    private long handle;
    private final String libraryName;
    private final String libraryPath;
    private final Map functions = new HashMap();
    private static final Map libraries = new HashMap();
    private static final Map searchPaths = Collections.synchronizedMap(new HashMap());
    private static final List librarySearchPath = new LinkedList();
    private static final List userSearchPath = new LinkedList();

    private static native long open(String str);

    private static native void close(long j);

    private static native long findSymbol(long j, String str);

    static {
        if (Native.POINTER_SIZE == 0) {
            throw new Error("Native library not initialized");
        }
        userSearchPath.addAll(initPaths("jna.library.path"));
        String webstartPath = Native.getWebStartLibraryPath("jnidispatch");
        if (webstartPath != null) {
            librarySearchPath.add(webstartPath);
        }
        if (System.getProperty("jna.platform.library.path") == null && !Platform.isWindows()) {
            String platformPath = "";
            String sep = "";
            String archPath = "";
            if (Platform.isLinux() || Platform.isSolaris() || Platform.isFreeBSD()) {
                archPath = new StringBuffer().append(Platform.isSolaris() ? "/" : "").append(Pointer.SIZE * 8).toString();
            }
            String[] paths = {new StringBuffer().append("/usr/lib").append(archPath).toString(), new StringBuffer().append("/lib").append(archPath).toString(), "/usr/lib", "/lib"};
            if (Platform.isLinux() && Pointer.SIZE == 8) {
                paths = new String[]{new StringBuffer().append("/usr/lib").append(archPath).toString(), new StringBuffer().append("/lib").append(archPath).toString()};
            }
            for (int i = 0; i < paths.length; i++) {
                File dir = new File(paths[i]);
                if (dir.exists() && dir.isDirectory()) {
                    platformPath = new StringBuffer().append(platformPath).append(sep).append(paths[i]).toString();
                    sep = File.pathSeparator;
                }
            }
            if (!"".equals(platformPath)) {
                System.setProperty("jna.platform.library.path", platformPath);
            }
        }
        librarySearchPath.addAll(initPaths("jna.platform.library.path"));
    }

    private NativeLibrary(String libraryName, String libraryPath, long handle) {
        this.libraryName = getLibraryName(libraryName);
        this.libraryPath = libraryPath;
        this.handle = handle;
        if (Platform.isWindows() && "kernel32".equals(this.libraryName.toLowerCase())) {
            synchronized (this.functions) {
                Function f = new Function(this, this, "GetLastError", 1) { // from class: com.sun.jna.NativeLibrary.1
                    private final NativeLibrary this$0;

                    {
                        this.this$0 = this;
                    }

                    @Override // com.sun.jna.Function
                    Object invoke(Object[] args, Class returnType) {
                        return new Integer(Native.getLastError());
                    }
                };
                this.functions.put("GetLastError", f);
            }
        }
    }

    private static NativeLibrary loadLibrary(String libraryName) throws Throwable {
        List searchPath = new LinkedList();
        String webstartPath = Native.getWebStartLibraryPath(libraryName);
        if (webstartPath != null) {
            searchPath.add(webstartPath);
        }
        List customPaths = (List) searchPaths.get(libraryName);
        if (customPaths != null) {
            synchronized (customPaths) {
                searchPath.addAll(0, customPaths);
            }
        }
        searchPath.addAll(userSearchPath);
        String libraryPath = findLibraryPath(libraryName, searchPath);
        long handle = 0;
        try {
            handle = open(libraryPath);
        } catch (UnsatisfiedLinkError e) {
            searchPath.addAll(librarySearchPath);
        }
        if (handle == 0) {
            try {
                libraryPath = findLibraryPath(libraryName, searchPath);
                handle = open(libraryPath);
            } catch (UnsatisfiedLinkError e2) {
                e = e2;
                if (Platform.isLinux()) {
                    libraryPath = matchLibrary(libraryName, searchPath);
                    if (libraryPath != null) {
                        try {
                            handle = open(libraryPath);
                        } catch (UnsatisfiedLinkError e22) {
                            e = e22;
                        }
                    }
                } else if (Platform.isMac() && !libraryName.endsWith(".dylib")) {
                    libraryPath = new StringBuffer().append("/System/Library/Frameworks/").append(libraryName).append(".framework/").append(libraryName).toString();
                    if (new File(libraryPath).exists()) {
                        try {
                            handle = open(libraryPath);
                        } catch (UnsatisfiedLinkError e23) {
                            e = e23;
                        }
                    }
                } else if (Platform.isWindows()) {
                    libraryPath = findLibraryPath(new StringBuffer().append("lib").append(libraryName).toString(), searchPath);
                    try {
                        handle = open(libraryPath);
                    } catch (UnsatisfiedLinkError e24) {
                        e = e24;
                    }
                }
                if (handle == 0) {
                    throw new UnsatisfiedLinkError(new StringBuffer().append("Unable to load library '").append(libraryName).append("': ").append(e.getMessage()).toString());
                }
            }
        }
        return new NativeLibrary(libraryName, libraryPath, handle);
    }

    private String getLibraryName(String libraryName) {
        String simplified = libraryName;
        String template = mapLibraryName("---");
        int prefixEnd = template.indexOf("---");
        if (prefixEnd > 0 && simplified.startsWith(template.substring(0, prefixEnd))) {
            simplified = simplified.substring(prefixEnd);
        }
        String suffix = template.substring(prefixEnd + "---".length());
        int suffixStart = simplified.indexOf(suffix);
        if (suffixStart != -1) {
            simplified = simplified.substring(0, suffixStart);
        }
        return simplified;
    }

    public static final NativeLibrary getInstance(String libraryName) {
        NativeLibrary nativeLibrary;
        if (libraryName == null) {
            throw new NullPointerException("Library name may not be null");
        }
        synchronized (libraries) {
            WeakReference ref = (WeakReference) libraries.get(libraryName);
            NativeLibrary library = ref != null ? (NativeLibrary) ref.get() : null;
            if (library == null) {
                library = loadLibrary(libraryName);
                WeakReference ref2 = new WeakReference(library);
                libraries.put(library.getName(), ref2);
                libraries.put(library.getFile().getAbsolutePath(), ref2);
                libraries.put(library.getFile().getName(), ref2);
            }
            nativeLibrary = library;
        }
        return nativeLibrary;
    }

    public static final void addSearchPath(String libraryName, String path) {
        synchronized (searchPaths) {
            List customPaths = (List) searchPaths.get(libraryName);
            if (customPaths == null) {
                customPaths = Collections.synchronizedList(new LinkedList());
                searchPaths.put(libraryName, customPaths);
            }
            customPaths.add(path);
        }
    }

    public Function getFunction(String functionName) {
        return getFunction(functionName, 0);
    }

    public Function getFunction(String functionName, int callingConvention) {
        Function function;
        if (functionName == null) {
            throw new NullPointerException("Function name may not be null");
        }
        synchronized (this.functions) {
            Function function2 = (Function) this.functions.get(functionName);
            if (function2 == null) {
                function2 = new Function(this, functionName, callingConvention);
                this.functions.put(functionName, function2);
            }
            function = function2;
        }
        return function;
    }

    public Pointer getGlobalVariableAddress(String symbolName) {
        try {
            return new Pointer(getSymbolAddress(symbolName));
        } catch (UnsatisfiedLinkError e) {
            throw new UnsatisfiedLinkError(new StringBuffer().append("Error looking up '").append(symbolName).append("': ").append(e.getMessage()).toString());
        }
    }

    long getSymbolAddress(String name) {
        if (this.handle == 0) {
            throw new UnsatisfiedLinkError("Library has been unloaded");
        }
        return findSymbol(this.handle, name);
    }

    public String toString() {
        return new StringBuffer().append("Native Library <").append(this.libraryPath).append("@").append(this.handle).append(">").toString();
    }

    public String getName() {
        return this.libraryName;
    }

    public File getFile() {
        return new File(this.libraryPath);
    }

    protected void finalize() {
        dispose();
    }

    public void dispose() {
        synchronized (libraries) {
            libraries.remove(getName());
            libraries.remove(getFile().getAbsolutePath());
            libraries.remove(getFile().getName());
        }
        synchronized (this) {
            if (this.handle != 0) {
                close(this.handle);
                this.handle = 0L;
            }
        }
    }

    private static List initPaths(String key) {
        String value = System.getProperty(key, "");
        if ("".equals(value)) {
            return Collections.EMPTY_LIST;
        }
        StringTokenizer st = new StringTokenizer(value, File.pathSeparator);
        List list = new ArrayList();
        while (st.hasMoreTokens()) {
            String path = st.nextToken();
            if (!"".equals(path)) {
                list.add(path);
            }
        }
        return list;
    }

    private static String findLibraryPath(String libName, List searchPath) {
        if (new File(libName).isAbsolute()) {
            return libName;
        }
        String name = mapLibraryName(libName);
        Iterator it = searchPath.iterator();
        while (it.hasNext()) {
            File file = new File(new File((String) it.next()), name);
            if (file.exists()) {
                return file.getAbsolutePath();
            }
        }
        return name;
    }

    private static String mapLibraryName(String libName) {
        if (Platform.isMac()) {
            if (libName.startsWith("lib") && (libName.endsWith(".dylib") || libName.endsWith(".jnilib"))) {
                return libName;
            }
            String name = System.mapLibraryName(libName);
            if (name.endsWith(".jnilib")) {
                return new StringBuffer().append(name.substring(0, name.lastIndexOf(".jnilib"))).append(".dylib").toString();
            }
            return name;
        }
        if (Platform.isLinux() && isVersionedName(libName)) {
            return libName;
        }
        return System.mapLibraryName(libName);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean isVersionedName(String name) {
        int so;
        if (name.startsWith("lib") && (so = name.lastIndexOf(".so.")) != -1 && so + 4 < name.length()) {
            for (int i = so + 4; i < name.length(); i++) {
                char ch2 = name.charAt(i);
                if (!Character.isDigit(ch2) && ch2 != '.') {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    static String matchLibrary(String libName, List searchPath) {
        File lib = new File(libName);
        if (lib.isAbsolute()) {
            searchPath = Arrays.asList(lib.getParent());
        }
        FilenameFilter filter = new FilenameFilter(libName) { // from class: com.sun.jna.NativeLibrary.2
            private final String val$libName;

            {
                this.val$libName = libName;
            }

            @Override // java.io.FilenameFilter
            public boolean accept(File dir, String filename) {
                return (filename.startsWith(new StringBuffer().append("lib").append(this.val$libName).append(".so").toString()) || (filename.startsWith(new StringBuffer().append(this.val$libName).append(".so").toString()) && this.val$libName.startsWith("lib"))) && NativeLibrary.isVersionedName(filename);
            }
        };
        List matches = new LinkedList();
        Iterator it = searchPath.iterator();
        while (it.hasNext()) {
            File[] files = new File((String) it.next()).listFiles(filter);
            if (files != null && files.length > 0) {
                matches.addAll(Arrays.asList(files));
            }
        }
        double bestVersion = -1.0d;
        String bestMatch = null;
        Iterator it2 = matches.iterator();
        while (it2.hasNext()) {
            String path = ((File) it2.next()).getAbsolutePath();
            String ver = path.substring(path.lastIndexOf(".so.") + 4);
            double version = parseVersion(ver);
            if (version > bestVersion) {
                bestVersion = version;
                bestMatch = path;
            }
        }
        return bestMatch;
    }

    static double parseVersion(String ver) {
        String num;
        double v = 0.0d;
        double divisor = 1.0d;
        int dot = ver.indexOf(".");
        while (ver != null) {
            if (dot != -1) {
                num = ver.substring(0, dot);
                ver = ver.substring(dot + 1);
                dot = ver.indexOf(".");
            } else {
                num = ver;
                ver = null;
            }
            try {
                v += Integer.parseInt(num) / divisor;
                divisor *= 100.0d;
            } catch (NumberFormatException e) {
                return 0.0d;
            }
        }
        return v;
    }
}
