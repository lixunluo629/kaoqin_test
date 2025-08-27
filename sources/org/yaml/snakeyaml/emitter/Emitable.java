package org.yaml.snakeyaml.emitter;

import java.io.IOException;
import org.yaml.snakeyaml.events.Event;

/* loaded from: snakeyaml-1.17.jar:org/yaml/snakeyaml/emitter/Emitable.class */
public interface Emitable {
    void emit(Event event) throws IOException;
}
