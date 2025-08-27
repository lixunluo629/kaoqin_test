package io.netty.util.internal;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/util/internal/NativeLibraryUtil.class */
final class NativeLibraryUtil {
    public static void loadLibrary(String libName, boolean absolute) {
        if (absolute) {
            System.load(libName);
        } else {
            System.loadLibrary(libName);
        }
    }

    private NativeLibraryUtil() {
    }
}
