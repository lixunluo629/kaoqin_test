package org.apache.ibatis.javassist.compiler.ast;

import org.apache.ibatis.javassist.CtField;
import org.apache.ibatis.javassist.compiler.CompileError;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/javassist/compiler/ast/Member.class */
public class Member extends Symbol {
    private CtField field;

    public Member(String name) {
        super(name);
        this.field = null;
    }

    public void setField(CtField f) {
        this.field = f;
    }

    public CtField getField() {
        return this.field;
    }

    @Override // org.apache.ibatis.javassist.compiler.ast.Symbol, org.apache.ibatis.javassist.compiler.ast.ASTree
    public void accept(Visitor v) throws CompileError {
        v.atMember(this);
    }
}
