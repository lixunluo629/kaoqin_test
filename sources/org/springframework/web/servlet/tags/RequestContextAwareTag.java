package org.springframework.web.servlet.tags;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.TagSupport;
import javax.servlet.jsp.tagext.TryCatchFinally;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.support.JspAwareRequestContext;
import org.springframework.web.servlet.support.RequestContext;

/* loaded from: spring-webmvc-4.3.25.RELEASE.jar:org/springframework/web/servlet/tags/RequestContextAwareTag.class */
public abstract class RequestContextAwareTag extends TagSupport implements TryCatchFinally {
    public static final String REQUEST_CONTEXT_PAGE_ATTRIBUTE = "org.springframework.web.servlet.tags.REQUEST_CONTEXT";
    protected final Log logger = LogFactory.getLog(getClass());
    private RequestContext requestContext;

    protected abstract int doStartTagInternal() throws Exception;

    /* JADX INFO: Thrown type has an unknown type hierarchy: javax.servlet.jsp.JspTagException */
    public final int doStartTag() throws JspException, JspTagException {
        try {
            this.requestContext = (RequestContext) this.pageContext.getAttribute(REQUEST_CONTEXT_PAGE_ATTRIBUTE);
            if (this.requestContext == null) {
                this.requestContext = new JspAwareRequestContext(this.pageContext);
                this.pageContext.setAttribute(REQUEST_CONTEXT_PAGE_ATTRIBUTE, this.requestContext);
            }
            return doStartTagInternal();
        } catch (RuntimeException ex) {
            this.logger.error(ex.getMessage(), ex);
            throw ex;
        } catch (Exception ex2) {
            this.logger.error(ex2.getMessage(), ex2);
            throw new JspTagException(ex2.getMessage());
        } catch (JspException e) {
            this.logger.error(e.getMessage(), e);
            throw e;
        }
    }

    protected final RequestContext getRequestContext() {
        return this.requestContext;
    }

    public void doCatch(Throwable throwable) throws Throwable {
        throw throwable;
    }

    public void doFinally() {
        this.requestContext = null;
    }
}
