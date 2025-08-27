package org.springframework.scripting.jruby;

import java.io.IOException;
import java.lang.reflect.Method;
import org.jruby.RubyException;
import org.jruby.exceptions.JumpException;
import org.jruby.exceptions.RaiseException;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.scripting.ScriptCompilationException;
import org.springframework.scripting.ScriptFactory;
import org.springframework.scripting.ScriptSource;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;

@Deprecated
/* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/scripting/jruby/JRubyScriptFactory.class */
public class JRubyScriptFactory implements ScriptFactory, BeanClassLoaderAware {
    private static final Method getMessageMethod = ClassUtils.getMethodIfAvailable(RubyException.class, "getMessage", new Class[0]);
    private final String scriptSourceLocator;
    private final Class<?>[] scriptInterfaces;
    private ClassLoader beanClassLoader = ClassUtils.getDefaultClassLoader();

    public JRubyScriptFactory(String scriptSourceLocator, Class<?>... scriptInterfaces) {
        Assert.hasText(scriptSourceLocator, "'scriptSourceLocator' must not be empty");
        Assert.notEmpty(scriptInterfaces, "'scriptInterfaces' must not be empty");
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
        return true;
    }

    @Override // org.springframework.scripting.ScriptFactory
    public Object getScriptedObject(ScriptSource scriptSource, Class<?>... actualInterfaces) throws ScriptCompilationException, IOException {
        try {
            return JRubyScriptUtils.createJRubyObject(scriptSource.getScriptAsString(), actualInterfaces, this.beanClassLoader);
        } catch (JumpException ex) {
            throw new ScriptCompilationException(scriptSource, (Throwable) ex);
        } catch (RaiseException ex2) {
            String msg = null;
            RubyException rubyEx = ex2.getException();
            if (rubyEx != null) {
                if (getMessageMethod != null) {
                    msg = ReflectionUtils.invokeMethod(getMessageMethod, rubyEx).toString();
                } else if (rubyEx.message != null) {
                    msg = rubyEx.message.toString();
                }
            }
            throw new ScriptCompilationException(scriptSource, msg != null ? msg : "Unexpected JRuby error", ex2);
        }
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
        return "JRubyScriptFactory: script source locator [" + this.scriptSourceLocator + "]";
    }
}
