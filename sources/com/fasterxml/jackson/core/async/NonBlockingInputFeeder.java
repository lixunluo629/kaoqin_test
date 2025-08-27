package com.fasterxml.jackson.core.async;

/* loaded from: jackson-core-2.11.2.jar:com/fasterxml/jackson/core/async/NonBlockingInputFeeder.class */
public interface NonBlockingInputFeeder {
    boolean needMoreInput();

    void endOfInput();
}
