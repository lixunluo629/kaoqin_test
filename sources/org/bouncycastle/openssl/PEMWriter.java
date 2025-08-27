package org.bouncycastle.openssl;

import java.io.IOException;
import java.io.Writer;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import org.bouncycastle.util.io.pem.PemGenerationException;
import org.bouncycastle.util.io.pem.PemObjectGenerator;
import org.bouncycastle.util.io.pem.PemWriter;

/* JADX WARN: Classes with same name are omitted:
  bcpkix-jdk15on-1.64.jar:org/bouncycastle/openssl/PEMWriter.class
 */
/* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/openssl/PEMWriter.class */
public class PEMWriter extends PemWriter {
    private String provider;

    public PEMWriter(Writer writer) {
        this(writer, "BC");
    }

    public PEMWriter(Writer writer, String str) {
        super(writer);
        this.provider = str;
    }

    public void writeObject(Object obj) throws IOException {
        try {
            super.writeObject((PemObjectGenerator) new MiscPEMGenerator(obj));
        } catch (PemGenerationException e) {
            if (!(e.getCause() instanceof IOException)) {
                throw e;
            }
            throw ((IOException) e.getCause());
        }
    }

    @Override // org.bouncycastle.util.io.pem.PemWriter
    public void writeObject(PemObjectGenerator pemObjectGenerator) throws IOException {
        super.writeObject(pemObjectGenerator);
    }

    public void writeObject(Object obj, String str, char[] cArr, SecureRandom secureRandom) throws IOException {
        try {
            super.writeObject((PemObjectGenerator) new MiscPEMGenerator(obj, str, cArr, secureRandom, this.provider));
        } catch (NoSuchProviderException e) {
            throw new EncryptionException(e.getMessage(), e);
        }
    }
}
