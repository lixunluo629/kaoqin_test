package org.springframework.web.servlet.mvc.method.annotation;

import java.util.List;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.support.WebBindingInitializer;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.annotation.InitBinderDataBinderFactory;
import org.springframework.web.method.support.InvocableHandlerMethod;

/* loaded from: spring-webmvc-4.3.25.RELEASE.jar:org/springframework/web/servlet/mvc/method/annotation/ServletRequestDataBinderFactory.class */
public class ServletRequestDataBinderFactory extends InitBinderDataBinderFactory {
    public ServletRequestDataBinderFactory(List<InvocableHandlerMethod> binderMethods, WebBindingInitializer initializer) {
        super(binderMethods, initializer);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.springframework.web.bind.support.DefaultDataBinderFactory
    public ServletRequestDataBinder createBinderInstance(Object target, String objectName, NativeWebRequest request) {
        return new ExtendedServletRequestDataBinder(target, objectName);
    }
}
