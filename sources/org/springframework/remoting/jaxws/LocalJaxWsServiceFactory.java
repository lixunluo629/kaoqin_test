package org.springframework.remoting.jaxws;

import java.io.IOException;
import java.net.URL;
import java.util.concurrent.Executor;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebServiceFeature;
import javax.xml.ws.handler.HandlerResolver;
import org.springframework.core.io.Resource;
import org.springframework.lang.UsesJava7;
import org.springframework.util.Assert;

/* loaded from: spring-web-4.3.25.RELEASE.jar:org/springframework/remoting/jaxws/LocalJaxWsServiceFactory.class */
public class LocalJaxWsServiceFactory {
    private URL wsdlDocumentUrl;
    private String namespaceUri;
    private String serviceName;
    private WebServiceFeature[] serviceFeatures;
    private Executor executor;
    private HandlerResolver handlerResolver;

    public void setWsdlDocumentUrl(URL wsdlDocumentUrl) {
        this.wsdlDocumentUrl = wsdlDocumentUrl;
    }

    public void setWsdlDocumentResource(Resource wsdlDocumentResource) throws IOException {
        Assert.notNull(wsdlDocumentResource, "WSDL Resource must not be null.");
        this.wsdlDocumentUrl = wsdlDocumentResource.getURL();
    }

    public URL getWsdlDocumentUrl() {
        return this.wsdlDocumentUrl;
    }

    public void setNamespaceUri(String namespaceUri) {
        this.namespaceUri = namespaceUri != null ? namespaceUri.trim() : null;
    }

    public String getNamespaceUri() {
        return this.namespaceUri;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getServiceName() {
        return this.serviceName;
    }

    public void setServiceFeatures(WebServiceFeature... serviceFeatures) {
        this.serviceFeatures = serviceFeatures;
    }

    public void setExecutor(Executor executor) {
        this.executor = executor;
    }

    public void setHandlerResolver(HandlerResolver handlerResolver) {
        this.handlerResolver = handlerResolver;
    }

    @UsesJava7
    public Service createJaxWsService() {
        Service serviceCreate;
        Service service;
        Service serviceCreate2;
        Assert.notNull(this.serviceName, "No service name specified");
        if (this.serviceFeatures != null) {
            if (this.wsdlDocumentUrl != null) {
                serviceCreate2 = Service.create(this.wsdlDocumentUrl, getQName(this.serviceName), this.serviceFeatures);
            } else {
                serviceCreate2 = Service.create(getQName(this.serviceName), this.serviceFeatures);
            }
            service = serviceCreate2;
        } else {
            if (this.wsdlDocumentUrl != null) {
                serviceCreate = Service.create(this.wsdlDocumentUrl, getQName(this.serviceName));
            } else {
                serviceCreate = Service.create(getQName(this.serviceName));
            }
            service = serviceCreate;
        }
        if (this.executor != null) {
            service.setExecutor(this.executor);
        }
        if (this.handlerResolver != null) {
            service.setHandlerResolver(this.handlerResolver);
        }
        return service;
    }

    protected QName getQName(String name) {
        return getNamespaceUri() != null ? new QName(getNamespaceUri(), name) : new QName(name);
    }
}
