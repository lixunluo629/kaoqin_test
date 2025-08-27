package org.springframework.scheduling.config;

import java.util.concurrent.RejectedExecutionHandler;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.StringUtils;

/* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/scheduling/config/TaskExecutorFactoryBean.class */
public class TaskExecutorFactoryBean implements FactoryBean<TaskExecutor>, BeanNameAware, InitializingBean, DisposableBean {
    private String poolSize;
    private Integer queueCapacity;
    private RejectedExecutionHandler rejectedExecutionHandler;
    private Integer keepAliveSeconds;
    private String beanName;
    private ThreadPoolTaskExecutor target;

    public void setPoolSize(String poolSize) {
        this.poolSize = poolSize;
    }

    public void setQueueCapacity(int queueCapacity) {
        this.queueCapacity = Integer.valueOf(queueCapacity);
    }

    public void setRejectedExecutionHandler(RejectedExecutionHandler rejectedExecutionHandler) {
        this.rejectedExecutionHandler = rejectedExecutionHandler;
    }

    public void setKeepAliveSeconds(int keepAliveSeconds) {
        this.keepAliveSeconds = Integer.valueOf(keepAliveSeconds);
    }

    @Override // org.springframework.beans.factory.BeanNameAware
    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

    @Override // org.springframework.beans.factory.InitializingBean
    public void afterPropertiesSet() throws NumberFormatException {
        this.target = new ThreadPoolTaskExecutor();
        determinePoolSizeRange();
        if (this.queueCapacity != null) {
            this.target.setQueueCapacity(this.queueCapacity.intValue());
        }
        if (this.keepAliveSeconds != null) {
            this.target.setKeepAliveSeconds(this.keepAliveSeconds.intValue());
        }
        if (this.rejectedExecutionHandler != null) {
            this.target.setRejectedExecutionHandler(this.rejectedExecutionHandler);
        }
        if (this.beanName != null) {
            this.target.setThreadNamePrefix(this.beanName + "-");
        }
        this.target.afterPropertiesSet();
    }

    private void determinePoolSizeRange() throws NumberFormatException {
        int corePoolSize;
        int maxPoolSize;
        if (StringUtils.hasText(this.poolSize)) {
            try {
                int separatorIndex = this.poolSize.indexOf(45);
                if (separatorIndex != -1) {
                    corePoolSize = Integer.valueOf(this.poolSize.substring(0, separatorIndex)).intValue();
                    maxPoolSize = Integer.valueOf(this.poolSize.substring(separatorIndex + 1, this.poolSize.length())).intValue();
                    if (corePoolSize > maxPoolSize) {
                        throw new IllegalArgumentException("Lower bound of pool-size range must not exceed the upper bound");
                    }
                    if (this.queueCapacity == null) {
                        if (corePoolSize == 0) {
                            this.target.setAllowCoreThreadTimeOut(true);
                            corePoolSize = maxPoolSize;
                        } else {
                            throw new IllegalArgumentException("A non-zero lower bound for the size range requires a queue-capacity value");
                        }
                    }
                } else {
                    Integer value = Integer.valueOf(this.poolSize);
                    corePoolSize = value.intValue();
                    maxPoolSize = value.intValue();
                }
                this.target.setCorePoolSize(corePoolSize);
                this.target.setMaxPoolSize(maxPoolSize);
            } catch (NumberFormatException ex) {
                throw new IllegalArgumentException("Invalid pool-size value [" + this.poolSize + "]: only single maximum integer (e.g. \"5\") and minimum-maximum range (e.g. \"3-5\") are supported", ex);
            }
        }
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.springframework.beans.factory.FactoryBean
    public TaskExecutor getObject() {
        return this.target;
    }

    @Override // org.springframework.beans.factory.FactoryBean
    public Class<? extends TaskExecutor> getObjectType() {
        return this.target != null ? this.target.getClass() : ThreadPoolTaskExecutor.class;
    }

    @Override // org.springframework.beans.factory.FactoryBean
    public boolean isSingleton() {
        return true;
    }

    @Override // org.springframework.beans.factory.DisposableBean
    public void destroy() {
        this.target.destroy();
    }
}
