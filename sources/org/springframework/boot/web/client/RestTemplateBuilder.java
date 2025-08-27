package org.springframework.boot.web.client;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.http.client.AbstractClientHttpRequestFactoryWrapper;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.client.support.BasicAuthorizationInterceptor;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriTemplateHandler;

/* loaded from: spring-boot-1.5.22.RELEASE.jar:org/springframework/boot/web/client/RestTemplateBuilder.class */
public class RestTemplateBuilder {
    private static final Map<String, String> REQUEST_FACTORY_CANDIDATES;
    private final boolean detectRequestFactory;
    private final String rootUri;
    private final Set<HttpMessageConverter<?>> messageConverters;
    private final ClientHttpRequestFactory requestFactory;
    private final UriTemplateHandler uriTemplateHandler;
    private final ResponseErrorHandler errorHandler;
    private final BasicAuthorizationInterceptor basicAuthorization;
    private final Set<RestTemplateCustomizer> restTemplateCustomizers;
    private final Set<RequestFactoryCustomizer> requestFactoryCustomizers;
    private final Set<ClientHttpRequestInterceptor> interceptors;

    /* loaded from: spring-boot-1.5.22.RELEASE.jar:org/springframework/boot/web/client/RestTemplateBuilder$RequestFactoryCustomizer.class */
    private interface RequestFactoryCustomizer {
        void customize(ClientHttpRequestFactory clientHttpRequestFactory);
    }

    static {
        Map<String, String> candidates = new LinkedHashMap<>();
        candidates.put("org.apache.http.client.HttpClient", "org.springframework.http.client.HttpComponentsClientHttpRequestFactory");
        candidates.put("okhttp3.OkHttpClient", "org.springframework.http.client.OkHttp3ClientHttpRequestFactory");
        candidates.put("com.squareup.okhttp.OkHttpClient", "org.springframework.http.client.OkHttpClientHttpRequestFactory");
        candidates.put("io.netty.channel.EventLoopGroup", "org.springframework.http.client.Netty4ClientHttpRequestFactory");
        REQUEST_FACTORY_CANDIDATES = Collections.unmodifiableMap(candidates);
    }

    public RestTemplateBuilder(RestTemplateCustomizer... customizers) {
        Assert.notNull(customizers, "Customizers must not be null");
        this.detectRequestFactory = true;
        this.rootUri = null;
        this.messageConverters = null;
        this.requestFactory = null;
        this.uriTemplateHandler = null;
        this.errorHandler = null;
        this.basicAuthorization = null;
        this.restTemplateCustomizers = Collections.unmodifiableSet(new LinkedHashSet(Arrays.asList(customizers)));
        this.requestFactoryCustomizers = Collections.emptySet();
        this.interceptors = Collections.emptySet();
    }

    private RestTemplateBuilder(boolean detectRequestFactory, String rootUri, Set<HttpMessageConverter<?>> messageConverters, ClientHttpRequestFactory requestFactory, UriTemplateHandler uriTemplateHandler, ResponseErrorHandler errorHandler, BasicAuthorizationInterceptor basicAuthorization, Set<RestTemplateCustomizer> restTemplateCustomizers, Set<RequestFactoryCustomizer> requestFactoryCustomizers, Set<ClientHttpRequestInterceptor> interceptors) {
        this.detectRequestFactory = detectRequestFactory;
        this.rootUri = rootUri;
        this.messageConverters = messageConverters;
        this.requestFactory = requestFactory;
        this.uriTemplateHandler = uriTemplateHandler;
        this.errorHandler = errorHandler;
        this.basicAuthorization = basicAuthorization;
        this.restTemplateCustomizers = restTemplateCustomizers;
        this.requestFactoryCustomizers = requestFactoryCustomizers;
        this.interceptors = interceptors;
    }

    public RestTemplateBuilder detectRequestFactory(boolean detectRequestFactory) {
        return new RestTemplateBuilder(detectRequestFactory, this.rootUri, this.messageConverters, this.requestFactory, this.uriTemplateHandler, this.errorHandler, this.basicAuthorization, this.restTemplateCustomizers, this.requestFactoryCustomizers, this.interceptors);
    }

    public RestTemplateBuilder rootUri(String rootUri) {
        return new RestTemplateBuilder(this.detectRequestFactory, rootUri, this.messageConverters, this.requestFactory, this.uriTemplateHandler, this.errorHandler, this.basicAuthorization, this.restTemplateCustomizers, this.requestFactoryCustomizers, this.interceptors);
    }

    public RestTemplateBuilder messageConverters(HttpMessageConverter<?>... messageConverters) {
        Assert.notNull(messageConverters, "MessageConverters must not be null");
        return messageConverters(Arrays.asList(messageConverters));
    }

    public RestTemplateBuilder messageConverters(Collection<? extends HttpMessageConverter<?>> messageConverters) {
        Assert.notNull(messageConverters, "MessageConverters must not be null");
        return new RestTemplateBuilder(this.detectRequestFactory, this.rootUri, Collections.unmodifiableSet(new LinkedHashSet(messageConverters)), this.requestFactory, this.uriTemplateHandler, this.errorHandler, this.basicAuthorization, this.restTemplateCustomizers, this.requestFactoryCustomizers, this.interceptors);
    }

    public RestTemplateBuilder additionalMessageConverters(HttpMessageConverter<?>... messageConverters) {
        Assert.notNull(messageConverters, "MessageConverters must not be null");
        return additionalMessageConverters(Arrays.asList(messageConverters));
    }

    public RestTemplateBuilder additionalMessageConverters(Collection<? extends HttpMessageConverter<?>> messageConverters) {
        Assert.notNull(messageConverters, "MessageConverters must not be null");
        return new RestTemplateBuilder(this.detectRequestFactory, this.rootUri, append((Set) this.messageConverters, (Collection) messageConverters), this.requestFactory, this.uriTemplateHandler, this.errorHandler, this.basicAuthorization, this.restTemplateCustomizers, this.requestFactoryCustomizers, this.interceptors);
    }

    public RestTemplateBuilder defaultMessageConverters() {
        return new RestTemplateBuilder(this.detectRequestFactory, this.rootUri, Collections.unmodifiableSet(new LinkedHashSet(new RestTemplate().getMessageConverters())), this.requestFactory, this.uriTemplateHandler, this.errorHandler, this.basicAuthorization, this.restTemplateCustomizers, this.requestFactoryCustomizers, this.interceptors);
    }

    public RestTemplateBuilder interceptors(ClientHttpRequestInterceptor... interceptors) {
        Assert.notNull(interceptors, "interceptors must not be null");
        return interceptors(Arrays.asList(interceptors));
    }

    public RestTemplateBuilder interceptors(Collection<ClientHttpRequestInterceptor> interceptors) {
        Assert.notNull(interceptors, "interceptors must not be null");
        return new RestTemplateBuilder(this.detectRequestFactory, this.rootUri, this.messageConverters, this.requestFactory, this.uriTemplateHandler, this.errorHandler, this.basicAuthorization, this.restTemplateCustomizers, this.requestFactoryCustomizers, Collections.unmodifiableSet(new LinkedHashSet(interceptors)));
    }

    public RestTemplateBuilder additionalInterceptors(ClientHttpRequestInterceptor... interceptors) {
        Assert.notNull(interceptors, "interceptors must not be null");
        return additionalInterceptors(Arrays.asList(interceptors));
    }

    public RestTemplateBuilder additionalInterceptors(Collection<? extends ClientHttpRequestInterceptor> interceptors) {
        Assert.notNull(interceptors, "interceptors must not be null");
        return new RestTemplateBuilder(this.detectRequestFactory, this.rootUri, this.messageConverters, this.requestFactory, this.uriTemplateHandler, this.errorHandler, this.basicAuthorization, this.restTemplateCustomizers, this.requestFactoryCustomizers, append((Set) this.interceptors, (Collection) interceptors));
    }

    public RestTemplateBuilder requestFactory(Class<? extends ClientHttpRequestFactory> requestFactory) {
        Assert.notNull(requestFactory, "RequestFactory must not be null");
        return requestFactory(createRequestFactory(requestFactory));
    }

    private ClientHttpRequestFactory createRequestFactory(Class<? extends ClientHttpRequestFactory> requestFactory) throws NoSuchMethodException, SecurityException {
        try {
            Constructor<?> constructor = requestFactory.getDeclaredConstructor(new Class[0]);
            constructor.setAccessible(true);
            return constructor.newInstance(new Object[0]);
        } catch (Exception ex) {
            throw new IllegalStateException(ex);
        }
    }

    public RestTemplateBuilder requestFactory(ClientHttpRequestFactory requestFactory) {
        Assert.notNull(requestFactory, "RequestFactory must not be null");
        return new RestTemplateBuilder(this.detectRequestFactory, this.rootUri, this.messageConverters, requestFactory, this.uriTemplateHandler, this.errorHandler, this.basicAuthorization, this.restTemplateCustomizers, this.requestFactoryCustomizers, this.interceptors);
    }

    public RestTemplateBuilder uriTemplateHandler(UriTemplateHandler uriTemplateHandler) {
        Assert.notNull(uriTemplateHandler, "UriTemplateHandler must not be null");
        return new RestTemplateBuilder(this.detectRequestFactory, this.rootUri, this.messageConverters, this.requestFactory, uriTemplateHandler, this.errorHandler, this.basicAuthorization, this.restTemplateCustomizers, this.requestFactoryCustomizers, this.interceptors);
    }

    public RestTemplateBuilder errorHandler(ResponseErrorHandler errorHandler) {
        Assert.notNull(errorHandler, "ErrorHandler must not be null");
        return new RestTemplateBuilder(this.detectRequestFactory, this.rootUri, this.messageConverters, this.requestFactory, this.uriTemplateHandler, errorHandler, this.basicAuthorization, this.restTemplateCustomizers, this.requestFactoryCustomizers, this.interceptors);
    }

    public RestTemplateBuilder basicAuthorization(String username, String password) {
        return new RestTemplateBuilder(this.detectRequestFactory, this.rootUri, this.messageConverters, this.requestFactory, this.uriTemplateHandler, this.errorHandler, new BasicAuthorizationInterceptor(username, password), this.restTemplateCustomizers, this.requestFactoryCustomizers, this.interceptors);
    }

    public RestTemplateBuilder customizers(RestTemplateCustomizer... restTemplateCustomizers) {
        Assert.notNull(restTemplateCustomizers, "RestTemplateCustomizers must not be null");
        return customizers(Arrays.asList(restTemplateCustomizers));
    }

    public RestTemplateBuilder customizers(Collection<? extends RestTemplateCustomizer> restTemplateCustomizers) {
        Assert.notNull(restTemplateCustomizers, "RestTemplateCustomizers must not be null");
        return new RestTemplateBuilder(this.detectRequestFactory, this.rootUri, this.messageConverters, this.requestFactory, this.uriTemplateHandler, this.errorHandler, this.basicAuthorization, Collections.unmodifiableSet(new LinkedHashSet(restTemplateCustomizers)), this.requestFactoryCustomizers, this.interceptors);
    }

    public RestTemplateBuilder additionalCustomizers(RestTemplateCustomizer... restTemplateCustomizers) {
        Assert.notNull(restTemplateCustomizers, "RestTemplateCustomizers must not be null");
        return additionalCustomizers(Arrays.asList(restTemplateCustomizers));
    }

    public RestTemplateBuilder additionalCustomizers(Collection<? extends RestTemplateCustomizer> customizers) {
        Assert.notNull(customizers, "RestTemplateCustomizers must not be null");
        return new RestTemplateBuilder(this.detectRequestFactory, this.rootUri, this.messageConverters, this.requestFactory, this.uriTemplateHandler, this.errorHandler, this.basicAuthorization, append((Set) this.restTemplateCustomizers, (Collection) customizers), this.requestFactoryCustomizers, this.interceptors);
    }

    public RestTemplateBuilder setConnectTimeout(int connectTimeout) {
        return new RestTemplateBuilder(this.detectRequestFactory, this.rootUri, this.messageConverters, this.requestFactory, this.uriTemplateHandler, this.errorHandler, this.basicAuthorization, this.restTemplateCustomizers, append((Set<Set<RequestFactoryCustomizer>>) this.requestFactoryCustomizers, (Set<RequestFactoryCustomizer>) new ConnectTimeoutRequestFactoryCustomizer(connectTimeout)), this.interceptors);
    }

    public RestTemplateBuilder setReadTimeout(int readTimeout) {
        return new RestTemplateBuilder(this.detectRequestFactory, this.rootUri, this.messageConverters, this.requestFactory, this.uriTemplateHandler, this.errorHandler, this.basicAuthorization, this.restTemplateCustomizers, append((Set<Set<RequestFactoryCustomizer>>) this.requestFactoryCustomizers, (Set<RequestFactoryCustomizer>) new ReadTimeoutRequestFactoryCustomizer(readTimeout)), this.interceptors);
    }

    public RestTemplate build() {
        return build(RestTemplate.class);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public <T extends RestTemplate> T build(Class<T> cls) {
        return (T) configure((RestTemplate) BeanUtils.instantiate(cls));
    }

    public <T extends RestTemplate> T configure(T restTemplate) throws IllegalArgumentException {
        configureRequestFactory(restTemplate);
        if (!CollectionUtils.isEmpty(this.messageConverters)) {
            restTemplate.setMessageConverters(new ArrayList(this.messageConverters));
        }
        if (this.uriTemplateHandler != null) {
            restTemplate.setUriTemplateHandler(this.uriTemplateHandler);
        }
        if (this.errorHandler != null) {
            restTemplate.setErrorHandler(this.errorHandler);
        }
        if (this.rootUri != null) {
            RootUriTemplateHandler.addTo(restTemplate, this.rootUri);
        }
        if (this.basicAuthorization != null) {
            restTemplate.getInterceptors().add(this.basicAuthorization);
        }
        if (!CollectionUtils.isEmpty(this.restTemplateCustomizers)) {
            for (RestTemplateCustomizer customizer : this.restTemplateCustomizers) {
                customizer.customize(restTemplate);
            }
        }
        restTemplate.getInterceptors().addAll(this.interceptors);
        return restTemplate;
    }

    private void configureRequestFactory(RestTemplate restTemplate) throws IllegalArgumentException {
        ClientHttpRequestFactory requestFactory = null;
        if (this.requestFactory != null) {
            requestFactory = this.requestFactory;
        } else if (this.detectRequestFactory) {
            requestFactory = detectRequestFactory();
        }
        if (requestFactory != null) {
            ClientHttpRequestFactory unwrappedRequestFactory = unwrapRequestFactoryIfNecessary(requestFactory);
            for (RequestFactoryCustomizer customizer : this.requestFactoryCustomizers) {
                customizer.customize(unwrappedRequestFactory);
            }
            restTemplate.setRequestFactory(requestFactory);
        }
    }

    private ClientHttpRequestFactory unwrapRequestFactoryIfNecessary(ClientHttpRequestFactory requestFactory) {
        if (!(requestFactory instanceof AbstractClientHttpRequestFactoryWrapper)) {
            return requestFactory;
        }
        ClientHttpRequestFactory unwrappedRequestFactory = requestFactory;
        Field field = ReflectionUtils.findField(AbstractClientHttpRequestFactoryWrapper.class, "requestFactory");
        ReflectionUtils.makeAccessible(field);
        do {
            unwrappedRequestFactory = (ClientHttpRequestFactory) ReflectionUtils.getField(field, unwrappedRequestFactory);
        } while (unwrappedRequestFactory instanceof AbstractClientHttpRequestFactoryWrapper);
        return unwrappedRequestFactory;
    }

    private ClientHttpRequestFactory detectRequestFactory() throws IllegalArgumentException {
        for (Map.Entry<String, String> candidate : REQUEST_FACTORY_CANDIDATES.entrySet()) {
            ClassLoader classLoader = getClass().getClassLoader();
            if (ClassUtils.isPresent(candidate.getKey(), classLoader)) {
                Class<?> factoryClass = ClassUtils.resolveClassName(candidate.getValue(), classLoader);
                ClientHttpRequestFactory requestFactory = (ClientHttpRequestFactory) BeanUtils.instantiate(factoryClass);
                initializeIfNecessary(requestFactory);
                return requestFactory;
            }
        }
        return new SimpleClientHttpRequestFactory();
    }

    private void initializeIfNecessary(ClientHttpRequestFactory requestFactory) {
        if (requestFactory instanceof InitializingBean) {
            try {
                ((InitializingBean) requestFactory).afterPropertiesSet();
            } catch (Exception ex) {
                throw new IllegalStateException("Failed to initialize request factory " + requestFactory, ex);
            }
        }
    }

    private <T> Set<T> append(Set<T> set, T addition) {
        Set<T> result = new LinkedHashSet<>(set != null ? set : Collections.emptySet());
        result.add(addition);
        return Collections.unmodifiableSet(result);
    }

    private <T> Set<T> append(Set<T> set, Collection<? extends T> additions) {
        Set<T> result = new LinkedHashSet<>(set != null ? set : Collections.emptySet());
        result.addAll(additions);
        return Collections.unmodifiableSet(result);
    }

    /* loaded from: spring-boot-1.5.22.RELEASE.jar:org/springframework/boot/web/client/RestTemplateBuilder$TimeoutRequestFactoryCustomizer.class */
    private static abstract class TimeoutRequestFactoryCustomizer implements RequestFactoryCustomizer {
        private final int timeout;
        private final String methodName;

        TimeoutRequestFactoryCustomizer(int timeout, String methodName) {
            this.timeout = timeout;
            this.methodName = methodName;
        }

        @Override // org.springframework.boot.web.client.RestTemplateBuilder.RequestFactoryCustomizer
        public void customize(ClientHttpRequestFactory factory) {
            ReflectionUtils.invokeMethod(findMethod(factory), factory, Integer.valueOf(this.timeout));
        }

        private Method findMethod(ClientHttpRequestFactory factory) {
            Method method = ReflectionUtils.findMethod(factory.getClass(), this.methodName, Integer.TYPE);
            if (method != null) {
                return method;
            }
            throw new IllegalStateException("Request factory " + factory.getClass() + " does not have a " + this.methodName + "(int) method");
        }
    }

    /* loaded from: spring-boot-1.5.22.RELEASE.jar:org/springframework/boot/web/client/RestTemplateBuilder$ReadTimeoutRequestFactoryCustomizer.class */
    private static class ReadTimeoutRequestFactoryCustomizer extends TimeoutRequestFactoryCustomizer {
        ReadTimeoutRequestFactoryCustomizer(int readTimeout) {
            super(readTimeout, "setReadTimeout");
        }
    }

    /* loaded from: spring-boot-1.5.22.RELEASE.jar:org/springframework/boot/web/client/RestTemplateBuilder$ConnectTimeoutRequestFactoryCustomizer.class */
    private static class ConnectTimeoutRequestFactoryCustomizer extends TimeoutRequestFactoryCustomizer {
        ConnectTimeoutRequestFactoryCustomizer(int connectTimeout) {
            super(connectTimeout, "setConnectTimeout");
        }
    }
}
