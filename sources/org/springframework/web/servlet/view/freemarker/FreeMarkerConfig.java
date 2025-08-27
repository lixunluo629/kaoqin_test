package org.springframework.web.servlet.view.freemarker;

import freemarker.ext.jsp.TaglibFactory;
import freemarker.template.Configuration;

/* loaded from: spring-webmvc-4.3.25.RELEASE.jar:org/springframework/web/servlet/view/freemarker/FreeMarkerConfig.class */
public interface FreeMarkerConfig {
    Configuration getConfiguration();

    TaglibFactory getTaglibFactory();
}
