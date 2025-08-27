package org.springframework.web.jsf;

import javax.faces.application.NavigationHandler;
import javax.faces.context.FacesContext;

/* loaded from: spring-web-4.3.25.RELEASE.jar:org/springframework/web/jsf/DecoratingNavigationHandler.class */
public abstract class DecoratingNavigationHandler extends NavigationHandler {
    private NavigationHandler decoratedNavigationHandler;

    public abstract void handleNavigation(FacesContext facesContext, String str, String str2, NavigationHandler navigationHandler);

    protected DecoratingNavigationHandler() {
    }

    protected DecoratingNavigationHandler(NavigationHandler originalNavigationHandler) {
        this.decoratedNavigationHandler = originalNavigationHandler;
    }

    public final NavigationHandler getDecoratedNavigationHandler() {
        return this.decoratedNavigationHandler;
    }

    public final void handleNavigation(FacesContext facesContext, String fromAction, String outcome) {
        handleNavigation(facesContext, fromAction, outcome, this.decoratedNavigationHandler);
    }

    protected final void callNextHandlerInChain(FacesContext facesContext, String fromAction, String outcome, NavigationHandler originalNavigationHandler) {
        NavigationHandler decoratedNavigationHandler = getDecoratedNavigationHandler();
        if (decoratedNavigationHandler instanceof DecoratingNavigationHandler) {
            DecoratingNavigationHandler decHandler = (DecoratingNavigationHandler) decoratedNavigationHandler;
            decHandler.handleNavigation(facesContext, fromAction, outcome, originalNavigationHandler);
        } else if (decoratedNavigationHandler != null) {
            decoratedNavigationHandler.handleNavigation(facesContext, fromAction, outcome);
        } else if (originalNavigationHandler != null) {
            originalNavigationHandler.handleNavigation(facesContext, fromAction, outcome);
        }
    }
}
