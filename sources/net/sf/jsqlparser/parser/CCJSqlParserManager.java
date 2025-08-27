package net.sf.jsqlparser.parser;

import java.io.Reader;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.statement.Statement;

/* loaded from: jsqlparser-0.9.1.jar:net/sf/jsqlparser/parser/CCJSqlParserManager.class */
public class CCJSqlParserManager implements JSqlParser {
    @Override // net.sf.jsqlparser.parser.JSqlParser
    public Statement parse(Reader statementReader) throws JSQLParserException {
        CCJSqlParser parser = new CCJSqlParser(statementReader);
        try {
            return parser.Statement();
        } catch (Exception ex) {
            throw new JSQLParserException(ex);
        }
    }
}
