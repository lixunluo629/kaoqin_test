package lombok.eclipse.handlers;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import lombok.ConfigurationKeys;
import lombok.core.AST;
import lombok.core.AnnotationValues;
import lombok.core.handlers.HandlerUtil;
import lombok.eclipse.Eclipse;
import lombok.eclipse.EclipseAnnotationHandler;
import lombok.eclipse.EclipseNode;
import lombok.eclipse.handlers.EclipseHandlerUtil;
import lombok.extern.apachecommons.CommonsLog;
import lombok.extern.java.Log;
import lombok.extern.jbosslog.JBossLog;
import lombok.extern.log4j.Log4j;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import lombok.extern.slf4j.XSlf4j;
import org.apache.commons.logging.LogFactory;
import org.eclipse.jdt.internal.compiler.ast.ClassLiteralAccess;
import org.eclipse.jdt.internal.compiler.ast.Expression;
import org.eclipse.jdt.internal.compiler.ast.FieldDeclaration;
import org.eclipse.jdt.internal.compiler.ast.MessageSend;
import org.eclipse.jdt.internal.compiler.ast.QualifiedTypeReference;
import org.eclipse.jdt.internal.compiler.ast.SingleTypeReference;
import org.eclipse.jdt.internal.compiler.ast.StringLiteral;
import org.eclipse.jdt.internal.compiler.ast.TypeDeclaration;
import org.eclipse.jdt.internal.compiler.ast.TypeReference;

/* loaded from: lombok-1.16.22.jar:lombok/eclipse/handlers/HandleLog.SCL.lombok */
public class HandleLog {
    private static /* synthetic */ int[] $SWITCH_TABLE$lombok$core$AST$Kind;

    static /* synthetic */ int[] $SWITCH_TABLE$lombok$core$AST$Kind() {
        int[] iArr = $SWITCH_TABLE$lombok$core$AST$Kind;
        if (iArr != null) {
            return iArr;
        }
        int[] iArr2 = new int[AST.Kind.valuesCustom().length];
        try {
            iArr2[AST.Kind.ANNOTATION.ordinal()] = 6;
        } catch (NoSuchFieldError unused) {
        }
        try {
            iArr2[AST.Kind.ARGUMENT.ordinal()] = 7;
        } catch (NoSuchFieldError unused2) {
        }
        try {
            iArr2[AST.Kind.COMPILATION_UNIT.ordinal()] = 1;
        } catch (NoSuchFieldError unused3) {
        }
        try {
            iArr2[AST.Kind.FIELD.ordinal()] = 3;
        } catch (NoSuchFieldError unused4) {
        }
        try {
            iArr2[AST.Kind.INITIALIZER.ordinal()] = 4;
        } catch (NoSuchFieldError unused5) {
        }
        try {
            iArr2[AST.Kind.LOCAL.ordinal()] = 8;
        } catch (NoSuchFieldError unused6) {
        }
        try {
            iArr2[AST.Kind.METHOD.ordinal()] = 5;
        } catch (NoSuchFieldError unused7) {
        }
        try {
            iArr2[AST.Kind.STATEMENT.ordinal()] = 9;
        } catch (NoSuchFieldError unused8) {
        }
        try {
            iArr2[AST.Kind.TYPE.ordinal()] = 2;
        } catch (NoSuchFieldError unused9) {
        }
        $SWITCH_TABLE$lombok$core$AST$Kind = iArr2;
        return iArr2;
    }

    private HandleLog() {
        throw new UnsupportedOperationException();
    }

    public static void processAnnotation(LoggingFramework framework, AnnotationValues<? extends Annotation> annotation, org.eclipse.jdt.internal.compiler.ast.Annotation source, EclipseNode annotationNode, String loggerTopic) {
        EclipseNode owner = annotationNode.up();
        switch ($SWITCH_TABLE$lombok$core$AST$Kind()[owner.getKind().ordinal()]) {
            case 2:
                String logFieldName = (String) annotationNode.getAst().readConfiguration(ConfigurationKeys.LOG_ANY_FIELD_NAME);
                if (logFieldName == null) {
                    logFieldName = "log";
                }
                boolean useStatic = !Boolean.FALSE.equals(annotationNode.getAst().readConfiguration(ConfigurationKeys.LOG_ANY_FIELD_IS_STATIC));
                TypeDeclaration typeDecl = null;
                if (owner.get() instanceof TypeDeclaration) {
                    typeDecl = (TypeDeclaration) owner.get();
                }
                int modifiers = typeDecl == null ? 0 : typeDecl.modifiers;
                boolean notAClass = (modifiers & 8704) != 0;
                if (typeDecl == null || notAClass) {
                    annotationNode.addError(String.valueOf(framework.getAnnotationAsString()) + " is legal only on classes and enums.");
                    break;
                } else if (EclipseHandlerUtil.fieldExists(logFieldName, owner) != EclipseHandlerUtil.MemberExistsResult.NOT_EXISTS) {
                    annotationNode.addWarning("Field '" + logFieldName + "' already exists.");
                    break;
                } else {
                    ClassLiteralAccess loggingType = selfType(owner, source);
                    FieldDeclaration fieldDeclaration = createField(framework, source, loggingType, logFieldName, useStatic, loggerTopic);
                    fieldDeclaration.traverse(new SetGeneratedByVisitor(source), typeDecl.staticInitializerScope);
                    EclipseHandlerUtil.injectField(owner, fieldDeclaration);
                    owner.rebuild();
                    break;
                }
                break;
        }
    }

    public static ClassLiteralAccess selfType(EclipseNode type, org.eclipse.jdt.internal.compiler.ast.Annotation source) {
        int pS = source.sourceStart;
        int pE = source.sourceEnd;
        long p = (pS << 32) | pE;
        TypeDeclaration typeDeclaration = type.get();
        SingleTypeReference singleTypeReference = new SingleTypeReference(typeDeclaration.name, p);
        EclipseHandlerUtil.setGeneratedBy(singleTypeReference, source);
        ClassLiteralAccess result = new ClassLiteralAccess(source.sourceEnd, singleTypeReference);
        EclipseHandlerUtil.setGeneratedBy(result, source);
        return result;
    }

    private static FieldDeclaration createField(LoggingFramework framework, org.eclipse.jdt.internal.compiler.ast.Annotation source, ClassLiteralAccess loggingType, String logFieldName, boolean useStatic, String loggerTopic) {
        StringLiteral stringLiteralCreateFactoryParameter;
        int pS = source.sourceStart;
        int pE = source.sourceEnd;
        long p = (pS << 32) | pE;
        FieldDeclaration fieldDecl = new FieldDeclaration(logFieldName.toCharArray(), 0, -1);
        EclipseHandlerUtil.setGeneratedBy(fieldDecl, source);
        fieldDecl.declarationSourceEnd = -1;
        fieldDecl.modifiers = 2 | (useStatic ? 8 : 0) | 16;
        fieldDecl.type = createTypeReference(framework.getLoggerTypeName(), source);
        MessageSend factoryMethodCall = new MessageSend();
        EclipseHandlerUtil.setGeneratedBy(factoryMethodCall, source);
        factoryMethodCall.receiver = EclipseHandlerUtil.createNameReference(framework.getLoggerFactoryTypeName(), source);
        factoryMethodCall.selector = framework.getLoggerFactoryMethodName().toCharArray();
        if (loggerTopic == null || loggerTopic.trim().length() == 0) {
            stringLiteralCreateFactoryParameter = framework.createFactoryParameter(loggingType, source);
        } else {
            stringLiteralCreateFactoryParameter = new StringLiteral(loggerTopic.toCharArray(), pS, pE, 0);
        }
        factoryMethodCall.arguments = new Expression[]{stringLiteralCreateFactoryParameter};
        factoryMethodCall.nameSourcePosition = p;
        factoryMethodCall.sourceStart = pS;
        factoryMethodCall.statementEnd = pE;
        factoryMethodCall.sourceEnd = pE;
        fieldDecl.initialization = factoryMethodCall;
        return fieldDecl;
    }

    public static TypeReference createTypeReference(String typeName, org.eclipse.jdt.internal.compiler.ast.Annotation source) {
        QualifiedTypeReference qualifiedTypeReference;
        int pS = source.sourceStart;
        int pE = source.sourceEnd;
        long p = (pS << 32) | pE;
        if (typeName.contains(".")) {
            char[][] typeNameTokens = Eclipse.fromQualifiedName(typeName);
            long[] pos = new long[typeNameTokens.length];
            Arrays.fill(pos, p);
            qualifiedTypeReference = new QualifiedTypeReference(typeNameTokens, pos);
        } else {
            qualifiedTypeReference = null;
        }
        EclipseHandlerUtil.setGeneratedBy(qualifiedTypeReference, source);
        return qualifiedTypeReference;
    }

    /* loaded from: lombok-1.16.22.jar:lombok/eclipse/handlers/HandleLog$HandleCommonsLog.SCL.lombok */
    public static class HandleCommonsLog extends EclipseAnnotationHandler<CommonsLog> {
        @Override // lombok.eclipse.EclipseAnnotationHandler
        public void handle(AnnotationValues<CommonsLog> annotation, org.eclipse.jdt.internal.compiler.ast.Annotation source, EclipseNode annotationNode) {
            HandlerUtil.handleFlagUsage(annotationNode, ConfigurationKeys.LOG_COMMONS_FLAG_USAGE, "@apachecommons.CommonsLog", ConfigurationKeys.LOG_ANY_FLAG_USAGE, "any @Log");
            HandleLog.processAnnotation(LoggingFramework.COMMONS, annotation, source, annotationNode, ((CommonsLog) annotation.getInstance()).topic());
        }
    }

    /* loaded from: lombok-1.16.22.jar:lombok/eclipse/handlers/HandleLog$HandleJulLog.SCL.lombok */
    public static class HandleJulLog extends EclipseAnnotationHandler<Log> {
        @Override // lombok.eclipse.EclipseAnnotationHandler
        public void handle(AnnotationValues<Log> annotation, org.eclipse.jdt.internal.compiler.ast.Annotation source, EclipseNode annotationNode) {
            HandlerUtil.handleFlagUsage(annotationNode, ConfigurationKeys.LOG_JUL_FLAG_USAGE, "@java.Log", ConfigurationKeys.LOG_ANY_FLAG_USAGE, "any @Log");
            HandleLog.processAnnotation(LoggingFramework.JUL, annotation, source, annotationNode, ((Log) annotation.getInstance()).topic());
        }
    }

    /* loaded from: lombok-1.16.22.jar:lombok/eclipse/handlers/HandleLog$HandleLog4jLog.SCL.lombok */
    public static class HandleLog4jLog extends EclipseAnnotationHandler<Log4j> {
        @Override // lombok.eclipse.EclipseAnnotationHandler
        public void handle(AnnotationValues<Log4j> annotation, org.eclipse.jdt.internal.compiler.ast.Annotation source, EclipseNode annotationNode) {
            HandlerUtil.handleFlagUsage(annotationNode, ConfigurationKeys.LOG_LOG4J_FLAG_USAGE, "@Log4j", ConfigurationKeys.LOG_ANY_FLAG_USAGE, "any @Log");
            HandleLog.processAnnotation(LoggingFramework.LOG4J, annotation, source, annotationNode, ((Log4j) annotation.getInstance()).topic());
        }
    }

    /* loaded from: lombok-1.16.22.jar:lombok/eclipse/handlers/HandleLog$HandleLog4j2Log.SCL.lombok */
    public static class HandleLog4j2Log extends EclipseAnnotationHandler<Log4j2> {
        @Override // lombok.eclipse.EclipseAnnotationHandler
        public void handle(AnnotationValues<Log4j2> annotation, org.eclipse.jdt.internal.compiler.ast.Annotation source, EclipseNode annotationNode) {
            HandlerUtil.handleFlagUsage(annotationNode, ConfigurationKeys.LOG_LOG4J2_FLAG_USAGE, "@Log4j2", ConfigurationKeys.LOG_ANY_FLAG_USAGE, "any @Log");
            HandleLog.processAnnotation(LoggingFramework.LOG4J2, annotation, source, annotationNode, ((Log4j2) annotation.getInstance()).topic());
        }
    }

    /* loaded from: lombok-1.16.22.jar:lombok/eclipse/handlers/HandleLog$HandleSlf4jLog.SCL.lombok */
    public static class HandleSlf4jLog extends EclipseAnnotationHandler<Slf4j> {
        @Override // lombok.eclipse.EclipseAnnotationHandler
        public void handle(AnnotationValues<Slf4j> annotation, org.eclipse.jdt.internal.compiler.ast.Annotation source, EclipseNode annotationNode) {
            HandlerUtil.handleFlagUsage(annotationNode, ConfigurationKeys.LOG_SLF4J_FLAG_USAGE, "@Slf4j", ConfigurationKeys.LOG_ANY_FLAG_USAGE, "any @Log");
            HandleLog.processAnnotation(LoggingFramework.SLF4J, annotation, source, annotationNode, ((Slf4j) annotation.getInstance()).topic());
        }
    }

    /* loaded from: lombok-1.16.22.jar:lombok/eclipse/handlers/HandleLog$HandleXSlf4jLog.SCL.lombok */
    public static class HandleXSlf4jLog extends EclipseAnnotationHandler<XSlf4j> {
        @Override // lombok.eclipse.EclipseAnnotationHandler
        public void handle(AnnotationValues<XSlf4j> annotation, org.eclipse.jdt.internal.compiler.ast.Annotation source, EclipseNode annotationNode) {
            HandlerUtil.handleFlagUsage(annotationNode, ConfigurationKeys.LOG_XSLF4J_FLAG_USAGE, "@XSlf4j", ConfigurationKeys.LOG_ANY_FLAG_USAGE, "any @Log");
            HandleLog.processAnnotation(LoggingFramework.XSLF4J, annotation, source, annotationNode, ((XSlf4j) annotation.getInstance()).topic());
        }
    }

    /* loaded from: lombok-1.16.22.jar:lombok/eclipse/handlers/HandleLog$HandleJBossLog.SCL.lombok */
    public static class HandleJBossLog extends EclipseAnnotationHandler<JBossLog> {
        @Override // lombok.eclipse.EclipseAnnotationHandler
        public void handle(AnnotationValues<JBossLog> annotation, org.eclipse.jdt.internal.compiler.ast.Annotation source, EclipseNode annotationNode) {
            HandlerUtil.handleFlagUsage(annotationNode, ConfigurationKeys.LOG_JBOSSLOG_FLAG_USAGE, "@JBossLog", ConfigurationKeys.LOG_ANY_FLAG_USAGE, "any @Log");
            HandleLog.processAnnotation(LoggingFramework.JBOSSLOG, annotation, source, annotationNode, ((JBossLog) annotation.getInstance()).topic());
        }
    }

    /* loaded from: lombok-1.16.22.jar:lombok/eclipse/handlers/HandleLog$LoggingFramework.SCL.lombok */
    enum LoggingFramework {
        COMMONS("org.apache.commons.logging.Log", LogFactory.FACTORY_PROPERTY, "getLog", "@CommonsLog"),
        JUL("java.util.logging.Logger", "java.util.logging.Logger", "getLogger", "@Log") { // from class: lombok.eclipse.handlers.HandleLog.LoggingFramework.1
            @Override // lombok.eclipse.handlers.HandleLog.LoggingFramework
            public Expression createFactoryParameter(ClassLiteralAccess type, org.eclipse.jdt.internal.compiler.ast.Annotation source) {
                int pS = source.sourceStart;
                int pE = source.sourceEnd;
                long p = (pS << 32) | pE;
                MessageSend factoryParameterCall = new MessageSend();
                EclipseHandlerUtil.setGeneratedBy(factoryParameterCall, source);
                factoryParameterCall.receiver = super.createFactoryParameter(type, source);
                factoryParameterCall.selector = "getName".toCharArray();
                factoryParameterCall.nameSourcePosition = p;
                factoryParameterCall.sourceStart = pS;
                factoryParameterCall.statementEnd = pE;
                factoryParameterCall.sourceEnd = pE;
                return factoryParameterCall;
            }
        },
        LOG4J("org.apache.log4j.Logger", "org.apache.log4j.Logger", "getLogger", "@Log4j"),
        LOG4J2("org.apache.logging.log4j.Logger", "org.apache.logging.log4j.LogManager", "getLogger", "@Log4j2"),
        SLF4J("org.slf4j.Logger", "org.slf4j.LoggerFactory", "getLogger", "@Slf4j"),
        XSLF4J("org.slf4j.ext.XLogger", "org.slf4j.ext.XLoggerFactory", "getXLogger", "@XSlf4j"),
        JBOSSLOG("org.jboss.logging.Logger", "org.jboss.logging.Logger", "getLogger", "@JBossLog");

        private final String loggerTypeName;
        private final String loggerFactoryTypeName;
        private final String loggerFactoryMethodName;
        private final String annotationAsString;

        /* renamed from: values, reason: to resolve conflict with enum method */
        public static LoggingFramework[] valuesCustom() {
            LoggingFramework[] loggingFrameworkArrValuesCustom = values();
            int length = loggingFrameworkArrValuesCustom.length;
            LoggingFramework[] loggingFrameworkArr = new LoggingFramework[length];
            System.arraycopy(loggingFrameworkArrValuesCustom, 0, loggingFrameworkArr, 0, length);
            return loggingFrameworkArr;
        }

        LoggingFramework(String loggerTypeName, String loggerFactoryTypeName, String loggerFactoryMethodName, String annotationAsString) {
            this.loggerTypeName = loggerTypeName;
            this.loggerFactoryTypeName = loggerFactoryTypeName;
            this.loggerFactoryMethodName = loggerFactoryMethodName;
            this.annotationAsString = annotationAsString;
        }

        /* synthetic */ LoggingFramework(String str, String str2, String str3, String str4, LoggingFramework loggingFramework) {
            this(str, str2, str3, str4);
        }

        final String getAnnotationAsString() {
            return this.annotationAsString;
        }

        final String getLoggerTypeName() {
            return this.loggerTypeName;
        }

        final String getLoggerFactoryTypeName() {
            return this.loggerFactoryTypeName;
        }

        final String getLoggerFactoryMethodName() {
            return this.loggerFactoryMethodName;
        }

        Expression createFactoryParameter(ClassLiteralAccess loggingType, org.eclipse.jdt.internal.compiler.ast.Annotation source) {
            TypeReference copy = EclipseHandlerUtil.copyType(loggingType.type, source);
            ClassLiteralAccess result = new ClassLiteralAccess(source.sourceEnd, copy);
            EclipseHandlerUtil.setGeneratedBy(result, source);
            return result;
        }
    }
}
