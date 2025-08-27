package org.springframework.boot.logging;

import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

/* loaded from: spring-boot-1.5.22.RELEASE.jar:org/springframework/boot/logging/Slf4JLoggingSystem.class */
public abstract class Slf4JLoggingSystem extends AbstractLoggingSystem {
    private static final String BRIDGE_HANDLER = "org.slf4j.bridge.SLF4JBridgeHandler";

    public Slf4JLoggingSystem(ClassLoader classLoader) {
        super(classLoader);
    }

    @Override // org.springframework.boot.logging.AbstractLoggingSystem, org.springframework.boot.logging.LoggingSystem
    public void beforeInitialize() {
        super.beforeInitialize();
        configureJdkLoggingBridgeHandler();
    }

    @Override // org.springframework.boot.logging.LoggingSystem
    public void cleanUp() {
        removeJdkLoggingBridgeHandler();
    }

    @Override // org.springframework.boot.logging.AbstractLoggingSystem
    protected void loadConfiguration(LoggingInitializationContext initializationContext, String location, LogFile logFile) {
        Assert.notNull(location, "Location must not be null");
        if (initializationContext != null) {
            applySystemProperties(initializationContext.getEnvironment(), logFile);
        }
    }

    private void configureJdkLoggingBridgeHandler() {
        try {
            if (isBridgeHandlerAvailable()) {
                removeJdkLoggingBridgeHandler();
                SLF4JBridgeHandler.install();
            }
        } catch (Throwable th) {
        }
    }

    protected final boolean isBridgeHandlerAvailable() {
        return ClassUtils.isPresent(BRIDGE_HANDLER, getClassLoader());
    }

    private void removeJdkLoggingBridgeHandler() {
        try {
            if (isBridgeHandlerAvailable()) {
                try {
                    SLF4JBridgeHandler.removeHandlersForRootLogger();
                } catch (NoSuchMethodError e) {
                    SLF4JBridgeHandler.uninstall();
                }
            }
        } catch (Throwable th) {
        }
    }
}
