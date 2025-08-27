package org.aspectj.weaver.patterns;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.aspectj.bridge.AbortException;
import org.aspectj.bridge.IMessage;
import org.aspectj.bridge.MessageUtil;
import org.aspectj.util.FuzzyBoolean;
import org.aspectj.weaver.AnnotatedElement;
import org.aspectj.weaver.BCException;
import org.aspectj.weaver.CompressingDataOutputStream;
import org.aspectj.weaver.ISourceContext;
import org.aspectj.weaver.ResolvedType;
import org.aspectj.weaver.UnresolvedType;
import org.aspectj.weaver.VersionedDataInputStream;
import org.aspectj.weaver.WeaverMessages;
import org.aspectj.weaver.World;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/patterns/WildAnnotationTypePattern.class */
public class WildAnnotationTypePattern extends AnnotationTypePattern {
    private TypePattern typePattern;
    private boolean resolved = false;
    Map<String, String> annotationValues;
    private static final byte VERSION = 1;

    public WildAnnotationTypePattern(TypePattern typePattern) {
        this.typePattern = typePattern;
        setLocation(typePattern.getSourceContext(), typePattern.start, typePattern.end);
    }

    public WildAnnotationTypePattern(TypePattern typePattern, Map<String, String> annotationValues) {
        this.typePattern = typePattern;
        this.annotationValues = annotationValues;
        setLocation(typePattern.getSourceContext(), typePattern.start, typePattern.end);
    }

    public TypePattern getTypePattern() {
        return this.typePattern;
    }

    @Override // org.aspectj.weaver.patterns.AnnotationTypePattern
    public FuzzyBoolean matches(AnnotatedElement annotated) {
        return matches(annotated, null);
    }

    /* JADX WARN: Code restructure failed: missing block: B:82:0x03ea, code lost:
    
        r0 = r0.substring(0, r0.lastIndexOf(46));
        r0 = r8.lookupType(r0, r6).resolve(r8.getWorld());
     */
    /* JADX WARN: Code restructure failed: missing block: B:83:0x0412, code lost:
    
        if (r0.isMissing() == false) goto L85;
     */
    /* JADX WARN: Code restructure failed: missing block: B:84:0x0415, code lost:
    
        r0 = org.aspectj.bridge.MessageUtil.error("Unable to resolve type '" + r0 + "' specified for value '" + r0 + "'", getSourceLocation());
        r8.getWorld().getMessageHandler().handleMessage(r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:85:0x0452, code lost:
    
        r0.put(r0, r0.getSignature());
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    protected void resolveAnnotationValues(org.aspectj.weaver.ResolvedType r7, org.aspectj.weaver.patterns.IScope r8) throws org.aspectj.bridge.AbortException, java.lang.NumberFormatException {
        /*
            Method dump skipped, instructions count: 1374
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: org.aspectj.weaver.patterns.WildAnnotationTypePattern.resolveAnnotationValues(org.aspectj.weaver.ResolvedType, org.aspectj.weaver.patterns.IScope):void");
    }

    @Override // org.aspectj.weaver.patterns.AnnotationTypePattern
    public FuzzyBoolean matches(AnnotatedElement annotated, ResolvedType[] parameterAnnotations) {
        if (!this.resolved) {
            throw new IllegalStateException("Can't match on an unresolved annotation type pattern");
        }
        if (this.annotationValues != null && !this.typePattern.hasFailedResolution()) {
            throw new IllegalStateException("Cannot use annotationvalues with a wild annotation pattern");
        }
        if (isForParameterAnnotationMatch()) {
            if (parameterAnnotations != null && parameterAnnotations.length != 0) {
                for (ResolvedType resolvedType : parameterAnnotations) {
                    if (this.typePattern.matches(resolvedType, TypePattern.STATIC).alwaysTrue()) {
                        return FuzzyBoolean.YES;
                    }
                }
            }
        } else {
            ResolvedType[] annTypes = annotated.getAnnotationTypes();
            if (annTypes != null && annTypes.length != 0) {
                for (ResolvedType resolvedType2 : annTypes) {
                    if (this.typePattern.matches(resolvedType2, TypePattern.STATIC).alwaysTrue()) {
                        return FuzzyBoolean.YES;
                    }
                }
            }
        }
        return FuzzyBoolean.NO;
    }

    @Override // org.aspectj.weaver.patterns.AnnotationTypePattern
    public void resolve(World world) {
        ResolvedType resolvedType;
        if (!this.resolved) {
            if ((this.typePattern instanceof WildTypePattern) && (this.annotationValues == null || this.annotationValues.isEmpty())) {
                WildTypePattern wildTypePattern = (WildTypePattern) this.typePattern;
                String fullyQualifiedName = wildTypePattern.maybeGetCleanName();
                if (fullyQualifiedName != null && fullyQualifiedName.indexOf(".") != -1 && (resolvedType = world.resolve(UnresolvedType.forName(fullyQualifiedName))) != null && !resolvedType.isMissing()) {
                    this.typePattern = new ExactTypePattern(resolvedType, false, false);
                }
            }
            this.resolved = true;
        }
    }

    @Override // org.aspectj.weaver.patterns.AnnotationTypePattern
    public AnnotationTypePattern resolveBindings(IScope scope, Bindings bindings, boolean allowBinding) throws AbortException, NumberFormatException {
        if (!scope.getWorld().isInJava5Mode()) {
            scope.message(MessageUtil.error(WeaverMessages.format(WeaverMessages.ANNOTATIONS_NEED_JAVA5), getSourceLocation()));
            return this;
        }
        if (this.resolved) {
            return this;
        }
        this.typePattern = this.typePattern.resolveBindings(scope, bindings, false, false);
        this.resolved = true;
        if (this.typePattern instanceof ExactTypePattern) {
            ExactTypePattern et = (ExactTypePattern) this.typePattern;
            if (!et.getExactType().resolve(scope.getWorld()).isAnnotation()) {
                IMessage m = MessageUtil.error(WeaverMessages.format(WeaverMessages.REFERENCE_TO_NON_ANNOTATION_TYPE, et.getExactType().getName()), getSourceLocation());
                scope.getWorld().getMessageHandler().handleMessage(m);
                this.resolved = false;
            }
            ResolvedType annotationType = et.getExactType().resolve(scope.getWorld());
            resolveAnnotationValues(annotationType, scope);
            ExactAnnotationTypePattern eatp = new ExactAnnotationTypePattern(annotationType, this.annotationValues);
            eatp.copyLocationFrom(this);
            if (isForParameterAnnotationMatch()) {
                eatp.setForParameterAnnotationMatch();
            }
            return eatp;
        }
        return this;
    }

    @Override // org.aspectj.weaver.patterns.AnnotationTypePattern
    public AnnotationTypePattern parameterizeWith(Map typeVariableMap, World w) {
        WildAnnotationTypePattern ret = new WildAnnotationTypePattern(this.typePattern.parameterizeWith(typeVariableMap, w));
        ret.copyLocationFrom(this);
        ret.resolved = this.resolved;
        return ret;
    }

    @Override // org.aspectj.weaver.patterns.PatternNode
    public void write(CompressingDataOutputStream s) throws IOException {
        s.writeByte(8);
        s.writeByte(1);
        this.typePattern.write(s);
        writeLocation(s);
        s.writeBoolean(isForParameterAnnotationMatch());
        if (this.annotationValues == null) {
            s.writeInt(0);
            return;
        }
        s.writeInt(this.annotationValues.size());
        Set<String> key = this.annotationValues.keySet();
        for (String k : key) {
            s.writeUTF(k);
            s.writeUTF(this.annotationValues.get(k));
        }
    }

    public static AnnotationTypePattern read(VersionedDataInputStream s, ISourceContext context) throws IOException {
        int annotationValueCount;
        byte version = s.readByte();
        if (version > 1) {
            throw new BCException("ExactAnnotationTypePattern was written by a newer version of AspectJ");
        }
        TypePattern t = TypePattern.read(s, context);
        WildAnnotationTypePattern ret = new WildAnnotationTypePattern(t);
        ret.readLocation(context, s);
        if (s.getMajorVersion() >= 4 && s.readBoolean()) {
            ret.setForParameterAnnotationMatch();
        }
        if (s.getMajorVersion() >= 5 && (annotationValueCount = s.readInt()) > 0) {
            Map<String, String> aValues = new HashMap<>();
            for (int i = 0; i < annotationValueCount; i++) {
                String key = s.readUTF();
                String val = s.readUTF();
                aValues.put(key, val);
            }
            ret.annotationValues = aValues;
        }
        return ret;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof WildAnnotationTypePattern)) {
            return false;
        }
        WildAnnotationTypePattern other = (WildAnnotationTypePattern) obj;
        return other.typePattern.equals(this.typePattern) && isForParameterAnnotationMatch() == other.isForParameterAnnotationMatch() && (this.annotationValues != null ? this.annotationValues.equals(other.annotationValues) : other.annotationValues == null);
    }

    public int hashCode() {
        return ((((17 + (37 * this.typePattern.hashCode())) * 37) + (isForParameterAnnotationMatch() ? 0 : 1)) * 37) + (this.annotationValues == null ? 0 : this.annotationValues.hashCode());
    }

    public String toString() {
        return "@(" + this.typePattern.toString() + ")";
    }

    @Override // org.aspectj.weaver.patterns.PatternNode
    public Object accept(PatternNodeVisitor visitor, Object data) {
        return visitor.visit(this, data);
    }
}
