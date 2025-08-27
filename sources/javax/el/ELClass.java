package javax.el;

/* loaded from: tomcat-embed-el-8.5.43.jar:javax/el/ELClass.class */
public class ELClass {
    private final Class<?> clazz;

    public ELClass(Class<?> clazz) {
        this.clazz = clazz;
    }

    public Class<?> getKlass() {
        return this.clazz;
    }
}
