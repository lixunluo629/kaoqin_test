package org.springframework.expression.spel.standard;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import org.springframework.aop.framework.autoproxy.target.QuickTargetSourceCreator;
import org.springframework.beans.PropertyAccessor;
import org.springframework.jdbc.datasource.init.ScriptUtils;

/* loaded from: spring-expression-4.3.25.RELEASE.jar:org/springframework/expression/spel/standard/TokenKind.class */
enum TokenKind {
    LITERAL_INT,
    LITERAL_LONG,
    LITERAL_HEXINT,
    LITERAL_HEXLONG,
    LITERAL_STRING,
    LITERAL_REAL,
    LITERAL_REAL_FLOAT,
    LPAREN("("),
    RPAREN(")"),
    COMMA(","),
    IDENTIFIER,
    COLON(":"),
    HASH("#"),
    RSQUARE("]"),
    LSQUARE(PropertyAccessor.PROPERTY_KEY_PREFIX),
    LCURLY("{"),
    RCURLY("}"),
    DOT("."),
    PLUS("+"),
    STAR("*"),
    MINUS("-"),
    SELECT_FIRST("^["),
    SELECT_LAST("$["),
    QMARK("?"),
    PROJECT("!["),
    DIV("/"),
    GE(">="),
    GT(">"),
    LE("<="),
    LT("<"),
    EQ("=="),
    NE("!="),
    MOD(QuickTargetSourceCreator.PREFIX_THREAD_LOCAL),
    NOT("!"),
    ASSIGN(SymbolConstants.EQUAL_SYMBOL),
    INSTANCEOF("instanceof"),
    MATCHES("matches"),
    BETWEEN("between"),
    SELECT("?["),
    POWER("^"),
    ELVIS("?:"),
    SAFE_NAVI("?."),
    BEAN_REF("@"),
    FACTORY_BEAN_REF("&"),
    SYMBOLIC_OR("||"),
    SYMBOLIC_AND("&&"),
    INC("++"),
    DEC(ScriptUtils.DEFAULT_COMMENT_PREFIX);

    final char[] tokenChars;
    private final boolean hasPayload;

    TokenKind(String tokenString) {
        this.tokenChars = tokenString.toCharArray();
        this.hasPayload = this.tokenChars.length == 0;
    }

    TokenKind() {
        this("");
    }

    @Override // java.lang.Enum
    public String toString() {
        return name() + (this.tokenChars.length != 0 ? "(" + new String(this.tokenChars) + ")" : "");
    }

    public boolean hasPayload() {
        return this.hasPayload;
    }

    public int getLength() {
        return this.tokenChars.length;
    }
}
