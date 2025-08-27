package org.springframework.context.support;

import com.alibaba.excel.constant.ExcelXmlConstants;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContextException;
import org.springframework.context.Lifecycle;
import org.springframework.context.LifecycleProcessor;
import org.springframework.context.Phased;
import org.springframework.context.SmartLifecycle;
import org.springframework.util.backoff.ExponentialBackOff;

/* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/context/support/DefaultLifecycleProcessor.class */
public class DefaultLifecycleProcessor implements LifecycleProcessor, BeanFactoryAware {
    private final Log logger = LogFactory.getLog(getClass());
    private volatile long timeoutPerShutdownPhase = ExponentialBackOff.DEFAULT_MAX_INTERVAL;
    private volatile boolean running;
    private volatile ConfigurableListableBeanFactory beanFactory;

    public void setTimeoutPerShutdownPhase(long timeoutPerShutdownPhase) {
        this.timeoutPerShutdownPhase = timeoutPerShutdownPhase;
    }

    @Override // org.springframework.beans.factory.BeanFactoryAware
    public void setBeanFactory(BeanFactory beanFactory) {
        if (!(beanFactory instanceof ConfigurableListableBeanFactory)) {
            throw new IllegalArgumentException("DefaultLifecycleProcessor requires a ConfigurableListableBeanFactory: " + beanFactory);
        }
        this.beanFactory = (ConfigurableListableBeanFactory) beanFactory;
    }

    @Override // org.springframework.context.Lifecycle
    public void start() {
        startBeans(false);
        this.running = true;
    }

    @Override // org.springframework.context.Lifecycle
    public void stop() throws InterruptedException {
        stopBeans();
        this.running = false;
    }

    @Override // org.springframework.context.LifecycleProcessor
    public void onRefresh() {
        startBeans(true);
        this.running = true;
    }

    @Override // org.springframework.context.LifecycleProcessor
    public void onClose() throws InterruptedException {
        stopBeans();
        this.running = false;
    }

    @Override // org.springframework.context.Lifecycle
    public boolean isRunning() {
        return this.running;
    }

    private void startBeans(boolean autoStartupOnly) {
        Map<String, Lifecycle> lifecycleBeans = getLifecycleBeans();
        Map<Integer, LifecycleGroup> phases = new HashMap<>();
        for (Map.Entry<String, ? extends Lifecycle> entry : lifecycleBeans.entrySet()) {
            Lifecycle bean = entry.getValue();
            if (!autoStartupOnly || ((bean instanceof SmartLifecycle) && ((SmartLifecycle) bean).isAutoStartup())) {
                int phase = getPhase(bean);
                LifecycleGroup group = phases.get(Integer.valueOf(phase));
                if (group == null) {
                    group = new LifecycleGroup(phase, this.timeoutPerShutdownPhase, lifecycleBeans, autoStartupOnly);
                    phases.put(Integer.valueOf(phase), group);
                }
                group.add(entry.getKey(), bean);
            }
        }
        if (!phases.isEmpty()) {
            List<Integer> keys = new ArrayList<>(phases.keySet());
            Collections.sort(keys);
            for (Integer key : keys) {
                phases.get(key).start();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void doStart(Map<String, ? extends Lifecycle> lifecycleBeans, String beanName, boolean autoStartupOnly) {
        Lifecycle bean = lifecycleBeans.remove(beanName);
        if (bean != null && bean != this) {
            String[] dependenciesForBean = this.beanFactory.getDependenciesForBean(beanName);
            for (String dependency : dependenciesForBean) {
                doStart(lifecycleBeans, dependency, autoStartupOnly);
            }
            if (!bean.isRunning()) {
                if (!autoStartupOnly || !(bean instanceof SmartLifecycle) || ((SmartLifecycle) bean).isAutoStartup()) {
                    if (this.logger.isDebugEnabled()) {
                        this.logger.debug("Starting bean '" + beanName + "' of type [" + bean.getClass().getName() + "]");
                    }
                    try {
                        bean.start();
                        if (this.logger.isDebugEnabled()) {
                            this.logger.debug("Successfully started bean '" + beanName + "'");
                        }
                    } catch (Throwable ex) {
                        throw new ApplicationContextException("Failed to start bean '" + beanName + "'", ex);
                    }
                }
            }
        }
    }

    private void stopBeans() throws InterruptedException {
        Map<String, Lifecycle> lifecycleBeans = getLifecycleBeans();
        Map<Integer, LifecycleGroup> phases = new HashMap<>();
        for (Map.Entry<String, Lifecycle> entry : lifecycleBeans.entrySet()) {
            Lifecycle bean = entry.getValue();
            int shutdownPhase = getPhase(bean);
            LifecycleGroup group = phases.get(Integer.valueOf(shutdownPhase));
            if (group == null) {
                group = new LifecycleGroup(shutdownPhase, this.timeoutPerShutdownPhase, lifecycleBeans, false);
                phases.put(Integer.valueOf(shutdownPhase), group);
            }
            group.add(entry.getKey(), bean);
        }
        if (!phases.isEmpty()) {
            List<Integer> keys = new ArrayList<>(phases.keySet());
            Collections.sort(keys, Collections.reverseOrder());
            for (Integer key : keys) {
                phases.get(key).stop();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void doStop(Map<String, ? extends Lifecycle> lifecycleBeans, final String beanName, final CountDownLatch latch, final Set<String> countDownBeanNames) {
        Lifecycle bean = lifecycleBeans.remove(beanName);
        if (bean != null) {
            String[] dependentBeans = this.beanFactory.getDependentBeans(beanName);
            for (String dependentBean : dependentBeans) {
                doStop(lifecycleBeans, dependentBean, latch, countDownBeanNames);
            }
            try {
                if (bean.isRunning()) {
                    if (bean instanceof SmartLifecycle) {
                        if (this.logger.isDebugEnabled()) {
                            this.logger.debug("Asking bean '" + beanName + "' of type [" + bean.getClass().getName() + "] to stop");
                        }
                        countDownBeanNames.add(beanName);
                        ((SmartLifecycle) bean).stop(new Runnable() { // from class: org.springframework.context.support.DefaultLifecycleProcessor.1
                            @Override // java.lang.Runnable
                            public void run() {
                                latch.countDown();
                                countDownBeanNames.remove(beanName);
                                if (DefaultLifecycleProcessor.this.logger.isDebugEnabled()) {
                                    DefaultLifecycleProcessor.this.logger.debug("Bean '" + beanName + "' completed its stop procedure");
                                }
                            }
                        });
                    } else {
                        if (this.logger.isDebugEnabled()) {
                            this.logger.debug("Stopping bean '" + beanName + "' of type [" + bean.getClass().getName() + "]");
                        }
                        bean.stop();
                        if (this.logger.isDebugEnabled()) {
                            this.logger.debug("Successfully stopped bean '" + beanName + "'");
                        }
                    }
                } else if (bean instanceof SmartLifecycle) {
                    latch.countDown();
                }
            } catch (Throwable ex) {
                if (this.logger.isWarnEnabled()) {
                    this.logger.warn("Failed to stop bean '" + beanName + "'", ex);
                }
            }
        }
    }

    protected Map<String, Lifecycle> getLifecycleBeans() {
        Lifecycle bean;
        Map<String, Lifecycle> beans = new LinkedHashMap<>();
        String[] beanNames = this.beanFactory.getBeanNamesForType(Lifecycle.class, false, false);
        for (String beanName : beanNames) {
            String beanNameToRegister = BeanFactoryUtils.transformedBeanName(beanName);
            boolean isFactoryBean = this.beanFactory.isFactoryBean(beanNameToRegister);
            String beanNameToCheck = isFactoryBean ? "&" + beanName : beanName;
            if (((this.beanFactory.containsSingleton(beanNameToRegister) && (!isFactoryBean || Lifecycle.class.isAssignableFrom(this.beanFactory.getType(beanNameToCheck)))) || SmartLifecycle.class.isAssignableFrom(this.beanFactory.getType(beanNameToCheck))) && (bean = (Lifecycle) this.beanFactory.getBean(beanNameToCheck, Lifecycle.class)) != this) {
                beans.put(beanNameToRegister, bean);
            }
        }
        return beans;
    }

    protected int getPhase(Lifecycle bean) {
        if (bean instanceof Phased) {
            return ((Phased) bean).getPhase();
        }
        return 0;
    }

    /* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/context/support/DefaultLifecycleProcessor$LifecycleGroup.class */
    private class LifecycleGroup {
        private final int phase;
        private final long timeout;
        private final Map<String, ? extends Lifecycle> lifecycleBeans;
        private final boolean autoStartupOnly;
        private final List<LifecycleGroupMember> members = new ArrayList();
        private int smartMemberCount;

        public LifecycleGroup(int phase, long timeout, Map<String, ? extends Lifecycle> lifecycleBeans, boolean autoStartupOnly) {
            this.phase = phase;
            this.timeout = timeout;
            this.lifecycleBeans = lifecycleBeans;
            this.autoStartupOnly = autoStartupOnly;
        }

        public void add(String name, Lifecycle bean) {
            this.members.add(DefaultLifecycleProcessor.this.new LifecycleGroupMember(name, bean));
            if (bean instanceof SmartLifecycle) {
                this.smartMemberCount++;
            }
        }

        public void start() {
            if (!this.members.isEmpty()) {
                if (DefaultLifecycleProcessor.this.logger.isInfoEnabled()) {
                    DefaultLifecycleProcessor.this.logger.info("Starting beans in phase " + this.phase);
                }
                Collections.sort(this.members);
                for (LifecycleGroupMember member : this.members) {
                    if (this.lifecycleBeans.containsKey(member.name)) {
                        DefaultLifecycleProcessor.this.doStart(this.lifecycleBeans, member.name, this.autoStartupOnly);
                    }
                }
            }
        }

        public void stop() throws InterruptedException {
            if (!this.members.isEmpty()) {
                if (DefaultLifecycleProcessor.this.logger.isInfoEnabled()) {
                    DefaultLifecycleProcessor.this.logger.info("Stopping beans in phase " + this.phase);
                }
                Collections.sort(this.members, Collections.reverseOrder());
                CountDownLatch latch = new CountDownLatch(this.smartMemberCount);
                Set<String> countDownBeanNames = Collections.synchronizedSet(new LinkedHashSet());
                for (LifecycleGroupMember member : this.members) {
                    if (this.lifecycleBeans.containsKey(member.name)) {
                        DefaultLifecycleProcessor.this.doStop(this.lifecycleBeans, member.name, latch, countDownBeanNames);
                    } else if (member.bean instanceof SmartLifecycle) {
                        latch.countDown();
                    }
                }
                try {
                    latch.await(this.timeout, TimeUnit.MILLISECONDS);
                    if (latch.getCount() > 0 && !countDownBeanNames.isEmpty() && DefaultLifecycleProcessor.this.logger.isWarnEnabled()) {
                        DefaultLifecycleProcessor.this.logger.warn("Failed to shut down " + countDownBeanNames.size() + " bean" + (countDownBeanNames.size() > 1 ? ExcelXmlConstants.CELL_DATA_FORMAT_TAG : "") + " with phase value " + this.phase + " within timeout of " + this.timeout + ": " + countDownBeanNames);
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    /* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/context/support/DefaultLifecycleProcessor$LifecycleGroupMember.class */
    private class LifecycleGroupMember implements Comparable<LifecycleGroupMember> {
        private final String name;
        private final Lifecycle bean;

        LifecycleGroupMember(String name, Lifecycle bean) {
            this.name = name;
            this.bean = bean;
        }

        @Override // java.lang.Comparable
        public int compareTo(LifecycleGroupMember other) {
            int thisPhase = DefaultLifecycleProcessor.this.getPhase(this.bean);
            int otherPhase = DefaultLifecycleProcessor.this.getPhase(other.bean);
            if (thisPhase == otherPhase) {
                return 0;
            }
            return thisPhase < otherPhase ? -1 : 1;
        }
    }
}
