package org.apache.tomcat.util.net.jsse;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import com.moredian.onpremise.core.utils.RSAUtils;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.ArrayList;
import java.util.List;
import javax.crypto.Cipher;
import javax.crypto.EncryptedPrivateKeyInfo;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import org.apache.tomcat.util.codec.binary.Base64;
import org.apache.tomcat.util.file.ConfigFileLoader;
import org.apache.tomcat.util.res.StringManager;

/* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/tomcat/util/net/jsse/PEMFile.class */
public class PEMFile {
    private static final StringManager sm = StringManager.getManager((Class<?>) PEMFile.class);
    private String filename;
    private List<X509Certificate> certificates;
    private PrivateKey privateKey;

    public List<X509Certificate> getCertificates() {
        return this.certificates;
    }

    public PrivateKey getPrivateKey() {
        return this.privateKey;
    }

    public PEMFile(String filename) throws GeneralSecurityException, IOException {
        this(filename, null);
    }

    public PEMFile(String filename, String password) throws GeneralSecurityException, IOException {
        this.certificates = new ArrayList();
        this.filename = filename;
        List<Part> parts = new ArrayList<>();
        InputStream inputStream = ConfigFileLoader.getInputStream(filename);
        Throwable th = null;
        try {
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.US_ASCII));
                Part part = null;
                while (true) {
                    String line = reader.readLine();
                    if (line == null) {
                        break;
                    }
                    if (line.startsWith(Part.BEGIN_BOUNDARY)) {
                        part = new Part();
                        part.type = line.substring(Part.BEGIN_BOUNDARY.length(), line.length() - 5).trim();
                    } else if (line.startsWith(Part.END_BOUNDARY)) {
                        parts.add(part);
                        part = null;
                    } else if (part != null && !line.contains(":") && !line.startsWith(SymbolConstants.SPACE_SYMBOL)) {
                        part.content += line;
                    }
                }
                if (inputStream != null) {
                    if (0 != 0) {
                        try {
                            inputStream.close();
                        } catch (Throwable x2) {
                            th.addSuppressed(x2);
                        }
                    } else {
                        inputStream.close();
                    }
                }
                for (Part part2 : parts) {
                    switch (part2.type) {
                        case "PRIVATE KEY":
                            this.privateKey = part2.toPrivateKey(null);
                            break;
                        case "ENCRYPTED PRIVATE KEY":
                            this.privateKey = part2.toPrivateKey(password);
                            break;
                        case "CERTIFICATE":
                        case "X509 CERTIFICATE":
                            this.certificates.add(part2.toCertificate());
                            break;
                    }
                }
            } finally {
            }
        } catch (Throwable th2) {
            if (inputStream != null) {
                if (th != null) {
                    try {
                        inputStream.close();
                    } catch (Throwable x22) {
                        th.addSuppressed(x22);
                    }
                } else {
                    inputStream.close();
                }
            }
            throw th2;
        }
    }

    /* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/tomcat/util/net/jsse/PEMFile$Part.class */
    private class Part {
        public static final String BEGIN_BOUNDARY = "-----BEGIN ";
        public static final String END_BOUNDARY = "-----END ";
        public String type;
        public String content;

        private Part() {
            this.content = "";
        }

        private byte[] decode() {
            return Base64.decodeBase64(this.content);
        }

        public X509Certificate toCertificate() throws CertificateException {
            CertificateFactory factory = CertificateFactory.getInstance("X.509");
            return (X509Certificate) factory.generateCertificate(new ByteArrayInputStream(decode()));
        }

        public PrivateKey toPrivateKey(String password) throws GeneralSecurityException, IOException {
            KeySpec keySpec;
            if (password == null) {
                keySpec = new PKCS8EncodedKeySpec(decode());
            } else {
                EncryptedPrivateKeyInfo privateKeyInfo = new EncryptedPrivateKeyInfo(decode());
                SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance(privateKeyInfo.getAlgName());
                SecretKey secretKey = secretKeyFactory.generateSecret(new PBEKeySpec(password.toCharArray()));
                Cipher cipher = Cipher.getInstance(privateKeyInfo.getAlgName());
                cipher.init(2, secretKey, privateKeyInfo.getAlgParameters());
                keySpec = privateKeyInfo.getKeySpec(cipher);
            }
            InvalidKeyException exception = new InvalidKeyException(PEMFile.sm.getString("jsse.pemParseError", PEMFile.this.filename));
            String[] arr$ = {RSAUtils.RSA_KEY_ALGORITHM, "DSA", "EC"};
            for (String algorithm : arr$) {
                try {
                    return KeyFactory.getInstance(algorithm).generatePrivate(keySpec);
                } catch (InvalidKeySpecException e) {
                    exception.addSuppressed(e);
                }
            }
            throw exception;
        }
    }
}
