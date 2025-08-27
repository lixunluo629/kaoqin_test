package org.springframework.beans.factory.xml;

import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.w3c.dom.Document;

/* loaded from: spring-beans-4.3.25.RELEASE.jar:org/springframework/beans/factory/xml/BeanDefinitionDocumentReader.class */
public interface BeanDefinitionDocumentReader {
    void registerBeanDefinitions(Document document, XmlReaderContext xmlReaderContext) throws BeanDefinitionStoreException;
}
