package org.springframework.web.servlet.config.annotation;

import java.util.List;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.validation.MessageCodesResolver;
import org.springframework.validation.Validator;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.HandlerExceptionResolver;

/* loaded from: spring-webmvc-4.3.25.RELEASE.jar:org/springframework/web/servlet/config/annotation/WebMvcConfigurer.class */
public interface WebMvcConfigurer {
    void configurePathMatch(PathMatchConfigurer pathMatchConfigurer);

    void configureContentNegotiation(ContentNegotiationConfigurer contentNegotiationConfigurer);

    void configureAsyncSupport(AsyncSupportConfigurer asyncSupportConfigurer);

    void configureDefaultServletHandling(DefaultServletHandlerConfigurer defaultServletHandlerConfigurer);

    void addFormatters(FormatterRegistry formatterRegistry);

    void addInterceptors(InterceptorRegistry interceptorRegistry);

    void addResourceHandlers(ResourceHandlerRegistry resourceHandlerRegistry);

    void addCorsMappings(CorsRegistry corsRegistry);

    void addViewControllers(ViewControllerRegistry viewControllerRegistry);

    void configureViewResolvers(ViewResolverRegistry viewResolverRegistry);

    void addArgumentResolvers(List<HandlerMethodArgumentResolver> list);

    void addReturnValueHandlers(List<HandlerMethodReturnValueHandler> list);

    void configureMessageConverters(List<HttpMessageConverter<?>> list);

    void extendMessageConverters(List<HttpMessageConverter<?>> list);

    void configureHandlerExceptionResolvers(List<HandlerExceptionResolver> list);

    void extendHandlerExceptionResolvers(List<HandlerExceptionResolver> list);

    Validator getValidator();

    MessageCodesResolver getMessageCodesResolver();
}
