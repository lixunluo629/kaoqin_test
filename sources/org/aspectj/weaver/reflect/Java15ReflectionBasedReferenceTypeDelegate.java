package org.aspectj.weaver.reflect;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Set;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.AjType;
import org.aspectj.lang.reflect.AjTypeSystem;
import org.aspectj.lang.reflect.Pointcut;
import org.aspectj.weaver.AnnotationAJ;
import org.aspectj.weaver.Member;
import org.aspectj.weaver.ReferenceType;
import org.aspectj.weaver.ResolvedMember;
import org.aspectj.weaver.ResolvedPointcutDefinition;
import org.aspectj.weaver.ResolvedType;
import org.aspectj.weaver.TypeVariable;
import org.aspectj.weaver.TypeVariableReferenceType;
import org.aspectj.weaver.UnresolvedType;
import org.aspectj.weaver.World;
import org.aspectj.weaver.tools.PointcutDesignatorHandler;
import org.aspectj.weaver.tools.PointcutParameter;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/reflect/Java15ReflectionBasedReferenceTypeDelegate.class */
public class Java15ReflectionBasedReferenceTypeDelegate extends ReflectionBasedReferenceTypeDelegate {
    private AjType<?> myType;
    private ResolvedType[] annotations;
    private ResolvedMember[] pointcuts;
    private ResolvedMember[] methods;
    private ResolvedMember[] fields;
    private TypeVariable[] typeVariables;
    private ResolvedType superclass;
    private ResolvedType[] superInterfaces;
    private JavaLangTypeToResolvedTypeConverter typeConverter;
    private String genericSignature = null;
    private Java15AnnotationFinder annotationFinder = null;
    private ArgNameFinder argNameFinder = null;

    @Override // org.aspectj.weaver.reflect.ReflectionBasedReferenceTypeDelegate
    public void initialize(ReferenceType aType, Class aClass, ClassLoader classLoader, World aWorld) {
        super.initialize(aType, aClass, classLoader, aWorld);
        this.myType = AjTypeSystem.getAjType(aClass);
        this.annotationFinder = new Java15AnnotationFinder();
        this.argNameFinder = this.annotationFinder;
        this.annotationFinder.setClassLoader(this.classLoaderReference.getClassLoader());
        this.annotationFinder.setWorld(aWorld);
        this.typeConverter = new JavaLangTypeToResolvedTypeConverter(aWorld);
    }

    @Override // org.aspectj.weaver.reflect.ReflectionBasedReferenceTypeDelegate
    public ReferenceType buildGenericType() {
        return (ReferenceType) UnresolvedType.forGenericTypeVariables(getResolvedTypeX().getSignature(), getTypeVariables()).resolve(getWorld());
    }

    @Override // org.aspectj.weaver.reflect.ReflectionBasedReferenceTypeDelegate, org.aspectj.weaver.ReferenceTypeDelegate
    public AnnotationAJ[] getAnnotations() {
        return super.getAnnotations();
    }

    @Override // org.aspectj.weaver.reflect.ReflectionBasedReferenceTypeDelegate, org.aspectj.weaver.ReferenceTypeDelegate
    public ResolvedType[] getAnnotationTypes() {
        if (this.annotations == null) {
            this.annotations = this.annotationFinder.getAnnotations(getBaseClass(), getWorld());
        }
        return this.annotations;
    }

    @Override // org.aspectj.weaver.reflect.ReflectionBasedReferenceTypeDelegate, org.aspectj.weaver.ReferenceTypeDelegate
    public boolean hasAnnotations() {
        if (this.annotations == null) {
            this.annotations = this.annotationFinder.getAnnotations(getBaseClass(), getWorld());
        }
        return this.annotations.length != 0;
    }

    @Override // org.aspectj.weaver.reflect.ReflectionBasedReferenceTypeDelegate, org.aspectj.weaver.ReferenceTypeDelegate
    public boolean hasAnnotation(UnresolvedType ofType) {
        ResolvedType[] myAnns = getAnnotationTypes();
        ResolvedType toLookFor = ofType.resolve(getWorld());
        for (ResolvedType resolvedType : myAnns) {
            if (resolvedType == toLookFor) {
                return true;
            }
        }
        return false;
    }

    @Override // org.aspectj.weaver.reflect.ReflectionBasedReferenceTypeDelegate, org.aspectj.weaver.ReferenceTypeDelegate
    public ResolvedMember[] getDeclaredFields() {
        if (this.fields == null) {
            Field[] reflectFields = this.myType.getDeclaredFields();
            ResolvedMember[] rFields = new ResolvedMember[reflectFields.length];
            for (int i = 0; i < reflectFields.length; i++) {
                rFields[i] = createGenericFieldMember(reflectFields[i]);
            }
            this.fields = rFields;
        }
        return this.fields;
    }

    @Override // org.aspectj.weaver.reflect.ReflectionBasedReferenceTypeDelegate, org.aspectj.weaver.ReferenceTypeDelegate
    public String getDeclaredGenericSignature() {
        if (this.genericSignature != null || isGeneric()) {
        }
        return this.genericSignature;
    }

    @Override // org.aspectj.weaver.reflect.ReflectionBasedReferenceTypeDelegate, org.aspectj.weaver.ReferenceTypeDelegate
    public ResolvedType[] getDeclaredInterfaces() {
        if (this.superInterfaces == null) {
            Type[] genericInterfaces = getBaseClass().getGenericInterfaces();
            this.superInterfaces = this.typeConverter.fromTypes(genericInterfaces);
        }
        return this.superInterfaces;
    }

    @Override // org.aspectj.weaver.reflect.ReflectionBasedReferenceTypeDelegate, org.aspectj.weaver.ReferenceTypeDelegate
    public ResolvedType getSuperclass() {
        if (this.superclass == null && getBaseClass() != Object.class) {
            Type t = getBaseClass().getGenericSuperclass();
            if (t != null) {
                this.superclass = this.typeConverter.fromType(t);
            }
            if (t == null) {
                this.superclass = getWorld().resolve(UnresolvedType.OBJECT);
            }
        }
        return this.superclass;
    }

    @Override // org.aspectj.weaver.reflect.ReflectionBasedReferenceTypeDelegate, org.aspectj.weaver.ReferenceTypeDelegate
    public TypeVariable[] getTypeVariables() {
        TypeVariable[] workInProgressSetOfVariables = getResolvedTypeX().getWorld().getTypeVariablesCurrentlyBeingProcessed(getBaseClass());
        if (workInProgressSetOfVariables != null) {
            return workInProgressSetOfVariables;
        }
        if (this.typeVariables == null) {
            java.lang.reflect.TypeVariable[] tVars = getBaseClass().getTypeParameters();
            TypeVariable[] rTypeVariables = new TypeVariable[tVars.length];
            for (int i = 0; i < tVars.length; i++) {
                rTypeVariables[i] = new TypeVariable(tVars[i].getName());
            }
            getResolvedTypeX().getWorld().recordTypeVariablesCurrentlyBeingProcessed(getBaseClass(), rTypeVariables);
            for (int i2 = 0; i2 < tVars.length; i2++) {
                TypeVariableReferenceType tvrt = (TypeVariableReferenceType) this.typeConverter.fromType(tVars[i2]);
                TypeVariable tv = tvrt.getTypeVariable();
                rTypeVariables[i2].setSuperclass(tv.getSuperclass());
                rTypeVariables[i2].setAdditionalInterfaceBounds(tv.getSuperInterfaces());
                rTypeVariables[i2].setDeclaringElement(tv.getDeclaringElement());
                rTypeVariables[i2].setDeclaringElementKind(tv.getDeclaringElementKind());
                rTypeVariables[i2].setRank(tv.getRank());
            }
            this.typeVariables = rTypeVariables;
            getResolvedTypeX().getWorld().forgetTypeVariablesCurrentlyBeingProcessed(getBaseClass());
        }
        return this.typeVariables;
    }

    @Override // org.aspectj.weaver.reflect.ReflectionBasedReferenceTypeDelegate, org.aspectj.weaver.ReferenceTypeDelegate
    public ResolvedMember[] getDeclaredMethods() {
        if (this.methods == null) {
            Method[] reflectMethods = this.myType.getDeclaredMethods();
            Constructor[] reflectCons = this.myType.getDeclaredConstructors();
            ResolvedMember[] rMethods = new ResolvedMember[reflectMethods.length + reflectCons.length];
            for (int i = 0; i < reflectMethods.length; i++) {
                rMethods[i] = createGenericMethodMember(reflectMethods[i]);
            }
            for (int i2 = 0; i2 < reflectCons.length; i2++) {
                rMethods[i2 + reflectMethods.length] = createGenericConstructorMember(reflectCons[i2]);
            }
            this.methods = rMethods;
        }
        return this.methods;
    }

    public ResolvedType getGenericResolvedType() {
        ResolvedType rt = getResolvedTypeX();
        if (rt.isParameterizedType() || rt.isRawType()) {
            return rt.getGenericType();
        }
        return rt;
    }

    private ResolvedMember createGenericMethodMember(Method forMethod) {
        ReflectionBasedResolvedMemberImpl ret = new ReflectionBasedResolvedMemberImpl(Member.METHOD, getGenericResolvedType(), forMethod.getModifiers(), this.typeConverter.fromType(forMethod.getReturnType()), forMethod.getName(), this.typeConverter.fromTypes(forMethod.getParameterTypes()), this.typeConverter.fromTypes(forMethod.getExceptionTypes()), forMethod);
        ret.setAnnotationFinder(this.annotationFinder);
        ret.setGenericSignatureInformationProvider(new Java15GenericSignatureInformationProvider(getWorld()));
        return ret;
    }

    private ResolvedMember createGenericConstructorMember(Constructor forConstructor) {
        ReflectionBasedResolvedMemberImpl ret = new ReflectionBasedResolvedMemberImpl(Member.METHOD, getGenericResolvedType(), forConstructor.getModifiers(), UnresolvedType.VOID, "<init>", this.typeConverter.fromTypes(forConstructor.getParameterTypes()), this.typeConverter.fromTypes(forConstructor.getExceptionTypes()), forConstructor);
        ret.setAnnotationFinder(this.annotationFinder);
        ret.setGenericSignatureInformationProvider(new Java15GenericSignatureInformationProvider(getWorld()));
        return ret;
    }

    private ResolvedMember createGenericFieldMember(Field forField) {
        ReflectionBasedResolvedMemberImpl ret = new ReflectionBasedResolvedMemberImpl(Member.FIELD, getGenericResolvedType(), forField.getModifiers(), this.typeConverter.fromType(forField.getType()), forField.getName(), new UnresolvedType[0], forField);
        ret.setAnnotationFinder(this.annotationFinder);
        ret.setGenericSignatureInformationProvider(new Java15GenericSignatureInformationProvider(getWorld()));
        return ret;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // org.aspectj.weaver.reflect.ReflectionBasedReferenceTypeDelegate, org.aspectj.weaver.ReferenceTypeDelegate
    public ResolvedMember[] getDeclaredPointcuts() throws SecurityException {
        InternalUseOnlyPointcutParser parser;
        if (this.pointcuts == null) {
            Pointcut[] pcs = this.myType.getDeclaredPointcuts();
            this.pointcuts = new ResolvedMember[pcs.length];
            World world = getWorld();
            if (world instanceof ReflectionWorld) {
                parser = new InternalUseOnlyPointcutParser(this.classLoaderReference.getClassLoader(), (ReflectionWorld) getWorld());
            } else {
                parser = new InternalUseOnlyPointcutParser(this.classLoaderReference.getClassLoader());
            }
            Set additionalPointcutHandlers = world.getRegisteredPointcutHandlers();
            for (PointcutDesignatorHandler handler : additionalPointcutHandlers) {
                parser.registerPointcutDesignatorHandler(handler);
            }
            for (int i = 0; i < pcs.length; i++) {
                AjType<?>[] ptypes = pcs[i].getParameterTypes();
                UnresolvedType[] weaverPTypes = new UnresolvedType[ptypes.length];
                for (int j = 0; j < weaverPTypes.length; j++) {
                    weaverPTypes[j] = this.typeConverter.fromType(ptypes[j].getJavaClass());
                }
                this.pointcuts[i] = new DeferredResolvedPointcutDefinition(getResolvedTypeX(), pcs[i].getModifiers(), pcs[i].getName(), weaverPTypes);
            }
            PointcutParameter[] pointcutParameterArr = new PointcutParameter[pcs.length];
            for (int i2 = 0; i2 < pcs.length; i2++) {
                AjType<?>[] ptypes2 = pcs[i2].getParameterTypes();
                String[] pnames = pcs[i2].getParameterNames();
                if (pnames.length != ptypes2.length) {
                    pnames = tryToDiscoverParameterNames(pcs[i2]);
                    if (pnames == null || pnames.length != ptypes2.length) {
                        throw new IllegalStateException("Required parameter names not available when parsing pointcut " + pcs[i2].getName() + " in type " + getResolvedTypeX().getName());
                    }
                }
                pointcutParameterArr[i2] = new PointcutParameter[ptypes2.length];
                for (int j2 = 0; j2 < pointcutParameterArr[i2].length; j2++) {
                    pointcutParameterArr[i2][j2] = parser.createPointcutParameter(pnames[j2], ptypes2[j2].getJavaClass());
                }
                String pcExpr = pcs[i2].getPointcutExpression().toString();
                org.aspectj.weaver.patterns.Pointcut pc = parser.resolvePointcutExpression(pcExpr, getBaseClass(), pointcutParameterArr[i2]);
                ((ResolvedPointcutDefinition) this.pointcuts[i2]).setParameterNames(pnames);
                ((ResolvedPointcutDefinition) this.pointcuts[i2]).setPointcut(pc);
            }
            for (int i3 = 0; i3 < this.pointcuts.length; i3++) {
                ResolvedPointcutDefinition rpd = (ResolvedPointcutDefinition) this.pointcuts[i3];
                rpd.setPointcut(parser.concretizePointcutExpression(rpd.getPointcut(), getBaseClass(), pointcutParameterArr[i3]));
            }
        }
        return this.pointcuts;
    }

    private String[] tryToDiscoverParameterNames(Pointcut pcut) throws SecurityException {
        Method[] ms = pcut.getDeclaringType().getJavaClass().getDeclaredMethods();
        for (Method m : ms) {
            if (m.getName().equals(pcut.getName())) {
                return this.argNameFinder.getParameterNames(m);
            }
        }
        return null;
    }

    @Override // org.aspectj.weaver.reflect.ReflectionBasedReferenceTypeDelegate, org.aspectj.weaver.ReferenceTypeDelegate
    public boolean isAnnotation() {
        return getBaseClass().isAnnotation();
    }

    @Override // org.aspectj.weaver.reflect.ReflectionBasedReferenceTypeDelegate, org.aspectj.weaver.ReferenceTypeDelegate
    public boolean isAnnotationStyleAspect() {
        return getBaseClass().isAnnotationPresent(Aspect.class);
    }

    @Override // org.aspectj.weaver.reflect.ReflectionBasedReferenceTypeDelegate, org.aspectj.weaver.ReferenceTypeDelegate
    public boolean isAnnotationWithRuntimeRetention() {
        if (isAnnotation() && getBaseClass().isAnnotationPresent(Retention.class)) {
            Retention retention = (Retention) getBaseClass().getAnnotation(Retention.class);
            RetentionPolicy policy = retention.value();
            return policy == RetentionPolicy.RUNTIME;
        }
        return false;
    }

    @Override // org.aspectj.weaver.reflect.ReflectionBasedReferenceTypeDelegate, org.aspectj.weaver.ReferenceTypeDelegate
    public boolean isAspect() {
        return this.myType.isAspect();
    }

    @Override // org.aspectj.weaver.reflect.ReflectionBasedReferenceTypeDelegate, org.aspectj.weaver.ReferenceTypeDelegate
    public boolean isEnum() {
        return getBaseClass().isEnum();
    }

    @Override // org.aspectj.weaver.reflect.ReflectionBasedReferenceTypeDelegate, org.aspectj.weaver.ReferenceTypeDelegate
    public boolean isGeneric() {
        return getBaseClass().getTypeParameters().length > 0;
    }

    @Override // org.aspectj.weaver.reflect.ReflectionBasedReferenceTypeDelegate, org.aspectj.weaver.ReferenceTypeDelegate
    public boolean isAnonymous() {
        return this.myClass.isAnonymousClass();
    }

    @Override // org.aspectj.weaver.reflect.ReflectionBasedReferenceTypeDelegate, org.aspectj.weaver.ReferenceTypeDelegate
    public boolean isNested() {
        return this.myClass.isMemberClass();
    }

    @Override // org.aspectj.weaver.reflect.ReflectionBasedReferenceTypeDelegate, org.aspectj.weaver.ReferenceTypeDelegate
    public ResolvedType getOuterClass() {
        return ReflectionBasedReferenceTypeDelegateFactory.resolveTypeInWorld(this.myClass.getEnclosingClass(), this.world);
    }
}
