package org.aspectj.weaver.patterns;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.xmlbeans.impl.jam.xml.JamXmlElements;
import org.aspectj.bridge.MessageUtil;
import org.aspectj.weaver.AnnotationAJ;
import org.aspectj.weaver.CompressingDataOutputStream;
import org.aspectj.weaver.ISourceContext;
import org.aspectj.weaver.ResolvedMember;
import org.aspectj.weaver.ResolvedType;
import org.aspectj.weaver.UnresolvedType;
import org.aspectj.weaver.VersionedDataInputStream;
import org.aspectj.weaver.WeaverMessages;
import org.aspectj.weaver.World;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/patterns/DeclareAnnotation.class */
public class DeclareAnnotation extends Declare {
    public static final Kind AT_TYPE = new Kind(1, "type");
    public static final Kind AT_FIELD = new Kind(2, JamXmlElements.FIELD);
    public static final Kind AT_METHOD = new Kind(3, JamXmlElements.METHOD);
    public static final Kind AT_CONSTRUCTOR = new Kind(4, "constructor");
    public static final Kind AT_REMOVE_FROM_FIELD = new Kind(5, "removeFromField");
    private Kind kind;
    private TypePattern typePattern;
    private ISignaturePattern signaturePattern;
    private ResolvedType containingAspect;
    private List<String> annotationMethods;
    private List<String> annotationStrings;
    private AnnotationAJ annotation;
    private ResolvedType annotationType;
    private int annotationStart;
    private int annotationEnd;
    boolean isRemover = false;

    public DeclareAnnotation(Kind kind, TypePattern typePattern) {
        this.typePattern = typePattern;
        this.kind = kind;
        init();
    }

    public DeclareAnnotation(Kind kind, ISignaturePattern sigPattern) {
        this.signaturePattern = sigPattern;
        this.kind = kind;
        init();
    }

    private void init() {
        this.annotationMethods = new ArrayList();
        this.annotationMethods.add("unknown");
        this.annotationStrings = new ArrayList();
        this.annotationStrings.add("@<annotation>");
    }

    public String getAnnotationString() {
        return this.annotationStrings.get(0);
    }

    public boolean isExactPattern() {
        return this.typePattern instanceof ExactTypePattern;
    }

    public String getAnnotationMethod() {
        return this.annotationMethods.get(0);
    }

    public String toString() {
        StringBuilder ret = new StringBuilder();
        ret.append("declare @");
        ret.append(this.kind);
        ret.append(" : ");
        ret.append(this.typePattern != null ? this.typePattern.toString() : this.signaturePattern.toString());
        ret.append(" : ");
        ret.append(this.annotationStrings.get(0));
        return ret.toString();
    }

    @Override // org.aspectj.weaver.patterns.PatternNode
    public Object accept(PatternNodeVisitor visitor, Object data) {
        return visitor.visit(this, data);
    }

    @Override // org.aspectj.weaver.patterns.Declare
    public void resolve(IScope scope) {
        if (!scope.getWorld().isInJava5Mode()) {
            String msg = null;
            if (this.kind == AT_TYPE) {
                msg = WeaverMessages.DECLARE_ATTYPE_ONLY_SUPPORTED_AT_JAVA5_LEVEL;
            } else if (this.kind == AT_METHOD) {
                msg = WeaverMessages.DECLARE_ATMETHOD_ONLY_SUPPORTED_AT_JAVA5_LEVEL;
            } else if (this.kind == AT_FIELD) {
                msg = WeaverMessages.DECLARE_ATFIELD_ONLY_SUPPORTED_AT_JAVA5_LEVEL;
            } else if (this.kind == AT_CONSTRUCTOR) {
                msg = WeaverMessages.DECLARE_ATCONS_ONLY_SUPPORTED_AT_JAVA5_LEVEL;
            }
            scope.message(MessageUtil.error(WeaverMessages.format(msg), getSourceLocation()));
            return;
        }
        if (this.typePattern != null) {
            this.typePattern = this.typePattern.resolveBindings(scope, Bindings.NONE, false, false);
        }
        if (this.signaturePattern != null) {
            this.signaturePattern = this.signaturePattern.resolveBindings(scope, Bindings.NONE);
        }
        this.containingAspect = scope.getEnclosingType();
    }

    @Override // org.aspectj.weaver.patterns.Declare
    public Declare parameterizeWith(Map<String, UnresolvedType> typeVariableBindingMap, World w) {
        DeclareAnnotation ret;
        if (this.kind == AT_TYPE) {
            ret = new DeclareAnnotation(this.kind, this.typePattern.parameterizeWith(typeVariableBindingMap, w));
        } else {
            ret = new DeclareAnnotation(this.kind, this.signaturePattern.parameterizeWith(typeVariableBindingMap, w));
        }
        ret.annotationMethods = this.annotationMethods;
        ret.annotationStrings = this.annotationStrings;
        ret.annotation = this.annotation;
        ret.containingAspect = this.containingAspect;
        ret.copyLocationFrom(this);
        return ret;
    }

    @Override // org.aspectj.weaver.patterns.Declare
    public boolean isAdviceLike() {
        return false;
    }

    public void setAnnotationString(String annotationString) {
        this.annotationStrings.set(0, annotationString);
    }

    public void setAnnotationLocation(int start, int end) {
        this.annotationStart = start;
        this.annotationEnd = end;
    }

    public int getAnnotationSourceStart() {
        return this.annotationStart;
    }

    public int getAnnotationSourceEnd() {
        return this.annotationEnd;
    }

    public void setAnnotationMethod(String methodName) {
        this.annotationMethods.set(0, methodName);
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof DeclareAnnotation)) {
            return false;
        }
        DeclareAnnotation other = (DeclareAnnotation) obj;
        if (!this.kind.equals(other.kind) || !this.annotationStrings.get(0).equals(other.annotationStrings.get(0)) || !this.annotationMethods.get(0).equals(other.annotationMethods.get(0))) {
            return false;
        }
        if (this.typePattern != null && !this.typePattern.equals(other.typePattern)) {
            return false;
        }
        if (this.signaturePattern != null && !this.signaturePattern.equals(other.signaturePattern)) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        int result = (37 * 19) + this.kind.hashCode();
        int result2 = (37 * ((37 * result) + this.annotationStrings.get(0).hashCode())) + this.annotationMethods.get(0).hashCode();
        if (this.typePattern != null) {
            result2 = (37 * result2) + this.typePattern.hashCode();
        }
        if (this.signaturePattern != null) {
            result2 = (37 * result2) + this.signaturePattern.hashCode();
        }
        return result2;
    }

    @Override // org.aspectj.weaver.patterns.PatternNode
    public void write(CompressingDataOutputStream s) throws IOException {
        s.writeByte(5);
        if (this.kind.id == AT_FIELD.id && this.isRemover) {
            s.writeInt(AT_REMOVE_FROM_FIELD.id);
        } else {
            s.writeInt(this.kind.id);
        }
        int max = this.annotationStrings.size();
        s.writeByte(max);
        for (int i = 0; i < max; i++) {
            s.writeUTF(this.annotationStrings.get(i));
        }
        int max2 = this.annotationMethods.size();
        s.writeByte(max2);
        for (int i2 = 0; i2 < max2; i2++) {
            s.writeUTF(this.annotationMethods.get(i2));
        }
        if (this.typePattern != null) {
            this.typePattern.write(s);
        }
        if (this.signaturePattern != null) {
            AbstractSignaturePattern.writeCompoundSignaturePattern(s, this.signaturePattern);
        }
        writeLocation(s);
    }

    public static Declare read(VersionedDataInputStream s, ISourceContext context) throws IOException {
        DeclareAnnotation ret = null;
        boolean isRemover = false;
        int kind = s.readInt();
        if (kind == AT_REMOVE_FROM_FIELD.id) {
            kind = AT_FIELD.id;
            isRemover = true;
        }
        if (s.getMajorVersion() >= 7) {
            s.readByte();
        }
        String annotationString = s.readUTF();
        if (s.getMajorVersion() >= 7) {
            s.readByte();
        }
        String annotationMethod = s.readUTF();
        switch (kind) {
            case 1:
                TypePattern tp = TypePattern.read(s, context);
                ret = new DeclareAnnotation(AT_TYPE, tp);
                break;
            case 2:
                if (s.getMajorVersion() >= 7) {
                    ret = new DeclareAnnotation(AT_FIELD, AbstractSignaturePattern.readCompoundSignaturePattern(s, context));
                } else {
                    SignaturePattern sp = SignaturePattern.read(s, context);
                    ret = new DeclareAnnotation(AT_FIELD, sp);
                }
                if (isRemover) {
                    ret.setRemover(true);
                    break;
                }
                break;
            case 3:
                if (s.getMajorVersion() >= 7) {
                    ret = new DeclareAnnotation(AT_METHOD, AbstractSignaturePattern.readCompoundSignaturePattern(s, context));
                    break;
                } else {
                    SignaturePattern sp2 = SignaturePattern.read(s, context);
                    ret = new DeclareAnnotation(AT_METHOD, sp2);
                    break;
                }
            case 4:
                if (s.getMajorVersion() >= 7) {
                    ret = new DeclareAnnotation(AT_CONSTRUCTOR, AbstractSignaturePattern.readCompoundSignaturePattern(s, context));
                    break;
                } else {
                    SignaturePattern sp3 = SignaturePattern.read(s, context);
                    ret = new DeclareAnnotation(AT_CONSTRUCTOR, sp3);
                    break;
                }
        }
        ret.setAnnotationString(annotationString);
        ret.setAnnotationMethod(annotationMethod);
        ret.readLocation(context, s);
        return ret;
    }

    public boolean matches(ResolvedMember resolvedmember, World world) {
        if ((this.kind == AT_METHOD || this.kind == AT_CONSTRUCTOR) && resolvedmember != null && resolvedmember.getName().charAt(0) == '<' && this.kind == AT_METHOD) {
            return false;
        }
        return this.signaturePattern.matches(resolvedmember, world, false);
    }

    public boolean matches(ResolvedType type) {
        if (!this.typePattern.matchesStatically(type)) {
            return false;
        }
        if (type.getWorld().getLint().typeNotExposedToWeaver.isEnabled() && !type.isExposedToWeaver()) {
            type.getWorld().getLint().typeNotExposedToWeaver.signal(type.getName(), getSourceLocation());
            return true;
        }
        return true;
    }

    public void setAspect(ResolvedType typeX) {
        this.containingAspect = typeX;
    }

    public UnresolvedType getAspect() {
        return this.containingAspect;
    }

    public void copyAnnotationTo(ResolvedType onType) {
        ensureAnnotationDiscovered();
        if (!onType.hasAnnotation(this.annotation.getType())) {
            onType.addAnnotation(this.annotation);
        }
    }

    public AnnotationAJ getAnnotation() {
        ensureAnnotationDiscovered();
        return this.annotation;
    }

    private void ensureAnnotationDiscovered() {
        if (this.annotation != null) {
            return;
        }
        String annotationMethod = this.annotationMethods.get(0);
        Iterator<ResolvedMember> iter = this.containingAspect.getMethods(true, true);
        while (iter.hasNext()) {
            ResolvedMember member = iter.next();
            if (member.getName().equals(annotationMethod)) {
                AnnotationAJ[] annos = member.getAnnotations();
                if (annos == null) {
                    return;
                }
                int idx = 0;
                if (annos.length > 0 && annos[0].getType().getSignature().equals("Lorg/aspectj/internal/lang/annotation/ajcDeclareAnnotation;")) {
                    idx = 1;
                }
                this.annotation = annos[idx];
                return;
            }
        }
    }

    public TypePattern getTypePattern() {
        return this.typePattern;
    }

    public ISignaturePattern getSignaturePattern() {
        return this.signaturePattern;
    }

    public boolean isStarredAnnotationPattern() {
        if (this.typePattern != null) {
            return this.typePattern.isStarAnnotation();
        }
        return this.signaturePattern.isStarAnnotation();
    }

    public Kind getKind() {
        return this.kind;
    }

    public boolean isDeclareAtConstuctor() {
        return this.kind.equals(AT_CONSTRUCTOR);
    }

    public boolean isDeclareAtMethod() {
        return this.kind.equals(AT_METHOD);
    }

    public boolean isDeclareAtType() {
        return this.kind.equals(AT_TYPE);
    }

    public boolean isDeclareAtField() {
        return this.kind.equals(AT_FIELD);
    }

    public ResolvedType getAnnotationType() {
        if (this.annotationType == null) {
            String annotationMethod = this.annotationMethods.get(0);
            Iterator<ResolvedMember> iter = this.containingAspect.getMethods(true, true);
            while (true) {
                if (!iter.hasNext()) {
                    break;
                }
                ResolvedMember member = iter.next();
                if (member.getName().equals(annotationMethod)) {
                    ResolvedType[] annoTypes = member.getAnnotationTypes();
                    if (annoTypes == null) {
                        return null;
                    }
                    int idx = 0;
                    if (annoTypes[0].getSignature().equals("Lorg/aspectj/internal/lang/annotation/ajcDeclareAnnotation;")) {
                        idx = 1;
                    }
                    this.annotationType = annoTypes[idx];
                }
            }
        }
        return this.annotationType;
    }

    public boolean isAnnotationAllowedOnField() {
        ensureAnnotationDiscovered();
        return this.annotation.allowedOnField();
    }

    public String getPatternAsString() {
        if (this.signaturePattern != null) {
            return this.signaturePattern.toString();
        }
        if (this.typePattern != null) {
            return this.typePattern.toString();
        }
        return "DONT KNOW";
    }

    public boolean couldEverMatch(ResolvedType type) {
        if (this.signaturePattern != null) {
            return this.signaturePattern.couldEverMatch(type);
        }
        return true;
    }

    @Override // org.aspectj.weaver.patterns.Declare
    public String getNameSuffix() {
        return getKind().toString();
    }

    /* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/patterns/DeclareAnnotation$Kind.class */
    public static class Kind {
        private final int id;
        private String s;

        private Kind(int n, String name) {
            this.id = n;
            this.s = name;
        }

        public int hashCode() {
            return 19 + (37 * this.id);
        }

        public boolean equals(Object obj) {
            if (!(obj instanceof Kind)) {
                return false;
            }
            Kind other = (Kind) obj;
            return other.id == this.id;
        }

        public String toString() {
            return "at_" + this.s;
        }
    }

    public void setRemover(boolean b) {
        this.isRemover = b;
    }

    public boolean isRemover() {
        return this.isRemover;
    }
}
