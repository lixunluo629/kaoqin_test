package org.bouncycastle.cert.path.validations;

import java.math.BigInteger;
import org.bouncycastle.asn1.x509.BasicConstraints;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.path.CertPathValidation;
import org.bouncycastle.cert.path.CertPathValidationContext;
import org.bouncycastle.cert.path.CertPathValidationException;
import org.bouncycastle.util.Memoable;

/* loaded from: bcpkix-jdk15on-1.64.jar:org/bouncycastle/cert/path/validations/BasicConstraintsValidation.class */
public class BasicConstraintsValidation implements CertPathValidation {
    private boolean isMandatory;
    private BasicConstraints bc;
    private int pathLengthRemaining;
    private BigInteger maxPathLength;

    public BasicConstraintsValidation() {
        this(true);
    }

    public BasicConstraintsValidation(boolean z) {
        this.isMandatory = z;
    }

    @Override // org.bouncycastle.cert.path.CertPathValidation
    public void validate(CertPathValidationContext certPathValidationContext, X509CertificateHolder x509CertificateHolder) throws CertPathValidationException {
        BigInteger pathLenConstraint;
        int iIntValue;
        if (this.maxPathLength != null && this.pathLengthRemaining < 0) {
            throw new CertPathValidationException("BasicConstraints path length exceeded");
        }
        certPathValidationContext.addHandledExtension(Extension.basicConstraints);
        BasicConstraints basicConstraintsFromExtensions = BasicConstraints.fromExtensions(x509CertificateHolder.getExtensions());
        if (basicConstraintsFromExtensions != null) {
            if (this.bc == null) {
                this.bc = basicConstraintsFromExtensions;
                if (basicConstraintsFromExtensions.isCA()) {
                    this.maxPathLength = basicConstraintsFromExtensions.getPathLenConstraint();
                    if (this.maxPathLength != null) {
                        this.pathLengthRemaining = this.maxPathLength.intValue();
                    }
                }
            } else if (basicConstraintsFromExtensions.isCA() && (pathLenConstraint = basicConstraintsFromExtensions.getPathLenConstraint()) != null && (iIntValue = pathLenConstraint.intValue()) < this.pathLengthRemaining) {
                this.pathLengthRemaining = iIntValue;
                this.bc = basicConstraintsFromExtensions;
            }
        } else if (this.bc != null) {
            this.pathLengthRemaining--;
        }
        if (this.isMandatory && this.bc == null) {
            throw new CertPathValidationException("BasicConstraints not present in path");
        }
    }

    @Override // org.bouncycastle.util.Memoable
    public Memoable copy() {
        BasicConstraintsValidation basicConstraintsValidation = new BasicConstraintsValidation(this.isMandatory);
        basicConstraintsValidation.bc = this.bc;
        basicConstraintsValidation.pathLengthRemaining = this.pathLengthRemaining;
        return basicConstraintsValidation;
    }

    @Override // org.bouncycastle.util.Memoable
    public void reset(Memoable memoable) {
        BasicConstraintsValidation basicConstraintsValidation = (BasicConstraintsValidation) memoable;
        this.isMandatory = basicConstraintsValidation.isMandatory;
        this.bc = basicConstraintsValidation.bc;
        this.pathLengthRemaining = basicConstraintsValidation.pathLengthRemaining;
    }
}
