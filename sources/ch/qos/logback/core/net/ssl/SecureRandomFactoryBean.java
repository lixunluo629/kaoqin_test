package ch.qos.logback.core.net.ssl;

import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;

/* JADX WARN: Classes with same name are omitted:
  logback-core-1.2.3.jar:ch/qos/logback/core/net/ssl/SecureRandomFactoryBean.class
 */
/* loaded from: logback-core-1.2.3.jar.bak:ch/qos/logback/core/net/ssl/SecureRandomFactoryBean.class */
public class SecureRandomFactoryBean {
    private String algorithm;
    private String provider;

    public SecureRandom createSecureRandom() throws NoSuchAlgorithmException, NoSuchProviderException {
        try {
            return getProvider() != null ? SecureRandom.getInstance(getAlgorithm(), getProvider()) : SecureRandom.getInstance(getAlgorithm());
        } catch (NoSuchAlgorithmException e) {
            throw new NoSuchAlgorithmException("no such secure random algorithm: " + getAlgorithm());
        } catch (NoSuchProviderException e2) {
            throw new NoSuchProviderException("no such secure random provider: " + getProvider());
        }
    }

    public String getAlgorithm() {
        if (this.algorithm == null) {
            return SSL.DEFAULT_SECURE_RANDOM_ALGORITHM;
        }
        return this.algorithm;
    }

    public void setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
    }

    public String getProvider() {
        return this.provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }
}
