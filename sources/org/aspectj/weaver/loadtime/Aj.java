package org.aspectj.weaver.loadtime;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import org.aspectj.weaver.tools.Trace;
import org.aspectj.weaver.tools.TraceFactory;
import org.aspectj.weaver.tools.WeavingAdaptor;
import org.aspectj.weaver.tools.cache.SimpleCache;
import org.aspectj.weaver.tools.cache.SimpleCacheFactory;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/loadtime/Aj.class */
public class Aj implements ClassPreProcessor {
    private IWeavingContext weavingContext;
    public static SimpleCache laCache = SimpleCacheFactory.createSimpleCache();
    private static ReferenceQueue adaptorQueue = new ReferenceQueue();
    private static Trace trace = TraceFactory.getTraceFactory().getTrace(Aj.class);
    private static final String deleLoader = "sun.reflect.DelegatingClassLoader";
    public static List<String> loadersToSkip;

    static {
        loadersToSkip = null;
        new ExplicitlyInitializedClassLoaderWeavingAdaptor(new ClassLoaderWeavingAdaptor());
        try {
            String loadersToSkipProperty = System.getProperty("aj.weaving.loadersToSkip", "");
            StringTokenizer st = new StringTokenizer(loadersToSkipProperty, ",");
            if (loadersToSkipProperty != null && loadersToSkip == null) {
                if (st.hasMoreTokens()) {
                    loadersToSkip = new ArrayList();
                }
                while (st.hasMoreTokens()) {
                    String nextLoader = st.nextToken();
                    loadersToSkip.add(nextLoader);
                }
            }
        } catch (Exception e) {
        }
    }

    public Aj() {
        this(null);
    }

    public Aj(IWeavingContext context) {
        if (trace.isTraceEnabled()) {
            trace.enter("<init>", (Object) this, new Object[]{context, getClass().getClassLoader()});
        }
        this.weavingContext = context;
        if (trace.isTraceEnabled()) {
            trace.exit("<init>");
        }
    }

    @Override // org.aspectj.weaver.loadtime.ClassPreProcessor
    public void initialize() {
    }

    /*  JADX ERROR: NullPointerException in pass: AttachTryCatchVisitor
        java.lang.NullPointerException: Cannot invoke "String.charAt(int)" because "obj" is null
        	at jadx.core.utils.Utils.cleanObjectName(Utils.java:41)
        	at jadx.core.dex.instructions.args.ArgType.object(ArgType.java:88)
        	at jadx.core.dex.info.ClassInfo.fromName(ClassInfo.java:42)
        	at jadx.core.dex.visitors.AttachTryCatchVisitor.convertToHandlers(AttachTryCatchVisitor.java:113)
        	at jadx.core.dex.visitors.AttachTryCatchVisitor.initTryCatches(AttachTryCatchVisitor.java:54)
        	at jadx.core.dex.visitors.AttachTryCatchVisitor.visit(AttachTryCatchVisitor.java:42)
        */
    @Override // org.aspectj.weaver.loadtime.ClassPreProcessor
    public byte[] preProcess(java.lang.String r9, byte[] r10, java.lang.ClassLoader r11, java.security.ProtectionDomain r12) {
        /*
            Method dump skipped, instructions count: 384
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: org.aspectj.weaver.loadtime.Aj.preProcess(java.lang.String, byte[], java.lang.ClassLoader, java.security.ProtectionDomain):byte[]");
    }

    /* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/loadtime/Aj$AdaptorKey.class */
    private static class AdaptorKey extends WeakReference {
        private final int loaderHashCode;
        private final int sysHashCode;
        private final int hashValue;
        private final String loaderClass;

        public AdaptorKey(ClassLoader loader) {
            super(loader, Aj.adaptorQueue);
            this.loaderHashCode = loader.hashCode();
            this.sysHashCode = System.identityHashCode(loader);
            this.loaderClass = loader.getClass().getName();
            this.hashValue = this.loaderHashCode + this.sysHashCode + this.loaderClass.hashCode();
        }

        public ClassLoader getClassLoader() {
            ClassLoader instance = (ClassLoader) get();
            return instance;
        }

        public boolean equals(Object obj) {
            if (!(obj instanceof AdaptorKey)) {
                return false;
            }
            AdaptorKey other = (AdaptorKey) obj;
            return other.loaderHashCode == this.loaderHashCode && other.sysHashCode == this.sysHashCode && this.loaderClass.equals(other.loaderClass);
        }

        public int hashCode() {
            return this.hashValue;
        }
    }

    public static int removeStaleAdaptors(boolean displayProgress) {
        int removed = 0;
        synchronized (WeaverContainer.weavingAdaptors) {
            if (displayProgress) {
                System.err.println("Weaver adaptors before queue processing:");
                Map<AdaptorKey, ExplicitlyInitializedClassLoaderWeavingAdaptor> m = WeaverContainer.weavingAdaptors;
                Set<AdaptorKey> keys = m.keySet();
                for (Object object : keys) {
                    System.err.println(object + " = " + WeaverContainer.weavingAdaptors.get(object));
                }
            }
            for (Object o = adaptorQueue.poll(); o != null; o = adaptorQueue.poll()) {
                if (displayProgress) {
                    System.err.println("Processing referencequeue entry " + o);
                }
                AdaptorKey wo = (AdaptorKey) o;
                boolean didit = WeaverContainer.weavingAdaptors.remove(wo) != null;
                if (didit) {
                    removed++;
                    if (displayProgress) {
                        System.err.println("Removed? " + didit);
                    }
                } else {
                    throw new RuntimeException("Eh?? key=" + wo);
                }
            }
            if (displayProgress) {
                System.err.println("Weaver adaptors after queue processing:");
                Map<AdaptorKey, ExplicitlyInitializedClassLoaderWeavingAdaptor> m2 = WeaverContainer.weavingAdaptors;
                Set<AdaptorKey> keys2 = m2.keySet();
                for (Object object2 : keys2) {
                    System.err.println(object2 + " = " + WeaverContainer.weavingAdaptors.get(object2));
                }
            }
        }
        return removed;
    }

    public static int getActiveAdaptorCount() {
        return WeaverContainer.weavingAdaptors.size();
    }

    public static void checkQ() {
        synchronized (adaptorQueue) {
            for (Object o = adaptorQueue.poll(); o != null; o = adaptorQueue.poll()) {
                AdaptorKey wo = (AdaptorKey) o;
                WeaverContainer.weavingAdaptors.remove(wo);
            }
        }
    }

    /* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/loadtime/Aj$WeaverContainer.class */
    static class WeaverContainer {
        static final Map<AdaptorKey, ExplicitlyInitializedClassLoaderWeavingAdaptor> weavingAdaptors = Collections.synchronizedMap(new HashMap());
        private static final ClassLoader myClassLoader = WeavingAdaptor.class.getClassLoader();
        private static ExplicitlyInitializedClassLoaderWeavingAdaptor myClassLoaderAdaptor;

        WeaverContainer() {
        }

        static WeavingAdaptor getWeaver(ClassLoader loader, IWeavingContext weavingContext) {
            ExplicitlyInitializedClassLoaderWeavingAdaptor adaptor;
            AdaptorKey adaptorKey = new AdaptorKey(loader);
            loader.getClass().getName();
            synchronized (weavingAdaptors) {
                Aj.checkQ();
                if (loader.equals(myClassLoader)) {
                    adaptor = myClassLoaderAdaptor;
                } else {
                    adaptor = weavingAdaptors.get(adaptorKey);
                }
                if (adaptor == null) {
                    ClassLoaderWeavingAdaptor weavingAdaptor = new ClassLoaderWeavingAdaptor();
                    adaptor = new ExplicitlyInitializedClassLoaderWeavingAdaptor(weavingAdaptor);
                    if (myClassLoaderAdaptor == null && loader.equals(myClassLoader)) {
                        myClassLoaderAdaptor = adaptor;
                    } else {
                        weavingAdaptors.put(adaptorKey, adaptor);
                    }
                }
            }
            return adaptor.getWeavingAdaptor(loader, weavingContext);
        }
    }

    /* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/loadtime/Aj$ExplicitlyInitializedClassLoaderWeavingAdaptor.class */
    static class ExplicitlyInitializedClassLoaderWeavingAdaptor {
        private final ClassLoaderWeavingAdaptor weavingAdaptor;
        private boolean isInitialized = false;

        public ExplicitlyInitializedClassLoaderWeavingAdaptor(ClassLoaderWeavingAdaptor weavingAdaptor) {
            this.weavingAdaptor = weavingAdaptor;
        }

        private void initialize(ClassLoader loader, IWeavingContext weavingContext) {
            if (!this.isInitialized) {
                this.isInitialized = true;
                this.weavingAdaptor.initialize(loader, weavingContext);
            }
        }

        public ClassLoaderWeavingAdaptor getWeavingAdaptor(ClassLoader loader, IWeavingContext weavingContext) {
            initialize(loader, weavingContext);
            return this.weavingAdaptor;
        }
    }

    public String getNamespace(ClassLoader loader) {
        ClassLoaderWeavingAdaptor weavingAdaptor = (ClassLoaderWeavingAdaptor) WeaverContainer.getWeaver(loader, this.weavingContext);
        return weavingAdaptor.getNamespace();
    }

    public boolean generatedClassesExist(ClassLoader loader) {
        return ((ClassLoaderWeavingAdaptor) WeaverContainer.getWeaver(loader, this.weavingContext)).generatedClassesExistFor(null);
    }

    public void flushGeneratedClasses(ClassLoader loader) {
        ((ClassLoaderWeavingAdaptor) WeaverContainer.getWeaver(loader, this.weavingContext)).flushGeneratedClasses();
    }
}
