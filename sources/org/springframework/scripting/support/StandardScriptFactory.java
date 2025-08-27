package org.springframework.scripting.support;

import java.io.IOException;
import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.scripting.ScriptCompilationException;
import org.springframework.scripting.ScriptFactory;
import org.springframework.scripting.ScriptSource;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

/* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/scripting/support/StandardScriptFactory.class */
public class StandardScriptFactory implements ScriptFactory, BeanClassLoaderAware {
    private final String scriptEngineName;
    private final String scriptSourceLocator;
    private final Class<?>[] scriptInterfaces;
    private ClassLoader beanClassLoader;
    private volatile ScriptEngine scriptEngine;

    public StandardScriptFactory(String scriptSourceLocator) {
        this(null, scriptSourceLocator, (Class[]) null);
    }

    public StandardScriptFactory(String scriptSourceLocator, Class<?>... scriptInterfaces) {
        this(null, scriptSourceLocator, scriptInterfaces);
    }

    public StandardScriptFactory(String scriptEngineName, String scriptSourceLocator) {
        this(scriptEngineName, scriptSourceLocator, (Class[]) null);
    }

    public StandardScriptFactory(String scriptEngineName, String scriptSourceLocator, Class<?>... scriptInterfaces) {
        this.beanClassLoader = ClassUtils.getDefaultClassLoader();
        Assert.hasText(scriptSourceLocator, "'scriptSourceLocator' must not be empty");
        this.scriptEngineName = scriptEngineName;
        this.scriptSourceLocator = scriptSourceLocator;
        this.scriptInterfaces = scriptInterfaces;
    }

    @Override // org.springframework.beans.factory.BeanClassLoaderAware
    public void setBeanClassLoader(ClassLoader classLoader) {
        this.beanClassLoader = classLoader;
    }

    @Override // org.springframework.scripting.ScriptFactory
    public String getScriptSourceLocator() {
        return this.scriptSourceLocator;
    }

    @Override // org.springframework.scripting.ScriptFactory
    public Class<?>[] getScriptInterfaces() {
        return this.scriptInterfaces;
    }

    @Override // org.springframework.scripting.ScriptFactory
    public boolean requiresConfigInterface() {
        return false;
    }

    @Override // org.springframework.scripting.ScriptFactory
    public Object getScriptedObject(ScriptSource scriptSource, Class<?>... actualInterfaces) throws ScriptCompilationException, IOException {
        Object script = evaluateScript(scriptSource);
        if (!ObjectUtils.isEmpty((Object[]) actualInterfaces)) {
            boolean adaptationRequired = false;
            for (Class<?> requestedIfc : actualInterfaces) {
                if (!(script instanceof Class)) {
                    if (!requestedIfc.isInstance(script)) {
                        adaptationRequired = true;
                        break;
                    }
                } else {
                    if (!requestedIfc.isAssignableFrom((Class) script)) {
                        adaptationRequired = true;
                        break;
                    }
                }
            }
            if (adaptationRequired) {
                script = adaptToInterfaces(script, scriptSource, actualInterfaces);
            }
        }
        if (script instanceof Class) {
            Class<?> scriptClass = (Class) script;
            try {
                return scriptClass.newInstance();
            } catch (IllegalAccessException ex) {
                throw new ScriptCompilationException(scriptSource, "Could not access script constructor: " + scriptClass.getName(), ex);
            } catch (InstantiationException ex2) {
                throw new ScriptCompilationException(scriptSource, "Unable to instantiate script class: " + scriptClass.getName(), ex2);
            }
        }
        return script;
    }

    protected Object evaluateScript(ScriptSource scriptSource) {
        try {
            if (this.scriptEngine == null) {
                this.scriptEngine = retrieveScriptEngine(scriptSource);
                if (this.scriptEngine == null) {
                    throw new IllegalStateException("Could not determine script engine for " + scriptSource);
                }
            }
            return this.scriptEngine.eval(scriptSource.getScriptAsString());
        } catch (Exception ex) {
            throw new ScriptCompilationException(scriptSource, ex);
        }
    }

    protected ScriptEngine retrieveScriptEngine(ScriptSource scriptSource) {
        String filename;
        String extension;
        ScriptEngine engine;
        ScriptEngineManager scriptEngineManager = new ScriptEngineManager(this.beanClassLoader);
        if (this.scriptEngineName != null) {
            return StandardScriptUtils.retrieveEngineByName(scriptEngineManager, this.scriptEngineName);
        }
        if ((scriptSource instanceof ResourceScriptSource) && (filename = ((ResourceScriptSource) scriptSource).getResource().getFilename()) != null && (extension = StringUtils.getFilenameExtension(filename)) != null && (engine = scriptEngineManager.getEngineByExtension(extension)) != null) {
            return engine;
        }
        return null;
    }

    protected Object adaptToInterfaces(Object script, ScriptSource scriptSource, Class<?>... actualInterfaces) {
        Class<?> adaptedIfc;
        if (actualInterfaces.length == 1) {
            adaptedIfc = actualInterfaces[0];
        } else {
            adaptedIfc = ClassUtils.createCompositeInterface(actualInterfaces, this.beanClassLoader);
        }
        if (adaptedIfc != null) {
            if (!(this.scriptEngine instanceof Invocable)) {
                throw new ScriptCompilationException(scriptSource, "ScriptEngine must implement Invocable in order to adapt it to an interface: " + this.scriptEngine);
            }
            Invocable invocable = this.scriptEngine;
            if (script != null) {
                script = invocable.getInterface(script, adaptedIfc);
            }
            if (script == null) {
                script = invocable.getInterface(adaptedIfc);
                if (script == null) {
                    throw new ScriptCompilationException(scriptSource, "Could not adapt script to interface [" + adaptedIfc.getName() + "]");
                }
            }
        }
        return script;
    }

    @Override // org.springframework.scripting.ScriptFactory
    public Class<?> getScriptedObjectType(ScriptSource scriptSource) throws ScriptCompilationException, IOException {
        return null;
    }

    @Override // org.springframework.scripting.ScriptFactory
    public boolean requiresScriptedObjectRefresh(ScriptSource scriptSource) {
        return scriptSource.isModified();
    }

    public String toString() {
        return "StandardScriptFactory: script source locator [" + this.scriptSourceLocator + "]";
    }
}
