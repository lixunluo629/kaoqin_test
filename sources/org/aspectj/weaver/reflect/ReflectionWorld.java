package org.aspectj.weaver.reflect;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.aspectj.bridge.AbortException;
import org.aspectj.bridge.IMessage;
import org.aspectj.bridge.IMessageHandler;
import org.aspectj.util.LangUtil;
import org.aspectj.weaver.BCException;
import org.aspectj.weaver.IWeavingSupport;
import org.aspectj.weaver.ReferenceType;
import org.aspectj.weaver.ReferenceTypeDelegate;
import org.aspectj.weaver.ResolvedType;
import org.aspectj.weaver.UnresolvedType;
import org.aspectj.weaver.WeakClassLoaderReference;
import org.aspectj.weaver.World;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/reflect/ReflectionWorld.class */
public class ReflectionWorld extends World implements IReflectionWorld {
    private static Map<WeakClassLoaderReference, ReflectionWorld> rworlds = Collections.synchronizedMap(new HashMap());
    private WeakClassLoaderReference classLoaderReference;
    private AnnotationFinder annotationFinder;
    private boolean mustUseOneFourDelegates;
    private Map<String, Class<?>> inProgressResolutionClasses;

    public static ReflectionWorld getReflectionWorldFor(WeakClassLoaderReference classLoaderReference) {
        return new ReflectionWorld(classLoaderReference);
    }

    public static void cleanUpWorlds() {
        synchronized (rworlds) {
            rworlds.clear();
        }
    }

    private ReflectionWorld() {
        this.mustUseOneFourDelegates = false;
        this.inProgressResolutionClasses = new HashMap();
    }

    public ReflectionWorld(WeakClassLoaderReference classloaderRef) {
        this.mustUseOneFourDelegates = false;
        this.inProgressResolutionClasses = new HashMap();
        setMessageHandler(new ExceptionBasedMessageHandler());
        setBehaveInJava5Way(LangUtil.is15VMOrGreater());
        this.classLoaderReference = classloaderRef;
        this.annotationFinder = makeAnnotationFinderIfAny(this.classLoaderReference.getClassLoader(), this);
    }

    public ReflectionWorld(ClassLoader aClassLoader) {
        this.mustUseOneFourDelegates = false;
        this.inProgressResolutionClasses = new HashMap();
        setMessageHandler(new ExceptionBasedMessageHandler());
        setBehaveInJava5Way(LangUtil.is15VMOrGreater());
        this.classLoaderReference = new WeakClassLoaderReference(aClassLoader);
        this.annotationFinder = makeAnnotationFinderIfAny(this.classLoaderReference.getClassLoader(), this);
    }

    public ReflectionWorld(boolean forceUseOf14Delegates, ClassLoader aClassLoader) {
        this(aClassLoader);
        this.mustUseOneFourDelegates = forceUseOf14Delegates;
        if (forceUseOf14Delegates) {
            setBehaveInJava5Way(false);
        }
    }

    public static AnnotationFinder makeAnnotationFinderIfAny(ClassLoader loader, World world) throws ClassNotFoundException {
        AnnotationFinder annotationFinder = null;
        try {
            if (LangUtil.is15VMOrGreater()) {
                Class<?> java15AnnotationFinder = Class.forName("org.aspectj.weaver.reflect.Java15AnnotationFinder");
                annotationFinder = (AnnotationFinder) java15AnnotationFinder.newInstance();
                annotationFinder.setClassLoader(loader);
                annotationFinder.setWorld(world);
            }
        } catch (ClassNotFoundException e) {
        } catch (IllegalAccessException ex) {
            throw new BCException("AspectJ internal error", ex);
        } catch (InstantiationException ex2) {
            throw new BCException("AspectJ internal error", ex2);
        }
        return annotationFinder;
    }

    public ClassLoader getClassLoader() {
        return this.classLoaderReference.getClassLoader();
    }

    @Override // org.aspectj.weaver.reflect.IReflectionWorld
    public AnnotationFinder getAnnotationFinder() {
        return this.annotationFinder;
    }

    @Override // org.aspectj.weaver.reflect.IReflectionWorld
    public ResolvedType resolve(Class aClass) {
        return resolve(this, (Class<?>) aClass);
    }

    public static ResolvedType resolve(World world, Class<?> aClass) {
        String className = aClass.getName();
        if (aClass.isArray()) {
            return world.resolve(UnresolvedType.forSignature(className.replace('.', '/')));
        }
        return world.resolve(className);
    }

    public ResolvedType resolveUsingClass(Class<?> clazz) {
        String signature = UnresolvedType.forName(clazz.getName()).getSignature();
        try {
            this.inProgressResolutionClasses.put(signature, clazz);
            ResolvedType resolvedTypeResolve = resolve(clazz.getName());
            this.inProgressResolutionClasses.remove(signature);
            return resolvedTypeResolve;
        } catch (Throwable th) {
            this.inProgressResolutionClasses.remove(signature);
            throw th;
        }
    }

    @Override // org.aspectj.weaver.World
    protected ReferenceTypeDelegate resolveDelegate(ReferenceType ty) {
        ReferenceTypeDelegate result;
        Class<?> clazz;
        if (this.mustUseOneFourDelegates) {
            result = ReflectionBasedReferenceTypeDelegateFactory.create14Delegate(ty, this, this.classLoaderReference.getClassLoader());
        } else {
            result = ReflectionBasedReferenceTypeDelegateFactory.createDelegate(ty, this, this.classLoaderReference.getClassLoader());
        }
        if (result == null && this.inProgressResolutionClasses.size() != 0 && (clazz = this.inProgressResolutionClasses.get(ty.getSignature())) != null) {
            result = ReflectionBasedReferenceTypeDelegateFactory.createDelegate(ty, this, clazz);
        }
        return result;
    }

    /* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/reflect/ReflectionWorld$ReflectionWorldException.class */
    public static class ReflectionWorldException extends RuntimeException {
        private static final long serialVersionUID = -3432261918302793005L;

        public ReflectionWorldException(String message) {
            super(message);
        }
    }

    /* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/reflect/ReflectionWorld$ExceptionBasedMessageHandler.class */
    private static class ExceptionBasedMessageHandler implements IMessageHandler {
        private ExceptionBasedMessageHandler() {
        }

        @Override // org.aspectj.bridge.IMessageHandler
        public boolean handleMessage(IMessage message) throws AbortException {
            throw new ReflectionWorldException(message.toString());
        }

        @Override // org.aspectj.bridge.IMessageHandler
        public boolean isIgnoring(IMessage.Kind kind) {
            if (kind == IMessage.INFO) {
                return true;
            }
            return false;
        }

        @Override // org.aspectj.bridge.IMessageHandler
        public void dontIgnore(IMessage.Kind kind) {
        }

        @Override // org.aspectj.bridge.IMessageHandler
        public void ignore(IMessage.Kind kind) {
        }
    }

    @Override // org.aspectj.weaver.World
    public IWeavingSupport getWeavingSupport() {
        return null;
    }

    @Override // org.aspectj.weaver.World
    public boolean isLoadtimeWeaving() {
        return true;
    }
}
