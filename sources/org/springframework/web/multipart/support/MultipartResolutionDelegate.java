package org.springframework.web.multipart.support;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import org.apache.tomcat.util.http.fileupload.FileUploadBase;
import org.springframework.core.MethodParameter;
import org.springframework.core.ResolvableType;
import org.springframework.util.ClassUtils;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.util.WebUtils;

/* loaded from: spring-web-4.3.25.RELEASE.jar:org/springframework/web/multipart/support/MultipartResolutionDelegate.class */
public abstract class MultipartResolutionDelegate {
    public static final Object UNRESOLVABLE = new Object();
    private static Class<?> servletPartClass;

    static {
        servletPartClass = null;
        try {
            servletPartClass = ClassUtils.forName("javax.servlet.http.Part", MultipartResolutionDelegate.class.getClassLoader());
        } catch (ClassNotFoundException e) {
        }
    }

    public static boolean isMultipartRequest(HttpServletRequest request) {
        return WebUtils.getNativeRequest(request, MultipartHttpServletRequest.class) != null || isMultipartContent(request);
    }

    private static boolean isMultipartContent(HttpServletRequest request) {
        String contentType = request.getContentType();
        return contentType != null && contentType.toLowerCase().startsWith(FileUploadBase.MULTIPART);
    }

    static MultipartHttpServletRequest asMultipartHttpServletRequest(HttpServletRequest request) {
        MultipartHttpServletRequest unwrapped = (MultipartHttpServletRequest) WebUtils.getNativeRequest(request, MultipartHttpServletRequest.class);
        if (unwrapped != null) {
            return unwrapped;
        }
        return adaptToMultipartHttpServletRequest(request);
    }

    private static MultipartHttpServletRequest adaptToMultipartHttpServletRequest(HttpServletRequest request) {
        if (servletPartClass != null) {
            return new StandardMultipartHttpServletRequest(request);
        }
        throw new MultipartException("Expected MultipartHttpServletRequest: is a MultipartResolver configured?");
    }

    public static boolean isMultipartArgument(MethodParameter parameter) {
        Class<?> paramType = parameter.getNestedParameterType();
        return MultipartFile.class == paramType || isMultipartFileCollection(parameter) || isMultipartFileArray(parameter) || (servletPartClass != null && (servletPartClass == paramType || isPartCollection(parameter) || isPartArray(parameter)));
    }

    public static Object resolveMultipartArgument(String name, MethodParameter parameter, HttpServletRequest request) throws Exception {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) WebUtils.getNativeRequest(request, MultipartHttpServletRequest.class);
        boolean isMultipart = multipartRequest != null || isMultipartContent(request);
        if (MultipartFile.class == parameter.getNestedParameterType()) {
            if (multipartRequest == null && isMultipart) {
                multipartRequest = adaptToMultipartHttpServletRequest(request);
            }
            if (multipartRequest != null) {
                return multipartRequest.getFile(name);
            }
            return null;
        }
        if (isMultipartFileCollection(parameter)) {
            if (multipartRequest == null && isMultipart) {
                multipartRequest = adaptToMultipartHttpServletRequest(request);
            }
            if (multipartRequest != null) {
                return multipartRequest.getFiles(name);
            }
            return null;
        }
        if (isMultipartFileArray(parameter)) {
            if (multipartRequest == null && isMultipart) {
                multipartRequest = adaptToMultipartHttpServletRequest(request);
            }
            if (multipartRequest != null) {
                List<MultipartFile> multipartFiles = multipartRequest.getFiles(name);
                return multipartFiles.toArray(new MultipartFile[multipartFiles.size()]);
            }
            return null;
        }
        if (servletPartClass != null) {
            if (servletPartClass == parameter.getNestedParameterType()) {
                if (isMultipart) {
                    return RequestPartResolver.resolvePart(request, name);
                }
                return null;
            }
            if (isPartCollection(parameter)) {
                if (isMultipart) {
                    return RequestPartResolver.resolvePartList(request, name);
                }
                return null;
            }
            if (isPartArray(parameter)) {
                if (isMultipart) {
                    return RequestPartResolver.resolvePartArray(request, name);
                }
                return null;
            }
        }
        return UNRESOLVABLE;
    }

    private static boolean isMultipartFileCollection(MethodParameter methodParam) {
        return MultipartFile.class == getCollectionParameterType(methodParam);
    }

    private static boolean isMultipartFileArray(MethodParameter methodParam) {
        return MultipartFile.class == methodParam.getNestedParameterType().getComponentType();
    }

    private static boolean isPartCollection(MethodParameter methodParam) {
        return servletPartClass == getCollectionParameterType(methodParam);
    }

    private static boolean isPartArray(MethodParameter methodParam) {
        return servletPartClass == methodParam.getNestedParameterType().getComponentType();
    }

    private static Class<?> getCollectionParameterType(MethodParameter methodParam) {
        Class<?> valueType;
        Class<?> paramType = methodParam.getNestedParameterType();
        if ((Collection.class == paramType || List.class.isAssignableFrom(paramType)) && (valueType = ResolvableType.forMethodParameter(methodParam).asCollection().resolveGeneric(new int[0])) != null) {
            return valueType;
        }
        return null;
    }

    /* loaded from: spring-web-4.3.25.RELEASE.jar:org/springframework/web/multipart/support/MultipartResolutionDelegate$RequestPartResolver.class */
    private static class RequestPartResolver {
        private RequestPartResolver() {
        }

        public static Object resolvePart(HttpServletRequest servletRequest, String name) throws Exception {
            return servletRequest.getPart(name);
        }

        public static Object resolvePartList(HttpServletRequest servletRequest, String name) throws Exception {
            Collection<Part> parts = servletRequest.getParts();
            List<Part> result = new ArrayList<>(parts.size());
            for (Part part : parts) {
                if (part.getName().equals(name)) {
                    result.add(part);
                }
            }
            return result;
        }

        public static Object resolvePartArray(HttpServletRequest servletRequest, String name) throws Exception {
            Collection<Part> parts = servletRequest.getParts();
            List<Part> result = new ArrayList<>(parts.size());
            for (Part part : parts) {
                if (part.getName().equals(name)) {
                    result.add(part);
                }
            }
            return result.toArray(new Part[result.size()]);
        }
    }
}
