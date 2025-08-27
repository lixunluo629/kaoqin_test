package org.bouncycastle.cms;

import org.bouncycastle.asn1.cms.RecipientInfo;
import org.bouncycastle.operator.GenericKey;

/* loaded from: bcpkix-jdk15on-1.64.jar:org/bouncycastle/cms/RecipientInfoGenerator.class */
public interface RecipientInfoGenerator {
    RecipientInfo generate(GenericKey genericKey) throws CMSException;
}
