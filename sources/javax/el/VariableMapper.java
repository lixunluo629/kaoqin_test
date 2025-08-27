package javax.el;

/* loaded from: tomcat-embed-el-8.5.43.jar:javax/el/VariableMapper.class */
public abstract class VariableMapper {
    public abstract ValueExpression resolveVariable(String str);

    public abstract ValueExpression setVariable(String str, ValueExpression valueExpression);
}
