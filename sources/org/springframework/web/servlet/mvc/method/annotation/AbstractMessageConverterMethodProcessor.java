package org.springframework.web.servlet.mvc.method.annotation;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.poi.openxml4j.opc.ContentTypes;
import org.bouncycastle.i18n.TextBundle;
import org.springframework.core.MethodParameter;
import org.springframework.core.ResolvableType;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.converter.GenericHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.accept.ContentNegotiationManager;
import org.springframework.web.accept.PathExtensionContentNegotiationStrategy;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.util.UrlPathHelper;

/* loaded from: spring-webmvc-4.3.25.RELEASE.jar:org/springframework/web/servlet/mvc/method/annotation/AbstractMessageConverterMethodProcessor.class */
public abstract class AbstractMessageConverterMethodProcessor extends AbstractMessageConverterMethodArgumentResolver implements HandlerMethodReturnValueHandler {
    private static final Set<String> WHITELISTED_EXTENSIONS = new HashSet(Arrays.asList("txt", TextBundle.TEXT_ENTRY, "yml", "properties", "csv", "json", "xml", "atom", "rss", ContentTypes.EXTENSION_PNG, "jpe", ContentTypes.EXTENSION_JPG_2, ContentTypes.EXTENSION_JPG_1, ContentTypes.EXTENSION_GIF, "wbmp", "bmp"));
    private static final Set<String> WHITELISTED_MEDIA_BASE_TYPES = new HashSet(Arrays.asList("audio", "image", "video"));
    private static final MediaType MEDIA_TYPE_APPLICATION = new MediaType("application");
    private static final UrlPathHelper DECODING_URL_PATH_HELPER = new UrlPathHelper();
    private static final UrlPathHelper RAW_URL_PATH_HELPER = new UrlPathHelper();
    private final ContentNegotiationManager contentNegotiationManager;
    private final PathExtensionContentNegotiationStrategy pathStrategy;
    private final Set<String> safeExtensions;

    static {
        RAW_URL_PATH_HELPER.setRemoveSemicolonContent(false);
        RAW_URL_PATH_HELPER.setUrlDecode(false);
    }

    protected AbstractMessageConverterMethodProcessor(List<HttpMessageConverter<?>> converters) {
        this(converters, null);
    }

    protected AbstractMessageConverterMethodProcessor(List<HttpMessageConverter<?>> converters, ContentNegotiationManager contentNegotiationManager) {
        this(converters, contentNegotiationManager, null);
    }

    protected AbstractMessageConverterMethodProcessor(List<HttpMessageConverter<?>> converters, ContentNegotiationManager manager, List<Object> requestResponseBodyAdvice) {
        super(converters, requestResponseBodyAdvice);
        this.safeExtensions = new HashSet();
        this.contentNegotiationManager = manager != null ? manager : new ContentNegotiationManager();
        this.pathStrategy = initPathStrategy(this.contentNegotiationManager);
        this.safeExtensions.addAll(this.contentNegotiationManager.getAllFileExtensions());
        this.safeExtensions.addAll(WHITELISTED_EXTENSIONS);
    }

    private static PathExtensionContentNegotiationStrategy initPathStrategy(ContentNegotiationManager manager) {
        PathExtensionContentNegotiationStrategy strategy = (PathExtensionContentNegotiationStrategy) manager.getStrategy(PathExtensionContentNegotiationStrategy.class);
        return strategy != null ? strategy : new PathExtensionContentNegotiationStrategy();
    }

    protected ServletServerHttpResponse createOutputMessage(NativeWebRequest webRequest) {
        HttpServletResponse response = (HttpServletResponse) webRequest.getNativeResponse(HttpServletResponse.class);
        return new ServletServerHttpResponse(response);
    }

    protected <T> void writeWithMessageConverters(T value, MethodParameter returnType, NativeWebRequest webRequest) throws HttpMediaTypeNotAcceptableException, IOException, HttpMessageNotWritableException {
        ServletServerHttpRequest inputMessage = createInputMessage(webRequest);
        ServletServerHttpResponse outputMessage = createOutputMessage(webRequest);
        writeWithMessageConverters(value, returnType, inputMessage, outputMessage);
    }

    /* JADX WARN: Multi-variable type inference failed */
    protected <T> void writeWithMessageConverters(T value, MethodParameter returnType, ServletServerHttpRequest inputMessage, ServletServerHttpResponse outputMessage) throws HttpMediaTypeNotAcceptableException, IOException, HttpMessageNotWritableException {
        Object outputValue;
        Class<?> valueType;
        Type declaredType;
        MediaType mediaType;
        if (value instanceof CharSequence) {
            outputValue = value.toString();
            valueType = String.class;
            declaredType = String.class;
        } else {
            outputValue = value;
            valueType = getReturnValueType(outputValue, returnType);
            declaredType = getGenericType(returnType);
        }
        HttpServletRequest request = inputMessage.getServletRequest();
        List<MediaType> requestedMediaTypes = getAcceptableMediaTypes(request);
        List<MediaType> producibleMediaTypes = getProducibleMediaTypes(request, valueType, declaredType);
        if (outputValue != null && producibleMediaTypes.isEmpty()) {
            throw new IllegalArgumentException("No converter found for return value of type: " + valueType);
        }
        Set<MediaType> compatibleMediaTypes = new LinkedHashSet<>();
        for (MediaType requestedType : requestedMediaTypes) {
            for (MediaType producibleType : producibleMediaTypes) {
                if (requestedType.isCompatibleWith(producibleType)) {
                    compatibleMediaTypes.add(getMostSpecificMediaType(requestedType, producibleType));
                }
            }
        }
        if (compatibleMediaTypes.isEmpty()) {
            if (outputValue != null) {
                throw new HttpMediaTypeNotAcceptableException(producibleMediaTypes);
            }
            return;
        }
        List<MediaType> mediaTypes = new ArrayList<>(compatibleMediaTypes);
        MediaType.sortBySpecificityAndQuality(mediaTypes);
        MediaType selectedMediaType = null;
        Iterator<MediaType> it = mediaTypes.iterator();
        do {
            if (!it.hasNext()) {
                break;
            }
            mediaType = it.next();
            if (mediaType.isConcrete()) {
                selectedMediaType = mediaType;
                break;
            } else if (mediaType.equals(MediaType.ALL)) {
                break;
            }
        } while (!mediaType.equals(MEDIA_TYPE_APPLICATION));
        selectedMediaType = MediaType.APPLICATION_OCTET_STREAM;
        if (selectedMediaType != null) {
            MediaType selectedMediaType2 = selectedMediaType.removeQualityValue();
            for (HttpMessageConverter<?> messageConverter : this.messageConverters) {
                if (messageConverter instanceof GenericHttpMessageConverter) {
                    if (((GenericHttpMessageConverter) messageConverter).canWrite(declaredType, valueType, selectedMediaType2)) {
                        Object outputValue2 = getAdvice().beforeBodyWrite(outputValue, returnType, selectedMediaType2, messageConverter.getClass(), inputMessage, outputMessage);
                        if (outputValue2 != null) {
                            addContentDispositionHeader(inputMessage, outputMessage);
                            ((GenericHttpMessageConverter) messageConverter).write(outputValue2, declaredType, selectedMediaType2, outputMessage);
                            if (this.logger.isDebugEnabled()) {
                                this.logger.debug("Written [" + outputValue2 + "] as \"" + selectedMediaType2 + "\" using [" + messageConverter + "]");
                                return;
                            }
                            return;
                        }
                        return;
                    }
                } else if (messageConverter.canWrite(valueType, selectedMediaType2)) {
                    Object outputValue3 = getAdvice().beforeBodyWrite(outputValue, returnType, selectedMediaType2, messageConverter.getClass(), inputMessage, outputMessage);
                    if (outputValue3 != null) {
                        addContentDispositionHeader(inputMessage, outputMessage);
                        messageConverter.write(outputValue3, selectedMediaType2, outputMessage);
                        if (this.logger.isDebugEnabled()) {
                            this.logger.debug("Written [" + outputValue3 + "] as \"" + selectedMediaType2 + "\" using [" + messageConverter + "]");
                            return;
                        }
                        return;
                    }
                    return;
                }
            }
        }
        if (outputValue != null) {
            throw new HttpMediaTypeNotAcceptableException(this.allSupportedMediaTypes);
        }
    }

    protected Class<?> getReturnValueType(Object value, MethodParameter returnType) {
        return value != null ? value.getClass() : returnType.getParameterType();
    }

    private Type getGenericType(MethodParameter returnType) {
        return HttpEntity.class.isAssignableFrom(returnType.getParameterType()) ? ResolvableType.forType(returnType.getGenericParameterType()).getGeneric(0).getType() : returnType.getGenericParameterType();
    }

    protected List<MediaType> getProducibleMediaTypes(HttpServletRequest request, Class<?> valueClass) {
        return getProducibleMediaTypes(request, valueClass, null);
    }

    protected List<MediaType> getProducibleMediaTypes(HttpServletRequest request, Class<?> valueClass, Type declaredType) {
        Set<MediaType> mediaTypes = (Set) request.getAttribute(HandlerMapping.PRODUCIBLE_MEDIA_TYPES_ATTRIBUTE);
        if (!CollectionUtils.isEmpty(mediaTypes)) {
            return new ArrayList(mediaTypes);
        }
        if (!this.allSupportedMediaTypes.isEmpty()) {
            List<MediaType> result = new ArrayList<>();
            for (HttpMessageConverter<?> converter : this.messageConverters) {
                if ((converter instanceof GenericHttpMessageConverter) && declaredType != null) {
                    if (((GenericHttpMessageConverter) converter).canWrite(declaredType, valueClass, null)) {
                        result.addAll(converter.getSupportedMediaTypes());
                    }
                } else if (converter.canWrite(valueClass, null)) {
                    result.addAll(converter.getSupportedMediaTypes());
                }
            }
            return result;
        }
        return Collections.singletonList(MediaType.ALL);
    }

    private List<MediaType> getAcceptableMediaTypes(HttpServletRequest request) throws HttpMediaTypeNotAcceptableException {
        List<MediaType> mediaTypes = this.contentNegotiationManager.resolveMediaTypes(new ServletWebRequest(request));
        return mediaTypes.isEmpty() ? Collections.singletonList(MediaType.ALL) : mediaTypes;
    }

    private MediaType getMostSpecificMediaType(MediaType acceptType, MediaType produceType) {
        MediaType produceTypeToUse = produceType.copyQualityValue(acceptType);
        return MediaType.SPECIFICITY_COMPARATOR.compare(acceptType, produceTypeToUse) <= 0 ? acceptType : produceTypeToUse;
    }

    private void addContentDispositionHeader(ServletServerHttpRequest request, ServletServerHttpResponse response) {
        HttpHeaders headers = response.getHeaders();
        if (headers.containsKey("Content-Disposition")) {
            return;
        }
        try {
            int status = response.getServletResponse().getStatus();
            if (status < 200 || status > 299) {
                return;
            }
        } catch (Throwable th) {
        }
        HttpServletRequest servletRequest = request.getServletRequest();
        String requestUri = RAW_URL_PATH_HELPER.getOriginatingRequestUri(servletRequest);
        String filename = requestUri.substring(requestUri.lastIndexOf(47) + 1);
        String pathParams = "";
        int index = filename.indexOf(59);
        if (index != -1) {
            pathParams = filename.substring(index);
            filename = filename.substring(0, index);
        }
        String ext = StringUtils.getFilenameExtension(DECODING_URL_PATH_HELPER.decodeRequestString(servletRequest, filename));
        String extInPathParams = StringUtils.getFilenameExtension(DECODING_URL_PATH_HELPER.decodeRequestString(servletRequest, pathParams));
        if (!safeExtension(servletRequest, ext) || !safeExtension(servletRequest, extInPathParams)) {
            headers.add("Content-Disposition", "inline;filename=f.txt");
        }
    }

    private boolean safeExtension(HttpServletRequest request, String extension) {
        if (!StringUtils.hasText(extension)) {
            return true;
        }
        String extension2 = extension.toLowerCase(Locale.ENGLISH);
        if (this.safeExtensions.contains(extension2)) {
            return true;
        }
        String pattern = (String) request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);
        if (pattern != null && pattern.endsWith("." + extension2)) {
            return true;
        }
        if (extension2.equals("html")) {
            String name = HandlerMapping.PRODUCIBLE_MEDIA_TYPES_ATTRIBUTE;
            Set<MediaType> mediaTypes = (Set) request.getAttribute(name);
            if (!CollectionUtils.isEmpty(mediaTypes) && mediaTypes.contains(MediaType.TEXT_HTML)) {
                return true;
            }
        }
        return safeMediaTypesForExtension(extension2);
    }

    private boolean safeMediaTypesForExtension(String extension) {
        List<MediaType> mediaTypes = null;
        try {
            mediaTypes = this.pathStrategy.resolveMediaTypeKey(null, extension);
        } catch (HttpMediaTypeNotAcceptableException e) {
        }
        if (CollectionUtils.isEmpty(mediaTypes)) {
            return false;
        }
        for (MediaType mediaType : mediaTypes) {
            if (!safeMediaType(mediaType)) {
                return false;
            }
        }
        return true;
    }

    private boolean safeMediaType(MediaType mediaType) {
        return WHITELISTED_MEDIA_BASE_TYPES.contains(mediaType.getType()) || mediaType.getSubtype().endsWith("+xml");
    }
}
