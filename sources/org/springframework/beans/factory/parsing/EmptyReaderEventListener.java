package org.springframework.beans.factory.parsing;

/* loaded from: spring-beans-4.3.25.RELEASE.jar:org/springframework/beans/factory/parsing/EmptyReaderEventListener.class */
public class EmptyReaderEventListener implements ReaderEventListener {
    @Override // org.springframework.beans.factory.parsing.ReaderEventListener
    public void defaultsRegistered(DefaultsDefinition defaultsDefinition) {
    }

    @Override // org.springframework.beans.factory.parsing.ReaderEventListener
    public void componentRegistered(ComponentDefinition componentDefinition) {
    }

    @Override // org.springframework.beans.factory.parsing.ReaderEventListener
    public void aliasRegistered(AliasDefinition aliasDefinition) {
    }

    @Override // org.springframework.beans.factory.parsing.ReaderEventListener
    public void importProcessed(ImportDefinition importDefinition) {
    }
}
