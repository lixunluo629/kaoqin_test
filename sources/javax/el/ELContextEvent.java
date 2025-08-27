package javax.el;

import java.util.EventObject;

/* loaded from: tomcat-embed-el-8.5.43.jar:javax/el/ELContextEvent.class */
public class ELContextEvent extends EventObject {
    private static final long serialVersionUID = 1255131906285426769L;

    public ELContextEvent(ELContext source) {
        super(source);
    }

    public ELContext getELContext() {
        return (ELContext) getSource();
    }
}
