package org.bouncycastle.cms;

import org.bouncycastle.asn1.ASN1Set;

/* loaded from: bcpkix-jdk15on-1.64.jar:org/bouncycastle/cms/AuthAttributesProvider.class */
interface AuthAttributesProvider {
    ASN1Set getAuthAttributes();

    boolean isAead();
}
