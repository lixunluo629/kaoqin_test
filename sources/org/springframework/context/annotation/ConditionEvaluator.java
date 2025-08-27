package org.springframework.context.annotation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ConfigurationCondition;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.core.env.Environment;
import org.springframework.core.env.EnvironmentCapable;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.ClassUtils;
import org.springframework.util.MultiValueMap;

/* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/context/annotation/ConditionEvaluator.class */
class ConditionEvaluator {
    private final ConditionContextImpl context;

    public ConditionEvaluator(BeanDefinitionRegistry registry, Environment environment, ResourceLoader resourceLoader) {
        this.context = new ConditionContextImpl(registry, environment, resourceLoader);
    }

    public boolean shouldSkip(AnnotatedTypeMetadata metadata) {
        return shouldSkip(metadata, null);
    }

    public boolean shouldSkip(AnnotatedTypeMetadata metadata, ConfigurationCondition.ConfigurationPhase phase) {
        if (metadata == null || !metadata.isAnnotated(Conditional.class.getName())) {
            return false;
        }
        if (phase == null) {
            if ((metadata instanceof AnnotationMetadata) && ConfigurationClassUtils.isConfigurationCandidate((AnnotationMetadata) metadata)) {
                return shouldSkip(metadata, ConfigurationCondition.ConfigurationPhase.PARSE_CONFIGURATION);
            }
            return shouldSkip(metadata, ConfigurationCondition.ConfigurationPhase.REGISTER_BEAN);
        }
        List<Condition> conditions = new ArrayList<>();
        for (String[] conditionClasses : getConditionClasses(metadata)) {
            for (String conditionClass : conditionClasses) {
                conditions.add(getCondition(conditionClass, this.context.getClassLoader()));
            }
        }
        AnnotationAwareOrderComparator.sort(conditions);
        for (Condition condition : conditions) {
            ConfigurationCondition.ConfigurationPhase requiredPhase = null;
            if (condition instanceof ConfigurationCondition) {
                requiredPhase = ((ConfigurationCondition) condition).getConfigurationPhase();
            }
            if (requiredPhase == null || requiredPhase == phase) {
                if (!condition.matches(this.context, metadata)) {
                    return true;
                }
            }
        }
        return false;
    }

    private List<String[]> getConditionClasses(AnnotatedTypeMetadata metadata) {
        MultiValueMap<String, Object> attributes = metadata.getAllAnnotationAttributes(Conditional.class.getName(), true);
        Object values = attributes != null ? (List) attributes.get("value") : null;
        return (List) (values != null ? values : Collections.emptyList());
    }

    private Condition getCondition(String conditionClassName, ClassLoader classloader) throws IllegalArgumentException {
        Class<?> conditionClass = ClassUtils.resolveClassName(conditionClassName, classloader);
        return (Condition) BeanUtils.instantiateClass(conditionClass);
    }

    /* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/context/annotation/ConditionEvaluator$ConditionContextImpl.class */
    private static class ConditionContextImpl implements ConditionContext {
        private final BeanDefinitionRegistry registry;
        private final ConfigurableListableBeanFactory beanFactory;
        private final Environment environment;
        private final ResourceLoader resourceLoader;

        public ConditionContextImpl(BeanDefinitionRegistry registry, Environment environment, ResourceLoader resourceLoader) {
            this.registry = registry;
            this.beanFactory = deduceBeanFactory(registry);
            this.environment = environment != null ? environment : deduceEnvironment(registry);
            this.resourceLoader = resourceLoader != null ? resourceLoader : deduceResourceLoader(registry);
        }

        private ConfigurableListableBeanFactory deduceBeanFactory(BeanDefinitionRegistry source) {
            if (source instanceof ConfigurableListableBeanFactory) {
                return (ConfigurableListableBeanFactory) source;
            }
            if (source instanceof ConfigurableApplicationContext) {
                return ((ConfigurableApplicationContext) source).getBeanFactory();
            }
            return null;
        }

        private Environment deduceEnvironment(BeanDefinitionRegistry source) {
            if (source instanceof EnvironmentCapable) {
                return ((EnvironmentCapable) source).getEnvironment();
            }
            return null;
        }

        private ResourceLoader deduceResourceLoader(BeanDefinitionRegistry source) {
            if (source instanceof ResourceLoader) {
                return (ResourceLoader) source;
            }
            return null;
        }

        @Override // org.springframework.context.annotation.ConditionContext
        public BeanDefinitionRegistry getRegistry() {
            return this.registry;
        }

        @Override // org.springframework.context.annotation.ConditionContext
        public ConfigurableListableBeanFactory getBeanFactory() {
            return this.beanFactory;
        }

        @Override // org.springframework.context.annotation.ConditionContext
        public Environment getEnvironment() {
            return this.environment;
        }

        @Override // org.springframework.context.annotation.ConditionContext
        public ResourceLoader getResourceLoader() {
            return this.resourceLoader;
        }

        @Override // org.springframework.context.annotation.ConditionContext
        public ClassLoader getClassLoader() {
            if (this.resourceLoader != null) {
                return this.resourceLoader.getClassLoader();
            }
            if (this.beanFactory != null) {
                return this.beanFactory.getBeanClassLoader();
            }
            return null;
        }
    }
}
