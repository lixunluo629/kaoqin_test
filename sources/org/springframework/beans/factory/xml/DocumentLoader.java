package org.springframework.beans.factory.xml;

import org.w3c.dom.Document;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;

/* loaded from: spring-beans-4.3.25.RELEASE.jar:org/springframework/beans/factory/xml/DocumentLoader.class */
public interface DocumentLoader {
    Document loadDocument(InputSource inputSource, EntityResolver entityResolver, ErrorHandler errorHandler, int i, boolean z) throws Exception;
}
