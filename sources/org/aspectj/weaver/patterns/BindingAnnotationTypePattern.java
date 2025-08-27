package org.aspectj.weaver.patterns;

import java.io.IOException;
import java.util.Map;
import org.aspectj.bridge.AbortException;
import org.aspectj.bridge.IMessage;
import org.aspectj.bridge.MessageUtil;
import org.aspectj.weaver.BCException;
import org.aspectj.weaver.CompressingDataOutputStream;
import org.aspectj.weaver.ISourceContext;
import org.aspectj.weaver.IntMap;
import org.aspectj.weaver.ResolvedType;
import org.aspectj.weaver.TypeVariableReference;
import org.aspectj.weaver.UnresolvedType;
import org.aspectj.weaver.VersionedDataInputStream;
import org.aspectj.weaver.WeaverMessages;
import org.aspectj.weaver.World;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/patterns/BindingAnnotationTypePattern.class */
public class BindingAnnotationTypePattern extends ExactAnnotationTypePattern implements BindingPattern {
    protected int formalIndex;
    private static final byte VERSION = 1;

    public BindingAnnotationTypePattern(UnresolvedType annotationType, int index) {
        super(annotationType, null);
        this.formalIndex = index;
    }

    public BindingAnnotationTypePattern(FormalBinding binding) {
        this(binding.getType(), binding.getIndex());
    }

    public void resolveBinding(World world) throws AbortException {
        if (this.resolved) {
            return;
        }
        this.resolved = true;
        this.annotationType = this.annotationType.resolve(world);
        ResolvedType resolvedAnnotationType = (ResolvedType) this.annotationType;
        if (!resolvedAnnotationType.isAnnotation()) {
            IMessage m = MessageUtil.error(WeaverMessages.format(WeaverMessages.REFERENCE_TO_NON_ANNOTATION_TYPE, this.annotationType.getName()), getSourceLocation());
            world.getMessageHandler().handleMessage(m);
            this.resolved = false;
        }
        if (this.annotationType.isTypeVariableReference()) {
            return;
        }
        verifyRuntimeRetention(world, resolvedAnnotationType);
    }

    private void verifyRuntimeRetention(World world, ResolvedType resolvedAnnotationType) throws AbortException {
        if (!resolvedAnnotationType.isAnnotationWithRuntimeRetention()) {
            IMessage m = MessageUtil.error(WeaverMessages.format(WeaverMessages.BINDING_NON_RUNTIME_RETENTION_ANNOTATION, this.annotationType.getName()), getSourceLocation());
            world.getMessageHandler().handleMessage(m);
            this.resolved = false;
        }
    }

    @Override // org.aspectj.weaver.patterns.ExactAnnotationTypePattern, org.aspectj.weaver.patterns.AnnotationTypePattern
    public AnnotationTypePattern parameterizeWith(Map typeVariableMap, World w) throws AbortException {
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
        BindingAnnotationTypePattern ret = new BindingAnnotationTypePattern(newAnnotationType, this.formalIndex);
        if (newAnnotationType instanceof ResolvedType) {
            ResolvedType rat = (ResolvedType) newAnnotationType;
            verifyRuntimeRetention(rat.getWorld(), rat);
        }
        ret.copyLocationFrom(this);
        return ret;
    }

    @Override // org.aspectj.weaver.patterns.BindingPattern
    public int getFormalIndex() {
        return this.formalIndex;
    }

    @Override // org.aspectj.weaver.patterns.ExactAnnotationTypePattern
    public boolean equals(Object obj) {
        if (!(obj instanceof BindingAnnotationTypePattern)) {
            return false;
        }
        BindingAnnotationTypePattern btp = (BindingAnnotationTypePattern) obj;
        return super.equals(btp) && btp.formalIndex == this.formalIndex;
    }

    @Override // org.aspectj.weaver.patterns.ExactAnnotationTypePattern
    public int hashCode() {
        return (super.hashCode() * 37) + this.formalIndex;
    }

    @Override // org.aspectj.weaver.patterns.AnnotationTypePattern
    public AnnotationTypePattern remapAdviceFormals(IntMap bindings) {
        if (!bindings.hasKey(this.formalIndex)) {
            return new ExactAnnotationTypePattern(this.annotationType, null);
        }
        int newFormalIndex = bindings.get(this.formalIndex);
        return new BindingAnnotationTypePattern(this.annotationType, newFormalIndex);
    }

    @Override // org.aspectj.weaver.patterns.ExactAnnotationTypePattern, org.aspectj.weaver.patterns.PatternNode
    public void write(CompressingDataOutputStream s) throws IOException {
        s.writeByte(2);
        s.writeByte(1);
        this.annotationType.write(s);
        s.writeShort((short) this.formalIndex);
        writeLocation(s);
    }

    public static AnnotationTypePattern read(VersionedDataInputStream s, ISourceContext context) throws IOException {
        byte version = s.readByte();
        if (version > 1) {
            throw new BCException("BindingAnnotationTypePattern was written by a more recent version of AspectJ");
        }
        AnnotationTypePattern ret = new BindingAnnotationTypePattern(UnresolvedType.read(s), s.readShort());
        ret.readLocation(context, s);
        return ret;
    }
}
