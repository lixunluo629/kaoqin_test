package org.springframework.web.servlet.handler;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import javax.servlet.http.HttpServletRequest;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.MethodIntrospector;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerMapping;

/* loaded from: spring-webmvc-4.3.25.RELEASE.jar:org/springframework/web/servlet/handler/AbstractHandlerMethodMapping.class */
public abstract class AbstractHandlerMethodMapping<T> extends AbstractHandlerMapping implements InitializingBean {
    private static final String SCOPED_TARGET_NAME_PREFIX = "scopedTarget.";
    private static final HandlerMethod PREFLIGHT_AMBIGUOUS_MATCH = new HandlerMethod(new EmptyHandler(), ClassUtils.getMethod(EmptyHandler.class, "handle", new Class[0]));
    private static final CorsConfiguration ALLOW_CORS_CONFIG = new CorsConfiguration();
    private HandlerMethodMappingNamingStrategy<T> namingStrategy;
    private boolean detectHandlerMethodsInAncestorContexts = false;
    private final AbstractHandlerMethodMapping<T>.MappingRegistry mappingRegistry = new MappingRegistry();

    protected abstract boolean isHandler(Class<?> cls);

    protected abstract T getMappingForMethod(Method method, Class<?> cls);

    protected abstract Set<String> getMappingPathPatterns(T t);

    protected abstract T getMatchingMapping(T t, HttpServletRequest httpServletRequest);

    protected abstract Comparator<T> getMappingComparator(HttpServletRequest httpServletRequest);

    static {
        ALLOW_CORS_CONFIG.addAllowedOrigin("*");
        ALLOW_CORS_CONFIG.addAllowedMethod("*");
        ALLOW_CORS_CONFIG.addAllowedHeader("*");
        ALLOW_CORS_CONFIG.setAllowCredentials(true);
    }

    public void setDetectHandlerMethodsInAncestorContexts(boolean detectHandlerMethodsInAncestorContexts) {
        this.detectHandlerMethodsInAncestorContexts = detectHandlerMethodsInAncestorContexts;
    }

    public void setHandlerMethodMappingNamingStrategy(HandlerMethodMappingNamingStrategy<T> namingStrategy) {
        this.namingStrategy = namingStrategy;
    }

    public HandlerMethodMappingNamingStrategy<T> getNamingStrategy() {
        return this.namingStrategy;
    }

    public Map<T, HandlerMethod> getHandlerMethods() {
        this.mappingRegistry.acquireReadLock();
        try {
            return Collections.unmodifiableMap(this.mappingRegistry.getMappings());
        } finally {
            this.mappingRegistry.releaseReadLock();
        }
    }

    public List<HandlerMethod> getHandlerMethodsForMappingName(String mappingName) {
        return this.mappingRegistry.getHandlerMethodsByMappingName(mappingName);
    }

    AbstractHandlerMethodMapping<T>.MappingRegistry getMappingRegistry() {
        return this.mappingRegistry;
    }

    public void registerMapping(T mapping, Object handler, Method method) {
        this.mappingRegistry.register(mapping, handler, method);
    }

    public void unregisterMapping(T mapping) {
        this.mappingRegistry.unregister(mapping);
    }

    @Override // org.springframework.beans.factory.InitializingBean
    public void afterPropertiesSet() {
        initHandlerMethods();
    }

    protected void initHandlerMethods() {
        String[] beanNamesForType;
        if (this.logger.isDebugEnabled()) {
            this.logger.debug("Looking for request mappings in application context: " + getApplicationContext());
        }
        if (this.detectHandlerMethodsInAncestorContexts) {
            beanNamesForType = BeanFactoryUtils.beanNamesForTypeIncludingAncestors(getApplicationContext(), (Class<?>) Object.class);
        } else {
            beanNamesForType = getApplicationContext().getBeanNamesForType(Object.class);
        }
        String[] beanNames = beanNamesForType;
        for (String beanName : beanNames) {
            if (!beanName.startsWith(SCOPED_TARGET_NAME_PREFIX)) {
                Class<?> beanType = null;
                try {
                    beanType = getApplicationContext().getType(beanName);
                } catch (Throwable ex) {
                    if (this.logger.isDebugEnabled()) {
                        this.logger.debug("Could not resolve target class for bean with name '" + beanName + "'", ex);
                    }
                }
                if (beanType != null && isHandler(beanType)) {
                    detectHandlerMethods(beanName);
                }
            }
        }
        handlerMethodsInitialized(getHandlerMethods());
    }

    protected void detectHandlerMethods(Object handler) {
        Class<?> handlerType = handler instanceof String ? getApplicationContext().getType((String) handler) : handler.getClass();
        final Class<?> userType = ClassUtils.getUserClass(handlerType);
        Map<Method, T> methods = MethodIntrospector.selectMethods(userType, new MethodIntrospector.MetadataLookup<T>() { // from class: org.springframework.web.servlet.handler.AbstractHandlerMethodMapping.1
            @Override // org.springframework.core.MethodIntrospector.MetadataLookup
            public T inspect(Method method) {
                try {
                    return (T) AbstractHandlerMethodMapping.this.getMappingForMethod(method, userType);
                } catch (Throwable th) {
                    throw new IllegalStateException("Invalid mapping on handler class [" + userType.getName() + "]: " + method, th);
                }
            }
        });
        if (this.logger.isDebugEnabled()) {
            this.logger.debug(methods.size() + " request handler methods found on " + userType + ": " + methods);
        }
        for (Map.Entry<Method, T> entry : methods.entrySet()) {
            Method invocableMethod = AopUtils.selectInvocableMethod(entry.getKey(), userType);
            T mapping = entry.getValue();
            registerHandlerMethod(handler, invocableMethod, mapping);
        }
    }

    protected void registerHandlerMethod(Object handler, Method method, T mapping) {
        this.mappingRegistry.register(mapping, handler, method);
    }

    protected HandlerMethod createHandlerMethod(Object handler, Method method) {
        HandlerMethod handlerMethod;
        if (handler instanceof String) {
            String beanName = (String) handler;
            handlerMethod = new HandlerMethod(beanName, getApplicationContext().getAutowireCapableBeanFactory(), method);
        } else {
            handlerMethod = new HandlerMethod(handler, method);
        }
        return handlerMethod;
    }

    protected CorsConfiguration initCorsConfiguration(Object handler, Method method, T mapping) {
        return null;
    }

    protected void handlerMethodsInitialized(Map<T, HandlerMethod> handlerMethods) {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.springframework.web.servlet.handler.AbstractHandlerMapping
    public HandlerMethod getHandlerInternal(HttpServletRequest request) throws Exception {
        String lookupPath = getUrlPathHelper().getLookupPathForRequest(request);
        if (this.logger.isDebugEnabled()) {
            this.logger.debug("Looking up handler method for path " + lookupPath);
        }
        this.mappingRegistry.acquireReadLock();
        try {
            HandlerMethod handlerMethod = lookupHandlerMethod(lookupPath, request);
            if (this.logger.isDebugEnabled()) {
                if (handlerMethod != null) {
                    this.logger.debug("Returning handler method [" + handlerMethod + "]");
                } else {
                    this.logger.debug("Did not find handler method for [" + lookupPath + "]");
                }
            }
            return handlerMethod != null ? handlerMethod.createWithResolvedBean() : null;
        } finally {
            this.mappingRegistry.releaseReadLock();
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    protected HandlerMethod lookupHandlerMethod(String lookupPath, HttpServletRequest request) throws Exception {
        ArrayList arrayList = new ArrayList();
        List<T> directPathMatches = this.mappingRegistry.getMappingsByUrl(lookupPath);
        if (directPathMatches != null) {
            addMatchingMappings(directPathMatches, arrayList, request);
        }
        if (arrayList.isEmpty()) {
            addMatchingMappings(this.mappingRegistry.getMappings().keySet(), arrayList, request);
        }
        if (!arrayList.isEmpty()) {
            Comparator<AbstractHandlerMethodMapping<T>.Match> comparator = new MatchComparator(getMappingComparator(request));
            Collections.sort(arrayList, comparator);
            if (this.logger.isTraceEnabled()) {
                this.logger.trace("Found " + arrayList.size() + " matching mapping(s) for [" + lookupPath + "] : " + arrayList);
            }
            AbstractHandlerMethodMapping<T>.Match bestMatch = (Match) arrayList.get(0);
            if (arrayList.size() > 1) {
                if (CorsUtils.isPreFlightRequest(request)) {
                    return PREFLIGHT_AMBIGUOUS_MATCH;
                }
                AbstractHandlerMethodMapping<T>.Match secondBestMatch = (Match) arrayList.get(1);
                if (comparator.compare(bestMatch, secondBestMatch) == 0) {
                    Method m1 = ((Match) bestMatch).handlerMethod.getMethod();
                    Method m2 = ((Match) secondBestMatch).handlerMethod.getMethod();
                    throw new IllegalStateException("Ambiguous handler methods mapped for HTTP path '" + ((Object) request.getRequestURL()) + "': {" + m1 + ", " + m2 + "}");
                }
            }
            request.setAttribute(BEST_MATCHING_HANDLER_ATTRIBUTE, ((Match) bestMatch).handlerMethod);
            handleMatch(((Match) bestMatch).mapping, lookupPath, request);
            return ((Match) bestMatch).handlerMethod;
        }
        return handleNoMatch(this.mappingRegistry.getMappings().keySet(), lookupPath, request);
    }

    private void addMatchingMappings(Collection<T> mappings, List<AbstractHandlerMethodMapping<T>.Match> matches, HttpServletRequest request) {
        for (T mapping : mappings) {
            T match = getMatchingMapping(mapping, request);
            if (match != null) {
                matches.add(new Match(match, this.mappingRegistry.getMappings().get(mapping)));
            }
        }
    }

    protected void handleMatch(T mapping, String lookupPath, HttpServletRequest request) {
        request.setAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE, lookupPath);
    }

    protected HandlerMethod handleNoMatch(Set<T> mappings, String lookupPath, HttpServletRequest request) throws Exception {
        return null;
    }

    @Override // org.springframework.web.servlet.handler.AbstractHandlerMapping
    protected CorsConfiguration getCorsConfiguration(Object handler, HttpServletRequest request) {
        CorsConfiguration corsConfig = super.getCorsConfiguration(handler, request);
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            if (handlerMethod.equals(PREFLIGHT_AMBIGUOUS_MATCH)) {
                return ALLOW_CORS_CONFIG;
            }
            CorsConfiguration corsConfigFromMethod = this.mappingRegistry.getCorsConfiguration(handlerMethod);
            corsConfig = corsConfig != null ? corsConfig.combine(corsConfigFromMethod) : corsConfigFromMethod;
        }
        return corsConfig;
    }

    /* loaded from: spring-webmvc-4.3.25.RELEASE.jar:org/springframework/web/servlet/handler/AbstractHandlerMethodMapping$MappingRegistry.class */
    class MappingRegistry {
        private final Map<T, MappingRegistration<T>> registry = new HashMap();
        private final Map<T, HandlerMethod> mappingLookup = new LinkedHashMap();
        private final MultiValueMap<String, T> urlLookup = new LinkedMultiValueMap();
        private final Map<String, List<HandlerMethod>> nameLookup = new ConcurrentHashMap();
        private final Map<HandlerMethod, CorsConfiguration> corsLookup = new ConcurrentHashMap();
        private final ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();

        MappingRegistry() {
        }

        public Map<T, HandlerMethod> getMappings() {
            return this.mappingLookup;
        }

        public List<T> getMappingsByUrl(String urlPath) {
            return (List) this.urlLookup.get(urlPath);
        }

        public List<HandlerMethod> getHandlerMethodsByMappingName(String mappingName) {
            return this.nameLookup.get(mappingName);
        }

        public CorsConfiguration getCorsConfiguration(HandlerMethod handlerMethod) {
            HandlerMethod original = handlerMethod.getResolvedFromHandlerMethod();
            return this.corsLookup.get(original != null ? original : handlerMethod);
        }

        public void acquireReadLock() {
            this.readWriteLock.readLock().lock();
        }

        public void releaseReadLock() {
            this.readWriteLock.readLock().unlock();
        }

        public void register(T mapping, Object handler, Method method) {
            this.readWriteLock.writeLock().lock();
            try {
                HandlerMethod handlerMethod = AbstractHandlerMethodMapping.this.createHandlerMethod(handler, method);
                assertUniqueMethodMapping(handlerMethod, mapping);
                if (AbstractHandlerMethodMapping.this.logger.isInfoEnabled()) {
                    AbstractHandlerMethodMapping.this.logger.info("Mapped \"" + mapping + "\" onto " + handlerMethod);
                }
                this.mappingLookup.put(mapping, handlerMethod);
                List<String> directUrls = getDirectUrls(mapping);
                for (String url : directUrls) {
                    this.urlLookup.add(url, mapping);
                }
                String name = null;
                if (AbstractHandlerMethodMapping.this.getNamingStrategy() != null) {
                    name = AbstractHandlerMethodMapping.this.getNamingStrategy().getName(handlerMethod, mapping);
                    addMappingName(name, handlerMethod);
                }
                CorsConfiguration corsConfig = AbstractHandlerMethodMapping.this.initCorsConfiguration(handler, method, mapping);
                if (corsConfig != null) {
                    this.corsLookup.put(handlerMethod, corsConfig);
                }
                this.registry.put(mapping, new MappingRegistration<>(mapping, handlerMethod, directUrls, name));
                this.readWriteLock.writeLock().unlock();
            } catch (Throwable th) {
                this.readWriteLock.writeLock().unlock();
                throw th;
            }
        }

        private void assertUniqueMethodMapping(HandlerMethod newHandlerMethod, T mapping) {
            HandlerMethod handlerMethod = this.mappingLookup.get(mapping);
            if (handlerMethod != null && !handlerMethod.equals(newHandlerMethod)) {
                throw new IllegalStateException("Ambiguous mapping. Cannot map '" + newHandlerMethod.getBean() + "' method \n" + newHandlerMethod + "\nto " + mapping + ": There is already '" + handlerMethod.getBean() + "' bean method\n" + handlerMethod + " mapped.");
            }
        }

        private List<String> getDirectUrls(T mapping) {
            List<String> urls = new ArrayList<>(1);
            for (String path : AbstractHandlerMethodMapping.this.getMappingPathPatterns(mapping)) {
                if (!AbstractHandlerMethodMapping.this.getPathMatcher().isPattern(path)) {
                    urls.add(path);
                }
            }
            return urls;
        }

        private void addMappingName(String name, HandlerMethod handlerMethod) {
            List<HandlerMethod> oldList = this.nameLookup.get(name);
            if (oldList == null) {
                oldList = Collections.emptyList();
            }
            for (HandlerMethod current : oldList) {
                if (handlerMethod.equals(current)) {
                    return;
                }
            }
            if (AbstractHandlerMethodMapping.this.logger.isTraceEnabled()) {
                AbstractHandlerMethodMapping.this.logger.trace("Mapping name '" + name + "'");
            }
            List<HandlerMethod> newList = new ArrayList<>(oldList.size() + 1);
            newList.addAll(oldList);
            newList.add(handlerMethod);
            this.nameLookup.put(name, newList);
            if (newList.size() > 1 && AbstractHandlerMethodMapping.this.logger.isTraceEnabled()) {
                AbstractHandlerMethodMapping.this.logger.trace("Mapping name clash for handlerMethods " + newList + ". Consider assigning explicit names.");
            }
        }

        public void unregister(T mapping) {
            this.readWriteLock.writeLock().lock();
            try {
                MappingRegistration<T> definition = this.registry.remove(mapping);
                if (definition == null) {
                    return;
                }
                this.mappingLookup.remove(definition.getMapping());
                for (String url : definition.getDirectUrls()) {
                    List<T> list = (List) this.urlLookup.get(url);
                    if (list != null) {
                        list.remove(definition.getMapping());
                        if (list.isEmpty()) {
                            this.urlLookup.remove(url);
                        }
                    }
                }
                removeMappingName(definition);
                this.corsLookup.remove(definition.getHandlerMethod());
                this.readWriteLock.writeLock().unlock();
            } finally {
                this.readWriteLock.writeLock().unlock();
            }
        }

        private void removeMappingName(MappingRegistration<T> definition) {
            String name = definition.getMappingName();
            if (name == null) {
                return;
            }
            HandlerMethod handlerMethod = definition.getHandlerMethod();
            List<HandlerMethod> oldList = this.nameLookup.get(name);
            if (oldList == null) {
                return;
            }
            if (oldList.size() <= 1) {
                this.nameLookup.remove(name);
                return;
            }
            List<HandlerMethod> newList = new ArrayList<>(oldList.size() - 1);
            for (HandlerMethod current : oldList) {
                if (!current.equals(handlerMethod)) {
                    newList.add(current);
                }
            }
            this.nameLookup.put(name, newList);
        }
    }

    /* loaded from: spring-webmvc-4.3.25.RELEASE.jar:org/springframework/web/servlet/handler/AbstractHandlerMethodMapping$MappingRegistration.class */
    private static class MappingRegistration<T> {
        private final T mapping;
        private final HandlerMethod handlerMethod;
        private final List<String> directUrls;
        private final String mappingName;

        public MappingRegistration(T mapping, HandlerMethod handlerMethod, List<String> directUrls, String mappingName) {
            Assert.notNull(mapping, "Mapping must not be null");
            Assert.notNull(handlerMethod, "HandlerMethod must not be null");
            this.mapping = mapping;
            this.handlerMethod = handlerMethod;
            this.directUrls = directUrls != null ? directUrls : Collections.emptyList();
            this.mappingName = mappingName;
        }

        public T getMapping() {
            return this.mapping;
        }

        public HandlerMethod getHandlerMethod() {
            return this.handlerMethod;
        }

        public List<String> getDirectUrls() {
            return this.directUrls;
        }

        public String getMappingName() {
            return this.mappingName;
        }
    }

    /* loaded from: spring-webmvc-4.3.25.RELEASE.jar:org/springframework/web/servlet/handler/AbstractHandlerMethodMapping$Match.class */
    private class Match {
        private final T mapping;
        private final HandlerMethod handlerMethod;

        public Match(T mapping, HandlerMethod handlerMethod) {
            this.mapping = mapping;
            this.handlerMethod = handlerMethod;
        }

        public String toString() {
            return this.mapping.toString();
        }
    }

    /* loaded from: spring-webmvc-4.3.25.RELEASE.jar:org/springframework/web/servlet/handler/AbstractHandlerMethodMapping$MatchComparator.class */
    private class MatchComparator implements Comparator<AbstractHandlerMethodMapping<T>.Match> {
        private final Comparator<T> comparator;

        public MatchComparator(Comparator<T> comparator) {
            this.comparator = comparator;
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // java.util.Comparator
        public int compare(AbstractHandlerMethodMapping<T>.Match match, AbstractHandlerMethodMapping<T>.Match match2) {
            return this.comparator.compare(((Match) match).mapping, ((Match) match2).mapping);
        }
    }

    /* loaded from: spring-webmvc-4.3.25.RELEASE.jar:org/springframework/web/servlet/handler/AbstractHandlerMethodMapping$EmptyHandler.class */
    private static class EmptyHandler {
        private EmptyHandler() {
        }

        public void handle() {
            throw new UnsupportedOperationException("Not implemented");
        }
    }
}
