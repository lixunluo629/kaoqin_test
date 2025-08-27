package org.hibernate.validator.internal.engine.messageinterpolation.parser;

import org.hibernate.validator.internal.util.logging.Log;
import org.hibernate.validator.internal.util.logging.LoggerFactory;

/* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/internal/engine/messageinterpolation/parser/InterpolationTermState.class */
public class InterpolationTermState implements ParserState {
    private static final Log log = LoggerFactory.make();

    @Override // org.hibernate.validator.internal.engine.messageinterpolation.parser.ParserState
    public void terminate(TokenCollector tokenCollector) throws MessageDescriptorFormatException {
        throw log.getNonTerminatedParameterException(tokenCollector.getOriginalMessageDescriptor(), '{');
    }

    @Override // org.hibernate.validator.internal.engine.messageinterpolation.parser.ParserState
    public void handleNonMetaCharacter(char character, TokenCollector tokenCollector) throws MessageDescriptorFormatException {
        tokenCollector.appendToToken(character);
    }

    @Override // org.hibernate.validator.internal.engine.messageinterpolation.parser.ParserState
    public void handleBeginTerm(char character, TokenCollector tokenCollector) throws MessageDescriptorFormatException {
        throw log.getNestedParameterException(tokenCollector.getOriginalMessageDescriptor());
    }

    @Override // org.hibernate.validator.internal.engine.messageinterpolation.parser.ParserState
    public void handleEndTerm(char character, TokenCollector tokenCollector) throws MessageDescriptorFormatException {
        tokenCollector.appendToToken(character);
        tokenCollector.terminateToken();
        BeginState beginState = new BeginState();
        tokenCollector.transitionState(beginState);
    }

    @Override // org.hibernate.validator.internal.engine.messageinterpolation.parser.ParserState
    public void handleEscapeCharacter(char character, TokenCollector tokenCollector) throws MessageDescriptorFormatException {
        tokenCollector.appendToToken(character);
        ParserState state = new EscapedState(this);
        tokenCollector.transitionState(state);
    }

    @Override // org.hibernate.validator.internal.engine.messageinterpolation.parser.ParserState
    public void handleELDesignator(char character, TokenCollector tokenCollector) throws MessageDescriptorFormatException {
        tokenCollector.appendToToken(character);
    }
}
