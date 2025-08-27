package org.springframework.web.servlet;

import java.io.IOException;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.util.NestedServletException;

/* loaded from: spring-webmvc-4.3.25.RELEASE.jar:org/springframework/web/servlet/ViewRendererServlet.class */
public class ViewRendererServlet extends HttpServlet {
    public static final String WEB_APPLICATION_CONTEXT_ATTRIBUTE = DispatcherServlet.WEB_APPLICATION_CONTEXT_ATTRIBUTE;
    public static final String VIEW_ATTRIBUTE = ViewRendererServlet.class.getName() + ".VIEW";
    public static final String MODEL_ATTRIBUTE = ViewRendererServlet.class.getName() + ".MODEL";

    @Override // javax.servlet.http.HttpServlet
    protected final void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override // javax.servlet.http.HttpServlet
    protected final void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    protected final void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            renderView(request, response);
        } catch (IOException ex) {
            throw ex;
        } catch (ServletException ex2) {
            throw ex2;
        } catch (Exception ex3) {
            throw new NestedServletException("View rendering failed", ex3);
        }
    }

    protected void renderView(HttpServletRequest request, HttpServletResponse response) throws Exception {
        View view = (View) request.getAttribute(VIEW_ATTRIBUTE);
        if (view == null) {
            throw new ServletException("Could not complete render request: View is null");
        }
        Map<String, Object> model = (Map) request.getAttribute(MODEL_ATTRIBUTE);
        view.render(model, request, response);
    }
}
