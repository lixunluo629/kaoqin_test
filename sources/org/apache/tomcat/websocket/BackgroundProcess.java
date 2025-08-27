package org.apache.tomcat.websocket;

/* loaded from: tomcat-embed-websocket-8.5.43.jar:org/apache/tomcat/websocket/BackgroundProcess.class */
public interface BackgroundProcess {
    void backgroundProcess();

    void setProcessPeriod(int i);

    int getProcessPeriod();
}
