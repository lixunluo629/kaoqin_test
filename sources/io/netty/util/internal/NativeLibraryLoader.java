package io.netty.util.internal;

import io.netty.util.CharsetUtil;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.attribute.PosixFilePermission;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import org.springframework.util.ClassUtils;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/util/internal/NativeLibraryLoader.class */
public final class NativeLibraryLoader {
    private static final InternalLogger logger;
    private static final String NATIVE_RESOURCE_HOME = "META-INF/native/";
    private static final File WORKDIR;
    private static final boolean DELETE_NATIVE_LIB_AFTER_LOADING;
    private static final boolean TRY_TO_PATCH_SHADED_ID;
    private static final byte[] UNIQUE_ID_BYTES;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !NativeLibraryLoader.class.desiredAssertionStatus();
        logger = InternalLoggerFactory.getInstance((Class<?>) NativeLibraryLoader.class);
        UNIQUE_ID_BYTES = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".getBytes(CharsetUtil.US_ASCII);
        String workdir = SystemPropertyUtil.get("io.netty.native.workdir");
        if (workdir != null) {
            File f = new File(workdir);
            f.mkdirs();
            try {
                f = f.getAbsoluteFile();
            } catch (Exception e) {
            }
            WORKDIR = f;
            logger.debug("-Dio.netty.native.workdir: " + WORKDIR);
        } else {
            WORKDIR = PlatformDependent.tmpdir();
            logger.debug("-Dio.netty.native.workdir: " + WORKDIR + " (io.netty.tmpdir)");
        }
        DELETE_NATIVE_LIB_AFTER_LOADING = SystemPropertyUtil.getBoolean("io.netty.native.deleteLibAfterLoading", true);
        logger.debug("-Dio.netty.native.deleteLibAfterLoading: {}", Boolean.valueOf(DELETE_NATIVE_LIB_AFTER_LOADING));
        TRY_TO_PATCH_SHADED_ID = SystemPropertyUtil.getBoolean("io.netty.native.tryPatchShadedId", true);
        logger.debug("-Dio.netty.native.tryPatchShadedId: {}", Boolean.valueOf(TRY_TO_PATCH_SHADED_ID));
    }

    public static void loadFirstAvailable(ClassLoader loader, String... names) {
        List<Throwable> suppressed = new ArrayList<>();
        for (String name : names) {
            try {
                load(name, loader);
                return;
            } catch (Throwable t) {
                suppressed.add(t);
                logger.debug("Unable to load the library '{}', trying next name...", name, t);
            }
        }
        IllegalArgumentException iae = new IllegalArgumentException("Failed to load any of the given libraries: " + Arrays.toString(names));
        ThrowableUtil.addSuppressedAndClear(iae, suppressed);
        throw iae;
    }

    private static String calculatePackagePrefix() {
        String maybeShaded = NativeLibraryLoader.class.getName();
        String expected = "io!netty!util!internal!NativeLibraryLoader".replace('!', '.');
        if (!maybeShaded.endsWith(expected)) {
            throw new UnsatisfiedLinkError(String.format("Could not find prefix added to %s to get %s. When shading, only adding a package prefix is supported", expected, maybeShaded));
        }
        return maybeShaded.substring(0, maybeShaded.length() - expected.length());
    }

    public static void load(String originalName, ClassLoader loader) throws IOException {
        URL url;
        String packagePrefix = calculatePackagePrefix().replace('.', '_');
        String name = packagePrefix + originalName;
        List<Throwable> suppressed = new ArrayList<>();
        try {
            loadLibrary(loader, name, false);
        } catch (Throwable ex) {
            suppressed.add(ex);
            if (logger.isDebugEnabled()) {
                logger.debug("{} cannot be loaded from java.library.path, now trying export to -Dio.netty.native.workdir: {}", name, WORKDIR, ex);
            }
            String libname = System.mapLibraryName(name);
            String path = NATIVE_RESOURCE_HOME + libname;
            File tmpFile = null;
            if (loader == null) {
                url = ClassLoader.getSystemResource(path);
            } else {
                url = loader.getResource(path);
            }
            try {
                if (url == null) {
                    try {
                        try {
                            if (PlatformDependent.isOsx()) {
                                String fileName = path.endsWith(".jnilib") ? "META-INF/native/lib" + name + ".dynlib" : "META-INF/native/lib" + name + ".jnilib";
                                if (loader == null) {
                                    url = ClassLoader.getSystemResource(fileName);
                                } else {
                                    url = loader.getResource(fileName);
                                }
                                if (url == null) {
                                    FileNotFoundException fnf = new FileNotFoundException(fileName);
                                    ThrowableUtil.addSuppressedAndClear(fnf, suppressed);
                                    throw fnf;
                                }
                            } else {
                                FileNotFoundException fnf2 = new FileNotFoundException(path);
                                ThrowableUtil.addSuppressedAndClear(fnf2, suppressed);
                                throw fnf2;
                            }
                        } catch (Exception e) {
                            UnsatisfiedLinkError ule = new UnsatisfiedLinkError("could not load a native library: " + name);
                            ule.initCause(e);
                            ThrowableUtil.addSuppressedAndClear(ule, suppressed);
                            throw ule;
                        }
                    } catch (UnsatisfiedLinkError e2) {
                        if (0 != 0) {
                            try {
                                if (tmpFile.isFile() && tmpFile.canRead() && !NoexecVolumeDetector.canExecuteExecutable(null)) {
                                    logger.info("{} exists but cannot be executed even when execute permissions set; check volume for \"noexec\" flag; use -D{}=[path] to set native working directory separately.", tmpFile.getPath(), "io.netty.native.workdir");
                                }
                            } catch (Throwable t) {
                                suppressed.add(t);
                                logger.debug("Error checking if {} is on a file store mounted with noexec", null, t);
                                ThrowableUtil.addSuppressedAndClear(e2, suppressed);
                                throw e2;
                            }
                        }
                        ThrowableUtil.addSuppressedAndClear(e2, suppressed);
                        throw e2;
                    }
                }
                int index = libname.lastIndexOf(46);
                String prefix = libname.substring(0, index);
                String suffix = libname.substring(index);
                File tmpFile2 = File.createTempFile(prefix, suffix, WORKDIR);
                InputStream in = url.openStream();
                OutputStream out = new FileOutputStream(tmpFile2);
                if (shouldShadedLibraryIdBePatched(packagePrefix)) {
                    patchShadedLibraryId(in, out, originalName, name);
                } else {
                    byte[] buffer = new byte[8192];
                    while (true) {
                        int length = in.read(buffer);
                        if (length <= 0) {
                            break;
                        } else {
                            out.write(buffer, 0, length);
                        }
                    }
                }
                out.flush();
                closeQuietly(out);
                loadLibrary(loader, tmpFile2.getPath(), true);
                closeQuietly(in);
                closeQuietly(null);
                if (tmpFile2 != null) {
                    if (!DELETE_NATIVE_LIB_AFTER_LOADING || !tmpFile2.delete()) {
                        tmpFile2.deleteOnExit();
                    }
                }
            } catch (Throwable th) {
                closeQuietly(null);
                closeQuietly(null);
                if (0 != 0 && (!DELETE_NATIVE_LIB_AFTER_LOADING || !tmpFile.delete())) {
                    tmpFile.deleteOnExit();
                }
                throw th;
            }
        }
    }

    static boolean patchShadedLibraryId(InputStream in, OutputStream out, String originalName, String name) throws IOException {
        boolean patched;
        byte[] buffer = new byte[8192];
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(in.available());
        while (true) {
            int length = in.read(buffer);
            if (length <= 0) {
                break;
            }
            byteArrayOutputStream.write(buffer, 0, length);
        }
        byteArrayOutputStream.flush();
        byte[] bytes = byteArrayOutputStream.toByteArray();
        byteArrayOutputStream.close();
        if (!patchShadedLibraryId(bytes, originalName, name)) {
            String os = PlatformDependent.normalizedOs();
            String arch = PlatformDependent.normalizedArch();
            String osArch = "_" + os + "_" + arch;
            if (originalName.endsWith(osArch)) {
                patched = patchShadedLibraryId(bytes, originalName.substring(0, originalName.length() - osArch.length()), name);
            } else {
                patched = false;
            }
        } else {
            patched = true;
        }
        out.write(bytes, 0, bytes.length);
        return patched;
    }

    private static boolean shouldShadedLibraryIdBePatched(String packagePrefix) {
        return TRY_TO_PATCH_SHADED_ID && PlatformDependent.isOsx() && !packagePrefix.isEmpty();
    }

    /* JADX WARN: Code restructure failed: missing block: B:34:0x004f, code lost:
    
        continue;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private static boolean patchShadedLibraryId(byte[] r12, java.lang.String r13, java.lang.String r14) {
        /*
            Method dump skipped, instructions count: 196
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: io.netty.util.internal.NativeLibraryLoader.patchShadedLibraryId(byte[], java.lang.String, java.lang.String):boolean");
    }

    private static void loadLibrary(ClassLoader loader, String name, boolean absolute) {
        try {
            try {
                Class<?> newHelper = tryToLoadClass(loader, NativeLibraryUtil.class);
                loadLibraryByHelper(newHelper, name, absolute);
                logger.debug("Successfully loaded the library {}", name);
            } catch (Exception e) {
                logger.debug("Unable to load the library '{}', trying other loading mechanism.", name, e);
                NativeLibraryUtil.loadLibrary(name, absolute);
                logger.debug("Successfully loaded the library {}", name);
            } catch (UnsatisfiedLinkError e2) {
                logger.debug("Unable to load the library '{}', trying other loading mechanism.", name, e2);
                NativeLibraryUtil.loadLibrary(name, absolute);
                logger.debug("Successfully loaded the library {}", name);
            }
        } catch (UnsatisfiedLinkError ule) {
            if (0 != 0) {
                ThrowableUtil.addSuppressed(ule, (Throwable) null);
            }
            throw ule;
        }
    }

    private static void loadLibraryByHelper(final Class<?> helper, final String name, final boolean absolute) throws UnsatisfiedLinkError {
        Object ret = AccessController.doPrivileged(new PrivilegedAction<Object>() { // from class: io.netty.util.internal.NativeLibraryLoader.1
            @Override // java.security.PrivilegedAction
            public Object run() throws NoSuchMethodException, SecurityException {
                try {
                    Method method = helper.getMethod("loadLibrary", String.class, Boolean.TYPE);
                    method.setAccessible(true);
                    return method.invoke(null, name, Boolean.valueOf(absolute));
                } catch (Exception e) {
                    return e;
                }
            }
        });
        if (ret instanceof Throwable) {
            Throwable t = (Throwable) ret;
            if (!$assertionsDisabled && (t instanceof UnsatisfiedLinkError)) {
                throw new AssertionError(t + " should be a wrapper throwable");
            }
            Throwable cause = t.getCause();
            if (cause instanceof UnsatisfiedLinkError) {
                throw ((UnsatisfiedLinkError) cause);
            }
            UnsatisfiedLinkError ule = new UnsatisfiedLinkError(t.getMessage());
            ule.initCause(t);
            throw ule;
        }
    }

    private static Class<?> tryToLoadClass(final ClassLoader loader, final Class<?> helper) throws ClassNotFoundException, IOException {
        try {
            return Class.forName(helper.getName(), false, loader);
        } catch (ClassNotFoundException e1) {
            if (loader == null) {
                throw e1;
            }
            try {
                final byte[] classBinary = classToByteArray(helper);
                return (Class) AccessController.doPrivileged(new PrivilegedAction<Class<?>>() { // from class: io.netty.util.internal.NativeLibraryLoader.2
                    /* JADX WARN: Can't rename method to resolve collision */
                    @Override // java.security.PrivilegedAction
                    public Class<?> run() throws NoSuchMethodException, SecurityException {
                        try {
                            Method defineClass = ClassLoader.class.getDeclaredMethod("defineClass", String.class, byte[].class, Integer.TYPE, Integer.TYPE);
                            defineClass.setAccessible(true);
                            return (Class) defineClass.invoke(loader, helper.getName(), classBinary, 0, Integer.valueOf(classBinary.length));
                        } catch (Exception e) {
                            throw new IllegalStateException("Define class failed!", e);
                        }
                    }
                });
            } catch (ClassNotFoundException e2) {
                ThrowableUtil.addSuppressed(e2, e1);
                throw e2;
            } catch (Error e22) {
                ThrowableUtil.addSuppressed(e22, e1);
                throw e22;
            } catch (RuntimeException e23) {
                ThrowableUtil.addSuppressed(e23, e1);
                throw e23;
            }
        }
    }

    /* JADX WARN: Finally extract failed */
    private static byte[] classToByteArray(Class<?> clazz) throws ClassNotFoundException, IOException {
        String fileName = clazz.getName();
        int lastDot = fileName.lastIndexOf(46);
        if (lastDot > 0) {
            fileName = fileName.substring(lastDot + 1);
        }
        URL classUrl = clazz.getResource(fileName + ClassUtils.CLASS_FILE_SUFFIX);
        if (classUrl == null) {
            throw new ClassNotFoundException(clazz.getName());
        }
        byte[] buf = new byte[1024];
        ByteArrayOutputStream out = new ByteArrayOutputStream(4096);
        InputStream in = null;
        try {
            try {
                in = classUrl.openStream();
                while (true) {
                    int r = in.read(buf);
                    if (r != -1) {
                        out.write(buf, 0, r);
                    } else {
                        byte[] byteArray = out.toByteArray();
                        closeQuietly(in);
                        closeQuietly(out);
                        return byteArray;
                    }
                }
            } catch (IOException ex) {
                throw new ClassNotFoundException(clazz.getName(), ex);
            }
        } catch (Throwable th) {
            closeQuietly(in);
            closeQuietly(out);
            throw th;
        }
    }

    private static void closeQuietly(Closeable c) throws IOException {
        if (c != null) {
            try {
                c.close();
            } catch (IOException e) {
            }
        }
    }

    private NativeLibraryLoader() {
    }

    /* loaded from: netty-all-4.1.50.Final.jar:io/netty/util/internal/NativeLibraryLoader$NoexecVolumeDetector.class */
    private static final class NoexecVolumeDetector {
        /* JADX INFO: Access modifiers changed from: private */
        @SuppressJava6Requirement(reason = "Usage guarded by java version check")
        public static boolean canExecuteExecutable(File file) throws IOException {
            if (PlatformDependent.javaVersion() < 7 || file.canExecute()) {
                return true;
            }
            Set<PosixFilePermission> existingFilePermissions = Files.getPosixFilePermissions(file.toPath(), new LinkOption[0]);
            Collection<? extends PosixFilePermission> collectionOf = EnumSet.of(PosixFilePermission.OWNER_EXECUTE, PosixFilePermission.GROUP_EXECUTE, PosixFilePermission.OTHERS_EXECUTE);
            if (existingFilePermissions.containsAll(collectionOf)) {
                return false;
            }
            Set<PosixFilePermission> newPermissions = EnumSet.copyOf((Collection) existingFilePermissions);
            newPermissions.addAll(collectionOf);
            Files.setPosixFilePermissions(file.toPath(), newPermissions);
            return file.canExecute();
        }

        private NoexecVolumeDetector() {
        }
    }
}
