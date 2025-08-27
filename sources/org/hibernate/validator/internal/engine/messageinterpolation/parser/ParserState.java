package org.hibernate.validator.internal.engine.messageinterpolation.parser;

/* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/internal/engine/messageinterpolation/parser/ParserState.class */
public interface ParserState {
    void terminate(TokenCollector tokenCollector) throws MessageDescriptorFormatException;

    void handleNonMetaCharacter(char c, TokenCollector tokenCollector) throws MessageDescriptorFormatException;

    void handleBeginTerm(char c, TokenCollector tokenCollector) throws MessageDescriptorFormatException;

    void handleEndTerm(char c, TokenCollector tokenCollector) throws MessageDescriptorFormatException;

    void handleEscapeCharacter(char c, TokenCollector tokenCollector) throws MessageDescriptorFormatException;

    void handleELDesignator(char c, TokenCollector tokenCollector) throws MessageDescriptorFormatException;
}
