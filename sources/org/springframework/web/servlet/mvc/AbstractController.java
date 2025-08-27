package org.springframework.web.servlet.mvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.WebContentGenerator;
import org.springframework.web.util.WebUtils;

/* loaded from: spring-webmvc-4.3.25.RELEASE.jar:org/springframework/web/servlet/mvc/AbstractController.class */
public abstract class AbstractController extends WebContentGenerator implements Controller {
    private boolean synchronizeOnSession;

    protected abstract ModelAndView handleRequestInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception;

    public AbstractController() {
        this(true);
    }

    public AbstractController(boolean restrictDefaultSupportedMethods) {
        super(restrictDefaultSupportedMethods);
        this.synchronizeOnSession = false;
    }

    public final void setSynchronizeOnSession(boolean synchronizeOnSession) {
        this.synchronizeOnSession = synchronizeOnSession;
    }

    public final boolean isSynchronizeOnSession() {
        return this.synchronizeOnSession;
    }

    @Override // org.springframework.web.servlet.mvc.Controller
    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session;
        ModelAndView modelAndViewHandleRequestInternal;
        if (HttpMethod.OPTIONS.matches(request.getMethod())) {
            response.setHeader("Allow", getAllowHeader());
            return null;
        }
        checkRequest(request);
        prepareResponse(response);
        if (this.synchronizeOnSession && (session = request.getSession(false)) != null) {
            Object mutex = WebUtils.getSessionMutex(session);
            synchronized (mutex) {
                modelAndViewHandleRequestInternal = handleRequestInternal(request, response);
            }
            return modelAndViewHandleRequestInternal;
        }
        return handleRequestInternal(request, response);
    }
}
