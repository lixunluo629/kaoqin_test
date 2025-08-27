package org.springframework.aop.target;

import org.apache.commons.pool.ObjectPool;
import org.apache.commons.pool.PoolableObjectFactory;
import org.apache.commons.pool.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.BaseObjectPoolConfig;
import org.springframework.beans.BeansException;
import org.springframework.core.Constants;

@Deprecated
/* loaded from: spring-aop-4.3.25.RELEASE.jar:org/springframework/aop/target/CommonsPoolTargetSource.class */
public class CommonsPoolTargetSource extends AbstractPoolingTargetSource implements PoolableObjectFactory {
    private static final Constants constants = new Constants(GenericObjectPool.class);
    private int maxIdle = 8;
    private int minIdle = 0;
    private long maxWait = -1;
    private long timeBetweenEvictionRunsMillis = -1;
    private long minEvictableIdleTimeMillis = BaseObjectPoolConfig.DEFAULT_MIN_EVICTABLE_IDLE_TIME_MILLIS;
    private byte whenExhaustedAction = 1;
    private ObjectPool pool;

    public CommonsPoolTargetSource() {
        setMaxSize(8);
    }

    public void setMaxIdle(int maxIdle) {
        this.maxIdle = maxIdle;
    }

    public int getMaxIdle() {
        return this.maxIdle;
    }

    public void setMinIdle(int minIdle) {
        this.minIdle = minIdle;
    }

    public int getMinIdle() {
        return this.minIdle;
    }

    public void setMaxWait(long maxWait) {
        this.maxWait = maxWait;
    }

    public long getMaxWait() {
        return this.maxWait;
    }

    public void setTimeBetweenEvictionRunsMillis(long timeBetweenEvictionRunsMillis) {
        this.timeBetweenEvictionRunsMillis = timeBetweenEvictionRunsMillis;
    }

    public long getTimeBetweenEvictionRunsMillis() {
        return this.timeBetweenEvictionRunsMillis;
    }

    public void setMinEvictableIdleTimeMillis(long minEvictableIdleTimeMillis) {
        this.minEvictableIdleTimeMillis = minEvictableIdleTimeMillis;
    }

    public long getMinEvictableIdleTimeMillis() {
        return this.minEvictableIdleTimeMillis;
    }

    public void setWhenExhaustedActionName(String whenExhaustedActionName) {
        setWhenExhaustedAction(constants.asNumber(whenExhaustedActionName).byteValue());
    }

    public void setWhenExhaustedAction(byte whenExhaustedAction) {
        this.whenExhaustedAction = whenExhaustedAction;
    }

    public byte getWhenExhaustedAction() {
        return this.whenExhaustedAction;
    }

    @Override // org.springframework.aop.target.AbstractPoolingTargetSource
    protected final void createPool() {
        this.logger.debug("Creating Commons object pool");
        this.pool = createObjectPool();
    }

    protected ObjectPool createObjectPool() {
        GenericObjectPool gop = new GenericObjectPool(this);
        gop.setMaxActive(getMaxSize());
        gop.setMaxIdle(getMaxIdle());
        gop.setMinIdle(getMinIdle());
        gop.setMaxWait(getMaxWait());
        gop.setTimeBetweenEvictionRunsMillis(getTimeBetweenEvictionRunsMillis());
        gop.setMinEvictableIdleTimeMillis(getMinEvictableIdleTimeMillis());
        gop.setWhenExhaustedAction(getWhenExhaustedAction());
        return gop;
    }

    @Override // org.springframework.aop.target.AbstractPoolingTargetSource, org.springframework.aop.TargetSource
    public Object getTarget() throws Exception {
        return this.pool.borrowObject();
    }

    @Override // org.springframework.aop.target.AbstractPoolingTargetSource, org.springframework.aop.target.AbstractBeanFactoryBasedTargetSource, org.springframework.aop.TargetSource
    public void releaseTarget(Object target) throws Exception {
        this.pool.returnObject(target);
    }

    @Override // org.springframework.aop.target.PoolingConfig
    public int getActiveCount() throws UnsupportedOperationException {
        return this.pool.getNumActive();
    }

    @Override // org.springframework.aop.target.PoolingConfig
    public int getIdleCount() throws UnsupportedOperationException {
        return this.pool.getNumIdle();
    }

    @Override // org.springframework.beans.factory.DisposableBean
    public void destroy() throws Exception {
        this.logger.debug("Closing Commons ObjectPool");
        this.pool.close();
    }

    public Object makeObject() throws BeansException {
        return newPrototypeInstance();
    }

    public void destroyObject(Object obj) throws Exception {
        destroyPrototypeInstance(obj);
    }

    public boolean validateObject(Object obj) {
        return true;
    }

    public void activateObject(Object obj) {
    }

    public void passivateObject(Object obj) {
    }
}
