package org.apache.ibatis.javassist.scopedpool;

import java.lang.ref.WeakReference;
import java.security.ProtectionDomain;
import java.util.Map;
import org.apache.ibatis.javassist.CannotCompileException;
import org.apache.ibatis.javassist.ClassPool;
import org.apache.ibatis.javassist.CtClass;
import org.apache.ibatis.javassist.LoaderClassPath;
import org.apache.ibatis.javassist.NotFoundException;
import org.springframework.util.ClassUtils;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/javassist/scopedpool/ScopedClassPool.class */
public class ScopedClassPool extends ClassPool {
    protected ScopedClassPoolRepository repository;
    protected WeakReference classLoader;
    protected LoaderClassPath classPath;
    protected SoftValueHashMap softcache;
    boolean isBootstrapCl;

    static {
        ClassPool.doPruning = false;
        ClassPool.releaseUnmodifiedClassFile = false;
    }

    protected ScopedClassPool(ClassLoader cl, ClassPool src, ScopedClassPoolRepository repository) {
        this(cl, src, repository, false);
    }

    protected ScopedClassPool(ClassLoader cl, ClassPool src, ScopedClassPoolRepository repository, boolean isTemp) {
        super(src);
        this.softcache = new SoftValueHashMap();
        this.isBootstrapCl = true;
        this.repository = repository;
        this.classLoader = new WeakReference(cl);
        if (cl != null) {
            this.classPath = new LoaderClassPath(cl);
            insertClassPath(this.classPath);
        }
        this.childFirstLookup = true;
        if (!isTemp && cl == null) {
            this.isBootstrapCl = true;
        }
    }

    @Override // org.apache.ibatis.javassist.ClassPool
    public ClassLoader getClassLoader() {
        ClassLoader cl = getClassLoader0();
        if (cl == null && !this.isBootstrapCl) {
            throw new IllegalStateException("ClassLoader has been garbage collected");
        }
        return cl;
    }

    protected ClassLoader getClassLoader0() {
        return (ClassLoader) this.classLoader.get();
    }

    public void close() {
        removeClassPath(this.classPath);
        this.classPath.close();
        this.classes.clear();
        this.softcache.clear();
    }

    public synchronized void flushClass(String classname) {
        this.classes.remove(classname);
        this.softcache.remove(classname);
    }

    public synchronized void soften(CtClass clazz) {
        if (this.repository.isPrune()) {
            clazz.prune();
        }
        this.classes.remove(clazz.getName());
        this.softcache.put(clazz.getName(), clazz);
    }

    public boolean isUnloadedClassLoader() {
        return false;
    }

    @Override // org.apache.ibatis.javassist.ClassPool
    protected CtClass getCached(String classname) {
        String classResourceName;
        CtClass clazz = getCachedLocally(classname);
        if (clazz == null) {
            boolean isLocal = false;
            ClassLoader dcl = getClassLoader0();
            if (dcl != null) {
                int lastIndex = classname.lastIndexOf(36);
                if (lastIndex < 0) {
                    classResourceName = classname.replaceAll("[\\.]", "/") + ClassUtils.CLASS_FILE_SUFFIX;
                } else {
                    classResourceName = classname.substring(0, lastIndex).replaceAll("[\\.]", "/") + classname.substring(lastIndex) + ClassUtils.CLASS_FILE_SUFFIX;
                }
                isLocal = dcl.getResource(classResourceName) != null;
            }
            if (!isLocal) {
                Map registeredCLs = this.repository.getRegisteredCLs();
                synchronized (registeredCLs) {
                    for (ScopedClassPool pool : registeredCLs.values()) {
                        if (pool.isUnloadedClassLoader()) {
                            this.repository.unregisterClassLoader(pool.getClassLoader());
                        } else {
                            clazz = pool.getCachedLocally(classname);
                            if (clazz != null) {
                                return clazz;
                            }
                        }
                    }
                }
            }
        }
        return clazz;
    }

    @Override // org.apache.ibatis.javassist.ClassPool
    protected void cacheCtClass(String classname, CtClass c, boolean dynamic) {
        if (dynamic) {
            super.cacheCtClass(classname, c, dynamic);
            return;
        }
        if (this.repository.isPrune()) {
            c.prune();
        }
        this.softcache.put(classname, c);
    }

    public void lockInCache(CtClass c) {
        super.cacheCtClass(c.getName(), c, false);
    }

    protected CtClass getCachedLocally(String classname) {
        CtClass ctClass;
        CtClass cached = (CtClass) this.classes.get(classname);
        if (cached != null) {
            return cached;
        }
        synchronized (this.softcache) {
            ctClass = (CtClass) this.softcache.get(classname);
        }
        return ctClass;
    }

    public synchronized CtClass getLocally(String classname) throws NotFoundException {
        this.softcache.remove(classname);
        CtClass clazz = (CtClass) this.classes.get(classname);
        if (clazz == null) {
            clazz = createCtClass(classname, true);
            if (clazz == null) {
                throw new NotFoundException(classname);
            }
            super.cacheCtClass(classname, clazz, false);
        }
        return clazz;
    }

    @Override // org.apache.ibatis.javassist.ClassPool
    public Class toClass(CtClass ct, ClassLoader loader, ProtectionDomain domain) throws CannotCompileException {
        lockInCache(ct);
        return super.toClass(ct, getClassLoader0(), domain);
    }
}
