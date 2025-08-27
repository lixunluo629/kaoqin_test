package org.springframework.web.servlet.view.script;

import java.nio.charset.Charset;
import javax.script.ScriptEngine;

/* loaded from: spring-webmvc-4.3.25.RELEASE.jar:org/springframework/web/servlet/view/script/ScriptTemplateConfigurer.class */
public class ScriptTemplateConfigurer implements ScriptTemplateConfig {
    private ScriptEngine engine;
    private String engineName;
    private Boolean sharedEngine;
    private String[] scripts;
    private String renderObject;
    private String renderFunction;
    private String contentType;
    private Charset charset;
    private String resourceLoaderPath;

    public void setEngine(ScriptEngine engine) {
        this.engine = engine;
    }

    @Override // org.springframework.web.servlet.view.script.ScriptTemplateConfig
    public ScriptEngine getEngine() {
        return this.engine;
    }

    public void setEngineName(String engineName) {
        this.engineName = engineName;
    }

    @Override // org.springframework.web.servlet.view.script.ScriptTemplateConfig
    public String getEngineName() {
        return this.engineName;
    }

    public void setSharedEngine(Boolean sharedEngine) {
        this.sharedEngine = sharedEngine;
    }

    @Override // org.springframework.web.servlet.view.script.ScriptTemplateConfig
    public Boolean isSharedEngine() {
        return this.sharedEngine;
    }

    public void setScripts(String... scriptNames) {
        this.scripts = scriptNames;
    }

    @Override // org.springframework.web.servlet.view.script.ScriptTemplateConfig
    public String[] getScripts() {
        return this.scripts;
    }

    public void setRenderObject(String renderObject) {
        this.renderObject = renderObject;
    }

    @Override // org.springframework.web.servlet.view.script.ScriptTemplateConfig
    public String getRenderObject() {
        return this.renderObject;
    }

    public void setRenderFunction(String renderFunction) {
        this.renderFunction = renderFunction;
    }

    @Override // org.springframework.web.servlet.view.script.ScriptTemplateConfig
    public String getRenderFunction() {
        return this.renderFunction;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    @Override // org.springframework.web.servlet.view.script.ScriptTemplateConfig
    public String getContentType() {
        return this.contentType;
    }

    public void setCharset(Charset charset) {
        this.charset = charset;
    }

    @Override // org.springframework.web.servlet.view.script.ScriptTemplateConfig
    public Charset getCharset() {
        return this.charset;
    }

    public void setResourceLoaderPath(String resourceLoaderPath) {
        this.resourceLoaderPath = resourceLoaderPath;
    }

    @Override // org.springframework.web.servlet.view.script.ScriptTemplateConfig
    public String getResourceLoaderPath() {
        return this.resourceLoaderPath;
    }
}
