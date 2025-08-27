package org.hibernate.validator.internal.xml;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.security.AccessController;
import java.security.PrivilegedAction;
import org.hibernate.validator.internal.util.logging.Log;
import org.hibernate.validator.internal.util.logging.LoggerFactory;
import org.hibernate.validator.internal.util.privilegedactions.GetClassLoader;

/* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/internal/xml/ResourceLoaderHelper.class */
final class ResourceLoaderHelper {
    private static final Log log = LoggerFactory.make();

    ResourceLoaderHelper() {
    }

    static InputStream getResettableInputStreamForPath(String path, ClassLoader externalClassLoader) {
        ClassLoader loader;
        String inputPath = path;
        if (inputPath.startsWith("/")) {
            inputPath = inputPath.substring(1);
        }
        InputStream inputStream = null;
        if (externalClassLoader != null) {
            log.debug("Trying to load " + path + " via user class loader");
            inputStream = externalClassLoader.getResourceAsStream(inputPath);
        }
        if (inputStream == null && (loader = (ClassLoader) run(GetClassLoader.fromContext())) != null) {
            log.debug("Trying to load " + path + " via TCCL");
            inputStream = loader.getResourceAsStream(inputPath);
        }
        if (inputStream == null) {
            log.debug("Trying to load " + path + " via Hibernate Validator's class loader");
            inputStream = ResourceLoaderHelper.class.getClassLoader().getResourceAsStream(inputPath);
        }
        if (inputStream == null) {
            return null;
        }
        if (inputStream.markSupported()) {
            return inputStream;
        }
        return new BufferedInputStream(inputStream);
    }

    private static <T> T run(PrivilegedAction<T> privilegedAction) {
        return System.getSecurityManager() != null ? (T) AccessController.doPrivileged(privilegedAction) : privilegedAction.run();
    }
}
