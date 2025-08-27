package org.apache.tomcat.websocket.pojo;

import java.lang.reflect.Method;
import javax.websocket.PongMessage;
import javax.websocket.Session;

/* loaded from: tomcat-embed-websocket-8.5.43.jar:org/apache/tomcat/websocket/pojo/PojoMessageHandlerWholePong.class */
public class PojoMessageHandlerWholePong extends PojoMessageHandlerWholeBase<PongMessage> {
    public PojoMessageHandlerWholePong(Object pojo, Method method, Session session, Object[] params, int indexPayload, boolean convert, int indexSession) {
        super(pojo, method, session, params, indexPayload, convert, indexSession, -1L);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.apache.tomcat.websocket.pojo.PojoMessageHandlerWholeBase
    public Object decode(PongMessage message) {
        return null;
    }

    @Override // org.apache.tomcat.websocket.pojo.PojoMessageHandlerWholeBase
    protected void onClose() {
    }
}
