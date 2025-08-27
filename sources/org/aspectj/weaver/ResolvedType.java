package org.aspectj.weaver;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import org.aspectj.bridge.AbortException;
import org.aspectj.bridge.IMessage;
import org.aspectj.bridge.ISourceLocation;
import org.aspectj.bridge.Message;
import org.aspectj.bridge.MessageUtil;
import org.aspectj.util.FuzzyBoolean;
import org.aspectj.weaver.AjAttribute;
import org.aspectj.weaver.Iterators;
import org.aspectj.weaver.UnresolvedType;
import org.aspectj.weaver.bcel.BcelWeaver;
import org.aspectj.weaver.patterns.Declare;
import org.aspectj.weaver.patterns.PerClause;
import org.springframework.beans.PropertyAccessor;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/ResolvedType.class */
public abstract class ResolvedType extends UnresolvedType implements AnnotatedElement {
    public static final ResolvedType[] EMPTY_RESOLVED_TYPE_ARRAY;
    public static final String PARAMETERIZED_TYPE_IDENTIFIER = "P";
    public ResolvedType[] temporaryAnnotationTypes;
    private ResolvedType[] resolvedTypeParams;
    private String binaryPath;
    protected World world;
    protected int bits;
    private static int AnnotationBitsInitialized;
    private static int AnnotationMarkedInherited;
    private static int MungersAnalyzed;
    private static int HasParentMunger;
    private static int TypeHierarchyCompleteBit;
    private static int GroovyObjectInitialized;
    private static int IsGroovyObject;
    protected static Set<String> validBoxing;
    private static final MethodGetter MethodGetterInstance;
    private static final MethodGetterIncludingItds MethodGetterWithItdsInstance;
    private static final PointcutGetter PointcutGetterInstance;
    private static final FieldGetter FieldGetterInstance;
    public CrosscuttingMembers crosscuttingMembers;
    public static final ResolvedType[] NONE;
    public static final ResolvedType[] EMPTY_ARRAY;
    public static final Missing MISSING;
    protected List<ConcreteTypeMunger> interTypeMungers;
    private FuzzyBoolean parameterizedWithTypeVariable;
    static final /* synthetic */ boolean $assertionsDisabled;

    public abstract ResolvedMember[] getDeclaredFields();

    public abstract ResolvedMember[] getDeclaredMethods();

    public abstract ResolvedType[] getDeclaredInterfaces();

    public abstract ResolvedMember[] getDeclaredPointcuts();

    public abstract ResolvedType getSuperclass();

    public abstract int getModifiers();

    public abstract ISourceContext getSourceContext();

    public abstract boolean isAssignableFrom(ResolvedType resolvedType);

    public abstract boolean isAssignableFrom(ResolvedType resolvedType, boolean z);

    public abstract boolean isCoerceableFrom(ResolvedType resolvedType);

    static {
        $assertionsDisabled = !ResolvedType.class.desiredAssertionStatus();
        EMPTY_RESOLVED_TYPE_ARRAY = new ResolvedType[0];
        AnnotationBitsInitialized = 1;
        AnnotationMarkedInherited = 2;
        MungersAnalyzed = 4;
        HasParentMunger = 8;
        TypeHierarchyCompleteBit = 16;
        GroovyObjectInitialized = 32;
        IsGroovyObject = 64;
        validBoxing = new HashSet();
        validBoxing.add("Ljava/lang/Byte;B");
        validBoxing.add("Ljava/lang/Character;C");
        validBoxing.add("Ljava/lang/Double;D");
        validBoxing.add("Ljava/lang/Float;F");
        validBoxing.add("Ljava/lang/Integer;I");
        validBoxing.add("Ljava/lang/Long;J");
        validBoxing.add("Ljava/lang/Short;S");
        validBoxing.add("Ljava/lang/Boolean;Z");
        validBoxing.add("BLjava/lang/Byte;");
        validBoxing.add("CLjava/lang/Character;");
        validBoxing.add("DLjava/lang/Double;");
        validBoxing.add("FLjava/lang/Float;");
        validBoxing.add("ILjava/lang/Integer;");
        validBoxing.add("JLjava/lang/Long;");
        validBoxing.add("SLjava/lang/Short;");
        validBoxing.add("ZLjava/lang/Boolean;");
        MethodGetterInstance = new MethodGetter();
        MethodGetterWithItdsInstance = new MethodGetterIncludingItds();
        PointcutGetterInstance = new PointcutGetter();
        FieldGetterInstance = new FieldGetter();
        NONE = new ResolvedType[0];
        EMPTY_ARRAY = NONE;
        MISSING = new Missing();
    }

    protected ResolvedType(String signature, World world) {
        super(signature);
        this.interTypeMungers = new ArrayList();
        this.parameterizedWithTypeVariable = FuzzyBoolean.MAYBE;
        this.world = world;
    }

    protected ResolvedType(String signature, String signatureErasure, World world) {
        super(signature, signatureErasure);
        this.interTypeMungers = new ArrayList();
        this.parameterizedWithTypeVariable = FuzzyBoolean.MAYBE;
        this.world = world;
    }

    @Override // org.aspectj.weaver.UnresolvedType
    public int getSize() {
        return 1;
    }

    public final Iterator<ResolvedType> getDirectSupertypes() {
        Iterator<ResolvedType> interfacesIterator = Iterators.array(getDeclaredInterfaces());
        ResolvedType superclass = getSuperclass();
        if (superclass == null) {
            return interfacesIterator;
        }
        return Iterators.snoc(interfacesIterator, superclass);
    }

    public boolean isCacheable() {
        return true;
    }

    public boolean isMissing() {
        return false;
    }

    public static boolean isMissing(UnresolvedType unresolved) {
        if (!(unresolved instanceof ResolvedType)) {
            return unresolved == MISSING;
        }
        ResolvedType resolved = (ResolvedType) unresolved;
        return resolved.isMissing();
    }

    public ResolvedType[] getAnnotationTypes() {
        return EMPTY_RESOLVED_TYPE_ARRAY;
    }

    public AnnotationAJ getAnnotationOfType(UnresolvedType ofType) {
        return null;
    }

    public ResolvedType getResolvedComponentType() {
        return null;
    }

    public World getWorld() {
        return this.world;
    }

    @Override // org.aspectj.weaver.UnresolvedType
    public boolean equals(Object other) {
        if (other instanceof ResolvedType) {
            return this == other;
        }
        return super.equals(other);
    }

    public Iterator<ResolvedMember> getFields() {
        final Iterators.Filter<ResolvedType> dupFilter = Iterators.dupFilter();
        Iterators.Getter<ResolvedType, ResolvedType> typeGetter = new Iterators.Getter<ResolvedType, ResolvedType>() { // from class: org.aspectj.weaver.ResolvedType.1
            @Override // org.aspectj.weaver.Iterators.Getter
            public Iterator<ResolvedType> get(ResolvedType o) {
                return dupFilter.filter(o.getDirectSupertypes());
            }
        };
        return Iterators.mapOver(Iterators.recur(this, typeGetter), FieldGetterInstance);
    }

    public Iterator<ResolvedMember> getMethods(boolean wantGenerics, boolean wantDeclaredParents) {
        return Iterators.mapOver(getHierarchy(wantGenerics, wantDeclaredParents), MethodGetterInstance);
    }

    public Iterator<ResolvedMember> getMethodsIncludingIntertypeDeclarations(boolean wantGenerics, boolean wantDeclaredParents) {
        return Iterators.mapOver(getHierarchy(wantGenerics, wantDeclaredParents), MethodGetterWithItdsInstance);
    }

    /* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/ResolvedType$MethodGetter.class */
    private static class MethodGetter implements Iterators.Getter<ResolvedType, ResolvedMember> {
        private MethodGetter() {
        }

        @Override // org.aspectj.weaver.Iterators.Getter
        public Iterator<ResolvedMember> get(ResolvedType type) {
            return Iterators.array(type.getDeclaredMethods());
        }
    }

    /* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/ResolvedType$PointcutGetter.class */
    private static class PointcutGetter implements Iterators.Getter<ResolvedType, ResolvedMember> {
        private PointcutGetter() {
        }

        @Override // org.aspectj.weaver.Iterators.Getter
        public Iterator<ResolvedMember> get(ResolvedType o) {
            return Iterators.array(o.getDeclaredPointcuts());
        }
    }

    /* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/ResolvedType$MethodGetterIncludingItds.class */
    private static class MethodGetterIncludingItds implements Iterators.Getter<ResolvedType, ResolvedMember> {
        private MethodGetterIncludingItds() {
        }

        @Override // org.aspectj.weaver.Iterators.Getter
        public Iterator<ResolvedMember> get(ResolvedType type) {
            ResolvedMember[] methods = type.getDeclaredMethods();
            if (type.interTypeMungers != null) {
                int additional = 0;
                Iterator<ConcreteTypeMunger> it = type.interTypeMungers.iterator();
                while (it.hasNext()) {
                    ResolvedMember rm = it.next().getSignature();
                    if (rm != null) {
                        additional++;
                    }
                }
                if (additional > 0) {
                    ResolvedMember[] methods2 = new ResolvedMember[methods.length + additional];
                    System.arraycopy(methods, 0, methods2, 0, methods.length);
                    int additional2 = methods.length;
                    for (ConcreteTypeMunger typeTransformer : type.interTypeMungers) {
                        ResolvedMember rm2 = typeTransformer.getSignature();
                        if (rm2 != null) {
                            int i = additional2;
                            additional2++;
                            methods2[i] = typeTransformer.getSignature();
                        }
                    }
                    methods = methods2;
                }
            }
            return Iterators.array(methods);
        }
    }

    /* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/ResolvedType$FieldGetter.class */
    private static class FieldGetter implements Iterators.Getter<ResolvedType, ResolvedMember> {
        private FieldGetter() {
        }

        @Override // org.aspectj.weaver.Iterators.Getter
        public Iterator<ResolvedMember> get(ResolvedType type) {
            return Iterators.array(type.getDeclaredFields());
        }
    }

    public Iterator<ResolvedType> getHierarchy() {
        return getHierarchy(false, false);
    }

    public Iterator<ResolvedType> getHierarchy(final boolean wantGenerics, final boolean wantDeclaredParents) {
        Iterators.Getter<ResolvedType, ResolvedType> interfaceGetter = new Iterators.Getter<ResolvedType, ResolvedType>() { // from class: org.aspectj.weaver.ResolvedType.2
            List<String> alreadySeen = new ArrayList();

            @Override // org.aspectj.weaver.Iterators.Getter
            public Iterator<ResolvedType> get(ResolvedType type) {
                ResolvedType[] interfaces = type.getDeclaredInterfaces();
                if (!wantDeclaredParents && type.hasNewParentMungers()) {
                    List<Integer> forRemoval = new ArrayList<>();
                    for (ConcreteTypeMunger munger : type.interTypeMungers) {
                        if (munger.getMunger() != null) {
                            ResolvedTypeMunger m = munger.getMunger();
                            if (m.getKind() == ResolvedTypeMunger.Parent) {
                                ResolvedType newType = ((NewParentTypeMunger) m).getNewParent();
                                if (!wantGenerics && newType.isParameterizedOrGenericType()) {
                                    newType = newType.getRawType();
                                }
                                for (int ii = 0; ii < interfaces.length; ii++) {
                                    ResolvedType iface = interfaces[ii];
                                    if (!wantGenerics && iface.isParameterizedOrGenericType()) {
                                        iface = iface.getRawType();
                                    }
                                    if (newType.getSignature().equals(iface.getSignature())) {
                                        forRemoval.add(Integer.valueOf(ii));
                                    }
                                }
                            }
                        }
                    }
                    if (forRemoval.size() > 0) {
                        ResolvedType[] interfaces2 = new ResolvedType[interfaces.length - forRemoval.size()];
                        int p = 0;
                        for (int ii2 = 0; ii2 < interfaces.length; ii2++) {
                            if (!forRemoval.contains(Integer.valueOf(ii2))) {
                                int i = p;
                                p++;
                                interfaces2[i] = interfaces[ii2];
                            }
                        }
                        interfaces = interfaces2;
                    }
                }
                return new Iterators.ResolvedTypeArrayIterator(interfaces, this.alreadySeen, wantGenerics);
            }
        };
        if (isInterface()) {
            return new SuperInterfaceWalker(interfaceGetter, this);
        }
        SuperInterfaceWalker superInterfaceWalker = new SuperInterfaceWalker(interfaceGetter);
        Iterator<ResolvedType> superClassesIterator = new SuperClassWalker(this, superInterfaceWalker, wantGenerics);
        return Iterators.append1(superClassesIterator, superInterfaceWalker);
    }

    public List<ResolvedMember> getMethodsWithoutIterator(boolean includeITDs, boolean allowMissing, boolean genericsAware) {
        List<ResolvedMember> methods = new ArrayList<>();
        Set<String> knowninterfaces = new HashSet<>();
        addAndRecurse(knowninterfaces, methods, this, includeITDs, allowMissing, genericsAware);
        return methods;
    }

    public List<ResolvedType> getHierarchyWithoutIterator(boolean includeITDs, boolean allowMissing, boolean genericsAware) {
        List<ResolvedType> types = new ArrayList<>();
        Set<String> visited = new HashSet<>();
        recurseHierarchy(visited, types, this, includeITDs, allowMissing, genericsAware);
        return types;
    }

    private void addAndRecurse(Set<String> knowninterfaces, List<ResolvedMember> collector, ResolvedType resolvedType, boolean includeITDs, boolean allowMissing, boolean genericsAware) {
        collector.addAll(Arrays.asList(resolvedType.getDeclaredMethods()));
        if (includeITDs && resolvedType.interTypeMungers != null) {
            for (ConcreteTypeMunger typeTransformer : this.interTypeMungers) {
                ResolvedMember rm = typeTransformer.getSignature();
                if (rm != null) {
                    collector.add(typeTransformer.getSignature());
                }
            }
        }
        if (!resolvedType.isInterface() && !resolvedType.equals(OBJECT)) {
            ResolvedType superType = resolvedType.getSuperclass();
            if (superType != null && !superType.isMissing()) {
                if (!genericsAware && superType.isParameterizedOrGenericType()) {
                    superType = superType.getRawType();
                }
                addAndRecurse(knowninterfaces, collector, superType, includeITDs, allowMissing, genericsAware);
            }
        }
        ResolvedType[] interfaces = resolvedType.getDeclaredInterfaces();
        for (ResolvedType iface : interfaces) {
            if (!genericsAware && iface.isParameterizedOrGenericType()) {
                iface = iface.getRawType();
            }
            boolean shouldSkip = false;
            int j = 0;
            while (true) {
                if (j >= resolvedType.interTypeMungers.size()) {
                    break;
                }
                ConcreteTypeMunger munger = resolvedType.interTypeMungers.get(j);
                if (munger.getMunger() == null || munger.getMunger().getKind() != ResolvedTypeMunger.Parent || !((NewParentTypeMunger) munger.getMunger()).getNewParent().equals(iface)) {
                    j++;
                } else {
                    shouldSkip = true;
                    break;
                }
            }
            if (!shouldSkip && !knowninterfaces.contains(iface.getSignature())) {
                knowninterfaces.add(iface.getSignature());
                if (allowMissing && iface.isMissing()) {
                    if (iface instanceof MissingResolvedTypeWithKnownSignature) {
                        ((MissingResolvedTypeWithKnownSignature) iface).raiseWarningOnMissingInterfaceWhilstFindingMethods();
                    }
                } else {
                    addAndRecurse(knowninterfaces, collector, iface, includeITDs, allowMissing, genericsAware);
                }
            }
        }
    }

    private void recurseHierarchy(Set<String> knowninterfaces, List<ResolvedType> collector, ResolvedType resolvedType, boolean includeITDs, boolean allowMissing, boolean genericsAware) {
        collector.add(resolvedType);
        if (!resolvedType.isInterface() && !resolvedType.equals(OBJECT)) {
            ResolvedType superType = resolvedType.getSuperclass();
            if (superType != null && !superType.isMissing()) {
                if (!genericsAware && (superType.isParameterizedType() || superType.isGenericType())) {
                    superType = superType.getRawType();
                }
                recurseHierarchy(knowninterfaces, collector, superType, includeITDs, allowMissing, genericsAware);
            }
        }
        ResolvedType[] interfaces = resolvedType.getDeclaredInterfaces();
        for (ResolvedType iface : interfaces) {
            if (!genericsAware && (iface.isParameterizedType() || iface.isGenericType())) {
                iface = iface.getRawType();
            }
            boolean shouldSkip = false;
            int j = 0;
            while (true) {
                if (j >= resolvedType.interTypeMungers.size()) {
                    break;
                }
                ConcreteTypeMunger munger = resolvedType.interTypeMungers.get(j);
                if (munger.getMunger() == null || munger.getMunger().getKind() != ResolvedTypeMunger.Parent || !((NewParentTypeMunger) munger.getMunger()).getNewParent().equals(iface)) {
                    j++;
                } else {
                    shouldSkip = true;
                    break;
                }
            }
            if (!shouldSkip && !knowninterfaces.contains(iface.getSignature())) {
                knowninterfaces.add(iface.getSignature());
                if (allowMissing && iface.isMissing()) {
                    if (iface instanceof MissingResolvedTypeWithKnownSignature) {
                        ((MissingResolvedTypeWithKnownSignature) iface).raiseWarningOnMissingInterfaceWhilstFindingMethods();
                    }
                } else {
                    recurseHierarchy(knowninterfaces, collector, iface, includeITDs, allowMissing, genericsAware);
                }
            }
        }
    }

    public ResolvedType[] getResolvedTypeParameters() {
        if (this.resolvedTypeParams == null) {
            this.resolvedTypeParams = this.world.resolve(this.typeParameters);
        }
        return this.resolvedTypeParams;
    }

    public ResolvedMember lookupField(Member field) {
        Iterator<ResolvedMember> i = getFields();
        while (i.hasNext()) {
            ResolvedMember resolvedMember = i.next();
            if (matches(resolvedMember, field)) {
                return resolvedMember;
            }
            if (resolvedMember.hasBackingGenericMember() && field.getName().equals(resolvedMember.getName()) && matches(resolvedMember.getBackingGenericMember(), field)) {
                return resolvedMember;
            }
        }
        return null;
    }

    public ResolvedMember lookupMethod(Member m) {
        ResolvedMember[] methods;
        List<ResolvedType> typesTolookat = new ArrayList<>();
        typesTolookat.add(this);
        int pos = 0;
        while (pos < typesTolookat.size()) {
            int i = pos;
            pos++;
            ResolvedType type = typesTolookat.get(i);
            if (!type.isMissing() && (methods = type.getDeclaredMethods()) != null) {
                for (ResolvedMember method : methods) {
                    if (matches(method, m)) {
                        return method;
                    }
                    if (method.hasBackingGenericMember() && m.getName().equals(method.getName()) && matches(method.getBackingGenericMember(), m)) {
                        return method;
                    }
                }
            }
            ResolvedType superclass = type.getSuperclass();
            if (superclass != null) {
                typesTolookat.add(superclass);
            }
            ResolvedType[] superinterfaces = type.getDeclaredInterfaces();
            if (superinterfaces != null) {
                for (ResolvedType interf : superinterfaces) {
                    if (!typesTolookat.contains(interf)) {
                        typesTolookat.add(interf);
                    }
                }
            }
        }
        return null;
    }

    public ResolvedMember lookupMethodInITDs(Member member) {
        for (ConcreteTypeMunger typeTransformer : this.interTypeMungers) {
            if (matches(typeTransformer.getSignature(), member)) {
                return typeTransformer.getSignature();
            }
        }
        return null;
    }

    private ResolvedMember lookupMember(Member m, ResolvedMember[] a) {
        for (ResolvedMember f : a) {
            if (matches(f, m)) {
                return f;
            }
        }
        return null;
    }

    public ResolvedMember lookupResolvedMember(ResolvedMember aMember, boolean allowMissing, boolean eraseGenerics) {
        Iterator<ResolvedMember> toSearch;
        ResolvedMember found = null;
        if (aMember.getKind() == Member.METHOD || aMember.getKind() == Member.CONSTRUCTOR) {
            toSearch = getMethodsIncludingIntertypeDeclarations(!eraseGenerics, true);
        } else {
            if (aMember.getKind() == Member.ADVICE) {
                return null;
            }
            if (!$assertionsDisabled && aMember.getKind() != Member.FIELD) {
                throw new AssertionError();
            }
            toSearch = getFields();
        }
        while (true) {
            if (!toSearch.hasNext()) {
                break;
            }
            ResolvedMember candidate = toSearch.next();
            if (eraseGenerics && candidate.hasBackingGenericMember()) {
                candidate = candidate.getBackingGenericMember();
            }
            if (candidate.matches(aMember, eraseGenerics)) {
                found = candidate;
                break;
            }
        }
        return found;
    }

    public static boolean matches(Member m1, Member m2) {
        if (m1 == null) {
            return m2 == null;
        }
        if (m2 == null) {
            return false;
        }
        boolean equalNames = m1.getName().equals(m2.getName());
        if (!equalNames) {
            return false;
        }
        boolean equalSignatures = m1.getSignature().equals(m2.getSignature());
        if (equalSignatures) {
            return true;
        }
        boolean equalCovariantSignatures = m1.getParameterSignature().equals(m2.getParameterSignature());
        if (equalCovariantSignatures) {
            return true;
        }
        return false;
    }

    public static boolean conflictingSignature(Member m1, Member m2) {
        return conflictingSignature(m1, m2, true);
    }

    public static boolean conflictingSignature(Member m1, Member m2, boolean v2itds) {
        if (m1 == null || m2 == null || !m1.getName().equals(m2.getName()) || m1.getKind() != m2.getKind()) {
            return false;
        }
        if (m1.getKind() == Member.FIELD) {
            if (v2itds) {
                if (m1.getDeclaringType().equals(m2.getDeclaringType())) {
                    return true;
                }
            } else {
                return m1.getDeclaringType().equals(m2.getDeclaringType());
            }
        } else if (m1.getKind() == Member.POINTCUT) {
            return true;
        }
        UnresolvedType[] p1 = m1.getGenericParameterTypes();
        UnresolvedType[] p2 = m2.getGenericParameterTypes();
        if (p1 == null) {
            p1 = m1.getParameterTypes();
        }
        if (p2 == null) {
            p2 = m2.getParameterTypes();
        }
        int n = p1.length;
        if (n != p2.length) {
            return false;
        }
        for (int i = 0; i < n; i++) {
            if (!p1[i].equals(p2[i])) {
                return false;
            }
        }
        return true;
    }

    public Iterator<ResolvedMember> getPointcuts() {
        final Iterators.Filter<ResolvedType> dupFilter = Iterators.dupFilter();
        Iterators.Getter<ResolvedType, ResolvedType> typeGetter = new Iterators.Getter<ResolvedType, ResolvedType>() { // from class: org.aspectj.weaver.ResolvedType.3
            @Override // org.aspectj.weaver.Iterators.Getter
            public Iterator<ResolvedType> get(ResolvedType o) {
                return dupFilter.filter(o.getDirectSupertypes());
            }
        };
        return Iterators.mapOver(Iterators.recur(this, typeGetter), PointcutGetterInstance);
    }

    public ResolvedPointcutDefinition findPointcut(String name) {
        Iterator<ResolvedMember> i = getPointcuts();
        while (i.hasNext()) {
            ResolvedPointcutDefinition f = (ResolvedPointcutDefinition) i.next();
            if (f != null && name.equals(f.getName())) {
                return f;
            }
        }
        if (!getOutermostType().equals(this)) {
            ResolvedType outerType = getOutermostType().resolve(this.world);
            ResolvedPointcutDefinition rpd = outerType.findPointcut(name);
            return rpd;
        }
        return null;
    }

    public CrosscuttingMembers collectCrosscuttingMembers(boolean shouldConcretizeIfNeeded) {
        this.crosscuttingMembers = new CrosscuttingMembers(this, shouldConcretizeIfNeeded);
        if (getPerClause() == null) {
            return this.crosscuttingMembers;
        }
        this.crosscuttingMembers.setPerClause(getPerClause());
        this.crosscuttingMembers.addShadowMungers(collectShadowMungers());
        this.crosscuttingMembers.addTypeMungers(getTypeMungers());
        this.crosscuttingMembers.addDeclares(collectDeclares(!doesNotExposeShadowMungers()));
        this.crosscuttingMembers.addPrivilegedAccesses(getPrivilegedAccesses());
        return this.crosscuttingMembers;
    }

    public final List<Declare> collectDeclares(boolean includeAdviceLike) {
        if (!isAspect()) {
            return Collections.emptyList();
        }
        List<Declare> ret = new ArrayList<>();
        if (!isAbstract()) {
            final Iterators.Filter<ResolvedType> dupFilter = Iterators.dupFilter();
            Iterators.Getter<ResolvedType, ResolvedType> typeGetter = new Iterators.Getter<ResolvedType, ResolvedType>() { // from class: org.aspectj.weaver.ResolvedType.4
                @Override // org.aspectj.weaver.Iterators.Getter
                public Iterator<ResolvedType> get(ResolvedType o) {
                    return dupFilter.filter(o.getDirectSupertypes());
                }
            };
            Iterator<ResolvedType> typeIterator = Iterators.recur(this, typeGetter);
            while (typeIterator.hasNext()) {
                ResolvedType ty = typeIterator.next();
                for (Declare dec : ty.getDeclares()) {
                    if (dec.isAdviceLike()) {
                        if (includeAdviceLike) {
                            ret.add(dec);
                        }
                    } else {
                        ret.add(dec);
                    }
                }
            }
        }
        return ret;
    }

    private final List<ShadowMunger> collectShadowMungers() {
        if (!isAspect() || isAbstract() || doesNotExposeShadowMungers()) {
            return Collections.emptyList();
        }
        List<ShadowMunger> acc = new ArrayList<>();
        final Iterators.Filter<ResolvedType> dupFilter = Iterators.dupFilter();
        Iterators.Getter<ResolvedType, ResolvedType> typeGetter = new Iterators.Getter<ResolvedType, ResolvedType>() { // from class: org.aspectj.weaver.ResolvedType.5
            @Override // org.aspectj.weaver.Iterators.Getter
            public Iterator<ResolvedType> get(ResolvedType o) {
                return dupFilter.filter(o.getDirectSupertypes());
            }
        };
        Iterator<ResolvedType> typeIterator = Iterators.recur(this, typeGetter);
        while (typeIterator.hasNext()) {
            ResolvedType ty = typeIterator.next();
            acc.addAll(ty.getDeclaredShadowMungers());
        }
        return acc;
    }

    public void addParent(ResolvedType newParent) {
    }

    protected boolean doesNotExposeShadowMungers() {
        return false;
    }

    public PerClause getPerClause() {
        return null;
    }

    public Collection<Declare> getDeclares() {
        return Collections.emptyList();
    }

    public Collection<ConcreteTypeMunger> getTypeMungers() {
        return Collections.emptyList();
    }

    public Collection<ResolvedMember> getPrivilegedAccesses() {
        return Collections.emptyList();
    }

    public final boolean isInterface() {
        return Modifier.isInterface(getModifiers());
    }

    public final boolean isAbstract() {
        return Modifier.isAbstract(getModifiers());
    }

    public boolean isClass() {
        return false;
    }

    public boolean isAspect() {
        return false;
    }

    public boolean isAnnotationStyleAspect() {
        return false;
    }

    public boolean isEnum() {
        return false;
    }

    public boolean isAnnotation() {
        return false;
    }

    public boolean isAnonymous() {
        return false;
    }

    public boolean isNested() {
        return false;
    }

    public ResolvedType getOuterClass() {
        return null;
    }

    public void addAnnotation(AnnotationAJ annotationX) {
        throw new RuntimeException("ResolvedType.addAnnotation() should never be called");
    }

    public AnnotationAJ[] getAnnotations() {
        throw new RuntimeException("ResolvedType.getAnnotations() should never be called");
    }

    public boolean hasAnnotations() {
        throw new RuntimeException("ResolvedType.getAnnotations() should never be called");
    }

    public boolean canAnnotationTargetType() {
        return false;
    }

    public AnnotationTargetKind[] getAnnotationTargetKinds() {
        return null;
    }

    public boolean isAnnotationWithRuntimeRetention() {
        return false;
    }

    public boolean isSynthetic() {
        return this.signature.indexOf(BcelWeaver.SYNTHETIC_CLASS_POSTFIX) != -1;
    }

    public final boolean isFinal() {
        return Modifier.isFinal(getModifiers());
    }

    protected Map<String, UnresolvedType> getMemberParameterizationMap() {
        if (!isParameterizedType()) {
            return Collections.emptyMap();
        }
        TypeVariable[] tvs = getGenericType().getTypeVariables();
        Map<String, UnresolvedType> parameterizationMap = new HashMap<>();
        if (tvs.length != this.typeParameters.length) {
            throw new IllegalStateException("Unexpected mismatch when building parameterization map. For type " + this.signature + " expecting " + tvs.length + ": " + toString(tvs) + " type parameters but found " + this.typeParameters.length + ":" + toString(this.typeParameters));
        }
        for (int i = 0; i < tvs.length; i++) {
            parameterizationMap.put(tvs[i].getName(), this.typeParameters[i]);
        }
        return parameterizationMap;
    }

    private String toString(UnresolvedType[] typeParameters) {
        StringBuilder s = new StringBuilder();
        for (UnresolvedType tv : typeParameters) {
            s.append(tv.getSignature()).append(SymbolConstants.SPACE_SYMBOL);
        }
        return s.toString();
    }

    private String toString(TypeVariable[] tvs) {
        StringBuilder s = new StringBuilder();
        for (TypeVariable tv : tvs) {
            s.append(tv.getName()).append(SymbolConstants.SPACE_SYMBOL);
        }
        return s.toString();
    }

    public List<ShadowMunger> getDeclaredAdvice() {
        List<ShadowMunger> l = new ArrayList<>();
        ResolvedMember[] methods = getDeclaredMethods();
        if (isParameterizedType()) {
            methods = getGenericType().getDeclaredMethods();
        }
        Map<String, UnresolvedType> typeVariableMap = getAjMemberParameterizationMap();
        int len = methods.length;
        for (int i = 0; i < len; i++) {
            ShadowMunger munger = methods[i].getAssociatedShadowMunger();
            if (munger != null) {
                if (ajMembersNeedParameterization()) {
                    munger = munger.parameterizeWith(this, typeVariableMap);
                    if (munger instanceof Advice) {
                        Advice advice = (Advice) munger;
                        UnresolvedType[] ptypes = methods[i].getGenericParameterTypes();
                        UnresolvedType[] newPTypes = new UnresolvedType[ptypes.length];
                        for (int j = 0; j < ptypes.length; j++) {
                            if (ptypes[j] instanceof TypeVariableReferenceType) {
                                TypeVariableReferenceType tvrt = (TypeVariableReferenceType) ptypes[j];
                                if (typeVariableMap.containsKey(tvrt.getTypeVariable().getName())) {
                                    newPTypes[j] = typeVariableMap.get(tvrt.getTypeVariable().getName());
                                } else {
                                    newPTypes[j] = ptypes[j];
                                }
                            } else {
                                newPTypes[j] = ptypes[j];
                            }
                        }
                        advice.setBindingParameterTypes(newPTypes);
                    }
                }
                munger.setDeclaringType(this);
                l.add(munger);
            }
        }
        return l;
    }

    public List<ShadowMunger> getDeclaredShadowMungers() {
        return getDeclaredAdvice();
    }

    public ResolvedMember[] getDeclaredJavaFields() {
        return filterInJavaVisible(getDeclaredFields());
    }

    public ResolvedMember[] getDeclaredJavaMethods() {
        return filterInJavaVisible(getDeclaredMethods());
    }

    private ResolvedMember[] filterInJavaVisible(ResolvedMember[] ms) {
        List<ResolvedMember> l = new ArrayList<>();
        int len = ms.length;
        for (int i = 0; i < len; i++) {
            if (!ms[i].isAjSynthetic() && ms[i].getAssociatedShadowMunger() == null) {
                l.add(ms[i]);
            }
        }
        return (ResolvedMember[]) l.toArray(new ResolvedMember[l.size()]);
    }

    public static ResolvedType makeArray(ResolvedType type, int dim) {
        if (dim == 0) {
            return type;
        }
        ResolvedType array = new ArrayReferenceType(PropertyAccessor.PROPERTY_KEY_PREFIX + type.getSignature(), PropertyAccessor.PROPERTY_KEY_PREFIX + type.getErasureSignature(), type.getWorld(), type);
        return makeArray(array, dim - 1);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/ResolvedType$Primitive.class */
    public static class Primitive extends ResolvedType {
        private final int size;
        private final int index;
        private static final boolean[][] assignTable = {new boolean[]{true, true, true, true, true, true, true, false, false}, new boolean[]{false, true, true, true, true, true, false, false, false}, new boolean[]{false, false, true, false, false, false, false, false, false}, new boolean[]{false, false, true, true, false, false, false, false, false}, new boolean[]{false, false, true, true, true, true, false, false, false}, new boolean[]{false, false, true, true, false, true, false, false, false}, new boolean[]{false, false, true, true, true, true, true, false, false}, new boolean[]{false, false, false, false, false, false, false, true, false}, new boolean[]{false, false, false, false, false, false, false, false, true}};
        private static final boolean[][] noConvertTable = {new boolean[]{true, true, false, false, true, false, true, false, false}, new boolean[]{false, true, false, false, true, false, false, false, false}, new boolean[]{false, false, true, false, false, false, false, false, false}, new boolean[]{false, false, false, true, false, false, false, false, false}, new boolean[]{false, false, false, false, true, false, false, false, false}, new boolean[]{false, false, false, false, false, true, false, false, false}, new boolean[]{false, false, false, false, true, false, true, false, false}, new boolean[]{false, false, false, false, false, false, false, true, false}, new boolean[]{false, false, false, false, false, false, false, false, true}};

        @Override // org.aspectj.weaver.ResolvedType, org.aspectj.weaver.UnresolvedType
        public /* bridge */ /* synthetic */ UnresolvedType getRawType() {
            return super.getRawType();
        }

        Primitive(String signature, int size, int index) {
            super(signature, null);
            this.size = size;
            this.index = index;
            this.typeKind = UnresolvedType.TypeKind.PRIMITIVE;
        }

        @Override // org.aspectj.weaver.ResolvedType, org.aspectj.weaver.UnresolvedType
        public final int getSize() {
            return this.size;
        }

        @Override // org.aspectj.weaver.ResolvedType
        public final int getModifiers() {
            return 17;
        }

        @Override // org.aspectj.weaver.UnresolvedType
        public final boolean isPrimitiveType() {
            return true;
        }

        @Override // org.aspectj.weaver.AnnotatedElement
        public boolean hasAnnotation(UnresolvedType ofType) {
            return false;
        }

        @Override // org.aspectj.weaver.ResolvedType
        public final boolean isAssignableFrom(ResolvedType other) {
            if (!other.isPrimitiveType()) {
                if (!this.world.isInJava5Mode()) {
                    return false;
                }
                return validBoxing.contains(getSignature() + other.getSignature());
            }
            return assignTable[((Primitive) other).index][this.index];
        }

        @Override // org.aspectj.weaver.ResolvedType
        public final boolean isAssignableFrom(ResolvedType other, boolean allowMissing) {
            return isAssignableFrom(other);
        }

        @Override // org.aspectj.weaver.ResolvedType
        public final boolean isCoerceableFrom(ResolvedType other) {
            if (this == other) {
                return true;
            }
            if (!other.isPrimitiveType() || this.index > 6 || ((Primitive) other).index > 6) {
                return false;
            }
            return true;
        }

        @Override // org.aspectj.weaver.UnresolvedType
        public ResolvedType resolve(World world) {
            if (this.world != world) {
                throw new IllegalStateException();
            }
            this.world = world;
            return super.resolve(world);
        }

        @Override // org.aspectj.weaver.ResolvedType
        public final boolean needsNoConversionFrom(ResolvedType other) {
            if (!other.isPrimitiveType()) {
                return false;
            }
            return noConvertTable[((Primitive) other).index][this.index];
        }

        @Override // org.aspectj.weaver.ResolvedType
        public final ResolvedMember[] getDeclaredFields() {
            return ResolvedMember.NONE;
        }

        @Override // org.aspectj.weaver.ResolvedType
        public final ResolvedMember[] getDeclaredMethods() {
            return ResolvedMember.NONE;
        }

        @Override // org.aspectj.weaver.ResolvedType
        public final ResolvedType[] getDeclaredInterfaces() {
            return ResolvedType.NONE;
        }

        @Override // org.aspectj.weaver.ResolvedType
        public final ResolvedMember[] getDeclaredPointcuts() {
            return ResolvedMember.NONE;
        }

        @Override // org.aspectj.weaver.ResolvedType
        public final ResolvedType getSuperclass() {
            return null;
        }

        @Override // org.aspectj.weaver.ResolvedType
        public ISourceContext getSourceContext() {
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/ResolvedType$Missing.class */
    public static class Missing extends ResolvedType {
        @Override // org.aspectj.weaver.ResolvedType, org.aspectj.weaver.UnresolvedType
        public /* bridge */ /* synthetic */ UnresolvedType getRawType() {
            return super.getRawType();
        }

        Missing() {
            super(UnresolvedType.MISSING_NAME, null);
        }

        @Override // org.aspectj.weaver.UnresolvedType
        public final String getName() {
            return UnresolvedType.MISSING_NAME;
        }

        @Override // org.aspectj.weaver.ResolvedType
        public final boolean isMissing() {
            return true;
        }

        @Override // org.aspectj.weaver.AnnotatedElement
        public boolean hasAnnotation(UnresolvedType ofType) {
            return false;
        }

        @Override // org.aspectj.weaver.ResolvedType
        public final ResolvedMember[] getDeclaredFields() {
            return ResolvedMember.NONE;
        }

        @Override // org.aspectj.weaver.ResolvedType
        public final ResolvedMember[] getDeclaredMethods() {
            return ResolvedMember.NONE;
        }

        @Override // org.aspectj.weaver.ResolvedType
        public final ResolvedType[] getDeclaredInterfaces() {
            return ResolvedType.NONE;
        }

        @Override // org.aspectj.weaver.ResolvedType
        public final ResolvedMember[] getDeclaredPointcuts() {
            return ResolvedMember.NONE;
        }

        @Override // org.aspectj.weaver.ResolvedType
        public final ResolvedType getSuperclass() {
            return null;
        }

        @Override // org.aspectj.weaver.ResolvedType
        public final int getModifiers() {
            return 0;
        }

        @Override // org.aspectj.weaver.ResolvedType
        public final boolean isAssignableFrom(ResolvedType other) {
            return false;
        }

        @Override // org.aspectj.weaver.ResolvedType
        public final boolean isAssignableFrom(ResolvedType other, boolean allowMissing) {
            return false;
        }

        @Override // org.aspectj.weaver.ResolvedType
        public final boolean isCoerceableFrom(ResolvedType other) {
            return false;
        }

        @Override // org.aspectj.weaver.ResolvedType
        public boolean needsNoConversionFrom(ResolvedType other) {
            return false;
        }

        @Override // org.aspectj.weaver.ResolvedType
        public ISourceContext getSourceContext() {
            return null;
        }
    }

    public ResolvedMember lookupMemberNoSupers(Member member) {
        ResolvedMember ret = lookupDirectlyDeclaredMemberNoSupers(member);
        if (ret == null && this.interTypeMungers != null) {
            for (ConcreteTypeMunger tm : this.interTypeMungers) {
                if (matches(tm.getSignature(), member)) {
                    return tm.getSignature();
                }
            }
        }
        return ret;
    }

    public ResolvedMember lookupMemberWithSupersAndITDs(Member member) {
        ResolvedMember ret = lookupMemberNoSupers(member);
        if (ret != null) {
            return ret;
        }
        ResolvedType supert = getSuperclass();
        while (ret == null && supert != null) {
            ret = supert.lookupMemberNoSupers(member);
            if (ret == null) {
                supert = supert.getSuperclass();
            }
        }
        return ret;
    }

    public ResolvedMember lookupDirectlyDeclaredMemberNoSupers(Member member) {
        ResolvedMember ret;
        if (member.getKind() == Member.FIELD) {
            ret = lookupMember(member, getDeclaredFields());
        } else {
            ret = lookupMember(member, getDeclaredMethods());
        }
        return ret;
    }

    public ResolvedMember lookupMemberIncludingITDsOnInterfaces(Member member) {
        return lookupMemberIncludingITDsOnInterfaces(member, this);
    }

    private ResolvedMember lookupMemberIncludingITDsOnInterfaces(Member member, ResolvedType onType) {
        ResolvedMember ret = onType.lookupMemberNoSupers(member);
        if (ret != null) {
            return ret;
        }
        ResolvedType superType = onType.getSuperclass();
        if (superType != null) {
            ret = lookupMemberIncludingITDsOnInterfaces(member, superType);
        }
        if (ret == null) {
            ResolvedType[] superInterfaces = onType.getDeclaredInterfaces();
            for (ResolvedType resolvedType : superInterfaces) {
                ret = resolvedType.lookupMethodInITDs(member);
                if (ret != null) {
                    return ret;
                }
            }
        }
        return ret;
    }

    public List<ConcreteTypeMunger> getInterTypeMungers() {
        return this.interTypeMungers;
    }

    public List<ConcreteTypeMunger> getInterTypeParentMungers() {
        List<ConcreteTypeMunger> l = new ArrayList<>();
        for (ConcreteTypeMunger element : this.interTypeMungers) {
            if (element.getMunger() instanceof NewParentTypeMunger) {
                l.add(element);
            }
        }
        return l;
    }

    public List<ConcreteTypeMunger> getInterTypeMungersIncludingSupers() {
        ArrayList<ConcreteTypeMunger> ret = new ArrayList<>();
        collectInterTypeMungers(ret);
        return ret;
    }

    public List<ConcreteTypeMunger> getInterTypeParentMungersIncludingSupers() {
        ArrayList<ConcreteTypeMunger> ret = new ArrayList<>();
        collectInterTypeParentMungers(ret);
        return ret;
    }

    private void collectInterTypeParentMungers(List<ConcreteTypeMunger> collector) {
        Iterator<ResolvedType> iter = getDirectSupertypes();
        while (iter.hasNext()) {
            ResolvedType superType = iter.next();
            superType.collectInterTypeParentMungers(collector);
        }
        collector.addAll(getInterTypeParentMungers());
    }

    protected void collectInterTypeMungers(List<ConcreteTypeMunger> collector) {
        Iterator<ResolvedType> iter = getDirectSupertypes();
        while (iter.hasNext()) {
            ResolvedType superType = iter.next();
            if (superType == null) {
                throw new BCException("UnexpectedProblem: a supertype in the hierarchy for " + getName() + " is null");
            }
            superType.collectInterTypeMungers(collector);
        }
        Iterator<ConcreteTypeMunger> iter1 = collector.iterator();
        while (iter1.hasNext()) {
            ConcreteTypeMunger superMunger = iter1.next();
            if (superMunger.getSignature() != null && superMunger.getSignature().isAbstract()) {
                Iterator<ConcreteTypeMunger> it = getInterTypeMungers().iterator();
                while (true) {
                    if (it.hasNext()) {
                        ConcreteTypeMunger myMunger = it.next();
                        if (conflictingSignature(myMunger.getSignature(), superMunger.getSignature())) {
                            iter1.remove();
                            break;
                        }
                    } else if (superMunger.getSignature().isPublic()) {
                        Iterator<ResolvedMember> iter2 = getMethods(true, true);
                        while (true) {
                            if (iter2.hasNext()) {
                                ResolvedMember method = iter2.next();
                                if (conflictingSignature(method, superMunger.getSignature())) {
                                    iter1.remove();
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
        collector.addAll(getInterTypeMungers());
    }

    public void checkInterTypeMungers() throws AbortException {
        if (isAbstract()) {
            return;
        }
        boolean itdProblem = false;
        Iterator<ConcreteTypeMunger> it = getInterTypeMungersIncludingSupers().iterator();
        while (it.hasNext()) {
            itdProblem = checkAbstractDeclaration(it.next()) || itdProblem;
        }
        if (itdProblem) {
            return;
        }
        for (ConcreteTypeMunger munger : getInterTypeMungersIncludingSupers()) {
            if (munger.getSignature() != null && munger.getSignature().isAbstract() && munger.getMunger().getKind() != ResolvedTypeMunger.PrivilegedAccess && munger.getMunger().getKind() != ResolvedTypeMunger.MethodDelegate2) {
                this.world.getMessageHandler().handleMessage(new Message("must implement abstract inter-type declaration: " + munger.getSignature(), "", IMessage.ERROR, getSourceLocation(), null, new ISourceLocation[]{getMungerLocation(munger)}));
            }
        }
    }

    private boolean checkAbstractDeclaration(ConcreteTypeMunger munger) throws AbortException {
        if (munger.getMunger() != null && (munger.getMunger() instanceof NewMethodTypeMunger)) {
            ResolvedMember itdMember = munger.getSignature();
            ResolvedType onType = itdMember.getDeclaringType().resolve(this.world);
            if (onType.isInterface() && itdMember.isAbstract() && !itdMember.isPublic()) {
                this.world.getMessageHandler().handleMessage(new Message(WeaverMessages.format(WeaverMessages.ITD_ABSTRACT_MUST_BE_PUBLIC_ON_INTERFACE, munger.getSignature(), onType), "", Message.ERROR, getSourceLocation(), null, new ISourceLocation[]{getMungerLocation(munger)}));
                return true;
            }
            return false;
        }
        return false;
    }

    private ISourceLocation getMungerLocation(ConcreteTypeMunger munger) {
        ISourceLocation sloc = munger.getSourceLocation();
        if (sloc == null) {
            sloc = munger.getAspectType().getSourceLocation();
        }
        return sloc;
    }

    public ResolvedType getDeclaringType() {
        if (isArray()) {
            return null;
        }
        if (isNested() || isAnonymous()) {
            return getOuterClass();
        }
        return null;
    }

    public static boolean isVisible(int modifiers, ResolvedType targetType, ResolvedType fromType) {
        if (Modifier.isPublic(modifiers)) {
            return true;
        }
        if (Modifier.isPrivate(modifiers)) {
            return targetType.getOutermostType().equals(fromType.getOutermostType());
        }
        if (Modifier.isProtected(modifiers)) {
            return samePackage(targetType, fromType) || targetType.isAssignableFrom(fromType);
        }
        return samePackage(targetType, fromType);
    }

    private static boolean samePackage(ResolvedType targetType, ResolvedType fromType) {
        String p1 = targetType.getPackageName();
        String p2 = fromType.getPackageName();
        if (p1 == null) {
            return p2 == null;
        }
        if (p2 == null) {
            return false;
        }
        return p1.equals(p2);
    }

    private boolean genericTypeEquals(ResolvedType other) {
        if (other.isParameterizedType() || other.isRawType()) {
            other.getGenericType();
        }
        if (((isParameterizedType() || isRawType()) && getGenericType().equals(other)) || equals(other)) {
            return true;
        }
        return false;
    }

    public ResolvedType discoverActualOccurrenceOfTypeInHierarchy(ResolvedType lookingFor) {
        if (!lookingFor.isGenericType()) {
            throw new BCException("assertion failed: method should only be called with generic type, but " + lookingFor + " is " + lookingFor.typeKind);
        }
        if (equals(OBJECT)) {
            return null;
        }
        if (genericTypeEquals(lookingFor)) {
            return this;
        }
        ResolvedType superT = getSuperclass();
        if (superT.genericTypeEquals(lookingFor)) {
            return superT;
        }
        ResolvedType[] superIs = getDeclaredInterfaces();
        for (ResolvedType superI : superIs) {
            if (superI.genericTypeEquals(lookingFor)) {
                return superI;
            }
            ResolvedType checkTheSuperI = superI.discoverActualOccurrenceOfTypeInHierarchy(lookingFor);
            if (checkTheSuperI != null) {
                return checkTheSuperI;
            }
        }
        return superT.discoverActualOccurrenceOfTypeInHierarchy(lookingFor);
    }

    public ConcreteTypeMunger fillInAnyTypeParameters(ConcreteTypeMunger munger) throws AbortException {
        ResolvedMember member = munger.getSignature();
        if (munger.isTargetTypeParameterized()) {
            if (0 != 0) {
                System.err.println("Processing attempted parameterization of " + munger + " targetting type " + this);
            }
            if (0 != 0) {
                System.err.println("  This type is " + this + "  (" + this.typeKind + ")");
            }
            if (0 != 0) {
                System.err.println("  Signature that needs parameterizing: " + member);
            }
            ResolvedType onTypeResolved = this.world.resolve(member.getDeclaringType());
            ResolvedType onType = onTypeResolved.getGenericType();
            if (onType == null) {
                getWorld().getMessageHandler().handleMessage(MessageUtil.error("The target type for the intertype declaration is not generic", munger.getSourceLocation()));
                return munger;
            }
            member.resolve(this.world);
            if (0 != 0) {
                System.err.println("  Actual target ontype: " + onType + "  (" + onType.typeKind + ")");
            }
            ResolvedType actualTarget = discoverActualOccurrenceOfTypeInHierarchy(onType);
            if (actualTarget == null) {
                throw new BCException("assertion failed: asked " + this + " for occurrence of " + onType + " in its hierarchy??");
            }
            if (!actualTarget.isGenericType() && 0 != 0) {
                System.err.println("Occurrence in " + this + " is actually " + actualTarget + "  (" + actualTarget.typeKind + ")");
            }
            munger = munger.parameterizedFor(actualTarget);
            if (0 != 0) {
                System.err.println("New sig: " + munger.getSignature());
            }
            if (0 != 0) {
                System.err.println("=====================================");
            }
        }
        return munger;
    }

    public void addInterTypeMunger(ConcreteTypeMunger munger, boolean isDuringCompilation) throws AbortException {
        ResolvedMember sig = munger.getSignature();
        this.bits &= MungersAnalyzed ^ (-1);
        if (sig == null || munger.getMunger() == null || munger.getMunger().getKind() == ResolvedTypeMunger.PrivilegedAccess) {
            this.interTypeMungers.add(munger);
            return;
        }
        ConcreteTypeMunger munger2 = fillInAnyTypeParameters(munger);
        ResolvedMember sig2 = munger2.getSignature();
        if (sig2.getKind() == Member.METHOD) {
            if (clashesWithExistingMember(munger2, getMethods(true, false))) {
                return;
            }
            if (isInterface() && clashesWithExistingMember(munger2, Arrays.asList(this.world.getCoreType(OBJECT).getDeclaredMethods()).iterator())) {
                return;
            }
        } else if (sig2.getKind() == Member.FIELD) {
            if (clashesWithExistingMember(munger2, Arrays.asList(getDeclaredFields()).iterator())) {
                return;
            }
            if (!isDuringCompilation) {
                ResolvedTypeMunger thisRealMunger = munger2.getMunger();
                if (thisRealMunger instanceof NewFieldTypeMunger) {
                    NewFieldTypeMunger newFieldTypeMunger = (NewFieldTypeMunger) thisRealMunger;
                    if (newFieldTypeMunger.version == 2) {
                        String thisRealMungerSignatureName = newFieldTypeMunger.getSignature().getName();
                        for (ConcreteTypeMunger typeMunger : this.interTypeMungers) {
                            if ((typeMunger.getMunger() instanceof NewFieldTypeMunger) && typeMunger.getSignature().getKind() == Member.FIELD) {
                                NewFieldTypeMunger existing = (NewFieldTypeMunger) typeMunger.getMunger();
                                if (existing.getSignature().getName().equals(thisRealMungerSignatureName) && existing.version == 2 && existing.getSignature().getDeclaringType().equals(newFieldTypeMunger.getSignature().getDeclaringType())) {
                                    StringBuffer sb = new StringBuffer();
                                    sb.append("Cannot handle two aspects both attempting to use new style ITDs for the same named field ");
                                    sb.append("on the same target type.  Please recompile at least one aspect with '-Xset:itdVersion=1'.");
                                    sb.append(" Aspects involved: " + munger2.getAspectType().getName() + " and " + typeMunger.getAspectType().getName() + ".");
                                    sb.append(" Field is named '" + existing.getSignature().getName() + "'");
                                    getWorld().getMessageHandler().handleMessage(new Message(sb.toString(), getSourceLocation(), true));
                                    return;
                                }
                            }
                        }
                    }
                }
            }
        } else if (clashesWithExistingMember(munger2, Arrays.asList(getDeclaredMethods()).iterator())) {
            return;
        }
        boolean needsAdding = true;
        boolean needsToBeAddedEarlier = false;
        Iterator<ConcreteTypeMunger> i = this.interTypeMungers.iterator();
        while (true) {
            if (!i.hasNext()) {
                break;
            }
            ConcreteTypeMunger existingMunger = i.next();
            boolean v2itds = munger2.getSignature().getKind() == Member.FIELD && (munger2.getMunger() instanceof NewFieldTypeMunger) && ((NewFieldTypeMunger) munger2.getMunger()).version == 2;
            if (conflictingSignature(existingMunger.getSignature(), munger2.getSignature(), v2itds) && isVisible(munger2.getSignature().getModifiers(), munger2.getAspectType(), existingMunger.getAspectType())) {
                int c = compareMemberPrecedence(sig2, existingMunger.getSignature());
                if (c == 0) {
                    c = getWorld().compareByPrecedenceAndHierarchy(munger2.getAspectType(), existingMunger.getAspectType());
                }
                if (c < 0) {
                    checkLegalOverride(munger2.getSignature(), existingMunger.getSignature(), 17, null);
                    needsAdding = false;
                    if (munger2.getSignature().getKind() == Member.FIELD && munger2.getSignature().getDeclaringType().resolve(this.world).isInterface() && ((NewFieldTypeMunger) munger2.getMunger()).version == 2) {
                        needsAdding = true;
                    }
                } else if (c > 0) {
                    checkLegalOverride(existingMunger.getSignature(), munger2.getSignature(), 17, null);
                    if (existingMunger.getSignature().getKind() == Member.FIELD && existingMunger.getSignature().getDeclaringType().resolve(this.world).isInterface() && ((NewFieldTypeMunger) existingMunger.getMunger()).version == 2) {
                        needsToBeAddedEarlier = true;
                    } else {
                        i.remove();
                    }
                } else {
                    interTypeConflictError(munger2, existingMunger);
                    interTypeConflictError(existingMunger, munger2);
                    return;
                }
            }
        }
        if (needsAdding) {
            if (!needsToBeAddedEarlier) {
                this.interTypeMungers.add(munger2);
            } else {
                this.interTypeMungers.add(0, munger2);
            }
        }
    }

    private boolean clashesWithExistingMember(ConcreteTypeMunger typeTransformer, Iterator<ResolvedMember> existingMembers) throws AbortException {
        List<ConcreteTypeMunger> mungersAffectingThisType;
        ResolvedMember typeTransformerSignature = typeTransformer.getSignature();
        ResolvedTypeMunger rtm = typeTransformer.getMunger();
        boolean v2itds = true;
        if ((rtm instanceof NewFieldTypeMunger) && ((NewFieldTypeMunger) rtm).version == 1) {
            v2itds = false;
        }
        while (existingMembers.hasNext()) {
            ResolvedMember existingMember = existingMembers.next();
            if (!existingMember.isBridgeMethod() && conflictingSignature(existingMember, typeTransformerSignature, v2itds)) {
                if (isVisible(existingMember.getModifiers(), this, typeTransformer.getAspectType())) {
                    int c = compareMemberPrecedence(typeTransformerSignature, existingMember);
                    if (c < 0) {
                        ResolvedType typeTransformerTargetType = typeTransformerSignature.getDeclaringType().resolve(this.world);
                        if (typeTransformerTargetType.isInterface()) {
                            ResolvedType existingMemberType = existingMember.getDeclaringType().resolve(this.world);
                            if ((rtm instanceof NewMethodTypeMunger) && !typeTransformerTargetType.equals(existingMemberType) && Modifier.isPrivate(typeTransformerSignature.getModifiers()) && Modifier.isPublic(existingMember.getModifiers())) {
                                this.world.getMessageHandler().handleMessage(new Message("private intertype declaration '" + typeTransformerSignature.toString() + "' clashes with public member '" + existingMember.toString() + "'", existingMember.getSourceLocation(), true));
                            }
                        }
                        checkLegalOverride(typeTransformerSignature, existingMember, 16, typeTransformer.getAspectType());
                        return true;
                    }
                    if (c > 0) {
                        checkLegalOverride(existingMember, typeTransformerSignature, 1, typeTransformer.getAspectType());
                    } else {
                        boolean sameReturnTypes = existingMember.getReturnType().equals(typeTransformerSignature.getReturnType());
                        if (sameReturnTypes) {
                            boolean isDuplicateOfPreviousITD = false;
                            ResolvedType declaringRt = existingMember.getDeclaringType().resolve(this.world);
                            WeaverStateInfo wsi = declaringRt.getWeaverState();
                            if (wsi != null && (mungersAffectingThisType = wsi.getTypeMungers(declaringRt)) != null) {
                                Iterator<ConcreteTypeMunger> iterator = mungersAffectingThisType.iterator();
                                while (iterator.hasNext() && !isDuplicateOfPreviousITD) {
                                    ConcreteTypeMunger ctMunger = iterator.next();
                                    if (ctMunger.getSignature().equals(existingMember) && ctMunger.aspectType.equals(typeTransformer.getAspectType())) {
                                        isDuplicateOfPreviousITD = true;
                                    }
                                }
                            }
                            if (!isDuplicateOfPreviousITD && (!typeTransformerSignature.getName().equals("<init>") || !existingMember.isDefaultConstructor())) {
                                String aspectName = typeTransformer.getAspectType().getName();
                                ISourceLocation typeTransformerLocation = typeTransformer.getSourceLocation();
                                ISourceLocation existingMemberLocation = existingMember.getSourceLocation();
                                String msg = WeaverMessages.format(WeaverMessages.ITD_MEMBER_CONFLICT, aspectName, existingMember);
                                getWorld().getMessageHandler().handleMessage(new Message(msg, typeTransformerLocation, true));
                                if (existingMemberLocation != null) {
                                    getWorld().getMessageHandler().handleMessage(new Message(msg, existingMemberLocation, true));
                                    return true;
                                }
                                return true;
                            }
                        } else {
                            continue;
                        }
                    }
                } else if (isDuplicateMemberWithinTargetType(existingMember, this, typeTransformerSignature)) {
                    getWorld().getMessageHandler().handleMessage(MessageUtil.error(WeaverMessages.format(WeaverMessages.ITD_MEMBER_CONFLICT, typeTransformer.getAspectType().getName(), existingMember), typeTransformer.getSourceLocation()));
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isDuplicateMemberWithinTargetType(ResolvedMember existingMember, ResolvedType targetType, ResolvedMember itdMember) {
        if (existingMember.isAbstract() || itdMember.isAbstract()) {
            return false;
        }
        UnresolvedType declaringType = existingMember.getDeclaringType();
        if (!targetType.equals(declaringType) || Modifier.isPrivate(itdMember.getModifiers())) {
            return false;
        }
        if (!itdMember.isPublic() && !targetType.getPackageName().equals(itdMember.getDeclaringType().getPackageName())) {
            return false;
        }
        return true;
    }

    public boolean checkLegalOverride(ResolvedMember parent, ResolvedMember child, int transformerPosition, ResolvedType aspectType) {
        boolean incompatibleReturnTypes;
        ResolvedType nonItdDeclaringType;
        WeaverStateInfo wsi;
        List<ConcreteTypeMunger> transformersOnThisType;
        if (Modifier.isFinal(parent.getModifiers())) {
            if (transformerPosition == 16 && aspectType != null && (wsi = (nonItdDeclaringType = child.getDeclaringType().resolve(this.world)).getWeaverState()) != null && (transformersOnThisType = wsi.getTypeMungers(nonItdDeclaringType)) != null) {
                for (ConcreteTypeMunger transformer : transformersOnThisType) {
                    if (transformer.aspectType.equals(aspectType) && parent.equalsApartFromDeclaringType(transformer.getSignature())) {
                        return true;
                    }
                }
            }
            this.world.showMessage(Message.ERROR, WeaverMessages.format(WeaverMessages.CANT_OVERRIDE_FINAL_MEMBER, parent), child.getSourceLocation(), null);
            return false;
        }
        if (this.world.isInJava5Mode() && parent.getKind() == Member.METHOD) {
            ResolvedType rtParentReturnType = parent.resolve(this.world).getGenericReturnType().resolve(this.world);
            ResolvedType rtChildReturnType = child.resolve(this.world).getGenericReturnType().resolve(this.world);
            incompatibleReturnTypes = !rtParentReturnType.isAssignableFrom(rtChildReturnType);
        } else {
            ResolvedType rtParentReturnType2 = parent.resolve(this.world).getGenericReturnType().resolve(this.world);
            ResolvedType rtChildReturnType2 = child.resolve(this.world).getGenericReturnType().resolve(this.world);
            incompatibleReturnTypes = !rtParentReturnType2.equals(rtChildReturnType2);
        }
        if (incompatibleReturnTypes) {
            this.world.showMessage(IMessage.ERROR, WeaverMessages.format(WeaverMessages.ITD_RETURN_TYPE_MISMATCH, parent, child), child.getSourceLocation(), parent.getSourceLocation());
            return false;
        }
        if (parent.getKind() == Member.POINTCUT) {
            UnresolvedType[] pTypes = parent.getParameterTypes();
            UnresolvedType[] cTypes = child.getParameterTypes();
            if (!Arrays.equals(pTypes, cTypes)) {
                this.world.showMessage(IMessage.ERROR, WeaverMessages.format(WeaverMessages.ITD_PARAM_TYPE_MISMATCH, parent, child), child.getSourceLocation(), parent.getSourceLocation());
                return false;
            }
        }
        if (isMoreVisible(parent.getModifiers(), child.getModifiers())) {
            this.world.showMessage(IMessage.ERROR, WeaverMessages.format(WeaverMessages.ITD_VISIBILITY_REDUCTION, parent, child), child.getSourceLocation(), parent.getSourceLocation());
            return false;
        }
        ResolvedType[] childExceptions = this.world.resolve(child.getExceptions());
        ResolvedType[] parentExceptions = this.world.resolve(parent.getExceptions());
        ResolvedType runtimeException = this.world.resolve("java.lang.RuntimeException");
        ResolvedType error = this.world.resolve("java.lang.Error");
        int leni = childExceptions.length;
        for (int i = 0; i < leni; i++) {
            if (!runtimeException.isAssignableFrom(childExceptions[i]) && !error.isAssignableFrom(childExceptions[i])) {
                for (ResolvedType resolvedType : parentExceptions) {
                    if (resolvedType.isAssignableFrom(childExceptions[i])) {
                        break;
                    }
                }
                return false;
            }
        }
        boolean parentStatic = Modifier.isStatic(parent.getModifiers());
        boolean childStatic = Modifier.isStatic(child.getModifiers());
        if (parentStatic && !childStatic) {
            this.world.showMessage(IMessage.ERROR, WeaverMessages.format(WeaverMessages.ITD_OVERRIDDEN_STATIC, child, parent), child.getSourceLocation(), null);
            return false;
        }
        if (childStatic && !parentStatic) {
            this.world.showMessage(IMessage.ERROR, WeaverMessages.format(WeaverMessages.ITD_OVERIDDING_STATIC, child, parent), child.getSourceLocation(), null);
            return false;
        }
        return true;
    }

    private int compareMemberPrecedence(ResolvedMember m1, ResolvedMember m2) {
        UnresolvedType declaring;
        if (Modifier.isProtected(m2.getModifiers()) && m2.getName().charAt(0) == 'c' && (declaring = m2.getDeclaringType()) != null && declaring.getName().equals("java.lang.Object") && m2.getName().equals("clone")) {
            return 1;
        }
        if (Modifier.isAbstract(m1.getModifiers())) {
            return -1;
        }
        if (Modifier.isAbstract(m2.getModifiers())) {
            return 1;
        }
        if (m1.getDeclaringType().equals(m2.getDeclaringType())) {
            return 0;
        }
        ResolvedType t1 = m1.getDeclaringType().resolve(this.world);
        ResolvedType t2 = m2.getDeclaringType().resolve(this.world);
        if (t1.isAssignableFrom(t2)) {
            return -1;
        }
        if (t2.isAssignableFrom(t1)) {
            return 1;
        }
        return 0;
    }

    public static boolean isMoreVisible(int m1, int m2) {
        if (Modifier.isPrivate(m1)) {
            return false;
        }
        if (isPackage(m1)) {
            return Modifier.isPrivate(m2);
        }
        if (Modifier.isProtected(m1)) {
            return Modifier.isPrivate(m2) || isPackage(m2);
        }
        if (Modifier.isPublic(m1)) {
            return !Modifier.isPublic(m2);
        }
        throw new RuntimeException("bad modifier: " + m1);
    }

    private static boolean isPackage(int i) {
        return 0 == (i & 7);
    }

    private void interTypeConflictError(ConcreteTypeMunger m1, ConcreteTypeMunger m2) {
        getWorld().showMessage(IMessage.ERROR, WeaverMessages.format(WeaverMessages.ITD_CONFLICT, m1.getAspectType().getName(), m2.getSignature(), m2.getAspectType().getName()), m2.getSourceLocation(), getSourceLocation());
    }

    public ResolvedMember lookupSyntheticMember(Member member) {
        for (ConcreteTypeMunger m : this.interTypeMungers) {
            ResolvedMember ret = m.getMatchingSyntheticMember(member);
            if (ret != null) {
                return ret;
            }
        }
        if (this.world.isJoinpointArrayConstructionEnabled() && isArray() && member.getKind() == Member.CONSTRUCTOR) {
            ResolvedMemberImpl ret2 = new ResolvedMemberImpl(Member.CONSTRUCTOR, this, 1, UnresolvedType.VOID, "<init>", this.world.resolve(member.getParameterTypes()));
            int count = ret2.getParameterTypes().length;
            String[] paramNames = new String[count];
            for (int i = 0; i < count; i++) {
                paramNames[i] = new StringBuffer("dim").append(i).toString();
            }
            ret2.setParameterNames(paramNames);
            return ret2;
        }
        return null;
    }

    /* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/ResolvedType$SuperClassWalker.class */
    static class SuperClassWalker implements Iterator<ResolvedType> {
        private ResolvedType curr;
        private SuperInterfaceWalker iwalker;
        private boolean wantGenerics;

        public SuperClassWalker(ResolvedType type, SuperInterfaceWalker iwalker, boolean genericsAware) {
            this.curr = type;
            this.iwalker = iwalker;
            this.wantGenerics = genericsAware;
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            return this.curr != null;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.util.Iterator
        public ResolvedType next() {
            ResolvedType ret = this.curr;
            if (!this.wantGenerics && ret.isParameterizedOrGenericType()) {
                ret = ret.getRawType();
            }
            this.iwalker.push(ret);
            this.curr = this.curr.getSuperclass();
            return ret;
        }

        @Override // java.util.Iterator
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    /* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/ResolvedType$SuperInterfaceWalker.class */
    static class SuperInterfaceWalker implements Iterator<ResolvedType> {
        private Iterators.Getter<ResolvedType, ResolvedType> ifaceGetter;
        Iterator<ResolvedType> delegate;
        public Queue<ResolvedType> toPersue;
        public Set<ResolvedType> visited;

        SuperInterfaceWalker(Iterators.Getter<ResolvedType, ResolvedType> ifaceGetter) {
            this.delegate = null;
            this.toPersue = new LinkedList();
            this.visited = new HashSet();
            this.ifaceGetter = ifaceGetter;
        }

        SuperInterfaceWalker(Iterators.Getter<ResolvedType, ResolvedType> ifaceGetter, ResolvedType interfaceType) {
            this.delegate = null;
            this.toPersue = new LinkedList();
            this.visited = new HashSet();
            this.ifaceGetter = ifaceGetter;
            this.delegate = Iterators.one(interfaceType);
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            if (this.delegate == null || !this.delegate.hasNext()) {
                if (!this.toPersue.isEmpty()) {
                    do {
                        ResolvedType next = this.toPersue.remove();
                        this.visited.add(next);
                        this.delegate = this.ifaceGetter.get(next);
                        if (this.delegate.hasNext()) {
                            break;
                        }
                    } while (!this.toPersue.isEmpty());
                } else {
                    return false;
                }
            }
            return this.delegate.hasNext();
        }

        public void push(ResolvedType ret) {
            this.toPersue.add(ret);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.util.Iterator
        public ResolvedType next() {
            ResolvedType next = this.delegate.next();
            if (this.visited.add(next)) {
                this.toPersue.add(next);
            }
            return next;
        }

        @Override // java.util.Iterator
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    public void clearInterTypeMungers() {
        if (isRawType()) {
            ResolvedType genericType = getGenericType();
            if (genericType.isRawType()) {
                System.err.println("DebugFor341926: Type " + getName() + " has an incorrect generic form");
            } else {
                genericType.clearInterTypeMungers();
            }
        }
        this.interTypeMungers = new ArrayList();
    }

    public boolean isTopmostImplementor(ResolvedType interfaceType) {
        boolean b = true;
        if (isInterface() || !interfaceType.isAssignableFrom(this, true)) {
            b = false;
        } else {
            ResolvedType superclass = getSuperclass();
            if (superclass.isMissing()) {
                b = true;
            } else if (interfaceType.isAssignableFrom(superclass, true)) {
                b = false;
            }
        }
        return b;
    }

    public ResolvedType getTopmostImplementor(ResolvedType interfaceType) {
        if (isInterface() || !interfaceType.isAssignableFrom(this)) {
            return null;
        }
        ResolvedType higherType = getSuperclass().getTopmostImplementor(interfaceType);
        if (higherType != null) {
            return higherType;
        }
        return this;
    }

    public List<ResolvedMember> getExposedPointcuts() {
        List<ResolvedMember> ret = new ArrayList<>();
        if (getSuperclass() != null) {
            ret.addAll(getSuperclass().getExposedPointcuts());
        }
        for (ResolvedType type : getDeclaredInterfaces()) {
            addPointcutsResolvingConflicts(ret, Arrays.asList(type.getDeclaredPointcuts()), false);
        }
        addPointcutsResolvingConflicts(ret, Arrays.asList(getDeclaredPointcuts()), true);
        for (ResolvedMember member : ret) {
            ResolvedPointcutDefinition inherited = (ResolvedPointcutDefinition) member;
            if (inherited != null && inherited.isAbstract() && !isAbstract()) {
                getWorld().showMessage(IMessage.ERROR, WeaverMessages.format(WeaverMessages.POINCUT_NOT_CONCRETE, inherited, getName()), inherited.getSourceLocation(), getSourceLocation());
            }
        }
        return ret;
    }

    private void addPointcutsResolvingConflicts(List<ResolvedMember> acc, List<ResolvedMember> added, boolean isOverriding) {
        Iterator<ResolvedMember> i = added.iterator();
        while (i.hasNext()) {
            ResolvedPointcutDefinition toAdd = (ResolvedPointcutDefinition) i.next();
            Iterator<ResolvedMember> j = acc.iterator();
            while (j.hasNext()) {
                ResolvedPointcutDefinition existing = (ResolvedPointcutDefinition) j.next();
                if (toAdd != null && existing != null && existing != toAdd) {
                    UnresolvedType pointcutDeclaringTypeUT = existing.getDeclaringType();
                    if (pointcutDeclaringTypeUT != null) {
                        ResolvedType pointcutDeclaringType = pointcutDeclaringTypeUT.resolve(getWorld());
                        if (!isVisible(existing.getModifiers(), pointcutDeclaringType, this)) {
                            if (existing.isAbstract() && conflictingSignature(existing, toAdd)) {
                                getWorld().showMessage(IMessage.ERROR, WeaverMessages.format(WeaverMessages.POINTCUT_NOT_VISIBLE, existing.getDeclaringType().getName() + "." + existing.getName() + "()", getName()), toAdd.getSourceLocation(), null);
                                j.remove();
                            }
                        }
                    }
                    if (conflictingSignature(existing, toAdd)) {
                        if (isOverriding) {
                            checkLegalOverride(existing, toAdd, 0, null);
                            j.remove();
                        } else {
                            getWorld().showMessage(IMessage.ERROR, WeaverMessages.format(WeaverMessages.CONFLICTING_INHERITED_POINTCUTS, getName() + toAdd.getSignature()), existing.getSourceLocation(), toAdd.getSourceLocation());
                            j.remove();
                        }
                    }
                }
            }
            acc.add(toAdd);
        }
    }

    public ISourceLocation getSourceLocation() {
        return null;
    }

    public boolean isExposedToWeaver() {
        return false;
    }

    public WeaverStateInfo getWeaverState() {
        return null;
    }

    public ReferenceType getGenericType() {
        return null;
    }

    @Override // org.aspectj.weaver.UnresolvedType
    public ResolvedType getRawType() {
        return super.getRawType().resolve(this.world);
    }

    public ResolvedType parameterizedWith(UnresolvedType[] typeParameters) {
        if (!isGenericType() && !isParameterizedType()) {
            return this;
        }
        return TypeFactory.createParameterizedType(getGenericType(), typeParameters, getWorld());
    }

    @Override // org.aspectj.weaver.UnresolvedType
    public UnresolvedType parameterize(Map<String, UnresolvedType> typeBindings) {
        if (!isParameterizedType()) {
            return this;
        }
        boolean workToDo = false;
        for (int i = 0; i < this.typeParameters.length; i++) {
            if (this.typeParameters[i].isTypeVariableReference() || (this.typeParameters[i] instanceof BoundedReferenceType) || this.typeParameters[i].isParameterizedType()) {
                workToDo = true;
            }
        }
        if (!workToDo) {
            return this;
        }
        UnresolvedType[] newTypeParams = new UnresolvedType[this.typeParameters.length];
        for (int i2 = 0; i2 < newTypeParams.length; i2++) {
            newTypeParams[i2] = this.typeParameters[i2];
            if (newTypeParams[i2].isTypeVariableReference()) {
                TypeVariableReferenceType tvrt = (TypeVariableReferenceType) newTypeParams[i2];
                UnresolvedType binding = typeBindings.get(tvrt.getTypeVariable().getName());
                if (binding != null) {
                    newTypeParams[i2] = binding;
                }
            } else if (newTypeParams[i2] instanceof BoundedReferenceType) {
                BoundedReferenceType brType = (BoundedReferenceType) newTypeParams[i2];
                newTypeParams[i2] = brType.parameterize(typeBindings);
            } else if (newTypeParams[i2].isParameterizedType()) {
                newTypeParams[i2] = newTypeParams[i2].parameterize(typeBindings);
            }
        }
        return TypeFactory.createParameterizedType(getGenericType(), newTypeParams, getWorld());
    }

    public boolean isException() {
        return this.world.getCoreType(UnresolvedType.JL_EXCEPTION).isAssignableFrom(this);
    }

    public boolean isCheckedException() {
        if (!isException() || this.world.getCoreType(UnresolvedType.RUNTIME_EXCEPTION).isAssignableFrom(this)) {
            return false;
        }
        return true;
    }

    public final boolean isConvertableFrom(ResolvedType other) {
        if (equals(OBJECT)) {
            return true;
        }
        if (this.world.isInJava5Mode() && (isPrimitiveType() ^ other.isPrimitiveType()) && validBoxing.contains(getSignature() + other.getSignature())) {
            return true;
        }
        if (isPrimitiveType() || other.isPrimitiveType()) {
            return isAssignableFrom(other);
        }
        return isCoerceableFrom(other);
    }

    public boolean needsNoConversionFrom(ResolvedType o) {
        return isAssignableFrom(o);
    }

    public String getSignatureForAttribute() {
        return this.signature;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public boolean isParameterizedWithTypeVariable() {
        if (this.parameterizedWithTypeVariable == FuzzyBoolean.MAYBE) {
            if (this.typeParameters == null || this.typeParameters.length == 0) {
                this.parameterizedWithTypeVariable = FuzzyBoolean.NO;
                return false;
            }
            for (int i = 0; i < this.typeParameters.length; i++) {
                ResolvedType aType = (ResolvedType) this.typeParameters[i];
                if (aType.isTypeVariableReference()) {
                    this.parameterizedWithTypeVariable = FuzzyBoolean.YES;
                    return true;
                }
                if (aType.isParameterizedType() && aType.isParameterizedWithTypeVariable()) {
                    this.parameterizedWithTypeVariable = FuzzyBoolean.YES;
                    return true;
                }
                if (aType.isGenericWildcard()) {
                    BoundedReferenceType boundedRT = (BoundedReferenceType) aType;
                    if (boundedRT.isExtends()) {
                        boolean b = false;
                        UnresolvedType upperBound = boundedRT.getUpperBound();
                        if (upperBound.isParameterizedType()) {
                            b = ((ResolvedType) upperBound).isParameterizedWithTypeVariable();
                        } else if (upperBound.isTypeVariableReference() && ((TypeVariableReference) upperBound).getTypeVariable().getDeclaringElementKind() == 1) {
                            b = true;
                        }
                        if (b) {
                            this.parameterizedWithTypeVariable = FuzzyBoolean.YES;
                            return true;
                        }
                    }
                    if (boundedRT.isSuper()) {
                        boolean b2 = false;
                        UnresolvedType lowerBound = boundedRT.getLowerBound();
                        if (lowerBound.isParameterizedType()) {
                            b2 = ((ResolvedType) lowerBound).isParameterizedWithTypeVariable();
                        } else if (lowerBound.isTypeVariableReference() && ((TypeVariableReference) lowerBound).getTypeVariable().getDeclaringElementKind() == 1) {
                            b2 = true;
                        }
                        if (b2) {
                            this.parameterizedWithTypeVariable = FuzzyBoolean.YES;
                            return true;
                        }
                    } else {
                        continue;
                    }
                }
            }
            this.parameterizedWithTypeVariable = FuzzyBoolean.NO;
        }
        return this.parameterizedWithTypeVariable.alwaysTrue();
    }

    protected boolean ajMembersNeedParameterization() {
        if (isParameterizedType()) {
            return true;
        }
        ResolvedType superclass = getSuperclass();
        if (superclass != null && !superclass.isMissing()) {
            return superclass.ajMembersNeedParameterization();
        }
        return false;
    }

    protected Map<String, UnresolvedType> getAjMemberParameterizationMap() {
        Map<String, UnresolvedType> myMap = getMemberParameterizationMap();
        if (myMap.isEmpty() && getSuperclass() != null) {
            return getSuperclass().getAjMemberParameterizationMap();
        }
        return myMap;
    }

    public void setBinaryPath(String binaryPath) {
        this.binaryPath = binaryPath;
    }

    public String getBinaryPath() {
        return this.binaryPath;
    }

    public void ensureConsistent() {
    }

    public boolean isInheritedAnnotation() {
        ensureAnnotationBitsInitialized();
        return (this.bits & AnnotationMarkedInherited) != 0;
    }

    private void ensureAnnotationBitsInitialized() {
        if ((this.bits & AnnotationBitsInitialized) == 0) {
            this.bits |= AnnotationBitsInitialized;
            if (hasAnnotation(UnresolvedType.AT_INHERITED)) {
                this.bits |= AnnotationMarkedInherited;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean hasNewParentMungers() {
        if ((this.bits & MungersAnalyzed) == 0) {
            this.bits |= MungersAnalyzed;
            for (ConcreteTypeMunger munger : this.interTypeMungers) {
                ResolvedTypeMunger resolvedTypeMunger = munger.getMunger();
                if (resolvedTypeMunger != null && resolvedTypeMunger.getKind() == ResolvedTypeMunger.Parent) {
                    this.bits |= HasParentMunger;
                }
            }
        }
        return (this.bits & HasParentMunger) != 0;
    }

    public void tagAsTypeHierarchyComplete() {
        if (isParameterizedOrRawType()) {
            ReferenceType genericType = getGenericType();
            genericType.tagAsTypeHierarchyComplete();
        } else {
            this.bits |= TypeHierarchyCompleteBit;
        }
    }

    public boolean isTypeHierarchyComplete() {
        if (isParameterizedOrRawType()) {
            return getGenericType().isTypeHierarchyComplete();
        }
        return (this.bits & TypeHierarchyCompleteBit) != 0;
    }

    public int getCompilerVersion() {
        return AjAttribute.WeaverVersionInfo.getCurrentWeaverMajorVersion();
    }

    public boolean isPrimitiveArray() {
        return false;
    }

    public boolean isGroovyObject() {
        if ((this.bits & GroovyObjectInitialized) == 0) {
            ResolvedType[] intfaces = getDeclaredInterfaces();
            boolean done = false;
            if (intfaces != null) {
                int length = intfaces.length;
                int i = 0;
                while (true) {
                    if (i >= length) {
                        break;
                    }
                    ResolvedType intface = intfaces[i];
                    if (!intface.getName().equals("groovy.lang.GroovyObject")) {
                        i++;
                    } else {
                        this.bits |= IsGroovyObject;
                        done = true;
                        break;
                    }
                }
            }
            if (!done && getSuperclass().getName().equals("groovy.lang.GroovyObjectSupport")) {
                this.bits |= IsGroovyObject;
            }
            this.bits |= GroovyObjectInitialized;
        }
        return (this.bits & IsGroovyObject) != 0;
    }
}
