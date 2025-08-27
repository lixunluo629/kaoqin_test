package com.itextpdf.kernel.security;

import java.io.Serializable;
import org.bouncycastle.cms.Recipient;
import org.bouncycastle.cms.RecipientId;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/security/IExternalDecryptionProcess.class */
public interface IExternalDecryptionProcess extends Serializable {
    RecipientId getCmsRecipientId();

    Recipient getCmsRecipient();
}
