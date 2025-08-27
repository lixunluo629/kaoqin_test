package javax.servlet;

/* loaded from: tomcat-embed-core-8.5.43.jar:javax/servlet/ServletRequestAttributeEvent.class */
public class ServletRequestAttributeEvent extends ServletRequestEvent {
    private static final long serialVersionUID = 1;
    private final String name;
    private final Object value;

    public ServletRequestAttributeEvent(ServletContext sc, ServletRequest request, String name, Object value) {
        super(sc, request);
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return this.name;
    }

    public Object getValue() {
        return this.value;
    }
}
