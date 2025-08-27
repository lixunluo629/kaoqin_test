package org.aspectj.weaver.ltw;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.aspectj.apache.bcel.classfile.JavaClass;
import org.aspectj.bridge.IMessageHandler;
import org.aspectj.util.LangUtil;
import org.aspectj.weaver.Dump;
import org.aspectj.weaver.ICrossReferenceHandler;
import org.aspectj.weaver.ReferenceType;
import org.aspectj.weaver.ReferenceTypeDelegate;
import org.aspectj.weaver.ResolvedType;
import org.aspectj.weaver.bcel.BcelWorld;
import org.aspectj.weaver.loadtime.IWeavingContext;
import org.aspectj.weaver.reflect.AnnotationFinder;
import org.aspectj.weaver.reflect.IReflectionWorld;
import org.aspectj.weaver.reflect.ReflectionBasedReferenceTypeDelegateFactory;
import org.aspectj.weaver.reflect.ReflectionWorld;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/ltw/LTWWorld.class */
public class LTWWorld extends BcelWorld implements IReflectionWorld {
    private AnnotationFinder annotationFinder;
    private IWeavingContext weavingContext;
    private String classLoaderString;
    private String classLoaderParentString;
    protected static final Class concurrentMapClass = null;
    private static final boolean ShareBootstrapTypes = false;
    protected static Map bootstrapTypes;
    private static final long serialVersionUID = 1;
    private boolean typeCompletionInProgress;
    private List typesForCompletion;

    public LTWWorld(ClassLoader loader, IWeavingContext weavingContext, IMessageHandler handler, ICrossReferenceHandler xrefHandler) {
        super(loader, handler, xrefHandler);
        this.typeCompletionInProgress = false;
        this.typesForCompletion = new ArrayList();
        this.weavingContext = weavingContext;
        this.classLoaderString = loader.toString();
        this.classLoaderParentString = loader.getParent() == null ? "<NullParent>" : loader.getParent().toString();
        setBehaveInJava5Way(LangUtil.is15VMOrGreater());
        this.annotationFinder = ReflectionWorld.makeAnnotationFinderIfAny(loader, this);
    }

    public ClassLoader getClassLoader() {
        return this.weavingContext.getClassLoader();
    }

    @Override // org.aspectj.weaver.bcel.BcelWorld, org.aspectj.weaver.World
    protected ReferenceTypeDelegate resolveDelegate(ReferenceType ty) {
        ReferenceTypeDelegate bootstrapLoaderDelegate = resolveIfBootstrapDelegate(ty);
        if (bootstrapLoaderDelegate != null) {
            return bootstrapLoaderDelegate;
        }
        return super.resolveDelegate(ty);
    }

    protected ReferenceTypeDelegate resolveIfBootstrapDelegate(ReferenceType ty) {
        return null;
    }

    private ReferenceTypeDelegate resolveReflectionTypeDelegate(ReferenceType ty, ClassLoader resolutionLoader) {
        ReferenceTypeDelegate res = ReflectionBasedReferenceTypeDelegateFactory.createDelegate(ty, this, resolutionLoader);
        return res;
    }

    public void loadedClass(Class clazz) {
    }

    @Override // org.aspectj.weaver.reflect.IReflectionWorld
    public AnnotationFinder getAnnotationFinder() {
        return this.annotationFinder;
    }

    @Override // org.aspectj.weaver.reflect.IReflectionWorld
    public ResolvedType resolve(Class aClass) {
        return ReflectionWorld.resolve(this, (Class<?>) aClass);
    }

    private static Map makeConcurrentMap() {
        if (concurrentMapClass != null) {
            try {
                return (Map) concurrentMapClass.newInstance();
            } catch (IllegalAccessException e) {
            } catch (InstantiationException e2) {
            }
        }
        return Collections.synchronizedMap(new HashMap());
    }

    private static Class makeConcurrentMapClass() {
        String[] betterChoices = {"java.util.concurrent.ConcurrentHashMap", "edu.emory.mathcs.backport.java.util.concurrent.ConcurrentHashMap", "EDU.oswego.cs.dl.util.concurrent.ConcurrentHashMap"};
        for (String str : betterChoices) {
            try {
                return Class.forName(str);
            } catch (ClassNotFoundException | SecurityException e) {
            }
        }
        return null;
    }

    @Override // org.aspectj.weaver.World
    public boolean isRunMinimalMemory() {
        if (isRunMinimalMemorySet()) {
            return super.isRunMinimalMemory();
        }
        return false;
    }

    @Override // org.aspectj.weaver.World
    protected void completeBinaryType(ResolvedType ret) {
        if (isLocallyDefined(ret.getName())) {
            if (this.typeCompletionInProgress) {
                this.typesForCompletion.add(ret);
                return;
            }
            try {
                this.typeCompletionInProgress = true;
                completeHierarchyForType(ret);
                while (this.typesForCompletion.size() != 0) {
                    ResolvedType rt = (ResolvedType) this.typesForCompletion.get(0);
                    completeHierarchyForType(rt);
                    this.typesForCompletion.remove(0);
                }
                return;
            } finally {
                this.typeCompletionInProgress = false;
            }
        }
        if (!ret.needsModifiableDelegate()) {
            completeNonLocalType(ret);
        }
    }

    private void completeHierarchyForType(ResolvedType ret) {
        getLint().typeNotExposedToWeaver.setSuppressed(true);
        weaveInterTypeDeclarations(ret);
        getLint().typeNotExposedToWeaver.setSuppressed(false);
    }

    protected boolean needsCompletion() {
        return true;
    }

    @Override // org.aspectj.weaver.World
    public boolean isLocallyDefined(String classname) {
        return this.weavingContext.isLocallyDefined(classname);
    }

    protected ResolvedType completeNonLocalType(ResolvedType ret) {
        if (ret.isMissing()) {
            return ret;
        }
        ResolvedType toResolve = ret;
        if (ret.isParameterizedType() || ret.isGenericType()) {
            toResolve = toResolve.getGenericType();
        }
        ReferenceTypeDelegate rtd = resolveReflectionTypeDelegate((ReferenceType) toResolve, getClassLoader());
        ((ReferenceType) ret).setDelegate(rtd);
        return ret;
    }

    @Override // org.aspectj.weaver.bcel.BcelWorld, org.aspectj.apache.bcel.util.Repository
    public void storeClass(JavaClass clazz) {
        ensureRepositorySetup();
        this.delegate.storeClass(clazz);
    }

    @Override // org.aspectj.weaver.World, org.aspectj.weaver.Dump.INode
    public void accept(Dump.IVisitor visitor) {
        visitor.visitObject("Class loader:");
        visitor.visitObject(this.classLoaderString);
        visitor.visitObject("Class loader parent:");
        visitor.visitObject(this.classLoaderParentString);
        super.accept(visitor);
    }

    @Override // org.aspectj.weaver.bcel.BcelWorld, org.aspectj.weaver.World
    public boolean isLoadtimeWeaving() {
        return true;
    }
}
