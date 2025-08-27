package org.aspectj.lang;

import org.aspectj.lang.reflect.SourceLocation;

/* JADX WARN: Classes with same name are omitted:
  aspectjrt-1.8.14.jar:org/aspectj/lang/JoinPoint.class
 */
/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/lang/JoinPoint.class */
public interface JoinPoint {
    public static final String METHOD_EXECUTION = "method-execution";
    public static final String METHOD_CALL = "method-call";
    public static final String CONSTRUCTOR_EXECUTION = "constructor-execution";
    public static final String CONSTRUCTOR_CALL = "constructor-call";
    public static final String FIELD_GET = "field-get";
    public static final String FIELD_SET = "field-set";
    public static final String STATICINITIALIZATION = "staticinitialization";
    public static final String PREINITIALIZATION = "preinitialization";
    public static final String INITIALIZATION = "initialization";
    public static final String EXCEPTION_HANDLER = "exception-handler";
    public static final String SYNCHRONIZATION_LOCK = "lock";
    public static final String SYNCHRONIZATION_UNLOCK = "unlock";
    public static final String ADVICE_EXECUTION = "adviceexecution";

    /* JADX WARN: Classes with same name are omitted:
  aspectjrt-1.8.14.jar:org/aspectj/lang/JoinPoint$EnclosingStaticPart.class
 */
    /* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/lang/JoinPoint$EnclosingStaticPart.class */
    public interface EnclosingStaticPart extends StaticPart {
    }

    /* JADX WARN: Classes with same name are omitted:
  aspectjrt-1.8.14.jar:org/aspectj/lang/JoinPoint$StaticPart.class
 */
    /* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/lang/JoinPoint$StaticPart.class */
    public interface StaticPart {
        Signature getSignature();

        SourceLocation getSourceLocation();

        String getKind();

        int getId();

        String toString();

        String toShortString();

        String toLongString();
    }

    String toString();

    String toShortString();

    String toLongString();

    Object getThis();

    Object getTarget();

    Object[] getArgs();

    Signature getSignature();

    SourceLocation getSourceLocation();

    String getKind();

    StaticPart getStaticPart();
}
