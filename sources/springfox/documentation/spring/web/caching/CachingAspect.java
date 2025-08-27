package springfox.documentation.spring.web.caching;

import com.fasterxml.classmate.TypeResolver;
import java.lang.reflect.Method;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.stereotype.Component;
import springfox.documentation.annotations.Cacheable;
import springfox.documentation.spring.web.DocumentationCache;

@Aspect
@Component
/* loaded from: springfox-spring-web-2.2.2.jar:springfox/documentation/spring/web/caching/CachingAspect.class */
public class CachingAspect {
    private static final Logger LOG = LoggerFactory.getLogger((Class<?>) CachingAspect.class);

    @Autowired
    private DocumentationCache cache;

    @Autowired
    private TypeResolver typeResolver;

    @Pointcut("execution(* springfox.documentation.spring.web.readers.operation.ApiOperationReader.read(..))")
    public void operationRead() {
    }

    @Pointcut("execution(* springfox.documentation.schema.property.ModelPropertiesProvider+.propertiesFor(..))")
    public void propertiesFor() {
    }

    @Pointcut("execution(* springfox.documentation.schema.ModelDependencyProvider.dependentModels(..))")
    public void dependenciesFor() {
    }

    @Pointcut("execution(* springfox.documentation.schema.ModelProvider+.modelFor(..))")
    public void model() {
    }

    @Around("(operationRead() || propertiesFor()) && @annotation(cacheable)")
    public Object operationsAndProperties(ProceedingJoinPoint joinPoint, Cacheable cacheable) throws Throwable {
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        KeyGenerator keyGenerator = cacheable.keyGenerator().newInstance();
        Object key = keyGenerator.generate(joinPoint.getTarget(), method, joinPoint.getArgs());
        LOG.info("Caching aspect applied for cache {} with key {}", cacheable.value(), key);
        return cachedValue(joinPoint, cacheable.value(), key);
    }

    @Around("(model() || dependenciesFor()) && @annotation(cacheable)")
    public Object modelsAndDependencies(ProceedingJoinPoint joinPoint, Cacheable cacheable) throws Throwable {
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        KeyGenerator keyGenerator = cacheable.keyGenerator().getDeclaredConstructor(TypeResolver.class).newInstance(this.typeResolver);
        Object key = keyGenerator.generate(joinPoint.getTarget(), method, joinPoint.getArgs());
        LOG.info("Caching aspect applied for cache {} with key {}", cacheable.value(), key);
        return cachedValue(joinPoint, cacheable.value(), key);
    }

    private Object cachedValue(ProceedingJoinPoint joinPoint, String cacheName, Object key) throws Throwable {
        Cache cache = this.cache.getCache(cacheName);
        if (cache.get(key) == null) {
            cache.put(key, joinPoint.proceed());
        }
        return cache.get(key).get();
    }
}
