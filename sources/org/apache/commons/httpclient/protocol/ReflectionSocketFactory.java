package org.apache.commons.httpclient.protocol;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.Socket;
import org.apache.commons.httpclient.ConnectTimeoutException;

/* loaded from: commons-httpclient-3.1.jar:org/apache/commons/httpclient/protocol/ReflectionSocketFactory.class */
public final class ReflectionSocketFactory {
    private static boolean REFLECTION_FAILED = false;
    private static Constructor INETSOCKETADDRESS_CONSTRUCTOR = null;
    private static Method SOCKETCONNECT_METHOD = null;
    private static Method SOCKETBIND_METHOD = null;
    private static Class SOCKETTIMEOUTEXCEPTION_CLASS = null;
    static Class class$java$net$InetAddress;
    static Class class$java$net$Socket;

    private ReflectionSocketFactory() {
    }

    public static Socket createSocket(String socketfactoryName, String host, int port, InetAddress localAddress, int localPort, int timeout) throws IllegalAccessException, NoSuchMethodException, InstantiationException, ClassNotFoundException, SecurityException, IOException, IllegalArgumentException, InvocationTargetException {
        Class clsClass$;
        Class clsClass$2;
        Class<?> clsClass$3;
        if (REFLECTION_FAILED) {
            return null;
        }
        try {
            Class socketfactoryClass = Class.forName(socketfactoryName);
            Method method = socketfactoryClass.getMethod("getDefault", new Class[0]);
            Object socketfactory = method.invoke(null, new Object[0]);
            Method method2 = socketfactoryClass.getMethod("createSocket", new Class[0]);
            Socket socket = (Socket) method2.invoke(socketfactory, new Object[0]);
            if (INETSOCKETADDRESS_CONSTRUCTOR == null) {
                Class addressClass = Class.forName("java.net.InetSocketAddress");
                Class<?>[] clsArr = new Class[2];
                if (class$java$net$InetAddress == null) {
                    clsClass$3 = class$("java.net.InetAddress");
                    class$java$net$InetAddress = clsClass$3;
                } else {
                    clsClass$3 = class$java$net$InetAddress;
                }
                clsArr[0] = clsClass$3;
                clsArr[1] = Integer.TYPE;
                INETSOCKETADDRESS_CONSTRUCTOR = addressClass.getConstructor(clsArr);
            }
            Object remoteaddr = INETSOCKETADDRESS_CONSTRUCTOR.newInstance(InetAddress.getByName(host), new Integer(port));
            Object localaddr = INETSOCKETADDRESS_CONSTRUCTOR.newInstance(localAddress, new Integer(localPort));
            if (SOCKETCONNECT_METHOD == null) {
                if (class$java$net$Socket == null) {
                    clsClass$2 = class$("java.net.Socket");
                    class$java$net$Socket = clsClass$2;
                } else {
                    clsClass$2 = class$java$net$Socket;
                }
                SOCKETCONNECT_METHOD = clsClass$2.getMethod("connect", Class.forName("java.net.SocketAddress"), Integer.TYPE);
            }
            if (SOCKETBIND_METHOD == null) {
                if (class$java$net$Socket == null) {
                    clsClass$ = class$("java.net.Socket");
                    class$java$net$Socket = clsClass$;
                } else {
                    clsClass$ = class$java$net$Socket;
                }
                SOCKETBIND_METHOD = clsClass$.getMethod("bind", Class.forName("java.net.SocketAddress"));
            }
            SOCKETBIND_METHOD.invoke(socket, localaddr);
            SOCKETCONNECT_METHOD.invoke(socket, remoteaddr, new Integer(timeout));
            return socket;
        } catch (InvocationTargetException e) {
            Throwable cause = e.getTargetException();
            if (SOCKETTIMEOUTEXCEPTION_CLASS == null) {
                try {
                    SOCKETTIMEOUTEXCEPTION_CLASS = Class.forName("java.net.SocketTimeoutException");
                } catch (ClassNotFoundException e2) {
                    REFLECTION_FAILED = true;
                    return null;
                }
            }
            if (SOCKETTIMEOUTEXCEPTION_CLASS.isInstance(cause)) {
                throw new ConnectTimeoutException(new StringBuffer().append("The host did not accept the connection within timeout of ").append(timeout).append(" ms").toString(), cause);
            }
            if (cause instanceof IOException) {
                throw ((IOException) cause);
            }
            return null;
        } catch (Exception e3) {
            REFLECTION_FAILED = true;
            return null;
        }
    }

    static Class class$(String x0) {
        try {
            return Class.forName(x0);
        } catch (ClassNotFoundException x1) {
            throw new NoClassDefFoundError(x1.getMessage());
        }
    }
}
