package org.bouncycastle.cms;

/* loaded from: bcpkix-jdk15on-1.64.jar:org/bouncycastle/cms/CMSEncryptedGenerator.class */
public class CMSEncryptedGenerator {
    protected CMSAttributeTableGenerator unprotectedAttributeGenerator = null;

    protected CMSEncryptedGenerator() {
    }

    public void setUnprotectedAttributeGenerator(CMSAttributeTableGenerator cMSAttributeTableGenerator) {
        this.unprotectedAttributeGenerator = cMSAttributeTableGenerator;
    }
}
