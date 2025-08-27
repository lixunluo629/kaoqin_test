package org.apache.xmlbeans.impl.jam.internal;

import java.lang.ref.WeakReference;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import org.apache.xmlbeans.impl.jam.JClass;
import org.apache.xmlbeans.impl.jam.JPackage;
import org.apache.xmlbeans.impl.jam.JamClassLoader;
import org.apache.xmlbeans.impl.jam.internal.elements.ArrayClassImpl;
import org.apache.xmlbeans.impl.jam.internal.elements.ClassImpl;
import org.apache.xmlbeans.impl.jam.internal.elements.ElementContext;
import org.apache.xmlbeans.impl.jam.internal.elements.PackageImpl;
import org.apache.xmlbeans.impl.jam.internal.elements.PrimitiveClassImpl;
import org.apache.xmlbeans.impl.jam.internal.elements.UnresolvedClassImpl;
import org.apache.xmlbeans.impl.jam.internal.elements.VoidClassImpl;
import org.apache.xmlbeans.impl.jam.mutable.MClass;
import org.apache.xmlbeans.impl.jam.provider.JamClassBuilder;
import org.apache.xmlbeans.impl.jam.visitor.MVisitor;
import org.apache.xmlbeans.impl.jam.visitor.TraversingMVisitor;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/jam/internal/JamClassLoaderImpl.class */
public class JamClassLoaderImpl implements JamClassLoader {
    private JamClassBuilder mBuilder;
    private MVisitor mInitializer;
    private ElementContext mContext;
    private Map mName2Package = new HashMap();
    private Map mFd2ClassCache = new HashMap();
    private Stack mInitializeStack = new Stack();
    private boolean mAlreadyInitializing = false;

    public JamClassLoaderImpl(ElementContext context, JamClassBuilder builder, MVisitor initializerOrNull) {
        this.mInitializer = null;
        if (builder == null) {
            throw new IllegalArgumentException("null builder");
        }
        if (context == null) {
            throw new IllegalArgumentException("null builder");
        }
        this.mBuilder = builder;
        this.mInitializer = initializerOrNull == null ? null : new TraversingMVisitor(initializerOrNull);
        this.mContext = context;
        initCache();
    }

    @Override // org.apache.xmlbeans.impl.jam.JamClassLoader
    public final JClass loadClass(String fd) {
        String pkg;
        String name;
        String pkg2;
        String name2;
        String fd2 = fd.trim();
        JClass out = cacheGet(fd2);
        if (out != null) {
            return out;
        }
        if (fd2.indexOf(91) != -1) {
            String normalFd = ArrayClassImpl.normalizeArrayName(fd2);
            JClass out2 = cacheGet(normalFd);
            if (out2 == null) {
                out2 = ArrayClassImpl.createClassForFD(normalFd, this);
                cachePut(out2, normalFd);
            }
            cachePut(out2, fd2);
            return out2;
        }
        int dollar = fd2.indexOf(36);
        if (dollar != -1) {
            String outerName = fd2.substring(0, dollar);
            ((ClassImpl) loadClass(outerName)).ensureLoaded();
            JClass out3 = cacheGet(fd2);
            int dot = fd2.lastIndexOf(46);
            if (out3 == null) {
                if (dot == -1) {
                    pkg2 = "";
                    name2 = fd2;
                } else {
                    pkg2 = fd2.substring(0, dot);
                    name2 = fd2.substring(dot + 1);
                }
                out3 = new UnresolvedClassImpl(pkg2, name2, this.mContext);
                this.mContext.warning("failed to resolve class " + fd2);
                cachePut(out3);
            }
            return out3;
        }
        int dot2 = fd2.lastIndexOf(46);
        if (dot2 == -1) {
            pkg = "";
            name = fd2;
        } else {
            pkg = fd2.substring(0, dot2);
            name = fd2.substring(dot2 + 1);
        }
        JClass out4 = this.mBuilder.build(pkg, name);
        if (out4 == null) {
            JClass out5 = new UnresolvedClassImpl(pkg, name, this.mContext);
            this.mContext.warning("failed to resolve class " + fd2);
            cachePut(out5);
            return out5;
        }
        cachePut(out4);
        return out4;
    }

    @Override // org.apache.xmlbeans.impl.jam.JamClassLoader
    public JPackage getPackage(String named) {
        JPackage out = (JPackage) this.mName2Package.get(named);
        if (out == null) {
            out = new PackageImpl(this.mContext, named);
            this.mName2Package.put(named, out);
        }
        return out;
    }

    private void initCache() {
        PrimitiveClassImpl.mapNameToPrimitive(this.mContext, this.mFd2ClassCache);
        this.mFd2ClassCache.put("void", new VoidClassImpl(this.mContext));
    }

    private void cachePut(JClass clazz) {
        this.mFd2ClassCache.put(clazz.getFieldDescriptor().trim(), new WeakReference(clazz));
    }

    private void cachePut(JClass clazz, String cachedName) {
        this.mFd2ClassCache.put(cachedName, new WeakReference(clazz));
    }

    private JClass cacheGet(String fd) {
        Object out = this.mFd2ClassCache.get(fd.trim());
        if (out == null) {
            return null;
        }
        if (out instanceof JClass) {
            return (JClass) out;
        }
        if (out instanceof WeakReference) {
            Object out2 = ((WeakReference) out).get();
            if (out2 == null) {
                this.mFd2ClassCache.remove(fd.trim());
                return null;
            }
            return (JClass) out2;
        }
        throw new IllegalStateException();
    }

    public void initialize(ClassImpl out) {
        if (this.mInitializer != null) {
            if (this.mAlreadyInitializing) {
                this.mInitializeStack.push(out);
                return;
            }
            out.accept(this.mInitializer);
            while (!this.mInitializeStack.isEmpty()) {
                ClassImpl initme = (ClassImpl) this.mInitializeStack.pop();
                initme.accept(this.mInitializer);
            }
            this.mAlreadyInitializing = false;
        }
    }

    public Collection getResolvedClasses() {
        return Collections.unmodifiableCollection(this.mFd2ClassCache.values());
    }

    public void addToCache(JClass c) {
        cachePut((MClass) c);
    }
}
