package org.springframework.web.servlet.mvc.method.annotation;

import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.MethodParameter;
import org.springframework.core.ResolvableType;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpRequest;
import org.springframework.http.InvalidMediaTypeException;
import org.springframework.http.MediaType;
import org.springframework.http.converter.GenericHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.lang.UsesJava8;
import org.springframework.util.Assert;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;

/* loaded from: spring-webmvc-4.3.25.RELEASE.jar:org/springframework/web/servlet/mvc/method/annotation/AbstractMessageConverterMethodArgumentResolver.class */
public abstract class AbstractMessageConverterMethodArgumentResolver implements HandlerMethodArgumentResolver {
    private static final Set<HttpMethod> SUPPORTED_METHODS = EnumSet.of(HttpMethod.POST, HttpMethod.PUT, HttpMethod.PATCH);
    private static final Object NO_VALUE = new Object();
    protected final Log logger;
    protected final List<HttpMessageConverter<?>> messageConverters;
    protected final List<MediaType> allSupportedMediaTypes;
    private final RequestResponseBodyAdviceChain advice;

    public AbstractMessageConverterMethodArgumentResolver(List<HttpMessageConverter<?>> converters) {
        this(converters, null);
    }

    public AbstractMessageConverterMethodArgumentResolver(List<HttpMessageConverter<?>> converters, List<Object> requestResponseBodyAdvice) {
        this.logger = LogFactory.getLog(getClass());
        Assert.notEmpty(converters, "'messageConverters' must not be empty");
        this.messageConverters = converters;
        this.allSupportedMediaTypes = getAllSupportedMediaTypes(converters);
        this.advice = new RequestResponseBodyAdviceChain(requestResponseBodyAdvice);
    }

    private static List<MediaType> getAllSupportedMediaTypes(List<HttpMessageConverter<?>> messageConverters) {
        Set<MediaType> allSupportedMediaTypes = new LinkedHashSet<>();
        for (HttpMessageConverter<?> messageConverter : messageConverters) {
            allSupportedMediaTypes.addAll(messageConverter.getSupportedMediaTypes());
        }
        List<MediaType> result = new ArrayList<>(allSupportedMediaTypes);
        MediaType.sortBySpecificity(result);
        return Collections.unmodifiableList(result);
    }

    protected RequestResponseBodyAdviceChain getAdvice() {
        return this.advice;
    }

    protected <T> Object readWithMessageConverters(NativeWebRequest webRequest, MethodParameter parameter, Type paramType) throws HttpMediaTypeNotSupportedException, IOException, HttpMessageNotReadableException {
        HttpInputMessage inputMessage = createInputMessage(webRequest);
        return readWithMessageConverters(inputMessage, parameter, paramType);
    }

    /* JADX WARN: Multi-variable type inference failed */
    protected <T> Object readWithMessageConverters(HttpInputMessage inputMessage, MethodParameter parameter, Type targetType) throws HttpMediaTypeNotSupportedException, IOException, HttpMessageNotReadableException {
        boolean noContentType = false;
        try {
            MediaType contentType = inputMessage.getHeaders().getContentType();
            if (contentType == null) {
                noContentType = true;
                contentType = MediaType.APPLICATION_OCTET_STREAM;
            }
            Class<?> contextClass = parameter != null ? parameter.getContainingClass() : null;
            Class<?> clsResolve = targetType instanceof Class ? (Class) targetType : null;
            if (clsResolve == null) {
                ResolvableType resolvableType = parameter != null ? ResolvableType.forMethodParameter(parameter) : ResolvableType.forType(targetType);
                clsResolve = resolvableType.resolve();
            }
            HttpMethod httpMethod = ((HttpRequest) inputMessage).getMethod();
            Object body = NO_VALUE;
            try {
                HttpInputMessage inputMessage2 = new EmptyBodyCheckingHttpInputMessage(inputMessage);
                Iterator<HttpMessageConverter<?>> it = this.messageConverters.iterator();
                while (true) {
                    if (!it.hasNext()) {
                        break;
                    }
                    HttpMessageConverter<?> converter = it.next();
                    Class<?> cls = converter.getClass();
                    if (converter instanceof GenericHttpMessageConverter) {
                        GenericHttpMessageConverter<?> genericConverter = (GenericHttpMessageConverter) converter;
                        if (genericConverter.canRead(targetType, contextClass, contentType)) {
                            if (this.logger.isDebugEnabled()) {
                                this.logger.debug("Read [" + targetType + "] as \"" + contentType + "\" with [" + converter + "]");
                            }
                            if (inputMessage2.getBody() != null) {
                                inputMessage2 = getAdvice().beforeBodyRead(inputMessage2, parameter, targetType, cls);
                                Object body2 = genericConverter.read(targetType, contextClass, inputMessage2);
                                body = getAdvice().afterBodyRead(body2, inputMessage2, parameter, targetType, cls);
                            } else {
                                body = getAdvice().handleEmptyBody(null, inputMessage2, parameter, targetType, cls);
                            }
                        }
                    } else if (clsResolve != null && converter.canRead(clsResolve, contentType)) {
                        if (this.logger.isDebugEnabled()) {
                            this.logger.debug("Read [" + targetType + "] as \"" + contentType + "\" with [" + converter + "]");
                        }
                        if (inputMessage2.getBody() != null) {
                            inputMessage2 = getAdvice().beforeBodyRead(inputMessage2, parameter, targetType, cls);
                            Object body3 = converter.read2(clsResolve, inputMessage2);
                            body = getAdvice().afterBodyRead(body3, inputMessage2, parameter, targetType, cls);
                        } else {
                            body = getAdvice().handleEmptyBody(null, inputMessage2, parameter, targetType, cls);
                        }
                    }
                }
                if (body == NO_VALUE) {
                    if (httpMethod != null && SUPPORTED_METHODS.contains(httpMethod)) {
                        if (noContentType && inputMessage2.getBody() == null) {
                            return null;
                        }
                        throw new HttpMediaTypeNotSupportedException(contentType, this.allSupportedMediaTypes);
                    }
                    return null;
                }
                return body;
            } catch (IOException ex) {
                throw new HttpMessageNotReadableException("I/O error while reading input message", ex);
            }
        } catch (InvalidMediaTypeException ex2) {
            throw new HttpMediaTypeNotSupportedException(ex2.getMessage());
        }
    }

    protected ServletServerHttpRequest createInputMessage(NativeWebRequest webRequest) {
        HttpServletRequest servletRequest = (HttpServletRequest) webRequest.getNativeRequest(HttpServletRequest.class);
        return new ServletServerHttpRequest(servletRequest);
    }

    protected void validateIfApplicable(WebDataBinder binder, MethodParameter parameter) {
        Annotation[] annotations = parameter.getParameterAnnotations();
        for (Annotation ann : annotations) {
            Validated validatedAnn = (Validated) AnnotationUtils.getAnnotation(ann, Validated.class);
            if (validatedAnn != null || ann.annotationType().getSimpleName().startsWith("Valid")) {
                Object hints = validatedAnn != null ? validatedAnn.value() : AnnotationUtils.getValue(ann);
                Object[] validationHints = hints instanceof Object[] ? (Object[]) hints : new Object[]{hints};
                binder.validate(validationHints);
                return;
            }
        }
    }

    protected boolean isBindExceptionRequired(WebDataBinder binder, MethodParameter parameter) {
        int i = parameter.getParameterIndex();
        Class<?>[] paramTypes = parameter.getMethod().getParameterTypes();
        boolean hasBindingResult = paramTypes.length > i + 1 && Errors.class.isAssignableFrom(paramTypes[i + 1]);
        return !hasBindingResult;
    }

    protected Object adaptArgumentIfNecessary(Object arg, MethodParameter parameter) {
        return parameter.isOptional() ? OptionalResolver.resolveValue(arg) : arg;
    }

    /* loaded from: spring-webmvc-4.3.25.RELEASE.jar:org/springframework/web/servlet/mvc/method/annotation/AbstractMessageConverterMethodArgumentResolver$EmptyBodyCheckingHttpInputMessage.class */
    private static class EmptyBodyCheckingHttpInputMessage implements HttpInputMessage {
        private final HttpHeaders headers;
        private final InputStream body;
        private final HttpMethod method;

        public EmptyBodyCheckingHttpInputMessage(HttpInputMessage inputMessage) throws IOException {
            this.headers = inputMessage.getHeaders();
            InputStream inputStream = inputMessage.getBody();
            if (inputStream == null) {
                this.body = null;
            } else if (inputStream.markSupported()) {
                inputStream.mark(1);
                this.body = inputStream.read() != -1 ? inputStream : null;
                inputStream.reset();
            } else {
                PushbackInputStream pushbackInputStream = new PushbackInputStream(inputStream);
                int b = pushbackInputStream.read();
                if (b == -1) {
                    this.body = null;
                } else {
                    this.body = pushbackInputStream;
                    pushbackInputStream.unread(b);
                }
            }
            this.method = ((HttpRequest) inputMessage).getMethod();
        }

        @Override // org.springframework.http.HttpMessage
        public HttpHeaders getHeaders() {
            return this.headers;
        }

        @Override // org.springframework.http.HttpInputMessage
        public InputStream getBody() throws IOException {
            return this.body;
        }

        public HttpMethod getMethod() {
            return this.method;
        }
    }

    @UsesJava8
    /* loaded from: spring-webmvc-4.3.25.RELEASE.jar:org/springframework/web/servlet/mvc/method/annotation/AbstractMessageConverterMethodArgumentResolver$OptionalResolver.class */
    private static class OptionalResolver {
        private OptionalResolver() {
        }

        public static Object resolveValue(Object value) {
            if (value == null || (((value instanceof Collection) && ((Collection) value).isEmpty()) || ((value instanceof Object[]) && ((Object[]) value).length == 0))) {
                return Optional.empty();
            }
            return Optional.of(value);
        }
    }
}
