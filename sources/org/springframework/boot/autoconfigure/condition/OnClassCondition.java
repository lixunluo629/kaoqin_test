package org.springframework.boot.autoconfigure.condition;

import java.lang.annotation.Annotation;
import java.security.AccessControlException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.autoconfigure.AutoConfigurationImportFilter;
import org.springframework.boot.autoconfigure.AutoConfigurationMetadata;
import org.springframework.boot.autoconfigure.condition.ConditionMessage;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.annotation.Order;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.util.ClassUtils;
import org.springframework.util.MultiValueMap;

@Order(Integer.MIN_VALUE)
/* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/condition/OnClassCondition.class */
class OnClassCondition extends SpringBootCondition implements AutoConfigurationImportFilter, BeanFactoryAware, BeanClassLoaderAware {
    private BeanFactory beanFactory;
    private ClassLoader beanClassLoader;

    /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/condition/OnClassCondition$OutcomesResolver.class */
    private interface OutcomesResolver {
        ConditionOutcome[] resolveOutcomes();
    }

    OnClassCondition() {
    }

    @Override // org.springframework.boot.autoconfigure.AutoConfigurationImportFilter
    public boolean[] match(String[] autoConfigurationClasses, AutoConfigurationMetadata autoConfigurationMetadata) {
        ConditionEvaluationReport report = getConditionEvaluationReport();
        ConditionOutcome[] outcomes = getOutcomes(autoConfigurationClasses, autoConfigurationMetadata);
        boolean[] match = new boolean[outcomes.length];
        for (int i = 0; i < outcomes.length; i++) {
            match[i] = outcomes[i] == null || outcomes[i].isMatch();
            if (!match[i] && outcomes[i] != null) {
                logOutcome(autoConfigurationClasses[i], outcomes[i]);
                if (report != null) {
                    report.recordConditionEvaluation(autoConfigurationClasses[i], this, outcomes[i]);
                }
            }
        }
        return match;
    }

    private ConditionEvaluationReport getConditionEvaluationReport() {
        if (this.beanFactory != null && (this.beanFactory instanceof ConfigurableBeanFactory)) {
            return ConditionEvaluationReport.get((ConfigurableListableBeanFactory) this.beanFactory);
        }
        return null;
    }

    private ConditionOutcome[] getOutcomes(String[] autoConfigurationClasses, AutoConfigurationMetadata autoConfigurationMetadata) {
        int split = autoConfigurationClasses.length / 2;
        OutcomesResolver firstHalfResolver = createOutcomesResolver(autoConfigurationClasses, 0, split, autoConfigurationMetadata);
        OutcomesResolver secondHalfResolver = new StandardOutcomesResolver(autoConfigurationClasses, split, autoConfigurationClasses.length, autoConfigurationMetadata, this.beanClassLoader);
        ConditionOutcome[] secondHalf = secondHalfResolver.resolveOutcomes();
        ConditionOutcome[] firstHalf = firstHalfResolver.resolveOutcomes();
        ConditionOutcome[] outcomes = new ConditionOutcome[autoConfigurationClasses.length];
        System.arraycopy(firstHalf, 0, outcomes, 0, firstHalf.length);
        System.arraycopy(secondHalf, 0, outcomes, split, secondHalf.length);
        return outcomes;
    }

    private OutcomesResolver createOutcomesResolver(String[] autoConfigurationClasses, int start, int end, AutoConfigurationMetadata autoConfigurationMetadata) {
        OutcomesResolver outcomesResolver = new StandardOutcomesResolver(autoConfigurationClasses, start, end, autoConfigurationMetadata, this.beanClassLoader);
        try {
            return new ThreadedOutcomesResolver(outcomesResolver);
        } catch (AccessControlException e) {
            return outcomesResolver;
        }
    }

    @Override // org.springframework.boot.autoconfigure.condition.SpringBootCondition
    public ConditionOutcome getMatchOutcome(ConditionContext context, AnnotatedTypeMetadata metadata) {
        ClassLoader classLoader = context.getClassLoader();
        ConditionMessage matchMessage = ConditionMessage.empty();
        List<String> onClasses = getCandidates(metadata, ConditionalOnClass.class);
        if (onClasses != null) {
            List<String> missing = getMatches(onClasses, MatchType.MISSING, classLoader);
            if (!missing.isEmpty()) {
                return ConditionOutcome.noMatch(ConditionMessage.forCondition((Class<? extends Annotation>) ConditionalOnClass.class, new Object[0]).didNotFind("required class", "required classes").items(ConditionMessage.Style.QUOTE, missing));
            }
            matchMessage = matchMessage.andCondition(ConditionalOnClass.class, new Object[0]).found("required class", "required classes").items(ConditionMessage.Style.QUOTE, getMatches(onClasses, MatchType.PRESENT, classLoader));
        }
        List<String> onMissingClasses = getCandidates(metadata, ConditionalOnMissingClass.class);
        if (onMissingClasses != null) {
            List<String> present = getMatches(onMissingClasses, MatchType.PRESENT, classLoader);
            if (!present.isEmpty()) {
                return ConditionOutcome.noMatch(ConditionMessage.forCondition((Class<? extends Annotation>) ConditionalOnMissingClass.class, new Object[0]).found("unwanted class", "unwanted classes").items(ConditionMessage.Style.QUOTE, present));
            }
            matchMessage = matchMessage.andCondition(ConditionalOnMissingClass.class, new Object[0]).didNotFind("unwanted class", "unwanted classes").items(ConditionMessage.Style.QUOTE, getMatches(onMissingClasses, MatchType.MISSING, classLoader));
        }
        return ConditionOutcome.match(matchMessage);
    }

    private List<String> getCandidates(AnnotatedTypeMetadata metadata, Class<?> annotationType) {
        MultiValueMap<String, Object> attributes = metadata.getAllAnnotationAttributes(annotationType.getName(), true);
        List<String> candidates = new ArrayList<>();
        if (attributes == null) {
            return Collections.emptyList();
        }
        addAll(candidates, (List) attributes.get("value"));
        addAll(candidates, (List) attributes.get("name"));
        return candidates;
    }

    private void addAll(List<String> list, List<Object> itemsToAdd) {
        if (itemsToAdd != null) {
            for (Object item : itemsToAdd) {
                Collections.addAll(list, (String[]) item);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public List<String> getMatches(Collection<String> candidates, MatchType matchType, ClassLoader classLoader) {
        List<String> matches = new ArrayList<>(candidates.size());
        for (String candidate : candidates) {
            if (matchType.matches(candidate, classLoader)) {
                matches.add(candidate);
            }
        }
        return matches;
    }

    @Override // org.springframework.beans.factory.BeanFactoryAware
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    @Override // org.springframework.beans.factory.BeanClassLoaderAware
    public void setBeanClassLoader(ClassLoader classLoader) {
        this.beanClassLoader = classLoader;
    }

    /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/condition/OnClassCondition$MatchType.class */
    private enum MatchType {
        PRESENT { // from class: org.springframework.boot.autoconfigure.condition.OnClassCondition.MatchType.1
            @Override // org.springframework.boot.autoconfigure.condition.OnClassCondition.MatchType
            public boolean matches(String className, ClassLoader classLoader) {
                return MatchType.isPresent(className, classLoader);
            }
        },
        MISSING { // from class: org.springframework.boot.autoconfigure.condition.OnClassCondition.MatchType.2
            @Override // org.springframework.boot.autoconfigure.condition.OnClassCondition.MatchType
            public boolean matches(String className, ClassLoader classLoader) {
                return !MatchType.isPresent(className, classLoader);
            }
        };

        public abstract boolean matches(String str, ClassLoader classLoader);

        /* JADX INFO: Access modifiers changed from: private */
        public static boolean isPresent(String className, ClassLoader classLoader) {
            if (classLoader == null) {
                classLoader = ClassUtils.getDefaultClassLoader();
            }
            try {
                forName(className, classLoader);
                return true;
            } catch (Throwable th) {
                return false;
            }
        }

        private static Class<?> forName(String className, ClassLoader classLoader) throws ClassNotFoundException {
            if (classLoader != null) {
                return classLoader.loadClass(className);
            }
            return Class.forName(className);
        }
    }

    /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/condition/OnClassCondition$ThreadedOutcomesResolver.class */
    private static final class ThreadedOutcomesResolver implements OutcomesResolver {
        private final Thread thread;
        private volatile ConditionOutcome[] outcomes;

        private ThreadedOutcomesResolver(final OutcomesResolver outcomesResolver) {
            this.thread = new Thread(new Runnable() { // from class: org.springframework.boot.autoconfigure.condition.OnClassCondition.ThreadedOutcomesResolver.1
                @Override // java.lang.Runnable
                public void run() {
                    ThreadedOutcomesResolver.this.outcomes = outcomesResolver.resolveOutcomes();
                }
            });
            this.thread.start();
        }

        @Override // org.springframework.boot.autoconfigure.condition.OnClassCondition.OutcomesResolver
        public ConditionOutcome[] resolveOutcomes() throws InterruptedException {
            try {
                this.thread.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            return this.outcomes;
        }
    }

    /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/condition/OnClassCondition$StandardOutcomesResolver.class */
    private final class StandardOutcomesResolver implements OutcomesResolver {
        private final String[] autoConfigurationClasses;
        private final int start;
        private final int end;
        private final AutoConfigurationMetadata autoConfigurationMetadata;
        private final ClassLoader beanClassLoader;

        private StandardOutcomesResolver(String[] autoConfigurationClasses, int start, int end, AutoConfigurationMetadata autoConfigurationMetadata, ClassLoader beanClassLoader) {
            this.autoConfigurationClasses = autoConfigurationClasses;
            this.start = start;
            this.end = end;
            this.autoConfigurationMetadata = autoConfigurationMetadata;
            this.beanClassLoader = beanClassLoader;
        }

        @Override // org.springframework.boot.autoconfigure.condition.OnClassCondition.OutcomesResolver
        public ConditionOutcome[] resolveOutcomes() {
            return getOutcomes(this.autoConfigurationClasses, this.start, this.end, this.autoConfigurationMetadata);
        }

        private ConditionOutcome[] getOutcomes(String[] autoConfigurationClasses, int start, int end, AutoConfigurationMetadata autoConfigurationMetadata) {
            ConditionOutcome[] outcomes = new ConditionOutcome[end - start];
            for (int i = start; i < end; i++) {
                String autoConfigurationClass = autoConfigurationClasses[i];
                Set<String> candidates = autoConfigurationMetadata.getSet(autoConfigurationClass, "ConditionalOnClass");
                if (candidates != null) {
                    outcomes[i - start] = getOutcome(candidates);
                }
            }
            return outcomes;
        }

        private ConditionOutcome getOutcome(Set<String> candidates) {
            try {
                List<String> missing = OnClassCondition.this.getMatches(candidates, MatchType.MISSING, this.beanClassLoader);
                if (!missing.isEmpty()) {
                    return ConditionOutcome.noMatch(ConditionMessage.forCondition((Class<? extends Annotation>) ConditionalOnClass.class, new Object[0]).didNotFind("required class", "required classes").items(ConditionMessage.Style.QUOTE, missing));
                }
                return null;
            } catch (Exception e) {
                return null;
            }
        }
    }
}
