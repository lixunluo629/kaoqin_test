package com.itextpdf.kernel.pdf;

import com.itextpdf.kernel.counter.event.IMetaInfo;
import java.io.OutputStream;
import java.security.PrivateKey;
import java.util.Map;
import org.bouncycastle.cms.CMSException;
import org.bouncycastle.cms.Recipient;
import org.bouncycastle.cms.RecipientInformation;
import org.bouncycastle.cms.jcajce.JceKeyTransEnvelopedRecipient;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/pdf/PdfEncryptor.class */
public final class PdfEncryptor {
    private IMetaInfo metaInfo;
    private EncryptionProperties properties;

    public static void encrypt(PdfReader reader, OutputStream os, EncryptionProperties properties, Map<String, String> newInfo) {
        new PdfEncryptor().setEncryptionProperties(properties).encrypt(reader, os, newInfo);
    }

    public static void encrypt(PdfReader reader, OutputStream os, EncryptionProperties properties) {
        encrypt(reader, os, properties, null);
    }

    public static String getPermissionsVerbose(int permissions) {
        StringBuilder buf = new StringBuilder("Allowed:");
        if ((2052 & permissions) == 2052) {
            buf.append(" Printing");
        }
        if ((8 & permissions) == 8) {
            buf.append(" Modify contents");
        }
        if ((16 & permissions) == 16) {
            buf.append(" Copy");
        }
        if ((32 & permissions) == 32) {
            buf.append(" Modify annotations");
        }
        if ((256 & permissions) == 256) {
            buf.append(" Fill in");
        }
        if ((512 & permissions) == 512) {
            buf.append(" Screen readers");
        }
        if ((1024 & permissions) == 1024) {
            buf.append(" Assembly");
        }
        if ((4 & permissions) == 4) {
            buf.append(" Degraded printing");
        }
        return buf.toString();
    }

    public static boolean isPrintingAllowed(int permissions) {
        return (2052 & permissions) == 2052;
    }

    public static boolean isModifyContentsAllowed(int permissions) {
        return (8 & permissions) == 8;
    }

    public static boolean isCopyAllowed(int permissions) {
        return (16 & permissions) == 16;
    }

    public static boolean isModifyAnnotationsAllowed(int permissions) {
        return (32 & permissions) == 32;
    }

    public static boolean isFillInAllowed(int permissions) {
        return (256 & permissions) == 256;
    }

    public static boolean isScreenReadersAllowed(int permissions) {
        return (512 & permissions) == 512;
    }

    public static boolean isAssemblyAllowed(int permissions) {
        return (1024 & permissions) == 1024;
    }

    public static boolean isDegradedPrintingAllowed(int permissions) {
        return (4 & permissions) == 4;
    }

    public static byte[] getContent(RecipientInformation recipientInfo, PrivateKey certificateKey, String certificateKeyProvider) throws CMSException {
        Recipient jceKeyTransRecipient = new JceKeyTransEnvelopedRecipient(certificateKey).setProvider(certificateKeyProvider);
        return recipientInfo.getContent(jceKeyTransRecipient);
    }

    public PdfEncryptor setEventCountingMetaInfo(IMetaInfo metaInfo) {
        this.metaInfo = metaInfo;
        return this;
    }

    public PdfEncryptor setEncryptionProperties(EncryptionProperties properties) {
        this.properties = properties;
        return this;
    }

    public void encrypt(PdfReader reader, OutputStream os, Map<String, String> newInfo) {
        WriterProperties writerProperties = new WriterProperties();
        writerProperties.encryptionProperties = this.properties;
        PdfWriter writer = new PdfWriter(os, writerProperties);
        StampingProperties stampingProperties = new StampingProperties();
        stampingProperties.setEventCountingMetaInfo(this.metaInfo);
        PdfDocument document = new PdfDocument(reader, writer, stampingProperties);
        document.getDocumentInfo().setMoreInfo(newInfo);
        document.close();
    }

    public void encrypt(PdfReader reader, OutputStream os) {
        encrypt(reader, os, (Map<String, String>) null);
    }
}
