package org.springframework.remoting.caucho;

import com.caucho.burlap.io.BurlapInput;
import com.caucho.burlap.io.BurlapOutput;
import com.caucho.burlap.server.BurlapSkeleton;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.remoting.support.RemoteExporter;
import org.springframework.util.Assert;

@Deprecated
/* loaded from: spring-web-4.3.25.RELEASE.jar:org/springframework/remoting/caucho/BurlapExporter.class */
public class BurlapExporter extends RemoteExporter implements InitializingBean {
    private BurlapSkeleton skeleton;

    @Override // org.springframework.beans.factory.InitializingBean
    public void afterPropertiesSet() {
        prepare();
    }

    public void prepare() {
        checkService();
        checkServiceInterface();
        this.skeleton = new BurlapSkeleton(getProxyForService(), getServiceInterface());
    }

    public void invoke(InputStream inputStream, OutputStream outputStream) throws Throwable {
        Assert.notNull(this.skeleton, "Burlap exporter has not been initialized");
        ClassLoader originalClassLoader = overrideThreadContextClassLoader();
        try {
            this.skeleton.invoke(new BurlapInput(inputStream), new BurlapOutput(outputStream));
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
            }
            try {
                outputStream.close();
            } catch (IOException e2) {
            }
            resetThreadContextClassLoader(originalClassLoader);
        }
    }
}
