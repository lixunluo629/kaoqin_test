package org.aspectj.weaver.reflect;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Collection;
import java.util.Collections;
import org.aspectj.weaver.AjAttribute;
import org.aspectj.weaver.AnnotationAJ;
import org.aspectj.weaver.AnnotationTargetKind;
import org.aspectj.weaver.ConcreteTypeMunger;
import org.aspectj.weaver.ISourceContext;
import org.aspectj.weaver.ReferenceType;
import org.aspectj.weaver.ReferenceTypeDelegate;
import org.aspectj.weaver.ResolvedMember;
import org.aspectj.weaver.ResolvedType;
import org.aspectj.weaver.SourceContextImpl;
import org.aspectj.weaver.TypeVariable;
import org.aspectj.weaver.UnresolvedType;
import org.aspectj.weaver.WeakClassLoaderReference;
import org.aspectj.weaver.WeaverStateInfo;
import org.aspectj.weaver.World;
import org.aspectj.weaver.patterns.Declare;
import org.aspectj.weaver.patterns.PerClause;
import org.springframework.util.ClassUtils;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/reflect/ReflectionBasedReferenceTypeDelegate.class */
public class ReflectionBasedReferenceTypeDelegate implements ReferenceTypeDelegate {
    private static final ClassLoader bootClassLoader = new URLClassLoader(new URL[0]);
    protected World world;
    private ReferenceType resolvedType;
    protected Class myClass = null;
    protected WeakClassLoaderReference classLoaderReference = null;
    private ResolvedMember[] fields = null;
    private ResolvedMember[] methods = null;
    private ResolvedType[] interfaces = null;

    public ReflectionBasedReferenceTypeDelegate(Class forClass, ClassLoader aClassLoader, World inWorld, ReferenceType resolvedType) {
        initialize(resolvedType, forClass, aClassLoader, inWorld);
    }

    public ReflectionBasedReferenceTypeDelegate() {
    }

    public void initialize(ReferenceType aType, Class<?> aClass, ClassLoader aClassLoader, World aWorld) {
        this.myClass = aClass;
        this.resolvedType = aType;
        this.world = aWorld;
        this.classLoaderReference = new WeakClassLoaderReference(aClassLoader != null ? aClassLoader : bootClassLoader);
    }

    public Class<?> getClazz() {
        return this.myClass;
    }

    protected Class getBaseClass() {
        return this.myClass;
    }

    protected World getWorld() {
        return this.world;
    }

    public ReferenceType buildGenericType() {
        throw new UnsupportedOperationException("Shouldn't be asking for generic type at 1.4 source level or lower");
    }

    @Override // org.aspectj.weaver.ReferenceTypeDelegate
    public boolean isAspect() {
        return false;
    }

    @Override // org.aspectj.weaver.ReferenceTypeDelegate
    public boolean isAnnotationStyleAspect() {
        return false;
    }

    @Override // org.aspectj.weaver.ReferenceTypeDelegate
    public boolean isInterface() {
        return this.myClass.isInterface();
    }

    @Override // org.aspectj.weaver.ReferenceTypeDelegate
    public boolean isEnum() {
        return false;
    }

    @Override // org.aspectj.weaver.ReferenceTypeDelegate
    public boolean isAnnotationWithRuntimeRetention() {
        return false;
    }

    @Override // org.aspectj.weaver.ReferenceTypeDelegate
    public boolean isAnnotation() {
        return false;
    }

    @Override // org.aspectj.weaver.ReferenceTypeDelegate
    public String getRetentionPolicy() {
        return null;
    }

    @Override // org.aspectj.weaver.ReferenceTypeDelegate
    public boolean canAnnotationTargetType() {
        return false;
    }

    @Override // org.aspectj.weaver.ReferenceTypeDelegate
    public AnnotationTargetKind[] getAnnotationTargetKinds() {
        return null;
    }

    @Override // org.aspectj.weaver.ReferenceTypeDelegate
    public boolean isClass() {
        return (this.myClass.isInterface() || this.myClass.isPrimitive() || this.myClass.isArray()) ? false : true;
    }

    @Override // org.aspectj.weaver.ReferenceTypeDelegate
    public boolean isGeneric() {
        return false;
    }

    @Override // org.aspectj.weaver.ReferenceTypeDelegate
    public boolean isAnonymous() {
        return this.myClass.isAnonymousClass();
    }

    @Override // org.aspectj.weaver.ReferenceTypeDelegate
    public boolean isNested() {
        return this.myClass.isMemberClass();
    }

    @Override // org.aspectj.weaver.ReferenceTypeDelegate
    public ResolvedType getOuterClass() {
        return ReflectionBasedReferenceTypeDelegateFactory.resolveTypeInWorld(this.myClass.getEnclosingClass(), this.world);
    }

    @Override // org.aspectj.weaver.ReferenceTypeDelegate
    public boolean isExposedToWeaver() {
        return false;
    }

    @Override // org.aspectj.weaver.ReferenceTypeDelegate
    public boolean hasAnnotation(UnresolvedType ofType) {
        return false;
    }

    @Override // org.aspectj.weaver.ReferenceTypeDelegate
    public AnnotationAJ[] getAnnotations() {
        return AnnotationAJ.EMPTY_ARRAY;
    }

    @Override // org.aspectj.weaver.ReferenceTypeDelegate
    public boolean hasAnnotations() {
        return false;
    }

    @Override // org.aspectj.weaver.ReferenceTypeDelegate
    public ResolvedType[] getAnnotationTypes() {
        return new ResolvedType[0];
    }

    @Override // org.aspectj.weaver.ReferenceTypeDelegate
    public ResolvedMember[] getDeclaredFields() {
        if (this.fields == null) {
            Field[] reflectFields = this.myClass.getDeclaredFields();
            ResolvedMember[] rFields = new ResolvedMember[reflectFields.length];
            for (int i = 0; i < reflectFields.length; i++) {
                rFields[i] = ReflectionBasedReferenceTypeDelegateFactory.createResolvedMember(reflectFields[i], this.world);
            }
            this.fields = rFields;
        }
        return this.fields;
    }

    @Override // org.aspectj.weaver.ReferenceTypeDelegate
    public ResolvedType[] getDeclaredInterfaces() {
        if (this.interfaces == null) {
            Class[] reflectInterfaces = this.myClass.getInterfaces();
            ResolvedType[] rInterfaces = new ResolvedType[reflectInterfaces.length];
            for (int i = 0; i < reflectInterfaces.length; i++) {
                rInterfaces[i] = ReflectionBasedReferenceTypeDelegateFactory.resolveTypeInWorld(reflectInterfaces[i], this.world);
            }
            this.interfaces = rInterfaces;
        }
        return this.interfaces;
    }

    @Override // org.aspectj.weaver.ReferenceTypeDelegate
    public boolean isCacheable() {
        return true;
    }

    @Override // org.aspectj.weaver.ReferenceTypeDelegate
    public ResolvedMember[] getDeclaredMethods() throws SecurityException {
        if (this.methods == null) {
            Method[] reflectMethods = this.myClass.getDeclaredMethods();
            Constructor[] reflectCons = this.myClass.getDeclaredConstructors();
            ResolvedMember[] rMethods = new ResolvedMember[reflectMethods.length + reflectCons.length];
            for (int i = 0; i < reflectMethods.length; i++) {
                rMethods[i] = ReflectionBasedReferenceTypeDelegateFactory.createResolvedMember(reflectMethods[i], this.world);
            }
            for (int i2 = 0; i2 < reflectCons.length; i2++) {
                rMethods[i2 + reflectMethods.length] = ReflectionBasedReferenceTypeDelegateFactory.createResolvedMember(reflectCons[i2], this.world);
            }
            this.methods = rMethods;
        }
        return this.methods;
    }

    @Override // org.aspectj.weaver.ReferenceTypeDelegate
    public ResolvedMember[] getDeclaredPointcuts() {
        return new ResolvedMember[0];
    }

    @Override // org.aspectj.weaver.ReferenceTypeDelegate
    public TypeVariable[] getTypeVariables() {
        return new TypeVariable[0];
    }

    @Override // org.aspectj.weaver.ReferenceTypeDelegate
    public PerClause getPerClause() {
        return null;
    }

    @Override // org.aspectj.weaver.ReferenceTypeDelegate
    public Collection<Declare> getDeclares() {
        return Collections.emptySet();
    }

    @Override // org.aspectj.weaver.ReferenceTypeDelegate
    public Collection<ConcreteTypeMunger> getTypeMungers() {
        return Collections.emptySet();
    }

    @Override // org.aspectj.weaver.ReferenceTypeDelegate
    public Collection getPrivilegedAccesses() {
        return Collections.EMPTY_SET;
    }

    @Override // org.aspectj.weaver.ReferenceTypeDelegate
    public int getModifiers() {
        return this.myClass.getModifiers();
    }

    @Override // org.aspectj.weaver.ReferenceTypeDelegate
    public ResolvedType getSuperclass() {
        if (this.myClass.getSuperclass() == null) {
            if (this.myClass == Object.class) {
                return null;
            }
            return this.world.resolve(UnresolvedType.OBJECT);
        }
        return ReflectionBasedReferenceTypeDelegateFactory.resolveTypeInWorld(this.myClass.getSuperclass(), this.world);
    }

    @Override // org.aspectj.weaver.ReferenceTypeDelegate
    public WeaverStateInfo getWeaverState() {
        return null;
    }

    @Override // org.aspectj.weaver.ReferenceTypeDelegate
    public ReferenceType getResolvedTypeX() {
        return this.resolvedType;
    }

    @Override // org.aspectj.weaver.ReferenceTypeDelegate
    public boolean doesNotExposeShadowMungers() {
        return false;
    }

    @Override // org.aspectj.weaver.ReferenceTypeDelegate
    public String getDeclaredGenericSignature() {
        return null;
    }

    public ReflectionBasedResolvedMemberImpl createResolvedMemberFor(Member aMember) {
        return null;
    }

    @Override // org.aspectj.weaver.ReferenceTypeDelegate
    public String getSourcefilename() {
        return this.resolvedType.getName() + ClassUtils.CLASS_FILE_SUFFIX;
    }

    @Override // org.aspectj.weaver.ReferenceTypeDelegate
    public ISourceContext getSourceContext() {
        return SourceContextImpl.UNKNOWN_SOURCE_CONTEXT;
    }

    @Override // org.aspectj.weaver.ReferenceTypeDelegate
    public boolean copySourceContext() {
        return true;
    }

    @Override // org.aspectj.weaver.ReferenceTypeDelegate
    public int getCompilerVersion() {
        return AjAttribute.WeaverVersionInfo.getCurrentWeaverMajorVersion();
    }

    @Override // org.aspectj.weaver.ReferenceTypeDelegate
    public void ensureConsistent() {
    }

    @Override // org.aspectj.weaver.ReferenceTypeDelegate
    public boolean isWeavable() {
        return false;
    }

    @Override // org.aspectj.weaver.ReferenceTypeDelegate
    public boolean hasBeenWoven() {
        return false;
    }
}
