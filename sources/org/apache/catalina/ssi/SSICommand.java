package org.apache.catalina.ssi;

import java.io.PrintWriter;

/* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/catalina/ssi/SSICommand.class */
public interface SSICommand {
    long process(SSIMediator sSIMediator, String str, String[] strArr, String[] strArr2, PrintWriter printWriter) throws SSIStopProcessingException;
}
