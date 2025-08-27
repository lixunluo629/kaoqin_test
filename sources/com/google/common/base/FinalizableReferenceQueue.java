package com.google.common.base;

import com.google.common.annotations.VisibleForTesting;
import java.io.Closeable;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.ref.PhantomReference;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Nullable;
import org.springframework.util.ClassUtils;

/* loaded from: guava-18.0.jar:com/google/common/base/FinalizableReferenceQueue.class */
public class FinalizableReferenceQueue implements Closeable {
    private static final Logger logger = Logger.getLogger(FinalizableReferenceQueue.class.getName());
    private static final String FINALIZER_CLASS_NAME = "com.google.common.base.internal.Finalizer";
    private static final Method startFinalizer;
    final ReferenceQueue<Object> queue = new ReferenceQueue<>();
    final PhantomReference<Object> frqRef = new PhantomReference<>(this, this.queue);
    final boolean threadStarted;

    /* loaded from: guava-18.0.jar:com/google/common/base/FinalizableReferenceQueue$FinalizerLoader.class */
    interface FinalizerLoader {
        @Nullable
        Class<?> loadFinalizer();
    }

    static {
        Class<?> finalizer = loadFinalizer(new SystemLoader(), new DecoupledLoader(), new DirectLoader());
        startFinalizer = getStartFinalizer(finalizer);
    }

    public FinalizableReferenceQueue() {
        boolean threadStarted = false;
        try {
            startFinalizer.invoke(null, FinalizableReference.class, this.queue, this.frqRef);
            threadStarted = true;
        } catch (IllegalAccessException impossible) {
            throw new AssertionError(impossible);
        } catch (Throwable t) {
            logger.log(Level.INFO, "Failed to start reference finalizer thread. Reference cleanup will only occur when new references are created.", t);
        }
        this.threadStarted = threadStarted;
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        this.frqRef.enqueue();
        cleanUp();
    }

    /* JADX WARN: Multi-variable type inference failed */
    void cleanUp() {
        if (this.threadStarted) {
            return;
        }
        while (true) {
            Reference<? extends Object> referencePoll = this.queue.poll();
            if (referencePoll != 0) {
                referencePoll.clear();
                try {
                    ((FinalizableReference) referencePoll).finalizeReferent();
                } catch (Throwable t) {
                    logger.log(Level.SEVERE, "Error cleaning up after reference.", t);
                }
            } else {
                return;
            }
        }
    }

    private static Class<?> loadFinalizer(FinalizerLoader... loaders) {
        for (FinalizerLoader loader : loaders) {
            Class<?> finalizer = loader.loadFinalizer();
            if (finalizer != null) {
                return finalizer;
            }
        }
        throw new AssertionError();
    }

    /* loaded from: guava-18.0.jar:com/google/common/base/FinalizableReferenceQueue$SystemLoader.class */
    static class SystemLoader implements FinalizerLoader {

        @VisibleForTesting
        static boolean disabled;

        SystemLoader() {
        }

        @Override // com.google.common.base.FinalizableReferenceQueue.FinalizerLoader
        public Class<?> loadFinalizer() {
            if (disabled) {
                return null;
            }
            try {
                ClassLoader systemLoader = ClassLoader.getSystemClassLoader();
                if (systemLoader != null) {
                    try {
                        return systemLoader.loadClass(FinalizableReferenceQueue.FINALIZER_CLASS_NAME);
                    } catch (ClassNotFoundException e) {
                        return null;
                    }
                }
                return null;
            } catch (SecurityException e2) {
                FinalizableReferenceQueue.logger.info("Not allowed to access system class loader.");
                return null;
            }
        }
    }

    /* loaded from: guava-18.0.jar:com/google/common/base/FinalizableReferenceQueue$DecoupledLoader.class */
    static class DecoupledLoader implements FinalizerLoader {
        private static final String LOADING_ERROR = "Could not load Finalizer in its own class loader.Loading Finalizer in the current class loader instead. As a result, you will not be ableto garbage collect this class loader. To support reclaiming this class loader, eitherresolve the underlying issue, or move Google Collections to your system class path.";

        DecoupledLoader() {
        }

        @Override // com.google.common.base.FinalizableReferenceQueue.FinalizerLoader
        public Class<?> loadFinalizer() {
            try {
                ClassLoader finalizerLoader = newLoader(getBaseUrl());
                return finalizerLoader.loadClass(FinalizableReferenceQueue.FINALIZER_CLASS_NAME);
            } catch (Exception e) {
                FinalizableReferenceQueue.logger.log(Level.WARNING, LOADING_ERROR, (Throwable) e);
                return null;
            }
        }

        URL getBaseUrl() throws IOException {
            String strConcat;
            String finalizerPath = String.valueOf(FinalizableReferenceQueue.FINALIZER_CLASS_NAME.replace('.', '/')).concat(ClassUtils.CLASS_FILE_SUFFIX);
            URL finalizerUrl = getClass().getClassLoader().getResource(finalizerPath);
            if (finalizerUrl == null) {
                throw new FileNotFoundException(finalizerPath);
            }
            String urlString = finalizerUrl.toString();
            if (!urlString.endsWith(finalizerPath)) {
                String strValueOf = String.valueOf(urlString);
                if (strValueOf.length() != 0) {
                    strConcat = "Unsupported path style: ".concat(strValueOf);
                } else {
                    strConcat = str;
                    String str = new String("Unsupported path style: ");
                }
                throw new IOException(strConcat);
            }
            return new URL(finalizerUrl, urlString.substring(0, urlString.length() - finalizerPath.length()));
        }

        URLClassLoader newLoader(URL base) {
            return new URLClassLoader(new URL[]{base}, null);
        }
    }

    /* loaded from: guava-18.0.jar:com/google/common/base/FinalizableReferenceQueue$DirectLoader.class */
    static class DirectLoader implements FinalizerLoader {
        DirectLoader() {
        }

        @Override // com.google.common.base.FinalizableReferenceQueue.FinalizerLoader
        public Class<?> loadFinalizer() {
            try {
                return Class.forName(FinalizableReferenceQueue.FINALIZER_CLASS_NAME);
            } catch (ClassNotFoundException e) {
                throw new AssertionError(e);
            }
        }
    }

    static Method getStartFinalizer(Class<?> finalizer) {
        try {
            return finalizer.getMethod("startFinalizer", Class.class, ReferenceQueue.class, PhantomReference.class);
        } catch (NoSuchMethodException e) {
            throw new AssertionError(e);
        }
    }
}
