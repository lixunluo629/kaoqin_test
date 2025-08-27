package org.apache.catalina.security;

/* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/catalina/security/SecurityClassLoad.class */
public final class SecurityClassLoad {
    public static void securityClassLoad(ClassLoader loader) throws Exception {
        securityClassLoad(loader, true);
    }

    static void securityClassLoad(ClassLoader loader, boolean requireSecurityManager) throws Exception {
        if (requireSecurityManager && System.getSecurityManager() == null) {
            return;
        }
        loadCorePackage(loader);
        loadCoyotePackage(loader);
        loadLoaderPackage(loader);
        loadRealmPackage(loader);
        loadServletsPackage(loader);
        loadSessionPackage(loader);
        loadUtilPackage(loader);
        loadValvesPackage(loader);
        loadJavaxPackage(loader);
        loadConnectorPackage(loader);
        loadTomcatPackage(loader);
    }

    private static final void loadCorePackage(ClassLoader loader) throws Exception {
        loader.loadClass("org.apache.catalina.core.AccessLogAdapter");
        loader.loadClass("org.apache.catalina.core.ApplicationContextFacade$PrivilegedExecuteMethod");
        loader.loadClass("org.apache.catalina.core.ApplicationDispatcher$PrivilegedForward");
        loader.loadClass("org.apache.catalina.core.ApplicationDispatcher$PrivilegedInclude");
        loader.loadClass("org.apache.catalina.core.ApplicationPushBuilder");
        loader.loadClass("org.apache.catalina.core.AsyncContextImpl");
        loader.loadClass("org.apache.catalina.core.AsyncContextImpl$AsyncRunnable");
        loader.loadClass("org.apache.catalina.core.AsyncContextImpl$DebugException");
        loader.loadClass("org.apache.catalina.core.AsyncListenerWrapper");
        loader.loadClass("org.apache.catalina.core.ContainerBase$PrivilegedAddChild");
        loadAnonymousInnerClasses(loader, "org.apache.catalina.core.DefaultInstanceManager");
        loader.loadClass("org.apache.catalina.core.DefaultInstanceManager$AnnotationCacheEntry");
        loader.loadClass("org.apache.catalina.core.DefaultInstanceManager$AnnotationCacheEntryType");
        loader.loadClass("org.apache.catalina.core.ApplicationHttpRequest$AttributeNamesEnumerator");
    }

    private static final void loadLoaderPackage(ClassLoader loader) throws Exception {
        loader.loadClass("org.apache.catalina.loader.WebappClassLoaderBase$PrivilegedFindClassByName");
        loader.loadClass("org.apache.catalina.loader.WebappClassLoaderBase$PrivilegedHasLoggingConfig");
    }

    private static final void loadRealmPackage(ClassLoader loader) throws Exception {
        loader.loadClass("org.apache.catalina.realm.LockOutRealm$LockRecord");
    }

    private static final void loadServletsPackage(ClassLoader loader) throws Exception {
        loader.loadClass("org.apache.catalina.servlets.DefaultServlet");
    }

    private static final void loadSessionPackage(ClassLoader loader) throws Exception {
        loader.loadClass("org.apache.catalina.session.StandardSession");
        loadAnonymousInnerClasses(loader, "org.apache.catalina.session.StandardSession");
        loader.loadClass("org.apache.catalina.session.StandardManager$PrivilegedDoUnload");
    }

    private static final void loadUtilPackage(ClassLoader loader) throws Exception {
        loader.loadClass("org.apache.catalina.util.ParameterMap");
        loader.loadClass("org.apache.catalina.util.RequestUtil");
        loader.loadClass("org.apache.catalina.util.TLSUtil");
    }

    private static final void loadValvesPackage(ClassLoader loader) throws Exception {
        loadAnonymousInnerClasses(loader, "org.apache.catalina.valves.AbstractAccessLogValve");
    }

    private static final void loadCoyotePackage(ClassLoader loader) throws Exception {
        loader.loadClass("org.apache.coyote.http11.Constants");
        Class<?> clazz = loader.loadClass("org.apache.coyote.Constants");
        clazz.getConstructor(new Class[0]).newInstance(new Object[0]);
        loader.loadClass("org.apache.coyote.http2.Stream$PrivilegedPush");
    }

    private static final void loadJavaxPackage(ClassLoader loader) throws Exception {
        loader.loadClass("javax.servlet.http.Cookie");
    }

    private static final void loadConnectorPackage(ClassLoader loader) throws Exception {
        loader.loadClass("org.apache.catalina.connector.RequestFacade$GetAttributePrivilegedAction");
        loader.loadClass("org.apache.catalina.connector.RequestFacade$GetParameterMapPrivilegedAction");
        loader.loadClass("org.apache.catalina.connector.RequestFacade$GetRequestDispatcherPrivilegedAction");
        loader.loadClass("org.apache.catalina.connector.RequestFacade$GetParameterPrivilegedAction");
        loader.loadClass("org.apache.catalina.connector.RequestFacade$GetParameterNamesPrivilegedAction");
        loader.loadClass("org.apache.catalina.connector.RequestFacade$GetParameterValuePrivilegedAction");
        loader.loadClass("org.apache.catalina.connector.RequestFacade$GetCharacterEncodingPrivilegedAction");
        loader.loadClass("org.apache.catalina.connector.RequestFacade$GetHeadersPrivilegedAction");
        loader.loadClass("org.apache.catalina.connector.RequestFacade$GetHeaderNamesPrivilegedAction");
        loader.loadClass("org.apache.catalina.connector.RequestFacade$GetCookiesPrivilegedAction");
        loader.loadClass("org.apache.catalina.connector.RequestFacade$GetLocalePrivilegedAction");
        loader.loadClass("org.apache.catalina.connector.RequestFacade$GetLocalesPrivilegedAction");
        loader.loadClass("org.apache.catalina.connector.ResponseFacade$SetContentTypePrivilegedAction");
        loader.loadClass("org.apache.catalina.connector.ResponseFacade$DateHeaderPrivilegedAction");
        loader.loadClass("org.apache.catalina.connector.RequestFacade$GetSessionPrivilegedAction");
        loadAnonymousInnerClasses(loader, "org.apache.catalina.connector.ResponseFacade");
        loadAnonymousInnerClasses(loader, "org.apache.catalina.connector.OutputBuffer");
        loadAnonymousInnerClasses(loader, "org.apache.catalina.connector.CoyoteInputStream");
        loadAnonymousInnerClasses(loader, "org.apache.catalina.connector.InputBuffer");
        loadAnonymousInnerClasses(loader, "org.apache.catalina.connector.Response");
    }

    private static final void loadTomcatPackage(ClassLoader loader) throws Exception {
        loader.loadClass("org.apache.tomcat.util.buf.B2CConverter");
        loader.loadClass("org.apache.tomcat.util.buf.ByteBufferUtils");
        loader.loadClass("org.apache.tomcat.util.buf.C2BConverter");
        loader.loadClass("org.apache.tomcat.util.buf.HexUtils");
        loader.loadClass("org.apache.tomcat.util.buf.StringCache");
        loader.loadClass("org.apache.tomcat.util.buf.StringCache$ByteEntry");
        loader.loadClass("org.apache.tomcat.util.buf.StringCache$CharEntry");
        loader.loadClass("org.apache.tomcat.util.buf.UriUtil");
        Class<?> clazz = loader.loadClass("org.apache.tomcat.util.collections.CaseInsensitiveKeyMap");
        clazz.getConstructor(new Class[0]).newInstance(new Object[0]);
        loader.loadClass("org.apache.tomcat.util.collections.CaseInsensitiveKeyMap$EntryImpl");
        loader.loadClass("org.apache.tomcat.util.collections.CaseInsensitiveKeyMap$EntryIterator");
        loader.loadClass("org.apache.tomcat.util.collections.CaseInsensitiveKeyMap$EntrySet");
        loader.loadClass("org.apache.tomcat.util.collections.CaseInsensitiveKeyMap$Key");
        loader.loadClass("org.apache.tomcat.util.http.CookieProcessor");
        loader.loadClass("org.apache.tomcat.util.http.NamesEnumerator");
        Class<?> clazz2 = loader.loadClass("org.apache.tomcat.util.http.FastHttpDateFormat");
        clazz2.getConstructor(new Class[0]).newInstance(new Object[0]);
        loader.loadClass("org.apache.tomcat.util.http.parser.HttpParser");
        loader.loadClass("org.apache.tomcat.util.http.parser.MediaType");
        loader.loadClass("org.apache.tomcat.util.http.parser.MediaTypeCache");
        loader.loadClass("org.apache.tomcat.util.http.parser.SkipResult");
        loader.loadClass("org.apache.tomcat.util.net.Constants");
        loader.loadClass("org.apache.tomcat.util.net.DispatchType");
        loader.loadClass("org.apache.tomcat.util.net.NioBlockingSelector$BlockPoller$RunnableAdd");
        loader.loadClass("org.apache.tomcat.util.net.NioBlockingSelector$BlockPoller$RunnableCancel");
        loader.loadClass("org.apache.tomcat.util.net.NioBlockingSelector$BlockPoller$RunnableRemove");
        loader.loadClass("org.apache.tomcat.util.net.Nio2Endpoint$Nio2SocketWrapper$OperationState");
        loader.loadClass("org.apache.tomcat.util.net.Nio2Endpoint$Nio2SocketWrapper$VectoredIOCompletionHandler");
        loader.loadClass("org.apache.tomcat.util.net.SocketWrapperBase$BlockingMode");
        loader.loadClass("org.apache.tomcat.util.net.SocketWrapperBase$CompletionCheck");
        loader.loadClass("org.apache.tomcat.util.net.SocketWrapperBase$CompletionHandlerCall");
        loader.loadClass("org.apache.tomcat.util.net.SocketWrapperBase$CompletionState");
        loader.loadClass("org.apache.tomcat.util.security.PrivilegedGetTccl");
        loader.loadClass("org.apache.tomcat.util.security.PrivilegedSetTccl");
    }

    private static final void loadAnonymousInnerClasses(ClassLoader loader, String enclosingClass) throws ClassNotFoundException {
        int i = 1;
        while (true) {
            try {
                loader.loadClass(enclosingClass + '$' + i);
                i++;
            } catch (ClassNotFoundException e) {
                return;
            }
        }
    }
}
