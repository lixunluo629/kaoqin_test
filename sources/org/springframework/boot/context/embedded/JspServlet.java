package org.springframework.boot.context.embedded;

import java.util.HashMap;
import java.util.Map;
import org.apache.catalina.core.Constants;

/* loaded from: spring-boot-1.5.22.RELEASE.jar:org/springframework/boot/context/embedded/JspServlet.class */
public class JspServlet {
    private String className = Constants.JSP_SERVLET_CLASS;
    private Map<String, String> initParameters = new HashMap();
    private boolean registered = true;

    public JspServlet() {
        this.initParameters.put("development", "false");
    }

    public String getClassName() {
        return this.className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public Map<String, String> getInitParameters() {
        return this.initParameters;
    }

    public void setInitParameters(Map<String, String> initParameters) {
        this.initParameters = initParameters;
    }

    public boolean getRegistered() {
        return this.registered;
    }

    public void setRegistered(boolean registered) {
        this.registered = registered;
    }
}
