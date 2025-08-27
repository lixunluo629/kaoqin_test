package org.springframework.web.client;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.support.InterceptingHttpAccessor;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.GenericHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.http.converter.ResourceHttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.feed.AtomFeedHttpMessageConverter;
import org.springframework.http.converter.feed.RssChannelHttpMessageConverter;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.support.AllEncompassingFormHttpMessageConverter;
import org.springframework.http.converter.xml.Jaxb2RootElementHttpMessageConverter;
import org.springframework.http.converter.xml.MappingJackson2XmlHttpMessageConverter;
import org.springframework.http.converter.xml.SourceHttpMessageConverter;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.web.util.AbstractUriTemplateHandler;
import org.springframework.web.util.DefaultUriTemplateHandler;
import org.springframework.web.util.UriTemplateHandler;

/* loaded from: spring-web-4.3.25.RELEASE.jar:org/springframework/web/client/RestTemplate.class */
public class RestTemplate extends InterceptingHttpAccessor implements RestOperations {
    private static boolean romePresent = ClassUtils.isPresent("com.rometools.rome.feed.WireFeed", RestTemplate.class.getClassLoader());
    private static final boolean jaxb2Present = ClassUtils.isPresent("javax.xml.bind.Binder", RestTemplate.class.getClassLoader());
    private static final boolean jackson2Present;
    private static final boolean jackson2XmlPresent;
    private static final boolean gsonPresent;
    private final List<HttpMessageConverter<?>> messageConverters;
    private ResponseErrorHandler errorHandler;
    private UriTemplateHandler uriTemplateHandler;
    private final ResponseExtractor<HttpHeaders> headersExtractor;

    static {
        jackson2Present = ClassUtils.isPresent("com.fasterxml.jackson.databind.ObjectMapper", RestTemplate.class.getClassLoader()) && ClassUtils.isPresent("com.fasterxml.jackson.core.JsonGenerator", RestTemplate.class.getClassLoader());
        jackson2XmlPresent = ClassUtils.isPresent("com.fasterxml.jackson.dataformat.xml.XmlMapper", RestTemplate.class.getClassLoader());
        gsonPresent = ClassUtils.isPresent("com.google.gson.Gson", RestTemplate.class.getClassLoader());
    }

    public RestTemplate() {
        this.messageConverters = new ArrayList();
        this.errorHandler = new DefaultResponseErrorHandler();
        this.uriTemplateHandler = new DefaultUriTemplateHandler();
        this.headersExtractor = new HeadersExtractor();
        this.messageConverters.add(new ByteArrayHttpMessageConverter());
        this.messageConverters.add(new StringHttpMessageConverter());
        this.messageConverters.add(new ResourceHttpMessageConverter());
        this.messageConverters.add(new SourceHttpMessageConverter());
        this.messageConverters.add(new AllEncompassingFormHttpMessageConverter());
        if (romePresent) {
            this.messageConverters.add(new AtomFeedHttpMessageConverter());
            this.messageConverters.add(new RssChannelHttpMessageConverter());
        }
        if (jackson2XmlPresent) {
            this.messageConverters.add(new MappingJackson2XmlHttpMessageConverter());
        } else if (jaxb2Present) {
            this.messageConverters.add(new Jaxb2RootElementHttpMessageConverter());
        }
        if (jackson2Present) {
            this.messageConverters.add(new MappingJackson2HttpMessageConverter());
        } else if (gsonPresent) {
            this.messageConverters.add(new GsonHttpMessageConverter());
        }
    }

    public RestTemplate(ClientHttpRequestFactory requestFactory) {
        this();
        setRequestFactory(requestFactory);
    }

    public RestTemplate(List<HttpMessageConverter<?>> messageConverters) {
        this.messageConverters = new ArrayList();
        this.errorHandler = new DefaultResponseErrorHandler();
        this.uriTemplateHandler = new DefaultUriTemplateHandler();
        this.headersExtractor = new HeadersExtractor();
        Assert.notEmpty(messageConverters, "At least one HttpMessageConverter required");
        this.messageConverters.addAll(messageConverters);
    }

    public void setMessageConverters(List<HttpMessageConverter<?>> messageConverters) {
        Assert.notEmpty(messageConverters, "At least one HttpMessageConverter required");
        if (this.messageConverters != messageConverters) {
            this.messageConverters.clear();
            this.messageConverters.addAll(messageConverters);
        }
    }

    public List<HttpMessageConverter<?>> getMessageConverters() {
        return this.messageConverters;
    }

    public void setErrorHandler(ResponseErrorHandler errorHandler) {
        Assert.notNull(errorHandler, "ResponseErrorHandler must not be null");
        this.errorHandler = errorHandler;
    }

    public ResponseErrorHandler getErrorHandler() {
        return this.errorHandler;
    }

    public void setDefaultUriVariables(Map<String, ?> defaultUriVariables) {
        Assert.isInstanceOf(AbstractUriTemplateHandler.class, this.uriTemplateHandler, "Can only use this property in conjunction with an AbstractUriTemplateHandler");
        ((AbstractUriTemplateHandler) this.uriTemplateHandler).setDefaultUriVariables(defaultUriVariables);
    }

    public void setUriTemplateHandler(UriTemplateHandler handler) {
        Assert.notNull(handler, "UriTemplateHandler must not be null");
        this.uriTemplateHandler = handler;
    }

    public UriTemplateHandler getUriTemplateHandler() {
        return this.uriTemplateHandler;
    }

    @Override // org.springframework.web.client.RestOperations
    public <T> T getForObject(String str, Class<T> cls, Object... objArr) throws RestClientException {
        return (T) execute(str, HttpMethod.GET, acceptHeaderRequestCallback(cls), new HttpMessageConverterExtractor(cls, getMessageConverters(), this.logger), objArr);
    }

    @Override // org.springframework.web.client.RestOperations
    public <T> T getForObject(String str, Class<T> cls, Map<String, ?> map) throws RestClientException {
        return (T) execute(str, HttpMethod.GET, acceptHeaderRequestCallback(cls), new HttpMessageConverterExtractor(cls, getMessageConverters(), this.logger), map);
    }

    @Override // org.springframework.web.client.RestOperations
    public <T> T getForObject(URI uri, Class<T> cls) throws RestClientException {
        return (T) execute(uri, HttpMethod.GET, acceptHeaderRequestCallback(cls), new HttpMessageConverterExtractor(cls, getMessageConverters(), this.logger));
    }

    @Override // org.springframework.web.client.RestOperations
    public <T> ResponseEntity<T> getForEntity(String url, Class<T> responseType, Object... uriVariables) throws RestClientException {
        RequestCallback requestCallback = acceptHeaderRequestCallback(responseType);
        ResponseExtractor<ResponseEntity<T>> responseExtractor = responseEntityExtractor(responseType);
        return (ResponseEntity) execute(url, HttpMethod.GET, requestCallback, responseExtractor, uriVariables);
    }

    @Override // org.springframework.web.client.RestOperations
    public <T> ResponseEntity<T> getForEntity(String url, Class<T> responseType, Map<String, ?> uriVariables) throws RestClientException {
        RequestCallback requestCallback = acceptHeaderRequestCallback(responseType);
        ResponseExtractor<ResponseEntity<T>> responseExtractor = responseEntityExtractor(responseType);
        return (ResponseEntity) execute(url, HttpMethod.GET, requestCallback, responseExtractor, uriVariables);
    }

    @Override // org.springframework.web.client.RestOperations
    public <T> ResponseEntity<T> getForEntity(URI url, Class<T> responseType) throws RestClientException {
        RequestCallback requestCallback = acceptHeaderRequestCallback(responseType);
        ResponseExtractor<ResponseEntity<T>> responseExtractor = responseEntityExtractor(responseType);
        return (ResponseEntity) execute(url, HttpMethod.GET, requestCallback, responseExtractor);
    }

    @Override // org.springframework.web.client.RestOperations
    public HttpHeaders headForHeaders(String url, Object... uriVariables) throws RestClientException {
        return (HttpHeaders) execute(url, HttpMethod.HEAD, (RequestCallback) null, headersExtractor(), uriVariables);
    }

    @Override // org.springframework.web.client.RestOperations
    public HttpHeaders headForHeaders(String url, Map<String, ?> uriVariables) throws RestClientException {
        return (HttpHeaders) execute(url, HttpMethod.HEAD, (RequestCallback) null, headersExtractor(), uriVariables);
    }

    @Override // org.springframework.web.client.RestOperations
    public HttpHeaders headForHeaders(URI url) throws RestClientException {
        return (HttpHeaders) execute(url, HttpMethod.HEAD, null, headersExtractor());
    }

    @Override // org.springframework.web.client.RestOperations
    public URI postForLocation(String url, Object request, Object... uriVariables) throws RestClientException {
        RequestCallback requestCallback = httpEntityCallback(request);
        HttpHeaders headers = (HttpHeaders) execute(url, HttpMethod.POST, requestCallback, headersExtractor(), uriVariables);
        return headers.getLocation();
    }

    @Override // org.springframework.web.client.RestOperations
    public URI postForLocation(String url, Object request, Map<String, ?> uriVariables) throws RestClientException {
        RequestCallback requestCallback = httpEntityCallback(request);
        HttpHeaders headers = (HttpHeaders) execute(url, HttpMethod.POST, requestCallback, headersExtractor(), uriVariables);
        return headers.getLocation();
    }

    @Override // org.springframework.web.client.RestOperations
    public URI postForLocation(URI url, Object request) throws RestClientException {
        RequestCallback requestCallback = httpEntityCallback(request);
        HttpHeaders headers = (HttpHeaders) execute(url, HttpMethod.POST, requestCallback, headersExtractor());
        return headers.getLocation();
    }

    @Override // org.springframework.web.client.RestOperations
    public <T> T postForObject(String str, Object obj, Class<T> cls, Object... objArr) throws RestClientException {
        return (T) execute(str, HttpMethod.POST, httpEntityCallback(obj, cls), new HttpMessageConverterExtractor(cls, getMessageConverters(), this.logger), objArr);
    }

    @Override // org.springframework.web.client.RestOperations
    public <T> T postForObject(String str, Object obj, Class<T> cls, Map<String, ?> map) throws RestClientException {
        return (T) execute(str, HttpMethod.POST, httpEntityCallback(obj, cls), new HttpMessageConverterExtractor(cls, getMessageConverters(), this.logger), map);
    }

    @Override // org.springframework.web.client.RestOperations
    public <T> T postForObject(URI uri, Object obj, Class<T> cls) throws RestClientException {
        return (T) execute(uri, HttpMethod.POST, httpEntityCallback(obj, cls), new HttpMessageConverterExtractor((Class) cls, getMessageConverters()));
    }

    @Override // org.springframework.web.client.RestOperations
    public <T> ResponseEntity<T> postForEntity(String url, Object request, Class<T> responseType, Object... uriVariables) throws RestClientException {
        RequestCallback requestCallback = httpEntityCallback(request, responseType);
        ResponseExtractor<ResponseEntity<T>> responseExtractor = responseEntityExtractor(responseType);
        return (ResponseEntity) execute(url, HttpMethod.POST, requestCallback, responseExtractor, uriVariables);
    }

    @Override // org.springframework.web.client.RestOperations
    public <T> ResponseEntity<T> postForEntity(String url, Object request, Class<T> responseType, Map<String, ?> uriVariables) throws RestClientException {
        RequestCallback requestCallback = httpEntityCallback(request, responseType);
        ResponseExtractor<ResponseEntity<T>> responseExtractor = responseEntityExtractor(responseType);
        return (ResponseEntity) execute(url, HttpMethod.POST, requestCallback, responseExtractor, uriVariables);
    }

    @Override // org.springframework.web.client.RestOperations
    public <T> ResponseEntity<T> postForEntity(URI url, Object request, Class<T> responseType) throws RestClientException {
        RequestCallback requestCallback = httpEntityCallback(request, responseType);
        ResponseExtractor<ResponseEntity<T>> responseExtractor = responseEntityExtractor(responseType);
        return (ResponseEntity) execute(url, HttpMethod.POST, requestCallback, responseExtractor);
    }

    @Override // org.springframework.web.client.RestOperations
    public void put(String url, Object request, Object... uriVariables) throws RestClientException {
        RequestCallback requestCallback = httpEntityCallback(request);
        execute(url, HttpMethod.PUT, requestCallback, (ResponseExtractor) null, uriVariables);
    }

    @Override // org.springframework.web.client.RestOperations
    public void put(String url, Object request, Map<String, ?> uriVariables) throws RestClientException {
        RequestCallback requestCallback = httpEntityCallback(request);
        execute(url, HttpMethod.PUT, requestCallback, (ResponseExtractor) null, uriVariables);
    }

    @Override // org.springframework.web.client.RestOperations
    public void put(URI url, Object request) throws RestClientException {
        RequestCallback requestCallback = httpEntityCallback(request);
        execute(url, HttpMethod.PUT, requestCallback, null);
    }

    @Override // org.springframework.web.client.RestOperations
    public <T> T patchForObject(String str, Object obj, Class<T> cls, Object... objArr) throws RestClientException {
        return (T) execute(str, HttpMethod.PATCH, httpEntityCallback(obj, cls), new HttpMessageConverterExtractor(cls, getMessageConverters(), this.logger), objArr);
    }

    @Override // org.springframework.web.client.RestOperations
    public <T> T patchForObject(String str, Object obj, Class<T> cls, Map<String, ?> map) throws RestClientException {
        return (T) execute(str, HttpMethod.PATCH, httpEntityCallback(obj, cls), new HttpMessageConverterExtractor(cls, getMessageConverters(), this.logger), map);
    }

    @Override // org.springframework.web.client.RestOperations
    public <T> T patchForObject(URI uri, Object obj, Class<T> cls) throws RestClientException {
        return (T) execute(uri, HttpMethod.PATCH, httpEntityCallback(obj, cls), new HttpMessageConverterExtractor((Class) cls, getMessageConverters()));
    }

    @Override // org.springframework.web.client.RestOperations
    public void delete(String url, Object... uriVariables) throws RestClientException {
        execute(url, HttpMethod.DELETE, (RequestCallback) null, (ResponseExtractor) null, uriVariables);
    }

    @Override // org.springframework.web.client.RestOperations
    public void delete(String url, Map<String, ?> uriVariables) throws RestClientException {
        execute(url, HttpMethod.DELETE, (RequestCallback) null, (ResponseExtractor) null, uriVariables);
    }

    @Override // org.springframework.web.client.RestOperations
    public void delete(URI url) throws RestClientException {
        execute(url, HttpMethod.DELETE, null, null);
    }

    @Override // org.springframework.web.client.RestOperations
    public Set<HttpMethod> optionsForAllow(String url, Object... uriVariables) throws RestClientException {
        ResponseExtractor<HttpHeaders> headersExtractor = headersExtractor();
        HttpHeaders headers = (HttpHeaders) execute(url, HttpMethod.OPTIONS, (RequestCallback) null, headersExtractor, uriVariables);
        return headers.getAllow();
    }

    @Override // org.springframework.web.client.RestOperations
    public Set<HttpMethod> optionsForAllow(String url, Map<String, ?> uriVariables) throws RestClientException {
        ResponseExtractor<HttpHeaders> headersExtractor = headersExtractor();
        HttpHeaders headers = (HttpHeaders) execute(url, HttpMethod.OPTIONS, (RequestCallback) null, headersExtractor, uriVariables);
        return headers.getAllow();
    }

    @Override // org.springframework.web.client.RestOperations
    public Set<HttpMethod> optionsForAllow(URI url) throws RestClientException {
        ResponseExtractor<HttpHeaders> headersExtractor = headersExtractor();
        HttpHeaders headers = (HttpHeaders) execute(url, HttpMethod.OPTIONS, null, headersExtractor);
        return headers.getAllow();
    }

    @Override // org.springframework.web.client.RestOperations
    public <T> ResponseEntity<T> exchange(String url, HttpMethod method, HttpEntity<?> requestEntity, Class<T> responseType, Object... uriVariables) throws RestClientException {
        RequestCallback requestCallback = httpEntityCallback(requestEntity, responseType);
        ResponseExtractor<ResponseEntity<T>> responseExtractor = responseEntityExtractor(responseType);
        return (ResponseEntity) execute(url, method, requestCallback, responseExtractor, uriVariables);
    }

    @Override // org.springframework.web.client.RestOperations
    public <T> ResponseEntity<T> exchange(String url, HttpMethod method, HttpEntity<?> requestEntity, Class<T> responseType, Map<String, ?> uriVariables) throws RestClientException {
        RequestCallback requestCallback = httpEntityCallback(requestEntity, responseType);
        ResponseExtractor<ResponseEntity<T>> responseExtractor = responseEntityExtractor(responseType);
        return (ResponseEntity) execute(url, method, requestCallback, responseExtractor, uriVariables);
    }

    @Override // org.springframework.web.client.RestOperations
    public <T> ResponseEntity<T> exchange(URI url, HttpMethod method, HttpEntity<?> requestEntity, Class<T> responseType) throws RestClientException {
        RequestCallback requestCallback = httpEntityCallback(requestEntity, responseType);
        ResponseExtractor<ResponseEntity<T>> responseExtractor = responseEntityExtractor(responseType);
        return (ResponseEntity) execute(url, method, requestCallback, responseExtractor);
    }

    @Override // org.springframework.web.client.RestOperations
    public <T> ResponseEntity<T> exchange(String url, HttpMethod method, HttpEntity<?> requestEntity, ParameterizedTypeReference<T> responseType, Object... uriVariables) throws RestClientException {
        Type type = responseType.getType();
        RequestCallback requestCallback = httpEntityCallback(requestEntity, type);
        ResponseExtractor<ResponseEntity<T>> responseExtractor = responseEntityExtractor(type);
        return (ResponseEntity) execute(url, method, requestCallback, responseExtractor, uriVariables);
    }

    @Override // org.springframework.web.client.RestOperations
    public <T> ResponseEntity<T> exchange(String url, HttpMethod method, HttpEntity<?> requestEntity, ParameterizedTypeReference<T> responseType, Map<String, ?> uriVariables) throws RestClientException {
        Type type = responseType.getType();
        RequestCallback requestCallback = httpEntityCallback(requestEntity, type);
        ResponseExtractor<ResponseEntity<T>> responseExtractor = responseEntityExtractor(type);
        return (ResponseEntity) execute(url, method, requestCallback, responseExtractor, uriVariables);
    }

    @Override // org.springframework.web.client.RestOperations
    public <T> ResponseEntity<T> exchange(URI url, HttpMethod method, HttpEntity<?> requestEntity, ParameterizedTypeReference<T> responseType) throws RestClientException {
        Type type = responseType.getType();
        RequestCallback requestCallback = httpEntityCallback(requestEntity, type);
        ResponseExtractor<ResponseEntity<T>> responseExtractor = responseEntityExtractor(type);
        return (ResponseEntity) execute(url, method, requestCallback, responseExtractor);
    }

    @Override // org.springframework.web.client.RestOperations
    public <T> ResponseEntity<T> exchange(RequestEntity<?> requestEntity, Class<T> responseType) throws RestClientException {
        Assert.notNull(requestEntity, "RequestEntity must not be null");
        RequestCallback requestCallback = httpEntityCallback(requestEntity, responseType);
        ResponseExtractor<ResponseEntity<T>> responseExtractor = responseEntityExtractor(responseType);
        return (ResponseEntity) execute(requestEntity.getUrl(), requestEntity.getMethod(), requestCallback, responseExtractor);
    }

    @Override // org.springframework.web.client.RestOperations
    public <T> ResponseEntity<T> exchange(RequestEntity<?> requestEntity, ParameterizedTypeReference<T> responseType) throws RestClientException {
        Assert.notNull(requestEntity, "RequestEntity must not be null");
        Type type = responseType.getType();
        RequestCallback requestCallback = httpEntityCallback(requestEntity, type);
        ResponseExtractor<ResponseEntity<T>> responseExtractor = responseEntityExtractor(type);
        return (ResponseEntity) execute(requestEntity.getUrl(), requestEntity.getMethod(), requestCallback, responseExtractor);
    }

    @Override // org.springframework.web.client.RestOperations
    public <T> T execute(String str, HttpMethod httpMethod, RequestCallback requestCallback, ResponseExtractor<T> responseExtractor, Object... objArr) throws RestClientException {
        return (T) doExecute(getUriTemplateHandler().expand(str, objArr), httpMethod, requestCallback, responseExtractor);
    }

    @Override // org.springframework.web.client.RestOperations
    public <T> T execute(String str, HttpMethod httpMethod, RequestCallback requestCallback, ResponseExtractor<T> responseExtractor, Map<String, ?> map) throws RestClientException {
        return (T) doExecute(getUriTemplateHandler().expand(str, map), httpMethod, requestCallback, responseExtractor);
    }

    @Override // org.springframework.web.client.RestOperations
    public <T> T execute(URI uri, HttpMethod httpMethod, RequestCallback requestCallback, ResponseExtractor<T> responseExtractor) throws RestClientException {
        return (T) doExecute(uri, httpMethod, requestCallback, responseExtractor);
    }

    protected <T> T doExecute(URI url, HttpMethod method, RequestCallback requestCallback, ResponseExtractor<T> responseExtractor) throws RestClientException {
        Assert.notNull(url, "'url' must not be null");
        Assert.notNull(method, "'method' must not be null");
        ClientHttpResponse response = null;
        try {
            try {
                ClientHttpRequest request = createRequest(url, method);
                if (requestCallback != null) {
                    requestCallback.doWithRequest(request);
                }
                response = request.execute();
                handleResponse(url, method, response);
                if (responseExtractor != null) {
                    T tExtractData = responseExtractor.extractData(response);
                    if (response != null) {
                        response.close();
                    }
                    return tExtractData;
                }
                if (response != null) {
                    response.close();
                }
                return null;
            } catch (IOException ex) {
                String resource = url.toString();
                String query = url.getRawQuery();
                throw new ResourceAccessException("I/O error on " + method.name() + " request for \"" + (query != null ? resource.substring(0, resource.indexOf(63)) : resource) + "\": " + ex.getMessage(), ex);
            }
        } catch (Throwable th) {
            if (response != null) {
                response.close();
            }
            throw th;
        }
    }

    protected void handleResponse(URI url, HttpMethod method, ClientHttpResponse response) throws IOException {
        ResponseErrorHandler errorHandler = getErrorHandler();
        boolean hasError = errorHandler.hasError(response);
        if (this.logger.isDebugEnabled()) {
            try {
                this.logger.debug(method.name() + " request for \"" + url + "\" resulted in " + response.getRawStatusCode() + " (" + response.getStatusText() + ")" + (hasError ? "; invoking error handler" : ""));
            } catch (IOException e) {
            }
        }
        if (hasError) {
            errorHandler.handleError(response);
        }
    }

    protected <T> RequestCallback acceptHeaderRequestCallback(Class<T> responseType) {
        return new AcceptHeaderRequestCallback(responseType);
    }

    protected <T> RequestCallback httpEntityCallback(Object requestBody) {
        return new HttpEntityRequestCallback(requestBody);
    }

    protected <T> RequestCallback httpEntityCallback(Object requestBody, Type responseType) {
        return new HttpEntityRequestCallback(requestBody, responseType);
    }

    protected <T> ResponseExtractor<ResponseEntity<T>> responseEntityExtractor(Type responseType) {
        return new ResponseEntityResponseExtractor(responseType);
    }

    protected ResponseExtractor<HttpHeaders> headersExtractor() {
        return this.headersExtractor;
    }

    /* loaded from: spring-web-4.3.25.RELEASE.jar:org/springframework/web/client/RestTemplate$AcceptHeaderRequestCallback.class */
    private class AcceptHeaderRequestCallback implements RequestCallback {
        private final Type responseType;

        private AcceptHeaderRequestCallback(Type responseType) {
            this.responseType = responseType;
        }

        @Override // org.springframework.web.client.RequestCallback
        public void doWithRequest(ClientHttpRequest request) throws IOException {
            if (this.responseType != null) {
                Class<?> responseClass = null;
                if (this.responseType instanceof Class) {
                    responseClass = (Class) this.responseType;
                }
                Set<MediaType> allSupportedMediaTypes = new LinkedHashSet<>();
                for (HttpMessageConverter<?> converter : RestTemplate.this.getMessageConverters()) {
                    if (responseClass != null) {
                        if (converter.canRead(responseClass, null)) {
                            allSupportedMediaTypes.addAll(getSupportedMediaTypes(converter));
                        }
                    } else if (converter instanceof GenericHttpMessageConverter) {
                        GenericHttpMessageConverter<?> genericConverter = (GenericHttpMessageConverter) converter;
                        if (genericConverter.canRead(this.responseType, null, null)) {
                            allSupportedMediaTypes.addAll(getSupportedMediaTypes(converter));
                        }
                    }
                }
                if (!allSupportedMediaTypes.isEmpty()) {
                    List<MediaType> result = new ArrayList<>(allSupportedMediaTypes);
                    MediaType.sortBySpecificity(result);
                    if (RestTemplate.this.logger.isDebugEnabled()) {
                        RestTemplate.this.logger.debug("Setting request Accept header to " + allSupportedMediaTypes);
                    }
                    request.getHeaders().setAccept(result);
                }
            }
        }

        private List<MediaType> getSupportedMediaTypes(HttpMessageConverter<?> messageConverter) {
            List<MediaType> supportedMediaTypes = messageConverter.getSupportedMediaTypes();
            List<MediaType> result = new ArrayList<>(supportedMediaTypes.size());
            for (MediaType supportedMediaType : supportedMediaTypes) {
                if (supportedMediaType.getCharset() != null) {
                    supportedMediaType = new MediaType(supportedMediaType.getType(), supportedMediaType.getSubtype());
                }
                result.add(supportedMediaType);
            }
            return result;
        }
    }

    /* loaded from: spring-web-4.3.25.RELEASE.jar:org/springframework/web/client/RestTemplate$HttpEntityRequestCallback.class */
    private class HttpEntityRequestCallback extends AcceptHeaderRequestCallback {
        private final HttpEntity<?> requestEntity;

        private HttpEntityRequestCallback(RestTemplate restTemplate, Object requestBody) {
            this(requestBody, (Type) null);
        }

        private HttpEntityRequestCallback(Object requestBody, Type responseType) {
            super(responseType);
            if (requestBody instanceof HttpEntity) {
                this.requestEntity = (HttpEntity) requestBody;
            } else if (requestBody != null) {
                this.requestEntity = new HttpEntity<>(requestBody);
            } else {
                this.requestEntity = HttpEntity.EMPTY;
            }
        }

        @Override // org.springframework.web.client.RestTemplate.AcceptHeaderRequestCallback, org.springframework.web.client.RequestCallback
        public void doWithRequest(ClientHttpRequest httpRequest) throws IOException, HttpMessageNotWritableException {
            super.doWithRequest(httpRequest);
            if (!this.requestEntity.hasBody()) {
                HttpHeaders httpHeaders = httpRequest.getHeaders();
                HttpHeaders requestHeaders = this.requestEntity.getHeaders();
                if (!requestHeaders.isEmpty()) {
                    for (Map.Entry<String, List<String>> entry : requestHeaders.entrySet()) {
                        httpHeaders.put(entry.getKey(), (List<String>) new LinkedList(entry.getValue()));
                    }
                }
                if (httpHeaders.getContentLength() < 0) {
                    httpHeaders.setContentLength(0L);
                    return;
                }
                return;
            }
            Object requestBody = this.requestEntity.getBody();
            Class<?> requestBodyClass = requestBody.getClass();
            Type requestBodyType = this.requestEntity instanceof RequestEntity ? ((RequestEntity) this.requestEntity).getType() : requestBodyClass;
            HttpHeaders httpHeaders2 = httpRequest.getHeaders();
            HttpHeaders requestHeaders2 = this.requestEntity.getHeaders();
            MediaType requestContentType = requestHeaders2.getContentType();
            for (HttpMessageConverter<?> messageConverter : RestTemplate.this.getMessageConverters()) {
                if (messageConverter instanceof GenericHttpMessageConverter) {
                    GenericHttpMessageConverter<Object> genericMessageConverter = (GenericHttpMessageConverter) messageConverter;
                    if (genericMessageConverter.canWrite(requestBodyType, requestBodyClass, requestContentType)) {
                        if (!requestHeaders2.isEmpty()) {
                            for (Map.Entry<String, List<String>> entry2 : requestHeaders2.entrySet()) {
                                httpHeaders2.put(entry2.getKey(), (List<String>) new LinkedList(entry2.getValue()));
                            }
                        }
                        if (RestTemplate.this.logger.isDebugEnabled()) {
                            if (requestContentType != null) {
                                RestTemplate.this.logger.debug("Writing [" + requestBody + "] as \"" + requestContentType + "\" using [" + messageConverter + "]");
                            } else {
                                RestTemplate.this.logger.debug("Writing [" + requestBody + "] using [" + messageConverter + "]");
                            }
                        }
                        genericMessageConverter.write(requestBody, requestBodyType, requestContentType, httpRequest);
                        return;
                    }
                } else if (messageConverter.canWrite(requestBodyClass, requestContentType)) {
                    if (!requestHeaders2.isEmpty()) {
                        for (Map.Entry<String, List<String>> entry3 : requestHeaders2.entrySet()) {
                            httpHeaders2.put(entry3.getKey(), (List<String>) new LinkedList(entry3.getValue()));
                        }
                    }
                    if (RestTemplate.this.logger.isDebugEnabled()) {
                        if (requestContentType != null) {
                            RestTemplate.this.logger.debug("Writing [" + requestBody + "] as \"" + requestContentType + "\" using [" + messageConverter + "]");
                        } else {
                            RestTemplate.this.logger.debug("Writing [" + requestBody + "] using [" + messageConverter + "]");
                        }
                    }
                    messageConverter.write(requestBody, requestContentType, httpRequest);
                    return;
                }
            }
            String message = "Could not write request: no suitable HttpMessageConverter found for request type [" + requestBodyClass.getName() + "]";
            if (requestContentType != null) {
                message = message + " and content type [" + requestContentType + "]";
            }
            throw new RestClientException(message);
        }
    }

    /* loaded from: spring-web-4.3.25.RELEASE.jar:org/springframework/web/client/RestTemplate$ResponseEntityResponseExtractor.class */
    private class ResponseEntityResponseExtractor<T> implements ResponseExtractor<ResponseEntity<T>> {
        private final HttpMessageConverterExtractor<T> delegate;

        public ResponseEntityResponseExtractor(Type responseType) {
            if (responseType != null && Void.class != responseType) {
                this.delegate = new HttpMessageConverterExtractor<>(responseType, RestTemplate.this.getMessageConverters(), RestTemplate.this.logger);
            } else {
                this.delegate = null;
            }
        }

        @Override // org.springframework.web.client.ResponseExtractor
        public ResponseEntity<T> extractData(ClientHttpResponse response) throws IOException {
            if (this.delegate != null) {
                T body = this.delegate.extractData(response);
                return ResponseEntity.status(response.getRawStatusCode()).headers(response.getHeaders()).body(body);
            }
            return ResponseEntity.status(response.getRawStatusCode()).headers(response.getHeaders()).build();
        }
    }

    /* loaded from: spring-web-4.3.25.RELEASE.jar:org/springframework/web/client/RestTemplate$HeadersExtractor.class */
    private static class HeadersExtractor implements ResponseExtractor<HttpHeaders> {
        private HeadersExtractor() {
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // org.springframework.web.client.ResponseExtractor
        public HttpHeaders extractData(ClientHttpResponse response) throws IOException {
            return response.getHeaders();
        }
    }
}
