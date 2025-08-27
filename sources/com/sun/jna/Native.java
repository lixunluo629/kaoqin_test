package com.sun.jna;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import com.sun.jna.Library;
import java.awt.Component;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.Window;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import org.apache.xmlbeans.XmlOptions;

/* loaded from: jna-3.0.9.jar:com/sun/jna/Native.class */
public final class Native {
    private static Map typeMappers = new WeakHashMap();
    private static Map alignments = new WeakHashMap();
    private static Map options = new WeakHashMap();
    private static Map libraries = new WeakHashMap();
    public static final int POINTER_SIZE;
    public static final int LONG_SIZE;
    public static final int WCHAR_SIZE;
    private static final ThreadLocal lastError;
    static Class class$com$sun$jna$Callback;
    static Class class$com$sun$jna$Library;
    static Class class$com$sun$jna$TypeMapper;
    static Class class$com$sun$jna$Native;
    static Class class$java$lang$String;
    static Class class$java$lang$ClassLoader;
    static Class class$com$sun$jna$NativeMapped;
    static Class class$java$lang$Boolean;
    static Class class$java$lang$Byte;
    static Class class$java$lang$Short;
    static Class class$java$lang$Character;
    static Class class$java$lang$Integer;
    static Class class$java$lang$Long;
    static Class class$java$lang$Float;
    static Class class$java$lang$Double;
    static Class class$com$sun$jna$Structure;
    static Class class$com$sun$jna$Structure$ByValue;
    static Class class$com$sun$jna$Pointer;
    static Class class$java$nio$Buffer;
    static Class class$com$sun$jna$WString;

    private static native void initIDs();

    public static native synchronized void setProtected(boolean z);

    public static native synchronized boolean isProtected();

    public static native synchronized void setPreserveLastError(boolean z);

    public static native synchronized boolean getPreserveLastError();

    private static native long getWindowHandle0(Component component);

    public static native Pointer getDirectBufferPointer(Buffer buffer);

    private static native int pointerSize();

    private static native int longSize();

    private static native int wideCharSize();

    private static native String getNativeVersion();

    private static native String getAPIChecksum();

    public static native void setLastError(int i);

    static {
        loadNativeLibrary();
        POINTER_SIZE = pointerSize();
        LONG_SIZE = longSize();
        WCHAR_SIZE = wideCharSize();
        initIDs();
        if (Boolean.getBoolean("jna.protected")) {
            setProtected(true);
        }
        lastError = new ThreadLocal() { // from class: com.sun.jna.Native.1
            @Override // java.lang.ThreadLocal
            protected synchronized Object initialValue() {
                return new Integer(0);
            }
        };
    }

    private Native() {
    }

    public static long getWindowID(Window w) throws HeadlessException {
        return getComponentID(w);
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: java.awt.HeadlessException */
    public static long getComponentID(Component c) throws HeadlessException {
        if (GraphicsEnvironment.isHeadless()) {
            throw new HeadlessException("No native windows when headless");
        }
        if (c.isLightweight()) {
            throw new IllegalArgumentException("Component must be heavyweight");
        }
        if (!c.isDisplayable()) {
            throw new IllegalStateException("Component must be displayable");
        }
        if (Platform.isX11() && System.getProperty("java.version").startsWith(XmlOptions.GENERATE_JAVA_14) && !c.isVisible()) {
            throw new IllegalStateException("Component must be visible");
        }
        return getWindowHandle0(c);
    }

    public static Pointer getWindowPointer(Window w) throws HeadlessException {
        return getComponentPointer(w);
    }

    public static Pointer getComponentPointer(Component c) throws HeadlessException {
        return new Pointer(getComponentID(c));
    }

    public static Pointer getByteBufferPointer(ByteBuffer b) {
        return getDirectBufferPointer(b);
    }

    public static String toString(byte[] buf) {
        String encoding = System.getProperty("jna.encoding");
        String s = null;
        if (encoding != null) {
            try {
                s = new String(buf, encoding);
            } catch (UnsupportedEncodingException e) {
            }
        }
        if (s == null) {
            s = new String(buf);
        }
        int term = s.indexOf(0);
        if (term != -1) {
            s = s.substring(0, term);
        }
        return s;
    }

    public static String toString(char[] buf) {
        String s = new String(buf);
        int term = s.indexOf(0);
        if (term != -1) {
            s = s.substring(0, term);
        }
        return s;
    }

    public static Object loadLibrary(String name, Class interfaceClass) {
        return loadLibrary(name, interfaceClass, Collections.EMPTY_MAP);
    }

    public static Object loadLibrary(String name, Class interfaceClass, Map libOptions) {
        Library.Handler handler = new Library.Handler(name, interfaceClass, libOptions);
        ClassLoader loader = interfaceClass.getClassLoader();
        Library proxy = (Library) Proxy.newProxyInstance(loader, new Class[]{interfaceClass}, handler);
        synchronized (libraries) {
            if (!libOptions.isEmpty()) {
                options.put(interfaceClass, libOptions);
            }
            if (libOptions.containsKey(Library.OPTION_TYPE_MAPPER)) {
                typeMappers.put(interfaceClass, libOptions.get(Library.OPTION_TYPE_MAPPER));
            }
            if (libOptions.containsKey(Library.OPTION_STRUCTURE_ALIGNMENT)) {
                alignments.put(interfaceClass, libOptions.get(Library.OPTION_STRUCTURE_ALIGNMENT));
            }
            libraries.put(interfaceClass, new WeakReference(proxy));
        }
        return proxy;
    }

    /* JADX WARN: Code restructure failed: missing block: B:13:0x0033, code lost:
    
        com.sun.jna.Native.libraries.put(r7, new java.lang.ref.WeakReference(r0.get(null)));
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private static void loadLibraryInstance(java.lang.Class r7) throws java.lang.SecurityException {
        /*
            r0 = r7
            if (r0 == 0) goto L7f
            java.util.Map r0 = com.sun.jna.Native.libraries
            r1 = r7
            boolean r0 = r0.containsKey(r1)
            if (r0 != 0) goto L7f
            r0 = r7
            java.lang.reflect.Field[] r0 = r0.getFields()     // Catch: java.lang.Exception -> L55
            r8 = r0
            r0 = 0
            r9 = r0
        L17:
            r0 = r9
            r1 = r8
            int r1 = r1.length     // Catch: java.lang.Exception -> L55
            if (r0 >= r1) goto L52
            r0 = r8
            r1 = r9
            r0 = r0[r1]     // Catch: java.lang.Exception -> L55
            r10 = r0
            r0 = r10
            java.lang.Class r0 = r0.getType()     // Catch: java.lang.Exception -> L55
            r1 = r7
            if (r0 != r1) goto L4c
            r0 = r10
            int r0 = r0.getModifiers()     // Catch: java.lang.Exception -> L55
            boolean r0 = java.lang.reflect.Modifier.isStatic(r0)     // Catch: java.lang.Exception -> L55
            if (r0 == 0) goto L4c
            java.util.Map r0 = com.sun.jna.Native.libraries     // Catch: java.lang.Exception -> L55
            r1 = r7
            java.lang.ref.WeakReference r2 = new java.lang.ref.WeakReference     // Catch: java.lang.Exception -> L55
            r3 = r2
            r4 = r10
            r5 = 0
            java.lang.Object r4 = r4.get(r5)     // Catch: java.lang.Exception -> L55
            r3.<init>(r4)     // Catch: java.lang.Exception -> L55
            java.lang.Object r0 = r0.put(r1, r2)     // Catch: java.lang.Exception -> L55
            goto L52
        L4c:
            int r9 = r9 + 1
            goto L17
        L52:
            goto L7f
        L55:
            r8 = move-exception
            java.lang.IllegalArgumentException r0 = new java.lang.IllegalArgumentException
            r1 = r0
            java.lang.StringBuffer r2 = new java.lang.StringBuffer
            r3 = r2
            r3.<init>()
            java.lang.String r3 = "Could not access instance of "
            java.lang.StringBuffer r2 = r2.append(r3)
            r3 = r7
            java.lang.StringBuffer r2 = r2.append(r3)
            java.lang.String r3 = " ("
            java.lang.StringBuffer r2 = r2.append(r3)
            r3 = r8
            java.lang.StringBuffer r2 = r2.append(r3)
            java.lang.String r3 = ")"
            java.lang.StringBuffer r2 = r2.append(r3)
            java.lang.String r2 = r2.toString()
            r1.<init>(r2)
            throw r0
        L7f:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sun.jna.Native.loadLibraryInstance(java.lang.Class):void");
    }

    static Class findCallbackClass(Class type) throws Throwable {
        Class clsClass$;
        Class clsClass$2;
        Class clsClass$3;
        if (class$com$sun$jna$Callback == null) {
            clsClass$ = class$("com.sun.jna.Callback");
            class$com$sun$jna$Callback = clsClass$;
        } else {
            clsClass$ = class$com$sun$jna$Callback;
        }
        if (!clsClass$.isAssignableFrom(type)) {
            throw new IllegalArgumentException(new StringBuffer().append(type.getName()).append(" is not derived from com.sun.jna.Callback").toString());
        }
        if (type.isInterface()) {
            return type;
        }
        Class[] ifaces = type.getInterfaces();
        int i = 0;
        while (true) {
            if (i >= ifaces.length) {
                break;
            }
            if (class$com$sun$jna$Callback == null) {
                clsClass$3 = class$("com.sun.jna.Callback");
                class$com$sun$jna$Callback = clsClass$3;
            } else {
                clsClass$3 = class$com$sun$jna$Callback;
            }
            if (!clsClass$3.isAssignableFrom(ifaces[i])) {
                i++;
            } else if (ifaces[i].getMethods().length == 1) {
                return ifaces[i];
            }
        }
        if (class$com$sun$jna$Callback == null) {
            clsClass$2 = class$("com.sun.jna.Callback");
            class$com$sun$jna$Callback = clsClass$2;
        } else {
            clsClass$2 = class$com$sun$jna$Callback;
        }
        if (clsClass$2.isAssignableFrom(type.getSuperclass())) {
            return findCallbackClass(type.getSuperclass());
        }
        return type;
    }

    static Class class$(String x0) throws Throwable {
        try {
            return Class.forName(x0);
        } catch (ClassNotFoundException x1) {
            throw new NoClassDefFoundError().initCause(x1);
        }
    }

    static Class findEnclosingLibraryClass(Class cls) throws Throwable {
        Class clsClass$;
        Class clsClass$2;
        if (cls == null) {
            return null;
        }
        if (class$com$sun$jna$Library == null) {
            clsClass$ = class$("com.sun.jna.Library");
            class$com$sun$jna$Library = clsClass$;
        } else {
            clsClass$ = class$com$sun$jna$Library;
        }
        if (clsClass$.isAssignableFrom(cls)) {
            return cls;
        }
        if (class$com$sun$jna$Callback == null) {
            clsClass$2 = class$("com.sun.jna.Callback");
            class$com$sun$jna$Callback = clsClass$2;
        } else {
            clsClass$2 = class$com$sun$jna$Callback;
        }
        if (clsClass$2.isAssignableFrom(cls)) {
            cls = findCallbackClass(cls);
        }
        Class fromDeclaring = findEnclosingLibraryClass(cls.getDeclaringClass());
        if (fromDeclaring != null) {
            return fromDeclaring;
        }
        return findEnclosingLibraryClass(cls.getSuperclass());
    }

    public static Map getLibraryOptions(Class type) {
        Map map;
        synchronized (libraries) {
            Class interfaceClass = findEnclosingLibraryClass(type);
            if (interfaceClass != null) {
                loadLibraryInstance(interfaceClass);
            } else {
                interfaceClass = type;
            }
            if (!options.containsKey(interfaceClass)) {
                try {
                    Field field = interfaceClass.getField("OPTIONS");
                    field.setAccessible(true);
                    options.put(interfaceClass, field.get(null));
                } catch (NoSuchFieldException e) {
                } catch (Exception e2) {
                    throw new IllegalArgumentException(new StringBuffer().append("OPTIONS must be a public field of type java.util.Map (").append(e2).append("): ").append(interfaceClass).toString());
                }
            }
            map = (Map) options.get(interfaceClass);
        }
        return map;
    }

    public static TypeMapper getTypeMapper(Class cls) {
        Class clsClass$;
        TypeMapper typeMapper;
        synchronized (libraries) {
            Class interfaceClass = findEnclosingLibraryClass(cls);
            if (interfaceClass != null) {
                loadLibraryInstance(interfaceClass);
            } else {
                interfaceClass = cls;
            }
            if (!typeMappers.containsKey(interfaceClass)) {
                try {
                    Field field = interfaceClass.getField("TYPE_MAPPER");
                    field.setAccessible(true);
                    typeMappers.put(interfaceClass, field.get(null));
                } catch (NoSuchFieldException e) {
                    Map options2 = getLibraryOptions(cls);
                    if (options2 != null && options2.containsKey(Library.OPTION_TYPE_MAPPER)) {
                        typeMappers.put(interfaceClass, options2.get(Library.OPTION_TYPE_MAPPER));
                    }
                } catch (Exception e2) {
                    StringBuffer stringBufferAppend = new StringBuffer().append("TYPE_MAPPER must be a public field of type ");
                    if (class$com$sun$jna$TypeMapper == null) {
                        clsClass$ = class$("com.sun.jna.TypeMapper");
                        class$com$sun$jna$TypeMapper = clsClass$;
                    } else {
                        clsClass$ = class$com$sun$jna$TypeMapper;
                    }
                    throw new IllegalArgumentException(stringBufferAppend.append(clsClass$.getName()).append(" (").append(e2).append("): ").append(interfaceClass).toString());
                }
            }
            typeMapper = (TypeMapper) typeMappers.get(interfaceClass);
        }
        return typeMapper;
    }

    public static int getStructureAlignment(Class cls) {
        int iIntValue;
        synchronized (libraries) {
            Class interfaceClass = findEnclosingLibraryClass(cls);
            if (interfaceClass != null) {
                loadLibraryInstance(interfaceClass);
            } else {
                interfaceClass = cls;
            }
            if (!alignments.containsKey(interfaceClass)) {
                try {
                    Field field = interfaceClass.getField("STRUCTURE_ALIGNMENT");
                    field.setAccessible(true);
                    alignments.put(interfaceClass, field.get(null));
                } catch (NoSuchFieldException e) {
                    Map options2 = getLibraryOptions(interfaceClass);
                    if (options2 != null && options2.containsKey(Library.OPTION_STRUCTURE_ALIGNMENT)) {
                        alignments.put(interfaceClass, options2.get(Library.OPTION_STRUCTURE_ALIGNMENT));
                    }
                } catch (Exception e2) {
                    throw new IllegalArgumentException(new StringBuffer().append("STRUCTURE_ALIGNMENT must be a public field of type int (").append(e2).append("): ").append(interfaceClass).toString());
                }
            }
            Integer value = (Integer) alignments.get(interfaceClass);
            iIntValue = value != null ? value.intValue() : 0;
        }
        return iIntValue;
    }

    static byte[] getBytes(String s) {
        String encoding = System.getProperty("jna.encoding");
        if (encoding != null) {
            try {
                return s.getBytes(encoding);
            } catch (UnsupportedEncodingException e) {
            }
        }
        return s.getBytes();
    }

    public static byte[] toByteArray(String s) {
        byte[] bytes = getBytes(s);
        byte[] buf = new byte[bytes.length + 1];
        System.arraycopy(bytes, 0, buf, 0, bytes.length);
        return buf;
    }

    public static char[] toCharArray(String s) {
        char[] chars = s.toCharArray();
        char[] buf = new char[chars.length + 1];
        System.arraycopy(chars, 0, buf, 0, chars.length);
        return buf;
    }

    private static String getNativeLibraryResourcePath() {
        String osPrefix;
        String arch = System.getProperty("os.arch").toLowerCase();
        if (Platform.isWindows()) {
            osPrefix = new StringBuffer().append("win32-").append(arch).toString();
        } else if (Platform.isMac()) {
            osPrefix = "darwin";
        } else if (Platform.isLinux()) {
            if ("x86".equals(arch)) {
                arch = "i386";
            } else if ("x86_64".equals(arch)) {
                arch = "amd64";
            }
            osPrefix = new StringBuffer().append("linux-").append(arch).toString();
        } else if (Platform.isSolaris()) {
            osPrefix = new StringBuffer().append("sunos-").append(arch).toString();
        } else {
            String osPrefix2 = System.getProperty("os.name").toLowerCase();
            int space = osPrefix2.indexOf(SymbolConstants.SPACE_SYMBOL);
            if (space != -1) {
                osPrefix2 = osPrefix2.substring(0, space);
            }
            osPrefix = new StringBuffer().append(osPrefix2).append("-").append(arch).toString();
        }
        return new StringBuffer().append("/com/sun/jna/").append(osPrefix).toString();
    }

    private static void loadNativeLibrary() throws Throwable {
        String orig;
        String ext;
        String bootPath = System.getProperty("jna.boot.library.path");
        if (bootPath != null) {
            String[] dirs = bootPath.split(File.pathSeparator);
            for (String str : dirs) {
                String path = new File(new File(str), System.mapLibraryName("jnidispatch")).getAbsolutePath();
                try {
                    System.load(path);
                    return;
                } catch (UnsatisfiedLinkError e) {
                    if (Platform.isMac()) {
                        if (path.endsWith("dylib")) {
                            orig = "dylib";
                            ext = "jnilib";
                        } else {
                            orig = "jnilib";
                            ext = "dylib";
                        }
                        try {
                            System.load(new StringBuffer().append(path.substring(0, path.lastIndexOf(orig))).append(ext).toString());
                            return;
                        } catch (UnsatisfiedLinkError e2) {
                        }
                    }
                }
            }
        }
        try {
            System.loadLibrary("jnidispatch");
        } catch (UnsatisfiedLinkError e3) {
            loadNativeLibraryFromJar();
        }
    }

    private static void loadNativeLibraryFromJar() throws Throwable {
        Class clsClass$;
        Class clsClass$2;
        File lib;
        Class clsClass$3;
        String libname = System.mapLibraryName("jnidispatch");
        String resourceName = new StringBuffer().append(getNativeLibraryResourcePath()).append("/").append(libname).toString();
        if (class$com$sun$jna$Native == null) {
            clsClass$ = class$("com.sun.jna.Native");
            class$com$sun$jna$Native = clsClass$;
        } else {
            clsClass$ = class$com$sun$jna$Native;
        }
        URL url = clsClass$.getResource(resourceName);
        if (url == null && Platform.isMac() && resourceName.endsWith(".dylib")) {
            resourceName = new StringBuffer().append(resourceName.substring(0, resourceName.lastIndexOf(".dylib"))).append(".jnilib").toString();
            if (class$com$sun$jna$Native == null) {
                clsClass$3 = class$("com.sun.jna.Native");
                class$com$sun$jna$Native = clsClass$3;
            } else {
                clsClass$3 = class$com$sun$jna$Native;
            }
            url = clsClass$3.getResource(resourceName);
        }
        if (url == null) {
            throw new UnsatisfiedLinkError(new StringBuffer().append("jnidispatch (").append(resourceName).append(") not found in resource path").toString());
        }
        if (url.getProtocol().toLowerCase().equals("file")) {
            lib = new File(URLDecoder.decode(url.getPath()));
        } else {
            if (class$com$sun$jna$Native == null) {
                clsClass$2 = class$("com.sun.jna.Native");
                class$com$sun$jna$Native = clsClass$2;
            } else {
                clsClass$2 = class$com$sun$jna$Native;
            }
            InputStream is = clsClass$2.getResourceAsStream(resourceName);
            if (is == null) {
                throw new Error("Can't obtain jnidispatch InputStream");
            }
            FileOutputStream fos = null;
            try {
                try {
                    lib = File.createTempFile("jna", null);
                    lib.deleteOnExit();
                    if (Platform.deleteNativeLibraryAfterVMExit()) {
                        Runtime.getRuntime().addShutdownHook(new DeleteNativeLibrary(lib));
                    }
                    fos = new FileOutputStream(lib);
                    byte[] buf = new byte[1024];
                    while (true) {
                        int count = is.read(buf, 0, buf.length);
                        if (count <= 0) {
                            break;
                        } else {
                            fos.write(buf, 0, count);
                        }
                    }
                    try {
                        is.close();
                    } catch (IOException e) {
                    }
                    if (fos != null) {
                        try {
                            fos.close();
                        } catch (IOException e2) {
                        }
                    }
                } catch (IOException e3) {
                    throw new Error(new StringBuffer().append("Failed to create temporary file for jnidispatch library: ").append(e3).toString());
                }
            } catch (Throwable th) {
                try {
                    is.close();
                } catch (IOException e4) {
                }
                if (fos != null) {
                    try {
                        fos.close();
                    } catch (IOException e5) {
                    }
                }
                throw th;
            }
        }
        System.load(lib.getAbsolutePath());
    }

    public static int getLastError() {
        return ((Integer) lastError.get()).intValue();
    }

    static void updateLastError(int e) {
        lastError.set(new Integer(e));
    }

    public static Library synchronizedLibrary(Library library) throws IllegalArgumentException {
        Class cls = library.getClass();
        if (!Proxy.isProxyClass(cls)) {
            throw new IllegalArgumentException("Library must be a proxy class");
        }
        InvocationHandler ih = Proxy.getInvocationHandler(library);
        if (!(ih instanceof Library.Handler)) {
            throw new IllegalArgumentException(new StringBuffer().append("Unrecognized proxy handler: ").append(ih).toString());
        }
        Library.Handler handler = (Library.Handler) ih;
        InvocationHandler newHandler = new InvocationHandler(handler, library) { // from class: com.sun.jna.Native.2
            private final Library.Handler val$handler;
            private final Library val$library;

            {
                this.val$handler = handler;
                this.val$library = library;
            }

            @Override // java.lang.reflect.InvocationHandler
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                Object objInvoke;
                synchronized (this.val$handler.getNativeLibrary()) {
                    objInvoke = this.val$handler.invoke(this.val$library, method, args);
                }
                return objInvoke;
            }
        };
        return (Library) Proxy.newProxyInstance(cls.getClassLoader(), cls.getInterfaces(), newHandler);
    }

    public static String getWebStartLibraryPath(String libName) throws Throwable {
        Class clsClass$;
        Class clsClass$2;
        Class<?> clsClass$3;
        if (System.getProperty("javawebstart.version") == null) {
            return null;
        }
        try {
            if (class$com$sun$jna$Native == null) {
                clsClass$ = class$("com.sun.jna.Native");
                class$com$sun$jna$Native = clsClass$;
            } else {
                clsClass$ = class$com$sun$jna$Native;
            }
            ClassLoader cl = clsClass$.getClassLoader();
            if (class$java$lang$ClassLoader == null) {
                clsClass$2 = class$("java.lang.ClassLoader");
                class$java$lang$ClassLoader = clsClass$2;
            } else {
                clsClass$2 = class$java$lang$ClassLoader;
            }
            Class<?>[] clsArr = new Class[1];
            if (class$java$lang$String == null) {
                clsClass$3 = class$("java.lang.String");
                class$java$lang$String = clsClass$3;
            } else {
                clsClass$3 = class$java$lang$String;
            }
            clsArr[0] = clsClass$3;
            Method m = clsClass$2.getDeclaredMethod("findLibrary", clsArr);
            m.setAccessible(true);
            String libpath = (String) m.invoke(cl, libName);
            if (libpath != null) {
                return new File(libpath).getParent();
            }
            String msg = new StringBuffer().append("Library '").append(libName).append("' was not found by class loader ").append(cl).toString();
            throw new IllegalArgumentException(msg);
        } catch (Exception e) {
            return null;
        }
    }

    /* loaded from: jna-3.0.9.jar:com/sun/jna/Native$DeleteNativeLibrary.class */
    public static class DeleteNativeLibrary extends Thread {
        private File file;

        public DeleteNativeLibrary(File file) {
            this.file = file;
        }

        private boolean unload(String path) throws Throwable {
            Class clsClass$;
            try {
                ClassLoader cl = getClass().getClassLoader();
                if (Native.class$java$lang$ClassLoader == null) {
                    clsClass$ = Native.class$("java.lang.ClassLoader");
                    Native.class$java$lang$ClassLoader = clsClass$;
                } else {
                    clsClass$ = Native.class$java$lang$ClassLoader;
                }
                Field f = clsClass$.getDeclaredField("nativeLibraries");
                f.setAccessible(true);
                List libs = (List) f.get(cl);
                for (Object lib : libs) {
                    Field f2 = lib.getClass().getDeclaredField("name");
                    f2.setAccessible(true);
                    String name = (String) f2.get(lib);
                    if (name.equals(path)) {
                        Method m = lib.getClass().getDeclaredMethod("finalize", new Class[0]);
                        m.setAccessible(true);
                        m.invoke(lib, new Object[0]);
                        return true;
                    }
                }
                return false;
            } catch (Exception e) {
                return false;
            }
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() throws IOException {
            if (!unload(this.file.getAbsolutePath()) || !this.file.delete()) {
                try {
                    Runtime.getRuntime().exec(new String[]{new StringBuffer().append(System.getProperty("java.home")).append("/bin/java").toString(), "-cp", System.getProperty("java.class.path"), getClass().getName(), this.file.getAbsolutePath()});
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        public static void main(String[] args) throws InterruptedException {
            if (args.length == 1) {
                File file = new File(args[0]);
                if (file.exists()) {
                    long start = System.currentTimeMillis();
                    while (true) {
                        if (file.delete() || !file.exists()) {
                            break;
                        }
                        try {
                            Thread.sleep(10L);
                        } catch (InterruptedException e) {
                        }
                        if (System.currentTimeMillis() - start > 5000) {
                            System.err.println(new StringBuffer().append("Could not remove temp file: ").append(file.getAbsolutePath()).toString());
                            break;
                        }
                    }
                }
            }
            System.exit(0);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static int getNativeSize(Class cls) throws Throwable {
        Class clsClass$;
        Class clsClass$2;
        Class clsClass$3;
        Class clsClass$4;
        Class clsClass$5;
        Class clsClass$6;
        Class clsClass$7;
        Class clsClass$8;
        Class clsClass$9;
        Class cls2;
        Class cls3;
        Class cls4;
        Class cls5;
        Class clsClass$10;
        Class clsClass$11;
        Class cls6;
        if (class$com$sun$jna$NativeMapped == null) {
            clsClass$ = class$("com.sun.jna.NativeMapped");
            class$com$sun$jna$NativeMapped = clsClass$;
        } else {
            clsClass$ = class$com$sun$jna$NativeMapped;
        }
        if (clsClass$.isAssignableFrom(cls)) {
            cls = NativeMappedConverter.getInstance(cls).nativeType();
        }
        if (cls == Boolean.TYPE) {
            return 4;
        }
        Class cls7 = cls;
        if (class$java$lang$Boolean == null) {
            clsClass$2 = class$("java.lang.Boolean");
            class$java$lang$Boolean = clsClass$2;
        } else {
            clsClass$2 = class$java$lang$Boolean;
        }
        if (cls7 == clsClass$2) {
            return 4;
        }
        if (cls == Byte.TYPE) {
            return 1;
        }
        Class cls8 = cls;
        if (class$java$lang$Byte == null) {
            clsClass$3 = class$("java.lang.Byte");
            class$java$lang$Byte = clsClass$3;
        } else {
            clsClass$3 = class$java$lang$Byte;
        }
        if (cls8 == clsClass$3) {
            return 1;
        }
        if (cls == Short.TYPE) {
            return 2;
        }
        Class cls9 = cls;
        if (class$java$lang$Short == null) {
            clsClass$4 = class$("java.lang.Short");
            class$java$lang$Short = clsClass$4;
        } else {
            clsClass$4 = class$java$lang$Short;
        }
        if (cls9 == clsClass$4) {
            return 2;
        }
        if (cls != Character.TYPE) {
            Class cls10 = cls;
            if (class$java$lang$Character == null) {
                clsClass$5 = class$("java.lang.Character");
                class$java$lang$Character = clsClass$5;
            } else {
                clsClass$5 = class$java$lang$Character;
            }
            if (cls10 != clsClass$5) {
                if (cls == Integer.TYPE) {
                    return 4;
                }
                Class cls11 = cls;
                if (class$java$lang$Integer == null) {
                    clsClass$6 = class$("java.lang.Integer");
                    class$java$lang$Integer = clsClass$6;
                } else {
                    clsClass$6 = class$java$lang$Integer;
                }
                if (cls11 == clsClass$6) {
                    return 4;
                }
                if (cls == Long.TYPE) {
                    return 8;
                }
                Class cls12 = cls;
                if (class$java$lang$Long == null) {
                    clsClass$7 = class$("java.lang.Long");
                    class$java$lang$Long = clsClass$7;
                } else {
                    clsClass$7 = class$java$lang$Long;
                }
                if (cls12 == clsClass$7) {
                    return 8;
                }
                if (cls == Float.TYPE) {
                    return 4;
                }
                Class cls13 = cls;
                if (class$java$lang$Float == null) {
                    clsClass$8 = class$("java.lang.Float");
                    class$java$lang$Float = clsClass$8;
                } else {
                    clsClass$8 = class$java$lang$Float;
                }
                if (cls13 == clsClass$8) {
                    return 4;
                }
                if (cls == Double.TYPE) {
                    return 8;
                }
                Class cls14 = cls;
                if (class$java$lang$Double == null) {
                    clsClass$9 = class$("java.lang.Double");
                    class$java$lang$Double = clsClass$9;
                } else {
                    clsClass$9 = class$java$lang$Double;
                }
                if (cls14 == clsClass$9) {
                    return 8;
                }
                if (class$com$sun$jna$Structure == null) {
                    Class clsClass$12 = class$("com.sun.jna.Structure");
                    class$com$sun$jna$Structure = clsClass$12;
                    cls2 = clsClass$12;
                } else {
                    cls2 = class$com$sun$jna$Structure;
                }
                if (cls2.isAssignableFrom(cls)) {
                    if (class$com$sun$jna$Structure$ByValue == null) {
                        Class clsClass$13 = class$("com.sun.jna.Structure$ByValue");
                        class$com$sun$jna$Structure$ByValue = clsClass$13;
                        cls6 = clsClass$13;
                    } else {
                        cls6 = class$com$sun$jna$Structure$ByValue;
                    }
                    if (cls6.isAssignableFrom(cls)) {
                        return Structure.newInstance(cls).size();
                    }
                    return POINTER_SIZE;
                }
                if (class$com$sun$jna$Pointer == null) {
                    Class clsClass$14 = class$("com.sun.jna.Pointer");
                    class$com$sun$jna$Pointer = clsClass$14;
                    cls3 = clsClass$14;
                } else {
                    cls3 = class$com$sun$jna$Pointer;
                }
                if (!cls3.isAssignableFrom(cls)) {
                    if (class$java$nio$Buffer == null) {
                        Class clsClass$15 = class$("java.nio.Buffer");
                        class$java$nio$Buffer = clsClass$15;
                        cls4 = clsClass$15;
                    } else {
                        cls4 = class$java$nio$Buffer;
                    }
                    if (!cls4.isAssignableFrom(cls)) {
                        if (class$com$sun$jna$Callback == null) {
                            Class clsClass$16 = class$("com.sun.jna.Callback");
                            class$com$sun$jna$Callback = clsClass$16;
                            cls5 = clsClass$16;
                        } else {
                            cls5 = class$com$sun$jna$Callback;
                        }
                        if (!cls5.isAssignableFrom(cls)) {
                            if (class$java$lang$String == null) {
                                clsClass$10 = class$("java.lang.String");
                                class$java$lang$String = clsClass$10;
                            } else {
                                clsClass$10 = class$java$lang$String;
                            }
                            if (clsClass$10 != cls) {
                                if (class$com$sun$jna$WString == null) {
                                    clsClass$11 = class$("com.sun.jna.WString");
                                    class$com$sun$jna$WString = clsClass$11;
                                } else {
                                    clsClass$11 = class$com$sun$jna$WString;
                                }
                                if (clsClass$11 != cls) {
                                    throw new IllegalArgumentException(new StringBuffer().append("Native size for type \"").append(cls.getName()).append("\" is unknown").toString());
                                }
                            }
                        }
                    }
                }
                return POINTER_SIZE;
            }
        }
        return WCHAR_SIZE;
    }

    public static boolean isSupportedNativeType(Class cls) throws Throwable {
        Class clsClass$;
        if (class$com$sun$jna$Structure == null) {
            clsClass$ = class$("com.sun.jna.Structure");
            class$com$sun$jna$Structure = clsClass$;
        } else {
            clsClass$ = class$com$sun$jna$Structure;
        }
        if (clsClass$.isAssignableFrom(cls)) {
            return true;
        }
        try {
            return getNativeSize(cls) != 0;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    public static void main(String[] args) throws Throwable {
        Class clsClass$;
        if (class$com$sun$jna$Native == null) {
            clsClass$ = class$("com.sun.jna.Native");
            class$com$sun$jna$Native = clsClass$;
        } else {
            clsClass$ = class$com$sun$jna$Native;
        }
        Package pkg = clsClass$.getPackage();
        String title = pkg.getSpecificationTitle();
        if (title == null) {
            title = "Java Native Access (JNA)";
        }
        String version = pkg.getSpecificationVersion();
        if (version == null) {
            version = "unknown - package information missing";
        }
        System.out.println(new StringBuffer().append(title).append(" API Version ").append(version).toString());
        String version2 = pkg.getImplementationVersion();
        if (version2 == null) {
            version2 = "unknown - package information missing";
        }
        System.out.println(new StringBuffer().append("Version: ").append(version2).toString());
        System.out.println(new StringBuffer().append(" Native: ").append(getNativeVersion()).append(" (").append(getAPIChecksum()).append(")").toString());
        System.exit(0);
    }
}
