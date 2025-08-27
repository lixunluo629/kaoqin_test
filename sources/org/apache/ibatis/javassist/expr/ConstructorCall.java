package org.apache.ibatis.javassist.expr;

import org.apache.ibatis.javassist.CtClass;
import org.apache.ibatis.javassist.CtConstructor;
import org.apache.ibatis.javassist.CtMethod;
import org.apache.ibatis.javassist.NotFoundException;
import org.apache.ibatis.javassist.bytecode.CodeIterator;
import org.apache.ibatis.javassist.bytecode.MethodInfo;
import org.apache.ibatis.ognl.OgnlContext;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/javassist/expr/ConstructorCall.class */
public class ConstructorCall extends MethodCall {
    protected ConstructorCall(int pos, CodeIterator i, CtClass decl, MethodInfo m) {
        super(pos, i, decl, m);
    }

    @Override // org.apache.ibatis.javassist.expr.MethodCall
    public String getMethodName() {
        return isSuper() ? "super" : OgnlContext.THIS_CONTEXT_KEY;
    }

    @Override // org.apache.ibatis.javassist.expr.MethodCall
    public CtMethod getMethod() throws NotFoundException {
        throw new NotFoundException("this is a constructor call.  Call getConstructor().");
    }

    public CtConstructor getConstructor() throws NotFoundException {
        return getCtClass().getConstructor(getSignature());
    }

    @Override // org.apache.ibatis.javassist.expr.MethodCall
    public boolean isSuper() {
        return super.isSuper();
    }
}
