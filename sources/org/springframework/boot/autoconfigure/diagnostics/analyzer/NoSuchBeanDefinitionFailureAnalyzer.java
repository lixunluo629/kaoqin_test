package org.springframework.boot.autoconfigure.diagnostics.analyzer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.autoconfigure.condition.ConditionEvaluationReport;
import org.springframework.boot.autoconfigure.condition.ConditionOutcome;
import org.springframework.boot.diagnostics.FailureAnalysis;
import org.springframework.boot.diagnostics.analyzer.AbstractInjectionFailureAnalyzer;
import org.springframework.context.annotation.Bean;
import org.springframework.core.ResolvableType;
import org.springframework.core.type.MethodMetadata;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

/* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/diagnostics/analyzer/NoSuchBeanDefinitionFailureAnalyzer.class */
class NoSuchBeanDefinitionFailureAnalyzer extends AbstractInjectionFailureAnalyzer<NoSuchBeanDefinitionException> implements BeanFactoryAware {
    private ConfigurableListableBeanFactory beanFactory;
    private MetadataReaderFactory metadataReaderFactory;
    private ConditionEvaluationReport report;

    NoSuchBeanDefinitionFailureAnalyzer() {
    }

    @Override // org.springframework.beans.factory.BeanFactoryAware
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        Assert.isInstanceOf(ConfigurableListableBeanFactory.class, beanFactory);
        this.beanFactory = (ConfigurableListableBeanFactory) beanFactory;
        this.metadataReaderFactory = new CachingMetadataReaderFactory(this.beanFactory.getBeanClassLoader());
        this.report = ConditionEvaluationReport.get(this.beanFactory);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.springframework.boot.diagnostics.analyzer.AbstractInjectionFailureAnalyzer
    public FailureAnalysis analyze(Throwable rootFailure, NoSuchBeanDefinitionException cause, String description) {
        if (cause.getNumberOfBeansFound() != 0) {
            return null;
        }
        List<AutoConfigurationResult> autoConfigurationResults = getAutoConfigurationResults(cause);
        StringBuilder message = new StringBuilder();
        Object[] objArr = new Object[2];
        objArr[0] = description != null ? description : "A component";
        objArr[1] = getBeanDescription(cause);
        message.append(String.format("%s required %s that could not be found.%n", objArr));
        if (!autoConfigurationResults.isEmpty()) {
            for (AutoConfigurationResult provider : autoConfigurationResults) {
                message.append(String.format("\t- %s%n", provider));
            }
        }
        Object[] objArr2 = new Object[2];
        objArr2[0] = !autoConfigurationResults.isEmpty() ? "revisiting the conditions above or defining" : "defining";
        objArr2[1] = getBeanDescription(cause);
        String action = String.format("Consider %s %s in your configuration.", objArr2);
        return new FailureAnalysis(message.toString(), action, cause);
    }

    private String getBeanDescription(NoSuchBeanDefinitionException cause) {
        if (cause.getResolvableType() != null) {
            Class<?> type = extractBeanType(cause.getResolvableType());
            return "a bean of type '" + type.getName() + "'";
        }
        return "a bean named '" + cause.getBeanName() + "'";
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Class<?> extractBeanType(ResolvableType resolvableType) {
        ResolvableType collectionType = resolvableType.asCollection();
        if (!collectionType.equals(ResolvableType.NONE)) {
            return collectionType.getGeneric(0).getRawClass();
        }
        ResolvableType mapType = resolvableType.asMap();
        if (!mapType.equals(ResolvableType.NONE)) {
            return mapType.getGeneric(1).getRawClass();
        }
        return resolvableType.getRawClass();
    }

    private List<AutoConfigurationResult> getAutoConfigurationResults(NoSuchBeanDefinitionException cause) {
        List<AutoConfigurationResult> results = new ArrayList<>();
        collectReportedConditionOutcomes(cause, results);
        collectExcludedAutoConfiguration(cause, results);
        return results;
    }

    private void collectReportedConditionOutcomes(NoSuchBeanDefinitionException cause, List<AutoConfigurationResult> results) {
        for (Map.Entry<String, ConditionEvaluationReport.ConditionAndOutcomes> entry : this.report.getConditionAndOutcomesBySource().entrySet()) {
            Source source = new Source(entry.getKey());
            ConditionEvaluationReport.ConditionAndOutcomes conditionAndOutcomes = entry.getValue();
            if (!conditionAndOutcomes.isFullMatch()) {
                BeanMethods methods = new BeanMethods(source, cause);
                Iterator<ConditionEvaluationReport.ConditionAndOutcome> it = conditionAndOutcomes.iterator();
                while (it.hasNext()) {
                    ConditionEvaluationReport.ConditionAndOutcome conditionAndOutcome = it.next();
                    if (!conditionAndOutcome.getOutcome().isMatch()) {
                        Iterator<MethodMetadata> it2 = methods.iterator();
                        while (it2.hasNext()) {
                            MethodMetadata method = it2.next();
                            results.add(new AutoConfigurationResult(method, conditionAndOutcome.getOutcome(), source.isMethod()));
                        }
                    }
                }
            }
        }
    }

    private void collectExcludedAutoConfiguration(NoSuchBeanDefinitionException cause, List<AutoConfigurationResult> results) {
        for (String excludedClass : this.report.getExclusions()) {
            Source source = new Source(excludedClass);
            BeanMethods methods = new BeanMethods(source, cause);
            Iterator<MethodMetadata> it = methods.iterator();
            while (it.hasNext()) {
                MethodMetadata method = it.next();
                String message = String.format("auto-configuration '%s' was excluded", ClassUtils.getShortName(excludedClass));
                results.add(new AutoConfigurationResult(method, new ConditionOutcome(false, message), false));
            }
        }
    }

    /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/diagnostics/analyzer/NoSuchBeanDefinitionFailureAnalyzer$Source.class */
    private class Source {
        private final String className;
        private final String methodName;

        Source(String source) {
            String[] tokens = source.split("#");
            this.className = tokens.length > 1 ? tokens[0] : source;
            this.methodName = tokens.length != 2 ? null : tokens[1];
        }

        public String getClassName() {
            return this.className;
        }

        public String getMethodName() {
            return this.methodName;
        }

        public boolean isMethod() {
            return this.methodName != null;
        }
    }

    /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/diagnostics/analyzer/NoSuchBeanDefinitionFailureAnalyzer$BeanMethods.class */
    private class BeanMethods implements Iterable<MethodMetadata> {
        private final List<MethodMetadata> methods;

        BeanMethods(Source source, NoSuchBeanDefinitionException cause) {
            this.methods = findBeanMethods(source, cause);
        }

        private List<MethodMetadata> findBeanMethods(Source source, NoSuchBeanDefinitionException cause) {
            try {
                MetadataReader classMetadata = NoSuchBeanDefinitionFailureAnalyzer.this.metadataReaderFactory.getMetadataReader(source.getClassName());
                Set<MethodMetadata> candidates = classMetadata.getAnnotationMetadata().getAnnotatedMethods(Bean.class.getName());
                List<MethodMetadata> result = new ArrayList<>();
                for (MethodMetadata candidate : candidates) {
                    if (isMatch(candidate, source, cause)) {
                        result.add(candidate);
                    }
                }
                return Collections.unmodifiableList(result);
            } catch (Exception e) {
                return Collections.emptyList();
            }
        }

        private boolean isMatch(MethodMetadata candidate, Source source, NoSuchBeanDefinitionException cause) {
            if (source.getMethodName() != null && !source.getMethodName().equals(candidate.getMethodName())) {
                return false;
            }
            String name = cause.getBeanName();
            ResolvableType resolvableType = cause.getResolvableType();
            return (name != null && hasName(candidate, name)) || (resolvableType != null && hasType(candidate, NoSuchBeanDefinitionFailureAnalyzer.this.extractBeanType(resolvableType)));
        }

        private boolean hasName(MethodMetadata methodMetadata, String name) {
            Map<String, Object> attributes = methodMetadata.getAnnotationAttributes(Bean.class.getName());
            String[] candidates = attributes != null ? (String[]) attributes.get("name") : null;
            if (candidates != null) {
                for (String candidate : candidates) {
                    if (candidate.equals(name)) {
                        return true;
                    }
                }
                return false;
            }
            return methodMetadata.getMethodName().equals(name);
        }

        private boolean hasType(MethodMetadata candidate, Class<?> type) {
            String returnTypeName = candidate.getReturnTypeName();
            if (type.getName().equals(returnTypeName)) {
                return true;
            }
            try {
                Class<?> returnType = ClassUtils.forName(returnTypeName, NoSuchBeanDefinitionFailureAnalyzer.this.beanFactory.getBeanClassLoader());
                return type.isAssignableFrom(returnType);
            } catch (Throwable th) {
                return false;
            }
        }

        @Override // java.lang.Iterable
        public Iterator<MethodMetadata> iterator() {
            return this.methods.iterator();
        }
    }

    /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/diagnostics/analyzer/NoSuchBeanDefinitionFailureAnalyzer$AutoConfigurationResult.class */
    private class AutoConfigurationResult {
        private final MethodMetadata methodMetadata;
        private final ConditionOutcome conditionOutcome;
        private final boolean methodEvaluated;

        AutoConfigurationResult(MethodMetadata methodMetadata, ConditionOutcome conditionOutcome, boolean methodEvaluated) {
            this.methodMetadata = methodMetadata;
            this.conditionOutcome = conditionOutcome;
            this.methodEvaluated = methodEvaluated;
        }

        public String toString() {
            if (this.methodEvaluated) {
                return String.format("Bean method '%s' in '%s' not loaded because %s", this.methodMetadata.getMethodName(), ClassUtils.getShortName(this.methodMetadata.getDeclaringClassName()), this.conditionOutcome.getMessage());
            }
            return String.format("Bean method '%s' not loaded because %s", this.methodMetadata.getMethodName(), this.conditionOutcome.getMessage());
        }
    }
}
