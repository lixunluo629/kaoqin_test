package net.sf.jsqlparser.parser;

import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.Statements;

/* loaded from: jsqlparser-0.9.1.jar:net/sf/jsqlparser/parser/CCJSqlParserUtil.class */
public final class CCJSqlParserUtil {
    public static Statement parse(Reader statementReader) throws JSQLParserException {
        CCJSqlParser parser = new CCJSqlParser(statementReader);
        try {
            return parser.Statement();
        } catch (Exception ex) {
            throw new JSQLParserException(ex);
        }
    }

    public static Statement parse(String sql) throws JSQLParserException {
        CCJSqlParser parser = new CCJSqlParser(new StringReader(sql));
        try {
            return parser.Statement();
        } catch (Exception ex) {
            throw new JSQLParserException(ex);
        }
    }

    public static Statement parse(InputStream is) throws JSQLParserException {
        CCJSqlParser parser = new CCJSqlParser(is);
        try {
            return parser.Statement();
        } catch (Exception ex) {
            throw new JSQLParserException(ex);
        }
    }

    public static Statement parse(InputStream is, String encoding) throws JSQLParserException {
        CCJSqlParser parser = new CCJSqlParser(is, encoding);
        try {
            return parser.Statement();
        } catch (Exception ex) {
            throw new JSQLParserException(ex);
        }
    }

    public static Expression parseExpression(String expression) throws JSQLParserException {
        CCJSqlParser parser = new CCJSqlParser(new StringReader(expression));
        try {
            return parser.SimpleExpression();
        } catch (Exception ex) {
            throw new JSQLParserException(ex);
        }
    }

    public static Expression parseCondExpression(String condExpr) throws JSQLParserException {
        CCJSqlParser parser = new CCJSqlParser(new StringReader(condExpr));
        try {
            return parser.Expression();
        } catch (Exception ex) {
            throw new JSQLParserException(ex);
        }
    }

    public static Statements parseStatements(String sqls) throws JSQLParserException {
        CCJSqlParser parser = new CCJSqlParser(new StringReader(sqls));
        try {
            return parser.Statements();
        } catch (Exception ex) {
            throw new JSQLParserException(ex);
        }
    }

    private CCJSqlParserUtil() {
    }
}
