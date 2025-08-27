package org.apache.coyote;

import java.util.concurrent.Executor;
import org.apache.tomcat.util.net.SSLHostConfig;

/* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/coyote/ProtocolHandler.class */
public interface ProtocolHandler {
    void setAdapter(Adapter adapter);

    Adapter getAdapter();

    Executor getExecutor();

    void init() throws Exception;

    void start() throws Exception;

    void pause() throws Exception;

    void resume() throws Exception;

    void stop() throws Exception;

    void destroy() throws Exception;

    void closeServerSocketGraceful();

    boolean isAprRequired();

    boolean isSendfileSupported();

    void addSslHostConfig(SSLHostConfig sSLHostConfig);

    SSLHostConfig[] findSslHostConfigs();

    void addUpgradeProtocol(UpgradeProtocol upgradeProtocol);

    UpgradeProtocol[] findUpgradeProtocols();
}
