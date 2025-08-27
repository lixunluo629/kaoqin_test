package org.springframework.remoting.rmi;

import java.rmi.NoSuchObjectException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Properties;
import javax.naming.NamingException;
import javax.rmi.PortableRemoteObject;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.jndi.JndiTemplate;

/* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/remoting/rmi/JndiRmiServiceExporter.class */
public class JndiRmiServiceExporter extends RmiBasedExporter implements InitializingBean, DisposableBean {
    private JndiTemplate jndiTemplate = new JndiTemplate();
    private String jndiName;
    private Remote exportedObject;

    public void setJndiTemplate(JndiTemplate jndiTemplate) {
        this.jndiTemplate = jndiTemplate != null ? jndiTemplate : new JndiTemplate();
    }

    public void setJndiEnvironment(Properties jndiEnvironment) {
        this.jndiTemplate = new JndiTemplate(jndiEnvironment);
    }

    public void setJndiName(String jndiName) {
        this.jndiName = jndiName;
    }

    @Override // org.springframework.beans.factory.InitializingBean
    public void afterPropertiesSet() throws NamingException, RemoteException {
        prepare();
    }

    public void prepare() throws NamingException, RemoteException {
        if (this.jndiName == null) {
            throw new IllegalArgumentException("Property 'jndiName' is required");
        }
        this.exportedObject = getObjectToExport();
        PortableRemoteObject.exportObject(this.exportedObject);
        rebind();
    }

    public void rebind() throws NamingException {
        if (this.logger.isInfoEnabled()) {
            this.logger.info("Binding RMI service to JNDI location [" + this.jndiName + "]");
        }
        this.jndiTemplate.rebind(this.jndiName, this.exportedObject);
    }

    @Override // org.springframework.beans.factory.DisposableBean
    public void destroy() throws NoSuchObjectException, NamingException {
        if (this.logger.isInfoEnabled()) {
            this.logger.info("Unbinding RMI service from JNDI location [" + this.jndiName + "]");
        }
        this.jndiTemplate.unbind(this.jndiName);
        PortableRemoteObject.unexportObject(this.exportedObject);
    }
}
