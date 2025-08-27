package org.springframework.remoting.rmi;

import java.io.IOException;
import java.io.InputStream;
import java.rmi.server.RMIClassLoader;
import org.springframework.core.ConfigurableObjectInputStream;

/* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/remoting/rmi/CodebaseAwareObjectInputStream.class */
public class CodebaseAwareObjectInputStream extends ConfigurableObjectInputStream {
    private final String codebaseUrl;

    public CodebaseAwareObjectInputStream(InputStream in, String codebaseUrl) throws IOException {
        this(in, (ClassLoader) null, codebaseUrl);
    }

    public CodebaseAwareObjectInputStream(InputStream in, ClassLoader classLoader, String codebaseUrl) throws IOException {
        super(in, classLoader);
        this.codebaseUrl = codebaseUrl;
    }

    public CodebaseAwareObjectInputStream(InputStream in, ClassLoader classLoader, boolean acceptProxyClasses) throws IOException {
        super(in, classLoader, acceptProxyClasses);
        this.codebaseUrl = null;
    }

    @Override // org.springframework.core.ConfigurableObjectInputStream
    protected Class<?> resolveFallbackIfPossible(String className, ClassNotFoundException ex) throws ClassNotFoundException, IOException {
        if (this.codebaseUrl == null) {
            throw ex;
        }
        return RMIClassLoader.loadClass(this.codebaseUrl, className);
    }

    @Override // org.springframework.core.ConfigurableObjectInputStream
    protected ClassLoader getFallbackClassLoader() throws IOException {
        return RMIClassLoader.getClassLoader(this.codebaseUrl);
    }
}
