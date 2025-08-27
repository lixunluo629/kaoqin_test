package org.springframework.web.servlet.view.script;

import java.nio.charset.Charset;
import javax.script.ScriptEngine;

/* loaded from: spring-webmvc-4.3.25.RELEASE.jar:org/springframework/web/servlet/view/script/ScriptTemplateConfig.class */
public interface ScriptTemplateConfig {
    ScriptEngine getEngine();

    String getEngineName();

    Boolean isSharedEngine();

    String[] getScripts();

    String getRenderObject();

    String getRenderFunction();

    String getContentType();

    Charset getCharset();

    String getResourceLoaderPath();
}
