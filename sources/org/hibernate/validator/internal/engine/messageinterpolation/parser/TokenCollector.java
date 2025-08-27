package org.hibernate.validator.internal.engine.messageinterpolation.parser;

import java.util.Collections;
import java.util.List;
import org.hibernate.validator.internal.engine.messageinterpolation.InterpolationTermType;
import org.hibernate.validator.internal.util.CollectionHelper;

/* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/internal/engine/messageinterpolation/parser/TokenCollector.class */
public class TokenCollector {
    public static final char BEGIN_TERM = '{';
    public static final char END_TERM = '}';
    public static final char EL_DESIGNATOR = '$';
    public static final char ESCAPE_CHARACTER = '\\';
    private final String originalMessageDescriptor;
    private final InterpolationTermType interpolationTermType;
    private int currentPosition;
    private Token currentToken;
    private ParserState currentParserState = new BeginState();
    private final List<Token> tokenList = CollectionHelper.newArrayList();

    public TokenCollector(String originalMessageDescriptor, InterpolationTermType interpolationTermType) throws MessageDescriptorFormatException {
        this.originalMessageDescriptor = originalMessageDescriptor;
        this.interpolationTermType = interpolationTermType;
        parse();
    }

    public void terminateToken() {
        if (this.currentToken == null) {
            return;
        }
        this.currentToken.terminate();
        this.tokenList.add(this.currentToken);
        this.currentToken = null;
    }

    public void appendToToken(char character) {
        if (this.currentToken == null) {
            this.currentToken = new Token(character);
        } else {
            this.currentToken.append(character);
        }
    }

    public void makeParameterToken() {
        this.currentToken.makeParameterToken();
    }

    public void makeELToken() {
        this.currentToken.makeELToken();
    }

    private void next() throws MessageDescriptorFormatException {
        if (this.currentPosition == this.originalMessageDescriptor.length()) {
            this.currentParserState.terminate(this);
            this.currentPosition++;
        }
        char currentCharacter = this.originalMessageDescriptor.charAt(this.currentPosition);
        this.currentPosition++;
        switch (currentCharacter) {
            case '$':
                this.currentParserState.handleELDesignator(currentCharacter, this);
                break;
            case '\\':
                this.currentParserState.handleEscapeCharacter(currentCharacter, this);
                break;
            case '{':
                this.currentParserState.handleBeginTerm(currentCharacter, this);
                break;
            case '}':
                this.currentParserState.handleEndTerm(currentCharacter, this);
                break;
            default:
                this.currentParserState.handleNonMetaCharacter(currentCharacter, this);
                break;
        }
    }

    public final void parse() throws MessageDescriptorFormatException {
        while (this.currentPosition <= this.originalMessageDescriptor.length()) {
            next();
        }
    }

    public void transitionState(ParserState newState) {
        this.currentParserState = newState;
    }

    public InterpolationTermType getInterpolationType() {
        return this.interpolationTermType;
    }

    public List<Token> getTokenList() {
        return Collections.unmodifiableList(this.tokenList);
    }

    public String getOriginalMessageDescriptor() {
        return this.originalMessageDescriptor;
    }
}
