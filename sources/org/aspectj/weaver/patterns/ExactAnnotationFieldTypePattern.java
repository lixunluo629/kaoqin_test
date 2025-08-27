package org.aspectj.weaver.patterns;

import java.io.IOException;
import java.util.Map;
import org.aspectj.bridge.AbortException;
import org.aspectj.bridge.IMessage;
import org.aspectj.util.FuzzyBoolean;
import org.aspectj.weaver.AnnotatedElement;
import org.aspectj.weaver.BCException;
import org.aspectj.weaver.CompressingDataOutputStream;
import org.aspectj.weaver.ISourceContext;
import org.aspectj.weaver.ReferenceType;
import org.aspectj.weaver.ResolvedMember;
import org.aspectj.weaver.ResolvedType;
import org.aspectj.weaver.UnresolvedType;
import org.aspectj.weaver.VersionedDataInputStream;
import org.aspectj.weaver.World;
import org.springframework.beans.factory.support.PropertiesBeanDefinitionReader;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/patterns/ExactAnnotationFieldTypePattern.class */
public class ExactAnnotationFieldTypePattern extends ExactAnnotationTypePattern {
    UnresolvedType annotationType;
    private ResolvedMember field;

    public ExactAnnotationFieldTypePattern(ExactAnnotationTypePattern p, String formalName) {
        super(formalName);
        this.annotationType = p.annotationType;
        copyLocationFrom(p);
    }

    public ExactAnnotationFieldTypePattern(UnresolvedType annotationType, String formalName) {
        super(formalName);
        this.annotationType = annotationType;
    }

    @Override // org.aspectj.weaver.patterns.ExactAnnotationTypePattern, org.aspectj.weaver.patterns.AnnotationTypePattern
    public AnnotationTypePattern resolveBindings(IScope scope, Bindings bindings, boolean allowBinding) throws AbortException {
        UnresolvedType type;
        int lastDot;
        if (this.resolved) {
            return this;
        }
        this.resolved = true;
        FormalBinding formalBinding = scope.lookupFormal(this.formalName);
        if (formalBinding == null) {
            scope.message(IMessage.ERROR, this, "When using @annotation(<annotationType>(<annotationField>)), <annotationField> must be bound");
            return this;
        }
        this.annotationType = scope.getWorld().resolve(this.annotationType, true);
        if (ResolvedType.isMissing(this.annotationType)) {
            String cleanname = this.annotationType.getName();
            while (true) {
                type = scope.lookupType(cleanname, this);
                if (!ResolvedType.isMissing(type) || (lastDot = cleanname.lastIndexOf(46)) == -1) {
                    break;
                }
                cleanname = cleanname.substring(0, lastDot) + PropertiesBeanDefinitionReader.CONSTRUCTOR_ARG_PREFIX + cleanname.substring(lastDot + 1);
            }
            this.annotationType = scope.getWorld().resolve(type, true);
            if (ResolvedType.isMissing(this.annotationType)) {
                return this;
            }
        }
        verifyIsAnnotationType((ResolvedType) this.annotationType, scope);
        ResolvedType formalBindingType = formalBinding.getType().resolve(scope.getWorld());
        String bindingTypeSignature = formalBindingType.getSignature();
        if (!formalBindingType.isEnum() && !bindingTypeSignature.equals("Ljava/lang/String;") && !bindingTypeSignature.equals("I")) {
            scope.message(IMessage.ERROR, this, "The field within the annotation must be an enum, string or int. '" + formalBinding.getType() + "' is not (compiler limitation)");
        }
        this.bindingPattern = true;
        ReferenceType theAnnotationType = (ReferenceType) this.annotationType;
        ResolvedMember[] annotationFields = theAnnotationType.getDeclaredMethods();
        this.field = null;
        boolean looksAmbiguous = false;
        for (ResolvedMember resolvedMember : annotationFields) {
            if (resolvedMember.getReturnType().equals(formalBinding.getType())) {
                if (this.field != null) {
                    boolean haveProblem = true;
                    if (this.field.getName().equals(this.formalName)) {
                        haveProblem = false;
                    } else if (resolvedMember.getName().equals(this.formalName)) {
                        this.field = resolvedMember;
                        haveProblem = false;
                    }
                    if (haveProblem) {
                        looksAmbiguous = true;
                    }
                } else {
                    this.field = resolvedMember;
                }
            }
        }
        if (looksAmbiguous && (this.field == null || !this.field.getName().equals(this.formalName))) {
            scope.message(IMessage.ERROR, this, "The field type '" + formalBinding.getType() + "' is ambiguous for annotation type '" + theAnnotationType.getName() + "'");
        }
        if (this.field == null) {
            scope.message(IMessage.ERROR, this, "No field of type '" + formalBinding.getType() + "' exists on annotation type '" + theAnnotationType.getName() + "'");
        }
        BindingAnnotationFieldTypePattern binding = new BindingAnnotationFieldTypePattern(formalBinding.getType(), formalBinding.getIndex(), theAnnotationType);
        binding.copyLocationFrom(this);
        binding.formalName = this.formalName;
        bindings.register(binding, scope);
        binding.resolveBinding(scope.getWorld());
        return binding;
    }

    @Override // org.aspectj.weaver.patterns.ExactAnnotationTypePattern, org.aspectj.weaver.patterns.PatternNode
    public void write(CompressingDataOutputStream s) throws IOException {
        s.writeByte(9);
        s.writeUTF(this.formalName);
        this.annotationType.write(s);
        writeLocation(s);
    }

    public static AnnotationTypePattern read(VersionedDataInputStream s, ISourceContext context) throws IOException {
        String formalName = s.readUTF();
        UnresolvedType annotationType = UnresolvedType.read(s);
        ExactAnnotationFieldTypePattern ret = new ExactAnnotationFieldTypePattern(annotationType, formalName);
        ret.readLocation(context, s);
        return ret;
    }

    @Override // org.aspectj.weaver.patterns.ExactAnnotationTypePattern, org.aspectj.weaver.patterns.PatternNode
    public Object accept(PatternNodeVisitor visitor, Object data) {
        return visitor.visit((ExactAnnotationTypePattern) this, data);
    }

    @Override // org.aspectj.weaver.patterns.ExactAnnotationTypePattern
    public boolean equals(Object obj) {
        if (!(obj instanceof ExactAnnotationFieldTypePattern)) {
            return false;
        }
        ExactAnnotationFieldTypePattern other = (ExactAnnotationFieldTypePattern) obj;
        return other.annotationType.equals(this.annotationType) && other.field.equals(this.field) && other.formalName.equals(this.formalName);
    }

    @Override // org.aspectj.weaver.patterns.ExactAnnotationTypePattern
    public int hashCode() {
        int hashcode = this.annotationType.hashCode();
        return (((hashcode * 37) + this.field.hashCode()) * 37) + this.formalName.hashCode();
    }

    @Override // org.aspectj.weaver.patterns.ExactAnnotationTypePattern, org.aspectj.weaver.patterns.AnnotationTypePattern
    public FuzzyBoolean fastMatches(AnnotatedElement annotated) {
        throw new BCException("unimplemented");
    }

    @Override // org.aspectj.weaver.patterns.ExactAnnotationTypePattern
    public UnresolvedType getAnnotationType() {
        throw new BCException("unimplemented");
    }

    @Override // org.aspectj.weaver.patterns.ExactAnnotationTypePattern
    public Map getAnnotationValues() {
        throw new BCException("unimplemented");
    }

    @Override // org.aspectj.weaver.patterns.ExactAnnotationTypePattern
    public ResolvedType getResolvedAnnotationType() {
        throw new BCException("unimplemented");
    }

    @Override // org.aspectj.weaver.patterns.ExactAnnotationTypePattern, org.aspectj.weaver.patterns.AnnotationTypePattern
    public FuzzyBoolean matches(AnnotatedElement annotated, ResolvedType[] parameterAnnotations) {
        throw new BCException("unimplemented");
    }

    @Override // org.aspectj.weaver.patterns.ExactAnnotationTypePattern, org.aspectj.weaver.patterns.AnnotationTypePattern
    public FuzzyBoolean matches(AnnotatedElement annotated) {
        throw new BCException("unimplemented");
    }

    @Override // org.aspectj.weaver.patterns.ExactAnnotationTypePattern
    public FuzzyBoolean matchesRuntimeType(AnnotatedElement annotated) {
        throw new BCException("unimplemented");
    }

    @Override // org.aspectj.weaver.patterns.ExactAnnotationTypePattern, org.aspectj.weaver.patterns.AnnotationTypePattern
    public AnnotationTypePattern parameterizeWith(Map typeVariableMap, World w) {
        throw new BCException("unimplemented");
    }

    @Override // org.aspectj.weaver.patterns.ExactAnnotationTypePattern, org.aspectj.weaver.patterns.AnnotationTypePattern
    public void resolve(World world) {
        throw new BCException("unimplemented");
    }

    @Override // org.aspectj.weaver.patterns.ExactAnnotationTypePattern
    public String toString() {
        if (!this.resolved && this.formalName != null) {
            return this.formalName;
        }
        StringBuffer ret = new StringBuffer();
        ret.append("@").append(this.annotationType.toString());
        ret.append("(").append(this.formalName).append(")");
        return ret.toString();
    }
}
