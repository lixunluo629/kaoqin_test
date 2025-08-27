package org.yaml.snakeyaml.serializer;

import org.yaml.snakeyaml.nodes.Node;

/* loaded from: snakeyaml-1.17.jar:org/yaml/snakeyaml/serializer/AnchorGenerator.class */
public interface AnchorGenerator {
    String nextAnchor(Node node);
}
