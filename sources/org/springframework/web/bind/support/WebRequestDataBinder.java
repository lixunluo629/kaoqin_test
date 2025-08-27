package org.springframework.web.bind.support;

import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.util.ClassUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.MultipartRequest;

/* loaded from: spring-web-4.3.25.RELEASE.jar:org/springframework/web/bind/support/WebRequestDataBinder.class */
public class WebRequestDataBinder extends WebDataBinder {
    private static final boolean servlet3Parts = ClassUtils.hasMethod(HttpServletRequest.class, "getParts", new Class[0]);

    public WebRequestDataBinder(Object target) {
        super(target);
    }

    public WebRequestDataBinder(Object target, String objectName) {
        super(target, objectName);
    }

    public void bind(WebRequest request) {
        MutablePropertyValues mpvs = new MutablePropertyValues(request.getParameterMap());
        if (isMultipartRequest(request) && (request instanceof NativeWebRequest)) {
            MultipartRequest multipartRequest = (MultipartRequest) ((NativeWebRequest) request).getNativeRequest(MultipartRequest.class);
            if (multipartRequest != null) {
                bindMultipart(multipartRequest.getMultiFileMap(), mpvs);
            } else if (servlet3Parts) {
                HttpServletRequest serlvetRequest = (HttpServletRequest) ((NativeWebRequest) request).getNativeRequest(HttpServletRequest.class);
                new Servlet3MultipartHelper(isBindEmptyMultipartFiles()).bindParts(serlvetRequest, mpvs);
            }
        }
        doBind(mpvs);
    }

    private boolean isMultipartRequest(WebRequest request) {
        String contentType = request.getHeader("Content-Type");
        return contentType != null && StringUtils.startsWithIgnoreCase(contentType, "multipart");
    }

    public void closeNoCatch() throws BindException {
        if (getBindingResult().hasErrors()) {
            throw new BindException(getBindingResult());
        }
    }

    /* loaded from: spring-web-4.3.25.RELEASE.jar:org/springframework/web/bind/support/WebRequestDataBinder$Servlet3MultipartHelper.class */
    private static class Servlet3MultipartHelper {
        private final boolean bindEmptyMultipartFiles;

        public Servlet3MultipartHelper(boolean bindEmptyMultipartFiles) {
            this.bindEmptyMultipartFiles = bindEmptyMultipartFiles;
        }

        public void bindParts(HttpServletRequest request, MutablePropertyValues mpvs) {
            try {
                MultiValueMap<String, Part> map = new LinkedMultiValueMap<>();
                for (Part part : request.getParts()) {
                    map.add(part.getName(), part);
                }
                for (Map.Entry<String, Part> entry : map.entrySet()) {
                    if (((List) entry.getValue()).size() == 1) {
                        Part part2 = (Part) ((List) entry.getValue()).get(0);
                        if (this.bindEmptyMultipartFiles || part2.getSize() > 0) {
                            mpvs.add(entry.getKey(), part2);
                        }
                    } else {
                        mpvs.add(entry.getKey(), entry.getValue());
                    }
                }
            } catch (Exception ex) {
                throw new MultipartException("Failed to get request parts", ex);
            }
        }
    }
}
