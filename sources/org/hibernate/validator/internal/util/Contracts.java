package org.hibernate.validator.internal.util;

import org.hibernate.validator.internal.util.logging.Log;
import org.hibernate.validator.internal.util.logging.LoggerFactory;
import org.hibernate.validator.internal.util.logging.Messages;

/* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/internal/util/Contracts.class */
public final class Contracts {
    private static final Log log = LoggerFactory.make();

    private Contracts() {
    }

    public static void assertNotNull(Object o) {
        assertNotNull(o, Messages.MESSAGES.mustNotBeNull());
    }

    public static void assertNotNull(Object o, String message) {
        if (o == null) {
            throw log.getIllegalArgumentException(message);
        }
    }

    public static void assertValueNotNull(Object o, String name) {
        if (o == null) {
            throw log.getIllegalArgumentException(Messages.MESSAGES.mustNotBeNull(name));
        }
    }

    public static void assertTrue(boolean condition, String message) {
        if (!condition) {
            throw log.getIllegalArgumentException(message);
        }
    }

    public static void assertNotEmpty(String s, String message) {
        if (s.length() == 0) {
            throw log.getIllegalArgumentException(message);
        }
    }
}
