package org.bouncycastle.asn1.x509;

/* JADX WARN: Classes with same name are omitted:
  bcprov-jdk15on-1.64.jar:org/bouncycastle/asn1/x509/X509NameTokenizer.class
 */
/* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/asn1/x509/X509NameTokenizer.class */
public class X509NameTokenizer {
    private String value;
    private int index;
    private char seperator;
    private StringBuffer buf;

    public X509NameTokenizer(String str) {
        this(str, ',');
    }

    public X509NameTokenizer(String str, char c) {
        this.buf = new StringBuffer();
        this.value = str;
        this.index = -1;
        this.seperator = c;
    }

    public boolean hasMoreTokens() {
        return this.index != this.value.length();
    }

    public String nextToken() {
        if (this.index == this.value.length()) {
            return null;
        }
        int i = this.index + 1;
        boolean z = false;
        boolean z2 = false;
        this.buf.setLength(0);
        while (i != this.value.length()) {
            char cCharAt = this.value.charAt(i);
            if (cCharAt == '\"') {
                if (z2) {
                    this.buf.append(cCharAt);
                } else {
                    z = !z;
                }
                z2 = false;
            } else if (z2 || z) {
                if (cCharAt == '#' && this.buf.charAt(this.buf.length() - 1) == '=') {
                    this.buf.append('\\');
                } else if (cCharAt == '+' && this.seperator != '+') {
                    this.buf.append('\\');
                }
                this.buf.append(cCharAt);
                z2 = false;
            } else if (cCharAt == '\\') {
                z2 = true;
            } else {
                if (cCharAt == this.seperator) {
                    break;
                }
                this.buf.append(cCharAt);
            }
            i++;
        }
        this.index = i;
        return this.buf.toString().trim();
    }
}
