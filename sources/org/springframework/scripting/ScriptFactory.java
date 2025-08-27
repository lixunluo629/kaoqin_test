package org.springframework.scripting;

import java.io.IOException;

/* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/scripting/ScriptFactory.class */
public interface ScriptFactory {
    String getScriptSourceLocator();

    Class<?>[] getScriptInterfaces();

    boolean requiresConfigInterface();

    Object getScriptedObject(ScriptSource scriptSource, Class<?>... clsArr) throws ScriptCompilationException, IOException;

    Class<?> getScriptedObjectType(ScriptSource scriptSource) throws ScriptCompilationException, IOException;

    boolean requiresScriptedObjectRefresh(ScriptSource scriptSource);
}
