package javax.el;

/* loaded from: tomcat-embed-el-8.5.43.jar:javax/el/MethodInfo.class */
public class MethodInfo {
    private final String name;
    private final Class<?>[] paramTypes;
    private final Class<?> returnType;

    public MethodInfo(String name, Class<?> returnType, Class<?>[] paramTypes) {
        this.name = name;
        this.returnType = returnType;
        this.paramTypes = paramTypes;
    }

    public String getName() {
        return this.name;
    }

    public Class<?> getReturnType() {
        return this.returnType;
    }

    public Class<?>[] getParamTypes() {
        return this.paramTypes;
    }
}
