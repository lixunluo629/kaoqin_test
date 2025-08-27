package org.apache.ibatis.javassist.compiler.ast;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import org.apache.ibatis.javassist.compiler.CompileError;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/javassist/compiler/ast/StringL.class */
public class StringL extends ASTree {
    protected String text;

    public StringL(String t) {
        this.text = t;
    }

    public String get() {
        return this.text;
    }

    @Override // org.apache.ibatis.javassist.compiler.ast.ASTree
    public String toString() {
        return SymbolConstants.QUOTES_SYMBOL + this.text + SymbolConstants.QUOTES_SYMBOL;
    }

    @Override // org.apache.ibatis.javassist.compiler.ast.ASTree
    public void accept(Visitor v) throws CompileError {
        v.atStringL(this);
    }
}
