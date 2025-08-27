package org.springframework.web.bind.annotation.support;

import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.BridgeMethodResolver;
import org.springframework.core.Conventions;
import org.springframework.core.GenericTypeResolver;
import org.springframework.core.MethodParameter;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.annotation.SynthesizingMethodParameter;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ValueConstants;
import org.springframework.web.bind.support.DefaultSessionAttributeStore;
import org.springframework.web.bind.support.SessionAttributeStore;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.bind.support.SimpleSessionStatus;
import org.springframework.web.bind.support.WebArgumentResolver;
import org.springframework.web.bind.support.WebBindingInitializer;
import org.springframework.web.bind.support.WebRequestDataBinder;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

@Deprecated
/* loaded from: spring-web-4.3.25.RELEASE.jar:org/springframework/web/bind/annotation/support/HandlerMethodInvoker.class */
public class HandlerMethodInvoker {
    private static final String MODEL_KEY_PREFIX_STALE = SessionAttributeStore.class.getName() + ".STALE.";
    private static final Log logger = LogFactory.getLog(HandlerMethodInvoker.class);
    private final HandlerMethodResolver methodResolver;
    private final WebBindingInitializer bindingInitializer;
    private final SessionAttributeStore sessionAttributeStore;
    private final ParameterNameDiscoverer parameterNameDiscoverer;
    private final WebArgumentResolver[] customArgumentResolvers;
    private final HttpMessageConverter<?>[] messageConverters;
    private final SimpleSessionStatus sessionStatus;

    public HandlerMethodInvoker(HandlerMethodResolver methodResolver) {
        this(methodResolver, null);
    }

    public HandlerMethodInvoker(HandlerMethodResolver methodResolver, WebBindingInitializer bindingInitializer) {
        this(methodResolver, bindingInitializer, new DefaultSessionAttributeStore(), null, null, null);
    }

    public HandlerMethodInvoker(HandlerMethodResolver methodResolver, WebBindingInitializer bindingInitializer, SessionAttributeStore sessionAttributeStore, ParameterNameDiscoverer parameterNameDiscoverer, WebArgumentResolver[] customArgumentResolvers, HttpMessageConverter<?>[] messageConverters) {
        this.sessionStatus = new SimpleSessionStatus();
        this.methodResolver = methodResolver;
        this.bindingInitializer = bindingInitializer;
        this.sessionAttributeStore = sessionAttributeStore;
        this.parameterNameDiscoverer = parameterNameDiscoverer;
        this.customArgumentResolvers = customArgumentResolvers;
        this.messageConverters = messageConverters;
    }

    public final Object invokeHandlerMethod(Method handlerMethod, Object handler, NativeWebRequest webRequest, ExtendedModelMap implicitModel) throws Exception {
        Method handlerMethodToInvoke = BridgeMethodResolver.findBridgedMethod(handlerMethod);
        try {
            boolean debug = logger.isDebugEnabled();
            for (String attrName : this.methodResolver.getActualSessionAttributeNames()) {
                Object attrValue = this.sessionAttributeStore.retrieveAttribute(webRequest, attrName);
                if (attrValue != null) {
                    implicitModel.addAttribute(attrName, attrValue);
                }
            }
            for (Method attributeMethod : this.methodResolver.getModelAttributeMethods()) {
                Method attributeMethodToInvoke = BridgeMethodResolver.findBridgedMethod(attributeMethod);
                Object[] args = resolveHandlerArguments(attributeMethodToInvoke, handler, webRequest, implicitModel);
                if (debug) {
                    logger.debug("Invoking model attribute method: " + attributeMethodToInvoke);
                }
                String attrName2 = ((ModelAttribute) AnnotationUtils.findAnnotation(attributeMethod, ModelAttribute.class)).value();
                if ("".equals(attrName2) || !implicitModel.containsAttribute(attrName2)) {
                    ReflectionUtils.makeAccessible(attributeMethodToInvoke);
                    Object attrValue2 = attributeMethodToInvoke.invoke(handler, args);
                    if ("".equals(attrName2)) {
                        Class<?> resolvedType = GenericTypeResolver.resolveReturnType(attributeMethodToInvoke, handler.getClass());
                        attrName2 = Conventions.getVariableNameForReturnType(attributeMethodToInvoke, resolvedType, attrValue2);
                    }
                    if (!implicitModel.containsAttribute(attrName2)) {
                        implicitModel.addAttribute(attrName2, attrValue2);
                    }
                }
            }
            Object[] args2 = resolveHandlerArguments(handlerMethodToInvoke, handler, webRequest, implicitModel);
            if (debug) {
                logger.debug("Invoking request handler method: " + handlerMethodToInvoke);
            }
            ReflectionUtils.makeAccessible(handlerMethodToInvoke);
            return handlerMethodToInvoke.invoke(handler, args2);
        } catch (IllegalStateException ex) {
            throw new HandlerMethodInvocationException(handlerMethodToInvoke, ex);
        } catch (InvocationTargetException ex2) {
            ReflectionUtils.rethrowException(ex2.getTargetException());
            return null;
        }
    }

    public final void updateModelAttributes(Object handler, Map<String, Object> mavModel, ExtendedModelMap implicitModel, NativeWebRequest webRequest) throws Exception {
        if (this.methodResolver.hasSessionAttributes() && this.sessionStatus.isComplete()) {
            Iterator<String> it = this.methodResolver.getActualSessionAttributeNames().iterator();
            while (it.hasNext()) {
                this.sessionAttributeStore.cleanupAttribute(webRequest, it.next());
            }
        }
        Map<String, Object> model = mavModel != null ? mavModel : implicitModel;
        if (model != null) {
            try {
                String[] originalAttrNames = StringUtils.toStringArray(model.keySet());
                for (String attrName : originalAttrNames) {
                    Object attrValue = model.get(attrName);
                    boolean isSessionAttr = this.methodResolver.isSessionAttribute(attrName, attrValue != null ? attrValue.getClass() : null);
                    if (isSessionAttr) {
                        if (this.sessionStatus.isComplete()) {
                            implicitModel.put(MODEL_KEY_PREFIX_STALE + attrName, Boolean.TRUE);
                        } else if (!implicitModel.containsKey(MODEL_KEY_PREFIX_STALE + attrName)) {
                            this.sessionAttributeStore.storeAttribute(webRequest, attrName, attrValue);
                        }
                    }
                    if (!attrName.startsWith(BindingResult.MODEL_KEY_PREFIX) && (isSessionAttr || isBindingCandidate(attrValue))) {
                        String bindingResultKey = BindingResult.MODEL_KEY_PREFIX + attrName;
                        if (mavModel != null && !model.containsKey(bindingResultKey)) {
                            WebDataBinder binder = createBinder(webRequest, attrValue, attrName);
                            initBinder(handler, attrName, binder, webRequest);
                            mavModel.put(bindingResultKey, binder.getBindingResult());
                        }
                    }
                }
            } catch (InvocationTargetException ex) {
                ReflectionUtils.rethrowException(ex.getTargetException());
            }
        }
    }

    private Object[] resolveHandlerArguments(Method handlerMethod, Object handler, NativeWebRequest webRequest, ExtendedModelMap implicitModel) throws Exception {
        Class<?>[] paramTypes = handlerMethod.getParameterTypes();
        Object[] args = new Object[paramTypes.length];
        int i = 0;
        while (i < args.length) {
            MethodParameter methodParam = new SynthesizingMethodParameter(handlerMethod, i);
            methodParam.initParameterNameDiscovery(this.parameterNameDiscoverer);
            GenericTypeResolver.resolveParameterType(methodParam, handler.getClass());
            String paramName = null;
            String headerName = null;
            boolean requestBodyFound = false;
            String cookieName = null;
            String pathVarName = null;
            String attrName = null;
            boolean required = false;
            String defaultValue = null;
            boolean validate = false;
            Object[] validationHints = null;
            int annotationsFound = 0;
            Annotation[] paramAnns = methodParam.getParameterAnnotations();
            for (Annotation paramAnn : paramAnns) {
                if (RequestParam.class.isInstance(paramAnn)) {
                    RequestParam requestParam = (RequestParam) paramAnn;
                    paramName = requestParam.name();
                    required = requestParam.required();
                    defaultValue = parseDefaultValueAttribute(requestParam.defaultValue());
                    annotationsFound++;
                } else if (RequestHeader.class.isInstance(paramAnn)) {
                    RequestHeader requestHeader = (RequestHeader) paramAnn;
                    headerName = requestHeader.name();
                    required = requestHeader.required();
                    defaultValue = parseDefaultValueAttribute(requestHeader.defaultValue());
                    annotationsFound++;
                } else if (RequestBody.class.isInstance(paramAnn)) {
                    requestBodyFound = true;
                    annotationsFound++;
                } else if (CookieValue.class.isInstance(paramAnn)) {
                    CookieValue cookieValue = (CookieValue) paramAnn;
                    cookieName = cookieValue.name();
                    required = cookieValue.required();
                    defaultValue = parseDefaultValueAttribute(cookieValue.defaultValue());
                    annotationsFound++;
                } else if (PathVariable.class.isInstance(paramAnn)) {
                    PathVariable pathVar = (PathVariable) paramAnn;
                    pathVarName = pathVar.value();
                    annotationsFound++;
                } else if (ModelAttribute.class.isInstance(paramAnn)) {
                    ModelAttribute attr = (ModelAttribute) paramAnn;
                    attrName = attr.value();
                    annotationsFound++;
                } else if (Value.class.isInstance(paramAnn)) {
                    defaultValue = ((Value) paramAnn).value();
                } else {
                    Validated validatedAnn = (Validated) AnnotationUtils.getAnnotation(paramAnn, Validated.class);
                    if (validatedAnn != null || paramAnn.annotationType().getSimpleName().startsWith("Valid")) {
                        validate = true;
                        Object hints = validatedAnn != null ? validatedAnn.value() : AnnotationUtils.getValue(paramAnn);
                        validationHints = hints instanceof Object[] ? (Object[]) hints : new Object[]{hints};
                    }
                }
            }
            if (annotationsFound > 1) {
                throw new IllegalStateException("Handler parameter annotations are exclusive choices - do not specify more than one such annotation on the same parameter: " + handlerMethod);
            }
            if (annotationsFound == 0) {
                Object argValue = resolveCommonArgument(methodParam, webRequest);
                if (argValue != WebArgumentResolver.UNRESOLVED) {
                    args[i] = argValue;
                } else if (defaultValue != null) {
                    args[i] = resolveDefaultValue(defaultValue);
                } else {
                    Class<?> paramType = methodParam.getParameterType();
                    if (Model.class.isAssignableFrom(paramType) || Map.class.isAssignableFrom(paramType)) {
                        if (!paramType.isAssignableFrom(implicitModel.getClass())) {
                            throw new IllegalStateException("Argument [" + paramType.getSimpleName() + "] is of type Model or Map but is not assignable from the actual model. You may need to switch newer MVC infrastructure classes to use this argument.");
                        }
                        args[i] = implicitModel;
                    } else if (SessionStatus.class.isAssignableFrom(paramType)) {
                        args[i] = this.sessionStatus;
                    } else if (HttpEntity.class.isAssignableFrom(paramType)) {
                        args[i] = resolveHttpEntityRequest(methodParam, webRequest);
                    } else {
                        if (Errors.class.isAssignableFrom(paramType)) {
                            throw new IllegalStateException("Errors/BindingResult argument declared without preceding model attribute. Check your handler method signature!");
                        }
                        if (BeanUtils.isSimpleProperty(paramType)) {
                            paramName = "";
                        } else {
                            attrName = "";
                        }
                    }
                }
            }
            if (paramName != null) {
                args[i] = resolveRequestParam(paramName, required, defaultValue, methodParam, webRequest, handler);
            } else if (headerName != null) {
                args[i] = resolveRequestHeader(headerName, required, defaultValue, methodParam, webRequest, handler);
            } else if (requestBodyFound) {
                args[i] = resolveRequestBody(methodParam, webRequest, handler);
            } else if (cookieName != null) {
                args[i] = resolveCookieValue(cookieName, required, defaultValue, methodParam, webRequest, handler);
            } else if (pathVarName != null) {
                args[i] = resolvePathVariable(pathVarName, methodParam, webRequest, handler);
            } else if (attrName != null) {
                WebDataBinder binder = resolveModelAttribute(attrName, methodParam, implicitModel, webRequest, handler);
                boolean assignBindingResult = args.length > i + 1 && Errors.class.isAssignableFrom(paramTypes[i + 1]);
                if (binder.getTarget() != null) {
                    doBind(binder, webRequest, validate, validationHints, !assignBindingResult);
                }
                args[i] = binder.getTarget();
                if (assignBindingResult) {
                    args[i + 1] = binder.getBindingResult();
                    i++;
                }
                implicitModel.putAll(binder.getBindingResult().getModel());
            }
            i++;
        }
        return args;
    }

    protected void initBinder(Object handler, String attrName, WebDataBinder binder, NativeWebRequest webRequest) throws Exception {
        if (this.bindingInitializer != null) {
            this.bindingInitializer.initBinder(binder, webRequest);
        }
        if (handler != null) {
            Set<Method> initBinderMethods = this.methodResolver.getInitBinderMethods();
            if (!initBinderMethods.isEmpty()) {
                boolean debug = logger.isDebugEnabled();
                for (Method initBinderMethod : initBinderMethods) {
                    Method methodToInvoke = BridgeMethodResolver.findBridgedMethod(initBinderMethod);
                    String[] targetNames = ((InitBinder) AnnotationUtils.findAnnotation(initBinderMethod, InitBinder.class)).value();
                    if (targetNames.length == 0 || Arrays.asList(targetNames).contains(attrName)) {
                        Object[] initBinderArgs = resolveInitBinderArguments(handler, methodToInvoke, binder, webRequest);
                        if (debug) {
                            logger.debug("Invoking init-binder method: " + methodToInvoke);
                        }
                        ReflectionUtils.makeAccessible(methodToInvoke);
                        Object returnValue = methodToInvoke.invoke(handler, initBinderArgs);
                        if (returnValue != null) {
                            throw new IllegalStateException("InitBinder methods must not have a return value: " + methodToInvoke);
                        }
                    }
                }
            }
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:20:0x00df, code lost:
    
        if (r19 != null) goto L34;
     */
    /* JADX WARN: Code restructure failed: missing block: B:22:0x00e4, code lost:
    
        if (r22 != null) goto L34;
     */
    /* JADX WARN: Code restructure failed: missing block: B:23:0x00e7, code lost:
    
        r0 = resolveCommonArgument(r0, r14);
     */
    /* JADX WARN: Code restructure failed: missing block: B:24:0x00f6, code lost:
    
        if (r0 == org.springframework.web.bind.support.WebArgumentResolver.UNRESOLVED) goto L26;
     */
    /* JADX WARN: Code restructure failed: missing block: B:25:0x00f9, code lost:
    
        r0[r17] = r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:26:0x0103, code lost:
    
        r0 = r0[r17];
     */
    /* JADX WARN: Code restructure failed: missing block: B:27:0x0110, code lost:
    
        if (r0.isInstance(r13) == false) goto L29;
     */
    /* JADX WARN: Code restructure failed: missing block: B:28:0x0113, code lost:
    
        r0[r17] = r13;
     */
    /* JADX WARN: Code restructure failed: missing block: B:30:0x0121, code lost:
    
        if (org.springframework.beans.BeanUtils.isSimpleProperty(r0) == false) goto L45;
     */
    /* JADX WARN: Code restructure failed: missing block: B:31:0x0124, code lost:
    
        r19 = "";
     */
    /* JADX WARN: Code restructure failed: missing block: B:33:0x0152, code lost:
    
        throw new java.lang.IllegalStateException("Unsupported argument [" + r0.getName() + "] for @InitBinder method: " + r12);
     */
    /* JADX WARN: Code restructure failed: missing block: B:35:0x0155, code lost:
    
        if (r19 == null) goto L37;
     */
    /* JADX WARN: Code restructure failed: missing block: B:36:0x0158, code lost:
    
        r0[r17] = resolveRequestParam(r19, r20, r21, r0, r14, null);
     */
    /* JADX WARN: Code restructure failed: missing block: B:38:0x0171, code lost:
    
        if (r22 == null) goto L48;
     */
    /* JADX WARN: Code restructure failed: missing block: B:39:0x0174, code lost:
    
        r0[r17] = resolvePathVariable(r22, r0, r14, null);
     */
    /* JADX WARN: Code restructure failed: missing block: B:40:0x0184, code lost:
    
        r17 = r17 + 1;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private java.lang.Object[] resolveInitBinderArguments(java.lang.Object r11, java.lang.reflect.Method r12, org.springframework.web.bind.WebDataBinder r13, org.springframework.web.context.request.NativeWebRequest r14) throws java.lang.Exception {
        /*
            Method dump skipped, instructions count: 397
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: org.springframework.web.bind.annotation.support.HandlerMethodInvoker.resolveInitBinderArguments(java.lang.Object, java.lang.reflect.Method, org.springframework.web.bind.WebDataBinder, org.springframework.web.context.request.NativeWebRequest):java.lang.Object[]");
    }

    /* JADX WARN: Multi-variable type inference failed */
    private Object resolveRequestParam(String paramName, boolean required, String defaultValue, MethodParameter methodParam, NativeWebRequest webRequest, Object handlerForInitBinderCall) throws Exception {
        Class<?> paramType = methodParam.getParameterType();
        if (Map.class.isAssignableFrom(paramType) && paramName.length() == 0) {
            return resolveRequestParamMap(paramType, webRequest);
        }
        if (paramName.length() == 0) {
            paramName = getRequiredParameterName(methodParam);
        }
        Object paramValue = null;
        MultipartRequest multipartRequest = (MultipartRequest) webRequest.getNativeRequest(MultipartRequest.class);
        if (multipartRequest != null) {
            List<MultipartFile> files = multipartRequest.getFiles(paramName);
            if (!files.isEmpty()) {
                paramValue = files.size() == 1 ? files.get(0) : files;
            }
        }
        if (paramValue == null) {
            String[] paramValues = webRequest.getParameterValues(paramName);
            if (paramValues != null) {
                paramValue = paramValues.length == 1 ? paramValues[0] : paramValues;
            }
        }
        if (paramValue == null) {
            if (defaultValue != null) {
                paramValue = resolveDefaultValue(defaultValue);
            } else if (required) {
                raiseMissingParameterException(paramName, paramType);
            }
            paramValue = checkValue(paramName, paramValue, paramType);
        }
        WebDataBinder binder = createBinder(webRequest, null, paramName);
        initBinder(handlerForInitBinderCall, paramName, binder, webRequest);
        return binder.convertIfNecessary(paramValue, paramType, methodParam);
    }

    private Map<String, ?> resolveRequestParamMap(Class<? extends Map<?, ?>> mapType, NativeWebRequest webRequest) {
        Map<String, String[]> parameterMap = webRequest.getParameterMap();
        if (MultiValueMap.class.isAssignableFrom(mapType)) {
            MultiValueMap<String, String> result = new LinkedMultiValueMap<>(parameterMap.size());
            for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
                for (String value : entry.getValue()) {
                    result.add(entry.getKey(), value);
                }
            }
            return result;
        }
        Map<String, String> result2 = new LinkedHashMap<>(parameterMap.size());
        for (Map.Entry<String, String[]> entry2 : parameterMap.entrySet()) {
            if (entry2.getValue().length > 0) {
                result2.put(entry2.getKey(), entry2.getValue()[0]);
            }
        }
        return result2;
    }

    /* JADX WARN: Multi-variable type inference failed */
    private Object resolveRequestHeader(String headerName, boolean required, String defaultValue, MethodParameter methodParam, NativeWebRequest webRequest, Object handlerForInitBinderCall) throws Exception {
        Class<?> paramType = methodParam.getParameterType();
        if (Map.class.isAssignableFrom(paramType)) {
            return resolveRequestHeaderMap(paramType, webRequest);
        }
        if (headerName.length() == 0) {
            headerName = getRequiredParameterName(methodParam);
        }
        Object headerValue = null;
        String[] headerValues = webRequest.getHeaderValues(headerName);
        if (headerValues != null) {
            headerValue = headerValues.length == 1 ? headerValues[0] : headerValues;
        }
        if (headerValue == null) {
            if (defaultValue != null) {
                headerValue = resolveDefaultValue(defaultValue);
            } else if (required) {
                raiseMissingHeaderException(headerName, paramType);
            }
            headerValue = checkValue(headerName, headerValue, paramType);
        }
        WebDataBinder binder = createBinder(webRequest, null, headerName);
        initBinder(handlerForInitBinderCall, headerName, binder, webRequest);
        return binder.convertIfNecessary(headerValue, paramType, methodParam);
    }

    private Map<String, ?> resolveRequestHeaderMap(Class<? extends Map<?, ?>> mapType, NativeWebRequest webRequest) {
        MultiValueMap<String, String> result;
        if (MultiValueMap.class.isAssignableFrom(mapType)) {
            if (HttpHeaders.class.isAssignableFrom(mapType)) {
                result = new HttpHeaders();
            } else {
                result = new LinkedMultiValueMap<>();
            }
            Iterator<String> iterator = webRequest.getHeaderNames();
            while (iterator.hasNext()) {
                String headerName = iterator.next();
                for (String headerValue : webRequest.getHeaderValues(headerName)) {
                    result.add(headerName, headerValue);
                }
            }
            return result;
        }
        Map<String, String> result2 = new LinkedHashMap<>();
        Iterator<String> iterator2 = webRequest.getHeaderNames();
        while (iterator2.hasNext()) {
            String headerName2 = iterator2.next();
            String headerValue2 = webRequest.getHeader(headerName2);
            result2.put(headerName2, headerValue2);
        }
        return result2;
    }

    protected Object resolveRequestBody(MethodParameter methodParam, NativeWebRequest webRequest, Object handler) throws Exception {
        return readWithMessageConverters(methodParam, createHttpInputMessage(webRequest), methodParam.getParameterType());
    }

    private HttpEntity<?> resolveHttpEntityRequest(MethodParameter methodParam, NativeWebRequest webRequest) throws Exception {
        HttpInputMessage inputMessage = createHttpInputMessage(webRequest);
        Class<?> paramType = getHttpEntityType(methodParam);
        Object body = readWithMessageConverters(methodParam, inputMessage, paramType);
        return new HttpEntity<>(body, inputMessage.getHeaders());
    }

    private Object readWithMessageConverters(MethodParameter methodParam, HttpInputMessage inputMessage, Class<?> paramType) throws Exception {
        MediaType contentType = inputMessage.getHeaders().getContentType();
        if (contentType == null) {
            StringBuilder builder = new StringBuilder(ClassUtils.getShortName(methodParam.getParameterType()));
            String paramName = methodParam.getParameterName();
            if (paramName != null) {
                builder.append(' ');
                builder.append(paramName);
            }
            throw new HttpMediaTypeNotSupportedException("Cannot extract parameter (" + builder.toString() + "): no Content-Type found");
        }
        List<MediaType> allSupportedMediaTypes = new ArrayList<>();
        if (this.messageConverters != null) {
            for (HttpMessageConverter<?> messageConverter : this.messageConverters) {
                allSupportedMediaTypes.addAll(messageConverter.getSupportedMediaTypes());
                if (messageConverter.canRead(paramType, contentType)) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("Reading [" + paramType.getName() + "] as \"" + contentType + "\" using [" + messageConverter + "]");
                    }
                    return messageConverter.read2(paramType, inputMessage);
                }
            }
        }
        throw new HttpMediaTypeNotSupportedException(contentType, allSupportedMediaTypes);
    }

    private Class<?> getHttpEntityType(MethodParameter methodParam) throws NegativeArraySizeException {
        Assert.isAssignable(HttpEntity.class, methodParam.getParameterType());
        ParameterizedType type = (ParameterizedType) methodParam.getGenericParameterType();
        if (type.getActualTypeArguments().length == 1) {
            Type typeArgument = type.getActualTypeArguments()[0];
            if (typeArgument instanceof Class) {
                return (Class) typeArgument;
            }
            if (typeArgument instanceof GenericArrayType) {
                Type componentType = ((GenericArrayType) typeArgument).getGenericComponentType();
                if (componentType instanceof Class) {
                    Object array = Array.newInstance((Class<?>) componentType, 0);
                    return array.getClass();
                }
            }
        }
        throw new IllegalArgumentException("HttpEntity parameter (" + methodParam.getParameterName() + ") is not parameterized");
    }

    private Object resolveCookieValue(String cookieName, boolean required, String defaultValue, MethodParameter methodParam, NativeWebRequest webRequest, Object handlerForInitBinderCall) throws Exception {
        Class<?> paramType = methodParam.getParameterType();
        if (cookieName.length() == 0) {
            cookieName = getRequiredParameterName(methodParam);
        }
        Object cookieValue = resolveCookieValue(cookieName, paramType, webRequest);
        if (cookieValue == null) {
            if (defaultValue != null) {
                cookieValue = resolveDefaultValue(defaultValue);
            } else if (required) {
                raiseMissingCookieException(cookieName, paramType);
            }
            cookieValue = checkValue(cookieName, cookieValue, paramType);
        }
        WebDataBinder binder = createBinder(webRequest, null, cookieName);
        initBinder(handlerForInitBinderCall, cookieName, binder, webRequest);
        return binder.convertIfNecessary(cookieValue, paramType, methodParam);
    }

    protected Object resolveCookieValue(String cookieName, Class<?> paramType, NativeWebRequest webRequest) throws Exception {
        throw new UnsupportedOperationException("@CookieValue not supported");
    }

    private Object resolvePathVariable(String pathVarName, MethodParameter methodParam, NativeWebRequest webRequest, Object handlerForInitBinderCall) throws Exception {
        Class<?> paramType = methodParam.getParameterType();
        if (pathVarName.length() == 0) {
            pathVarName = getRequiredParameterName(methodParam);
        }
        String pathVarValue = resolvePathVariable(pathVarName, paramType, webRequest);
        WebDataBinder binder = createBinder(webRequest, null, pathVarName);
        initBinder(handlerForInitBinderCall, pathVarName, binder, webRequest);
        return binder.convertIfNecessary(pathVarValue, paramType, methodParam);
    }

    protected String resolvePathVariable(String pathVarName, Class<?> paramType, NativeWebRequest webRequest) throws Exception {
        throw new UnsupportedOperationException("@PathVariable not supported");
    }

    private String getRequiredParameterName(MethodParameter methodParam) {
        String name = methodParam.getParameterName();
        if (name == null) {
            throw new IllegalStateException("No parameter name specified for argument of type [" + methodParam.getParameterType().getName() + "], and no parameter name information found in class file either.");
        }
        return name;
    }

    private Object checkValue(String name, Object value, Class<?> paramType) {
        if (value == null) {
            if (Boolean.TYPE == paramType) {
                return Boolean.FALSE;
            }
            if (paramType.isPrimitive()) {
                throw new IllegalStateException("Optional " + paramType + " parameter '" + name + "' is not present but cannot be translated into a null value due to being declared as a primitive type. Consider declaring it as object wrapper for the corresponding primitive type.");
            }
        }
        return value;
    }

    private WebDataBinder resolveModelAttribute(String attrName, MethodParameter methodParam, ExtendedModelMap implicitModel, NativeWebRequest webRequest, Object handler) throws Exception {
        Object bindObject;
        String name = attrName;
        if ("".equals(name)) {
            name = Conventions.getVariableNameForParameter(methodParam);
        }
        Class<?> paramType = methodParam.getParameterType();
        if (implicitModel.containsKey(name)) {
            bindObject = implicitModel.get(name);
        } else if (this.methodResolver.isSessionAttribute(name, paramType)) {
            bindObject = this.sessionAttributeStore.retrieveAttribute(webRequest, name);
            if (bindObject == null) {
                raiseSessionRequiredException("Session attribute '" + name + "' required - not found in session");
            }
        } else {
            bindObject = BeanUtils.instantiateClass(paramType);
        }
        WebDataBinder binder = createBinder(webRequest, bindObject, name);
        initBinder(handler, name, binder, webRequest);
        return binder;
    }

    protected boolean isBindingCandidate(Object value) {
        return (value == null || value.getClass().isArray() || (value instanceof Collection) || (value instanceof Map) || BeanUtils.isSimpleValueType(value.getClass())) ? false : true;
    }

    protected void raiseMissingParameterException(String paramName, Class<?> paramType) throws Exception {
        throw new IllegalStateException("Missing parameter '" + paramName + "' of type [" + paramType.getName() + "]");
    }

    protected void raiseMissingHeaderException(String headerName, Class<?> paramType) throws Exception {
        throw new IllegalStateException("Missing header '" + headerName + "' of type [" + paramType.getName() + "]");
    }

    protected void raiseMissingCookieException(String cookieName, Class<?> paramType) throws Exception {
        throw new IllegalStateException("Missing cookie value '" + cookieName + "' of type [" + paramType.getName() + "]");
    }

    protected void raiseSessionRequiredException(String message) throws Exception {
        throw new IllegalStateException(message);
    }

    protected WebDataBinder createBinder(NativeWebRequest webRequest, Object target, String objectName) throws Exception {
        return new WebRequestDataBinder(target, objectName);
    }

    private void doBind(WebDataBinder binder, NativeWebRequest webRequest, boolean validate, Object[] validationHints, boolean failOnErrors) throws Exception {
        doBind(binder, webRequest);
        if (validate) {
            binder.validate(validationHints);
        }
        if (failOnErrors && binder.getBindingResult().hasErrors()) {
            throw new BindException(binder.getBindingResult());
        }
    }

    protected void doBind(WebDataBinder binder, NativeWebRequest webRequest) throws Exception {
        ((WebRequestDataBinder) binder).bind(webRequest);
    }

    protected HttpInputMessage createHttpInputMessage(NativeWebRequest webRequest) throws Exception {
        throw new UnsupportedOperationException("@RequestBody not supported");
    }

    protected HttpOutputMessage createHttpOutputMessage(NativeWebRequest webRequest) throws Exception {
        throw new UnsupportedOperationException("@Body not supported");
    }

    protected String parseDefaultValueAttribute(String value) {
        if (ValueConstants.DEFAULT_NONE.equals(value)) {
            return null;
        }
        return value;
    }

    protected Object resolveDefaultValue(String value) {
        return value;
    }

    protected Object resolveCommonArgument(MethodParameter methodParameter, NativeWebRequest webRequest) throws Exception {
        if (this.customArgumentResolvers != null) {
            for (WebArgumentResolver argumentResolver : this.customArgumentResolvers) {
                Object value = argumentResolver.resolveArgument(methodParameter, webRequest);
                if (value != WebArgumentResolver.UNRESOLVED) {
                    return value;
                }
            }
        }
        Class<?> paramType = methodParameter.getParameterType();
        Object value2 = resolveStandardArgument(paramType, webRequest);
        if (value2 != WebArgumentResolver.UNRESOLVED && !ClassUtils.isAssignableValue(paramType, value2)) {
            throw new IllegalStateException("Standard argument type [" + paramType.getName() + "] resolved to incompatible value of type [" + (value2 != null ? value2.getClass() : null) + "]. Consider declaring the argument type in a less specific fashion.");
        }
        return value2;
    }

    protected Object resolveStandardArgument(Class<?> parameterType, NativeWebRequest webRequest) throws Exception {
        if (WebRequest.class.isAssignableFrom(parameterType)) {
            return webRequest;
        }
        return WebArgumentResolver.UNRESOLVED;
    }

    protected final void addReturnValueAsModelAttribute(Method handlerMethod, Class<?> handlerType, Object returnValue, ExtendedModelMap implicitModel) {
        ModelAttribute attr = (ModelAttribute) AnnotationUtils.findAnnotation(handlerMethod, ModelAttribute.class);
        String attrName = attr != null ? attr.value() : "";
        if ("".equals(attrName)) {
            Class<?> resolvedType = GenericTypeResolver.resolveReturnType(handlerMethod, handlerType);
            attrName = Conventions.getVariableNameForReturnType(handlerMethod, resolvedType, returnValue);
        }
        implicitModel.addAttribute(attrName, returnValue);
    }
}
