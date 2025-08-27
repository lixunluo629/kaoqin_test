package org.springframework.scheduling.annotation;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.aop.framework.AopInfrastructureBean;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.NoUniqueBeanDefinitionException;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.beans.factory.config.DestructionAwareBeanPostProcessor;
import org.springframework.beans.factory.config.NamedBeanHolder;
import org.springframework.beans.factory.support.MergedBeanDefinitionPostProcessor;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.EmbeddedValueResolverAware;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.MethodIntrospector;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.config.CronTask;
import org.springframework.scheduling.config.IntervalTask;
import org.springframework.scheduling.config.ScheduledTask;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.scheduling.support.ScheduledMethodRunnable;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.util.StringValueResolver;

/* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/scheduling/annotation/ScheduledAnnotationBeanPostProcessor.class */
public class ScheduledAnnotationBeanPostProcessor implements MergedBeanDefinitionPostProcessor, DestructionAwareBeanPostProcessor, Ordered, EmbeddedValueResolverAware, BeanNameAware, BeanFactoryAware, ApplicationContextAware, SmartInitializingSingleton, ApplicationListener<ContextRefreshedEvent>, DisposableBean {
    public static final String DEFAULT_TASK_SCHEDULER_BEAN_NAME = "taskScheduler";
    private Object scheduler;
    private StringValueResolver embeddedValueResolver;
    private String beanName;
    private BeanFactory beanFactory;
    private ApplicationContext applicationContext;
    protected final Log logger = LogFactory.getLog(getClass());
    private final ScheduledTaskRegistrar registrar = new ScheduledTaskRegistrar();
    private final Set<Class<?>> nonAnnotatedClasses = Collections.newSetFromMap(new ConcurrentHashMap(64));
    private final Map<Object, Set<ScheduledTask>> scheduledTasks = new IdentityHashMap(16);

    @Override // org.springframework.core.Ordered
    public int getOrder() {
        return Integer.MAX_VALUE;
    }

    public void setScheduler(Object scheduler) {
        this.scheduler = scheduler;
    }

    @Override // org.springframework.context.EmbeddedValueResolverAware
    public void setEmbeddedValueResolver(StringValueResolver resolver) {
        this.embeddedValueResolver = resolver;
    }

    @Override // org.springframework.beans.factory.BeanNameAware
    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

    @Override // org.springframework.beans.factory.BeanFactoryAware
    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    @Override // org.springframework.context.ApplicationContextAware
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
        if (this.beanFactory == null) {
            this.beanFactory = applicationContext;
        }
    }

    @Override // org.springframework.beans.factory.SmartInitializingSingleton
    public void afterSingletonsInstantiated() throws BeansException {
        this.nonAnnotatedClasses.clear();
        if (this.applicationContext == null) {
            finishRegistration();
        }
    }

    @Override // org.springframework.context.ApplicationListener
    public void onApplicationEvent(ContextRefreshedEvent event) throws BeansException {
        if (event.getApplicationContext() == this.applicationContext) {
            finishRegistration();
        }
    }

    private void finishRegistration() throws BeansException {
        if (this.scheduler != null) {
            this.registrar.setScheduler(this.scheduler);
        }
        if (this.beanFactory instanceof ListableBeanFactory) {
            Map<String, SchedulingConfigurer> beans = ((ListableBeanFactory) this.beanFactory).getBeansOfType(SchedulingConfigurer.class);
            List<SchedulingConfigurer> configurers = new ArrayList<>(beans.values());
            AnnotationAwareOrderComparator.sort(configurers);
            for (SchedulingConfigurer configurer : configurers) {
                configurer.configureTasks(this.registrar);
            }
        }
        if (this.registrar.hasTasks() && this.registrar.getScheduler() == null) {
            Assert.state(this.beanFactory != null, "BeanFactory must be set to find scheduler by type");
            try {
                this.registrar.setTaskScheduler((TaskScheduler) resolveSchedulerBean(TaskScheduler.class, false));
            } catch (NoUniqueBeanDefinitionException ex) {
                this.logger.debug("Could not find unique TaskScheduler bean", ex);
                try {
                    this.registrar.setTaskScheduler((TaskScheduler) resolveSchedulerBean(TaskScheduler.class, true));
                } catch (NoSuchBeanDefinitionException e) {
                    if (this.logger.isInfoEnabled()) {
                        this.logger.info("More than one TaskScheduler bean exists within the context, and none is named 'taskScheduler'. Mark one of them as primary or name it 'taskScheduler' (possibly as an alias); or implement the SchedulingConfigurer interface and call ScheduledTaskRegistrar#setScheduler explicitly within the configureTasks() callback: " + ex.getBeanNamesFound());
                    }
                }
            } catch (NoSuchBeanDefinitionException ex2) {
                this.logger.debug("Could not find default TaskScheduler bean", ex2);
                try {
                    this.registrar.setScheduler(resolveSchedulerBean(ScheduledExecutorService.class, false));
                } catch (NoUniqueBeanDefinitionException ex22) {
                    this.logger.debug("Could not find unique ScheduledExecutorService bean", ex22);
                    try {
                        this.registrar.setScheduler(resolveSchedulerBean(ScheduledExecutorService.class, true));
                    } catch (NoSuchBeanDefinitionException e2) {
                        if (this.logger.isInfoEnabled()) {
                            this.logger.info("More than one ScheduledExecutorService bean exists within the context, and none is named 'taskScheduler'. Mark one of them as primary or name it 'taskScheduler' (possibly as an alias); or implement the SchedulingConfigurer interface and call ScheduledTaskRegistrar#setScheduler explicitly within the configureTasks() callback: " + ex22.getBeanNamesFound());
                        }
                    }
                } catch (NoSuchBeanDefinitionException ex23) {
                    this.logger.debug("Could not find default ScheduledExecutorService bean", ex23);
                    this.logger.info("No TaskScheduler/ScheduledExecutorService bean found for scheduled processing");
                }
            }
        }
        this.registrar.afterPropertiesSet();
    }

    private <T> T resolveSchedulerBean(Class<T> cls, boolean z) throws BeansException {
        if (z) {
            T t = (T) this.beanFactory.getBean(DEFAULT_TASK_SCHEDULER_BEAN_NAME, cls);
            if (this.beanFactory instanceof ConfigurableBeanFactory) {
                ((ConfigurableBeanFactory) this.beanFactory).registerDependentBean(DEFAULT_TASK_SCHEDULER_BEAN_NAME, this.beanName);
            }
            return t;
        }
        if (this.beanFactory instanceof AutowireCapableBeanFactory) {
            NamedBeanHolder<T> namedBeanHolderResolveNamedBean = ((AutowireCapableBeanFactory) this.beanFactory).resolveNamedBean(cls);
            if (this.beanFactory instanceof ConfigurableBeanFactory) {
                ((ConfigurableBeanFactory) this.beanFactory).registerDependentBean(namedBeanHolderResolveNamedBean.getBeanName(), this.beanName);
            }
            return namedBeanHolderResolveNamedBean.getBeanInstance();
        }
        return (T) this.beanFactory.getBean(cls);
    }

    @Override // org.springframework.beans.factory.support.MergedBeanDefinitionPostProcessor
    public void postProcessMergedBeanDefinition(RootBeanDefinition beanDefinition, Class<?> beanType, String beanName) {
    }

    @Override // org.springframework.beans.factory.config.BeanPostProcessor
    public Object postProcessBeforeInitialization(Object bean, String beanName) {
        return bean;
    }

    @Override // org.springframework.beans.factory.config.BeanPostProcessor
    public Object postProcessAfterInitialization(Object bean, String beanName) throws NumberFormatException {
        if (bean instanceof AopInfrastructureBean) {
            return bean;
        }
        Class<?> targetClass = AopProxyUtils.ultimateTargetClass(bean);
        if (!this.nonAnnotatedClasses.contains(targetClass)) {
            Map<Method, Set<Scheduled>> annotatedMethods = MethodIntrospector.selectMethods(targetClass, new MethodIntrospector.MetadataLookup<Set<Scheduled>>() { // from class: org.springframework.scheduling.annotation.ScheduledAnnotationBeanPostProcessor.1
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // org.springframework.core.MethodIntrospector.MetadataLookup
                public Set<Scheduled> inspect(Method method) {
                    Set<Scheduled> scheduledMethods = AnnotatedElementUtils.getMergedRepeatableAnnotations(method, Scheduled.class, Schedules.class);
                    if (scheduledMethods.isEmpty()) {
                        return null;
                    }
                    return scheduledMethods;
                }
            });
            if (annotatedMethods.isEmpty()) {
                this.nonAnnotatedClasses.add(targetClass);
                if (this.logger.isTraceEnabled()) {
                    this.logger.trace("No @Scheduled annotations found on bean class: " + targetClass);
                }
            } else {
                for (Map.Entry<Method, Set<Scheduled>> entry : annotatedMethods.entrySet()) {
                    Method method = entry.getKey();
                    for (Scheduled scheduled : entry.getValue()) {
                        processScheduled(scheduled, method, bean);
                    }
                }
                if (this.logger.isDebugEnabled()) {
                    this.logger.debug(annotatedMethods.size() + " @Scheduled methods processed on bean '" + beanName + "': " + annotatedMethods);
                }
            }
        }
        return bean;
    }

    protected void processScheduled(Scheduled scheduled, Method method, Object bean) throws NumberFormatException {
        TimeZone timeZone;
        try {
            Assert.isTrue(method.getParameterTypes().length == 0, "Only no-arg methods may be annotated with @Scheduled");
            Method invocableMethod = AopUtils.selectInvocableMethod(method, bean.getClass());
            Runnable runnable = new ScheduledMethodRunnable(bean, invocableMethod);
            boolean processedSchedule = false;
            Set<ScheduledTask> tasks = new LinkedHashSet<>(4);
            long initialDelay = scheduled.initialDelay();
            String initialDelayString = scheduled.initialDelayString();
            if (StringUtils.hasText(initialDelayString)) {
                Assert.isTrue(initialDelay < 0, "Specify 'initialDelay' or 'initialDelayString', not both");
                if (this.embeddedValueResolver != null) {
                    initialDelayString = this.embeddedValueResolver.resolveStringValue(initialDelayString);
                }
                try {
                    initialDelay = Long.parseLong(initialDelayString);
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Invalid initialDelayString value \"" + initialDelayString + "\" - cannot parse into integer");
                }
            }
            String cron = scheduled.cron();
            if (StringUtils.hasText(cron)) {
                Assert.isTrue(initialDelay == -1, "'initialDelay' not supported for cron triggers");
                processedSchedule = true;
                String zone = scheduled.zone();
                if (this.embeddedValueResolver != null) {
                    cron = this.embeddedValueResolver.resolveStringValue(cron);
                    zone = this.embeddedValueResolver.resolveStringValue(zone);
                }
                if (StringUtils.hasText(zone)) {
                    timeZone = StringUtils.parseTimeZoneString(zone);
                } else {
                    timeZone = TimeZone.getDefault();
                }
                tasks.add(this.registrar.scheduleCronTask(new CronTask(runnable, new CronTrigger(cron, timeZone))));
            }
            if (initialDelay < 0) {
                initialDelay = 0;
            }
            long fixedDelay = scheduled.fixedDelay();
            if (fixedDelay >= 0) {
                Assert.isTrue(!processedSchedule, "Exactly one of the 'cron', 'fixedDelay(String)', or 'fixedRate(String)' attributes is required");
                processedSchedule = true;
                tasks.add(this.registrar.scheduleFixedDelayTask(new IntervalTask(runnable, fixedDelay, initialDelay)));
            }
            String fixedDelayString = scheduled.fixedDelayString();
            if (StringUtils.hasText(fixedDelayString)) {
                Assert.isTrue(!processedSchedule, "Exactly one of the 'cron', 'fixedDelay(String)', or 'fixedRate(String)' attributes is required");
                processedSchedule = true;
                if (this.embeddedValueResolver != null) {
                    fixedDelayString = this.embeddedValueResolver.resolveStringValue(fixedDelayString);
                }
                try {
                    tasks.add(this.registrar.scheduleFixedDelayTask(new IntervalTask(runnable, Long.parseLong(fixedDelayString), initialDelay)));
                } catch (NumberFormatException e2) {
                    throw new IllegalArgumentException("Invalid fixedDelayString value \"" + fixedDelayString + "\" - cannot parse into integer");
                }
            }
            long fixedRate = scheduled.fixedRate();
            if (fixedRate >= 0) {
                Assert.isTrue(!processedSchedule, "Exactly one of the 'cron', 'fixedDelay(String)', or 'fixedRate(String)' attributes is required");
                processedSchedule = true;
                tasks.add(this.registrar.scheduleFixedRateTask(new IntervalTask(runnable, fixedRate, initialDelay)));
            }
            String fixedRateString = scheduled.fixedRateString();
            if (StringUtils.hasText(fixedRateString)) {
                Assert.isTrue(!processedSchedule, "Exactly one of the 'cron', 'fixedDelay(String)', or 'fixedRate(String)' attributes is required");
                processedSchedule = true;
                if (this.embeddedValueResolver != null) {
                    fixedRateString = this.embeddedValueResolver.resolveStringValue(fixedRateString);
                }
                try {
                    tasks.add(this.registrar.scheduleFixedRateTask(new IntervalTask(runnable, Long.parseLong(fixedRateString), initialDelay)));
                } catch (NumberFormatException e3) {
                    throw new IllegalArgumentException("Invalid fixedRateString value \"" + fixedRateString + "\" - cannot parse into integer");
                }
            }
            Assert.isTrue(processedSchedule, "Exactly one of the 'cron', 'fixedDelay(String)', or 'fixedRate(String)' attributes is required");
            synchronized (this.scheduledTasks) {
                Set<ScheduledTask> registeredTasks = this.scheduledTasks.get(bean);
                if (registeredTasks == null) {
                    registeredTasks = new LinkedHashSet(4);
                    this.scheduledTasks.put(bean, registeredTasks);
                }
                registeredTasks.addAll(tasks);
            }
        } catch (IllegalArgumentException ex) {
            throw new IllegalStateException("Encountered invalid @Scheduled method '" + method.getName() + "': " + ex.getMessage());
        }
    }

    @Override // org.springframework.beans.factory.config.DestructionAwareBeanPostProcessor
    public void postProcessBeforeDestruction(Object bean, String beanName) {
        Set<ScheduledTask> tasks;
        synchronized (this.scheduledTasks) {
            tasks = this.scheduledTasks.remove(bean);
        }
        if (tasks != null) {
            for (ScheduledTask task : tasks) {
                task.cancel();
            }
        }
    }

    @Override // org.springframework.beans.factory.config.DestructionAwareBeanPostProcessor
    public boolean requiresDestruction(Object bean) {
        boolean zContainsKey;
        synchronized (this.scheduledTasks) {
            zContainsKey = this.scheduledTasks.containsKey(bean);
        }
        return zContainsKey;
    }

    @Override // org.springframework.beans.factory.DisposableBean
    public void destroy() {
        synchronized (this.scheduledTasks) {
            Collection<Set<ScheduledTask>> allTasks = this.scheduledTasks.values();
            for (Set<ScheduledTask> tasks : allTasks) {
                for (ScheduledTask task : tasks) {
                    task.cancel();
                }
            }
            this.scheduledTasks.clear();
        }
        this.registrar.destroy();
    }
}
