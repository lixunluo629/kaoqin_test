package org.aspectj.weaver.patterns;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import org.aspectj.bridge.AbortException;
import org.aspectj.bridge.MessageUtil;
import org.aspectj.util.FuzzyBoolean;
import org.aspectj.weaver.BCException;
import org.aspectj.weaver.ISourceContext;
import org.aspectj.weaver.IntMap;
import org.aspectj.weaver.ReferenceType;
import org.aspectj.weaver.ResolvedType;
import org.aspectj.weaver.TypeVariableReference;
import org.aspectj.weaver.UnresolvedType;
import org.aspectj.weaver.VersionedDataInputStream;
import org.aspectj.weaver.WeaverMessages;
import org.aspectj.weaver.World;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/patterns/TypePattern.class */
public abstract class TypePattern extends PatternNode {
    public static final MatchKind STATIC = new MatchKind("STATIC");
    public static final MatchKind DYNAMIC = new MatchKind("DYNAMIC");
    public static final TypePattern ELLIPSIS = new EllipsisTypePattern();
    public static final TypePattern ANY = new AnyTypePattern();
    public static final TypePattern NO = new NoTypePattern();
    protected boolean includeSubtypes;
    protected boolean isVarArgs;
    protected AnnotationTypePattern annotationPattern;
    protected TypePatternList typeParameters;
    public static final byte WILD = 1;
    public static final byte EXACT = 2;
    public static final byte BINDING = 3;
    public static final byte ELLIPSIS_KEY = 4;
    public static final byte ANY_KEY = 5;
    public static final byte NOT = 6;
    public static final byte OR = 7;
    public static final byte AND = 8;
    public static final byte NO_KEY = 9;
    public static final byte ANY_WITH_ANNO = 10;
    public static final byte HAS_MEMBER = 11;
    public static final byte TYPE_CATEGORY = 12;

    public abstract FuzzyBoolean matchesInstanceof(ResolvedType resolvedType);

    protected abstract boolean matchesExactly(ResolvedType resolvedType);

    protected abstract boolean matchesExactly(ResolvedType resolvedType, ResolvedType resolvedType2);

    public abstract TypePattern parameterizeWith(Map<String, UnresolvedType> map, World world);

    /* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/patterns/TypePattern$MatchKind.class */
    public static class MatchKind {
        private String name;

        public MatchKind(String name) {
            this.name = name;
        }

        public String toString() {
            return this.name;
        }
    }

    protected TypePattern(boolean includeSubtypes, boolean isVarArgs, TypePatternList typeParams) {
        this.isVarArgs = false;
        this.annotationPattern = AnnotationTypePattern.ANY;
        this.typeParameters = TypePatternList.EMPTY;
        this.includeSubtypes = includeSubtypes;
        this.isVarArgs = isVarArgs;
        this.typeParameters = typeParams == null ? TypePatternList.EMPTY : typeParams;
    }

    protected TypePattern(boolean includeSubtypes, boolean isVarArgs) {
        this(includeSubtypes, isVarArgs, null);
    }

    public AnnotationTypePattern getAnnotationPattern() {
        return this.annotationPattern;
    }

    public boolean isVarArgs() {
        return this.isVarArgs;
    }

    public boolean isStarAnnotation() {
        return this.annotationPattern == AnnotationTypePattern.ANY;
    }

    public boolean isArray() {
        return false;
    }

    protected TypePattern(boolean includeSubtypes) {
        this(includeSubtypes, false);
    }

    public void setAnnotationTypePattern(AnnotationTypePattern annPatt) {
        this.annotationPattern = annPatt;
    }

    public void setTypeParameters(TypePatternList typeParams) {
        this.typeParameters = typeParams;
    }

    public TypePatternList getTypeParameters() {
        return this.typeParameters;
    }

    public void setIsVarArgs(boolean isVarArgs) {
        this.isVarArgs = isVarArgs;
    }

    protected boolean couldEverMatchSameTypesAs(TypePattern other) {
        if (this.includeSubtypes || other.includeSubtypes || this.annotationPattern != AnnotationTypePattern.ANY || other.annotationPattern != AnnotationTypePattern.ANY) {
            return true;
        }
        return false;
    }

    public boolean matchesStatically(ResolvedType type) {
        if (this.includeSubtypes) {
            return matchesSubtypes(type);
        }
        return matchesExactly(type);
    }

    public final FuzzyBoolean matches(ResolvedType type, MatchKind kind) {
        if (type.isMissing()) {
            return FuzzyBoolean.NO;
        }
        if (kind == STATIC) {
            return FuzzyBoolean.fromBoolean(matchesStatically(type));
        }
        if (kind == DYNAMIC) {
            return matchesInstanceof(type);
        }
        throw new IllegalArgumentException("kind must be DYNAMIC or STATIC");
    }

    /* JADX WARN: Multi-variable type inference failed */
    protected boolean matchesSubtypes(ResolvedType resolvedType) {
        Iterator<ResolvedType> directSupertypes;
        ReferenceType referenceType;
        if (matchesExactly(resolvedType)) {
            return true;
        }
        if (resolvedType.isTypeVariableReference()) {
            directSupertypes = ((TypeVariableReference) resolvedType).getTypeVariable().getFirstBound().resolve(resolvedType.getWorld()).getDirectSupertypes();
            referenceType = resolvedType;
        } else {
            boolean zIsRawType = resolvedType.isRawType();
            ReferenceType genericType = resolvedType;
            if (zIsRawType) {
                genericType = resolvedType.getGenericType();
            }
            directSupertypes = genericType.getDirectSupertypes();
            referenceType = genericType;
        }
        Iterator<ResolvedType> it = directSupertypes;
        while (it.hasNext()) {
            if (matchesSubtypes(it.next(), referenceType)) {
                return true;
            }
        }
        return false;
    }

    protected boolean matchesSubtypes(ResolvedType superType, ResolvedType annotatedType) {
        if (matchesExactly(superType, annotatedType)) {
            return true;
        }
        if (superType.isParameterizedType() || superType.isRawType()) {
            superType = superType.getGenericType();
        }
        Iterator<ResolvedType> i = superType.getDirectSupertypes();
        while (i.hasNext()) {
            ResolvedType superSuperType = i.next();
            if (matchesSubtypes(superSuperType, annotatedType)) {
                return true;
            }
        }
        return false;
    }

    public UnresolvedType resolveExactType(IScope scope, Bindings bindings) {
        TypePattern p = resolveBindings(scope, bindings, false, true);
        if (!(p instanceof ExactTypePattern)) {
            return ResolvedType.MISSING;
        }
        return ((ExactTypePattern) p).getType();
    }

    public UnresolvedType getExactType() {
        if (this instanceof ExactTypePattern) {
            return ((ExactTypePattern) this).getType();
        }
        return ResolvedType.MISSING;
    }

    protected TypePattern notExactType(IScope s) throws AbortException {
        s.getMessageHandler().handleMessage(MessageUtil.error(WeaverMessages.format(WeaverMessages.EXACT_TYPE_PATTERN_REQD), getSourceLocation()));
        return NO;
    }

    public TypePattern resolveBindings(IScope scope, Bindings bindings, boolean allowBinding, boolean requireExactType) {
        this.annotationPattern = this.annotationPattern.resolveBindings(scope, bindings, allowBinding);
        return this;
    }

    public void resolve(World world) {
        this.annotationPattern.resolve(world);
    }

    public void postRead(ResolvedType enclosingType) {
    }

    public boolean isEllipsis() {
        return false;
    }

    public boolean isStar() {
        return false;
    }

    public TypePattern remapAdviceFormals(IntMap bindings) {
        return this;
    }

    public static TypePattern read(VersionedDataInputStream s, ISourceContext context) throws IOException {
        byte key = s.readByte();
        switch (key) {
            case 1:
                return WildTypePattern.read(s, context);
            case 2:
                return ExactTypePattern.read(s, context);
            case 3:
                return BindingTypePattern.read(s, context);
            case 4:
                return ELLIPSIS;
            case 5:
                return ANY;
            case 6:
                return NotTypePattern.read(s, context);
            case 7:
                return OrTypePattern.read(s, context);
            case 8:
                return AndTypePattern.read(s, context);
            case 9:
                return NO;
            case 10:
                return AnyWithAnnotationTypePattern.read(s, context);
            case 11:
                return HasMemberTypePattern.read(s, context);
            case 12:
                return TypeCategoryTypePattern.read(s, context);
            default:
                throw new BCException("unknown TypePattern kind: " + ((int) key));
        }
    }

    public boolean isIncludeSubtypes() {
        return this.includeSubtypes;
    }

    public boolean isBangVoid() {
        return false;
    }

    public boolean isVoid() {
        return false;
    }

    public boolean hasFailedResolution() {
        return false;
    }
}
