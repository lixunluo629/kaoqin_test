package org.apache.ibatis.javassist.scopedpool;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.WeakHashMap;
import org.apache.ibatis.javassist.ClassPool;
import org.apache.ibatis.javassist.LoaderClassPath;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/javassist/scopedpool/ScopedClassPoolRepositoryImpl.class */
public class ScopedClassPoolRepositoryImpl implements ScopedClassPoolRepository {
    private static final ScopedClassPoolRepositoryImpl instance = new ScopedClassPoolRepositoryImpl();
    boolean pruneWhenCached;
    private boolean prune = true;
    protected Map registeredCLs = Collections.synchronizedMap(new WeakHashMap());
    protected ScopedClassPoolFactory factory = new ScopedClassPoolFactoryImpl();
    protected ClassPool classpool = ClassPool.getDefault();

    public static ScopedClassPoolRepository getInstance() {
        return instance;
    }

    private ScopedClassPoolRepositoryImpl() {
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        this.classpool.insertClassPath(new LoaderClassPath(cl));
    }

    @Override // org.apache.ibatis.javassist.scopedpool.ScopedClassPoolRepository
    public boolean isPrune() {
        return this.prune;
    }

    @Override // org.apache.ibatis.javassist.scopedpool.ScopedClassPoolRepository
    public void setPrune(boolean prune) {
        this.prune = prune;
    }

    @Override // org.apache.ibatis.javassist.scopedpool.ScopedClassPoolRepository
    public ScopedClassPool createScopedClassPool(ClassLoader cl, ClassPool src) {
        return this.factory.create(cl, src, this);
    }

    @Override // org.apache.ibatis.javassist.scopedpool.ScopedClassPoolRepository
    public ClassPool findClassPool(ClassLoader cl) {
        if (cl == null) {
            return registerClassLoader(ClassLoader.getSystemClassLoader());
        }
        return registerClassLoader(cl);
    }

    @Override // org.apache.ibatis.javassist.scopedpool.ScopedClassPoolRepository
    public ClassPool registerClassLoader(ClassLoader ucl) {
        synchronized (this.registeredCLs) {
            if (this.registeredCLs.containsKey(ucl)) {
                return (ClassPool) this.registeredCLs.get(ucl);
            }
            ScopedClassPool pool = createScopedClassPool(ucl, this.classpool);
            this.registeredCLs.put(ucl, pool);
            return pool;
        }
    }

    @Override // org.apache.ibatis.javassist.scopedpool.ScopedClassPoolRepository
    public Map getRegisteredCLs() {
        clearUnregisteredClassLoaders();
        return this.registeredCLs;
    }

    @Override // org.apache.ibatis.javassist.scopedpool.ScopedClassPoolRepository
    public void clearUnregisteredClassLoaders() {
        ArrayList toUnregister = null;
        synchronized (this.registeredCLs) {
            Iterator it = this.registeredCLs.values().iterator();
            while (it.hasNext()) {
                ScopedClassPool pool = (ScopedClassPool) it.next();
                if (pool.isUnloadedClassLoader()) {
                    it.remove();
                    ClassLoader cl = pool.getClassLoader();
                    if (cl != null) {
                        if (toUnregister == null) {
                            toUnregister = new ArrayList();
                        }
                        toUnregister.add(cl);
                    }
                }
            }
            if (toUnregister != null) {
                for (int i = 0; i < toUnregister.size(); i++) {
                    unregisterClassLoader((ClassLoader) toUnregister.get(i));
                }
            }
        }
    }

    @Override // org.apache.ibatis.javassist.scopedpool.ScopedClassPoolRepository
    public void unregisterClassLoader(ClassLoader cl) {
        synchronized (this.registeredCLs) {
            ScopedClassPool pool = (ScopedClassPool) this.registeredCLs.remove(cl);
            if (pool != null) {
                pool.close();
            }
        }
    }

    public void insertDelegate(ScopedClassPoolRepository delegate) {
    }

    @Override // org.apache.ibatis.javassist.scopedpool.ScopedClassPoolRepository
    public void setClassPoolFactory(ScopedClassPoolFactory factory) {
        this.factory = factory;
    }

    @Override // org.apache.ibatis.javassist.scopedpool.ScopedClassPoolRepository
    public ScopedClassPoolFactory getClassPoolFactory() {
        return this.factory;
    }
}
