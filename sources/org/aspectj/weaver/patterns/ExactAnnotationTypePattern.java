package org.aspectj.weaver.patterns;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.aspectj.bridge.AbortException;
import org.aspectj.bridge.IMessage;
import org.aspectj.bridge.MessageUtil;
import org.aspectj.util.FuzzyBoolean;
import org.aspectj.weaver.AnnotatedElement;
import org.aspectj.weaver.AnnotationAJ;
import org.aspectj.weaver.BCException;
import org.aspectj.weaver.CompressingDataOutputStream;
import org.aspectj.weaver.ISourceContext;
import org.aspectj.weaver.ReferenceType;
import org.aspectj.weaver.ResolvedMember;
import org.aspectj.weaver.ResolvedType;
import org.aspectj.weaver.TypeVariableReference;
import org.aspectj.weaver.UnresolvedType;
import org.aspectj.weaver.VersionedDataInputStream;
import org.aspectj.weaver.WeaverMessages;
import org.aspectj.weaver.World;
import org.springframework.beans.factory.support.PropertiesBeanDefinitionReader;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/patterns/ExactAnnotationTypePattern.class */
public class ExactAnnotationTypePattern extends AnnotationTypePattern {
    protected UnresolvedType annotationType;
    protected String formalName;
    protected boolean resolved;
    protected boolean bindingPattern;
    private Map<String, String> annotationValues;
    private static byte VERSION = 1;

    public ExactAnnotationTypePattern(UnresolvedType annotationType, Map<String, String> annotationValues) {
        this.resolved = false;
        this.bindingPattern = false;
        this.annotationType = annotationType;
        this.annotationValues = annotationValues;
        this.resolved = annotationType instanceof ResolvedType;
    }

    private ExactAnnotationTypePattern(UnresolvedType annotationType) {
        this.resolved = false;
        this.bindingPattern = false;
        this.annotationType = annotationType;
        this.resolved = annotationType instanceof ResolvedType;
    }

    protected ExactAnnotationTypePattern(String formalName) {
        this.resolved = false;
        this.bindingPattern = false;
        this.formalName = formalName;
        this.resolved = false;
        this.bindingPattern = true;
    }

    public ResolvedType getResolvedAnnotationType() {
        if (!this.resolved) {
            throw new IllegalStateException("I need to be resolved first!");
        }
        return (ResolvedType) this.annotationType;
    }

    public UnresolvedType getAnnotationType() {
        return this.annotationType;
    }

    public Map<String, String> getAnnotationValues() {
        return this.annotationValues;
    }

    @Override // org.aspectj.weaver.patterns.AnnotationTypePattern
    public FuzzyBoolean fastMatches(AnnotatedElement annotated) {
        if (annotated.hasAnnotation(this.annotationType) && this.annotationValues == null) {
            return FuzzyBoolean.YES;
        }
        return FuzzyBoolean.MAYBE;
    }

    @Override // org.aspectj.weaver.patterns.AnnotationTypePattern
    public FuzzyBoolean matches(AnnotatedElement annotated) {
        return matches(annotated, null);
    }

    @Override // org.aspectj.weaver.patterns.AnnotationTypePattern
    public FuzzyBoolean matches(AnnotatedElement annotated, ResolvedType[] parameterAnnotations) throws AbortException {
        String s;
        String s2;
        if (!isForParameterAnnotationMatch()) {
            boolean checkSupers = false;
            if (getResolvedAnnotationType().isInheritedAnnotation() && (annotated instanceof ResolvedType)) {
                checkSupers = true;
            }
            if (annotated.hasAnnotation(this.annotationType)) {
                if (this.annotationType instanceof ReferenceType) {
                    ReferenceType rt = (ReferenceType) this.annotationType;
                    if (rt.getRetentionPolicy() != null && rt.getRetentionPolicy().equals("SOURCE")) {
                        rt.getWorld().getMessageHandler().handleMessage(MessageUtil.warn(WeaverMessages.format(WeaverMessages.NO_MATCH_BECAUSE_SOURCE_RETENTION, this.annotationType, annotated), getSourceLocation()));
                        return FuzzyBoolean.NO;
                    }
                }
                if (this.annotationValues != null) {
                    AnnotationAJ theAnnotation = annotated.getAnnotationOfType(this.annotationType);
                    Set<String> keys = this.annotationValues.keySet();
                    Iterator<String> it = keys.iterator();
                    while (it.hasNext()) {
                        String k = it.next();
                        boolean notEqual = false;
                        String v = this.annotationValues.get(k);
                        if (k.endsWith("!")) {
                            notEqual = true;
                            k = k.substring(0, k.length() - 1);
                        }
                        if (theAnnotation.hasNamedValue(k)) {
                            if (notEqual) {
                                if (theAnnotation.hasNameValuePair(k, v)) {
                                    return FuzzyBoolean.NO;
                                }
                            } else if (!theAnnotation.hasNameValuePair(k, v)) {
                                return FuzzyBoolean.NO;
                            }
                        } else {
                            ResolvedMember[] ms = ((ResolvedType) this.annotationType).getDeclaredMethods();
                            boolean foundMatch = false;
                            for (int i = 0; i < ms.length && !foundMatch; i++) {
                                if (ms[i].isAbstract() && ms[i].getParameterTypes().length == 0 && ms[i].getName().equals(k) && (s2 = ms[i].getAnnotationDefaultValue()) != null && s2.equals(v)) {
                                    foundMatch = true;
                                }
                            }
                            if (notEqual) {
                                if (foundMatch) {
                                    return FuzzyBoolean.NO;
                                }
                            } else if (!foundMatch) {
                                return FuzzyBoolean.NO;
                            }
                        }
                    }
                }
                return FuzzyBoolean.YES;
            }
            if (checkSupers) {
                ResolvedType superclass = ((ResolvedType) annotated).getSuperclass();
                while (true) {
                    ResolvedType toMatchAgainst = superclass;
                    if (toMatchAgainst == null) {
                        break;
                    }
                    if (toMatchAgainst.hasAnnotation(this.annotationType)) {
                        if (this.annotationValues != null) {
                            AnnotationAJ theAnnotation2 = toMatchAgainst.getAnnotationOfType(this.annotationType);
                            Set<String> keys2 = this.annotationValues.keySet();
                            for (String k2 : keys2) {
                                String v2 = this.annotationValues.get(k2);
                                if (theAnnotation2.hasNamedValue(k2)) {
                                    if (!theAnnotation2.hasNameValuePair(k2, v2)) {
                                        return FuzzyBoolean.NO;
                                    }
                                } else {
                                    ResolvedMember[] ms2 = ((ResolvedType) this.annotationType).getDeclaredMethods();
                                    boolean foundMatch2 = false;
                                    for (int i2 = 0; i2 < ms2.length && !foundMatch2; i2++) {
                                        if (ms2[i2].isAbstract() && ms2[i2].getParameterTypes().length == 0 && ms2[i2].getName().equals(k2) && (s = ms2[i2].getAnnotationDefaultValue()) != null && s.equals(v2)) {
                                            foundMatch2 = true;
                                        }
                                    }
                                    if (!foundMatch2) {
                                        return FuzzyBoolean.NO;
                                    }
                                }
                            }
                        }
                        return FuzzyBoolean.YES;
                    }
                    superclass = toMatchAgainst.getSuperclass();
                }
            }
        } else {
            if (parameterAnnotations == null) {
                return FuzzyBoolean.NO;
            }
            for (int i3 = 0; i3 < parameterAnnotations.length; i3++) {
                if (this.annotationType.equals(parameterAnnotations[i3])) {
                    if (this.annotationValues != null) {
                        parameterAnnotations[i3].getWorld().getMessageHandler().handleMessage(MessageUtil.error("Compiler limitation: annotation value matching for parameter annotations not yet supported"));
                        return FuzzyBoolean.NO;
                    }
                    return FuzzyBoolean.YES;
                }
            }
        }
        return FuzzyBoolean.NO;
    }

    public FuzzyBoolean matchesRuntimeType(AnnotatedElement annotated) {
        if (getResolvedAnnotationType().isInheritedAnnotation() && matches(annotated).alwaysTrue()) {
            return FuzzyBoolean.YES;
        }
        return FuzzyBoolean.MAYBE;
    }

    @Override // org.aspectj.weaver.patterns.AnnotationTypePattern
    public void resolve(World world) {
        if (!this.resolved) {
            this.annotationType = this.annotationType.resolve(world);
            this.resolved = true;
        }
    }

    @Override // org.aspectj.weaver.patterns.AnnotationTypePattern
    public AnnotationTypePattern resolveBindings(IScope scope, Bindings bindings, boolean allowBinding) throws AbortException {
        UnresolvedType type;
        int lastDot;
        FormalBinding formalBinding;
        if (this.resolved) {
            return this;
        }
        this.resolved = true;
        String simpleName = maybeGetSimpleName();
        if (simpleName != null && (formalBinding = scope.lookupFormal(simpleName)) != null) {
            if (bindings == null) {
                scope.message(IMessage.ERROR, this, "negation doesn't allow binding");
                return this;
            }
            if (!allowBinding) {
                scope.message(IMessage.ERROR, this, "name binding only allowed in @pcds, args, this, and target");
                return this;
            }
            this.formalName = simpleName;
            this.bindingPattern = true;
            verifyIsAnnotationType(formalBinding.getType().resolve(scope.getWorld()), scope);
            BindingAnnotationTypePattern binding = new BindingAnnotationTypePattern(formalBinding);
            binding.copyLocationFrom(this);
            bindings.register(binding, scope);
            binding.resolveBinding(scope.getWorld());
            if (isForParameterAnnotationMatch()) {
                binding.setForParameterAnnotationMatch();
            }
            return binding;
        }
        String cleanname = this.annotationType.getName();
        this.annotationType = scope.getWorld().resolve(this.annotationType, true);
        if (ResolvedType.isMissing(this.annotationType)) {
            while (true) {
                type = scope.lookupType(cleanname, this);
                if (!ResolvedType.isMissing(type) || (lastDot = cleanname.lastIndexOf(46)) == -1) {
                    break;
                }
                cleanname = cleanname.substring(0, lastDot) + PropertiesBeanDefinitionReader.CONSTRUCTOR_ARG_PREFIX + cleanname.substring(lastDot + 1);
            }
            this.annotationType = scope.getWorld().resolve(type, true);
        }
        verifyIsAnnotationType((ResolvedType) this.annotationType, scope);
        return this;
    }

    @Override // org.aspectj.weaver.patterns.AnnotationTypePattern
    public AnnotationTypePattern parameterizeWith(Map typeVariableMap, World w) {
        UnresolvedType newAnnotationType = this.annotationType;
        if (this.annotationType.isTypeVariableReference()) {
            TypeVariableReference t = (TypeVariableReference) this.annotationType;
            String key = t.getTypeVariable().getName();
            if (typeVariableMap.containsKey(key)) {
                newAnnotationType = (UnresolvedType) typeVariableMap.get(key);
            }
        } else if (this.annotationType.isParameterizedType()) {
            newAnnotationType = this.annotationType.parameterize(typeVariableMap);
        }
        ExactAnnotationTypePattern ret = new ExactAnnotationTypePattern(newAnnotationType, this.annotationValues);
        ret.formalName = this.formalName;
        ret.bindingPattern = this.bindingPattern;
        ret.copyLocationFrom(this);
        if (isForParameterAnnotationMatch()) {
            ret.setForParameterAnnotationMatch();
        }
        return ret;
    }

    protected String maybeGetSimpleName() {
        if (this.formalName != null) {
            return this.formalName;
        }
        String ret = this.annotationType.getName();
        if (ret.indexOf(46) == -1) {
            return ret;
        }
        return null;
    }

    protected void verifyIsAnnotationType(ResolvedType type, IScope scope) throws AbortException {
        if (!type.isAnnotation()) {
            IMessage m = MessageUtil.error(WeaverMessages.format(WeaverMessages.REFERENCE_TO_NON_ANNOTATION_TYPE, type.getName()), getSourceLocation());
            scope.getWorld().getMessageHandler().handleMessage(m);
            this.resolved = false;
        }
    }

    @Override // org.aspectj.weaver.patterns.PatternNode
    public void write(CompressingDataOutputStream s) throws IOException {
        s.writeByte(1);
        s.writeByte(VERSION);
        s.writeBoolean(this.bindingPattern);
        if (this.bindingPattern) {
            s.writeUTF(this.formalName);
        } else {
            this.annotationType.write(s);
        }
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
        ExactAnnotationTypePattern ret;
        int annotationValueCount;
        byte version = s.readByte();
        if (version > VERSION) {
            throw new BCException("ExactAnnotationTypePattern was written by a newer version of AspectJ");
        }
        boolean isBindingPattern = s.readBoolean();
        if (isBindingPattern) {
            ret = new ExactAnnotationTypePattern(s.readUTF());
        } else {
            ret = new ExactAnnotationTypePattern(UnresolvedType.read(s));
        }
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
        if (!(obj instanceof ExactAnnotationTypePattern)) {
            return false;
        }
        ExactAnnotationTypePattern other = (ExactAnnotationTypePattern) obj;
        return other.annotationType.equals(this.annotationType) && isForParameterAnnotationMatch() == other.isForParameterAnnotationMatch() && (this.annotationValues != null ? this.annotationValues.equals(other.annotationValues) : other.annotationValues == null);
    }

    public int hashCode() {
        return (((this.annotationType.hashCode() * 37) + (isForParameterAnnotationMatch() ? 0 : 1)) * 37) + (this.annotationValues == null ? 0 : this.annotationValues.hashCode());
    }

    public String toString() {
        if (!this.resolved && this.formalName != null) {
            return this.formalName;
        }
        String ret = "@" + this.annotationType.toString();
        if (this.formalName != null) {
            ret = ret + SymbolConstants.SPACE_SYMBOL + this.formalName;
        }
        return ret;
    }

    @Override // org.aspectj.weaver.patterns.PatternNode
    public Object accept(PatternNodeVisitor visitor, Object data) {
        return visitor.visit(this, data);
    }
}
