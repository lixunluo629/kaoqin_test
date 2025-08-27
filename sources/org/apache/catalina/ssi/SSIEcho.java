package org.apache.catalina.ssi;

import io.netty.handler.codec.rtsp.RtspHeaders;
import java.io.IOException;
import java.io.PrintWriter;

/* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/catalina/ssi/SSIEcho.class */
public class SSIEcho implements SSICommand {
    protected static final String DEFAULT_ENCODING = "entity";
    protected static final String MISSING_VARIABLE_VALUE = "(none)";

    @Override // org.apache.catalina.ssi.SSICommand
    public long process(SSIMediator ssiMediator, String commandName, String[] paramNames, String[] paramValues, PrintWriter writer) throws IOException {
        String encoding = DEFAULT_ENCODING;
        String originalValue = null;
        String errorMessage = ssiMediator.getConfigErrMsg();
        for (int i = 0; i < paramNames.length; i++) {
            String paramName = paramNames[i];
            String paramValue = paramValues[i];
            if (paramName.equalsIgnoreCase("var")) {
                originalValue = paramValue;
            } else if (paramName.equalsIgnoreCase("encoding")) {
                if (isValidEncoding(paramValue)) {
                    encoding = paramValue;
                } else {
                    ssiMediator.log("#echo--Invalid encoding: " + paramValue);
                    writer.write(ssiMediator.encode(errorMessage, DEFAULT_ENCODING));
                }
            } else {
                ssiMediator.log("#echo--Invalid attribute: " + paramName);
                writer.write(ssiMediator.encode(errorMessage, DEFAULT_ENCODING));
            }
        }
        String variableValue = ssiMediator.getVariableValue(originalValue, encoding);
        if (variableValue == null) {
            variableValue = MISSING_VARIABLE_VALUE;
        }
        writer.write(variableValue);
        return System.currentTimeMillis();
    }

    protected boolean isValidEncoding(String encoding) {
        return encoding.equalsIgnoreCase(RtspHeaders.Values.URL) || encoding.equalsIgnoreCase(DEFAULT_ENCODING) || encoding.equalsIgnoreCase("none");
    }
}
