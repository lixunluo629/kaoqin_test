package org.springframework.jmx.support;

/* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/jmx/support/RegistrationPolicy.class */
public enum RegistrationPolicy {
    FAIL_ON_EXISTING,
    IGNORE_EXISTING,
    REPLACE_EXISTING;

    static RegistrationPolicy valueOf(int registrationBehavior) {
        switch (registrationBehavior) {
            case 0:
                return FAIL_ON_EXISTING;
            case 1:
                return IGNORE_EXISTING;
            case 2:
                return REPLACE_EXISTING;
            default:
                throw new IllegalArgumentException("Unknown MBean registration behavior: " + registrationBehavior);
        }
    }
}
