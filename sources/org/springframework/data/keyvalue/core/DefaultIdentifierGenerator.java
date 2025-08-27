package org.springframework.data.keyvalue.core;

import ch.qos.logback.core.net.ssl.SSL;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.util.TypeInformation;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

/* loaded from: spring-data-keyvalue-1.2.23.RELEASE.jar:org/springframework/data/keyvalue/core/DefaultIdentifierGenerator.class */
enum DefaultIdentifierGenerator implements IdentifierGenerator {
    INSTANCE;

    private final AtomicReference<SecureRandom> secureRandom = new AtomicReference<>(null);

    DefaultIdentifierGenerator() {
    }

    @Override // org.springframework.data.keyvalue.core.IdentifierGenerator
    public <T> T generateIdentifierOfType(TypeInformation<T> typeInformation) {
        Class<T> type = typeInformation.getType();
        if (ClassUtils.isAssignable(UUID.class, type)) {
            return (T) UUID.randomUUID();
        }
        if (ClassUtils.isAssignable(String.class, type)) {
            return (T) UUID.randomUUID().toString();
        }
        if (ClassUtils.isAssignable(Integer.class, type)) {
            return (T) Integer.valueOf(getSecureRandom().nextInt());
        }
        if (ClassUtils.isAssignable(Long.class, type)) {
            return (T) Long.valueOf(getSecureRandom().nextLong());
        }
        throw new InvalidDataAccessApiUsageException("Non gereratable id type....");
    }

    private SecureRandom getSecureRandom() throws NoSuchAlgorithmException {
        SecureRandom secureRandom = this.secureRandom.get();
        if (secureRandom != null) {
            return secureRandom;
        }
        for (String algorithm : OsTools.secureRandomAlgorithmNames()) {
            try {
                secureRandom = SecureRandom.getInstance(algorithm);
            } catch (NoSuchAlgorithmException e) {
            }
        }
        if (secureRandom == null) {
            throw new InvalidDataAccessApiUsageException(String.format("Could not create SecureRandom instance for one of the algorithms '%s'.", StringUtils.collectionToCommaDelimitedString(OsTools.secureRandomAlgorithmNames())));
        }
        this.secureRandom.compareAndSet(null, secureRandom);
        return secureRandom;
    }

    /* loaded from: spring-data-keyvalue-1.2.23.RELEASE.jar:org/springframework/data/keyvalue/core/DefaultIdentifierGenerator$OsTools.class */
    private static class OsTools {
        private static final String OPERATING_SYSTEM_NAME = System.getProperty("os.name").toLowerCase();
        private static final List<String> SECURE_RANDOM_ALGORITHMS_LINUX_OSX_SOLARIS = Arrays.asList("NativePRNGBlocking", "NativePRNGNonBlocking", "NativePRNG", SSL.DEFAULT_SECURE_RANDOM_ALGORITHM);
        private static final List<String> SECURE_RANDOM_ALGORITHMS_WINDOWS = Arrays.asList(SSL.DEFAULT_SECURE_RANDOM_ALGORITHM, "Windows-PRNG");

        private OsTools() {
        }

        static List<String> secureRandomAlgorithmNames() {
            return OPERATING_SYSTEM_NAME.indexOf("win") >= 0 ? SECURE_RANDOM_ALGORITHMS_WINDOWS : SECURE_RANDOM_ALGORITHMS_LINUX_OSX_SOLARIS;
        }
    }
}
