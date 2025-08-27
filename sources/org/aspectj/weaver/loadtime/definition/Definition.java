package org.aspectj.weaver.loadtime.definition;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/loadtime/definition/Definition.class */
public class Definition {
    private final StringBuffer weaverOptions = new StringBuffer();
    private boolean dumpBefore = false;
    private boolean perClassloaderDumpDir = false;
    private final List<String> dumpPatterns = new ArrayList();
    private final List<String> includePatterns = new ArrayList();
    private final List<String> excludePatterns = new ArrayList();
    private final List<String> aspectClassNames = new ArrayList();
    private final List<String> aspectExcludePatterns = new ArrayList();
    private final List<String> aspectIncludePatterns = new ArrayList();
    private final List<ConcreteAspect> concreteAspects = new ArrayList();
    private final Map<String, String> scopedAspects = new HashMap();
    private final Map<String, String> requiredTypesForAspects = new HashMap();

    /* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/loadtime/definition/Definition$AdviceKind.class */
    public enum AdviceKind {
        Before,
        After,
        AfterReturning,
        AfterThrowing,
        Around
    }

    /* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/loadtime/definition/Definition$DeclareAnnotationKind.class */
    public enum DeclareAnnotationKind {
        Method,
        Field,
        Type
    }

    public String getWeaverOptions() {
        return this.weaverOptions.toString();
    }

    public List<String> getDumpPatterns() {
        return this.dumpPatterns;
    }

    public void setDumpBefore(boolean b) {
        this.dumpBefore = b;
    }

    public boolean shouldDumpBefore() {
        return this.dumpBefore;
    }

    public void setCreateDumpDirPerClassloader(boolean b) {
        this.perClassloaderDumpDir = b;
    }

    public boolean createDumpDirPerClassloader() {
        return this.perClassloaderDumpDir;
    }

    public List<String> getIncludePatterns() {
        return this.includePatterns;
    }

    public List<String> getExcludePatterns() {
        return this.excludePatterns;
    }

    public List<String> getAspectClassNames() {
        return this.aspectClassNames;
    }

    public List<String> getAspectExcludePatterns() {
        return this.aspectExcludePatterns;
    }

    public List<String> getAspectIncludePatterns() {
        return this.aspectIncludePatterns;
    }

    public List<ConcreteAspect> getConcreteAspects() {
        return this.concreteAspects;
    }

    /* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/loadtime/definition/Definition$ConcreteAspect.class */
    public static class ConcreteAspect {
        public final String name;
        public final String extend;
        public final String precedence;
        public final List<Pointcut> pointcuts;
        public final List<DeclareAnnotation> declareAnnotations;
        public final List<PointcutAndAdvice> pointcutsAndAdvice;
        public final String perclause;
        public List<DeclareErrorOrWarning> deows;

        public ConcreteAspect(String name, String extend) {
            this(name, extend, null, null);
        }

        public ConcreteAspect(String name, String extend, String precedence, String perclause) {
            this.name = name;
            if (extend == null || extend.length() == 0) {
                this.extend = null;
                if (precedence == null || precedence.length() == 0) {
                }
            } else {
                this.extend = extend;
            }
            this.precedence = precedence;
            this.pointcuts = new ArrayList();
            this.declareAnnotations = new ArrayList();
            this.pointcutsAndAdvice = new ArrayList();
            this.deows = new ArrayList();
            this.perclause = perclause;
        }
    }

    /* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/loadtime/definition/Definition$Pointcut.class */
    public static class Pointcut {
        public final String name;
        public final String expression;

        public Pointcut(String name, String expression) {
            this.name = name;
            this.expression = expression;
        }
    }

    /* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/loadtime/definition/Definition$DeclareAnnotation.class */
    public static class DeclareAnnotation {
        public final DeclareAnnotationKind declareAnnotationKind;
        public final String pattern;
        public final String annotation;

        public DeclareAnnotation(DeclareAnnotationKind kind, String pattern, String annotation) {
            this.declareAnnotationKind = kind;
            this.pattern = pattern;
            this.annotation = annotation;
        }
    }

    /* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/loadtime/definition/Definition$PointcutAndAdvice.class */
    public static class PointcutAndAdvice {
        public final AdviceKind adviceKind;
        public final String pointcut;
        public final String adviceClass;
        public final String adviceMethod;

        public PointcutAndAdvice(AdviceKind adviceKind, String pointcut, String adviceClass, String adviceMethod) {
            this.adviceKind = adviceKind;
            this.pointcut = pointcut;
            this.adviceClass = adviceClass;
            this.adviceMethod = adviceMethod;
        }
    }

    /* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/loadtime/definition/Definition$DeclareErrorOrWarning.class */
    public static class DeclareErrorOrWarning {
        public final boolean isError;
        public final String pointcut;
        public final String message;

        public DeclareErrorOrWarning(boolean isError, String pointcut, String message) {
            this.isError = isError;
            this.pointcut = pointcut;
            this.message = message;
        }
    }

    public void appendWeaverOptions(String option) {
        this.weaverOptions.append(option.trim()).append(' ');
    }

    public void addScopedAspect(String name, String scopePattern) {
        this.scopedAspects.put(name, scopePattern);
    }

    public String getScopeForAspect(String name) {
        return this.scopedAspects.get(name);
    }

    public void setAspectRequires(String name, String requiredType) {
        this.requiredTypesForAspects.put(name, requiredType);
    }

    public String getAspectRequires(String name) {
        return this.requiredTypesForAspects.get(name);
    }
}
