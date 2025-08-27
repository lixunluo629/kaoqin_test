package org.yaml.snakeyaml.external.com.google.gdata.util.common.base;

/* loaded from: snakeyaml-1.17.jar:org/yaml/snakeyaml/external/com/google/gdata/util/common/base/Escaper.class */
public interface Escaper {
    String escape(String str);

    Appendable escape(Appendable appendable);
}
