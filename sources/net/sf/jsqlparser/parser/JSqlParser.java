package net.sf.jsqlparser.parser;

import java.io.Reader;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.statement.Statement;

/* loaded from: jsqlparser-0.9.1.jar:net/sf/jsqlparser/parser/JSqlParser.class */
public interface JSqlParser {
    Statement parse(Reader reader) throws JSQLParserException;
}
