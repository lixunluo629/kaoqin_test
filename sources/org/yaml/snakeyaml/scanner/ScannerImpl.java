package org.yaml.snakeyaml.scanner;

import ch.qos.logback.classic.net.SyslogAppender;
import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.CharacterCodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import net.coobird.thumbnailator.ThumbnailParameter;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.yaml.snakeyaml.error.Mark;
import org.yaml.snakeyaml.error.YAMLException;
import org.yaml.snakeyaml.reader.StreamReader;
import org.yaml.snakeyaml.tokens.AliasToken;
import org.yaml.snakeyaml.tokens.AnchorToken;
import org.yaml.snakeyaml.tokens.BlockEndToken;
import org.yaml.snakeyaml.tokens.BlockEntryToken;
import org.yaml.snakeyaml.tokens.BlockMappingStartToken;
import org.yaml.snakeyaml.tokens.BlockSequenceStartToken;
import org.yaml.snakeyaml.tokens.DirectiveToken;
import org.yaml.snakeyaml.tokens.DocumentEndToken;
import org.yaml.snakeyaml.tokens.DocumentStartToken;
import org.yaml.snakeyaml.tokens.FlowEntryToken;
import org.yaml.snakeyaml.tokens.FlowMappingEndToken;
import org.yaml.snakeyaml.tokens.FlowMappingStartToken;
import org.yaml.snakeyaml.tokens.FlowSequenceEndToken;
import org.yaml.snakeyaml.tokens.FlowSequenceStartToken;
import org.yaml.snakeyaml.tokens.KeyToken;
import org.yaml.snakeyaml.tokens.ScalarToken;
import org.yaml.snakeyaml.tokens.StreamEndToken;
import org.yaml.snakeyaml.tokens.StreamStartToken;
import org.yaml.snakeyaml.tokens.TagToken;
import org.yaml.snakeyaml.tokens.TagTuple;
import org.yaml.snakeyaml.tokens.Token;
import org.yaml.snakeyaml.tokens.ValueToken;
import org.yaml.snakeyaml.util.ArrayStack;
import org.yaml.snakeyaml.util.UriEncoder;

/* loaded from: snakeyaml-1.17.jar:org/yaml/snakeyaml/scanner/ScannerImpl.class */
public final class ScannerImpl implements Scanner {
    private static final Pattern NOT_HEXA = Pattern.compile("[^0-9A-Fa-f]");
    public static final Map<Character, String> ESCAPE_REPLACEMENTS = new HashMap();
    public static final Map<Character, Integer> ESCAPE_CODES = new HashMap();
    private final StreamReader reader;
    private boolean done = false;
    private int flowLevel = 0;
    private int tokensTaken = 0;
    private int indent = -1;
    private boolean allowSimpleKey = true;
    private List<Token> tokens = new ArrayList(100);
    private ArrayStack<Integer> indents = new ArrayStack<>(10);
    private Map<Integer, SimpleKey> possibleSimpleKeys = new LinkedHashMap();

    static {
        ESCAPE_REPLACEMENTS.put('0', ThumbnailParameter.DETERMINE_FORMAT);
        ESCAPE_REPLACEMENTS.put('a', "\u0007");
        ESCAPE_REPLACEMENTS.put('b', "\b");
        ESCAPE_REPLACEMENTS.put('t', SyslogAppender.DEFAULT_STACKTRACE_PATTERN);
        ESCAPE_REPLACEMENTS.put('n', ScriptUtils.FALLBACK_STATEMENT_SEPARATOR);
        ESCAPE_REPLACEMENTS.put('v', "\u000b");
        ESCAPE_REPLACEMENTS.put('f', "\f");
        ESCAPE_REPLACEMENTS.put('r', "\r");
        ESCAPE_REPLACEMENTS.put('e', "\u001b");
        ESCAPE_REPLACEMENTS.put(' ', SymbolConstants.SPACE_SYMBOL);
        ESCAPE_REPLACEMENTS.put('\"', SymbolConstants.QUOTES_SYMBOL);
        ESCAPE_REPLACEMENTS.put('\\', "\\");
        ESCAPE_REPLACEMENTS.put('N', "\u0085");
        ESCAPE_REPLACEMENTS.put('_', " ");
        ESCAPE_REPLACEMENTS.put('L', "\u2028");
        ESCAPE_REPLACEMENTS.put('P', "\u2029");
        ESCAPE_CODES.put('x', 2);
        ESCAPE_CODES.put('u', 4);
        ESCAPE_CODES.put('U', 8);
    }

    public ScannerImpl(StreamReader reader) {
        this.reader = reader;
        fetchStreamStart();
    }

    @Override // org.yaml.snakeyaml.scanner.Scanner
    public boolean checkToken(Token.ID... choices) throws IOException, NumberFormatException {
        while (needMoreTokens()) {
            fetchMoreTokens();
        }
        if (!this.tokens.isEmpty()) {
            if (choices.length == 0) {
                return true;
            }
            Token.ID first = this.tokens.get(0).getTokenId();
            for (Token.ID id : choices) {
                if (first == id) {
                    return true;
                }
            }
            return false;
        }
        return false;
    }

    @Override // org.yaml.snakeyaml.scanner.Scanner
    public Token peekToken() throws IOException, NumberFormatException {
        while (needMoreTokens()) {
            fetchMoreTokens();
        }
        return this.tokens.get(0);
    }

    @Override // org.yaml.snakeyaml.scanner.Scanner
    public Token getToken() {
        if (!this.tokens.isEmpty()) {
            this.tokensTaken++;
            return this.tokens.remove(0);
        }
        return null;
    }

    private boolean needMoreTokens() {
        if (this.done) {
            return false;
        }
        if (this.tokens.isEmpty()) {
            return true;
        }
        stalePossibleSimpleKeys();
        return nextPossibleSimpleKey() == this.tokensTaken;
    }

    private void fetchMoreTokens() throws IOException, NumberFormatException {
        scanToNextToken();
        stalePossibleSimpleKeys();
        unwindIndent(this.reader.getColumn());
        char ch2 = this.reader.peek();
        switch (ch2) {
            case 0:
                fetchStreamEnd();
                return;
            case '!':
                fetchTag();
                return;
            case '\"':
                fetchDouble();
                return;
            case '%':
                if (checkDirective()) {
                    fetchDirective();
                    return;
                }
                break;
            case '&':
                fetchAnchor();
                return;
            case '\'':
                fetchSingle();
                return;
            case '*':
                fetchAlias();
                return;
            case ',':
                fetchFlowEntry();
                return;
            case '-':
                if (checkDocumentStart()) {
                    fetchDocumentStart();
                    return;
                } else if (checkBlockEntry()) {
                    fetchBlockEntry();
                    return;
                }
                break;
            case '.':
                if (checkDocumentEnd()) {
                    fetchDocumentEnd();
                    return;
                }
                break;
            case ':':
                if (checkValue()) {
                    fetchValue();
                    return;
                }
                break;
            case '>':
                if (this.flowLevel == 0) {
                    fetchFolded();
                    return;
                }
                break;
            case '?':
                if (checkKey()) {
                    fetchKey();
                    return;
                }
                break;
            case '[':
                fetchFlowSequenceStart();
                return;
            case ']':
                fetchFlowSequenceEnd();
                return;
            case '{':
                fetchFlowMappingStart();
                return;
            case '|':
                if (this.flowLevel == 0) {
                    fetchLiteral();
                    return;
                }
                break;
            case '}':
                fetchFlowMappingEnd();
                return;
        }
        if (checkPlain()) {
            fetchPlain();
            return;
        }
        String chRepresentation = String.valueOf(ch2);
        Iterator i$ = ESCAPE_REPLACEMENTS.keySet().iterator();
        while (true) {
            if (i$.hasNext()) {
                Character s = i$.next();
                String v = ESCAPE_REPLACEMENTS.get(s);
                if (v.equals(chRepresentation)) {
                    chRepresentation = "\\" + s;
                }
            }
        }
        if (ch2 == '\t') {
            chRepresentation = chRepresentation + "(TAB)";
        }
        String text = String.format("found character '%s' that cannot start any token. (Do not use %s for indentation)", chRepresentation, chRepresentation);
        throw new ScannerException("while scanning for the next token", null, text, this.reader.getMark());
    }

    private int nextPossibleSimpleKey() {
        if (!this.possibleSimpleKeys.isEmpty()) {
            return this.possibleSimpleKeys.values().iterator().next().getTokenNumber();
        }
        return -1;
    }

    private void stalePossibleSimpleKeys() {
        if (!this.possibleSimpleKeys.isEmpty()) {
            Iterator<SimpleKey> iterator = this.possibleSimpleKeys.values().iterator();
            while (iterator.hasNext()) {
                SimpleKey key = iterator.next();
                if (key.getLine() != this.reader.getLine() || this.reader.getIndex() - key.getIndex() > 1024) {
                    if (key.isRequired()) {
                        throw new ScannerException("while scanning a simple key", key.getMark(), "could not find expected ':'", this.reader.getMark());
                    }
                    iterator.remove();
                }
            }
        }
    }

    private void savePossibleSimpleKey() {
        boolean required = this.flowLevel == 0 && this.indent == this.reader.getColumn();
        if (!this.allowSimpleKey && required) {
            throw new YAMLException("A simple key is required only if it is the first token in the current line");
        }
        if (this.allowSimpleKey) {
            removePossibleSimpleKey();
            int tokenNumber = this.tokensTaken + this.tokens.size();
            SimpleKey key = new SimpleKey(tokenNumber, required, this.reader.getIndex(), this.reader.getLine(), this.reader.getColumn(), this.reader.getMark());
            this.possibleSimpleKeys.put(Integer.valueOf(this.flowLevel), key);
        }
    }

    private void removePossibleSimpleKey() {
        SimpleKey key = this.possibleSimpleKeys.remove(Integer.valueOf(this.flowLevel));
        if (key != null && key.isRequired()) {
            throw new ScannerException("while scanning a simple key", key.getMark(), "could not find expected ':'", this.reader.getMark());
        }
    }

    private void unwindIndent(int col) {
        if (this.flowLevel != 0) {
            return;
        }
        while (this.indent > col) {
            Mark mark = this.reader.getMark();
            this.indent = this.indents.pop().intValue();
            this.tokens.add(new BlockEndToken(mark, mark));
        }
    }

    private boolean addIndent(int column) {
        if (this.indent < column) {
            this.indents.push(Integer.valueOf(this.indent));
            this.indent = column;
            return true;
        }
        return false;
    }

    private void fetchStreamStart() {
        Mark mark = this.reader.getMark();
        Token token = new StreamStartToken(mark, mark);
        this.tokens.add(token);
    }

    private void fetchStreamEnd() {
        unwindIndent(-1);
        removePossibleSimpleKey();
        this.allowSimpleKey = false;
        this.possibleSimpleKeys.clear();
        Mark mark = this.reader.getMark();
        Token token = new StreamEndToken(mark, mark);
        this.tokens.add(token);
        this.done = true;
    }

    private void fetchDirective() throws IOException {
        unwindIndent(-1);
        removePossibleSimpleKey();
        this.allowSimpleKey = false;
        Token tok = scanDirective();
        this.tokens.add(tok);
    }

    private void fetchDocumentStart() throws IOException {
        fetchDocumentIndicator(true);
    }

    private void fetchDocumentEnd() throws IOException {
        fetchDocumentIndicator(false);
    }

    private void fetchDocumentIndicator(boolean isDocumentStart) throws IOException {
        Token token;
        unwindIndent(-1);
        removePossibleSimpleKey();
        this.allowSimpleKey = false;
        Mark startMark = this.reader.getMark();
        this.reader.forward(3);
        Mark endMark = this.reader.getMark();
        if (isDocumentStart) {
            token = new DocumentStartToken(startMark, endMark);
        } else {
            token = new DocumentEndToken(startMark, endMark);
        }
        this.tokens.add(token);
    }

    private void fetchFlowSequenceStart() throws IOException {
        fetchFlowCollectionStart(false);
    }

    private void fetchFlowMappingStart() throws IOException {
        fetchFlowCollectionStart(true);
    }

    private void fetchFlowCollectionStart(boolean isMappingStart) throws IOException {
        Token token;
        savePossibleSimpleKey();
        this.flowLevel++;
        this.allowSimpleKey = true;
        Mark startMark = this.reader.getMark();
        this.reader.forward(1);
        Mark endMark = this.reader.getMark();
        if (isMappingStart) {
            token = new FlowMappingStartToken(startMark, endMark);
        } else {
            token = new FlowSequenceStartToken(startMark, endMark);
        }
        this.tokens.add(token);
    }

    private void fetchFlowSequenceEnd() throws IOException {
        fetchFlowCollectionEnd(false);
    }

    private void fetchFlowMappingEnd() throws IOException {
        fetchFlowCollectionEnd(true);
    }

    private void fetchFlowCollectionEnd(boolean isMappingEnd) throws IOException {
        Token token;
        removePossibleSimpleKey();
        this.flowLevel--;
        this.allowSimpleKey = false;
        Mark startMark = this.reader.getMark();
        this.reader.forward();
        Mark endMark = this.reader.getMark();
        if (isMappingEnd) {
            token = new FlowMappingEndToken(startMark, endMark);
        } else {
            token = new FlowSequenceEndToken(startMark, endMark);
        }
        this.tokens.add(token);
    }

    private void fetchFlowEntry() throws IOException {
        this.allowSimpleKey = true;
        removePossibleSimpleKey();
        Mark startMark = this.reader.getMark();
        this.reader.forward();
        Mark endMark = this.reader.getMark();
        Token token = new FlowEntryToken(startMark, endMark);
        this.tokens.add(token);
    }

    private void fetchBlockEntry() throws IOException {
        if (this.flowLevel == 0) {
            if (!this.allowSimpleKey) {
                throw new ScannerException(null, null, "sequence entries are not allowed here", this.reader.getMark());
            }
            if (addIndent(this.reader.getColumn())) {
                Mark mark = this.reader.getMark();
                this.tokens.add(new BlockSequenceStartToken(mark, mark));
            }
        }
        this.allowSimpleKey = true;
        removePossibleSimpleKey();
        Mark startMark = this.reader.getMark();
        this.reader.forward();
        Mark endMark = this.reader.getMark();
        Token token = new BlockEntryToken(startMark, endMark);
        this.tokens.add(token);
    }

    private void fetchKey() throws IOException {
        if (this.flowLevel == 0) {
            if (!this.allowSimpleKey) {
                throw new ScannerException(null, null, "mapping keys are not allowed here", this.reader.getMark());
            }
            if (addIndent(this.reader.getColumn())) {
                Mark mark = this.reader.getMark();
                this.tokens.add(new BlockMappingStartToken(mark, mark));
            }
        }
        this.allowSimpleKey = this.flowLevel == 0;
        removePossibleSimpleKey();
        Mark startMark = this.reader.getMark();
        this.reader.forward();
        Mark endMark = this.reader.getMark();
        Token token = new KeyToken(startMark, endMark);
        this.tokens.add(token);
    }

    private void fetchValue() throws IOException {
        SimpleKey key = this.possibleSimpleKeys.remove(Integer.valueOf(this.flowLevel));
        if (key != null) {
            this.tokens.add(key.getTokenNumber() - this.tokensTaken, new KeyToken(key.getMark(), key.getMark()));
            if (this.flowLevel == 0 && addIndent(key.getColumn())) {
                this.tokens.add(key.getTokenNumber() - this.tokensTaken, new BlockMappingStartToken(key.getMark(), key.getMark()));
            }
            this.allowSimpleKey = false;
        } else {
            if (this.flowLevel == 0 && !this.allowSimpleKey) {
                throw new ScannerException(null, null, "mapping values are not allowed here", this.reader.getMark());
            }
            if (this.flowLevel == 0 && addIndent(this.reader.getColumn())) {
                Mark mark = this.reader.getMark();
                this.tokens.add(new BlockMappingStartToken(mark, mark));
            }
            this.allowSimpleKey = this.flowLevel == 0;
            removePossibleSimpleKey();
        }
        Mark startMark = this.reader.getMark();
        this.reader.forward();
        Mark endMark = this.reader.getMark();
        Token token = new ValueToken(startMark, endMark);
        this.tokens.add(token);
    }

    private void fetchAlias() throws IOException {
        savePossibleSimpleKey();
        this.allowSimpleKey = false;
        Token tok = scanAnchor(false);
        this.tokens.add(tok);
    }

    private void fetchAnchor() throws IOException {
        savePossibleSimpleKey();
        this.allowSimpleKey = false;
        Token tok = scanAnchor(true);
        this.tokens.add(tok);
    }

    private void fetchTag() throws IOException {
        savePossibleSimpleKey();
        this.allowSimpleKey = false;
        Token tok = scanTag();
        this.tokens.add(tok);
    }

    private void fetchLiteral() throws IOException, NumberFormatException {
        fetchBlockScalar('|');
    }

    private void fetchFolded() throws IOException, NumberFormatException {
        fetchBlockScalar('>');
    }

    private void fetchBlockScalar(char style) throws IOException, NumberFormatException {
        this.allowSimpleKey = true;
        removePossibleSimpleKey();
        Token tok = scanBlockScalar(style);
        this.tokens.add(tok);
    }

    private void fetchSingle() throws IOException {
        fetchFlowScalar('\'');
    }

    private void fetchDouble() throws IOException {
        fetchFlowScalar('\"');
    }

    private void fetchFlowScalar(char style) throws IOException {
        savePossibleSimpleKey();
        this.allowSimpleKey = false;
        Token tok = scanFlowScalar(style);
        this.tokens.add(tok);
    }

    private void fetchPlain() throws IOException {
        savePossibleSimpleKey();
        this.allowSimpleKey = false;
        Token tok = scanPlain();
        this.tokens.add(tok);
    }

    private boolean checkDirective() {
        return this.reader.getColumn() == 0;
    }

    private boolean checkDocumentStart() {
        if (this.reader.getColumn() == 0 && "---".equals(this.reader.prefix(3)) && Constant.NULL_BL_T_LINEBR.has(this.reader.peek(3))) {
            return true;
        }
        return false;
    }

    private boolean checkDocumentEnd() {
        if (this.reader.getColumn() == 0 && "...".equals(this.reader.prefix(3)) && Constant.NULL_BL_T_LINEBR.has(this.reader.peek(3))) {
            return true;
        }
        return false;
    }

    private boolean checkBlockEntry() {
        return Constant.NULL_BL_T_LINEBR.has(this.reader.peek(1));
    }

    private boolean checkKey() {
        if (this.flowLevel != 0) {
            return true;
        }
        return Constant.NULL_BL_T_LINEBR.has(this.reader.peek(1));
    }

    private boolean checkValue() {
        if (this.flowLevel != 0) {
            return true;
        }
        return Constant.NULL_BL_T_LINEBR.has(this.reader.peek(1));
    }

    private boolean checkPlain() {
        char ch2 = this.reader.peek();
        return Constant.NULL_BL_T_LINEBR.hasNo(ch2, "-?:,[]{}#&*!|>'\"%@`") || (Constant.NULL_BL_T_LINEBR.hasNo(this.reader.peek(1)) && (ch2 == '-' || (this.flowLevel == 0 && "?:".indexOf(ch2) != -1)));
    }

    private void scanToNextToken() throws IOException {
        if (this.reader.getIndex() == 0 && this.reader.peek() == 65279) {
            this.reader.forward();
        }
        boolean found = false;
        while (!found) {
            int ff = 0;
            while (this.reader.peek(ff) == ' ') {
                ff++;
            }
            if (ff > 0) {
                this.reader.forward(ff);
            }
            if (this.reader.peek() == '#') {
                int ff2 = 0;
                while (Constant.NULL_OR_LINEBR.hasNo(this.reader.peek(ff2))) {
                    ff2++;
                }
                if (ff2 > 0) {
                    this.reader.forward(ff2);
                }
            }
            if (scanLineBreak().length() != 0) {
                if (this.flowLevel == 0) {
                    this.allowSimpleKey = true;
                }
            } else {
                found = true;
            }
        }
    }

    private Token scanDirective() throws IOException {
        Mark endMark;
        Mark startMark = this.reader.getMark();
        this.reader.forward();
        String name = scanDirectiveName(startMark);
        List<?> value = null;
        if ("YAML".equals(name)) {
            value = scanYamlDirectiveValue(startMark);
            endMark = this.reader.getMark();
        } else if ("TAG".equals(name)) {
            value = scanTagDirectiveValue(startMark);
            endMark = this.reader.getMark();
        } else {
            endMark = this.reader.getMark();
            int ff = 0;
            while (Constant.NULL_OR_LINEBR.hasNo(this.reader.peek(ff))) {
                ff++;
            }
            if (ff > 0) {
                this.reader.forward(ff);
            }
        }
        scanDirectiveIgnoredLine(startMark);
        return new DirectiveToken(name, value, startMark, endMark);
    }

    private String scanDirectiveName(Mark startMark) throws IOException {
        char ch2;
        int length = 0;
        char cPeek = this.reader.peek(0);
        while (true) {
            ch2 = cPeek;
            if (!Constant.ALPHA.has(ch2)) {
                break;
            }
            length++;
            cPeek = this.reader.peek(length);
        }
        if (length == 0) {
            throw new ScannerException("while scanning a directive", startMark, "expected alphabetic or numeric character, but found " + ch2 + "(" + ((int) ch2) + ")", this.reader.getMark());
        }
        String value = this.reader.prefixForward(length);
        char ch3 = this.reader.peek();
        if (Constant.NULL_BL_LINEBR.hasNo(ch3)) {
            throw new ScannerException("while scanning a directive", startMark, "expected alphabetic or numeric character, but found " + ch3 + "(" + ((int) ch3) + ")", this.reader.getMark());
        }
        return value;
    }

    private List<Integer> scanYamlDirectiveValue(Mark startMark) throws IOException {
        while (this.reader.peek() == ' ') {
            this.reader.forward();
        }
        Integer major = scanYamlDirectiveNumber(startMark);
        if (this.reader.peek() != '.') {
            throw new ScannerException("while scanning a directive", startMark, "expected a digit or '.', but found " + this.reader.peek() + "(" + ((int) this.reader.peek()) + ")", this.reader.getMark());
        }
        this.reader.forward();
        Integer minor = scanYamlDirectiveNumber(startMark);
        if (Constant.NULL_BL_LINEBR.hasNo(this.reader.peek())) {
            throw new ScannerException("while scanning a directive", startMark, "expected a digit or ' ', but found " + this.reader.peek() + "(" + ((int) this.reader.peek()) + ")", this.reader.getMark());
        }
        List<Integer> result = new ArrayList<>(2);
        result.add(major);
        result.add(minor);
        return result;
    }

    private Integer scanYamlDirectiveNumber(Mark startMark) {
        char ch2 = this.reader.peek();
        if (!Character.isDigit(ch2)) {
            throw new ScannerException("while scanning a directive", startMark, "expected a digit, but found " + ch2 + "(" + ((int) ch2) + ")", this.reader.getMark());
        }
        int length = 0;
        while (Character.isDigit(this.reader.peek(length))) {
            length++;
        }
        Integer value = Integer.valueOf(Integer.parseInt(this.reader.prefixForward(length)));
        return value;
    }

    private List<String> scanTagDirectiveValue(Mark startMark) throws IOException {
        while (this.reader.peek() == ' ') {
            this.reader.forward();
        }
        String handle = scanTagDirectiveHandle(startMark);
        while (this.reader.peek() == ' ') {
            this.reader.forward();
        }
        String prefix = scanTagDirectivePrefix(startMark);
        List<String> result = new ArrayList<>(2);
        result.add(handle);
        result.add(prefix);
        return result;
    }

    private String scanTagDirectiveHandle(Mark startMark) throws IOException {
        String value = scanTagHandle("directive", startMark);
        char ch2 = this.reader.peek();
        if (ch2 != ' ') {
            throw new ScannerException("while scanning a directive", startMark, "expected ' ', but found " + this.reader.peek() + "(" + ch2 + ")", this.reader.getMark());
        }
        return value;
    }

    private String scanTagDirectivePrefix(Mark startMark) throws IOException {
        String value = scanTagUri("directive", startMark);
        if (Constant.NULL_BL_LINEBR.hasNo(this.reader.peek())) {
            throw new ScannerException("while scanning a directive", startMark, "expected ' ', but found " + this.reader.peek() + "(" + ((int) this.reader.peek()) + ")", this.reader.getMark());
        }
        return value;
    }

    private String scanDirectiveIgnoredLine(Mark startMark) throws IOException {
        int ff = 0;
        while (this.reader.peek(ff) == ' ') {
            ff++;
        }
        if (ff > 0) {
            this.reader.forward(ff);
        }
        if (this.reader.peek() == '#') {
            int ff2 = 0;
            while (Constant.NULL_OR_LINEBR.hasNo(this.reader.peek(ff2))) {
                ff2++;
            }
            this.reader.forward(ff2);
        }
        char ch2 = this.reader.peek();
        String lineBreak = scanLineBreak();
        if (lineBreak.length() == 0 && ch2 != 0) {
            throw new ScannerException("while scanning a directive", startMark, "expected a comment or a line break, but found " + ch2 + "(" + ((int) ch2) + ")", this.reader.getMark());
        }
        return lineBreak;
    }

    private Token scanAnchor(boolean isAnchor) throws IOException {
        char ch2;
        Token tok;
        Mark startMark = this.reader.getMark();
        char indicator = this.reader.peek();
        String name = indicator == '*' ? "alias" : "anchor";
        this.reader.forward();
        int length = 0;
        char cPeek = this.reader.peek(0);
        while (true) {
            ch2 = cPeek;
            if (!Constant.ALPHA.has(ch2)) {
                break;
            }
            length++;
            cPeek = this.reader.peek(length);
        }
        if (length == 0) {
            throw new ScannerException("while scanning an " + name, startMark, "expected alphabetic or numeric character, but found " + ch2, this.reader.getMark());
        }
        String value = this.reader.prefixForward(length);
        char ch3 = this.reader.peek();
        if (Constant.NULL_BL_T_LINEBR.hasNo(ch3, "?:,]}%@`")) {
            throw new ScannerException("while scanning an " + name, startMark, "expected alphabetic or numeric character, but found " + ch3 + "(" + ((int) this.reader.peek()) + ")", this.reader.getMark());
        }
        Mark endMark = this.reader.getMark();
        if (isAnchor) {
            tok = new AnchorToken(value, startMark, endMark);
        } else {
            tok = new AliasToken(value, startMark, endMark);
        }
        return tok;
    }

    private Token scanTag() throws IOException {
        String suffix;
        Mark startMark = this.reader.getMark();
        char ch2 = this.reader.peek(1);
        String handle = null;
        if (ch2 == '<') {
            this.reader.forward(2);
            suffix = scanTagUri("tag", startMark);
            if (this.reader.peek() != '>') {
                throw new ScannerException("while scanning a tag", startMark, "expected '>', but found '" + this.reader.peek() + "' (" + ((int) this.reader.peek()) + ")", this.reader.getMark());
            }
            this.reader.forward();
        } else if (Constant.NULL_BL_T_LINEBR.has(ch2)) {
            suffix = "!";
            this.reader.forward();
        } else {
            int length = 1;
            boolean useHandle = false;
            while (true) {
                if (!Constant.NULL_BL_LINEBR.hasNo(ch2)) {
                    break;
                }
                if (ch2 == '!') {
                    useHandle = true;
                    break;
                }
                length++;
                ch2 = this.reader.peek(length);
            }
            if (useHandle) {
                handle = scanTagHandle("tag", startMark);
            } else {
                handle = "!";
                this.reader.forward();
            }
            suffix = scanTagUri("tag", startMark);
        }
        char ch3 = this.reader.peek();
        if (Constant.NULL_BL_LINEBR.hasNo(ch3)) {
            throw new ScannerException("while scanning a tag", startMark, "expected ' ', but found '" + ch3 + "' (" + ((int) ch3) + ")", this.reader.getMark());
        }
        TagTuple value = new TagTuple(handle, suffix);
        Mark endMark = this.reader.getMark();
        return new TagToken(value, startMark, endMark);
    }

    private Token scanBlockScalar(char style) throws IOException, NumberFormatException {
        boolean folded;
        int indent;
        String breaks;
        Mark endMark;
        if (style == '>') {
            folded = true;
        } else {
            folded = false;
        }
        StringBuilder chunks = new StringBuilder();
        Mark startMark = this.reader.getMark();
        this.reader.forward();
        Chomping chompi = scanBlockScalarIndicators(startMark);
        int increment = chompi.getIncrement();
        scanBlockScalarIgnoredLine(startMark);
        int minIndent = this.indent + 1;
        if (minIndent < 1) {
            minIndent = 1;
        }
        if (increment == -1) {
            Object[] brme = scanBlockScalarIndentation();
            breaks = (String) brme[0];
            int maxIndent = ((Integer) brme[1]).intValue();
            endMark = (Mark) brme[2];
            indent = Math.max(minIndent, maxIndent);
        } else {
            indent = (minIndent + increment) - 1;
            Object[] brme2 = scanBlockScalarBreaks(indent);
            breaks = (String) brme2[0];
            endMark = (Mark) brme2[1];
        }
        String lineBreak = "";
        while (this.reader.getColumn() == indent && this.reader.peek() != 0) {
            chunks.append(breaks);
            boolean leadingNonSpace = " \t".indexOf(this.reader.peek()) == -1;
            int length = 0;
            while (Constant.NULL_OR_LINEBR.hasNo(this.reader.peek(length))) {
                length++;
            }
            chunks.append(this.reader.prefixForward(length));
            lineBreak = scanLineBreak();
            Object[] brme3 = scanBlockScalarBreaks(indent);
            breaks = (String) brme3[0];
            endMark = (Mark) brme3[1];
            if (this.reader.getColumn() != indent || this.reader.peek() == 0) {
                break;
            }
            if (folded && ScriptUtils.FALLBACK_STATEMENT_SEPARATOR.equals(lineBreak) && leadingNonSpace && " \t".indexOf(this.reader.peek()) == -1) {
                if (breaks.length() == 0) {
                    chunks.append(SymbolConstants.SPACE_SYMBOL);
                }
            } else {
                chunks.append(lineBreak);
            }
        }
        if (chompi.chompTailIsNotFalse()) {
            chunks.append(lineBreak);
        }
        if (chompi.chompTailIsTrue()) {
            chunks.append(breaks);
        }
        return new ScalarToken(chunks.toString(), false, startMark, endMark, style);
    }

    private Chomping scanBlockScalarIndicators(Mark startMark) throws NumberFormatException, IOException {
        Boolean chomping = null;
        int increment = -1;
        char ch2 = this.reader.peek();
        if (ch2 == '-' || ch2 == '+') {
            if (ch2 == '+') {
                chomping = Boolean.TRUE;
            } else {
                chomping = Boolean.FALSE;
            }
            this.reader.forward();
            char ch3 = this.reader.peek();
            if (Character.isDigit(ch3)) {
                increment = Integer.parseInt(String.valueOf(ch3));
                if (increment == 0) {
                    throw new ScannerException("while scanning a block scalar", startMark, "expected indentation indicator in the range 1-9, but found 0", this.reader.getMark());
                }
                this.reader.forward();
            }
        } else if (Character.isDigit(ch2)) {
            increment = Integer.parseInt(String.valueOf(ch2));
            if (increment == 0) {
                throw new ScannerException("while scanning a block scalar", startMark, "expected indentation indicator in the range 1-9, but found 0", this.reader.getMark());
            }
            this.reader.forward();
            char ch4 = this.reader.peek();
            if (ch4 == '-' || ch4 == '+') {
                if (ch4 == '+') {
                    chomping = Boolean.TRUE;
                } else {
                    chomping = Boolean.FALSE;
                }
                this.reader.forward();
            }
        }
        char ch5 = this.reader.peek();
        if (Constant.NULL_BL_LINEBR.hasNo(ch5)) {
            throw new ScannerException("while scanning a block scalar", startMark, "expected chomping or indentation indicators, but found " + ch5, this.reader.getMark());
        }
        return new Chomping(chomping, increment);
    }

    private String scanBlockScalarIgnoredLine(Mark startMark) throws IOException {
        int ff = 0;
        while (this.reader.peek(ff) == ' ') {
            ff++;
        }
        if (ff > 0) {
            this.reader.forward(ff);
        }
        if (this.reader.peek() == '#') {
            int ff2 = 0;
            while (Constant.NULL_OR_LINEBR.hasNo(this.reader.peek(ff2))) {
                ff2++;
            }
            if (ff2 > 0) {
                this.reader.forward(ff2);
            }
        }
        char ch2 = this.reader.peek();
        String lineBreak = scanLineBreak();
        if (lineBreak.length() == 0 && ch2 != 0) {
            throw new ScannerException("while scanning a block scalar", startMark, "expected a comment or a line break, but found " + ch2, this.reader.getMark());
        }
        return lineBreak;
    }

    private Object[] scanBlockScalarIndentation() throws IOException {
        StringBuilder chunks = new StringBuilder();
        int maxIndent = 0;
        Mark endMark = this.reader.getMark();
        while (Constant.LINEBR.has(this.reader.peek(), " \r")) {
            if (this.reader.peek() != ' ') {
                chunks.append(scanLineBreak());
                endMark = this.reader.getMark();
            } else {
                this.reader.forward();
                if (this.reader.getColumn() > maxIndent) {
                    maxIndent = this.reader.getColumn();
                }
            }
        }
        return new Object[]{chunks.toString(), Integer.valueOf(maxIndent), endMark};
    }

    private Object[] scanBlockScalarBreaks(int indent) throws IOException {
        StringBuilder chunks = new StringBuilder();
        Mark endMark = this.reader.getMark();
        int ff = 0;
        for (int col = this.reader.getColumn(); col < indent && this.reader.peek(ff) == ' '; col++) {
            ff++;
        }
        if (ff > 0) {
            this.reader.forward(ff);
        }
        while (true) {
            String lineBreak = scanLineBreak();
            if (lineBreak.length() != 0) {
                chunks.append(lineBreak);
                endMark = this.reader.getMark();
                int ff2 = 0;
                for (int col2 = this.reader.getColumn(); col2 < indent && this.reader.peek(ff2) == ' '; col2++) {
                    ff2++;
                }
                if (ff2 > 0) {
                    this.reader.forward(ff2);
                }
            } else {
                return new Object[]{chunks.toString(), endMark};
            }
        }
    }

    private Token scanFlowScalar(char style) throws IOException {
        boolean _double;
        if (style == '\"') {
            _double = true;
        } else {
            _double = false;
        }
        StringBuilder chunks = new StringBuilder();
        Mark startMark = this.reader.getMark();
        char quote = this.reader.peek();
        this.reader.forward();
        chunks.append(scanFlowScalarNonSpaces(_double, startMark));
        while (this.reader.peek() != quote) {
            chunks.append(scanFlowScalarSpaces(startMark));
            chunks.append(scanFlowScalarNonSpaces(_double, startMark));
        }
        this.reader.forward();
        Mark endMark = this.reader.getMark();
        return new ScalarToken(chunks.toString(), false, startMark, endMark, style);
    }

    /* JADX WARN: Code restructure failed: missing block: B:46:0x01d4, code lost:
    
        return r0.toString();
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private java.lang.String scanFlowScalarNonSpaces(boolean r8, org.yaml.snakeyaml.error.Mark r9) throws java.io.IOException, java.lang.NumberFormatException {
        /*
            Method dump skipped, instructions count: 472
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: org.yaml.snakeyaml.scanner.ScannerImpl.scanFlowScalarNonSpaces(boolean, org.yaml.snakeyaml.error.Mark):java.lang.String");
    }

    private String scanFlowScalarSpaces(Mark startMark) throws IOException {
        StringBuilder chunks = new StringBuilder();
        int length = 0;
        while (" \t".indexOf(this.reader.peek(length)) != -1) {
            length++;
        }
        String whitespaces = this.reader.prefixForward(length);
        char ch2 = this.reader.peek();
        if (ch2 == 0) {
            throw new ScannerException("while scanning a quoted scalar", startMark, "found unexpected end of stream", this.reader.getMark());
        }
        String lineBreak = scanLineBreak();
        if (lineBreak.length() != 0) {
            String breaks = scanFlowScalarBreaks(startMark);
            if (!ScriptUtils.FALLBACK_STATEMENT_SEPARATOR.equals(lineBreak)) {
                chunks.append(lineBreak);
            } else if (breaks.length() == 0) {
                chunks.append(SymbolConstants.SPACE_SYMBOL);
            }
            chunks.append(breaks);
        } else {
            chunks.append(whitespaces);
        }
        return chunks.toString();
    }

    private String scanFlowScalarBreaks(Mark startMark) throws IOException {
        StringBuilder chunks = new StringBuilder();
        while (true) {
            String prefix = this.reader.prefix(3);
            if (("---".equals(prefix) || "...".equals(prefix)) && Constant.NULL_BL_T_LINEBR.has(this.reader.peek(3))) {
                throw new ScannerException("while scanning a quoted scalar", startMark, "found unexpected document separator", this.reader.getMark());
            }
            while (" \t".indexOf(this.reader.peek()) != -1) {
                this.reader.forward();
            }
            String lineBreak = scanLineBreak();
            if (lineBreak.length() != 0) {
                chunks.append(lineBreak);
            } else {
                return chunks.toString();
            }
        }
    }

    private Token scanPlain() throws IOException {
        char ch2;
        StringBuilder chunks = new StringBuilder();
        Mark startMark = this.reader.getMark();
        Mark endMark = startMark;
        int indent = this.indent + 1;
        String spaces = "";
        while (true) {
            int length = 0;
            if (this.reader.peek() == '#') {
                break;
            }
            while (true) {
                ch2 = this.reader.peek(length);
                if (Constant.NULL_BL_T_LINEBR.has(ch2) || ((this.flowLevel == 0 && ch2 == ':' && Constant.NULL_BL_T_LINEBR.has(this.reader.peek(length + 1))) || !(this.flowLevel == 0 || ",:?[]{}".indexOf(ch2) == -1))) {
                    break;
                }
                length++;
            }
            if (this.flowLevel != 0 && ch2 == ':' && Constant.NULL_BL_T_LINEBR.hasNo(this.reader.peek(length + 1), ",[]{}")) {
                this.reader.forward(length);
                throw new ScannerException("while scanning a plain scalar", startMark, "found unexpected ':'", this.reader.getMark(), "Please check http://pyyaml.org/wiki/YAMLColonInFlowContext for details.");
            }
            if (length == 0) {
                break;
            }
            this.allowSimpleKey = false;
            chunks.append(spaces);
            chunks.append(this.reader.prefixForward(length));
            endMark = this.reader.getMark();
            spaces = scanPlainSpaces();
            if (spaces.length() == 0 || this.reader.peek() == '#' || (this.flowLevel == 0 && this.reader.getColumn() < indent)) {
                break;
            }
        }
        return new ScalarToken(chunks.toString(), startMark, endMark, true);
    }

    private String scanPlainSpaces() throws IOException {
        int length = 0;
        while (true) {
            if (this.reader.peek(length) != ' ' && this.reader.peek(length) != '\t') {
                break;
            }
            length++;
        }
        String whitespaces = this.reader.prefixForward(length);
        String lineBreak = scanLineBreak();
        if (lineBreak.length() != 0) {
            this.allowSimpleKey = true;
            String prefix = this.reader.prefix(3);
            if ("---".equals(prefix)) {
                return "";
            }
            if ("...".equals(prefix) && Constant.NULL_BL_T_LINEBR.has(this.reader.peek(3))) {
                return "";
            }
            StringBuilder breaks = new StringBuilder();
            while (true) {
                if (this.reader.peek() == ' ') {
                    this.reader.forward();
                } else {
                    String lb = scanLineBreak();
                    if (lb.length() != 0) {
                        breaks.append(lb);
                        String prefix2 = this.reader.prefix(3);
                        if ("---".equals(prefix2)) {
                            return "";
                        }
                        if ("...".equals(prefix2) && Constant.NULL_BL_T_LINEBR.has(this.reader.peek(3))) {
                            return "";
                        }
                    } else {
                        if (!ScriptUtils.FALLBACK_STATEMENT_SEPARATOR.equals(lineBreak)) {
                            return lineBreak + ((Object) breaks);
                        }
                        if (breaks.length() == 0) {
                            return SymbolConstants.SPACE_SYMBOL;
                        }
                        return breaks.toString();
                    }
                }
            }
        } else {
            return whitespaces;
        }
    }

    private String scanTagHandle(String name, Mark startMark) throws IOException {
        char ch2 = this.reader.peek();
        if (ch2 != '!') {
            throw new ScannerException("while scanning a " + name, startMark, "expected '!', but found " + ch2 + "(" + ((int) ch2) + ")", this.reader.getMark());
        }
        int length = 1;
        char ch3 = this.reader.peek(1);
        if (ch3 != ' ') {
            while (Constant.ALPHA.has(ch3)) {
                length++;
                ch3 = this.reader.peek(length);
            }
            if (ch3 != '!') {
                this.reader.forward(length);
                throw new ScannerException("while scanning a " + name, startMark, "expected '!', but found " + ch3 + "(" + ((int) ch3) + ")", this.reader.getMark());
            }
            length++;
        }
        String value = this.reader.prefixForward(length);
        return value;
    }

    private String scanTagUri(String name, Mark startMark) throws IOException {
        char ch2;
        StringBuilder chunks = new StringBuilder();
        int length = 0;
        char cPeek = this.reader.peek(0);
        while (true) {
            ch2 = cPeek;
            if (!Constant.URI_CHARS.has(ch2)) {
                break;
            }
            if (ch2 == '%') {
                chunks.append(this.reader.prefixForward(length));
                length = 0;
                chunks.append(scanUriEscapes(name, startMark));
            } else {
                length++;
            }
            cPeek = this.reader.peek(length);
        }
        if (length != 0) {
            chunks.append(this.reader.prefixForward(length));
        }
        if (chunks.length() == 0) {
            throw new ScannerException("while scanning a " + name, startMark, "expected URI, but found " + ch2 + "(" + ((int) ch2) + ")", this.reader.getMark());
        }
        return chunks.toString();
    }

    private String scanUriEscapes(String name, Mark startMark) throws IOException {
        int length = 1;
        while (this.reader.peek(length * 3) == '%') {
            length++;
        }
        Mark beginningMark = this.reader.getMark();
        ByteBuffer buff = ByteBuffer.allocate(length);
        while (this.reader.peek() == '%') {
            this.reader.forward();
            try {
                byte code = (byte) Integer.parseInt(this.reader.prefix(2), 16);
                buff.put(code);
                this.reader.forward(2);
            } catch (NumberFormatException e) {
                throw new ScannerException("while scanning a " + name, startMark, "expected URI escape sequence of 2 hexadecimal numbers, but found " + this.reader.peek() + "(" + ((int) this.reader.peek()) + ") and " + this.reader.peek(1) + "(" + ((int) this.reader.peek(1)) + ")", this.reader.getMark());
            }
        }
        buff.flip();
        try {
            return UriEncoder.decode(buff);
        } catch (CharacterCodingException e2) {
            throw new ScannerException("while scanning a " + name, startMark, "expected URI in UTF-8: " + e2.getMessage(), beginningMark);
        }
    }

    private String scanLineBreak() throws IOException {
        char ch2 = this.reader.peek();
        if (ch2 == '\r' || ch2 == '\n' || ch2 == 133) {
            if (ch2 == '\r' && '\n' == this.reader.peek(1)) {
                this.reader.forward(2);
                return ScriptUtils.FALLBACK_STATEMENT_SEPARATOR;
            }
            this.reader.forward();
            return ScriptUtils.FALLBACK_STATEMENT_SEPARATOR;
        }
        if (ch2 == 8232 || ch2 == 8233) {
            this.reader.forward();
            return String.valueOf(ch2);
        }
        return "";
    }

    /* loaded from: snakeyaml-1.17.jar:org/yaml/snakeyaml/scanner/ScannerImpl$Chomping.class */
    private static class Chomping {
        private final Boolean value;
        private final int increment;

        public Chomping(Boolean value, int increment) {
            this.value = value;
            this.increment = increment;
        }

        public boolean chompTailIsNotFalse() {
            return this.value == null || this.value.booleanValue();
        }

        public boolean chompTailIsTrue() {
            return this.value != null && this.value.booleanValue();
        }

        public int getIncrement() {
            return this.increment;
        }
    }
}
