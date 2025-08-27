package org.springframework.web.servlet.view.velocity;

import java.lang.reflect.Method;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.context.Context;
import org.apache.velocity.tools.view.context.ChainedContext;
import org.apache.velocity.tools.view.servlet.ServletToolboxManager;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;

@Deprecated
/* loaded from: spring-webmvc-4.3.25.RELEASE.jar:org/springframework/web/servlet/view/velocity/VelocityToolboxView.class */
public class VelocityToolboxView extends VelocityView {
    private String toolboxConfigLocation;

    public void setToolboxConfigLocation(String toolboxConfigLocation) {
        this.toolboxConfigLocation = toolboxConfigLocation;
    }

    protected String getToolboxConfigLocation() {
        return this.toolboxConfigLocation;
    }

    @Override // org.springframework.web.servlet.view.velocity.VelocityView
    protected Context createVelocityContext(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ChainedContext velocityContext = new ChainedContext(new VelocityContext(model), getVelocityEngine(), request, response, getServletContext());
        if (getToolboxConfigLocation() != null) {
            Map<?, ?> toolboxContext = ServletToolboxManager.getInstance(getServletContext(), getToolboxConfigLocation()).getToolbox(velocityContext);
            velocityContext.setToolbox(toolboxContext);
        }
        return velocityContext;
    }

    @Override // org.springframework.web.servlet.view.velocity.VelocityView
    protected void initTool(Object tool, Context velocityContext) throws Exception {
        Method initMethod = ClassUtils.getMethodIfAvailable(tool.getClass(), "init", Object.class);
        if (initMethod != null) {
            ReflectionUtils.invokeMethod(initMethod, tool, velocityContext);
        }
    }
}
