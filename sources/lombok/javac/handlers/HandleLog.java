package lombok.javac.handlers;

import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.Name;
import java.lang.annotation.Annotation;
import lombok.ConfigurationKeys;
import lombok.core.AnnotationValues;
import lombok.core.handlers.HandlerUtil;
import lombok.extern.apachecommons.CommonsLog;
import lombok.extern.java.Log;
import lombok.extern.jbosslog.JBossLog;
import lombok.extern.log4j.Log4j;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import lombok.extern.slf4j.XSlf4j;
import lombok.javac.JavacAnnotationHandler;
import lombok.javac.JavacNode;
import lombok.javac.JavacTreeMaker;
import lombok.javac.handlers.JavacHandlerUtil;

/* loaded from: lombok-1.16.22.jar:lombok/javac/handlers/HandleLog.SCL.lombok */
public class HandleLog {
    private HandleLog() {
        throw new UnsupportedOperationException();
    }

    public static void processAnnotation(LoggingFramework framework, AnnotationValues<?> annotation, JavacNode annotationNode, String loggerTopic) {
        JavacHandlerUtil.deleteAnnotationIfNeccessary(annotationNode, framework.getAnnotationClass());
        JavacNode typeNode = annotationNode.up();
        switch (typeNode.getKind()) {
            case TYPE:
                String logFieldName = (String) annotationNode.getAst().readConfiguration(ConfigurationKeys.LOG_ANY_FIELD_NAME);
                if (logFieldName == null) {
                    logFieldName = "log";
                }
                boolean useStatic = !Boolean.FALSE.equals(annotationNode.getAst().readConfiguration(ConfigurationKeys.LOG_ANY_FIELD_IS_STATIC));
                if ((typeNode.get().mods.flags & 512) != 0) {
                    annotationNode.addError("@Log is legal only on classes and enums.");
                    break;
                } else if (JavacHandlerUtil.fieldExists(logFieldName, typeNode) != JavacHandlerUtil.MemberExistsResult.NOT_EXISTS) {
                    annotationNode.addWarning("Field '" + logFieldName + "' already exists.");
                    break;
                } else {
                    JCTree.JCFieldAccess loggingType = selfType(typeNode);
                    createField(framework, typeNode, loggingType, annotationNode.get(), logFieldName, useStatic, loggerTopic);
                    break;
                }
            default:
                annotationNode.addError("@Log is legal only on types.");
                break;
        }
    }

    public static JCTree.JCFieldAccess selfType(JavacNode typeNode) {
        JavacTreeMaker maker = typeNode.getTreeMaker();
        Name name = typeNode.get().name;
        return maker.Select(maker.Ident(name), typeNode.toName("class"));
    }

    private static boolean createField(LoggingFramework framework, JavacNode typeNode, JCTree.JCFieldAccess loggingType, JCTree source, String logFieldName, boolean useStatic, String loggerTopic) {
        JCTree.JCLiteral jCLiteralCreateFactoryParameter;
        JavacTreeMaker maker = typeNode.getTreeMaker();
        JCTree.JCExpression loggerType = JavacHandlerUtil.chainDotsString(typeNode, framework.getLoggerTypeName());
        JCTree.JCExpression factoryMethod = JavacHandlerUtil.chainDotsString(typeNode, framework.getLoggerFactoryMethodName());
        if (loggerTopic == null || loggerTopic.trim().length() == 0) {
            jCLiteralCreateFactoryParameter = framework.createFactoryParameter(typeNode, loggingType);
        } else {
            jCLiteralCreateFactoryParameter = maker.Literal(loggerTopic);
        }
        JCTree.JCMethodInvocation factoryMethodCall = maker.Apply(List.nil(), factoryMethod, List.of(jCLiteralCreateFactoryParameter));
        JCTree.JCVariableDecl fieldDecl = JavacHandlerUtil.recursiveSetGeneratedBy(maker.VarDef(maker.Modifiers(18 | (useStatic ? 8 : 0)), typeNode.toName(logFieldName), loggerType, factoryMethodCall), source, typeNode.getContext());
        JavacHandlerUtil.injectFieldAndMarkGenerated(typeNode, fieldDecl);
        return true;
    }

    /* loaded from: lombok-1.16.22.jar:lombok/javac/handlers/HandleLog$HandleCommonsLog.SCL.lombok */
    public static class HandleCommonsLog extends JavacAnnotationHandler<CommonsLog> {
        @Override // lombok.javac.JavacAnnotationHandler
        public void handle(AnnotationValues<CommonsLog> annotation, JCTree.JCAnnotation ast, JavacNode annotationNode) {
            HandlerUtil.handleFlagUsage(annotationNode, ConfigurationKeys.LOG_COMMONS_FLAG_USAGE, "@apachecommons.CommonsLog", ConfigurationKeys.LOG_ANY_FLAG_USAGE, "any @Log");
            HandleLog.processAnnotation(LoggingFramework.COMMONS, annotation, annotationNode, ((CommonsLog) annotation.getInstance()).topic());
        }
    }

    /* loaded from: lombok-1.16.22.jar:lombok/javac/handlers/HandleLog$HandleJulLog.SCL.lombok */
    public static class HandleJulLog extends JavacAnnotationHandler<Log> {
        @Override // lombok.javac.JavacAnnotationHandler
        public void handle(AnnotationValues<Log> annotation, JCTree.JCAnnotation ast, JavacNode annotationNode) {
            HandlerUtil.handleFlagUsage(annotationNode, ConfigurationKeys.LOG_JUL_FLAG_USAGE, "@java.Log", ConfigurationKeys.LOG_ANY_FLAG_USAGE, "any @Log");
            HandleLog.processAnnotation(LoggingFramework.JUL, annotation, annotationNode, ((Log) annotation.getInstance()).topic());
        }
    }

    /* loaded from: lombok-1.16.22.jar:lombok/javac/handlers/HandleLog$HandleLog4jLog.SCL.lombok */
    public static class HandleLog4jLog extends JavacAnnotationHandler<Log4j> {
        @Override // lombok.javac.JavacAnnotationHandler
        public void handle(AnnotationValues<Log4j> annotation, JCTree.JCAnnotation ast, JavacNode annotationNode) {
            HandlerUtil.handleFlagUsage(annotationNode, ConfigurationKeys.LOG_LOG4J_FLAG_USAGE, "@Log4j", ConfigurationKeys.LOG_ANY_FLAG_USAGE, "any @Log");
            HandleLog.processAnnotation(LoggingFramework.LOG4J, annotation, annotationNode, ((Log4j) annotation.getInstance()).topic());
        }
    }

    /* loaded from: lombok-1.16.22.jar:lombok/javac/handlers/HandleLog$HandleLog4j2Log.SCL.lombok */
    public static class HandleLog4j2Log extends JavacAnnotationHandler<Log4j2> {
        @Override // lombok.javac.JavacAnnotationHandler
        public void handle(AnnotationValues<Log4j2> annotation, JCTree.JCAnnotation ast, JavacNode annotationNode) {
            HandlerUtil.handleFlagUsage(annotationNode, ConfigurationKeys.LOG_LOG4J2_FLAG_USAGE, "@Log4j2", ConfigurationKeys.LOG_ANY_FLAG_USAGE, "any @Log");
            HandleLog.processAnnotation(LoggingFramework.LOG4J2, annotation, annotationNode, ((Log4j2) annotation.getInstance()).topic());
        }
    }

    /* loaded from: lombok-1.16.22.jar:lombok/javac/handlers/HandleLog$HandleSlf4jLog.SCL.lombok */
    public static class HandleSlf4jLog extends JavacAnnotationHandler<Slf4j> {
        @Override // lombok.javac.JavacAnnotationHandler
        public void handle(AnnotationValues<Slf4j> annotation, JCTree.JCAnnotation ast, JavacNode annotationNode) {
            HandlerUtil.handleFlagUsage(annotationNode, ConfigurationKeys.LOG_SLF4J_FLAG_USAGE, "@Slf4j", ConfigurationKeys.LOG_ANY_FLAG_USAGE, "any @Log");
            HandleLog.processAnnotation(LoggingFramework.SLF4J, annotation, annotationNode, ((Slf4j) annotation.getInstance()).topic());
        }
    }

    /* loaded from: lombok-1.16.22.jar:lombok/javac/handlers/HandleLog$HandleXSlf4jLog.SCL.lombok */
    public static class HandleXSlf4jLog extends JavacAnnotationHandler<XSlf4j> {
        @Override // lombok.javac.JavacAnnotationHandler
        public void handle(AnnotationValues<XSlf4j> annotation, JCTree.JCAnnotation ast, JavacNode annotationNode) {
            HandlerUtil.handleFlagUsage(annotationNode, ConfigurationKeys.LOG_XSLF4J_FLAG_USAGE, "@XSlf4j", ConfigurationKeys.LOG_ANY_FLAG_USAGE, "any @Log");
            HandleLog.processAnnotation(LoggingFramework.XSLF4J, annotation, annotationNode, ((XSlf4j) annotation.getInstance()).topic());
        }
    }

    /* loaded from: lombok-1.16.22.jar:lombok/javac/handlers/HandleLog$HandleJBossLog.SCL.lombok */
    public static class HandleJBossLog extends JavacAnnotationHandler<JBossLog> {
        @Override // lombok.javac.JavacAnnotationHandler
        public void handle(AnnotationValues<JBossLog> annotation, JCTree.JCAnnotation ast, JavacNode annotationNode) {
            HandlerUtil.handleFlagUsage(annotationNode, ConfigurationKeys.LOG_JBOSSLOG_FLAG_USAGE, "@JBossLog", ConfigurationKeys.LOG_ANY_FLAG_USAGE, "any @Log");
            HandleLog.processAnnotation(LoggingFramework.JBOSSLOG, annotation, annotationNode, ((JBossLog) annotation.getInstance()).topic());
        }
    }

    /* loaded from: lombok-1.16.22.jar:lombok/javac/handlers/HandleLog$LoggingFramework.SCL.lombok */
    enum LoggingFramework {
        COMMONS(CommonsLog.class, "org.apache.commons.logging.Log", "org.apache.commons.logging.LogFactory.getLog"),
        JUL(Log.class, "java.util.logging.Logger", "java.util.logging.Logger.getLogger") { // from class: lombok.javac.handlers.HandleLog.LoggingFramework.1
            @Override // lombok.javac.handlers.HandleLog.LoggingFramework
            public JCTree.JCExpression createFactoryParameter(JavacNode typeNode, JCTree.JCFieldAccess loggingType) {
                JavacTreeMaker maker = typeNode.getTreeMaker();
                return maker.Apply(List.nil(), maker.Select(loggingType, typeNode.toName("getName")), List.nil());
            }
        },
        LOG4J(Log4j.class, "org.apache.log4j.Logger", "org.apache.log4j.Logger.getLogger"),
        LOG4J2(Log4j2.class, "org.apache.logging.log4j.Logger", "org.apache.logging.log4j.LogManager.getLogger"),
        SLF4J(Slf4j.class, "org.slf4j.Logger", "org.slf4j.LoggerFactory.getLogger"),
        XSLF4J(XSlf4j.class, "org.slf4j.ext.XLogger", "org.slf4j.ext.XLoggerFactory.getXLogger"),
        JBOSSLOG(JBossLog.class, "org.jboss.logging.Logger", "org.jboss.logging.Logger.getLogger");

        private final Class<? extends Annotation> annotationClass;
        private final String loggerTypeName;
        private final String loggerFactoryName;

        LoggingFramework(Class cls, String loggerTypeName, String loggerFactoryName) {
            this.annotationClass = cls;
            this.loggerTypeName = loggerTypeName;
            this.loggerFactoryName = loggerFactoryName;
        }

        final Class<? extends Annotation> getAnnotationClass() {
            return this.annotationClass;
        }

        final String getLoggerTypeName() {
            return this.loggerTypeName;
        }

        final String getLoggerFactoryMethodName() {
            return this.loggerFactoryName;
        }

        JCTree.JCExpression createFactoryParameter(JavacNode typeNode, JCTree.JCFieldAccess loggingType) {
            return loggingType;
        }
    }
}
