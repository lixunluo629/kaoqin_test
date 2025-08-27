package org.aspectj.weaver;

import java.util.Collections;
import java.util.List;
import org.aspectj.bridge.ISourceLocation;
import org.aspectj.bridge.context.CompilationAndWeavingContext;
import org.springframework.jdbc.datasource.init.ScriptUtils;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/MissingResolvedTypeWithKnownSignature.class */
public class MissingResolvedTypeWithKnownSignature extends ResolvedType {
    private static ResolvedMember[] NO_MEMBERS = new ResolvedMember[0];
    private static ResolvedType[] NO_TYPES = new ResolvedType[0];
    private boolean issuedCantFindTypeError;
    private boolean issuedJoinPointWarning;
    private boolean issuedMissingInterfaceWarning;

    public MissingResolvedTypeWithKnownSignature(String signature, World world) {
        super(signature, world);
        this.issuedCantFindTypeError = false;
        this.issuedJoinPointWarning = false;
        this.issuedMissingInterfaceWarning = false;
    }

    @Override // org.aspectj.weaver.ResolvedType
    public boolean isMissing() {
        return true;
    }

    public MissingResolvedTypeWithKnownSignature(String signature, String signatureErasure, World world) {
        super(signature, signatureErasure, world);
        this.issuedCantFindTypeError = false;
        this.issuedJoinPointWarning = false;
        this.issuedMissingInterfaceWarning = false;
    }

    @Override // org.aspectj.weaver.ResolvedType
    public ResolvedMember[] getDeclaredFields() {
        raiseCantFindType(WeaverMessages.CANT_FIND_TYPE_FIELDS);
        return NO_MEMBERS;
    }

    @Override // org.aspectj.weaver.ResolvedType
    public ResolvedMember[] getDeclaredMethods() {
        raiseCantFindType(WeaverMessages.CANT_FIND_TYPE_METHODS);
        return NO_MEMBERS;
    }

    @Override // org.aspectj.weaver.ResolvedType
    public AnnotationAJ[] getAnnotations() {
        raiseCantFindType(WeaverMessages.CANT_FIND_TYPE_ANNOTATION);
        return AnnotationAJ.EMPTY_ARRAY;
    }

    @Override // org.aspectj.weaver.ResolvedType
    public ResolvedType[] getDeclaredInterfaces() {
        raiseCantFindType(WeaverMessages.CANT_FIND_TYPE_INTERFACES);
        return NO_TYPES;
    }

    @Override // org.aspectj.weaver.ResolvedType
    public ResolvedMember[] getDeclaredPointcuts() {
        raiseCantFindType(WeaverMessages.CANT_FIND_TYPE_POINTCUTS);
        return NO_MEMBERS;
    }

    @Override // org.aspectj.weaver.ResolvedType
    public ResolvedType getSuperclass() {
        raiseCantFindType(WeaverMessages.CANT_FIND_TYPE_SUPERCLASS);
        return ResolvedType.MISSING;
    }

    @Override // org.aspectj.weaver.ResolvedType
    public int getModifiers() {
        raiseCantFindType(WeaverMessages.CANT_FIND_TYPE_MODIFIERS);
        return 0;
    }

    @Override // org.aspectj.weaver.ResolvedType
    public ISourceContext getSourceContext() {
        return new ISourceContext() { // from class: org.aspectj.weaver.MissingResolvedTypeWithKnownSignature.1
            @Override // org.aspectj.weaver.ISourceContext
            public ISourceLocation makeSourceLocation(IHasPosition position) {
                return null;
            }

            @Override // org.aspectj.weaver.ISourceContext
            public ISourceLocation makeSourceLocation(int line, int offset) {
                return null;
            }

            @Override // org.aspectj.weaver.ISourceContext
            public int getOffset() {
                return 0;
            }

            @Override // org.aspectj.weaver.ISourceContext
            public void tidy() {
            }
        };
    }

    @Override // org.aspectj.weaver.ResolvedType
    public boolean isAssignableFrom(ResolvedType other) {
        raiseCantFindType(WeaverMessages.CANT_FIND_TYPE_ASSIGNABLE, other.getName());
        return false;
    }

    @Override // org.aspectj.weaver.ResolvedType
    public boolean isAssignableFrom(ResolvedType other, boolean allowMissing) {
        if (allowMissing) {
            return false;
        }
        return isAssignableFrom(other);
    }

    @Override // org.aspectj.weaver.ResolvedType
    public boolean isCoerceableFrom(ResolvedType other) {
        raiseCantFindType(WeaverMessages.CANT_FIND_TYPE_COERCEABLE, other.getName());
        return false;
    }

    @Override // org.aspectj.weaver.AnnotatedElement
    public boolean hasAnnotation(UnresolvedType ofType) {
        raiseCantFindType(WeaverMessages.CANT_FIND_TYPE_ANNOTATION);
        return false;
    }

    @Override // org.aspectj.weaver.ResolvedType
    public List getInterTypeMungers() {
        return Collections.EMPTY_LIST;
    }

    @Override // org.aspectj.weaver.ResolvedType
    public List getInterTypeMungersIncludingSupers() {
        return Collections.EMPTY_LIST;
    }

    @Override // org.aspectj.weaver.ResolvedType
    public List getInterTypeParentMungers() {
        return Collections.EMPTY_LIST;
    }

    @Override // org.aspectj.weaver.ResolvedType
    public List getInterTypeParentMungersIncludingSupers() {
        return Collections.EMPTY_LIST;
    }

    @Override // org.aspectj.weaver.ResolvedType
    protected void collectInterTypeMungers(List collector) {
    }

    public void raiseWarningOnJoinPointSignature(String signature) {
        if (this.issuedJoinPointWarning) {
            return;
        }
        String message = WeaverMessages.format(WeaverMessages.CANT_FIND_TYPE_JOINPOINT, getName(), signature);
        this.world.getLint().cantFindTypeAffectingJoinPointMatch.signal(message + ScriptUtils.FALLBACK_STATEMENT_SEPARATOR + CompilationAndWeavingContext.getCurrentContext(), null);
        this.issuedJoinPointWarning = true;
    }

    public void raiseWarningOnMissingInterfaceWhilstFindingMethods() {
        if (this.issuedMissingInterfaceWarning) {
            return;
        }
        String message = WeaverMessages.format(WeaverMessages.CANT_FIND_TYPE_INTERFACE_METHODS, getName(), this.signature);
        this.world.getLint().cantFindTypeAffectingJoinPointMatch.signal(message + ScriptUtils.FALLBACK_STATEMENT_SEPARATOR + CompilationAndWeavingContext.getCurrentContext(), null);
        this.issuedMissingInterfaceWarning = true;
    }

    private void raiseCantFindType(String key) {
        if (!this.world.getLint().cantFindType.isEnabled() || this.issuedCantFindTypeError) {
            return;
        }
        String message = WeaverMessages.format(key, getName());
        this.world.getLint().cantFindType.signal(message + ScriptUtils.FALLBACK_STATEMENT_SEPARATOR + CompilationAndWeavingContext.getCurrentContext(), null);
        this.issuedCantFindTypeError = true;
    }

    private void raiseCantFindType(String key, String insert) {
        if (this.issuedCantFindTypeError) {
            return;
        }
        String message = WeaverMessages.format(key, getName(), insert);
        this.world.getLint().cantFindType.signal(message + ScriptUtils.FALLBACK_STATEMENT_SEPARATOR + CompilationAndWeavingContext.getCurrentContext(), null);
        this.issuedCantFindTypeError = true;
    }
}
