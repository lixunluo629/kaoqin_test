package org.springframework.beans.factory.support;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeanInstantiationException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.cglib.core.ClassGenerator;
import org.springframework.cglib.core.DefaultGeneratorStrategy;
import org.springframework.cglib.core.SpringNamingPolicy;
import org.springframework.cglib.proxy.Callback;
import org.springframework.cglib.proxy.CallbackFilter;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.Factory;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;
import org.springframework.cglib.proxy.NoOp;
import org.springframework.util.StringUtils;

/* loaded from: spring-beans-4.3.25.RELEASE.jar:org/springframework/beans/factory/support/CglibSubclassingInstantiationStrategy.class */
public class CglibSubclassingInstantiationStrategy extends SimpleInstantiationStrategy {
    private static final int PASSTHROUGH = 0;
    private static final int LOOKUP_OVERRIDE = 1;
    private static final int METHOD_REPLACER = 2;

    @Override // org.springframework.beans.factory.support.SimpleInstantiationStrategy
    protected Object instantiateWithMethodInjection(RootBeanDefinition bd, String beanName, BeanFactory owner) {
        return instantiateWithMethodInjection(bd, beanName, owner, null, new Object[0]);
    }

    @Override // org.springframework.beans.factory.support.SimpleInstantiationStrategy
    protected Object instantiateWithMethodInjection(RootBeanDefinition bd, String beanName, BeanFactory owner, Constructor<?> ctor, Object... args) {
        return new CglibSubclassCreator(bd, owner).instantiate(ctor, args);
    }

    /* loaded from: spring-beans-4.3.25.RELEASE.jar:org/springframework/beans/factory/support/CglibSubclassingInstantiationStrategy$CglibSubclassCreator.class */
    private static class CglibSubclassCreator {
        private static final Class<?>[] CALLBACK_TYPES = {NoOp.class, LookupOverrideMethodInterceptor.class, ReplaceOverrideMethodInterceptor.class};
        private final RootBeanDefinition beanDefinition;
        private final BeanFactory owner;

        CglibSubclassCreator(RootBeanDefinition beanDefinition, BeanFactory owner) {
            this.beanDefinition = beanDefinition;
            this.owner = owner;
        }

        public Object instantiate(Constructor<?> ctor, Object... args) throws BeanInstantiationException, IllegalAccessException, NoSuchMethodException, InstantiationException, SecurityException, IllegalArgumentException, InvocationTargetException {
            Object instance;
            Class<?> subclass = createEnhancedSubclass(this.beanDefinition);
            if (ctor == null) {
                instance = BeanUtils.instantiateClass(subclass);
            } else {
                try {
                    Constructor<?> enhancedSubclassConstructor = subclass.getConstructor(ctor.getParameterTypes());
                    instance = enhancedSubclassConstructor.newInstance(args);
                } catch (Exception ex) {
                    throw new BeanInstantiationException(this.beanDefinition.getBeanClass(), "Failed to invoke constructor for CGLIB enhanced subclass [" + subclass.getName() + "]", ex);
                }
            }
            Factory factory = (Factory) instance;
            factory.setCallbacks(new Callback[]{NoOp.INSTANCE, new LookupOverrideMethodInterceptor(this.beanDefinition, this.owner), new ReplaceOverrideMethodInterceptor(this.beanDefinition, this.owner)});
            return instance;
        }

        private Class<?> createEnhancedSubclass(RootBeanDefinition beanDefinition) {
            Enhancer enhancer = new Enhancer();
            enhancer.setSuperclass(beanDefinition.getBeanClass());
            enhancer.setNamingPolicy(SpringNamingPolicy.INSTANCE);
            if (this.owner instanceof ConfigurableBeanFactory) {
                ClassLoader cl = ((ConfigurableBeanFactory) this.owner).getBeanClassLoader();
                enhancer.setStrategy(new ClassLoaderAwareGeneratorStrategy(cl));
            }
            enhancer.setCallbackFilter(new MethodOverrideCallbackFilter(beanDefinition));
            enhancer.setCallbackTypes(CALLBACK_TYPES);
            return enhancer.createClass();
        }
    }

    /* loaded from: spring-beans-4.3.25.RELEASE.jar:org/springframework/beans/factory/support/CglibSubclassingInstantiationStrategy$CglibIdentitySupport.class */
    private static class CglibIdentitySupport {
        private final RootBeanDefinition beanDefinition;

        public CglibIdentitySupport(RootBeanDefinition beanDefinition) {
            this.beanDefinition = beanDefinition;
        }

        public RootBeanDefinition getBeanDefinition() {
            return this.beanDefinition;
        }

        public boolean equals(Object other) {
            return getClass() == other.getClass() && this.beanDefinition.equals(((CglibIdentitySupport) other).beanDefinition);
        }

        public int hashCode() {
            return this.beanDefinition.hashCode();
        }
    }

    /* loaded from: spring-beans-4.3.25.RELEASE.jar:org/springframework/beans/factory/support/CglibSubclassingInstantiationStrategy$ClassLoaderAwareGeneratorStrategy.class */
    private static class ClassLoaderAwareGeneratorStrategy extends DefaultGeneratorStrategy {
        private final ClassLoader classLoader;

        public ClassLoaderAwareGeneratorStrategy(ClassLoader classLoader) {
            this.classLoader = classLoader;
        }

        @Override // org.springframework.cglib.core.DefaultGeneratorStrategy, org.springframework.cglib.core.GeneratorStrategy
        public byte[] generate(ClassGenerator cg) throws Exception {
            if (this.classLoader == null) {
                return super.generate(cg);
            }
            Thread currentThread = Thread.currentThread();
            try {
                ClassLoader threadContextClassLoader = currentThread.getContextClassLoader();
                boolean overrideClassLoader = !this.classLoader.equals(threadContextClassLoader);
                if (overrideClassLoader) {
                    currentThread.setContextClassLoader(this.classLoader);
                }
                try {
                    byte[] bArrGenerate = super.generate(cg);
                    if (overrideClassLoader) {
                        currentThread.setContextClassLoader(threadContextClassLoader);
                    }
                    return bArrGenerate;
                } catch (Throwable th) {
                    if (overrideClassLoader) {
                        currentThread.setContextClassLoader(threadContextClassLoader);
                    }
                    throw th;
                }
            } catch (Throwable th2) {
                return super.generate(cg);
            }
        }
    }

    /* loaded from: spring-beans-4.3.25.RELEASE.jar:org/springframework/beans/factory/support/CglibSubclassingInstantiationStrategy$MethodOverrideCallbackFilter.class */
    private static class MethodOverrideCallbackFilter extends CglibIdentitySupport implements CallbackFilter {
        private static final Log logger = LogFactory.getLog(MethodOverrideCallbackFilter.class);

        public MethodOverrideCallbackFilter(RootBeanDefinition beanDefinition) {
            super(beanDefinition);
        }

        @Override // org.springframework.cglib.proxy.CallbackFilter
        public int accept(Method method) {
            MethodOverride methodOverride = getBeanDefinition().getMethodOverrides().getOverride(method);
            if (logger.isTraceEnabled()) {
                logger.trace("Override for '" + method.getName() + "' is [" + methodOverride + "]");
            }
            if (methodOverride == null) {
                return 0;
            }
            if (methodOverride instanceof LookupOverride) {
                return 1;
            }
            if (methodOverride instanceof ReplaceOverride) {
                return 2;
            }
            throw new UnsupportedOperationException("Unexpected MethodOverride subclass: " + methodOverride.getClass().getName());
        }
    }

    /* loaded from: spring-beans-4.3.25.RELEASE.jar:org/springframework/beans/factory/support/CglibSubclassingInstantiationStrategy$LookupOverrideMethodInterceptor.class */
    private static class LookupOverrideMethodInterceptor extends CglibIdentitySupport implements MethodInterceptor {
        private final BeanFactory owner;

        public LookupOverrideMethodInterceptor(RootBeanDefinition beanDefinition, BeanFactory owner) {
            super(beanDefinition);
            this.owner = owner;
        }

        @Override // org.springframework.cglib.proxy.MethodInterceptor
        public Object intercept(Object obj, Method method, Object[] args, MethodProxy mp) throws Throwable {
            LookupOverride lo = (LookupOverride) getBeanDefinition().getMethodOverrides().getOverride(method);
            Object[] argsToUse = args.length > 0 ? args : null;
            if (StringUtils.hasText(lo.getBeanName())) {
                return this.owner.getBean(lo.getBeanName(), argsToUse);
            }
            return this.owner.getBean(method.getReturnType(), argsToUse);
        }
    }

    /* loaded from: spring-beans-4.3.25.RELEASE.jar:org/springframework/beans/factory/support/CglibSubclassingInstantiationStrategy$ReplaceOverrideMethodInterceptor.class */
    private static class ReplaceOverrideMethodInterceptor extends CglibIdentitySupport implements MethodInterceptor {
        private final BeanFactory owner;

        public ReplaceOverrideMethodInterceptor(RootBeanDefinition beanDefinition, BeanFactory owner) {
            super(beanDefinition);
            this.owner = owner;
        }

        @Override // org.springframework.cglib.proxy.MethodInterceptor
        public Object intercept(Object obj, Method method, Object[] args, MethodProxy mp) throws Throwable {
            ReplaceOverride ro = (ReplaceOverride) getBeanDefinition().getMethodOverrides().getOverride(method);
            MethodReplacer mr = (MethodReplacer) this.owner.getBean(ro.getMethodReplacerBeanName(), MethodReplacer.class);
            return mr.reimplement(obj, method, args);
        }
    }
}
