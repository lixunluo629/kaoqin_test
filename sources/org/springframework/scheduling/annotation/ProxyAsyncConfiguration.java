package org.springframework.scheduling.annotation;

import java.lang.annotation.Annotation;
import org.apache.xmlbeans.impl.jam.xml.JamXmlElements;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Role;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.scheduling.config.TaskManagementConfigUtils;
import org.springframework.util.Assert;

@Configuration
@Role(2)
/* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/scheduling/annotation/ProxyAsyncConfiguration.class */
public class ProxyAsyncConfiguration extends AbstractAsyncConfiguration {
    @Bean(name = {TaskManagementConfigUtils.ASYNC_ANNOTATION_PROCESSOR_BEAN_NAME})
    @Role(2)
    public AsyncAnnotationBeanPostProcessor asyncAdvisor() {
        Assert.notNull(this.enableAsync, "@EnableAsync annotation metadata was not injected");
        AsyncAnnotationBeanPostProcessor bpp = new AsyncAnnotationBeanPostProcessor();
        Class<? extends Annotation> customAsyncAnnotation = this.enableAsync.getClass(JamXmlElements.ANNOTATION);
        if (customAsyncAnnotation != AnnotationUtils.getDefaultValue((Class<? extends Annotation>) EnableAsync.class, JamXmlElements.ANNOTATION)) {
            bpp.setAsyncAnnotationType(customAsyncAnnotation);
        }
        if (this.executor != null) {
            bpp.setExecutor(this.executor);
        }
        if (this.exceptionHandler != null) {
            bpp.setExceptionHandler(this.exceptionHandler);
        }
        bpp.setProxyTargetClass(this.enableAsync.getBoolean("proxyTargetClass"));
        bpp.setOrder(((Integer) this.enableAsync.getNumber("order")).intValue());
        return bpp;
    }
}
