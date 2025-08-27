package org.hibernate.validator.internal.util.privilegedactions;

import java.security.PrivilegedAction;
import org.hibernate.validator.internal.util.logging.Log;
import org.hibernate.validator.internal.util.logging.LoggerFactory;

/* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/internal/util/privilegedactions/LoadClass.class */
public final class LoadClass implements PrivilegedAction<Class<?>> {
    private static final Log log = LoggerFactory.make();
    private static final String HIBERNATE_VALIDATOR_CLASS_NAME = "org.hibernate.validator";
    private final String className;
    private final ClassLoader classLoader;
    private final boolean fallbackOnTCCL;

    public static LoadClass action(String className, ClassLoader classLoader) {
        return new LoadClass(className, classLoader, true);
    }

    public static LoadClass action(String className, ClassLoader classLoader, boolean fallbackOnTCCL) {
        return new LoadClass(className, classLoader, fallbackOnTCCL);
    }

    private LoadClass(String className, ClassLoader classLoader, boolean fallbackOnTCCL) {
        this.className = className;
        this.classLoader = classLoader;
        this.fallbackOnTCCL = fallbackOnTCCL;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // java.security.PrivilegedAction
    public Class<?> run() {
        if (this.className.startsWith(HIBERNATE_VALIDATOR_CLASS_NAME)) {
            return loadClassInValidatorNameSpace();
        }
        return loadNonValidatorClass();
    }

    /* JADX WARN: Removed duplicated region for block: B:11:0x0024  */
    /* JADX WARN: Removed duplicated region for block: B:20:0x005a  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private java.lang.Class<?> loadClassInValidatorNameSpace() {
        /*
            r5 = this;
            java.lang.Class<org.hibernate.validator.HibernateValidator> r0 = org.hibernate.validator.HibernateValidator.class
            java.lang.ClassLoader r0 = r0.getClassLoader()
            r6 = r0
            r0 = r5
            java.lang.String r0 = r0.className     // Catch: java.lang.ClassNotFoundException -> L14 java.lang.RuntimeException -> L1a
            r1 = 1
            java.lang.Class<org.hibernate.validator.HibernateValidator> r2 = org.hibernate.validator.HibernateValidator.class
            java.lang.ClassLoader r2 = r2.getClassLoader()     // Catch: java.lang.ClassNotFoundException -> L14 java.lang.RuntimeException -> L1a
            java.lang.Class r0 = java.lang.Class.forName(r0, r1, r2)     // Catch: java.lang.ClassNotFoundException -> L14 java.lang.RuntimeException -> L1a
            return r0
        L14:
            r8 = move-exception
            r0 = r8
            r7 = r0
            goto L1d
        L1a:
            r8 = move-exception
            r0 = r8
            r7 = r0
        L1d:
            r0 = r5
            boolean r0 = r0.fallbackOnTCCL
            if (r0 == 0) goto L5a
            java.lang.Thread r0 = java.lang.Thread.currentThread()
            java.lang.ClassLoader r0 = r0.getContextClassLoader()
            r8 = r0
            r0 = r8
            if (r0 == 0) goto L4b
            r0 = r5
            java.lang.String r0 = r0.className     // Catch: java.lang.ClassNotFoundException -> L39
            r1 = 0
            r2 = r8
            java.lang.Class r0 = java.lang.Class.forName(r0, r1, r2)     // Catch: java.lang.ClassNotFoundException -> L39
            return r0
        L39:
            r9 = move-exception
            org.hibernate.validator.internal.util.logging.Log r0 = org.hibernate.validator.internal.util.privilegedactions.LoadClass.log
            r1 = r5
            java.lang.String r1 = r1.className
            r2 = r8
            r3 = r9
            javax.validation.ValidationException r0 = r0.getUnableToLoadClassException(r1, r2, r3)
            throw r0
        L4b:
            org.hibernate.validator.internal.util.logging.Log r0 = org.hibernate.validator.internal.util.privilegedactions.LoadClass.log
            r1 = r5
            java.lang.String r1 = r1.className
            r2 = r6
            r3 = r7
            javax.validation.ValidationException r0 = r0.getUnableToLoadClassException(r1, r2, r3)
            throw r0
        L5a:
            org.hibernate.validator.internal.util.logging.Log r0 = org.hibernate.validator.internal.util.privilegedactions.LoadClass.log
            r1 = r5
            java.lang.String r1 = r1.className
            r2 = r6
            r3 = r7
            javax.validation.ValidationException r0 = r0.getUnableToLoadClassException(r1, r2, r3)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: org.hibernate.validator.internal.util.privilegedactions.LoadClass.loadClassInValidatorNameSpace():java.lang.Class");
    }

    private Class<?> loadNonValidatorClass() {
        Exception exception = null;
        try {
            if (this.classLoader != null) {
                return Class.forName(this.className, false, this.classLoader);
            }
        } catch (ClassNotFoundException e) {
            exception = e;
        } catch (RuntimeException e2) {
            exception = e2;
        }
        if (this.fallbackOnTCCL) {
            try {
                ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
                if (contextClassLoader != null) {
                    return Class.forName(this.className, false, contextClassLoader);
                }
            } catch (ClassNotFoundException e3) {
            } catch (RuntimeException e4) {
            }
            ClassLoader loader = LoadClass.class.getClassLoader();
            try {
                return Class.forName(this.className, true, loader);
            } catch (ClassNotFoundException e5) {
                throw log.getUnableToLoadClassException(this.className, loader, e5);
            }
        }
        throw log.getUnableToLoadClassException(this.className, this.classLoader, exception);
    }
}
