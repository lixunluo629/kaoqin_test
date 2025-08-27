package org.aspectj.weaver.patterns;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.apache.xmlbeans.XmlErrorCodes;
import org.aspectj.util.FuzzyBoolean;
import org.aspectj.weaver.BCException;
import org.aspectj.weaver.CompressingDataOutputStream;
import org.aspectj.weaver.ISourceContext;
import org.aspectj.weaver.ResolvedType;
import org.aspectj.weaver.TypeVariableReference;
import org.aspectj.weaver.TypeVariableReferenceType;
import org.aspectj.weaver.UnresolvedType;
import org.aspectj.weaver.VersionedDataInputStream;
import org.aspectj.weaver.World;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/patterns/ExactTypePattern.class */
public class ExactTypePattern extends TypePattern {
    protected UnresolvedType type;
    protected transient ResolvedType resolvedType;
    public boolean checked;
    public boolean isVoid;
    public static final Map<String, Class<?>> primitiveTypesMap = new HashMap();
    public static final Map<String, Class<?>> boxedPrimitivesMap;
    private static final Map<String, Class<?>> boxedTypesMap;
    private static final byte EXACT_VERSION = 1;

    static {
        primitiveTypesMap.put(XmlErrorCodes.INT, Integer.TYPE);
        primitiveTypesMap.put("short", Short.TYPE);
        primitiveTypesMap.put(XmlErrorCodes.LONG, Long.TYPE);
        primitiveTypesMap.put("byte", Byte.TYPE);
        primitiveTypesMap.put("char", Character.TYPE);
        primitiveTypesMap.put(XmlErrorCodes.FLOAT, Float.TYPE);
        primitiveTypesMap.put(XmlErrorCodes.DOUBLE, Double.TYPE);
        boxedPrimitivesMap = new HashMap();
        boxedPrimitivesMap.put("java.lang.Integer", Integer.class);
        boxedPrimitivesMap.put("java.lang.Short", Short.class);
        boxedPrimitivesMap.put("java.lang.Long", Long.class);
        boxedPrimitivesMap.put("java.lang.Byte", Byte.class);
        boxedPrimitivesMap.put("java.lang.Character", Character.class);
        boxedPrimitivesMap.put("java.lang.Float", Float.class);
        boxedPrimitivesMap.put("java.lang.Double", Double.class);
        boxedTypesMap = new HashMap();
        boxedTypesMap.put(XmlErrorCodes.INT, Integer.class);
        boxedTypesMap.put("short", Short.class);
        boxedTypesMap.put(XmlErrorCodes.LONG, Long.class);
        boxedTypesMap.put("byte", Byte.class);
        boxedTypesMap.put("char", Character.class);
        boxedTypesMap.put(XmlErrorCodes.FLOAT, Float.class);
        boxedTypesMap.put(XmlErrorCodes.DOUBLE, Double.class);
    }

    @Override // org.aspectj.weaver.patterns.TypePattern
    protected boolean matchesSubtypes(ResolvedType type) {
        boolean match = super.matchesSubtypes(type);
        if (match) {
            return match;
        }
        if (type.isArray() && this.type.isArray()) {
            ResolvedType componentType = type.getComponentType().resolve(type.getWorld());
            UnresolvedType newPatternType = this.type.getComponentType();
            ExactTypePattern etp = new ExactTypePattern(newPatternType, this.includeSubtypes, false);
            return etp.matchesSubtypes(componentType, type);
        }
        return match;
    }

    public ExactTypePattern(UnresolvedType type, boolean includeSubtypes, boolean isVarArgs) {
        super(includeSubtypes, isVarArgs);
        this.checked = false;
        this.isVoid = false;
        this.type = type;
    }

    @Override // org.aspectj.weaver.patterns.TypePattern
    public boolean isArray() {
        return this.type.isArray();
    }

    @Override // org.aspectj.weaver.patterns.TypePattern
    protected boolean couldEverMatchSameTypesAs(TypePattern other) {
        if (super.couldEverMatchSameTypesAs(other)) {
            return true;
        }
        UnresolvedType otherType = other.getExactType();
        if (!ResolvedType.isMissing(otherType)) {
            return this.type.equals(otherType);
        }
        if (other instanceof WildTypePattern) {
            WildTypePattern owtp = (WildTypePattern) other;
            String yourSimpleNamePrefix = owtp.getNamePatterns()[0].maybeGetSimpleName();
            if (yourSimpleNamePrefix != null) {
                return this.type.getName().startsWith(yourSimpleNamePrefix);
            }
            return true;
        }
        return true;
    }

    @Override // org.aspectj.weaver.patterns.TypePattern
    protected boolean matchesExactly(ResolvedType matchType) {
        boolean annMatch;
        boolean typeMatch = this.type.equals(matchType);
        if (!typeMatch && (matchType.isParameterizedType() || matchType.isGenericType())) {
            typeMatch = this.type.equals(matchType.getRawType());
        }
        if (!typeMatch && matchType.isTypeVariableReference()) {
            typeMatch = matchesTypeVariable((TypeVariableReferenceType) matchType);
        }
        if (!typeMatch) {
            return false;
        }
        this.annotationPattern.resolve(matchType.getWorld());
        if (matchType.temporaryAnnotationTypes != null) {
            annMatch = this.annotationPattern.matches(matchType, matchType.temporaryAnnotationTypes).alwaysTrue();
        } else {
            annMatch = this.annotationPattern.matches(matchType).alwaysTrue();
        }
        return typeMatch && annMatch;
    }

    private boolean matchesTypeVariable(TypeVariableReferenceType matchType) {
        return this.type.equals(matchType.getTypeVariable().getFirstBound());
    }

    @Override // org.aspectj.weaver.patterns.TypePattern
    protected boolean matchesExactly(ResolvedType matchType, ResolvedType annotatedType) {
        boolean annMatch;
        boolean typeMatch = this.type.equals(matchType);
        if (!typeMatch && (matchType.isParameterizedType() || matchType.isGenericType())) {
            typeMatch = this.type.equals(matchType.getRawType());
        }
        if (!typeMatch && matchType.isTypeVariableReference()) {
            typeMatch = matchesTypeVariable((TypeVariableReferenceType) matchType);
        }
        this.annotationPattern.resolve(matchType.getWorld());
        if (annotatedType.temporaryAnnotationTypes != null) {
            annMatch = this.annotationPattern.matches(annotatedType, annotatedType.temporaryAnnotationTypes).alwaysTrue();
        } else {
            annMatch = this.annotationPattern.matches(annotatedType).alwaysTrue();
        }
        return typeMatch && annMatch;
    }

    public UnresolvedType getType() {
        return this.type;
    }

    public ResolvedType getResolvedExactType(World world) {
        if (this.resolvedType == null) {
            this.resolvedType = world.resolve(this.type);
        }
        return this.resolvedType;
    }

    @Override // org.aspectj.weaver.patterns.TypePattern
    public boolean isVoid() {
        if (!this.checked) {
            this.isVoid = this.type.getSignature().equals("V");
            this.checked = true;
        }
        return this.isVoid;
    }

    @Override // org.aspectj.weaver.patterns.TypePattern
    public FuzzyBoolean matchesInstanceof(ResolvedType matchType) {
        this.annotationPattern.resolve(matchType.getWorld());
        if (this.type.equals(ResolvedType.OBJECT)) {
            return FuzzyBoolean.YES.and(this.annotationPattern.matches(matchType));
        }
        ResolvedType resolvedType = this.type.resolve(matchType.getWorld());
        if (resolvedType.isAssignableFrom(matchType)) {
            return FuzzyBoolean.YES.and(this.annotationPattern.matches(matchType));
        }
        if (this.type.isPrimitiveType()) {
            return FuzzyBoolean.NO;
        }
        return matchType.isCoerceableFrom(this.type.resolve(matchType.getWorld())) ? FuzzyBoolean.MAYBE : FuzzyBoolean.NO;
    }

    public boolean equals(Object other) {
        if (!(other instanceof ExactTypePattern) || (other instanceof BindingTypePattern)) {
            return false;
        }
        ExactTypePattern o = (ExactTypePattern) other;
        return this.includeSubtypes == o.includeSubtypes && this.isVarArgs == o.isVarArgs && this.typeParameters.equals(o.typeParameters) && o.type.equals(this.type) && o.annotationPattern.equals(this.annotationPattern);
    }

    public int hashCode() {
        int result = (37 * 17) + this.type.hashCode();
        return (37 * ((37 * ((37 * ((37 * result) + new Boolean(this.includeSubtypes).hashCode())) + new Boolean(this.isVarArgs).hashCode())) + this.typeParameters.hashCode())) + this.annotationPattern.hashCode();
    }

    @Override // org.aspectj.weaver.patterns.PatternNode
    public void write(CompressingDataOutputStream out) throws IOException {
        out.writeByte(2);
        out.writeByte(1);
        out.writeCompressedSignature(this.type.getSignature());
        out.writeBoolean(this.includeSubtypes);
        out.writeBoolean(this.isVarArgs);
        this.annotationPattern.write(out);
        this.typeParameters.write(out);
        writeLocation(out);
    }

    public static TypePattern read(VersionedDataInputStream s, ISourceContext context) throws IOException {
        if (s.getMajorVersion() >= 2) {
            return readTypePattern150(s, context);
        }
        return readTypePatternOldStyle(s, context);
    }

    public static TypePattern readTypePattern150(VersionedDataInputStream s, ISourceContext context) throws IOException {
        byte version = s.readByte();
        if (version > 1) {
            throw new BCException("ExactTypePattern was written by a more recent version of AspectJ");
        }
        TypePattern ret = new ExactTypePattern(s.isAtLeast169() ? s.readSignatureAsUnresolvedType() : UnresolvedType.read(s), s.readBoolean(), s.readBoolean());
        ret.setAnnotationTypePattern(AnnotationTypePattern.read(s, context));
        ret.setTypeParameters(TypePatternList.read(s, context));
        ret.readLocation(context, s);
        return ret;
    }

    public static TypePattern readTypePatternOldStyle(DataInputStream s, ISourceContext context) throws IOException {
        TypePattern ret = new ExactTypePattern(UnresolvedType.read(s), s.readBoolean(), false);
        ret.readLocation(context, s);
        return ret;
    }

    public String toString() {
        StringBuffer buff = new StringBuffer();
        if (this.annotationPattern != AnnotationTypePattern.ANY) {
            buff.append('(');
            buff.append(this.annotationPattern.toString());
            buff.append(' ');
        }
        String typeString = this.type.toString();
        if (this.isVarArgs) {
            typeString = typeString.substring(0, typeString.lastIndexOf(91));
        }
        buff.append(typeString);
        if (this.includeSubtypes) {
            buff.append('+');
        }
        if (this.isVarArgs) {
            buff.append("...");
        }
        if (this.annotationPattern != AnnotationTypePattern.ANY) {
            buff.append(')');
        }
        return buff.toString();
    }

    @Override // org.aspectj.weaver.patterns.TypePattern
    public TypePattern resolveBindings(IScope scope, Bindings bindings, boolean allowBinding, boolean requireExactType) {
        throw new BCException("trying to re-resolve");
    }

    @Override // org.aspectj.weaver.patterns.TypePattern
    public TypePattern parameterizeWith(Map<String, UnresolvedType> typeVariableMap, World w) {
        UnresolvedType newType = this.type;
        if (this.type.isTypeVariableReference()) {
            TypeVariableReference t = (TypeVariableReference) this.type;
            String key = t.getTypeVariable().getName();
            if (typeVariableMap.containsKey(key)) {
                newType = typeVariableMap.get(key);
            }
        } else if (this.type.isParameterizedType()) {
            newType = w.resolve(this.type).parameterize(typeVariableMap);
        }
        ExactTypePattern ret = new ExactTypePattern(newType, this.includeSubtypes, this.isVarArgs);
        ret.annotationPattern = this.annotationPattern.parameterizeWith(typeVariableMap, w);
        ret.copyLocationFrom(this);
        return ret;
    }

    @Override // org.aspectj.weaver.patterns.PatternNode
    public Object accept(PatternNodeVisitor visitor, Object data) {
        return visitor.visit(this, data);
    }
}
