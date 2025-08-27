package org.apache.catalina.ssi;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;

/* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/catalina/ssi/SSIPrintenv.class */
public class SSIPrintenv implements SSICommand {
    @Override // org.apache.catalina.ssi.SSICommand
    public long process(SSIMediator ssiMediator, String commandName, String[] paramNames, String[] paramValues, PrintWriter writer) throws IOException {
        long lastModified = 0;
        if (paramNames.length > 0) {
            String errorMessage = ssiMediator.getConfigErrMsg();
            writer.write(errorMessage);
        } else {
            Collection<String> variableNames = ssiMediator.getVariableNames();
            for (String variableName : variableNames) {
                String variableValue = ssiMediator.getVariableValue(variableName, "entity");
                if (variableValue == null) {
                    variableValue = "(none)";
                }
                writer.write(variableName);
                writer.write(61);
                writer.write(variableValue);
                writer.write(10);
                lastModified = System.currentTimeMillis();
            }
        }
        return lastModified;
    }
}
