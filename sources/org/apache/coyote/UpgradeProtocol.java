package org.apache.coyote;

import org.apache.coyote.http11.upgrade.InternalHttpUpgradeHandler;
import org.apache.tomcat.util.net.SocketWrapperBase;

/* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/coyote/UpgradeProtocol.class */
public interface UpgradeProtocol {
    String getHttpUpgradeName(boolean z);

    byte[] getAlpnIdentifier();

    String getAlpnName();

    Processor getProcessor(SocketWrapperBase<?> socketWrapperBase, Adapter adapter);

    InternalHttpUpgradeHandler getInternalUpgradeHandler(Adapter adapter, Request request);

    boolean accept(Request request);
}
