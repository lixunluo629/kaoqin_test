package org.aspectj.weaver;

import java.util.ArrayList;
import java.util.List;
import org.aspectj.bridge.ISourceLocation;
import org.aspectj.util.GenericSignature;
import org.aspectj.util.GenericSignatureParser;
import org.aspectj.weaver.AjAttribute;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/AbstractReferenceTypeDelegate.class */
public abstract class AbstractReferenceTypeDelegate implements ReferenceTypeDelegate {
    private String sourcefilename = UNKNOWN_SOURCE_FILE;
    private ISourceContext sourceContext = SourceContextImpl.UNKNOWN_SOURCE_CONTEXT;
    protected boolean exposedToWeaver;
    protected ReferenceType resolvedTypeX;
    protected GenericSignature.ClassSignature cachedGenericClassTypeSignature;
    public static final String UNKNOWN_SOURCE_FILE = "<Unknown>";

    public AbstractReferenceTypeDelegate(ReferenceType resolvedTypeX, boolean exposedToWeaver) {
        this.resolvedTypeX = resolvedTypeX;
        this.exposedToWeaver = exposedToWeaver;
    }

    @Override // org.aspectj.weaver.ReferenceTypeDelegate
    public final boolean isClass() {
        return (isAspect() || isInterface()) ? false : true;
    }

    @Override // org.aspectj.weaver.ReferenceTypeDelegate
    public boolean isCacheable() {
        return false;
    }

    @Override // org.aspectj.weaver.ReferenceTypeDelegate
    public boolean doesNotExposeShadowMungers() {
        return false;
    }

    @Override // org.aspectj.weaver.ReferenceTypeDelegate
    public boolean isExposedToWeaver() {
        return this.exposedToWeaver;
    }

    @Override // org.aspectj.weaver.ReferenceTypeDelegate
    public ReferenceType getResolvedTypeX() {
        return this.resolvedTypeX;
    }

    @Override // org.aspectj.weaver.ReferenceTypeDelegate
    public final String getSourcefilename() {
        return this.sourcefilename;
    }

    public final void setSourcefilename(String sourceFileName) {
        this.sourcefilename = sourceFileName;
        if (sourceFileName != null && sourceFileName.equals(UNKNOWN_SOURCE_FILE)) {
            this.sourcefilename = "Type '" + getResolvedTypeX().getName() + "' (no debug info available)";
        } else {
            String pname = getResolvedTypeX().getPackageName();
            if (pname != null) {
                this.sourcefilename = pname.replace('.', '/') + '/' + sourceFileName;
            }
        }
        if (this.sourcefilename != null && (this.sourceContext instanceof SourceContextImpl)) {
            ((SourceContextImpl) this.sourceContext).setSourceFileName(this.sourcefilename);
        }
    }

    public ISourceLocation getSourceLocation() {
        return getSourceContext().makeSourceLocation(0, 0);
    }

    @Override // org.aspectj.weaver.ReferenceTypeDelegate
    public ISourceContext getSourceContext() {
        return this.sourceContext;
    }

    public void setSourceContext(ISourceContext isc) {
        this.sourceContext = isc;
    }

    public GenericSignature.ClassSignature getGenericClassTypeSignature() {
        String sig;
        if (this.cachedGenericClassTypeSignature == null && (sig = getDeclaredGenericSignature()) != null) {
            GenericSignatureParser parser = new GenericSignatureParser();
            this.cachedGenericClassTypeSignature = parser.parseAsClassSignature(sig);
        }
        return this.cachedGenericClassTypeSignature;
    }

    protected GenericSignature.FormalTypeParameter[] getFormalTypeParametersFromOuterClass() {
        List<GenericSignature.FormalTypeParameter> typeParameters = new ArrayList<>();
        ResolvedType outerClassType = getOuterClass();
        if (!(outerClassType instanceof ReferenceType)) {
            if (outerClassType == null) {
                return GenericSignature.FormalTypeParameter.NONE;
            }
            throw new BCException("Whilst processing type '" + this.resolvedTypeX.getSignature() + "' - cannot cast the outer type to a reference type.  Signature=" + outerClassType.getSignature() + " toString()=" + outerClassType.toString() + " class=" + outerClassType.getClassName());
        }
        ReferenceType outer = (ReferenceType) outerClassType;
        ReferenceTypeDelegate outerDelegate = outer.getDelegate();
        AbstractReferenceTypeDelegate outerObjectType = (AbstractReferenceTypeDelegate) outerDelegate;
        if (outerObjectType.isNested()) {
            GenericSignature.FormalTypeParameter[] parentParams = outerObjectType.getFormalTypeParametersFromOuterClass();
            for (GenericSignature.FormalTypeParameter formalTypeParameter : parentParams) {
                typeParameters.add(formalTypeParameter);
            }
        }
        GenericSignature.ClassSignature outerSig = outerObjectType.getGenericClassTypeSignature();
        if (outerSig != null) {
            for (int i = 0; i < outerSig.formalTypeParameters.length; i++) {
                typeParameters.add(outerSig.formalTypeParameters[i]);
            }
        }
        GenericSignature.FormalTypeParameter[] ret = new GenericSignature.FormalTypeParameter[typeParameters.size()];
        typeParameters.toArray(ret);
        return ret;
    }

    @Override // org.aspectj.weaver.ReferenceTypeDelegate
    public boolean copySourceContext() {
        return true;
    }

    @Override // org.aspectj.weaver.ReferenceTypeDelegate
    public int getCompilerVersion() {
        return AjAttribute.WeaverVersionInfo.getCurrentWeaverMajorVersion();
    }

    @Override // org.aspectj.weaver.ReferenceTypeDelegate
    public void ensureConsistent() {
    }

    @Override // org.aspectj.weaver.ReferenceTypeDelegate
    public boolean isWeavable() {
        return false;
    }

    @Override // org.aspectj.weaver.ReferenceTypeDelegate
    public boolean hasBeenWoven() {
        return false;
    }
}
