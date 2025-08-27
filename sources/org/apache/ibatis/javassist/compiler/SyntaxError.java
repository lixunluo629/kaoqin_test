package org.apache.ibatis.javassist.compiler;

import com.moredian.onpremise.core.common.constants.SymbolConstants;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/javassist/compiler/SyntaxError.class */
public class SyntaxError extends CompileError {
    public SyntaxError(Lex lexer) {
        super("syntax error near \"" + lexer.getTextAround() + SymbolConstants.QUOTES_SYMBOL, lexer);
    }
}
