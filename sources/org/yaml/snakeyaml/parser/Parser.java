package org.yaml.snakeyaml.parser;

import org.yaml.snakeyaml.events.Event;

/* loaded from: snakeyaml-1.17.jar:org/yaml/snakeyaml/parser/Parser.class */
public interface Parser {
    boolean checkEvent(Event.ID id);

    Event peekEvent();

    Event getEvent();
}
