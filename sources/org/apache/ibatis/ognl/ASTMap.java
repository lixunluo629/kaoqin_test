package org.apache.ibatis.ognl;

import java.util.HashMap;
import java.util.Map;
import org.apache.ibatis.ognl.enhance.UnsupportedCompilationException;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/ognl/ASTMap.class */
class ASTMap extends SimpleNode {
    private static Class DEFAULT_MAP_CLASS;
    private String className;

    static {
        try {
            DEFAULT_MAP_CLASS = Class.forName("java.util.LinkedHashMap");
        } catch (ClassNotFoundException e) {
            DEFAULT_MAP_CLASS = HashMap.class;
        }
    }

    public ASTMap(int id) {
        super(id);
    }

    public ASTMap(OgnlParser p, int id) {
        super(p, id);
    }

    protected void setClassName(String value) {
        this.className = value;
    }

    @Override // org.apache.ibatis.ognl.SimpleNode
    protected Object getValueBody(OgnlContext context, Object source) throws OgnlException {
        Map answer;
        if (this.className == null) {
            try {
                answer = (Map) DEFAULT_MAP_CLASS.newInstance();
            } catch (Exception ex) {
                throw new OgnlException("Default Map class '" + DEFAULT_MAP_CLASS.getName() + "' instantiation error", ex);
            }
        } else {
            try {
                answer = (Map) OgnlRuntime.classForName(context, this.className).newInstance();
            } catch (Exception ex2) {
                throw new OgnlException("Map implementor '" + this.className + "' not found", ex2);
            }
        }
        for (int i = 0; i < jjtGetNumChildren(); i++) {
            ASTKeyValue kv = (ASTKeyValue) this._children[i];
            Node k = kv.getKey();
            Node v = kv.getValue();
            answer.put(k.getValue(context, source), v == null ? null : v.getValue(context, source));
        }
        return answer;
    }

    @Override // org.apache.ibatis.ognl.SimpleNode
    public String toString() {
        String result = "#";
        if (this.className != null) {
            result = result + "@" + this.className + "@";
        }
        String result2 = result + "{ ";
        for (int i = 0; i < jjtGetNumChildren(); i++) {
            ASTKeyValue kv = (ASTKeyValue) this._children[i];
            if (i > 0) {
                result2 = result2 + ", ";
            }
            result2 = result2 + kv.getKey() + " : " + kv.getValue();
        }
        return result2 + " }";
    }

    @Override // org.apache.ibatis.ognl.SimpleNode, org.apache.ibatis.ognl.JavaSource
    public String toGetSourceString(OgnlContext context, Object target) {
        throw new UnsupportedCompilationException("Map expressions not supported as native java yet.");
    }

    @Override // org.apache.ibatis.ognl.SimpleNode, org.apache.ibatis.ognl.JavaSource
    public String toSetSourceString(OgnlContext context, Object target) {
        throw new UnsupportedCompilationException("Map expressions not supported as native java yet.");
    }
}
