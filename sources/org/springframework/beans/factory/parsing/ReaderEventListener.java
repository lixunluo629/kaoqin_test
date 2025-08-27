package org.springframework.beans.factory.parsing;

import java.util.EventListener;

/* loaded from: spring-beans-4.3.25.RELEASE.jar:org/springframework/beans/factory/parsing/ReaderEventListener.class */
public interface ReaderEventListener extends EventListener {
    void defaultsRegistered(DefaultsDefinition defaultsDefinition);

    void componentRegistered(ComponentDefinition componentDefinition);

    void aliasRegistered(AliasDefinition aliasDefinition);

    void importProcessed(ImportDefinition importDefinition);
}
