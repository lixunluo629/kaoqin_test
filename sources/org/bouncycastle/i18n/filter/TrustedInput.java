package org.bouncycastle.i18n.filter;

/* JADX WARN: Classes with same name are omitted:
  bcprov-jdk15on-1.64.jar:org/bouncycastle/i18n/filter/TrustedInput.class
 */
/* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/i18n/filter/TrustedInput.class */
public class TrustedInput {
    protected Object input;

    public TrustedInput(Object obj) {
        this.input = obj;
    }

    public Object getInput() {
        return this.input;
    }

    public String toString() {
        return this.input.toString();
    }
}
