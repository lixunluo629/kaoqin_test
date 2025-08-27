package springfox.documentation.service;

import java.util.List;

/* loaded from: springfox-core-2.2.2.jar:springfox/documentation/service/AllowableListValues.class */
public class AllowableListValues implements AllowableValues {
    private final List<String> values;
    private final String valueType;

    public AllowableListValues(List<String> values, String valueType) {
        this.values = values;
        this.valueType = valueType;
    }

    public List<String> getValues() {
        return this.values;
    }

    public String getValueType() {
        return this.valueType;
    }
}
