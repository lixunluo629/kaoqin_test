package org.bouncycastle.crypto.params;

/* JADX WARN: Classes with same name are omitted:
  bcprov-jdk15on-1.64.jar:org/bouncycastle/crypto/params/DHKeyParameters.class
 */
/* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/crypto/params/DHKeyParameters.class */
public class DHKeyParameters extends AsymmetricKeyParameter {
    private DHParameters params;

    protected DHKeyParameters(boolean z, DHParameters dHParameters) {
        super(z);
        this.params = dHParameters;
    }

    public DHParameters getParameters() {
        return this.params;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof DHKeyParameters)) {
            return false;
        }
        DHKeyParameters dHKeyParameters = (DHKeyParameters) obj;
        return this.params == null ? dHKeyParameters.getParameters() == null : this.params.equals(dHKeyParameters.getParameters());
    }

    public int hashCode() {
        int iHashCode = isPrivate() ? 0 : 1;
        if (this.params != null) {
            iHashCode ^= this.params.hashCode();
        }
        return iHashCode;
    }
}
