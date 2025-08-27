package org.apache.ibatis.javassist.compiler;

/* compiled from: Lex.java */
/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/javassist/compiler/Token.class */
class Token {
    public Token next = null;
    public int tokenId;
    public long longValue;
    public double doubleValue;
    public String textValue;

    Token() {
    }
}
