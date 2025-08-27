package org.apache.ibatis.javassist.tools.web;

import java.io.IOException;
import java.net.Socket;

/* compiled from: Webserver.java */
/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/javassist/tools/web/ServiceThread.class */
class ServiceThread extends Thread {
    Webserver web;
    Socket sock;

    public ServiceThread(Webserver w, Socket s) {
        this.web = w;
        this.sock = s;
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        try {
            this.web.process(this.sock);
        } catch (IOException e) {
        }
    }
}
