package org.aspectj.weaver;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.aspectj.bridge.ISourceLocation;
import org.aspectj.weaver.ResolvedType;
import org.aspectj.weaver.UnresolvedType;
import org.aspectj.weaver.World;
import org.aspectj.weaver.patterns.Declare;
import org.aspectj.weaver.patterns.PerClause;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/ReferenceType.class */
public class ReferenceType extends ResolvedType {
    public static final ReferenceType[] EMPTY_ARRAY = new ReferenceType[0];
    private final List<WeakReference<ReferenceType>> derivativeTypes;
    ReferenceType genericType;
    ReferenceType rawType;
    ReferenceTypeDelegate delegate;
    int startPos;
    int endPos;
    ResolvedMember[] parameterizedMethods;
    ResolvedMember[] parameterizedFields;
    ResolvedMember[] parameterizedPointcuts;
    WeakReference<ResolvedType[]> parameterizedInterfaces;
    Collection<Declare> parameterizedDeclares;
    private ResolvedType[] annotationTypes;
    private AnnotationAJ[] annotations;
    private ResolvedType newSuperclass;
    private ResolvedType[] newInterfaces;
    WeakReference<ResolvedType> superclassReference;

    public ReferenceType(String signature, World world) {
        super(signature, world);
        this.derivativeTypes = new ArrayList();
        this.genericType = null;
        this.rawType = null;
        this.delegate = null;
        this.startPos = 0;
        this.endPos = 0;
        this.parameterizedMethods = null;
        this.parameterizedFields = null;
        this.parameterizedPointcuts = null;
        this.parameterizedInterfaces = new WeakReference<>(null);
        this.parameterizedDeclares = null;
        this.annotationTypes = null;
        this.annotations = null;
        this.superclassReference = new WeakReference<>(null);
    }

    public ReferenceType(String signature, String signatureErasure, World world) {
        super(signature, signatureErasure, world);
        this.derivativeTypes = new ArrayList();
        this.genericType = null;
        this.rawType = null;
        this.delegate = null;
        this.startPos = 0;
        this.endPos = 0;
        this.parameterizedMethods = null;
        this.parameterizedFields = null;
        this.parameterizedPointcuts = null;
        this.parameterizedInterfaces = new WeakReference<>(null);
        this.parameterizedDeclares = null;
        this.annotationTypes = null;
        this.annotations = null;
        this.superclassReference = new WeakReference<>(null);
    }

    public static ReferenceType fromTypeX(UnresolvedType tx, World world) {
        ReferenceType rt = new ReferenceType(tx.getErasureSignature(), world);
        rt.typeKind = tx.typeKind;
        return rt;
    }

    public ReferenceType(ResolvedType theGenericType, ResolvedType[] theParameters, World aWorld) {
        super(makeParameterizedSignature(theGenericType, theParameters), theGenericType.signatureErasure, aWorld);
        this.derivativeTypes = new ArrayList();
        this.genericType = null;
        this.rawType = null;
        this.delegate = null;
        this.startPos = 0;
        this.endPos = 0;
        this.parameterizedMethods = null;
        this.parameterizedFields = null;
        this.parameterizedPointcuts = null;
        this.parameterizedInterfaces = new WeakReference<>(null);
        this.parameterizedDeclares = null;
        this.annotationTypes = null;
        this.annotations = null;
        this.superclassReference = new WeakReference<>(null);
        ReferenceType genericReferenceType = (ReferenceType) theGenericType;
        this.typeParameters = theParameters;
        this.genericType = genericReferenceType;
        this.typeKind = UnresolvedType.TypeKind.PARAMETERIZED;
        this.delegate = genericReferenceType.getDelegate();
        genericReferenceType.addDependentType(this);
    }

    synchronized void addDependentType(ReferenceType dependent) {
        synchronized (this.derivativeTypes) {
            this.derivativeTypes.add(new WeakReference<>(dependent));
        }
    }

    public void checkDuplicates(ReferenceType newRt) {
        synchronized (this.derivativeTypes) {
            List<WeakReference<ReferenceType>> forRemoval = new ArrayList<>();
            for (WeakReference<ReferenceType> derivativeTypeReference : this.derivativeTypes) {
                ReferenceType derivativeType = derivativeTypeReference.get();
                if (derivativeType == null) {
                    forRemoval.add(derivativeTypeReference);
                } else if (derivativeType.getTypekind() == newRt.getTypekind()) {
                    if (equal2(newRt.getTypeParameters(), derivativeType.getTypeParameters()) && World.TypeMap.useExpendableMap) {
                        throw new IllegalStateException();
                    }
                }
            }
            this.derivativeTypes.removeAll(forRemoval);
        }
    }

    private boolean equal2(UnresolvedType[] typeParameters, UnresolvedType[] resolvedParameters) {
        if (typeParameters.length != resolvedParameters.length) {
            return false;
        }
        int len = typeParameters.length;
        for (int p = 0; p < len; p++) {
            if (!typeParameters[p].equals(resolvedParameters[p])) {
                return false;
            }
        }
        return true;
    }

    @Override // org.aspectj.weaver.ResolvedType
    public String getSignatureForAttribute() {
        if (this.genericType == null || this.typeParameters == null) {
            return getSignature();
        }
        return makeDeclaredSignature(this.genericType, this.typeParameters);
    }

    public ReferenceType(UnresolvedType genericType, World world) {
        super(genericType.getSignature(), world);
        this.derivativeTypes = new ArrayList();
        this.genericType = null;
        this.rawType = null;
        this.delegate = null;
        this.startPos = 0;
        this.endPos = 0;
        this.parameterizedMethods = null;
        this.parameterizedFields = null;
        this.parameterizedPointcuts = null;
        this.parameterizedInterfaces = new WeakReference<>(null);
        this.parameterizedDeclares = null;
        this.annotationTypes = null;
        this.annotations = null;
        this.superclassReference = new WeakReference<>(null);
        this.typeKind = UnresolvedType.TypeKind.GENERIC;
        this.typeVariables = genericType.typeVariables;
    }

    @Override // org.aspectj.weaver.ResolvedType
    public boolean isClass() {
        return getDelegate().isClass();
    }

    @Override // org.aspectj.weaver.ResolvedType
    public int getCompilerVersion() {
        return getDelegate().getCompilerVersion();
    }

    @Override // org.aspectj.weaver.UnresolvedType
    public boolean isGenericType() {
        return (isParameterizedType() || isRawType() || !getDelegate().isGeneric()) ? false : true;
    }

    public String getGenericSignature() {
        String sig = getDelegate().getDeclaredGenericSignature();
        return sig == null ? "" : sig;
    }

    @Override // org.aspectj.weaver.ResolvedType
    public AnnotationAJ[] getAnnotations() {
        return getDelegate().getAnnotations();
    }

    @Override // org.aspectj.weaver.ResolvedType
    public boolean hasAnnotations() {
        return getDelegate().hasAnnotations();
    }

    @Override // org.aspectj.weaver.ResolvedType
    public void addAnnotation(AnnotationAJ annotationX) {
        if (this.annotations == null) {
            this.annotations = new AnnotationAJ[]{annotationX};
        } else {
            AnnotationAJ[] newAnnotations = new AnnotationAJ[this.annotations.length + 1];
            System.arraycopy(this.annotations, 0, newAnnotations, 1, this.annotations.length);
            newAnnotations[0] = annotationX;
            this.annotations = newAnnotations;
        }
        addAnnotationType(annotationX.getType());
    }

    public boolean hasAnnotation(UnresolvedType ofType) {
        boolean onDelegate = getDelegate().hasAnnotation(ofType);
        if (onDelegate) {
            return true;
        }
        if (this.annotationTypes != null) {
            for (int i = 0; i < this.annotationTypes.length; i++) {
                if (this.annotationTypes[i].equals(ofType)) {
                    return true;
                }
            }
            return false;
        }
        return false;
    }

    private void addAnnotationType(ResolvedType ofType) {
        if (this.annotationTypes == null) {
            this.annotationTypes = new ResolvedType[1];
            this.annotationTypes[0] = ofType;
        } else {
            ResolvedType[] newAnnotationTypes = new ResolvedType[this.annotationTypes.length + 1];
            System.arraycopy(this.annotationTypes, 0, newAnnotationTypes, 1, this.annotationTypes.length);
            newAnnotationTypes[0] = ofType;
            this.annotationTypes = newAnnotationTypes;
        }
    }

    @Override // org.aspectj.weaver.ResolvedType, org.aspectj.weaver.AnnotatedElement
    public ResolvedType[] getAnnotationTypes() {
        if (getDelegate() == null) {
            throw new BCException("Unexpected null delegate for type " + getName());
        }
        if (this.annotationTypes == null) {
            return getDelegate().getAnnotationTypes();
        }
        ResolvedType[] delegateAnnotationTypes = getDelegate().getAnnotationTypes();
        ResolvedType[] result = new ResolvedType[this.annotationTypes.length + delegateAnnotationTypes.length];
        System.arraycopy(delegateAnnotationTypes, 0, result, 0, delegateAnnotationTypes.length);
        System.arraycopy(this.annotationTypes, 0, result, delegateAnnotationTypes.length, this.annotationTypes.length);
        return result;
    }

    @Override // org.aspectj.weaver.UnresolvedType
    public String getNameAsIdentifier() {
        return getRawName().replace('.', '_');
    }

    @Override // org.aspectj.weaver.ResolvedType, org.aspectj.weaver.AnnotatedElement
    public AnnotationAJ getAnnotationOfType(UnresolvedType ofType) {
        AnnotationAJ[] axs = getDelegate().getAnnotations();
        if (axs != null) {
            for (int i = 0; i < axs.length; i++) {
                if (axs[i].getTypeSignature().equals(ofType.getSignature())) {
                    return axs[i];
                }
            }
        }
        if (this.annotations != null) {
            String searchSig = ofType.getSignature();
            for (int i2 = 0; i2 < this.annotations.length; i2++) {
                if (this.annotations[i2].getTypeSignature().equals(searchSig)) {
                    return this.annotations[i2];
                }
            }
            return null;
        }
        return null;
    }

    @Override // org.aspectj.weaver.ResolvedType
    public boolean isAspect() {
        return getDelegate().isAspect();
    }

    @Override // org.aspectj.weaver.ResolvedType
    public boolean isAnnotationStyleAspect() {
        return getDelegate().isAnnotationStyleAspect();
    }

    @Override // org.aspectj.weaver.ResolvedType
    public boolean isEnum() {
        return getDelegate().isEnum();
    }

    @Override // org.aspectj.weaver.ResolvedType
    public boolean isAnnotation() {
        return getDelegate().isAnnotation();
    }

    @Override // org.aspectj.weaver.ResolvedType
    public boolean isAnonymous() {
        return getDelegate().isAnonymous();
    }

    @Override // org.aspectj.weaver.ResolvedType
    public boolean isNested() {
        return getDelegate().isNested();
    }

    @Override // org.aspectj.weaver.ResolvedType
    public ResolvedType getOuterClass() {
        return getDelegate().getOuterClass();
    }

    public String getRetentionPolicy() {
        return getDelegate().getRetentionPolicy();
    }

    @Override // org.aspectj.weaver.ResolvedType
    public boolean isAnnotationWithRuntimeRetention() {
        return getDelegate().isAnnotationWithRuntimeRetention();
    }

    @Override // org.aspectj.weaver.ResolvedType
    public boolean canAnnotationTargetType() {
        return getDelegate().canAnnotationTargetType();
    }

    @Override // org.aspectj.weaver.ResolvedType
    public AnnotationTargetKind[] getAnnotationTargetKinds() {
        return getDelegate().getAnnotationTargetKinds();
    }

    @Override // org.aspectj.weaver.ResolvedType
    public boolean isCoerceableFrom(ResolvedType o) {
        ResolvedType other = o.resolve(this.world);
        if (isAssignableFrom(other) || other.isAssignableFrom(this)) {
            return true;
        }
        if (isParameterizedType() && other.isParameterizedType()) {
            return isCoerceableFromParameterizedType(other);
        }
        if (isParameterizedType() && other.isRawType()) {
            return ((ReferenceType) getRawType()).isCoerceableFrom(other.getGenericType());
        }
        if (isRawType() && other.isParameterizedType()) {
            return getGenericType().isCoerceableFrom(other.getRawType());
        }
        if ((!isInterface() && !other.isInterface()) || isFinal() || other.isFinal()) {
            return false;
        }
        ResolvedMember[] a = getDeclaredMethods();
        ResolvedMember[] b = other.getDeclaredMethods();
        for (ResolvedMember resolvedMember : a) {
            for (ResolvedMember resolvedMember2 : b) {
                if (!resolvedMember2.isCompatibleWith(resolvedMember)) {
                    return false;
                }
            }
        }
        return true;
    }

    private final boolean isCoerceableFromParameterizedType(ResolvedType other) {
        if (!other.isParameterizedType()) {
            return false;
        }
        ResolvedType myRawType = getRawType();
        ResolvedType theirRawType = other.getRawType();
        if ((myRawType == theirRawType || myRawType.isCoerceableFrom(theirRawType)) && getTypeParameters().length == other.getTypeParameters().length) {
            ResolvedType[] myTypeParameters = getResolvedTypeParameters();
            ResolvedType[] theirTypeParameters = other.getResolvedTypeParameters();
            for (int i = 0; i < myTypeParameters.length; i++) {
                if (myTypeParameters[i] != theirTypeParameters[i]) {
                    if (myTypeParameters[i].isGenericWildcard()) {
                        BoundedReferenceType wildcard = (BoundedReferenceType) myTypeParameters[i];
                        if (!wildcard.canBeCoercedTo(theirTypeParameters[i])) {
                            return false;
                        }
                    } else if (myTypeParameters[i].isTypeVariableReference()) {
                        TypeVariableReferenceType tvrt = (TypeVariableReferenceType) myTypeParameters[i];
                        TypeVariable tv = tvrt.getTypeVariable();
                        tv.resolve(this.world);
                        if (!tv.canBeBoundTo(theirTypeParameters[i])) {
                            return false;
                        }
                    } else if (theirTypeParameters[i].isTypeVariableReference()) {
                        TypeVariableReferenceType tvrt2 = (TypeVariableReferenceType) theirTypeParameters[i];
                        TypeVariable tv2 = tvrt2.getTypeVariable();
                        tv2.resolve(this.world);
                        if (!tv2.canBeBoundTo(myTypeParameters[i])) {
                            return false;
                        }
                    } else if (theirTypeParameters[i].isGenericWildcard()) {
                        BoundedReferenceType wildcard2 = (BoundedReferenceType) theirTypeParameters[i];
                        if (!wildcard2.canBeCoercedTo(myTypeParameters[i])) {
                            return false;
                        }
                    } else {
                        return false;
                    }
                }
            }
            return true;
        }
        return false;
    }

    @Override // org.aspectj.weaver.ResolvedType
    public boolean isAssignableFrom(ResolvedType other) {
        return isAssignableFrom(other, false);
    }

    /* JADX WARN: Code restructure failed: missing block: B:87:0x01ba, code lost:
    
        r12 = false;
     */
    /* JADX WARN: Multi-variable type inference failed */
    @Override // org.aspectj.weaver.ResolvedType
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public boolean isAssignableFrom(org.aspectj.weaver.ResolvedType r5, boolean r6) {
        /*
            Method dump skipped, instructions count: 850
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: org.aspectj.weaver.ReferenceType.isAssignableFrom(org.aspectj.weaver.ResolvedType, boolean):boolean");
    }

    @Override // org.aspectj.weaver.ResolvedType
    public ISourceContext getSourceContext() {
        return getDelegate().getSourceContext();
    }

    @Override // org.aspectj.weaver.ResolvedType
    public ISourceLocation getSourceLocation() {
        ISourceContext isc = getDelegate().getSourceContext();
        return isc.makeSourceLocation(new Position(this.startPos, this.endPos));
    }

    @Override // org.aspectj.weaver.ResolvedType
    public boolean isExposedToWeaver() {
        return getDelegate() == null || this.delegate.isExposedToWeaver();
    }

    @Override // org.aspectj.weaver.ResolvedType
    public WeaverStateInfo getWeaverState() {
        return getDelegate().getWeaverState();
    }

    @Override // org.aspectj.weaver.ResolvedType
    public ResolvedMember[] getDeclaredFields() {
        if (this.parameterizedFields != null) {
            return this.parameterizedFields;
        }
        if (isParameterizedType() || isRawType()) {
            ResolvedMember[] delegateFields = getDelegate().getDeclaredFields();
            this.parameterizedFields = new ResolvedMember[delegateFields.length];
            for (int i = 0; i < delegateFields.length; i++) {
                this.parameterizedFields[i] = delegateFields[i].parameterizedWith(getTypesForMemberParameterization(), this, isParameterizedType());
            }
            return this.parameterizedFields;
        }
        return getDelegate().getDeclaredFields();
    }

    @Override // org.aspectj.weaver.ResolvedType
    public ResolvedType[] getDeclaredInterfaces() {
        ResolvedType[] interfaces = this.parameterizedInterfaces.get();
        if (interfaces != null) {
            return interfaces;
        }
        ResolvedType[] delegateInterfaces = getDelegate().getDeclaredInterfaces();
        if (isRawType()) {
            if (this.newInterfaces != null) {
                throw new IllegalStateException("The raw type should never be accumulating new interfaces, they should be on the generic type.  Type is " + getName());
            }
            ResolvedType[] newInterfacesFromGenericType = this.genericType.newInterfaces;
            if (newInterfacesFromGenericType != null) {
                ResolvedType[] extraInterfaces = new ResolvedType[delegateInterfaces.length + newInterfacesFromGenericType.length];
                System.arraycopy(delegateInterfaces, 0, extraInterfaces, 0, delegateInterfaces.length);
                System.arraycopy(newInterfacesFromGenericType, 0, extraInterfaces, delegateInterfaces.length, newInterfacesFromGenericType.length);
                delegateInterfaces = extraInterfaces;
            }
        } else if (this.newInterfaces != null) {
            ResolvedType[] extraInterfaces2 = new ResolvedType[delegateInterfaces.length + this.newInterfaces.length];
            System.arraycopy(delegateInterfaces, 0, extraInterfaces2, 0, delegateInterfaces.length);
            System.arraycopy(this.newInterfaces, 0, extraInterfaces2, delegateInterfaces.length, this.newInterfaces.length);
            delegateInterfaces = extraInterfaces2;
        }
        if (isParameterizedType()) {
            ResolvedType[] interfaces2 = new ResolvedType[delegateInterfaces.length];
            for (int i = 0; i < delegateInterfaces.length; i++) {
                if (delegateInterfaces[i].isParameterizedType()) {
                    interfaces2[i] = delegateInterfaces[i].parameterize(getMemberParameterizationMap()).resolve(this.world);
                } else {
                    interfaces2[i] = delegateInterfaces[i];
                }
            }
            this.parameterizedInterfaces = new WeakReference<>(interfaces2);
            return interfaces2;
        }
        if (isRawType()) {
            UnresolvedType[] paramTypes = getTypesForMemberParameterization();
            ResolvedType[] interfaces3 = new ResolvedType[delegateInterfaces.length];
            int max = interfaces3.length;
            for (int i2 = 0; i2 < max; i2++) {
                interfaces3[i2] = delegateInterfaces[i2];
                if (interfaces3[i2].isGenericType()) {
                    interfaces3[i2] = interfaces3[i2].getRawType().resolve(getWorld());
                } else if (interfaces3[i2].isParameterizedType()) {
                    UnresolvedType[] toUseForParameterization = determineThoseTypesToUse(interfaces3[i2], paramTypes);
                    interfaces3[i2] = interfaces3[i2].parameterizedWith(toUseForParameterization);
                }
            }
            this.parameterizedInterfaces = new WeakReference<>(interfaces3);
            return interfaces3;
        }
        if (getDelegate().isCacheable()) {
            this.parameterizedInterfaces = new WeakReference<>(delegateInterfaces);
        }
        return delegateInterfaces;
    }

    /* JADX WARN: Multi-variable type inference failed */
    private UnresolvedType[] determineThoseTypesToUse(ResolvedType parameterizedInterface, UnresolvedType[] paramTypes) {
        UnresolvedType[] typeParameters = parameterizedInterface.getTypeParameters();
        UnresolvedType[] unresolvedTypeArr = new UnresolvedType[typeParameters.length];
        for (int i = 0; i < typeParameters.length; i++) {
            ArrayReferenceType arrayReferenceType = typeParameters[i];
            if (arrayReferenceType.isTypeVariableReference()) {
                TypeVariableReference tvrt = (TypeVariableReference) arrayReferenceType;
                TypeVariable tv = tvrt.getTypeVariable();
                int rank = getRank(tv.getName());
                if (rank != -1) {
                    unresolvedTypeArr[i] = paramTypes[rank];
                } else {
                    unresolvedTypeArr[i] = typeParameters[i];
                }
            } else {
                unresolvedTypeArr[i] = typeParameters[i];
            }
        }
        return unresolvedTypeArr;
    }

    private int getRank(String tvname) {
        TypeVariable[] thisTypesTVars = getGenericType().getTypeVariables();
        for (int i = 0; i < thisTypesTVars.length; i++) {
            TypeVariable tv = thisTypesTVars[i];
            if (tv.getName().equals(tvname)) {
                return i;
            }
        }
        return -1;
    }

    @Override // org.aspectj.weaver.ResolvedType
    public ResolvedMember[] getDeclaredMethods() {
        if (this.parameterizedMethods != null) {
            return this.parameterizedMethods;
        }
        if (isParameterizedType() || isRawType()) {
            ResolvedMember[] delegateMethods = getDelegate().getDeclaredMethods();
            UnresolvedType[] parameters = getTypesForMemberParameterization();
            this.parameterizedMethods = new ResolvedMember[delegateMethods.length];
            for (int i = 0; i < delegateMethods.length; i++) {
                this.parameterizedMethods[i] = delegateMethods[i].parameterizedWith(parameters, this, isParameterizedType());
            }
            return this.parameterizedMethods;
        }
        return getDelegate().getDeclaredMethods();
    }

    @Override // org.aspectj.weaver.ResolvedType
    public ResolvedMember[] getDeclaredPointcuts() {
        if (this.parameterizedPointcuts != null) {
            return this.parameterizedPointcuts;
        }
        if (isParameterizedType()) {
            ResolvedMember[] delegatePointcuts = getDelegate().getDeclaredPointcuts();
            this.parameterizedPointcuts = new ResolvedMember[delegatePointcuts.length];
            for (int i = 0; i < delegatePointcuts.length; i++) {
                this.parameterizedPointcuts[i] = delegatePointcuts[i].parameterizedWith(getTypesForMemberParameterization(), this, isParameterizedType());
            }
            return this.parameterizedPointcuts;
        }
        return getDelegate().getDeclaredPointcuts();
    }

    private UnresolvedType[] getTypesForMemberParameterization() {
        UnresolvedType[] parameters = null;
        if (isParameterizedType()) {
            parameters = getTypeParameters();
        } else if (isRawType()) {
            TypeVariable[] tvs = getGenericType().getTypeVariables();
            parameters = new UnresolvedType[tvs.length];
            for (int i = 0; i < tvs.length; i++) {
                parameters[i] = tvs[i].getFirstBound();
            }
        }
        return parameters;
    }

    @Override // org.aspectj.weaver.UnresolvedType
    public TypeVariable[] getTypeVariables() {
        if (this.typeVariables == null) {
            this.typeVariables = getDelegate().getTypeVariables();
            for (int i = 0; i < this.typeVariables.length; i++) {
                this.typeVariables[i].resolve(this.world);
            }
        }
        return this.typeVariables;
    }

    @Override // org.aspectj.weaver.ResolvedType
    public PerClause getPerClause() {
        PerClause pclause = getDelegate().getPerClause();
        if (pclause != null && isParameterizedType()) {
            Map<String, UnresolvedType> parameterizationMap = getAjMemberParameterizationMap();
            pclause = (PerClause) pclause.parameterizeWith(parameterizationMap, this.world);
        }
        return pclause;
    }

    @Override // org.aspectj.weaver.ResolvedType
    public Collection<Declare> getDeclares() {
        Collection<Declare> declares;
        if (this.parameterizedDeclares != null) {
            return this.parameterizedDeclares;
        }
        if (ajMembersNeedParameterization()) {
            Collection<Declare> genericDeclares = getDelegate().getDeclares();
            this.parameterizedDeclares = new ArrayList();
            Map<String, UnresolvedType> parameterizationMap = getAjMemberParameterizationMap();
            for (Declare declareStatement : genericDeclares) {
                this.parameterizedDeclares.add(declareStatement.parameterizeWith(parameterizationMap, this.world));
            }
            declares = this.parameterizedDeclares;
        } else {
            declares = getDelegate().getDeclares();
        }
        for (Declare d : declares) {
            d.setDeclaringType(this);
        }
        return declares;
    }

    @Override // org.aspectj.weaver.ResolvedType
    public Collection<ConcreteTypeMunger> getTypeMungers() {
        return getDelegate().getTypeMungers();
    }

    @Override // org.aspectj.weaver.ResolvedType
    public Collection<ResolvedMember> getPrivilegedAccesses() {
        return getDelegate().getPrivilegedAccesses();
    }

    @Override // org.aspectj.weaver.ResolvedType
    public int getModifiers() {
        return getDelegate().getModifiers();
    }

    @Override // org.aspectj.weaver.ResolvedType
    public ResolvedType getSuperclass() {
        if (this.newSuperclass != null) {
            if (isParameterizedType() && this.newSuperclass.isParameterizedType()) {
                return this.newSuperclass.parameterize(getMemberParameterizationMap()).resolve(getWorld());
            }
            if (getDelegate().isCacheable()) {
                this.superclassReference = new WeakReference<>(null);
            }
            return this.newSuperclass;
        }
        try {
            this.world.setTypeVariableLookupScope(this);
            ResolvedType ret = getDelegate().getSuperclass();
            if (isParameterizedType() && ret.isParameterizedType()) {
                ret = ret.parameterize(getMemberParameterizationMap()).resolve(getWorld());
            }
            if (getDelegate().isCacheable()) {
                this.superclassReference = new WeakReference<>(ret);
            }
            return ret;
        } finally {
            this.world.setTypeVariableLookupScope(null);
        }
    }

    public ReferenceTypeDelegate getDelegate() {
        return this.delegate;
    }

    public void setDelegate(ReferenceTypeDelegate delegate) {
        if (this.delegate != null && this.delegate.copySourceContext() && this.delegate.getSourceContext() != SourceContextImpl.UNKNOWN_SOURCE_CONTEXT) {
            ((AbstractReferenceTypeDelegate) delegate).setSourceContext(this.delegate.getSourceContext());
        }
        this.delegate = delegate;
        synchronized (this.derivativeTypes) {
            List<WeakReference<ReferenceType>> forRemoval = new ArrayList<>();
            for (WeakReference<ReferenceType> derivativeRef : this.derivativeTypes) {
                ReferenceType derivative = derivativeRef.get();
                if (derivative != null) {
                    derivative.setDelegate(delegate);
                } else {
                    forRemoval.add(derivativeRef);
                }
            }
            this.derivativeTypes.removeAll(forRemoval);
        }
        if (isRawType() && getGenericType() != null) {
            ReferenceType genType = getGenericType();
            if (genType.getDelegate() != delegate) {
                genType.setDelegate(delegate);
            }
        }
        clearParameterizationCaches();
        ensureConsistent();
    }

    private void clearParameterizationCaches() {
        this.parameterizedFields = null;
        this.parameterizedInterfaces.clear();
        this.parameterizedMethods = null;
        this.parameterizedPointcuts = null;
        this.superclassReference = new WeakReference<>(null);
    }

    public int getEndPos() {
        return this.endPos;
    }

    public int getStartPos() {
        return this.startPos;
    }

    public void setEndPos(int endPos) {
        this.endPos = endPos;
    }

    public void setStartPos(int startPos) {
        this.startPos = startPos;
    }

    @Override // org.aspectj.weaver.ResolvedType
    public boolean doesNotExposeShadowMungers() {
        return getDelegate().doesNotExposeShadowMungers();
    }

    public String getDeclaredGenericSignature() {
        return getDelegate().getDeclaredGenericSignature();
    }

    public void setGenericType(ReferenceType rt) {
        this.genericType = rt;
        if (this.typeKind == UnresolvedType.TypeKind.SIMPLE) {
            this.typeKind = UnresolvedType.TypeKind.RAW;
            this.signatureErasure = this.signature;
            if (this.newInterfaces != null) {
                throw new IllegalStateException("Simple type promoted to raw, but simple type had new interfaces/superclass.  Type is " + getName());
            }
        }
        if (this.typeKind == UnresolvedType.TypeKind.RAW) {
            this.genericType.addDependentType(this);
        }
        if (isRawType()) {
            this.genericType.rawType = this;
        }
        if (isRawType() && rt.isRawType()) {
            new RuntimeException("PR341926 diagnostics: Incorrect setup for a generic type, raw type should not point to raw: " + getName()).printStackTrace();
        }
    }

    public void demoteToSimpleType() {
        this.genericType = null;
        this.typeKind = UnresolvedType.TypeKind.SIMPLE;
        this.signatureErasure = null;
    }

    @Override // org.aspectj.weaver.ResolvedType
    public ReferenceType getGenericType() {
        if (isGenericType()) {
            return this;
        }
        return this.genericType;
    }

    private static String makeParameterizedSignature(ResolvedType aGenericType, ResolvedType[] someParameters) {
        String rawSignature = aGenericType.getErasureSignature();
        StringBuffer ret = new StringBuffer();
        ret.append("P");
        ret.append(rawSignature.substring(1, rawSignature.length() - 1));
        ret.append("<");
        for (ResolvedType resolvedType : someParameters) {
            ret.append(resolvedType.getSignature());
        }
        ret.append(">;");
        return ret.toString();
    }

    private static String makeDeclaredSignature(ResolvedType aGenericType, UnresolvedType[] someParameters) {
        StringBuffer ret = new StringBuffer();
        String rawSig = aGenericType.getErasureSignature();
        ret.append(rawSig.substring(0, rawSig.length() - 1));
        ret.append("<");
        for (int i = 0; i < someParameters.length; i++) {
            if (someParameters[i] instanceof ReferenceType) {
                ret.append(((ReferenceType) someParameters[i]).getSignatureForAttribute());
            } else if (someParameters[i] instanceof ResolvedType.Primitive) {
                ret.append(((ResolvedType.Primitive) someParameters[i]).getSignatureForAttribute());
            } else {
                throw new IllegalStateException("DebugFor325731: expected a ReferenceType or Primitive but was " + someParameters[i] + " of type " + someParameters[i].getClass().getName());
            }
        }
        ret.append(">;");
        return ret.toString();
    }

    @Override // org.aspectj.weaver.ResolvedType
    public void ensureConsistent() {
        ReferenceType genericType;
        this.annotations = null;
        this.annotationTypes = null;
        this.newSuperclass = null;
        this.bits = 0;
        this.newInterfaces = null;
        this.typeVariables = null;
        this.parameterizedInterfaces.clear();
        this.superclassReference = new WeakReference<>(null);
        if (getDelegate() != null) {
            this.delegate.ensureConsistent();
        }
        if (isParameterizedOrRawType() && (genericType = getGenericType()) != null) {
            genericType.ensureConsistent();
        }
    }

    @Override // org.aspectj.weaver.ResolvedType
    public void addParent(ResolvedType newParent) {
        if (isRawType()) {
            throw new IllegalStateException("The raw type should never be accumulating new interfaces, they should be on the generic type.  Type is " + getName());
        }
        if (newParent.isClass()) {
            this.newSuperclass = newParent;
            this.superclassReference = new WeakReference<>(null);
            return;
        }
        if (this.newInterfaces == null) {
            this.newInterfaces = new ResolvedType[1];
            this.newInterfaces[0] = newParent;
        } else {
            ResolvedType[] existing = getDelegate().getDeclaredInterfaces();
            if (existing != null) {
                for (ResolvedType resolvedType : existing) {
                    if (resolvedType.equals(newParent)) {
                        return;
                    }
                }
            }
            ResolvedType[] newNewInterfaces = new ResolvedType[this.newInterfaces.length + 1];
            System.arraycopy(this.newInterfaces, 0, newNewInterfaces, 1, this.newInterfaces.length);
            newNewInterfaces[0] = newParent;
            this.newInterfaces = newNewInterfaces;
        }
        if (isGenericType()) {
            synchronized (this.derivativeTypes) {
                for (WeakReference<ReferenceType> derivativeTypeRef : this.derivativeTypes) {
                    ReferenceType derivativeType = derivativeTypeRef.get();
                    if (derivativeType != null) {
                        derivativeType.parameterizedInterfaces.clear();
                    }
                }
            }
        }
        this.parameterizedInterfaces.clear();
    }

    private boolean equal(UnresolvedType[] typeParameters, ResolvedType[] resolvedParameters) {
        if (typeParameters.length != resolvedParameters.length) {
            return false;
        }
        int len = typeParameters.length;
        for (int p = 0; p < len; p++) {
            if (!typeParameters[p].equals(resolvedParameters[p])) {
                return false;
            }
        }
        return true;
    }

    public ReferenceType findDerivativeType(ResolvedType[] typeParameters) {
        synchronized (this.derivativeTypes) {
            List<WeakReference<ReferenceType>> forRemoval = new ArrayList<>();
            for (WeakReference<ReferenceType> derivativeTypeRef : this.derivativeTypes) {
                ReferenceType derivativeType = derivativeTypeRef.get();
                if (derivativeType == null) {
                    forRemoval.add(derivativeTypeRef);
                } else if (!derivativeType.isRawType()) {
                    if (equal(derivativeType.typeParameters, typeParameters)) {
                        return derivativeType;
                    }
                }
            }
            this.derivativeTypes.removeAll(forRemoval);
            return null;
        }
    }

    public boolean hasNewInterfaces() {
        return this.newInterfaces != null;
    }
}
