package javax.websocket;

/* loaded from: tomcat-embed-websocket-8.5.43.jar:javax/websocket/MessageHandler.class */
public interface MessageHandler {

    /* loaded from: tomcat-embed-websocket-8.5.43.jar:javax/websocket/MessageHandler$Partial.class */
    public interface Partial<T> extends MessageHandler {
        void onMessage(T t, boolean z);
    }

    /* loaded from: tomcat-embed-websocket-8.5.43.jar:javax/websocket/MessageHandler$Whole.class */
    public interface Whole<T> extends MessageHandler {
        void onMessage(T t);
    }
}
