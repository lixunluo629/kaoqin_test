package org.springframework.aop.framework;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.cglib.proxy.Callback;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.Factory;
import org.springframework.objenesis.SpringObjenesis;

/* loaded from: spring-aop-4.3.25.RELEASE.jar:org/springframework/aop/framework/ObjenesisCglibAopProxy.class */
class ObjenesisCglibAopProxy extends CglibAopProxy {
    private static final Log logger = LogFactory.getLog(ObjenesisCglibAopProxy.class);
    private static final SpringObjenesis objenesis = new SpringObjenesis();

    public ObjenesisCglibAopProxy(AdvisedSupport config) {
        super(config);
    }

    @Override // org.springframework.aop.framework.CglibAopProxy
    protected Object createProxyClassAndInstance(Enhancer enhancer, Callback[] callbacks) {
        Object objNewInstance;
        Class<?> proxyClass = enhancer.createClass();
        Object proxyInstance = null;
        if (objenesis.isWorthTrying()) {
            try {
                proxyInstance = objenesis.newInstance(proxyClass, enhancer.getUseCache());
            } catch (Throwable ex) {
                logger.debug("Unable to instantiate proxy using Objenesis, falling back to regular proxy construction", ex);
            }
        }
        if (proxyInstance == null) {
            try {
                if (this.constructorArgs != null) {
                    objNewInstance = proxyClass.getConstructor(this.constructorArgTypes).newInstance(this.constructorArgs);
                } else {
                    objNewInstance = proxyClass.newInstance();
                }
                proxyInstance = objNewInstance;
            } catch (Throwable ex2) {
                throw new AopConfigException("Unable to instantiate proxy using Objenesis, and regular proxy instantiation via default constructor fails as well", ex2);
            }
        }
        ((Factory) proxyInstance).setCallbacks(callbacks);
        return proxyInstance;
    }
}
